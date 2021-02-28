package ore.area;


import cn.nukkit.Achievement;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.command.ConsoleCommandSender;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.event.block.BlockPlaceEvent;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.player.PlayerDeathEvent;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.event.player.PlayerMoveEvent;
import cn.nukkit.event.player.PlayerQuitEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.Location;
import cn.nukkit.level.Position;
import ore.area.events.*;
import ore.area.utils.ItemIDSunName;
import ore.area.utils.Tools;
import ore.area.utils.area.AreaClass;
import ore.area.utils.area.DefaultBlockClass;
import ore.area.utils.player.PlayerClass;
import ore.area.utils.task.ParticleTask;
import ore.area.utils.task.PlayerTransferTask;

import java.util.LinkedHashMap;
import java.util.LinkedList;


/**
 * @author 若水
 */
public class ListenerEvents implements Listener {

    private LinkedList<Player> join = new LinkedList<>();

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        PlayerClass playerClass = PlayerClass.getPlayerClass(player.getName());
        if(!AreaMainClass.getInstance().playerClasses.containsKey(player.getName())){
            AreaMainClass.getInstance().playerClasses.put(player.getName(),playerClass);
        }

    }

    @EventHandler
    public void onReset(AreaResetEvent event){
        AreaClass areaClass = AreaClass.getAreaClass(event.getAreaClass().getName());
        if(areaClass != null){
            if(areaClass.isKey()){
                for(Player player:Server.getInstance().getOnlinePlayers().values()){
                    String n = Tools.getPlayerTouchArea(player.getPosition());
                    if(n != null){
                        if(n.equals(event.getAreaClass().getName())){
                            player.teleport(areaClass.getTransfer());
                            player.sendMessage(AreaMainClass.getLang("reset.area.message").replace("{name}",n));
                            String bost = AreaMainClass.getLang("area.refresh.broadcast","§d {name} 矿区刷新啦..").replace("{name}",areaClass.getName());
                            if(!"".equalsIgnoreCase(bost)) {
                                Server.getInstance().broadcastMessage(bost);
                            }
                        }
                    }

                }
            }
        }
    }



    @EventHandler
    public void onBlockBreak(BlockBreakEvent event){
        if(event.isCancelled()){
            return;
        }
        Player player = event.getPlayer();
        Block block = event.getBlock();
        if(player.isOp()){
            return;
        }
        if(AreaMainClass.getInstance().canProtectionLevel()){
            if(Tools.canInAreaLevel(block)){
                String name = Tools.getPlayerTouchArea(block);
                if(name != null){
                    AreaClass areaClass = AreaClass.getAreaClass(name);
                    if(areaClass != null){
                        if (!join.contains(player)) {
                            Tools.sendMessage(player, AreaMainClass.getLang("player.join.area.error"));
                            event.setCancelled();
                            return;
                        }
                        if(!areaClass.isKey()){
                            Tools.sendMessage(player,Tools.getLanguage("area.lock.message").replace("{name}",name));
                            event.setCancelled();
                            return;
                        }
                    }
                    PlayerClass playerClass = PlayerClass.getPlayerClass(player.getName());
                    if(!playerClass.canKey(name)){
                        Tools.sendMessage(player,Tools.getLanguage("player.buy.area.last").replace("{name}",name));
                        event.setCancelled();
                    }
                }else{
                    event.setCancelled();
                }
            }
        }else{
            String name = Tools.getPlayerTouchArea(block);
            if(name != null){
                AreaClass areaClass = AreaClass.getAreaClass(name);
                if(areaClass != null){
                    if (!join.contains(player)) {
                        Tools.sendMessage(player, AreaMainClass.getLang("player.join.area.error"));
                        event.setCancelled();
                        return;
                    }
                    if(!areaClass.isKey()){
                        Tools.sendMessage(player,Tools.getLanguage("area.lock.message").replace("{name}",name));
                        event.setCancelled();
                        return;
                    }
                }
                if(!PlayerClass.getPlayerClass(player.getName()).canKey(name)){
                    Tools.sendMessage(player,Tools.getLanguage("player.buy.area.last").replace("{name}",name));
                    event.setCancelled();
                }
            }
        }

    }


    @EventHandler(priority = EventPriority.MONITOR)
    public void onSendBreak(BlockBreakEvent event){
        if(event.isCancelled()){
            return;
        }
        Player player = event.getPlayer();

        Block block = event.getBlock();
        Item[] item = event.getDrops();
        PlayerClass playerClass = PlayerClass.getPlayerClass(player.getName());
        playerClass.addCount(block.toItem());
        int count = playerClass.getBreakBlockCount(block.toItem());
        LinkedList<String> strings = DefaultBlockClass.getSuccessByCount(block.toItem(),count);
        if(strings != null){
            Achievement achievement = Achievement.achievements.get(count+DefaultBlockClass.getBlockSaveString(block.toItem()));
            if(achievement != null){
                player.awardAchievement(count+DefaultBlockClass.getBlockSaveString(block.toItem()));
                Tools.sendMessage(player,
                        Tools.getLanguage("player.break.block.success").replace("{block}"
                                , ItemIDSunName.getIDByName(block.getId(),block.getDamage()))
                                .replace("{count}",count+""));
                Server.getInstance().getPluginManager().callEvent(new PlayerBreakBlockAchievementEvent(player,count+
                        DefaultBlockClass.getBlockSaveString(block.toItem()),block));
            }

            if(strings.size() > 0){
                for(String cmd:strings){
                    Server.getInstance().dispatchCommand(new ConsoleCommandSender(),cmd.replace("@p",player.getName()));
                }
            }
        }
        if(AreaMainClass.getInstance().isCanSendInventory()){
            if(item.length > 0){
                if(block.isBreakable(event.getItem())){
                    event.setDrops(new Item[0]);
                    for(Item item1:item){
                        if(player.getInventory().canAddItem(item1)){
                            player.getInventory().addItem(item1);
                        }else{
                            player.getLevel().dropItem(player,item1);
                        }
                    }
                }
            }
        }

    }


    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event){
        Player player = event.getPlayer();
        Block block = event.getBlock();
        if(player.isOp()){
            return;
        }

        if(AreaMainClass.getInstance().canProtectionLevel()){
            if(Tools.canInAreaLevel(block)){

                String name = Tools.getPlayerTouchArea(block);
                if(name != null){
                    AreaClass areaClass = AreaClass.getAreaClass(name);
                    if(areaClass != null){
                        if (!join.contains(player)) {
                            Tools.sendMessage(player, AreaMainClass.getLang("player.join.area.error"));
                            event.setCancelled();
                            return;
                        }
                        if(!areaClass.isKey()){
                            Tools.sendMessage(player,Tools.getLanguage("area.lock.message").replace("{name}",name));
                            event.setCancelled();
                            return;
                        }
                    }
                    if(!PlayerClass.getPlayerClass(player.getName()).canKey(name)){
                        Tools.sendMessage(player,Tools.getLanguage("player.buy.area.last").replace("{name}",name));
                        event.setCancelled();
                    }
                }else{
                    event.setCancelled();
                }
            }
        }else{
            String name = Tools.getPlayerTouchArea(block);
            if(name != null){
                AreaClass areaClass = AreaClass.getAreaClass(name);
                if(areaClass != null){
                    if (!join.contains(player)) {
                        Tools.sendMessage(player, AreaMainClass.getLang("player.join.area.error"));
                        event.setCancelled();
                        return;
                    }
                    if(!areaClass.isKey()){
                        Tools.sendMessage(player,Tools.getLanguage("area.lock.message").replace("{name}",name));
                        event.setCancelled();
                        return;
                    }
                }
                if(!PlayerClass.getPlayerClass(player.getName()).canKey(name)){
                    Tools.sendMessage(player,Tools.getLanguage("player.buy.area.last").replace("{name}",name));
                    event.setCancelled();
                }
            }
        }
    }
    @EventHandler
    public void onTransfer(PlayerTransferAreaEvent event) {
        Player player = event.getPlayer();

        AreaClass aClass = event.getAreaClass();
        if (aClass != null) {

            boolean can = AreaMainClass.getInstance().canShowTransfer();
            if(AreaMainClass.getInstance().transfer.contains(player.getName())){
                Tools.sendMessage(player,Tools.getLanguage("player.transfer.exists"));
                return;
            }
            PlayerClass playerClass = PlayerClass.getPlayerClass(player.getName());
            if(playerClass.getJoin(event.getAreaClass().getName()) == 0 && aClass.getJoinCount() != -1){
                boolean canUse = false;
                if(AreaMainClass.getInstance().useTime.containsKey(player.getName())){
                    LinkedHashMap<String,Integer> map = AreaMainClass.getInstance().useTime.get(player.getName());
                    if(map.containsKey(aClass.getName())){
                        canUse = true;
                    }
                }
                if(!canUse) {
                    Tools.sendMessage(player, Tools.getLanguage("player.join.area.count.max").replace("{name}", event.getAreaClass().getName()));
                    return;
                }
            }
            if(aClass.getTransferMoney() > 0){
                if(Tools.getMoney().myMoney(player) < aClass.getTransferMoney()){
                    Tools.sendMessage(player,Tools.getLanguage("player.buy.area.error")
                            .replace("{name}",aClass.getName())
                            .replace("{money}",aClass.getTransferMoney()+"")
                    .replace("{mymoney}",String.format("%.2f",AreaMainClass.getInstance().getMoney().myMoney(player)))
                    .replace("{math}",String.format("%.2f",aClass.getTransferMoney() - AreaMainClass.getInstance().getMoney().myMoney(player))));
                    return;
                }else{
                    Tools.getMoney().reduceMoney(player,aClass.getTransferMoney());
                    Tools.sendMessage(player,Tools.getLanguage("player.transfer.money.success")
                            .replace("{name}",aClass.getName()).replace("{money}",aClass.getTransferMoney()+""));
                }
            }

            AreaMainClass.getInstance().transfer.add(player.getName());
            Position pos = player.getPosition();
            if(can){
                Server.getInstance().getScheduler().scheduleRepeatingTask(new ParticleTask(AreaMainClass.getInstance(),player.getName(),pos),2);
            }
            Server.getInstance().getScheduler().scheduleRepeatingTask(new PlayerTransferTask(AreaMainClass.getInstance(),player,AreaMainClass.getInstance().getTransferTime(),aClass),20);
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event){
        Player player = event.getPlayer();
        Location from = event.getFrom();
        Location to = event.getTo();
        if(from.getX() != to.getX() || from.getZ() != to.getZ()){
            if(AreaMainClass.getInstance().transfer.contains(player.getName())){
                AreaMainClass.getInstance().transfer.remove(player.getName());
                Tools.sendMessage(player,AreaMainClass.getLang("transfer.area.cancel"),
                        "",AreaMainClass.getInstance().getMessageType());

            }

        }
        String name = Tools.getPlayerInArea(player);
        if(name != null){
            AreaClass area = AreaClass.getAreaClass(name);
            if(area != null){
                if(!AreaMainClass.getInstance().canJoin.containsKey(player.getName())){
                    AreaMainClass.getInstance().canJoin.put(player.getName(), name);
                    PlayerJoinAreaEvent joinEvent = new PlayerJoinAreaEvent(player,area);
                    Server.getInstance().getPluginManager().callEvent(joinEvent);
                    if(joinEvent.isCancelled()){
                        AreaMainClass.getInstance().canJoin.remove(player.getName());

                    }
                }else{
                    String exit = AreaMainClass.getInstance().canJoin.get(player.getName());
                    if(!exit.equals(name)){
                        PlayerJoinAreaEvent joinEvent = new PlayerJoinAreaEvent(player,area);
                        Server.getInstance().getPluginManager().callEvent(joinEvent);
                        if(joinEvent.isCancelled()){
                            AreaMainClass.getInstance().canJoin.remove(player.getName());
                        }
                    }
                }
            }

        }else{
            if(AreaMainClass.getInstance().canJoin.containsKey(player.getName())){
                String e = AreaMainClass.getInstance().canJoin.get(player.getName());
                PlayerQuitAreaEvent joinEvent = new PlayerQuitAreaEvent(player, AreaClass.getAreaClass(e));
                Server.getInstance().getPluginManager().callEvent(joinEvent);
                AreaMainClass.getInstance().canJoin.remove(player.getName());
            }
        }
    }

    @EventHandler
    public void onTimeOut(PlayerUseTimeEndEvent event){
        Player player = event.getPlayer();
        player.teleport(event.getAreaClass().getTransfer());
        Tools.konkBack(player,event.getAreaClass().getTransfer());
        join.remove(player);
        Tools.sendMessage(player,AreaMainClass.getLang("player.area.time.out"));
    }

    @EventHandler
    public void onReset(PlayerUseCountResetEvent event){
        Player player = event.getPlayer();
        Tools.sendMessage(player,AreaMainClass.getLang("player.join.count.reset","§2已刷新\n§f你进入 {name} 矿区的次数已刷新")
                .replace("{name}",event.getAreaClass().getName()));
    }

    @EventHandler
    public void onPlayerJoinArea(PlayerJoinAreaEvent event){
        Player player = event.getPlayer();
        if(event.getAreaClass().isKey()){
            PlayerClass playerClass = PlayerClass.getPlayerClass(player.getName());
            if(playerClass.canKey(event.getAreaClass().getName())) {
                if(!AreaMainClass.getInstance().useTime.containsKey(player.getName())){
                    AreaMainClass.getInstance().useTime.put(player.getName(),new LinkedHashMap<>());
                }
                LinkedHashMap<String,Integer> map = AreaMainClass.getInstance().useTime.get(player.getName());
                if(!map.containsKey(event.getAreaClass().getName())) {
                    if (playerClass.getJoin(event.getAreaClass().getName()) == 0 && event.getAreaClass().getJoinCount() != -1) {
                        Tools.sendMessage(player, Tools.getLanguage("player.join.area.count.max").replace("{name}", event.getAreaClass().getName()));
                        Tools.konkBack(player, event.getAreaClass().getPos().getCenterPosition());
                        event.setCancelled();
                        return;
                    }
                    playerClass.setJoinCount(event.getAreaClass().getName(), playerClass.getJoin(event.getAreaClass().getName()) - 1);
                    if (event.getAreaClass().getUseTime() != -1) {
                        map.put(event.getAreaClass().getName(), event.getAreaClass().getUseTime());
                    }
                    AreaMainClass.getInstance().useTime.put(player.getName(),map);
                }

            }else{
                Tools.sendMessage(player,Tools.getLanguage("player.not.key.area").replace("{name}",event.getAreaClass().getName()));
                Tools.konkBack(player, event.getAreaClass().getPos().getCenterPosition());
                return;
            }
        }else{
            Tools.sendMessage(player,Tools.getLanguage("area.lock.message").replace("{name}",event.getAreaClass().getName()));
            Tools.konkBack(player, event.getAreaClass().getPos().getCenterPosition());
            return;
        }
        AreaClass areaClass = event.getAreaClass();
        if(AreaMainClass.getInstance().isSendJoinMessage()){
            if(areaClass != null){
                if(!areaClass.isKey()){
                    Tools.sendMessage(player,Tools.getLanguage("area.lock.message").replace("{name}",areaClass.getName()));
                    return;
                }
                String title = AreaMainClass.getLang("join.area.title");
                title = title.replace("{name}",areaClass.getName());
                String sub = AreaMainClass.getLang("join.area.sub.title");
                sub = sub.replace("{sub}",areaClass.getSubMessage());
                Tools.sendMessage(player,title,sub,AreaMainClass.getInstance().getMessageType());
                join.add(player);
            }
        }

    }
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event){
        Player player = event.getPlayer();
        PlayerClass playerClass = PlayerClass.getPlayerClass(player.getName());
        playerClass.save();
        AreaMainClass.getInstance().canJoin.remove(player.getName());
        join.remove(player);
    }

    @EventHandler
    public void onPlayerQuitArea(PlayerQuitAreaEvent event){
        Player player = event.getPlayer();
        join.remove(player);
        if(AreaMainClass.getInstance().isSendQuitMessage()){
            AreaClass areaClass = event.getAreaClass();
            if(areaClass != null){
                if(!areaClass.isKey()){
                    return;
                }
                String title = AreaMainClass.getLang("quit.area.title");
                title = title.replace("{name}",areaClass.getName());
                String sub = AreaMainClass.getLang("quit.area.sub.title");
                sub = sub.replace("{sub}",areaClass.getSubMessage());
                Tools.sendMessage(player,title,sub,AreaMainClass.getInstance().getMessageType());
            }
        }
//        AreaMainClass.getInstance().useTime.remove(player.getName());
    }

    @EventHandler
    public void onPVP(EntityDamageEvent event){
        if(event instanceof EntityDamageByEntityEvent){
            Entity entity = event.getEntity();
            Entity dam = ((EntityDamageByEntityEvent) event).getDamager();
            if(entity instanceof Player && dam instanceof Player){
                AreaClass areaClass = Tools.getCanPVPArea(entity);
                if(AreaMainClass.getInstance().canProtectionLevel()){
                    if(Tools.canInAreaLevel(entity)){
                        event.setCancelled();
                    }
                }
                if(areaClass != null){
                    if(!areaClass.isCanPVP()){
                        event.setCancelled();
                        Tools.sendMessage((Player) dam,Tools.getLanguage("player.not.pvp"),"","msg");
                    }else{
                        event.setCancelled(false);
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onDeath(PlayerDeathEvent event){
        Player player = event.getEntity();
        AreaClass areaClass = Tools.getCanPVPArea(player);
        if(areaClass != null) {
            if(areaClass.isCanDrop()){
                if(event.getKeepInventory()){
                    event.setKeepInventory(false);
                    Tools.sendMessage(player,Tools.getLanguage("player.death.drop"),"","msg");
                }
            }
        }
    }

    @EventHandler
    public void onBuyAreaEvent(PlayerBuyAreaEvent event){
        Player player = event.getPlayer();
        AreaClass areaClass = event.getAreaClass();
        if(areaClass != null){
            if(AreaMainClass.getInstance().canSendkeyArea()){
                String broad = AreaMainClass.getLang("buy.area.scauss");
                broad = broad.replace("{player}",player.getName())
                        .replace("{level}",areaClass.getLevel()+"")
                        .replace("{name}",areaClass.getName());
                Server.getInstance().broadcastMessage(broad);
            }

            Tools.spawnFirework(player);
            String title = AreaMainClass.getLang("player.buy.area.success").replace("{name}",areaClass.getName());
            Tools.sendMessage(player,title,"",AreaMainClass.getInstance().getMessageType());
        }
    }




}

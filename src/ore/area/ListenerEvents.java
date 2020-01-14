package ore.area;


import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.command.ConsoleCommandSender;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.event.block.BlockEvent;
import cn.nukkit.event.block.BlockPlaceEvent;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.event.player.PlayerMoveEvent;
import cn.nukkit.event.player.PlayerQuitEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.Location;
import cn.nukkit.level.Position;
import cn.nukkit.scheduler.Task;
import ore.area.events.*;
import ore.area.utils.BossAPI;
import ore.area.utils.ItemIDSunName;
import ore.area.utils.Tools;
import ore.area.utils.area.AreaClass;
import ore.area.utils.area.DefaultBlockClass;
import ore.area.utils.player.PlayerClass;
import ore.area.utils.task.ParticleTask;
import ore.area.utils.task.PlayerLoadTask;
import ore.area.utils.task.PlayerTransferTask;

import java.util.LinkedList;


/**
 * @author 若水
 */
public class ListenerEvents implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        Server.getInstance().getScheduler().scheduleRepeatingTask(new PlayerLoadTask(player),20);
        PlayerClass playerClass = PlayerClass.getPlayerClass(player);
        if(!AreaMainClass.getInstance().playerClasses.containsKey(player)){
            AreaMainClass.getInstance().playerClasses.put(player,playerClass);
        }

    }

    @EventHandler
    public void onReset(AreaResetEvent event){
        AreaClass areaClass = AreaClass.getAreaClass(event.getName());
        if(areaClass != null){
            if(areaClass.isKey()){
                for(Player player:Server.getInstance().getOnlinePlayers().values()){
                    String n = Tools.getPlayerTouchArea(player.getPosition());
                    if(n != null){
                        if(n.equals(event.getName())){
                            player.teleport(areaClass.getTransfer());
                            player.sendMessage(AreaMainClass.getLang("reset.area.message").replace("{name}",n));
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
                        if(!areaClass.isKey()){
                            event.setCancelled();
                            return;
                        }
                    }
                    PlayerClass playerClass = PlayerClass.getPlayerClass(player);
                    if(!playerClass.canKey(name)){
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
                    if(!areaClass.isKey()){
                        event.setCancelled();
                        return;
                    }
                }
                if(!PlayerClass.getPlayerClass(player).canKey(name)){
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
        PlayerClass playerClass = PlayerClass.getPlayerClass(player);
        playerClass.addCount(block);
        int count = playerClass.getBreakBlockCount(block);
        LinkedList<String> strings = DefaultBlockClass.getSuccessByCount(block,count);
        if(strings != null){
            Tools.sendMessage(player,
                    Tools.getLanguage("player.break.block.success").replace("{block}"
                            , ItemIDSunName.getIDByName(block.getId(),block.getDamage()))
                            .replace("{count}",count+""));
            player.awardAchievement(count+DefaultBlockClass.getBlockSaveString(block));
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
                        if(!areaClass.isKey()){
                            event.setCancelled();
                            return;
                        }
                    }
                    if(!PlayerClass.getPlayerClass(player).canKey(name)){
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
                    if(!areaClass.isKey()){
                        event.setCancelled();
                        return;
                    }
                }
                if(!PlayerClass.getPlayerClass(player).canKey(name)){
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
            if(aClass.getTransferMoney() > 0){
                if(Tools.getMoney().myMoney(player) < aClass.getTransferMoney()){
                    Tools.sendMessage(player,Tools.getLanguage("player.buy.area.error")
                            .replace("{name}",aClass.getName()).replace("{money}",aClass.getTransferMoney()+""));
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
                Server.getInstance().getScheduler().scheduleRepeatingTask(new ParticleTask(player.getName(),pos),2);
            }
            Server.getInstance().getScheduler().scheduleRepeatingTask(new PlayerTransferTask(player,AreaMainClass.getInstance().getTransferTime(),aClass),20);
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
    }


    @EventHandler
    public void onPlayerJoinArea(PlayerJoinAreaEvent event){
        Player player = event.getPlayer();
        if(AreaMainClass.getInstance().isSendJoinMessage()){
            AreaClass areaClass = event.getAreaClass();
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

            }
        }
    }
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event){
        Player player = event.getPlayer();
        PlayerClass playerClass = PlayerClass.getPlayerClass(player);
        playerClass.save();
    }

    @EventHandler
    public void onPlayerQuitArea(PlayerQuitAreaEvent event){
        Player player = event.getPlayer();
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

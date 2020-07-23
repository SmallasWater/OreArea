package ore.area.windows;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.server.DataPacketReceiveEvent;
import cn.nukkit.network.protocol.ModalFormResponsePacket;
import ore.area.AreaMainClass;
import ore.area.events.PlayerBuyAreaEvent;
import ore.area.events.PlayerTransferAreaEvent;
import ore.area.utils.Tools;
import ore.area.utils.area.AreaClass;
import ore.area.utils.LoadMoney;
import ore.area.utils.player.PlayerClass;

import java.util.LinkedList;

/**
 * @author 若水
 */
public class ListenerWindow implements Listener {
    private static final String NULL = "null";


    @EventHandler
    public void getUI(DataPacketReceiveEvent event){
        String data;
        ModalFormResponsePacket ui;
        Player player = event.getPlayer();
        if((event.getPacket() instanceof ModalFormResponsePacket)) {
            ui = (ModalFormResponsePacket) event.getPacket();
            data = ui.data.trim();
            int fromId = ui.formId;
            switch(fromId){
                case CreateWindow.MENU:
                    if(NULL.equals(data)){
                        return;
                    }
                    LinkedList<AreaClass> areaClass = Tools.sqrtAreaClass();
                    if(areaClass.size() < Integer.parseInt(data)){
                        player.sendMessage("请重试.....");
                        return;
                    }
                    AreaClass area = areaClass.get(Integer.parseInt(data));
                    AreaMainClass.getInstance().clickArea.put(player,area);
                    CreateWindow.sendSub(player);
                    break;
                case CreateWindow.SUB:
                    if(NULL.equals(data)){
                        return;
                    }
                    PlayerClass playerClass = PlayerClass.getPlayerClass(player.getName());
                    area = AreaMainClass.getInstance().clickArea.get(player);
                    boolean canTransfer = !AreaMainClass.getInstance().isKeyCanOpen();

                    if(Integer.parseInt(data) == 0){
                        if(!area.isKey()){
                            Tools.sendMessage(player,Tools.getLanguage("transfer.area.lock").replace("{name}",area.getName()));
                            return;
                        }
                        if(!canTransfer){
                            if(!playerClass.canKey(area.getName())){
                                if(!"".equals(area.getLastArea())){
                                    if(!playerClass.canKey(area.getLastArea())){
                                        Tools.sendMessage(player,Tools.getLanguage("player.buy.area.last").replace("{name}",area.getLastArea()));
                                        return;
                                    }
                                }
                                LoadMoney money = AreaMainClass.getInstance().getMoney();
                                if(money.myMoney(player) < area.getMoney()){
                                    String t = AreaMainClass.getLang("player.buy.area.error");
                                    t = t.replace("{mymoney}",String.format("%.2f",money.myMoney(player)))
                                            .replace("{money}",String.format("%.2f",area.getMoney()))
                                            .replace("{math}",String.format("%.2f",area.getMoney() - money.myMoney(player)));
                                    player.sendMessage(t);
                                    return;
                                }else {
                                    money.reduceMoney(player, area.getMoney());
                                    PlayerBuyAreaEvent buyAreaEvent = new PlayerBuyAreaEvent(player, area);
                                    Server.getInstance().getPluginManager().callEvent(buyAreaEvent);
                                    playerClass.addKey(area.getName());
                                    player.sendMessage(AreaMainClass.getLang("player.buy.area.success").replace("{name}",area.getName()));
                                    return;
                                }
                            }else{
                                PlayerTransferAreaEvent transferAreaEvent = new PlayerTransferAreaEvent(player, area);
                                Server.getInstance().getPluginManager().callEvent(transferAreaEvent);
                            }
                        }else{
                            PlayerTransferAreaEvent transferAreaEvent = new PlayerTransferAreaEvent(player, area);
                            Server.getInstance().getPluginManager().callEvent(transferAreaEvent);
                        }
                    }else{
                        CreateWindow.sendMenu(player);
                    }

                    break;
                    default: break;

            }
        }
    }
}

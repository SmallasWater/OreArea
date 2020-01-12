package ore.area.utils.task;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.scheduler.Task;
import ore.area.AreaMainClass;
import ore.area.utils.BossAPI;
import ore.area.utils.Tools;
import ore.area.utils.area.AreaClass;



/**
 * @author 若水
 */
public class PlayerTransferTask extends Task {

    private Player player;
    private int time;
    private AreaClass aClass;

    public PlayerTransferTask(Player player, int time, AreaClass aClass){
        this.player = player;
        this.time = time;
        this.aClass = aClass;
    }
    @Override
    public void onRun(int i) {
        if(player.isOnline()){
            if(!AreaMainClass.getInstance().transfer.contains(player.getName())){
                this.cancel();
                return;
            }
            if(time > 0){
                time--;
                String title = AreaMainClass.getLang("tansfer.area.message")
                        .replace("{name}",aClass.getName());
                String sub =  AreaMainClass.getLang("tansfer.area.sub.message").replace("{s}"
                        ,time+"");
                Tools.sendMessage(player,title,sub,AreaMainClass.getInstance().getTransferMessageType());
            }else{
                if(AreaMainClass.getInstance().bossMessage.containsKey(player)){
                    BossAPI api = new BossAPI(player);
                    api.remove();
                }
                AreaMainClass.getInstance().transfer.remove(player.getName());
                player.teleport(aClass.getTransfer());
                if(AreaMainClass.getInstance().canSendTransferBroadCastMessage()){
                    Server.getInstance().broadcastMessage(AreaMainClass.getLang("transaction.area.scauss")
                            .replace("{player}",player.getName())
                                    .replace("{name}",aClass.getName()));
                }
                if(AreaMainClass.getInstance().canSendTransferMessage()){
                    Tools.sendMessage(player,AreaMainClass.getLang("transaction.area.scauss")
                                    .replace("{player}","你")
                                            .replace("{name}",aClass.getName()),aClass.getSubMessage()
                            ,AreaMainClass.getInstance().getTransferMessageType());
                }
                this.cancel();
            }
        }else{
            this.cancel();
        }
    }
}

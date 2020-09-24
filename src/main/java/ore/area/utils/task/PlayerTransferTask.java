package ore.area.utils.task;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.scheduler.PluginTask;
import cn.nukkit.scheduler.Task;
import ore.area.AreaMainClass;
import ore.area.utils.BossApi;
import ore.area.utils.Tools;
import ore.area.utils.area.AreaClass;



/**
 * @author 若水
 */
public class PlayerTransferTask extends PluginTask<AreaMainClass> {

    private Player player;
    private int time;
    private AreaClass aClass;

    public PlayerTransferTask(AreaMainClass plugin,Player player, int time, AreaClass aClass){
        super(plugin);
        this.player = player;
        this.time = time;
        this.aClass = aClass;
    }
    @Override
    public void onRun(int i) {
        if(player.isOnline()){
            if(!getOwner().transfer.contains(player.getName())){
                this.cancel();
                return;
            }
            if(time > 0){
                time--;
                String title = AreaMainClass.getLang("tansfer.area.message")
                        .replace("{name}",aClass.getName());
                String sub =  AreaMainClass.getLang("tansfer.area.sub.message").replace("{s}"
                        ,time+"");
                Tools.sendMessage(player,title,sub,getOwner().getTransferMessageType());
            }else{
                if(getOwner().bossMessage.containsKey(player)){
                    BossApi api = new BossApi(player);
                    api.remove();
                }
                getOwner().transfer.remove(player.getName());
                player.teleport(aClass.getTransfer());
                if(getOwner().canSendTransferBroadCastMessage()){
                    Server.getInstance().broadcastMessage(AreaMainClass.getLang("transaction.area.scauss")
                            .replace("{player}",player.getName())
                                    .replace("{name}",aClass.getName()));
                }
                if(getOwner().canSendTransferMessage()){
                    Tools.sendMessage(player,AreaMainClass.getLang("transaction.area.scauss")
                                    .replace("{player}",AreaMainClass.isChinese()?"你":"yours")
                                            .replace("{name}",aClass.getName()),aClass.getSubMessage()
                            ,getOwner().getTransferMessageType());
                }
                this.cancel();
            }
        }else{
            this.cancel();
        }
    }
}

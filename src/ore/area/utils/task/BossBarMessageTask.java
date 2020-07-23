package ore.area.utils.task;

import cn.nukkit.Player;
import cn.nukkit.scheduler.PluginTask;
import cn.nukkit.scheduler.Task;
import ore.area.AreaMainClass;
import ore.area.utils.BossApi;

/**
 * @author 若水
 */
public class BossBarMessageTask extends PluginTask<AreaMainClass> {

    public BossBarMessageTask(AreaMainClass owner) {
        super(owner);
    }

    @Override
    public void onRun(int i) {
        if(getOwner().bossMessage.size() > 0){
            try{
                for (Player player: getOwner().bossMessage.keySet()) {
                    if(player.isOnline()){
                        BossApi bossBarAPI = new BossApi(player);
                        bossBarAPI.showBoss();
                    }else{
                        this.cancel();
                    }
                }
            }catch (Exception ignored){}
        }
    }
}

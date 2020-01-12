package ore.area.utils.task;

import cn.nukkit.Player;
import cn.nukkit.scheduler.Task;
import ore.area.AreaMainClass;
import ore.area.utils.BossAPI;

/**
 * @author 若水
 */
public class BossBarMessageTask extends Task {

    @Override
    public void onRun(int i) {
        if(AreaMainClass.getInstance().bossMessage.size() > 0){
            try{
                for (Player player: AreaMainClass.getInstance().bossMessage.keySet()) {
                    if(player.isOnline()){
                        BossAPI bossBarAPI = new BossAPI(player);
                        bossBarAPI.showBoss();
                    }else{
                        this.cancel();
                    }
                }
            }catch (Exception ignored){}
        }
    }
}

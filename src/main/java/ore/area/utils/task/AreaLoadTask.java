package ore.area.utils.task;


import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.plugin.Plugin;
import cn.nukkit.scheduler.PluginTask;
import cn.nukkit.scheduler.Task;
import ore.area.AreaMainClass;
import ore.area.events.PlayerUseCountResetEvent;
import ore.area.utils.area.AreaClass;
import ore.area.utils.Tools;
import ore.area.utils.player.PlayerClass;

import java.util.Date;


/**
 * @author 若水 计算冷却类...
 */
public class AreaLoadTask extends PluginTask<AreaMainClass> {


    public AreaLoadTask(AreaMainClass plugin){
        super(plugin);

    }


    @Override
    public void onRun(int i) {
        for(AreaClass cl:AreaMainClass.getInstance().areas.values()) {
            if (cl != null) {
                if (cl.isKey()) {
                    if(cl.getReset() != -1) {
                        if (Tools.inTime()) {
                            if (!AreaMainClass.timer.containsKey(cl.getName())) {
                                AreaMainClass.timer.put(cl.getName(), cl.getReset());
                            }
                            if (AreaMainClass.timer.get(cl.getName()) == 0) {
                                cl.setBlock();
                                AreaMainClass.timer.put(cl.getName(), cl.getReset());
                            } else {
                                AreaMainClass.timer.put(cl.getName(), AreaMainClass.timer.get(cl.getName()) - 1);
                            }
                        }
                    }else{
                        AreaMainClass.timer.remove(cl.getName());
                    }
                }
                if(Tools.getTime(cl.getLastTime()) > cl.getResetDay()){
                    for(PlayerClass playerClass: AreaMainClass.getInstance().playerClasses.values()){
                        Player player = Server.getInstance().getPlayer(playerClass.getPlayerName());
                        if(player != null) {
                            PlayerUseCountResetEvent event = new PlayerUseCountResetEvent(player,cl);
                            Server.getInstance().getPluginManager().callEvent(event);
                            if(event.isCancelled()){
                                return;
                            }
                        }
                        playerClass.setJoinCount(cl.getName(),cl.getJoinCount());
                    }
                    cl.setLastTime(new Date());

                }

            }
        }
    }
}

package ore.area.utils.task;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.scheduler.Task;
import ore.area.AreaMainClass;
import ore.area.events.PlayerJoinAreaEvent;
import ore.area.events.PlayerQuitAreaEvent;
import ore.area.utils.Tools;
import ore.area.utils.area.AreaClass;

/**
 *
 * @author 若水
 */
public class PlayerLoadTask extends Task {

    private Player player;
    public PlayerLoadTask(Player player){
        this.player = player;
    }

    @Override
    public void onRun(int i) {
        if(player.isOnline()){
            String name = Tools.getPlayerTouchArea(player);
            if(name != null){
                AreaClass area = AreaClass.getAreaClass(name);
                if(area != null){
                    if(!AreaMainClass.getInstance().canJoin.containsKey(player.getName())){
                        AreaMainClass.getInstance().canJoin.put(player.getName(), name);
                        PlayerJoinAreaEvent joinEvent = new PlayerJoinAreaEvent(player,area );
                        Server.getInstance().getPluginManager().callEvent(joinEvent);
                    }else{
                        String exit = AreaMainClass.getInstance().canJoin.get(player.getName());
                        if(!exit.equals(name)){
                            PlayerJoinAreaEvent joinEvent = new PlayerJoinAreaEvent(player,area);
                            Server.getInstance().getPluginManager().callEvent(joinEvent);
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
        }else{
            AreaMainClass.getInstance().canJoin.remove(player.getName());
            this.cancel();
        }
    }
}

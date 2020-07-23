package ore.area.utils.task;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.scheduler.PluginTask;
import ore.area.AreaMainClass;
import ore.area.events.PlayerUseTimeEndEvent;
import ore.area.utils.Tools;
import ore.area.utils.area.AreaClass;

import java.util.LinkedHashMap;

/**
 * @author SmallasWater
 */
public class PlayerUseTimeTask extends PluginTask<AreaMainClass> {

    public PlayerUseTimeTask(AreaMainClass owner) {
        super(owner);
    }

    @Override
    public void onRun(int i) {
        int time;
        LinkedHashMap<String,Integer> map;
        String areaClass;
        for(Player player:Server.getInstance().getOnlinePlayers().values()) {
//            for (String playerName : getOwner().useTime.keySet()) {
                if (player.isOnline()) {
                    if(getOwner().useTime.containsKey(player.getName())) {
                        map = getOwner().useTime.get(player.getName());
                        for (String areaName : map.keySet()) {
                            areaClass = Tools.getPlayerInArea(player);
                            if (areaClass != null) {
                                if (areaName.equalsIgnoreCase(areaClass)) {
                                    time = map.get(areaClass);
                                    if (time > 1) {
                                        map.put(areaClass, time - 1);
                                        getOwner().useTime.put(player.getName(), map);
                                    } else {
                                        map.remove(areaClass);
                                        getOwner().useTime.put(player.getName(), map);
                                        AreaClass area = AreaClass.getAreaClass(areaName);
                                        if (area != null) {
                                            PlayerUseTimeEndEvent event = new PlayerUseTimeEndEvent(player, area);
                                            Server.getInstance().getPluginManager().callEvent(event);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
//        }
    }
}

package ore.area.events;

import cn.nukkit.Player;
import ore.area.utils.area.AreaClass;

/**
 * @author SmallasWater
 */
public class PlayerUseTimeEndEvent extends PlayerAreaEvent {

    public PlayerUseTimeEndEvent(Player player, AreaClass areaClass) {
        super(player, areaClass);
    }
}

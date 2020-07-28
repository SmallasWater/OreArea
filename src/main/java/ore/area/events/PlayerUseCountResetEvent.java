package ore.area.events;

import cn.nukkit.Player;
import ore.area.utils.area.AreaClass;

/**
 * @author SmallasWater
 */
public class PlayerUseCountResetEvent extends PlayerAreaEvent{

    public PlayerUseCountResetEvent(Player player, AreaClass areaClass) {
        super(player, areaClass);
    }
}

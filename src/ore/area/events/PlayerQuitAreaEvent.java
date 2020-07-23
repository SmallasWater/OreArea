package ore.area.events;

import cn.nukkit.Player;
import ore.area.utils.area.AreaClass;

/**
 * @author 若水
 */
public class PlayerQuitAreaEvent extends PlayerAreaEvent {

    public PlayerQuitAreaEvent(Player player, AreaClass areaClass) {
        super(player, areaClass);
    }
}

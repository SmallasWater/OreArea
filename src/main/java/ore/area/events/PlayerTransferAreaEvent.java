package ore.area.events;

import cn.nukkit.Player;
import ore.area.utils.area.AreaClass;

/**
 * @author 若水
 */
public class PlayerTransferAreaEvent extends PlayerAreaEvent {


    public PlayerTransferAreaEvent(Player player, AreaClass areaClass) {
        super(player, areaClass);
    }
}

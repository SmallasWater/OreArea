package ore.area.events;

import cn.nukkit.Player;
import cn.nukkit.event.Cancellable;
import ore.area.utils.area.AreaClass;

/**
 * @author 若水
 */
public class PlayerJoinAreaEvent extends PlayerAreaEvent implements Cancellable {


    public PlayerJoinAreaEvent(Player player, AreaClass areaClass) {
        super(player, areaClass);
    }
}

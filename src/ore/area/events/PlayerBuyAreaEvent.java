package ore.area.events;

import cn.nukkit.Player;
import ore.area.utils.area.AreaClass;

/**
 * @author 若水
 */
public class PlayerBuyAreaEvent extends PlayerAreaEvent {


    public PlayerBuyAreaEvent(Player player, AreaClass areaClass) {
        super(player, areaClass);
    }
}

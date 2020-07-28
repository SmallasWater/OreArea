package ore.area.events;

import cn.nukkit.Player;
import ore.area.utils.area.AreaClass;

/**
 * @author SmallasWater
 */
public class PlayerAreaEvent extends AreaEvent {

    private Player player;
    PlayerAreaEvent(Player player, AreaClass areaClass){
        super(areaClass);
        this.player = player;

    }

    public Player getPlayer() {
        return player;
    }
}

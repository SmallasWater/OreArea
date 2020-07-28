package ore.area.events;

import cn.nukkit.event.Cancellable;
import cn.nukkit.event.Event;
import cn.nukkit.event.HandlerList;
import ore.area.utils.area.AreaClass;

/**
 * @author SmallasWater
 */
public class AreaEvent extends Event implements Cancellable {
    private static final HandlerList HANDLERS = new HandlerList();

    private AreaClass areaClass;
    public static HandlerList getHandlers() {
        return HANDLERS;
    }
    AreaEvent(AreaClass areaClass) {
        this.areaClass = areaClass;
    }

    public AreaClass getAreaClass() {
        return areaClass;
    }
}

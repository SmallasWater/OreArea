package ore.area.events;

import cn.nukkit.event.HandlerList;
import cn.nukkit.event.plugin.PluginEvent;
import cn.nukkit.plugin.Plugin;
import javafx.event.Event;

public class AreaResetEvent extends PluginEvent {
    private static final HandlerList handlers = new HandlerList();

    public static HandlerList getHandlers() {
        return handlers;
    }
    public String name;
    public AreaResetEvent(Plugin plugin,String area) {
        super(plugin);
        this.name = area;
    }

    public String getName() {
        return name;
    }
}

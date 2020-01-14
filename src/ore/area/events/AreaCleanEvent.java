package ore.area.events;

import cn.nukkit.event.HandlerList;
import cn.nukkit.event.plugin.PluginEvent;
import cn.nukkit.plugin.Plugin;

public class AreaCleanEvent extends PluginEvent {
    private static final HandlerList handlers = new HandlerList();

    public static HandlerList getHandlers() {
        return handlers;
    }
    public String name;
    public AreaCleanEvent(String name,Plugin plugin) {
        super(plugin);
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

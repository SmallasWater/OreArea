package ore.area.utils;

import cn.nukkit.Player;
import cn.nukkit.utils.DummyBossBar;

public class BossBar extends DummyBossBar.Builder{

    private final long bossBarId;

    private String text;
    private float length;
    public BossBar(Player player, long bossBarId) {
        super(player);
        this.bossBarId = bossBarId;
    }

    public void setLength(float length) {
        this.length = length;
    }

    public float getLength() {
        return length;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }


    public long getBossBarId() {
        return bossBarId;
    }
}

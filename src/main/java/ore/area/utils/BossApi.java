package ore.area.utils;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.scheduler.Task;
import cn.nukkit.utils.DummyBossBar;
import ore.area.AreaMainClass;

/**
 * @author 若水
 */
public class BossApi {
    protected Player player;

    public BossApi(Player player) {
        this.player = player;
    }


    void createBossBar(String text) {
        if(AreaMainClass.getInstance().bossMessage.containsKey(player)){
            BossBar bar = AreaMainClass.getInstance().bossMessage.get(player);
            bar.setText(text);
            AreaMainClass.getInstance().bossMessage.put(player,bar);
        }else{
            long eid = 0xbb075;
            BossBar bossBar = (new BossBar(this.player, eid));
            bossBar.setLength(0);
            bossBar.setText(text);
            AreaMainClass.getInstance().bossMessage.put(this.player, bossBar);
            player.createBossBar(AreaMainClass.getInstance().bossMessage.get(this.player).build());
        }

    }


    public void showBoss() {
        if (AreaMainClass.getInstance().bossMessage.containsKey(player)) {
            BossBar bar = AreaMainClass.getInstance().bossMessage.get(player);
            DummyBossBar bossBar = AreaMainClass.getInstance().bossMessage.get(player).build();
            bossBar.setText(bar.getText());
            bossBar.setLength(0);
            player.createBossBar(bossBar);
        }else{
            remove();
        }
    }
    public void remove(){
        if(AreaMainClass.getInstance().bossMessage.containsKey(player)){
            player.removeBossBar(AreaMainClass.getInstance().bossMessage.get(player).getBossBarId());
            BossBar bar = AreaMainClass.getInstance().bossMessage.get(player);
            AreaMainClass.getInstance().bossMessage.remove(player,bar);
        }
    }
}

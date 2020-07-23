package ore.area.utils.task;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.level.Position;
import cn.nukkit.level.particle.EnchantParticle;
import cn.nukkit.level.particle.FlameParticle;
import cn.nukkit.level.particle.SmokeParticle;
import cn.nukkit.level.particle.SplashParticle;
import cn.nukkit.math.Vector3;
import cn.nukkit.scheduler.PluginTask;
import cn.nukkit.scheduler.Task;
import ore.area.AreaMainClass;
import ore.area.utils.Tools;

import java.util.LinkedList;

/**
 * @author 若水
 */
public class ParticleTask extends PluginTask<AreaMainClass> {

    public String player;
    public Position pos;
    private double r = 3;

    public ParticleTask(AreaMainClass plugin,String player,Position pos){
        super(plugin);
        this.player = player;
        this.pos = pos;
    }
    @Override
    public void onRun(int i) {
        if(AreaMainClass.getInstance().transfer.contains(player)){
            if(r > 1.7){
                r -= 0.3;
            }else{
                 r = 3;
            }
            LinkedList<double[]> poss = Tools.showParticle(r);
            for (double[] p : poss) {
                pos.getLevel().addParticle(new SmokeParticle(
                        new Vector3(p[0] + pos.getX(), p[1] + pos.getY(), p[2] + pos.getZ())));
            }
        }else{
            this.cancel();
        }
    }
}

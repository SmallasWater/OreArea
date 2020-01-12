package ore.area.utils.task;



import cn.nukkit.level.Level;

import cn.nukkit.scheduler.AsyncTask;
import ore.area.utils.area.AreaClass;
import ore.area.utils.area.BlockClass;
import ore.area.utils.area.Vector;


import java.util.Random;


/**
 * @author 若水
 */
public class AsyncBlockTask extends AsyncTask {

    private AreaClass aClass;

    public AsyncBlockTask(AreaClass aClass){
        this.aClass = aClass;
    }


    @Override
    public void onRun() {
        Vector pos = aClass.getPos();
        pos.sort();
        Level l = pos.getLevel();
        for(int x = pos.getStartX(); x <= pos.getEndX();x++){
            for(int y = pos.getStartY(); y <= pos.getEndY();y++){
                for(int z = pos.getStartZ(); z <= pos.getEndZ();z++){
                    if(!l.loadChunk(x,z)){
                        l.generateChunk(x,z);
                    }
                    boolean b = false;
                    for(BlockClass block:aClass.getBlocks()){
                        int a = new Random().nextInt(100);
                        if(a <= block.getSpawnRation()){
                            b = true;
                            l.setBlock(x,y,z,block.getBlock(),false,true);
                            break;
                        }
                    }
                    if(!b){
                        l.setBlock(x,y,z,aClass.getBlocks().get(new Random().nextInt(aClass.getBlocks().size())).getBlock(),false,true);
                    }
                }
            }
        }
    }
}

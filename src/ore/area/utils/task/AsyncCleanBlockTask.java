package ore.area.utils.task;


import cn.nukkit.block.BlockAir;
import cn.nukkit.level.Level;
import ore.area.utils.area.AreaClass;

import ore.area.utils.area.Vector;


public class AsyncCleanBlockTask extends AsyncBlockTask {
    public AsyncCleanBlockTask(AreaClass aClass) {
        super(aClass);
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
                    l.setBlock(x,y,z,new BlockAir(),false,true);
                }
            }
        }
    }
}

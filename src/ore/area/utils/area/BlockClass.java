package ore.area.utils.area;


import cn.nukkit.block.Block;



/**
 * @author 若水
 */
public class BlockClass {

    private Block block;

    private int spawnRation;


    public BlockClass(Block block){
        this(block,1);
    }

    public BlockClass(Block block,int spawnRation){
        this.block = block;
        this.spawnRation = spawnRation;

    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof BlockClass){
            return block.equals(((BlockClass)obj).getBlock()) && spawnRation == ((BlockClass)obj).getSpawnRation();
        }
        return false;
    }

    public Block getBlock() {
        return block;
    }

    public int getSpawnRation() {
        return spawnRation;
    }

    @Override
    public String toString() {
        return "方块: "+block.getId()+" 几率: "+spawnRation;
    }
}

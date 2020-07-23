package ore.area.utils.area;

import cn.nukkit.level.Level;
import cn.nukkit.level.Position;

/**
 * @author 若水
 */
public class Vector implements Cloneable{

    private Level level;

    private int startX;

    private int endX;

    private int startY;

    private int endY;

    private int startZ;

    private int endZ;
    public Vector(Level level, int startX, int endX, int startY, int endY, int startZ, int endZ) {
        this.level = level;
        this.startX = startX;
        this.endX = endX;
        this.startY = startY;
        this.endY = endY;
        this.startZ = startZ;
        this.endZ = endZ;

    }


    public Vector(Position pos1,Position pos2) {
        this.level = pos1.getLevel();
        this.startX = (int) pos1.getX();
        this.endX = (int) pos2.getX();
        this.startY = (int) pos1.getY();
        this.endY = (int) pos2.getY();
        this.startZ = (int) pos1.getZ();
        this.endZ = (int) pos2.getZ();
    }



    public void sort(){
        int temp;
        if(this.startX > this.endX){
            temp = this.endX;
            this.endX = this.startX;
            this.startX = temp;
        }

        if(this.startY > this.endY){
            temp = this.endY;
            this.endY = this.startY;
            this.startY = temp;
        }

        if(this.startZ > this.endZ){
            temp = this.endZ;
            this.endZ = this.startZ;
            this.startZ = temp;
        }
    }

    Position getPos1(){
        return new Position(this.startX, this.startY, this.startZ,level);
    }

    Position getPos2(){
        return new Position(this.endX, this.endY, this.endZ,level);
    }


    public Position getCenterPosition(){
        return new Position(((this.endX - this.startX) / 2) + this.startX, this.endY, ((this.endZ - this.startZ) / 2) + this.startZ,level);
    }

    public Level getLevel() {
        return level;
    }

    public int getStartX() {
        return startX;
    }

    public int getEndX() {
        return endX;
    }

    public int getEndY() {
        return endY;
    }

    public int getEndZ() {
        return endZ;
    }

    public int getStartY() {
        return startY;
    }

    public int getStartZ() {
        return startZ;
    }


    @Override
    public String toString() {
        return "startX: "+startX+" endX: "+endX+" startY: "+startY+" endY: "+endY+" startZ "+startZ+" endZ "+endZ;
    }

    @Override
    public Vector clone() {
        try{
            return (Vector) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }
}

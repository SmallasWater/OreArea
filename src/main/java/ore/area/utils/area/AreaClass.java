package ore.area.utils.area;

import cn.nukkit.Server;


import cn.nukkit.form.element.ElementButtonImageData;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.utils.Config;
import ore.area.AreaMainClass;
import ore.area.events.AreaCleanEvent;
import ore.area.events.AreaResetEvent;
import ore.area.utils.Tools;
import ore.area.utils.task.AsyncBlockTask;
import ore.area.utils.task.AsyncCleanBlockTask;


import java.io.File;
import java.util.*;

/**
 * @author 若水
 */
public class AreaClass {

    private Vector pos;

    private boolean key;

    private double money;

    private double transferMoney;

    private Position transfer;

    private LinkedList<BlockClass> blocks;

    private String name;

    private int reset;

    private int useTime;

    private String lastArea = "";

    private int level;

    private String message;

    private String subMessage;

    private String buttonImage;

    private boolean canPVP;

    private boolean canDrop;

    private int joinCount;

    private Date lastTime;

    private int resetDay;




    private AreaClass(int level, String name, Vector pos, double money, Position transfer, boolean key, int reset, LinkedList<BlockClass> spawnBlocks){
        this.pos = pos;
        this.money = money;
        this.transfer = transfer;
        this.key = key;
        this.blocks = spawnBlocks;
        this.name = name;
        this.reset = reset;
        this.level = level;

    }

    public void setJoinCount(int joinCount) {
        this.joinCount = joinCount;
    }

    public int getJoinCount() {
        return joinCount;
    }

    /**
     * 获取使用时间
     * */
    public int getUseTime() {
        return useTime;
    }

    /**
     * 设置使用时间
     * */
    public void setUseTime(int useTime) {
        this.useTime = useTime;
    }

    /**
     * 获取传送金钱
     * */

    public double getTransferMoney() {
        return transferMoney;
    }

    public void setTransferMoney(double transferMoney) {
        this.transferMoney = transferMoney;
    }

    /**
     * 设置解锁矿区金钱
     * */
    public void setMoney(double money) {
        this.money = money;
    }

    /**
     * 获取矿区级别 (一般用作排序)
     * */
    public int getLevel() {
        return level;
    }

    /**
     * 设置矿区级别
     * */
    public void setLevel(int level) {
        this.level = level;
    }

    /**
     * 获取矿区名称
     * */
    public String getName() {
        return name;
    }

    /**
     * 获取刷新时间
     * */
    public int getReset() {
        return reset;
    }

    /**
     *  根据 {@link Vector} 创建矿区
     * */
    public static boolean createAreaClass(String name, Vector pos){
        if(!AreaMainClass.getInstance().areas.containsKey(name)){
            if(Tools.checkOverlap(pos.clone()) == null){

                AreaMainClass.getInstance().saveResource("area.yml","/Area/"+name+".yml",false);
                Config config = new Config(AreaMainClass.getInstance().getAreaFile(name),Config.YAML);
                config.set("level",pos.getLevel().getFolderName());
                config.set("pos",Tools.getPosMap(pos));
                Position p = Tools.getDefaultPosition(pos.getPos1(),pos.getPos2());
                config.set("transfer",Tools.getPosMap((int)p.x,(int)p.y,(int)p.z));
                config.set("areaLevel",AreaMainClass.getInstance().areas.size() + 1);
                config.set("reset.lastUpdataTime",Tools.toDateString(new Date()));
                config.save();
                AreaClass cn = getClassByConfig(name,config);
                if(cn != null){
                    AreaMainClass.getInstance().areas.put(name,cn);
                    cn.setBlock();
                    return true;
                }
            }
            AreaMainClass.getInstance().sort();
        }
        return false;
    }

    public Date getLastTime() {
        return lastTime;
    }

    public int getResetDay() {
        return resetDay;
    }

    private void setMessage(String message) {
        this.message = message;
    }

    /**
     * 获取矿区介绍
     * */
    public String getMessage() {
        return message;
    }


    /**
     * 设置矿区小标题
     * */
    public void setSubMessage(String subMessage) {
        this.subMessage = subMessage;
    }

    /**
     * 设置矿区传送点
     * */
    public void setTransfer(Position transfer) {
        this.transfer = transfer;
    }


    /**
     * 获取矿区传送点
     * */
    public Position getTransfer() {
        return transfer;
    }


    /**
     * 获取矿区小标题
     * */
    public String getSubMessage() {
        return subMessage;
    }


    /**
     * 通过矿区名称获取缓存在服务器的 {@link AreaClass}
     * */
    public static AreaClass getAreaClass(String name){
        if(AreaMainClass.getInstance().areas.containsKey(name)){
            return AreaMainClass.getInstance().areas.get(name);
        }
        return null;
    }



    @Override
    public int hashCode() {
        return pos.hashCode();
    }

    /**
     * 获取矿区价值
     * */
    public double getMoney() {
        return money;
    }

    public boolean isKey() {
        return key;
    }

    public Vector getPos() {
        return pos;
    }


    public LinkedList<BlockClass> getBlocks() {
        return blocks;
    }


    /**
     * 设置是否开启矿区
     * */
    public void setKey(boolean key) {
        this.key = key;
    }


    /**
     * 通过Config 文件 获取 {@link AreaClass}
     * */
    public static AreaClass getClassByConfig(String name,Config config){
        try{
            String levelName = config.getString("level");
            Level level = null;
            if (Server.getInstance().isLevelLoaded(levelName)) {
                level = Server.getInstance().getLevelByName(levelName);
            }else{
                if(Server.getInstance().loadLevel(levelName)){
                    level = Server.getInstance().getLevelByName(levelName);
                }
            }
            if(level != null){
                Vector pos = Tools.getVectorByMap((Map) config.get("pos"),level);
                boolean key = config.getBoolean("key");
                Position transfer = Tools.getPositionByMap((Map) config.get("transfer"),level);
                double money = config.getDouble("price");
                LinkedList<BlockClass> blocks = new LinkedList<>();
                Map map = (Map)config.get("spawnBlock");
                for(Object s:map.keySet()){
                    blocks.add(Tools.getBlockClassByMap(String.valueOf(s), (int)map.get(s)));
                }
                AreaClass c =  new AreaClass(config.getInt("areaLevel"),name,pos,money,transfer,key,config.getInt("resetTime(s)"),blocks);
                c.setMessage(config.getString("message"));
                Date date = Tools.getDate(config.getString("reset.lastUpdataTime"));
                if(date == null){
                    date = new Date();
                }
                c.setLastTime(date);
                c.setResetDay(config.getInt("reset.day",7));
                c.setLastArea(config.getString("last-area"));
                c.setSubMessage(config.getString("subMessage"));
                c.setButtonImage(config.getString("buttonImage"));
                c.setTransferMoney(config.getDouble("transferMoney"));
                c.setCanPVP(config.getBoolean("can-pvp",false));
                c.setCanDrop(config.getBoolean("can-drop",false));
                c.setUseTime(config.getInt("useTime(s)",600));
                c.setJoinCount(config.getInt("joinCount",2));

                return c;
            }
            return null;
        }catch (Exception e){
            Server.getInstance().getLogger().info("[OreAre] 读取"+name+"配置出现错误 已删除错误文件");
            File file = AreaMainClass.getInstance().getAreaFile(name);
            if(file.delete()){
                Server.getInstance().getLogger().info("[OreAre] 读取"+name+"文件移除成功");
            }else{
                Server.getInstance().getLogger().info("[OreAre] 读取"+name+"文件移除失败");
            }
            return null;
        }
    }

    private void setResetDay(int resetDay) {
        this.resetDay = resetDay;
    }

    public void setLastTime(Date lastTime) {
        this.lastTime = lastTime;
    }

    public boolean isCanDrop() {
        return canDrop;
    }

    public boolean isCanPVP() {
        return canPVP;
    }

    public void setCanPVP(boolean canPVP) {
        this.canPVP = canPVP;
    }

    public void setCanDrop(boolean canDrop) {
        this.canDrop = canDrop;
    }

    /**
     * 设置解锁矿区前先解锁的矿区
     * */
    public void setLastArea(String lastArea) {
        this.lastArea = lastArea;
    }

    /**
     * 获取前矿区
     * */
    public String getLastArea() {
        return lastArea;
    }

    /**
     * 设置按键图片
     *  path: 路径
     * */
    private void setButtonImage(String buttonImage) {
        this.buttonImage = buttonImage;
    }

    /**
     * 保存矿区数据
     * */
    public void save(){
        Config config = new Config(AreaMainClass.getInstance().getAreaFile(name),Config.YAML);
        config.set("level",pos.getLevel().getFolderName());
        config.set("areaLevel",level);
        config.set("pos",Tools.getPosMap(pos));
        config.set("price",money);
        config.set("can-pvp",canPVP);
        config.set("can-drop",canDrop);
        config.set("resetTime(s)",reset);
        config.set("useTime(s)",useTime);
        config.set("joinCount",joinCount);
        config.set("reset.lastUpdataTime",Tools.toDateString(lastTime));
        config.set("reset.day",resetDay);
        config.set("transfer",Tools.getPosMap((int) transfer.x,(int) transfer.y,(int) transfer.z));
        config.set("transferMoney",transferMoney);
        config.set("key",key);
        config.set("last-area",lastArea);
        config.set("message",message);
        config.set("subMessage",subMessage);
        config.set("buttonImage",buttonImage);
        config.set("spawnBlock",Tools.getMapByBlockClass(blocks));
        config.save();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof AreaClass){
            return ((AreaClass) obj).getName().equals(name);
        }
        return false;
    }

    public static AreaClass getAreaClassByLevel(int level){
        for (AreaClass areaClass:AreaMainClass.getInstance().areas.values()){
            if(areaClass.getLevel() == level){
                return areaClass;
            }
        }
        return null;
    }

    public static int getMaxAreaLevel(){
        int level = 0;
        for (AreaClass areaClass:AreaMainClass.getInstance().areas.values()){
            if(areaClass.getLevel() > level) {
                level = areaClass.getLevel();
            }
        }
        return level;
    }

    /**
     * 清空方块
     * */
    public synchronized void cleanBlock(){
        AreaCleanEvent event = new AreaCleanEvent(this);
        Server.getInstance().getPluginManager().callEvent(event);
        Server.getInstance().getScheduler().scheduleAsyncTask(AreaMainClass.getInstance(),new AsyncCleanBlockTask(this));
    }

    /**
     * 为矿区填充方块
     * */
    public synchronized void setBlock(){
        if(isKey()){
            AreaResetEvent event = new AreaResetEvent(this);
            Server.getInstance().getPluginManager().callEvent(event);
            Server.getInstance().getScheduler().scheduleAsyncTask(AreaMainClass.getInstance(),new AsyncBlockTask(this));
        }
    }
    /**
     * 获取按键图片...
     * */
    public ElementButtonImageData getButtonImage(){
        String type;
        String path;
        if(buttonImage.split(":").length > 1){
            String objPath = buttonImage.split(":")[0];
            if(objPath.equals(ElementButtonImageData.IMAGE_DATA_TYPE_PATH)
                    || objPath.equals(ElementButtonImageData.IMAGE_DATA_TYPE_URL)){
                type = objPath;
                path = buttonImage.split(":")[1];
            }else{
                type = buttonImage.split(":")[1];
                path = buttonImage.split(":")[0];
            }
        }else{
            type = ElementButtonImageData.IMAGE_DATA_TYPE_PATH;
            path = buttonImage;
        }
        return new ElementButtonImageData(type, path);
    }


    /**
     * 删除矿区
     * */
    public void delete(){
        if(!AreaMainClass.getInstance().getAreaFile(name).delete()){
            Server.getInstance().getLogger().warning("删除矿区 "+name+"失败");
        }else{
            key = false;
            this.cleanBlock();
            AreaMainClass.getInstance().areas.remove(name);
            AreaMainClass.getInstance().sort();
        }
    }
}

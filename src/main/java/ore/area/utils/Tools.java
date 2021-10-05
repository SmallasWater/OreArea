package ore.area.utils;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.entity.item.EntityFirework;
import cn.nukkit.item.ItemFirework;
import cn.nukkit.level.Level;
import cn.nukkit.level.Location;
import cn.nukkit.level.Position;
import cn.nukkit.level.particle.DustParticle;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.NBTIO;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.DoubleTag;
import cn.nukkit.nbt.tag.FloatTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.scheduler.Task;
import cn.nukkit.utils.BlockColor;
import cn.nukkit.utils.DyeColor;
import ore.area.AreaMainClass;
import ore.area.utils.area.AreaClass;
import ore.area.utils.area.BlockClass;
import ore.area.utils.area.Vector;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

/**
 * @author 若水
 */
public class Tools {



    public static Position getDefaultPosition(Position pos1,Position pos2) {
        double y;
        if(pos1.getY() >= pos2.getY()){
            y = pos1.getY();
        }else{
            y = pos2.getY();
        }
        return pos1.setComponents(pos1.getX(),y,pos1.getZ());

    }
    /** 将 2019/6/9/24/11 格式的string转换为 Date
     * @param format 时间格式
     *
     * @return 时间类*/
    public static Date getDate(String format){
        SimpleDateFormat lsdStrFormat =  new SimpleDateFormat("yyyy-MM-dd");
        try{
            return lsdStrFormat.parse(format);
        } catch (ParseException ex) {
            return null;
        }
    }
    /** 获取相差天数
     * @param oldData 时间1
     *
     * @return 天数*/

    public static int getTime(Date oldData) {
        long temp = System.currentTimeMillis() - oldData.getTime();
        long temp2 = temp % (1000 * 3600);
        return ((int) temp2 / 1000 / 60) / (60 * 24);
    }

    /** 将Date 转换 String*/
    public static String toDateString(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }


    public static LinkedHashMap<String,Integer> getPlayerRankingList(LinkedHashMap<String,Integer> lists){
        LinkedHashMap<String,Integer> rank = new LinkedHashMap<>(lists.size());
        for(String name: lists.keySet()){
            rank.put(name, lists.get(name));
        }
        List<Map.Entry<String, Integer>> wordMap = new ArrayList<>(rank.entrySet());
        wordMap.sort((o1, o2) -> {
            double result = o2.getValue() - o1.getValue();
            if (result > 0) {
                return -1;
            } else if (result == 0) {
                return 0;
            } else {
                return 1;
            }
        });
        return rank;
    }

    public static LoadMoney getMoney(){
        return AreaMainClass.getInstance().getMoney();
    }

    public static String getLanguage(String type){
        return AreaMainClass.getLang(type);

    }

    public static Vector getVectorByMap(Map map, Level level){
        int sx ,sy ,sz ,ex ,ey ,ez ;
        sx = (int) map.get("startX");
        ex = (int) map.get("endX");
        sy = (int) map.get("startY");
        ey = (int) map.get("endY");
        sz = (int) map.get("startZ");
        ez = (int) map.get("endZ");
        return new Vector(level,sx,ex,sy,ey,sz,ez);
    }

    public static void knockBack(Player player,Position vector){
        if(AreaMainClass.getInstance().isKonckBackPlayer()) {
            player.knockBack(player, 0, (player.x - vector.x), (player.z - vector.z), 0.8);
        }
    }

    public static BlockClass getBlockClassByMap(String name, int value){
        Block block;
        if(name.split(":").length > 1){
            block = Block.get(Integer.parseInt(name.split(":")[0]),Integer.parseInt(name.split(":")[1]));
        }else{
            block = Block.get(Integer.parseInt(name));
        }
        return new BlockClass(block,value);
    }

    public static void showParticle(Vector data, Level level,Player player){
        Vector vector = data.clone().sort();
        BlockColor color = new BlockColor(255,255,255);
        int x,y,z;
        for(y = vector.getStartY();y <= vector.getEndY();y++){
            if(y == vector.getStartY() || y == vector.getEndY()){
                for (z = vector.getStartZ(); z <= vector.getEndZ(); z++) {
                    level.addParticle(new DustParticle(new Vector3(vector.getStartX(), y, z), color));
                    level.addParticle(new DustParticle(new Vector3(vector.getEndX(), y, z), color));
                }
                for(x = vector.getStartX(); x<vector.getEndX();x++){
                    level.addParticle(new DustParticle(new Vector3(x, y, vector.getStartZ()), color),player);
                    level.addParticle(new DustParticle(new Vector3(x, y, vector.getEndZ()), color),player);
                }
            }else{
                level.addParticle(new DustParticle(new Vector3(vector.getStartX(), y, vector.getStartZ()), color),player);
                level.addParticle(new DustParticle(new Vector3(vector.getEndX(), y, vector.getStartZ()), color),player);
                level.addParticle(new DustParticle(new Vector3(vector.getEndX(), y, vector.getEndZ()), color),player);
                level.addParticle(new DustParticle(new Vector3(vector.getStartX(), y, vector.getEndZ()), color),player);
            }

        }

    }


    public static LinkedHashMap<String,Object> getMapByBlockClass(LinkedList<BlockClass> classes){
        LinkedHashMap<String,Object> map = new LinkedHashMap<>();
        for(BlockClass blockClass : classes){
            Block block = blockClass.getBlock();
            String blockName;
            if(block.getDamage() == 0){
                blockName = block.getId()+"";
            }else{
                blockName = block.getId()+":"+block.getDamage();
            }
            map.put(blockName,blockClass.getSpawnRation());
        }
        return map;
    }
    /** 获取坐标是否允许PVP*/
    public static AreaClass getCanPVPArea(Location position){
       return getDefaultArea(position,3);
    }

    /** 获取点击位置是否在矿区*/
    public static String getPlayerTouchArea(Position position){
        AreaClass areaClass = getDefaultArea(position,0);
        if(areaClass != null){
            return areaClass.getName();
        }
        return null;
    }
    public static AreaClass getDefaultArea(Position position,int y){
        for(AreaClass areaClass:AreaMainClass.getInstance().areas.values()){
            Vector vector = areaClass.getPos().clone();
            vector.sort();
            if(position.level.getFolderName().equals(vector.getLevel().getFolderName())
                    && vector.getStartX() <= position.getFloorX() && vector.getEndX() >= position.getFloorX()
                    && vector.getStartY() <= position.getFloorY() && (vector.getEndY() + y) >= position.getFloorY()
                    && vector.getStartZ() <= position.getFloorZ() && vector.getEndZ() >= position.getFloorZ()){
                return areaClass;
            }
        }
        return null;
    }

    /** 获取玩家站立坐标是否在矿区*/
    public static String getPlayerInArea(Position position){
        AreaClass areaClass = getDefaultArea(position,2);
        if(areaClass != null){
            return areaClass.getName();
        }
        return null;
    }

    /***
     *  判断方块是否在矿区世界
     * */
    public static boolean canInAreaLevel(Position pos){
        for(AreaClass areaClass:AreaMainClass.getInstance().areas.values()){
            if(areaClass.getPos().getLevel().getFolderName().equals(pos.getLevel().getFolderName())){
                return true;
            }
        }
        return false;
    }



    /** 放烟花*/
    public static void spawnFirework(Player player) {

        Level level = player.getLevel();
        ItemFirework item = new ItemFirework();
        CompoundTag tag = new CompoundTag();
        Random random = new Random();
        CompoundTag ex = new CompoundTag();
        ex.putByteArray("FireworkColor",new byte[]{
                (byte) DyeColor.values()[random.nextInt(ItemFirework.FireworkExplosion.ExplosionType.values().length)].getDyeData()
        });
        ex.putByteArray("FireworkFade",new byte[0]);
        ex.putBoolean("FireworkFlicker",random.nextBoolean());
        ex.putBoolean("FireworkTrail",random.nextBoolean());
        ex.putByte("FireworkType",ItemFirework.FireworkExplosion.ExplosionType.values()
                [random.nextInt(ItemFirework.FireworkExplosion.ExplosionType.values().length)].ordinal());
        tag.putCompound("Fireworks",(new CompoundTag("Fireworks")).putList(new ListTag<CompoundTag>("Explosions").add(ex)).putByte("Flight",1));
        item.setNamedTag(tag);
        CompoundTag nbt = new CompoundTag();
        nbt.putList(new ListTag<DoubleTag>("Pos")
                .add(new DoubleTag("",player.x+0.5D))
                .add(new DoubleTag("",player.y+0.5D))
                .add(new DoubleTag("",player.z+0.5D))
        );
        nbt.putList(new ListTag<DoubleTag>("Motion")
                .add(new DoubleTag("",0.0D))
                .add(new DoubleTag("",0.0D))
                .add(new DoubleTag("",0.0D))
        );
        nbt.putList(new ListTag<FloatTag>("Rotation")
                .add(new FloatTag("",0.0F))
                .add(new FloatTag("",0.0F))

        );
        nbt.putCompound("FireworkItem", NBTIO.putItemHelper(item));
        EntityFirework entity = new EntityFirework(level.getChunk((int)player.x >> 4, (int)player.z >> 4), nbt);
        entity.spawnToAll();
    }



    public static LinkedList<double[]> showParticle(double s) {
        double x,z;
        LinkedList<double[]> pos = new LinkedList<>();
        //密度控制
        for(int i=0;i<=90;i+=3){
            x = s * Math.cos(Math.toRadians(i));
            z = s * Math.sin(Math.toRadians(i));
            pos.add(new double[]{x,2,z});
            pos.add(new double[]{-z,2,x});
            pos.add(new double[]{-x,2,-z});
            pos.add(new double[]{z,2,-x});
        }
        return pos;
    }

    public static LinkedList<AreaClass> sqrtAreaClass(){
        LinkedList<AreaClass> areaClass = new LinkedList<>();
        LinkedHashMap<String,Integer> map = new LinkedHashMap<>();
        for(AreaClass c:AreaMainClass.getInstance().areas.values()){
            map.put(c.getName(),c.getLevel());
        }
        List<Map.Entry<String,Integer>> list = new ArrayList<>(map.entrySet());
        list.sort(Map.Entry.comparingByValue());
        for(Map.Entry<String,Integer> mapping:list){
            areaClass.add(AreaClass.getAreaClass(mapping.getKey()));
        }
        return areaClass;
    }

    private static final Pattern PAT =  Pattern.compile("^[+]?([0-9]+(.[0-9]{1,2})?)$");
    /**
     * 判断是否为数字
     * */
    public static boolean isNumber(String source){
        boolean flag = false;
        try {
            if(PAT.matcher(source).matches()){
                flag = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 判断是否存在区域重复
     * */
    public static String checkOverlap(Vector vector){
        vector.sort();
        for(AreaClass overlap: AreaMainClass.getInstance().areas.values()){
            if(overlap.getPos().getLevel().getFolderName().equals(vector.getLevel().getFolderName())){
                if(vector.getStartX() <= overlap.getPos().getEndX()
                        && vector.getEndX() >= overlap.getPos().getStartX() &&
                    vector.getStartY() <= overlap.getPos().getEndY()
                        && vector.getEndY() >= overlap.getPos().getStartY() &&
                    vector.getStartZ() <= overlap.getPos().getEndZ()
                        && vector.getEndZ() >= overlap.getPos().getStartZ()){
                    return overlap.getName();
                }
            }
        }
        return null;

    }

    public static void sendMessage(Player player,String message){
        sendMessage(player, message,"",AreaMainClass.getInstance().getMessageType());
    }

    public static void sendMessage(Player player, String message, String sub, String type){
        switch(type){
            case "tip":
                player.sendTip(message+sub);
                break;
            case "popup":
                player.sendPopup(message+sub);
                break;
            case "action":
                player.sendActionBar(message+sub);
                break;
            case "title":
                if("".equals(sub)){
                   String[] strings = message.split("\\n");
                   String title = strings[0];
                   StringBuilder s = new StringBuilder();
                   if(strings.length > 1){
                       for(int i = 1;i < strings.length;i++){
                           s.append(strings[i]).append("\n");
                       }
                   }
                   player.sendTitle(title,s.toString());
                }else{
                    player.sendTitle(message,sub);
                }
                break;
            case "boss":
                BossApi api = new BossApi(player);
                api.createBossBar(message+sub);
                Server.getInstance().getScheduler().scheduleDelayedTask(new Task() {
                    @Override
                    public void onRun(int i) {
                        if(AreaMainClass.getInstance().bossMessage.containsKey(player)){
                            BossApi api = new BossApi(player);
                            api.remove();
                        }
                    }
                },40);
                break;
            case "msg":
                player.sendMessage(message+sub);
                break;
            default:break;
        }
    }




    public static LinkedHashMap<String,Object> getPosMap(Vector vector){
        return new LinkedHashMap<String,Object>(){
            {
                put("startX",vector.getStartX());
                put("startY",vector.getStartY());
                put("startZ",vector.getStartZ());
                put("endX",vector.getEndX());
                put("endY",vector.getEndY());
                put("endZ",vector.getEndZ());

            }
        };
    }

    public static LinkedHashMap<String,Object> getPosMap(int x,int y,int z){
        return new LinkedHashMap<String,Object>(){
            {
                put("x",x);
                put("y",y);
                put("z",z);

            }
        };
    }

    /**
     * 判断是否在刷新时间段
     * */
    public static boolean inTime(){
        if(AreaMainClass.getInstance().getTimeLoad().size() > 0){
            TimeZone timeZone = TimeZone.getTimeZone("GMT+8");
            SimpleDateFormat format = new SimpleDateFormat("HH:mm");
            format.setTimeZone(timeZone);
            int h,m,h1,m1;
            for(String fix:AreaMainClass.getInstance().getTimeLoad()){
                String[] lists = fix.split("-");
                h = Integer.parseInt(lists[0].trim().split(":")[0]);
                m = Integer.parseInt(lists[0].trim().split(":")[1]);
                h1 = Integer.parseInt(lists[1].trim().split(":")[0]);
                m1 = Integer.parseInt(lists[1].trim().split(":")[1]);
                try {
                    Date time1 = format.parse(h+":"+m);
                    Date time2 = format.parse(h1+":"+m1);
                    if(belongCalendar(format.parse(format.format(new Date())),time1,time2)){
                        return true;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
            return false;
        }else{
            return true;
        }
    }

    private static boolean belongCalendar(Date nowTime,Date beginTime,Date endTime){
        Calendar date = Calendar.getInstance();
        date.setTime(nowTime);

        Calendar begin = Calendar.getInstance();
        begin.setTime(beginTime);

        Calendar end = Calendar.getInstance();
        end.setTime(endTime);

        return date.after(begin) && date.before(end);
    }


    public static Position getPositionByMap(Map transfer, Level level) {
        return Position.fromObject(new Vector3((int)transfer.get("x"),(int) transfer.get("y"), (int)transfer.get("z")),level);
    }
}

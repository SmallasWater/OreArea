package ore.area.utils.player;


import cn.nukkit.block.Block;
import cn.nukkit.utils.Config;
import ore.area.AreaMainClass;
import ore.area.utils.area.AreaClass;
import ore.area.utils.area.DefaultBlockClass;


import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * @author SmallasWater
 */
public class PlayerClass {

    private String playerName;

    private LinkedList<String> keyAreas = new LinkedList<>();

    private LinkedHashMap<String,Integer> join;

    private LinkedHashMap<String,Integer> breakBlocks = new LinkedHashMap<>();

    private PlayerClass(String playerName){
        this.playerName = playerName;

    }

    private void setBreakBlocks(LinkedHashMap<String, Integer> breakBlocks) {
        this.breakBlocks = breakBlocks;
    }

    public int getBreakBlockCount(Block block){
        if(breakBlocks.containsKey(DefaultBlockClass.getBlockSaveString(block))){
            return breakBlocks.get(DefaultBlockClass.getBlockSaveString(block));
        }
        return 0;
    }
    public LinkedHashMap<String, Integer> getBreakBlocks() {
        return breakBlocks;
    }

    private void setKeyAreas(LinkedList<String> keyAreas) {
        this.keyAreas = keyAreas;
    }

    public LinkedList<String> getKeyAreas() {
        return keyAreas;
    }

    public boolean canKey(String area){
        return keyAreas.contains(area);
    }

    public void addCount(Block block){
        if(AreaMainClass.getInstance().defaultBlocks.containsKey(DefaultBlockClass.getBlockSaveString(block))){
            if(breakBlocks.containsKey(DefaultBlockClass.getBlockSaveString(block))){
                breakBlocks.put(DefaultBlockClass.getBlockSaveString(block),breakBlocks.get(DefaultBlockClass.getBlockSaveString(block)) + 1);
            }else{
                breakBlocks.put(DefaultBlockClass.getBlockSaveString(block),1);
            }
        }

    }

    public void addKey(String area){
        keyAreas.add(area);
    }

    public String getPlayerName() {
        return playerName;
    }

    public static PlayerClass getPlayerClass(String playerName){
        if(!AreaMainClass.getInstance().playerClasses.containsKey(playerName)){
            PlayerClass playerClass = new PlayerClass(playerName);
            Config config = AreaMainClass.getInstance().getPlayerConfig(playerName);
            playerClass.setKeyAreas(new LinkedList<>(config.getStringList("area")));
            LinkedHashMap<String,Integer> breaks = new LinkedHashMap<>();
            Map map = config.get("breaks",new LinkedHashMap<String,Integer>());
            if(map.size() > 0){
                for(Object s:map.keySet()){
                    breaks.put(s.toString(), (int) map.get(s));
                }
            }
            playerClass.setBreakBlocks(breaks);
            Map map1 = (Map) config.get("join");
            LinkedHashMap<String,Integer> maps = new LinkedHashMap<>();
            for(Object objName:map1.keySet()){
                maps.put(objName.toString(), (Integer) map1.get(objName));
            }
            playerClass.setJoin(maps);
            AreaMainClass.getInstance().playerClasses.put(playerName,playerClass);
        }
        return AreaMainClass.getInstance().playerClasses.get(playerName);

    }

    private void setJoin(LinkedHashMap<String, Integer> join) {
        this.join = join;
    }

    public void setJoinCount(String areaName,int count) {
        join.put(areaName,count);
    }

    public int getJoin(String areaName) {
        AreaClass areaClass = AreaClass.getAreaClass(areaName);
        if(areaClass == null){
            return 0;
        }
        if(!join.containsKey(areaName)){
            if(areaClass.getJoinCount() != -1) {
                join.put(areaName, areaClass.getJoinCount());
            }else{
                join.put(areaName, 0);
            }
        }
        return join.get(areaName);
    }

    public int getMaxAreaLevel(){
        int i = 1;
        for(String a:keyAreas){
            AreaClass areaClass = AreaClass.getAreaClass(a);
            if(areaClass != null){
                if (areaClass.getLevel() > i){
                    i = areaClass.getLevel();
                }
            }
        }
        return i;
    }

    public AreaClass getMaxLevelArea(){
        int i = getMaxAreaLevel();
        for(String a:keyAreas){
            AreaClass areaClass = AreaClass.getAreaClass(a);
            if(areaClass != null){
                if (areaClass.getLevel() == i){
                    return areaClass;
                }
            }
        }
        return null;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof PlayerClass){
            return ((PlayerClass) obj).getPlayerName().equals(playerName);
        }
        return false;
    }

    public void save(){
        Config cn = AreaMainClass.getInstance().getPlayerConfig(playerName);
        cn.set("area",keyAreas);
        cn.set("breaks",breakBlocks);
        cn.set("join",join);
        cn.save();
    }


}

package ore.area.utils.player;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.utils.Config;
import ore.area.AreaMainClass;
import ore.area.utils.area.AreaClass;
import ore.area.utils.area.DefaultBlockClass;


import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

public class PlayerClass {

    private Player player;

    private LinkedList<String> keyAreas;

    private LinkedHashMap<String,Integer> breakBlocks = new LinkedHashMap<>();

    private PlayerClass(Player player){
        this.player = player;

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

    public Player getPlayer() {
        return player;
    }

    public static PlayerClass getPlayerClass(Player player){
        if(AreaMainClass.getInstance().playerClasses.containsKey(player)){
            return AreaMainClass.getInstance().playerClasses.get(player);
        }else{
            PlayerClass playerClass = new PlayerClass(player);
            Config config = AreaMainClass.getInstance().getPlayerConfig(player.getName());
            playerClass.setKeyAreas(new LinkedList<>(config.getStringList("area")));
            LinkedHashMap<String,Integer> breaks = new LinkedHashMap<>();
            Map map = config.get("breaks",new LinkedHashMap<String,Integer>());
            if(map.size() > 0){
                for(Object s:map.keySet()){
                    breaks.put(s.toString(), (int) map.get(s));
                }
            }
            playerClass.setBreakBlocks(breaks);
            return playerClass;
        }

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

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof PlayerClass){
            return ((PlayerClass) obj).getPlayer().getName().equals(getPlayer().getName());
        }
        return false;
    }

    public void save(){
        Config cn = AreaMainClass.getInstance().getPlayerConfig(player.getName());
        cn.set("area",keyAreas);
        cn.set("breaks",breakBlocks);
        cn.save();
    }
}

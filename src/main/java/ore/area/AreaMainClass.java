package ore.area;

import cn.nukkit.Achievement;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.level.Position;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;
import ore.area.commands.OreCommand;
import ore.area.utils.*;
import ore.area.utils.area.AreaClass;
import ore.area.utils.area.DefaultBlockClass;
import ore.area.utils.player.PlayerClass;
import ore.area.utils.task.AreaLoadTask;
import ore.area.utils.task.BossBarMessageTask;
import ore.area.utils.task.PlayerUseTimeTask;
import ore.area.windows.ListenerWindow;
import tip.utils.Api;
import updata.AutoData;

import java.io.File;
import java.util.*;

/**
 * @author 若水
 */
public class AreaMainClass extends PluginBase {

    private static final String PLAYER_NAME = "/Player";
    private static final String AREA_NAME = "/Area";
    private static final String LANGUAGE = "/language.yml";
    private static final String BLOCKS = "/blocks.yml";
    private static AreaMainClass instance;

    private LoadMoney money;

    private LinkedHashMap<String,Config> playerConfig = new LinkedHashMap<>();

    private static Config language;

    private static Config blocks;

    private boolean canSendInventory;

    public LinkedHashMap<String, AreaClass> areas = new LinkedHashMap<>();

    public LinkedHashMap<String,LinkedHashMap<String,Integer>> useTime = new LinkedHashMap<>();

    public LinkedHashMap<Player,AreaClass> clickArea = new LinkedHashMap<>();

    public LinkedHashMap<String,String> canJoin = new LinkedHashMap<>();

    public LinkedHashMap<Player, BossBar> bossMessage = new LinkedHashMap<>();


    public LinkedList<String> transfer = new LinkedList<>();

    private LinkedList<String> timeLoad = new LinkedList<>();

    public LinkedHashMap<String,PlayerClass> playerClasses = new LinkedHashMap<>();

    public LinkedHashMap<String,DefaultBlockClass> defaultBlocks = new LinkedHashMap<>();

    public static LinkedHashMap<String, Integer > timer = new LinkedHashMap<>();

    public LinkedHashMap<Player,LinkedList< Position>> pos = new LinkedHashMap<>();




    @Override
    public void onEnable() {
        if(Server.getInstance().getPluginManager().getPlugin("AutoUpData") != null){
            if(AutoData.defaultUpData(this,getFile(),"SmallasWater","OreArea")){
                return;
            }
        }
        instance = this;
        if(Server.getInstance().getPluginManager().getPlugin("Tips") != null){
            Api.registerVariables("orearea", OreAreaVariable.class);
        }
        saveDefaultConfig();
        reloadConfig();
        init();
        getServer().getCommandMap().register("OreArea", new OreCommand(this));
        this.getServer().getPluginManager().registerEvents(new ListenerWindow(),this);
        for(DefaultBlockClass defaultBlockClass:defaultBlocks.values()){
            LinkedHashMap<Integer,LinkedList<String>> success = defaultBlockClass.getSuccess();
            for(Integer i:success.keySet()){
                Block block = defaultBlockClass.getBlock();
                Achievement.add(i+DefaultBlockClass.getBlockSaveString(block),new Achievement("§d>>§a破坏 §e"+i+" §a次 "
                        + ItemIDSunName.getIDByName(block.getId(),block.getDamage())+" §d<<"));
            }

        }
        this.getServer().getScheduler().scheduleRepeatingTask(new PlayerUseTimeTask(this),20);
        this.getServer().getScheduler().scheduleRepeatingTask(new AreaLoadTask(this),20);
        this.getServer().getPluginManager().registerEvents(new ListenerEvents(),this);
        if("boss".equals(getMessageType())){
            this.getServer().getScheduler().scheduleRepeatingTask(new BossBarMessageTask(this),20);
        }
        money = new LoadMoney();
    }

    public void init(){
        if(!new File(this.getDataFolder()+AREA_NAME).exists()){
            if(!new File(this.getDataFolder()+AREA_NAME).mkdirs()){
                this.getLogger().info("创建文件夹"+AREA_NAME+"失败....");
            }
        }
        if(!new File(this.getDataFolder()+PLAYER_NAME).exists()){
            if(!new File(this.getDataFolder()+PLAYER_NAME).mkdir()){
                this.getLogger().info("创建文件夹"+PLAYER_NAME+"失败");
            }
        }
        if(!new File(this.getDataFolder()+LANGUAGE).exists()){
            this.saveResource("language.yml");
        }
        if(!new File(this.getDataFolder()+BLOCKS).exists()){
            this.saveResource("blocks.yml");
        }
        saveDefaultConfig();
        reloadConfig();
        timeLoad = new LinkedList<>();
        canSendInventory = getConfig().getBoolean("掉落物是否直接发送背包",true);
        language = new Config(this.getDataFolder()+LANGUAGE,Config.YAML);
        blocks = new Config(this.getDataFolder()+BLOCKS,Config.YAML);
        timeLoad.addAll(getConfig().getStringList("刷新矿区时间段"));
        readAllFiles();
        readAllPlayer();
        readAllBlocks();
    }

    boolean isCanSendInventory() {
        return canSendInventory;
    }

    public LinkedList<String> getTimeLoad() {
        return timeLoad;
    }

    public static String getLang(String lang) {
        return language.getString(lang);
    }

    public static String getLang(String lang,String defaultValue) {
        return language.getString(lang,defaultValue);
    }

    public File getAreaFile(String name){
        return new File(this.getDataFolder()+AREA_NAME+"/"+name+".yml");
    }

    public LoadMoney getMoney() {
        return money;
    }

    private File getPlayerFile(String name){
        return new File(this.getDataFolder()+PLAYER_NAME+"/"+name+".yml");
    }

    public static AreaMainClass getInstance() {
        return instance;
    }

    public static Config getBlocksConfig() {
        return blocks;
    }

    public Config getPlayerConfig(String name){
        if(!playerConfig.containsKey(name)){
            if(!getPlayerFile(name).exists()){
                this.saveResource("player.yml",PLAYER_NAME+"/"+name+".yml",false);
            }
            playerConfig.put(name,new Config(getPlayerFile(name),Config.YAML));
        }
        return playerConfig.get(name);
    }

    private void readAllPlayer(){
        playerConfig = new LinkedHashMap<>();
        for(String name:getPlayerFiles()){
            playerConfig.put(name, new Config(getPlayerFile(name),Config.YAML));
        }
    }

    private void readAllBlocks(){
        defaultBlocks = new LinkedHashMap<>();
        Map<String,Object> maps = getBlocksConfig().getAll();
        for(String s:maps.keySet()){
            defaultBlocks.put(s,DefaultBlockClass.getInstance(s, (Map) maps.get(s)));
        }
    }

    private void readAllFiles(){
        areas = new LinkedHashMap<>();
        for(String name:getAreaFiles()){
            AreaClass areaClass = AreaClass.getClassByConfig(name,new Config(AreaMainClass.getInstance().getAreaFile(name),Config.YAML));
            if(areaClass != null){
                areas.put(name,areaClass);

            }
        }
        sort();
    }

    public void sort(){
        if(areas.size() > 0){
            LinkedHashMap<String,Integer> linkedHashMap = new LinkedHashMap<>();
            for(AreaClass areaClass:areas.values()){
                linkedHashMap.put(areaClass.getName(),areaClass.getLevel());
            }
            linkedHashMap = Tools.getPlayerRankingList(linkedHashMap);
            LinkedHashMap<String,AreaClass> map = new LinkedHashMap<>();
            for (String name:linkedHashMap.keySet()){
                map.put(name,areas.get(name));
            }
            this.areas = map;
        }
    }

    private String[] getPlayerFiles(){
        return getDefaultFiles(PLAYER_NAME);
    }

    private String[] getDefaultFiles(String playerName) {
        List<String> names = new ArrayList<>();
        File files = new File(this.getDataFolder()+ playerName);
        if(files.isDirectory()){
            File[] filesArray = files.listFiles();
            if(filesArray != null){
                if(filesArray.length>0){
                    for(File file : filesArray){
                        names.add( file.getName().substring(0, file.getName().lastIndexOf(".")));
                    }
                }
            }
        }
        return names.toArray(new String[0]);
    }

    private String[] getAreaFiles(){
        return getDefaultFiles(AREA_NAME);
    }

    /**
     * 获取是否进入矿区提示
     * */
    boolean isSendJoinMessage(){
        return getConfig().getBoolean("进入矿区是否提示");
    }

    /**
     * 获取是否离开矿区提示
     * */
    boolean isSendQuitMessage(){
        return getConfig().getBoolean("离开矿区是否提示");
    }

    /**
     * 获取是否开启购买解锁
     * */
    public boolean isKeyCanOpen(){
        return getConfig().getBoolean("是否开启玩家购买解锁");
    }

    /**
     * 获取是否显示传送粒子
     * */
    boolean canShowTransfer(){
        return getConfig().getBoolean("是否显示粒子");
    }

    /**
     * 获取是否保护矿区世界
     * */
    boolean canProtectionLevel(){
        return getConfig().getBoolean("是否保护矿区",false);
    }



    /**
     * 获取传送时间
     * */
    int getTransferTime(){
        return getConfig().getInt("传送时间");
    }

    /**
     * 获取玩家传送矿区是否提示
     * */
    public boolean canSendTransferMessage(){
        return getConfig().getBoolean("玩家传送到矿区是否提示");
    }

    /**
     * 获取玩家传送矿区是否全服公告
     * */
    public boolean canSendTransferBroadCastMessage(){
        return getConfig().getBoolean("玩家传送矿区是否全服公告");
    }


    /**
     * 获取解锁矿区是否公告
     * */
    boolean canSendkeyArea(){
        return getConfig().getBoolean("玩家解锁矿区是否公告");
    }


    /**
     * 获取全部成就
     * */
    public LinkedList<String> getAchievements(){
        LinkedList<String> achievements = new LinkedList<>();
        for(DefaultBlockClass defaultBlockClass:defaultBlocks.values()){
            for(int saveId:defaultBlockClass.getSuccess().keySet()) {
                boolean achievement = Achievement.achievements.containsKey(
                        saveId+DefaultBlockClass.getBlockSaveString(defaultBlockClass.getBlock()));
                if(achievement){
                    achievements.add(saveId+DefaultBlockClass.getBlockSaveString(defaultBlockClass.getBlock()));
                }
            }
        }
        return achievements;
    }


    /**
     * 获取显示类型
     * */
    public String getMessageType(){
        return getConfig().getString("提示类型").toLowerCase();
    }

    /**
     * 获取显示类型
     * */
    public String getTransferMessageType(){
        return getConfig().getString("玩家传送到矿区提示").toLowerCase();
    }


    @Override
    public void onDisable() {
        for(AreaClass areaClass:areas.values()){
            areaClass.save();
        }
        for(PlayerClass playerClass:playerClasses.values()){
            playerClass.save();
        }
    }
}

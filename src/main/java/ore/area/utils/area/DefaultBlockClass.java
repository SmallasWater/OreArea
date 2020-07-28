package ore.area.utils.area;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemPickaxeDiamond;
import cn.nukkit.utils.Config;
import ore.area.AreaMainClass;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author SmallasWater
 */
public class DefaultBlockClass {
    private Block block;

    private double sellMoney;

    /**
     * 1:
     *   sellmoney: 0.2
     *   success:
     *     1000: []*/
    private LinkedHashMap<Integer, LinkedList<String>> success = new LinkedHashMap<>();

    private DefaultBlockClass(Block block, double sellMoney){
        this.block = block;
        this.sellMoney = sellMoney;
    }

    public static void createDefaultBlock(Block block, double sellMoney){
        DefaultBlockClass blockClass = new DefaultBlockClass(block,sellMoney);
        if(!AreaMainClass.getInstance().defaultBlocks.containsKey(DefaultBlockClass.getBlockSaveString(block))){
            AreaMainClass.getInstance().defaultBlocks.put(DefaultBlockClass.getBlockSaveString(block),blockClass);
        }else{
           blockClass =  AreaMainClass.getInstance().defaultBlocks.get(DefaultBlockClass.getBlockSaveString(block));
           blockClass.setSellMoney(sellMoney);
        }

        blockClass.save();
    }

    public static DefaultBlockClass getInstance(Block block){
        for(DefaultBlockClass defaultBlocks:AreaMainClass.getInstance().defaultBlocks.values()){
            if(DefaultBlockClass.getBlockSaveString(defaultBlocks.block).equals(DefaultBlockClass.getBlockSaveString(block))){
                return defaultBlocks;
            }
        }
        return null;
    }
    public void addSuccess(int count){
        addSuccess(count,new LinkedList<>());
    }

    public void addSuccess(int count,LinkedList<String> cmds){
        success.put(count,cmds);
        save();
    }

    public static Block getBlockByString(String id){
        Block block;
        if(id.split(":").length > 1){
            block = Block.get(Integer.parseInt(id.split(":")[0]),Integer.parseInt(id.split(":")[1]));
        }else{
            block = Block.get(Integer.parseInt(id.split(":")[0]),0);
        }
        return block;
    }

    public static DefaultBlockClass getInstance(String id,Map map){
        Block block = getBlockByString(id);
        double sellMoney = (double) map.get("sellmoney");
        DefaultBlockClass blockClass = new DefaultBlockClass(block,sellMoney);
        LinkedHashMap<Integer,LinkedList<String>> success = new LinkedHashMap<>();
        Map map1 = (Map) map.get("success");
        if(map1.size() > 0){
            List list;
            for (Object o:map1.keySet()){
                list = (List) map1.get(o);
                success.put(Integer.parseInt(o.toString()),toStringList(list));
            }
            blockClass.setSuccess(success);
        }
        return blockClass;
    }

    public static LinkedList<String> getSuccessByCount(Block block,int count){
        LinkedList<String> linkedList = null;
        DefaultBlockClass blockClass = DefaultBlockClass.getInstance(block);
        if(blockClass != null){
            if(blockClass.success.containsKey(count)){
                linkedList = blockClass.success.get(count);
            }
        }
        return linkedList;
    }

    private static LinkedList<String> toStringList(List list){
        LinkedList<String> linkedList = new LinkedList<>();
        for (Object o:list){
            linkedList.add(o.toString());
        }
        return linkedList;
    }

    public Block getBlock() {
        return block;
    }

    public Item getItem(){
        Item item = getBlock().getDrops(new ItemPickaxeDiamond())[0];
        item.setCount(1);
        return item;
    }

    public double getSellMoney() {
        return sellMoney;
    }

    public static double getMoneyByItem(Item block){
        double money = 0.0D;
        for(DefaultBlockClass blockClass:AreaMainClass.getInstance().defaultBlocks.values()){
            Item item = blockClass.getItem();
            if(item.equals(block,true,false)){
                money = blockClass.sellMoney;
            }
        }
        return money;
    }

    public void setSellMoney(double sellMoney) {
        this.sellMoney = sellMoney;
    }

    public LinkedHashMap<Integer, LinkedList<String>> getSuccess() {
        return success;
    }

    public void setSuccess(LinkedHashMap<Integer, LinkedList<String>> success) {
        this.success = success;
        save();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof DefaultBlockClass){
            Block block = ((DefaultBlockClass) obj).getBlock();
            return ((block.getId()+":"+block.getDamage()).equals(this.block.getId()+":"+this.block.getDamage()));
        }
        return false;
    }

    public static String getBlockSaveString(Block block){
        if(block.getDamage() != 0){
            return block.getId()+":"+block.getDamage();
        }
        return block.getId()+"";
    }

    public void save(){
        Config config = AreaMainClass.getBlocksConfig();
        LinkedHashMap<String,Object> save = new LinkedHashMap<>();
        save.put("sellmoney",sellMoney);
        save.put("success",getSuccessToSave());
        config.set(getBlockSaveString(block),save);
        config.save();
    }

    private LinkedHashMap<String,Object> getSuccessToSave(){
        LinkedHashMap<String,Object> o = new LinkedHashMap<>();
        for(Integer i:success.keySet()){
            o.put(i+"",success.get(i));
        }
        return o;
    }

    public static double getPlayerInventorySellMoney(Player player){
        double money = 0.0D;
        for (Item item:player.getInventory().getContents().values()){
            for(DefaultBlockClass blockClass:AreaMainClass.getInstance().defaultBlocks.values()){
                if(item.equals(blockClass.getItem(),true,false)){
                    money += blockClass.getSellMoney() * item.getCount();
                    player.getInventory().removeItem(item);
                }
            }
        }
        return money;
    }


}

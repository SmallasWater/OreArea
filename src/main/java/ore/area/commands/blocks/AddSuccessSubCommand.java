package ore.area.commands.blocks;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.command.CommandSender;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBlock;
import ore.area.AreaMainClass;
import ore.area.commands.SubCommand;
import ore.area.utils.area.DefaultBlockClass;

/**
 * @author SmallasWater
 */
public class AddSuccessSubCommand extends SubCommand {
    public AddSuccessSubCommand(AreaMainClass plugin) {
        super(plugin);
    }

    @Override
    public boolean canUse(CommandSender sender) {
        return sender.hasPermission("ore.area.kq.success") && sender.isPlayer();
    }

    @Override
    public String getName() {
        return "success";
    }

    @Override
    public String[] getAliases() {
        return new String[0];
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if(args.length > 1){
            int c = Integer.parseInt(args[1]);
            Item item = ((Player) sender).getInventory().getItemInHand();
            if(item.getId() != 0){
                Block block = ItemBlock.get(item.getId(),item.getDamage()).getBlock();
                if(block.getId() != 0){
                    if(AreaMainClass.getInstance().defaultBlocks.containsKey(DefaultBlockClass.getBlockSaveString(item))){
                        DefaultBlockClass defaultBlockClass = AreaMainClass.getInstance().defaultBlocks.get(DefaultBlockClass.getBlockSaveString(item));
                        defaultBlockClass.addSuccess(c);
                    }else{
                        DefaultBlockClass.createDefaultBlock(item,0.0D);
                        DefaultBlockClass defaultBlockClass = AreaMainClass.getInstance().defaultBlocks.get(DefaultBlockClass.getBlockSaveString(item));
                        defaultBlockClass.addSuccess(c);

                    }
                    sender.sendMessage("§e>> §a成功设置物品: (ID: "+item.getId()+") 成就条件: "+c);
                }else{
                    sender.sendMessage("§e>> §c请手持方块!!");
                }
            }else{
                sender.sendMessage("§e>> §c请不要手持空气");
            }

        }
        return false;
    }

    @Override
    public String getHelp() {
        return "§a/kq success <数量> §7将手中的方块id设置为破坏次数成就";
    }

    @Override
    public String helpMessage() {
        return " §7将手中的方块id设置为破坏次数成就 设置成功后将记录玩家破坏此方块的次数 \n§c条件: [需设置破坏次数,需手持方块,在游戏内执行] §2权限组: (ore.area.kq.success)";
    }
}

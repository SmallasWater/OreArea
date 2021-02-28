package ore.area.commands.blocks;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.command.CommandSender;
import cn.nukkit.item.Item;
import ore.area.AreaMainClass;
import ore.area.commands.SubCommand;
import ore.area.utils.area.DefaultBlockClass;

/**
 * @author SmallasWater
 */
public class CreateBlockSubCommand extends SubCommand {
    public CreateBlockSubCommand(AreaMainClass plugin) {
        super(plugin);
    }

    @Override
    public boolean canUse(CommandSender sender) {
        return sender.hasPermission("ore.area.kq.cb") && sender.isPlayer();
    }

    @Override
    public String getName() {
        return "cb";
    }

    @Override
    public String[] getAliases() {
        return new String[0];
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (sender instanceof Player){
            if (args.length > 1){
                double m = Double.parseDouble(args[1]);
                Item item = ((Player) sender).getInventory().getItemInHand();
                if(item.getId() != 0){
                    DefaultBlockClass.createDefaultBlock(item,m);
                    sender.sendMessage("§e>> §a成功将物品: (ID: "+item.getId()+") 设置价格: "+m);

                }else{
                    sender.sendMessage("§e>> §c请不要手持空气");
                }
            }
        }

        return false;
    }

    @Override
    public String getHelp() {
        return "§a/kq cb <金钱> §7设置手持物品的回收价格";
    }

    @Override
    public String helpMessage() {
        return " §7设置手持物品(方块)§l掉落物§r§7的回收价格  \n§c条件: [需设置价格,需手持物品,在游戏内执行] §2权限组: (ore.area.kq.cb)";
    }
}

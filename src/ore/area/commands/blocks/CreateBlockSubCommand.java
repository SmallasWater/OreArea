package ore.area.commands.blocks;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.command.CommandSender;
import cn.nukkit.item.Item;
import ore.area.AreaMainClass;
import ore.area.commands.SubCommand;
import ore.area.utils.area.DefaultBlockClass;

public class CreateBlockSubCommand extends SubCommand {
    public CreateBlockSubCommand(AreaMainClass plugin) {
        super(plugin);
    }

    @Override
    public boolean canUse(CommandSender sender) {
        return sender.isOp() && sender.isPlayer();
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
                    Block block = Block.get(item.getId(),item.getDamage());
                    if(block != null){
                        DefaultBlockClass.createDefaultBlock(block,m);
                        sender.sendMessage("§e>> §a成功将方块: (ID: "+block.getId()+") 设置价格: "+m);
                    }else{
                        sender.sendMessage("§e>> §c请手持方块!!");
                    }
                }else{
                    sender.sendMessage("§e>> §c请不要手持空气");
                }
            }
        }

        return false;
    }
}

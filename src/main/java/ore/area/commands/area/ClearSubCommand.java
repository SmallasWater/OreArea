package ore.area.commands.area;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import ore.area.AreaMainClass;
import ore.area.commands.SubCommand;
import ore.area.utils.area.AreaClass;

/**
 * @author SmallasWater
 */
public class ClearSubCommand extends SubCommand {
    public ClearSubCommand(AreaMainClass plugin) {
        super(plugin);
    }

    @Override
    public boolean canUse(CommandSender sender) {
        return sender.hasPermission("ore.area.kq.clear");
    }

    @Override
    public String getName() {
        return isChinese()?"清空":"clean";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"clean","clear"};
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if(args.length > 1){
            String areaName = args[1];
            AreaClass areaClass = AreaClass.getAreaClass(areaName);
            if(areaClass != null){
                areaClass.cleanBlock();
                sender.sendMessage(isChinese()?"§e>> §a"+areaName+"矿区已清空!":"§e>> §a"+areaName+"OreArea is Clean!");
            }else{
                sender.sendMessage(isChinese()?"§e>> §c抱歉,"+areaName+"矿区不存在..":"§e>> §cSorry,"+areaName+"OreArea is not exists..");
            }
        }
        return false;
    }

    @Override
    public String getHelp() {
        return "§a/kq clean <"+(isChinese()?"矿区名称":"name")+">";
    }

    @Override
    public String helpMessage() {
        return isChinese()?"§7清空矿区内的所有方块 \n§c条件: [必须保证矿区存在] §2权限组: (ore.area.kq.clear)":"§7Clean all blocks for OreArea \n§cterm: [The existence of mining area must be ensured] §2permissions: (ore.area.kq.clear)";
    }
}

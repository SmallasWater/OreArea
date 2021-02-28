package ore.area.commands.setting;

import cn.nukkit.command.CommandSender;
import ore.area.AreaMainClass;
import ore.area.commands.SubCommand;
import ore.area.utils.area.AreaClass;

/**
 * @author SmallasWater
 */
public class PvpSubCommand extends SubCommand {
    public PvpSubCommand(AreaMainClass plugin) {
        super(plugin);
    }

    @Override
    public boolean canUse(CommandSender sender) {
        return sender.hasPermission("ore.area.kq.pvp");
    }

    @Override
    public String getName() {
        return "pvp";
    }

    @Override
    public String[] getAliases() {
        return new String[0];
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if(args.length == 2){
            String name = args[1];
            AreaClass areaClass = AreaClass.getAreaClass(name);
            if(areaClass != null){
                areaClass.setCanPVP(!areaClass.isCanPVP());
                sender.sendMessage("§e>> §d矿区"+name+"的PVP限制"+(areaClass.isCanPVP()?" §a已关闭":" §c已开启"));
                return true;
            }else{
                sender.sendMessage("§e>> §c不存在"+name+"矿区");
                return true;
            }
        }

        return false;
    }

    @Override
    public String getHelp() {
        return "§a/kq pvp <矿区名称> §7设置是否允许玩家在矿区内PVP";
    }

    @Override
    public String helpMessage() {
        return "§7设置是否允许玩家在矿区内PVP \n§c条件: [需设置矿区名] §2权限组: (ore.area.kq.pvp)";
    }
}

package ore.area.commands.setting;

import cn.nukkit.command.CommandSender;
import ore.area.AreaMainClass;
import ore.area.commands.SubCommand;
import ore.area.utils.area.AreaClass;

/**
 * @author SmallasWater
 */
public class SetUseTimeSubCommand extends SubCommand {
    public SetUseTimeSubCommand(AreaMainClass plugin) {
        super(plugin);
    }

    @Override
    public boolean canUse(CommandSender sender) {
        return sender.hasPermission("ore.area.kq.setUseTime");
    }

    @Override
    public String getName() {
        return "setut";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"sut"};
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if(args.length == 3){
            String name = args[1];
            int time = -1;
            try {
                time = Integer.parseInt(args[2]);
            }catch (Exception ignore){ }
            AreaClass a1 = AreaClass.getAreaClass(name);
            if(a1 != null){
                a1.setUseTime(time);
                sender.sendMessage("§e>> §b成功设置§2"+name+"§b矿区 的玩家使用时间为 §7:"
                        +(time == -1?"§c无限制":time+"秒"));
                return true;
            }else{
                sender.sendMessage("§e>> §c抱歉，不存在"+name+"矿区");
                return true;
            }
        }

        return false;
    }

    @Override
    public String getHelp() {
        return "§a/kq setut <矿区名称> <使用时间(秒) -1为永久> §7设置玩家在矿区的使用时间";
    }
}

package ore.area.commands.setting;

import cn.nukkit.command.CommandSender;
import ore.area.AreaMainClass;
import ore.area.commands.SubCommand;
import ore.area.utils.Tools;
import ore.area.utils.area.AreaClass;

public class SetLastSubCommand extends SubCommand {

    public SetLastSubCommand(AreaMainClass plugin) {
        super(plugin);
    }

    @Override
    public boolean canUse(CommandSender sender) {
        return sender.hasPermission("ore.area.kq.setlast");
    }

    @Override
    public String getName() {
        return "last";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"setlast","setLast"};
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {

            if(args.length == 3){
                String name = args[1];
                String last = args[2];
                AreaClass a1 = AreaClass.getAreaClass(name);
                if(a1 != null){
                    a1.setLastArea(last);
                    a1.save();
                    sender.sendMessage("§e>> §b成功设置"+name+"矿区的前置矿区为"+last);
                    return true;
                }else{
                    sender.sendMessage("§e>> §c抱歉，不存在"+name+"矿区");
                    return true;
                }
            }

        return false;
    }
}

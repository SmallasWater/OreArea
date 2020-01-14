package ore.area.commands.area;

import cn.nukkit.command.CommandSender;
import ore.area.AreaMainClass;
import ore.area.commands.SubCommand;
import ore.area.utils.area.AreaClass;

public class ClearSubCommand extends SubCommand {
    public ClearSubCommand(AreaMainClass plugin) {
        super(plugin);
    }

    @Override
    public boolean canUse(CommandSender sender) {
        return sender.isOp();
    }

    @Override
    public String getName() {
        return "清空";
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
                sender.sendMessage("§e>> §a"+areaName+"矿区已清空!");
            }else{
                sender.sendMessage("§e>> §c抱歉,"+areaName+"矿区不存在..");
            }
        }
        return false;
    }
}

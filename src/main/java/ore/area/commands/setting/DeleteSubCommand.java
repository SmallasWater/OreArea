package ore.area.commands.setting;

import cn.nukkit.command.CommandSender;
import ore.area.AreaMainClass;
import ore.area.commands.SubCommand;
import ore.area.utils.area.AreaClass;

/**
 * @author SmallasWater
 */
public class DeleteSubCommand  extends SubCommand {
    public DeleteSubCommand(AreaMainClass plugin) {
        super(plugin);
    }

    @Override
    public boolean canUse(CommandSender sender) {
        return sender.hasPermission("ore.area.kq.delete");
    }

    @Override
    public String getName() {
        return "delete";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"删除"};
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if(args.length == 2){
            String name = args[1];
            AreaClass areaClass = AreaClass.getAreaClass(name);
            if(areaClass != null){
                areaClass.delete();
                sender.sendMessage("§e>> §a矿区"+name+"删除成功");
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
        return "§a/kq delete <矿区名称> §7删除矿区";
    }
}

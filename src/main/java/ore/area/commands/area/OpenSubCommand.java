package ore.area.commands.area;


import cn.nukkit.command.CommandSender;
import ore.area.AreaMainClass;
import ore.area.commands.SubCommand;
import ore.area.utils.area.AreaClass;

/**
 * @author 若水
 */
public class OpenSubCommand extends SubCommand {
    public OpenSubCommand(AreaMainClass plugin) {
        super(plugin);
    }


    @Override
    public boolean canUse(CommandSender sender) {
        return sender.hasPermission("ore.area.kq.open");
    }


    @Override
    public String getName() {
        return "开启";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"open"};
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if(args.length > 1){
            String name = args[1];
            AreaClass aClass = AreaClass.getAreaClass(name);
            if(aClass != null){
                aClass.setKey(true);
                sender.sendMessage("§e>> §b"+name+"矿区成功开启..");
            }else{
                sender.sendMessage("§e>> §c抱歉,"+name+"矿区不存在..");
            }
        }else{
            return false;
        }
        return true;
    }

    @Override
    public String getHelp() {
        return "§a/kq open <矿区名称> §7开放一个矿区 ";
    }

    @Override
    public String helpMessage() {
        return " §7开放一个矿区 让玩家可以重新使用矿区 \n§c条件: [必须保证矿区存在§c] §2权限组: (ore.area.kq.open)";
    }
}

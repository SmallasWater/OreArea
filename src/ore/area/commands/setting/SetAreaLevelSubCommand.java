package ore.area.commands.setting;

import cn.nukkit.command.CommandSender;
import ore.area.AreaMainClass;
import ore.area.commands.SubCommand;
import ore.area.utils.Tools;
import ore.area.utils.area.AreaClass;


public class SetAreaLevelSubCommand extends SubCommand {

    public SetAreaLevelSubCommand(AreaMainClass plugin) {
        super(plugin);
    }

    @Override
    public boolean canUse(CommandSender sender) {
        return sender.hasPermission("ore.area.kq.setLevel");
    }

    @Override
    public String getName() {
        return "setLevel";
    }

    @Override
    public String[] getAliases() {
        return new String[0];
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        try{
            if(args.length == 3){
                String name = args[1];
                if(Tools.isNumber(args[2])){
                    int i = Integer.parseInt(args[2]);
                    AreaClass areaClass = AreaClass.getAreaClass(name);
                    if(areaClass == null){
                        sender.sendMessage("§e>> §c抱歉，不存在"+name+"矿区");
                        return true;
                    }else{
                       if(AreaClass.getAreaClassByLevel(i) == null){
                           areaClass.setLevel(i);
                           sender.sendMessage("§e>> §b成功设置"+name+"矿区的等级为"+i);
                           return true;
                       }else{
                           sender.sendMessage("§e>> §c已存在等级为 "+i+"的矿区");
                           return true;
                       }

                    }
                }else{
                    sender.sendMessage("§e>> §c请输入正确的等级");
                    return true;
                }
            }
        }catch (NumberFormatException e){
            return false;
        }
        return false;
    }

    @Override
    public String getHelp() {
        return "§a/kq setLevel <矿区名称> <等级> §7设置矿区等级";
    }
}

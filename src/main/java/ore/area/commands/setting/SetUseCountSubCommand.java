package ore.area.commands.setting;

import cn.nukkit.command.CommandSender;
import ore.area.AreaMainClass;
import ore.area.commands.SubCommand;
import ore.area.utils.area.AreaClass;

/**
 * @author SmallasWater
 */
public class SetUseCountSubCommand extends SubCommand {
    public SetUseCountSubCommand(AreaMainClass plugin) {
        super(plugin);
    }

    @Override
    public boolean canUse(CommandSender sender) {
        return sender.hasPermission("ore.area.kq.usecount");
    }

    @Override
    public String getName() {
        return "setCount";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"setcount"};
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if(args.length > 2){
            String areaName = args[1];
            int count;
            try {
                count = Integer.parseInt(args[2]);
                AreaClass areaClass = AreaClass.getAreaClass(areaName);
                if(areaClass != null){
                    areaClass.setJoinCount(count);
                    sender.sendMessage("§e>> §b成功设置"+areaName+"矿区的使用次数上限"+count);
                }else{
                    sender.sendMessage("§e>> §c抱歉，不存在"+areaName+"矿区");
                    return true;
                }

            }catch (Exception e){
                sender.sendMessage("§e>> §c请输入正确的数值");
                return false;
            }


        }
        return false;
    }

    @Override
    public String getHelp() {
        return "§a/kq sc <矿区名称> <次数> §7设置矿区可以进入的次数";
    }

    @Override
    public String helpMessage() {
        return "§7设置矿区可以进入的次数 若次数设置 -1 则不限制玩家的进入次数 \n§c条件: [需设置矿区名,需设置整数次数] §2权限组: (ore.area.kq.usecount)";
    }
}

package ore.area.commands.setting;

import cn.nukkit.command.CommandSender;
import ore.area.AreaMainClass;
import ore.area.commands.SubCommand;
import ore.area.utils.Tools;
import ore.area.utils.area.AreaClass;


/**
 * @author 若水
 */
public class MoneySubCommand extends SubCommand {
    public MoneySubCommand(AreaMainClass plugin) {
        super(plugin);
    }

    @Override
    public boolean canUse(CommandSender sender) {
        return sender.hasPermission("ore.area.kq.setmoney");
    }

    @Override
    public String getName() {
        return "money";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"setMoney","setmoney"};
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        try{
            if(args.length == 3){
                String name = args[1];
                if(Tools.isNumber(args[2])){
                    double i = Double.parseDouble(args[2]);
                    AreaClass areaClass = AreaClass.getAreaClass(name);
                    if(areaClass == null){
                        sender.sendMessage("§e>> §c抱歉，不存在"+name+"矿区");
                        return true;
                    }else{
                        areaClass.setMoney(i);
                        sender.sendMessage("§e>> §b成功设置"+name+"矿区的解锁金钱为"+i);
                        return true;
                    }
                }else{
                    sender.sendMessage("§e>> §c请输入正确的价格");
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
        return "§a/kq setmoney <矿区名称> <价格> §7设置解锁矿区价格";
    }
}

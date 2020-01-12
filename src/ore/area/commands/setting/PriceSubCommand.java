package ore.area.commands.setting;

import cn.nukkit.command.CommandSender;
import ore.area.AreaMainClass;
import ore.area.commands.SubCommand;
import ore.area.utils.Tools;
import ore.area.utils.area.AreaClass;


/**
 * @author 若水
 */
public class PriceSubCommand extends SubCommand {
    public PriceSubCommand(AreaMainClass plugin) {
        super(plugin);
    }

    @Override
    public boolean canUse(CommandSender sender) {
        return sender.hasPermission("ore.area.kq.setprice");
    }

    @Override
    public String getName() {
        return "price";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"setPrice","setprice"};
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
                        areaClass.setTransferMoney(i);
                        areaClass.save();
                        sender.sendMessage("§e>> §b成功设置"+name+"矿区的传送金钱为"+i);
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
}

package ore.area.commands.setting;

import cn.nukkit.command.CommandSender;
import ore.area.AreaMainClass;
import ore.area.commands.SubCommand;
import ore.area.utils.area.AreaClass;

/**
 * @author SmallasWater
 */
public class DropSubCommand extends SubCommand {

    public DropSubCommand(AreaMainClass plugin) {
        super(plugin);
    }

    @Override
    public boolean canUse(CommandSender sender) {
        return sender.hasPermission("ore.area.kq.drop");
    }

    @Override
    public String getName() {
        return "drop";
    }

    @Override
    public String[] getAliases() {
        return new String[0];
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if(args.length > 1){
            String name = args[1];
            AreaClass areaClass = AreaClass.getAreaClass(name);
            if(areaClass != null){
                areaClass.setCanDrop(!areaClass.isCanDrop());
                sender.sendMessage("§e>> §d矿区"+name+"的死亡掉落"+(areaClass.isCanDrop()?" §c已开启":" §a已关闭"));
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
        return "§a/kq drop <矿区名称> §7设置矿区死亡是否掉落";
    }

    @Override
    public String helpMessage() {
        return "§7设置玩家在矿区内死亡是否掉落物品,若当前配置为§a掉落§7 则再次执行此指令配置将更改为 §c不掉落§7  \n§c条件: [需设置矿区名] §2权限组: (ore.area.kq.drop)";
    }
}

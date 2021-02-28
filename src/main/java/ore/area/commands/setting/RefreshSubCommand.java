package ore.area.commands.setting;

import cn.nukkit.Server;
import cn.nukkit.command.CommandSender;
import ore.area.AreaMainClass;
import ore.area.commands.SubCommand;
import ore.area.utils.area.AreaClass;


/**
 * @author SmallasWater
 */
public class RefreshSubCommand extends SubCommand {
    public RefreshSubCommand(AreaMainClass plugin) {
        super(plugin);
    }

    @Override
    public boolean canUse(CommandSender sender) {
        return sender.hasPermission("ore.area.kq.refresh");
    }

    @Override
    public String getName() {
        return "refresh";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"刷新"};
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if(args.length == 2){
            String name = args[1];
            AreaClass areaClass = AreaClass.getAreaClass(name);
            if(areaClass != null){
                areaClass.setBlock();
                sender.sendMessage("§e>> §a矿区"+name+"刷新成功");
                String bastMessage = AreaMainClass.getLang("area.refresh.broadcast","§d {name} 矿区刷新啦..").replace("{name}",areaClass.getName());
                if(!"".equalsIgnoreCase(bastMessage)) {
                    Server.getInstance().broadcastMessage(bastMessage);
                }
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
        return "§a/kq refresh <矿区名称> §7刷新矿区内的方块";
    }

    @Override
    public String helpMessage() {
        return "§7刷新矿区内的方块 \n§c条件: [需设置矿区名] §2权限组: (ore.area.kq.refresh)";
    }
}

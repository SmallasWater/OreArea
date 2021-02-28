package ore.area.commands.area;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.CommandSender;
import ore.area.AreaMainClass;
import ore.area.commands.SubCommand;
import ore.area.utils.area.AreaClass;

/**
 * @author 若水
 */
public class CloseSubCommand extends SubCommand {
    public CloseSubCommand(AreaMainClass plugin) {
        super(plugin);
    }

    @Override
    public boolean canUse(CommandSender sender) {
        return sender.hasPermission("ore.area.kq.close");
    }

    @Override
    public String getName() {
        return "关闭";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"close"};
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if(args.length > 1){
            String name = args[1];
            AreaClass aClass = AreaClass.getAreaClass(name);
            if(aClass != null){
                aClass.setKey(false);
                for(Player player:aClass.getPos().getLevel().getPlayers().values()){
                    player.sendMessage(AreaMainClass.getLang("kick.area.message"));
                    player.teleport(Server.getInstance().getDefaultLevel().getSafeSpawn());
                }
                sender.sendMessage("§e>> §a"+name+"矿区成功关闭..");
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
        return "§a/kq close <矿区名称> §7将矿区关闭 若矿区内存在玩家 则将玩家传送到服务器默认出生点";
    }

    @Override
    public String helpMessage() {
        return " §7将矿区关闭 若矿区内存在玩家 则将玩家传送到服务器默认出生点 \n§c条件: [必须保证矿区存在] §2权限组: (ore.area.kq.close)";
    }
}

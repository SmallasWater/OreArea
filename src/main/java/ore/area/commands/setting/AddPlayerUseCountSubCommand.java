package ore.area.commands.setting;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.CommandSender;
import ore.area.AreaMainClass;
import ore.area.commands.SubCommand;
import ore.area.utils.area.AreaClass;
import ore.area.utils.player.PlayerClass;

/**
 * @author SmallasWater
 */
public class AddPlayerUseCountSubCommand extends SubCommand {

    public AddPlayerUseCountSubCommand(AreaMainClass plugin) {
        super(plugin);
    }

    @Override
    public boolean canUse(CommandSender sender) {
        return sender.hasPermission("ore.area.kq.addcount");
    }

    @Override
    public String getName() {
        return "addcount";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"ac", "addCount"};
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (args.length > 3) {
            String playerName = args[1];
            String areaName = args[2];
            String count = args[3];
            Player player = Server.getInstance().getPlayer(playerName);
            if (player != null) {
                AreaClass areaClass = AreaClass.getAreaClass(areaName);
                if (areaClass != null) {
                    PlayerClass playerClass = PlayerClass.getPlayerClass(player.getName());
                    int c = areaClass.getJoinCount();
                    try {
                        c = Integer.parseInt(count);
                    } catch (Exception ignore) {
                    }
                    if ("max".equalsIgnoreCase(count)) {
                        playerClass.setJoinCount(areaName, c);
                    } else {
                        playerClass.setJoinCount(areaName, playerClass.getJoin(areaName) + c);
                    }
                    sender.sendMessage("§e>> §a成功增加§d " + c + " §a次 玩家" + player.getName() + "在" + areaName + "矿区的使用次数");
                    return true;
                } else {
                    sender.sendMessage("§e>> §c不存在" + areaName + "矿区");
                    return true;
                }

            } else {
                sender.sendMessage("§e>> §c玩家 " + playerName + " 不在线...");
                return true;
            }


        }
        return false;
    }

    @Override
    public String getHelp() {
        return "§a/kq ac <玩家名> <矿区名称> <次数(max为满次数)> §7增加玩家进入矿区的次数 ";
    }

    @Override
    public String helpMessage() {
        return "§7增加玩家进入矿区的次数  \n§c条件: [需设置玩家名，需设置矿区名，需设置使用次数] §2权限组: (ore.area.kq.addcount)";
    }
}

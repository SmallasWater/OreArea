package ore.area.commands.area;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.level.Position;
import ore.area.AreaMainClass;
import ore.area.commands.SubCommand;
import ore.area.utils.area.AreaClass;
import ore.area.utils.area.Vector;

import java.util.LinkedList;

/**
 * @author 若水
 */
public class CreateSubCommand extends SubCommand {
    public CreateSubCommand(AreaMainClass plugin) {
        super(plugin);
    }

    @Override
    public boolean canUse(CommandSender sender) {
        return sender.hasPermission("ore.area.kq.create") && sender.isPlayer();
    }

    @Override
    public String getName() {
        return "创建";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"create"};
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            if (args.length == 2) {
                if (AreaMainClass.getInstance().pos.containsKey(sender)) {
                    LinkedList<Position> positions = AreaMainClass.getInstance().pos.get(sender);
                    if (positions.size() < 2) {
                        sender.sendMessage("§e>> §c抱歉，您未设置两个坐标..");
                        AreaMainClass.getInstance().pos.remove(sender);
                        return true;
                    } else {
                        String name = args[1];
                        if (AreaClass.createAreaClass(name, new Vector(positions.get(0), positions.get(1)))) {
                            sender.sendMessage("§e>> §a恭喜，" + name + "矿区创建成功,记得设置矿区出生点哦");
                            AreaMainClass.getInstance().pos.remove(sender);
                        } else {
                            sender.sendMessage("§e>> §c抱歉，" + name + "创建矿区失败..");
                            sender.sendMessage("§e>> §c原因: 矿区重复");
                            AreaMainClass.getInstance().pos.remove(sender);
                        }
                    }
                } else {
                    sender.sendMessage("§e>> §c抱歉，您未设置任何坐标..");
                }
            } else {
                return false;
            }
        }
        return true;
    }

    @Override
    public String getHelp() {
        return "§a/kq create <矿区名称> §7创建矿区";
    }

    @Override
    public String helpMessage() {
        return " §7创建矿区 创建成功后需要到配置文件修改详细信息  \n§c条件: [在游戏内执行,矿区名称不冲突,已设置范围§c] §2权限组: (ore.area.kq.create)";
    }
}

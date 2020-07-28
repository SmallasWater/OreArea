package ore.area.commands.area;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.level.Position;
import ore.area.AreaMainClass;
import ore.area.commands.SubCommand;

import java.util.LinkedList;


/**
 * @author 若水
 */
public class Pos1SubCommand extends SubCommand {

    public Pos1SubCommand(AreaMainClass plugin) {
        super(plugin);
    }

    @Override
    public boolean canUse(CommandSender sender) {
        return sender.hasPermission("ore.area.kq.pos") && sender.isPlayer();
    }

    @Override
    public String getName() {
        return "第一点";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"pos1"};
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if(sender instanceof Player){
            LinkedList<Position> positions = new LinkedList<>();
            Position position = new Position(((Player) sender).getFloorX(), ((Player) sender).getFloorY()
                    ,((Player) sender).getFloorZ(),((Player) sender).getLevel());
            positions.add(position);
            AreaMainClass.getInstance().pos.put((Player) sender, positions);
            sender.sendMessage("§e>> §a第一点设置成功 请设置第二点");
            sender.sendMessage("§e>> §a第一点坐标: §6x: "+position.x+" y: "+position.y+" z: "+position.z);
        }

        return true;
    }

    @Override
    public String getHelp() {
        return "§a/kq pos1 §7设置矿区第一点";
    }
}

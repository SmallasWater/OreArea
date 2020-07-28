package ore.area.commands.setting;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.level.Position;
import ore.area.AreaMainClass;
import ore.area.commands.SubCommand;
import ore.area.utils.area.AreaClass;


/**
 * @author 若水
 */
public class TransferSubCommand extends SubCommand {


    public TransferSubCommand(AreaMainClass plugin) {
        super(plugin);
    }

    @Override
    public boolean canUse(CommandSender sender) {
        return sender.hasPermission("ore.area.kq.settransfer") && sender.isPlayer();
    }

    @Override
    public String getName() {
        return "传送点";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"settransfer"};
    }

    @Override
    public boolean execute(CommandSender sender, String[] args)
    {
        if(args.length > 1){
            AreaClass aClass = AreaClass.getAreaClass(args[1]);
            if(aClass != null){
                aClass.setTransfer(Position.fromObject((Player)sender,((Player) sender).getLevel()));
                sender.sendMessage("§e>> §d"+args[1]+"矿区传送点设置成功!");
            }else{
                sender.sendMessage("§e>> §c抱歉，"+args[1]+"矿区不存在..");
            }
        }
        return false;
    }

    @Override
    public String getHelp() {
        return "§a/kq settransfer <矿区名称> §7将脚底坐标设置为矿区传送点";
    }
}

package ore.area.commands.player;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.CommandSender;
import ore.area.AreaMainClass;
import ore.area.commands.SubCommand;
import ore.area.events.PlayerTransferAreaEvent;
import ore.area.utils.Tools;
import ore.area.utils.area.AreaClass;
import ore.area.utils.player.PlayerClass;


/**
 * @author 若水
 */
public class TeleportSubCommand  extends SubCommand {

    public TeleportSubCommand(AreaMainClass plugin) {
        super(plugin);
    }

    @Override
    public boolean canUse(CommandSender sender) {
        return sender.hasPermission("ore.area.kq.transfer") && sender.isPlayer();
    }

    @Override
    public String getName() {
        return "传送";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"tp"};
    }

    @Override
    public boolean execute(CommandSender sender, String[] args)
    {
        if(args.length == 2){
            AreaClass areaClass = AreaClass.getAreaClass(args[1]);
            if(areaClass != null){
                if(!areaClass.isKey()){
                    Tools.sendMessage((Player) sender,Tools.getLanguage("transfer.area.lock").replace("{name}",areaClass.getName()));
                    return true;
                }
                PlayerClass playerClass = PlayerClass.getPlayerClass((Player) sender);
                if(playerClass.canKey(areaClass.getName())){
                    PlayerTransferAreaEvent event = new PlayerTransferAreaEvent((Player) sender,areaClass);
                    Server.getInstance().getPluginManager().callEvent(event);
                }
            }else{
                sender.sendMessage("§e>> §c抱歉，"+args[1]+"矿区不存在..");
            }

        }else{
            return false;
        }
        return true;
    }
}

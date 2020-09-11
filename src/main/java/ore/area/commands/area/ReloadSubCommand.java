package ore.area.commands.area;

import cn.nukkit.command.CommandSender;
import ore.area.AreaMainClass;
import ore.area.commands.SubCommand;
import ore.area.utils.area.AreaClass;
import ore.area.utils.player.PlayerClass;

/**
 * @author SmallasWater
 */
public class ReloadSubCommand extends SubCommand {
    public ReloadSubCommand(AreaMainClass plugin) {
        super(plugin);
    }

    @Override
    public boolean canUse(CommandSender sender) {
        return sender.hasPermission("ore.area.kq.reload");
    }

    @Override
    public String getName() {
        return "reload";
    }

    @Override
    public String[] getAliases() {
        return new String[0];
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        sender.sendMessage("§e>> §a重新读取成功");
        for(AreaClass areaClass:AreaMainClass.getInstance().areas.values()){
            areaClass.save();
        }
        for(PlayerClass playerClass:AreaMainClass.getInstance().playerClasses.values()){
            playerClass.save();
        }
        AreaMainClass.getInstance().init();
        return true;
    }

    @Override
    public String getHelp() {
        return "§a/kq reload";
    }

    @Override
    public String helpMessage() {
        return " §7将对配置文件进行覆盖式读取 先保存缓存数据 再读取配置文件 [保存的时候会覆盖修改的数据] \n条件: 无 \n§2权限组: (ore.area.kq.reload)";
    }
}

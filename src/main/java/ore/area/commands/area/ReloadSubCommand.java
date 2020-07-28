package ore.area.commands.area;

import cn.nukkit.command.CommandSender;
import ore.area.AreaMainClass;
import ore.area.commands.SubCommand;

public class ReloadSubCommand extends SubCommand {
    public ReloadSubCommand(AreaMainClass plugin) {
        super(plugin);
    }

    @Override
    public boolean canUse(CommandSender sender) {
        return sender.isOp();
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
        AreaMainClass.getInstance().init();
        return true;
    }

    @Override
    public String getHelp() {
        return "§a/kq reload §7重新加载配置";
    }
}

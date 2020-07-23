package ore.area.commands.player;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import ore.area.AreaMainClass;
import ore.area.commands.SubCommand;
import ore.area.utils.area.DefaultBlockClass;

public class SellInvSubCommand extends SubCommand {
    public SellInvSubCommand(AreaMainClass plugin) {
        super(plugin);
    }

    @Override
    public boolean canUse(CommandSender sender) {
        return sender.isPlayer();
    }

    @Override
    public String getName() {
        return "sellinv";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"快捷出售"};
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        double money = DefaultBlockClass.getPlayerInventorySellMoney((Player) sender);
        sender.sendMessage("§e>>§a成功出售背包矿物 获得: $§a"+money);
        AreaMainClass.getInstance().getMoney().addMoney((Player) sender,money);
        return true;
    }

    @Override
    public String getHelp() {
        return "§a/kq sellinv §7出售背包矿物";
    }
}

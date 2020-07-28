package ore.area.commands;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.PluginCommand;
import cn.nukkit.utils.TextFormat;
import ore.area.AreaMainClass;
import ore.area.commands.area.*;
import ore.area.commands.blocks.AddSuccessSubCommand;
import ore.area.commands.blocks.CreateBlockSubCommand;
import ore.area.commands.player.SellInvSubCommand;
import ore.area.commands.player.ShowAreaSubCommand;
import ore.area.commands.player.TeleportSubCommand;
import ore.area.commands.setting.*;
import ore.area.windows.CreateWindow;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 若水
 */
public class OreCommand extends PluginCommand<AreaMainClass> {

    private final List<SubCommand> commands = new ArrayList<>();
    private final ConcurrentHashMap<String, Integer> SubCommand = new ConcurrentHashMap<>();

    public OreCommand(AreaMainClass plugin) {
        super("kq",plugin);
        this.setAliases(new String[]{"矿区"});
        this.setPermission("ore.area.kq");
        this.setDescription("矿区主命令");
        this.loadSubCommand(new CloseSubCommand(plugin));
        this.loadSubCommand(new CreateSubCommand(plugin));
        this.loadSubCommand(new OpenSubCommand(plugin));
        this.loadSubCommand(new Pos1SubCommand(plugin));
        this.loadSubCommand(new Pos2SubCommand(plugin));
        this.loadSubCommand(new TeleportSubCommand(plugin));
        this.loadSubCommand(new MoneySubCommand(plugin));
        this.loadSubCommand(new PriceSubCommand(plugin));
        this.loadSubCommand(new TransferSubCommand(plugin));
        this.loadSubCommand(new DeleteSubCommand(plugin));
        this.loadSubCommand(new RefreshSubCommand(plugin));
        this.loadSubCommand(new SetLastSubCommand(plugin));
        this.loadSubCommand(new SellInvSubCommand(plugin));
        this.loadSubCommand(new CreateBlockSubCommand(plugin));
        this.loadSubCommand(new AddSuccessSubCommand(plugin));
        this.loadSubCommand(new ReloadSubCommand(plugin));
        this.loadSubCommand(new ClearSubCommand(plugin));
        this.loadSubCommand(new ShowAreaSubCommand(plugin));
        this.loadSubCommand(new PvpSubCommand(plugin));
        this.loadSubCommand(new DropSubCommand(plugin));
        this.loadSubCommand(new SetUseTimeSubCommand(plugin));
        this.loadSubCommand(new AddPlayerUseCountSubCommand(plugin));
        this.loadSubCommand(new SetUseCountSubCommand(plugin));
        this.loadSubCommand(new SetAreaLevelSubCommand(plugin));
    }

    private void loadSubCommand(SubCommand cmd) {
        commands.add(cmd);
        int commandId = (commands.size()) - 1;
        SubCommand.put(cmd.getName().toLowerCase(), commandId);
        for (String alias : cmd.getAliases()) {
            SubCommand.put(alias.toLowerCase(), commandId);
        }
    }

    @Override
    public boolean execute(CommandSender sender, String s, String[] args) {
        if (!sender.hasPermission("ore.area.kq")) {
            sender.sendMessage(TextFormat.RED+"抱歉，，您没有使用此指令权限");
            return true;
        }
        if(args.length == 0){
            if(sender instanceof Player){
                CreateWindow.sendMenu((Player) sender);
            }else{
                sender.sendMessage("控制台无法显示GUI");
            }
            return true;
        }
        String subCommand = args[0].toLowerCase();
        if (SubCommand.containsKey(subCommand)) {
            SubCommand command = commands.get(SubCommand.get(subCommand));
            boolean canUse = command.canUse(sender);
            if (canUse) {
                return command.execute(sender, args);
            } else if (sender instanceof Player) {
                return true;
            } else {
                sender.sendMessage("请不要在控制台执行此指令");
            }
        } else {
            return sendHelp(sender,args);
        }
        return true;
    }

    private static final int PAGE_COUNT = 10;

    private boolean sendHelp(CommandSender sender, String[] args) {
        if ("help".equals(args[0]) || "?".equals(args[0])) {
            LinkedList<SubCommand> helps = new LinkedList<>();
            commands.forEach(subCommand -> {
                if (subCommand.canUse(sender)) {
                    helps.add(subCommand);
                }
            });
            if (helps.size() > 0) {
                int page = 1;
                int maxPage = (int) (helps.size() % PAGE_COUNT != 0 ? Math.floor(helps.size() / PAGE_COUNT) + 1 : Math.floor(helps.size() / PAGE_COUNT));
                if (args.length > 1) {
                    boolean pass = false;
                    String command = args[1];
                    for(ore.area.commands.SubCommand subCommand: helps){
                        if(subCommand.getName().contains(command) || Arrays.asList(subCommand.getAliases()).contains(command)){
                            pass = true;
                            sender.sendMessage(subCommand.getHelp());
                        }
                    }
                    if(pass){
                        return true;
                    }
                    try {
                        page = Integer.parseInt(command);
                    } catch (Exception ignore) {
                    }
                }
                sender.sendMessage("§a§l >> §eHelp for OreArea 当前页: " + page + " 共计 " + maxPage + "页 §a<<");
                if (page > maxPage) {
                    page = maxPage;
                }
                for (int i = (page - 1)* PAGE_COUNT ; i < page * PAGE_COUNT; i++) {
                    if (i == helps.size()) {
                        return true;
                    }
                    sender.sendMessage(helps.get(i).getHelp());
                }

            }else{
                sender.sendMessage("§a§l >> §e无相关帮助指令");
            }
        }
        return true;
    }



}

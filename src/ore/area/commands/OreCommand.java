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

import java.util.ArrayList;
import java.util.List;
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


    private boolean sendHelp(CommandSender sender, String[] args) {
        if ("help".equals(args[0])) {
            if(sender.isOp()){
                if(args.length > 1){
                    if("2".equals(args[1])){
                        sender.sendMessage("§a§l >> §eHelp for OreArea 当前页: 2 共计 3页 §a<<");
                        sender.sendMessage("§a/kq tp <名称>§7传送到矿区");
                        sender.sendMessage("§a/kq settransfer <矿区名称> §7将脚底坐标设置为矿区传送点");
                        sender.sendMessage("§a/kq setprice <矿区名称> <价格> §7设置传送到矿区的价格");
                        sender.sendMessage("§a/kq setmoney <矿区名称> <价格> §7设置解锁矿区价格");
                        sender.sendMessage("§a/kq setlast <矿区名称> <矿区名> §7设置矿区前置");
                        sender.sendMessage("§a/kq 刷新 <矿区名称> §7刷新矿区");
                        sender.sendMessage("§a/kq cb <金钱> §7将手中的方块设置回收价格");
                        sender.sendMessage("§a/kq sellinv §7出售背包矿物");
                        sender.sendMessage("§a/kq success <数量> §7添加手中方块破坏成就个数");
                        sender.sendMessage("§a/kq clean <矿区名称> §7清空矿区方块");
                        sender.sendMessage("§a§l >> §eHelp for OreArea 当前页: 2 共计 3页 §a<<");
                        return true;
                    }
                    if("3".equals(args[1])){
                        sender.sendMessage("§a§l >> §eHelp for OreArea 当前页: 3 共计 3页 §a<<");
                        sender.sendMessage("§a/kq showMax §7弹出当前解锁最高等级矿区GUI");
                        sender.sendMessage("§a/kq showNext §7弹出解锁当前最高等级矿区下一个矿区GUI");
                        sender.sendMessage("§a/kq show <矿区名称> §7弹出矿区GUI");
                        sender.sendMessage("§a/kq pvp <矿区名称>§7设置矿区PVP限制");
                        sender.sendMessage("§a/kq drop <矿区名称> §7设置矿区死亡掉落限制");
                        sender.sendMessage("§a/kq sut <矿区名称> <使用时间(秒) -1为永久> §7设置玩家在矿区的使用时间");
                        sender.sendMessage("§a/kq ac <玩家名> <矿区名称> <次数> §7给玩家增加进入矿区次数");
                        sender.sendMessage("§a/kq sc <矿区名称> <次数> §7设置矿区进入次数");
                        sender.sendMessage("§a§l >> §eHelp for OreArea 当前页: 3 共计 3页 §a<<");
                        return true;
                    }
                }
                sender.sendMessage("§a§l >> §eHelp for OreArea 当前页: 1 共计 3页 §a<<");
                sender.sendMessage("§a/kq §7打开矿区GUI");
                sender.sendMessage("§a/kq pos1 §7设置矿区第一点");
                sender.sendMessage("§a/kq pos2 §7设置矿区第二点");
                sender.sendMessage("§a/kq create <矿区名称> §7创建矿区");
                sender.sendMessage("§a/kq tp <矿区名称> §7传送到矿区");
                sender.sendMessage("§a/kq open <矿区名称> §7开放一个矿区");
                sender.sendMessage("§a/kq close <矿区名称> §7关闭一个矿区");
                sender.sendMessage("§a/kq delete <矿区名称> §7删除矿区");
                sender.sendMessage("§a/kq reload §7重新加载配置");
                sender.sendMessage("§a§l >> §eHelp for OreArea 当前页: 1 共计 3页 §a<<");
            }else{
                sender.sendMessage("§a§l >> §eHelp for OreArea 当前页: 1 共计 1页 §a<<");
                sender.sendMessage("§a/kq §7打开矿区GUI");
                sender.sendMessage("§a/kq tp <矿区名称> §7传送到矿区");
                sender.sendMessage("§a/kq sellinv §7出售背包矿物");
                sender.sendMessage("§a/kq showMax §7弹出当前解锁最高等级矿区GUI");
                sender.sendMessage("§a/kq showNext §7弹出解锁当前最高等级矿区下一个矿区GUI");
                sender.sendMessage("§a/kq show <矿区名称> §7弹出矿区GUI");
                sender.sendMessage("§a§l >> §eHelp for OreArea 当前页: 1 共计 1页 §a<<");
            }
        }
        return true;
    }



}

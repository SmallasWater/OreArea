package ore.area.commands.player;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import ore.area.AreaMainClass;
import ore.area.commands.SubCommand;
import ore.area.utils.area.AreaClass;
import ore.area.utils.player.PlayerClass;
import ore.area.windows.CreateWindow;

/**
 * @author SmallasWater
 */
public class ShowAreaSubCommand extends SubCommand {
    public ShowAreaSubCommand(AreaMainClass plugin) {
        super(plugin);
    }

    @Override
    public boolean canUse(CommandSender sender) {
        return sender.hasPermission("ore.area.kq.show");
    }

    @Override
    public String getName() {
        return "显示";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"show","showMax","showNext"};
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if(args.length > 0){
            PlayerClass playerClass = PlayerClass.getPlayerClass(sender.getName());
            AreaClass areaClass;
            switch (args[0]){
                case "show":
                case "显示":
                    if(args.length > 1){
                        String areaName = args[1];
                        areaClass = AreaClass.getAreaClass(areaName);
                        if(areaClass != null){
                            AreaMainClass.getInstance().clickArea.put((Player) sender,areaClass);
                            CreateWindow.sendSub((Player) sender);
                        }else{
                            sender.sendMessage("§e>>§c"+areaName+"矿区不存在...");
                        }
                    }else{
                        sender.sendMessage("§e>>§c请输入矿区名..");
                    }
                    break;
                case "showNext":
                case "下一级":
                case "next":
                     areaClass = AreaClass.getAreaClassByLevel(playerClass.getMaxAreaLevel()+1);
                    if(areaClass != null){
                        AreaMainClass.getInstance().clickArea.put((Player) sender,areaClass);
                        CreateWindow.sendSub((Player) sender);
                    }else{
                        if(AreaClass.getMaxAreaLevel() == playerClass.getMaxAreaLevel()){
                            sender.sendMessage("§e>>§a您当前到达矿区的最高等级啦~~");
                            return true;
                        }
                        sender.sendMessage("§e>>§c不存在等级为 "+playerClass.getMaxAreaLevel()+1+"的矿区..");
                    }
                    break;
                case "showMax":
                case "当前":
                case "new":
                    areaClass = AreaClass.getAreaClassByLevel(playerClass.getMaxAreaLevel());
                    if(areaClass != null){
                        AreaMainClass.getInstance().clickArea.put((Player) sender,areaClass);
                        CreateWindow.sendSub((Player) sender);
                    }else{
                        sender.sendMessage("§e>>§c不存在等级为 "+playerClass.getMaxAreaLevel()+"的矿区..");
                    }
                    break;
                default:break;
            }
        }
        return false;
    }

    @Override
    public String getHelp() {
        return "§a/kq <show/showNext/showMax> <矿区名称(show)> §7弹出GUI (具体用法请执行/kq help show)";
    }
    @Override
    public String helpMessage() {
        return " §7弹出GUI 可配合菜单 NPC 使用 " +
                "\nshow: 根据名字显示矿区 指令格式:/kq show <矿区名称> §c条件: [在游戏内执行] " +
                "\nshowNext: 显示玩家当前解锁等级的下一等级矿区 指令格式:/kq showNext §c条件: [在游戏内执行,需保证等级存在] "+
                "\nshowNext: 显示玩家当前等级的矿区 指令格式:/kq showMax §c条件: [在游戏内执行,需保证等级存在] "+
                "\n §2权限组: (ore.area.kq.show)";
    }
}

package ore.area.commands.player;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import ore.area.AreaMainClass;
import ore.area.commands.SubCommand;
import ore.area.utils.area.AreaClass;
import ore.area.utils.player.PlayerClass;
import ore.area.windows.CreateWindow;

public class ShowAreaSubCommand extends SubCommand {
    public ShowAreaSubCommand(AreaMainClass plugin) {
        super(plugin);
    }

    @Override
    public boolean canUse(CommandSender sender) {
        return sender.isPlayer();
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
            PlayerClass playerClass = PlayerClass.getPlayerClass((Player) sender);
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
                     areaClass = AreaClass.getAreaClassByLevel(playerClass.getMaxAreaLevel()+1);
                    if(areaClass != null){
                        AreaMainClass.getInstance().clickArea.put((Player) sender,areaClass);
                        CreateWindow.sendSub((Player) sender);
                    }else{
                        sender.sendMessage("§e>>§c不存在等级为 "+playerClass.getMaxAreaLevel()+1+"的矿区..");
                    }
                    break;
                case "showMax":
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
}

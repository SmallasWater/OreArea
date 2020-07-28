package ore.area.commands;


import cn.nukkit.Server;
import cn.nukkit.command.CommandSender;
import ore.area.AreaMainClass;

/**
 * @author 若水
 */
public abstract class SubCommand {

    private final AreaMainClass plugin;

    protected SubCommand(AreaMainClass plugin) {
        if (plugin == null) {
            Server.getInstance().getLogger().error("AreaMainClass is null");
        }
        this.plugin = plugin;
    }

    /**
     * @return AreaMainClass
     */
    protected AreaMainClass getPlugin() {
        return plugin;
    }


    /**
     * @param sender CommandSender
     * @return boolean
     */
    public abstract boolean canUse(CommandSender sender);


    /**
     * 获取名称
     * @return string
     */
    public abstract String getName();
    /**
     * 获取别名
     * @return string[]
     */
    public abstract String[] getAliases();

    /**
     * 命令响应
     * @param sender the sender      - CommandSender
     * @param args   The arrugements      - String[]
     * @return true if true
     */
    public abstract boolean execute(CommandSender sender, String[] args);

    /**
     * 帮助信息
     * */
    public abstract String getHelp();


}

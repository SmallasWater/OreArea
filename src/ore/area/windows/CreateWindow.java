package ore.area.windows;

import cn.nukkit.Player;

import cn.nukkit.form.element.ElementButton;
import cn.nukkit.form.element.ElementButtonImageData;
import cn.nukkit.form.window.FormWindowSimple;
import ore.area.AreaMainClass;
import ore.area.utils.Tools;
import ore.area.utils.area.AreaClass;
import ore.area.utils.player.PlayerClass;

/**
 * @author 若水
 */
public class CreateWindow {

    static final int MENU = 0xFFEAD001;

    static final int SUB = 0xFFEAD002;

    public static void sendMenu(Player player){
        FormWindowSimple simple = new FormWindowSimple(AreaMainClass.getLang("gui.menu.title")
                ,AreaMainClass.getLang("gui.menu.content"));
        PlayerClass playerClass = PlayerClass.getPlayerClass(player.getName());
        boolean canKey = AreaMainClass.getInstance().isKeyCanOpen();
        for(AreaClass area:Tools.sqrtAreaClass()){
            if(canKey){
                canKey = playerClass.canKey(area.getName());
            }

            String t = AreaMainClass.getLang("gui.menu.button");
            t = t.replace("{level}",area.getLevel()+"")
                    .replace("{name}",area.getName())
                    .replace("{lock}",!canKey ?
                            AreaMainClass.getLang("gui.area.lock"):
                            AreaMainClass.getLang("gui.area.unlock"));
            simple.addButton(new ElementButton(t,area.getButtonImage()));
        }
        player.showFormWindow(simple,MENU);
    }

    public static void sendSub(Player player){
        if(AreaMainClass.getInstance().clickArea.containsKey(player)){
            AreaClass areaClass = AreaMainClass.getInstance().clickArea.get(player);
            PlayerClass playerClass = PlayerClass.getPlayerClass(player.getName());
            String sub = "无";
            if(!"".equals(areaClass.getLastArea())){
                if(playerClass.canKey(areaClass.getLastArea())){
                    sub = Tools.getLanguage("gui.area.unlock.sub").replace("{name}",areaClass.getLastArea());
                }else{
                    sub = Tools.getLanguage("gui.area.lock.sub").replace("{name}",areaClass.getLastArea());
                }

            }
            FormWindowSimple simple = new FormWindowSimple(AreaMainClass
                    .getLang("gui.menu.sub.title").replace("{name}",areaClass.getName())
                    ,"");
            StringBuilder content = new StringBuilder(AreaMainClass.getLang("gui.menu.sub.content"));
            String pvp = areaClass.isCanPVP()?(AreaMainClass.getLang("area.not.pvp", "§c禁止")):AreaMainClass.getLang("area.pvp", "§a允许");
            String drop = !areaClass.isCanDrop()?(AreaMainClass.getLang("area.not.drop", "§a不掉落")):AreaMainClass.getLang("area.drop", "§c掉落");
            content = new StringBuilder(content.toString().replace("{name}", areaClass.getName())
                    .replace("{level}", areaClass.getLevel() + "")
                    .replace("{money}", String.format("%.2f", areaClass.getTransferMoney()))
                    .replace("{key}", areaClass.isKey() ?
                            AreaMainClass.getLang("area.unlock") : AreaMainClass.getLang("area.lock"))
                    .replace("{last}",sub)
                    .replace("{message}", areaClass.getMessage())
                    .replace("{pvp}",pvp)
                    .replace("{drop}",drop)
                    .replace("{time}",Math.round(areaClass.getUseTime()/60)+"分钟")
                    .replace("{count}",playerClass.getJoin(areaClass.getName())+"")
                    .replace("{maxcount}",areaClass.getJoinCount() == -1?"无限制":""+areaClass.getJoinCount()+""));
            boolean canBuy = AreaMainClass.getInstance().isKeyCanOpen();
            simple.setContent(content.toString());
            if(content.toString().split("\\n").length < 11){
                for(int i = content.toString().split("\\n").length; i < 11; i++){
                    content.append("\n");
                }
                simple.setContent(content.toString());
            }
            if(canBuy){
                canBuy = !playerClass.canKey(areaClass.getName());
            }
            if(canBuy){
                String t = AreaMainClass.getLang("gui.menu.sub.buy");
                t = t.replace("{money}",String.format("%.2f", areaClass.getMoney()));
                simple.addButton(new ElementButton(t,new ElementButtonImageData("path","textures/ui/MCoin")));
            }else{
                simple.addButton(new ElementButton(AreaMainClass.getLang("gui.menu.sub.unbuy")
                        ,new ElementButtonImageData("path","textures/ui/icon_recipe_nature")));
            }
            simple.addButton(new ElementButton(AreaMainClass.getLang("gui.menu.sub.back")
                    ,new ElementButtonImageData("path","textures/ui/refresh_light")));
            player.showFormWindow(simple,SUB);
        }
    }
}

package ore.area.utils;

import cn.nukkit.Player;
import ore.area.AreaMainClass;
import ore.area.utils.area.AreaClass;
import ore.area.utils.player.PlayerClass;
import tip.utils.variables.BaseVariable;

import java.util.LinkedHashMap;

/**
 * @author SmallasWater
 */
public class OreAreaVariable extends BaseVariable {
    public OreAreaVariable(Player player) {
        super(player);
    }

    @Override
    public void strReplace() {
        AreaClass areaClass = Tools.getDefaultArea(player,2);
        String time = "§c不在矿区范围";
        String reset = "§c不在矿区范围";
        String name = "§c无";
        if(areaClass != null){
            if(AreaMainClass.getInstance().useTime.containsKey(player.getName())) {
                LinkedHashMap<String,Integer> map = AreaMainClass.getInstance().useTime.get(player.getName());
                if(map.containsKey(areaClass.getName())){
                    int i = Math.round(map.get(areaClass.getName()) /60);
                    if(i != 0) {
                        time = "§7" +i + "§2分钟";
                    }else{
                        time = "§7" +areaClass.getUseTime() + "§2秒";
                    }
                }
            }else{
                if(!areaClass.isKey()){
                    time = "§c未开启";
                }else{
                    PlayerClass playerClass = PlayerClass.getPlayerClass(player.getName());
                    if(playerClass.canKey(areaClass.getName())) {
                        if(areaClass.getUseTime() == -1) {
                            time = "§7无时限";
                        }else{
                            int i = Math.round(areaClass.getUseTime() /60);
                            if(i != 0) {
                                time = "§7" +i + "§2分钟";
                            }else{
                                time = "§7" +areaClass.getUseTime() + "§2秒";
                            }
                        }
                    }else{
                        time = "§c未解锁矿区";
                    }
                }

            }
            if(AreaMainClass.timer.containsKey(areaClass.getName())) {
                reset = "§7"+AreaMainClass.timer.get(areaClass.getName())+" §e秒后刷新";
            }else{
                reset = "§c未刷新";
            }
            name = areaClass.getName();
        }
        addStrReplaceString("{oreareaTime}",time);
        addStrReplaceString("{oreareaReset}",reset);
        addStrReplaceString("{oreareaName}",name);

    }
}

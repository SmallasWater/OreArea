package ore.area.utils.task;


import cn.nukkit.scheduler.Task;
import ore.area.AreaMainClass;
import ore.area.utils.area.AreaClass;
import ore.area.utils.Tools;



/**
 * @author 若水 计算冷却类...
 */
public class AreaLoadTask extends Task {

    private String name;

    public AreaLoadTask(String name){
        this.name = name;
    }


    @Override
    public void onRun(int i) {
        AreaClass cl = AreaClass.getAreaClass(name);
        if(cl != null){
            if(cl.isKey()){
                if(Tools.inTime()){
                    if(!AreaMainClass.timer.containsKey(cl.getName())){
                        AreaMainClass.timer.put(cl.getName(),cl.getReset());
                    }
                    if(AreaMainClass.timer.get(cl.getName()) == 0){
                        cl.setBlock();
                        AreaMainClass.timer.put(cl.getName(),cl.getReset());
                    }else{
                        AreaMainClass.timer.put(cl.getName(),AreaMainClass.timer.get(cl.getName()) - 1);
                    }
                }
            }
        }else{
            this.cancel();
        }
    }
}

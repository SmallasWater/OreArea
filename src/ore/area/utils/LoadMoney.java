package ore.area.utils;

import cn.nukkit.Player;
import cn.nukkit.Server;
import me.onebone.economyapi.EconomyAPI;
import money.Money;


/**
 * 使用 EconomyAPI 或 Money
 *
 * @author SmallasWater*/
public class LoadMoney {

    private static final int MONEY = 1;
    private static final int ECONOMY_API = 2;

    private int money = 0;

    public LoadMoney(){
        if(Server.getInstance().getPluginManager().getPlugin("EconomyAPI") != null){
            money = ECONOMY_API;
        }else if(Server.getInstance().getPluginManager().getPlugin("Money") != null){
            money = MONEY;
        }
    }

    public String getMonetaryUnit(){
        if (this.money == ECONOMY_API) {
            return EconomyAPI.getInstance().getMonetaryUnit();
        }
        return "$";
    }

    public double myMoney(Player player){
        return myMoney(player.getName());
    }

    private double myMoney(String player){
        switch (this.money){
            case MONEY:
                if(Money.getInstance().getPlayers().contains(player)){
                    return Money.getInstance().getMoney(player);
                }
                break;
            case ECONOMY_API:
                return EconomyAPI.getInstance().myMoney(player) ;

            default:break;
        }
        return 0;
    }

    public void addMoney(Player player, double money){
        addMoney(player.getName(), money);
    }

    private void addMoney(String player, double money){
        switch (this.money){
            case MONEY:
                if(Money.getInstance().getPlayers().contains(player)){
                    Money.getInstance().addMoney(player, (float) money);
                    return;
                }
                break;
            case ECONOMY_API:
                EconomyAPI.getInstance().addMoney(player, money, true);
                return;

            default:break;
        }
    }
    public void reduceMoney(Player player, double money){
        reduceMoney(player.getName(), money);
    }

    private void reduceMoney(String player, double money){
        switch (this.money){
            case MONEY:
                if(Money.getInstance().getPlayers().contains(player)){
                    Money.getInstance().reduceMoney(player, (float) money);
                    return;
                }
                break;
            case ECONOMY_API:
                EconomyAPI.getInstance().reduceMoney(player, money, true);
                return;

            default:break;
        }
    }





}

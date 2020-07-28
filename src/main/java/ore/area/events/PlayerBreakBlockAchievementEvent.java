package ore.area.events;


import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.event.player.PlayerAchievementAwardedEvent;



/**
 * @author 若水
 */
public class PlayerBreakBlockAchievementEvent extends PlayerAchievementAwardedEvent {


    private Block block;

    public PlayerBreakBlockAchievementEvent(Player player, String achievementId,Block block) {
        super(player, achievementId);
        this.block = block;
    }




    public Block getBlock() {
        return block;
    }
}

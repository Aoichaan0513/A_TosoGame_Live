package jp.aoichaan0513.A_TosoGame_Live.Listeners;

import jp.aoichaan0513.A_TosoGame_Live.API.Scoreboard.Teams;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;

public class onBreak implements Listener {
    @EventHandler
    public void onHangingBreak(HangingBreakByEntityEvent e) {
        if (e.getRemover().getType() == EntityType.PLAYER) {
            Player p = (Player) e.getRemover();
            if (!Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_ADMIN, p)) {
                e.setCancelled(true);
            }
        }
    }
}

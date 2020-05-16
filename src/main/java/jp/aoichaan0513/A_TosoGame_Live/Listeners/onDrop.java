package jp.aoichaan0513.A_TosoGame_Live.Listeners;

import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI;
import jp.aoichaan0513.A_TosoGame_Live.API.Scoreboard.Teams;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class onDrop implements Listener {

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        if (!Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_ADMIN, e.getPlayer())) {
            e.setCancelled(true);
            e.getPlayer().sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "アイテムを投げることはできません。");
            return;
        }
    }
}

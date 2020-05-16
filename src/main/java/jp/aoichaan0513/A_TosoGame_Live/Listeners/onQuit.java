package jp.aoichaan0513.A_TosoGame_Live.Listeners;

import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI;
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.MissionManager;
import jp.aoichaan0513.A_TosoGame_Live.API.Scoreboard.ScoreBoard;
import jp.aoichaan0513.A_TosoGame_Live.API.Scoreboard.Teams;
import jp.aoichaan0513.A_TosoGame_Live.API.TosoGameAPI;
import jp.aoichaan0513.A_TosoGame_Live.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Scoreboard;

public class onQuit implements Listener {

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();

        e.setQuitMessage(ChatColor.YELLOW + "<- " + ChatColor.GOLD + p.getName());

        if (Main.shuffleList.contains(p))
            Main.shuffleList.remove(p);
        if (Main.playerList.contains(p))
            Main.playerList.remove(p);
        if (onMove.zoneList.contains(p))
            onMove.zoneList.remove(p);

        ScoreBoard.removeBoard(p);

        if (Main.invisibleList.contains(p.getUniqueId())) {
            for (Player player : Bukkit.getOnlinePlayers())
                player.showPlayer(p);
            Main.invisibleList.remove(p.getUniqueId());
        }

        if (MissionManager.isBossBar())
            MissionManager.getBossBar().removePlayer(p);

        for (Player player : Bukkit.getOnlinePlayers())
            TosoGameAPI.showPlayers(player);

        if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_HUNTER, p)) {
            for (Player player : Bukkit.getOnlinePlayers())
                if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_ADMIN, player))
                    player.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "ハンターが退出しました。");
        } else if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_SUCCESS, p)) {
            Teams.leaveTeam(Teams.OnlineTeam.TOSO_SUCCESS, p);
            Teams.joinTeam(Teams.OnlineTeam.TOSO_PLAYER, p);

        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            Scoreboard board = ScoreBoard.getBoard(player);

            if (!TosoGameAPI.isAdmin(player)) {
                if (player.getInventory().getItemInMainHand().getType() == Material.BOOK) {
                    ItemMeta itemMeta = player.getInventory().getItemInMainHand().getItemMeta();

                    if (ChatColor.stripColor(itemMeta.getDisplayName()).equals(Main.PHONE_ITEM_NAME)) {
                        board.getObjective(TosoGameAPI.Objective.SIDEBAR.getName()).setDisplaySlot(DisplaySlot.SIDEBAR);
                    } else {
                        board.clearSlot(DisplaySlot.SIDEBAR);
                    }
                } else if (player.getInventory().getItemInOffHand().getType() == Material.BOOK) {
                    ItemMeta itemMeta = player.getInventory().getItemInOffHand().getItemMeta();

                    if (ChatColor.stripColor(itemMeta.getDisplayName()).equals(Main.PHONE_ITEM_NAME)) {
                        board.getObjective(TosoGameAPI.Objective.SIDEBAR.getName()).setDisplaySlot(DisplaySlot.SIDEBAR);
                    } else {
                        board.clearSlot(DisplaySlot.SIDEBAR);
                    }
                } else {
                    board.clearSlot(DisplaySlot.SIDEBAR);
                }
            } else {
                board.getObjective(TosoGameAPI.Objective.SIDEBAR.getName()).setDisplaySlot(DisplaySlot.SIDEBAR);
            }
            return;
        }
    }
}

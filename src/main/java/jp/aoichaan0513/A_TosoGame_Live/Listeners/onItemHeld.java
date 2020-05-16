package jp.aoichaan0513.A_TosoGame_Live.Listeners;

import jp.aoichaan0513.A_TosoGame_Live.API.Scoreboard.ScoreBoard;
import jp.aoichaan0513.A_TosoGame_Live.API.Scoreboard.Teams;
import jp.aoichaan0513.A_TosoGame_Live.API.TosoGameAPI;
import jp.aoichaan0513.A_TosoGame_Live.Main;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Scoreboard;

public class onItemHeld implements Listener {

    @EventHandler
    public void onItemHeld(PlayerItemHeldEvent e) {
        Player p = e.getPlayer();
        ItemStack item = p.getInventory().getItem(e.getNewSlot());

        if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_PLAYER, p) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_SUCCESS, p)) {
            Scoreboard board = ScoreBoard.getBoard(p);

            if (!ScoreBoard.isHidePlayer(p)) {
                if (item != null) {
                    if (item.getType() == Material.BOOK) {
                        ItemMeta itemMeta = item.getItemMeta();
                        if (ChatColor.stripColor(itemMeta.getDisplayName()).equals(Main.PHONE_ITEM_NAME)) {
                            if (board.getObjective(TosoGameAPI.Objective.SIDEBAR.getName()).getDisplaySlot() != DisplaySlot.SIDEBAR)
                                board.getObjective(TosoGameAPI.Objective.SIDEBAR.getName()).setDisplaySlot(DisplaySlot.SIDEBAR);
                        } else {
                            board.clearSlot(DisplaySlot.SIDEBAR);
                        }
                    } else {
                        board.clearSlot(DisplaySlot.SIDEBAR);
                    }
                } else {
                    board.clearSlot(DisplaySlot.SIDEBAR);
                }
            } else {
                if (board.getObjective(TosoGameAPI.Objective.SIDEBAR.getName()).getDisplaySlot() != DisplaySlot.SIDEBAR)
                    board.getObjective(TosoGameAPI.Objective.SIDEBAR.getName()).setDisplaySlot(DisplaySlot.SIDEBAR);
            }
        } else {
            Scoreboard board = ScoreBoard.getBoard(p);

            if (board.getObjective(TosoGameAPI.Objective.SIDEBAR.getName()).getDisplaySlot() != DisplaySlot.SIDEBAR)
                board.getObjective(TosoGameAPI.Objective.SIDEBAR.getName()).setDisplaySlot(DisplaySlot.SIDEBAR);
        }
    }
}
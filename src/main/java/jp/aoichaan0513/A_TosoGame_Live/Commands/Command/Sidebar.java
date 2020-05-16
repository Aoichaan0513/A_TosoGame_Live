package jp.aoichaan0513.A_TosoGame_Live.Commands.Command;

import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI;
import jp.aoichaan0513.A_TosoGame_Live.API.Scoreboard.ScoreBoard;
import jp.aoichaan0513.A_TosoGame_Live.API.Scoreboard.Teams;
import jp.aoichaan0513.A_TosoGame_Live.API.TosoGameAPI;
import jp.aoichaan0513.A_TosoGame_Live.Commands.ICommand;
import jp.aoichaan0513.A_TosoGame_Live.Main;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Scoreboard;

import java.util.List;

public class Sidebar extends ICommand {

    public Sidebar(String name) {
        super(name);
    }

    @Override
    public void onPlayerCommand(Player sp, Command cmd, String label, String[] args) {
        if (!ScoreBoard.isHidePlayer(sp)) {
            ScoreBoard.addHidePlayer(sp);

            Scoreboard board = ScoreBoard.getBoard(sp);
            board.getObjective(TosoGameAPI.Objective.SIDEBAR.getName()).setDisplaySlot(DisplaySlot.SIDEBAR);

            sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SUCCESS) + "サイドバーを常時表示に変更しました。");
            return;
        } else {
            ScoreBoard.removeHidePlayer(sp);

            Scoreboard board = ScoreBoard.getBoard(sp);
            if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_PLAYER, sp) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_SUCCESS, sp)) {
                if (sp.getInventory().getItemInMainHand().getType() == Material.BOOK) {
                    ItemMeta itemMeta = sp.getInventory().getItemInMainHand().getItemMeta();
                    if (ChatColor.stripColor(itemMeta.getDisplayName()).equals(Main.PHONE_ITEM_NAME)) {
                        board.getObjective(TosoGameAPI.Objective.SIDEBAR.getName()).setDisplaySlot(DisplaySlot.SIDEBAR);
                    } else {
                        board.clearSlot(DisplaySlot.SIDEBAR);
                    }
                } else if (sp.getInventory().getItemInOffHand().getType() == Material.BOOK) {
                    ItemMeta itemMeta = sp.getInventory().getItemInOffHand().getItemMeta();
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

            sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SUCCESS) + "サイドバーを自動切り替えに変更しました。");
            return;
        }
    }

    @Override
    public void onBlockCommand(BlockCommandSender bs, Command cmd, String label, String[] args) {
        MainAPI.sendMessage(bs, MainAPI.ErrorMessage.NOT_PLAYER);
        return;
    }

    @Override
    public void onConsoleCommand(ConsoleCommandSender cs, Command cmd, String label, String[] args) {
        MainAPI.sendMessage(cs, MainAPI.ErrorMessage.NOT_PLAYER);
        return;
    }

    @Override
    public List<String> onPlayerTabComplete(Player sp, Command cmd, String alias, String[] args) {
        return null;
    }

    @Override
    public List<String> onBlockTabComplete(BlockCommandSender bs, Command cmd, String alias, String[] args) {
        return null;
    }

    @Override
    public List<String> onConsoleTabComplete(ConsoleCommandSender cs, Command cmd, String alias, String[] args) {
        return null;
    }
}

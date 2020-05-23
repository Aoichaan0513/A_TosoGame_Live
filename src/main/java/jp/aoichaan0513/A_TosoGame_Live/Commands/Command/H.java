package jp.aoichaan0513.A_TosoGame_Live.Commands.Command;

import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI;
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.GameManager;
import jp.aoichaan0513.A_TosoGame_Live.API.Scoreboard.Teams;
import jp.aoichaan0513.A_TosoGame_Live.Commands.ICommand;
import jp.aoichaan0513.A_TosoGame_Live.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class H extends ICommand {

    public H(String name) {
        super(name);
    }

    @Override
    public void onPlayerCommand(Player sp, Command cmd, String label, String[] args) {
        if (Hunter.num > 0) {
            if (GameManager.isGame(GameManager.GameState.GAME)) {
                if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_JAIL, sp)) {
                    if (!Main.hunterShuffleSet.contains(sp.getUniqueId()))
                        Main.hunterShuffleSet.add(sp.getUniqueId());
                    else
                        Main.hunterShuffleSet.remove(sp.getUniqueId());

                    sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.WARNING) + ChatColor.UNDERLINE + "ハンター募集" + ChatColor.GOLD + ChatColor.UNDERLINE + (Main.hunterShuffleSet.contains(sp.getUniqueId()) ? "に応募" : "の応募をキャンセル") + ChatColor.RESET + ChatColor.YELLOW + "しました。");
                    return;
                }
                MainAPI.sendMessage(sp, MainAPI.ErrorMessage.PERMISSIONS_TEAM_JAIL);
                return;
            } else {
                if (!Main.hunterShuffleSet.contains(sp.getUniqueId()))
                    Main.hunterShuffleSet.add(sp.getUniqueId());
                else
                    Main.hunterShuffleSet.remove(sp.getUniqueId());

                sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.WARNING) + ChatColor.UNDERLINE + "ハンター募集" + ChatColor.GOLD + ChatColor.UNDERLINE + (Main.hunterShuffleSet.contains(sp.getUniqueId()) ? "に応募" : "の応募をキャンセル") + ChatColor.RESET + ChatColor.YELLOW + "しました。");
                return;
            }
        }
        sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "ハンターを募集していないため実行できません。");
        return;
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

package jp.aoichaan0513.A_TosoGame_Live.Commands.Command;

import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI;
import jp.aoichaan0513.A_TosoGame_Live.API.Scoreboard.Teams;
import jp.aoichaan0513.A_TosoGame_Live.API.TosoGameAPI;
import jp.aoichaan0513.A_TosoGame_Live.Commands.ICommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Hide extends ICommand {

    public Hide(String name) {
        super(name);
    }

    @Override
    public void onPlayerCommand(Player sp, Command cmd, String label, String[] args) {
        if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_JAIL, sp)) {
            if (args.length != 0) {
                if (args[0].equalsIgnoreCase("admin")) {
                    for (Player player : Bukkit.getOnlinePlayers())
                        sp.hidePlayer(player);

                    sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SUCCESS) + ChatColor.GREEN + "周りを非表示にしました。(運営を含む)");
                    return;
                }
                sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "引数が不正です。\n" +
                        MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "コマンドの使い方:\n" +
                        MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "\"/hide\" - 運営以外を非表示\n" +
                        MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "\"/hide admin\" - 運営も非表示");
                return;
            } else {
                for (Player player : Bukkit.getOnlinePlayers())
                    if (!TosoGameAPI.isAdmin(player))
                        sp.hidePlayer(player);

                sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SUCCESS) + ChatColor.GREEN + "周りを非表示にしました。");
                return;
            }
        }
        sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "確保されていないため実行できません。");
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
        if (args.length == 1) {
            if (args[0].length() == 0) {
                return Arrays.asList("admin");
            } else {
                if ("admin".startsWith(args[0])) {
                    return Collections.singletonList("admin");
                }
            }
        }
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

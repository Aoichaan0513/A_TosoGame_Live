package jp.aoichaan0513.A_TosoGame_Live.Commands.Command;

import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI;
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.GameManager;
import jp.aoichaan0513.A_TosoGame_Live.API.Scoreboard.Teams;
import jp.aoichaan0513.A_TosoGame_Live.API.TosoGameAPI;
import jp.aoichaan0513.A_TosoGame_Live.Commands.ICommand;
import jp.aoichaan0513.A_TosoGame_Live.Listeners.onInteract;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class Btp extends ICommand {

    public Btp(String name) {
        super(name);
    }

    @Override
    public void onPlayerCommand(Player sp, Command cmd, String label, String[] args) {
        if (TosoGameAPI.isAdmin(sp)) {
            if (!GameManager.isGame()) {
                if (onInteract.loc != null) {
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        if (!Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_ADMIN, p)) {
                            p.setGameMode(GameMode.SPECTATOR);
                            p.teleport(new Location(onInteract.loc.getWorld(), onInteract.loc.getBlockX() + 0.5, onInteract.loc.getBlockY(), onInteract.loc.getBlockZ() + 0.5));
                        }
                    }
                    sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + ChatColor.GREEN + ChatColor.UNDERLINE + (Bukkit.getOnlinePlayers().size() - Teams.getOnlineCount(Teams.OnlineTeam.TOSO_ADMIN)) + "人" + ChatColor.RESET + ChatColor.GRAY + "を生存ブロックの位置にテレポートしました。");
                    return;
                }
                sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "生存ミッションを実行していないため実行できません。");
                return;
            }
            sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "ゲームが実行されているため実行できません。");
            return;
        }
        MainAPI.sendMessage(sp, MainAPI.ErrorMessage.PERMISSIONS);
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

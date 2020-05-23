package jp.aoichaan0513.A_TosoGame_Live.Commands.Command;

import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI;
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.GameManager;
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.World.WorldConfig;
import jp.aoichaan0513.A_TosoGame_Live.API.Scoreboard.Teams;
import jp.aoichaan0513.A_TosoGame_Live.API.TosoGameAPI;
import jp.aoichaan0513.A_TosoGame_Live.Commands.ICommand;
import jp.aoichaan0513.A_TosoGame_Live.Main;
import jp.aoichaan0513.A_TosoGame_Live.Runnable.RespawnRunnable;
import org.bukkit.GameMode;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class Spec extends ICommand {

    public Spec(String name) {
        super(name);
    }

    @Override
    public void onPlayerCommand(Player sp, Command cmd, String label, String[] args) {
        if (GameManager.isGame(GameManager.GameState.GAME)) {
            if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_JAIL, sp)) {
                if (!TosoGameAPI.isRes || !RespawnRunnable.isAllowRespawn(sp)) {
                    WorldConfig worldConfig = Main.getWorldConfig();

                    if (sp.getGameMode() == GameMode.ADVENTURE) {
                        sp.setGameMode(GameMode.SPECTATOR);
                        sp.getInventory().setHeldItemSlot(0);
                        TosoGameAPI.teleport(sp, worldConfig.getRespawnLocationConfig().getLocations().values());
                        sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.WARNING) + "観戦モードになりました。\n" +
                                MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "\"/spec\"で観戦モードから戻れます。");
                        return;
                    } else {
                        sp.setGameMode(GameMode.ADVENTURE);
                        TosoGameAPI.teleport(sp, worldConfig.getJailLocationConfig().getLocations().values());
                        sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.WARNING) + "観戦モードから戻りました。");
                        return;
                    }
                }
                sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "途中参加・復活が可能なため実行できません。");
                return;
            }
            sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "確保されていないため実行できません。");
            return;
        }
        MainAPI.sendMessage(sp, MainAPI.ErrorMessage.NOT_GAME);
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

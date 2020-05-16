package jp.aoichaan0513.A_TosoGame_Live.Commands.Command;

import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI;
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.GameManager;
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.World.WorldConfig;
import jp.aoichaan0513.A_TosoGame_Live.API.TosoGameAPI;
import jp.aoichaan0513.A_TosoGame_Live.Commands.ICommand;
import jp.aoichaan0513.A_TosoGame_Live.Main;
import jp.aoichaan0513.A_TosoGame_Live.Timer.Timer;
import org.bukkit.Bukkit;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Start extends ICommand {

    public Start(String name) {
        super(name);
    }

    @Override
    public void onPlayerCommand(Player sp, Command cmd, String label, String[] args) {
        if (TosoGameAPI.isAdmin(sp)) {
            if (!GameManager.isGame()) {
                WorldConfig worldConfig = Main.getWorldConfig();

                if (args.length != 0) {
                    if (args[0].equalsIgnoreCase("skip")) {
                        Timer.start(1, worldConfig.getGameConfig().getGame());
                        return;
                    } else {
                        try {
                            int i = Integer.parseInt(args[0]);
                            Bukkit.broadcastMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "カウントダウン開始");
                            Timer.start(i + 1, worldConfig.getGameConfig().getGame());
                            return;
                        } catch (NumberFormatException ex) {
                            sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "数字を指定してください。");
                            return;
                        }
                    }
                }
                Bukkit.broadcastMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "カウントダウン開始");
                Timer.start(worldConfig.getGameConfig().getCountDown(), worldConfig.getGameConfig().getGame());
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
        if (!GameManager.isGame()) {
            WorldConfig worldConfig = Main.getWorldConfig();

            if (args.length != 0) {
                if (args[0].equalsIgnoreCase("skip")) {
                    Timer.start(1, worldConfig.getGameConfig().getGame());
                    return;
                } else {
                    try {
                        int i = Integer.parseInt(args[0]);
                        Bukkit.broadcastMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "カウントダウン開始");
                        Timer.start(i + 1, worldConfig.getGameConfig().getGame());
                        return;
                    } catch (NumberFormatException ex) {
                        bs.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "数字を指定してください。");
                        return;
                    }
                }
            }
            Bukkit.broadcastMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "カウントダウン開始");
            Timer.start(worldConfig.getGameConfig().getCountDown(), worldConfig.getGameConfig().getGame());
            return;
        }
        bs.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "ゲームが実行されているため実行できません。");
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

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (!command.getName().equalsIgnoreCase("start")) return null;
        if (args.length == 1) {
            if (args[0].length() == 0) {
                return Arrays.asList("skip");
            } else {
                if ("skip".startsWith(args[0])) {
                    return Collections.singletonList("skip");
                }
            }
        }
        return null;
    }
}

package jp.aoichaan0513.A_TosoGame_Live.Commands.Command;

import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI;
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.GameManager;
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.OPGameManager;
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.World.WorldConfig;
import jp.aoichaan0513.A_TosoGame_Live.API.OPGame.Dice;
import jp.aoichaan0513.A_TosoGame_Live.API.Scoreboard.Teams;
import jp.aoichaan0513.A_TosoGame_Live.API.TosoGameAPI;
import jp.aoichaan0513.A_TosoGame_Live.Commands.ICommand;
import jp.aoichaan0513.A_TosoGame_Live.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class OPGame extends ICommand {

    public OPGame(String name) {
        super(name);
    }

    @Override
    public void onPlayerCommand(Player sp, Command cmd, String label, String[] args) {
        if (TosoGameAPI.isAdmin(sp)) {
            if (GameManager.isGame(GameManager.GameState.NONE)) {
                if (args.length != 0) {
                    try {
                        WorldConfig worldConfig = Main.getWorldConfig();

                        int i = Integer.parseInt(args[0]);
                        if (i == 1) {
                            if ((Bukkit.getOnlinePlayers().size() - Teams.getOnlineCount(Teams.OnlineTeam.TOSO_ADMIN)) > 0) {
                                GameManager.setGameState(GameManager.GameState.READY);

                                Dice.start();

                                for (Player p : Bukkit.getOnlinePlayers()) {
                                    if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_PLAYER, p)) {
                                        TosoGameAPI.teleport(p, worldConfig.getOPGameLocationConfig().getGOPLocations());
                                    }
                                }
                                OPGameManager.player = Dice.getShufflePlayer();
                                OPGameManager.player.teleport(worldConfig.getOPGameLocationConfig().getOPLocation());
                                sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "オープニングゲームを開始しました。");
                                Bukkit.broadcastMessage(MainAPI.getPrefix(MainAPI.PrefixType.WARNING) + "オープニングゲームを開始しました。\n" +
                                        MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + OPGameManager.player.getName() + "がサイコロを投げます。");
                                OPGameManager.player.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.WARNING) + "\"/shuffle\"と入力してサイコロを投げてください。");
                                return;
                            }
                            sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "逃走者の人数が少ないためオープニングゲームを実行できません。");
                            return;
                        } else if (i == 2) {
                            return;
                        }
                        sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "引数が不正です。数字を指定してください。\n" +
                                MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "コマンドの使い方:\n" +
                                MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "\"/opgame 1\" - サイコロミッション\n" +
                                MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "\"/opgame 2\" - 実装中");
                        return;
                    } catch (NumberFormatException e) {
                        sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "引数が不正です。数字を指定してください。\n" +
                                MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "コマンドの使い方:\n" +
                                MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "\"/opgame 1\" - サイコロミッション\n" +
                                MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "\"/opgame 2\" - 実装中");
                        return;
                    }
                }
                sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "引数が不正です。数字を指定してください。\n" +
                        MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "コマンドの使い方:\n" +
                        MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "\"/opgame 1\" - サイコロミッション\n" +
                        MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "\"/opgame 2\" - 実装中");
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
        if (GameManager.isGame(GameManager.GameState.NONE)) {
            if (args.length != 0) {
                try {
                    WorldConfig worldConfig = Main.getWorldConfig();

                    int i = Integer.parseInt(args[0]);
                    if (i == 1) {
                        if ((Bukkit.getOnlinePlayers().size() - Teams.getOnlineCount(Teams.OnlineTeam.TOSO_ADMIN)) > 0) {
                            GameManager.setGameState(GameManager.GameState.READY);

                            Dice.start();

                            for (Player p : Bukkit.getOnlinePlayers())
                                if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_PLAYER, p))
                                    TosoGameAPI.teleport(p, worldConfig.getOPGameLocationConfig().getGOPLocations());

                            OPGameManager.player = Dice.getShufflePlayer();
                            OPGameManager.player.teleport(worldConfig.getOPGameLocationConfig().getOPLocation());

                            bs.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "オープニングゲームを開始しました。");
                            Bukkit.broadcastMessage(MainAPI.getPrefix(MainAPI.PrefixType.WARNING) + "オープニングゲームを開始しました。\n" +
                                    MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + OPGameManager.player.getName() + "がサイコロを投げます。");
                            OPGameManager.player.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.WARNING) + "\"/shuffle\"と入力してサイコロを投げてください。");
                            return;
                        }
                        bs.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "逃走者の人数が少ないためオープニングゲームを実行できません。");
                        return;
                    } else if (i == 2) {
                        return;
                    }
                    bs.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "引数が不正です。数字を指定してください。\n" +
                            MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "コマンドの使い方:\n" +
                            MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "\"/opgame 1\" - サイコロミッション\n" +
                            MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "\"/opgame 2\" - 実装中");
                    return;
                } catch (NumberFormatException e) {
                    bs.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "引数が不正です。数字を指定してください。\n" +
                            MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "コマンドの使い方:\n" +
                            MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "\"/opgame 1\" - サイコロミッション\n" +
                            MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "\"/opgame 2\" - 実装中");
                    return;
                }
            }
            bs.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "引数が不正です。数字を指定してください。\n" +
                    MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "コマンドの使い方:\n" +
                    MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "\"/opgame 1\" - サイコロミッション\n" +
                    MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "\"/opgame 2\" - 実装中");
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
        if (TosoGameAPI.isAdmin(sp)) {
            if (args.length == 1) {
                if (args[0].length() == 0) {
                    return Arrays.asList("1", "2");
                } else {
                    if ("1".startsWith(args[0])) {
                        return Collections.singletonList("1");
                    } else if ("2".startsWith(args[0])) {
                        return Collections.singletonList("2");
                    }
                }
            }
        }
        return null;
    }

    @Override
    public List<String> onBlockTabComplete(BlockCommandSender bs, Command cmd, String alias, String[] args) {
        if (args.length == 1) {
            if (args[0].length() == 0) {
                return Arrays.asList("1", "2");
            } else {
                if ("1".startsWith(args[0])) {
                    return Collections.singletonList("1");
                } else if ("2".startsWith(args[0])) {
                    return Collections.singletonList("2");
                }
            }
        }
        return null;
    }

    @Override
    public List<String> onConsoleTabComplete(ConsoleCommandSender cs, Command cmd, String alias, String[] args) {
        return null;
    }
}

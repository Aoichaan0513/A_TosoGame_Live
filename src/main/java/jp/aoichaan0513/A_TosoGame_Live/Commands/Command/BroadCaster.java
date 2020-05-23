package jp.aoichaan0513.A_TosoGame_Live.Commands.Command;

import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI;
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.Player.PlayerConfig;
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.Player.PlayerManager;
import jp.aoichaan0513.A_TosoGame_Live.API.TosoGameAPI;
import jp.aoichaan0513.A_TosoGame_Live.Commands.ICommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.Map;
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
                                    PlayerManager.loadConfig(p).setBroadCaster(true);

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
                                    PlayerManager.loadConfig(p).setBroadCaster(false);

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
                        StringBuffer stringBuffer = new StringBuffer();
                        for (Map.Entry<UUID, PlayerConfig> entry : PlayerManager.getConfigs())
                            if (entry.getValue().getBroadCaster())
                                stringBuffer.append(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + Bukkit.getOfflinePlayer(entry.getKey()).getName() + "\n");

                        sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.WARNING) + "配信者リスト:\n" +
                                stringBuffer.toString().trim());
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
                                PlayerManager.loadConfig(p).setBroadCaster(true);

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
                                PlayerManager.loadConfig(p).setBroadCaster(false);

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
                    StringBuffer stringBuffer = new StringBuffer();
                    for (Map.Entry<UUID, PlayerConfig> entry : PlayerManager.getConfigs())
                        if (entry.getValue().getBroadCaster())
                            stringBuffer.append(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + Bukkit.getOfflinePlayer(entry.getKey()).getName() + "\n");

                    bs.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.WARNING) + "配信者リスト:\n" +
                            stringBuffer.toString().trim());
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
                                PlayerManager.loadConfig(p).setBroadCaster(true);

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
                                PlayerManager.loadConfig(p).setBroadCaster(false);

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
                    StringBuffer stringBuffer = new StringBuffer();
                    for (Map.Entry<UUID, PlayerConfig> entry : PlayerManager.getConfigs())
                        if (entry.getValue().getBroadCaster())
                            stringBuffer.append(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + Bukkit.getOfflinePlayer(entry.getKey()).getName() + "\n");

                    cs.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.WARNING) + "配信者リスト:\n" +
                            stringBuffer.toString().trim());
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
        if (!TosoGameAPI.isAdmin(sp) || args.length != 1) return null;
        return getTabList(args[0], new HashSet<>(Arrays.asList("add", "remove", "list")));
    }

    @Override
    public List<String> onBlockTabComplete(BlockCommandSender bs, Command cmd, String alias, String[] args) {
        if (args.length != 1) return null;
        return getTabList(args[0], new HashSet<>(Arrays.asList("add", "remove", "list")));
    }

    @Override
    public List<String> onConsoleTabComplete(ConsoleCommandSender cs, Command cmd, String alias, String[] args) {
        if (args.length != 1) return null;
        return getTabList(args[0], new HashSet<>(Arrays.asList("add", "remove", "list")));
    }
}

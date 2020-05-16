package jp.aoichaan0513.A_TosoGame_Live.Commands.Command;

import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI;
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.ActionBarManager;
import jp.aoichaan0513.A_TosoGame_Live.API.TosoGameAPI;
import jp.aoichaan0513.A_TosoGame_Live.Commands.ICommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Disappear extends ICommand {

    public Disappear(String name) {
        super(name);
    }

    @Override
    public void onPlayerCommand(Player sp, Command cmd, String label, String[] args) {
        if (TosoGameAPI.isAdmin(sp)) {
            if (args.length != 0) {
                for (String name : args) {
                    Player target = Bukkit.getPlayerExact(name);
                    if (target == null) {
                        sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + ChatColor.RED + ChatColor.UNDERLINE + name + ChatColor.RESET + ChatColor.GRAY + "はオフラインです。");
                        continue;
                    } else {
                        Player p = Bukkit.getPlayerExact(name);
                        if (!MainAPI.isHidePlayer(p)) {
                            for (Player player : Bukkit.getOnlinePlayers())
                                player.hidePlayer(p);
                            MainAPI.addHidePlayer(p, true);

                            sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SUCCESS) + p.getName() + "の姿を非表示にしました。");

                            ActionBarManager.sendActionBar(p, ChatColor.RED + "あなたの姿は非表示になっています。");
                            continue;
                        }
                        sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + ChatColor.RED + ChatColor.UNDERLINE + p.getName() + ChatColor.RESET + ChatColor.GRAY + "はすでに姿が非表示になっています。");
                        continue;
                    }
                }
                return;
            } else {
                if (!MainAPI.isHidePlayer(sp)) {
                    for (Player player : Bukkit.getOnlinePlayers())
                        player.hidePlayer(sp);
                    MainAPI.addHidePlayer(sp, true);

                    sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SUCCESS) + ChatColor.GREEN + "あなたの姿を非表示にしました。");

                    ActionBarManager.sendActionBar(sp, ChatColor.RED + "あなたの姿は非表示になっています。");
                    return;
                }
                sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "すでに姿が非表示になっています。");
                return;
            }
        }
        MainAPI.sendMessage(sp, MainAPI.ErrorMessage.PERMISSIONS);
        return;
    }

    @Override
    public void onBlockCommand(BlockCommandSender bs, Command cmd, String label, String[] args) {
        if (args.length != 0) {
            for (String name : args) {
                Player target = Bukkit.getPlayerExact(name);
                if (target == null) {
                    bs.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + ChatColor.RED + ChatColor.UNDERLINE + name + ChatColor.RESET + ChatColor.GRAY + "はオフラインです。");
                    continue;
                } else {
                    Player p = Bukkit.getPlayerExact(name);
                    if (!MainAPI.isHidePlayer(p)) {
                        for (Player player : Bukkit.getOnlinePlayers())
                            player.hidePlayer(p);
                        MainAPI.addHidePlayer(p, true);

                        bs.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SUCCESS) + p.getName() + "の姿を非表示にしました。");

                        ActionBarManager.sendActionBar(p, ChatColor.RED + "あなたの姿は非表示になっています。");
                        continue;
                    }
                    bs.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + ChatColor.RED + ChatColor.UNDERLINE + p.getName() + ChatColor.RESET + ChatColor.GRAY + "はすでに姿が非表示になっています。");
                    continue;
                }
            }
            return;
        }
        MainAPI.sendMessage(bs, MainAPI.ErrorMessage.ARGS_PLAYER);
        return;
    }

    @Override
    public void onConsoleCommand(ConsoleCommandSender cs, Command cmd, String label, String[] args) {
        if (args.length != 0) {
            for (String name : args) {
                Player target = Bukkit.getPlayerExact(name);
                if (target == null) {
                    cs.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + ChatColor.RED + ChatColor.UNDERLINE + name + ChatColor.RESET + ChatColor.GRAY + "はオフラインです。");
                    continue;
                } else {
                    Player p = Bukkit.getPlayerExact(name);
                    if (!MainAPI.isHidePlayer(p)) {
                        for (Player player : Bukkit.getOnlinePlayers())
                            player.hidePlayer(p);
                        MainAPI.addHidePlayer(p, true);

                        cs.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SUCCESS) + p.getName() + "の姿を非表示にしました。");

                        ActionBarManager.sendActionBar(p, ChatColor.RED + "あなたの姿は非表示になっています。");
                        continue;
                    }
                    cs.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + ChatColor.RED + ChatColor.UNDERLINE + p.getName() + ChatColor.RESET + ChatColor.GRAY + "はすでに姿が非表示になっています。");
                    continue;
                }
            }
            return;
        }
        MainAPI.sendMessage(cs, MainAPI.ErrorMessage.ARGS_PLAYER);
        return;
    }

    @Override
    public List<String> onPlayerTabComplete(Player sp, Command cmd, String alias, String[] args) {
        if (TosoGameAPI.isAdmin(sp)) {
            if (args.length == 1) {
                if (args[0].length() == 0) {
                    ArrayList<String> list = new ArrayList<>();

                    for (Player player : Bukkit.getOnlinePlayers())
                        if (!MainAPI.isHidePlayer(player))
                            list.add(player.getName());
                    return list;
                } else {
                    ArrayList<String> list = new ArrayList<>();

                    if (list.isEmpty()) {
                        for (Player player : Bukkit.getOnlinePlayers())
                            if (!MainAPI.isHidePlayer(player))
                                list.add(player.getName());

                        for (String str : list)
                            if (str.startsWith(args[0]))
                                return Collections.singletonList(str);
                    } else {
                        for (String str : list)
                            if (str.startsWith(args[0]))
                                return Collections.singletonList(str);
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
                ArrayList<String> list = new ArrayList<>();

                for (Player player : Bukkit.getOnlinePlayers())
                    if (!MainAPI.isHidePlayer(player))
                        list.add(player.getName());
                return list;
            } else {
                ArrayList<String> list = new ArrayList<>();

                if (list.isEmpty()) {
                    for (Player player : Bukkit.getOnlinePlayers())
                        if (!MainAPI.isHidePlayer(player))
                            list.add(player.getName());

                    for (String str : list)
                        if (str.startsWith(args[0]))
                            return Collections.singletonList(str);
                } else {
                    for (String str : list)
                        if (str.startsWith(args[0]))
                            return Collections.singletonList(str);
                }
            }
        }
        return null;
    }

    @Override
    public List<String> onConsoleTabComplete(ConsoleCommandSender cs, Command cmd, String alias, String[] args) {
        if (args.length == 1) {
            if (args[0].length() == 0) {
                ArrayList<String> list = new ArrayList<>();

                for (Player player : Bukkit.getOnlinePlayers())
                    if (!MainAPI.isHidePlayer(player))
                        list.add(player.getName());
                return list;
            } else {
                ArrayList<String> list = new ArrayList<>();

                if (list.isEmpty()) {
                    for (Player player : Bukkit.getOnlinePlayers())
                        if (!MainAPI.isHidePlayer(player))
                            list.add(player.getName());

                    for (String str : list)
                        if (str.startsWith(args[0]))
                            return Collections.singletonList(str);
                } else {
                    for (String str : list)
                        if (str.startsWith(args[0]))
                            return Collections.singletonList(str);
                }
            }
        }
        return null;
    }
}

package jp.aoichaan0513.A_TosoGame_Live.Commands.Command;

import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI;
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.GameManager;
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.MissionManager;
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.World.WorldConfig;
import jp.aoichaan0513.A_TosoGame_Live.API.Scoreboard.Teams;
import jp.aoichaan0513.A_TosoGame_Live.API.TosoGameAPI;
import jp.aoichaan0513.A_TosoGame_Live.Commands.ICommand;
import jp.aoichaan0513.A_TosoGame_Live.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class Tuho extends ICommand {

    public static int num = -1;

    public Tuho(String name) {
        super(name);
    }

    @Override
    public void onPlayerCommand(Player sp, Command cmd, String label, String[] args) {
        if (TosoGameAPI.isAdmin(sp)) {
            if (args.length != 0) {
                WorldConfig worldConfig = Main.getWorldConfig();

                if (args[0].equalsIgnoreCase("join")) {
                    if (args.length != 1) {
                        if (args[1].startsWith("@")) {
                            String arg = args[1].substring(1);
                            if (arg.equalsIgnoreCase("all")) {
                                int i = Teams.getOnlineCount(Teams.OnlineTeam.TOSO_JAIL);
                                for (Player p : Bukkit.getOnlinePlayers()) {
                                    if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_JAIL, p)) {
                                        Teams.joinTeam(Teams.OnlineTeam.TOSO_TUHO, p);
                                        p.setGameMode(GameMode.ADVENTURE);

                                        TosoGameAPI.setArmor(p);
                                        TosoGameAPI.removeOp(p);

                                        Main.opGamePlayerSet.remove(p.getUniqueId());

                                        TosoGameAPI.showPlayers(p);
                                        p.teleport(worldConfig.getHunterLocationConfig().getLocation(1));

                                        if (MissionManager.isBossBar())
                                            MissionManager.getBossBar().removePlayer(p);

                                        p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "あなたを通報部隊に追加しました。");
                                        Bukkit.broadcastMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + p.getName() + "が通報部隊になりました。");
                                    }
                                }
                                sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + ChatColor.GREEN + ChatColor.UNDERLINE + i + "人" + ChatColor.RESET + ChatColor.GRAY + "を通報部隊に追加しました。");
                                return;
                            } else if (arg.startsWith("team")) {
                                if (arg.length() > 5) {
                                    String team = arg.substring(5);
                                    if (team.equalsIgnoreCase("admin")) {
                                        int i = Teams.getOnlineCount(Teams.OnlineTeam.TOSO_ADMIN);
                                        for (Player p : Bukkit.getOnlinePlayers()) {
                                            if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_ADMIN, p)) {
                                                Teams.joinTeam(Teams.OnlineTeam.TOSO_TUHO, p);
                                                p.setGameMode(GameMode.ADVENTURE);

                                                TosoGameAPI.setArmor(p);
                                                TosoGameAPI.removeOp(p);

                                                Main.opGamePlayerSet.remove(p.getUniqueId());

                                                TosoGameAPI.showPlayers(p);
                                                p.teleport(worldConfig.getHunterLocationConfig().getLocation(1));

                                                if (MissionManager.isBossBar())
                                                    MissionManager.getBossBar().removePlayer(p);

                                                p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "あなたを通報部隊に追加しました。");
                                                Bukkit.broadcastMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + p.getName() + "が通報部隊になりました。");
                                            }
                                        }
                                        sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + ChatColor.GREEN + ChatColor.UNDERLINE + i + "人" + ChatColor.RESET + ChatColor.GRAY + "を通報部隊に追加しました。");
                                        return;
                                    } else if (team.equalsIgnoreCase("player")) {
                                        int i = Teams.getOnlineCount(Teams.OnlineTeam.TOSO_PLAYER);
                                        for (Player p : Bukkit.getOnlinePlayers()) {
                                            if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_PLAYER, p)) {
                                                Teams.joinTeam(Teams.OnlineTeam.TOSO_TUHO, p);
                                                p.setGameMode(GameMode.ADVENTURE);

                                                TosoGameAPI.setArmor(p);
                                                TosoGameAPI.removeOp(p);

                                                Main.opGamePlayerSet.remove(p.getUniqueId());

                                                TosoGameAPI.showPlayers(p);
                                                p.teleport(worldConfig.getHunterLocationConfig().getLocation(1));

                                                if (MissionManager.isBossBar())
                                                    MissionManager.getBossBar().removePlayer(p);

                                                p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "あなたを通報部隊に追加しました。");
                                                Bukkit.broadcastMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + p.getName() + "が通報部隊になりました。");
                                            }
                                        }
                                        sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + ChatColor.GREEN + ChatColor.UNDERLINE + i + "人" + ChatColor.RESET + ChatColor.GRAY + "を通報部隊に追加しました。");
                                        return;
                                    } else if (team.equalsIgnoreCase("success")) {
                                        int i = Teams.getOnlineCount(Teams.OnlineTeam.TOSO_SUCCESS);
                                        for (Player p : Bukkit.getOnlinePlayers()) {
                                            if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_SUCCESS, p)) {
                                                Teams.joinTeam(Teams.OnlineTeam.TOSO_TUHO, p);
                                                p.setGameMode(GameMode.ADVENTURE);

                                                TosoGameAPI.setArmor(p);
                                                TosoGameAPI.removeOp(p);

                                                Main.opGamePlayerSet.remove(p.getUniqueId());

                                                TosoGameAPI.showPlayers(p);
                                                p.teleport(worldConfig.getHunterLocationConfig().getLocation(1));

                                                if (MissionManager.isBossBar())
                                                    MissionManager.getBossBar().removePlayer(p);

                                                p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "あなたを通報部隊に追加しました。");
                                                Bukkit.broadcastMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + p.getName() + "が通報部隊になりました。");
                                            }
                                        }
                                        sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + ChatColor.GREEN + ChatColor.UNDERLINE + i + "人" + ChatColor.RESET + ChatColor.GRAY + "を通報部隊に追加しました。");
                                        return;
                                    } else if (team.equalsIgnoreCase("jail")) {
                                        int i = Teams.getOnlineCount(Teams.OnlineTeam.TOSO_JAIL);
                                        for (Player p : Bukkit.getOnlinePlayers()) {
                                            if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_JAIL, p)) {
                                                Teams.joinTeam(Teams.OnlineTeam.TOSO_TUHO, p);
                                                p.setGameMode(GameMode.ADVENTURE);

                                                TosoGameAPI.setArmor(p);
                                                TosoGameAPI.removeOp(p);

                                                Main.opGamePlayerSet.remove(p.getUniqueId());

                                                TosoGameAPI.showPlayers(p);
                                                p.teleport(worldConfig.getHunterLocationConfig().getLocation(1));

                                                if (MissionManager.isBossBar())
                                                    MissionManager.getBossBar().removePlayer(p);

                                                p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "あなたを通報部隊に追加しました。");
                                                Bukkit.broadcastMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + p.getName() + "が通報部隊になりました。");
                                            }
                                        }
                                        sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + ChatColor.GREEN + ChatColor.UNDERLINE + i + "人" + ChatColor.RESET + ChatColor.GRAY + "を通報部隊に追加しました。");
                                        return;
                                    } else if (team.equalsIgnoreCase("hunter")) {
                                        int i = Teams.getOnlineCount(Teams.OnlineTeam.TOSO_HUNTER);
                                        for (Player p : Bukkit.getOnlinePlayers()) {
                                            if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_HUNTER, p)) {
                                                Teams.joinTeam(Teams.OnlineTeam.TOSO_TUHO, p);
                                                p.setGameMode(GameMode.ADVENTURE);

                                                TosoGameAPI.setArmor(p);
                                                TosoGameAPI.removeOp(p);

                                                Main.opGamePlayerSet.remove(p.getUniqueId());

                                                TosoGameAPI.showPlayers(p);
                                                p.teleport(worldConfig.getHunterLocationConfig().getLocation(1));

                                                if (MissionManager.isBossBar())
                                                    MissionManager.getBossBar().removePlayer(p);

                                                p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "あなたを通報部隊に追加しました。");
                                                Bukkit.broadcastMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + p.getName() + "が通報部隊になりました。");
                                            }
                                        }
                                        sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + ChatColor.GREEN + ChatColor.UNDERLINE + i + "人" + ChatColor.RESET + ChatColor.GRAY + "を通報部隊に追加しました。");
                                        return;
                                    }
                                }
                                sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "引数が不正です。チーム名を指定してください。");
                                return;
                            }
                        } else {
                            for (int i = 1; i < args.length; i++) {
                                Player target = Bukkit.getPlayerExact(args[i]);
                                if (target == null) {
                                    sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + ChatColor.RED + ChatColor.UNDERLINE + args[i] + ChatColor.RESET + ChatColor.GRAY + "はオフラインです。");
                                    continue;
                                } else {
                                    Player p = Bukkit.getPlayerExact(args[i]);
                                    if (!Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_TUHO, p)) {
                                        Teams.joinTeam(Teams.OnlineTeam.TOSO_TUHO, p);
                                        p.setGameMode(GameMode.ADVENTURE);

                                        TosoGameAPI.setArmor(p);
                                        TosoGameAPI.removeOp(p);

                                        Main.opGamePlayerSet.remove(p.getUniqueId());

                                        TosoGameAPI.showPlayers(p);
                                        p.teleport(worldConfig.getHunterLocationConfig().getLocation(1));

                                        if (MissionManager.isBossBar())
                                            MissionManager.getBossBar().removePlayer(p);

                                        p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "あなたを通報部隊に追加しました。");
                                        Bukkit.broadcastMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + p.getName() + "が通報部隊になりました。");
                                        continue;
                                    }
                                    sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + p.getName() + "はすでに通報部隊になっています。");
                                    continue;
                                }
                            }
                            return;
                        }
                    }
                    MainAPI.sendMessage(sp, MainAPI.ErrorMessage.ARGS_PLAYER);
                    return;
                } else if (args[0].equalsIgnoreCase("leave")) {
                    if (args.length != 1) {
                        if (args[1].startsWith("@")) {
                            String arg = args[1].substring(1);
                            if (arg.equalsIgnoreCase("all")) {
                                int i = Teams.getOnlineCount(Teams.OnlineTeam.TOSO_TUHO);
                                for (Player p : Bukkit.getOnlinePlayers()) {
                                    if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_TUHO, p)) {
                                        Teams.joinTeam(Teams.OnlineTeam.TOSO_JAIL, p);
                                        p.setGameMode(GameMode.ADVENTURE);

                                        for (PotionEffect effect : p.getActivePotionEffects())
                                            p.removePotionEffect(effect.getType());

                                        TosoGameAPI.removeArmor(p);
                                        TosoGameAPI.removeOp(p);

                                        Main.opGamePlayerSet.remove(p.getUniqueId());

                                        TosoGameAPI.showPlayers(p);

                                        TosoGameAPI.teleport(p, worldConfig.getJailLocationConfig().getLocations().values());

                                        if (MissionManager.isBossBar())
                                            MissionManager.getBossBar().addPlayer(p);

                                        p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "あなたを牢獄に追加しました。");
                                        Bukkit.broadcastMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + p.getName() + "が通報部隊から抜けました。");
                                    }
                                }
                                sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + ChatColor.GREEN + ChatColor.UNDERLINE + i + "人" + ChatColor.RESET + ChatColor.GRAY + "が通報部隊から抜けました。");
                                return;
                            } else if (arg.startsWith("team")) {
                                if (arg.length() > 5) {
                                    String team = arg.substring(5);
                                    if (team.equalsIgnoreCase("admin")) {
                                        int i = Teams.getOnlineCount(Teams.OnlineTeam.TOSO_TUHO);
                                        for (Player p : Bukkit.getOnlinePlayers()) {
                                            if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_TUHO, p)) {
                                                Teams.joinTeam(Teams.OnlineTeam.TOSO_ADMIN, p);
                                                p.setGameMode(GameMode.CREATIVE);

                                                for (PotionEffect effect : p.getActivePotionEffects())
                                                    p.removePotionEffect(effect.getType());

                                                TosoGameAPI.removeArmor(p);
                                                TosoGameAPI.addOp(p);

                                                Main.opGamePlayerSet.remove(p.getUniqueId());

                                                TosoGameAPI.showPlayers(p);

                                                TosoGameAPI.teleport(p, worldConfig.getRespawnLocationConfig().getLocations().values());

                                                if (MissionManager.isBossBar())
                                                    MissionManager.getBossBar().addPlayer(p);

                                                p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "あなたを運営に追加しました。");
                                                Bukkit.broadcastMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + p.getName() + "が通報部隊から抜けました。");
                                            }
                                        }
                                        sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + ChatColor.GREEN + ChatColor.UNDERLINE + i + "人" + ChatColor.RESET + ChatColor.GRAY + "が通報部隊から抜けました。");
                                        return;
                                    } else if (team.equalsIgnoreCase("player")) {
                                        int i = Teams.getOnlineCount(Teams.OnlineTeam.TOSO_TUHO);
                                        for (Player p : Bukkit.getOnlinePlayers()) {
                                            if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_TUHO, p)) {
                                                Teams.joinTeam(Teams.OnlineTeam.TOSO_PLAYER, p);
                                                p.setGameMode(GameMode.ADVENTURE);

                                                TosoGameAPI.removeArmor(p);
                                                TosoGameAPI.setPotionEffect(p, true);
                                                TosoGameAPI.removeOp(p);

                                                Main.opGamePlayerSet.remove(p.getUniqueId());

                                                TosoGameAPI.showPlayers(p);
                                                TosoGameAPI.teleport(p, worldConfig.getRespawnLocationConfig().getLocations().values());

                                                if (MissionManager.isBossBar())
                                                    MissionManager.getBossBar().addPlayer(p);

                                                p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "あなたを逃走者に追加しました。");
                                                Bukkit.broadcastMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + p.getName() + "が通報部隊から抜けました。");
                                            }
                                        }
                                        sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + ChatColor.GREEN + ChatColor.UNDERLINE + i + "人" + ChatColor.RESET + ChatColor.GRAY + "が通報部隊から抜けました。");
                                        return;
                                    } else if (team.equalsIgnoreCase("success")) {
                                        int i = Teams.getOnlineCount(Teams.OnlineTeam.TOSO_TUHO);
                                        for (Player p : Bukkit.getOnlinePlayers()) {
                                            if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_TUHO, p)) {
                                                Teams.joinTeam(Teams.OnlineTeam.TOSO_SUCCESS, p);
                                                p.setGameMode(GameMode.ADVENTURE);

                                                TosoGameAPI.removeArmor(p);
                                                TosoGameAPI.setPotionEffect(p, true);
                                                TosoGameAPI.removeOp(p);

                                                Main.opGamePlayerSet.remove(p.getUniqueId());

                                                TosoGameAPI.showPlayers(p);
                                                TosoGameAPI.teleport(p, worldConfig.getRespawnLocationConfig().getLocations().values());

                                                if (MissionManager.isBossBar())
                                                    MissionManager.getBossBar().addPlayer(p);

                                                p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "あなたを生存者に追加しました。");
                                                Bukkit.broadcastMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + p.getName() + "が通報部隊から抜けました。");
                                            }
                                        }
                                        sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + ChatColor.GREEN + ChatColor.UNDERLINE + i + "人" + ChatColor.RESET + ChatColor.GRAY + "が通報部隊から抜けました。");
                                        return;
                                    } else if (team.equalsIgnoreCase("jail")) {
                                        int i = Teams.getOnlineCount(Teams.OnlineTeam.TOSO_TUHO);
                                        for (Player p : Bukkit.getOnlinePlayers()) {
                                            if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_TUHO, p)) {
                                                Teams.joinTeam(Teams.OnlineTeam.TOSO_JAIL, p);
                                                p.setGameMode(GameMode.ADVENTURE);

                                                TosoGameAPI.removeArmor(p);
                                                TosoGameAPI.setPotionEffect(p, true);
                                                TosoGameAPI.removeOp(p);

                                                Main.opGamePlayerSet.remove(p.getUniqueId());

                                                TosoGameAPI.showPlayers(p);
                                                TosoGameAPI.teleport(p, worldConfig.getJailLocationConfig().getLocations().values());

                                                if (MissionManager.isBossBar())
                                                    MissionManager.getBossBar().addPlayer(p);

                                                p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "あなたを牢獄に追加しました。");
                                                Bukkit.broadcastMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + p.getName() + "が通報部隊から抜けました。");
                                            }
                                        }
                                        sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + ChatColor.GREEN + ChatColor.UNDERLINE + i + "人" + ChatColor.RESET + ChatColor.GRAY + "が通報部隊から抜けました。");
                                        return;
                                    } else if (team.equalsIgnoreCase("hunter")) {
                                        int i = Teams.getOnlineCount(Teams.OnlineTeam.TOSO_TUHO);
                                        for (Player p : Bukkit.getOnlinePlayers()) {
                                            if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_TUHO, p)) {
                                                Teams.joinTeam(Teams.OnlineTeam.TOSO_HUNTER, p);
                                                p.setGameMode(GameMode.ADVENTURE);

                                                TosoGameAPI.setArmor(p);
                                                TosoGameAPI.setPotionEffect(p, true);
                                                TosoGameAPI.removeOp(p);

                                                Main.opGamePlayerSet.remove(p.getUniqueId());

                                                TosoGameAPI.showPlayers(p);
                                                p.teleport(worldConfig.getHunterLocationConfig().getLocation(1));

                                                if (MissionManager.isBossBar())
                                                    MissionManager.getBossBar().addPlayer(p);

                                                p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "あなたをハンターに追加しました。");
                                                Bukkit.broadcastMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + p.getName() + "が通報部隊から抜けました。");
                                            }
                                        }
                                        sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + ChatColor.GREEN + ChatColor.UNDERLINE + i + "人" + ChatColor.RESET + ChatColor.GRAY + "が通報部隊から抜けました。");
                                        return;
                                    }
                                }
                                sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "引数が不正です。チーム名を指定してください。");
                                return;
                            }
                        } else {
                            for (int i = 1; i < args.length; i++) {
                                Player target = Bukkit.getPlayerExact(args[i]);
                                if (target == null) {
                                    sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + ChatColor.RED + ChatColor.UNDERLINE + args[i] + ChatColor.RESET + ChatColor.GRAY + "はオフラインです。");
                                    continue;
                                } else {
                                    Player p = Bukkit.getPlayerExact(args[i]);
                                    if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_TUHO, p)) {
                                        Teams.joinTeam(Teams.OnlineTeam.TOSO_JAIL, p);
                                        p.setGameMode(GameMode.ADVENTURE);

                                        TosoGameAPI.setPotionEffect(p, true);
                                        TosoGameAPI.removeArmor(p);
                                        TosoGameAPI.removeOp(p);

                                        Main.opGamePlayerSet.remove(p.getUniqueId());

                                        TosoGameAPI.showPlayers(p);
                                        TosoGameAPI.teleport(p, worldConfig.getJailLocationConfig().getLocations().values());

                                        if (MissionManager.isBossBar())
                                            MissionManager.getBossBar().addPlayer(p);

                                        p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "あなたを牢獄に追加しました。");
                                        Bukkit.broadcastMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + p.getName() + "が通報部隊から抜けました。");
                                        continue;
                                    }
                                    sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + p.getName() + "はすでに通報部隊から抜けています。");
                                    continue;
                                }
                            }
                            return;
                        }
                    }
                    MainAPI.sendMessage(sp, MainAPI.ErrorMessage.ARGS_PLAYER);
                    return;
                } else if (args[0].equalsIgnoreCase("random") || args[0].equalsIgnoreCase("rand")) {
                    if (args.length != 1) {
                        random(sp, args[1], worldConfig);
                        return;
                    }
                    MainAPI.sendMessage(sp, MainAPI.ErrorMessage.ARGS_INTEGER);
                    return;
                }
                sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "引数が不正です。\n" +
                        MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "コマンドの使い方:\n" +
                        MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "\"/tuho join <プレイヤー名>\" - 通報部隊に参加\n" +
                        MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "\"/tuho leave <プレイヤー名>\" - 通報部隊から離脱\n" +
                        MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "\"/tuho random <数値>\" または \"/tuho rand <数値>\" - 数値の数だけ通報部隊をランダムで追加");
                return;
            }
            sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "引数が不正です。\n" +
                    MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "コマンドの使い方:\n" +
                    MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "\"/tuho join <プレイヤー名>\" - 通報部隊に参加\n" +
                    MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "\"/tuho leave <プレイヤー名>\" - 通報部隊から離脱\n" +
                    MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "\"/tuho random <数値>\" または \"/tuho rand <数値>\" - 数値の数だけ通報部隊をランダムで追加");
            return;
        }
        MainAPI.sendMessage(sp, MainAPI.ErrorMessage.PERMISSIONS);
        return;
    }

    @Override
    public void onBlockCommand(BlockCommandSender bs, Command cmd, String label, String[] args) {
        if (args.length != 0) {
            WorldConfig worldConfig = Main.getWorldConfig();

            if (args[0].equalsIgnoreCase("join")) {
                if (args.length != 1) {
                    for (int i = 1; i < args.length; i++) {
                        Player target = Bukkit.getPlayerExact(args[i]);
                        if (target == null) {
                            bs.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + ChatColor.RED + ChatColor.UNDERLINE + args[i] + ChatColor.RESET + ChatColor.GRAY + "はオフラインです。");
                            continue;
                        } else {
                            Player p = Bukkit.getPlayerExact(args[i]);
                            if (!Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_TUHO, p)) {
                                Teams.joinTeam(Teams.OnlineTeam.TOSO_TUHO, p);
                                p.setGameMode(GameMode.ADVENTURE);

                                TosoGameAPI.setArmor(p);
                                TosoGameAPI.setPotionEffect(p, true);
                                TosoGameAPI.removeOp(p);

                                Main.opGamePlayerSet.remove(p.getUniqueId());

                                TosoGameAPI.showPlayers(p);
                                p.teleport(worldConfig.getHunterLocationConfig().getLocation(1));

                                if (MissionManager.isBossBar())
                                    MissionManager.getBossBar().removePlayer(p);

                                p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "あなたを通報部隊に追加しました。");
                                Bukkit.broadcastMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + p.getName() + "が通報部隊になりました。");
                                continue;
                            }
                            bs.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + p.getName() + "はすでに通報部隊になっています。");
                            continue;
                        }
                    }
                    return;
                }
                MainAPI.sendMessage(bs, MainAPI.ErrorMessage.ARGS_PLAYER);
                return;
            } else if (args[0].equalsIgnoreCase("leave")) {
                if (args.length != 1) {
                    for (int i = 1; i < args.length; i++) {
                        Player target = Bukkit.getPlayerExact(args[i]);
                        if (target == null) {
                            bs.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + ChatColor.RED + ChatColor.UNDERLINE + args[i] + ChatColor.RESET + ChatColor.GRAY + "はオフラインです。");
                            continue;
                        } else {
                            Player p = Bukkit.getPlayerExact(args[i]);
                            if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_TUHO, p)) {
                                Teams.joinTeam(Teams.OnlineTeam.TOSO_JAIL, p);
                                p.setGameMode(GameMode.ADVENTURE);

                                TosoGameAPI.removeArmor(p);
                                TosoGameAPI.setPotionEffect(p, true);
                                TosoGameAPI.removeOp(p);

                                Main.opGamePlayerSet.remove(p.getUniqueId());

                                TosoGameAPI.showPlayers(p);
                                TosoGameAPI.teleport(p, worldConfig.getJailLocationConfig().getLocations().values());

                                if (MissionManager.isBossBar())
                                    MissionManager.getBossBar().addPlayer(p);

                                p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "あなたを牢獄に追加しました。");
                                Bukkit.broadcastMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + p.getName() + "が通報部隊から抜けました。");
                                continue;
                            }
                            bs.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + p.getName() + "はすでに通報部隊から抜けています。");
                            continue;
                        }
                    }
                    return;
                }
                MainAPI.sendMessage(bs, MainAPI.ErrorMessage.ARGS_PLAYER);
                return;
            } else if (args[0].equalsIgnoreCase("random") || args[0].equalsIgnoreCase("rand")) {
                if (args.length != 1) {
                    random(bs, args[1], worldConfig);
                    return;
                }
                MainAPI.sendMessage(bs, MainAPI.ErrorMessage.ARGS_INTEGER);
                return;
            }
            bs.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "引数が不正です。\n" +
                    MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "コマンドの使い方:\n" +
                    MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "\"/tuho join <プレイヤー名>\" - 通報部隊に参加\n" +
                    MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "\"/tuho leave <プレイヤー名>\" - 通報部隊から離脱\n" +
                    MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "\"/tuho random <数値>\" または \"/tuho rand <数値>\" - 数値の数だけ通報部隊をランダムで追加");
            return;
        }
        bs.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "引数が不正です。\n" +
                MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "コマンドの使い方:\n" +
                MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "\"/tuho join <プレイヤー名>\" - 通報部隊に参加\n" +
                MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "\"/tuho leave <プレイヤー名>\" - 通報部隊から離脱\n" +
                MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "\"/tuho random <数値>\" または \"/tuho rand <数値>\" - 数値の数だけ通報部隊をランダムで追加");
        return;
    }

    @Override
    public void onConsoleCommand(ConsoleCommandSender cs, Command cmd, String label, String[] args) {
        MainAPI.sendMessage(cs, MainAPI.ErrorMessage.NOT_PLAYER);
        return;
    }

    @Override
    public List<String> onPlayerTabComplete(Player sp, Command cmd, String alias, String[] args) {
        if (!TosoGameAPI.isAdmin(sp)) return null;
        if (args.length == 1) {
            return getTabList(args[0], new HashSet<>(Arrays.asList("join", "leave", "random", "rand")));
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("join") || args[0].equalsIgnoreCase("leave")) {
                Set<String> set = new HashSet<>(Arrays.asList("@all", "@team:admin", "@team:player", "@team:success", "@team:jail", "@team:hunter"));
                for (Player player : Bukkit.getOnlinePlayers())
                    set.add(player.getName());
                return getTabList(args[1], new HashSet<>(set));
            }
        }
        return null;
    }

    @Override
    public List<String> onBlockTabComplete(BlockCommandSender bs, Command cmd, String alias, String[] args) {
        if (args.length != 1) return null;
        return getTabList(args[0], new HashSet<>(Arrays.asList("join", "leave", "random", "rand")));
    }

    @Override
    public List<String> onConsoleTabComplete(ConsoleCommandSender cs, Command cmd, String alias, String[] args) {
        return null;
    }


    private void random(CommandSender sender, String count, WorldConfig worldConfig) {
        try {
            if (num == -1) {
                Main.tuhoShuffleSet.clear();
                num = Integer.parseInt(count);

                sender.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "通報部隊募集を開始しました。");
                if (GameManager.isGame(GameManager.GameState.GAME)) {
                    for (Player player : Bukkit.getOnlinePlayers())
                        if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_JAIL, player))
                            player.sendMessage(MainAPI.getPrefix(ChatColor.YELLOW) + "通報部隊を" + ChatColor.GOLD + ChatColor.UNDERLINE + num + "人" + ChatColor.RESET + ChatColor.YELLOW + "募集します。\n" +
                                    MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "通報部隊を希望の方は20秒以内に\"" + ChatColor.UNDERLINE + "/t" + ChatColor.RESET + ChatColor.GRAY + "\"と入力してください。");
                } else {
                    Bukkit.broadcastMessage(MainAPI.getPrefix(ChatColor.YELLOW) + "通報部隊を" + ChatColor.GOLD + ChatColor.UNDERLINE + num + "人" + ChatColor.RESET + ChatColor.YELLOW + "募集します。\n" +
                            MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "通報部隊を希望の方は20秒以内に\"" + ChatColor.UNDERLINE + "/t" + ChatColor.RESET + ChatColor.GRAY + "\"と入力してください。");
                }

                new BukkitRunnable() {
                    int count = 20;

                    @Override
                    public void run() {
                        if (count == 10) {
                            Bukkit.broadcastMessage(MainAPI.getPrefix(ChatColor.YELLOW) + "通報部隊の募集終了まで" + ChatColor.GOLD + ChatColor.UNDERLINE + "残り10秒" + ChatColor.RESET + ChatColor.YELLOW + "です。");
                        } else if (count == 0) {
                            if (!Main.tuhoShuffleSet.isEmpty()) {
                                List<UUID> tuhoShuffleList = new ArrayList<>(Main.tuhoShuffleSet);

                                Bukkit.broadcastMessage(MainAPI.getPrefix(ChatColor.YELLOW) + "通報部隊を" + ChatColor.GOLD + ChatColor.UNDERLINE + num + "人" + ChatColor.RESET + ChatColor.YELLOW + "選出しています…");
                                Collections.shuffle(tuhoShuffleList);
                                for (int i = 0; i < num && i < tuhoShuffleList.size(); i++) {
                                    Player p = Bukkit.getPlayer(tuhoShuffleList.get(i));
                                    tuhoShuffleList.remove(p);

                                    Teams.joinTeam(Teams.OnlineTeam.TOSO_TUHO, p);
                                    p.setGameMode(GameMode.ADVENTURE);

                                    TosoGameAPI.setArmor(p);
                                    TosoGameAPI.removeOp(p);

                                    Main.opGamePlayerSet.remove(p.getUniqueId());

                                    TosoGameAPI.showPlayers(p);
                                    p.teleport(worldConfig.getHunterLocationConfig().getLocation(1));

                                    if (MissionManager.isBossBar())
                                        MissionManager.getBossBar().removePlayer(p);

                                    p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "あなたを通報部隊に追加しました。");
                                    Bukkit.broadcastMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + p.getName() + "が通報部隊になりました。");
                                }
                                sender.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + ChatColor.GREEN + ChatColor.UNDERLINE + num + "人" + ChatColor.RESET + ChatColor.GRAY + "を通報部隊に追加しました。");
                                Main.tuhoShuffleSet.clear();
                                num = -1;
                            } else {
                                Bukkit.broadcastMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "通報部隊を希望する方がいなかったため選出をキャンセルしました。");
                                Main.tuhoShuffleSet.clear();
                                num = -1;
                            }
                        }

                        count--;
                    }
                }.runTaskTimer(Main.getInstance(), 0, 20);
                return;
            }
            sender.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "通報部隊募集中のため実行できません。");
            return;
        } catch (NumberFormatException e) {
            MainAPI.sendMessage(sender, MainAPI.ErrorMessage.ARGS_INTEGER);
            return;
        }
    }
}

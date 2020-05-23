package jp.aoichaan0513.A_TosoGame_Live.Commands.Command;

import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI;
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.MissionManager;
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.World.WorldConfig;
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.World.WorldManager;
import jp.aoichaan0513.A_TosoGame_Live.API.Scoreboard.Teams;
import jp.aoichaan0513.A_TosoGame_Live.API.TosoGameAPI;
import jp.aoichaan0513.A_TosoGame_Live.Commands.ICommand;
import jp.aoichaan0513.A_TosoGame_Live.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.ConsoleCommandSender;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Player extends ICommand {

    public Player(String name) {
        super(name);
    }

    @Override
    public void onPlayerCommand(org.bukkit.entity.Player sp, Command cmd, String label, String[] args) {
        if (TosoGameAPI.isAdmin(sp)) {
            if (args.length != 0) {
                WorldConfig worldConfig = Main.getWorldConfig();

                if (args[0].equalsIgnoreCase("join")) {
                    if (args.length != 1) {
                        if (args[1].startsWith("@")) {
                            String arg = args[1].substring(1);
                            if (arg.equalsIgnoreCase("all")) {
                                int i = Teams.getOnlineCount(Teams.OnlineTeam.TOSO_JAIL);

                                for (org.bukkit.entity.Player p : Bukkit.getOnlinePlayers()) {
                                    if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_JAIL, p)) {
                                        Teams.joinTeam(Teams.OnlineTeam.TOSO_PLAYER, p);
                                        p.setGameMode(GameMode.ADVENTURE);

                                        TosoGameAPI.setItem(WorldManager.GameType.START, p);
                                        TosoGameAPI.setPotionEffect(p);
                                        TosoGameAPI.removeOp(p);

                                        Main.opGamePlayerSet.add(p.getUniqueId());

                                        TosoGameAPI.showPlayers(p);
                                        TosoGameAPI.teleport(p, worldConfig.getRespawnLocationConfig().getLocations().values());

                                        if (MissionManager.isBossBar())
                                            MissionManager.getBossBar().addPlayer(p);

                                        p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "あなたを逃走者に追加しました。");
                                        Bukkit.broadcastMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + p.getName() + "が逃走者になりました。");
                                    }
                                }
                                sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + i + "人を逃走者に追加しました。");
                                return;
                            } else if (arg.startsWith("team")) {
                                if (arg.length() > 5) {
                                    String team = arg.substring(5);
                                    if (team.equalsIgnoreCase("admin")) {
                                        int i = Teams.getOnlineCount(Teams.OnlineTeam.TOSO_ADMIN);

                                        for (org.bukkit.entity.Player p : Bukkit.getOnlinePlayers()) {
                                            if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_ADMIN, p)) {
                                                Teams.joinTeam(Teams.OnlineTeam.TOSO_PLAYER, p);
                                                p.setGameMode(GameMode.ADVENTURE);

                                                TosoGameAPI.setItem(WorldManager.GameType.START, p);
                                                TosoGameAPI.setPotionEffect(p);
                                                TosoGameAPI.removeOp(p);

                                                Main.opGamePlayerSet.add(p.getUniqueId());

                                                TosoGameAPI.showPlayers(p);
                                                TosoGameAPI.teleport(p, worldConfig.getRespawnLocationConfig().getLocations().values());

                                                if (MissionManager.isBossBar())
                                                    MissionManager.getBossBar().addPlayer(p);

                                                p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "あなたを逃走者に追加しました。");
                                                Bukkit.broadcastMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + p.getName() + "が逃走者になりました。");
                                            }
                                        }
                                        sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + i + "人を逃走者に追加しました。");
                                        return;
                                    } else if (team.equalsIgnoreCase("success")) {
                                        int i = Teams.getOnlineCount(Teams.OnlineTeam.TOSO_SUCCESS);

                                        for (org.bukkit.entity.Player p : Bukkit.getOnlinePlayers()) {
                                            if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_SUCCESS, p)) {
                                                Teams.joinTeam(Teams.OnlineTeam.TOSO_PLAYER, p);
                                                p.setGameMode(GameMode.ADVENTURE);

                                                TosoGameAPI.setItem(WorldManager.GameType.START, p);
                                                TosoGameAPI.setPotionEffect(p);
                                                TosoGameAPI.removeOp(p);

                                                Main.opGamePlayerSet.add(p.getUniqueId());

                                                TosoGameAPI.showPlayers(p);
                                                TosoGameAPI.teleport(p, worldConfig.getRespawnLocationConfig().getLocations().values());

                                                if (MissionManager.isBossBar())
                                                    MissionManager.getBossBar().addPlayer(p);

                                                p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "あなたを逃走者に追加しました。");
                                                Bukkit.broadcastMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + p.getName() + "が逃走者になりました。");
                                            }
                                        }
                                        sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + i + "人を逃走者に追加しました。");
                                        return;
                                    } else if (team.equalsIgnoreCase("jail")) {
                                        int i = Teams.getOnlineCount(Teams.OnlineTeam.TOSO_JAIL);

                                        for (org.bukkit.entity.Player p : Bukkit.getOnlinePlayers()) {
                                            if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_JAIL, p)) {
                                                Teams.joinTeam(Teams.OnlineTeam.TOSO_PLAYER, p);
                                                p.setGameMode(GameMode.ADVENTURE);

                                                TosoGameAPI.setItem(WorldManager.GameType.START, p);
                                                TosoGameAPI.setPotionEffect(p);
                                                TosoGameAPI.removeOp(p);

                                                Main.opGamePlayerSet.add(p.getUniqueId());

                                                TosoGameAPI.showPlayers(p);
                                                TosoGameAPI.teleport(p, worldConfig.getRespawnLocationConfig().getLocations().values());

                                                if (MissionManager.isBossBar())
                                                    MissionManager.getBossBar().addPlayer(p);

                                                p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "あなたを逃走者に追加しました。");
                                                Bukkit.broadcastMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + p.getName() + "が逃走者になりました。");
                                            }
                                        }
                                        sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + i + "人を逃走者に追加しました。");
                                        return;
                                    } else if (team.equalsIgnoreCase("hunter")) {
                                        int i = Teams.getOnlineCount(Teams.OnlineTeam.TOSO_HUNTER);

                                        for (org.bukkit.entity.Player p : Bukkit.getOnlinePlayers()) {
                                            if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_HUNTER, p)) {
                                                Teams.joinTeam(Teams.OnlineTeam.TOSO_PLAYER, p);
                                                p.setGameMode(GameMode.ADVENTURE);

                                                TosoGameAPI.setItem(WorldManager.GameType.START, p);
                                                TosoGameAPI.setPotionEffect(p);
                                                TosoGameAPI.removeOp(p);

                                                Main.opGamePlayerSet.add(p.getUniqueId());

                                                TosoGameAPI.showPlayers(p);
                                                TosoGameAPI.teleport(p, worldConfig.getRespawnLocationConfig().getLocations().values());

                                                if (MissionManager.isBossBar())
                                                    MissionManager.getBossBar().addPlayer(p);

                                                p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "あなたを逃走者に追加しました。");
                                                Bukkit.broadcastMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + p.getName() + "が逃走者になりました。");
                                            }
                                        }
                                        sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + i + "人を逃走者に追加しました。");
                                        return;
                                    } else if (team.equalsIgnoreCase("tuho")) {
                                        int i = Teams.getOnlineCount(Teams.OnlineTeam.TOSO_TUHO);

                                        for (org.bukkit.entity.Player p : Bukkit.getOnlinePlayers()) {
                                            if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_TUHO, p)) {
                                                Teams.joinTeam(Teams.OnlineTeam.TOSO_PLAYER, p);
                                                p.setGameMode(GameMode.ADVENTURE);

                                                TosoGameAPI.setItem(WorldManager.GameType.START, p);
                                                TosoGameAPI.setPotionEffect(p);
                                                TosoGameAPI.removeOp(p);

                                                Main.opGamePlayerSet.add(p.getUniqueId());

                                                TosoGameAPI.showPlayers(p);
                                                TosoGameAPI.teleport(p, worldConfig.getRespawnLocationConfig().getLocations().values());

                                                if (MissionManager.isBossBar())
                                                    MissionManager.getBossBar().addPlayer(p);

                                                p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "あなたを逃走者に追加しました。");
                                                Bukkit.broadcastMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + p.getName() + "が逃走者になりました。");
                                            }
                                        }
                                        sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + i + "人を逃走者に追加しました。");
                                        return;
                                    }
                                }
                                sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "引数が不正です。チーム名を指定してください。");
                                return;
                            }
                        } else {
                            for (int i = 1; i < args.length; i++) {
                                org.bukkit.entity.Player target = Bukkit.getPlayerExact(args[i]);
                                if (target == null) {
                                    sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + ChatColor.RED + ChatColor.UNDERLINE + args[i] + ChatColor.RESET + ChatColor.GRAY + "はオフラインです。");
                                    continue;
                                } else {
                                    org.bukkit.entity.Player p = Bukkit.getPlayerExact(args[i]);
                                    if (!Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_PLAYER, p)) {
                                        Teams.joinTeam(Teams.OnlineTeam.TOSO_PLAYER, p);
                                        p.setGameMode(GameMode.ADVENTURE);

                                        TosoGameAPI.setItem(WorldManager.GameType.START, p);
                                        TosoGameAPI.setPotionEffect(p);
                                        TosoGameAPI.removeOp(p);

                                        Main.opGamePlayerSet.add(p.getUniqueId());

                                        TosoGameAPI.showPlayers(p);

                                        TosoGameAPI.teleport(p, worldConfig.getRespawnLocationConfig().getLocations().values());

                                        if (MissionManager.isBossBar())
                                            MissionManager.getBossBar().addPlayer(p);

                                        p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "あなたを逃走者に追加しました。");
                                        Bukkit.broadcastMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + p.getName() + "が逃走者になりました。");
                                        continue;
                                    }
                                    sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + p.getName() + "はすでに逃走者になっています。");
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
                                int i = Teams.getOnlineCount(Teams.OnlineTeam.TOSO_PLAYER);
                                for (org.bukkit.entity.Player p : Bukkit.getOnlinePlayers()) {
                                    if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_PLAYER, p)) {
                                        Teams.joinTeam(Teams.OnlineTeam.TOSO_JAIL, p);
                                        p.setGameMode(GameMode.ADVENTURE);

                                        TosoGameAPI.removeOp(p);

                                        Main.opGamePlayerSet.remove(p.getUniqueId());

                                        TosoGameAPI.showPlayers(p);
                                        TosoGameAPI.teleport(p, worldConfig.getJailLocationConfig().getLocations().values());

                                        if (MissionManager.isBossBar())
                                            MissionManager.getBossBar().addPlayer(p);

                                        p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "あなたを牢獄に追加しました。");
                                        Bukkit.broadcastMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + p.getName() + "が逃走者から抜けました。");
                                    }
                                }
                                sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + i + "人が逃走者から抜けました。");
                                return;
                            } else if (arg.startsWith("team")) {
                                if (arg.length() > 5) {
                                    String team = arg.substring(5);
                                    if (team.equalsIgnoreCase("admin")) {
                                        int i = Teams.getOnlineCount(Teams.OnlineTeam.TOSO_PLAYER);
                                        for (org.bukkit.entity.Player p : Bukkit.getOnlinePlayers()) {
                                            if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_PLAYER, p)) {
                                                Teams.joinTeam(Teams.OnlineTeam.TOSO_ADMIN, p);
                                                p.setGameMode(GameMode.CREATIVE);

                                                Main.opGamePlayerSet.remove(p.getUniqueId());

                                                TosoGameAPI.showPlayers(p);
                                                TosoGameAPI.addOp(p);

                                                TosoGameAPI.teleport(p, worldConfig.getRespawnLocationConfig().getLocations().values());

                                                if (MissionManager.isBossBar())
                                                    MissionManager.getBossBar().addPlayer(p);

                                                p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "あなたを運営に追加しました。");
                                                Bukkit.broadcastMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + p.getName() + "が逃走者から抜けました。");
                                            }
                                        }
                                        sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + i + "人が逃走者から抜けました。");
                                        return;
                                    } else if (team.equalsIgnoreCase("success")) {
                                        int i = Teams.getOnlineCount(Teams.OnlineTeam.TOSO_PLAYER);
                                        for (org.bukkit.entity.Player p : Bukkit.getOnlinePlayers()) {
                                            if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_PLAYER, p)) {
                                                Teams.joinTeam(Teams.OnlineTeam.TOSO_SUCCESS, p);
                                                p.setGameMode(GameMode.ADVENTURE);

                                                Main.opGamePlayerSet.remove(p.getUniqueId());

                                                TosoGameAPI.showPlayers(p);
                                                TosoGameAPI.removeOp(p);

                                                TosoGameAPI.teleport(p, worldConfig.getRespawnLocationConfig().getLocations().values());

                                                if (MissionManager.isBossBar())
                                                    MissionManager.getBossBar().addPlayer(p);

                                                p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "あなたを生存者に追加しました。");
                                                Bukkit.broadcastMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + p.getName() + "が逃走者から抜けました。");
                                            }
                                        }
                                        sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + i + "人が逃走者から抜けました。");
                                        return;
                                    } else if (team.equalsIgnoreCase("jail")) {
                                        int i = Teams.getOnlineCount(Teams.OnlineTeam.TOSO_PLAYER);
                                        for (org.bukkit.entity.Player p : Bukkit.getOnlinePlayers()) {
                                            if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_PLAYER, p)) {
                                                Teams.joinTeam(Teams.OnlineTeam.TOSO_JAIL, p);
                                                p.setGameMode(GameMode.ADVENTURE);

                                                Main.opGamePlayerSet.remove(p.getUniqueId());

                                                TosoGameAPI.showPlayers(p);
                                                TosoGameAPI.removeOp(p);

                                                TosoGameAPI.teleport(p, worldConfig.getJailLocationConfig().getLocations().values());

                                                if (MissionManager.isBossBar())
                                                    MissionManager.getBossBar().addPlayer(p);

                                                p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "あなたを牢獄に追加しました。");
                                                Bukkit.broadcastMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + p.getName() + "が逃走者から抜けました。");
                                            }
                                        }
                                        sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + i + "人が逃走者から抜けました。");
                                        return;
                                    } else if (team.equalsIgnoreCase("hunter")) {
                                        int i = Teams.getOnlineCount(Teams.OnlineTeam.TOSO_PLAYER);
                                        for (org.bukkit.entity.Player p : Bukkit.getOnlinePlayers()) {
                                            if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_PLAYER, p)) {
                                                Teams.joinTeam(Teams.OnlineTeam.TOSO_HUNTER, p);
                                                p.setGameMode(GameMode.ADVENTURE);

                                                TosoGameAPI.setArmor(p);
                                                TosoGameAPI.setPotionEffect(p, true);
                                                TosoGameAPI.removeOp(p);

                                                Main.opGamePlayerSet.remove(p.getUniqueId());

                                                p.teleport(worldConfig.getHunterLocationConfig().getLocation(1));

                                                if (MissionManager.isBossBar())
                                                    MissionManager.getBossBar().removePlayer(p);

                                                p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "あなたをハンターに追加しました。");
                                                Bukkit.broadcastMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + p.getName() + "が逃走者から抜けました。");
                                            }
                                        }
                                        sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + i + "人が逃走者から抜けました。");
                                        return;
                                    } else if (team.equalsIgnoreCase("tuho")) {
                                        int i = Teams.getOnlineCount(Teams.OnlineTeam.TOSO_PLAYER);
                                        for (org.bukkit.entity.Player p : Bukkit.getOnlinePlayers()) {
                                            if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_PLAYER, p)) {
                                                Teams.joinTeam(Teams.OnlineTeam.TOSO_TUHO, p);
                                                p.setGameMode(GameMode.ADVENTURE);

                                                Main.opGamePlayerSet.remove(p.getUniqueId());

                                                TosoGameAPI.showPlayers(p);
                                                TosoGameAPI.removeOp(p);

                                                TosoGameAPI.teleport(p, worldConfig.getJailLocationConfig().getLocations().values());

                                                if (MissionManager.isBossBar())
                                                    MissionManager.getBossBar().addPlayer(p);

                                                p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "あなたを牢獄に追加しました。");
                                                Bukkit.broadcastMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + p.getName() + "が逃走者から抜けました。");
                                            }
                                        }
                                        sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + i + "人が逃走者から抜けました。");
                                        return;
                                    }
                                }
                                sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "引数が不正です。チーム名を指定してください。");
                                return;
                            }
                        } else {
                            for (int i = 1; i < args.length; i++) {
                                org.bukkit.entity.Player target = Bukkit.getPlayerExact(args[i]);
                                if (target == null) {
                                    sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + ChatColor.RED + ChatColor.UNDERLINE + args[i] + ChatColor.RESET + ChatColor.GRAY + "はオフラインです。");
                                    continue;
                                } else {
                                    org.bukkit.entity.Player p = Bukkit.getPlayerExact(args[i]);
                                    if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_PLAYER, p)) {
                                        Teams.joinTeam(Teams.OnlineTeam.TOSO_JAIL, p);
                                        p.setGameMode(GameMode.ADVENTURE);

                                        Main.opGamePlayerSet.remove(p.getUniqueId());

                                        TosoGameAPI.showPlayers(p);
                                        TosoGameAPI.removeOp(p);

                                        TosoGameAPI.teleport(p, worldConfig.getJailLocationConfig().getLocations().values());

                                        if (MissionManager.isBossBar())
                                            MissionManager.getBossBar().addPlayer(p);

                                        p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "あなたを牢獄に追加しました。");
                                        Bukkit.broadcastMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + p.getName() + "が逃走者から抜けました。");
                                        continue;
                                    }
                                    sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + p.getName() + "はすでに逃走者から抜けています。");
                                    continue;
                                }
                            }
                            return;
                        }
                    }
                    MainAPI.sendMessage(sp, MainAPI.ErrorMessage.ARGS_PLAYER);
                    return;
                }
                sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "引数が不正です。\n" +
                        MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "コマンドの使い方:\n" +
                        MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "\"/player join <プレイヤー名>\" - 逃走者に参加\n" +
                        MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "\"/player leave <プレイヤー名>\" - 逃走者から離脱");
                return;
            }
            sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "引数が不正です。\n" +
                    MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "コマンドの使い方:\n" +
                    MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "\"/player join <プレイヤー名>\" - 逃走者に参加\n" +
                    MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "\"/player leave <プレイヤー名>\" - 逃走者から離脱");
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
                        org.bukkit.entity.Player target = Bukkit.getPlayerExact(args[i]);
                        if (target == null) {
                            bs.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + ChatColor.RED + ChatColor.UNDERLINE + args[i] + ChatColor.RESET + ChatColor.GRAY + "はオフラインです。");
                            continue;
                        } else {
                            org.bukkit.entity.Player p = Bukkit.getPlayerExact(args[i]);
                            if (!Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_PLAYER, p)) {
                                Teams.joinTeam(Teams.OnlineTeam.TOSO_PLAYER, p);
                                p.setGameMode(GameMode.ADVENTURE);

                                TosoGameAPI.setItem(WorldManager.GameType.START, p);
                                TosoGameAPI.setPotionEffect(p);
                                TosoGameAPI.removeOp(p);

                                Main.opGamePlayerSet.add(p.getUniqueId());

                                TosoGameAPI.showPlayers(p);
                                TosoGameAPI.teleport(p, worldConfig.getRespawnLocationConfig().getLocations().values());

                                if (MissionManager.isBossBar())
                                    MissionManager.getBossBar().addPlayer(p);

                                p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "あなたを逃走者に追加しました。");
                                Bukkit.broadcastMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + p.getName() + "が逃走者になりました。");
                                continue;
                            }
                            bs.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + p.getName() + "はすでに逃走者になっています。");
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
                        org.bukkit.entity.Player target = Bukkit.getPlayerExact(args[i]);
                        if (target == null) {
                            bs.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + ChatColor.RED + ChatColor.UNDERLINE + args[i] + ChatColor.RESET + ChatColor.GRAY + "はオフラインです。");
                            continue;
                        } else {
                            org.bukkit.entity.Player p = Bukkit.getPlayerExact(args[i]);
                            if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_PLAYER, p)) {
                                Teams.joinTeam(Teams.OnlineTeam.TOSO_JAIL, p);
                                p.setGameMode(GameMode.ADVENTURE);

                                TosoGameAPI.removeOp(p);

                                Main.opGamePlayerSet.remove(p.getUniqueId());

                                TosoGameAPI.showPlayers(p);
                                TosoGameAPI.teleport(p, worldConfig.getJailLocationConfig().getLocations().values());

                                if (MissionManager.isBossBar())
                                    MissionManager.getBossBar().addPlayer(p);

                                p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "あなたを牢獄に追加しました。");
                                Bukkit.broadcastMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + p.getName() + "が逃走者から抜けました。");
                                continue;
                            }
                            bs.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + p.getName() + "はすでに逃走者から抜けています。");
                            continue;
                        }
                    }
                    return;
                }
                MainAPI.sendMessage(bs, MainAPI.ErrorMessage.ARGS_PLAYER);
                return;
            }
            bs.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "引数が不正です。\n" +
                    MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "コマンドの使い方:\n" +
                    MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "\"/player join <プレイヤー名>\" - 逃走者に参加\n" +
                    MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "\"/player leave <プレイヤー名>\" - 逃走者から離脱");
            return;
        }
        bs.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "引数が不正です。\n" +
                MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "コマンドの使い方:\n" +
                MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "\"/player join <プレイヤー名>\" - 逃走者に参加\n" +
                MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "\"/player leave <プレイヤー名>\" - 逃走者から離脱");
        return;
    }

    @Override
    public void onConsoleCommand(ConsoleCommandSender cs, Command cmd, String label, String[] args) {
        MainAPI.sendMessage(cs, MainAPI.ErrorMessage.NOT_PLAYER);
        return;
    }

    @Override
    public List<String> onPlayerTabComplete(org.bukkit.entity.Player sp, Command cmd, String alias, String[] args) {
        if (!TosoGameAPI.isAdmin(sp)) return null;
        if (TosoGameAPI.isAdmin(sp)) {
            if (args.length == 1) {
                return getTabList(args[0], new HashSet<>(Arrays.asList("join", "leave")));
            } else if (args.length == 2) {
                if (args[0].equalsIgnoreCase("join") || args[0].equalsIgnoreCase("leave")) {
                    Set<String> set = new HashSet<>(Arrays.asList("@all", "@team:admin", "@team:success", "@team:jail", "@team:hunter", "@team:tuho"));
                    for (org.bukkit.entity.Player player : Bukkit.getOnlinePlayers())
                        set.add(player.getName());
                    return getTabList(args[1], new HashSet<>(set));
                }
            }
        }
        return null;
    }

    @Override
    public List<String> onBlockTabComplete(BlockCommandSender bs, Command cmd, String alias, String[] args) {
        if (args.length != 1) return null;
        return getTabList(args[0], new HashSet<>(Arrays.asList("join", "leave")));
    }

    @Override
    public List<String> onConsoleTabComplete(ConsoleCommandSender cs, Command cmd, String alias, String[] args) {
        return null;
    }
}

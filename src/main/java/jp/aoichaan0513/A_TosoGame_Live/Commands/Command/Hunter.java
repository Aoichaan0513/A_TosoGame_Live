package jp.aoichaan0513.A_TosoGame_Live.Commands.Command;

import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI;
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.GameManager;
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.MissionManager;
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.World.WorldConfig;
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.World.WorldManager;
import jp.aoichaan0513.A_TosoGame_Live.API.Scoreboard.Teams;
import jp.aoichaan0513.A_TosoGame_Live.API.TosoGameAPI;
import jp.aoichaan0513.A_TosoGame_Live.Commands.ICommand;
import jp.aoichaan0513.A_TosoGame_Live.Main;
import org.bukkit.*;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Hunter extends ICommand {

    public static int num = -1;

    public Hunter(String name) {
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
                                        Teams.joinTeam(Teams.OnlineTeam.TOSO_HUNTER, p);
                                        p.setGameMode(GameMode.ADVENTURE);

                                        TosoGameAPI.setArmor(p);
                                        TosoGameAPI.setPotionEffect(p, true);
                                        TosoGameAPI.removeOp(p);

                                        if (Main.playerList.contains(p))
                                            Main.playerList.remove(p);

                                        TosoGameAPI.showPlayers(p);
                                        p.teleport(worldConfig.getHunterLocationConfig().getLocation(1));

                                        if (MissionManager.isBossBar())
                                            MissionManager.getBossBar().removePlayer(p);

                                        p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "あなたをハンターに追加しました。");
                                        Bukkit.broadcastMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + p.getName() + "がハンターになりました。");
                                    }
                                }
                                sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + ChatColor.GREEN + ChatColor.UNDERLINE + i + "人" + ChatColor.RESET + ChatColor.GRAY + "をハンターに追加しました。");
                                return;
                            } else if (arg.startsWith("team")) {
                                if (arg.length() > 5) {
                                    String team = arg.substring(5);
                                    if (team.equalsIgnoreCase("admin")) {
                                        int i = Teams.getOnlineCount(Teams.OnlineTeam.TOSO_ADMIN);
                                        for (Player p : Bukkit.getOnlinePlayers()) {
                                            if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_ADMIN, p)) {
                                                Teams.joinTeam(Teams.OnlineTeam.TOSO_HUNTER, p);
                                                p.setGameMode(GameMode.ADVENTURE);

                                                TosoGameAPI.setArmor(p);
                                                TosoGameAPI.setPotionEffect(p, true);
                                                TosoGameAPI.removeOp(p);

                                                if (Main.playerList.contains(p))
                                                    Main.playerList.remove(p);

                                                TosoGameAPI.showPlayers(p);
                                                p.teleport(worldConfig.getHunterLocationConfig().getLocation(1));

                                                if (MissionManager.isBossBar())
                                                    MissionManager.getBossBar().removePlayer(p);

                                                p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "あなたをハンターに追加しました。");
                                                Bukkit.broadcastMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + p.getName() + "がハンターになりました。");
                                            }
                                        }
                                        sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + ChatColor.GREEN + ChatColor.UNDERLINE + i + "人" + ChatColor.RESET + ChatColor.GRAY + "をハンターに追加しました。");
                                        return;
                                    } else if (team.equalsIgnoreCase("player")) {
                                        int i = Teams.getOnlineCount(Teams.OnlineTeam.TOSO_PLAYER);
                                        for (Player p : Bukkit.getOnlinePlayers()) {
                                            if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_PLAYER, p)) {
                                                Teams.joinTeam(Teams.OnlineTeam.TOSO_HUNTER, p);
                                                p.setGameMode(GameMode.ADVENTURE);

                                                TosoGameAPI.setArmor(p);
                                                TosoGameAPI.setPotionEffect(p, true);
                                                TosoGameAPI.removeOp(p);

                                                if (Main.playerList.contains(p))
                                                    Main.playerList.remove(p);

                                                TosoGameAPI.showPlayers(p);
                                                p.teleport(worldConfig.getHunterLocationConfig().getLocation(1));

                                                if (MissionManager.isBossBar())
                                                    MissionManager.getBossBar().removePlayer(p);

                                                p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "あなたをハンターに追加しました。");
                                                Bukkit.broadcastMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + p.getName() + "がハンターになりました。");
                                            }
                                        }
                                        sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + ChatColor.GREEN + ChatColor.UNDERLINE + i + "人" + ChatColor.RESET + ChatColor.GRAY + "をハンターに追加しました。");
                                        return;
                                    } else if (team.equalsIgnoreCase("success")) {
                                        int i = Teams.getOnlineCount(Teams.OnlineTeam.TOSO_SUCCESS);
                                        for (Player p : Bukkit.getOnlinePlayers()) {
                                            if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_SUCCESS, p)) {
                                                Teams.joinTeam(Teams.OnlineTeam.TOSO_HUNTER, p);
                                                p.setGameMode(GameMode.ADVENTURE);

                                                TosoGameAPI.setArmor(p);
                                                TosoGameAPI.setPotionEffect(p, true);
                                                TosoGameAPI.removeOp(p);

                                                if (Main.playerList.contains(p))
                                                    Main.playerList.remove(p);

                                                TosoGameAPI.showPlayers(p);
                                                p.teleport(worldConfig.getHunterLocationConfig().getLocation(1));

                                                if (MissionManager.isBossBar())
                                                    MissionManager.getBossBar().removePlayer(p);

                                                p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "あなたをハンターに追加しました。");
                                                Bukkit.broadcastMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + p.getName() + "がハンターになりました。");
                                            }
                                        }
                                        sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + ChatColor.GREEN + ChatColor.UNDERLINE + i + "人" + ChatColor.RESET + ChatColor.GRAY + "をハンターに追加しました。");
                                        return;
                                    } else if (team.equalsIgnoreCase("jail")) {
                                        int i = Teams.getOnlineCount(Teams.OnlineTeam.TOSO_JAIL);
                                        for (Player p : Bukkit.getOnlinePlayers()) {
                                            if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_JAIL, p)) {
                                                Teams.joinTeam(Teams.OnlineTeam.TOSO_HUNTER, p);
                                                p.setGameMode(GameMode.ADVENTURE);

                                                TosoGameAPI.setArmor(p);
                                                TosoGameAPI.setPotionEffect(p, true);
                                                TosoGameAPI.removeOp(p);

                                                if (Main.playerList.contains(p))
                                                    Main.playerList.remove(p);

                                                TosoGameAPI.showPlayers(p);
                                                p.teleport(worldConfig.getHunterLocationConfig().getLocation(1));

                                                if (MissionManager.isBossBar())
                                                    MissionManager.getBossBar().removePlayer(p);

                                                p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "あなたをハンターに追加しました。");
                                                Bukkit.broadcastMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + p.getName() + "がハンターになりました。");
                                            }
                                        }
                                        sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + ChatColor.GREEN + ChatColor.UNDERLINE + i + "人" + ChatColor.RESET + ChatColor.GRAY + "をハンターに追加しました。");
                                        return;
                                    } else if (team.equalsIgnoreCase("tuho")) {
                                        int i = Teams.getOnlineCount(Teams.OnlineTeam.TOSO_TUHO);
                                        for (Player p : Bukkit.getOnlinePlayers()) {
                                            if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_TUHO, p)) {
                                                Teams.joinTeam(Teams.OnlineTeam.TOSO_HUNTER, p);
                                                p.setGameMode(GameMode.ADVENTURE);

                                                TosoGameAPI.setArmor(p);
                                                TosoGameAPI.setPotionEffect(p, true);
                                                TosoGameAPI.removeOp(p);

                                                if (Main.playerList.contains(p))
                                                    Main.playerList.remove(p);

                                                TosoGameAPI.showPlayers(p);
                                                p.teleport(worldConfig.getHunterLocationConfig().getLocation(1));

                                                if (MissionManager.isBossBar())
                                                    MissionManager.getBossBar().removePlayer(p);

                                                p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "あなたをハンターに追加しました。");
                                                Bukkit.broadcastMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + p.getName() + "がハンターになりました。");
                                            }
                                        }
                                        sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + ChatColor.GREEN + ChatColor.UNDERLINE + i + "人" + ChatColor.RESET + ChatColor.GRAY + "人をハンターに追加しました。");
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
                                    if (!Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_HUNTER, p)) {
                                        Teams.joinTeam(Teams.OnlineTeam.TOSO_HUNTER, p);
                                        p.setGameMode(GameMode.ADVENTURE);

                                        TosoGameAPI.setArmor(p);
                                        TosoGameAPI.setPotionEffect(p, true);
                                        TosoGameAPI.removeOp(p);

                                        if (Main.playerList.contains(p))
                                            Main.playerList.remove(p);

                                        TosoGameAPI.showPlayers(p);
                                        p.teleport(worldConfig.getHunterLocationConfig().getLocation(1));

                                        if (MissionManager.isBossBar())
                                            MissionManager.getBossBar().removePlayer(p);

                                        p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "あなたをハンターに追加しました。");
                                        Bukkit.broadcastMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + p.getName() + "がハンターになりました。");
                                        continue;
                                    }
                                    sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + p.getName() + "はすでにハンターになっています。");
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
                                int i = Teams.getOnlineCount(Teams.OnlineTeam.TOSO_HUNTER);
                                for (Player p : Bukkit.getOnlinePlayers()) {
                                    if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_HUNTER, p)) {
                                        Teams.joinTeam(Teams.OnlineTeam.TOSO_JAIL, p);
                                        p.setGameMode(GameMode.ADVENTURE);

                                        TosoGameAPI.removeArmor(p);
                                        TosoGameAPI.setPotionEffect(p, true);
                                        TosoGameAPI.removeOp(p);

                                        if (Main.playerList.contains(p))
                                            Main.playerList.remove(p);

                                        TosoGameAPI.showPlayers(p);

                                        TosoGameAPI.teleport(p, worldConfig.getJailLocationConfig().getLocations());

                                        if (MissionManager.isBossBar())
                                            MissionManager.getBossBar().addPlayer(p);

                                        p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "あなたを牢獄に追加しました。");
                                        Bukkit.broadcastMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + p.getName() + "がハンターから抜けました。");
                                    }
                                }
                                sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + ChatColor.GREEN + ChatColor.UNDERLINE + i + "人" + ChatColor.RESET + ChatColor.GRAY + "がハンターから抜けました。");
                                return;
                            } else if (arg.startsWith("team")) {
                                if (arg.length() > 5) {
                                    String team = arg.substring(5);
                                    if (team.equalsIgnoreCase("admin")) {
                                        int i = Teams.getOnlineCount(Teams.OnlineTeam.TOSO_HUNTER);
                                        for (Player p : Bukkit.getOnlinePlayers()) {
                                            if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_HUNTER, p)) {
                                                Teams.joinTeam(Teams.OnlineTeam.TOSO_ADMIN, p);
                                                p.setGameMode(GameMode.CREATIVE);

                                                TosoGameAPI.removeArmor(p);
                                                TosoGameAPI.setPotionEffect(p, true);
                                                TosoGameAPI.addOp(p);

                                                if (Main.playerList.contains(p))
                                                    Main.playerList.remove(p);

                                                TosoGameAPI.showPlayers(p);

                                                TosoGameAPI.teleport(p, worldConfig.getRespawnLocationConfig().getLocations());

                                                if (MissionManager.isBossBar())
                                                    MissionManager.getBossBar().addPlayer(p);

                                                p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "あなたを運営に追加しました。");
                                                Bukkit.broadcastMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + p.getName() + "がハンターから抜けました。");
                                            }
                                        }
                                        sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + ChatColor.GREEN + ChatColor.UNDERLINE + i + "人" + ChatColor.RESET + ChatColor.GRAY + "がハンターから抜けました。");
                                        return;
                                    } else if (team.equalsIgnoreCase("player")) {
                                        int i = Teams.getOnlineCount(Teams.OnlineTeam.TOSO_HUNTER);
                                        for (Player p : Bukkit.getOnlinePlayers()) {
                                            if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_HUNTER, p)) {
                                                Teams.joinTeam(Teams.OnlineTeam.TOSO_PLAYER, p);
                                                p.setGameMode(GameMode.ADVENTURE);

                                                TosoGameAPI.removeArmor(p);
                                                TosoGameAPI.setPotionEffect(p, true);
                                                TosoGameAPI.removeOp(p);

                                                if (Main.playerList.contains(p))
                                                    Main.playerList.remove(p);

                                                TosoGameAPI.showPlayers(p);

                                                TosoGameAPI.teleport(p, worldConfig.getRespawnLocationConfig().getLocations());

                                                if (MissionManager.isBossBar())
                                                    MissionManager.getBossBar().addPlayer(p);

                                                p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "あなたを逃走者に追加しました。");
                                                Bukkit.broadcastMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + p.getName() + "がハンターから抜けました。");
                                            }
                                        }
                                        sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + ChatColor.GREEN + ChatColor.UNDERLINE + i + "人" + ChatColor.RESET + ChatColor.GRAY + "がハンターから抜けました。");
                                        return;
                                    } else if (team.equalsIgnoreCase("success")) {
                                        int i = Teams.getOnlineCount(Teams.OnlineTeam.TOSO_HUNTER);
                                        for (Player p : Bukkit.getOnlinePlayers()) {
                                            if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_HUNTER, p)) {
                                                Teams.joinTeam(Teams.OnlineTeam.TOSO_SUCCESS, p);
                                                p.setGameMode(GameMode.ADVENTURE);

                                                TosoGameAPI.removeArmor(p);
                                                TosoGameAPI.setPotionEffect(p, true);
                                                TosoGameAPI.removeOp(p);

                                                if (Main.playerList.contains(p))
                                                    Main.playerList.remove(p);

                                                TosoGameAPI.showPlayers(p);

                                                TosoGameAPI.teleport(p, worldConfig.getRespawnLocationConfig().getLocations());

                                                if (MissionManager.isBossBar())
                                                    MissionManager.getBossBar().addPlayer(p);

                                                p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "あなたを生存者に追加しました。");
                                                Bukkit.broadcastMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + p.getName() + "がハンターから抜けました。");
                                            }
                                        }
                                        sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + ChatColor.GREEN + ChatColor.UNDERLINE + i + "人" + ChatColor.RESET + ChatColor.GRAY + "がハンターから抜けました。");
                                        return;
                                    } else if (team.equalsIgnoreCase("jail")) {
                                        int i = Teams.getOnlineCount(Teams.OnlineTeam.TOSO_HUNTER);
                                        for (Player p : Bukkit.getOnlinePlayers()) {
                                            if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_HUNTER, p)) {
                                                Teams.joinTeam(Teams.OnlineTeam.TOSO_JAIL, p);
                                                p.setGameMode(GameMode.ADVENTURE);

                                                TosoGameAPI.removeArmor(p);
                                                TosoGameAPI.setPotionEffect(p, true);
                                                TosoGameAPI.removeOp(p);

                                                if (Main.playerList.contains(p))
                                                    Main.playerList.remove(p);

                                                TosoGameAPI.showPlayers(p);

                                                TosoGameAPI.teleport(p, worldConfig.getJailLocationConfig().getLocations());

                                                if (MissionManager.isBossBar())
                                                    MissionManager.getBossBar().addPlayer(p);

                                                p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "あなたを牢獄に追加しました。");
                                                Bukkit.broadcastMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + p.getName() + "がハンターから抜けました。");
                                            }
                                        }
                                        sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + ChatColor.GREEN + ChatColor.UNDERLINE + i + "人" + ChatColor.RESET + ChatColor.GRAY + "がハンターから抜けました。");
                                        return;
                                    } else if (team.equalsIgnoreCase("tuho")) {
                                        int i = Teams.getOnlineCount(Teams.OnlineTeam.TOSO_HUNTER);
                                        for (Player p : Bukkit.getOnlinePlayers()) {
                                            if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_HUNTER, p)) {
                                                Teams.joinTeam(Teams.OnlineTeam.TOSO_TUHO, p);
                                                p.setGameMode(GameMode.ADVENTURE);

                                                TosoGameAPI.removeArmor(p);
                                                TosoGameAPI.setPotionEffect(p, true);
                                                TosoGameAPI.removeOp(p);

                                                if (Main.playerList.contains(p))
                                                    Main.playerList.remove(p);

                                                TosoGameAPI.showPlayers(p);

                                                p.teleport(worldConfig.getHunterLocationConfig().getLocation(1));

                                                if (MissionManager.isBossBar())
                                                    MissionManager.getBossBar().removePlayer(p);

                                                p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "あなたを通報部隊に追加しました。");
                                                Bukkit.broadcastMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + p.getName() + "がハンターから抜けました。");
                                            }
                                        }
                                        sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + ChatColor.GREEN + ChatColor.UNDERLINE + i + "人" + ChatColor.RESET + ChatColor.GRAY + "がハンターから抜けました。");
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
                                    if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_HUNTER, p)) {
                                        Teams.joinTeam(Teams.OnlineTeam.TOSO_JAIL, p);
                                        p.setGameMode(GameMode.ADVENTURE);

                                        TosoGameAPI.removeArmor(p);
                                        TosoGameAPI.setPotionEffect(p, true);
                                        TosoGameAPI.removeOp(p);

                                        if (Main.playerList.contains(p))
                                            Main.playerList.remove(p);

                                        TosoGameAPI.showPlayers(p);

                                        TosoGameAPI.teleport(p, worldConfig.getJailLocationConfig().getLocations());

                                        if (MissionManager.isBossBar())
                                            MissionManager.getBossBar().addPlayer(p);

                                        p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "あなたを牢獄に追加しました。");
                                        Bukkit.broadcastMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + p.getName() + "がハンターから抜けました。");
                                        continue;
                                    }
                                    sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + p.getName() + "はすでにハンターから抜けています。");
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
                } else if (args[0].equalsIgnoreCase("zombie")) {
                    if (WorldManager.getWorld().getDifficulty() != Difficulty.PEACEFUL) {
                        if (args.length != 1) {
                            try {
                                int v = Integer.parseInt(args[1]);
                                if (v > 0) {
                                    Bukkit.broadcastMessage(MainAPI.getPrefix(MainAPI.PrefixType.WARNING) + "ゾンビハンターを" + ChatColor.GOLD + ChatColor.UNDERLINE + v + "体" + ChatColor.RESET + ChatColor.YELLOW + "追加しています…");

                                    for (int i = 0; i < v; i++) {
                                        Zombie z = (Zombie) WorldManager.getWorld().spawnEntity(worldConfig.getHunterLocationConfig().getLocation(1), EntityType.ZOMBIE);
                                        z.setBaby(false);
                                        z.getEquipment().setHelmet(new ItemStack(Material.DIAMOND_HELMET));
                                        z.getEquipment().setChestplate(new ItemStack(Material.DIAMOND_CHESTPLATE));
                                        z.getEquipment().setLeggings(new ItemStack(Material.DIAMOND_LEGGINGS));
                                        z.getEquipment().setBoots(new ItemStack(Material.DIAMOND_BOOTS));
                                        z.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 200000, 1, false, false));
                                    }
                                    sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + ChatColor.GREEN + ChatColor.UNDERLINE + v + "体" + ChatColor.RESET + ChatColor.GRAY + "をゾンビハンターに追加しました。");
                                    return;
                                }
                                sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "引数が不正です。1以上で数字を指定してください。");
                                return;
                            } catch (NumberFormatException e) {
                                MainAPI.sendMessage(sp, MainAPI.ErrorMessage.ARGS_INTEGER);
                                return;
                            }
                        }
                        MainAPI.sendMessage(sp, MainAPI.ErrorMessage.ARGS_INTEGER);
                        return;
                    }
                    sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "このワールドの難易度がピースフルのため実行できません。");
                    return;
                }
                sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "引数が不正です。\n" +
                        MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "コマンドの使い方:\n" +
                        MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "\"/hunter join <プレイヤー名>\" - ハンターに参加\n" +
                        MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "\"/hunter leave <プレイヤー名>\" - ハンターから離脱\n" +
                        MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "\"/hunter random <数値>\" または \"/hunter rand <数値>\" - 数値の数だけハンターをランダムで追加\n" +
                        MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "\"/hunter zombie <数値>\" - 数値の数だけゾンビハンターを追加");
                return;
            }
            sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "引数が不正です。\n" +
                    MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "コマンドの使い方:\n" +
                    MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "\"/hunter join <プレイヤー名>\" - ハンターに参加\n" +
                    MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "\"/hunter leave <プレイヤー名>\" - ハンターから離脱\n" +
                    MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "\"/hunter random <数値>\" または \"/hunter rand <数値>\" - 数値の数だけハンターをランダムで追加\n" +
                    MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "\"/hunter zombie <数値>\" - 数値の数だけゾンビハンターを追加");
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
                            if (!Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_HUNTER, p)) {
                                Teams.joinTeam(Teams.OnlineTeam.TOSO_HUNTER, p);
                                p.setGameMode(GameMode.ADVENTURE);

                                TosoGameAPI.setArmor(p);
                                TosoGameAPI.setPotionEffect(p, true);
                                TosoGameAPI.removeOp(p);

                                if (Main.playerList.contains(p))
                                    Main.playerList.remove(p);

                                TosoGameAPI.showPlayers(p);

                                p.teleport(worldConfig.getHunterLocationConfig().getLocation(1));

                                if (MissionManager.isBossBar())
                                    MissionManager.getBossBar().removePlayer(p);

                                p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "あなたをハンターに追加しました。");
                                Bukkit.broadcastMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + p.getName() + "がハンターになりました。");
                                continue;
                            }
                            bs.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + p.getName() + "はすでにハンターになっています。");
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
                            if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_HUNTER, p)) {
                                Teams.joinTeam(Teams.OnlineTeam.TOSO_JAIL, p);
                                p.setGameMode(GameMode.ADVENTURE);

                                TosoGameAPI.removeArmor(p);
                                TosoGameAPI.setPotionEffect(p, true);
                                TosoGameAPI.removeOp(p);

                                if (Main.playerList.contains(p))
                                    Main.playerList.remove(p);

                                TosoGameAPI.showPlayers(p);

                                TosoGameAPI.teleport(p, worldConfig.getJailLocationConfig().getLocations());

                                if (MissionManager.isBossBar())
                                    MissionManager.getBossBar().addPlayer(p);

                                p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "あなたを牢獄に追加しました。");
                                Bukkit.broadcastMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + p.getName() + "がハンターから抜けました。");
                                continue;
                            }
                            bs.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + p.getName() + "はすでにハンターから抜けています。");
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
            } else if (args[0].equalsIgnoreCase("zombie")) {
                if (WorldManager.getWorld().getDifficulty() != Difficulty.PEACEFUL) {
                    if (args.length != 1) {
                        try {
                            int v = Integer.parseInt(args[1]);
                            if (v > 0) {
                                Bukkit.broadcastMessage(MainAPI.getPrefix(MainAPI.PrefixType.WARNING) + "ゾンビハンターを" + ChatColor.GOLD + ChatColor.UNDERLINE + v + "体" + ChatColor.RESET + ChatColor.YELLOW + "追加しています…");

                                for (int i = 0; i < v; i++) {
                                    Zombie z = (Zombie) WorldManager.getWorld().spawnEntity(worldConfig.getHunterLocationConfig().getLocation(1), EntityType.ZOMBIE);
                                    z.setBaby(false);
                                    z.getEquipment().setHelmet(new ItemStack(Material.DIAMOND_HELMET));
                                    z.getEquipment().setChestplate(new ItemStack(Material.DIAMOND_CHESTPLATE));
                                    z.getEquipment().setLeggings(new ItemStack(Material.DIAMOND_LEGGINGS));
                                    z.getEquipment().setBoots(new ItemStack(Material.DIAMOND_BOOTS));
                                    z.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 200000, 1, false, false));
                                }
                                bs.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + ChatColor.GREEN + ChatColor.UNDERLINE + v + "体" + ChatColor.RESET + ChatColor.GRAY + "をゾンビハンターに追加しました。");
                                return;
                            }
                            bs.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "引数が不正です。1以上で数字を指定してください。");
                            return;
                        } catch (NumberFormatException e) {
                            MainAPI.sendMessage(bs, MainAPI.ErrorMessage.ARGS_INTEGER);
                            return;
                        }
                    }
                    MainAPI.sendMessage(bs, MainAPI.ErrorMessage.ARGS_INTEGER);
                    return;
                }
                bs.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "このワールドの難易度がピースフルのため実行できません。");
                return;
            }
            bs.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "引数が不正です。\n" +
                    MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "コマンドの使い方:\n" +
                    MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "\"/hunter join <プレイヤー名>\" - ハンターに参加\n" +
                    MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "\"/hunter leave <プレイヤー名>\" - ハンターから離脱\n" +
                    MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "\"/hunter random <数値>\" または \"/hunter rand <数値>\" - 数値の数だけハンターをランダムで追加\n" +
                    MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "\"/hunter zombie <数値>\" - 数値の数だけゾンビハンターを追加");
            return;
        }
        bs.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "引数が不正です。\n" +
                MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "コマンドの使い方:\n" +
                MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "\"/hunter join <プレイヤー名>\" - ハンターに参加\n" +
                MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "\"/hunter leave <プレイヤー名>\" - ハンターから離脱\n" +
                MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "\"/hunter random <数値>\" または \"/hunter rand <数値>\" - 数値の数だけハンターをランダムで追加\n" +
                MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "\"/hunter zombie <数値>\" - 数値の数だけゾンビハンターを追加");
        return;
    }

    @Override
    public void onConsoleCommand(ConsoleCommandSender cs, Command cmd, String label, String[] args) {
        MainAPI.sendMessage(cs, MainAPI.ErrorMessage.NOT_PLAYER);
        return;
    }

    private void random(CommandSender s, String count, WorldConfig worldConfig) {
        try {
            if (num == -1 && Tuho.num == -1) {
                Main.shuffleList.clear();
                num = Integer.parseInt(count);

                s.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "ハンター募集を開始しました。");
                if (GameManager.isGame(GameManager.GameState.GAME)) {
                    for (Player player : Bukkit.getOnlinePlayers())
                        if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_JAIL, player))
                            player.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.WARNING) + "ハンターを" + ChatColor.GOLD + ChatColor.UNDERLINE + num + "人" + ChatColor.RESET + ChatColor.GOLD + "募集します。\n" +
                                    MainAPI.getPrefix(MainAPI.PrefixType.WARNING) + "ハンター希望の方は20秒以内に\"/h\"と入力してください。");
                } else {
                    Bukkit.broadcastMessage(MainAPI.getPrefix(MainAPI.PrefixType.WARNING) + "ハンターを" + ChatColor.GOLD + ChatColor.UNDERLINE + num + "人" + ChatColor.RESET + ChatColor.GOLD + "募集します。\n" +
                            MainAPI.getPrefix(MainAPI.PrefixType.WARNING) + "ハンター希望の方は20秒以内に\"/h\"と入力してください。");
                }

                new BukkitRunnable() {
                    int count = 20;

                    @Override
                    public void run() {
                        if (count == 10) {
                            Bukkit.broadcastMessage(MainAPI.getPrefix(MainAPI.PrefixType.WARNING) + "ハンター募集終了まで残り10秒です。");
                        } else if (count == 0) {
                            if (Main.shuffleList.size() > 0) {
                                Bukkit.broadcastMessage(MainAPI.getPrefix(MainAPI.PrefixType.WARNING) + "ハンターを" + ChatColor.GOLD + ChatColor.UNDERLINE + num + "人" + ChatColor.RESET + ChatColor.YELLOW + "選出しています…");
                                Collections.shuffle(Main.shuffleList);
                                for (int i = 0; i < num && i < Main.shuffleList.size(); i++) {
                                    Player p = Main.shuffleList.get(i);
                                    Main.shuffleList.remove(p);

                                    Teams.joinTeam(Teams.OnlineTeam.TOSO_HUNTER, p);
                                    p.setGameMode(GameMode.ADVENTURE);

                                    TosoGameAPI.setArmor(p);
                                    TosoGameAPI.setPotionEffect(p, true);
                                    TosoGameAPI.removeOp(p);

                                    if (Main.playerList.contains(p))
                                        Main.playerList.remove(p);

                                    TosoGameAPI.showPlayers(p);

                                    p.teleport(worldConfig.getHunterLocationConfig().getLocation(1));

                                    if (MissionManager.isBossBar())
                                        MissionManager.getBossBar().removePlayer(p);

                                    p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "あなたをハンターに追加しました。");
                                    Bukkit.broadcastMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + p.getName() + "がハンターになりました。");
                                }
                                s.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + ChatColor.GREEN + ChatColor.UNDERLINE + num + "人" + ChatColor.RESET + ChatColor.GRAY + "をハンターに追加しました。");
                                Main.shuffleList.clear();
                                num = -1;
                            } else {
                                Bukkit.broadcastMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "ハンターを希望する方がいなかったため選出をキャンセルしました。");
                                Main.shuffleList.clear();
                                num = -1;
                            }
                        }

                        count--;
                    }
                }.runTaskTimerAsynchronously(Main.getInstance(), 20, 20);
                return;
            }
            s.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "ハンター募集中のため実行できません。");
            return;
        } catch (NumberFormatException e) {
            s.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "引数が不正です。数字を指定してください。");
            return;
        }
    }

    @Override
    public List<String> onPlayerTabComplete(Player sp, Command cmd, String alias, String[] args) {
        if (TosoGameAPI.isAdmin(sp)) {
            if (args.length == 1) {
                if (args[0].length() == 0) {
                    return Arrays.asList("join", "leave", "random", "rand", "zombie");
                } else {
                    if ("join".startsWith(args[0])) {
                        return Collections.singletonList("join");
                    } else if ("leave".startsWith(args[0])) {
                        return Collections.singletonList("leave");
                    } else if ("random".startsWith(args[0])) {
                        return Collections.singletonList("random");
                    } else if ("rand".startsWith(args[0])) {
                        return Collections.singletonList("rand");
                    } else if ("zombie".startsWith(args[0])) {
                        return Collections.singletonList("zombie");
                    }
                }
            } else if (args.length == 2) {
                if (args[0].equalsIgnoreCase("join") || args[0].equalsIgnoreCase("leave")) {
                    if (args[1].length() == 0) {
                        ArrayList<String> list = new ArrayList<String>() {{
                            add("@all");
                            add("@team:admin");
                            add("@team:player");
                            add("@team:success");
                            add("@team:jail");
                            add("@team:tuho");
                        }};
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            list.add(player.getName());
                        }
                        return list;
                    } else {
                        if ("@".startsWith(args[1])) {
                            return Arrays.asList("@all", "@team:admin", "@team:player", "@team:success", "@team:jail", "@team:tuho");
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
                return Arrays.asList("join", "leave", "random", "rand", "zombie");
            } else {
                if ("join".startsWith(args[0])) {
                    return Collections.singletonList("join");
                } else if ("leave".startsWith(args[0])) {
                    return Collections.singletonList("leave");
                } else if ("random".startsWith(args[0])) {
                    return Collections.singletonList("random");
                } else if ("rand".startsWith(args[0])) {
                    return Collections.singletonList("rand");
                } else if ("zombie".startsWith(args[0])) {
                    return Collections.singletonList("zombie");
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

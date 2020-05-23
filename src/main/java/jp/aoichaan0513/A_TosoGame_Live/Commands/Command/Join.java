package jp.aoichaan0513.A_TosoGame_Live.Commands.Command;

import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI;
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.MissionManager;
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
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import java.util.List;

public class Join extends ICommand {

    public Join(String name) {
        super(name);
    }

    @Override
    public void onPlayerCommand(Player sp, Command cmd, String label, String[] args) {
        if (TosoGameAPI.isBroadCaster(sp) || TosoGameAPI.hasPermission(sp)) {
            if (args.length != 0) {
                if (TosoGameAPI.isBroadCaster(sp) || TosoGameAPI.isAdmin(sp)) {
                    for (String name : args) {
                        Player target = Bukkit.getPlayerExact(name);
                        if (target == null) {
                            sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + ChatColor.RED + ChatColor.UNDERLINE + name + ChatColor.RESET + ChatColor.GRAY + "はオフラインです。");
                            continue;
                        } else {
                            Player p = Bukkit.getPlayerExact(name);
                            if (!Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_PLAYER, p)) {
                                Teams.joinTeam(Teams.OnlineTeam.TOSO_PLAYER, p);
                                p.setGameMode(GameMode.ADVENTURE);

                                TosoGameAPI.setItem(WorldManager.GameType.START, p);
                                TosoGameAPI.setPotionEffect(p);
                                TosoGameAPI.removeOp(p);

                                Main.opGamePlayerSet.add(p.getUniqueId());

                                TosoGameAPI.showPlayers(p);
                                TosoGameAPI.hidePlayers(p);

                                for (PotionEffect effect : p.getActivePotionEffects())
                                    p.removePotionEffect(effect.getType());

                                if (MissionManager.isBossBar())
                                    MissionManager.getBossBar().addPlayer(p);

                                sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + p.getName() + "を逃走者に追加しました。");
                                p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "あなたを逃走者に追加しました。");
                                Bukkit.broadcastMessage(MainAPI.getPrefix(MainAPI.PrefixType.SUCCESS) + p.getName() + "がゲームに参加しました。");
                                continue;
                            }
                            sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + p.getName() + "はすでにゲームに参加しています。");
                            continue;
                        }
                    }
                    return;
                }
                MainAPI.sendMessage(sp, MainAPI.ErrorMessage.PERMISSIONS);
                return;
            } else {
                if (!Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_PLAYER, sp)) {
                    Teams.joinTeam(Teams.OnlineTeam.TOSO_PLAYER, sp);
                    sp.setGameMode(GameMode.ADVENTURE);

                    TosoGameAPI.setItem(WorldManager.GameType.START, sp);
                    TosoGameAPI.setPotionEffect(sp);
                    TosoGameAPI.removeOp(sp);

                    Main.opGamePlayerSet.add(sp.getUniqueId());

                    TosoGameAPI.showPlayers(sp);
                    TosoGameAPI.hidePlayers(sp);

                    for (PotionEffect effect : sp.getActivePotionEffects())
                        sp.removePotionEffect(effect.getType());

                    if (MissionManager.isBossBar())
                        MissionManager.getBossBar().addPlayer(sp);

                    sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "あなたを逃走者に追加しました。");
                    Bukkit.broadcastMessage(MainAPI.getPrefix(MainAPI.PrefixType.SUCCESS) + sp.getName() + "がゲームに参加しました。");
                    return;
                }
                sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "すでにゲームに参加しています。");
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
                    if (!Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_PLAYER, p)) {
                        Teams.joinTeam(Teams.OnlineTeam.TOSO_PLAYER, p);
                        p.setGameMode(GameMode.ADVENTURE);

                        TosoGameAPI.setItem(WorldManager.GameType.START, p);
                        TosoGameAPI.setPotionEffect(p);
                        TosoGameAPI.removeOp(p);

                        Main.opGamePlayerSet.add(p.getUniqueId());

                        TosoGameAPI.showPlayers(p);
                        TosoGameAPI.hidePlayers(p);

                        for (PotionEffect effect : p.getActivePotionEffects())
                            p.removePotionEffect(effect.getType());

                        if (MissionManager.isBossBar())
                            MissionManager.getBossBar().addPlayer(p);

                        bs.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + p.getName() + "を逃走者に追加しました。");
                        p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "あなたを逃走者に追加しました。");
                        Bukkit.broadcastMessage(MainAPI.getPrefix(MainAPI.PrefixType.SUCCESS) + p.getName() + "がゲームに参加しました。");
                        continue;
                    }
                    bs.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + p.getName() + "はすでにゲームに参加しています。");
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
                    if (!Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_PLAYER, p)) {
                        Teams.joinTeam(Teams.OnlineTeam.TOSO_PLAYER, p);
                        p.setGameMode(GameMode.ADVENTURE);

                        TosoGameAPI.setItem(WorldManager.GameType.START, p);
                        TosoGameAPI.setPotionEffect(p);
                        TosoGameAPI.removeOp(p);

                        Main.opGamePlayerSet.add(p.getUniqueId());

                        TosoGameAPI.showPlayers(p);
                        TosoGameAPI.hidePlayers(p);

                        for (PotionEffect effect : p.getActivePotionEffects())
                            p.removePotionEffect(effect.getType());

                        if (MissionManager.isBossBar())
                            MissionManager.getBossBar().addPlayer(p);

                        cs.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + p.getName() + "を逃走者に追加しました。");
                        p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "あなたを逃走者に追加しました。");
                        Bukkit.broadcastMessage(MainAPI.getPrefix(MainAPI.PrefixType.SUCCESS) + p.getName() + "がゲームに参加しました。");
                        continue;
                    }
                    cs.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + p.getName() + "はすでにゲームに参加しています。");
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

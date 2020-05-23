package jp.aoichaan0513.A_TosoGame_Live.Listeners;

import jp.aoichaan0513.A_TosoGame_Live.API.Enums.Advancement;
import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI;
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.*;
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.Player.PlayerConfig;
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.Player.PlayerManager;
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.World.WorldConfig;
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.World.WorldManager;
import jp.aoichaan0513.A_TosoGame_Live.API.Maps.MapUtility;
import jp.aoichaan0513.A_TosoGame_Live.API.Scoreboard.Scoreboard;
import jp.aoichaan0513.A_TosoGame_Live.API.Scoreboard.Teams;
import jp.aoichaan0513.A_TosoGame_Live.API.TosoGameAPI;
import jp.aoichaan0513.A_TosoGame_Live.Main;
import jp.aoichaan0513.A_TosoGame_Live.Runnable.RespawnRunnable;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scoreboard.DisplaySlot;

public class onJoin implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();

        e.setJoinMessage(!MainAPI.isHidePlayer(p) ? ChatColor.YELLOW + "-> " + ChatColor.GOLD + p.getName() : "");

        WorldConfig worldConfig = Main.getWorldConfig();

        PlayerConfig playerConfig = PlayerManager.loadConfig(p);
        if (!playerConfig.getAdvancementConfig().hasAdvancement(Advancement.FIRST_JOIN)) {
            playerConfig.getAdvancementConfig().addAdvancement(Advancement.FIRST_JOIN);
            Advancement.FIRST_JOIN.sendMessage(p);
        }

        if (!TosoGameAPI.difficultyMap.containsKey(p.getUniqueId()))
            TosoGameAPI.difficultyMap.put(p.getUniqueId(), WorldManager.Difficulty.NORMAL);

        WorldConfig.DifficultyConfig difficultyConfig = worldConfig.getDifficultyConfig(WorldManager.Difficulty.NORMAL);

        if (TosoGameAPI.isAdmin(p) && MapUtility.getMap() == null)
            p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.WARNING) + "マップの設定がまだ完了していません。\n" +
                    MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "マップの設定をした後に\"/map generate\"を実行してください。");

        BossBarManager.showBar(p);
        if (!MoneyManager.hasRate(p))
            MoneyManager.setRate(p, difficultyConfig.getRate());

        Scoreboard.addHidePlayer(p);
        TosoGameAPI.setPotionEffect(p, true);

        MissionManager.reloadBook(p);
        if (MissionManager.isBossBar())
            MissionManager.getBossBar().addPlayer(p);

        if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_PLAYER, p)) {
            Main.opGamePlayerSet.add(p.getUniqueId());

            if (OPGameManager.getOPGame())
                TosoGameAPI.teleport(p, worldConfig.getOPGameLocationConfig().getGOPLocations().values());
        } else if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_JAIL, p)) {
            RespawnRunnable.setCoolTime(p);
            /*
            p.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, TosoGameAPI.respawnCoolTimeMap.getOrDefault(p.getUniqueId(), 1) * (difficultyConfig.getRespawnCoolTime() * 20), 1, false, false));
            if (TosoGameAPI.respawnCoolTimeMap.containsKey(p.getUniqueId()))
                TosoGameAPI.respawnCoolTimeMap.replace(p.getUniqueId(), TosoGameAPI.respawnCoolTimeMap.get(p.getUniqueId()), TosoGameAPI.respawnCoolTimeMap.get(p.getUniqueId()) + 1);
            else
                TosoGameAPI.respawnCoolTimeMap.put(p.getUniqueId(), 2);
            */

            TosoGameAPI.teleport(p, worldConfig.getJailLocationConfig().getLocations().values());
        } else if (!Teams.hasJoinedTeams(p)) {
            if (GameManager.isGame()) {
                if (TosoGameAPI.isRes) {
                    Teams.joinTeam(Teams.OnlineTeam.TOSO_PLAYER, p);

                    TosoGameAPI.setItem(WorldManager.GameType.START, p);
                    TosoGameAPI.setPotionEffect(p);

                    Main.opGamePlayerSet.add(p.getUniqueId());

                    if (OPGameManager.getOPGame()) {
                        TosoGameAPI.teleport(p, worldConfig.getOPGameLocationConfig().getGOPLocations().values());
                    } else {
                        TosoGameAPI.teleport(p, worldConfig.getRespawnLocationConfig().getLocations().values());
                    }
                } else {
                    p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "途中参加が禁止のため牢獄に追加されました。");

                    Teams.joinTeam(Teams.OnlineTeam.TOSO_JAIL, p);

                    /*
                    p.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, TosoGameAPI.respawnCoolTimeMap.getOrDefault(p.getUniqueId(), 1) * (difficultyConfig.getRespawnCoolTime() * 20), 1, false, false));
                    if (TosoGameAPI.respawnCoolTimeMap.containsKey(p.getUniqueId()))
                        TosoGameAPI.respawnCoolTimeMap.replace(p.getUniqueId(), TosoGameAPI.respawnCoolTimeMap.get(p.getUniqueId()), TosoGameAPI.respawnCoolTimeMap.get(p.getUniqueId()) + 1);
                    else
                        TosoGameAPI.respawnCoolTimeMap.put(p.getUniqueId(), 2);
                    */

                    TosoGameAPI.teleport(p, worldConfig.getJailLocationConfig().getLocations().values());
                }
            } else {
                Teams.joinTeam(Teams.OnlineTeam.TOSO_PLAYER, p);

                TosoGameAPI.setItem(WorldManager.GameType.START, p);
                TosoGameAPI.setPotionEffect(p);

                Main.opGamePlayerSet.add(p.getUniqueId());

                TosoGameAPI.teleport(p, worldConfig.getRespawnLocationConfig().getLocations().values());
            }
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            TosoGameAPI.showPlayers(player);

            org.bukkit.scoreboard.Scoreboard board = Scoreboard.getBoard(player);

            if (!TosoGameAPI.isAdmin(player)) {
                if (player.getInventory().getItemInMainHand().getType() == Material.BOOK) {
                    ItemMeta itemMeta = player.getInventory().getItemInMainHand().getItemMeta();

                    if (ChatColor.stripColor(itemMeta.getDisplayName()).equals(Main.PHONE_ITEM_NAME)) {
                        board.getObjective(TosoGameAPI.Objective.SIDEBAR.getName()).setDisplaySlot(DisplaySlot.SIDEBAR);
                    } else {
                        board.clearSlot(DisplaySlot.SIDEBAR);
                    }
                } else if (player.getInventory().getItemInOffHand().getType() == Material.BOOK) {
                    ItemMeta itemMeta = player.getInventory().getItemInOffHand().getItemMeta();

                    if (ChatColor.stripColor(itemMeta.getDisplayName()).equals(Main.PHONE_ITEM_NAME)) {
                        board.getObjective(TosoGameAPI.Objective.SIDEBAR.getName()).setDisplaySlot(DisplaySlot.SIDEBAR);
                    } else {
                        board.clearSlot(DisplaySlot.SIDEBAR);
                    }
                } else {
                    board.clearSlot(DisplaySlot.SIDEBAR);
                }
            } else {
                board.getObjective(TosoGameAPI.Objective.SIDEBAR.getName()).setDisplaySlot(DisplaySlot.SIDEBAR);
            }
        }
        p.setPlayerListHeaderFooter("" + ChatColor.RED + ChatColor.BOLD + "Run" + ChatColor.RESET + ChatColor.GRAY + " for " + ChatColor.DARK_RED + ChatColor.BOLD + "Money", "");
        p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "現在のチームは" + ChatColor.BOLD + ChatColor.UNDERLINE + Teams.getJoinedTeam(p).getDisplayName() + ChatColor.RESET + ChatColor.GRAY + "に設定されています。\n" +
                MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "現在の難易度は" + ChatColor.BOLD + ChatColor.UNDERLINE + TosoGameAPI.difficultyMap.get(p.getUniqueId()).getDisplayName() + ChatColor.RESET + ChatColor.GRAY + "に設定されています。");

        org.bukkit.scoreboard.Scoreboard scoreboard = Scoreboard.getBoard(p);
        Teams.setTeamOption(scoreboard);
        p.setScoreboard(scoreboard);

        Teams.setTeamOptions();
        return;
    }
}


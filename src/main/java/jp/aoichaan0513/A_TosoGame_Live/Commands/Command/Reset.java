package jp.aoichaan0513.A_TosoGame_Live.Commands.Command;

import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI;
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.*;
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.Inventory.Left.Call;
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.Inventory.Right.MissionInventory;
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.Player.PlayerConfig;
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.Player.PlayerManager;
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.World.WorldConfig;
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.World.WorldManager;
import jp.aoichaan0513.A_TosoGame_Live.API.Scoreboard.Scoreboard;
import jp.aoichaan0513.A_TosoGame_Live.API.Scoreboard.Teams;
import jp.aoichaan0513.A_TosoGame_Live.API.TosoGameAPI;
import jp.aoichaan0513.A_TosoGame_Live.Commands.ICommand;
import jp.aoichaan0513.A_TosoGame_Live.Listeners.onDamage;
import jp.aoichaan0513.A_TosoGame_Live.Listeners.onInteract;
import jp.aoichaan0513.A_TosoGame_Live.Listeners.onInventory;
import jp.aoichaan0513.A_TosoGame_Live.Listeners.onMove;
import jp.aoichaan0513.A_TosoGame_Live.Main;
import jp.aoichaan0513.A_TosoGame_Live.Mission.HunterZone;
import jp.aoichaan0513.A_TosoGame_Live.Runnable.RespawnRunnable;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Reset extends ICommand {

    public Reset(String name) {
        super(name);
    }

    @Override
    public void onPlayerCommand(Player sp, Command cmd, String label, String[] args) {
        if (TosoGameAPI.isAdmin(sp)) {
            if (!GameManager.isGame()) {
                reset(sp);
                return;
            }
            MainAPI.sendMessage(sp, MainAPI.ErrorMessage.GAME);
            return;
        }
        MainAPI.sendMessage(sp, MainAPI.ErrorMessage.PERMISSIONS);
        return;
    }

    @Override
    public void onBlockCommand(BlockCommandSender bs, Command cmd, String label, String[] args) {
        if (!GameManager.isGame()) {
            reset(bs);
            return;
        }
        MainAPI.sendMessage(bs, MainAPI.ErrorMessage.GAME);
        return;
    }

    @Override
    public void onConsoleCommand(ConsoleCommandSender cs, Command cmd, String label, String[] args) {
        MainAPI.sendMessage(cs, MainAPI.ErrorMessage.NOT_PLAYER);
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


    private void reset(CommandSender sender) {
        WorldConfig worldConfig = Main.getWorldConfig();

        sender.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "データをリセットしています…");

        GameManager.setGameState(GameManager.GameState.NONE);

        BossBarManager.resetBar();

        Call.reset();

        Main.opGamePlayerSet.clear();
        Main.hunterShuffleSet.clear();
        Main.tuhoShuffleSet.clear();
        Main.invisibleSet.clear();

        // TosoGameAPI.respawnCoolTimeMap.clear();
        RespawnRunnable.reset();
        TosoGameAPI.difficultyMap.clear();
        onMove.zoneList.clear();
        onDamage.hunterMap.clear();
        HunterZone.codeList.clear();

        for (Player player : Bukkit.getOnlinePlayers())
            TosoGameAPI.difficultyMap.put(player.getUniqueId(), WorldManager.Difficulty.NORMAL);

        OPGameManager.list.clear();
        OPGameManager.setOPGame(false);
        OPGameManager.setDice(false);
        OPGameManager.player = null;

        MissionManager.resetBossBar();
        MissionManager.resetMission();
        MissionInventory.reset();

        for (Map.Entry<UUID, Long> entry : MoneyManager.getRewardMap().entrySet()) {
            PlayerConfig playerConfig = PlayerManager.reloadConfig(entry.getKey());
            playerConfig.setMoney(playerConfig.getMoney() + MoneyManager.getReward(entry.getKey()));
        }

        MoneyManager.resetReward();
        MoneyManager.resetRate();

        for (Entity entity : WorldManager.getWorld().getEntities())
            if (entity.getType() == EntityType.ZOMBIE)
                entity.remove();

        onInventory.isAllowOpen = false;
        TosoGameAPI.isRes = true;
        onInteract.successBlockLoc = null;
        onInteract.hunterZoneBlockLoc = null;
        HunterZone.code = "";

        List<Player> list = (List<Player>) Bukkit.getOnlinePlayers();
        for (int i = 0; i < list.size(); i++) {
            long l = i / 20;
            Player player = list.get(i);

            player.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "あなたのデータは" + l + "秒後にリセットが行われます。");

            new BukkitRunnable() {

                @Override
                public void run() {
                    player.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "あなたのデータをリセットしています…");

                    Teams.setTeamOption(player);
                    TosoGameAPI.showPlayers(player);

                    if (!Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_ADMIN, player)) {
                        TosoGameAPI.removeArmor(player);
                        player.getInventory().clear();

                        Teams.joinTeam(Teams.OnlineTeam.TOSO_PLAYER, player);
                        player.setGameMode(GameMode.ADVENTURE);

                        Main.opGamePlayerSet.add(player.getUniqueId());

                        MissionManager.reloadBook(player);

                        for (PotionEffect effect : player.getActivePotionEffects())
                            player.removePotionEffect(effect.getType());

                        org.bukkit.scoreboard.Scoreboard board = Scoreboard.getBoard(player);

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

                        player.teleport(worldConfig.getRespawnLocationConfig().getLocation(1));
                    } else {
                        org.bukkit.scoreboard.Scoreboard board = Scoreboard.getBoard(player);
                        board.getObjective(TosoGameAPI.Objective.SIDEBAR.getName()).setDisplaySlot(DisplaySlot.SIDEBAR);
                    }

                    player.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SUCCESS) + "あなたのデータをリセットしました。\n" +
                            MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "現在のチームは" + ChatColor.BOLD + ChatColor.UNDERLINE + Teams.getJoinedTeam(player).getDisplayName() + ChatColor.RESET + ChatColor.GRAY + "に設定されています。\n" +
                            MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "現在の難易度は" + ChatColor.BOLD + ChatColor.UNDERLINE + TosoGameAPI.difficultyMap.get(player.getUniqueId()).getDisplayName() + ChatColor.RESET + ChatColor.GRAY + "に設定されています。");
                }
            }.runTaskLater(Main.getInstance(), l);
        }

        Teams.setTeamOptions();

        BossBarManager.showBar();

        Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> sender.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SUCCESS) + Bukkit.getOnlinePlayers().size() + "人のデータをリセットしました。"), (list.size() - 1) / 20);
        return;
    }
}

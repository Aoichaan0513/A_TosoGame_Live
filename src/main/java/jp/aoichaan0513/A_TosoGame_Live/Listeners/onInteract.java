package jp.aoichaan0513.A_TosoGame_Live.Listeners;

import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI;
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.GameManager;
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.Inventory.MainInventory;
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.Inventory.Right.MissionInventory;
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.MissionManager;
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.MoneyManager;
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.World.WorldConfig;
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.World.WorldManager;
import jp.aoichaan0513.A_TosoGame_Live.API.Scoreboard.Teams;
import jp.aoichaan0513.A_TosoGame_Live.API.TosoGameAPI;
import jp.aoichaan0513.A_TosoGame_Live.Main;
import jp.aoichaan0513.A_TosoGame_Live.Mission.HunterZone;
import jp.aoichaan0513.A_TosoGame_Live.Runnable.RespawnRunnable;
import jp.aoichaan0513.A_TosoGame_Live.Utils.DateTime.TimeFormat;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.type.Stairs;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Arrays;

public class onInteract implements Listener {

    public static Location successBlockLoc;
    public static Location hunterZoneBlockLoc;

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();

        WorldConfig worldConfig = Main.getWorldConfig();
        WorldConfig.DifficultyConfig difficultyConfig = worldConfig.getDifficultyConfig(p);

        WorldManager.GameType itemGameType = RespawnRunnable.getGameType(p);

        if (e.getAction() == Action.PHYSICAL) {
            if (!Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_JAIL, p)) return;
            if (e.getClickedBlock().getLocation().clone().add(0, -1, 0).getBlock().getType() == Material.IRON_BLOCK && e.getClickedBlock().getType() == Material.STONE_PRESSURE_PLATE) {
                if (GameManager.isGame(GameManager.GameState.GAME)) {
                    if (TosoGameAPI.isRes) {
                        if (RespawnRunnable.isAllowRespawn(p)) {
                            if (!RespawnRunnable.isCoolTime(p)) {
                                Teams.joinTeam(Teams.OnlineTeam.TOSO_PLAYER, p);
                                p.setGameMode(GameMode.ADVENTURE);

                                TosoGameAPI.setItem(WorldManager.GameType.RESPAWN, p);
                                TosoGameAPI.setPotionEffect(p);

                                Main.opGamePlayerSet.add(p.getUniqueId());

                                TosoGameAPI.showPlayers(p);
                                TosoGameAPI.teleport(p, worldConfig.getRespawnLocationConfig().getLocations().values());

                                p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "あなたを逃走者に追加しました。\n" +
                                        MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "あなたはあと" + (difficultyConfig.getRespawnDenyCount() - RespawnRunnable.getCount(p)) + "回復活できます。");
                                Bukkit.broadcastMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + p.getName() + "が復活しました。(" + ChatColor.UNDERLINE + "残り" + (Teams.getOnlineCount(Teams.OnlineTeam.TOSO_PLAYER) + Teams.getOnlineCount(Teams.OnlineTeam.TOSO_SUCCESS)) + "人" + ChatColor.RESET + ChatColor.GRAY + ")");
                                return;
                            } else {
                                TosoGameAPI.teleport(p, worldConfig.getJailLocationConfig().getLocations().values());
                                p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "あなたは" + ChatColor.RED + ChatColor.BOLD + ChatColor.UNDERLINE + "復活クールタイム中" + ChatColor.RESET + ChatColor.GRAY + "のため復活できません。");
                                return;
                            }
                        } else {
                            TosoGameAPI.teleport(p, worldConfig.getJailLocationConfig().getLocations().values());
                            p.setGameMode(GameMode.ADVENTURE);
                            p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "あなたは" + ChatColor.RED + ChatColor.BOLD + ChatColor.UNDERLINE + difficultyConfig.getRespawnDenyCount() + "回復活" + ChatColor.RESET + ChatColor.GRAY + "したためこれ以上は復活できません。");
                            return;
                        }

                        /*
                        if (TosoGameAPI.respawnCountMap.containsKey(p.getUniqueId())) {
                            if (RespawnRunnable.isAllowRespawn(p)) {
                                if (!p.hasPotionEffect(PotionEffectType.FIRE_RESISTANCE)) {
                                    TosoGameAPI.respawnCountMap.replace(p.getUniqueId(), TosoGameAPI.respawnCountMap.get(p.getUniqueId()), TosoGameAPI.respawnCountMap.get(p.getUniqueId()) + 1);

                                    Teams.joinTeam(Teams.OnlineTeam.TOSO_PLAYER, p);
                                    p.setGameMode(GameMode.ADVENTURE);

                                    TosoGameAPI.setItem(WorldManager.GameType.RESPAWN, p);
                                    TosoGameAPI.setPotionEffect(p);



                                        if (!Main.opGamePlayerList.contains(p.getUniqueId()))
                                            Main.opGamePlayerList.add(p.getUniqueId());

                                    TosoGameAPI.showPlayers(p);
                                    TosoGameAPI.teleport(p, worldConfig.getRespawnLocationConfig().getLocations());

                                    p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "あなたを逃走者に追加しました。\n" +
                                            MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "あなたはあと" + (difficultyConfig.getRespawnDenyCount() - TosoGameAPI.respawnCountMap.get(p.getUniqueId())) + "回復活できます。");
                                    Bukkit.broadcastMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + p.getName() + "が復活しました。(" + ChatColor.UNDERLINE + "残り" + (Teams.getOnlineCount(Teams.OnlineTeam.TOSO_PLAYER) + Teams.getOnlineCount(Teams.OnlineTeam.TOSO_SUCCESS)) + "人" + ChatColor.RESET + ChatColor.GRAY + ")");
                                    return;
                                } else {
                                    TosoGameAPI.teleport(p, worldConfig.getJailLocationConfig().getLocations());
                                    p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "あなたは" + ChatColor.RED + ChatColor.BOLD + ChatColor.UNDERLINE + "復活クールタイム中" + ChatColor.RESET + ChatColor.GRAY + "のため復活できません。");
                                    return;
                                }
                            } else {
                                TosoGameAPI.teleport(p, worldConfig.getJailLocationConfig().getLocations());
                                p.setGameMode(GameMode.ADVENTURE);
                                p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "あなたは" + ChatColor.RED + ChatColor.BOLD + ChatColor.UNDERLINE + difficultyConfig.getRespawnDenyCount() + "回復活" + ChatColor.RESET + ChatColor.GRAY + "したためこれ以上は復活できません。");
                                return;
                            }
                        } else {
                            TosoGameAPI.respawnCountMap.put(p.getUniqueId(), 1);

                            Teams.joinTeam(Teams.OnlineTeam.TOSO_PLAYER, p);
                            p.setGameMode(GameMode.ADVENTURE);

                            TosoGameAPI.setItem(WorldManager.GameType.RESPAWN, p);
                            TosoGameAPI.setPotionEffect(p);



                                        if (!Main.opGamePlayerList.contains(p.getUniqueId()))
                                            Main.opGamePlayerList.add(p.getUniqueId());

                            TosoGameAPI.showPlayers(p);
                            TosoGameAPI.teleport(p, worldConfig.getRespawnLocationConfig().getLocations());

                            p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "あなたを逃走者に追加しました。\n" +
                                    MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "あなたはあと" + (difficultyConfig.getRespawnDenyCount() - TosoGameAPI.respawnCountMap.get(p.getUniqueId())) + "回復活できます。");
                            Bukkit.broadcastMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + p.getName() + "が復活しました。(" + ChatColor.UNDERLINE + "残り" + (Teams.getOnlineCount(Teams.OnlineTeam.TOSO_PLAYER) + Teams.getOnlineCount(Teams.OnlineTeam.TOSO_SUCCESS)) + "人" + ChatColor.RESET + ChatColor.GRAY + ")");
                            return;
                        }
                        */
                    } else {
                        TosoGameAPI.teleport(p, worldConfig.getRespawnLocationConfig().getLocations().values());
                        p.setGameMode(GameMode.ADVENTURE);
                        p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "ゲーム終了まで" + ChatColor.RED + ChatColor.BOLD + ChatColor.UNDERLINE + "残り" + TimeFormat.formatJapan(worldConfig.getGameConfig().getRespawnDeny()) + "以下" + ChatColor.RESET + ChatColor.GRAY + "になったため復活できません。");
                        return;
                    }
                }
            }
        } else {
            if (p.getInventory().getItemInMainHand().getType() == Material.BOOK && (p.getInventory().getItemInOffHand().getType() == Material.AIR || p.getInventory().getItemInOffHand().getType() == Material.FILLED_MAP)
                    || p.getInventory().getItemInOffHand().getType() == Material.BOOK && (p.getInventory().getItemInMainHand().getType() == Material.AIR || p.getInventory().getItemInMainHand().getType() == Material.FILLED_MAP)) {
                if (e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK) {
                    if (p.getInventory().getItemInMainHand().getType() == Material.BOOK) {
                        ItemMeta meta = p.getInventory().getItemInMainHand().getItemMeta();
                        if (!ChatColor.stripColor(meta.getDisplayName()).equals(Main.PHONE_ITEM_NAME)) return;

                        e.setCancelled(true);
                        p.openInventory(MainInventory.getInventory(p));
                    } else {
                        ItemMeta meta = p.getInventory().getItemInOffHand().getItemMeta();
                        if (!ChatColor.stripColor(meta.getDisplayName()).equals(Main.PHONE_ITEM_NAME)) return;

                        e.setCancelled(true);
                        p.openInventory(MainInventory.getInventory(p));
                    }
                } else if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    if (p.getInventory().getItemInMainHand().getType() == Material.BOOK) {
                        ItemMeta meta = p.getInventory().getItemInMainHand().getItemMeta();
                        if (!ChatColor.stripColor(meta.getDisplayName()).equals(Main.PHONE_ITEM_NAME)) return;

                        e.setCancelled(true);
                        p.openInventory(MissionInventory.getInventory(MissionManager.MissionBookType.MISSION));
                    } else {
                        ItemMeta meta = p.getInventory().getItemInOffHand().getItemMeta();
                        if (!ChatColor.stripColor(meta.getDisplayName()).equals(Main.PHONE_ITEM_NAME)) return;

                        e.setCancelled(true);
                        p.openInventory(MissionInventory.getInventory(MissionManager.MissionBookType.MISSION));
                    }
                }
            } else {
                if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_ADMIN, p)) {
                    if (e.getAction() == Action.LEFT_CLICK_BLOCK) {
                        if (p.getInventory().getItemInMainHand().getType() == Material.GOLDEN_AXE || p.getInventory().getItemInOffHand().getType() == Material.GOLDEN_AXE) {
                            if (GameManager.isGame()) return;
                            e.setCancelled(true);
                            worldConfig.getMapBorderConfig().setLocation(WorldConfig.BorderType.POINT_1, e.getClickedBlock().getLocation());
                            p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.WARNING) + "マップのボーダーの角1を設定しました。\n" +
                                    MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + e.getClickedBlock().getX() + ", " + e.getClickedBlock().getY() + ", " + e.getClickedBlock().getZ());
                            return;
                        } else if (p.getInventory().getItemInMainHand().getType() == Material.IRON_AXE || p.getInventory().getItemInOffHand().getType() == Material.IRON_AXE) {
                            if (GameManager.isGame()) return;
                            e.setCancelled(true);
                            worldConfig.getHunterZoneBorderConfig().setLocation(WorldConfig.BorderType.POINT_1, e.getClickedBlock().getLocation());
                            p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.WARNING) + "ハンターゾーンのボーダーの角1を設定しました。\n" +
                                    MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + e.getClickedBlock().getX() + ", " + e.getClickedBlock().getY() + ", " + e.getClickedBlock().getZ());
                            return;
                        } else if (p.getInventory().getItemInMainHand().getType() == Material.STONE_AXE || p.getInventory().getItemInOffHand().getType() == Material.STONE_AXE) {
                            if (GameManager.isGame()) return;
                            e.setCancelled(true);
                            worldConfig.getOPGameBorderConfig().setLocation(WorldConfig.BorderType.POINT_1, e.getClickedBlock().getLocation());
                            p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.WARNING) + "オープニングゲームのボーダーの角1を設定しました。\n" +
                                    MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + e.getClickedBlock().getX() + ", " + e.getClickedBlock().getY() + ", " + e.getClickedBlock().getZ());
                            return;
                        }
                    } else if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                        if (p.getInventory().getItemInMainHand().getType() == Material.GOLDEN_AXE || p.getInventory().getItemInOffHand().getType() == Material.GOLDEN_AXE) {
                            if (GameManager.isGame()) return;
                            e.setCancelled(true);
                            worldConfig.getMapBorderConfig().setLocation(WorldConfig.BorderType.POINT_2, e.getClickedBlock().getLocation());
                            p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.WARNING) + "マップのボーダーの角2を設定しました。\n" +
                                    MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + e.getClickedBlock().getX() + ", " + e.getClickedBlock().getY() + ", " + e.getClickedBlock().getZ());
                            return;
                        } else if (p.getInventory().getItemInMainHand().getType() == Material.IRON_AXE || p.getInventory().getItemInOffHand().getType() == Material.IRON_AXE) {
                            if (GameManager.isGame()) return;
                            e.setCancelled(true);
                            worldConfig.getHunterZoneBorderConfig().setLocation(WorldConfig.BorderType.POINT_2, e.getClickedBlock().getLocation());
                            p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.WARNING) + "ハンターゾーンのボーダーの角2を設定しました。\n" +
                                    MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + e.getClickedBlock().getX() + ", " + e.getClickedBlock().getY() + ", " + e.getClickedBlock().getZ());
                            return;
                        } else if (p.getInventory().getItemInMainHand().getType() == Material.STONE_AXE || p.getInventory().getItemInOffHand().getType() == Material.STONE_AXE) {
                            if (GameManager.isGame()) return;
                            e.setCancelled(true);
                            worldConfig.getOPGameBorderConfig().setLocation(WorldConfig.BorderType.POINT_2, e.getClickedBlock().getLocation());
                            p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.WARNING) + "オープニングゲームのボーダーの角2を設定しました。\n" +
                                    MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + e.getClickedBlock().getX() + ", " + e.getClickedBlock().getY() + ", " + e.getClickedBlock().getZ());
                            return;
                        } else {
                            if (e.getClickedBlock().getType() == Material.OAK_BUTTON || e.getClickedBlock().getType() == Material.SPRUCE_BUTTON
                                    || e.getClickedBlock().getType() == Material.BIRCH_BUTTON || e.getClickedBlock().getType() == Material.JUNGLE_BUTTON
                                    || e.getClickedBlock().getType() == Material.ACACIA_BUTTON || e.getClickedBlock().getType() == Material.DARK_OAK_BUTTON
                                    || e.getClickedBlock().getType() == Material.STONE_BUTTON) {
                                Directional directional = (Directional) e.getClickedBlock().getBlockData();
                                Block block = e.getClickedBlock().getRelative(directional.getFacing());

                                if (block.getType() == Material.EMERALD_BLOCK) {
                                    if (GameManager.isGame(GameManager.GameState.GAME) && TosoGameAPI.isAdmin(p)) {
                                        if (worldConfig.getGameConfig().getSuccessMission()) {
                                            if (MissionManager.getMission() == MissionManager.MissionType.MISSION_SUCCESS || !MissionManager.isMission()) {
                                                successBlockLoc = block.getLocation();

                                                if (!MissionManager.isMission()) {
                                                    MissionManager.sendFileMission(MissionManager.MissionType.MISSION_SUCCESS.getId(), p);

                                                    TosoGameAPI.sendNotificationSound();

                                                    p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "生存ミッションを開始しました。\n" +
                                                            MainAPI.getPrefix(MainAPI.PrefixType.SUCCESS) + "ミッションを開始しました。");
                                                    Bukkit.broadcastMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "ミッションが通知されました。");
                                                } else {
                                                    p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "生存ミッションのブロック位置を変更しました。");
                                                }
                                            }
                                            return;
                                        } else {
                                            p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "生存ミッションが有効になっていないため開始できません。");
                                            return;
                                        }
                                    }
                                } else if (block.getType() == Material.BONE_BLOCK) {
                                    if (GameManager.isGame(GameManager.GameState.GAME) && TosoGameAPI.isAdmin(p)) {
                                        if (MissionManager.getMission() == MissionManager.MissionType.MISSION_HUNTER_ZONE || !MissionManager.isMission()) {
                                            hunterZoneBlockLoc = block.getLocation();

                                            if (!MissionManager.isMission()) {
                                                MissionManager.sendFileMission(MissionManager.MissionType.MISSION_HUNTER_ZONE.getId(), p);

                                                TosoGameAPI.sendNotificationSound();

                                                p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "ハンターゾーンミッションを開始しました。\n" +
                                                        MainAPI.getPrefix(MainAPI.PrefixType.SUCCESS) + "ミッションを開始しました。");
                                                Bukkit.broadcastMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "ミッションが通知されました。");
                                            } else {
                                                p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "ハンターゾーンミッションのブロック位置を変更しました。");
                                            }
                                        }
                                        return;
                                    }
                                }
                            }
                        }
                    }
                } else if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_PLAYER, p) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_SUCCESS, p)) {
                    if (p.getInventory().getItemInMainHand().getType() == Material.BONE) {
                        if (!GameManager.isGame(GameManager.GameState.GAME) || p.hasPotionEffect(PotionEffectType.INVISIBILITY) || p.hasCooldown(Material.BONE))
                            return;

                        int duration = 20 * difficultyConfig.getBone(itemGameType).getDuration();

                        p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);

                        p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, duration, 1, false, false));

                        Main.invisibleSet.add(p.getUniqueId());
                        for (Player player : Bukkit.getOnlinePlayers())
                            TosoGameAPI.showPlayers(player);

                        p.setCooldown(Material.BONE, duration + 20 * 5);

                        if (p.getInventory().getItemInMainHand().getAmount() == 1) {
                            p.getInventory().setItemInMainHand(null);
                        } else {
                            p.getInventory().getItemInMainHand().setAmount(p.getInventory().getItemInMainHand().getAmount() - 1);
                            p.getInventory().setItemInMainHand(p.getInventory().getItemInMainHand());
                        }

                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                Main.invisibleSet.remove(p.getUniqueId());
                                for (Player player : Bukkit.getOnlinePlayers())
                                    TosoGameAPI.showPlayers(player);
                            }
                        }.runTaskLater(Main.getInstance(), duration);
                        return;
                    } else if (p.getInventory().getItemInOffHand().getType() == Material.BONE) {
                        if (!GameManager.isGame(GameManager.GameState.GAME) || p.hasPotionEffect(PotionEffectType.INVISIBILITY) || p.hasCooldown(Material.BONE))
                            return;

                        int duration = 20 * difficultyConfig.getBone(itemGameType).getDuration();

                        p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);

                        p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, duration, 1, false, false));

                        Main.invisibleSet.add(p.getUniqueId());
                        for (Player player : Bukkit.getOnlinePlayers())
                            TosoGameAPI.showPlayers(player);

                        p.setCooldown(Material.BONE, duration + 20 * 5);

                        if (p.getInventory().getItemInOffHand().getAmount() == 1) {
                            p.getInventory().setItemInOffHand(null);
                        } else {
                            p.getInventory().getItemInOffHand().setAmount(p.getInventory().getItemInOffHand().getAmount() - 1);
                            p.getInventory().setItemInOffHand(p.getInventory().getItemInOffHand());
                        }

                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                Main.invisibleSet.remove(p.getUniqueId());
                                for (Player player : Bukkit.getOnlinePlayers())
                                    TosoGameAPI.showPlayers(player);
                            }
                        }.runTaskLater(Main.getInstance(), duration);
                        return;
                    } else if (p.getInventory().getItemInMainHand().getType() == Material.FEATHER) {
                        if (!GameManager.isGame(GameManager.GameState.GAME) || p.hasPotionEffect(PotionEffectType.SPEED) || p.hasCooldown(Material.FEATHER))
                            return;

                        int duration = 20 * difficultyConfig.getFeather(itemGameType).getDuration();

                        p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);

                        p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, duration, 1, false, false));

                        p.setCooldown(Material.FEATHER, duration + 20 * 5);

                        if (p.getInventory().getItemInMainHand().getAmount() == 1) {
                            p.getInventory().setItemInMainHand(null);
                        } else {
                            p.getInventory().getItemInMainHand().setAmount(p.getInventory().getItemInMainHand().getAmount() - 1);
                            p.getInventory().setItemInMainHand(p.getInventory().getItemInMainHand());
                        }
                        return;
                    } else if (p.getInventory().getItemInOffHand().getType() == Material.FEATHER) {
                        if (!GameManager.isGame(GameManager.GameState.GAME) || p.hasPotionEffect(PotionEffectType.SPEED) || p.hasCooldown(Material.FEATHER))
                            return;

                        int duration = 20 * difficultyConfig.getFeather(itemGameType).getDuration();

                        p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);

                        p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, duration, 1, false, false));

                        p.setCooldown(Material.FEATHER, duration + 20 * 5);

                        if (p.getInventory().getItemInOffHand().getAmount() == 1) {
                            p.getInventory().setItemInOffHand(null);
                        } else {
                            p.getInventory().getItemInOffHand().setAmount(p.getInventory().getItemInOffHand().getAmount() - 1);
                            p.getInventory().setItemInOffHand(p.getInventory().getItemInOffHand());
                        }
                        return;
                    } else if (p.getInventory().getItemInMainHand().getType() == Material.STICK || p.getInventory().getItemInOffHand().getType() == Material.STICK) {
                        if (!GameManager.isGame(GameManager.GameState.GAME)) return;
                        if (p.hasPotionEffect(PotionEffectType.NIGHT_VISION)) {
                            p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
                            p.removePotionEffect(PotionEffectType.NIGHT_VISION);
                            p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "暗視を無効にしました。");
                            return;
                        } else {
                            p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
                            p.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 200000, 1, false, false));
                            p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "暗視を有効にしました。");
                            return;
                        }
                    }
                } else if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_JAIL, p)) {
                    if (p.getInventory().getItemInMainHand().getType() == Material.ENDER_PEARL) {
                        e.setCancelled(true);
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            if (!TosoGameAPI.isAdmin(player)) {
                                p.hidePlayer(player);
                            }
                        }
                        p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SUCCESS) + ChatColor.GREEN + "周りを非表示にしました。");

                        ItemMeta itemMeta = p.getInventory().getItemInMainHand().getItemMeta();
                        itemMeta.setDisplayName(ChatColor.GREEN + "プレイヤーを表示");
                        itemMeta.setLore(Arrays.asList(ChatColor.YELLOW + "右クリックしてプレイヤーを表示します。"));
                        p.getInventory().getItemInMainHand().setType(Material.ENDER_EYE);
                        p.getInventory().getItemInMainHand().setItemMeta(itemMeta);
                    } else if (p.getInventory().getItemInOffHand().getType() == Material.ENDER_PEARL) {
                        e.setCancelled(true);
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            if (!TosoGameAPI.isAdmin(player)) {
                                p.hidePlayer(player);
                            }
                        }
                        p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SUCCESS) + ChatColor.GREEN + "周りを非表示にしました。");

                        ItemMeta itemMeta = p.getInventory().getItemInOffHand().getItemMeta();
                        itemMeta.setDisplayName(ChatColor.GREEN + "プレイヤーを表示");
                        itemMeta.setLore(Arrays.asList(ChatColor.YELLOW + "右クリックしてプレイヤーを表示します。"));
                        p.getInventory().getItemInOffHand().setType(Material.ENDER_EYE);
                        p.getInventory().getItemInOffHand().setItemMeta(itemMeta);
                    } else if (p.getInventory().getItemInMainHand().getType() == Material.ENDER_EYE) {
                        e.setCancelled(true);
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            p.showPlayer(player);
                        }
                        p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SUCCESS) + ChatColor.GREEN + "周りを表示しました。");

                        ItemMeta itemMeta = p.getInventory().getItemInMainHand().getItemMeta();
                        itemMeta.setDisplayName(ChatColor.GREEN + "プレイヤーを表示");
                        itemMeta.setLore(Arrays.asList(ChatColor.YELLOW + "右クリックしてプレイヤーを非表示にします。"));
                        p.getInventory().getItemInMainHand().setType(Material.ENDER_PEARL);
                        p.getInventory().getItemInMainHand().setItemMeta(itemMeta);
                    } else if (p.getInventory().getItemInOffHand().getType() == Material.ENDER_EYE) {
                        e.setCancelled(true);
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            p.showPlayer(player);
                        }
                        p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SUCCESS) + ChatColor.GREEN + "周りを表示しました。");

                        ItemMeta itemMeta = p.getInventory().getItemInOffHand().getItemMeta();
                        itemMeta.setDisplayName(ChatColor.GREEN + "プレイヤーを非表示");
                        itemMeta.setLore(Arrays.asList(ChatColor.YELLOW + "右クリックしてプレイヤーを非表示にします。"));
                        p.getInventory().getItemInOffHand().setType(Material.ENDER_PEARL);
                        p.getInventory().getItemInOffHand().setItemMeta(itemMeta);
                    }
                }
            }
        }

        if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (p.getInventory().getItemInMainHand().getType() == Material.AIR && e.getClickedBlock().getBlockData() instanceof Stairs
                    && ((Stairs) e.getClickedBlock().getBlockData()).getHalf() == Bisected.Half.BOTTOM
                    && e.getClickedBlock().getLocation().clone().add(0, -1, 0).getBlock().getType() == Material.BEDROCK) {
                Location loc = e.getClickedBlock().getLocation().clone().add(0.5, 0, 0.5);
                if (!Main.arrowSet.stream().anyMatch(arrow -> arrow.getLocation().getBlockX() == loc.getBlockX() && arrow.getLocation().getBlockY() == loc.getBlockY() && arrow.getLocation().getBlockZ() == loc.getBlockZ())) {
                    Arrow arrow = p.getWorld().spawnArrow(loc, new Vector(0, 1, 0), 0, 0);
                    arrow.setPassenger(p);
                    Main.arrowSet.add(arrow);
                }
            } else {
                if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_PLAYER, p) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_SUCCESS, p)) {
                    if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_PLAYER, p)) {
                        if (e.getClickedBlock().getType() == Material.OAK_SIGN || e.getClickedBlock().getType() == Material.SPRUCE_SIGN
                                || e.getClickedBlock().getType() == Material.BIRCH_SIGN || e.getClickedBlock().getType() == Material.JUNGLE_SIGN
                                || e.getClickedBlock().getType() == Material.ACACIA_SIGN || e.getClickedBlock().getType() == Material.DARK_OAK_SIGN
                                || e.getClickedBlock().getType() == Material.OAK_WALL_SIGN || e.getClickedBlock().getType() == Material.SPRUCE_WALL_SIGN
                                || e.getClickedBlock().getType() == Material.BIRCH_WALL_SIGN || e.getClickedBlock().getType() == Material.JUNGLE_WALL_SIGN
                                || e.getClickedBlock().getType() == Material.ACACIA_WALL_SIGN || e.getClickedBlock().getType() == Material.DARK_OAK_WALL_SIGN) {
                            Sign sign = (Sign) e.getClickedBlock().getState();

                            if (sign.getLine(0).equalsIgnoreCase(MainAPI.getPrefix()) && ChatColor.stripColor(sign.getLine(1)).equalsIgnoreCase("自首") && ChatColor.stripColor(sign.getLine(3)).equalsIgnoreCase("クリック")) {
                                Location loc = p.getLocation();

                                p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.WARNING) + ChatColor.GRAY + "");

                                new BukkitRunnable() {
                                    @Override
                                    public void run() {

                                    }
                                }.runTaskLater(Main.getInstance(), 20 * 10);

                                TosoGameAPI.teleport(p, worldConfig.getRespawnLocationConfig().getLocations().values());

                                Teams.joinTeam(Teams.OnlineTeam.TOSO_SUCCESS, p);
                                p.setGameMode(GameMode.SPECTATOR);
                                p.getInventory().clear();
                                p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "あなたを生存者 (自首)に追加しました。");
                                Bukkit.broadcastMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + p.getName() + "が自首しました。");
                                return;
                            }
                        } else {
                            if (MissionManager.getMission() == MissionManager.MissionType.MISSION_SUCCESS) {
                                if (e.getClickedBlock().getType() == Material.OAK_BUTTON || e.getClickedBlock().getType() == Material.SPRUCE_BUTTON
                                        || e.getClickedBlock().getType() == Material.BIRCH_BUTTON || e.getClickedBlock().getType() == Material.JUNGLE_BUTTON
                                        || e.getClickedBlock().getType() == Material.ACACIA_BUTTON || e.getClickedBlock().getType() == Material.DARK_OAK_BUTTON
                                        || e.getClickedBlock().getType() == Material.STONE_BUTTON) {
                                    if (!GameManager.isGame(GameManager.GameState.GAME)) return;

                                    Directional directional = (Directional) e.getClickedBlock().getBlockData();
                                    Block block = e.getClickedBlock().getRelative(directional.getFacing());

                                    if (block.getType() == Material.EMERALD_BLOCK) {
                                        if (block.getLocation().getBlockX() == successBlockLoc.getBlockX()
                                                && block.getLocation().getBlockY() == successBlockLoc.getBlockY()
                                                && block.getLocation().getBlockZ() == successBlockLoc.getBlockZ()) {
                                            Teams.joinTeam(Teams.OnlineTeam.TOSO_SUCCESS, p);
                                            p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "あなたを生存者に追加しました。\n" +
                                                    MainAPI.getPrefix(MainAPI.PrefixType.WARNING) + "サーバーから退出した場合は逃走者になります。");
                                        }
                                    }
                                }
                            } else if (MissionManager.getMission() == MissionManager.MissionType.MISSION_AREA) {
                                if (e.getClickedBlock().getType() == Material.GOLD_BLOCK) {
                                    if (p.getInventory().getItemInMainHand().getType() == Material.STONE_PRESSURE_PLATE || p.getInventory().getItemInOffHand().getType() == Material.STONE_PRESSURE_PLATE) {
                                        Location loc = e.getClickedBlock().getLocation().clone();
                                        loc.add(0, 1, 0);
                                        loc.getBlock().setType(Material.STONE_PRESSURE_PLATE);
                                        Inventory i = p.getInventory();
                                        i.removeItem(new ItemStack(Material.STONE_PRESSURE_PLATE, 1));
                                        return;
                                    }
                                }
                            } else if (MissionManager.getMission() == MissionManager.MissionType.MISSION_HUNTER_ZONE) {
                                if (e.getClickedBlock().getType() == Material.OAK_BUTTON || e.getClickedBlock().getType() == Material.SPRUCE_BUTTON
                                        || e.getClickedBlock().getType() == Material.BIRCH_BUTTON || e.getClickedBlock().getType() == Material.JUNGLE_BUTTON
                                        || e.getClickedBlock().getType() == Material.ACACIA_BUTTON || e.getClickedBlock().getType() == Material.DARK_OAK_BUTTON
                                        || e.getClickedBlock().getType() == Material.STONE_BUTTON) {
                                    if (!GameManager.isGame(GameManager.GameState.GAME)) return;

                                    Directional directional = (Directional) e.getClickedBlock().getBlockData();
                                    Block block = e.getClickedBlock().getRelative(directional.getFacing());

                                    if (block.getType() == Material.BONE_BLOCK) {
                                        if (block.getLocation().getBlockX() == hunterZoneBlockLoc.getBlockX()
                                                && block.getLocation().getBlockY() == hunterZoneBlockLoc.getBlockY()
                                                && block.getLocation().getBlockZ() == hunterZoneBlockLoc.getBlockZ()) {
                                            p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.WARNING) + "ハンターゾーンミッションのコード: " + HunterZone.code + "\n" +
                                                    MainAPI.getPrefix(MainAPI.PrefixType.WARNING) + "\"/code " + HunterZone.code + "\"と入力してミッションを完了してください。\n" +
                                                    MainAPI.getPrefix(MainAPI.PrefixType.WARNING) + "このコードを他の逃走者に教えるかどうかはあなた次第です。");
                                        }
                                    }
                                }
                            }
                        }
                    } else if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_SUCCESS, p)) {
                        if (MissionManager.getMission() == MissionManager.MissionType.MISSION_AREA) {
                            if (e.getClickedBlock().getType() == Material.GOLD_BLOCK) {
                                if (p.getInventory().getItemInMainHand().getType() == Material.STONE_PRESSURE_PLATE || p.getInventory().getItemInOffHand().getType() == Material.STONE_PRESSURE_PLATE) {
                                    Location loc = e.getClickedBlock().getLocation().clone();
                                    loc.add(0, 1, 0);
                                    loc.getBlock().setType(Material.STONE_PRESSURE_PLATE);
                                    Inventory i = p.getInventory();
                                    i.removeItem(new ItemStack(Material.STONE_PRESSURE_PLATE, 1));
                                    return;
                                }
                            }
                        } else if (MissionManager.getMission() == MissionManager.MissionType.MISSION_HUNTER_ZONE) {
                            if (e.getClickedBlock().getType() == Material.OAK_BUTTON || e.getClickedBlock().getType() == Material.SPRUCE_BUTTON
                                    || e.getClickedBlock().getType() == Material.BIRCH_BUTTON || e.getClickedBlock().getType() == Material.JUNGLE_BUTTON
                                    || e.getClickedBlock().getType() == Material.ACACIA_BUTTON || e.getClickedBlock().getType() == Material.DARK_OAK_BUTTON
                                    || e.getClickedBlock().getType() == Material.STONE_BUTTON) {
                                if (!GameManager.isGame(GameManager.GameState.GAME)) return;

                                Directional directional = (Directional) e.getClickedBlock().getBlockData();
                                Block block = e.getClickedBlock().getRelative(directional.getFacing());

                                if (block.getType() == Material.BONE_BLOCK) {
                                    if (block.getLocation().getBlockX() == hunterZoneBlockLoc.getBlockX()
                                            && block.getLocation().getBlockY() == hunterZoneBlockLoc.getBlockY()
                                            && block.getLocation().getBlockZ() == hunterZoneBlockLoc.getBlockZ()) {
                                        p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.WARNING) + "ハンターゾーンミッションのコード: " + HunterZone.code + "\n" +
                                                MainAPI.getPrefix(MainAPI.PrefixType.WARNING) + "\"/code " + HunterZone.code + "\"と入力してミッションを完了してください。\n" +
                                                MainAPI.getPrefix(MainAPI.PrefixType.WARNING) + "このコードを他の逃走者に教えるかどうかはあなた次第です。");
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onInteractEntity(PlayerInteractEntityEvent e) {
        Player damager = e.getPlayer();

        if (e.getRightClicked() instanceof ItemFrame) {
            if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_ADMIN, damager)) return;
            e.setCancelled(true);
        } else if (e.getRightClicked() instanceof Player) {
            Player player = (Player) e.getRightClicked();

            if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_ADMIN, damager)) {
                if (damager.getInventory().getItemInMainHand().getType() == Material.GOLD_NUGGET || damager.getInventory().getItemInOffHand().getType() == Material.GOLD_NUGGET) {
                    e.setCancelled(true);
                    damager.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SUCCESS) + player.getName() + "の情報\n" +
                            MainAPI.getPrefix(MainAPI.PrefixType.PRIMARY) + ChatColor.UNDERLINE + "権限所持者" + ChatColor.GRAY + ": " + (TosoGameAPI.hasPermission(player) ? "はい" : "いいえ") + "\n" +
                            MainAPI.getPrefix(MainAPI.PrefixType.PRIMARY) + ChatColor.UNDERLINE + "配信者" + ChatColor.GRAY + ": " + (TosoGameAPI.isBroadCaster(player) ? "はい" : "いいえ") + "\n" +
                            MainAPI.getPrefix(MainAPI.PrefixType.PRIMARY) + ChatColor.UNDERLINE + "チーム" + ChatColor.RESET + ChatColor.GRAY + ": " + ChatColor.YELLOW + Teams.getTeam(Teams.DisplaySlot.SIDEBAR, player) + "\n" +
                            MainAPI.getPrefix(MainAPI.PrefixType.PRIMARY) + ChatColor.UNDERLINE + "難易度" + ChatColor.RESET + ChatColor.GRAY + ": " + ChatColor.YELLOW + TosoGameAPI.difficultyMap.get(player.getUniqueId()).getDisplayName() + "\n" +
                            MainAPI.getPrefix(MainAPI.PrefixType.PRIMARY) + ChatColor.UNDERLINE + "賞金" + ChatColor.RESET + ChatColor.GRAY + ": " + ChatColor.YELLOW + MoneyManager.getReward(player) + ChatColor.GRAY + "円 (" + MoneyManager.getRate(player) + "円/秒)");
                    return;
                }
            }
        }
    }
}

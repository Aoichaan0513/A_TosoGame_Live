package jp.aoichaan0513.A_TosoGame_Live.Listeners;

import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI;
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.GameManager;
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.World.WorldConfig;
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.World.WorldManager;
import jp.aoichaan0513.A_TosoGame_Live.API.Scoreboard.Teams;
import jp.aoichaan0513.A_TosoGame_Live.API.TosoGameAPI;
import jp.aoichaan0513.A_TosoGame_Live.Main;
import jp.aoichaan0513.A_TosoGame_Live.Runnable.RespawnRunnable;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class onDamage implements Listener {

    public static HashMap<Player, Integer> hashMap = new HashMap<>();

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player && e.getEntity() instanceof Player) {
            Player damager = (Player) e.getDamager();
            Player player = (Player) e.getEntity();

            WorldConfig worldConfig = Main.getWorldConfig();
            WorldConfig.DifficultyConfig difficultyConfig = worldConfig.getDifficultyConfig(player);

            /*
            if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_ADMIN, damager) && (damager.getInventory().getItemInMainHand().getType() == Material.GOLD_NUGGET || damager.getInventory().getItemInOffHand().getType() == Material.GOLD_NUGGET)) {
                e.setCancelled(true);
                damager.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SUCCESS) + player.getName() + "の情報\n" +
                        MainAPI.getPrefix(MainAPI.PrefixType.WARNING) + "所属チーム" + ChatColor.GRAY + ": " + (Teams.getTeam(Teams.DisplaySlot.SIDEBAR, player).equals("") ? "不明" : Teams.getTeam(Teams.DisplaySlot.SIDEBAR, player)) + "\n" +
                        MainAPI.getPrefix(MainAPI.PrefixType.WARNING) + "権限所持者か" + ChatColor.GRAY + ": " + (TosoGameAPI.isPermissionHave(player) ? "はい" : "いいえ") + "\n" +
                        MainAPI.getPrefix(MainAPI.PrefixType.WARNING) + "配信者か" + ChatColor.GRAY + ": " + (TosoGameAPI.isBroadCaster(player) ? "はい" : "いいえ"));
                return;
            }
            */

            if (GameManager.isGame(GameManager.GameState.GAME)) {
                if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_PLAYER, player) && Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_HUNTER, damager)
                        || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_SUCCESS, player) && Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_HUNTER, damager)) {
                    if (!player.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
                        e.setCancelled(false);

                        if (hashMap.containsKey(damager)) {
                            hashMap.replace(damager, hashMap.get(damager), hashMap.get(damager) + 1);
                        } else {
                            hashMap.put(damager, 1);
                        }

                        damager.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + player.getName() + "を確保しました。");
                        player.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "あなたを牢獄に追加しました。\n" +
                                MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "あなたは" + damager.getName() + "に確保されました。3秒後に牢獄へテレポートします。");

                        Teams.joinTeam(Teams.OnlineTeam.TOSO_JAIL, player);
                        TosoGameAPI.setItem(WorldManager.GameType.RESPAWN, player);
                        TosoGameAPI.setPotionEffect(player);

                        if (Main.playerList.contains(player))
                            Main.playerList.remove(player);
                        if (onMove.zoneList.contains(player))
                            onMove.zoneList.remove(player);

                        TosoGameAPI.showPlayers(player);
                        TosoGameAPI.hidePlayers(player);

                        if (Main.invisibleList.contains(player.getUniqueId())) {
                            Main.invisibleList.remove(player.getUniqueId());
                            for (Player p : Bukkit.getOnlinePlayers())
                                p.showPlayer(player);
                        }
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                TosoGameAPI.teleport(player, worldConfig.getJailLocationConfig().getLocations());

                                RespawnRunnable.addCoolTime(player);
                                /*
                                player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, TosoGameAPI.respawnCoolTimeMap.getOrDefault(player.getUniqueId(), 1) * (difficultyConfig.getRespawnCoolTime() * 20), 1, false, false));
                                if (TosoGameAPI.respawnCoolTimeMap.containsKey(player.getUniqueId()))
                                    TosoGameAPI.respawnCoolTimeMap.replace(player.getUniqueId(), TosoGameAPI.respawnCoolTimeMap.get(player.getUniqueId()), TosoGameAPI.respawnCoolTimeMap.get(player.getUniqueId()) + 1);
                                else
                                    TosoGameAPI.respawnCoolTimeMap.put(player.getUniqueId(), 2);
                                */

                                Bukkit.broadcastMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + player.getName() + "が確保されました。(" + ChatColor.UNDERLINE + "残り" + (Teams.getOnlineCount(Teams.OnlineTeam.TOSO_PLAYER) + Teams.getOnlineCount(Teams.OnlineTeam.TOSO_SUCCESS)) + "人" + ChatColor.RESET + ChatColor.GRAY + ")");
                            }
                        }.runTaskLater(Main.getInstance(), 60);
                        return;
                    } else {
                        e.setCancelled(true);
                        return;
                    }
                } else if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_PLAYER, player) && Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_TUHO, damager)
                        || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_SUCCESS, player) && Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_TUHO, damager)) {
                    if ((!player.hasPotionEffect(PotionEffectType.INVISIBILITY)) && (!player.hasPotionEffect(PotionEffectType.GLOWING))) {
                        e.setCancelled(false);

                        damager.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + player.getName() + "の位置情報を通知しました。");
                        player.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "あなたの位置情報が通知されました。");

                        player.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 300, 1, false, false));

                        Location loc = player.getLocation();
                        for (String str : Teams.getOnlineTeam(Teams.OnlineTeam.TOSO_HUNTER).getEntries()) {
                            Player p = Bukkit.getPlayerExact(str);
                            p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.WARNING) + player.getName() + "の位置情報が通知されました。\n" +
                                    MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "X: " + loc.getBlockX() + ", Y: " + loc.getBlockY() + ", Z: " + loc.getBlockZ());
                        }
                        return;
                    } else {
                        e.setCancelled(true);
                        return;
                    }
                } else {
                    e.setCancelled(true);
                    if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_PLAYER, player) && Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_ADMIN, damager)) {
                        TosoGameAPI.teleport(player, worldConfig.getRespawnLocationConfig().getLocations());
                    } else if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_SUCCESS, player) && Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_ADMIN, damager)) {
                        TosoGameAPI.teleport(player, worldConfig.getRespawnLocationConfig().getLocations());
                    } else if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_JAIL, player) && Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_ADMIN, damager)) {
                        TosoGameAPI.teleport(player, worldConfig.getJailLocationConfig().getLocations());
                    } else if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_HUNTER, player) && Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_ADMIN, damager)) {
                        TosoGameAPI.teleport(player, worldConfig.getHunterLocationConfig().getLocation(1));
                    } else if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_TUHO, player) && Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_ADMIN, damager)) {
                        TosoGameAPI.teleport(player, worldConfig.getHunterLocationConfig().getLocation(1));
                    }
                }
            } else {
                e.setCancelled(true);
                if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_PLAYER, player) && Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_ADMIN, damager)) {
                    TosoGameAPI.teleport(player, worldConfig.getRespawnLocationConfig().getLocations());
                } else if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_SUCCESS, player) && Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_ADMIN, damager)) {
                    TosoGameAPI.teleport(player, worldConfig.getRespawnLocationConfig().getLocations());
                } else if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_JAIL, player) && Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_ADMIN, damager)) {
                    TosoGameAPI.teleport(player, worldConfig.getJailLocationConfig().getLocations());
                } else if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_HUNTER, player) && Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_ADMIN, damager)) {
                    TosoGameAPI.teleport(player, worldConfig.getHunterLocationConfig().getLocation(1));
                } else if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_TUHO, player) && Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_ADMIN, damager)) {
                    TosoGameAPI.teleport(player, worldConfig.getHunterLocationConfig().getLocation(1));
                }
            }
        } else if (e.getDamager() instanceof Zombie && e.getEntity() instanceof Player) {
            Zombie damager = (Zombie) e.getDamager();
            Player player = (Player) e.getEntity();

            WorldConfig worldConfig = Main.getWorldConfig();
            WorldConfig.DifficultyConfig difficultyConfig = worldConfig.getDifficultyConfig(player);

            if (GameManager.isGame(GameManager.GameState.GAME)) {
                if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_PLAYER, player) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_SUCCESS, player)) {
                    if (!player.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
                        e.setCancelled(false);

                        player.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "あなたを牢獄に追加しました。\n" +
                                MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "あなたはゾンビに確保されました。3秒後に牢獄へテレポートします。");
                        Teams.joinTeam(Teams.OnlineTeam.TOSO_JAIL, player);

                        TosoGameAPI.setItem(WorldManager.GameType.RESPAWN, player);
                        TosoGameAPI.setPotionEffect(player);

                        if (Main.playerList.contains(player))
                            Main.playerList.remove(player);
                        if (onMove.zoneList.contains(player))
                            onMove.zoneList.remove(player);

                        TosoGameAPI.showPlayers(player);
                        TosoGameAPI.hidePlayers(player);

                        if (Main.invisibleList.contains(player.getUniqueId())) {
                            Main.invisibleList.remove(player.getUniqueId());
                            for (Player p : Bukkit.getOnlinePlayers())
                                p.showPlayer(player);
                        }
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                TosoGameAPI.teleport(player, worldConfig.getJailLocationConfig().getLocations());

                                RespawnRunnable.addCoolTime(player);

                                /*
                                player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, TosoGameAPI.respawnCoolTimeMap.getOrDefault(player.getUniqueId(), 1) * (difficultyConfig.getRespawnCoolTime() * 20), 1, false, false));
                                if (TosoGameAPI.respawnCoolTimeMap.containsKey(player.getUniqueId()))
                                    TosoGameAPI.respawnCoolTimeMap.replace(player.getUniqueId(), TosoGameAPI.respawnCoolTimeMap.get(player.getUniqueId()), TosoGameAPI.respawnCoolTimeMap.get(player.getUniqueId()) + 1);
                                else
                                    TosoGameAPI.respawnCoolTimeMap.put(player.getUniqueId(), 2);
                                */

                                Bukkit.broadcastMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + player.getName() + "が確保されました。(" + ChatColor.UNDERLINE + "残り" + (Teams.getOnlineCount(Teams.OnlineTeam.TOSO_PLAYER) + Teams.getOnlineCount(Teams.OnlineTeam.TOSO_SUCCESS)) + "人" + ChatColor.RESET + ChatColor.GRAY + ")");
                            }
                        }.runTaskLater(Main.getInstance(), 60);
                        return;
                    } else {
                        e.setCancelled(true);
                        return;
                    }
                }
            }
        }
    }

    @EventHandler
    public void onProjectileDamage(EntityDamageByEntityEvent e) {
        if (!GameManager.isGame(GameManager.GameState.GAME)) return;
        Entity defent = e.getEntity();
        Entity attent = e.getDamager();

        if (defent instanceof Player) {
            Player def = (Player) defent;

            if (attent instanceof Egg) {
                if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_HUNTER, def)) {
                    e.setCancelled(false);
                    e.setDamage(0.1);
                    def.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20 * 5, 4, false, false));
                    def.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20 * 5, 4, false, false));
                } else {
                    e.setCancelled(true);
                }
            } else if (attent instanceof Snowball) {
                if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_HUNTER, def)) {
                    e.setCancelled(false);
                    e.setDamage(0.1);
                    if (!isFenceStop(def))
                        ironFence(def);
                } else {
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onProjectileLaunch(ProjectileLaunchEvent e) {
        Projectile projectile = e.getEntity();

        if (projectile instanceof EnderPearl) {
            ProjectileSource shooter = e.getEntity().getShooter();
            if (shooter instanceof Player) {
                Player p = (Player) shooter;
                if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_JAIL, p)) {
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent e) {
        Entity entity = e.getEntity();
        EntityDamageEvent.DamageCause damageCause = e.getCause();

        if (entity instanceof Player || entity instanceof Zombie) {
            if (entity instanceof Player && damageCause == EntityDamageEvent.DamageCause.FALL) {
                Player p = (Player) entity;
                double damage = e.getDamage();

                e.setCancelled(true);
                if (GameManager.isGame(GameManager.GameState.GAME)) {
                    if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_PLAYER, p) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_HUNTER, p) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_SUCCESS, p) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_TUHO, p)) {
                        if (p.hasPotionEffect(PotionEffectType.SLOW))
                            p.removePotionEffect(PotionEffectType.SLOW);

                        int level = -1;
                        if (damage < 4)
                            level = 0;
                        else if (damage < 7)
                            level = 1;
                        else if (damage < 12)
                            level = 2;
                        else
                            level = 3;

                        p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20 * 6 + 5, level, false, false));
                        p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "足を挫いてしまった！");
                        return;
                    }
                }
            } else if (entity instanceof Player && damageCause == EntityDamageEvent.DamageCause.VOID) {
                Player p = (Player) entity;

                e.setCancelled(true);

                WorldConfig worldConfig = Main.getWorldConfig();
                if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_PLAYER, p) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_SUCCESS, p)) {
                    TosoGameAPI.teleport(p, worldConfig.getRespawnLocationConfig().getLocations());
                } else if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_JAIL, p)) {
                    TosoGameAPI.teleport(p, worldConfig.getJailLocationConfig().getLocations());
                } else if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_HUNTER, p) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_TUHO, p)) {
                    TosoGameAPI.teleport(p, worldConfig.getHunterLocationConfig().getLocation(1));
                } else {
                    TosoGameAPI.teleport(p, worldConfig.getWorld().getSpawnLocation());
                }
            } else if (entity instanceof Zombie && damageCause == EntityDamageEvent.DamageCause.FALL) {
                Zombie z = (Zombie) entity;
                double damage = e.getDamage();

                e.setCancelled(true);
                if (GameManager.isGame(GameManager.GameState.GAME)) {
                    if (z.hasPotionEffect(PotionEffectType.SLOW))
                        z.removePotionEffect(PotionEffectType.SLOW);

                    int level = -1;
                    if (damage < 4)
                        level = 0;
                    else if (damage < 7)
                        level = 1;
                    else if (damage < 12)
                        level = 2;
                    else
                        level = 3;

                    z.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20 * 6 + 5, level, false, false));
                    return;
                }
            } else {
                if (damageCause == EntityDamageEvent.DamageCause.SUICIDE) return;
                e.setCancelled(true);
                return;
            }
        }
    }

    ArrayList<Player> fenceList = new ArrayList<>();
    @Deprecated
    HashMap<Integer, Location[][][]> fence = new HashMap<>();

    private void ironFence(Player p) {
        if (!isFenceStop(p)) {
            long time = 400;
            fenceList.add(p);
            Location loc1 = p.getLocation();
            Location loc3 = loc1.clone().add(1, 2, 0);
            Location loc4 = loc1.clone().add(1, 2, 1);
            Location loc5 = loc1.clone().add(0, 2, 1);
            Location loc6 = loc1.clone().add(-1, 2, 0);
            Location loc7 = loc1.clone().add(-1, 2, -1);
            Location loc8 = loc1.clone().add(0, 2, -1);

            Location loc9 = loc1.clone().add(1, 1, 0);
            Location loc10 = loc1.clone().add(1, 1, 1);
            Location loc11 = loc1.clone().add(0, 1, 1);
            Location loc12 = loc1.clone().add(-1, 1, 0);
            Location loc13 = loc1.clone().add(-1, 1, -1);
            Location loc14 = loc1.clone().add(0, 1, -1);

            Location loc15 = loc1.clone().add(1, 0, 0);
            Location loc16 = loc1.clone().add(1, 0, 1);
            Location loc17 = loc1.clone().add(0, 0, 1);
            Location loc18 = loc1.clone().add(-1, 0, 0);
            Location loc19 = loc1.clone().add(-1, 0, -1);
            Location loc20 = loc1.clone().add(0, 0, -1);

            Location loc21 = loc1.clone().add(1, 0, -1);
            Location loc22 = loc1.clone().add(-1, 0, 1);
            Location loc23 = loc1.clone().add(1, 1, -1);
            Location loc24 = loc1.clone().add(-1, 1, 1);
            Location loc25 = loc1.clone().add(1, 2, -1);
            Location loc26 = loc1.clone().add(-1, 2, 1);

            List<Location> llist = new ArrayList<Location>();
            llist.add(loc3);
            llist.add(loc4);
            llist.add(loc5);
            llist.add(loc6);
            llist.add(loc7);
            llist.add(loc8);
            llist.add(loc9);
            llist.add(loc10);
            llist.add(loc11);
            llist.add(loc12);
            llist.add(loc13);
            llist.add(loc14);
            llist.add(loc15);
            llist.add(loc16);
            llist.add(loc17);
            llist.add(loc18);
            llist.add(loc19);
            llist.add(loc20);
            llist.add(loc21);
            llist.add(loc22);
            llist.add(loc23);
            llist.add(loc24);
            llist.add(loc25);
            llist.add(loc26);

            final List<Location> rlist = new ArrayList<Location>();
            for (Location l : llist) {
                Block b = l.getBlock();
                if (b.getType().equals(Material.AIR)) {
                    rlist.add(l);
                    b.setType(Material.IRON_BARS);
                }
            }
            Location loc2 = loc1.clone().add(0, 2, 0);
            Block b2 = loc2.getBlock();
            if (b2.getType().equals(Material.AIR)) {
                rlist.add(loc2);
                b2.setType(Material.OBSIDIAN);
            }

            new BukkitRunnable() {
                public void run() {
                    for (Location l : rlist) {
                        Block b = l.getBlock();
                        b.setType(Material.AIR);
                    }
                    fenceList.remove(p);
                }
            }.runTaskLater(Main.getInstance(), time);
        }
    }

    @Deprecated
    private void fence(Player p) {
        if (!isFenceStop(p)) {
            long time = 300;
            fenceList.add(p);
            int i = placeFence(p.getLocation());
            new BukkitRunnable() {
                @Override
                public void run() {
                    fenceList.remove(p);
                    replaceFence(i, p.getLocation());
                }
            }.runTaskLater(Main.getInstance(), time);
        }
    }

    @Deprecated
    private int placeFence(Location l) {
        int i = 0;
        while (fence.containsKey(Integer.valueOf(i))) {
            i++;
        }
        Location[][][] locs = new Location[3][4][3];
        World w = l.getWorld();
        int cx = 0;
        for (int x = l.getBlockX() - 1; x < l.getBlockX() + 2; x++) {
            int cy = 0;
            for (int y = l.getBlockY() - 1; y < l.getBlockY() + 3; y++) {
                int cz = 0;
                for (int z = l.getBlockZ() - 1; z < l.getBlockZ() + 2; z++) {
                    if (x == l.getBlockX() && z == l.getBlockZ()) {
                        locs[cx][cy][cz] = null;
                        continue;
                    }
                    Location loc = new Location(w, x, y, z);
                    Block b = loc.getBlock();
                    if (b.getType().equals(Material.AIR)) {
                        b.setType(Material.IRON_BARS);
                        locs[cx][cy][cz] = loc;
                    } else {
                        locs[cx][cy][cz] = null;
                    }
                    cz++;
                }
                cy++;
            }
            cx++;
        }
        Block b = l.clone().add(0, 2, 0).getBlock();
        if (b.getType() == Material.AIR) {
            b.setType(Material.OBSIDIAN);
        }

        fence.put(Integer.valueOf(i), locs);
        return i;
    }

    @Deprecated
    private void replaceFence(int i, Location l) {
        if (fence.containsKey(Integer.valueOf(i))) {
            Location[][][] locs = fence.get(Integer.valueOf(i));
            Block b = l.clone().add(0, 2, 0).getBlock();
            if (b.getType() == Material.OBSIDIAN) {
                b.setType(Material.AIR);
            }
            for (int x = 0; x < 3; x++) {
                for (int y = 0; y < 4; y++) {
                    for (int z = 0; z < 3; z++) {
                        Location loc = locs[x][y][z];
                        if (loc != null) {
                            loc.getBlock().setType(Material.AIR);
                        }
                    }
                }
            }
        }
    }

    private boolean isFenceStop(Player p) {
        return fenceList.contains(p);
    }
}

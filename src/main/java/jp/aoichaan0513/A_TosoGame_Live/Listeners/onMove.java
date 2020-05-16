package jp.aoichaan0513.A_TosoGame_Live.Listeners;

import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI;
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.ActionBarManager;
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.MissionManager;
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.OPGameManager;
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.World.WorldConfig;
import jp.aoichaan0513.A_TosoGame_Live.API.Scoreboard.Teams;
import jp.aoichaan0513.A_TosoGame_Live.API.TosoGameAPI;
import jp.aoichaan0513.A_TosoGame_Live.Main;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class onMove implements Listener {

    public static ArrayList<Player> zoneList = new ArrayList<>();

    @EventHandler(ignoreCancelled = true)
    public void onPlayerMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();

        if (!Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_ADMIN, p)) {
            WorldConfig worldConfig = Main.getWorldConfig();

            if (isAllowArea(e.getTo(), worldConfig.getMapBorderConfig())) {
                e.setTo(e.getFrom());
                p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "マップから出ることは出来ません。");
                return;
            }

            if (MissionManager.isMission()) {
                if (MissionManager.getMission() == MissionManager.MissionType.MISSION_HUNTER_ZONE) {
                    if (isAllowArea(e.getTo(), worldConfig.getHunterZoneBorderConfig())) {
                        if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_PLAYER, p) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_SUCCESS, p)) {
                            if (zoneList.contains(p)) {
                                zoneList.remove(p);
                            } else {
                                if (zoneList.size() < 3) {
                                    if (!zoneList.contains(p)) {
                                        zoneList.add(p);
                                    }
                                } else {
                                    e.setTo(e.getFrom());
                                    p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "3人以上は入れません。");
                                }
                            }
                        } else {
                            e.setTo(e.getFrom());
                            p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "現在入ることは出来ません。");
                        }
                        return;
                    }
                } else {
                    if (isAllowArea(e.getTo(), worldConfig.getHunterZoneBorderConfig())) {
                        if (!zoneList.contains(p)) {
                            e.setTo(e.getFrom());
                            p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "現在入ることは出来ません。");
                        } else {
                            zoneList.remove(p);
                        }
                        return;
                    }
                }
            } else {
                if (isAllowArea(e.getTo(), worldConfig.getHunterZoneBorderConfig())) {
                    if (!zoneList.contains(p)) {
                        e.setTo(e.getFrom());
                        p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "現在入ることは出来ません。");
                    } else {
                        zoneList.remove(p);
                    }
                    return;
                }
            }

            if (OPGameManager.getOPGame()) {
                if (OPGameManager.getDice()) {
                    if (OPGameManager.player != null) {
                        if (!p.getName().equals(OPGameManager.player.getName())) {
                            if (Main.playerList.contains(p)) {
                                if (isAllowArea(e.getTo(), worldConfig.getOPGameBorderConfig())) {
                                    e.setCancelled(true);
                                    p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "ここから外に出ることは出来ません。");
                                    new BukkitRunnable() {

                                        @Override
                                        public void run() {
                                            TosoGameAPI.teleport(p, worldConfig.getOPGameLocationConfig().getGOPLocations());
                                        }
                                    }.runTaskLater(Main.getInstance(), 1);
                                    return;
                                }
                            }
                        } else {
                            if (e.getFrom().getBlockX() != e.getTo().getBlockX() || e.getFrom().getBlockY() != e.getTo().getBlockY() || e.getFrom().getBlockZ() != e.getTo().getBlockZ())
                                e.setCancelled(true);
                        }
                    }
                }
            }

            if (!worldConfig.getGameConfig().getJump()) {
                if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_PLAYER, p) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_HUNTER, p) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_SUCCESS, p) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_TUHO, p)) {
                    if (p.isSprinting() && !p.isOnGround() && e.getTo().getY() > e.getFrom().getY() + 0.2d) {
                        e.setTo(e.getFrom());

                        ActionBarManager.sendActionBar(p, ChatColor.YELLOW + "⚠ ダッシュジャンプは出来ません。");
                        return;
                    }
                }
            }
        }
    }

    public boolean isAllowArea(Location loc, WorldConfig.IBorderConfig borderConfig) {
        return borderConfig.isLocation(WorldConfig.BorderType.POINT_1) && borderConfig.isLocation(WorldConfig.BorderType.POINT_2) &&
                isAllowArea(loc, borderConfig.getLocation(WorldConfig.BorderType.POINT_1), borderConfig.getLocation(WorldConfig.BorderType.POINT_2));
    }

    public boolean isAllowArea(Location loc, Location loc1, Location loc2) {
        int x1 = Math.min(loc1.getBlockX(), loc2.getBlockX());
        int x2 = Math.max(loc1.getBlockX(), loc2.getBlockX());
        int y1 = Math.min(loc1.getBlockY(), loc2.getBlockY());
        int y2 = Math.max(loc1.getBlockY(), loc2.getBlockY());
        int z1 = Math.min(loc1.getBlockZ(), loc2.getBlockZ());
        int z2 = Math.max(loc1.getBlockZ(), loc2.getBlockZ());

        /*
        if (loc1.getBlockX() == loc.getBlockX()) {
            if (loc.getBlockZ() >= z1 && loc.getBlockZ() <= z2 || loc.getBlockZ() <= z1 && loc.getBlockZ() >= z2)
                if (loc.getBlockY() >= y1 && loc.getBlockY() <= y2 || loc.getBlockY() <= y1 && loc.getBlockY() >= y2)
                    return true;
        } else if (loc1.getBlockZ() == loc.getBlockZ()) {
            if (loc.getBlockX() >= x1 && loc.getBlockX() <= x2 || loc.getBlockX() <= x1 && loc.getBlockX() >= x2)
                if (loc.getBlockY() >= y1 && loc.getBlockY() <= y2 || loc.getBlockY() <= y1 && loc.getBlockY() >= y2)
                    return true;
        } else if (loc2.getBlockX() == loc.getBlockX()) {
            if (loc.getBlockZ() >= z1 && loc.getBlockZ() <= z2 || loc.getBlockZ() <= z1 && loc.getBlockZ() >= z2)
                if (loc.getBlockY() >= y1 && loc.getBlockY() <= y2 || loc.getBlockY() <= y1 && loc.getBlockY() >= y2)
                    return true;
        } else if (loc2.getBlockZ() == loc.getBlockZ()) {
            if (loc.getBlockX() >= z1 && loc.getBlockX() <= z2 || loc.getBlockX() <= z1 && loc.getBlockX() >= z2)
                if (loc.getBlockY() >= y1 && loc.getBlockY() <= y2 || loc.getBlockY() <= y1 && loc.getBlockY() >= y2)
                    return true;
        }
        return false;
        */

        return loc1.getBlockX() == loc.getBlockX() && (loc.getBlockZ() >= z1 && loc.getBlockZ() <= z2 || loc.getBlockZ() <= z1 && loc.getBlockZ() >= z2) && (loc.getBlockY() >= y1 && loc.getBlockY() <= y2 || loc.getBlockY() <= y1 && loc.getBlockY() >= y2)
                || loc1.getBlockZ() == loc.getBlockZ() && (loc.getBlockX() >= x1 && loc.getBlockX() <= x2 || loc.getBlockX() <= x1 && loc.getBlockX() >= x2) && (loc.getBlockY() >= y1 && loc.getBlockY() <= y2 || loc.getBlockY() <= y1 && loc.getBlockY() >= y2)
                || loc2.getBlockX() == loc.getBlockX() && (loc.getBlockZ() >= z1 && loc.getBlockZ() <= z2 || loc.getBlockZ() <= z1 && loc.getBlockZ() >= z2) && (loc.getBlockY() >= y1 && loc.getBlockY() <= y2 || loc.getBlockY() <= y1 && loc.getBlockY() >= y2)
                || loc2.getBlockZ() == loc.getBlockZ() && (loc.getBlockX() >= x1 && loc.getBlockX() <= x2 || loc.getBlockX() <= x1 && loc.getBlockX() >= x2) && (loc.getBlockY() >= y1 && loc.getBlockY() <= y2 || loc.getBlockY() <= y1 && loc.getBlockY() >= y2);
    }

    public Location getKnockbackLoc(Location fromLoc, Location toLoc, Location loc1, Location loc2) {
        if (loc1.getBlockX() == toLoc.getBlockX()) {
            if (toLoc.getBlockZ() >= loc1.getBlockZ() && toLoc.getBlockZ() <= loc2.getBlockZ() || toLoc.getBlockZ() <= loc1.getBlockZ() && toLoc.getBlockZ() >= loc2.getBlockZ()) {
                if (toLoc.getBlockX() >= fromLoc.getBlockX()) {
                    return new Location(toLoc.getWorld(), toLoc.getBlockX() - 2, toLoc.getBlockY(), toLoc.getBlockZ(), toLoc.getYaw(), toLoc.getPitch());
                } else if (toLoc.getBlockX() <= fromLoc.getBlockX()) {
                    return new Location(toLoc.getWorld(), toLoc.getBlockX() + 2, toLoc.getBlockY(), toLoc.getBlockZ(), toLoc.getYaw(), toLoc.getPitch());
                }
            }
        } else if (loc1.getBlockZ() == toLoc.getBlockZ()) {
            if (toLoc.getBlockX() >= loc1.getBlockX() && toLoc.getBlockX() <= loc2.getBlockX() || toLoc.getBlockX() <= loc1.getBlockX() && toLoc.getBlockX() >= loc2.getBlockX()) {
                if (toLoc.getBlockZ() >= fromLoc.getBlockZ()) {
                    return new Location(toLoc.getWorld(), toLoc.getBlockX(), toLoc.getBlockY(), toLoc.getBlockZ() - 2, toLoc.getYaw(), toLoc.getPitch());
                } else if (toLoc.getBlockZ() <= fromLoc.getBlockZ()) {
                    return new Location(toLoc.getWorld(), toLoc.getBlockX(), toLoc.getBlockY(), toLoc.getBlockZ() + 2, toLoc.getYaw(), toLoc.getPitch());
                }
            }
        } else if (loc2.getBlockX() == toLoc.getBlockX()) {
            if (toLoc.getBlockZ() >= loc1.getBlockZ() && toLoc.getBlockZ() <= loc2.getBlockZ() || toLoc.getBlockZ() <= loc1.getBlockZ() && toLoc.getBlockZ() >= loc2.getBlockZ()) {
                if (toLoc.getBlockX() >= fromLoc.getBlockX()) {
                    return new Location(toLoc.getWorld(), toLoc.getBlockX() - 2, toLoc.getBlockY(), toLoc.getBlockZ(), toLoc.getYaw(), toLoc.getPitch());
                } else if (toLoc.getBlockX() <= fromLoc.getBlockX()) {
                    return new Location(toLoc.getWorld(), toLoc.getBlockX() + 2, toLoc.getBlockY(), toLoc.getBlockZ(), toLoc.getYaw(), toLoc.getPitch());
                }
            }
        } else if (loc2.getBlockZ() == toLoc.getBlockZ()) {
            if (toLoc.getBlockX() >= loc1.getBlockX() && toLoc.getBlockX() <= loc2.getBlockX() || toLoc.getBlockX() <= loc1.getBlockX() && toLoc.getBlockX() >= loc2.getBlockX()) {
                if (toLoc.getBlockZ() >= fromLoc.getBlockZ()) {
                    return new Location(toLoc.getWorld(), toLoc.getBlockX(), toLoc.getBlockY(), toLoc.getBlockZ() - 2, toLoc.getYaw(), toLoc.getPitch());
                } else if (toLoc.getBlockZ() <= fromLoc.getBlockZ()) {
                    return new Location(toLoc.getWorld(), toLoc.getBlockX(), toLoc.getBlockY(), toLoc.getBlockZ() + 2, toLoc.getYaw(), toLoc.getPitch());
                }
            }
        }
        return new Location(toLoc.getWorld(), toLoc.getBlockX(), toLoc.getBlockY(), toLoc.getBlockZ(), toLoc.getYaw(), toLoc.getPitch());
    }
}

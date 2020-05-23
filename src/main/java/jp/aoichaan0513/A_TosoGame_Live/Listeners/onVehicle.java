package jp.aoichaan0513.A_TosoGame_Live.Listeners;

import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI;
import jp.aoichaan0513.A_TosoGame_Live.API.Scoreboard.Teams;
import jp.aoichaan0513.A_TosoGame_Live.Main;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.spigotmc.event.entity.EntityDismountEvent;

public class onVehicle implements Listener {

    @EventHandler
    public void onVehicleEnter(VehicleEnterEvent e) {
        if (!(e.getEntered().getType() == EntityType.PLAYER)) return;
        Player p = (Player) e.getEntered();

        if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_ADMIN, p) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_JAIL, p))
            return;
        if (e.getVehicle().getType() == EntityType.MINECART || e.getVehicle().getType() == EntityType.BOAT
                || e.getVehicle().getType() == EntityType.PIG || e.getVehicle().getType() == EntityType.HORSE) {
            e.setCancelled(true);
            p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "乗り物に乗ることは出来ません。");
        }
    }

    @EventHandler
    public void onEntityDismount(EntityDismountEvent e) {
        Entity dismounted = e.getDismounted();
        Entity entity = e.getEntity();

        if (dismounted instanceof Arrow && entity instanceof Player) {
            Arrow arrow = (Arrow) dismounted;

            arrow.remove();
            Main.arrowSet.remove(Main.arrowSet.stream().filter(a -> a.hashCode() == arrow.hashCode()).findFirst().get());
        }
    }

    @EventHandler
    public void onPlayerBedEnter(PlayerBedEnterEvent e) {
        Player p = e.getPlayer();
        if (!Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_ADMIN, p)) {
            e.setCancelled(true);
            p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "ベッドに寝ることは出来ません。");
        }
    }
}

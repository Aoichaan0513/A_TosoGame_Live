package jp.aoichaan0513.A_TosoGame_Live.Listeners.Minecraft

import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI
import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI.PrefixType
import jp.aoichaan0513.A_TosoGame_Live.API.Scoreboard.Teams
import jp.aoichaan0513.A_TosoGame_Live.API.Scoreboard.Teams.OnlineTeam
import jp.aoichaan0513.A_TosoGame_Live.Main
import org.bukkit.entity.Arrow
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerBedEnterEvent
import org.bukkit.event.vehicle.VehicleEnterEvent
import org.spigotmc.event.entity.EntityDismountEvent

class onVehicle : Listener {

    @EventHandler
    fun onVehicleEnter(e: VehicleEnterEvent) {
        if (e.entered.type != EntityType.PLAYER) return
        val p = e.entered as Player
        if (Teams.hasJoinedTeam(OnlineTeam.TOSO_ADMIN, p) || Teams.hasJoinedTeam(OnlineTeam.TOSO_JAIL, p)) return
        if (e.vehicle.type == EntityType.MINECART || e.vehicle.type == EntityType.BOAT || e.vehicle.type == EntityType.PIG || e.vehicle.type == EntityType.HORSE) {
            e.isCancelled = true
            p.sendMessage("${MainAPI.getPrefix(PrefixType.ERROR)}乗り物に乗ることはできません。")
        }
    }

    @EventHandler
    fun onEntityDismount(e: EntityDismountEvent) {
        val dismounted = e.dismounted
        val entity = e.entity

        if (entity !is Player || dismounted !is Arrow || Main.arrowSet.none { it.hashCode() == dismounted.hashCode() }) return
        dismounted.remove()
        Main.arrowSet.removeIf { it.hashCode() == dismounted.hashCode() }
    }

    @EventHandler
    fun onPlayerBedEnter(e: PlayerBedEnterEvent) {
        val p = e.player
        if (!Teams.hasJoinedTeam(OnlineTeam.TOSO_ADMIN, p)) {
            e.isCancelled = true
            p.sendMessage("${MainAPI.getPrefix(PrefixType.ERROR)}ベッドに寝ることはできません。")
        }
    }
}
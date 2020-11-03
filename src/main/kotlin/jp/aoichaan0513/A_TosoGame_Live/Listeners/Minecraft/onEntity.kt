package jp.aoichaan0513.A_TosoGame_Live.Listeners.Minecraft

import jp.aoichaan0513.A_TosoGame_Live.API.Manager.GameManager
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.GameManager.GameState
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.World.WorldManager
import jp.aoichaan0513.A_TosoGame_Live.Utils.isPlayerGroup
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.CreatureSpawnEvent
import org.bukkit.event.entity.EntityBreakDoorEvent
import org.bukkit.event.entity.EntityTargetLivingEntityEvent
import org.bukkit.event.world.ChunkUnloadEvent
import org.bukkit.potion.PotionEffectType

class onEntity : Listener {

    @EventHandler
    fun onEntityBreakDoor(e: EntityBreakDoorEvent) {
        if (e.entityType != EntityType.PLAYER)
            e.isCancelled = true
    }

    @EventHandler
    fun onEntityTarget(e: EntityTargetLivingEntityEvent) {
        if (e.entity.location.world !== WorldManager.world || e.entityType != EntityType.ZOMBIE
                || e.target == null || e.target!!.type != EntityType.PLAYER) return

        val p = e.target as? Player ?: return

        if (GameManager.isGame(GameState.GAME)) {
            if (p.isPlayerGroup) {
                if (!p.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
                    e.isCancelled = false
                } else {
                    e.target = null
                    e.isCancelled = true
                }
            } else {
                e.target = null
                e.isCancelled = true
            }
        } else {
            e.target = null
            e.isCancelled = true
        }
    }

    @EventHandler
    fun onEntitySpawn(e: CreatureSpawnEvent) {
        if (e.location.world !== WorldManager.world || e.entityType == EntityType.ZOMBIE || e.entityType == EntityType.ARMOR_STAND || e.entityType == EntityType.VILLAGER) return
        e.isCancelled = true
    }

    @EventHandler
    fun onChunkUnloadEvent(e: ChunkUnloadEvent) {
        val world = e.world
        val chunk = e.chunk

        if (world !== WorldManager.world) return
        for (entity in world.entities) {
            if (entity.type == EntityType.ZOMBIE) {
                if (chunk === entity.location.chunk)
                    world.setChunkForceLoaded(chunk.x, chunk.z, true)
            } else if (entity.type == EntityType.ARROW) {
                val passenger = entity.passenger
                if (passenger != null && passenger.type == EntityType.PLAYER && chunk === entity.location.chunk)
                    world.setChunkForceLoaded(chunk.x, chunk.z, true)
            }
        }
    }
}
package jp.aoichaan0513.A_TosoGame_Live.Listeners.Minecraft

import jp.aoichaan0513.A_TosoGame_Live.Main
import jp.aoichaan0513.A_TosoGame_Live.Utils.isAdminTeam
import org.bukkit.block.data.type.Stairs
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.hanging.HangingBreakByEntityEvent

class onBreak : Listener {

    @EventHandler
    fun onBreak(e: BlockBreakEvent) {
        val block = e.block

        if (block.blockData !is Stairs || Main.arrowSet.none { it.location.blockX == block.x && it.location.blockY == block.y && it.location.blockZ == block.z }) return

        val armorStand = Main.arrowSet.first { it.location.blockX == block.x && it.location.blockY == block.y && it.location.blockZ == block.z }
        armorStand.remove()
        Main.arrowSet.remove(armorStand)
    }

    @EventHandler
    fun onHangingBreak(e: HangingBreakByEntityEvent) {
        val p = e.remover as? Player ?: return

        if (!p.isAdminTeam)
            e.isCancelled = true
    }
}
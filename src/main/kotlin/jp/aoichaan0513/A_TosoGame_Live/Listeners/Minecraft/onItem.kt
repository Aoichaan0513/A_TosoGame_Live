package jp.aoichaan0513.A_TosoGame_Live.Listeners.Minecraft

import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI
import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI.PrefixType
import jp.aoichaan0513.A_TosoGame_Live.API.Scoreboard.Scoreboard
import jp.aoichaan0513.A_TosoGame_Live.API.TosoGameAPI
import jp.aoichaan0513.A_TosoGame_Live.Main
import jp.aoichaan0513.A_TosoGame_Live.Utils.isAdminTeam
import jp.aoichaan0513.A_TosoGame_Live.Utils.isPlayerGroup
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityPickupItemEvent
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerItemHeldEvent
import org.bukkit.scoreboard.DisplaySlot

class onItem : Listener {

    @EventHandler
    fun onDropItem(e: PlayerDropItemEvent) {
        val p = e.player

        if (p.isAdminTeam) return
        e.isCancelled = true
        p.sendMessage("${MainAPI.getPrefix(PrefixType.ERROR)}アイテムを投げることはできません。")
    }

    @EventHandler
    fun onPickupItem(e: EntityPickupItemEvent) {
        val p = e.entity as? Player ?: return

        if (p.isAdminTeam) return
        e.isCancelled = true
    }

    @EventHandler
    fun onItemHeld(e: PlayerItemHeldEvent) {
        val p = e.player
        val item = p.inventory.getItem(e.newSlot)

        val board = Scoreboard.getBoard(p)
        val objective = board.getObjective(TosoGameAPI.Objective.SIDEBAR.objectName) ?: return
        if (p.isPlayerGroup) {
            if (!Scoreboard.isHidePlayer(p)) {
                if (item != null && item.type == Material.BOOK) {
                    val itemMeta = item.itemMeta

                    if (itemMeta != null && ChatColor.stripColor(itemMeta.displayName).equals(Main.PHONE_ITEM_NAME)) {
                        objective.displaySlot = DisplaySlot.SIDEBAR
                    } else {
                        board.clearSlot(DisplaySlot.SIDEBAR)
                    }
                } else {
                    board.clearSlot(DisplaySlot.SIDEBAR)
                }
            } else {
                objective.displaySlot = DisplaySlot.SIDEBAR
            }
        } else {
            if (board.getObjective(DisplaySlot.SIDEBAR) != null) return
            objective.displaySlot = DisplaySlot.SIDEBAR
        }
    }
}
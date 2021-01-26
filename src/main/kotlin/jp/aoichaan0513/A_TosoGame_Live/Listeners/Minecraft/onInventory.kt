package jp.aoichaan0513.A_TosoGame_Live.Listeners.Minecraft

import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI
import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI.PrefixType
import jp.aoichaan0513.A_TosoGame_Live.Inventory.*
import jp.aoichaan0513.A_TosoGame_Live.Mission.MissionManager
import jp.aoichaan0513.A_TosoGame_Live.Utils.isAdminTeam
import jp.aoichaan0513.A_TosoGame_Live.Utils.isHunterGroup
import jp.aoichaan0513.A_TosoGame_Live.Utils.isJailTeam
import jp.aoichaan0513.A_TosoGame_Live.Utils.isPlayerGroup
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.*
import org.bukkit.inventory.InventoryView

class onInventory : Listener {

    companion object {
        var isAllowOpen = false
    }

    private val ITEM_BLACKS = setOf(
            Material.FILLED_MAP, Material.BOOK,
            Material.BONE, Material.FEATHER,
            Material.EGG, Material.SNOWBALL,
            Material.RABBIT_FOOT, Material.SLIME_BALL,
            Material.ENDER_PEARL, Material.ENDER_EYE
    )

    @EventHandler
    fun onInventoryClick(e: InventoryClickEvent) {
        val p = e.whoClicked as Player
        val clickedInventory = e.clickedInventory
        // val inventory = e.inventory
        val inventoryView = e.view
        val itemStack = e.currentItem
        val slotType = e.slotType

        if (p.isAdminTeam) return

        if (p.isPlayerGroup || p.isJailTeam) {
            if (slotType == InventoryType.SlotType.CRAFTING) {
                e.result = Event.Result.DENY
                e.isCancelled = true
            } else {
                if (clickedInventory?.type != InventoryType.PLAYER) return

                if (itemStack == null || itemStack.type == Material.AIR || isAllowedInventory(inventoryView)) return

                if (ITEM_BLACKS.contains(itemStack.type)) {
                    e.result = Event.Result.DENY
                    e.isCancelled = true
                }
            }
        } else if (p.isHunterGroup) {
            e.result = Event.Result.DENY
            e.isCancelled = true
        }
    }

    @EventHandler
    fun onInventoryOpen(e: InventoryOpenEvent) {
        val p = e.player as Player
        // val inventory = e.inventory
        val inventoryView = e.view

        if (p.isAdminTeam) return

        if (isAllowedInventory(inventoryView)) return
        if (p.isPlayerGroup || p.isJailTeam) {
            if (MissionManager.isInventoryAllowOpenMission || isAllowOpen) return

            e.isCancelled = true
            p.sendMessage("${MainAPI.getPrefix(PrefixType.ERROR)}現在開くことができません。")
        } else if (p.isHunterGroup) {
            e.isCancelled = true
            p.sendMessage("${MainAPI.getPrefix(PrefixType.ERROR)}現在開くことができません。")
        }
    }

    @EventHandler
    fun onInventoryDrag(e: InventoryDragEvent) {
        val slots = e.rawSlots

        if (slots.any { it in 1..4 }) {
            e.result = Event.Result.DENY
            e.isCancelled = true
        }
    }

    @EventHandler
    fun onCraftItem(e: CraftItemEvent) {
        val p = e.whoClicked as Player

        if (p.isAdminTeam) return

        e.isCancelled = true
    }

    private fun isAllowedInventory(inventoryView: InventoryView): Boolean {
        val title = inventoryView.title
        return title == MainInventory.title || title == MissionInventory.missionTitle
                || title == MissionInventory.tutatuHintTitle || title == MissionInventory.endTitle
                || title == PlayerSettingsInventory.title || title == PlayerSettingsInventory.inventoryTitle
                || title == PlayerSettingsInventory.itemSelectTitle || title.startsWith(CallInventory.title)
                || title.startsWith(ResultInventory.title)
    }
}
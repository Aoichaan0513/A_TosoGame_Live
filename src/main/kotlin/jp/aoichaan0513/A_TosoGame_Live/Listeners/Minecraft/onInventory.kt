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

    private val itemBlackSet = setOf(Material.FILLED_MAP, Material.BOOK, Material.BONE, Material.FEATHER, Material.EGG, Material.SNOWBALL, Material.RABBIT_FOOT, Material.SLIME_BALL, Material.ENDER_PEARL, Material.ENDER_EYE)

    @EventHandler
    fun onInventoryClick(e: InventoryClickEvent) {
        val p = e.whoClicked as Player
        val clickedInventory = e.clickedInventory
        val inventory = e.inventory
        val inventoryView = e.view
        val itemStack = e.currentItem
        val slotType = e.slotType

        if (!p.isAdminTeam) {
            if (p.isPlayerGroup || p.isJailTeam) {
                // p.sendMessage("スロット番号: ${e.rawSlot}, ${e.slot}")
                if (slotType == InventoryType.SlotType.CRAFTING) {
                    e.result = Event.Result.DENY
                    e.isCancelled = true
                } else {
                    if (clickedInventory?.type != InventoryType.PLAYER) return

                    if (inventory.type == InventoryType.CHEST || inventory.type == InventoryType.ENDER_CHEST
                            || inventory.type == InventoryType.DISPENSER || inventory.type == InventoryType.DROPPER
                            || inventory.type == InventoryType.HOPPER || inventory.type == InventoryType.ANVIL
                            || inventory.type == InventoryType.SHULKER_BOX || inventory.type == InventoryType.BARREL
                            || inventory.type == InventoryType.BREWING || inventory.type == InventoryType.FURNACE
                            || inventory.type == InventoryType.SMOKER || inventory.type == InventoryType.BLAST_FURNACE) {
                        if (itemStack == null || itemStack.type == Material.AIR || isAllowedInventory(inventoryView)) return

                        if (itemBlackSet.contains(itemStack.type)) {
                            e.result = Event.Result.DENY
                            e.isCancelled = true
                        }
                    }
                }
            } else if (p.isHunterGroup) {
                e.result = Event.Result.DENY
                e.isCancelled = true
            }
        }
    }

    @EventHandler
    fun onInventoryOpen(e: InventoryOpenEvent) {
        val p = e.player as Player
        val inventory = e.inventory
        val inventoryView = e.view

        if (!p.isAdminTeam) {
            if (p.isPlayerGroup || p.isJailTeam) {
                if (inventory.type == InventoryType.CHEST || inventory.type == InventoryType.ENDER_CHEST
                        || inventory.type == InventoryType.DISPENSER || inventory.type == InventoryType.DROPPER
                        || inventory.type == InventoryType.HOPPER || inventory.type == InventoryType.ANVIL
                        || inventory.type == InventoryType.SHULKER_BOX || inventory.type == InventoryType.BARREL
                        || inventory.type == InventoryType.BREWING || inventory.type == InventoryType.FURNACE
                        || inventory.type == InventoryType.SMOKER || inventory.type == InventoryType.BLAST_FURNACE) {
                    if (isAllowedInventory(inventoryView) || MissionManager.isInventoryAllowOpenMission || isAllowOpen) return

                    e.isCancelled = true
                    p.sendMessage("${MainAPI.getPrefix(PrefixType.ERROR)}現在開くことができません。")
                }
            } else if (p.isHunterGroup) {
                if (inventory.type == InventoryType.CHEST || inventory.type == InventoryType.ENDER_CHEST
                        || inventory.type == InventoryType.DISPENSER || inventory.type == InventoryType.DROPPER
                        || inventory.type == InventoryType.HOPPER || inventory.type == InventoryType.ANVIL
                        || inventory.type == InventoryType.SHULKER_BOX || inventory.type == InventoryType.BARREL
                        || inventory.type == InventoryType.BREWING || inventory.type == InventoryType.FURNACE
                        || inventory.type == InventoryType.SMOKER || inventory.type == InventoryType.BLAST_FURNACE) {
                    if (isAllowedInventory(inventoryView)) return

                    e.isCancelled = true
                    p.sendMessage("${MainAPI.getPrefix(PrefixType.ERROR)}現在開くことができません。")
                }
            }
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
        return title.equals(MainInventory.title) || inventoryView.title.equals(MissionInventory.missionTitle)
                || inventoryView.title.equals(MissionInventory.tutatuHintTitle) || inventoryView.title.equals(MissionInventory.endTitle)
                || inventoryView.title.equals(PlayerSettingsInventory.title) || inventoryView.title.equals(PlayerSettingsInventory.inventoryTitle)
                || inventoryView.title.equals(PlayerSettingsInventory.itemSelectTitle) || inventoryView.title.startsWith(CallInventory.title)
                || inventoryView.title.startsWith(ResultInventory.title)
    }
}
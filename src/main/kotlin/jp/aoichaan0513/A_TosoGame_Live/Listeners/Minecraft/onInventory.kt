package jp.aoichaan0513.A_TosoGame_Live.Listeners.Minecraft

import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI
import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI.PrefixType
import jp.aoichaan0513.A_TosoGame_Live.API.Scoreboard.Teams
import jp.aoichaan0513.A_TosoGame_Live.API.Scoreboard.Teams.OnlineTeam
import jp.aoichaan0513.A_TosoGame_Live.Inventory.CallInventory
import jp.aoichaan0513.A_TosoGame_Live.Inventory.MainInventory
import jp.aoichaan0513.A_TosoGame_Live.Inventory.MissionInventory
import jp.aoichaan0513.A_TosoGame_Live.Inventory.PlayerSettingsInventory
import jp.aoichaan0513.A_TosoGame_Live.Mission.MissionManager
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.InventoryView

class onInventory : Listener {

    @EventHandler
    fun onInventoryClick(e: InventoryClickEvent) {
        val p = e.whoClicked as Player
        val inventory = e.inventory
        val itemStack = e.currentItem

        if (!Teams.hasJoinedTeam(OnlineTeam.TOSO_ADMIN, p)) {
            if (Teams.hasJoinedTeam(OnlineTeam.TOSO_PLAYER, p) || Teams.hasJoinedTeam(OnlineTeam.TOSO_SUCCESS, p) || Teams.hasJoinedTeam(OnlineTeam.TOSO_JAIL, p)) {
                if (inventory.type == InventoryType.PLAYER) {
                    if (itemStack == null || itemStack.type == Material.AIR) return
                    if (itemStack.type == Material.MAP || itemStack.type == Material.FILLED_MAP) {
                        e.result = Event.Result.DENY
                        e.isCancelled = true
                    }
                } else if (inventory.type == InventoryType.CHEST || inventory.type == InventoryType.ENDER_CHEST
                        || inventory.type == InventoryType.DISPENSER || inventory.type == InventoryType.DROPPER
                        || inventory.type == InventoryType.HOPPER || inventory.type == InventoryType.ANVIL
                        || inventory.type == InventoryType.SHULKER_BOX || inventory.type == InventoryType.BARREL) {
                    if (!MissionManager.isMission) {
                        e.result = Event.Result.DENY
                        e.isCancelled = true
                    }
                }
            } else if (Teams.hasJoinedTeam(OnlineTeam.TOSO_HUNTER, p) || Teams.hasJoinedTeam(OnlineTeam.TOSO_TUHO, p)) {
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

        if (!Teams.hasJoinedTeam(OnlineTeam.TOSO_ADMIN, p)) {
            if (Teams.hasJoinedTeam(OnlineTeam.TOSO_PLAYER, p) || Teams.hasJoinedTeam(OnlineTeam.TOSO_SUCCESS, p) || Teams.hasJoinedTeam(OnlineTeam.TOSO_JAIL, p)) {
                if (inventory.type == InventoryType.CHEST || inventory.type == InventoryType.ENDER_CHEST
                        || inventory.type == InventoryType.DISPENSER || inventory.type == InventoryType.DROPPER
                        || inventory.type == InventoryType.HOPPER || inventory.type == InventoryType.ANVIL
                        || inventory.type == InventoryType.SHULKER_BOX || inventory.type == InventoryType.BARREL
                        || inventory.type == InventoryType.BREWING || inventory.type == InventoryType.FURNACE) {
                    if (isAllowedInventory(inventoryView) || MissionManager.isMission || isAllowOpen) return

                    e.isCancelled = true
                    p.sendMessage("${MainAPI.getPrefix(PrefixType.ERROR)}現在開くことができません。")
                }
            } else if (Teams.hasJoinedTeam(OnlineTeam.TOSO_HUNTER, p) || Teams.hasJoinedTeam(OnlineTeam.TOSO_TUHO, p)) {
                if (inventory.type == InventoryType.CHEST || inventory.type == InventoryType.ENDER_CHEST
                        || inventory.type == InventoryType.DISPENSER || inventory.type == InventoryType.DROPPER
                        || inventory.type == InventoryType.HOPPER || inventory.type == InventoryType.ANVIL
                        || inventory.type == InventoryType.SHULKER_BOX || inventory.type == InventoryType.BARREL
                        || inventory.type == InventoryType.BREWING || inventory.type == InventoryType.FURNACE) {
                    if (isAllowedInventory(inventoryView)) return

                    e.isCancelled = true
                    p.sendMessage("${MainAPI.getPrefix(PrefixType.ERROR)}現在開くことができません。")
                }
            }
        }
    }

    private fun isAllowedInventory(inventoryView: InventoryView): Boolean {
        val title = inventoryView.title
        return title.equals(MainInventory.title) || inventoryView.title.equals(MissionInventory.missionTitle)
                || inventoryView.title.equals(MissionInventory.tutatuHintTitle) || inventoryView.title.equals(MissionInventory.endTitle)
                || inventoryView.title.equals(PlayerSettingsInventory.title) || inventoryView.title.equals(PlayerSettingsInventory.inventoryTitle)
                || inventoryView.title.equals(PlayerSettingsInventory.itemSelectTitle) || inventoryView.title.startsWith(CallInventory.title)
    }

    companion object {
        var isAllowOpen = false
    }
}
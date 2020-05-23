package jp.aoichaan0513.A_TosoGame_Live.Listeners;

import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI;
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.Inventory.Left.PlayerListInventory;
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.Inventory.MainInventory;
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.Inventory.Right.MissionInventory;
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.MissionManager;
import jp.aoichaan0513.A_TosoGame_Live.API.Scoreboard.Teams;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

public class onInventory implements Listener {

    public static boolean isAllowOpen = false;

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        Inventory inventory = e.getInventory();
        ItemStack itemStack = e.getCurrentItem();

        if (!Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_ADMIN, p)) {
            if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_PLAYER, p) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_SUCCESS, p) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_JAIL, p)) {
                if (inventory.getType() == InventoryType.PLAYER) {
                    if (itemStack == null || itemStack.getType() == Material.AIR) return;
                    if (itemStack.getType() == Material.MAP || itemStack.getType() == Material.FILLED_MAP) {
                        e.setResult(Event.Result.DENY);
                        e.setCancelled(true);
                    }
                } else if (inventory.getType() == InventoryType.CHEST || inventory.getType() == InventoryType.ENDER_CHEST
                        || inventory.getType() == InventoryType.DISPENSER || inventory.getType() == InventoryType.DROPPER
                        || inventory.getType() == InventoryType.HOPPER || inventory.getType() == InventoryType.ANVIL
                        || inventory.getType() == InventoryType.SHULKER_BOX || inventory.getType() == InventoryType.BARREL) {
                    if (!MissionManager.isMission()) {
                        e.setResult(Event.Result.DENY);
                        e.setCancelled(true);
                    }
                }
            } else if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_HUNTER, p) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_TUHO, p)) {
                e.setResult(Event.Result.DENY);
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent e) {
        Player p = (Player) e.getPlayer();
        Inventory inventory = e.getInventory();
        InventoryView inventoryView = e.getView();

        if (!Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_ADMIN, p)) {
            if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_PLAYER, p) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_SUCCESS, p) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_JAIL, p)) {
                if (inventory.getType() == InventoryType.CHEST || inventory.getType() == InventoryType.ENDER_CHEST
                        || inventory.getType() == InventoryType.DISPENSER || inventory.getType() == InventoryType.DROPPER
                        || inventory.getType() == InventoryType.HOPPER || inventory.getType() == InventoryType.ANVIL
                        || inventory.getType() == InventoryType.SHULKER_BOX || inventory.getType() == InventoryType.BARREL
                        || inventory.getType() == InventoryType.BREWING || inventory.getType() == InventoryType.FURNACE) {
                    if (inventoryView.getTitle().equals(MainInventory.title) || inventoryView.getTitle().equals(PlayerListInventory.title)
                            || inventoryView.getTitle().equals(MissionInventory.missionTitle) || inventoryView.getTitle().equals(MissionInventory.tutatuHintTitle)
                            || inventoryView.getTitle().equals(MissionInventory.endTitle))
                        return;

                    if (MissionManager.isMission() || isAllowOpen) return;
                    e.setCancelled(true);
                    p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "現在開くことが出来ません。");
                }
            } else if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_HUNTER, p) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_TUHO, p)) {
                if (inventory.getType() == InventoryType.CHEST || inventory.getType() == InventoryType.ENDER_CHEST
                        || inventory.getType() == InventoryType.DISPENSER || inventory.getType() == InventoryType.DROPPER
                        || inventory.getType() == InventoryType.HOPPER || inventory.getType() == InventoryType.ANVIL
                        || inventory.getType() == InventoryType.SHULKER_BOX) {
                    if (inventoryView.getTitle().equals(MainInventory.title) || inventoryView.getTitle().equals(PlayerListInventory.title)
                            || inventoryView.getTitle().equals(MissionInventory.missionTitle) || inventoryView.getTitle().equals(MissionInventory.tutatuHintTitle)
                            || inventoryView.getTitle().equals(MissionInventory.endTitle))
                        return;

                    e.setCancelled(true);
                    p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "現在開くことが出来ません。");
                }
            }
        }
    }
}

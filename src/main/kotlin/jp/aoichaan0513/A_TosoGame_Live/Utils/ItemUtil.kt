package jp.aoichaan0513.A_TosoGame_Live.Utils

import org.bukkit.ChatColor
import org.bukkit.inventory.ItemFlag

class ItemUtil {
    companion object {

        val itemFlags = arrayOf(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS)

        fun getItemName(str: String): String {
            return "${ChatColor.GREEN}${ChatColor.BOLD}${str.trim()}"
        }
    }
}
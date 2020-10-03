package jp.aoichaan0513.A_TosoGame_Live.Utils

import org.bukkit.ChatColor

fun String.color(): String {
    return ChatColor.translateAlternateColorCodes('&', this)
}
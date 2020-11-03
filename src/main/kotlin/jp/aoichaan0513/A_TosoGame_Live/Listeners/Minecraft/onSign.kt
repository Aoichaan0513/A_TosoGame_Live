package jp.aoichaan0513.A_TosoGame_Live.Listeners.Minecraft

import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI
import org.bukkit.ChatColor
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.SignChangeEvent

class onSign : Listener {

    @EventHandler
    fun onSignChange(e: SignChangeEvent) {
        if (e.getLine(0).equals("逃走中", true) && e.getLine(1).equals("自首", true)) {
            e.setLine(0, MainAPI.prefix)
            e.setLine(1, "${ChatColor.BOLD}自首")
            e.setLine(3, "${ChatColor.ITALIC}クリック")
        }
    }
}
package jp.aoichaan0513.A_TosoGame_Live.API.Manager

import net.md_5.bungee.api.ChatMessageType
import net.md_5.bungee.api.chat.BaseComponent
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.Bukkit
import org.bukkit.entity.Player

class ActionBarManager {
    companion object {

        fun sendActionBar(text: String) {
            for (player in Bukkit.getOnlinePlayers())
                sendActionBar(player, text)
        }

        fun sendActionBar(baseComponent: BaseComponent) {
            for (player in Bukkit.getOnlinePlayers())
                sendActionBar(player, baseComponent)
        }

        fun sendActionBar(p: Player, text: String) {
            sendActionBar(p, TextComponent(text))
        }

        fun sendActionBar(p: Player, baseComponent: BaseComponent) {
            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, baseComponent)
        }
    }
}
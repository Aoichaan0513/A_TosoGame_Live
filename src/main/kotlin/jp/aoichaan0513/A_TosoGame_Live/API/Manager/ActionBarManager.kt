package jp.aoichaan0513.A_TosoGame_Live.API.Manager

import jp.aoichaan0513.ActionBarBuilder.ActionBarBuilder
import net.md_5.bungee.api.ChatMessageType
import net.md_5.bungee.api.chat.BaseComponent
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import java.util.*

class ActionBarManager {
    companion object {

        val map = mutableMapOf<UUID, ActionBarBuilder>()

        val DEFAULT_SEPARATOR = "${ChatColor.RESET}${ChatColor.GRAY} / ${ChatColor.RESET}"

        operator fun get(p: Player): ActionBarBuilder? {
            return map[p.uniqueId]
        }

        fun getOrSet(p: Player, ActionBarBuilder: ActionBarBuilder = ActionBarBuilder(DEFAULT_SEPARATOR)): ActionBarBuilder {
            return map.getOrPut(p.uniqueId, { ActionBarBuilder })
        }

        fun set(p: Player, ActionBarBuilder: ActionBarBuilder): ActionBarBuilder {
            map[p.uniqueId] = ActionBarBuilder
            return ActionBarBuilder
        }

        fun remove(p: Player) {
            map.remove(p.uniqueId)
        }

        fun reset(p: Player): ActionBarBuilder {
            return set(p, ActionBarBuilder(DEFAULT_SEPARATOR))
        }

        fun resetAll() {
            for (player in Bukkit.getOnlinePlayers())
                reset(player)
        }

        fun send(ActionBarBuilder: ActionBarBuilder) {
            send(ActionBarBuilder.build())
        }

        fun send(p: Player, ActionBarBuilder: ActionBarBuilder) {
            send(p, ActionBarBuilder.build())
        }

        fun send(text: String) {
            for (player in Bukkit.getOnlinePlayers())
                send(player, text)
        }

        fun send(baseComponent: BaseComponent) {
            for (player in Bukkit.getOnlinePlayers())
                send(player, baseComponent)
        }

        fun send(p: Player, text: String) {
            send(p, TextComponent(text))
        }

        fun send(p: Player, baseComponent: BaseComponent) {
            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, baseComponent)
        }
    }
}
package jp.aoichaan0513.A_TosoGame_Live.Listeners.Minecraft

import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI
import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI.PrefixType
import jp.aoichaan0513.A_TosoGame_Live.API.TosoGameAPI
import jp.aoichaan0513.A_TosoGame_Live.Commands.Command.Script
import jp.aoichaan0513.A_TosoGame_Live.Main
import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.ComponentBuilder
import net.md_5.bungee.api.chat.HoverEvent
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.BlockCommandSender
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerCommandPreprocessEvent
import org.bukkit.event.server.ServerCommandEvent

class onCommand : Listener {

    @EventHandler
    fun onPlayerCommand(e: PlayerCommandPreprocessEvent) {
        val p = e.player
        val msg = e.message

        val cmdName = if (msg.split(" ")[0].split(":").size > 1) msg.split(" ")[0].split(":")[1] else msg.split(" ")[0].substring(1)

        val textComponent1 = TextComponent("${ChatColor.GRAY}> ${p.name}: ")
        val textComponent2 = TextComponent(msg)
        textComponent2.color = net.md_5.bungee.api.ChatColor.GRAY
        textComponent2.clickEvent = ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, e.message)
        textComponent2.hoverEvent = HoverEvent(HoverEvent.Action.SHOW_TEXT, ComponentBuilder("${ChatColor.GRAY}${ChatColor.BOLD}クリックでコマンドをチャット欄に入力します。").create())
        textComponent1.addExtra(textComponent2)

        Bukkit.getOnlinePlayers().filter { TosoGameAPI.isAdmin(it) }.forEach {
            it.spigot().sendMessage(textComponent1)
        }

        if (set.contains("/$cmdName")) {
            if (TosoGameAPI.isAdmin(p)) return
            e.isCancelled = true
            p.sendMessage("${MainAPI.getPrefix(PrefixType.ERROR)}実行できません。")
        } else {
            if (Script.hasFile(cmdName)) {
                e.isCancelled = true
                val worldConfig = Main.worldConfig
                if (worldConfig.gameConfig.script) {
                    p.performCommand("script ${cmdName.substring(1)}${e.message.substring(cmdName.length)}")
                } else {
                    p.sendMessage("${MainAPI.getPrefix(PrefixType.ERROR)}管理者によってスクリプト機能が無効にされています。")
                }
            }
        }
    }

    @EventHandler
    fun onServerCommand(e: ServerCommandEvent) {
        if (e.sender is BlockCommandSender) {
            /*
            Block b = ((BlockCommandSender) e.getSender()).getBlock();

            Bukkit.getConsoleSender().sendMessage(ChatColor.GRAY + "> Command Block: /" + e.getCommand() + "\n" +
                    ChatColor.GRAY + "X: " + b.getX() + ", Y: " + b.getY() + ", Z:" + b.getZ());
            */
        } else if (e.sender is ConsoleCommandSender) {
            Bukkit.getConsoleSender().sendMessage("${ChatColor.GRAY}> Console: /${e.command}")
        }
    }

    companion object {
        private val set = mutableSetOf<String>()

        fun addBlockCommand(cmdName: String) {
            set.add("/$cmdName")
        }

        fun removeBlockCommand(cmdName: String) {
            set.remove("/$cmdName")
        }
    }
}
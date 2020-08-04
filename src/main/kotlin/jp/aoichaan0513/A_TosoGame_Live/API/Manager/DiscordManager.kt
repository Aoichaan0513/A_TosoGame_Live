package jp.aoichaan0513.A_TosoGame_Live.API.Manager

import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI
import jp.aoichaan0513.A_TosoGame_Live.Main
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.ChatColor
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import java.util.*

class DiscordManager {
    companion object {

        // 実際に保持する用
        val integrationMap = mutableMapOf<UUID, Long>()

        // 連携用
        val hashMap = mutableMapOf<String, UUID>()

        // 発信処理
        fun outGoingCall(fromPlayer: Player, toPlayer: Player) {
            fromPlayer.sendMessage("${MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY)}${toPlayer.name}に発信しています…")

            object : BukkitRunnable() {
                var i = 5
                var c = 36

                override fun run() {
                    if (c < 0) {
                        if (i < 0) {
                            cancel()
                        } else {
                            c = 36
                            i--
                        }
                    } else {
                        if (c in 8..18 || c in 26..36)
                            toPlayer.playSound(toPlayer.location, Sound.BLOCK_NOTE_BLOCK_HARP, 1f, 1f)
                        c--
                    }
                }
            }.runTaskTimerAsynchronously(Main.pluginInstance, 0, 2)

            val textComponent1 = TextComponent("${MainAPI.getPrefix(MainAPI.PrefixType.WARNING)}${ChatColor.BOLD}${ChatColor.UNDERLINE}${fromPlayer.name}${ChatColor.RESET}${ChatColor.GOLD}から着信です。\n")
            val textComponent2 = TextComponent(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY))
            val textComponent3 = TextComponent("${ChatColor.GREEN}${ChatColor.BOLD}${ChatColor.UNDERLINE}応答")
            val textComponent4 = TextComponent("${ChatColor.RESET} ")
            val textComponent5 = TextComponent("${ChatColor.RED}${ChatColor.BOLD}${ChatColor.UNDERLINE}切断")

            textComponent1.addExtra(textComponent2)
            textComponent1.addExtra(textComponent3)
            textComponent1.addExtra(textComponent4)
            textComponent1.addExtra(textComponent5)

            toPlayer.spigot().sendMessage(textComponent1)
            return
        }
    }

    enum class CallState {
        READY,
        CALL,
        NONE
    }
}
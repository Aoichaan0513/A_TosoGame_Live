package jp.aoichaan0513.A_TosoGame_Live.Listeners.Minecraft

import jp.aoichaan0513.A_TosoGame_Live.API.Manager.Player.PlayerConfig
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.Player.PlayerManager
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerResourcePackStatusEvent

class onResourcePack : Listener {

    @EventHandler
    fun onResourcePack(e: PlayerResourcePackStatusEvent) {
        val p = e.player
        val status = e.status

        val playerConfig = PlayerManager.loadConfig(p)

        playerConfig.bookForegroundColor = when (status) {
            PlayerResourcePackStatusEvent.Status.ACCEPTED,
            PlayerResourcePackStatusEvent.Status.SUCCESSFULLY_LOADED -> PlayerConfig.BookForegroundColor.WHITE
            PlayerResourcePackStatusEvent.Status.DECLINED,
            PlayerResourcePackStatusEvent.Status.FAILED_DOWNLOAD -> PlayerConfig.BookForegroundColor.BLACK
        }
    }
}
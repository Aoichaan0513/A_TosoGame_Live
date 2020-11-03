package jp.aoichaan0513.A_TosoGame_Live.API.Listener

import org.bukkit.event.Cancellable
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

class MissionEndEvent(val id: Int) : Event(), Cancellable {
    companion object {

        @JvmStatic
        val handlerList = HandlerList()
    }

    private var cancelled = false

    // イベント管理系 (いじらないように！)
    override fun isCancelled(): Boolean {
        return cancelled
    }

    override fun setCancelled(cancel: Boolean) {
        cancelled = cancel
    }

    override fun getHandlers(): HandlerList {
        return handlerList
    }
}
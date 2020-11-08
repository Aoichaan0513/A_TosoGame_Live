package jp.aoichaan0513.A_TosoGame_Live.API.Manager.Player

import jp.aoichaan0513.A_TosoGame_Live.Main
import jp.aoichaan0513.A_TosoGame_Live.Utils.isAdminTeam
import jp.aoichaan0513.A_TosoGame_Live.Utils.isHunterGroup
import jp.aoichaan0513.A_TosoGame_Live.Utils.isJailTeam
import jp.aoichaan0513.A_TosoGame_Live.Utils.isPlayerGroup
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.*

class VisibilityManager {
    companion object {

        val liveHidePlayerSet = mutableSetOf<UUID>()
        val itemInvisiblePlayerSet = mutableSetOf<UUID>()
        val jailHidePlayerMap = mutableMapOf<UUID, Boolean>()


        fun hidePlayers(p: Player) {
            // 確保者が周りを非表示にした時
            val jailPlayer = jailHidePlayerMap[p.uniqueId]
            if (p.isJailTeam) {
                if (jailPlayer != null) {
                    if (jailPlayer) {
                        for (player in Bukkit.getOnlinePlayers())
                            p.hidePlayer(player)
                    } else {
                        for (player in Bukkit.getOnlinePlayers().filter { !it.isAdminTeam })
                            p.hidePlayer(player)
                    }
                } else {
                    _hidePlayers(p)
                }
            } else {
                _hidePlayers(p)
            }
        }

        private fun _hidePlayers(p: Player) {
            for (player in Bukkit.getOnlinePlayers()) {
                if (PlayerManager.loadConfig(player).visibility) {
                    // disappear コマンドで非表示にしていた時
                    p.hidePlayer(player)
                } else {
                    // 配信者が非表示設定にしていた時
                    if (isHide(player, VisibilityType.LIVE)) {
                        if (p.isPlayerGroup)
                            p.hidePlayer(player)
                        else
                            p.showPlayer(player)
                    } else {
                        // 逃走者が透明を使用した時
                        if (p.isJailTeam || p.isHunterGroup) {
                            if (isHide(player, VisibilityType.ITEM)) {
                                Main.tabListPlayerSet.add(player.uniqueId)
                                p.hidePlayer(player)
                                Main.tabListPlayerSet.remove(player.uniqueId)
                            } else {
                                p.showPlayer(player)
                            }
                        } else {
                            p.showPlayer(player)
                        }
                    }
                }
            }
        }


        fun isHide(p: Player, visibilityType: VisibilityType): Boolean {
            return getList(visibilityType).contains(p.uniqueId)
        }

        fun add(p: Player, visibilityType: VisibilityType) {
            getList(visibilityType).add(p.uniqueId)
        }

        fun remove(p: Player, visibilityType: VisibilityType) {
            getList(visibilityType).remove(p.uniqueId)
        }

        fun clear(visibilityType: VisibilityType) {
            getList(visibilityType).clear()
        }

        fun getList(visibilityType: VisibilityType): MutableSet<UUID> {
            return when (visibilityType) {
                VisibilityType.LIVE -> liveHidePlayerSet
                VisibilityType.ITEM -> itemInvisiblePlayerSet
            }
        }

        fun addJailHide(p: Player, isAdmin: Boolean = false) {
            jailHidePlayerMap[p.uniqueId] = isAdmin
        }

        fun removeJailHide(p: Player) {
            jailHidePlayerMap.remove(p.uniqueId)
        }
    }

    enum class VisibilityType {
        LIVE,
        ITEM
    }
}
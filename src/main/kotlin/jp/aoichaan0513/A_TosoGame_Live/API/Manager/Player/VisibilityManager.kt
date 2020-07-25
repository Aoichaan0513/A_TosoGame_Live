package jp.aoichaan0513.A_TosoGame_Live.API.Manager.Player

import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI
import jp.aoichaan0513.A_TosoGame_Live.API.TosoGameAPI
import jp.aoichaan0513.A_TosoGame_Live.Utils.isHunterGroup
import jp.aoichaan0513.A_TosoGame_Live.Utils.isJailTeam
import jp.aoichaan0513.A_TosoGame_Live.Utils.isPlayerGroup
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.*

class VisibilityManager {
    companion object {

        val adminHidePlayerSet = mutableSetOf<UUID>()
        val liveHidePlayerSet = mutableSetOf<UUID>()
        val itemInvisiblePlayerSet = mutableSetOf<UUID>()
        val jailHidePlayerMap = mutableMapOf<UUID, Boolean>()


        fun hidePlayers(p: Player) {
            // アイテム・コマンドで確保者が周りを非表示にした時
            val jailPlayer = jailHidePlayerMap[p.uniqueId]
            if (p.isJailTeam && jailPlayer != null) {
                if (jailPlayer) {
                    for (player in Bukkit.getOnlinePlayers())
                        p.hidePlayer(player)
                } else {
                    for (player in Bukkit.getOnlinePlayers().filter { !TosoGameAPI.isAdmin(it) })
                        p.hidePlayer(player)
                }
            } else {
                for (player in Bukkit.getOnlinePlayers()) {
                    if (adminHidePlayerSet.contains(player.uniqueId)) {
                        // disappear コマンドで非表示にしていた時
                        p.hidePlayer(player)
                    } else {
                        // 配信者が非表示設定にしていた時
                        if (liveHidePlayerSet.contains(player.uniqueId)) {
                            if (p.isPlayerGroup)
                                p.hidePlayer(player)
                            else
                                p.showPlayer(player)
                        } else {
                            p.showPlayer(player)
                        }
                    }
                }

                // 逃走者が透明を使用した時
                if (p.isJailTeam || p.isHunterGroup) {
                    for (player in MainAPI.getOnlinePlayers(itemInvisiblePlayerSet))
                        p.hidePlayer(player)
                } else {
                    for (player in MainAPI.getOnlinePlayers(itemInvisiblePlayerSet))
                        p.showPlayer(player)
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
                VisibilityType.ADMIN -> adminHidePlayerSet
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
        ADMIN,
        LIVE,
        ITEM
    }
}
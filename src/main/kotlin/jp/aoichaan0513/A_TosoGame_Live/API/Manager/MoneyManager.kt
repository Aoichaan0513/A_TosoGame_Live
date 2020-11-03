package jp.aoichaan0513.A_TosoGame_Live.API.Manager

import jp.aoichaan0513.A_TosoGame_Live.API.Manager.GameManager.GameState
import jp.aoichaan0513.A_TosoGame_Live.Main
import jp.aoichaan0513.A_TosoGame_Live.Utils.isPlayerGroup
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.*

class MoneyManager {
    companion object {

        val rewardMap = mutableMapOf<UUID, Long>()
        val rateMap = mutableMapOf<UUID, Int>()

        // 賞金関連
        fun hasReward(uuid: UUID): Boolean {
            return rewardMap.containsKey(uuid)
        }

        fun hasReward(p: Player): Boolean {
            return hasReward(p.uniqueId)
        }

        fun getReward(uuid: UUID): Long {
            return rewardMap[uuid] ?: 0
        }

        fun getReward(p: Player): Long {
            return getReward(p.uniqueId)
        }

        fun setReward(p: Player, l: Long) {
            rewardMap[p.uniqueId] = l
        }

        fun addReward() {
            if (!GameManager.isGame(GameState.GAME)) return
            for (p in Bukkit.getOnlinePlayers())
                addReward(p)
        }

        fun addReward(p: Player) {
            if (!GameManager.isGame(GameState.GAME)) return
            if (p.isPlayerGroup)
                addReward(p, getRate(p).toLong())
        }

        fun addReward(p: Player, l: Long) {
            if (!GameManager.isGame(GameState.GAME)) return
            if (p.isPlayerGroup)
                rewardMap[p.uniqueId] = if (rewardMap.containsKey(p.uniqueId)) rewardMap[p.uniqueId]!! + l else getRate(p).toLong()
        }

        fun removeReward(p: Player, l: Long) {
            if (!GameManager.isGame(GameState.GAME)) return
            if (p.isPlayerGroup)
                rewardMap[p.uniqueId] = if (rewardMap.containsKey(p.uniqueId) && l >= rewardMap[p.uniqueId]!!) rewardMap[p.uniqueId]!! - l else 0
        }

        fun resetReward() {
            if (GameManager.isGame(GameState.GAME)) return
            rewardMap.clear()
        }

        // レート関連
        fun hasRate(uuid: UUID): Boolean {
            return rateMap.containsKey(uuid)
        }

        fun hasRate(p: Player): Boolean {
            return hasRate(p.uniqueId)
        }

        fun getRate(uuid: UUID): Int {
            if (!rateMap.containsKey(uuid)) setRate(uuid)
            return rateMap[uuid]!!
        }

        fun getRate(p: Player): Int {
            return getRate(p.uniqueId)
        }

        fun setRate(uuid: UUID, rate: Int = Main.worldConfig.getDifficultyConfig(uuid).rate) {
            rateMap[uuid] = rate
        }

        fun setRate(p: Player, rate: Int = Main.worldConfig.getDifficultyConfig(p).rate) {
            setRate(p.uniqueId, rate)
        }

        fun addRate(p: Player, rate: Int) {
            rateMap[p.uniqueId] = getRate(p) + rate
        }

        fun removeRate(p: Player, rate: Int) {
            val i = getRate(p)
            rateMap[p.uniqueId] = if (i >= rate) i - rate else 0
        }

        fun resetRate() {
            if (GameManager.isGame(GameState.GAME)) return
            rateMap.clear()
            for (player in Bukkit.getOnlinePlayers()) setRate(player)
        }

        fun formatMoney(l: Long): String {
            return "%,d".format(l)
        }

        @Deprecated("")
        fun formatMoney(i: Int): String {
            return "%,d".format(i)
        }
    }
}
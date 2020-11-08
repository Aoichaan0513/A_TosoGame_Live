package jp.aoichaan0513.A_TosoGame_Live.Runnable

import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.GameManager
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.GameManager.GameState
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.World.WorldManager.GameType
import jp.aoichaan0513.A_TosoGame_Live.API.Scoreboard.Teams
import jp.aoichaan0513.A_TosoGame_Live.API.Scoreboard.Teams.OnlineTeam
import jp.aoichaan0513.A_TosoGame_Live.API.TosoGameAPI
import jp.aoichaan0513.A_TosoGame_Live.Main
import jp.aoichaan0513.A_TosoGame_Live.Utils.isJailTeam
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.GameMode
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitRunnable
import java.util.*

class RespawnRunnable : BukkitRunnable() {
    companion object {

        // 自動復活秒数
        val autoTimeMap = mutableMapOf<UUID, Int>()

        // 復活クールタイム秒数
        val coolTimeMap = mutableMapOf<UUID, Int>()

        // 復活回数
        val countMap = mutableMapOf<UUID, Int>()

        // 自動復活
        fun setAutoTime(p: Player) {
            if (!isAllowRespawn(p) || !TosoGameAPI.isRespawn) return

            val worldConfig = Main.worldConfig
            val difficultyConfig = worldConfig.getDifficultyConfig(p)

            if (difficultyConfig.respawnAutoTime < 0) return

            setAutoTime(p, difficultyConfig.respawnAutoTime)
        }

        fun setAutoTime(p: Player, time: Int) {
            autoTimeMap[p.uniqueId] = time
        }

        fun getAutoTime(p: Player): Int {
            val worldConfig = Main.worldConfig
            val difficultyConfig = worldConfig.getDifficultyConfig(p)
            return autoTimeMap[p.uniqueId] ?: difficultyConfig.respawnAutoTime
        }

        fun isAutoTime(p: Player): Boolean {
            val autoTime = autoTimeMap[p.uniqueId]
            return autoTime != null && autoTime > 0
        }


        // 復活クールタイム
        fun addCoolTime(p: Player) {
            if (!isAllowRespawn(p) || !TosoGameAPI.isRespawn) return
            setCoolTime(p, setCount(p, if (countMap.containsKey(p.uniqueId)) countMap[p.uniqueId]!! + 1 else 1))
        }

        fun setCoolTime(p: Player) {
            setCoolTime(p, getCount(p))
        }

        fun setCoolTime(p: Player, count: Int) {
            val worldConfig = Main.worldConfig
            val difficultyConfig = worldConfig.getDifficultyConfig(p)

            setCount(p, count)
            coolTimeMap[p.uniqueId] = difficultyConfig.respawnCoolTime * count
        }

        fun getCoolTime(p: Player): Int {
            val worldConfig = Main.worldConfig
            val difficultyConfig = worldConfig.getDifficultyConfig(p)
            return coolTimeMap[p.uniqueId] ?: difficultyConfig.respawnCoolTime
        }

        fun isCoolTime(p: Player): Boolean {
            val coolTime = coolTimeMap[p.uniqueId]
            return coolTime != null && coolTime > 0
        }


        // 復活回数
        fun setCount(p: Player, count: Int): Int {
            countMap[p.uniqueId] = count
            return countMap[p.uniqueId]!!
        }

        fun getCount(p: Player): Int {
            return countMap.getOrDefault(p.uniqueId, 1)
        }

        fun isAllowRespawn(p: Player): Boolean {
            val worldConfig = Main.worldConfig
            val difficultyConfig = worldConfig.getDifficultyConfig(p)
            return getCount(p) <= difficultyConfig.respawnDenyCount
        }

        fun getGameType(p: Player): GameType {
            return if (countMap.containsKey(p.uniqueId)) GameType.RESPAWN else GameType.START
        }

        fun reset() {
            autoTimeMap.clear()
            coolTimeMap.clear()
            countMap.clear()
        }
    }

    override fun run() {
        if (!GameManager.isGame(GameState.GAME) || !TosoGameAPI.isRespawn) return

        for (player in Bukkit.getOnlinePlayers().filter { it.isJailTeam && countMap.containsKey(it.uniqueId) }) {
            val autoTime = autoTimeMap[player.uniqueId]
            val coolTime = coolTimeMap[player.uniqueId]

            if (autoTime != null) {
                if (autoTime < 1) {
                    autoTimeMap.remove(player.uniqueId)

                    if (!isAllowRespawn(player)) return
                    val worldConfig = Main.worldConfig
                    val difficultyConfig = worldConfig.getDifficultyConfig(player)

                    Teams.joinTeam(OnlineTeam.TOSO_PLAYER, player)
                    player.gameMode = GameMode.ADVENTURE

                    TosoGameAPI.setItem(GameType.RESPAWN, player)
                    TosoGameAPI.removeOp(player)

                    TosoGameAPI.teleport(player, worldConfig.respawnLocationConfig.locations.values)

                    player.addPotionEffect(PotionEffect(PotionEffectType.NIGHT_VISION, 200000, 1, false, false))
                    player.addPotionEffect(PotionEffect(PotionEffectType.INVISIBILITY, 20 * 15, 1, false, false))
                    player.addPotionEffect(PotionEffect(PotionEffectType.SPEED, 20 * 15, 1, false, false))

                    TosoGameAPI.hidePlayer(player)

                    player.sendMessage("""
                        ${MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY)}あなたを逃走者に追加しました。
                        ${MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY)}あなたはあと${difficultyConfig.respawnDenyCount - getCount(player)}回復活できます。
                    """.trimIndent())
                    Bukkit.broadcastMessage("${MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY)}${player.name}が復活しました。(${ChatColor.UNDERLINE}残り${Teams.getOnlineCount(OnlineTeam.TOSO_PLAYER) + Teams.getOnlineCount(OnlineTeam.TOSO_SUCCESS)}人${ChatColor.RESET}${ChatColor.GRAY})")
                    continue
                } else {
                    autoTimeMap[player.uniqueId] = autoTime - 1
                }
            }

            if (coolTime != null) {
                if (coolTime < 1)
                    coolTimeMap.remove(player.uniqueId)
                else
                    coolTimeMap[player.uniqueId] = coolTime - 1
            }
        }
    }
}
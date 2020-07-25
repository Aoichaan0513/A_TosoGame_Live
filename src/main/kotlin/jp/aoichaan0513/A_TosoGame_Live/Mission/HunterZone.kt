package jp.aoichaan0513.A_TosoGame_Live.Mission

import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI
import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI.PrefixType
import jp.aoichaan0513.A_TosoGame_Live.API.Scoreboard.Teams
import jp.aoichaan0513.A_TosoGame_Live.API.TosoGameAPI
import jp.aoichaan0513.A_TosoGame_Live.Main
import jp.aoichaan0513.A_TosoGame_Live.Utils.DateTime.TimeFormat
import org.apache.commons.lang.RandomStringUtils
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask
import java.util.*

class HunterZone {
    companion object {

        private var hunterZoneRunnable: HunterZoneRunnable? = null
        private var timer: BukkitTask? = null

        var isStart = false
            private set

        // コードを入力したプレイヤー
        private val codeSet = mutableSetOf<UUID>()

        // ハンターゾーンに入ったプレイヤー
        private val joinedPlayerSet = mutableSetOf<UUID>()
        private val leavedPlayerSet = mutableSetOf<UUID>()

        var code = ""
            private set

        fun startMission() {
            if (timer != null) return

            code = RandomStringUtils.randomNumeric(4)

            hunterZoneRunnable = HunterZoneRunnable(getInitialMissionTime().toInt() / 20)
            timer = hunterZoneRunnable?.runTaskTimer(Main.pluginInstance, 0L, 20L)

            isStart = true
        }

        fun endMission() {
            if (timer == null) return

            timer?.cancel()

            timer = null
            hunterZoneRunnable = null

            val worldConfig = Main.worldConfig
            MainAPI.getOnlinePlayers(joinedPlayerSet).forEach {
                it.addPotionEffect(PotionEffect(PotionEffectType.INVISIBILITY, 20 * 15, 1, false, false))
                TosoGameAPI.teleport(it, worldConfig.respawnLocationConfig.locations.values)

                TosoGameAPI.hidePlayer(it)
            }
            joinedPlayerSet.clear()
        }

        fun resetMission() {
            if (timer != null) return

            code = ""
            codeSet.clear()
            joinedPlayerSet.clear()
            leavedPlayerSet.clear()
            isStart = false
        }


        fun addCodeSet(p: Player) {
            codeSet.add(p.uniqueId)
        }

        fun containsCodeSet(p: Player): Boolean {
            return codeSet.contains(p.uniqueId)
        }

        fun addJoinedSet(p: Player) {
            joinedPlayerSet.add(p.uniqueId)
        }

        fun removeJoinedSet(p: Player) {
            joinedPlayerSet.remove(p.uniqueId)
        }

        fun removeJoinedSet(p: Player, b: Boolean) {
            removeJoinedSet(p)
            if (b) leavedPlayerSet.add(p.uniqueId)
        }

        fun containsJoinedSet(p: Player): Boolean {
            return joinedPlayerSet.contains(p.uniqueId)
        }

        val joinedSetCount: Int
            get() = joinedPlayerSet.size

        fun removeLeavedSet(p: Player) {
            leavedPlayerSet.remove(p.uniqueId)
        }

        fun containsLeavedSet(p: Player): Boolean {
            return leavedPlayerSet.contains(p.uniqueId)
        }


        fun getMissionTime(): Double {
            return hunterZoneRunnable?.missionTime?.toDouble() ?: getInitialMissionTime()
        }

        fun getInitialMissionTime(): Double {
            return ((20 * 60) * 7).toDouble()
        }

        private class HunterZoneRunnable(private val initialMissionTime: Int) : BukkitRunnable() {

            var missionTime: Int
                private set

            init {
                missionTime = initialMissionTime
            }

            override fun run() {
                missionTime--

                if (missionTime == 0) {
                    Bukkit.broadcastMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}ハンターゾーンミッションが終了しました。")
                    for (player in Bukkit.getOnlinePlayers().filter { Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_PLAYER, it) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_SUCCESS, it) }) {
                        if (!codeSet.contains(player.uniqueId)) {
                            player.sendMessage("""
                                ${MainAPI.getPrefix(PrefixType.WARNING)}あなたはコードを入力していないため発光しました。
                                ${MainAPI.getPrefix(PrefixType.SECONDARY)}解除するには"${ChatColor.UNDERLINE}/code 入手したコード${ChatColor.RESET}${ChatColor.GRAY}"と入力して装置を解除してください。
                            """.trimIndent())
                            player.addPotionEffect(PotionEffect(PotionEffectType.GLOWING, 200000, 1, false, false))
                        }
                    }
                    MissionManager.endMission(MissionManager.MissionState.HUNTER_ZONE)
                } else if (missionTime == 60) {
                    Bukkit.broadcastMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}ハンターゾーンミッション終了まで残り${TimeFormat.formatMin(missionTime)}分")
                }
            }
        }
    }
}
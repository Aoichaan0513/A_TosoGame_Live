package jp.aoichaan0513.A_TosoGame_Live.Mission

import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI
import jp.aoichaan0513.A_TosoGame_Live.API.Scoreboard.Teams
import jp.aoichaan0513.A_TosoGame_Live.Main
import jp.aoichaan0513.A_TosoGame_Live.Utils.DateTime.TimeFormat
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask
import java.util.*

class TimedDevice {
    companion object {

        private var timedDeviceRunnable: TimedDeviceRunnable? = null
        private var timer: BukkitTask? = null

        var isStart = false
            private set

        private val hashMap = mutableMapOf<Int, List<UUID>>()
        private val clearedNumberSet = mutableSetOf<Int>()

        fun startMission() {
            if (timer != null) return

            val playerList = mutableListOf<UUID>()
            Bukkit.getOnlinePlayers().filter { Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_PLAYER, it) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_SUCCESS, it) }
                    .forEach { playerList.add(it.uniqueId) }

            val divideList = MainAPI.divide(playerList, 2)
            if (divideList.size > 30) return

            val lastResult = mutableSetOf<Int>()

            for (list in divideList) {
                val result = (1..30).filter { !lastResult.contains(it) }.shuffled().first()
                hashMap[result] = list
                lastResult.add(result)
            }

            timedDeviceRunnable = TimedDeviceRunnable(getInitialMissionTime().toInt() / 20)
            timer = timedDeviceRunnable?.runTaskTimer(Main.pluginInstance, 0L, 20L)

            isStart = true
        }

        fun endMission() {
            if (timer == null) return

            timer?.cancel()

            timer = null
            timedDeviceRunnable = null
        }

        fun resetMission() {
            if (timer != null) return

            isStart = false
        }


        fun addClearedNumberSet(i: Int) {
            clearedNumberSet.add(i)
        }

        fun addClearedNumberSet(p: Player) {
            addClearedNumberSet(hashMap.entries.firstOrNull { it.value.contains(p.uniqueId) }?.key ?: return)
        }


        fun getMissionTime(): Double {
            return timedDeviceRunnable?.missionTime?.toDouble() ?: getInitialMissionTime()
        }

        fun getInitialMissionTime(): Double {
            return ((20 * 60) * 5).toDouble()
        }

        private class TimedDeviceRunnable(private val initialMissionTime: Int) : BukkitRunnable() {
            var missionTime: Int
                private set

            init {
                missionTime = initialMissionTime
            }

            override fun run() {
                missionTime--

                if (missionTime == 0) {
                    Bukkit.broadcastMessage("${MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY)}時限装置解除ミッションが終了しました。")
                    for (player in Bukkit.getOnlinePlayers().filter { Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_PLAYER, it) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_SUCCESS, it) }) {
                    }
                    MissionManager.endMission(MissionManager.MissionState.TIMED_DEVICE)
                    endMission()
                } else if (missionTime == 60) {
                    Bukkit.broadcastMessage("${MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY)}時限装置解除ミッション終了まで残り${TimeFormat.formatMin(missionTime)}分")
                }
            }
        }
    }
}
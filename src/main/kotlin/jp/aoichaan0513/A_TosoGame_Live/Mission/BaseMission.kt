package jp.aoichaan0513.A_TosoGame_Live.Mission

import jp.aoichaan0513.A_TosoGame_Live.Main
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask

class BaseMission {
    companion object {

        private var timer: BukkitTask? = null
        var isStart = false
            private set

        fun startMission() {
            if (timer != null) return
            timer = BaseMissionRunnable((20 * 60) * 0).runTaskTimer(Main.pluginInstance, 0L, 20L)
            isStart = true
        }

        fun endMission() {
            if (timer == null) return
            timer!!.cancel()
            timer = null
        }

        fun resetMission() {
            if (timer != null) return
            isStart = false
        }

        fun endAndResetMission() {
            HunterZone.endMission()
            HunterZone.resetMission()
        }


        private class BaseMissionRunnable(private val initialMissionTime: Int) : BukkitRunnable() {
            private var missionTime: Int

            init {
                missionTime = initialMissionTime
            }

            override fun run() {
                missionTime--
            }
        }
    }
}
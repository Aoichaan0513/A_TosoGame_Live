package jp.aoichaan0513.A_TosoGame_Live.Mission

import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI
import jp.aoichaan0513.A_TosoGame_Live.API.TosoGameAPI
import jp.aoichaan0513.A_TosoGame_Live.Main
import jp.aoichaan0513.A_TosoGame_Live.Utils.DateTime.TimeFormat
import jp.aoichaan0513.A_TosoGame_Live.Utils.isHunterTeam
import jp.aoichaan0513.A_TosoGame_Live.Utils.isPlayerGroup
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

        var internalMissionState = MissionManager.InternalMissionState.NONE
            private set

        var isStart = false
            private set

        // ハンターのリスト
        private val hunterSet = mutableSetOf<UUID>()

        // コードを入力したプレイヤー
        private val codeSet = mutableSetOf<UUID>()

        // ハンターゾーンに入ったプレイヤー
        private val joinedPlayerSet = mutableSetOf<UUID>()
        private val leavedPlayerSet = mutableSetOf<UUID>()

        var code = ""
            private set

        fun startMission(hunterCount: Int = 1) {
            if (timer != null || hunterSet.isEmpty()) return

            val worldConfig = Main.worldConfig
            val loc = worldConfig.hunterLocationConfig.getLocation()

            val hunters = mutableSetOf<UUID>()

            val players = MainAPI.getOnlinePlayers(hunterSet).filter { it.isHunterTeam }.shuffled()
            for (i in 0 until hunterCount) {
                val player = players[i]
                hunters.add(player.uniqueId)

                Bukkit.getScheduler().runTaskLaterAsynchronously(Main.pluginInstance, Runnable { TosoGameAPI.teleport(player, loc) }, 20 * 3)

                player.sendMessage("""
                    ${MainAPI.getPrefix(ChatColor.YELLOW)}あなたを${ChatColor.GOLD}${ChatColor.UNDERLINE}ハンターゾーンのハンター${ChatColor.RESET}${ChatColor.YELLOW}に追加しました。
                    ${MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY)}3秒後にハンターゾーンにテレポートします。 
                """.trimIndent())
            }

            hunterSet.clear()
            hunters.forEach { hunterSet.add(it) }

            code = RandomStringUtils.randomNumeric(4)

            hunterZoneRunnable = HunterZoneRunnable(getInitialMissionTime().toInt() / 20)
            timer = hunterZoneRunnable?.runTaskTimer(Main.pluginInstance, 0L, 20L)

            internalMissionState = MissionManager.InternalMissionState.START
            isStart = true
        }

        fun endMission() {
            if (timer == null) return

            timer?.cancel()

            timer = null
            hunterZoneRunnable = null

            val worldConfig = Main.worldConfig
            val loc = worldConfig.hunterLocationConfig.getLocation(1)

            MainAPI.getOnlinePlayers(hunterSet).filter { it.isHunterTeam }.forEach {
                Bukkit.getScheduler().runTaskLaterAsynchronously(Main.pluginInstance, Runnable { TosoGameAPI.teleport(it, loc) }, 20 * 3)

                it.sendMessage("""
                    ${MainAPI.getPrefix(ChatColor.YELLOW)}あなたを${ChatColor.GOLD}${ChatColor.UNDERLINE}ハンターゾーンのハンター${ChatColor.RESET}${ChatColor.YELLOW}から削除しました。
                    ${MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY)}3秒後にハンターボックスにテレポートします。 
                """.trimIndent())
            }

            hunterSet.clear()

            MainAPI.getOnlinePlayers(joinedPlayerSet).forEach {
                it.addPotionEffect(PotionEffect(PotionEffectType.INVISIBILITY, 20 * 15, 1, false, false))
                TosoGameAPI.teleport(it, worldConfig.respawnLocationConfig.locations.values)

                TosoGameAPI.hidePlayer(it)
            }

            internalMissionState = MissionManager.InternalMissionState.END
        }

        fun resetMission() {
            if (timer != null) return

            hunterSet.clear()
            code = ""
            codeSet.clear()
            joinedPlayerSet.clear()
            leavedPlayerSet.clear()

            internalMissionState = MissionManager.InternalMissionState.NONE
            isStart = false
        }

        fun endAndResetMission() {
            endMission()
            resetMission()
        }


        val hunterSetCount: Int
            get() = hunterSet.size

        fun addHunterSet(p: Player) {
            hunterSet.add(p.uniqueId)
        }

        fun removeHunterSet(p: Player) {
            hunterSet.remove(p.uniqueId)
        }

        fun containsHunterSet(p: Player): Boolean {
            return hunterSet.contains(p.uniqueId)
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

        val joinedSetCount
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
                    Bukkit.broadcastMessage("${MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY)}ハンターゾーンミッションが終了しました。")
                    for (player in Bukkit.getOnlinePlayers().filter { it.isPlayerGroup && !codeSet.contains(it.uniqueId) }) {
                        if (!codeSet.contains(player.uniqueId)) {
                            player.sendMessage("""
                                ${MainAPI.getPrefix(MainAPI.PrefixType.WARNING)}あなたはコードを入力していないため発光しました。
                                ${MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY)}解除するには"${ChatColor.UNDERLINE}/code 入手したコード${ChatColor.RESET}${ChatColor.GRAY}"と入力して装置を解除してください。
                            """.trimIndent())
                            player.addPotionEffect(PotionEffect(PotionEffectType.GLOWING, 200000, 1, false, false))
                        }
                    }
                    MissionManager.endMission(MissionManager.MissionState.HUNTER_ZONE)
                    endMission()
                } else if (missionTime == 60) {
                    Bukkit.broadcastMessage("${MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY)}ハンターゾーンミッション終了まで残り${TimeFormat.formatMin(missionTime)}分")
                }
            }
        }
    }
}
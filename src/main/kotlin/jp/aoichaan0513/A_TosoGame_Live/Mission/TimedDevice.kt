package jp.aoichaan0513.A_TosoGame_Live.Mission

import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.GameManager
import jp.aoichaan0513.A_TosoGame_Live.Main
import jp.aoichaan0513.A_TosoGame_Live.Utils.DateTime.TimeFormat
import jp.aoichaan0513.A_TosoGame_Live.Utils.ItemUtil
import jp.aoichaan0513.A_TosoGame_Live.Utils.isPlayerGroup
import org.bukkit.*
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask
import java.util.*

class TimedDevice {
    companion object {

        private var timedDeviceRunnable: TimedDeviceRunnable? = null
        private var timer: BukkitTask? = null

        private var taskRunnable: BukkitTask? = null

        var internalMissionState = MissionManager.InternalMissionState.NONE
            private set

        var isStart = false
            private set

        private val hashMap = mutableMapOf<Int, Set<UUID>>()
        private val clearedNumberSet = mutableSetOf<Int>()
        private val failedNumberSet = mutableSetOf<Int>()

        fun startMission() {
            if (timer != null) return

            val playerList = mutableListOf<UUID>()
            Bukkit.getOnlinePlayers().filter { it.isPlayerGroup }.forEach { playerList.add(it.uniqueId) }

            val divideList = MainAPI.divide(playerList, 2)
            if (divideList.size > 30) return

            val lastResult = mutableSetOf<Int>()

            for (list in divideList) {
                val result = (1..30).filter { !lastResult.contains(it) }.shuffled().first()
                hashMap[result] = list.toSet()
                lastResult.add(result)

                val itemStack = ItemStack(Material.PAPER, 1)
                val itemMeta = itemStack.itemMeta!!
                itemMeta.addItemFlags(*ItemUtil.itemFlags)
                itemMeta.setCustomModelData("10$result".toInt())
                itemMeta.setDisplayName(ItemUtil.getItemName("【$result】 カードキー"))
                itemMeta.lore = listOf(
                        "${ChatColor.YELLOW}${result}番のカードキーだ。",
                        "${ChatColor.YELLOW}同じ番号のカードキーを所持しているプレイヤーを見つけ、",
                        "${ChatColor.YELLOW}そのプレイヤーにカードキーを持ってタッチすることで認証することができる。"
                )
                itemStack.itemMeta = itemMeta

                MainAPI.getOnlinePlayers(list).forEach { it.inventory.addItem(itemStack) }
            }

            timedDeviceRunnable = TimedDeviceRunnable(getInitialMissionTime().toInt() / 20)
            timer = timedDeviceRunnable?.runTaskTimer(Main.pluginInstance, 0L, 20L)

            internalMissionState = MissionManager.InternalMissionState.START
            isStart = true
        }

        fun endMission() {
            if (timer == null) return

            timer?.cancel()

            timer = null
            timedDeviceRunnable = null

            internalMissionState = MissionManager.InternalMissionState.END
        }

        fun resetMission() {
            if (timer != null) return

            taskRunnable?.cancel()
            taskRunnable = null

            hashMap.clear()
            clearedNumberSet.clear()
            failedNumberSet.clear()

            internalMissionState = MissionManager.InternalMissionState.NONE
            isStart = false
        }


        fun addClearedNumberSet(i: Int) {
            clearedNumberSet.add(i)
        }

        fun addClearedNumberSet(p: Player) {
            addClearedNumberSet(hashMap.entries.firstOrNull { it.value.contains(p.uniqueId) }?.key ?: return)
        }


        fun addFailedNumberSet(i: Int) {
            failedNumberSet.add(i)
        }

        fun addFailedNumberSet(p: Player) {
            addFailedNumberSet(hashMap.entries.firstOrNull { it.value.contains(p.uniqueId) }?.key ?: return)
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

            var temporaryTaskRunnable: BukkitTask

            init {
                missionTime = initialMissionTime

                temporaryTaskRunnable = object : BukkitRunnable() {

                    override fun run() {
                        if (!GameManager.isGame(GameManager.GameState.GAME)) cancel()
                        for (set in hashMap.filter { !clearedNumberSet.contains(it.key) && failedNumberSet.contains(it.key) }.values)
                            for (player in getPlayers(set))
                                player.world.playSound(player.location, Sound.BLOCK_ANVIL_LAND, SoundCategory.RECORDS, 1f, 1f)
                    }
                }.runTaskTimer(Main.pluginInstance, 0, 20)
            }

            override fun run() {
                missionTime--

                if (missionTime == 0) {
                    Bukkit.broadcastMessage("${MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY)}時限装置解除ミッションが終了しました。")
                    for (set in hashMap.filter { !clearedNumberSet.contains(it.key) }.values) {
                        for (player in getPlayers(set)) {
                            player.sendMessage("""
                                ${MainAPI.getPrefix(MainAPI.PrefixType.WARNING)}あなたは時限装置を解除していないため位置情報がハンターに通知されました。
                                ${MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY)}時限装置を解除するには同じ番号のカードキーを利用して装置を解除してください。
                            """.trimIndent())
                        }
                    }

                    temporaryTaskRunnable.cancel()
                    taskRunnable = object : BukkitRunnable() {

                        var c = 60

                        override fun run() {
                            if (c < 1) cancel()
                            if (!GameManager.isGame(GameManager.GameState.GAME)) cancel()
                            for (set in hashMap.filter { !clearedNumberSet.contains(it.key) }.values)
                                for (player in getPlayers(set))
                                    player.world.playSound(player.location, Sound.BLOCK_ANVIL_LAND, SoundCategory.RECORDS, 1f, 1f)
                            c--
                        }
                    }.runTaskTimer(Main.pluginInstance, 0, 20)

                    MissionManager.endMission(MissionManager.MissionState.TIMED_DEVICE)
                    endMission()
                } else if (missionTime == 60) {
                    Bukkit.broadcastMessage("${MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY)}時限装置解除ミッション終了まで残り${TimeFormat.formatMin(missionTime)}分")
                }
            }

            private fun getPlayers(collection: Collection<UUID>): Set<Player> {
                return MainAPI.getOnlinePlayers(collection).filter { it.isPlayerGroup && !it.hasPotionEffect(PotionEffectType.INVISIBILITY) }.toSet()
            }
        }
    }
}
package jp.aoichaan0513.A_TosoGame_Live.API.Manager

import jp.aoichaan0513.A_TosoGame_Live.API.Manager.GameManager.GameState
import jp.aoichaan0513.A_TosoGame_Live.Main
import jp.aoichaan0513.A_TosoGame_Live.Mission.MissionManager
import jp.aoichaan0513.A_TosoGame_Live.OPGame.Dice
import jp.aoichaan0513.A_TosoGame_Live.OPGame.OPGameManager
import jp.aoichaan0513.A_TosoGame_Live.Utils.DateTimeUtil
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.boss.BarColor
import org.bukkit.boss.BarStyle
import org.bukkit.boss.BossBar
import org.bukkit.entity.Player
import java.util.*

class BossBarManager {
    companion object {

        private val hashMap = mutableMapOf<UUID, BossBar>()

        fun addBar() {
            for (p in Bukkit.getOnlinePlayers())
                addBar(p)
        }

        fun addBar(p: Player): BossBar {
            val bossBar = Bukkit.createBossBar("", BarColor.WHITE, BarStyle.SOLID)
            hashMap[p.uniqueId] = bossBar
            return bossBar
        }

        fun removeBar(p: Player) {
            if (hashMap.containsKey(p.uniqueId))
                hashMap.remove(p.uniqueId)
        }

        fun getBar(p: Player): BossBar {
            return hashMap[p.uniqueId] ?: addBar(p)
        }

        fun resetBar() {
            for (bossBar in hashMap.values)
                bossBar.removeAll()
            hashMap.clear()
        }

        fun showBar(time: Int = 0, maxTime: Int = 1) {
            for (p in Bukkit.getOnlinePlayers())
                showBar(p, time, maxTime)
        }

        fun showBar(p: Player, time: Int = 0, maxTime: Int = 1) {
            val bossBar = getBar(p)

            val debugMessage = if (Main.isDebug) "${ChatColor.RESET}${ChatColor.GRAY} / ${ChatColor.DARK_RED}${ChatColor.BOLD}${ChatColor.UNDERLINE}デバッグモード" else ""

            when (GameManager.gameState) {
                GameState.READY -> {
                    when (OPGameManager.opGameState) {
                        OPGameManager.OPGameState.DICE -> {
                            val count = Dice.count

                            val worldConfig = Main.worldConfig
                            val maxCount = worldConfig.opGameConfig.diceCount

                            bossBar.setTitle("${ChatColor.YELLOW}${ChatColor.BOLD}オープニングゲーム${ChatColor.RESET}${ChatColor.GRAY} / ${ChatColor.YELLOW}サイコロ${ChatColor.GRAY}: ${ChatColor.GOLD}${ChatColor.BOLD}${ChatColor.UNDERLINE}$count${ChatColor.RESET}${ChatColor.YELLOW} / ${ChatColor.YELLOW}${ChatColor.GOLD}${ChatColor.BOLD}${ChatColor.UNDERLINE}$maxCount$debugMessage")
                            bossBar.progress = (count.toDouble() / maxCount).coerceAtMost(1.0)
                            bossBar.color = BarColor.YELLOW
                            bossBar.addPlayer(p)
                        }
                        else -> {
                            bossBar.setTitle("${ChatColor.YELLOW}ゲーム開始まで残り${ChatColor.GRAY}: ${ChatColor.GOLD}${ChatColor.BOLD}${ChatColor.UNDERLINE}${DateTimeUtil.formatTimestamp(time).toJapan}$debugMessage")
                            bossBar.progress = (time.toDouble() / maxTime).coerceAtMost(1.0)
                            bossBar.color = BarColor.YELLOW
                            bossBar.addPlayer(p)
                        }
                    }
                }
                GameState.GAME -> {
                    bossBar.setTitle("${ChatColor.BOLD}${ChatColor.RED}ゲーム終了まで残り${ChatColor.GRAY}: ${ChatColor.DARK_RED}${ChatColor.BOLD}${ChatColor.UNDERLINE}${DateTimeUtil.formatTimestamp(time).toJapan}${if (MissionManager.isMissions) "${ChatColor.RESET}${ChatColor.GRAY} / ${ChatColor.YELLOW}${ChatColor.BOLD}ミッション中" else ""}$debugMessage")
                    bossBar.progress = (time.toDouble() / maxTime).coerceAtMost(1.0)
                    bossBar.color = BarColor.RED
                    bossBar.addPlayer(p)
                }
                GameState.END -> {
                    bossBar.setTitle("${ChatColor.GRAY}${ChatColor.BOLD}ゲーム終了$debugMessage")
                    bossBar.progress = 1.0
                    bossBar.color = BarColor.WHITE
                    bossBar.addPlayer(p)
                }
                else -> {
                    bossBar.setTitle("${ChatColor.GREEN}${ChatColor.BOLD}ゲーム準備中…$debugMessage")
                    bossBar.progress = 1.0
                    bossBar.color = BarColor.GREEN
                    bossBar.addPlayer(p)
                }
            }
        }
    }
}
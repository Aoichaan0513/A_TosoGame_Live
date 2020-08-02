package jp.aoichaan0513.A_TosoGame_Live.API.Scoreboard

import jp.aoichaan0513.A_TosoGame_Live.API.Manager.GameManager
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.GameManager.GameState
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.MoneyManager
import jp.aoichaan0513.A_TosoGame_Live.API.Scoreboard.Teams.OnlineTeam
import jp.aoichaan0513.A_TosoGame_Live.API.TosoGameAPI
import jp.aoichaan0513.A_TosoGame_Live.Main
import jp.aoichaan0513.A_TosoGame_Live.OPGame.OPGameManager
import jp.aoichaan0513.A_TosoGame_Live.Runnable.GameRunnable
import jp.aoichaan0513.A_TosoGame_Live.Utils.DateTime.TimeFormat
import jp.aoichaan0513.A_TosoGame_Live.Utils.isAdminTeam
import jp.aoichaan0513.A_TosoGame_Live.Utils.isJailTeam
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.scoreboard.DisplaySlot
import org.bukkit.scoreboard.Scoreboard
import java.util.*

class Scoreboard {
    companion object {

        val boardMap = mutableMapOf<UUID, Scoreboard>()

        fun setBoard(p: Player): Scoreboard {
            val scoreboard = boardMap[p.uniqueId] ?: Teams.setScoreboard(Bukkit.getScoreboardManager()!!.newScoreboard)

            scoreboard.getObjective(TosoGameAPI.Objective.SIDEBAR.objectName)?.unregister()

            val objective = scoreboard.registerNewObjective(TosoGameAPI.Objective.SIDEBAR.objectName, "dummy", "${ChatColor.RED}${ChatColor.BOLD}Run${ChatColor.RESET}${ChatColor.GRAY} for ${ChatColor.DARK_RED}${ChatColor.BOLD}Money")
            objective.displaySlot = DisplaySlot.SIDEBAR
            objective.getScore(SidebarScore.SCORE_15.text).score = SidebarScore.SCORE_15.score
            objective.getScore(SidebarScore.SCORE_14.text).score = SidebarScore.SCORE_14.score
            objective.getScore(SidebarScore.SCORE_13.text).score = SidebarScore.SCORE_13.score
            objective.getScore(SidebarScore.SCORE_12.text).score = SidebarScore.SCORE_12.score
            objective.getScore(SidebarScore.SCORE_11.text).score = SidebarScore.SCORE_11.score
            objective.getScore(SidebarScore.SCORE_10.text).score = SidebarScore.SCORE_10.score
            objective.getScore(SidebarScore.SCORE_9.text).score = SidebarScore.SCORE_9.score
            objective.getScore(SidebarScore.SCORE_8.text).score = SidebarScore.SCORE_8.score
            objective.getScore(SidebarScore.SCORE_7.text).score = SidebarScore.SCORE_7.score
            objective.getScore(SidebarScore.SCORE_6.text).score = SidebarScore.SCORE_6.score
            objective.getScore(SidebarScore.SCORE_5.text).score = SidebarScore.SCORE_5.score
            objective.getScore(SidebarScore.SCORE_4.text).score = SidebarScore.SCORE_4.score
            objective.getScore(SidebarScore.SCORE_3.text).score = SidebarScore.SCORE_3.score
            objective.getScore(SidebarScore.SCORE_2.text).score = SidebarScore.SCORE_2.score
            objective.getScore(SidebarScore.SCORE_1.text).score = SidebarScore.SCORE_1.score

            Teams.setTeamOption(scoreboard)
            setTeams(p, scoreboard)
            p.scoreboard = scoreboard
            boardMap[p.uniqueId] = scoreboard
            return scoreboard
        }

        fun removeBoard(p: Player) {
            if (boardMap.containsKey(p.uniqueId))
                boardMap.remove(p.uniqueId)
            p.scoreboard = Teams.setScoreboard(Bukkit.getScoreboardManager()!!.newScoreboard)
            Teams.setTeamOption(p)
            return
        }

        private fun setTeams(p: Player, scoreboard: Scoreboard) {
            val worldConfig = Main.worldConfig

            val (prefix, suffix) = getGameState()
            val status = scoreboard.getTeam(TosoGameAPI.Objective.SIDEBAR_STATUS.objectName)
                    ?: scoreboard.registerNewTeam(TosoGameAPI.Objective.SIDEBAR_STATUS.objectName)
            status.addEntry(SidebarScore.SCORE_15.text)
            status.prefix = prefix
            status.suffix = suffix

            val team = scoreboard.getTeam(TosoGameAPI.Objective.SIDEBAR_TEAM.objectName)
                    ?: scoreboard.registerNewTeam(TosoGameAPI.Objective.SIDEBAR_TEAM.objectName)
            team.addEntry(SidebarScore.SCORE_13.text)
            team.suffix = Teams.getJoinedTeam(p).displayName

            val difficulty = scoreboard.getTeam(TosoGameAPI.Objective.SIDEBAR_DIFFICULTY.objectName)
                    ?: scoreboard.registerNewTeam(TosoGameAPI.Objective.SIDEBAR_DIFFICULTY.objectName)
            difficulty.addEntry(SidebarScore.SCORE_11.text)
            difficulty.suffix = worldConfig.getDifficultyConfig(p).difficulty.displayName

            val reward = scoreboard.getTeam(TosoGameAPI.Objective.SIDEBAR_REWARD.objectName)
                    ?: scoreboard.registerNewTeam(TosoGameAPI.Objective.SIDEBAR_REWARD.objectName)
            reward.addEntry(SidebarScore.SCORE_9.text)
            reward.suffix = getReward(p)

            val rate = scoreboard.getTeam(TosoGameAPI.Objective.SIDEBAR_RATE.objectName)
                    ?: scoreboard.registerNewTeam(TosoGameAPI.Objective.SIDEBAR_RATE.objectName)
            rate.addEntry(SidebarScore.SCORE_8.text)
            rate.suffix = "(${MoneyManager.getRate(p)}円/秒)"

            val teamPlayer = scoreboard.getTeam(TosoGameAPI.Objective.SIDEBAR_TEAM_PLAYER.objectName)
                    ?: scoreboard.registerNewTeam(TosoGameAPI.Objective.SIDEBAR_TEAM_PLAYER.objectName)
            teamPlayer.addEntry(SidebarScore.SCORE_5.text)
            teamPlayer.suffix = "${ChatColor.WHITE}${Teams.getOnlineCount(OnlineTeam.TOSO_PLAYER)}${ChatColor.GRAY}人"

            val teamHunter = scoreboard.getTeam(TosoGameAPI.Objective.SIDEBAR_TEAM_HUNTER.objectName)
                    ?: scoreboard.registerNewTeam(TosoGameAPI.Objective.SIDEBAR_TEAM_HUNTER.objectName)
            teamHunter.addEntry(SidebarScore.SCORE_4.text)
            teamHunter.suffix = "${ChatColor.RED}${Teams.getOnlineCount(OnlineTeam.TOSO_HUNTER)}${ChatColor.GRAY}人"

            val teamJail = scoreboard.getTeam(TosoGameAPI.Objective.SIDEBAR_TEAM_JAIL.objectName)
                    ?: scoreboard.registerNewTeam(TosoGameAPI.Objective.SIDEBAR_TEAM_JAIL.objectName)
            teamJail.addEntry(SidebarScore.SCORE_3.text)
            teamJail.suffix = "${ChatColor.BLACK}${Teams.getOnlineCount(OnlineTeam.TOSO_JAIL)}${ChatColor.GRAY}人"

            val teamSuccess = scoreboard.getTeam(TosoGameAPI.Objective.SIDEBAR_TEAM_SUCCESS.objectName)
                    ?: scoreboard.registerNewTeam(TosoGameAPI.Objective.SIDEBAR_TEAM_SUCCESS.objectName)
            teamSuccess.addEntry(SidebarScore.SCORE_2.text)
            teamSuccess.suffix = "${ChatColor.BLUE}${Teams.getOnlineCount(OnlineTeam.TOSO_SUCCESS)}${ChatColor.GRAY}人"

            val teamTuho = scoreboard.getTeam(TosoGameAPI.Objective.SIDEBAR_TEAM_TUHO.objectName)
                    ?: scoreboard.registerNewTeam(TosoGameAPI.Objective.SIDEBAR_TEAM_TUHO.objectName)
            teamTuho.addEntry(SidebarScore.SCORE_1.text)
            teamTuho.suffix = "${ChatColor.GREEN}${Teams.getOnlineCount(OnlineTeam.TOSO_TUHO)}${ChatColor.GRAY}人"
        }

        fun getBoard(p: Player): Scoreboard {
            return boardMap[p.uniqueId] ?: setBoard(p)
        }

        fun updateScoreboard() {
            val worldConfig = Main.worldConfig

            for (player in Bukkit.getOnlinePlayers()) {
                val scoreboard = boardMap[player.uniqueId] ?: setBoard(player)

                val (prefix, suffix) = getGameState()
                val status = scoreboard.getTeam(TosoGameAPI.Objective.SIDEBAR_STATUS.objectName)!!
                status.prefix = prefix
                status.suffix = suffix

                scoreboard.getTeam(TosoGameAPI.Objective.SIDEBAR_TEAM.objectName)!!.suffix = Teams.getJoinedTeam(player).displayName
                scoreboard.getTeam(TosoGameAPI.Objective.SIDEBAR_DIFFICULTY.objectName)!!.suffix = worldConfig.getDifficultyConfig(player).difficulty.displayName
                scoreboard.getTeam(TosoGameAPI.Objective.SIDEBAR_REWARD.objectName)!!.suffix = getReward(player)
                scoreboard.getTeam(TosoGameAPI.Objective.SIDEBAR_RATE.objectName)!!.suffix = "(${MoneyManager.getRate(player)}円/秒)"
                scoreboard.getTeam(TosoGameAPI.Objective.SIDEBAR_TEAM_PLAYER.objectName)!!.suffix = "${ChatColor.WHITE}${Teams.getOnlineCount(OnlineTeam.TOSO_PLAYER)}${ChatColor.GRAY}人"
                scoreboard.getTeam(TosoGameAPI.Objective.SIDEBAR_TEAM_HUNTER.objectName)!!.suffix = "${ChatColor.RED}${Teams.getOnlineCount(OnlineTeam.TOSO_HUNTER)}${ChatColor.GRAY}人"
                scoreboard.getTeam(TosoGameAPI.Objective.SIDEBAR_TEAM_JAIL.objectName)!!.suffix = "${ChatColor.BLACK}${Teams.getOnlineCount(OnlineTeam.TOSO_JAIL)}${ChatColor.GRAY}人"
                scoreboard.getTeam(TosoGameAPI.Objective.SIDEBAR_TEAM_SUCCESS.objectName)!!.suffix = "${ChatColor.BLUE}${Teams.getOnlineCount(OnlineTeam.TOSO_SUCCESS)}${ChatColor.GRAY}人"
                scoreboard.getTeam(TosoGameAPI.Objective.SIDEBAR_TEAM_TUHO.objectName)!!.suffix = "${ChatColor.GREEN}${Teams.getOnlineCount(OnlineTeam.TOSO_TUHO)}${ChatColor.GRAY}人"
            }
        }

        private fun getGameState(): Pair<String, String> {
            return when (GameManager.gameState) {
                GameState.READY -> when (OPGameManager.opGameState) {
                    OPGameManager.OPGameState.DICE -> "${ChatColor.YELLOW}OPG${ChatColor.RESET}${ChatColor.GRAY}: " to "${ChatColor.GOLD}${ChatColor.BOLD}${ChatColor.UNDERLINE}サイコロ"
                    else -> "${ChatColor.YELLOW}残り${ChatColor.RESET}${ChatColor.GRAY}: " to "${ChatColor.GOLD}${ChatColor.BOLD}${ChatColor.UNDERLINE}${TimeFormat.formatJapan(GameRunnable.countDown)}"
                }
                GameState.GAME -> "${ChatColor.RED}残り${ChatColor.RESET}${ChatColor.GRAY}: " to "${ChatColor.DARK_RED}${ChatColor.BOLD}${ChatColor.UNDERLINE}${TimeFormat.formatJapan(GameRunnable.gameTime)}"
                GameState.END -> "" to "${ChatColor.GRAY}${ChatColor.BOLD}   ゲーム終了  ${ChatColor.RESET}"
                else -> "" to "${ChatColor.GREEN}${ChatColor.BOLD}  ゲーム準備中… ${ChatColor.RESET}"
            }
        }

        private fun getReward(p: Player): String {
            return if (p.isAdminTeam || p.isJailTeam) "${ChatColor.GRAY}${ChatColor.STRIKETHROUGH}${MoneyManager.formatMoney(MoneyManager.getReward(p))}円"
            else "${ChatColor.GOLD}${MoneyManager.formatMoney(MoneyManager.getReward(p))}${ChatColor.GRAY}円"
        }

        private val hidePlayerSet = mutableSetOf<UUID>()

        fun getHidePlayerSet(): Set<UUID> {
            return hidePlayerSet
        }

        fun isHidePlayer(p: Player): Boolean {
            return hidePlayerSet.contains(p.uniqueId)
        }

        fun addHidePlayer(p: Player) {
            hidePlayerSet.add(p.uniqueId)
        }

        fun removeHidePlayer(p: Player) {
            hidePlayerSet.remove(p.uniqueId)
        }
    }

    enum class SidebarScore(val score: Int, var text: String) {
        SCORE_15(15, "${ChatColor.BLACK}${ChatColor.RESET}"),
        SCORE_14(14, "  "),
        SCORE_13(13, "${ChatColor.AQUA}${ChatColor.UNDERLINE}チーム${ChatColor.RESET}${ChatColor.GRAY}: ${ChatColor.RESET}"),
        SCORE_12(12, "   "),
        SCORE_11(11, "${ChatColor.AQUA}${ChatColor.UNDERLINE}難易度${ChatColor.RESET}${ChatColor.GRAY}: ${ChatColor.RESET}"),
        SCORE_10(10, "    "),
        SCORE_9(9, "${ChatColor.AQUA}${ChatColor.UNDERLINE}現在の賞金${ChatColor.RESET}${ChatColor.GRAY}: ${ChatColor.RESET}"),
        SCORE_8(8, "${ChatColor.GRAY}  ${ChatColor.GRAY}"),
        SCORE_7(7, "     "),
        SCORE_6(6, "${ChatColor.AQUA}${ChatColor.UNDERLINE}参加人数${ChatColor.RESET}${ChatColor.GRAY}: "),
        SCORE_5(5, "${ChatColor.WHITE}  逃走者${ChatColor.GRAY}: ${ChatColor.RESET}"),
        SCORE_4(4, "${ChatColor.RED}  ハンター${ChatColor.GRAY}: ${ChatColor.RESET}"),
        SCORE_3(3, "${ChatColor.BLACK}  牢獄${ChatColor.GRAY}: ${ChatColor.RESET}"),
        SCORE_2(2, "${ChatColor.BLUE}  生存者${ChatColor.GRAY}: ${ChatColor.RESET}"),
        SCORE_1(1, "${ChatColor.GREEN}  通報部隊${ChatColor.GRAY}: ${ChatColor.RESET}");

    }
}
package jp.aoichaan0513.A_TosoGame_Live.OPGame

import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.BossBarManager
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.GameManager
import jp.aoichaan0513.A_TosoGame_Live.API.Scoreboard.Teams
import jp.aoichaan0513.A_TosoGame_Live.API.TosoGameAPI
import jp.aoichaan0513.A_TosoGame_Live.Main
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.*

class OPGameManager {
    companion object {

        var opGameState = OPGameState.NONE
            private set

        var player: Player? = null

        var isRunned = false

        var runnedSet = mutableSetOf<UUID>()
            private set

        // オープニングゲームを実行するプレイヤー
        val runPlayer: Player
            get() {
                isRunned = false

                val players = Bukkit.getOnlinePlayers()
                        .filter { Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_PLAYER, it) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_SUCCESS, it) }
                        .filterNot { runnedSet.contains(it.uniqueId) }.toMutableList()

                for (i in 0..2)
                    players.shuffle()

                val player = players[0]
                runnedSet.add(player.uniqueId)

                this.player = player
                return player
            }

        // 実行できる参加者が存在するか
        val hasNext: Boolean
            get() {
                return Bukkit.getOnlinePlayers()
                        .filter { Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_PLAYER, it) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_SUCCESS, it) }
                        .filterNot { runnedSet.contains(it.uniqueId) }.isNotEmpty()
            }

        fun startOPGame(sender: CommandSender, state: OPGameState) {
            if (state == OPGameState.NONE || !GameManager.isGame(GameManager.GameState.NONE) || opGameState != OPGameState.NONE) return

            val worldConfig = Main.worldConfig

            GameManager.gameState = GameManager.GameState.READY
            opGameState = state

            BossBarManager.showBar()

            for (p in Bukkit.getOnlinePlayers())
                if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_PLAYER, p))
                    TosoGameAPI.teleport(p, worldConfig.opGameLocationConfig.gOPLocations.values)

            sender.sendMessage("${MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY)}オープニングゲームを開始しました。")
        }

        fun endOPGame(sender: CommandSender) {
            if (opGameState == OPGameState.NONE) return

            opGameState = OPGameState.NONE
            player = null

            BossBarManager.showBar()

            sender.sendMessage("${MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY)}オープニングゲームを終了しました。")
        }

        fun resetOPGame() {
            runnedSet.clear()
            opGameState = OPGameState.NONE
            player = null
        }
    }

    enum class OPGameState {
        DICE,
        LEVER,
        NONE
    }
}
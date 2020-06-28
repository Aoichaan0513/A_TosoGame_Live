package jp.aoichaan0513.A_TosoGame_Live.API.Manager

import jp.aoichaan0513.A_TosoGame_Live.Main
import jp.aoichaan0513.A_TosoGame_Live.Mission.MissionManager
import jp.aoichaan0513.A_TosoGame_Live.OPGame.Dice
import jp.aoichaan0513.A_TosoGame_Live.OPGame.OPGameManager
import jp.aoichaan0513.A_TosoGame_Live.Runnable.GameRunnable
import org.bukkit.Bukkit
import org.bukkit.scheduler.BukkitTask

class GameManager {
    companion object {

        var gameState = GameState.NONE
        private var timer: BukkitTask? = null

        fun startGame(countDown: Int, gameTime: Int) {
            if (timer != null) return
            timer = GameRunnable(countDown, gameTime).runTaskTimer(Main.pluginInstance, 0L, 20L)
            gameState = GameState.READY
        }

        fun endGame() {
            if (timer == null) return
            timer!!.cancel()
            timer = null
            gameState = GameState.END

            MissionManager.endMissions()
            Dice.end()
            OPGameManager.endOPGame(Bukkit.getConsoleSender())
        }

        fun isGame(): Boolean {
            return gameState == GameState.READY || gameState == GameState.GAME
        }

        fun isGame(game: GameState): Boolean {
            return gameState == game
        }
    }

    enum class GameState {
        READY,
        GAME,
        END,
        NONE
    }
}
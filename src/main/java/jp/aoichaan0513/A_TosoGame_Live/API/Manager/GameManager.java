package jp.aoichaan0513.A_TosoGame_Live.API.Manager;

import jp.aoichaan0513.A_TosoGame_Live.Main;
import jp.aoichaan0513.A_TosoGame_Live.Runnable.GameRunnable;
import org.bukkit.scheduler.BukkitTask;

public class GameManager {

    private static GameState game = GameState.NONE;

    private static BukkitTask timer = null;

    public static void startGame(int countDown, int gameTime) {
        if (timer != null) return;
        timer = new GameRunnable(countDown, gameTime).runTaskTimer(Main.getInstance(), 0L, 20L);
        GameManager.setGameState(GameManager.GameState.READY);
    }

    public static void endGame() {
        if (timer == null) return;
        timer.cancel();
        timer = null;
        GameManager.setGameState(GameManager.GameState.END);
        MissionManager.endMission();
    }

    public static boolean isGame(GameState game) {
        return GameManager.game == game;
    }

    public static boolean isGame() {
        return game == GameState.READY || game == GameState.GAME;
    }

    public static void setGameState(GameState game) {
        GameManager.game = game;
    }

    public static GameState getGameState() {
        return game;
    }

    public enum GameState {
        READY,
        GAME,
        END,
        NONE
    }
}

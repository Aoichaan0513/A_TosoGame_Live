package jp.aoichaan0513.A_TosoGame_Live.Timer;

import jp.aoichaan0513.A_TosoGame_Live.API.Manager.GameManager;
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.MissionManager;
import jp.aoichaan0513.A_TosoGame_Live.Main;
import jp.aoichaan0513.A_TosoGame_Live.Runnable.GameRunnable;
import org.bukkit.scheduler.BukkitTask;

public class Timer {

    private static GameRunnable runner = null;
    private static BukkitTask timer = null;

    public static void startGame(int setting_countdown, int setting_gametime) {
        if (timer != null) return;
        runner = new GameRunnable(setting_countdown, setting_gametime);
        timer = runner.runTaskTimer(Main.getInstance(), 0L, 20L);
        GameManager.setGameState(GameManager.GameState.READY);
    }

    public static void endGame() {
        if (timer == null) return;
        timer.cancel();
        timer = null;
        runner = null;
        GameManager.setGameState(GameManager.GameState.END);
        MissionManager.endMission();
    }
}

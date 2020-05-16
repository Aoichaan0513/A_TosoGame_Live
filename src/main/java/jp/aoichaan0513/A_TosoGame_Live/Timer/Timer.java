package jp.aoichaan0513.A_TosoGame_Live.Timer;

import jp.aoichaan0513.A_TosoGame_Live.API.Manager.GameManager;
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.MissionManager;
import jp.aoichaan0513.A_TosoGame_Live.Main;
import org.bukkit.scheduler.BukkitTask;

public class Timer {

    private static TimerRunnable runner = null;
    private static BukkitTask timer = null;

    public static void start(int setting_countdown, int setting_gametime) {
        if (timer == null) {
            runner = new TimerRunnable(setting_countdown, setting_gametime);
            timer = runner.runTaskTimer(Main.getInstance(), 0L, 20L);
            GameManager.setGameState(GameManager.GameState.READY);
            return;
        }
        return;
    }

    public static void end() {
        if (timer != null) {
            timer.cancel();
            timer = null;
            runner = null;
            GameManager.setGameState(GameManager.GameState.END);
            MissionManager.endMission();
            return;
        }
        return;
    }
}

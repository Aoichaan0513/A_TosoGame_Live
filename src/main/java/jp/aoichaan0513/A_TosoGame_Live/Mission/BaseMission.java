package jp.aoichaan0513.A_TosoGame_Live.Mission;

import jp.aoichaan0513.A_TosoGame_Live.Main;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class BaseMission {

    public MissionTimer runner = null;
    private static BukkitTask timer = null;

    public boolean isStarted = false;

    public void start() {
        if (timer == null) {
            runner = new MissionTimer(0);
            timer = runner.runTaskTimer(Main.getInstance(), 0L, 20L);
            return;
        }
        return;
    }

    public void end() {
        if (timer != null) {
            timer.cancel();
            timer = null;
            runner = null;
            return;
        }
        return;
    }

    public boolean isStarted() {
        return isStarted;
    }

    public void setStarted(boolean started) {
        isStarted = started;
    }

    public static class MissionTimer extends BukkitRunnable {

        private static int initialTime;
        private static int time;

        public MissionTimer(int time) {
            this.initialTime = time;
            this.time = time;
        }

        @Override
        public void run() {
            time--;
        }
    }
}

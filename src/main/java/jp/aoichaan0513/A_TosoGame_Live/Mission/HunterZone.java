package jp.aoichaan0513.A_TosoGame_Live.Mission;

import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI;
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.MissionManager;
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.World.WorldConfig;
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.World.WorldManager;
import jp.aoichaan0513.A_TosoGame_Live.API.Scoreboard.Teams;
import jp.aoichaan0513.A_TosoGame_Live.API.TosoGameAPI;
import jp.aoichaan0513.A_TosoGame_Live.Main;
import jp.aoichaan0513.A_TosoGame_Live.Utils.DateTime.TimeFormat;
import org.apache.commons.lang.RandomStringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;

public class HunterZone {

    private static ZoneTimer runner = null;
    private static BukkitTask timer = null;

    public static ArrayList<Player> codeList = new ArrayList<>();
    public static String code = "";

    public static void start() {
        if (timer == null) {
            runner = new ZoneTimer(420);
            timer = runner.runTaskTimer(Main.getInstance(), 0L, 20L);
            code = RandomStringUtils.randomNumeric(4);
            return;
        }
        return;
    }

    public static void end() {
        if (timer != null) {
            timer.cancel();
            timer = null;
            runner = null;
            code = "";
            codeList.clear();
            return;
        }
        return;
    }

    private static class ZoneTimer extends BukkitRunnable {

        private static int missionTime;

        public ZoneTimer(int missionTime) {
            this.missionTime = missionTime;
        }

        @Override
        public void run() {
            missionTime--;
            MissionManager.getBossBar().setTitle(ChatColor.YELLOW + "" + ChatColor.BOLD + "ハンターゾーンミッション終了まで: " + ChatColor.RESET + TimeFormat.formatJapan(missionTime));
            // MissionManager.getBossBar().setProgress(missionTime / 420);
            MissionManager.getBossBar().setColor(BarColor.BLUE);
            if (missionTime == 0) {
                Bukkit.broadcastMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "ハンターゾーンミッションが終了しました。");
                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_PLAYER, p) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_SUCCESS, p)) {
                        if (!codeList.contains(p)) {
                            p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "あなたを牢獄に追加しました。\n" +
                                    MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "あなたは確保されました。");

                            WorldConfig worldConfig = Main.getWorldConfig();
                            TosoGameAPI.teleport(p, worldConfig.getJailLocationConfig().getLocations().values());

                            Teams.joinTeam(Teams.OnlineTeam.TOSO_JAIL, p);
                            TosoGameAPI.setItem(WorldManager.GameType.RESPAWN, p);

                            Main.opGamePlayerSet.remove(p.getUniqueId());

                            Bukkit.broadcastMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + p.getName() + "が確保されました。(" + ChatColor.UNDERLINE + "残り" + (Teams.getOnlineCount(Teams.OnlineTeam.TOSO_PLAYER) + Teams.getOnlineCount(Teams.OnlineTeam.TOSO_SUCCESS)) + "人" + ChatColor.RESET + ChatColor.GRAY + ")");
                        }
                    }
                }
                MissionManager.endMission();
                HunterZone.end();
            } else if (missionTime == 60) {
                Bukkit.broadcastMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "ハンターゾーンミッション終了まで残り" + TimeFormat.formatMin(missionTime) + "分");
            }
        }
    }
}

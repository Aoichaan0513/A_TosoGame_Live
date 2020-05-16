package jp.aoichaan0513.A_TosoGame_Live.API.Scoreboard;

import jp.aoichaan0513.A_TosoGame_Live.API.Manager.GameManager;
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.RateManager;
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.World.WorldConfig;
import jp.aoichaan0513.A_TosoGame_Live.API.Timer.TimerFormat;
import jp.aoichaan0513.A_TosoGame_Live.API.TosoGameAPI;
import jp.aoichaan0513.A_TosoGame_Live.Main;
import jp.aoichaan0513.A_TosoGame_Live.Timer.TimerRunnable;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ScoreBoard {

    private static HashMap<UUID, Scoreboard> hashMap = new HashMap<>();

    public static Scoreboard setBoard(Player p) {
        Scoreboard scoreboard = hashMap.containsKey(p.getUniqueId()) ? hashMap.get(p.getUniqueId()) : Teams.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());

        if (scoreboard.getObjective(TosoGameAPI.Objective.SIDEBAR.getName()) != null)
            scoreboard.getObjective(TosoGameAPI.Objective.SIDEBAR.getName()).unregister();

        Objective objective = scoreboard.registerNewObjective(TosoGameAPI.Objective.SIDEBAR.getName(), "dummy", "" + ChatColor.RED + ChatColor.BOLD + "Run" + ChatColor.RESET + ChatColor.GRAY + " for " + ChatColor.DARK_RED + ChatColor.BOLD + "Money");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        objective.getScore(SidebarScore.SCORE_15.getText()).setScore(SidebarScore.SCORE_15.getScore());
        objective.getScore(SidebarScore.SCORE_14.getText()).setScore(SidebarScore.SCORE_14.getScore());
        objective.getScore(SidebarScore.SCORE_13.getText()).setScore(SidebarScore.SCORE_13.getScore());
        objective.getScore(SidebarScore.SCORE_12.getText()).setScore(SidebarScore.SCORE_12.getScore());
        objective.getScore(SidebarScore.SCORE_11.getText()).setScore(SidebarScore.SCORE_11.getScore());
        objective.getScore(SidebarScore.SCORE_10.getText()).setScore(SidebarScore.SCORE_10.getScore());
        objective.getScore(SidebarScore.SCORE_9.getText()).setScore(SidebarScore.SCORE_9.getScore());
        objective.getScore(SidebarScore.SCORE_8.getText()).setScore(SidebarScore.SCORE_8.getScore());
        objective.getScore(SidebarScore.SCORE_7.getText()).setScore(SidebarScore.SCORE_7.getScore());
        objective.getScore(SidebarScore.SCORE_6.getText()).setScore(SidebarScore.SCORE_6.getScore());
        objective.getScore(SidebarScore.SCORE_5.getText()).setScore(SidebarScore.SCORE_5.getScore());
        objective.getScore(SidebarScore.SCORE_4.getText()).setScore(SidebarScore.SCORE_4.getScore());
        objective.getScore(SidebarScore.SCORE_3.getText()).setScore(SidebarScore.SCORE_3.getScore());
        objective.getScore(SidebarScore.SCORE_2.getText()).setScore(SidebarScore.SCORE_2.getScore());
        objective.getScore(SidebarScore.SCORE_1.getText()).setScore(SidebarScore.SCORE_1.getScore());

        Teams.setTeamOption(scoreboard);
        setTeams(p, scoreboard);
        p.setScoreboard(scoreboard);

        hashMap.put(p.getUniqueId(), scoreboard);

        return scoreboard;
    }

    public static void removeBoard(Player p) {
        if (hashMap.containsKey(p.getUniqueId()))
            hashMap.remove(p.getUniqueId());

        p.setScoreboard(Teams.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard()));
        Teams.setTeamOption(p);
        return;
    }

    private static void setTeams(Player p, Scoreboard scoreboard) {
        WorldConfig worldConfig = Main.getWorldConfig();

        Team status = scoreboard.getTeam(TosoGameAPI.Objective.SIDEBAR_STATUS.getName()) != null ? scoreboard.getTeam(TosoGameAPI.Objective.SIDEBAR_STATUS.getName()) : scoreboard.registerNewTeam(TosoGameAPI.Objective.SIDEBAR_STATUS.getName());
        status.addEntry(SidebarScore.SCORE_15.getText());
        switch (GameManager.getGameState()) {
            case READY:
                status.setPrefix(ChatColor.YELLOW + "残り" + ChatColor.RESET + ChatColor.GRAY + ": ");
                status.setSuffix("" + ChatColor.GOLD + ChatColor.BOLD + ChatColor.UNDERLINE + TimerFormat.formatJapan(TimerRunnable.getCountDown()));
                break;
            case GAME:
                status.setPrefix(ChatColor.RED + "残り" + ChatColor.RESET + ChatColor.GRAY + ": ");
                status.setSuffix("" + ChatColor.DARK_RED + ChatColor.BOLD + ChatColor.UNDERLINE + TimerFormat.formatJapan(TimerRunnable.getGameTime()));
                break;
            case END:
                status.setPrefix("");
                status.setSuffix("  " + ChatColor.GRAY + ChatColor.BOLD + "  ゲーム終了");
                break;
            default:
                status.setPrefix("");
                status.setSuffix("" + ChatColor.GREEN + ChatColor.BOLD + "  ゲーム準備中…");
                break;
        }

        Team team = scoreboard.getTeam(TosoGameAPI.Objective.SIDEBAR_TEAM.getName()) != null ? scoreboard.getTeam(TosoGameAPI.Objective.SIDEBAR_TEAM.getName()) : scoreboard.registerNewTeam(TosoGameAPI.Objective.SIDEBAR_TEAM.getName());
        team.addEntry(SidebarScore.SCORE_13.getText());
        team.setSuffix(Teams.getJoinedTeam(p).getDisplayName());

        Team difficulty = scoreboard.getTeam(TosoGameAPI.Objective.SIDEBAR_DIFFICULTY.getName()) != null ? scoreboard.getTeam(TosoGameAPI.Objective.SIDEBAR_DIFFICULTY.getName()) : scoreboard.registerNewTeam(TosoGameAPI.Objective.SIDEBAR_DIFFICULTY.getName());
        difficulty.addEntry(SidebarScore.SCORE_11.getText());
        difficulty.setSuffix(worldConfig.getDifficultyConfig(p).getDifficulty().getDisplayName());


        Team reward = scoreboard.getTeam(TosoGameAPI.Objective.SIDEBAR_REWARD.getName()) != null ? scoreboard.getTeam(TosoGameAPI.Objective.SIDEBAR_REWARD.getName()) : scoreboard.registerNewTeam(TosoGameAPI.Objective.SIDEBAR_REWARD.getName());
        reward.addEntry(SidebarScore.SCORE_9.getText());
        reward.setSuffix("" + ChatColor.GOLD + RateManager.getMoney(p) + ChatColor.YELLOW + "円");

        Team rate = scoreboard.getTeam(TosoGameAPI.Objective.SIDEBAR_RATE.getName()) != null ? scoreboard.getTeam(TosoGameAPI.Objective.SIDEBAR_RATE.getName()) : scoreboard.registerNewTeam(TosoGameAPI.Objective.SIDEBAR_RATE.getName());
        rate.addEntry(SidebarScore.SCORE_8.getText());
        rate.setSuffix(ChatColor.GRAY + "  (" + RateManager.getRate(p) + "円/秒)");


        Team teamPlayer = scoreboard.getTeam(TosoGameAPI.Objective.SIDEBAR_TEAM_PLAYER.getName()) != null ? scoreboard.getTeam(TosoGameAPI.Objective.SIDEBAR_TEAM_PLAYER.getName()) : scoreboard.registerNewTeam(TosoGameAPI.Objective.SIDEBAR_TEAM_PLAYER.getName());
        teamPlayer.addEntry(SidebarScore.SCORE_5.getText());
        teamPlayer.setSuffix("" + ChatColor.WHITE + Teams.getOnlineCount(Teams.OnlineTeam.TOSO_PLAYER) + ChatColor.GRAY + "人");

        Team teamHunter = scoreboard.getTeam(TosoGameAPI.Objective.SIDEBAR_TEAM_HUNTER.getName()) != null ? scoreboard.getTeam(TosoGameAPI.Objective.SIDEBAR_TEAM_HUNTER.getName()) : scoreboard.registerNewTeam(TosoGameAPI.Objective.SIDEBAR_TEAM_HUNTER.getName());
        teamHunter.addEntry(SidebarScore.SCORE_4.getText());
        teamHunter.setSuffix("" + ChatColor.RED + Teams.getOnlineCount(Teams.OnlineTeam.TOSO_HUNTER) + ChatColor.GRAY + "人");

        Team teamJail = scoreboard.getTeam(TosoGameAPI.Objective.SIDEBAR_TEAM_JAIL.getName()) != null ? scoreboard.getTeam(TosoGameAPI.Objective.SIDEBAR_TEAM_JAIL.getName()) : scoreboard.registerNewTeam(TosoGameAPI.Objective.SIDEBAR_TEAM_JAIL.getName());
        teamJail.addEntry(SidebarScore.SCORE_3.getText());
        teamJail.setSuffix("" + ChatColor.BLACK + Teams.getOnlineCount(Teams.OnlineTeam.TOSO_JAIL) + ChatColor.GRAY + "人");

        Team teamSuccess = scoreboard.getTeam(TosoGameAPI.Objective.SIDEBAR_TEAM_SUCCESS.getName()) != null ? scoreboard.getTeam(TosoGameAPI.Objective.SIDEBAR_TEAM_SUCCESS.getName()) : scoreboard.registerNewTeam(TosoGameAPI.Objective.SIDEBAR_TEAM_SUCCESS.getName());
        teamSuccess.addEntry(SidebarScore.SCORE_2.getText());
        teamSuccess.setSuffix("" + ChatColor.BLUE + Teams.getOnlineCount(Teams.OnlineTeam.TOSO_SUCCESS) + ChatColor.GRAY + "人");

        Team teamTuho = scoreboard.getTeam(TosoGameAPI.Objective.SIDEBAR_TEAM_TUHO.getName()) != null ? scoreboard.getTeam(TosoGameAPI.Objective.SIDEBAR_TEAM_TUHO.getName()) : scoreboard.registerNewTeam(TosoGameAPI.Objective.SIDEBAR_TEAM_TUHO.getName());
        teamTuho.addEntry(SidebarScore.SCORE_1.getText());
        teamTuho.setSuffix("" + ChatColor.GREEN + Teams.getOnlineCount(Teams.OnlineTeam.TOSO_TUHO) + ChatColor.GRAY + "人");
    }

    public static Scoreboard getBoard(Player p) {
        return hashMap.containsKey(p.getUniqueId()) ? hashMap.get(p.getUniqueId()) : setBoard(p);
    }

    public static HashMap<UUID, Scoreboard> getBoardMap() {
        return hashMap;
    }

    public static void updateScoreboard() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!hashMap.containsKey(player.getUniqueId()))
                setBoard(player);

            Scoreboard scoreboard = hashMap.get(player.getUniqueId());

            Team team = scoreboard.getTeam(TosoGameAPI.Objective.SIDEBAR_STATUS.getName());
            switch (GameManager.getGameState()) {
                case READY:
                    team.setPrefix(ChatColor.YELLOW + "残り" + ChatColor.RESET + ChatColor.GRAY + ": ");
                    team.setSuffix("" + ChatColor.GOLD + ChatColor.BOLD + ChatColor.UNDERLINE + TimerFormat.formatJapan(TimerRunnable.getCountDown()));
                    break;
                case GAME:
                    team.setPrefix(ChatColor.RED + "残り" + ChatColor.RESET + ChatColor.GRAY + ": ");
                    team.setSuffix("" + ChatColor.DARK_RED + ChatColor.BOLD + ChatColor.UNDERLINE + TimerFormat.formatJapan(TimerRunnable.getGameTime()));
                    break;
                case END:
                    team.setPrefix("");
                    team.setSuffix("  " + ChatColor.GRAY + ChatColor.BOLD + "  ゲーム終了");
                    break;
                default:
                    team.setPrefix("");
                    team.setSuffix("" + ChatColor.GREEN + ChatColor.BOLD + "  ゲーム準備中…");
                    break;
            }

            scoreboard.getTeam(TosoGameAPI.Objective.SIDEBAR_TEAM.getName()).setSuffix(Teams.getJoinedTeam(player).getDisplayName());
            scoreboard.getTeam(TosoGameAPI.Objective.SIDEBAR_DIFFICULTY.getName()).setSuffix(TosoGameAPI.difficultyMap.get(player.getUniqueId()).getDisplayName());

            scoreboard.getTeam(TosoGameAPI.Objective.SIDEBAR_REWARD.getName()).setSuffix("" + ChatColor.GOLD + RateManager.getMoney(player) + ChatColor.YELLOW + "円");
            scoreboard.getTeam(TosoGameAPI.Objective.SIDEBAR_RATE.getName()).setSuffix(ChatColor.GRAY + "  (" + RateManager.getRate(player) + "円/秒)");

            scoreboard.getTeam(TosoGameAPI.Objective.SIDEBAR_TEAM_PLAYER.getName()).setSuffix("" + ChatColor.WHITE + Teams.getOnlineCount(Teams.OnlineTeam.TOSO_PLAYER) + ChatColor.GRAY + "人");
            scoreboard.getTeam(TosoGameAPI.Objective.SIDEBAR_TEAM_HUNTER.getName()).setSuffix("" + ChatColor.RED + Teams.getOnlineCount(Teams.OnlineTeam.TOSO_HUNTER) + ChatColor.GRAY + "人");
            scoreboard.getTeam(TosoGameAPI.Objective.SIDEBAR_TEAM_JAIL.getName()).setSuffix("" + ChatColor.BLACK + Teams.getOnlineCount(Teams.OnlineTeam.TOSO_JAIL) + ChatColor.GRAY + "人");
            scoreboard.getTeam(TosoGameAPI.Objective.SIDEBAR_TEAM_SUCCESS.getName()).setSuffix("" + ChatColor.BLUE + Teams.getOnlineCount(Teams.OnlineTeam.TOSO_SUCCESS) + ChatColor.GRAY + "人");
            scoreboard.getTeam(TosoGameAPI.Objective.SIDEBAR_TEAM_TUHO.getName()).setSuffix("" + ChatColor.GREEN + Teams.getOnlineCount(Teams.OnlineTeam.TOSO_TUHO) + ChatColor.GRAY + "人");
        }
    }

    public static void updateTitle(String title) {
        // if (SidebarScore.SCORE_15.getText().equalsIgnoreCase(title)) return;

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!hashMap.containsKey(player.getUniqueId()))
                setBoard(player);

            Scoreboard scoreboard = hashMap.get(player.getUniqueId());

            scoreboard.getTeam(TosoGameAPI.Objective.SIDEBAR_STATUS.getName()).setSuffix(ChatColor.BOLD + (GameManager.isGame() ? TimerFormat.format(TimerRunnable.getTime()) : (GameManager.isGame(GameManager.GameState.END) ? "終了" : "準備中")));

            /*
            scoreboard.resetScores(SidebarScore.SCORE_15.getText());
            scoreboard.getObjective(TosoGameAPI.Objective.SIDEBAR.getName()).getScore(title).setScore(SidebarScore.SCORE_15.getScore());
            */
        }
        // SidebarScore.SCORE_15.setText(title);
    }

    private static List<UUID> hidePlayerList = new ArrayList<>();

    public static void hidePlayers(Player p) {
        for (UUID uuid : hidePlayerList) {
            Player player = Bukkit.getPlayer(uuid);

            if (player == null) continue;
            p.hidePlayer(player);
        }
    }

    public static List<UUID> getHidePlayerList() {
        return hidePlayerList;
    }

    public static boolean isHidePlayer(Player p) {
        return hidePlayerList.contains(p.getUniqueId());
    }

    public static void addHidePlayer(Player p) {
        if (hidePlayerList.contains(p.getUniqueId())) return;
        hidePlayerList.add(p.getUniqueId());
    }

    public static void removeHidePlayer(Player p) {
        if (!hidePlayerList.contains(p.getUniqueId())) return;
        hidePlayerList.remove(p.getUniqueId());
    }

    public enum SidebarScore {
        SCORE_15(15, ChatColor.BLACK + "" + ChatColor.RESET),
        SCORE_14(14, "  "),
        SCORE_13(13, "" + ChatColor.AQUA + ChatColor.UNDERLINE + "チーム" + ChatColor.RESET + ChatColor.GRAY + ": "),
        SCORE_12(12, "   "),
        SCORE_11(11, "" + ChatColor.AQUA + ChatColor.UNDERLINE + "難易度" + ChatColor.RESET + ChatColor.GRAY + ": "),
        SCORE_10(10, "    "),
        SCORE_9(9, "" + ChatColor.AQUA + ChatColor.UNDERLINE + "現在の賞金" + ChatColor.RESET + ChatColor.GRAY + ": "),
        SCORE_8(8, ChatColor.LIGHT_PURPLE + "" + ChatColor.RESET),
        SCORE_7(7, "     "),
        SCORE_6(6, "" + ChatColor.AQUA + ChatColor.UNDERLINE + "参加人数" + ChatColor.RESET + ChatColor.GRAY + ": "),
        SCORE_5(5, ChatColor.WHITE + "  逃走者" + ChatColor.GRAY + ": "),
        SCORE_4(4, ChatColor.RED + "  ハンター" + ChatColor.GRAY + ": "),
        SCORE_3(3, ChatColor.BLACK + "  牢獄" + ChatColor.GRAY + ": "),
        SCORE_2(2, ChatColor.BLUE + "  生存者" + ChatColor.GRAY + ": "),
        SCORE_1(1, ChatColor.GREEN + "  通報部隊" + ChatColor.GRAY + ": ");

        private final int score;
        private String text;

        private SidebarScore(int score, String text) {
            this.score = score;
            this.text = text;
        }

        public int getScore() {
            return score;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }

    /*
    public enum SidebarScore {
        SCORE_15(15, ChatColor.BOLD + (GameManager.isGame() ? TimerFormat.format(TimerRunnable.getTime()) : (GameManager.isGame(GameManager.GameState.END) ? "ゲーム終了" : "ゲーム開始前"))),
        SCORE_14(14, "  "),
        SCORE_13(13, "" + ChatColor.AQUA + ChatColor.UNDERLINE + "チーム" + ChatColor.RESET + ChatColor.GRAY + ": " + ChatColor.YELLOW + "テスト"),
        SCORE_12(12, "   "),
        SCORE_11(11, "" + ChatColor.AQUA + ChatColor.UNDERLINE + "難易度" + ChatColor.RESET + ChatColor.GRAY + ": " + ChatColor.YELLOW + "テスト"),
        SCORE_10(10, "    "),
        SCORE_9(9, "" + ChatColor.AQUA + ChatColor.UNDERLINE + "現在の賞金" + ChatColor.RESET + ChatColor.GRAY + ": " + ChatColor.YELLOW + "0" + ChatColor.GOLD + "円"),
        SCORE_8(8, ChatColor.GRAY + "  (0円/秒)"),
        SCORE_7(7, "     "),
        SCORE_6(6, "" + ChatColor.AQUA + ChatColor.UNDERLINE + "参加人数"),
        SCORE_5(5, ChatColor.WHITE + "  逃走者" + ChatColor.GRAY + ": 0人"),
        SCORE_4(4, ChatColor.RED + "  ハンター" + ChatColor.GRAY + ": 0人"),
        SCORE_3(3, ChatColor.BLACK + "  牢獄" + ChatColor.GRAY + ": 0人"),
        SCORE_2(2, ChatColor.BLUE + "  生存者" + ChatColor.GRAY + ": 0人"),
        SCORE_1(1, ChatColor.GREEN + "  通報部隊" + ChatColor.GRAY + ": 0人");

        private final int score;
        private String text;

        private SidebarScore(int score, String text) {
            this.score = score;
            this.text = text;
        }

        public int getScore() {
            return score;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }
     */
}

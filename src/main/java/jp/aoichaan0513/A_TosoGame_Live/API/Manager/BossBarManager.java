package jp.aoichaan0513.A_TosoGame_Live.API.Manager;

import jp.aoichaan0513.A_TosoGame_Live.API.Timer.TimerFormat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BossBarManager {

    private static HashMap<UUID, BossBar> hashMap = new HashMap<>();

    public static void addBar() {
        for (Player p : Bukkit.getOnlinePlayers())
            addBar(p);
    }

    public static BossBar addBar(Player p) {
        BossBar bossBar = Bukkit.createBossBar("", BarColor.WHITE, BarStyle.SOLID);
        hashMap.put(p.getUniqueId(), bossBar);
        return bossBar;
    }

    public static void removeBar(Player p) {
        if (hashMap.containsKey(p.getUniqueId()))
            hashMap.remove(p.getUniqueId());
    }

    public static BossBar getBar(Player p) {
        return hashMap.containsKey(p.getUniqueId()) ? hashMap.get(p.getUniqueId()) : addBar(p);
    }

    public static void resetBar() {
        for (Map.Entry<UUID, BossBar> entry : hashMap.entrySet())
            entry.getValue().removeAll();
        hashMap.clear();
    }

    public static void showBar() {
        for (Player p : Bukkit.getOnlinePlayers())
            showBar(p, 0, 1);
    }

    public static void showBar(int time, int maxTime) {
        for (Player p : Bukkit.getOnlinePlayers())
            showBar(p, time, maxTime);
    }

    public static void showBar(Player p) {
        showBar(p, 0, 1);
    }

    public static void showBar(Player p, int time, int maxTime) {
        BossBar bossBar = getBar(p);
        switch (GameManager.getGameState()) {
            case READY:
                bossBar.setTitle(ChatColor.YELLOW + "ゲーム開始まで" + ChatColor.GRAY + ": " + ChatColor.GOLD + ChatColor.BOLD + ChatColor.UNDERLINE + TimerFormat.formatJapan(time));
                bossBar.setProgress((double) time / maxTime);
                bossBar.setColor(BarColor.YELLOW);
                bossBar.addPlayer(p);
                break;
            case GAME:
                if (MissionManager.isMission())
                    bossBar.setTitle(ChatColor.BOLD + "" + ChatColor.RED + "ゲーム終了まで" + ChatColor.GRAY + ": " + ChatColor.DARK_RED + ChatColor.BOLD + ChatColor.UNDERLINE + TimerFormat.formatJapan(time) + ChatColor.RESET + ChatColor.GRAY + " / " + ChatColor.YELLOW + ChatColor.BOLD + "ミッション実施中");
                else
                    bossBar.setTitle(ChatColor.BOLD + "" + ChatColor.RED + "ゲーム終了まで" + ChatColor.GRAY + ": " + ChatColor.DARK_RED + ChatColor.BOLD + ChatColor.UNDERLINE + TimerFormat.formatJapan(time));

                bossBar.setProgress((double) time / maxTime);
                bossBar.setColor(BarColor.RED);
                bossBar.addPlayer(p);
                break;
            case END:
                bossBar.setTitle(ChatColor.GRAY + "" + ChatColor.BOLD + "ゲーム終了");
                bossBar.setProgress(1);
                bossBar.setColor(BarColor.WHITE);
                bossBar.addPlayer(p);
                break;
            default:
                bossBar.setTitle(ChatColor.GREEN + "" + ChatColor.BOLD + "ゲーム準備中…");
                bossBar.setProgress(1);
                bossBar.setColor(BarColor.GREEN);
                bossBar.addPlayer(p);
                break;
        }
    }
}

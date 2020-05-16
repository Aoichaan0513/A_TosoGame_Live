package jp.aoichaan0513.A_TosoGame_Live.Runnable;

import jp.aoichaan0513.A_TosoGame_Live.API.Manager.ActionBarManager;
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.GameManager;
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.World.WorldConfig;
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.World.WorldManager;
import jp.aoichaan0513.A_TosoGame_Live.API.Scoreboard.Teams;
import jp.aoichaan0513.A_TosoGame_Live.API.Timer.TimerFormat;
import jp.aoichaan0513.A_TosoGame_Live.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;

public class RespawnRunnable extends BukkitRunnable {

    // 復活回数
    public static HashMap<UUID, Integer> countMap = new HashMap<>();

    // 復活クールタイム秒数
    public static HashMap<UUID, Integer> timeMap = new HashMap<>();

    @Override
    public void run() {
        if (!GameManager.isGame(GameManager.GameState.GAME)) return;

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_JAIL, player) || !countMap.containsKey(player.getUniqueId()) || !timeMap.containsKey(player.getUniqueId()))
                continue;

            int time = timeMap.get(player.getUniqueId());

            if (time > 0) {
                ActionBarManager.sendActionBar(player, ChatColor.GRAY + "復活まで残り" + ChatColor.GOLD + ChatColor.BOLD + ChatColor.UNDERLINE + TimerFormat.formatJapan(time));
                timeMap.put(player.getUniqueId(), time--);
            } else {
                timeMap.remove(player.getUniqueId());
            }
        }
    }

    public static void addCoolTime(Player p) {
        setCoolTime(p, countMap.containsKey(p.getUniqueId()) ? countMap.get(p.getUniqueId()) + 1 : 1);
    }

    public static void setCoolTime(Player p) {
        setCoolTime(p, getCount(p));
    }

    public static void setCoolTime(Player p, int count) {
        WorldConfig worldConfig = Main.getWorldConfig();
        WorldConfig.DifficultyConfig difficultyConfig = worldConfig.getDifficultyConfig(p);

        timeMap.put(p.getUniqueId(), difficultyConfig.getRespawnCoolTime() * countMap.put(p.getUniqueId(), count));
    }

    public static int getCount(Player p) {
        return countMap.getOrDefault(p.getUniqueId(), 1);
    }

    public static int getCoolTime(Player p) {
        WorldConfig worldConfig = Main.getWorldConfig();
        WorldConfig.DifficultyConfig difficultyConfig = worldConfig.getDifficultyConfig(p);

        return timeMap.getOrDefault(p.getUniqueId(), difficultyConfig.getRespawnCoolTime());
    }

    public static boolean isAllowRespawn(Player p) {
        WorldConfig worldConfig = Main.getWorldConfig();
        WorldConfig.DifficultyConfig difficultyConfig = worldConfig.getDifficultyConfig(p);

        return getCount(p) < difficultyConfig.getRespawnDenyCount();
    }

    public static boolean isCoolTime(Player p) {
        return timeMap.containsKey(p.getUniqueId()) && timeMap.get(p.getUniqueId()) > 0;
    }

    public static WorldManager.GameType getGameType(Player p) {
        return countMap.containsKey(p.getUniqueId()) ? WorldManager.GameType.RESPAWN : WorldManager.GameType.START;
    }

    public static void reset() {
        countMap.clear();
        timeMap.clear();
    }
}

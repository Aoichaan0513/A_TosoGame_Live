package jp.aoichaan0513.A_TosoGame_Live.API.Manager;

import jp.aoichaan0513.A_TosoGame_Live.API.Manager.World.WorldConfig;
import jp.aoichaan0513.A_TosoGame_Live.API.Scoreboard.Teams;
import jp.aoichaan0513.A_TosoGame_Live.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class RateManager {

    private static HashMap<UUID, Long> moneyMap = new HashMap<>();
    private static HashMap<UUID, Integer> rateMap = new HashMap<>();

    // 賞金関連
    public static boolean hasMoney(Player p) {
        return moneyMap.containsKey(p.getUniqueId());
    }

    public static long getMoney(Player p) {
        return moneyMap.getOrDefault(p.getUniqueId(), 0L);
    }

    public static void setMoney(Player p, long l) {
        if (!GameManager.isGame(GameManager.GameState.GAME)) return;
        if (isPlayer(p))
            moneyMap.put(p.getUniqueId(), l);
    }

    public static void addMoney() {
        if (!GameManager.isGame(GameManager.GameState.GAME)) return;
        for (Player p : Bukkit.getOnlinePlayers())
            addMoney(p);
    }

    public static void addMoney(Player p) {
        if (!GameManager.isGame(GameManager.GameState.GAME)) return;
        if (isPlayer(p))
            moneyMap.put(p.getUniqueId(), moneyMap.containsKey(p.getUniqueId()) ? moneyMap.get(p.getUniqueId()) + getRate(p) : (long) getRate(p));
    }

    public static void addMoney(Player p, long l) {
        if (!GameManager.isGame(GameManager.GameState.GAME)) return;
        if (isPlayer(p))
            moneyMap.put(p.getUniqueId(), moneyMap.containsKey(p.getUniqueId()) ? moneyMap.get(p.getUniqueId()) + l : (long) getRate(p));
    }

    public static void addMoney(Player p, boolean bool) {
        if (!GameManager.isGame(GameManager.GameState.GAME)) return;
        if (bool) {
            moneyMap.put(p.getUniqueId(), moneyMap.containsKey(p.getUniqueId()) ? moneyMap.get(p.getUniqueId()) + getRate(p) : (long) getRate(p));
        } else {
            addMoney(p);
        }
    }

    public static void removeMoney(Player p, long l) {
        if (!GameManager.isGame(GameManager.GameState.GAME)) return;
        if (isPlayer(p))
            moneyMap.put(p.getUniqueId(), moneyMap.containsKey(p.getUniqueId()) && l >= moneyMap.get(p.getUniqueId()) ? moneyMap.get(p.getUniqueId()) - l : 0);
    }

    public static void resetMoney() {
        if (GameManager.isGame(GameManager.GameState.GAME)) return;
        moneyMap.clear();
    }

    // レート関連
    public static boolean hasRate(Player p) {
        return rateMap.containsKey(p.getUniqueId());
    }

    public static int getRate(Player p) {
        if (rateMap.containsKey(p.getUniqueId())) {
            return rateMap.get(p.getUniqueId());
        } else {
            setRate(p);
            return rateMap.get(p.getUniqueId());
        }
    }

    public static void setRate(Player p) {
        WorldConfig worldConfig = Main.getWorldConfig();
        setRate(p, worldConfig.getDifficultyConfig(p).getRate());
    }

    public static void setRate(Player p, int rate) {
        rateMap.put(p.getUniqueId(), rate);
    }

    public static void addRate(Player p, int rate) {
        rateMap.put(p.getUniqueId(), getRate(p) + rate);
    }

    public static void removeRate(Player p, int rate) {
        int i = getRate(p);
        if (i >= rate)
            rateMap.put(p.getUniqueId(), i - rate);
        else
            rateMap.put(p.getUniqueId(), 0);
    }

    public static void resetRate() {
        if (GameManager.isGame(GameManager.GameState.GAME)) return;
        rateMap.clear();
        for (Player player : Bukkit.getOnlinePlayers())
            setRate(player);
    }

    public static boolean isPlayer(Player p) {
        return Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_PLAYER, p) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_SUCCESS, p);
    }
}

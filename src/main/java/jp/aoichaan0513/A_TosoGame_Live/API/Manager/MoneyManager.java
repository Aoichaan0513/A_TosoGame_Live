package jp.aoichaan0513.A_TosoGame_Live.API.Manager;

import jp.aoichaan0513.A_TosoGame_Live.API.Manager.World.WorldConfig;
import jp.aoichaan0513.A_TosoGame_Live.API.Scoreboard.Teams;
import jp.aoichaan0513.A_TosoGame_Live.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class MoneyManager {

    private static HashMap<UUID, Long> rewardMap = new HashMap<>();
    private static HashMap<UUID, Integer> rateMap = new HashMap<>();

    // 賞金関連
    public static boolean hasReward(UUID uuid) {
        return rewardMap.containsKey(uuid);
    }

    public static boolean hasReward(Player p) {
        return hasReward(p.getUniqueId());
    }

    public static long getReward(UUID uuid) {
        return rewardMap.getOrDefault(uuid, 0L);
    }

    public static long getReward(Player p) {
        return getReward(p.getUniqueId());
    }

    public static void setReward(Player p, long l) {
        rewardMap.put(p.getUniqueId(), l);
    }

    public static void addReward() {
        if (!GameManager.isGame(GameManager.GameState.GAME)) return;
        for (Player p : Bukkit.getOnlinePlayers())
            addReward(p);
    }

    public static void addReward(Player p) {
        if (!GameManager.isGame(GameManager.GameState.GAME)) return;
        if (isPlayer(p))
            addReward(p, getRate(p));
    }

    public static void addReward(Player p, long l) {
        if (!GameManager.isGame(GameManager.GameState.GAME)) return;
        if (isPlayer(p))
            rewardMap.put(p.getUniqueId(), rewardMap.containsKey(p.getUniqueId()) ? rewardMap.get(p.getUniqueId()) + l : (long) getRate(p));
    }

    public static void removeReward(Player p, long l) {
        if (!GameManager.isGame(GameManager.GameState.GAME)) return;
        if (isPlayer(p))
            rewardMap.put(p.getUniqueId(), rewardMap.containsKey(p.getUniqueId()) && l >= rewardMap.get(p.getUniqueId()) ? rewardMap.get(p.getUniqueId()) - l : 0);
    }

    public static void resetReward() {
        if (GameManager.isGame(GameManager.GameState.GAME)) return;
        rewardMap.clear();
    }

    public static HashMap<UUID, Long> getRewardMap() {
        return rewardMap;
    }

    // レート関連
    public static boolean hasRate(UUID uuid) {
        return rateMap.containsKey(uuid);
    }

    public static boolean hasRate(Player p) {
        return hasRate(p.getUniqueId());
    }

    public static int getRate(UUID uuid) {
        if (!rateMap.containsKey(uuid))
            setRate(uuid);
        return rateMap.get(uuid);
    }

    public static int getRate(Player p) {
        return getRate(p.getUniqueId());
    }

    public static void setRate(UUID uuid) {
        WorldConfig worldConfig = Main.getWorldConfig();
        setRate(uuid, worldConfig.getDifficultyConfig(uuid).getRate());
    }

    public static void setRate(Player p) {
        setRate(p.getUniqueId());
    }

    public static void setRate(UUID uuid, int rate) {
        rateMap.put(uuid, rate);
    }

    public static void setRate(Player p, int rate) {
        setRate(p.getUniqueId(), rate);
    }

    public static void addRate(Player p, int rate) {
        rateMap.put(p.getUniqueId(), getRate(p) + rate);
    }

    public static void removeRate(Player p, int rate) {
        int i = getRate(p);
        rateMap.put(p.getUniqueId(), i >= rate ? i - rate : 0);
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

    public static HashMap<UUID, Integer> getRateMap() {
        return rateMap;
    }


    public static String formatMoney(long l) {
        return String.format("%,d", l);
    }

    @Deprecated
    public static String formatMoney(int i) {
        return String.format("%,d", i);
    }
}

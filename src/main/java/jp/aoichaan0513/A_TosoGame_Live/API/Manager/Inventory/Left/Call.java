package jp.aoichaan0513.A_TosoGame_Live.API.Manager.Inventory.Left;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;

public class Call {
    private static HashMap<Player, Player> hashMap = new HashMap<>();
    private static HashMap<Player, Player> acceptMap = new HashMap<>();

    public static HashMap<Player, BukkitTask> soundMap = new HashMap<>();

    public static void reset() {
        acceptMap.clear();
        hashMap.clear();
    }

    public static void startCall(Player from, Player to) {
        if (!acceptMap.containsKey(to))
            acceptMap.put(to, from);
        return;
    }

    public static Player endCall(Player p) {
        if (hashMap.containsKey(p)) {
            Player player = hashMap.get(p);
            hashMap.remove(p);
            return player;
        } else if (hashMap.containsValue(p)) {
            for (Map.Entry<Player, Player> entry : hashMap.entrySet()) {
                if (entry.getValue() == p) {
                    Player player = entry.getKey();
                    hashMap.remove(entry.getKey());
                    return player;
                }
            }
        }
        return null;
    }

    public static Player acceptCall(Player p) {
        if (acceptMap.containsKey(p)) {
            Player player = acceptMap.get(p);
            hashMap.put(p, player);
            acceptMap.remove(p);
            return player;
        }
        return null;
    }

    public static Player rejectCall(Player p) {
        if (acceptMap.containsKey(p)) {
            Player player = acceptMap.get(p);
            acceptMap.remove(p);
            return player;
        } else if (acceptMap.containsValue(p)) {
            Player player = null;
            for (Map.Entry<Player, Player> entry : acceptMap.entrySet()) {
                if (entry.getValue() == p) {
                    player = entry.getKey();
                    acceptMap.remove(entry.getKey());
                }
            }
            return player;
        }
        return null;
    }

    public static Player getCall(Player p) {
        if (hashMap.containsKey(p)) {
            return hashMap.get(p);
        } else if (hashMap.containsValue(p)) {
            for (Map.Entry<Player, Player> entry : hashMap.entrySet()) {
                if (entry.getValue() == p) {
                    return entry.getKey();
                }
            }
        }
        return null;
    }

    public static boolean isCall(Player p) {
        return hashMap.containsKey(p) || hashMap.containsValue(p);
    }

    public static boolean isAccept(Player p) {
        return acceptMap.containsKey(p) || acceptMap.containsValue(p);
    }
}

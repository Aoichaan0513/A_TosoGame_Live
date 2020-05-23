package jp.aoichaan0513.A_TosoGame_Live.API.Manager.Player;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class PlayerManager {

    private static Map<UUID, PlayerConfig> hashMap = new HashMap<>();

    public static PlayerConfig loadConfig(UUID uuid) {
        if (!hashMap.containsKey(uuid)) {
            PlayerConfig playerConfig = new PlayerConfig(uuid);
            hashMap.put(uuid, playerConfig);
        }
        return hashMap.get(uuid);
    }

    public static PlayerConfig loadConfig(Player p) {
        return loadConfig(p.getUniqueId());
    }

    public static PlayerConfig reloadConfig(UUID uuid) {
        PlayerConfig playerConfig = new PlayerConfig(uuid);
        hashMap.put(uuid, playerConfig);
        return hashMap.get(uuid);
    }

    public static PlayerConfig reloadConfig(Player p) {
        return reloadConfig(p.getUniqueId());
    }

    public static void reloadConfig() {
        for (Player player : Bukkit.getOnlinePlayers())
            reloadConfig(player);
    }

    public static Set<Map.Entry<UUID, PlayerConfig>> getConfigs() {
        return hashMap.entrySet();
    }
}

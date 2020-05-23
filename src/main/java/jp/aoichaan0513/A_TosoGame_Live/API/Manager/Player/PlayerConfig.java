package jp.aoichaan0513.A_TosoGame_Live.API.Manager.Player;

import jp.aoichaan0513.A_TosoGame_Live.API.Enums.Advancement;
import jp.aoichaan0513.A_TosoGame_Live.API.Interfaces.IConfig;
import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI;
import jp.aoichaan0513.A_TosoGame_Live.Main;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class PlayerConfig {

    private static UUID uuid;

    private static File file;
    private static YamlConfiguration yamlConfiguration;

    private static long money = 0;
    private static boolean hasPermission = false;
    private static boolean isBroadCaster = false;

    private static AdvancementConfig advancementConfig;

    public PlayerConfig(UUID uuid) {
        String baseFileName = "player.yml";
        String fileName = uuid.toString() + ".yml";

        PlayerConfig.uuid = uuid;

        file = new File(Main.getInstance().getDataFolder() + Main.FILE_SEPARATOR + "players" + Main.FILE_SEPARATOR + fileName);
        if (file.exists()) {
            yamlConfiguration = YamlConfiguration.loadConfiguration(file);
        } else {
            try {
                Files.copy(Main.getInstance().getResource(baseFileName), Paths.get(Main.getInstance().getDataFolder() + Main.FILE_SEPARATOR + "players" + Main.FILE_SEPARATOR + fileName));
            } catch (IOException e) {
                e.printStackTrace();
            }
            yamlConfiguration = YamlConfiguration.loadConfiguration(file);
        }

        money = yamlConfiguration.getLong("money", 0);
        hasPermission = yamlConfiguration.getBoolean("hasPermission", false);
        isBroadCaster = yamlConfiguration.getBoolean("isBroadCaster", false);

        advancementConfig = new AdvancementConfig(file, yamlConfiguration);
    }

    public UUID getUUID() {
        return uuid;
    }

    public File getFile() {
        return file;
    }

    public YamlConfiguration getConfig() {
        return yamlConfiguration;
    }


    /**
     * 所持金を取得します。
     *
     * @return 所持金
     */
    public long getMoney() {
        return money;
    }

    /**
     * 所持金を設定します。
     *
     * @param money 所持金
     */
    public void setMoney(long money) {
        PlayerConfig.money = money;
        yamlConfiguration.set("money", money);
        save();
    }

    /**
     * コマンドを使用して運営チームへの参加・離脱等が出来るかを取得します。
     *
     * @return 権限が付与されているか
     */
    public boolean getPermission() {
        return hasPermission;
    }

    /**
     * コマンドを使用して運営チームへの参加・離脱等が出来るかを設定します。
     *
     * @param hasPermission 権限が付与されているか
     */
    public void setPermission(boolean hasPermission) {
        PlayerConfig.hasPermission = hasPermission;
        yamlConfiguration.set("hasPermission", hasPermission);
        save();
    }


    /**
     * 配信者モードかどうかを取得します。
     *
     * @return 配信者モードか
     */
    public boolean getBroadCaster() {
        return isBroadCaster;
    }

    /**
     * 配信者モードかどうかを設定します。
     *
     * @param isBroadCaster 配信者モードか
     */
    public void setBroadCaster(boolean isBroadCaster) {
        PlayerConfig.isBroadCaster = isBroadCaster;
        yamlConfiguration.set("isBroadCaster", isBroadCaster);
        save();
    }

    public AdvancementConfig getAdvancementConfig() {
        return advancementConfig;
    }

    private static void setAdvancementConfig(AdvancementConfig advancementConfig) {
        PlayerConfig.advancementConfig = advancementConfig;
    }

    public static class AdvancementConfig extends IConfig {

        private final String PATH = "advancements";

        public AdvancementConfig(File file, YamlConfiguration yamlConfiguration) {
            super(file, yamlConfiguration);
        }

        public void addAdvancement(Advancement advancement) {
            if (advancement == Advancement.UNKNOWN) return;

            Set<Integer> set = new HashSet<>(yamlConfiguration.getIntegerList(PATH));
            set.add(advancement.getId());

            yamlConfiguration.set(PATH, new ArrayList<>(set));
            save();
        }

        public void removeAdvancement(Advancement advancement) {
            if (advancement == Advancement.UNKNOWN) return;

            Set<Integer> set = new HashSet<>(yamlConfiguration.getIntegerList(PATH));
            set.remove(advancement.getId());

            yamlConfiguration.set(PATH, new ArrayList<>(set));
            save();
        }

        public boolean hasAdvancement(Advancement advancement) {
            return getAdvancements().stream().anyMatch(a -> a.getId() == advancement.getId());
        }

        public Set<Advancement> getAdvancements() {
            Set<Advancement> set = new HashSet<>();
            for (int i : yamlConfiguration.getIntegerList(PATH))
                set.add(Advancement.getAdvancement(i));

            return set;
        }

        public void setAdvancements(Collection<Advancement> advancements) {
            Set<Integer> set = new HashSet<>();
            for (Advancement advancement : advancements)
                if (advancement != Advancement.UNKNOWN)
                    set.add(advancement.getId());

            yamlConfiguration.set(PATH, new ArrayList<>(set));
            save();
        }

        public void setAdvancements(Advancement... advancements) {
            Set<Integer> set = new HashSet<>();
            for (Advancement advancement : advancements)
                if (advancement != Advancement.UNKNOWN)
                    set.add(advancement.getId());

            yamlConfiguration.set(PATH, new ArrayList<>(set));
            save();
        }

        @Override
        public void save() {
            super.save();

            PlayerConfig.file = file;
            PlayerConfig.yamlConfiguration = this.yamlConfiguration;

            setAdvancementConfig(this);

            PlayerManager.reloadConfig(uuid);
        }
    }


    public void save() {
        try {
            yamlConfiguration.save(file);
            PlayerManager.reloadConfig(uuid);

            System.out.println(file.getName());
            System.out.println(hashCode());

            for (Map.Entry<UUID, PlayerConfig> entry : PlayerManager.getConfigs())
                System.out.println(entry.getKey() + ": " + entry.getValue().toString());

        } catch (IOException e) {
            Bukkit.getConsoleSender().sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "予期しないエラーが発生しました。\n" +
                    MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "位置: " + Main.PACKAGE_PATH + ".API.Manager.Player.PlayerConfig");
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "UUID: " + getUUID().toString() + ", hasPermission: " + getPermission() + ", isBroadCaster: " + getBroadCaster();
    }
}

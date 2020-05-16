package jp.aoichaan0513.A_TosoGame_Live.API.Manager.World;

import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI;
import jp.aoichaan0513.A_TosoGame_Live.API.TosoGameAPI;
import jp.aoichaan0513.A_TosoGame_Live.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WorldConfig {

    private World world;

    private File file;
    private YamlConfiguration yamlConfiguration;

    // private EnumMap<WorldManager.Difficulty, DifficultyConfig> difficultyConfigMap = new EnumMap<>(WorldManager.Difficulty.class);
    private HashMap<WorldManager.Difficulty, DifficultyConfig> difficultyConfigMap = new HashMap<>();

    private MapConfig mapConfig;
    private GameConfig gameConfig;

    private OPGameConfig opGameConfig;

    private OPGameLocationConfig opGameLocationConfig;
    private HunterLocationConfig hunterLocationConfig;
    private JailLocationConfig jailLocationConfig;
    private RespawnLocationConfig respawnLocationConfig;

    private HunterDoorConfig hunterDoorConfig;

    private MapBorderConfig mapBorderConfig;
    private OPGameBorderConfig opGameBorderConfig;
    private HunterZoneBorderConfig hunterZoneBorderConfig;

    public WorldConfig(World world) {
        String fileName = "map.yml";

        this.world = world;

        this.file = new File(world.getName() + Main.FILE_SEPARATOR + fileName);
        if (this.file.exists()) {
            this.yamlConfiguration = YamlConfiguration.loadConfiguration(file);
        } else {
            try {
                Files.copy(Main.getInstance().getResource(fileName), Paths.get(world.getName() + Main.FILE_SEPARATOR + fileName));
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.yamlConfiguration = YamlConfiguration.loadConfiguration(file);
        }

        this.mapConfig = new MapConfig(this);
        this.gameConfig = new GameConfig(this);

        this.opGameConfig = new OPGameConfig(this);

        this.opGameLocationConfig = new OPGameLocationConfig(this);
        this.hunterLocationConfig = new HunterLocationConfig(this);
        this.jailLocationConfig = new JailLocationConfig(this);
        this.respawnLocationConfig = new RespawnLocationConfig(this);

        this.hunterDoorConfig = new HunterDoorConfig(this);

        this.mapBorderConfig = new MapBorderConfig(this);
        this.opGameBorderConfig = new OPGameBorderConfig(this);
        this.hunterZoneBorderConfig = new HunterZoneBorderConfig(this);

        for (String name : this.yamlConfiguration.getConfigurationSection("difficulty").getKeys(false)) {
            WorldManager.Difficulty difficulty = WorldManager.Difficulty.getDifficulty(name);
            this.difficultyConfigMap.put(difficulty, new DifficultyConfig(difficulty, this));
        }
    }

    public World getWorld() {
        return world;
    }

    public File getFile() {
        return file;
    }

    public YamlConfiguration getConfig() {
        return yamlConfiguration;
    }


    /**
     * マップの基本設定
     */
    public MapConfig getMapConfig() {
        return mapConfig;
    }

    private void setMapConfig(MapConfig mapConfig) {
        this.mapConfig = mapConfig;
    }

    /**
     * ゲームの基本設定
     */
    public GameConfig getGameConfig() {
        return gameConfig;
    }

    private void setGameConfig(GameConfig gameConfig) {
        this.gameConfig = gameConfig;
    }


    /**
     * オープニングゲーム設定
     */
    public OPGameConfig getOPGameConfig() {
        return opGameConfig;
    }

    public void setOPGameConfig(OPGameConfig opGameConfig) {
        this.opGameConfig = opGameConfig;
    }


    /**
     * 位置設定
     */
    public OPGameLocationConfig getOPGameLocationConfig() {
        return opGameLocationConfig;
    }

    public void setOPGameLocationConfig(OPGameLocationConfig opGameLocationConfig) {
        this.opGameLocationConfig = opGameLocationConfig;
    }

    /**
     * 位置設定
     */
    public HunterLocationConfig getHunterLocationConfig() {
        return hunterLocationConfig;
    }

    public void setHunterLocationConfig(HunterLocationConfig hunterLocationConfig) {
        this.hunterLocationConfig = hunterLocationConfig;
    }

    /**
     * 位置設定
     */
    public JailLocationConfig getJailLocationConfig() {
        return jailLocationConfig;
    }

    public void setJailLocationConfig(JailLocationConfig jailLocationConfig) {
        this.jailLocationConfig = jailLocationConfig;
    }

    /**
     * 位置設定
     */
    public RespawnLocationConfig getRespawnLocationConfig() {
        return respawnLocationConfig;
    }

    public void setRespawnLocationConfig(RespawnLocationConfig respawnLocationConfig) {
        this.respawnLocationConfig = respawnLocationConfig;
    }


    /**
     * ドア設定
     */
    public HunterDoorConfig getHunterDoorConfig() {
        return hunterDoorConfig;
    }

    public void setHunterDoorConfig(HunterDoorConfig hunterDoorConfig) {
        this.hunterDoorConfig = hunterDoorConfig;
    }


    /**
     * ボーダー設定
     */
    public MapBorderConfig getMapBorderConfig() {
        return mapBorderConfig;
    }

    public void setMapBorderConfig(MapBorderConfig mapBorderConfig) {
        this.mapBorderConfig = mapBorderConfig;
    }

    /**
     * ボーダー設定
     */
    public OPGameBorderConfig getOPGameBorderConfig() {
        return opGameBorderConfig;
    }

    public void setOPGameBorderConfig(OPGameBorderConfig opGameBorderConfig) {
        this.opGameBorderConfig = opGameBorderConfig;
    }

    /**
     * ボーダー設定
     */
    public HunterZoneBorderConfig getHunterZoneBorderConfig() {
        return hunterZoneBorderConfig;
    }

    public void setHunterZoneBorderConfig(HunterZoneBorderConfig hunterZoneBorderConfig) {
        this.hunterZoneBorderConfig = hunterZoneBorderConfig;
    }


    /**
     * 難易度
     */
    public DifficultyConfig getDifficultyConfig(Player p) {
        WorldManager.Difficulty difficulty = TosoGameAPI.difficultyMap.containsKey(p.getUniqueId()) ? TosoGameAPI.difficultyMap.get(p.getUniqueId()) : TosoGameAPI.difficultyMap.put(p.getUniqueId(), WorldManager.Difficulty.NORMAL);
        System.out.println(difficultyConfigMap.get(difficulty).getDifficulty().getName());
        return difficultyConfigMap.get(difficulty);
    }

    public DifficultyConfig getDifficultyConfig(WorldManager.Difficulty difficulty) {
        // System.out.println(new DifficultyConfig(difficulty, this).getDifficulty().getName());
        return difficultyConfigMap.containsKey(difficulty) ? difficultyConfigMap.get(difficulty) : difficultyConfigMap.put(difficulty, new DifficultyConfig(difficulty, this));
    }


    /* ================================================== */


    /**
     * マップの基本設定
     */
    public static class MapConfig extends IConfig {

        private final String PATH = WorldManager.PathType.MAP.getPath();

        public MapConfig(WorldConfig worldConfig) {
            super(worldConfig);
        }

        /**
         * マップの名前を取得します。
         *
         * @return 設定されている名前 / 設定されていない場合は「未設定」
         */
        public String getName() {
            return yamlConfiguration.getString(PATH + ".name", "未設定");
        }

        /**
         * マップの説明を取得します。
         *
         * @return 設定されている説明 / 設定されていない場合は「未設定」
         */
        public String getDescription() {
            return yamlConfiguration.getString(PATH + ".description", "未設定");
        }

        /**
         * マップバージョンを取得します。
         *
         * @return マップバージョン / 設定されていない場合は「1.0」
         */
        public double getVersion() {
            return yamlConfiguration.getDouble(PATH + ".version", 1.0);
        }

        /**
         * マップの製作者情報を表示します。
         *
         * @return 製作者一覧 / 設定されていない場合は「未設定」
         */
        public String getAuthors() {
            String path = PATH + ".authors";
            return yamlConfiguration.contains(path) && !yamlConfiguration.getStringList(path).isEmpty() ? yamlConfiguration.getStringList(path).toString() : "未設定";
        }

        /**
         * マップのアイコンを取得します。
         *
         * @return 設定されているアイコン / 設定されていない場合は「草ブロック」
         */
        public Material getIcon() {
            return Material.matchMaterial(yamlConfiguration.getString(PATH + ".icon", "grass_block"));
        }
    }

    /**
     * ゲームの基本設定
     */
    public static class GameConfig extends IConfig {

        private final String PATH_TIME = WorldManager.PathType.GAME_TIME.getPath();
        private final String PATH_OTHER = WorldManager.PathType.GAME_TIME.getPath();

        public GameConfig(WorldConfig worldConfig) {
            super(worldConfig);
        }

        /**
         * ゲーム開始前のカウントダウン時間を取得します。
         *
         * @return カウントダウン時間 (秒) / 設定されていない場合は15秒
         */
        public int getCountDown() {
            return yamlConfiguration.getInt(PATH_TIME + ".countdown", 15);
        }

        /**
         * ゲーム開始前のカウントダウン時間を設定します。
         *
         * @param countdown カウントダウン (秒)
         */
        public void setCountDown(int countdown) {
            yamlConfiguration.set(PATH_TIME + ".countdown", countdown);
            save();
        }

        /**
         * ゲーム時間を取得します。
         *
         * @return ゲーム時間 (秒) / 設定されていない場合は1200秒 (20分)
         */
        public int getGame() {
            return yamlConfiguration.getInt(PATH_TIME + ".game", 1200);
        }

        /**
         * ゲーム時間を設定します。
         *
         * @param game ゲーム時間 (秒)
         */
        public void setGame(int game) {
            yamlConfiguration.set(PATH_TIME + ".game", game);
            save();
        }

        /**
         * 復活が禁止になる時間を取得します。
         *
         * @return 復活禁止時間 (秒) / 設定されていない場合は240秒 (4分)
         */
        public int getRespawnDeny() {
            return yamlConfiguration.getInt(PATH_TIME + ".respawn", 240);
        }

        /**
         * 復活が禁止になる時間を設定します。
         *
         * @param respawnDeny 復活禁止時間 (秒)
         */
        public void setRespawnDeny(int respawnDeny) {
            yamlConfiguration.set(PATH_TIME + ".respawn", respawnDeny);
            save();
        }

        /**
         * スクリプト機能を有効かどうかを取得します。
         *
         * @return スクリプト機能が有効か無効か / 設定されていない場合は無効
         */
        public boolean getScript() {
            return yamlConfiguration.getBoolean(PATH_OTHER + ".script", false);
        }

        /**
         * スクリプト機能を有効かどうかを設定します。
         *
         * @param script 有効か無効か
         */
        public void setScript(boolean script) {
            yamlConfiguration.set(PATH_OTHER + ".script", script);
            save();
        }

        /**
         * 生存ミッションが有効かどうかを取得します、
         *
         * @return 生存ミッションが有効か無効か / 設定されていない場合は有効
         */
        public boolean getSuccessMission() {
            return yamlConfiguration.getBoolean(PATH_OTHER + ".success", true);
        }

        /**
         * 生存ミッションが有効かどうかを設定します。
         *
         * @param success 有効か無効か
         */
        public void setSuccessMission(boolean success) {
            yamlConfiguration.set(PATH_OTHER + ".success", success);
            save();
        }

        /**
         * ダッシュジャンプが有効かどうかを取得します。
         *
         * @return ダッシュジャンプが有効か無効か / 設定されていない場合は有効
         */
        public boolean getJump() {
            return yamlConfiguration.getBoolean(PATH_OTHER + ".dashjump", true);
        }

        /**
         * ダッシュジャンプが有効かどうかを設定します。
         *
         * @param jump 有効か無効か
         */
        public void setJump(boolean jump) {
            yamlConfiguration.set(PATH_OTHER + ".dashjump", jump);
            save();
        }

        @Override
        public void save() {
            super.save();
            worldConfig.setGameConfig(this);
        }
    }


    /**
     * オープニングゲーム設定
     */
    public static class OPGameConfig extends IConfig {

        private final String PATH_DICE = WorldManager.PathType.OPGAME_DICE.getPath();

        public OPGameConfig(WorldConfig worldConfig) {
            super(worldConfig);
        }

        /**
         * サイコロミッションの合計数を取得します。
         *
         * @return 設定されている合計数 / 設定されていない場合は30
         */
        public int getDiceCount() {
            return yamlConfiguration.getInt(PATH_DICE + ".count", 30);
        }

        /**
         * サイコロミッションの合計数を設定します。
         *
         * @param diceCount 設定する合計数
         */
        public void setDiceCount(int diceCount) {
            yamlConfiguration.set(PATH_DICE + ".count", diceCount);
            save();
        }

        @Override
        public void save() {
            super.save();
            worldConfig.setOPGameConfig(this);
        }
    }


    /**
     * 位置設定
     */
    public static class OPGameLocationConfig extends IConfig {

        private final String PATH_OPGAME = WorldManager.PathType.LOCATION_OPGAME.getPath();
        private final String PATH_GOPGAME = WorldManager.PathType.LOCATION_GOPGAME.getPath();

        public OPGameLocationConfig(WorldConfig worldConfig) {
            super(worldConfig);
        }

        /**
         * オープニングゲーム時に代表者がテレポートされる位置を取得します。
         *
         * @return 設定されている位置
         */
        public Location getOPLocation() {
            double x = yamlConfiguration.getDouble(PATH_OPGAME + ".x");
            double y = yamlConfiguration.getDouble(PATH_OPGAME + ".y");
            double z = yamlConfiguration.getDouble(PATH_OPGAME + ".z");
            int yaw = yamlConfiguration.getInt(PATH_OPGAME + ".yaw");
            int pitch = yamlConfiguration.getInt(PATH_OPGAME + ".pitch");

            return new Location(worldConfig.getWorld(), x, y, z, yaw, pitch);
        }

        /**
         * オープニングゲーム時に代表者がテレポートされる位置を設定します。
         *
         * @param location 設定する位置
         */
        public void setOPLocation(Location location) {
            yamlConfiguration.set(PATH_OPGAME + ".x", location.getBlockX() + 0.5);
            yamlConfiguration.set(PATH_OPGAME + ".y", location.getBlockY());
            yamlConfiguration.set(PATH_OPGAME + ".z", location.getBlockZ() + 0.5);
            yamlConfiguration.set(PATH_OPGAME + ".yaw", location.getYaw());
            yamlConfiguration.set(PATH_OPGAME + ".pitch", location.getPitch());
            save();
        }

        /**
         * オープニングゲームのプレイヤー待機場所の位置をすべて取得します。
         *
         * @return 設定されている位置 (ミュータブルリスト)
         */
        public List<Location> getGOPLocations() {
            List<Location> list = new ArrayList<>();

            for (String key : yamlConfiguration.getConfigurationSection(PATH_GOPGAME).getKeys(false)) {
                String path = PATH_GOPGAME + "." + key;

                double x = yamlConfiguration.getDouble(path + ".x");
                double y = yamlConfiguration.getDouble(path + ".y");
                double z = yamlConfiguration.getDouble(path + ".z");
                int yaw = yamlConfiguration.getInt(path + ".yaw");
                int pitch = yamlConfiguration.getInt(path + ".pitch");
                list.add(new Location(worldConfig.getWorld(), x, y, z, yaw, pitch));
            }

            if (list.isEmpty())
                list.add(worldConfig.getWorld().getSpawnLocation());

            return list;
        }

        /**
         * オープニングゲームのプレイヤー待機場所の位置を取得します。
         *
         * @param i 設定されている番号
         * @return 設定されている位置
         */
        public Location getGOPLocation(int i) {
            if (i < 1) return null;

            String path = PATH_GOPGAME + ".p" + i;

            if (yamlConfiguration.contains(path)) {
                double x = yamlConfiguration.getDouble(path + ".x");
                double y = yamlConfiguration.getDouble(path + ".y");
                double z = yamlConfiguration.getDouble(path + ".z");
                int yaw = yamlConfiguration.getInt(path + ".yaw");
                int pitch = yamlConfiguration.getInt(path + ".pitch");

                return new Location(worldConfig.getWorld(), x, y, z, yaw, pitch);
            } else {
                return worldConfig.getWorld().getSpawnLocation();
            }
        }

        /**
         * オープニングゲームのプレイヤー待機場所の位置を設定します。
         *
         * @param i        設定する番号
         * @param location 設定する位置
         */
        public void setGOPLocation(int i, Location location) {
            if (i < 1) return;

            String path = PATH_GOPGAME + ".p" + i;

            yamlConfiguration.set(path + ".x", location.getBlockX() + 0.5);
            yamlConfiguration.set(path + ".y", location.getBlockY());
            yamlConfiguration.set(path + ".z", location.getBlockZ() + 0.5);
            yamlConfiguration.set(path + ".yaw", location.getYaw());
            yamlConfiguration.set(path + ".pitch", location.getPitch());
            save();
        }

        @Override
        public void save() {
            super.save();
            worldConfig.setOPGameLocationConfig(this);
        }
    }

    /**
     * 位置設定
     */
    public static class HunterLocationConfig extends IConfig {

        private final String PATH = WorldManager.PathType.LOCATION_HUNTER.getPath();

        public HunterLocationConfig(WorldConfig worldConfig) {
            super(worldConfig);
        }

        /**
         * ハンターがテレポートされる位置を取得します。
         *
         * @param i 設定されている番号
         * @return 設定されている位置
         */
        public Location getLocation(int i) {
            if (i < 1) return null;

            String path = PATH + ".p" + i;

            if (yamlConfiguration.contains(path)) {
                double x = yamlConfiguration.getDouble(path + ".x");
                double y = yamlConfiguration.getDouble(path + ".y");
                double z = yamlConfiguration.getDouble(path + ".z");
                int yaw = yamlConfiguration.getInt(path + ".yaw");
                int pitch = yamlConfiguration.getInt(path + ".pitch");

                return new Location(worldConfig.getWorld(), x, y, z, yaw, pitch);
            } else {
                return worldConfig.getWorld().getSpawnLocation();
            }
        }

        /**
         * ハンターがテレポートされる位置を設定します。
         *
         * @param i        設定する番号
         * @param location 設定する位置
         */
        public void setLocation(int i, Location location) {
            if (i < 1) return;

            String path = PATH + ".p" + i;

            yamlConfiguration.set(path + ".x", location.getBlockX() + 0.5);
            yamlConfiguration.set(path + ".y", location.getBlockY());
            yamlConfiguration.set(path + ".z", location.getBlockZ() + 0.5);
            yamlConfiguration.set(path + ".yaw", location.getYaw());
            yamlConfiguration.set(path + ".pitch", location.getPitch());
            save();
        }

        @Override
        public void save() {
            super.save();
            worldConfig.setHunterLocationConfig(this);
        }
    }

    /**
     * 位置設定
     */
    public static class JailLocationConfig extends IConfig {

        private final String PATH = WorldManager.PathType.LOCATION_JAIL.getPath();

        public JailLocationConfig(WorldConfig worldConfig) {
            super(worldConfig);
        }

        /**
         * 確保者がテレポートされる位置をすべて取得します。
         *
         * @return 設定されている位置 (ミュータブルリスト)
         */
        public List<Location> getLocations() {
            List<Location> list = new ArrayList<>();

            for (String key : yamlConfiguration.getConfigurationSection(PATH).getKeys(false)) {
                String path = PATH + "." + key;

                double x = yamlConfiguration.getDouble(path + ".x");
                double y = yamlConfiguration.getDouble(path + ".y");
                double z = yamlConfiguration.getDouble(path + ".z");
                int yaw = yamlConfiguration.getInt(path + ".yaw");
                int pitch = yamlConfiguration.getInt(path + ".pitch");
                list.add(new Location(worldConfig.getWorld(), x, y, z, yaw, pitch));
            }

            if (list.isEmpty())
                list.add(worldConfig.getWorld().getSpawnLocation());

            return list;
        }

        /**
         * 確保者がテレポートされる位置を取得します。
         *
         * @param i 設定されている番号
         * @return 設定されている位置
         */
        public Location getLocation(int i) {
            if (i < 1) return null;

            String path = PATH + ".p" + i;

            if (yamlConfiguration.contains(path)) {
                double x = yamlConfiguration.getDouble(path + ".x");
                double y = yamlConfiguration.getDouble(path + ".y");
                double z = yamlConfiguration.getDouble(path + ".z");
                int yaw = yamlConfiguration.getInt(path + ".yaw");
                int pitch = yamlConfiguration.getInt(path + ".pitch");

                return new Location(worldConfig.getWorld(), x, y, z, yaw, pitch);
            } else {
                return worldConfig.getWorld().getSpawnLocation();
            }
        }

        /**
         * 確保者がテレポートされる位置を設定します。
         *
         * @param i        設定する番号
         * @param location 設定する位置
         */
        public void setLocation(int i, Location location) {
            if (i < 1) return;

            String path = PATH + ".p" + i;

            yamlConfiguration.set(path + ".x", location.getBlockX() + 0.5);
            yamlConfiguration.set(path + ".y", location.getBlockY());
            yamlConfiguration.set(path + ".z", location.getBlockZ() + 0.5);
            yamlConfiguration.set(path + ".yaw", location.getYaw());
            yamlConfiguration.set(path + ".pitch", location.getPitch());
            save();
        }

        @Override
        public void save() {
            super.save();
            worldConfig.setJailLocationConfig(this);
        }
    }

    /**
     * 位置設定
     */
    public static class RespawnLocationConfig extends IConfig {

        private final String PATH = WorldManager.PathType.LOCATION_RESPAWN.getPath();

        public RespawnLocationConfig(WorldConfig worldConfig) {
            super(worldConfig);
        }

        /**
         * 逃走者がテレポートされる位置をすべて取得します。
         *
         * @return 設定されている位置 (ミュータブルリスト)
         */
        public List<Location> getLocations() {
            List<Location> list = new ArrayList<>();

            for (String key : yamlConfiguration.getConfigurationSection(PATH).getKeys(false)) {
                String path = PATH + "." + key;

                double x = yamlConfiguration.getDouble(path + ".x");
                double y = yamlConfiguration.getDouble(path + ".y");
                double z = yamlConfiguration.getDouble(path + ".z");
                int yaw = yamlConfiguration.getInt(path + ".yaw");
                int pitch = yamlConfiguration.getInt(path + ".pitch");

                list.add(new Location(worldConfig.getWorld(), x, y, z, yaw, pitch));
            }

            if (list.isEmpty())
                list.add(worldConfig.getWorld().getSpawnLocation());

            return list;
        }

        /**
         * 逃走者がテレポートされる位置を取得します。
         *
         * @param i 設定されている番号
         * @return 設定されている位置
         */
        public Location getLocation(int i) {
            if (i < 1) return null;

            String path = PATH + ".p" + i;

            if (yamlConfiguration.contains(path)) {
                double x = yamlConfiguration.getDouble(path + ".x");
                double y = yamlConfiguration.getDouble(path + ".y");
                double z = yamlConfiguration.getDouble(path + ".z");
                int yaw = yamlConfiguration.getInt(path + ".yaw");
                int pitch = yamlConfiguration.getInt(path + ".pitch");

                return new Location(worldConfig.getWorld(), x, y, z, yaw, pitch);
            } else {
                return worldConfig.getWorld().getSpawnLocation();
            }
        }

        /**
         * 逃走者がテレポートされる位置を設定します。
         *
         * @param i        設定する番号
         * @param location 設定する位置
         */
        public void setLocation(int i, Location location) {
            if (i < 1) return;

            String path = PATH + ".p" + i;

            yamlConfiguration.set(path + ".x", location.getBlockX() + 0.5);
            yamlConfiguration.set(path + ".y", location.getBlockY());
            yamlConfiguration.set(path + ".z", location.getBlockZ() + 0.5);
            yamlConfiguration.set(path + ".yaw", location.getYaw());
            yamlConfiguration.set(path + ".pitch", location.getPitch());
            save();
        }

        @Override
        public void save() {
            super.save();
            worldConfig.setRespawnLocationConfig(this);
        }
    }


    /**
     * ドア設定
     */
    public static class HunterDoorConfig extends IConfig {

        private final String PATH = WorldManager.PathType.DOOR_HUNTER.getPath();

        public HunterDoorConfig(WorldConfig worldConfig) {
            super(worldConfig);
        }

        /**
         * ドアをすべて開放します。
         */
        public void openHunterDoors() {
            if (!yamlConfiguration.contains(PATH)) return;

            for (String key : yamlConfiguration.getConfigurationSection(PATH).getKeys(false)) {
                String path = PATH + "." + key;

                double x = yamlConfiguration.getDouble(path + ".x");
                double y = yamlConfiguration.getDouble(path + ".y");
                double z = yamlConfiguration.getDouble(path + ".z");

                Location loc = new Location(worldConfig.getWorld(), x, y, z);
                Block block = loc.getBlock();
                if (block.getType() == Material.IRON_DOOR)
                    block.setType(Material.AIR);
            }
        }

        /**
         * ドアを開放します。
         *
         * @param i 設定されている番号
         */
        public void openHunterDoor(int i) {
            String path = PATH + ".p" + i;

            if (i < 1 || !yamlConfiguration.contains(path)) return;

            if (yamlConfiguration.contains(path)) {
                double x = yamlConfiguration.getDouble(path + ".x");
                double y = yamlConfiguration.getDouble(path + ".y");
                double z = yamlConfiguration.getDouble(path + ".z");

                Location loc = new Location(worldConfig.getWorld(), x, y, z);
                Block block = loc.getBlock();
                if (block.getType() == Material.IRON_DOOR)
                    block.setType(Material.AIR);
            }
            return;
        }

        /**
         * ドア位置をすべて取得します。
         *
         * @return 設定されている位置 (ミュータブルリスト)
         */
        public List<Location> getLocations() {
            List<Location> list = new ArrayList<>();

            if (yamlConfiguration.contains(PATH)) {
                for (String key : yamlConfiguration.getConfigurationSection(PATH).getKeys(false)) {
                    String path = PATH + "." + key;

                    double x = yamlConfiguration.getDouble(path + ".x");
                    double y = yamlConfiguration.getDouble(path + ".y");
                    double z = yamlConfiguration.getDouble(path + ".z");
                    list.add(new Location(worldConfig.getWorld(), x, y, z));
                }
            } else {
                list.add(worldConfig.getWorld().getSpawnLocation());
            }

            return list;
        }

        /**
         * ドア位置を取得します。
         *
         * @param i 設定されている番号
         * @return 設定されている位置
         */
        public Location getLocation(int i) {
            if (i < 1) return null;

            String path = PATH + ".p" + i;

            if (yamlConfiguration.contains(path)) {
                double x = yamlConfiguration.getDouble(path + ".x");
                double y = yamlConfiguration.getDouble(path + ".y");
                double z = yamlConfiguration.getDouble(path + ".z");

                return new Location(worldConfig.getWorld(), x, y, z);
            } else {
                return worldConfig.getWorld().getSpawnLocation();
            }
        }

        /**
         * ドア位置を設定します。
         *
         * @param i        設定する番号
         * @param location 設定する位置
         */
        public void setLocation(int i, Location location) {
            if (i < 1) return;

            String path = PATH + ".p" + i;

            yamlConfiguration.set(path + ".x", location.getBlockX());
            yamlConfiguration.set(path + ".y", location.getBlockY());
            yamlConfiguration.set(path + ".z", location.getBlockZ());
            save();
        }

        @Override
        public void save() {
            super.save();
            worldConfig.setHunterDoorConfig(this);
        }
    }


    /**
     * ボーダー設定
     */
    public static class MapBorderConfig extends IBorderConfig {

        private final String PATH = WorldManager.PathType.BORDER_MAP.getPath();

        public MapBorderConfig(WorldConfig worldConfig) {
            super(worldConfig);
        }

        /**
         * ハンターゾーンのボーダー位置が設定されているかを取得します。
         *
         * @param borderType 設定されている種類
         * @return 設定されているか
         */
        public boolean isLocation(BorderType borderType) {
            String path = PATH + ".p" + borderType.getPoint();
            return yamlConfiguration.contains(path);
        }

        /**
         * マップ全体のボーダー位置を取得します。
         *
         * @param borderType 設定されている種類
         * @return 設定されている位置
         */
        public Location getLocation(BorderType borderType) {
            String path = PATH + ".p" + borderType.getPoint();

            if (yamlConfiguration.contains(path)) {
                double x = yamlConfiguration.getDouble(path + ".x");
                double y = yamlConfiguration.getDouble(path + ".y");
                double z = yamlConfiguration.getDouble(path + ".z");

                return new Location(worldConfig.getWorld(), x, y, z);
            } else {
                return worldConfig.getWorld().getSpawnLocation();
            }
        }

        /**
         * マップ全体のボーダー位置を設定します。
         *
         * @param borderType 設定する種類
         * @param location   設定する位置
         */
        public void setLocation(BorderType borderType, Location location) {
            String path = PATH + ".p" + borderType.getPoint();

            yamlConfiguration.set(path + ".x", location.getBlockX());
            yamlConfiguration.set(path + ".y", location.getBlockY());
            yamlConfiguration.set(path + ".z", location.getBlockZ());
            save();
        }

        @Override
        public void save() {
            super.save();
            worldConfig.setMapBorderConfig(this);
        }
    }

    /**
     * ボーダー設定
     */
    public static class OPGameBorderConfig extends IBorderConfig {

        private final String PATH = WorldManager.PathType.BORDER_OPGAME.getPath();

        public OPGameBorderConfig(WorldConfig worldConfig) {
            super(worldConfig);
        }

        /**
         * ハンターゾーンのボーダー位置が設定されているかを取得します。
         *
         * @param borderType 設定されている種類
         * @return 設定されているか
         */
        public boolean isLocation(BorderType borderType) {
            String path = PATH + ".p" + borderType.getPoint();
            return yamlConfiguration.contains(path);
        }

        /**
         * オープニングゲームのプレイヤー待機場所のボーダー位置を取得します。
         *
         * @param borderType 設定されている種類
         * @return 設定されている位置
         */
        public Location getLocation(BorderType borderType) {
            String path = PATH + ".p" + borderType.getPoint();

            if (yamlConfiguration.contains(path)) {
                double x = yamlConfiguration.getDouble(path + ".x");
                double y = yamlConfiguration.getDouble(path + ".y");
                double z = yamlConfiguration.getDouble(path + ".z");

                return new Location(worldConfig.getWorld(), x, y, z);
            } else {
                return worldConfig.getWorld().getSpawnLocation();
            }
        }

        /**
         * オープニングゲームのプレイヤー待機場所のボーダー位置を設定します。
         *
         * @param borderType 設定する種類
         * @param location   設定する位置
         */
        public void setLocation(BorderType borderType, Location location) {
            String path = PATH + ".p" + borderType.getPoint();

            yamlConfiguration.set(path + ".x", location.getBlockX());
            yamlConfiguration.set(path + ".y", location.getBlockY());
            yamlConfiguration.set(path + ".z", location.getBlockZ());
            save();
        }

        @Override
        public void save() {
            super.save();
            worldConfig.setOPGameBorderConfig(this);
        }
    }

    /**
     * ボーダー設定
     */
    public static class HunterZoneBorderConfig extends IBorderConfig {

        private final String PATH = WorldManager.PathType.BORDER_HUNTERZONE.getPath();

        public HunterZoneBorderConfig(WorldConfig worldConfig) {
            super(worldConfig);
        }

        /**
         * ハンターゾーンのボーダー位置が設定されているかを取得します。
         *
         * @param borderType 設定されている種類
         * @return 設定されているか
         */
        public boolean isLocation(BorderType borderType) {
            String path = PATH + ".p" + borderType.getPoint();
            return yamlConfiguration.contains(path);
        }

        /**
         * ハンターゾーンのボーダー位置を取得します。
         *
         * @param borderType 設定されている種類
         * @return 設定されている位置
         */
        public Location getLocation(BorderType borderType) {
            String path = PATH + ".p" + borderType.getPoint();

            if (yamlConfiguration.contains(path)) {
                double x = yamlConfiguration.getDouble(path + ".x");
                double y = yamlConfiguration.getDouble(path + ".y");
                double z = yamlConfiguration.getDouble(path + ".z");

                return new Location(worldConfig.getWorld(), x, y, z);
            } else {
                return worldConfig.getWorld().getSpawnLocation();
            }
        }

        /**
         * ハンターゾーンのボーダー位置を設定します。
         *
         * @param borderType 設定する種類
         * @param location   設定する位置
         */
        public void setLocation(BorderType borderType, Location location) {
            String path = PATH + ".p" + borderType.getPoint();

            yamlConfiguration.set(path + ".x", location.getBlockX());
            yamlConfiguration.set(path + ".y", location.getBlockY());
            yamlConfiguration.set(path + ".z", location.getBlockZ());
            save();
        }

        @Override
        public void save() {
            super.save();
            worldConfig.setHunterZoneBorderConfig(this);
        }
    }


    /**
     * 難易度
     */
    public static class DifficultyConfig {

        private static WorldManager.Difficulty difficulty;
        private static String configPath;

        private static File file;
        private static YamlConfiguration yamlConfiguration;

        public DifficultyConfig(WorldManager.Difficulty difficulty, WorldConfig worldConfig) {
            this.difficulty = difficulty;
            this.configPath = "difficulty." + difficulty.getName() + ".";

            this.file = worldConfig.getFile();
            this.yamlConfiguration = worldConfig.getConfig();
        }

        public WorldManager.Difficulty getDifficulty() {
            return difficulty;
        }

        public String getName() {
            return yamlConfiguration.getString(configPath + "name");
        }

        // 自動復活秒数
        public boolean getHealth() {
            return yamlConfiguration.getBoolean(configPath + "health", false);
        }

        public void setHealth(boolean health) {
            yamlConfiguration.set(configPath + "health", health);
            try {
                yamlConfiguration.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // 自動復活秒数
        public int getRespawnAutoTime() {
            return yamlConfiguration.getInt(configPath + "respawn.auto", -1);
        }

        public void setRespawnAutoTime(int time) {
            yamlConfiguration.set(configPath + "respawn.auto", time);
            try {
                yamlConfiguration.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // 復活可能回数
        public int getRespawnDenyCount() {
            return yamlConfiguration.getInt(configPath + "respawn.count", 7);
        }

        public void setRespawnDenyCount(int count) {
            yamlConfiguration.set(configPath + "respawn.count", count);
            try {
                yamlConfiguration.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // 復活するまでの一回あたりのクールタイム
        public int getRespawnCoolTime() {
            return yamlConfiguration.getInt(configPath + "respawn.coolTime", 60);
        }

        public void setRespawnCoolTime(int time) {
            yamlConfiguration.set(configPath + "respawn.coolTime", time);
            try {
                yamlConfiguration.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // レート
        public int getRate() {
            return yamlConfiguration.getInt(configPath + "rate", 200);
        }

        public void setRate(int rate) {
            yamlConfiguration.set(configPath + "rate", rate);
            try {
                yamlConfiguration.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // 骨の数
        public IItem getBone(WorldManager.GameType gameType) {
            ItemType itemType = ItemType.BONE;
            String path = configPath + "item." + gameType.name().toLowerCase() + "." + itemType.getName();

            System.out.println(gameType == WorldManager.GameType.START);
            System.out.println(path);

            int defaultCount = gameType == WorldManager.GameType.START ? 8 : 5;

            return new IItem(gameType, itemType, yamlConfiguration.getInt(path + ".count", defaultCount), yamlConfiguration.getInt(path + ".duration", 10));
        }

        // 羽の数
        public IItem getFeather(WorldManager.GameType gameType) {
            ItemType itemType = ItemType.FEATHER;
            String path = configPath + "item." + gameType.name().toLowerCase() + "." + itemType.getName();

            System.out.println(gameType == WorldManager.GameType.START);
            System.out.println(path);

            int defaultCount = gameType == WorldManager.GameType.START ? 8 : 5;

            return new IItem(gameType, itemType, yamlConfiguration.getInt(path + ".count", defaultCount), yamlConfiguration.getInt(path + ".duration", 10));
        }

        // 卵の数
        public IItem getEgg(WorldManager.GameType gameType) {
            ItemType itemType = ItemType.EGG;
            String path = configPath + "item." + gameType.name().toLowerCase() + "." + itemType.getName();

            System.out.println(gameType == WorldManager.GameType.START);
            System.out.println(path);

            int defaultCount = gameType == WorldManager.GameType.START ? 8 : 5;

            return new IItem(gameType, itemType, yamlConfiguration.getInt(path + ".count", defaultCount), yamlConfiguration.getInt(path + ".duration", 10));
        }

        public static class IItem {

            private WorldManager.GameType gameType;
            private ItemType itemType;

            private int count;
            private int duration;

            public IItem(WorldManager.GameType gameType, ItemType itemType, int count, int duration) {
                this.gameType = gameType;
                this.itemType = itemType;

                this.count = count;
                this.duration = duration;
            }

            public int getCount() {
                return count;
            }

            public int getDuration() {
                return duration;
            }

            public void setCount(int count) {
                this.count = count;
                yamlConfiguration.set(configPath + "item." + gameType.name().toLowerCase() + "." + itemType.getName() + ".count", count);
                try {
                    yamlConfiguration.save(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            public void setDuration(int duration) {
                this.duration = duration;
                yamlConfiguration.set(configPath + "item." + gameType.name().toLowerCase() + "." + itemType.getName() + ".duration", duration);
                try {
                    yamlConfiguration.save(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        public enum ItemType {
            BONE,
            FEATHER,
            EGG;

            public String getName() {
                return name().toLowerCase();
            }
        }
    }


    public abstract static class IConfig {

        public WorldConfig worldConfig;

        public File file;
        public YamlConfiguration yamlConfiguration;

        public IConfig(WorldConfig worldConfig) {
            this.worldConfig = worldConfig;
            this.file = worldConfig.file;
            this.yamlConfiguration = worldConfig.yamlConfiguration;
        }

        public void save() {
            try {
                yamlConfiguration.save(file);
            } catch (IOException e) {
                Bukkit.getConsoleSender().sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "予期しないエラーが発生しました。\n" +
                        MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "位置: " + Main.PACKAGE_PATH + ".API.Manager.World.WorldConfig.IConfig (継承元・先クラス)");
                e.printStackTrace();
            }
        }
    }

    public abstract static class IBorderConfig {

        public WorldConfig worldConfig;

        public File file;
        public YamlConfiguration yamlConfiguration;

        public IBorderConfig(WorldConfig worldConfig) {
            this.worldConfig = worldConfig;
            this.file = worldConfig.file;
            this.yamlConfiguration = worldConfig.yamlConfiguration;
        }

        public abstract boolean isLocation(BorderType borderType);

        public abstract Location getLocation(BorderType borderType);

        public abstract void setLocation(BorderType borderType, Location location);

        public void save() {
            try {
                yamlConfiguration.save(file);
            } catch (IOException e) {
                Bukkit.getConsoleSender().sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "予期しないエラーが発生しました。\n" +
                        MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "位置: " + Main.PACKAGE_PATH + ".API.Manager.World.WorldConfig.IBorderConfig (継承元・先クラス)");
                e.printStackTrace();
            }
        }
    }


    // ボーダータイプ
    public enum BorderType {
        POINT_1,
        POINT_2;

        public int getPoint() {
            return ordinal() + 1;
        }
    }
}

package jp.aoichaan0513.A_TosoGame_Live.API.Manager.World;

import jp.aoichaan0513.A_TosoGame_Live.API.Manager.GameManager;
import jp.aoichaan0513.A_TosoGame_Live.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;

public class WorldManager {

    public static World getWorld() {
        return Bukkit.getWorld(getWorldName());
    }

    public static String getWorldName() {
        return Main.getMainConfig().getString("worldName");
    }

    public static void setWorld(World world) {
        if (GameManager.isGame()) return;
        Main.getMainConfig().set("worldName", world.getName());
        Main.setMainConfig();
        Main.getInstance().reloadConfig();
        return;
    }

    public static void setWorld(String world) {
        if (GameManager.isGame()) return;
        Main.getMainConfig().set("worldName", world);
        Main.setMainConfig();
        Main.getInstance().reloadConfig();
        return;
    }

    public static void setWorld(World world, boolean bool) {
        if (GameManager.isGame()) return;
        if (bool) {
            Main.getMainConfig().set("worldName", world.getName());
            Main.setMainConfig();
            return;
        } else {
            setWorld(world);
            return;
        }
    }

    public static void setWorld(String world, boolean bool) {
        if (GameManager.isGame()) return;
        if (bool) {
            Main.getMainConfig().set("worldName", world);
            Main.setMainConfig();
            return;
        } else {
            setWorld(world);
            return;
        }
    }

    public enum Difficulty {
        EASY(ChatColor.GREEN + "イージー"),
        NORMAL(ChatColor.YELLOW + "ノーマル"),
        HARD(ChatColor.RED + "ハード"),
        HARDCORE(ChatColor.DARK_RED + "ハードコア");

        private final String displayName;

        private Difficulty(String displayName) {
            this.displayName = displayName;
        }

        public String getName() {
            return name().toLowerCase();
        }

        public String getDisplayName() {
            return displayName;
        }

        public static Difficulty getDifficulty(String name) {
            return valueOf(name.toUpperCase());
        }
    }

    // ゲームタイプ
    public enum GameType {
        START,
        RESPAWN
    }


    public enum PathType {
        MAP("map"),
        GAME_TIME("game.time"),
        GAME_OTHER("game.other"),

        OPGAME_DICE("opgame.dice"),

        LOCATION_OPGAME("location.opgame"),
        LOCATION_GOPGAME("location.gopgame"),
        LOCATION_HUNTER("location.hunter"),
        LOCATION_JAIL("location.jail"),
        LOCATION_RESPAWN("location.respawn"),

        DOOR_HUNTER("door.hunter"),

        BORDER_MAP("border.map"),
        BORDER_OPGAME("border.opgame"),
        BORDER_HUNTERZONE("border.hunterzone");

        private final String path;

        private PathType(String path) {
            this.path = path;
        }

        public String getPath() {
            return path;
        }
    }
}

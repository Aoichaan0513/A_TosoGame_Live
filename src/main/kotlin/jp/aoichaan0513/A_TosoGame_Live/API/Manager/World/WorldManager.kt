package jp.aoichaan0513.A_TosoGame_Live.API.Manager.World

import jp.aoichaan0513.A_TosoGame_Live.API.Manager.GameManager
import jp.aoichaan0513.A_TosoGame_Live.Main
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.World

class WorldManager {
    companion object {

        var world: World
            get() = Bukkit.getWorld(worldName)!!
            set(world) {
                if (GameManager.isGame()) return

                Main.mainConfig.set("worldName", world.name)
                Main.setMainConfig()
                Main.pluginInstance.reloadConfig()
                return
            }

        val worldName: String
            get() = Main.mainConfig.getString("worldName")!!

        fun setWorld(world: String) {
            if (GameManager.isGame()) return

            Main.mainConfig.set("worldName", world)
            Main.setMainConfig()
            Main.pluginInstance.reloadConfig()
            return
        }
    }

    enum class Difficulty(val id: Int, val displayName: String, val color: ChatColor) {
        EASY(0, "${ChatColor.GREEN}イージー", ChatColor.GREEN),
        NORMAL(1, "${ChatColor.YELLOW}ノーマル", ChatColor.YELLOW),
        HARD(2, "${ChatColor.RED}ハード", ChatColor.RED),
        HARDCORE(3, "${ChatColor.DARK_RED}ハードコア", ChatColor.DARK_RED);

        val difficultyName
            get() = name.toLowerCase()

        companion object {

            fun getDifficulty(id: Int): Difficulty {
                return values().firstOrNull { it.id == id } ?: NORMAL
            }

            fun getDifficulty(name: String): Difficulty {
                return values().firstOrNull { it.name.toUpperCase().equals(name.toUpperCase()) } ?: NORMAL
            }
        }
    }

    // ゲームタイプ
    enum class GameType {
        START,
        RESPAWN
    }

    enum class PathType(val path: String) {
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
    }
}
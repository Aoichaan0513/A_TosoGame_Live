package jp.aoichaan0513.A_TosoGame_Live.API.Manager.World

import jp.aoichaan0513.A_TosoGame_Live.API.Enums.ItemType
import jp.aoichaan0513.A_TosoGame_Live.API.Interfaces.IConfig
import jp.aoichaan0513.A_TosoGame_Live.API.Interfaces.IDoor
import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI
import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI.PrefixType
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.Player.DifficultyManager
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.World.WorldManager.GameType
import jp.aoichaan0513.A_TosoGame_Live.Main
import jp.aoichaan0513.A_TosoGame_Live.Utils.ParseUtil
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.block.Block
import org.bukkit.block.BlockFace
import org.bukkit.block.data.Bisected
import org.bukkit.block.data.type.Door
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*


class WorldConfig(val world: World) {

    var file: File
    var config: YamlConfiguration

    var mapConfig: MapConfig
        private set
    var gameConfig: GameConfig
        private set

    /**
     * オープニングゲーム設定
     */
    var opGameConfig: OPGameConfig
        private set

    /**
     * 位置設定
     */
    var opGameLocationConfig: OPGameLocationConfig
        private set
    var hunterLocationConfig: HunterLocationConfig
        private set
    var jailLocationConfig: JailLocationConfig
        private set
    var respawnLocationConfig: RespawnLocationConfig
        private set
    var hunterDoorConfig: HunterDoorConfig
        private set
    var mapBorderConfig: MapBorderConfig
        private set

    /**
     * ボーダー設定
     */
    var opGameBorderConfig: OPGameBorderConfig
        private set
    var hunterZoneBorderConfig: HunterZoneBorderConfig
        private set

    private val difficultyConfigMap = mutableMapOf<WorldManager.Difficulty, DifficultyConfig>()

    init {
        val fileName = "map.yml"

        file = File("${world.name}${Main.FILE_SEPARATOR}$fileName")
        if (file.exists()) {
            config = YamlConfiguration.loadConfiguration(file)
        } else {
            try {
                Files.copy(Main.pluginInstance.getResource(fileName), Paths.get("${world.name}${Main.FILE_SEPARATOR}$fileName"))
            } catch (e: IOException) {
                e.printStackTrace()
            }
            config = YamlConfiguration.loadConfiguration(file)
        }

        mapConfig = MapConfig(file, config)
        gameConfig = GameConfig(file, config)
        opGameConfig = OPGameConfig(file, config)
        opGameLocationConfig = OPGameLocationConfig(file, config)
        hunterLocationConfig = HunterLocationConfig(file, config)
        jailLocationConfig = JailLocationConfig(file, config)
        respawnLocationConfig = RespawnLocationConfig(file, config)
        hunterDoorConfig = HunterDoorConfig(file, config)
        mapBorderConfig = MapBorderConfig(file, config)
        opGameBorderConfig = OPGameBorderConfig(file, config)
        hunterZoneBorderConfig = HunterZoneBorderConfig(file, config)

        for (name in config.getConfigurationSection("difficulty")!!.getKeys(false)) {
            val difficulty = WorldManager.Difficulty.getDifficulty(name)
            difficultyConfigMap[difficulty] = DifficultyConfig(difficulty)
        }
    }


    /**
     * 難易度
     */
    fun getDifficultyConfig(difficulty: WorldManager.Difficulty): DifficultyConfig {
        if (!difficultyConfigMap.containsKey(difficulty))
            difficultyConfigMap[difficulty] = DifficultyConfig(difficulty)
        return difficultyConfigMap[difficulty]!!
    }

    fun getDifficultyConfig(uuid: UUID): DifficultyConfig {
        if (!DifficultyManager.isDifficulty(uuid))
            DifficultyManager.setDifficulty(uuid)
        val difficulty = DifficultyManager.getDifficulty(uuid)
        return difficultyConfigMap[difficulty]!!
    }

    fun getDifficultyConfig(p: Player): DifficultyConfig {
        return getDifficultyConfig(p.uniqueId)
    }

    /* ================================================== */

    /**
     * マップの基本設定
     */
    inner class MapConfig(f: File, c: YamlConfiguration) : IConfig(f, c) {
        private val PATH = WorldManager.PathType.MAP.path

        val name: String
            get() = c.getString("$PATH.name", "未設定")!!

        val description: String
            get() = c.getString("$PATH.description", "未設定")!!

        val version: Double
            get() = c.getDouble("$PATH.version", 1.0)

        val authors: String
            get() {
                val path = "$PATH.authors"
                return if (c.contains(path) && c.getStringList(path).isNotEmpty()) c.getStringList(path).toString() else "未設定"
            }

        val icon: Material
            get() = Material.matchMaterial(c.getString("$PATH.icon", "grass_block")!!) ?: Material.GRASS_BLOCK
    }

    /**
     * ゲームの基本設定
     */
    inner class GameConfig(f: File, c: YamlConfiguration) : IConfig(f, c) {
        private val PATH_TIME = WorldManager.PathType.GAME_TIME.path
        private val PATH_OTHER = WorldManager.PathType.GAME_TIME.path

        var countDown: Int
            get() = c.getInt("$PATH_TIME.countdown", 15)
            set(countdown) {
                c["$PATH_TIME.countdown"] = countdown
                save()
            }

        var game: Int
            get() = c.getInt("$PATH_TIME.game", 1200)
            set(game) {
                c["$PATH_TIME.game"] = game
                save()
            }

        var respawnDeny: Int
            get() = c.getInt("$PATH_TIME.respawn", 240)
            set(respawnDeny) {
                c["$PATH_TIME.respawn"] = respawnDeny
                save()
            }

        var script: Boolean
            get() = c.getBoolean("$PATH_OTHER.script", false)
            set(script) {
                c["$PATH_OTHER.script"] = script
                save()
            }

        var successMission: Boolean
            get() = c.getBoolean("$PATH_OTHER.success", true)
            set(success) {
                c["$PATH_OTHER.success"] = success
                save()
            }

        var jump: Boolean
            get() = c.getBoolean("$PATH_OTHER.dashjump", true)
            set(jump) {
                c["$PATH_OTHER.dashjump"] = jump
                save()
            }

        override fun save() {
            super.save()
            file = f
            config = c
            gameConfig = this
        }
    }

    /**
     * オープニングゲーム設定
     */
    inner class OPGameConfig(f: File, c: YamlConfiguration) : IConfig(f, c) {
        private val PATH_DICE = WorldManager.PathType.OPGAME_DICE.path

        /**
         * サイコロミッションの合計数を取得します。
         *
         * @return 設定されている合計数 / 設定されていない場合は30
         */
        /**
         * サイコロミッションの合計数を設定します。
         *
         * @param diceCount 設定する合計数
         */
        var diceCount: Int
            get() = c.getInt("$PATH_DICE.count", 30)
            set(diceCount) {
                c["$PATH_DICE.count"] = diceCount
                save()
            }

        override fun save() {
            super.save()
            file = f
            config = c
            opGameConfig = this
        }
    }

    /**
     * 位置設定
     */
    inner class OPGameLocationConfig(f: File, c: YamlConfiguration) : IConfig(f, c) {
        private val PATH_OPGAME = WorldManager.PathType.LOCATION_OPGAME.path
        private val PATH_GOPGAME = WorldManager.PathType.LOCATION_GOPGAME.path

        var opLocation: Location
            get() {
                val x = c.getDouble("$PATH_OPGAME.x")
                val y = c.getDouble("$PATH_OPGAME.y")
                val z = c.getDouble("$PATH_OPGAME.z")
                val yaw = c.getInt("$PATH_OPGAME.yaw")
                val pitch = c.getInt("$PATH_OPGAME.pitch")
                return Location(world, x, y, z, yaw.toFloat(), pitch.toFloat())
            }
            set(location) {
                c["$PATH_OPGAME.x"] = location.blockX + 0.5
                c["$PATH_OPGAME.y"] = location.blockY
                c["$PATH_OPGAME.z"] = location.blockZ + 0.5
                c["$PATH_OPGAME.yaw"] = location.yaw
                c["$PATH_OPGAME.pitch"] = location.pitch
                save()
            }

        val gOPLocations: Map<Int, Location>
            get() {
                val hashMap = mutableMapOf<Int, Location>()
                if (c.contains(PATH_GOPGAME)) {
                    for (key in c.getConfigurationSection(PATH_GOPGAME)!!.getKeys(false)) {
                        if (!ParseUtil.isInt(key.substring(1))) continue

                        val path = "$PATH_GOPGAME.$key"
                        val x = c.getDouble("$path.x")
                        val y = c.getDouble("$path.y")
                        val z = c.getDouble("$path.z")
                        val yaw = c.getInt("$path.yaw")
                        val pitch = c.getInt("$path.pitch")
                        hashMap[key.substring(1).toInt()] = Location(world, x, y, z, yaw.toFloat(), pitch.toFloat())
                    }
                } else {
                    hashMap[0] = world.spawnLocation
                }
                return hashMap
            }

        /**
         * オープニングゲームのプレイヤー待機場所の位置を取得します。
         *
         * @param i 設定されている番号
         * @return 設定されている位置
         */
        fun getGOPLocation(i: Int): Location {
            val path = "$PATH_GOPGAME.p$i"
            return if (i > 0 && c.contains(path)) {
                val x = c.getDouble("$path.x")
                val y = c.getDouble("$path.y")
                val z = c.getDouble("$path.z")
                val yaw = c.getInt("$path.yaw")
                val pitch = c.getInt("$path.pitch")
                Location(world, x, y, z, yaw.toFloat(), pitch.toFloat())
            } else {
                world.spawnLocation
            }
        }

        /**
         * オープニングゲームのプレイヤー待機場所の位置を設定します。
         *
         * @param i        設定する番号
         * @param location 設定する位置
         */
        fun setGOPLocation(i: Int, location: Location) {
            if (i < 1) return

            val path = "$PATH_GOPGAME.p$i"
            c["$path.x"] = location.blockX + 0.5
            c["$path.y"] = location.blockY
            c["$path.z"] = location.blockZ + 0.5
            c["$path.yaw"] = location.yaw
            c["$path.pitch"] = location.pitch
            save()
        }

        override fun save() {
            super.save()
            file = f
            config = c
            opGameLocationConfig = this
        }
    }

    /**
     * 位置設定
     */
    inner class HunterLocationConfig(f: File, c: YamlConfiguration) : IConfig(f, c) {
        private val PATH = WorldManager.PathType.LOCATION_HUNTER.path

        /**
         * ハンターがテレポートされる位置を取得します。
         *
         * @param i 設定されている番号
         * @return 設定されている位置
         */
        fun getLocation(i: Int): Location {
            val path = "$PATH.p$i"

            return if (i > 0 && c.contains(path)) {
                val x = c.getDouble("$path.x")
                val y = c.getDouble("$path.y")
                val z = c.getDouble("$path.z")
                val yaw = c.getInt("$path.yaw")
                val pitch = c.getInt("$path.pitch")
                Location(world, x, y, z, yaw.toFloat(), pitch.toFloat())
            } else {
                world.spawnLocation
            }
        }

        /**
         * ハンターがテレポートされる位置を設定します。
         *
         * @param i        設定する番号
         * @param location 設定する位置
         */
        fun setLocation(i: Int, location: Location) {
            if (i < 1) return

            val path = "$PATH.p$i"
            c["$path.x"] = location.blockX + 0.5
            c["$path.y"] = location.blockY
            c["$path.z"] = location.blockZ + 0.5
            c["$path.yaw"] = location.yaw
            c["$path.pitch"] = location.pitch
            save()
        }

        override fun save() {
            super.save()
            file = f
            config = c
            hunterLocationConfig = this
        }
    }

    /**
     * 位置設定
     */
    inner class JailLocationConfig(f: File, c: YamlConfiguration) : IConfig(f, c) {
        private val PATH = WorldManager.PathType.LOCATION_JAIL.path

        /**
         * 確保者がテレポートされる位置をすべて取得します。
         *
         * @return 設定されている位置 (ミュータブルリスト)
         */
        val locations: Map<Int, Location>
            get() {
                val hashMap = mutableMapOf<Int, Location>()
                if (c.contains(PATH)) {
                    for (key in c.getConfigurationSection(PATH)!!.getKeys(false)) {
                        if (!ParseUtil.isInt(key.substring(1))) continue
                        val path = "$PATH.$key"
                        val x = c.getDouble("$path.x")
                        val y = c.getDouble("$path.y")
                        val z = c.getDouble("$path.z")
                        val yaw = c.getInt("$path.yaw")
                        val pitch = c.getInt("$path.pitch")
                        hashMap[key.substring(1).toInt()] = Location(world, x, y, z, yaw.toFloat(), pitch.toFloat())
                    }
                } else {
                    hashMap[0] = world.spawnLocation
                }
                return hashMap
            }

        /**
         * 確保者がテレポートされる位置を取得します。
         *
         * @param i 設定されている番号
         * @return 設定されている位置
         */
        fun getLocation(i: Int): Location {
            val path = "$PATH.p$i"

            return if (i > 0 && c.contains(path)) {
                val x = c.getDouble("$path.x")
                val y = c.getDouble("$path.y")
                val z = c.getDouble("$path.z")
                val yaw = c.getInt("$path.yaw")
                val pitch = c.getInt("$path.pitch")
                Location(world, x, y, z, yaw.toFloat(), pitch.toFloat())
            } else {
                world.spawnLocation
            }
        }

        /**
         * 確保者がテレポートされる位置を設定します。
         *
         * @param i        設定する番号
         * @param location 設定する位置
         */
        fun setLocation(i: Int, location: Location) {
            if (i < 1) return
            val path = "$PATH.p$i"
            c["$path.x"] = location.blockX + 0.5
            c["$path.y"] = location.blockY
            c["$path.z"] = location.blockZ + 0.5
            c["$path.yaw"] = location.yaw
            c["$path.pitch"] = location.pitch
            save()
        }

        override fun save() {
            super.save()
            file = f
            config = c
            jailLocationConfig = this
        }
    }

    /**
     * 位置設定
     */
    inner class RespawnLocationConfig(f: File, c: YamlConfiguration) : IConfig(f, c) {
        private val PATH = WorldManager.PathType.LOCATION_RESPAWN.path

        /**
         * 逃走者がテレポートされる位置をすべて取得します。
         *
         * @return 設定されている位置 (ミュータブルリスト)
         */
        val locations: Map<Int, Location>
            get() {
                val hashMap = mutableMapOf<Int, Location>()
                if (c.contains(PATH)) {
                    for (key in c.getConfigurationSection(PATH)!!.getKeys(false)) {
                        if (!ParseUtil.isInt(key.substring(1))) continue
                        val path = "$PATH.$key"
                        val x = c.getDouble("$path.x")
                        val y = c.getDouble("$path.y")
                        val z = c.getDouble("$path.z")
                        val yaw = c.getInt("$path.yaw")
                        val pitch = c.getInt("$path.pitch")
                        hashMap[key.substring(1).toInt()] = Location(world, x, y, z, yaw.toFloat(), pitch.toFloat())
                    }
                } else {
                    hashMap[0] = world.spawnLocation
                }
                return hashMap
            }

        /**
         * 逃走者がテレポートされる位置を取得します。
         *
         * @param i 設定されている番号
         * @return 設定されている位置
         */
        fun getLocation(i: Int): Location {
            val path = "$PATH.p$i"
            return if (i > 0 && c.contains(path)) {
                val x = c.getDouble("$path.x")
                val y = c.getDouble("$path.y")
                val z = c.getDouble("$path.z")
                val yaw = c.getInt("$path.yaw")
                val pitch = c.getInt("$path.pitch")
                Location(world, x, y, z, yaw.toFloat(), pitch.toFloat())
            } else {
                world.spawnLocation
            }
        }

        /**
         * 逃走者がテレポートされる位置を設定します。
         *
         * @param i        設定する番号
         * @param location 設定する位置
         */
        fun setLocation(i: Int, location: Location) {
            if (i < 1) return

            val path = "$PATH.p$i"
            c["$path.x"] = location.blockX + 0.5
            c["$path.y"] = location.blockY
            c["$path.z"] = location.blockZ + 0.5
            c["$path.yaw"] = location.yaw
            c["$path.pitch"] = location.pitch
            save()
        }

        override fun save() {
            super.save()
            file = f
            config = c
            respawnLocationConfig = this
        }
    }

    /**
     * ドア設定
     */
    inner class HunterDoorConfig(f: File, c: YamlConfiguration) : IConfig(f, c) {
        private val PATH = WorldManager.PathType.DOOR_HUNTER.path

        /**
         * ドアをすべて開放します。
         */
        fun openHunterDoors() {
            if (!c.contains(PATH)) return
            for (key in c.getConfigurationSection(PATH)!!.getKeys(false)) {
                val path = "$PATH.$key"

                val x = c.getInt("$path.location.x")
                val y = c.getInt("$path.location.y")
                val z = c.getInt("$path.location.z")

                val block = world.getBlockAt(x, y, z)
                block.type = Material.AIR
            }
        }

        /**
         * ドアを開放します。
         *
         * @param i 設定されている番号
         */
        fun openHunterDoor(i: Int) {
            val path = "$PATH.p$i"

            if (i < 1 || !c.contains(path)) return
            if (c.contains(path)) {
                val x = c.getInt("$path.location.x")
                val y = c.getInt("$path.location.y")
                val z = c.getInt("$path.location.z")

                val block = world.getBlockAt(x, y, z)
                block.type = Material.AIR
            }
            return
        }

        fun closeHunterDoors() {
            if (!c.contains(PATH)) return
            for (key in c.getConfigurationSection(PATH)!!.getKeys(false)) {
                val path = "$PATH.$key"

                val door = c.getString("$path.door")!!

                val x = c.getInt("$path.location.x")
                val y = c.getInt("$path.location.y")
                val z = c.getInt("$path.location.z")

                val blockData = Bukkit.createBlockData(door) as Door

                val blockBottom = world.getBlockAt(x, y, z)
                blockData.half = Bisected.Half.BOTTOM
                blockBottom.setBlockData(blockData, false)

                val blockTop = blockBottom.getRelative(BlockFace.UP)
                blockData.half = Bisected.Half.TOP
                blockTop.setBlockData(blockData, false)
            }
        }

        fun closeHunterDoor(i: Int) {
            val path = "$PATH.p$i"

            if (i < 1 || !c.contains(path)) return
            if (c.contains(path)) {
                val door = c.getString("$path.door")!!

                val x = c.getInt("$path.location.x")
                val y = c.getInt("$path.location.y")
                val z = c.getInt("$path.location.z")

                val blockData = Bukkit.createBlockData(door) as Door

                val blockBottom = world.getBlockAt(x, y, z)
                blockData.half = Bisected.Half.BOTTOM
                blockBottom.setBlockData(blockData, false)

                val blockTop = blockBottom.getRelative(BlockFace.UP)
                blockData.half = Bisected.Half.TOP
                blockTop.setBlockData(blockData, false)
            }
            return
        }

        /**
         * ドア位置をすべて取得します。
         *
         * @return 設定されている位置 (ミュータブルリスト)
         */
        val doors: Map<Int, IDoor>
            get() {
                val hashMap = mutableMapOf<Int, IDoor>()
                if (c.contains(PATH)) {
                    for (key in c.getConfigurationSection(PATH)!!.getKeys(false)) {
                        if (!ParseUtil.isInt(key.substring(1))) continue
                        val path = "$PATH.$key"
                        val door = c.getString("$path.door")!!

                        val x = c.getDouble("$path.location.x")
                        val y = c.getDouble("$path.location.y")
                        val z = c.getDouble("$path.location.z")
                        hashMap[key.substring(1).toInt()] = IDoor(Bukkit.createBlockData(door), Location(world, x, y, z))
                    }
                }
                return hashMap
            }

        /**
         * ドア位置を取得します。
         *
         * @param i 設定されている番号
         * @return 設定されている位置
         */
        fun getDoor(i: Int): IDoor? {
            val path = "$PATH.p$i"

            return if (i > 0 && c.contains(path)) {
                val door = c.getString("$path.door")!!

                val x = c.getDouble("$path.location.x")
                val y = c.getDouble("$path.location.y")
                val z = c.getDouble("$path.location.z")
                IDoor(Bukkit.createBlockData(door), Location(world, x, y, z))
            } else {
                null
            }
        }

        /**
         * ドア位置を設定します。
         *
         * @param i        設定する番号
         * @param location 設定する位置
         */
        fun setDoor(i: Int, block: Block) {
            if (i < 1 || block.type != Material.IRON_DOOR || (block.blockData as Door).half != Bisected.Half.BOTTOM) return

            val path = "$PATH.p$i"
            c["$path.door"] = block.blockData.asString

            c["$path.location.x"] = block.x
            c["$path.location.y"] = block.y
            c["$path.location.z"] = block.z
            save()
        }

        override fun save() {
            super.save()
            file = f
            config = c
            hunterDoorConfig = this
        }
    }

    /**
     * ボーダー設定
     */
    inner class MapBorderConfig(f: File, c: YamlConfiguration) : IBorderConfig(f, c) {
        private val PATH = WorldManager.PathType.BORDER_MAP.path

        /**
         * ハンターゾーンのボーダー位置が設定されているかを取得します。
         *
         * @param borderType 設定されている種類
         * @return 設定されているか
         */
        override fun isLocation(borderType: BorderType): Boolean {
            val path = "$PATH.p${borderType.point}"
            return c.contains(path)
        }

        /**
         * マップ全体のボーダー位置を取得します。
         *
         * @param borderType 設定されている種類
         * @return 設定されている位置
         */
        override fun getLocation(borderType: BorderType): Location {
            val path = "$PATH.p${borderType.point}"
            return if (c.contains(path)) {
                val x = c.getDouble("$path.x")
                val y = c.getDouble("$path.y")
                val z = c.getDouble("$path.z")
                Location(world, x, y, z)
            } else {
                world.spawnLocation
            }
        }

        /**
         * マップ全体のボーダー位置を設定します。
         *
         * @param borderType 設定する種類
         * @param location   設定する位置
         */
        override fun setLocation(borderType: BorderType, location: Location) {
            val path = "$PATH.p${borderType.point}"
            c["$path.x"] = location.blockX
            c["$path.y"] = location.blockY
            c["$path.z"] = location.blockZ
            save()
        }

        override fun save() {
            super.save()
            file = f
            config = c
            mapBorderConfig = this
        }
    }

    /**
     * ボーダー設定
     */
    inner class OPGameBorderConfig(f: File, c: YamlConfiguration) : IBorderConfig(f, c) {
        private val PATH = WorldManager.PathType.BORDER_OPGAME.path

        /**
         * ハンターゾーンのボーダー位置が設定されているかを取得します。
         *
         * @param borderType 設定されている種類
         * @return 設定されているか
         */
        override fun isLocation(borderType: BorderType): Boolean {
            val path = "$PATH.p${borderType.point}"
            return c.contains(path)
        }

        /**
         * オープニングゲームのプレイヤー待機場所のボーダー位置を取得します。
         *
         * @param borderType 設定されている種類
         * @return 設定されている位置
         */
        override fun getLocation(borderType: BorderType): Location {
            val path = "$PATH.p${borderType.point}"
            return if (c.contains(path)) {
                val x = c.getDouble("$path.x")
                val y = c.getDouble("$path.y")
                val z = c.getDouble("$path.z")
                Location(world, x, y, z)
            } else {
                world.spawnLocation
            }
        }

        /**
         * オープニングゲームのプレイヤー待機場所のボーダー位置を設定します。
         *
         * @param borderType 設定する種類
         * @param location   設定する位置
         */
        override fun setLocation(borderType: BorderType, location: Location) {
            val path = "$PATH.p${borderType.point}"
            c["$path.x"] = location.blockX
            c["$path.y"] = location.blockY
            c["$path.z"] = location.blockZ
            save()
        }

        override fun save() {
            super.save()
            file = f
            config = c
            opGameBorderConfig = this
        }
    }

    /**
     * ボーダー設定
     */
    inner class HunterZoneBorderConfig(f: File, c: YamlConfiguration) : IBorderConfig(f, c) {

        private val PATH = WorldManager.PathType.BORDER_HUNTERZONE.path

        /**
         * ハンターゾーンのボーダー位置が設定されているかを取得します。
         *
         * @param borderType 設定されている種類
         * @return 設定されているか
         */
        override fun isLocation(borderType: BorderType): Boolean {
            val path = "$PATH.p${borderType.point}"
            return c.contains(path)
        }

        /**
         * ハンターゾーンのボーダー位置を取得します。
         *
         * @param borderType 設定されている種類
         * @return 設定されている位置
         */
        override fun getLocation(borderType: BorderType): Location {
            val path = "$PATH.p${borderType.point}"
            return if (c.contains(path)) {
                val x = c.getDouble("$path.x")
                val y = c.getDouble("$path.y")
                val z = c.getDouble("$path.z")
                Location(world, x, y, z)
            } else {
                world.spawnLocation
            }
        }

        /**
         * ハンターゾーンのボーダー位置を設定します。
         *
         * @param borderType 設定する種類
         * @param location   設定する位置
         */
        override fun setLocation(borderType: BorderType, location: Location) {
            val path = "$PATH.p${borderType.point}"
            c["$path.x"] = location.blockX
            c["$path.y"] = location.blockY
            c["$path.z"] = location.blockZ
            save()
        }

        override fun save() {
            super.save()
            file = f
            config = c
            hunterZoneBorderConfig = this
        }
    }

    /**
     * 難易度
     */
    inner class DifficultyConfig(val difficulty: WorldManager.Difficulty) {

        private val configPath: String
        private val f: File
        private val c: YamlConfiguration

        init {
            configPath = "difficulty.${difficulty.difficultyName}."
            f = file
            c = config
        }

        val name: String
            get() = c.getString("${configPath}name")!!

        // 自動復活秒数
        var health: Boolean
            get() = c.getBoolean("${configPath}health", false)
            set(health) {
                c["${configPath}health"] = health
                save()
            }

        // 復活可能回数
        var respawnDenyCount: Int
            get() = c.getInt("${configPath}respawn.count", 7)
            set(count) {
                c["${configPath}respawn.count"] = count
                save()
            }

        // 自動復活秒数
        var respawnAutoTime: Int
            get() = c.getInt("${configPath}respawn.autoTime", -1)
            set(time) {
                c["${configPath}respawn.autoTime"] = time
                save()
            }

        // 復活するまでの一回あたりのクールタイム
        var respawnCoolTime: Int
            get() = c.getInt("${configPath}respawn.coolTime", 60)
            set(time) {
                c["${configPath}respawn.coolTime"] = time
                save()
            }

        // レート
        var rate: Int
            get() = c.getInt("${configPath}rate", 200)
            set(rate) {
                c["${configPath}rate"] = rate
                save()
            }

        // 骨の数
        fun getBone(gameType: GameType): IItem {
            val itemType = ItemType.BONE
            val path = "${configPath}item.${gameType.name.toLowerCase()}.${itemType.itemName}"
            val defaultCount = if (gameType == GameType.START) 8 else 5
            return IItem(difficulty, gameType, itemType, c.getInt("$path.count", defaultCount), c.getInt("$path.duration", 10))
        }

        // 羽の数
        fun getFeather(gameType: GameType): IItem {
            val itemType = ItemType.FEATHER
            val path = "${configPath}item.${gameType.name.toLowerCase()}.${itemType.itemName}"
            val defaultCount = if (gameType == GameType.START) 8 else 5
            return IItem(difficulty, gameType, itemType, c.getInt("$path.count", defaultCount), c.getInt("$path.duration", 10))
        }

        // 卵の数
        fun getEgg(gameType: GameType): IItem {
            val itemType = ItemType.EGG
            val path = "${configPath}item.${gameType.name.toLowerCase()}.${itemType.itemName}"
            val defaultCount = if (gameType == GameType.START) 8 else 5
            return IItem(difficulty, gameType, itemType, c.getInt("$path.count", defaultCount), 0)
        }

        fun save() {
            try {
                config.save(file)
                file = f
                config = c
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        inner class IItem(difficulty: WorldManager.Difficulty, private val gameType: GameType, private val itemType: ItemType, count: Int, duration: Int) {

            private val configPath: String = "difficulty.${difficulty.difficultyName}."

            var count: Int = 0
                set(value) {
                    field = value
                    config["${configPath}item.${gameType.name.toLowerCase()}.${itemType.itemName}.count"] = value
                    try {
                        config.save(file)
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }

            var duration: Int = 0
                set(value) {
                    if (itemType == ItemType.EGG) return

                    field = value
                    config["${configPath}item.${gameType.name.toLowerCase()}.${itemType.itemName}.duration"] = value
                    try {
                        config.save(file)
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }

            init {
                this.count = count
                this.duration = duration
            }
        }
    }

    abstract class IBorderConfig(var f: File, var c: YamlConfiguration) {

        abstract fun isLocation(borderType: BorderType): Boolean
        abstract fun getLocation(borderType: BorderType): Location
        abstract fun setLocation(borderType: BorderType, location: Location)

        open fun save() {
            try {
                c.save(f)
            } catch (e: IOException) {
                Bukkit.getConsoleSender().sendMessage("""
                    ${MainAPI.getPrefix(PrefixType.ERROR)}予期しないエラーが発生しました。
                    ${MainAPI.getPrefix(PrefixType.SECONDARY)}位置: ${Main.PACKAGE_PATH}.API.Manager.World.WorldConfig.IBorderConfig (継承元・先クラス)
                    """.trimIndent())
                e.printStackTrace()
            }
        }
    }

    // ボーダータイプ
    enum class BorderType {
        POINT_1,
        POINT_2;

        val point: Int
            get() = ordinal + 1
    }
}
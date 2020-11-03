package jp.aoichaan0513.A_TosoGame_Live

import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.ActionBarManager
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.BossBarManager
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.GameManager
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.Player.DifficultyManager
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.Player.PlayerManager
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.Player.VisibilityManager
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.World.WorldConfig
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.World.WorldManager
import jp.aoichaan0513.A_TosoGame_Live.API.Map.MapUtility
import jp.aoichaan0513.A_TosoGame_Live.API.Scoreboard.Scoreboard
import jp.aoichaan0513.A_TosoGame_Live.API.Scoreboard.Teams
import jp.aoichaan0513.A_TosoGame_Live.API.TosoGameAPI
import jp.aoichaan0513.A_TosoGame_Live.Commands.Command.*
import jp.aoichaan0513.A_TosoGame_Live.Commands.Command.GameMode
import jp.aoichaan0513.A_TosoGame_Live.Commands.Command.Location
import jp.aoichaan0513.A_TosoGame_Live.Commands.Command.Map
import jp.aoichaan0513.A_TosoGame_Live.Commands.ICommand
import jp.aoichaan0513.A_TosoGame_Live.Listeners.Discord.onMessage
import jp.aoichaan0513.A_TosoGame_Live.Listeners.Minecraft.*
import jp.aoichaan0513.A_TosoGame_Live.Mission.MissionManager
import jp.aoichaan0513.A_TosoGame_Live.Runnable.RespawnRunnable
import jp.aoichaan0513.A_TosoGame_Live.Utils.DateTime.TimeFormat
import jp.aoichaan0513.A_TosoGame_Live.Utils.HttpConnection
import jp.aoichaan0513.A_TosoGame_Live.Utils.isJailTeam
import jp.aoichaan0513.A_TosoGame_Live.Utils.isPlayerGroup
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.OnlineStatus
import net.dv8tion.jda.api.requests.GatewayIntent
import org.bukkit.*
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.entity.Arrow
import org.bukkit.entity.Player
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.potion.PotionEffectType
import org.json.JSONObject
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.nio.file.FileSystems
import java.nio.file.Files
import java.nio.file.Paths
import java.text.DecimalFormat
import java.util.*

class Main : JavaPlugin(), Listener {

    companion object {

        lateinit var pluginInstance: JavaPlugin
        var botInstance: JDA? = null
            private set

        lateinit var mainConfig: FileConfiguration

        lateinit var worldConfig: WorldConfig

        const val PHONE_ITEM_NAME = "スマートフォン"
        const val PACKAGE_PATH = "jp.aoichaan0513.A_TosoGame_Live"

        val FILE_SEPARATOR = FileSystems.getDefault().separator
        val CHARSET = Charsets.UTF_8

        private var commands = hashMapOf<String, ICommand>()
        private var listeners = listOf<Listener>()

        // ハンター・通報部隊抽選応募リスト
        val hunterShuffleSet = mutableSetOf<UUID>()
        val tuhoShuffleSet = mutableSetOf<UUID>()

        // プレイヤーが座るときの矢のリスト
        val arrowSet = mutableSetOf<Arrow>()

        val projectChannel = ResourceBundle.getBundle("settings").getString("projectChannel")
        val projectVersion = ResourceBundle.getBundle("settings").getString("projectVersion")
        val projectBuildDate = ResourceBundle.getBundle("settings").getString("projectBuildDate")

        fun setMainConfig(): FileConfiguration? {
            pluginInstance.saveConfig()
            mainConfig = pluginInstance.config
            return mainConfig
        }

        fun loadConfig() {
            loadFolder("updates")
            loadFolder("players")
            loadFolder("missions")
            loadFolder("scripts")
            loadFolder("scripts${FILE_SEPARATOR}commands")
            loadFolder("scripts${FILE_SEPARATOR}missions")

            loadFile("scripts${FILE_SEPARATOR}commands", "template.js")
            loadFile("scripts${FILE_SEPARATOR}missions", "template.js")
            loadFile("scripts${FILE_SEPARATOR}missions", "0.js")

            loadBuiltinMission("1.txt")
            loadBuiltinMission("2.txt")
            loadBuiltinMission("3.txt")
            loadBuiltinMission("4.txt")

            MissionManager.initMissions()
            return
        }

        private fun loadFile(folderName: String, fileName: String) {
            val file = File("${pluginInstance.dataFolder}$FILE_SEPARATOR$folderName", fileName)
            if (!file.exists()) {
                Bukkit.getConsoleSender().sendMessage("${MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY)}ファイル  \"$folderName$FILE_SEPARATOR$fileName\" を作成します…")
                pluginInstance.saveResource("$folderName$FILE_SEPARATOR$fileName", false)
                Bukkit.getConsoleSender().sendMessage("${MainAPI.getPrefix(MainAPI.PrefixType.SUCCESS)}ファイル \"$folderName$FILE_SEPARATOR$fileName\" を作成しました。")
            } else {
                Bukkit.getConsoleSender().sendMessage("${MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY)}ファイル \"$folderName$FILE_SEPARATOR$fileName\" が見つかったためスルーしました。")
            }
        }

        private fun loadFolder(folderName: String) {
            val file = File("${pluginInstance.dataFolder}$FILE_SEPARATOR$folderName")
            if (!file.exists()) {
                if (file.mkdir())
                    Bukkit.getConsoleSender().sendMessage("${MainAPI.getPrefix(MainAPI.PrefixType.SUCCESS)}フォルダ \"$folderName\" を作成しました。")
                else
                    Bukkit.getConsoleSender().sendMessage("${MainAPI.getPrefix(MainAPI.PrefixType.ERROR)}フォルダ \"$folderName\" を作成できませんでした。")
            } else {
                Bukkit.getConsoleSender().sendMessage("${MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY)}フォルダ \"$folderName\" が見つかったためスルーしました。")
            }
        }

        private fun loadBuiltinMission(fileName: String) {
            val file = File("${pluginInstance.dataFolder}${FILE_SEPARATOR}missions", fileName)
            if (!file.exists()) {
                Bukkit.getConsoleSender().sendMessage("${MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY)}ビルトインミッション \"$fileName\" を作成します…")
                pluginInstance.saveResource("missions$FILE_SEPARATOR$fileName", false)
                Bukkit.getConsoleSender().sendMessage("${MainAPI.getPrefix(MainAPI.PrefixType.SUCCESS)}ビルトインミッション \"$fileName\" を作成しました。")
            } else {
                Bukkit.getConsoleSender().sendMessage("${MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY)}ビルトインミッション \"$fileName\" が見つかったためスルーしました。")
            }
        }

        private fun loadMap() {
            if (worldConfig.config.contains("border.map.p1.x") && worldConfig.config.contains("border.map.p1.z")
                    && worldConfig.config.contains("border.map.p2.x") && worldConfig.config.contains("border.map.p2.z")) {
                Bukkit.getConsoleSender().sendMessage("${MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY)}地図を生成しています…")
                if (MapUtility.generateMap())
                    Bukkit.getConsoleSender().sendMessage("${MainAPI.getPrefix(MainAPI.PrefixType.SUCCESS)}地図の生成が完了しました。")
                else
                    Bukkit.getConsoleSender().sendMessage("${MainAPI.getPrefix(MainAPI.PrefixType.ERROR)}地図の生成ができませんでした。")
            } else {
                Bukkit.getConsoleSender().sendMessage("""
                    ${MainAPI.getPrefix(MainAPI.PrefixType.ERROR)}マップの設定が完了していないため地図の生成ができませんでした。
                    ${MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY)}マップの設定をした後に"/map generate"を実行してください。
                """.trimIndent())
            }
        }
    }

    override fun onEnable() {
        pluginInstance = this

        saveDefaultConfig()
        mainConfig = config

        Teams.setScoreboard()

        loadConfig()
        loadWorld()
        loadCommand()
        loadBlockedCommand()
        loadListener()
        loadBot()

        for (player in Bukkit.getOnlinePlayers()) {
            DifficultyManager.setDifficulty(player)
            if (!Scoreboard.boardMap.containsKey(player.uniqueId))
                Scoreboard.setBoard(player)
        }

        BossBarManager.showBar()

        runTask()
        RespawnRunnable().runTaskTimer(pluginInstance, 0, 20)

        Bukkit.getConsoleSender().sendMessage("${MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY)}プラグインを起動しました。")
    }

    override fun onDisable() {
        botInstance?.shutdownNow()

        Teams.resetScoreboard()
        BossBarManager.resetBar()
        MissionManager.resetBossBar()

        Bukkit.getConsoleSender().sendMessage("${MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY)}プラグインを終了しました。")
    }


    private fun loadCommand() {
        Bukkit.getConsoleSender().sendMessage("${MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY)}コマンドを読み込んでいます…")

        commands = hashMapOf(
                // メインコマンド
                "toso" to Toso("toso"),

                // ゲーム進行コマンド (運営用)
                "game" to Game("game"), // コマンドブロック対応
                "mission" to Mission("mission"), // コマンドブロック対応
                "team" to Team("team"), // コマンドブロック対応

                // ゲーム進行コマンド (プレイヤー用)
                "h" to H("h"),
                "t" to T("t"),
                "hz" to HZ("hz"),

                // ミッション用コマンド
                "code" to Code("code"),

                // オープニングゲーム用コマンド
                "shuffle" to Shuffle("shuffle"),

                // 設定用コマンド
                "location" to Location("location"),
                "map" to Map("map"),

                // ユーティリティコマンド
                "btp" to Btp("btp"),
                "resource" to Resource("resource"),
                "menu" to Menu("menu"),

                // プレイヤー用コマンド
                "join" to Join("join"), // コマンドブロック対応
                "leave" to Leave("leave"), // コマンドブロック対応
                "broadcaster" to Broadcaster("broadcaster"), // コマンドブロック対応
                "disappear" to Disappear("disappear"), // コマンドブロック対応
                "appear" to Appear("appear"), // コマンドブロック対応
                "gamemode" to GameMode("gamemode"), // コマンドブロック対応
                "hide" to Hide("hide"),
                "show" to Show("show"),
                "spec" to Spec("spec"),
                "sidebar" to Sidebar("sidebar"),
                "ping" to Ping("ping"),

                // スクリプトコマンド
                "script" to Script("script"),

                // 特に意味がないコマンド
                "nick" to Nick("nick"),
                "ride" to Ride("ride")
        )

        commands.forEach { getCommand(it.key)!!.setExecutor(it.value) }

        Bukkit.getConsoleSender().sendMessage("${MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY)}コマンドを${ChatColor.GREEN}${ChatColor.UNDERLINE}${commands.size}件${ChatColor.RESET}${ChatColor.GRAY}読み込みました。")
        return
    }

    private fun loadBlockedCommand() {
        onCommand.addBlockCommand("gamemode")
        onCommand.addBlockCommand("gm")
        onCommand.addBlockCommand("gms")
        onCommand.addBlockCommand("gm0")
        onCommand.addBlockCommand("gmc")
        onCommand.addBlockCommand("gm1")
        onCommand.addBlockCommand("gma")
        onCommand.addBlockCommand("gm2")
        onCommand.addBlockCommand("gmsp")
        onCommand.addBlockCommand("gm3")

        onCommand.addBlockCommand("?")
        onCommand.addBlockCommand("help")
        onCommand.addBlockCommand("about")
        onCommand.addBlockCommand("plugins")
        onCommand.addBlockCommand("pl")
        onCommand.addBlockCommand("version")
        onCommand.addBlockCommand("ver")

        onCommand.addBlockCommand("tell")
        onCommand.addBlockCommand("w")
        onCommand.addBlockCommand("r")
        onCommand.addBlockCommand("m")
        onCommand.addBlockCommand("me")
        onCommand.addBlockCommand("msg")
        return
    }

    private fun loadListener() {
        Bukkit.getConsoleSender().sendMessage("${MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY)}リスナーを読み込んでいます…")

        listeners = arrayListOf(
                // プレイヤー系
                onJoin(),
                onQuit(),
                onResourcePack(),

                // クリック・ダメージ系
                onInteract(),
                onDamage(),

                // 移動系
                onMove(),
                onVehicle(),

                // チャット・コマンド系
                onChat(),
                onCommand(),

                // インベントリ系
                onInventory(),
                onInventoryGui(),

                // エンティティ系
                onEntity(),

                // アイテム系
                onItem(),

                // ブロック系
                onBreak(),
                onSign(),

                // ミッション系 (独自)
                onMission()
        )

        val pluginManager = Bukkit.getPluginManager()
        listeners.forEach { pluginManager.registerEvents(it, pluginInstance) }

        Bukkit.getConsoleSender().sendMessage("${MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY)}リスナーを${ChatColor.GREEN}${ChatColor.UNDERLINE}${listeners.size}件${ChatColor.RESET}${ChatColor.GRAY}読み込みました。")
        return
    }

    private fun loadWorld() {
        if (!WorldManager.worldName.startsWith("world")) {
            Bukkit.getConsoleSender().sendMessage("${MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY)}ワールドを読み込んでいます…")
            val world = Bukkit.createWorld(WorldCreator(WorldManager.worldName))!!
            world.difficulty = Difficulty.EASY
            world.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false)
            world.setGameRule(GameRule.DO_WEATHER_CYCLE, false)
            world.setGameRule(GameRule.DO_MOB_SPAWNING, false)
            worldConfig = WorldConfig(world)
            Bukkit.getConsoleSender().sendMessage("${MainAPI.getPrefix(MainAPI.PrefixType.SUCCESS)}ワールドを読み込みました。")
            return
        } else {
            val world = WorldManager.world
            world.difficulty = Difficulty.EASY
            world.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false)
            world.setGameRule(GameRule.DO_WEATHER_CYCLE, false)
            world.setGameRule(GameRule.DO_MOB_SPAWNING, false)
            worldConfig = WorldConfig(world)
            return
        }
    }

    private fun loadBot() {
        if (!mainConfig.getBoolean("discordIntegration.enabled")) return
        botInstance = JDABuilder.createDefault(mainConfig.getString("discordIntegration.token"), GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_MESSAGE_REACTIONS, GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_VOICE_STATES, GatewayIntent.DIRECT_MESSAGES, GatewayIntent.DIRECT_MESSAGE_REACTIONS)
                .setStatus(OnlineStatus.ONLINE)
                .addEventListeners(onMessage())
                .build()
    }


    private fun download() {
        val urlStr = "https://incha.work/services/files/plugins/org/aoichaan0513/a_tosogame_live/"
        val strPostUrl = "${urlStr}api"
        Bukkit.getConsoleSender().sendMessage("${MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY)}更新を確認しています…")
        val jsonObject = JSONObject(HttpConnection(strPostUrl, "{\"channel\":\"$projectChannel\", \"current\":\"$projectVersion\"}").result)

        if (jsonObject.getBoolean("result")) {
            Bukkit.getConsoleSender().sendMessage("""
                ${MainAPI.getPrefix(MainAPI.PrefixType.WARNING)}更新が見つかりました。
                ${MainAPI.getPrefix(MainAPI.PrefixType.WARNING)}プラグインチャンネル: $projectChannel
                ${MainAPI.getPrefix(MainAPI.PrefixType.WARNING)}現在のバージョン: $projectVersion
                ${MainAPI.getPrefix(MainAPI.PrefixType.WARNING)}最新のバージョン: ${jsonObject.getString("latest")}
                ${MainAPI.getPrefix(MainAPI.PrefixType.WARNING)}最新のバージョンをダウンロードしています…
            """.trimIndent())
            try {
                val url = URL("$urlStr${jsonObject.getString("file")}")
                val conn = url.openConnection() as HttpURLConnection
                conn.allowUserInteraction = false
                conn.instanceFollowRedirects = true
                conn.requestMethod = "GET"
                conn.connect()

                val httpStatusCode = conn.responseCode
                if (httpStatusCode == HttpURLConnection.HTTP_OK) {
                    // Input Stream
                    val dataInStream = DataInputStream(conn.inputStream)

                    // Output Stream
                    val dataOutStream = DataOutputStream(BufferedOutputStream(FileOutputStream("$dataFolder${FILE_SEPARATOR}updates${FILE_SEPARATOR}${jsonObject.getString("file")}")))

                    // Read Data
                    val b = ByteArray(4096)
                    var readByte: Int
                    while (-1 != dataInStream.read(b).also { readByte = it })
                        dataOutStream.write(b, 0, readByte)

                    // Close Stream
                    dataInStream.close()
                    dataOutStream.close()

                    Bukkit.getConsoleSender().sendMessage("""
                        ${MainAPI.getPrefix(MainAPI.PrefixType.SUCCESS)}最新のバージョンをダウンロードしました。
                        ${MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY)}プラグインを最新のバージョンに置き換えています…
                    """.trimIndent())

                    try {
                        val sourcePath = Paths.get("$dataFolder${FILE_SEPARATOR}updates${FILE_SEPARATOR}${jsonObject.getString("file")}")
                        val targetPath = Paths.get("${server.worldContainer.absolutePath}${FILE_SEPARATOR}plugins${FILE_SEPARATOR}${jsonObject.getString("file")}")
                        Files.move(sourcePath, targetPath)
                        Bukkit.getConsoleSender().sendMessage("${MainAPI.getPrefix(MainAPI.PrefixType.SUCCESS)}最新のバージョンを移動しました。")
                        try {
                            val targetPath2 = Paths.get("${server.worldContainer.absolutePath}${FILE_SEPARATOR}plugins${FILE_SEPARATOR}A_TosoGame_Live-${description.version}.jar")
                            if (Files.deleteIfExists(targetPath2)) {
                                Bukkit.getConsoleSender().sendMessage("""
                                    ${MainAPI.getPrefix(MainAPI.PrefixType.SUCCESS)}現在のバージョンを削除しました。
                                    ${MainAPI.getPrefix(MainAPI.PrefixType.WARNING)}サーバーを再起動しています…
                                """.trimIndent())
                                server.reload()
                            } else {
                                Bukkit.getConsoleSender().sendMessage("${MainAPI.getPrefix(MainAPI.PrefixType.WARNING)}サーバーを再起動しています…")
                                server.reload()
                            }
                        } catch (e: IOException) {
                            Bukkit.getConsoleSender().sendMessage("""
                                ${MainAPI.getPrefix(MainAPI.PrefixType.ERROR)}更新に失敗しました。以前のバージョンのプラグインを削除できませんでした。
                                ${MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY)}詳細は下記のエラーログを確認してください。
                            """.trimIndent())
                            e.printStackTrace()
                            return
                        }
                    } catch (e: IOException) {
                        Bukkit.getConsoleSender().sendMessage("""
                            ${MainAPI.getPrefix(MainAPI.PrefixType.ERROR)}更新に失敗しました。プラグインの移動に失敗しました。
                            ${MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY)}詳細は下記のエラーログを確認してください。
                        """.trimIndent())
                        e.printStackTrace()
                        return
                    }
                } else {
                    Bukkit.getConsoleSender().sendMessage("${MainAPI.getPrefix(MainAPI.PrefixType.ERROR)}更新に失敗しました。サーバーへのアクセスに失敗しました。")
                    return
                }
            } catch (e: Exception) {
                Bukkit.getConsoleSender().sendMessage("""
                    ${MainAPI.getPrefix(MainAPI.PrefixType.ERROR)}更新に失敗しました。予期しないエラーが発生しました。
                    ${MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY)}詳細は下記のエラーログを確認してください。
                """.trimIndent())
                e.printStackTrace()
                return
            }
        } else {
            Bukkit.getConsoleSender().sendMessage("${MainAPI.getPrefix(MainAPI.PrefixType.SUCCESS)}更新はありませんでした。")
            return
        }
    }


    private fun runTask() {
        val decimalFormat = DecimalFormat("0.0")

        server.scheduler.runTaskTimerAsynchronously(pluginInstance, Runnable {
            Scoreboard.updateScoreboard()

            for (player in Bukkit.getOnlinePlayers())
                ActionBarManager.sendActionBar(player, getActionbarMessage(player, decimalFormat))
        }, 0, 0)
        server.scheduler.runTaskTimer(pluginInstance, Runnable {
            for (player in Bukkit.getOnlinePlayers()) {
                VisibilityManager.hidePlayers(player)

                if (GameManager.isGame()) {
                    if (player.isPlayerGroup) {
                        player.foodLevel = 16
                    } else {
                        player.walkSpeed = 0.2f
                        player.health = 20.0
                        player.foodLevel = 20
                    }
                } else {
                    player.walkSpeed = 0.2f
                    player.health = 20.0
                    player.foodLevel = 20
                }
            }
        }, 0, 0)
    }

    private fun getActionbarMessage(p: Player, decimalFormat: DecimalFormat): String {
        val separator = "${ChatColor.RESET}${ChatColor.GRAY} / ${ChatColor.RESET}"

        val isHiddenMessage = if (PlayerManager.loadConfig(p).visibility) "${ChatColor.RED}姿を非表示中" else ""

        val target = p.spectatorTarget
        val spectatorMessage = if (p.gameMode == org.bukkit.GameMode.SPECTATOR && target != null && target is Player) "${ChatColor.GRAY}${target.name} に憑依中" else ""

        val invisibleMessage = if (p.isPlayerGroup) {
            if (p.hasPotionEffect(PotionEffectType.INVISIBILITY))
                "${ChatColor.GOLD}${ChatColor.UNDERLINE}透明化${ChatColor.GRAY}終了まで残り${ChatColor.GOLD}${ChatColor.BOLD}${ChatColor.UNDERLINE}${decimalFormat.format(p.getPotionEffect(PotionEffectType.INVISIBILITY)!!.duration.toDouble() / 20)}${ChatColor.RESET}${ChatColor.GRAY}秒"
            else if (p.hasCooldown(Material.BONE))
                "${ChatColor.GOLD}${ChatColor.UNDERLINE}透明化クールダウン${ChatColor.GRAY}終了まで残り${ChatColor.GOLD}${ChatColor.BOLD}${ChatColor.UNDERLINE}${decimalFormat.format(p.getCooldown(Material.BONE).toDouble() / 20)}${ChatColor.RESET}${ChatColor.GRAY}秒"
            else
                ""
        } else {
            ""
        }

        val speedMessage = if (p.isPlayerGroup) {
            if (p.hasPotionEffect(PotionEffectType.SPEED))
                "${ChatColor.GOLD}${ChatColor.UNDERLINE}移動速度上昇${ChatColor.GRAY}終了まで残り${ChatColor.GOLD}${ChatColor.BOLD}${ChatColor.UNDERLINE}${decimalFormat.format(p.getPotionEffect(PotionEffectType.SPEED)!!.duration.toDouble() / 20)}${ChatColor.RESET}${ChatColor.GRAY}秒"
            else if (p.hasCooldown(Material.FEATHER))
                "${ChatColor.GOLD}${ChatColor.UNDERLINE}移動速度上昇クールダウン${ChatColor.GRAY}終了まで残り${ChatColor.GOLD}${ChatColor.BOLD}${ChatColor.UNDERLINE}${decimalFormat.format(p.getCooldown(Material.FEATHER).toDouble() / 20)}${ChatColor.RESET}${ChatColor.GRAY}秒"
            else
                ""
        } else {
            ""
        }

        val respawnAutoTime = RespawnRunnable.autoTimeMap[p.uniqueId]
        val respawnCoolTime = RespawnRunnable.coolTimeMap[p.uniqueId]

        val respawnMessage = if (GameManager.isGame() && p.isJailTeam) {
            if (TosoGameAPI.isRespawn && RespawnRunnable.isAllowRespawn(p))
                "${if (respawnAutoTime != null && respawnAutoTime > 0) "${ChatColor.GOLD}${ChatColor.UNDERLINE}自動復活${ChatColor.GRAY}まで残り${ChatColor.GOLD}${ChatColor.BOLD}${ChatColor.UNDERLINE}${TimeFormat.formatJapan(respawnAutoTime)}" else ""}${if (respawnAutoTime != null && respawnAutoTime > 0 && respawnCoolTime != null && respawnCoolTime > 0) separator else ""}${if (respawnCoolTime != null && respawnCoolTime > 0) "${ChatColor.GOLD}${ChatColor.UNDERLINE}復活可能${ChatColor.GRAY}まで残り${ChatColor.GOLD}${ChatColor.BOLD}${ChatColor.UNDERLINE}${TimeFormat.formatJapan(respawnCoolTime)}" else ""}"
            else
                "${ChatColor.GOLD}${ChatColor.BOLD}${ChatColor.UNDERLINE}復活不可"

        } else {
            ""
        }

        return "${if (isHiddenMessage.isNotEmpty()) "$isHiddenMessage${if (spectatorMessage.isNotEmpty() || invisibleMessage.isNotEmpty() || speedMessage.isNotEmpty()) separator else ""}" else ""}${if (spectatorMessage.isNotEmpty()) "$spectatorMessage${if (invisibleMessage.isNotEmpty() || speedMessage.isNotEmpty()) separator else ""}" else ""}$invisibleMessage${if (invisibleMessage.isNotEmpty() && speedMessage.isNotEmpty()) separator else ""}$speedMessage${if (respawnMessage.isNotEmpty()) "$respawnMessage${if (invisibleMessage.isNotEmpty() || invisibleMessage.isNotEmpty() || speedMessage.isNotEmpty()) separator else ""}" else ""}"
    }
}
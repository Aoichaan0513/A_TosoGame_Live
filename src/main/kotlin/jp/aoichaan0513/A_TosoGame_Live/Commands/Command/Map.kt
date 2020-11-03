package jp.aoichaan0513.A_TosoGame_Live.Commands.Command

import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI
import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI.ErrorMessage
import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI.PrefixType
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.World.WorldConfig
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.World.WorldManager
import jp.aoichaan0513.A_TosoGame_Live.API.Map.MapUtility
import jp.aoichaan0513.A_TosoGame_Live.API.TosoGameAPI
import jp.aoichaan0513.A_TosoGame_Live.Commands.ICommand
import jp.aoichaan0513.A_TosoGame_Live.Inventory.MapInventory
import jp.aoichaan0513.A_TosoGame_Live.Main
import org.bukkit.Bukkit
import org.bukkit.Difficulty
import org.bukkit.GameRule
import org.bukkit.WorldCreator
import org.bukkit.command.BlockCommandSender
import org.bukkit.command.Command
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.entity.Player
import java.io.File
import java.util.*

class Map(name: String) : ICommand(name) {
    override fun onPlayerCommand(sp: Player, cmd: Command, label: String, args: Array<String>) {
        if (TosoGameAPI.isAdmin(sp)) {
            if (args.isNotEmpty()) {
                val worldConfig = Main.worldConfig
                if (args[0].equals("load", true)) {
                    if (args.size != 1) {
                        if (File(args[1]).exists()) {
                            if (Bukkit.getWorlds().none { it.name.equals(args[1], true) }) {
                                sp.sendMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}マップを読み込んでいます…")
                                val world = Bukkit.createWorld(WorldCreator(args[1]))!!
                                world.difficulty = Difficulty.EASY
                                world.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false)
                                world.setGameRule(GameRule.DO_WEATHER_CYCLE, false)
                                world.setGameRule(GameRule.DO_MOB_SPAWNING, false)
                                WorldManager.world = world
                                Main.worldConfig = WorldConfig(world)
                                sp.teleport(world.spawnLocation)
                                sp.sendMessage("${MainAPI.getPrefix(PrefixType.SUCCESS)}マップを読み込みました。")
                                return
                            }
                            sp.sendMessage("${MainAPI.getPrefix(PrefixType.ERROR)}指定したマップはすでに読み込まれています。")
                            return
                        }
                        sp.sendMessage("${MainAPI.getPrefix(PrefixType.ERROR)}引数が不正です。存在するマップ名を指定してください。")
                        return
                    }
                    sp.sendMessage("${MainAPI.getPrefix(PrefixType.ERROR)}引数が不正です。マップ名を指定してください。")
                    return
                } else if (args[0].equals("unload", true)) {
                    if (args.size != 1) {
                        if (File(args[1]).exists()) {
                            if (Bukkit.getWorlds().any { it.name.equals(args[1], true) }) {
                                if (Bukkit.getWorld(args[1])!!.players.size == 0) {
                                    sp.sendMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}読み込んだマップを破棄しています…")
                                    val world = Bukkit.getWorld("world")
                                    world!!.difficulty = Difficulty.EASY
                                    world.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false)
                                    world.setGameRule(GameRule.DO_WEATHER_CYCLE, false)
                                    world.setGameRule(GameRule.DO_MOB_SPAWNING, false)
                                    WorldManager.world = world
                                    Main.worldConfig = WorldConfig(world)
                                    sp.teleport(world.spawnLocation)
                                    Bukkit.unloadWorld(args[1], true)
                                    sp.sendMessage("${MainAPI.getPrefix(PrefixType.SUCCESS)}読み込んだマップを破棄しました。")
                                    return
                                }
                                sp.sendMessage("${MainAPI.getPrefix(PrefixType.ERROR)}指定したマップにプレイヤーがいるため読み込みを破棄することができません。")
                                return
                            }
                            sp.sendMessage("${MainAPI.getPrefix(PrefixType.ERROR)}指定したマップは読み込まれていません。")
                            return
                        }
                        sp.sendMessage("${MainAPI.getPrefix(PrefixType.ERROR)}引数が不正です。存在するマップ名を指定してください。")
                        return
                    }
                    sp.sendMessage("${MainAPI.getPrefix(PrefixType.ERROR)}引数が不正です。マップ名を指定してください。")
                    return
                } else if (args[0].equals("generate", true)) {
                    if (worldConfig.config.contains(WorldManager.PathType.BORDER_MAP.path + ".p1.x") && worldConfig.config.contains(WorldManager.PathType.BORDER_MAP.path + ".p1.z")
                            && worldConfig.config.contains(WorldManager.PathType.BORDER_MAP.path + ".p2.x") && worldConfig.config.contains(WorldManager.PathType.BORDER_MAP.path + ".p2.z")) {
                        Bukkit.broadcastMessage("""
                            ${MainAPI.getPrefix(PrefixType.WARNING)}地図を生成しています…
                            ${MainAPI.getPrefix(PrefixType.SECONDARY)}ラグが発生しますのでご注意ください。
                        """.trimIndent())
                        sp.sendMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}地図を生成しています…")
                        if (MapUtility.generateMap()) {
                            sp.sendMessage("${MainAPI.getPrefix(PrefixType.SUCCESS)}地図の生成が完了しました。")
                            Bukkit.broadcastMessage("${MainAPI.getPrefix(PrefixType.WARNING)}地図の生成が完了しました。")
                        } else {
                            sp.sendMessage("${MainAPI.getPrefix(PrefixType.ERROR)}地図の生成ができませんでした。")
                        }
                        return
                    } else {
                        sp.sendMessage("""
                            ${MainAPI.getPrefix(PrefixType.ERROR)}マップの設定が完了していないため地図の生成ができませんでした。
                            ${MainAPI.getPrefix(PrefixType.SECONDARY)}マップの設定をした後に"/map generate"を実行してください。
                        """.trimIndent())
                        return
                    }
                } else if (args[0].equals("edit", true)) {
                    sp.openInventory(MapInventory.editInventory)
                    sp.sendMessage("${MainAPI.getPrefix(PrefixType.SUCCESS)}マップ設定を開きました。")
                    return
                } else if (args[0].equals("list", true)) {
                    sp.openInventory(MapInventory.listInventory)
                    sp.sendMessage("${MainAPI.getPrefix(PrefixType.SUCCESS)}マップリストを開きました。")
                    return
                }
            }
            sp.sendMessage("""
                ${MainAPI.getPrefix(PrefixType.ERROR)}引数が不正です。
                ${MainAPI.getPrefix(PrefixType.ERROR)}コマンドの使い方:
                ${MainAPI.getPrefix(PrefixType.ERROR)}"/map load <マップ名>" - 指定したマップを読み込む
                ${MainAPI.getPrefix(PrefixType.ERROR)}"/map unload <マップ名>" - 指定したマップの読み込みを破棄
                ${MainAPI.getPrefix(PrefixType.ERROR)}"/map generate" - マップの地図を生成
                ${MainAPI.getPrefix(PrefixType.ERROR)}"/map edit" - マップの設定を変更
                ${MainAPI.getPrefix(PrefixType.ERROR)}"/map list" - マップリストを表示
            """.trimIndent())
            return
        }
        MainAPI.sendMessage(sp, ErrorMessage.PERMISSIONS_TEAM_ADMIN)
    }

    override fun onBlockCommand(bs: BlockCommandSender, cmd: Command, label: String, args: Array<String>) {
        MainAPI.sendMessage(bs, ErrorMessage.NOT_PLAYER)
    }

    override fun onConsoleCommand(cs: ConsoleCommandSender, cmd: Command, label: String, args: Array<String>) {
        MainAPI.sendMessage(cs, ErrorMessage.NOT_PLAYER)
    }

    override fun onPlayerTabComplete(sp: Player, cmd: Command, alias: String, args: Array<String>): List<String>? {
        if (!TosoGameAPI.isAdmin(sp)) return emptyList()
        if (args.size == 1) {
            return getTabList(args[0], HashSet(Arrays.asList("load", "unload", "generate", "edit", "list")))
        } else if (args.size == 2) {
            if (args[1].isEmpty()) {
                if (args[0].equals("load", true)) {
                    val set = mutableSetOf<String>()
                    if (Bukkit.getWorldContainer().listFiles() != null) {
                        Bukkit.getWorldContainer().listFiles()
                                .filter { it.isDirectory && File("${it.name}${Main.FILE_SEPARATOR}map.yml").exists() }
                                .forEach { set.add(it.name) }
                    }
                    return set.toList()
                }
            }
        }
        return emptyList()
    }

    override fun onBlockTabComplete(bs: BlockCommandSender, cmd: Command, alias: String, args: Array<String>): List<String>? {
        return emptyList()
    }

    override fun onConsoleTabComplete(cs: ConsoleCommandSender, cmd: Command, alias: String, args: Array<String>): List<String>? {
        return emptyList()
    }
}
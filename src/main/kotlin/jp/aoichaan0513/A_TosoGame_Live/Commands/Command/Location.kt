package jp.aoichaan0513.A_TosoGame_Live.Commands.Command

import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI
import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI.ErrorMessage
import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI.PrefixType
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.World.WorldManager
import jp.aoichaan0513.A_TosoGame_Live.API.TosoGameAPI
import jp.aoichaan0513.A_TosoGame_Live.Commands.ICommand
import jp.aoichaan0513.A_TosoGame_Live.Main
import jp.aoichaan0513.A_TosoGame_Live.Utils.ParseUtil
import org.bukkit.command.BlockCommandSender
import org.bukkit.command.Command
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.entity.Player

class Location(name: String) : ICommand(name) {

    override fun onPlayerCommand(sp: Player, cmd: Command, label: String, args: Array<String>) {
        if (TosoGameAPI.isAdmin(sp)) {
            val worldConfig = Main.worldConfig
            if (args.isNotEmpty()) {
                if (args[0].equals("opgame", true) || args[0].equals("opg", true)) {
                    worldConfig.opGameLocationConfig.opLocation = sp.location
                    sp.sendMessage("${MainAPI.getPrefix(PrefixType.SUCCESS)}オープニングゲーム地点を設定しました。")
                    return
                } else if (args[0].equals("gopgame", true) || args[0].equals("gopg", true)) {
                    if (args.size != 1) {
                        if (ParseUtil.isInt(args[1])) {
                            val i = args[1].toInt()
                            if (i >= 1) {
                                worldConfig.opGameLocationConfig.setGOPLocation(i, sp.location)
                                sp.sendMessage("${MainAPI.getPrefix(PrefixType.SUCCESS)}オープニングゲーム集合地点の位置" + i + "を設定しました。")
                                return
                            }
                            sp.sendMessage("${MainAPI.getPrefix(PrefixType.ERROR)}引数が不正です。1以上で数字を指定してください。")
                            return
                        } else {
                            MainAPI.sendMessage(sp, ErrorMessage.ARGS_INTEGER)
                            return
                        }
                    }
                    sp.sendMessage("""
                        ${MainAPI.getPrefix(PrefixType.ERROR)}引数が不正です。
                        ${MainAPI.getPrefix(PrefixType.ERROR)}コマンドの使い方:
                        ${MainAPI.getPrefix(PrefixType.ERROR)}"/location ${args[0]} <数値>" - オープニングゲーム集合地点の設定
                    """.trimIndent())
                    return
                } else if (args[0].equals("hunter", true)) {
                    if (args.size != 1) {
                        if (ParseUtil.isInt(args[1])) {
                            val i = args[1].toInt()
                            if (i >= 1) {
                                worldConfig.hunterLocationConfig.setLocation(i, sp.location)
                                sp.sendMessage("${MainAPI.getPrefix(PrefixType.SUCCESS)}ハンター集合地点の位置" + i + "を設定しました。")
                                return
                            }
                            sp.sendMessage("${MainAPI.getPrefix(PrefixType.ERROR)}引数が不正です。1以上で数字を指定してください。")
                            return
                        } else {
                            MainAPI.sendMessage(sp, ErrorMessage.ARGS_INTEGER)
                            return
                        }
                    }
                    sp.sendMessage("""
                        ${MainAPI.getPrefix(PrefixType.ERROR)}引数が不正です。
                        ${MainAPI.getPrefix(PrefixType.ERROR)}コマンドの使い方:
                        ${MainAPI.getPrefix(PrefixType.ERROR)}"/location ${args[0]} <数値>" - ハンター集合地点の設定
                    """.trimIndent())
                    return
                } else if (args[0].equals("door", true)) {
                    if (args.size != 1) {
                        if (args[1].equals("all", true)) {
                            worldConfig.hunterDoorConfig.openHunterDoors()
                            sp.sendMessage("${MainAPI.getPrefix(PrefixType.SUCCESS)}設定したハンターボックスのドアすべてを開きました。")
                            return
                        } else {
                            if (ParseUtil.isInt(args[1])) {
                                val i = args[1].toInt()
                                if (i >= 1) {
                                    if (worldConfig.config.contains(WorldManager.PathType.DOOR_HUNTER.path + ".p" + i)) {
                                        worldConfig.hunterDoorConfig.openHunterDoor(i)
                                        sp.sendMessage("${MainAPI.getPrefix(PrefixType.SUCCESS)}ハンターボックスのドア" + i + "を開きました。")
                                        return
                                    }
                                    sp.sendMessage("${MainAPI.getPrefix(PrefixType.ERROR)}ハンターボックスのドア" + i + "は設定されていないため開くことができません。")
                                    return
                                }
                                sp.sendMessage("${MainAPI.getPrefix(PrefixType.ERROR)}引数が不正です。1以上で数字を指定してください。")
                                return
                            } else {
                                MainAPI.sendMessage(sp, ErrorMessage.ARGS_INTEGER)
                                return
                            }
                        }
                    }
                    sp.sendMessage("""
                        ${MainAPI.getPrefix(PrefixType.ERROR)}引数が不正です。
                        ${MainAPI.getPrefix(PrefixType.ERROR)}コマンドの使い方:
                        ${MainAPI.getPrefix(PrefixType.ERROR)}"/location door open <数値>" - 指定したドアを開く
                        ${MainAPI.getPrefix(PrefixType.ERROR)}"/location door open all" - 設定したすべてのドアを開く
                    """.trimIndent())
                    return
                } else if (args[0].equals("jail", true)) {
                    if (args.size != 1) {
                        if (ParseUtil.isInt(args[1])) {
                            val i = args[1].toInt()
                            if (i >= 1) {
                                worldConfig.jailLocationConfig.setLocation(i, sp.location)
                                sp.sendMessage("${MainAPI.getPrefix(PrefixType.SUCCESS)}牢獄地点の位置" + i + "を設定しました。")
                                return
                            }
                            sp.sendMessage("${MainAPI.getPrefix(PrefixType.ERROR)}引数が不正です。1以上で数字を指定してください。")
                            return
                        } else {
                            MainAPI.sendMessage(sp, ErrorMessage.ARGS_INTEGER)
                            return
                        }
                    }
                    sp.sendMessage("""
                        ${MainAPI.getPrefix(PrefixType.ERROR)}引数が不正です。
                        ${MainAPI.getPrefix(PrefixType.ERROR)}コマンドの使い方:
                        ${MainAPI.getPrefix(PrefixType.ERROR)}"/location jail <数値>" - 牢獄地点の設定
                    """.trimIndent())
                    return
                } else if (args[0].equals("respawn", true) || args[0].equals("res", true)) {
                    if (args.size != 1) {
                        if (ParseUtil.isInt(args[1])) {
                            val i = args[1].toInt()
                            if (i >= 1) {
                                worldConfig.respawnLocationConfig.setLocation(i, sp.location)
                                sp.sendMessage("${MainAPI.getPrefix(PrefixType.SUCCESS)}復活地点の位置" + i + "を設定しました。")
                                return
                            }
                            sp.sendMessage("${MainAPI.getPrefix(PrefixType.ERROR)}引数が不正です。1以上で数字を指定してください。")
                            return
                        } else {
                            MainAPI.sendMessage(sp, ErrorMessage.ARGS_INTEGER)
                            return
                        }
                    }
                    sp.sendMessage("""
                        ${MainAPI.getPrefix(PrefixType.ERROR)}引数が不正です。
                        ${MainAPI.getPrefix(PrefixType.ERROR)}コマンドの使い方:
                        ${MainAPI.getPrefix(PrefixType.ERROR)}"/location ${args[0]} <数値>" - 復活地点の設定
                    """.trimIndent())
                    return
                }
                sp.sendMessage("""
                    ${MainAPI.getPrefix(PrefixType.ERROR)}引数が不正です。
                    ${MainAPI.getPrefix(PrefixType.ERROR)}コマンドの使い方:
                    ${MainAPI.getPrefix(PrefixType.ERROR)}"/location opgame" または "/location opg" - オープニングゲーム地点の設定
                    ${MainAPI.getPrefix(PrefixType.ERROR)}"/location gopgame <数値>" または "/location gopg <数値>" - オープニングゲーム集合地点の設定
                    ${MainAPI.getPrefix(PrefixType.ERROR)}"/location hunter" - ハンター集合地点の設定
                    ${MainAPI.getPrefix(PrefixType.ERROR)}"/location door <引数…>" - ドア位置の設定
                    ${MainAPI.getPrefix(PrefixType.ERROR)}"/location jail <数値>" - 牢獄地点の設定
                    ${MainAPI.getPrefix(PrefixType.ERROR)}"/location respawn <数値>" または "/location res <数値>" - 復活地点の設定
                """.trimIndent())
                return
            }
            sp.sendMessage("""
                ${MainAPI.getPrefix(PrefixType.ERROR)}引数が不正です。
                ${MainAPI.getPrefix(PrefixType.ERROR)}コマンドの使い方:
                ${MainAPI.getPrefix(PrefixType.ERROR)}"/location opgame" または "/location opg" - オープニングゲーム地点の設定
                ${MainAPI.getPrefix(PrefixType.ERROR)}"/location gopgame <数値>" または "/location gopg <数値>" - オープニングゲーム集合地点の設定
                ${MainAPI.getPrefix(PrefixType.ERROR)}"/location hunter" - ハンター集合地点の設定
                ${MainAPI.getPrefix(PrefixType.ERROR)}"/location door <引数…>" - ドア位置の設定
                ${MainAPI.getPrefix(PrefixType.ERROR)}"/location jail <数値>" - 牢獄地点の設定
                ${MainAPI.getPrefix(PrefixType.ERROR)}"/location respawn <数値>" または "/location res <数値>" - 復活地点の設定
            """.trimIndent())
            return
        }
        MainAPI.sendMessage(sp, ErrorMessage.PERMISSIONS)
        return
    }

    override fun onBlockCommand(bs: BlockCommandSender, cmd: Command, label: String, args: Array<String>) {
        MainAPI.sendMessage(bs, ErrorMessage.NOT_PLAYER)
        return
    }

    override fun onConsoleCommand(cs: ConsoleCommandSender, cmd: Command, label: String, args: Array<String>) {
        MainAPI.sendMessage(cs, ErrorMessage.NOT_PLAYER)
        return
    }

    override fun onPlayerTabComplete(sp: Player, cmd: Command, alias: String, args: Array<String>): List<String>? {
        if (TosoGameAPI.isAdmin(sp)) {
            if (args.size == 1) {
                return getTabList(args[0], "opgame", "opg", "gopgame", "gopg", "hunter", "door", "jail", "respawn", "res")
            } else if (args.size == 2) {
                if (args[0].equals("door", true)) {
                    val worldConfig = Main.worldConfig
                    val set = mutableSetOf("all")
                    for (i in worldConfig.hunterDoorConfig.doors.keys)
                        set.add(i.toString())
                    return getTabList(args[1], set)
                }
            }
        }
        return null
    }

    override fun onBlockTabComplete(bs: BlockCommandSender, cmd: Command, alias: String, args: Array<String>): List<String>? {
        return null
    }

    override fun onConsoleTabComplete(cs: ConsoleCommandSender, cmd: Command, alias: String, args: Array<String>): List<String>? {
        return null
    }
}
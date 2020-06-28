package jp.aoichaan0513.A_TosoGame_Live.Commands.Command

import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI
import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI.ErrorMessage
import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI.PrefixType
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.GameManager
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.GameManager.GameState
import jp.aoichaan0513.A_TosoGame_Live.API.TosoGameAPI
import jp.aoichaan0513.A_TosoGame_Live.Commands.ICommand
import jp.aoichaan0513.A_TosoGame_Live.Listeners.Minecraft.onInventory
import jp.aoichaan0513.A_TosoGame_Live.Main
import jp.aoichaan0513.A_TosoGame_Live.Mission.MissionManager
import jp.aoichaan0513.A_TosoGame_Live.Utils.ParseUtil
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.BlockCommandSender
import org.bukkit.command.Command
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.entity.Player
import java.io.File

class Mission(name: String) : ICommand(name) {

    override fun onPlayerCommand(sp: Player, cmd: Command, label: String, args: Array<String>) {
        if (TosoGameAPI.isAdmin(sp)) {
            if (GameManager.isGame(GameState.GAME)) {
                if (args.isNotEmpty()) {
                    if (args[0].equals("send", true)) {
                        if (args.size != 1) {
                            if (ParseUtil.isInt(args[1])) {
                                val i = args[1].toInt()

                                if (MissionManager.hasFile(i)) {
                                    MissionManager.sendMission(sp, i)
                                    return
                                } else {
                                    val stringBuilder = StringBuilder()
                                    val folder = File("${Main.pluginInstance.getDataFolder()}${Main.FILE_SEPARATOR}missions")
                                    folder.listFiles().filter { it.isFile && it.extension.equals("txt") }
                                            .forEach { stringBuilder.append("${MainAPI.getPrefix(PrefixType.ERROR)}ID${it.nameWithoutExtension}: ${it.readLines(Main.CHARSET)[0]}\n") }
                                    sp.sendMessage("""
                                            ${MainAPI.getPrefix(PrefixType.ERROR)}引数が不正です。そのようなファイルはありません。
                                            ${MainAPI.getPrefix(PrefixType.ERROR)}ミッションリスト:
                                            ${stringBuilder.toString().trim { it <= ' ' }}
                                        """.trimIndent())
                                    return
                                }
                            } else {
                                val stringBuilder = StringBuilder()
                                val folder = File("${Main.pluginInstance.getDataFolder()}${Main.FILE_SEPARATOR}missions")
                                folder.listFiles().filter { it.isFile && it.extension.equals("txt") }
                                        .forEach { stringBuilder.append("${MainAPI.getPrefix(PrefixType.ERROR)}ID${it.nameWithoutExtension}: ${it.readLines(Main.CHARSET)[0]}\n") }
                                sp.sendMessage("""
                                        ${MainAPI.getPrefix(PrefixType.ERROR)}引数が不正です。そのようなファイルはありません。
                                        ${MainAPI.getPrefix(PrefixType.ERROR)}ミッションリスト:
                                        ${stringBuilder.toString().trim { it <= ' ' }}
                                    """.trimIndent())
                                return
                            }
                        } else {
                            val stringBuilder = StringBuilder()
                            val folder = File("${Main.pluginInstance.getDataFolder()}${Main.FILE_SEPARATOR}missions")
                            folder.listFiles().filter { it.isFile && it.extension.equals("txt") }
                                    .forEach { stringBuilder.append("${MainAPI.getPrefix(PrefixType.ERROR)}ID${it.nameWithoutExtension}: ${it.readLines(Main.CHARSET)[0]}\n") }
                            sp.sendMessage("""
                                    ${MainAPI.getPrefix(PrefixType.ERROR)}引数が不正です。そのようなファイルはありません。
                                    ${MainAPI.getPrefix(PrefixType.ERROR)}ミッションリスト:
                                    ${stringBuilder.toString().trim { it <= ' ' }}
                                """.trimIndent())
                            return
                        }
                    } else if (args[0].equals("tutatu", true)) {
                        if (args.size > 1) {
                            val stringBuilder = StringBuilder()
                            for (i in 1 until args.size)
                                stringBuilder.append("${args[i].replace("&n", "\n")} ")

                            MissionManager.sendMission(sp, MissionManager.MissionType.TUTATU_HINT, MissionManager.MissionDetailType.TUTATU, stringBuilder.toString().trim())
                            return
                        }
                        sp.sendMessage("${MainAPI.getPrefix(PrefixType.ERROR)}引数が不正です。説明を指定してください。")
                        return
                    } else if (args[0].equals("hint", true)) {
                        if (args.size > 1) {
                            val stringBuilder = StringBuilder()
                            for (i in 1 until args.size)
                                stringBuilder.append("${args[i].replace("&n", "\n")} ")

                            MissionManager.sendMission(sp, MissionManager.MissionType.TUTATU_HINT, MissionManager.MissionDetailType.HINT, stringBuilder.toString().trim())
                            return
                        }
                        sp.sendMessage("${MainAPI.getPrefix(PrefixType.ERROR)}引数が不正です。説明を指定してください。")
                        return
                    } else if (args[0].equals("chest", true)) {
                        onInventory.isAllowOpen = !onInventory.isAllowOpen
                        sp.sendMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}チェストの開放を${if (onInventory.isAllowOpen) "${ChatColor.GREEN}${ChatColor.UNDERLINE}有効" else "${ChatColor.RED}${ChatColor.UNDERLINE}無効"}${ChatColor.RESET}${ChatColor.GRAY}にしました。")
                        return
                    } else if (args[0].equals("end", true)) {
                        if (MissionManager.isMission) {
                            MissionManager.endMissions()
                            sp.sendMessage("${MainAPI.getPrefix(PrefixType.SUCCESS)}ミッションを終了しました。")
                            Bukkit.broadcastMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}ミッションが終了しました。")
                            return
                        }
                        sp.sendMessage("${MainAPI.getPrefix(PrefixType.ERROR)}ミッションが開始されていないため終了できません。")
                        return
                    }
                    sp.sendMessage("""
                        ${MainAPI.getPrefix(PrefixType.ERROR)}引数が不正です。
                        ${MainAPI.getPrefix(PrefixType.ERROR)}コマンドの使い方:
                        ${MainAPI.getPrefix(PrefixType.ERROR)}"/mission send <ファイルID>" - ファイルからミッション追加
                        ${MainAPI.getPrefix(PrefixType.ERROR)}"/mission tutatu <説明>" - 説明から通達追加
                        ${MainAPI.getPrefix(PrefixType.ERROR)}"/mission hint <説明>" - 説明からヒント追加
                        ${MainAPI.getPrefix(PrefixType.ERROR)}"/mission chest" - チェストの開放を有効・無効化
                        ${MainAPI.getPrefix(PrefixType.ERROR)}"/mission end" - 実行中のミッションを終了
                    """.trimIndent())
                    return
                }
                sp.sendMessage("""
                    ${MainAPI.getPrefix(PrefixType.ERROR)}引数が不正です。
                    ${MainAPI.getPrefix(PrefixType.ERROR)}コマンドの使い方:
                    ${MainAPI.getPrefix(PrefixType.ERROR)}"/mission send <ファイルID>" - ファイルからミッション追加
                    ${MainAPI.getPrefix(PrefixType.ERROR)}"/mission tutatu <説明>" - 説明から通達追加
                    ${MainAPI.getPrefix(PrefixType.ERROR)}"/mission hint <説明>" - 説明からヒント追加
                    ${MainAPI.getPrefix(PrefixType.ERROR)}"/mission chest" - チェストの開放を有効・無効化
                    ${MainAPI.getPrefix(PrefixType.ERROR)}"/mission end" - 実行中のミッションを終了
                """.trimIndent())
                return
            }
            MainAPI.sendMessage(sp, ErrorMessage.NOT_GAME)
            return
        }
        MainAPI.sendMessage(sp, ErrorMessage.PERMISSIONS)
        return
    }

    override fun onBlockCommand(bs: BlockCommandSender, cmd: Command, label: String, args: Array<String>) {
        if (GameManager.isGame(GameState.GAME)) {
            if (args.isNotEmpty()) {
                if (args[0].equals("send", true)) {
                    if (args.size != 1) {
                        if (ParseUtil.isInt(args[1])) {
                            val i = args[1].toInt()

                            if (MissionManager.hasFile(i)) {
                                MissionManager.sendMission(bs, i)
                                return
                            } else {
                                val stringBuilder = StringBuilder()
                                val folder = File("${Main.pluginInstance.getDataFolder()}${Main.FILE_SEPARATOR}missions")
                                folder.listFiles().filter { it.isFile && it.extension.equals("txt") }
                                        .forEach { stringBuilder.append("${MainAPI.getPrefix(PrefixType.ERROR)}ID${it.nameWithoutExtension}: ${it.readLines(Main.CHARSET)[0]}\n") }
                                bs.sendMessage("""
                                        ${MainAPI.getPrefix(PrefixType.ERROR)}引数が不正です。そのようなファイルはありません。
                                        ${MainAPI.getPrefix(PrefixType.ERROR)}ミッションリスト:
                                        ${stringBuilder.toString().trim { it <= ' ' }}
                                    """.trimIndent())
                                return
                            }
                        } else {
                            val stringBuilder = StringBuilder()
                            val folder = File("${Main.pluginInstance.getDataFolder()}${Main.FILE_SEPARATOR}missions")
                            folder.listFiles().filter { it.isFile && it.extension.equals("txt") }
                                    .forEach { stringBuilder.append("${MainAPI.getPrefix(PrefixType.ERROR)}ID${it.nameWithoutExtension}: ${it.readLines(Main.CHARSET)[0]}\n") }
                            bs.sendMessage("""
                                    ${MainAPI.getPrefix(PrefixType.ERROR)}引数が不正です。そのようなファイルはありません。
                                    ${MainAPI.getPrefix(PrefixType.ERROR)}ミッションリスト:
                                    ${stringBuilder.toString().trim { it <= ' ' }}
                                """.trimIndent())
                            return
                        }
                    } else {
                        val stringBuilder = StringBuilder()
                        val folder = File("${Main.pluginInstance.getDataFolder()}${Main.FILE_SEPARATOR}missions")
                        folder.listFiles().filter { it.isFile && it.extension.equals("txt") }
                                .forEach { stringBuilder.append("${MainAPI.getPrefix(PrefixType.ERROR)}ID${it.nameWithoutExtension}: ${it.readLines(Main.CHARSET)[0]}\n") }
                        bs.sendMessage("""
                                ${MainAPI.getPrefix(PrefixType.ERROR)}引数が不正です。そのようなファイルはありません。
                                ${MainAPI.getPrefix(PrefixType.ERROR)}ミッションリスト:
                                ${stringBuilder.toString().trim { it <= ' ' }}
                            """.trimIndent())
                        return
                    }
                } else if (args[0].equals("tutatu", true)) {
                    if (args.size > 1) {
                        val stringBuilder = StringBuilder()
                        for (i in 1 until args.size)
                            stringBuilder.append("${args[i].replace("&n", "\n")} ")

                        MissionManager.sendMission(bs, MissionManager.MissionType.TUTATU_HINT, MissionManager.MissionDetailType.TUTATU, stringBuilder.toString().trim())
                        return
                    }
                    bs.sendMessage("${MainAPI.getPrefix(PrefixType.ERROR)}引数が不正です。説明を指定してください。")
                    return
                } else if (args[0].equals("hint", true)) {
                    if (args.size > 1) {
                        val stringBuilder = StringBuilder()
                        for (i in 1 until args.size)
                            stringBuilder.append("${args[i].replace("&n", "\n")} ")

                        MissionManager.sendMission(bs, MissionManager.MissionType.TUTATU_HINT, MissionManager.MissionDetailType.HINT, stringBuilder.toString().trim())
                        return
                    }
                    bs.sendMessage("${MainAPI.getPrefix(PrefixType.ERROR)}引数が不正です。説明を指定してください。")
                    return
                } else if (args[0].equals("chest", true)) {
                    onInventory.isAllowOpen = !onInventory.isAllowOpen
                    bs.sendMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}チェストの開放を${if (onInventory.isAllowOpen) "${ChatColor.GREEN}${ChatColor.UNDERLINE}有効" else "${ChatColor.RED}${ChatColor.UNDERLINE}無効"}${ChatColor.RESET}${ChatColor.GRAY}にしました。")
                    return
                } else if (args[0].equals("end", true)) {
                    if (MissionManager.isMission) {
                        MissionManager.endMissions()
                        bs.sendMessage("${MainAPI.getPrefix(PrefixType.SUCCESS)}ミッションを終了しました。")
                        Bukkit.broadcastMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}ミッションが終了しました。")
                        return
                    }
                    bs.sendMessage("${MainAPI.getPrefix(PrefixType.ERROR)}ミッションが開始されていないため終了できません。")
                    return
                }
                bs.sendMessage("""
                    ${MainAPI.getPrefix(PrefixType.ERROR)}引数が不正です。
                    ${MainAPI.getPrefix(PrefixType.ERROR)}コマンドの使い方:
                    ${MainAPI.getPrefix(PrefixType.ERROR)}"/mission send <ファイルID>" - ファイルからミッション追加
                    ${MainAPI.getPrefix(PrefixType.ERROR)}"/mission tutatu <説明>" - 説明から通達追加
                    ${MainAPI.getPrefix(PrefixType.ERROR)}"/mission hint <説明>" - 説明からヒント追加
                    ${MainAPI.getPrefix(PrefixType.ERROR)}"/mission chest" - チェストの開放を有効・無効化
                    ${MainAPI.getPrefix(PrefixType.ERROR)}"/mission end" - 実行中のミッションを終了
                """.trimIndent())
                return
            }
            bs.sendMessage("""
                ${MainAPI.getPrefix(PrefixType.ERROR)}引数が不正です。
                ${MainAPI.getPrefix(PrefixType.ERROR)}コマンドの使い方:
                ${MainAPI.getPrefix(PrefixType.ERROR)}"/mission send <ファイルID>" - ファイルからミッション追加
                ${MainAPI.getPrefix(PrefixType.ERROR)}"/mission tutatu <説明>" - 説明から通達追加
                ${MainAPI.getPrefix(PrefixType.ERROR)}"/mission hint <説明>" - 説明からヒント追加
                ${MainAPI.getPrefix(PrefixType.ERROR)}"/mission chest" - チェストの開放を有効・無効化
                ${MainAPI.getPrefix(PrefixType.ERROR)}"/mission end" - 実行中のミッションを終了
            """.trimIndent())
            return
        }
        MainAPI.sendMessage(bs, ErrorMessage.NOT_GAME)
        return
    }

    override fun onConsoleCommand(cs: ConsoleCommandSender, cmd: Command, label: String, args: Array<String>) {
        MainAPI.sendMessage(cs, ErrorMessage.NOT_PLAYER)
        return
    }

    override fun onPlayerTabComplete(sp: Player, cmd: Command, alias: String, args: Array<String>): List<String>? {
        if (!TosoGameAPI.isAdmin(sp)) return null
        if (args.size == 1) {
            return getTabList(args[0], "send", "tutatu", "hint", "chest", "end")
        } else if (args.size == 2) {
            if (args[0].equals("send", true)) {
                val set = mutableSetOf<String>()
                val file = File("plugins${Main.FILE_SEPARATOR}A_TosoGame_Live${Main.FILE_SEPARATOR}missions")
                file.listFiles().filter { it.isFile && it.extension.equals("txt", true) }.forEach { set.add(it.nameWithoutExtension) }
                return getTabList(args[1], set)
            }
        }
        return null
    }

    override fun onBlockTabComplete(bs: BlockCommandSender, cmd: Command, alias: String, args: Array<String>): List<String>? {
        if (args.size == 1) {
            return getTabList(args[0], "send", "tutatu", "hint", "chest", "end")
        } else if (args.size == 2) {
            if (args[0].equals("send", true)) {
                val set = mutableSetOf<String>()
                val file = File("plugins${Main.FILE_SEPARATOR}A_TosoGame_Live${Main.FILE_SEPARATOR}missions")
                file.listFiles().filter { it.isFile && it.extension.equals("txt", true) }.forEach { set.add(it.nameWithoutExtension) }
                return getTabList(args[1], set)
            }
        }
        return null
    }

    override fun onConsoleTabComplete(cs: ConsoleCommandSender, cmd: Command, alias: String, args: Array<String>): List<String>? {
        return null
    }
}
package jp.aoichaan0513.A_TosoGame_Live.Commands.Command

import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI
import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI.ErrorMessage
import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI.PrefixType
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.Player.PlayerManager
import jp.aoichaan0513.A_TosoGame_Live.API.TosoGameAPI
import jp.aoichaan0513.A_TosoGame_Live.Commands.ICommand
import org.bukkit.Bukkit
import org.bukkit.command.BlockCommandSender
import org.bukkit.command.Command
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.entity.Player

class BroadCaster(name: String) : ICommand(name) {
    override fun onPlayerCommand(sp: Player, cmd: Command, label: String, args: Array<String>) {
        if (label.equals("broadcaster", true) || label.equals("yt", true)) {
            if (TosoGameAPI.isAdmin(sp)) {
                if (args.isNotEmpty()) {
                    if (args[0].equals("add", true)) {
                        if (args.size != 1) {
                            val target = Bukkit.getPlayerExact(args[1])
                            if (target == null) {
                                MainAPI.sendOfflineMessage(sp, args[1])
                                return
                            } else {
                                val p = Bukkit.getPlayerExact(args[1])!!
                                if (!TosoGameAPI.isBroadCaster(p)) {
                                    PlayerManager.loadConfig(p).broadCaster = true
                                    sp.sendMessage(MainAPI.getPrefix(PrefixType.SUCCESS) + p.name + "を配信者に追加しました。")
                                    return
                                }
                                sp.sendMessage(MainAPI.getPrefix(PrefixType.ERROR) + p.name + "はすでに配信者になっています。")
                                return
                            }
                        }
                        MainAPI.sendMessage(sp, ErrorMessage.ARGS_PLAYER)
                        return
                    } else if (args[0].equals("remove", true)) {
                        if (args.size != 1) {
                            val target = Bukkit.getPlayerExact(args[1])
                            if (target == null) {
                                MainAPI.sendOfflineMessage(sp, args[1])
                                return
                            } else {
                                val p = Bukkit.getPlayerExact(args[1])!!
                                if (TosoGameAPI.isBroadCaster(p)) {
                                    PlayerManager.loadConfig(p).broadCaster = false
                                    sp.sendMessage(MainAPI.getPrefix(PrefixType.SUCCESS) + p.name + "を配信者から削除しました。")
                                    return
                                }
                                sp.sendMessage(MainAPI.getPrefix(PrefixType.ERROR) + p.name + "はすでに配信者ではありません。")
                                return
                            }
                        }
                        MainAPI.sendMessage(sp, ErrorMessage.ARGS_PLAYER)
                        return
                    } else if (args[0].equals("list", true)) {
                        val stringBuilder = StringBuilder()
                        for ((key, value) in PlayerManager.configs)
                            if (value.broadCaster)
                                stringBuilder.append("${MainAPI.getPrefix(PrefixType.SECONDARY)}${Bukkit.getOfflinePlayer(key).name}\n")
                        sp.sendMessage("""
                            ${MainAPI.getPrefix(PrefixType.WARNING)}配信者リスト:
                            ${stringBuilder.toString().trim { it <= ' ' }}
                        """.trimIndent())
                        return
                    }
                }
                sp.sendMessage("""
                    ${MainAPI.getPrefix(PrefixType.ERROR)}引数が不正です。
                    ${MainAPI.getPrefix(PrefixType.ERROR)}コマンドの使い方:
                    ${MainAPI.getPrefix(PrefixType.ERROR)}"/broadcaster add <プレイヤー名>" - 配信者を追加
                    ${MainAPI.getPrefix(PrefixType.ERROR)}"/broadcaster remove <プレイヤー名>" - 配信者を削除
                    ${MainAPI.getPrefix(PrefixType.ERROR)}"/broadcaster list" - 配信者リストを表示
                """.trimIndent())
                return
            }
            MainAPI.sendMessage(sp, ErrorMessage.PERMISSIONS)
            return
        } else if (label.equals("lhide", true)) {
            if (TosoGameAPI.isBroadCaster(sp)) {
                TosoGameAPI.addHidePlayer(sp)
                sp.sendMessage("${MainAPI.getPrefix(PrefixType.SUCCESS)}あなたを非表示にしました。")
                return
            }
            MainAPI.sendMessage(sp, ErrorMessage.PERMISSIONS)
            return
        } else if (label.equals("lshow", true)) {
            if (TosoGameAPI.isBroadCaster(sp)) {
                TosoGameAPI.removeHidePlayer(sp)
                sp.sendMessage("${MainAPI.getPrefix(PrefixType.SUCCESS)}あなたを表示しました。")
                return
            }
            MainAPI.sendMessage(sp, ErrorMessage.PERMISSIONS)
            return
        }
    }

    override fun onBlockCommand(bs: BlockCommandSender, cmd: Command, label: String, args: Array<String>) {
        if (label.equals("broadcaster", true) || label.equals("yt", true)) {
            if (args.isNotEmpty()) {
                if (args[0].equals("add", true)) {
                    if (args.size != 1) {
                        val target = Bukkit.getPlayerExact(args[1])
                        if (target == null) {
                            MainAPI.sendOfflineMessage(bs, args[1])
                            return
                        } else {
                            val p = Bukkit.getPlayerExact(args[1])!!
                            if (!TosoGameAPI.isBroadCaster(p)) {
                                PlayerManager.loadConfig(p).broadCaster = true
                                bs.sendMessage(MainAPI.getPrefix(PrefixType.SUCCESS) + p.name + "を配信者に追加しました。")
                                return
                            }
                            bs.sendMessage(MainAPI.getPrefix(PrefixType.ERROR) + p.name + "はすでに配信者になっています。")
                            return
                        }
                    }
                    MainAPI.sendMessage(bs, ErrorMessage.ARGS_PLAYER)
                    return
                } else if (args[0].equals("remove", true)) {
                    if (args.size != 1) {
                        val target = Bukkit.getPlayerExact(args[1])
                        if (target == null) {
                            MainAPI.sendOfflineMessage(bs, args[1])
                            return
                        } else {
                            val p = Bukkit.getPlayerExact(args[1])!!
                            if (TosoGameAPI.isBroadCaster(p)) {
                                PlayerManager.loadConfig(p).broadCaster = false
                                bs.sendMessage(MainAPI.getPrefix(PrefixType.SUCCESS) + p.name + "を配信者から削除しました。")
                                return
                            }
                            bs.sendMessage(MainAPI.getPrefix(PrefixType.ERROR) + p.name + "はすでに配信者ではありません。")
                            return
                        }
                    }
                    MainAPI.sendMessage(bs, ErrorMessage.ARGS_PLAYER)
                    return
                } else if (args[0].equals("list", true)) {
                    val stringBuilder = StringBuilder()
                    for ((key, value) in PlayerManager.configs)
                        if (value.broadCaster)
                            stringBuilder.append("${MainAPI.getPrefix(PrefixType.SECONDARY)}${Bukkit.getOfflinePlayer(key).name}\n")
                    bs.sendMessage("""
                        ${MainAPI.getPrefix(PrefixType.WARNING)}配信者リスト:
                        ${stringBuilder.toString().trim { it <= ' ' }}
                    """.trimIndent())
                    return
                }
            }
            bs.sendMessage("""
                ${MainAPI.getPrefix(PrefixType.ERROR)}引数が不正です。
                ${MainAPI.getPrefix(PrefixType.ERROR)}コマンドの使い方:
                ${MainAPI.getPrefix(PrefixType.ERROR)}"/broadcaster add <プレイヤー名>" - 配信者を追加
                ${MainAPI.getPrefix(PrefixType.ERROR)}"/broadcaster remove <プレイヤー名>" - 配信者を削除
                ${MainAPI.getPrefix(PrefixType.ERROR)}"/broadcaster list" - 配信者リストを表示
            """.trimIndent())
            return
        } else if (label.equals("lhide", true) || label.equals("lshow", true)) {
            MainAPI.sendMessage(bs, ErrorMessage.NOT_PLAYER)
            return
        }
    }

    override fun onConsoleCommand(cs: ConsoleCommandSender, cmd: Command, label: String, args: Array<String>) {
        if (label.equals("broadcaster", true) || label.equals("yt", true)) {
            if (args.isNotEmpty()) {
                if (args[0].equals("add", true)) {
                    if (args.size != 1) {
                        val target = Bukkit.getPlayerExact(args[1])
                        if (target == null) {
                            MainAPI.sendOfflineMessage(cs, args[1])
                            return
                        } else {
                            val p = Bukkit.getPlayerExact(args[1])!!
                            if (!TosoGameAPI.isBroadCaster(p)) {
                                PlayerManager.loadConfig(p).broadCaster = true
                                cs.sendMessage(MainAPI.getPrefix(PrefixType.SUCCESS) + p.name + "を配信者に追加しました。")
                                return
                            }
                            cs.sendMessage(MainAPI.getPrefix(PrefixType.ERROR) + p.name + "はすでに配信者になっています。")
                            return
                        }
                    }
                    MainAPI.sendMessage(cs, ErrorMessage.ARGS_PLAYER)
                    return
                } else if (args[0].equals("remove", true)) {
                    if (args.size != 1) {
                        val target = Bukkit.getPlayerExact(args[1])
                        if (target == null) {
                            MainAPI.sendOfflineMessage(cs, args[1])
                            return
                        } else {
                            val p = Bukkit.getPlayerExact(args[1])!!
                            if (TosoGameAPI.isBroadCaster(p)) {
                                PlayerManager.loadConfig(p).broadCaster = false
                                cs.sendMessage(MainAPI.getPrefix(PrefixType.SUCCESS) + p.name + "を配信者から削除しました。")
                                return
                            }
                            cs.sendMessage(MainAPI.getPrefix(PrefixType.ERROR) + p.name + "はすでに配信者ではありません。")
                            return
                        }
                    }
                    MainAPI.sendMessage(cs, ErrorMessage.ARGS_PLAYER)
                    return
                } else if (args[0].equals("list", true)) {
                    val stringBuilder = StringBuilder()
                    for ((key, value) in PlayerManager.configs)
                        if (value.broadCaster)
                            stringBuilder.append("${MainAPI.getPrefix(PrefixType.SECONDARY)}${Bukkit.getOfflinePlayer(key).name}\n")
                    cs.sendMessage("""
                        ${MainAPI.getPrefix(PrefixType.WARNING)}配信者リスト:
                        ${stringBuilder.toString().trim { it <= ' ' }}
                    """.trimIndent())
                    return
                }
            }
            cs.sendMessage("""
                ${MainAPI.getPrefix(PrefixType.ERROR)}引数が不正です。
                ${MainAPI.getPrefix(PrefixType.ERROR)}コマンドの使い方:
                ${MainAPI.getPrefix(PrefixType.ERROR)}"/broadcaster add <プレイヤー名>" - 配信者を追加
                ${MainAPI.getPrefix(PrefixType.ERROR)}"/broadcaster remove <プレイヤー名>" - 配信者を削除
                ${MainAPI.getPrefix(PrefixType.ERROR)}"/broadcaster list" - 配信者リストを表示
            """.trimIndent())
            return
        } else if (label.equals("lhide", true) || label.equals("lshow", true)) {
            MainAPI.sendMessage(cs, ErrorMessage.NOT_PLAYER)
            return
        }
    }

    override fun onPlayerTabComplete(sp: Player, cmd: Command, alias: String, args: Array<String>): List<String>? {
        if (!TosoGameAPI.isAdmin(sp) || args.size != 1) return null
        return getTabList(args[0], "add", "remove", "list")
    }

    override fun onBlockTabComplete(bs: BlockCommandSender, cmd: Command, alias: String, args: Array<String>): List<String>? {
        if (args.size != 1) return null
        return getTabList(args[0], "add", "remove", "list")
    }

    override fun onConsoleTabComplete(cs: ConsoleCommandSender, cmd: Command, alias: String, args: Array<String>): List<String>? {
        if (args.size != 1) return null
        return getTabList(args[0], "add", "remove", "list")
    }
}
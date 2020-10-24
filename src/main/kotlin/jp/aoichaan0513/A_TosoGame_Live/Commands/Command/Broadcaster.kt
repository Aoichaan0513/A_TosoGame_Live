package jp.aoichaan0513.A_TosoGame_Live.Commands.Command

import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI
import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI.ErrorMessage
import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI.PrefixType
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.Player.PlayerManager
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.Player.VisibilityManager
import jp.aoichaan0513.A_TosoGame_Live.API.TosoGameAPI
import jp.aoichaan0513.A_TosoGame_Live.Commands.ICommand
import jp.aoichaan0513.A_TosoGame_Live.Utils.toString
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.BlockCommandSender
import org.bukkit.command.Command
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.entity.Player

class Broadcaster(name: String) : ICommand(name) {

    override fun onPlayerCommand(sp: Player, cmd: Command, label: String, args: Array<String>) {
        if (label.equals("broadcaster", true) || label.equals("yt", true)) {
            if (TosoGameAPI.isAdmin(sp)) {
                if (args.isNotEmpty()) {
                    when (args[0].toLowerCase()) {
                        "add" -> {
                            if (args.size != 1) {
                                val p = Bukkit.getPlayerExact(args[1])
                                if (p != null) {
                                    if (!TosoGameAPI.isBroadCaster(p)) {
                                        PlayerManager.loadConfig(p).broadCaster = true
                                        sp.sendMessage("${MainAPI.getPrefix(PrefixType.SUCCESS)}${p.name}を配信者に追加しました。")
                                        return
                                    }
                                    sp.sendMessage("${MainAPI.getPrefix(PrefixType.ERROR)}${p.name}はすでに配信者になっています。")
                                    return
                                }
                                MainAPI.sendOfflineMessage(sp, args[1])
                                return
                            }
                            MainAPI.sendMessage(sp, ErrorMessage.ARGS_PLAYER)
                        }
                        "remove" -> {
                            if (args.size != 1) {
                                val p = Bukkit.getPlayerExact(args[1])
                                if (p != null) {
                                    if (TosoGameAPI.isBroadCaster(p)) {
                                        PlayerManager.loadConfig(p).broadCaster = false
                                        sp.sendMessage("${MainAPI.getPrefix(PrefixType.SUCCESS)}${p.name}を配信者から削除しました。")
                                        return
                                    }
                                    sp.sendMessage("${MainAPI.getPrefix(PrefixType.ERROR)}${p.name}はすでに配信者ではありません。")
                                    return
                                }
                                MainAPI.sendOfflineMessage(sp, args[1])
                                return
                            }
                            MainAPI.sendMessage(sp, ErrorMessage.ARGS_PLAYER)
                        }
                        "list" -> {
                            sp.sendMessage("${MainAPI.getPrefix(PrefixType.WARNING)}配信者リスト: ${MainAPI.getOnlinePlayers(PlayerManager.configs.filter { it.value.broadCaster }.map { it.key }).toString({ "${ChatColor.YELLOW}${it.name}${ChatColor.GRAY}" }, "")}")
                        }
                    }
                    return
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
            MainAPI.sendMessage(sp, ErrorMessage.PERMISSIONS_TEAM_ADMIN)
        } else if (label.equals("lhide", true)) {
            if (TosoGameAPI.isBroadCaster(sp)) {
                VisibilityManager.add(sp, VisibilityManager.VisibilityType.LIVE)
                sp.sendMessage("${MainAPI.getPrefix(PrefixType.SUCCESS)}あなたを非表示にしました。")
                return
            }
            MainAPI.sendMessage(sp, ErrorMessage.PERMISSIONS)
        } else if (label.equals("lshow", true)) {
            if (TosoGameAPI.isBroadCaster(sp)) {
                VisibilityManager.remove(sp, VisibilityManager.VisibilityType.LIVE)
                sp.sendMessage("${MainAPI.getPrefix(PrefixType.SUCCESS)}あなたを表示しました。")
                return
            }
            MainAPI.sendMessage(sp, ErrorMessage.PERMISSIONS)
        }
    }

    override fun onBlockCommand(bs: BlockCommandSender, cmd: Command, label: String, args: Array<String>) {
        if (label.equals("broadcaster", true) || label.equals("yt", true)) {
            if (args.isNotEmpty()) {
                when (args[0].toLowerCase()) {
                    "add" -> {
                        if (args.size != 1) {
                            val p = Bukkit.getPlayerExact(args[1])
                            if (p != null) {
                                if (!TosoGameAPI.isBroadCaster(p)) {
                                    PlayerManager.loadConfig(p).broadCaster = true
                                    bs.sendMessage("${MainAPI.getPrefix(PrefixType.SUCCESS)}${p.name}を配信者に追加しました。")
                                    return
                                }
                                bs.sendMessage("${MainAPI.getPrefix(PrefixType.ERROR)}${p.name}はすでに配信者になっています。")
                                return
                            }
                            MainAPI.sendOfflineMessage(bs, args[1])
                            return
                        }
                        MainAPI.sendMessage(bs, ErrorMessage.ARGS_PLAYER)
                    }
                    "remove" -> {
                        if (args.size != 1) {
                            val p = Bukkit.getPlayerExact(args[1])
                            if (p != null) {
                                if (TosoGameAPI.isBroadCaster(p)) {
                                    PlayerManager.loadConfig(p).broadCaster = false
                                    bs.sendMessage("${MainAPI.getPrefix(PrefixType.SUCCESS)}${p.name}を配信者から削除しました。")
                                    return
                                }
                                bs.sendMessage("${MainAPI.getPrefix(PrefixType.ERROR)}${p.name}はすでに配信者ではありません。")
                                return
                            }
                            MainAPI.sendOfflineMessage(bs, args[1])
                            return
                        }
                        MainAPI.sendMessage(bs, ErrorMessage.ARGS_PLAYER)
                    }
                    "list" -> {
                        bs.sendMessage("${MainAPI.getPrefix(PrefixType.WARNING)}配信者リスト: ${MainAPI.getOnlinePlayers(PlayerManager.configs.filter { it.value.broadCaster }.map { it.key }).toString({ "${ChatColor.YELLOW}${it.name}${ChatColor.GRAY}" }, "")}")
                    }
                }
                return
            }
            bs.sendMessage("""
                ${MainAPI.getPrefix(PrefixType.ERROR)}引数が不正です。
                ${MainAPI.getPrefix(PrefixType.ERROR)}コマンドの使い方:
                ${MainAPI.getPrefix(PrefixType.ERROR)}"/broadcaster add <プレイヤー名>" - 配信者を追加
                ${MainAPI.getPrefix(PrefixType.ERROR)}"/broadcaster remove <プレイヤー名>" - 配信者を削除
                ${MainAPI.getPrefix(PrefixType.ERROR)}"/broadcaster list" - 配信者リストを表示
            """.trimIndent())
        } else if (label.equals("lhide", true) || label.equals("lshow", true)) {
            MainAPI.sendMessage(bs, ErrorMessage.NOT_PLAYER)
        }
    }

    override fun onConsoleCommand(cs: ConsoleCommandSender, cmd: Command, label: String, args: Array<String>) {
        if (label.equals("broadcaster", true) || label.equals("yt", true)) {
            if (args.isNotEmpty()) {
                when (args[0].toLowerCase()) {
                    "add" -> {
                        if (args.size != 1) {
                            val p = Bukkit.getPlayerExact(args[1])
                            if (p != null) {
                                if (!TosoGameAPI.isBroadCaster(p)) {
                                    PlayerManager.loadConfig(p).broadCaster = true
                                    cs.sendMessage("${MainAPI.getPrefix(PrefixType.SUCCESS)}${p.name}を配信者に追加しました。")
                                    return
                                }
                                cs.sendMessage("${MainAPI.getPrefix(PrefixType.ERROR)}${p.name}はすでに配信者になっています。")
                                return
                            }
                            MainAPI.sendOfflineMessage(cs, args[1])
                            return
                        }
                        MainAPI.sendMessage(cs, ErrorMessage.ARGS_PLAYER)
                    }
                    "remove" -> {
                        if (args.size != 1) {
                            val p = Bukkit.getPlayerExact(args[1])
                            if (p != null) {
                                if (TosoGameAPI.isBroadCaster(p)) {
                                    PlayerManager.loadConfig(p).broadCaster = false
                                    cs.sendMessage("${MainAPI.getPrefix(PrefixType.SUCCESS)}${p.name}を配信者から削除しました。")
                                    return
                                }
                                cs.sendMessage("${MainAPI.getPrefix(PrefixType.ERROR)}${p.name}はすでに配信者ではありません。")
                                return
                            }
                            MainAPI.sendOfflineMessage(cs, args[1])
                            return
                        }
                        MainAPI.sendMessage(cs, ErrorMessage.ARGS_PLAYER)
                    }
                    "list" -> {
                        cs.sendMessage("${MainAPI.getPrefix(PrefixType.WARNING)}配信者リスト: ${MainAPI.getOnlinePlayers(PlayerManager.configs.filter { it.value.broadCaster }.map { it.key }).toString({ "${ChatColor.YELLOW}${it.name}${ChatColor.GRAY}" }, "")}")
                    }
                }
                return
            }
            cs.sendMessage("""
                ${MainAPI.getPrefix(PrefixType.ERROR)}引数が不正です。
                ${MainAPI.getPrefix(PrefixType.ERROR)}コマンドの使い方:
                ${MainAPI.getPrefix(PrefixType.ERROR)}"/broadcaster add <プレイヤー名>" - 配信者を追加
                ${MainAPI.getPrefix(PrefixType.ERROR)}"/broadcaster remove <プレイヤー名>" - 配信者を削除
                ${MainAPI.getPrefix(PrefixType.ERROR)}"/broadcaster list" - 配信者リストを表示
            """.trimIndent())
        } else if (label.equals("lhide", true) || label.equals("lshow", true)) {
            MainAPI.sendMessage(cs, ErrorMessage.NOT_PLAYER)
        }
    }

    override fun onPlayerTabComplete(sp: Player, cmd: Command, alias: String, args: Array<String>): List<String>? {
        if (!TosoGameAPI.isAdmin(sp) || args.size != 1) return emptyList()
        return getTabList(args[0], "add", "remove", "list")
    }

    override fun onBlockTabComplete(bs: BlockCommandSender, cmd: Command, alias: String, args: Array<String>): List<String>? {
        if (args.size != 1) return emptyList()
        return getTabList(args[0], "add", "remove", "list")
    }

    override fun onConsoleTabComplete(cs: ConsoleCommandSender, cmd: Command, alias: String, args: Array<String>): List<String>? {
        if (args.size != 1) return emptyList()
        return getTabList(args[0], "add", "remove", "list")
    }
}
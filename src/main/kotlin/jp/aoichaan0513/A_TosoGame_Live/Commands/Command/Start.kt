package jp.aoichaan0513.A_TosoGame_Live.Commands.Command

import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI
import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI.ErrorMessage
import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI.PrefixType
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.GameManager
import jp.aoichaan0513.A_TosoGame_Live.API.TosoGameAPI
import jp.aoichaan0513.A_TosoGame_Live.Commands.ICommand
import jp.aoichaan0513.A_TosoGame_Live.Main
import jp.aoichaan0513.A_TosoGame_Live.Utils.ParseUtil
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.BlockCommandSender
import org.bukkit.command.Command
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.entity.Player

class Start(name: String) : ICommand(name) {

    override fun onPlayerCommand(sp: Player, cmd: Command, label: String, args: Array<String>) {
        if (TosoGameAPI.isAdmin(sp)) {
            if (!GameManager.isGame()) {
                if (GameManager.isGame(GameManager.GameState.NONE)) {
                    val worldConfig = Main.worldConfig
                    if (args.isNotEmpty()) {
                        if (args[0].equals("skip", true)) {
                            GameManager.startGame(1, worldConfig.gameConfig.game)
                            return
                        } else {
                            if (ParseUtil.isInt(args[0])) {
                                val i = args[0].toInt()
                                Bukkit.broadcastMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}カウントダウン開始")
                                GameManager.startGame(i + 1, worldConfig.gameConfig.game)
                                return
                            } else {
                                MainAPI.sendMessage(sp, ErrorMessage.ARGS_INTEGER)
                                return
                            }
                        }
                    } else {
                        Bukkit.broadcastMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}カウントダウン開始")
                        GameManager.startGame(worldConfig.gameConfig.countDown, worldConfig.gameConfig.game)
                        return
                    }
                }
                sp.sendMessage("""
                    ${MainAPI.getPrefix(PrefixType.ERROR)}ゲームが終了しているため実行できません。
                    ${MainAPI.getPrefix(PrefixType.SECONDARY)}"${ChatColor.RED}${ChatColor.UNDERLINE}/reset${ChatColor.RESET}${ChatColor.GRAY}"でゲームをリセットしてから実行してください。
                """.trimIndent())
                return
            }
            MainAPI.sendMessage(sp, ErrorMessage.GAME)
            return
        }
        MainAPI.sendMessage(sp, ErrorMessage.PERMISSIONS)
    }

    override fun onBlockCommand(bs: BlockCommandSender, cmd: Command, label: String, args: Array<String>) {
        if (!GameManager.isGame()) {
            if (GameManager.isGame(GameManager.GameState.NONE)) {
                val worldConfig = Main.worldConfig
                if (args.isNotEmpty()) {
                    if (args[0].equals("skip", true)) {
                        GameManager.startGame(1, worldConfig.gameConfig.game)
                        return
                    } else {
                        if (ParseUtil.isInt(args[0])) {
                            val i = args[0].toInt()
                            Bukkit.broadcastMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}カウントダウン開始")
                            GameManager.startGame(i + 1, worldConfig.gameConfig.game)
                            return
                        } else {
                            MainAPI.sendMessage(bs, ErrorMessage.ARGS_INTEGER)
                            return
                        }
                    }
                } else {
                    Bukkit.broadcastMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}カウントダウン開始")
                    GameManager.startGame(worldConfig.gameConfig.countDown, worldConfig.gameConfig.game)
                    return
                }
            }
            bs.sendMessage("""
                ${MainAPI.getPrefix(PrefixType.ERROR)}ゲームが終了しているため実行できません。
                ${MainAPI.getPrefix(PrefixType.SECONDARY)}"${ChatColor.RED}${ChatColor.UNDERLINE}reset${ChatColor.RESET}${ChatColor.GRAY}"でゲームをリセットしてから実行してください。
            """.trimIndent())
            return
        }
        MainAPI.sendMessage(bs, ErrorMessage.GAME)
    }

    override fun onConsoleCommand(cs: ConsoleCommandSender, cmd: Command, label: String, args: Array<String>) {
        MainAPI.sendMessage(cs, ErrorMessage.NOT_PLAYER)
        return
    }

    override fun onPlayerTabComplete(sp: Player, cmd: Command, alias: String, args: Array<String>): List<String>? {
        if (!TosoGameAPI.isAdmin(sp) || args.size != 1) return null
        return getTabList(args[0], "skip")
    }

    override fun onBlockTabComplete(bs: BlockCommandSender, cmd: Command, alias: String, args: Array<String>): List<String>? {
        if (args.size != 1) return null
        return getTabList(args[0], "skip")
    }

    override fun onConsoleTabComplete(cs: ConsoleCommandSender, cmd: Command, alias: String, args: Array<String>): List<String>? {
        return null
    }
}
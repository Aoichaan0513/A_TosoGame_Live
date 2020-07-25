package jp.aoichaan0513.A_TosoGame_Live.Commands.Command

import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI
import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI.ErrorMessage
import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI.PrefixType
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.GameManager
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.GameManager.GameState
import jp.aoichaan0513.A_TosoGame_Live.API.Scoreboard.Teams
import jp.aoichaan0513.A_TosoGame_Live.API.Scoreboard.Teams.OnlineTeam
import jp.aoichaan0513.A_TosoGame_Live.API.TosoGameAPI
import jp.aoichaan0513.A_TosoGame_Live.Commands.ICommand
import jp.aoichaan0513.A_TosoGame_Live.OPGame.Dice
import jp.aoichaan0513.A_TosoGame_Live.Utils.ParseUtil
import org.bukkit.command.BlockCommandSender
import org.bukkit.command.Command
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.entity.Player

class OPGame(name: String) : ICommand(name) {

    override fun onPlayerCommand(sp: Player, cmd: Command, label: String, args: Array<String>) {
        if (TosoGameAPI.isAdmin(sp)) {
            if (GameManager.isGame(GameState.NONE)) {
                if (args.isNotEmpty()) {
                    if (ParseUtil.isInt(args[0])) {
                        val i = args[0].toInt()

                        when (i) {
                            1 -> {
                                if (Teams.getOnlineCount(OnlineTeam.TOSO_PLAYER) > 0) {
                                    Dice.start(sp)
                                    return
                                }
                                sp.sendMessage("${MainAPI.getPrefix(PrefixType.ERROR)}逃走者の人数が少ないためオープニングゲームを実行できません。")
                                return
                            }
                            2 -> return
                        }
                    } else {
                        sp.sendMessage("""
                            ${MainAPI.getPrefix(PrefixType.ERROR)}引数が不正です。数字を指定してください。
                            ${MainAPI.getPrefix(PrefixType.ERROR)}コマンドの使い方:
                            ${MainAPI.getPrefix(PrefixType.ERROR)}"/opgame 1" - サイコロミッション
                            ${MainAPI.getPrefix(PrefixType.ERROR)}"/opgame 2" - 実装中
                        """.trimIndent())
                        return
                    }
                }
                sp.sendMessage("""
                    ${MainAPI.getPrefix(PrefixType.ERROR)}引数が不正です。数字を指定してください。
                    ${MainAPI.getPrefix(PrefixType.ERROR)}コマンドの使い方:
                    ${MainAPI.getPrefix(PrefixType.ERROR)}"/opgame 1" - サイコロミッション
                    ${MainAPI.getPrefix(PrefixType.ERROR)}"/opgame 2" - 実装中
                """.trimIndent())
                return
            }
            sp.sendMessage("${MainAPI.getPrefix(PrefixType.ERROR)}ゲームが実行されているため実行できません。")
            return
        }
        MainAPI.sendMessage(sp, ErrorMessage.PERMISSIONS_TEAM_ADMIN)
    }

    override fun onBlockCommand(bs: BlockCommandSender, cmd: Command, label: String, args: Array<String>) {
        if (GameManager.isGame(GameState.NONE)) {
            if (args.isNotEmpty()) {
                if (ParseUtil.isInt(args[0])) {
                    val i = args[0].toInt()

                    when (i) {
                        1 -> {
                            if (Teams.getOnlineCount(OnlineTeam.TOSO_PLAYER) > 0) {
                                Dice.start(bs)
                                return
                            }
                            bs.sendMessage("${MainAPI.getPrefix(PrefixType.ERROR)}逃走者の人数が少ないためオープニングゲームを実行できません。")
                            return
                        }
                        2 -> return
                    }
                } else {
                    bs.sendMessage("""
                        ${MainAPI.getPrefix(PrefixType.ERROR)}引数が不正です。数字を指定してください。
                        ${MainAPI.getPrefix(PrefixType.ERROR)}コマンドの使い方:
                        ${MainAPI.getPrefix(PrefixType.ERROR)}"/opgame 1" - サイコロミッション
                        ${MainAPI.getPrefix(PrefixType.ERROR)}"/opgame 2" - 実装中
                    """.trimIndent())
                    return
                }
            }
            bs.sendMessage("""
                ${MainAPI.getPrefix(PrefixType.ERROR)}引数が不正です。数字を指定してください。
                ${MainAPI.getPrefix(PrefixType.ERROR)}コマンドの使い方:
                ${MainAPI.getPrefix(PrefixType.ERROR)}"/opgame 1" - サイコロミッション
                ${MainAPI.getPrefix(PrefixType.ERROR)}"/opgame 2" - 実装中
            """.trimIndent())
            return
        }
        bs.sendMessage("${MainAPI.getPrefix(PrefixType.ERROR)}ゲームが実行されているため実行できません。")
    }

    override fun onConsoleCommand(cs: ConsoleCommandSender, cmd: Command, label: String, args: Array<String>) {
        MainAPI.sendMessage(cs, ErrorMessage.NOT_PLAYER)
    }

    override fun onPlayerTabComplete(sp: Player, cmd: Command, alias: String, args: Array<String>): List<String>? {
        if (!TosoGameAPI.isAdmin(sp) || args.size != 1) return emptyList()
        return getTabList(args[0], "1", "2")
    }

    override fun onBlockTabComplete(bs: BlockCommandSender, cmd: Command, alias: String, args: Array<String>): List<String>? {
        if (args.size != 1) return emptyList()
        return getTabList(args[0], "1", "2")
    }

    override fun onConsoleTabComplete(cs: ConsoleCommandSender, cmd: Command, alias: String, args: Array<String>): List<String>? {
        return emptyList()
    }
}
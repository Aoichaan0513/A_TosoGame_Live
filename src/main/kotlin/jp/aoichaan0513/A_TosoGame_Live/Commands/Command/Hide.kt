package jp.aoichaan0513.A_TosoGame_Live.Commands.Command

import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI
import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI.ErrorMessage
import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI.PrefixType
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.GameManager
import jp.aoichaan0513.A_TosoGame_Live.API.Scoreboard.Teams
import jp.aoichaan0513.A_TosoGame_Live.API.Scoreboard.Teams.OnlineTeam
import jp.aoichaan0513.A_TosoGame_Live.API.TosoGameAPI
import jp.aoichaan0513.A_TosoGame_Live.Commands.ICommand
import org.bukkit.Bukkit
import org.bukkit.command.BlockCommandSender
import org.bukkit.command.Command
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.entity.Player

class Hide(name: String) : ICommand(name) {

    override fun onPlayerCommand(sp: Player, cmd: Command, label: String, args: Array<String>) {
        if (GameManager.isGame(GameManager.GameState.GAME)) {
            if (Teams.hasJoinedTeam(OnlineTeam.TOSO_JAIL, sp)) {
                if (args.isNotEmpty()) {
                    if (args[0].equals("admin", true)) {
                        for (player in Bukkit.getOnlinePlayers())
                            sp.hidePlayer(player)
                        sp.sendMessage("${MainAPI.getPrefix(PrefixType.SUCCESS)}周りを非表示にしました。(運営を含む)")
                        return
                    }
                    sp.sendMessage("""
                        ${MainAPI.getPrefix(PrefixType.ERROR)}引数が不正です。
                        ${MainAPI.getPrefix(PrefixType.ERROR)}コマンドの使い方:
                        ${MainAPI.getPrefix(PrefixType.ERROR)}"/hide" - 運営以外を非表示
                        ${MainAPI.getPrefix(PrefixType.ERROR)}"/hide admin" - 運営も非表示
                    """.trimIndent())
                    return
                } else {
                    for (player in Bukkit.getOnlinePlayers().filterNot { TosoGameAPI.isAdmin(it) })
                        sp.hidePlayer(player)
                    sp.sendMessage("${MainAPI.getPrefix(PrefixType.SUCCESS)}周りを非表示にしました。")
                    return
                }
            }
            MainAPI.sendMessage(sp, ErrorMessage.PERMISSIONS_TEAM_JAIL)
            return
        }
        MainAPI.sendMessage(sp, ErrorMessage.NOT_GAME)
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
        if (args.size != 1) return null
        return getTabList(args[0], "admin")
    }

    override fun onBlockTabComplete(bs: BlockCommandSender, cmd: Command, alias: String, args: Array<String>): List<String>? {
        return null
    }

    override fun onConsoleTabComplete(cs: ConsoleCommandSender, cmd: Command, alias: String, args: Array<String>): List<String>? {
        return null
    }
}
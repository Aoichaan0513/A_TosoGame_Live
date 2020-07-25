package jp.aoichaan0513.A_TosoGame_Live.Commands.Command

import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI
import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI.ErrorMessage
import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI.PrefixType
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.GameManager
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.Player.VisibilityManager
import jp.aoichaan0513.A_TosoGame_Live.API.Scoreboard.Teams
import jp.aoichaan0513.A_TosoGame_Live.API.Scoreboard.Teams.OnlineTeam
import jp.aoichaan0513.A_TosoGame_Live.Commands.ICommand
import org.bukkit.command.BlockCommandSender
import org.bukkit.command.Command
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.entity.Player

class Show(name: String) : ICommand(name) {

    override fun onPlayerCommand(sp: Player, cmd: Command, label: String, args: Array<String>) {
        if (GameManager.isGame(GameManager.GameState.GAME)) {
            if (Teams.hasJoinedTeam(OnlineTeam.TOSO_JAIL, sp)) {
                VisibilityManager.removeJailHide(sp)

                sp.sendMessage("${MainAPI.getPrefix(PrefixType.SUCCESS)}周りを表示しました。")
                return
            }
            MainAPI.sendMessage(sp, ErrorMessage.PERMISSIONS_TEAM_JAIL)
            return
        }
        MainAPI.sendMessage(sp, ErrorMessage.NOT_GAME)
    }

    override fun onBlockCommand(bs: BlockCommandSender, cmd: Command, label: String, args: Array<String>) {
        MainAPI.sendMessage(bs, ErrorMessage.NOT_PLAYER)
    }

    override fun onConsoleCommand(cs: ConsoleCommandSender, cmd: Command, label: String, args: Array<String>) {
        MainAPI.sendMessage(cs, ErrorMessage.NOT_PLAYER)
    }

    override fun onPlayerTabComplete(sp: Player, cmd: Command, alias: String, args: Array<String>): List<String>? {
        return emptyList()
    }

    override fun onBlockTabComplete(bs: BlockCommandSender, cmd: Command, alias: String, args: Array<String>): List<String>? {
        return emptyList()
    }

    override fun onConsoleTabComplete(cs: ConsoleCommandSender, cmd: Command, alias: String, args: Array<String>): List<String>? {
        return emptyList()
    }
}
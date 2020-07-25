package jp.aoichaan0513.A_TosoGame_Live.Commands.Command

import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI
import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI.ErrorMessage
import jp.aoichaan0513.A_TosoGame_Live.API.Scoreboard.Teams
import jp.aoichaan0513.A_TosoGame_Live.API.Scoreboard.Teams.OnlineTeam
import jp.aoichaan0513.A_TosoGame_Live.Commands.ICommand
import jp.aoichaan0513.A_TosoGame_Live.Inventory.MissionInventory
import jp.aoichaan0513.A_TosoGame_Live.Mission.MissionManager
import jp.aoichaan0513.A_TosoGame_Live.Utils.ParseUtil
import org.bukkit.command.BlockCommandSender
import org.bukkit.command.Command
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.entity.Player

class Menu(name: String) : ICommand(name) {

    override fun onPlayerCommand(sp: Player, cmd: Command, label: String, args: Array<String>) {
        if (args.isNotEmpty()) {
            if (args[0].equals("open", true)) {
                if (Teams.hasJoinedTeam(OnlineTeam.TOSO_ADMIN, sp) || Teams.hasJoinedTeam(OnlineTeam.TOSO_PLAYER, sp) || Teams.hasJoinedTeam(OnlineTeam.TOSO_SUCCESS, sp)) {
                    if (args.size != 1 && args.size != 2) {
                        if (!ParseUtil.isInt(args[2])) return

                        MissionInventory.openBook(sp, args[2].toInt(), when (args[1]) {
                            "mission" -> MissionManager.MissionType.MISSION
                            "tutatu", "hint" -> MissionManager.MissionType.TUTATU_HINT
                            else -> MissionManager.MissionType.END
                        })
                    }
                }
            }
        }
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
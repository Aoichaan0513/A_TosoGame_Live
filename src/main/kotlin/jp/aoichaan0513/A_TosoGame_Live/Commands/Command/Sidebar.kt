package jp.aoichaan0513.A_TosoGame_Live.Commands.Command

import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI
import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI.ErrorMessage
import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI.PrefixType
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.GameManager
import jp.aoichaan0513.A_TosoGame_Live.API.Scoreboard.Scoreboard
import jp.aoichaan0513.A_TosoGame_Live.API.Scoreboard.Teams
import jp.aoichaan0513.A_TosoGame_Live.API.Scoreboard.Teams.OnlineTeam
import jp.aoichaan0513.A_TosoGame_Live.API.TosoGameAPI
import jp.aoichaan0513.A_TosoGame_Live.Commands.ICommand
import jp.aoichaan0513.A_TosoGame_Live.Main
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.command.BlockCommandSender
import org.bukkit.command.Command
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.entity.Player
import org.bukkit.scoreboard.DisplaySlot

class Sidebar(name: String) : ICommand(name) {

    override fun onPlayerCommand(sp: Player, cmd: Command, label: String, args: Array<String>) {
        if (!Scoreboard.isHidePlayer(sp)) {
            Scoreboard.addHidePlayer(sp)

            val board = Scoreboard.getBoard(sp)
            board.getObjective(TosoGameAPI.Objective.SIDEBAR.objectName)?.setDisplaySlot(DisplaySlot.SIDEBAR)
            sp.sendMessage("${MainAPI.getPrefix(PrefixType.SUCCESS)}サイドバーを常時表示に変更しました。")
            return
        } else {
            Scoreboard.removeHidePlayer(sp)

            val board = Scoreboard.getBoard(sp)
            if (GameManager.isGame() && (Teams.hasJoinedTeam(OnlineTeam.TOSO_PLAYER, sp) || Teams.hasJoinedTeam(OnlineTeam.TOSO_SUCCESS, sp))) {
                if (sp.inventory.itemInMainHand.type == Material.BOOK) {
                    val itemMeta = sp.inventory.itemInMainHand.itemMeta

                    if (itemMeta != null && ChatColor.stripColor(itemMeta.displayName) == Main.PHONE_ITEM_NAME)
                        board.getObjective(TosoGameAPI.Objective.SIDEBAR.objectName)?.setDisplaySlot(DisplaySlot.SIDEBAR)
                    else
                        board.clearSlot(DisplaySlot.SIDEBAR)
                } else if (sp.inventory.itemInOffHand.type == Material.BOOK) {
                    val itemMeta = sp.inventory.itemInOffHand.itemMeta

                    if (itemMeta != null && ChatColor.stripColor(itemMeta.displayName) == Main.PHONE_ITEM_NAME)
                        board.getObjective(TosoGameAPI.Objective.SIDEBAR.objectName)?.setDisplaySlot(DisplaySlot.SIDEBAR)
                    else
                        board.clearSlot(DisplaySlot.SIDEBAR)
                } else {
                    board.clearSlot(DisplaySlot.SIDEBAR)
                }
            } else {
                board.getObjective(TosoGameAPI.Objective.SIDEBAR.objectName)?.setDisplaySlot(DisplaySlot.SIDEBAR)
            }
            sp.sendMessage("${MainAPI.getPrefix(PrefixType.SUCCESS)}サイドバーを自動切り替えに変更しました。")
            return
        }
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
        return null
    }

    override fun onBlockTabComplete(bs: BlockCommandSender, cmd: Command, alias: String, args: Array<String>): List<String>? {
        return null
    }

    override fun onConsoleTabComplete(cs: ConsoleCommandSender, cmd: Command, alias: String, args: Array<String>): List<String>? {
        return null
    }
}
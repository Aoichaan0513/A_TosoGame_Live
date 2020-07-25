package jp.aoichaan0513.A_TosoGame_Live.Commands.Command

import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI
import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI.ErrorMessage
import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI.PrefixType
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.GameManager
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.GameManager.GameState
import jp.aoichaan0513.A_TosoGame_Live.API.Scoreboard.Teams
import jp.aoichaan0513.A_TosoGame_Live.API.Scoreboard.Teams.OnlineTeam
import jp.aoichaan0513.A_TosoGame_Live.Commands.ICommand
import jp.aoichaan0513.A_TosoGame_Live.Main
import org.bukkit.ChatColor
import org.bukkit.command.BlockCommandSender
import org.bukkit.command.Command
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.entity.Player

class T(name: String) : ICommand(name) {

    override fun onPlayerCommand(sp: Player, cmd: Command, label: String, args: Array<String>) {
        if (Team.isTuhoRandom) {
            if (GameManager.isGame(GameState.GAME)) {
                if (Teams.hasJoinedTeam(OnlineTeam.TOSO_JAIL, sp)) {
                    if (!Main.tuhoShuffleSet.contains(sp.uniqueId)) Main.tuhoShuffleSet.add(sp.uniqueId) else Main.tuhoShuffleSet.remove(sp.uniqueId)
                    sp.sendMessage("${MainAPI.getPrefix(PrefixType.WARNING)}${ChatColor.UNDERLINE}通報部隊募集${ChatColor.GOLD}${ChatColor.UNDERLINE}${if (Main.hunterShuffleSet.contains(sp.uniqueId)) "に応募" else "の応募をキャンセル"}${ChatColor.RESET}${ChatColor.YELLOW}しました。")
                    return
                }
                MainAPI.sendMessage(sp, ErrorMessage.PERMISSIONS_TEAM_JAIL)
                return
            } else {
                if (!Main.tuhoShuffleSet.contains(sp.uniqueId)) Main.tuhoShuffleSet.add(sp.uniqueId) else Main.tuhoShuffleSet.remove(sp.uniqueId)
                sp.sendMessage("${MainAPI.getPrefix(PrefixType.WARNING)}${ChatColor.UNDERLINE}通報部隊募集${ChatColor.GOLD}${ChatColor.UNDERLINE}${if (Main.hunterShuffleSet.contains(sp.uniqueId)) "に応募" else "の応募をキャンセル"}${ChatColor.RESET}${ChatColor.YELLOW}しました。")
                return
            }
        }
        sp.sendMessage("${MainAPI.getPrefix(PrefixType.ERROR)}通報部隊を募集していないため実行できません。")
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
        return null
    }

    override fun onBlockTabComplete(bs: BlockCommandSender, cmd: Command, alias: String, args: Array<String>): List<String>? {
        return null
    }

    override fun onConsoleTabComplete(cs: ConsoleCommandSender, cmd: Command, alias: String, args: Array<String>): List<String>? {
        return null
    }
}
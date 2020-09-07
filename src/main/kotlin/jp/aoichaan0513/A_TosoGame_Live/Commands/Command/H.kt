package jp.aoichaan0513.A_TosoGame_Live.Commands.Command

import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI
import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI.ErrorMessage
import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI.PrefixType
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.GameManager
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.GameManager.GameState
import jp.aoichaan0513.A_TosoGame_Live.Commands.ICommand
import jp.aoichaan0513.A_TosoGame_Live.Main
import jp.aoichaan0513.A_TosoGame_Live.Utils.isHunterGroup
import jp.aoichaan0513.A_TosoGame_Live.Utils.isJailTeam
import org.bukkit.ChatColor
import org.bukkit.command.BlockCommandSender
import org.bukkit.command.Command
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.entity.Player

class H(name: String) : ICommand(name) {

    override fun onPlayerCommand(sp: Player, cmd: Command, label: String, args: Array<String>) {
        if (Team.isHunterRandom || !sp.isHunterGroup) {
            if (GameManager.isGame(GameState.GAME)) {
                if (sp.isJailTeam) {
                    if (!Main.hunterShuffleSet.contains(sp.uniqueId)) Main.hunterShuffleSet.add(sp.uniqueId) else Main.hunterShuffleSet.remove(sp.uniqueId)
                    sp.sendMessage("${MainAPI.getPrefix(PrefixType.WARNING)}${ChatColor.UNDERLINE}ハンター募集${ChatColor.GOLD}${ChatColor.UNDERLINE}${if (Main.hunterShuffleSet.contains(sp.uniqueId)) "に応募" else "の応募をキャンセル"}${ChatColor.RESET}${ChatColor.YELLOW}しました。")
                    return
                }
                MainAPI.sendMessage(sp, ErrorMessage.PERMISSIONS_TEAM_JAIL)
            } else {
                if (!Main.hunterShuffleSet.contains(sp.uniqueId)) Main.hunterShuffleSet.add(sp.uniqueId) else Main.hunterShuffleSet.remove(sp.uniqueId)
                sp.sendMessage("${MainAPI.getPrefix(PrefixType.WARNING)}${ChatColor.UNDERLINE}ハンター募集${ChatColor.GOLD}${ChatColor.UNDERLINE}${if (Main.hunterShuffleSet.contains(sp.uniqueId)) "に応募" else "の応募をキャンセル"}${ChatColor.RESET}${ChatColor.YELLOW}しました。")
            }
            return
        }
        sp.sendMessage("${MainAPI.getPrefix(PrefixType.ERROR)}ハンターを募集していないため実行できません。")
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
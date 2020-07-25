package jp.aoichaan0513.A_TosoGame_Live.Commands.Command

import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI
import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI.ErrorMessage
import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI.PrefixType
import jp.aoichaan0513.A_TosoGame_Live.Commands.ICommand
import jp.aoichaan0513.A_TosoGame_Live.OPGame.Dice
import jp.aoichaan0513.A_TosoGame_Live.OPGame.OPGameManager
import org.bukkit.command.BlockCommandSender
import org.bukkit.command.Command
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.entity.Player

class Shuffle(name: String) : ICommand(name) {

    override fun onPlayerCommand(sp: Player, cmd: Command, label: String, args: Array<String>) {
        val opGamePlayer = OPGameManager.player

        if (opGamePlayer != null) {
            if (opGamePlayer.uniqueId === sp.uniqueId) {
                if (!OPGameManager.isRunned) {
                    Dice.shuffle()
                    return
                }
                sp.sendMessage("${MainAPI.getPrefix(PrefixType.ERROR)}すでにサイコロを振ったため実行できません。")
                return
            }
            sp.sendMessage("${MainAPI.getPrefix(PrefixType.ERROR)}現在実行できません。")
            return
        }
        sp.sendMessage("${MainAPI.getPrefix(PrefixType.ERROR)}現在実行できません。")
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
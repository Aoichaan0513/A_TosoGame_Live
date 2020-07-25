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
import jp.aoichaan0513.A_TosoGame_Live.Main
import jp.aoichaan0513.A_TosoGame_Live.Runnable.RespawnRunnable
import org.bukkit.GameMode
import org.bukkit.command.BlockCommandSender
import org.bukkit.command.Command
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.entity.Player

class Spec(name: String) : ICommand(name) {

    override fun onPlayerCommand(sp: Player, cmd: Command, label: String, args: Array<String>) {
        if (GameManager.isGame(GameState.GAME)) {
            if (Teams.hasJoinedTeam(OnlineTeam.TOSO_JAIL, sp)) {
                if (!TosoGameAPI.isRes || !RespawnRunnable.isAllowRespawn(sp)) {
                    val worldConfig = Main.worldConfig
                    if (sp.gameMode == GameMode.ADVENTURE) {
                        sp.gameMode = GameMode.SPECTATOR
                        sp.inventory.heldItemSlot = 0
                        TosoGameAPI.teleport(sp, worldConfig.respawnLocationConfig.locations.values)
                        sp.sendMessage("""
                            ${MainAPI.getPrefix(PrefixType.WARNING)}観戦モードになりました。
                            ${MainAPI.getPrefix(PrefixType.SECONDARY)}"/spec"で観戦モードから戻れます。
                        """.trimIndent())
                        return
                    } else {
                        sp.gameMode = GameMode.ADVENTURE
                        TosoGameAPI.teleport(sp, worldConfig.jailLocationConfig.locations.values)
                        sp.sendMessage("${MainAPI.getPrefix(PrefixType.WARNING)}観戦モードから戻りました。")
                        return
                    }
                }
                sp.sendMessage("${MainAPI.getPrefix(PrefixType.ERROR)}途中参加・復活が可能なため実行できません。")
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
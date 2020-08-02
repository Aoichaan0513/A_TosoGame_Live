package jp.aoichaan0513.A_TosoGame_Live.Commands.Command

import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI
import jp.aoichaan0513.A_TosoGame_Live.Commands.ICommand
import jp.aoichaan0513.A_TosoGame_Live.Mission.HunterZone
import jp.aoichaan0513.A_TosoGame_Live.Utils.isHunterTeam
import org.bukkit.ChatColor
import org.bukkit.command.BlockCommandSender
import org.bukkit.command.Command
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.entity.Player

class HZ(name: String) : ICommand(name) {

    override fun onPlayerCommand(sp: Player, cmd: Command, label: String, args: Array<String>) {
        if (sp.isHunterTeam) {
            if (!HunterZone.isStart) {
                if (!HunterZone.containsHunterSet(sp)) HunterZone.addHunterSet(sp) else HunterZone.removeHunterSet(sp)
                sp.sendMessage("""
                    ${MainAPI.getPrefix(MainAPI.PrefixType.WARNING)}${ChatColor.UNDERLINE}ハンターゾーンミッションのハンター${ChatColor.GOLD}${ChatColor.UNDERLINE}${if (HunterZone.containsHunterSet(sp)) "に応募" else "の応募をキャンセル"}${ChatColor.RESET}${ChatColor.YELLOW}しました。
                    ${MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY)}再度実行することで${if (!HunterZone.containsHunterSet(sp)) "${ChatColor.UNDERLINE}応募${ChatColor.RESET}${ChatColor.GRAY}" else "${ChatColor.UNDERLINE}応募をキャンセル${ChatColor.RESET}${ChatColor.GRAY}すること"}ができます。
                """.trimIndent())
            } else {
                sp.sendMessage("${MainAPI.getPrefix(MainAPI.PrefixType.ERROR)}ハンターゾーンミッションを実行したため実行できません。")
            }
            return
        }
        MainAPI.sendMessage(sp, MainAPI.ErrorMessage.PERMISSIONS_TEAM_HUNTER)
    }

    override fun onBlockCommand(bs: BlockCommandSender, cmd: Command, label: String, args: Array<String>) {
        MainAPI.sendMessage(bs, MainAPI.ErrorMessage.NOT_PLAYER)
    }

    override fun onConsoleCommand(cs: ConsoleCommandSender, cmd: Command, label: String, args: Array<String>) {
        MainAPI.sendMessage(cs, MainAPI.ErrorMessage.NOT_PLAYER)
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
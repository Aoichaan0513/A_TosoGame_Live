package jp.aoichaan0513.A_TosoGame_Live.Commands.Command

import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI
import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI.ErrorMessage
import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI.PrefixType
import jp.aoichaan0513.A_TosoGame_Live.API.TosoGameAPI
import jp.aoichaan0513.A_TosoGame_Live.Commands.ICommand
import org.bukkit.Bukkit
import org.bukkit.command.BlockCommandSender
import org.bukkit.command.Command
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.entity.Player

class Ride(name: String) : ICommand(name) {
    override fun onPlayerCommand(sp: Player, cmd: Command, label: String, args: Array<String>) {
        if (TosoGameAPI.isAdmin(sp)) {
            if (args.isNotEmpty()) {
                val target = Bukkit.getPlayerExact(args[0])
                if (target == null) {
                    MainAPI.sendOfflineMessage(sp, args[0])
                    return
                } else {
                    val p = Bukkit.getPlayerExact(args[0])
                    if (sp.uniqueId != p!!.uniqueId) {
                        p.setPassenger(sp)
                        sp.sendMessage(MainAPI.getPrefix(PrefixType.SECONDARY) + p.name + "に乗りました。")
                        return
                    }
                    sp.sendMessage("${MainAPI.getPrefix(PrefixType.ERROR)}自分自身には乗れません。")
                    return
                }
            }
            sp.sendMessage("${MainAPI.getPrefix(PrefixType.ERROR)}引数が不正です。プレイヤーを指定してください。")
            return
        }
        MainAPI.sendMessage(sp, ErrorMessage.PERMISSIONS_TEAM_ADMIN)
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
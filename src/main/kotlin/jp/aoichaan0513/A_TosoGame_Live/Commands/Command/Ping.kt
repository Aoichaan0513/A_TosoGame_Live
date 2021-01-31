package jp.aoichaan0513.A_TosoGame_Live.Commands.Command

import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI
import jp.aoichaan0513.A_TosoGame_Live.API.TosoGameAPI
import jp.aoichaan0513.A_TosoGame_Live.Commands.ICommand
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.BlockCommandSender
import org.bukkit.command.Command
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer
import org.bukkit.entity.Player

class Ping(name: String) : ICommand(name) {

    override fun onPlayerCommand(sp: Player, cmd: Command, label: String, args: Array<String>) {
        if (args.isNotEmpty()) {
            if (TosoGameAPI.isAdmin(sp)) {
                val p = Bukkit.getPlayerExact(args[0])
                if (p != null) {
                    sp.sendMessage("${MainAPI.getPrefix(MainAPI.PrefixType.WARNING)}${p.name}'s Pong! ${ChatColor.YELLOW}${getPing(p)}${ChatColor.GOLD}ms")
                    return
                }
                MainAPI.sendOfflineMessage(sp, args[0])
                return
            }
        }
        sp.sendMessage("${MainAPI.getPrefix(MainAPI.PrefixType.WARNING)}Pong! ${ChatColor.YELLOW}${getPing(sp)}${ChatColor.GOLD}ms")
    }

    override fun onBlockCommand(bs: BlockCommandSender, cmd: Command, label: String, args: Array<String>) {
        MainAPI.sendMessage(bs, MainAPI.ErrorMessage.NOT_PLAYER)
    }

    override fun onConsoleCommand(cs: ConsoleCommandSender, cmd: Command, label: String, args: Array<String>) {
        MainAPI.sendMessage(cs, MainAPI.ErrorMessage.NOT_PLAYER)
    }

    override fun onPlayerTabComplete(sp: Player, cmd: Command, alias: String, args: Array<String>): List<String>? {
        if (!TosoGameAPI.isAdmin(sp)) return emptyList()
        return null
    }

    override fun onBlockTabComplete(bs: BlockCommandSender, cmd: Command, alias: String, args: Array<String>): List<String>? {
        return emptyList()
    }

    override fun onConsoleTabComplete(cs: ConsoleCommandSender, cmd: Command, alias: String, args: Array<String>): List<String>? {
        return emptyList()
    }


    private fun getPing(p: Player): Int {
        if (p !is CraftPlayer) return 0
        return p.handle.ping
    }
}
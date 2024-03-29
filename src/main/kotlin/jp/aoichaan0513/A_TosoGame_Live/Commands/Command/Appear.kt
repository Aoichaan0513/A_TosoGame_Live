package jp.aoichaan0513.A_TosoGame_Live.Commands.Command

import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.Player.PlayerManager
import jp.aoichaan0513.A_TosoGame_Live.API.TosoGameAPI
import jp.aoichaan0513.A_TosoGame_Live.Commands.ICommand
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.BlockCommandSender
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.entity.Player

class Appear(name: String) : ICommand(name) {

    override fun onPlayerCommand(sp: Player, cmd: Command, label: String, args: Array<String>) {
        if (TosoGameAPI.isAdmin(sp)) {
            if (args.isNotEmpty()) {
                for (name in args.filter { it.isNotEmpty() }) {
                    val p = Bukkit.getPlayerExact(name)
                    if (p != null) {
                        val playerConfig = PlayerManager.loadConfig(p)
                        if (playerConfig.visibility) {
                            playerConfig.visibility = false
                            sp.sendMessage("${MainAPI.getPrefix(MainAPI.PrefixType.SUCCESS)}${ChatColor.BOLD}${ChatColor.UNDERLINE}${p.name}${ChatColor.RESET}${ChatColor.GREEN}の姿を表示しました。")
                            continue
                        }
                        sp.sendMessage("${MainAPI.getPrefix(MainAPI.PrefixType.ERROR)}${ChatColor.BOLD}${ChatColor.UNDERLINE}${p.name}${ChatColor.RESET}${ChatColor.RED}はすでに姿が表示されています。")
                        continue
                    }
                    MainAPI.sendOfflineMessage(sp, name)
                }
                return
            } else {
                val playerConfig = PlayerManager.loadConfig(sp)
                if (playerConfig.visibility) {
                    playerConfig.visibility = false
                    sp.sendMessage("${MainAPI.getPrefix(MainAPI.PrefixType.SUCCESS)}あなたの姿を表示しました。")
                    return
                }
                sp.sendMessage("${MainAPI.getPrefix(MainAPI.PrefixType.ERROR)}すでに姿が表示されています。")
                return
            }
        }
        MainAPI.sendMessage(sp, MainAPI.ErrorMessage.PERMISSIONS_TEAM_ADMIN)
    }

    override fun onBlockCommand(bs: BlockCommandSender, cmd: Command, label: String, args: Array<String>) {
        if (args.isNotEmpty()) {
            for (name in args.filter { it.isNotEmpty() }) {
                val p = Bukkit.getPlayerExact(name)
                if (p != null) {
                    val playerConfig = PlayerManager.loadConfig(p)
                    if (playerConfig.visibility) {
                        playerConfig.visibility = false
                        bs.sendMessage("${MainAPI.getPrefix(MainAPI.PrefixType.SUCCESS)}${ChatColor.BOLD}${ChatColor.UNDERLINE}${p.name}${ChatColor.RESET}${ChatColor.GREEN}の姿を表示しました。")
                        continue
                    }
                    bs.sendMessage("${MainAPI.getPrefix(MainAPI.PrefixType.ERROR)}${ChatColor.BOLD}${ChatColor.UNDERLINE}${p.name}${ChatColor.RESET}${ChatColor.RED}はすでに姿が表示されています。")
                    continue
                }
                MainAPI.sendOfflineMessage(bs, name)
            }
            return
        }
        MainAPI.sendMessage(bs, MainAPI.ErrorMessage.ARGS_PLAYER)
    }

    override fun onConsoleCommand(cs: ConsoleCommandSender, cmd: Command, label: String, args: Array<String>) {
        if (args.isNotEmpty()) {
            for (name in args.filter { it.isNotEmpty() }) {
                val p = Bukkit.getPlayerExact(name)
                if (p != null) {
                    val playerConfig = PlayerManager.loadConfig(p)
                    if (playerConfig.visibility) {
                        playerConfig.visibility = false
                        cs.sendMessage("${MainAPI.getPrefix(MainAPI.PrefixType.SUCCESS)}${ChatColor.BOLD}${ChatColor.UNDERLINE}${p.name}${ChatColor.RESET}${ChatColor.GREEN}の姿を表示しました。")
                        continue
                    }
                    cs.sendMessage("${MainAPI.getPrefix(MainAPI.PrefixType.ERROR)}${ChatColor.BOLD}${ChatColor.UNDERLINE}${p.name}${ChatColor.RESET}${ChatColor.RED}はすでに姿が表示されています。")
                    continue
                }
                MainAPI.sendOfflineMessage(cs, name)
            }
            return
        }
        MainAPI.sendMessage(cs, MainAPI.ErrorMessage.ARGS_PLAYER)
    }

    override fun onPlayerTabComplete(sp: Player, cmd: Command, alias: String, args: Array<String>): List<String>? {
        return getTabList(sp, args)
    }

    override fun onBlockTabComplete(bs: BlockCommandSender, cmd: Command, alias: String, args: Array<String>): List<String>? {
        return getTabList(bs, args)
    }

    override fun onConsoleTabComplete(cs: ConsoleCommandSender, cmd: Command, alias: String, args: Array<String>): List<String>? {
        return getTabList(cs, args)
    }


    private fun getTabList(sender: CommandSender, args: Array<String>): List<String>? {
        if (MainAPI.isPlayer(sender) && !TosoGameAPI.isAdmin(sender as Player)) return emptyList()
        return getTabList(args[args.size - 1], Bukkit.getOnlinePlayers().filter { PlayerManager.loadConfig(it).visibility }.map { it.name })
    }
}
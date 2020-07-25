package jp.aoichaan0513.A_TosoGame_Live.Commands.Command

import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI
import jp.aoichaan0513.A_TosoGame_Live.API.Scoreboard.Teams
import jp.aoichaan0513.A_TosoGame_Live.API.TosoGameAPI
import jp.aoichaan0513.A_TosoGame_Live.Commands.ICommand
import jp.aoichaan0513.A_TosoGame_Live.Utils.isPlayerTeam
import jp.aoichaan0513.A_TosoGame_Live.Utils.setTeam
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.BlockCommandSender
import org.bukkit.command.Command
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.entity.Player

class Join(name: String) : ICommand(name) {

    override fun onPlayerCommand(sp: Player, cmd: Command, label: String, args: Array<String>) {
        if (TosoGameAPI.isBroadCaster(sp) || TosoGameAPI.hasPermission(sp)) {
            if (args.isNotEmpty()) {
                if (TosoGameAPI.isBroadCaster(sp) || TosoGameAPI.isAdmin(sp)) {
                    for (name in args) {
                        val p = Bukkit.getPlayerExact(name)
                        if (p != null) {
                            if (!p.isPlayerTeam) {
                                p.setTeam(Teams.OnlineTeam.TOSO_PLAYER, false)

                                sp.sendMessage("${MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY)}${p.name}を逃走者に追加しました。")
                                Bukkit.broadcastMessage("${MainAPI.getPrefix(MainAPI.PrefixType.SUCCESS)}${ChatColor.BOLD}${ChatColor.UNDERLINE}${p.name}${ChatColor.RESET}${ChatColor.GREEN}がゲームに参加しました。")
                                continue
                            }
                            sp.sendMessage("${MainAPI.getPrefix(MainAPI.PrefixType.ERROR)}${ChatColor.BOLD}${ChatColor.UNDERLINE}${p.name}${ChatColor.RESET}${ChatColor.RED}はすでにゲームに参加しています。")
                            continue
                        }
                        MainAPI.sendOfflineMessage(sp, name)
                        continue
                    }
                    return
                }
                MainAPI.sendMessage(sp, MainAPI.ErrorMessage.PERMISSIONS)
            } else {
                if (!sp.isPlayerTeam) {
                    sp.setTeam(Teams.OnlineTeam.TOSO_PLAYER, false)

                    Bukkit.broadcastMessage("${MainAPI.getPrefix(MainAPI.PrefixType.SUCCESS)}${ChatColor.BOLD}${ChatColor.UNDERLINE}${sp.name}${ChatColor.RESET}${ChatColor.GREEN}がゲームに参加しました。")
                    return
                }
                sp.sendMessage("${MainAPI.getPrefix(MainAPI.PrefixType.ERROR)}すでにゲームに参加しています。")
            }
            return
        }
        MainAPI.sendMessage(sp, MainAPI.ErrorMessage.PERMISSIONS)
    }

    override fun onBlockCommand(bs: BlockCommandSender, cmd: Command, label: String, args: Array<String>) {
        if (args.isNotEmpty()) {
            for (name in args) {
                val p = Bukkit.getPlayerExact(name)
                if (p != null) {
                    if (!p.isPlayerTeam) {
                        p.setTeam(Teams.OnlineTeam.TOSO_PLAYER, false)

                        bs.sendMessage("${MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY)}${p.name}を逃走者に追加しました。")
                        Bukkit.broadcastMessage("${MainAPI.getPrefix(MainAPI.PrefixType.SUCCESS)}${ChatColor.BOLD}${ChatColor.UNDERLINE}${p.name}${ChatColor.RESET}${ChatColor.GREEN}がゲームに参加しました。")
                        continue
                    }
                    bs.sendMessage("${MainAPI.getPrefix(MainAPI.PrefixType.ERROR)}${ChatColor.BOLD}${ChatColor.UNDERLINE}${p.name}${ChatColor.RESET}${ChatColor.RED}はすでにゲームに参加しています。")
                    continue
                }
                MainAPI.sendOfflineMessage(bs, name)
                continue
            }
            return
        }
        MainAPI.sendMessage(bs, MainAPI.ErrorMessage.ARGS_PLAYER)
    }

    override fun onConsoleCommand(cs: ConsoleCommandSender, cmd: Command, label: String, args: Array<String>) {
        if (args.isNotEmpty()) {
            for (name in args) {
                val p = Bukkit.getPlayerExact(name)
                if (p != null) {
                    if (!p.isPlayerTeam) {
                        p.setTeam(Teams.OnlineTeam.TOSO_PLAYER, false)

                        cs.sendMessage("${MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY)}${p.name}を逃走者に追加しました。")
                        Bukkit.broadcastMessage("${MainAPI.getPrefix(MainAPI.PrefixType.SUCCESS)}${ChatColor.BOLD}${ChatColor.UNDERLINE}${p.name}${ChatColor.RESET}${ChatColor.GREEN}がゲームに参加しました。")
                        continue
                    }
                    cs.sendMessage("${MainAPI.getPrefix(MainAPI.PrefixType.ERROR)}${ChatColor.BOLD}${ChatColor.UNDERLINE}${p.name}${ChatColor.RESET}${ChatColor.RED}はすでにゲームに参加しています。")
                    continue
                }
                MainAPI.sendOfflineMessage(cs, name)
                continue
            }
            return
        }
        MainAPI.sendMessage(cs, MainAPI.ErrorMessage.ARGS_PLAYER)
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
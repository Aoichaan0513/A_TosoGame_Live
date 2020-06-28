package jp.aoichaan0513.A_TosoGame_Live.Commands.Command

import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI
import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI.ErrorMessage
import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI.PrefixType
import jp.aoichaan0513.A_TosoGame_Live.API.TosoGameAPI
import jp.aoichaan0513.A_TosoGame_Live.Commands.ICommand
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.BlockCommandSender
import org.bukkit.command.Command
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.entity.Player

class Appear(name: String) : ICommand(name) {

    override fun onPlayerCommand(sp: Player, cmd: Command, label: String, args: Array<String>) {
        if (TosoGameAPI.isAdmin(sp)) {
            if (args.isNotEmpty()) {
                for (name in args) {
                    val p = Bukkit.getPlayerExact(name)
                    if (p != null) {
                        if (MainAPI.isHidePlayer(p)) {
                            for (player in Bukkit.getOnlinePlayers())
                                player.showPlayer(p)
                            MainAPI.removeHidePlayer(p, true)
                            sp.sendMessage(MainAPI.getPrefix(PrefixType.SUCCESS) + p.name + "の姿を表示しました。")
                            continue
                        }
                        sp.sendMessage(MainAPI.getPrefix(PrefixType.SECONDARY) + ChatColor.RED + ChatColor.UNDERLINE + p.name + ChatColor.RESET + ChatColor.GRAY + "はすでに姿が表示されています。")
                        continue
                    }
                    MainAPI.sendOfflineMessage(sp, name)
                    continue
                }
                return
            } else {
                if (MainAPI.isHidePlayer(sp)) {
                    for (player in Bukkit.getOnlinePlayers())
                        player.showPlayer(sp)
                    MainAPI.removeHidePlayer(sp, true)
                    sp.sendMessage(MainAPI.getPrefix(PrefixType.SUCCESS) + ChatColor.GREEN + "あなたの姿を表示しました。")
                    return
                }
                sp.sendMessage("${MainAPI.getPrefix(PrefixType.ERROR)}すでに姿が表示されています。")
                return
            }
        }
        MainAPI.sendMessage(sp, ErrorMessage.PERMISSIONS)
        return
    }

    override fun onBlockCommand(bs: BlockCommandSender, cmd: Command, label: String, args: Array<String>) {
        if (args.isNotEmpty()) {
            for (name in args) {
                val p = Bukkit.getPlayerExact(name)
                if (p != null) {
                    if (MainAPI.isHidePlayer(p)) {
                        for (player in Bukkit.getOnlinePlayers())
                            player.showPlayer(p)
                        MainAPI.removeHidePlayer(p, true)
                        bs.sendMessage(MainAPI.getPrefix(PrefixType.SUCCESS) + p.name + "の姿を表示しました。")
                        continue
                    }
                    bs.sendMessage(MainAPI.getPrefix(PrefixType.SECONDARY) + ChatColor.RED + ChatColor.UNDERLINE + p.name + ChatColor.RESET + ChatColor.GRAY + "はすでに姿が表示されています。")
                    continue
                }
                MainAPI.sendOfflineMessage(bs, name)
                continue
            }
            return
        }
        MainAPI.sendMessage(bs, ErrorMessage.ARGS_PLAYER)
        return
    }

    override fun onConsoleCommand(cs: ConsoleCommandSender, cmd: Command, label: String, args: Array<String>) {
        if (args.isNotEmpty()) {
            for (name in args) {
                val p = Bukkit.getPlayerExact(name)
                if (p != null) {
                    if (MainAPI.isHidePlayer(p)) {
                        for (player in Bukkit.getOnlinePlayers())
                            player.showPlayer(p)
                        MainAPI.removeHidePlayer(p, true)
                        cs.sendMessage(MainAPI.getPrefix(PrefixType.SUCCESS) + p.name + "の姿を表示しました。")
                        continue
                    }
                    cs.sendMessage(MainAPI.getPrefix(PrefixType.SECONDARY) + ChatColor.RED + ChatColor.UNDERLINE + p.name + ChatColor.RESET + ChatColor.GRAY + "はすでに姿が表示されています。")
                    continue
                }
                MainAPI.sendOfflineMessage(cs, name)
                continue
            }
            return
        }
        MainAPI.sendMessage(cs, ErrorMessage.ARGS_PLAYER)
        return
    }

    override fun onPlayerTabComplete(sp: Player, cmd: Command, alias: String, args: Array<String>): List<String>? {
        if (!TosoGameAPI.isAdmin(sp)) return null
        val set = mutableSetOf<String>()
        Bukkit.getOnlinePlayers().filter { MainAPI.isHidePlayer(it) }.forEach { set.add(it.name) }
        return getTabList(args[args.size - 1], set)
    }

    override fun onBlockTabComplete(bs: BlockCommandSender, cmd: Command, alias: String, args: Array<String>): List<String>? {
        val set = mutableSetOf<String>()
        Bukkit.getOnlinePlayers().filter { MainAPI.isHidePlayer(it) }.forEach { set.add(it.name) }
        return getTabList(args[args.size - 1], set)
    }

    override fun onConsoleTabComplete(cs: ConsoleCommandSender, cmd: Command, alias: String, args: Array<String>): List<String>? {
        val set = mutableSetOf<String>()
        Bukkit.getOnlinePlayers().filter { MainAPI.isHidePlayer(it) }.forEach { set.add(it.name) }
        return getTabList(args[args.size - 1], set)
    }
}
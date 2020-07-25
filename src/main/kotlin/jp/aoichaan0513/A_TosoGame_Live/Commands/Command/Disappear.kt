package jp.aoichaan0513.A_TosoGame_Live.Commands.Command

import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.Player.VisibilityManager
import jp.aoichaan0513.A_TosoGame_Live.API.TosoGameAPI
import jp.aoichaan0513.A_TosoGame_Live.Commands.ICommand
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.BlockCommandSender
import org.bukkit.command.Command
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.entity.Player

class Disappear(name: String) : ICommand(name) {

    override fun onPlayerCommand(sp: Player, cmd: Command, label: String, args: Array<String>) {
        if (TosoGameAPI.isAdmin(sp)) {
            if (args.isNotEmpty()) {
                for (name in args) {
                    val p = Bukkit.getPlayerExact(name)
                    if (p != null) {
                        if (!VisibilityManager.isHide(p, VisibilityManager.VisibilityType.ADMIN)) {
                            VisibilityManager.add(p, VisibilityManager.VisibilityType.ADMIN)
                            sp.sendMessage("${MainAPI.getPrefix(MainAPI.PrefixType.SUCCESS)}${ChatColor.BOLD}${ChatColor.UNDERLINE}${p.name}${ChatColor.RESET}${ChatColor.GREEN}の姿を非表示にしました。")
                            continue
                        }
                        sp.sendMessage("${MainAPI.getPrefix(MainAPI.PrefixType.ERROR)}${ChatColor.BOLD}${ChatColor.UNDERLINE}${p.name}${ChatColor.RESET}${ChatColor.RED}はすでに姿が非表示になっています。")
                        continue
                    }
                    MainAPI.sendOfflineMessage(sp, name)
                    continue
                }
                return
            } else {
                if (!VisibilityManager.isHide(sp, VisibilityManager.VisibilityType.ADMIN)) {
                    VisibilityManager.add(sp, VisibilityManager.VisibilityType.ADMIN)
                    sp.sendMessage("${MainAPI.getPrefix(MainAPI.PrefixType.SUCCESS)}あなたの姿を非表示にしました。")
                    return
                }
                sp.sendMessage("${MainAPI.getPrefix(MainAPI.PrefixType.ERROR)}すでに姿が非表示になっています。")
                return
            }
        }
        MainAPI.sendMessage(sp, MainAPI.ErrorMessage.PERMISSIONS_TEAM_ADMIN)
    }

    override fun onBlockCommand(bs: BlockCommandSender, cmd: Command, label: String, args: Array<String>) {
        if (args.isNotEmpty()) {
            for (name in args) {
                val p = Bukkit.getPlayerExact(name)
                if (p != null) {
                    if (!VisibilityManager.isHide(p, VisibilityManager.VisibilityType.ADMIN)) {
                        VisibilityManager.add(p, VisibilityManager.VisibilityType.ADMIN)
                        bs.sendMessage("${MainAPI.getPrefix(MainAPI.PrefixType.SUCCESS)}${ChatColor.BOLD}${ChatColor.UNDERLINE}${p.name}${ChatColor.RESET}${ChatColor.GREEN}の姿を非表示にしました。")
                        continue
                    }
                    bs.sendMessage("${MainAPI.getPrefix(MainAPI.PrefixType.ERROR)}${ChatColor.BOLD}${ChatColor.UNDERLINE}${p.name}${ChatColor.RESET}${ChatColor.RED}はすでに姿が非表示になっています。")
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
                    if (!VisibilityManager.isHide(p, VisibilityManager.VisibilityType.ADMIN)) {
                        VisibilityManager.add(p, VisibilityManager.VisibilityType.ADMIN)
                        cs.sendMessage("${MainAPI.getPrefix(MainAPI.PrefixType.SUCCESS)}${ChatColor.BOLD}${ChatColor.UNDERLINE}${p.name}${ChatColor.RESET}${ChatColor.GREEN}の姿を非表示にしました。")
                        continue
                    }
                    cs.sendMessage("${MainAPI.getPrefix(MainAPI.PrefixType.ERROR)}${ChatColor.BOLD}${ChatColor.UNDERLINE}${p.name}${ChatColor.RESET}${ChatColor.RED}はすでに姿が非表示になっています。")
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
        if (!TosoGameAPI.isAdmin(sp)) return emptyList()
        val set = mutableSetOf<String>()
        Bukkit.getOnlinePlayers().filter { !VisibilityManager.isHide(it, VisibilityManager.VisibilityType.ADMIN) }.forEach { set.add(it.name) }
        return getTabList(args[args.size - 1], set)
    }

    override fun onBlockTabComplete(bs: BlockCommandSender, cmd: Command, alias: String, args: Array<String>): List<String>? {
        val set = mutableSetOf<String>()
        Bukkit.getOnlinePlayers().filter { !VisibilityManager.isHide(it, VisibilityManager.VisibilityType.ADMIN) }.forEach { set.add(it.name) }
        return getTabList(args[args.size - 1], set)
    }

    override fun onConsoleTabComplete(cs: ConsoleCommandSender, cmd: Command, alias: String, args: Array<String>): List<String>? {
        val set = mutableSetOf<String>()
        Bukkit.getOnlinePlayers().filter { !VisibilityManager.isHide(it, VisibilityManager.VisibilityType.ADMIN) }.forEach { set.add(it.name) }
        return getTabList(args[args.size - 1], set)
    }
}
package jp.aoichaan0513.A_TosoGame_Live.Commands.Command

import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI
import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI.ErrorMessage
import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI.PrefixType
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.ActionBarManager
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
                    val target = Bukkit.getPlayerExact(name)
                    if (target == null) {
                        MainAPI.sendOfflineMessage(sp, name)
                        continue
                    } else {
                        val p = Bukkit.getPlayerExact(name)!!
                        if (!MainAPI.isHidePlayer(p)) {
                            for (player in Bukkit.getOnlinePlayers())
                                player.hidePlayer(p)
                            MainAPI.addHidePlayer(p, true)
                            sp.sendMessage(MainAPI.getPrefix(PrefixType.SUCCESS) + p.name + "の姿を非表示にしました。")
                            ActionBarManager.sendActionBar(p, ChatColor.RED.toString() + "あなたの姿は非表示になっています。")
                            continue
                        }
                        sp.sendMessage(MainAPI.getPrefix(PrefixType.SECONDARY) + ChatColor.RED + ChatColor.UNDERLINE + p.name + ChatColor.RESET + ChatColor.GRAY + "はすでに姿が非表示になっています。")
                        continue
                    }
                }
                return
            } else {
                if (!MainAPI.isHidePlayer(sp)) {
                    for (player in Bukkit.getOnlinePlayers())
                        player.hidePlayer(sp)
                    MainAPI.addHidePlayer(sp, true)
                    sp.sendMessage(MainAPI.getPrefix(PrefixType.SUCCESS) + ChatColor.GREEN + "あなたの姿を非表示にしました。")
                    ActionBarManager.sendActionBar(sp, ChatColor.RED.toString() + "あなたの姿は非表示になっています。")
                    return
                }
                sp.sendMessage("${MainAPI.getPrefix(PrefixType.ERROR)}すでに姿が非表示になっています。")
                return
            }
        }
        MainAPI.sendMessage(sp, ErrorMessage.PERMISSIONS)
        return
    }

    override fun onBlockCommand(bs: BlockCommandSender, cmd: Command, label: String, args: Array<String>) {
        if (args.isNotEmpty()) {
            for (name in args) {
                val target = Bukkit.getPlayerExact(name)
                if (target == null) {
                    MainAPI.sendOfflineMessage(bs, name)
                    continue
                } else {
                    val p = Bukkit.getPlayerExact(name)!!
                    if (!MainAPI.isHidePlayer(p)) {
                        for (player in Bukkit.getOnlinePlayers())
                            player.hidePlayer(p)
                        MainAPI.addHidePlayer(p, true)
                        bs.sendMessage(MainAPI.getPrefix(PrefixType.SUCCESS) + p.name + "の姿を非表示にしました。")
                        ActionBarManager.sendActionBar(p, ChatColor.RED.toString() + "あなたの姿は非表示になっています。")
                        continue
                    }
                    bs.sendMessage(MainAPI.getPrefix(PrefixType.SECONDARY) + ChatColor.RED + ChatColor.UNDERLINE + p.name + ChatColor.RESET + ChatColor.GRAY + "はすでに姿が非表示になっています。")
                    continue
                }
            }
            return
        }
        MainAPI.sendMessage(bs, ErrorMessage.ARGS_PLAYER)
        return
    }

    override fun onConsoleCommand(cs: ConsoleCommandSender, cmd: Command, label: String, args: Array<String>) {
        if (args.isNotEmpty()) {
            for (name in args) {
                val target = Bukkit.getPlayerExact(name)
                if (target == null) {
                    MainAPI.sendOfflineMessage(cs, name)
                    continue
                } else {
                    val p = Bukkit.getPlayerExact(name)!!
                    if (!MainAPI.isHidePlayer(p)) {
                        for (player in Bukkit.getOnlinePlayers())
                            player.hidePlayer(p)
                        MainAPI.addHidePlayer(p, true)
                        cs.sendMessage(MainAPI.getPrefix(PrefixType.SUCCESS) + p.name + "の姿を非表示にしました。")
                        ActionBarManager.sendActionBar(p, ChatColor.RED.toString() + "あなたの姿は非表示になっています。")
                        continue
                    }
                    cs.sendMessage(MainAPI.getPrefix(PrefixType.SECONDARY) + ChatColor.RED + ChatColor.UNDERLINE + p.name + ChatColor.RESET + ChatColor.GRAY + "はすでに姿が非表示になっています。")
                    continue
                }
            }
            return
        }
        MainAPI.sendMessage(cs, ErrorMessage.ARGS_PLAYER)
        return
    }

    override fun onPlayerTabComplete(sp: Player, cmd: Command, alias: String, args: Array<String>): List<String>? {
        if (!TosoGameAPI.isAdmin(sp)) return null
        val set = mutableSetOf<String>()
        Bukkit.getOnlinePlayers().filter { !MainAPI.isHidePlayer(it) }.forEach { set.add(it.name) }
        return getTabList(args[args.size - 1], set)
    }

    override fun onBlockTabComplete(bs: BlockCommandSender, cmd: Command, alias: String, args: Array<String>): List<String>? {
        val set = mutableSetOf<String>()
        Bukkit.getOnlinePlayers().filter { !MainAPI.isHidePlayer(it) }.forEach { set.add(it.name) }
        return getTabList(args[args.size - 1], set)
    }

    override fun onConsoleTabComplete(cs: ConsoleCommandSender, cmd: Command, alias: String, args: Array<String>): List<String>? {
        val set = mutableSetOf<String>()
        Bukkit.getOnlinePlayers().filter { !MainAPI.isHidePlayer(it) }.forEach { set.add(it.name) }
        return getTabList(args[args.size - 1], set)
    }
}
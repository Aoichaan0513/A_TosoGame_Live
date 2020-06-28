package jp.aoichaan0513.A_TosoGame_Live.Commands.Command

import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI
import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI.ErrorMessage
import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI.PrefixType
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.World.WorldManager.GameType
import jp.aoichaan0513.A_TosoGame_Live.API.Scoreboard.Scoreboard
import jp.aoichaan0513.A_TosoGame_Live.API.Scoreboard.Teams
import jp.aoichaan0513.A_TosoGame_Live.API.Scoreboard.Teams.OnlineTeam
import jp.aoichaan0513.A_TosoGame_Live.API.TosoGameAPI
import jp.aoichaan0513.A_TosoGame_Live.Commands.ICommand
import jp.aoichaan0513.A_TosoGame_Live.Mission.MissionManager
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.GameMode
import org.bukkit.command.BlockCommandSender
import org.bukkit.command.Command
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.entity.Player
import org.bukkit.scoreboard.DisplaySlot

class Leave(name: String) : ICommand(name) {

    override fun onPlayerCommand(sp: Player, cmd: Command, label: String, args: Array<String>) {
        if (TosoGameAPI.isBroadCaster(sp) || TosoGameAPI.hasPermission(sp)) {
            if (args.isNotEmpty()) {
                if (TosoGameAPI.isBroadCaster(sp) || TosoGameAPI.isAdmin(sp)) {
                    for (name in args) {
                        val p = Bukkit.getPlayerExact(name)
                        if (p != null) {
                            if (!Teams.hasJoinedTeam(OnlineTeam.TOSO_ADMIN, p)) {
                                Teams.joinTeam(OnlineTeam.TOSO_ADMIN, p)
                                p.gameMode = GameMode.CREATIVE

                                TosoGameAPI.setItem(GameType.START, p)
                                TosoGameAPI.setPotionEffect(p)
                                TosoGameAPI.addOp(p)

                                val board = Scoreboard.getBoard(p)
                                val objective = board.getObjective(TosoGameAPI.Objective.SIDEBAR.objectName) ?: return
                                if (objective.getDisplaySlot() != DisplaySlot.SIDEBAR)
                                    objective.setDisplaySlot(DisplaySlot.SIDEBAR)

                                TosoGameAPI.showPlayers(p)
                                TosoGameAPI.hidePlayers(p)

                                MissionManager.bossBar?.addPlayer(p)

                                sp.sendMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}${p.name}を運営に追加しました。")
                                p.sendMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}あなたを運営に追加しました。")
                                Bukkit.broadcastMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}${ChatColor.GREEN}${ChatColor.UNDERLINE}${p.name}${ChatColor.RESET}${ChatColor.GRAY}がゲームから抜けました。")
                                continue
                            }
                            sp.sendMessage("${MainAPI.getPrefix(PrefixType.ERROR)}${p.name}はすでにゲームから離脱しています。")
                            continue
                        }
                        MainAPI.sendOfflineMessage(sp, name)
                        continue
                    }
                    return
                }
                MainAPI.sendMessage(sp, ErrorMessage.PERMISSIONS)
                return
            } else {
                if (!Teams.hasJoinedTeam(OnlineTeam.TOSO_ADMIN, sp)) {
                    Teams.joinTeam(OnlineTeam.TOSO_ADMIN, sp)
                    sp.gameMode = GameMode.CREATIVE

                    TosoGameAPI.setItem(GameType.START, sp)
                    TosoGameAPI.setPotionEffect(sp)
                    TosoGameAPI.addOp(sp)

                    val board = Scoreboard.getBoard(sp)
                    val objective = board.getObjective(TosoGameAPI.Objective.SIDEBAR.objectName) ?: return
                    if (objective.getDisplaySlot() != DisplaySlot.SIDEBAR)
                        objective.setDisplaySlot(DisplaySlot.SIDEBAR)

                    TosoGameAPI.showPlayers(sp)
                    TosoGameAPI.hidePlayers(sp)

                    if (MissionManager.isBossBar)
                        MissionManager.bossBar!!.addPlayer(sp)

                    sp.sendMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}あなたを運営に追加しました。")
                    Bukkit.broadcastMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}${ChatColor.GREEN}${ChatColor.UNDERLINE}${sp.name}${ChatColor.RESET}${ChatColor.GRAY}がゲームから抜けました。")
                    return
                }
                sp.sendMessage("${MainAPI.getPrefix(PrefixType.ERROR)}すでにゲームから離脱しています。")
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
                    if (!Teams.hasJoinedTeam(OnlineTeam.TOSO_ADMIN, p)) {
                        Teams.joinTeam(OnlineTeam.TOSO_ADMIN, p)
                        p.gameMode = GameMode.CREATIVE

                        TosoGameAPI.setItem(GameType.START, p)
                        TosoGameAPI.setPotionEffect(p)
                        TosoGameAPI.addOp(p)

                        val board = Scoreboard.getBoard(p)
                        val objective = board.getObjective(TosoGameAPI.Objective.SIDEBAR.objectName) ?: return
                        if (objective.getDisplaySlot() != DisplaySlot.SIDEBAR)
                            objective.setDisplaySlot(DisplaySlot.SIDEBAR)

                        TosoGameAPI.showPlayers(p)
                        TosoGameAPI.hidePlayers(p)

                        if (MissionManager.isBossBar)
                            MissionManager.bossBar!!.addPlayer(p)

                        bs.sendMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}${p.name}を運営に追加しました。")
                        p.sendMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}あなたを運営に追加しました。")
                        Bukkit.broadcastMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}${ChatColor.GREEN}${ChatColor.UNDERLINE}${p.name}${ChatColor.RESET}${ChatColor.GRAY}がゲームから抜けました。")
                        continue
                    }
                    bs.sendMessage("${MainAPI.getPrefix(PrefixType.ERROR)}${p.name}はすでにゲームから離脱しています。")
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
                    if (!Teams.hasJoinedTeam(OnlineTeam.TOSO_ADMIN, p)) {
                        Teams.joinTeam(OnlineTeam.TOSO_ADMIN, p)
                        p.gameMode = GameMode.CREATIVE

                        TosoGameAPI.setItem(GameType.START, p)
                        TosoGameAPI.setPotionEffect(p)
                        TosoGameAPI.addOp(p)

                        val board = Scoreboard.getBoard(p)
                        val objective = board.getObjective(TosoGameAPI.Objective.SIDEBAR.objectName) ?: return
                        if (objective.getDisplaySlot() != DisplaySlot.SIDEBAR)
                            objective.setDisplaySlot(DisplaySlot.SIDEBAR)

                        TosoGameAPI.showPlayers(p)
                        TosoGameAPI.hidePlayers(p)

                        if (MissionManager.isBossBar)
                            MissionManager.bossBar!!.addPlayer(p)

                        cs.sendMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}${p.name}を運営に追加しました。")
                        p.sendMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}あなたを運営に追加しました。")
                        Bukkit.broadcastMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}${ChatColor.GREEN}${ChatColor.UNDERLINE}${p.name}${ChatColor.RESET}${ChatColor.GRAY}がゲームから抜けました。")
                        continue
                    }
                    cs.sendMessage("${MainAPI.getPrefix(PrefixType.ERROR)}${p.name}はすでにゲームから離脱しています。")
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
        return null
    }

    override fun onBlockTabComplete(bs: BlockCommandSender, cmd: Command, alias: String, args: Array<String>): List<String>? {
        return null
    }

    override fun onConsoleTabComplete(cs: ConsoleCommandSender, cmd: Command, alias: String, args: Array<String>): List<String>? {
        return null
    }
}
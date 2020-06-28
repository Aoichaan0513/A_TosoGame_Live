package jp.aoichaan0513.A_TosoGame_Live.Commands.Command

import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI
import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI.ErrorMessage
import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI.PrefixType
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.World.WorldManager
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.World.WorldManager.GameType
import jp.aoichaan0513.A_TosoGame_Live.API.Scoreboard.Teams
import jp.aoichaan0513.A_TosoGame_Live.API.Scoreboard.Teams.OnlineTeam
import jp.aoichaan0513.A_TosoGame_Live.API.TosoGameAPI
import jp.aoichaan0513.A_TosoGame_Live.Commands.ICommand
import jp.aoichaan0513.A_TosoGame_Live.Main
import jp.aoichaan0513.A_TosoGame_Live.Mission.MissionManager
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.GameMode
import org.bukkit.command.BlockCommandSender
import org.bukkit.command.Command
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.entity.Player
import java.util.*

class Player(name: String) : ICommand(name) {

    override fun onPlayerCommand(sp: Player, cmd: Command, label: String, args: Array<String>) {
        if (TosoGameAPI.isAdmin(sp)) {
            if (args.isNotEmpty()) {
                val worldConfig = Main.worldConfig
                if (args[0].equals("join", true)) {
                    if (args.size != 1) {
                        if (args[1].startsWith("@")) {
                            val arg = args[1].substring(1)
                            if (arg.equals("all", true)) {
                                val i = Teams.getOnlineCount(OnlineTeam.TOSO_JAIL)
                                for (p in Bukkit.getOnlinePlayers()) {
                                    if (Teams.hasJoinedTeam(OnlineTeam.TOSO_JAIL, p)) {
                                        Teams.joinTeam(OnlineTeam.TOSO_PLAYER, p)
                                        p.gameMode = GameMode.ADVENTURE

                                        TosoGameAPI.setItem(GameType.START, p)
                                        TosoGameAPI.setPotionEffect(p)
                                        TosoGameAPI.removeOp(p)

                                        TosoGameAPI.showPlayers(p)
                                        TosoGameAPI.teleport(p, worldConfig.respawnLocationConfig.locations.values)

                                        MissionManager.bossBar?.addPlayer(p)

                                        p.sendMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}あなたを逃走者に追加しました。")
                                        Bukkit.broadcastMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}${p.name}が逃走者になりました。")
                                    }
                                }
                                sp.sendMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}${ChatColor.GREEN}${ChatColor.UNDERLINE}${i}人${ChatColor.RESET}${ChatColor.GRAY}を逃走者に追加しました。")
                                return
                            } else if (arg.startsWith("team")) {
                                if (arg.length > 5) {
                                    val team = arg.substring(5)
                                    if (team.equals("admin", true)) {
                                        val i = Teams.getOnlineCount(OnlineTeam.TOSO_ADMIN)
                                        for (p in Bukkit.getOnlinePlayers()) {
                                            if (Teams.hasJoinedTeam(OnlineTeam.TOSO_ADMIN, p)) {
                                                Teams.joinTeam(OnlineTeam.TOSO_PLAYER, p)
                                                p.gameMode = GameMode.ADVENTURE

                                                TosoGameAPI.setItem(GameType.START, p)
                                                TosoGameAPI.setPotionEffect(p)
                                                TosoGameAPI.removeOp(p)

                                                TosoGameAPI.showPlayers(p)
                                                TosoGameAPI.teleport(p, worldConfig.respawnLocationConfig.locations.values)

                                                MissionManager.bossBar?.addPlayer(p)

                                                p.sendMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}あなたを逃走者に追加しました。")
                                                Bukkit.broadcastMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}${p.name}が逃走者になりました。")
                                            }
                                        }
                                        sp.sendMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}${ChatColor.GREEN}${ChatColor.UNDERLINE}${i}人${ChatColor.RESET}${ChatColor.GRAY}を逃走者に追加しました。")
                                        return
                                    } else if (team.equals("success", true)) {
                                        val i = Teams.getOnlineCount(OnlineTeam.TOSO_SUCCESS)
                                        for (p in Bukkit.getOnlinePlayers()) {
                                            if (Teams.hasJoinedTeam(OnlineTeam.TOSO_SUCCESS, p)) {
                                                Teams.joinTeam(OnlineTeam.TOSO_PLAYER, p)
                                                p.gameMode = GameMode.ADVENTURE

                                                TosoGameAPI.setItem(GameType.START, p)
                                                TosoGameAPI.setPotionEffect(p)
                                                TosoGameAPI.removeOp(p)

                                                TosoGameAPI.showPlayers(p)
                                                TosoGameAPI.teleport(p, worldConfig.respawnLocationConfig.locations.values)

                                                MissionManager.bossBar?.addPlayer(p)

                                                p.sendMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}あなたを逃走者に追加しました。")
                                                Bukkit.broadcastMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}${p.name}が逃走者になりました。")
                                            }
                                        }
                                        sp.sendMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}${ChatColor.GREEN}${ChatColor.UNDERLINE}${i}人${ChatColor.RESET}${ChatColor.GRAY}を逃走者に追加しました。")
                                        return
                                    } else if (team.equals("jail", true)) {
                                        val i = Teams.getOnlineCount(OnlineTeam.TOSO_JAIL)
                                        for (p in Bukkit.getOnlinePlayers()) {
                                            if (Teams.hasJoinedTeam(OnlineTeam.TOSO_JAIL, p)) {
                                                Teams.joinTeam(OnlineTeam.TOSO_PLAYER, p)
                                                p.gameMode = GameMode.ADVENTURE

                                                TosoGameAPI.setItem(GameType.START, p)
                                                TosoGameAPI.setPotionEffect(p)
                                                TosoGameAPI.removeOp(p)

                                                TosoGameAPI.showPlayers(p)
                                                TosoGameAPI.teleport(p, worldConfig.respawnLocationConfig.locations.values)

                                                MissionManager.bossBar?.addPlayer(p)

                                                p.sendMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}あなたを逃走者に追加しました。")
                                                Bukkit.broadcastMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}${p.name}が逃走者になりました。")
                                            }
                                        }
                                        sp.sendMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}${ChatColor.GREEN}${ChatColor.UNDERLINE}${i}人${ChatColor.RESET}${ChatColor.GRAY}を逃走者に追加しました。")
                                        return
                                    } else if (team.equals("hunter", true)) {
                                        val i = Teams.getOnlineCount(OnlineTeam.TOSO_HUNTER)
                                        for (p in Bukkit.getOnlinePlayers()) {
                                            if (Teams.hasJoinedTeam(OnlineTeam.TOSO_HUNTER, p)) {
                                                Teams.joinTeam(OnlineTeam.TOSO_PLAYER, p)
                                                p.gameMode = GameMode.ADVENTURE

                                                TosoGameAPI.setItem(GameType.START, p)
                                                TosoGameAPI.setPotionEffect(p)
                                                TosoGameAPI.removeOp(p)

                                                TosoGameAPI.showPlayers(p)
                                                TosoGameAPI.teleport(p, worldConfig.respawnLocationConfig.locations.values)

                                                MissionManager.bossBar?.addPlayer(p)

                                                p.sendMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}あなたを逃走者に追加しました。")
                                                Bukkit.broadcastMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}${p.name}が逃走者になりました。")
                                            }
                                        }
                                        sp.sendMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}${ChatColor.GREEN}${ChatColor.UNDERLINE}${i}人${ChatColor.RESET}${ChatColor.GRAY}を逃走者に追加しました。")
                                        return
                                    } else if (team.equals("tuho", true)) {
                                        val i = Teams.getOnlineCount(OnlineTeam.TOSO_TUHO)
                                        for (p in Bukkit.getOnlinePlayers()) {
                                            if (Teams.hasJoinedTeam(OnlineTeam.TOSO_TUHO, p)) {
                                                Teams.joinTeam(OnlineTeam.TOSO_PLAYER, p)
                                                p.gameMode = GameMode.ADVENTURE

                                                TosoGameAPI.setItem(GameType.START, p)
                                                TosoGameAPI.setPotionEffect(p)
                                                TosoGameAPI.removeOp(p)

                                                TosoGameAPI.showPlayers(p)
                                                TosoGameAPI.teleport(p, worldConfig.respawnLocationConfig.locations.values)

                                                MissionManager.bossBar?.addPlayer(p)

                                                p.sendMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}あなたを逃走者に追加しました。")
                                                Bukkit.broadcastMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}${p.name}が逃走者になりました。")
                                            }
                                        }
                                        sp.sendMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}${ChatColor.GREEN}${ChatColor.UNDERLINE}${i}人${ChatColor.RESET}${ChatColor.GRAY}を逃走者に追加しました。")
                                        return
                                    }
                                }
                                sp.sendMessage("${MainAPI.getPrefix(PrefixType.ERROR)}引数が不正です。チーム名を指定してください。")
                                return
                            }
                        } else {
                            for (i in 1 until args.size) {
                                val p = Bukkit.getPlayerExact(args[i])
                                if (p != null) {
                                    if (!Teams.hasJoinedTeam(OnlineTeam.TOSO_PLAYER, p)) {
                                        Teams.joinTeam(OnlineTeam.TOSO_PLAYER, p)
                                        p.gameMode = GameMode.ADVENTURE

                                        TosoGameAPI.setItem(GameType.START, p)
                                        TosoGameAPI.setPotionEffect(p)
                                        TosoGameAPI.removeOp(p)

                                        TosoGameAPI.showPlayers(p)
                                        TosoGameAPI.teleport(p, worldConfig.respawnLocationConfig.locations.values)

                                        MissionManager.bossBar?.addPlayer(p)

                                        p.sendMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}あなたを逃走者に追加しました。")
                                        Bukkit.broadcastMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}${p.name}が逃走者になりました。")
                                        continue
                                    }
                                    sp.sendMessage("${MainAPI.getPrefix(PrefixType.ERROR)}${p.name}はすでに逃走者になっています。")
                                    continue
                                }
                                MainAPI.sendOfflineMessage(sp, args[i])
                                continue
                            }
                            return
                        }
                    }
                    MainAPI.sendMessage(sp, ErrorMessage.ARGS_PLAYER)
                    return
                } else if (args[0].equals("leave", true)) {
                    if (args.size != 1) {
                        if (args[1].startsWith("@")) {
                            val arg = args[1].substring(1)
                            if (arg.equals("all", true)) {
                                val i = Teams.getOnlineCount(OnlineTeam.TOSO_PLAYER)
                                for (p in Bukkit.getOnlinePlayers()) {
                                    if (Teams.hasJoinedTeam(OnlineTeam.TOSO_PLAYER, p)) {
                                        Teams.joinTeam(OnlineTeam.TOSO_JAIL, p)
                                        p.gameMode = GameMode.ADVENTURE

                                        TosoGameAPI.setItem(GameType.RESPAWN, p)
                                        TosoGameAPI.setPotionEffect(p)
                                        TosoGameAPI.removeOp(p)

                                        TosoGameAPI.showPlayers(p)
                                        TosoGameAPI.teleport(p, worldConfig.jailLocationConfig.locations.values)

                                        MissionManager.bossBar?.addPlayer(p)
                                        p.sendMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}あなたを牢獄に追加しました。")
                                        Bukkit.broadcastMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}${p.name}が逃走者から抜けました。")
                                    }
                                }
                                sp.sendMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}${ChatColor.GREEN}${ChatColor.UNDERLINE}${i}人${ChatColor.RESET}${ChatColor.GRAY}が逃走者から抜けました。")
                                return
                            } else if (arg.startsWith("team")) {
                                if (arg.length > 5) {
                                    val team = arg.substring(5)
                                    if (team.equals("admin", true)) {
                                        val i = Teams.getOnlineCount(OnlineTeam.TOSO_PLAYER)
                                        for (p in Bukkit.getOnlinePlayers()) {
                                            if (Teams.hasJoinedTeam(OnlineTeam.TOSO_PLAYER, p)) {
                                                Teams.joinTeam(OnlineTeam.TOSO_ADMIN, p)
                                                p.gameMode = GameMode.CREATIVE

                                                TosoGameAPI.setItem(WorldManager.GameType.START, p)
                                                TosoGameAPI.setPotionEffect(p, true)
                                                TosoGameAPI.addOp(p)

                                                TosoGameAPI.showPlayers(p)
                                                TosoGameAPI.teleport(p, worldConfig.respawnLocationConfig.locations.values)

                                                MissionManager.bossBar?.addPlayer(p)

                                                p.sendMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}あなたを運営に追加しました。")
                                                Bukkit.broadcastMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}${p.name}が逃走者から抜けました。")
                                            }
                                        }
                                        sp.sendMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}${ChatColor.GREEN}${ChatColor.UNDERLINE}${i}人${ChatColor.RESET}${ChatColor.GRAY}が逃走者から抜けました。")
                                        return
                                    } else if (team.equals("success", true)) {
                                        val i = Teams.getOnlineCount(OnlineTeam.TOSO_PLAYER)
                                        for (p in Bukkit.getOnlinePlayers()) {
                                            if (Teams.hasJoinedTeam(OnlineTeam.TOSO_PLAYER, p)) {
                                                Teams.joinTeam(OnlineTeam.TOSO_SUCCESS, p)
                                                p.gameMode = GameMode.ADVENTURE

                                                TosoGameAPI.setItem(WorldManager.GameType.START, p)
                                                TosoGameAPI.setPotionEffect(p, true)
                                                TosoGameAPI.removeOp(p)

                                                TosoGameAPI.showPlayers(p)
                                                TosoGameAPI.teleport(p, worldConfig.respawnLocationConfig.locations.values)

                                                MissionManager.bossBar?.addPlayer(p)

                                                p.sendMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}あなたを生存者に追加しました。")
                                                Bukkit.broadcastMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}${p.name}が逃走者から抜けました。")
                                            }
                                        }
                                        sp.sendMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}${ChatColor.GREEN}${ChatColor.UNDERLINE}${i}人${ChatColor.RESET}${ChatColor.GRAY}が逃走者から抜けました。")
                                        return
                                    } else if (team.equals("jail", true)) {
                                        val i = Teams.getOnlineCount(OnlineTeam.TOSO_PLAYER)
                                        for (p in Bukkit.getOnlinePlayers()) {
                                            if (Teams.hasJoinedTeam(OnlineTeam.TOSO_PLAYER, p)) {
                                                Teams.joinTeam(OnlineTeam.TOSO_JAIL, p)
                                                p.gameMode = GameMode.ADVENTURE

                                                TosoGameAPI.setItem(WorldManager.GameType.START, p)
                                                TosoGameAPI.setPotionEffect(p, true)
                                                TosoGameAPI.removeOp(p)

                                                TosoGameAPI.showPlayers(p)
                                                TosoGameAPI.teleport(p, worldConfig.jailLocationConfig.locations.values)

                                                MissionManager.bossBar?.addPlayer(p)

                                                p.sendMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}あなたを牢獄に追加しました。")
                                                Bukkit.broadcastMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}${p.name}が逃走者から抜けました。")
                                            }
                                        }
                                        sp.sendMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}${ChatColor.GREEN}${ChatColor.UNDERLINE}${i}人${ChatColor.RESET}${ChatColor.GRAY}が逃走者から抜けました。")
                                        return
                                    } else if (team.equals("hunter", true)) {
                                        val i = Teams.getOnlineCount(OnlineTeam.TOSO_PLAYER)
                                        for (p in Bukkit.getOnlinePlayers()) {
                                            if (Teams.hasJoinedTeam(OnlineTeam.TOSO_PLAYER, p)) {
                                                Teams.joinTeam(OnlineTeam.TOSO_HUNTER, p)
                                                p.gameMode = GameMode.ADVENTURE

                                                TosoGameAPI.setItem(WorldManager.GameType.START, p)
                                                TosoGameAPI.setPotionEffect(p, true)
                                                TosoGameAPI.removeOp(p)

                                                TosoGameAPI.showPlayers(p)
                                                p.teleport(worldConfig.hunterLocationConfig.getLocation(1))

                                                MissionManager.bossBar?.addPlayer(p)

                                                p.sendMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}あなたをハンターに追加しました。")
                                                Bukkit.broadcastMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}${p.name}が逃走者から抜けました。")
                                            }
                                        }
                                        sp.sendMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}${ChatColor.GREEN}${ChatColor.UNDERLINE}${i}人${ChatColor.RESET}${ChatColor.GRAY}が逃走者から抜けました。")
                                        return
                                    } else if (team.equals("tuho", true)) {
                                        val i = Teams.getOnlineCount(OnlineTeam.TOSO_PLAYER)
                                        for (p in Bukkit.getOnlinePlayers()) {
                                            if (Teams.hasJoinedTeam(OnlineTeam.TOSO_PLAYER, p)) {
                                                Teams.joinTeam(OnlineTeam.TOSO_TUHO, p)
                                                p.gameMode = GameMode.ADVENTURE

                                                TosoGameAPI.setItem(WorldManager.GameType.START, p)
                                                TosoGameAPI.setPotionEffect(p, true)
                                                TosoGameAPI.removeOp(p)

                                                TosoGameAPI.showPlayers(p)
                                                TosoGameAPI.teleport(p, worldConfig.jailLocationConfig.locations.values)

                                                MissionManager.bossBar?.addPlayer(p)

                                                p.sendMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}あなたを牢獄に追加しました。")
                                                Bukkit.broadcastMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}${p.name}が逃走者から抜けました。")
                                            }
                                        }
                                        sp.sendMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}${ChatColor.GREEN}${ChatColor.UNDERLINE}${i}人${ChatColor.RESET}${ChatColor.GRAY}が逃走者から抜けました。")
                                        return
                                    }
                                }
                                sp.sendMessage("${MainAPI.getPrefix(PrefixType.ERROR)}引数が不正です。チーム名を指定してください。")
                                return
                            }
                        } else {
                            for (i in 1 until args.size) {
                                val p = Bukkit.getPlayerExact(args[i])
                                if (p != null) {
                                    if (Teams.hasJoinedTeam(OnlineTeam.TOSO_PLAYER, p)) {
                                        Teams.joinTeam(OnlineTeam.TOSO_JAIL, p)
                                        p.gameMode = GameMode.ADVENTURE

                                        TosoGameAPI.setItem(WorldManager.GameType.START, p)
                                        TosoGameAPI.setPotionEffect(p, true)
                                        TosoGameAPI.removeOp(p)

                                        TosoGameAPI.showPlayers(p)
                                        TosoGameAPI.teleport(p, worldConfig.jailLocationConfig.locations.values)

                                        MissionManager.bossBar?.addPlayer(p)

                                        p.sendMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}あなたを牢獄に追加しました。")
                                        Bukkit.broadcastMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}${p.name}が逃走者から抜けました。")
                                        continue
                                    }
                                    sp.sendMessage("${MainAPI.getPrefix(PrefixType.ERROR)}${p.name}はすでに逃走者から抜けています。")
                                    continue
                                }
                                MainAPI.sendOfflineMessage(sp, args[i])
                                continue
                            }
                            return
                        }
                    }
                    MainAPI.sendMessage(sp, ErrorMessage.ARGS_PLAYER)
                    return
                }
                sp.sendMessage("""
                    ${MainAPI.getPrefix(PrefixType.ERROR)}引数が不正です。
                    ${MainAPI.getPrefix(PrefixType.ERROR)}コマンドの使い方:
                    ${MainAPI.getPrefix(PrefixType.ERROR)}"/player join <プレイヤー名>" - 逃走者に参加
                    ${MainAPI.getPrefix(PrefixType.ERROR)}"/player leave <プレイヤー名>" - 逃走者から離脱
                """.trimIndent())
                return
            }
            sp.sendMessage("""
                ${MainAPI.getPrefix(PrefixType.ERROR)}引数が不正です。
                ${MainAPI.getPrefix(PrefixType.ERROR)}コマンドの使い方:
                ${MainAPI.getPrefix(PrefixType.ERROR)}"/player join <プレイヤー名>" - 逃走者に参加
                ${MainAPI.getPrefix(PrefixType.ERROR)}"/player leave <プレイヤー名>" - 逃走者から離脱
            """.trimIndent())
            return
        }
        MainAPI.sendMessage(sp, ErrorMessage.PERMISSIONS)
        return
    }

    override fun onBlockCommand(bs: BlockCommandSender, cmd: Command, label: String, args: Array<String>) {
        if (args.isNotEmpty()) {
            val worldConfig = Main.worldConfig
            if (args[0].equals("join", true)) {
                if (args.size != 1) {
                    for (i in 1 until args.size) {
                        val p = Bukkit.getPlayerExact(args[i])
                        if (p != null) {
                            if (Teams.hasJoinedTeam(OnlineTeam.TOSO_PLAYER, p)) {
                                Teams.joinTeam(OnlineTeam.TOSO_JAIL, p)
                                p.gameMode = GameMode.ADVENTURE

                                TosoGameAPI.setItem(GameType.START, p)
                                TosoGameAPI.setPotionEffect(p)
                                TosoGameAPI.removeOp(p)

                                TosoGameAPI.showPlayers(p)
                                TosoGameAPI.teleport(p, worldConfig.jailLocationConfig.locations.values)

                                MissionManager.bossBar?.addPlayer(p)

                                p.sendMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}あなたを牢獄に追加しました。")
                                Bukkit.broadcastMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}${p.name}が逃走者から抜けました。")
                                continue
                            }
                            bs.sendMessage("${MainAPI.getPrefix(PrefixType.ERROR)}${p.name}はすでに逃走者から抜けています。")
                            continue
                        } else {
                            MainAPI.sendOfflineMessage(bs, args[i])
                            continue
                        }
                    }
                    return
                }
                MainAPI.sendMessage(bs, ErrorMessage.ARGS_PLAYER)
                return
            } else if (args[0].equals("leave", true)) {
                if (args.size != 1) {
                    for (i in 1 until args.size) {
                        val p = Bukkit.getPlayerExact(args[i])
                        if (p != null) {
                            if (Teams.hasJoinedTeam(OnlineTeam.TOSO_PLAYER, p)) {
                                Teams.joinTeam(OnlineTeam.TOSO_JAIL, p)
                                p.gameMode = GameMode.ADVENTURE

                                TosoGameAPI.setItem(GameType.START, p)
                                TosoGameAPI.setPotionEffect(p)
                                TosoGameAPI.removeOp(p)

                                TosoGameAPI.showPlayers(p)
                                TosoGameAPI.teleport(p, worldConfig.jailLocationConfig.locations.values)

                                MissionManager.bossBar?.addPlayer(p)

                                p.sendMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}あなたを牢獄に追加しました。")
                                Bukkit.broadcastMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}${p.name}が逃走者から抜けました。")
                                continue
                            }
                            bs.sendMessage("${MainAPI.getPrefix(PrefixType.ERROR)}${p.name}はすでに逃走者になっています。")
                            continue
                        } else {
                            MainAPI.sendOfflineMessage(bs, args[i])
                            continue
                        }
                    }
                    return
                }
                MainAPI.sendMessage(bs, ErrorMessage.ARGS_PLAYER)
                return
            }
            bs.sendMessage("""
                ${MainAPI.getPrefix(PrefixType.ERROR)}引数が不正です。
                ${MainAPI.getPrefix(PrefixType.ERROR)}コマンドの使い方:
                ${MainAPI.getPrefix(PrefixType.ERROR)}"/player join <プレイヤー名>" - 逃走者に参加
                ${MainAPI.getPrefix(PrefixType.ERROR)}"/player leave <プレイヤー名>" - 逃走者から離脱
            """.trimIndent())
            return
        }
        bs.sendMessage("""
            ${MainAPI.getPrefix(PrefixType.ERROR)}引数が不正です。
            ${MainAPI.getPrefix(PrefixType.ERROR)}コマンドの使い方:
            ${MainAPI.getPrefix(PrefixType.ERROR)}"/player join <プレイヤー名>" - 逃走者に参加
            ${MainAPI.getPrefix(PrefixType.ERROR)}"/player leave <プレイヤー名>" - 逃走者から離脱
        """.trimIndent())
        return
    }

    override fun onConsoleCommand(cs: ConsoleCommandSender, cmd: Command, label: String, args: Array<String>) {
        MainAPI.sendMessage(cs, ErrorMessage.NOT_PLAYER)
        return
    }

    override fun onPlayerTabComplete(sp: Player, cmd: Command, alias: String, args: Array<String>): List<String>? {
        if (!TosoGameAPI.isAdmin(sp)) return null
        if (args.size == 1) {
            return getTabList(args[0], HashSet(Arrays.asList("join", "leave")))
        } else if (args.size == 2) {
            if (args[0].equals("join", true) || args[0].equals("leave", true)) {
                val set = mutableSetOf("@all", "@team:admin", "@team:success", "@team:jail", "@team:hunter", "@team:tuho")
                for (player in Bukkit.getOnlinePlayers()) set.add(player.name)
                return getTabList(args[1], HashSet(set))
            }
        }
        return null
    }

    override fun onBlockTabComplete(bs: BlockCommandSender, cmd: Command, alias: String, args: Array<String>): List<String>? {
        if (args.size != 1) return null
        return getTabList(args[0], HashSet(Arrays.asList("join", "leave")))
    }

    override fun onConsoleTabComplete(cs: ConsoleCommandSender, cmd: Command, alias: String, args: Array<String>): List<String>? {
        return null
    }
}
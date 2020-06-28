package jp.aoichaan0513.A_TosoGame_Live.Commands.Command

import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI
import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI.ErrorMessage
import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI.PrefixType
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.GameManager
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.GameManager.GameState
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.World.WorldConfig
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.World.WorldManager
import jp.aoichaan0513.A_TosoGame_Live.API.Scoreboard.Teams
import jp.aoichaan0513.A_TosoGame_Live.API.Scoreboard.Teams.OnlineTeam
import jp.aoichaan0513.A_TosoGame_Live.API.TosoGameAPI
import jp.aoichaan0513.A_TosoGame_Live.Commands.ICommand
import jp.aoichaan0513.A_TosoGame_Live.Main
import jp.aoichaan0513.A_TosoGame_Live.Mission.MissionManager
import jp.aoichaan0513.A_TosoGame_Live.Utils.ParseUtil
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.GameMode
import org.bukkit.command.BlockCommandSender
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import java.util.*

class Tuho(name: String) : ICommand(name) {
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
                                        Teams.joinTeam(OnlineTeam.TOSO_TUHO, p)
                                        p.gameMode = GameMode.ADVENTURE

                                        TosoGameAPI.setItem(WorldManager.GameType.START, p)
                                        TosoGameAPI.setPotionEffect(p)
                                        TosoGameAPI.removeOp(p)

                                        TosoGameAPI.showPlayers(p)
                                        p.teleport(worldConfig.hunterLocationConfig.getLocation(1))

                                        MissionManager.bossBar?.removePlayer(p)

                                        p.sendMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}あなたを通報部隊に追加しました。")
                                        Bukkit.broadcastMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}${p.name}が通報部隊になりました。")
                                    }
                                }
                                sp.sendMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}${ChatColor.GREEN}${ChatColor.UNDERLINE}${i}人${ChatColor.RESET}${ChatColor.GRAY}を通報部隊に追加しました。")
                                return
                            } else if (arg.startsWith("team")) {
                                if (arg.length > 5) {
                                    val team = arg.substring(5)
                                    if (team.equals("admin", true)) {
                                        val i = Teams.getOnlineCount(OnlineTeam.TOSO_ADMIN)
                                        for (p in Bukkit.getOnlinePlayers()) {
                                            if (Teams.hasJoinedTeam(OnlineTeam.TOSO_ADMIN, p)) {
                                                Teams.joinTeam(OnlineTeam.TOSO_TUHO, p)
                                                p.gameMode = GameMode.ADVENTURE

                                                TosoGameAPI.setItem(WorldManager.GameType.START, p)
                                                TosoGameAPI.setPotionEffect(p)
                                                TosoGameAPI.removeOp(p)

                                                TosoGameAPI.showPlayers(p)
                                                p.teleport(worldConfig.hunterLocationConfig.getLocation(1))

                                                MissionManager.bossBar?.removePlayer(p)

                                                p.sendMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}あなたを通報部隊に追加しました。")
                                                Bukkit.broadcastMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}${p.name}が通報部隊になりました。")
                                            }
                                        }
                                        sp.sendMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}${ChatColor.GREEN}${ChatColor.UNDERLINE}${i}人${ChatColor.RESET}${ChatColor.GRAY}を通報部隊に追加しました。")
                                        return
                                    } else if (team.equals("player", true)) {
                                        val i = Teams.getOnlineCount(OnlineTeam.TOSO_PLAYER)
                                        for (p in Bukkit.getOnlinePlayers()) {
                                            if (Teams.hasJoinedTeam(OnlineTeam.TOSO_PLAYER, p)) {
                                                Teams.joinTeam(OnlineTeam.TOSO_TUHO, p)
                                                p.gameMode = GameMode.ADVENTURE

                                                TosoGameAPI.setItem(WorldManager.GameType.START, p)
                                                TosoGameAPI.setPotionEffect(p)
                                                TosoGameAPI.removeOp(p)

                                                TosoGameAPI.showPlayers(p)
                                                p.teleport(worldConfig.hunterLocationConfig.getLocation(1))

                                                MissionManager.bossBar?.removePlayer(p)

                                                p.sendMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}あなたを通報部隊に追加しました。")
                                                Bukkit.broadcastMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}${p.name}が通報部隊になりました。")
                                            }
                                        }
                                        sp.sendMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}${ChatColor.GREEN}${ChatColor.UNDERLINE}${i}人${ChatColor.RESET}${ChatColor.GRAY}を通報部隊に追加しました。")
                                        return
                                    } else if (team.equals("success", true)) {
                                        val i = Teams.getOnlineCount(OnlineTeam.TOSO_SUCCESS)
                                        for (p in Bukkit.getOnlinePlayers()) {
                                            if (Teams.hasJoinedTeam(OnlineTeam.TOSO_SUCCESS, p)) {
                                                Teams.joinTeam(OnlineTeam.TOSO_TUHO, p)
                                                p.gameMode = GameMode.ADVENTURE

                                                TosoGameAPI.setItem(WorldManager.GameType.START, p)
                                                TosoGameAPI.setPotionEffect(p)
                                                TosoGameAPI.removeOp(p)

                                                TosoGameAPI.showPlayers(p)
                                                p.teleport(worldConfig.hunterLocationConfig.getLocation(1))

                                                MissionManager.bossBar?.removePlayer(p)

                                                p.sendMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}あなたを通報部隊に追加しました。")
                                                Bukkit.broadcastMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}${p.name}が通報部隊になりました。")
                                            }
                                        }
                                        sp.sendMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}${ChatColor.GREEN}${ChatColor.UNDERLINE}${i}人${ChatColor.RESET}${ChatColor.GRAY}を通報部隊に追加しました。")
                                        return
                                    } else if (team.equals("jail", true)) {
                                        val i = Teams.getOnlineCount(OnlineTeam.TOSO_JAIL)
                                        for (p in Bukkit.getOnlinePlayers()) {
                                            if (Teams.hasJoinedTeam(OnlineTeam.TOSO_JAIL, p)) {
                                                Teams.joinTeam(OnlineTeam.TOSO_TUHO, p)
                                                p.gameMode = GameMode.ADVENTURE

                                                TosoGameAPI.setItem(WorldManager.GameType.START, p)
                                                TosoGameAPI.setPotionEffect(p)
                                                TosoGameAPI.removeOp(p)

                                                TosoGameAPI.showPlayers(p)
                                                p.teleport(worldConfig.hunterLocationConfig.getLocation(1))

                                                MissionManager.bossBar?.removePlayer(p)

                                                p.sendMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}あなたを通報部隊に追加しました。")
                                                Bukkit.broadcastMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}${p.name}が通報部隊になりました。")
                                            }
                                        }
                                        sp.sendMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}${ChatColor.GREEN}${ChatColor.UNDERLINE}${i}人${ChatColor.RESET}${ChatColor.GRAY}を通報部隊に追加しました。")
                                        return
                                    } else if (team.equals("hunter", true)) {
                                        val i = Teams.getOnlineCount(OnlineTeam.TOSO_HUNTER)
                                        for (p in Bukkit.getOnlinePlayers()) {
                                            if (Teams.hasJoinedTeam(OnlineTeam.TOSO_HUNTER, p)) {
                                                Teams.joinTeam(OnlineTeam.TOSO_TUHO, p)
                                                p.gameMode = GameMode.ADVENTURE

                                                TosoGameAPI.setItem(WorldManager.GameType.START, p)
                                                TosoGameAPI.setPotionEffect(p)
                                                TosoGameAPI.removeOp(p)

                                                TosoGameAPI.showPlayers(p)
                                                p.teleport(worldConfig.hunterLocationConfig.getLocation(1))

                                                MissionManager.bossBar?.removePlayer(p)

                                                p.sendMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}あなたを通報部隊に追加しました。")
                                                Bukkit.broadcastMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}${p.name}が通報部隊になりました。")
                                            }
                                        }
                                        sp.sendMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}${ChatColor.GREEN}${ChatColor.UNDERLINE}${i}人${ChatColor.RESET}${ChatColor.GRAY}を通報部隊に追加しました。")
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
                                    if (!Teams.hasJoinedTeam(OnlineTeam.TOSO_TUHO, p)) {
                                        Teams.joinTeam(OnlineTeam.TOSO_TUHO, p)
                                        p.gameMode = GameMode.ADVENTURE

                                        TosoGameAPI.setItem(WorldManager.GameType.START, p)
                                        TosoGameAPI.setPotionEffect(p)
                                        TosoGameAPI.removeOp(p)

                                        TosoGameAPI.showPlayers(p)
                                        p.teleport(worldConfig.hunterLocationConfig.getLocation(1))

                                        MissionManager.bossBar?.removePlayer(p)

                                        p.sendMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}あなたを通報部隊に追加しました。")
                                        Bukkit.broadcastMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}${p.name}が通報部隊になりました。")
                                        continue
                                    }
                                    sp.sendMessage("${MainAPI.getPrefix(PrefixType.ERROR)}${p.name}はすでに通報部隊になっています。")
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
                                val i = Teams.getOnlineCount(OnlineTeam.TOSO_TUHO)
                                for (p in Bukkit.getOnlinePlayers()) {
                                    if (Teams.hasJoinedTeam(OnlineTeam.TOSO_TUHO, p)) {
                                        Teams.joinTeam(OnlineTeam.TOSO_JAIL, p)
                                        p.gameMode = GameMode.ADVENTURE

                                        TosoGameAPI.setItem(WorldManager.GameType.START, p)
                                        TosoGameAPI.setPotionEffect(p)
                                        TosoGameAPI.removeOp(p)

                                        TosoGameAPI.showPlayers(p)
                                        TosoGameAPI.teleport(p, worldConfig.jailLocationConfig.locations.values)

                                        MissionManager.bossBar?.addPlayer(p)

                                        p.sendMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}あなたを牢獄に追加しました。")
                                        Bukkit.broadcastMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}${p.name}が通報部隊から抜けました。")
                                    }
                                }
                                sp.sendMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}${ChatColor.GREEN}${ChatColor.UNDERLINE}${i}人${ChatColor.RESET}${ChatColor.GRAY}が通報部隊から抜けました。")
                                return
                            } else if (arg.startsWith("team")) {
                                if (arg.length > 5) {
                                    val team = arg.substring(5)
                                    if (team.equals("admin", true)) {
                                        val i = Teams.getOnlineCount(OnlineTeam.TOSO_TUHO)
                                        for (p in Bukkit.getOnlinePlayers()) {
                                            if (Teams.hasJoinedTeam(OnlineTeam.TOSO_TUHO, p)) {
                                                Teams.joinTeam(OnlineTeam.TOSO_ADMIN, p)
                                                p.gameMode = GameMode.CREATIVE

                                                TosoGameAPI.setItem(WorldManager.GameType.START, p)
                                                TosoGameAPI.setPotionEffect(p)
                                                TosoGameAPI.addOp(p)

                                                TosoGameAPI.showPlayers(p)
                                                TosoGameAPI.teleport(p, worldConfig.respawnLocationConfig.locations.values)

                                                MissionManager.bossBar?.addPlayer(p)

                                                p.sendMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}あなたを運営に追加しました。")
                                                Bukkit.broadcastMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}${p.name}が通報部隊から抜けました。")
                                            }
                                        }
                                        sp.sendMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}${ChatColor.GREEN}${ChatColor.UNDERLINE}${i}人${ChatColor.RESET}${ChatColor.GRAY}が通報部隊から抜けました。")
                                        return
                                    } else if (team.equals("player", true)) {
                                        val i = Teams.getOnlineCount(OnlineTeam.TOSO_TUHO)
                                        for (p in Bukkit.getOnlinePlayers()) {
                                            if (Teams.hasJoinedTeam(OnlineTeam.TOSO_TUHO, p)) {
                                                Teams.joinTeam(OnlineTeam.TOSO_PLAYER, p)
                                                p.gameMode = GameMode.ADVENTURE

                                                TosoGameAPI.setItem(WorldManager.GameType.START, p)
                                                TosoGameAPI.setPotionEffect(p)
                                                TosoGameAPI.removeOp(p)

                                                TosoGameAPI.showPlayers(p)
                                                TosoGameAPI.teleport(p, worldConfig.respawnLocationConfig.locations.values)

                                                MissionManager.bossBar?.addPlayer(p)

                                                p.sendMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}あなたを逃走者に追加しました。")
                                                Bukkit.broadcastMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}${p.name}が通報部隊から抜けました。")
                                            }
                                        }
                                        sp.sendMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}${ChatColor.GREEN}${ChatColor.UNDERLINE}${i}人${ChatColor.RESET}${ChatColor.GRAY}が通報部隊から抜けました。")
                                        return
                                    } else if (team.equals("success", true)) {
                                        val i = Teams.getOnlineCount(OnlineTeam.TOSO_TUHO)
                                        for (p in Bukkit.getOnlinePlayers()) {
                                            if (Teams.hasJoinedTeam(OnlineTeam.TOSO_TUHO, p)) {
                                                Teams.joinTeam(OnlineTeam.TOSO_SUCCESS, p)
                                                p.gameMode = GameMode.ADVENTURE

                                                TosoGameAPI.setItem(WorldManager.GameType.START, p)
                                                TosoGameAPI.setPotionEffect(p)
                                                TosoGameAPI.removeOp(p)

                                                TosoGameAPI.showPlayers(p)
                                                TosoGameAPI.teleport(p, worldConfig.respawnLocationConfig.locations.values)

                                                MissionManager.bossBar?.addPlayer(p)

                                                p.sendMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}あなたを生存者に追加しました。")
                                                Bukkit.broadcastMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}${p.name}が通報部隊から抜けました。")
                                            }
                                        }
                                        sp.sendMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}${ChatColor.GREEN}${ChatColor.UNDERLINE}${i}人${ChatColor.RESET}${ChatColor.GRAY}が通報部隊から抜けました。")
                                        return
                                    } else if (team.equals("jail", true)) {
                                        val i = Teams.getOnlineCount(OnlineTeam.TOSO_TUHO)
                                        for (p in Bukkit.getOnlinePlayers()) {
                                            if (Teams.hasJoinedTeam(OnlineTeam.TOSO_TUHO, p)) {
                                                Teams.joinTeam(OnlineTeam.TOSO_JAIL, p)
                                                p.gameMode = GameMode.ADVENTURE

                                                TosoGameAPI.setItem(WorldManager.GameType.START, p)
                                                TosoGameAPI.setPotionEffect(p)
                                                TosoGameAPI.removeOp(p)

                                                TosoGameAPI.showPlayers(p)
                                                TosoGameAPI.teleport(p, worldConfig.jailLocationConfig.locations.values)

                                                MissionManager.bossBar?.addPlayer(p)

                                                p.sendMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}あなたを牢獄に追加しました。")
                                                Bukkit.broadcastMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}${p.name}が通報部隊から抜けました。")
                                            }
                                        }
                                        sp.sendMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}${ChatColor.GREEN}${ChatColor.UNDERLINE}${i}人${ChatColor.RESET}${ChatColor.GRAY}が通報部隊から抜けました。")
                                        return
                                    } else if (team.equals("hunter", true)) {
                                        val i = Teams.getOnlineCount(OnlineTeam.TOSO_TUHO)
                                        for (p in Bukkit.getOnlinePlayers()) {
                                            if (Teams.hasJoinedTeam(OnlineTeam.TOSO_TUHO, p)) {
                                                Teams.joinTeam(OnlineTeam.TOSO_HUNTER, p)
                                                p.gameMode = GameMode.ADVENTURE

                                                TosoGameAPI.setItem(WorldManager.GameType.START, p)
                                                TosoGameAPI.setPotionEffect(p, true)
                                                TosoGameAPI.removeOp(p)

                                                TosoGameAPI.showPlayers(p)
                                                p.teleport(worldConfig.hunterLocationConfig.getLocation(1))

                                                MissionManager.bossBar?.addPlayer(p)

                                                p.sendMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}あなたをハンターに追加しました。")
                                                Bukkit.broadcastMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}${p.name}が通報部隊から抜けました。")
                                            }
                                        }
                                        sp.sendMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}${ChatColor.GREEN}${ChatColor.UNDERLINE}${i}人${ChatColor.RESET}${ChatColor.GRAY}が通報部隊から抜けました。")
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
                                    if (Teams.hasJoinedTeam(OnlineTeam.TOSO_TUHO, p)) {
                                        Teams.joinTeam(OnlineTeam.TOSO_JAIL, p)
                                        p.gameMode = GameMode.ADVENTURE

                                        TosoGameAPI.setItem(WorldManager.GameType.START, p)
                                        TosoGameAPI.setPotionEffect(p)
                                        TosoGameAPI.removeOp(p)

                                        TosoGameAPI.showPlayers(p)
                                        TosoGameAPI.teleport(p, worldConfig.jailLocationConfig.locations.values)

                                        MissionManager.bossBar?.addPlayer(p)

                                        p.sendMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}あなたを牢獄に追加しました。")
                                        Bukkit.broadcastMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}${p.name}が通報部隊から抜けました。")
                                        continue
                                    }
                                    sp.sendMessage("${MainAPI.getPrefix(PrefixType.ERROR)}${p.name}はすでに通報部隊から抜けています。")
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
                } else if (args[0].equals("random", true) || args[0].equals("rand", true)) {
                    if (args.size != 1) {
                        random(sp, args[1], worldConfig)
                        return
                    }
                    MainAPI.sendMessage(sp, ErrorMessage.ARGS_INTEGER)
                    return
                }
                sp.sendMessage("""
                    ${MainAPI.getPrefix(PrefixType.ERROR)}引数が不正です。
                    ${MainAPI.getPrefix(PrefixType.ERROR)}コマンドの使い方:
                    ${MainAPI.getPrefix(PrefixType.ERROR)}"/tuho join <プレイヤー名>" - 通報部隊に参加
                    ${MainAPI.getPrefix(PrefixType.ERROR)}"/tuho leave <プレイヤー名>" - 通報部隊から離脱
                    ${MainAPI.getPrefix(PrefixType.ERROR)}"/tuho random <数値>" または "/tuho rand <数値>" - 数値の数だけ通報部隊をランダムで追加
                """.trimIndent())
                return
            }
            sp.sendMessage("""
                ${MainAPI.getPrefix(PrefixType.ERROR)}引数が不正です。
                ${MainAPI.getPrefix(PrefixType.ERROR)}コマンドの使い方:
                ${MainAPI.getPrefix(PrefixType.ERROR)}"/tuho join <プレイヤー名>" - 通報部隊に参加
                ${MainAPI.getPrefix(PrefixType.ERROR)}"/tuho leave <プレイヤー名>" - 通報部隊から離脱
                ${MainAPI.getPrefix(PrefixType.ERROR)}"/tuho random <数値>" または "/tuho rand <数値>" - 数値の数だけ通報部隊をランダムで追加
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
                            if (!Teams.hasJoinedTeam(OnlineTeam.TOSO_TUHO, p)) {
                                Teams.joinTeam(OnlineTeam.TOSO_TUHO, p)
                                p.gameMode = GameMode.ADVENTURE

                                TosoGameAPI.setItem(WorldManager.GameType.START, p)
                                TosoGameAPI.setPotionEffect(p)
                                TosoGameAPI.removeOp(p)

                                TosoGameAPI.showPlayers(p)
                                p.teleport(worldConfig.hunterLocationConfig.getLocation(1))

                                MissionManager.bossBar?.removePlayer(p)

                                p.sendMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}あなたを通報部隊に追加しました。")
                                Bukkit.broadcastMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}${p.name}が通報部隊になりました。")
                                continue
                            }
                            bs.sendMessage("${MainAPI.getPrefix(PrefixType.ERROR)}${p.name}はすでに通報部隊になっています。")
                            continue
                        }
                        MainAPI.sendOfflineMessage(bs, args[i])
                        continue
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
                            if (Teams.hasJoinedTeam(OnlineTeam.TOSO_TUHO, p)) {
                                Teams.joinTeam(OnlineTeam.TOSO_JAIL, p)
                                p.gameMode = GameMode.ADVENTURE

                                TosoGameAPI.setItem(WorldManager.GameType.START, p)
                                TosoGameAPI.setPotionEffect(p)
                                TosoGameAPI.removeOp(p)

                                TosoGameAPI.showPlayers(p)
                                TosoGameAPI.teleport(p, worldConfig.jailLocationConfig.locations.values)

                                MissionManager.bossBar?.addPlayer(p)

                                p.sendMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}あなたを牢獄に追加しました。")
                                Bukkit.broadcastMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}${p.name}が通報部隊から抜けました。")
                                continue
                            }
                            bs.sendMessage("${MainAPI.getPrefix(PrefixType.ERROR)}${p.name}はすでに通報部隊から抜けています。")
                            continue
                        }
                        MainAPI.sendOfflineMessage(bs, args[i])
                        continue
                    }
                    return
                }
                MainAPI.sendMessage(bs, ErrorMessage.ARGS_PLAYER)
                return
            } else if (args[0].equals("random", true) || args[0].equals("rand", true)) {
                if (args.size != 1) {
                    random(bs, args[1], worldConfig)
                    return
                }
                MainAPI.sendMessage(bs, ErrorMessage.ARGS_INTEGER)
                return
            }
            bs.sendMessage("""
                ${MainAPI.getPrefix(PrefixType.ERROR)}引数が不正です。
                ${MainAPI.getPrefix(PrefixType.ERROR)}コマンドの使い方:
                ${MainAPI.getPrefix(PrefixType.ERROR)}"/tuho join <プレイヤー名>" - 通報部隊に参加
                ${MainAPI.getPrefix(PrefixType.ERROR)}"/tuho leave <プレイヤー名>" - 通報部隊から離脱
                ${MainAPI.getPrefix(PrefixType.ERROR)}"/tuho random <数値>" または "/tuho rand <数値>" - 数値の数だけ通報部隊をランダムで追加
            """.trimIndent())
            return
        }
        bs.sendMessage("""
            ${MainAPI.getPrefix(PrefixType.ERROR)}引数が不正です。
            ${MainAPI.getPrefix(PrefixType.ERROR)}コマンドの使い方:
            ${MainAPI.getPrefix(PrefixType.ERROR)}"/tuho join <プレイヤー名>" - 通報部隊に参加
            ${MainAPI.getPrefix(PrefixType.ERROR)}"/tuho leave <プレイヤー名>" - 通報部隊から離脱
            ${MainAPI.getPrefix(PrefixType.ERROR)}"/tuho random <数値>" または "/tuho rand <数値>" - 数値の数だけ通報部隊をランダムで追加
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
            return getTabList(args[0], "join", "leave", "random", "rand")
        } else if (args.size == 2) {
            if (args[0].equals("join", true) || args[0].equals("leave", true)) {
                val set = mutableSetOf("@all", "@team:admin", "@team:player", "@team:success", "@team:jail", "@team:hunter")
                for (player in Bukkit.getOnlinePlayers()) set.add(player.name)
                return getTabList(args[1], set)
            }
        }
        return null
    }

    override fun onBlockTabComplete(bs: BlockCommandSender, cmd: Command, alias: String, args: Array<String>): List<String>? {
        if (args.size != 1) return null
        return getTabList(args[0], "join", "leave", "random", "rand")
    }

    override fun onConsoleTabComplete(cs: ConsoleCommandSender, cmd: Command, alias: String, args: Array<String>): List<String>? {
        return null
    }

    private fun random(sender: CommandSender, c: String, worldConfig: WorldConfig) {
        if (ParseUtil.isInt(c)) {
            if (num == -1) {
                Main.tuhoShuffleSet.clear()
                num = c.toInt()

                sender.sendMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}通報部隊募集を開始しました。")
                if (GameManager.isGame(GameState.GAME)) {
                    for (player in Bukkit.getOnlinePlayers())
                        if (Teams.hasJoinedTeam(OnlineTeam.TOSO_JAIL, player))
                            player.sendMessage("""
                                ${MainAPI.getPrefix(ChatColor.YELLOW)}通報部隊を${ChatColor.GOLD}${ChatColor.UNDERLINE}${num}人${ChatColor.RESET}${ChatColor.YELLOW}募集します。
                                ${MainAPI.getPrefix(PrefixType.SECONDARY)}通報部隊を希望の方は20秒以内に"${ChatColor.UNDERLINE}/t${ChatColor.RESET}${ChatColor.GRAY}"と入力してください。
                            """.trimIndent())
                } else {
                    Bukkit.broadcastMessage("""
                        ${MainAPI.getPrefix(ChatColor.YELLOW)}通報部隊を${ChatColor.GOLD}${ChatColor.UNDERLINE}${num}人${ChatColor.RESET}${ChatColor.YELLOW}募集します。
                        ${MainAPI.getPrefix(PrefixType.SECONDARY)}通報部隊を希望の方は20秒以内に"${ChatColor.UNDERLINE}/t${ChatColor.RESET}${ChatColor.GRAY}"と入力してください。
                    """.trimIndent())
                }

                object : BukkitRunnable() {
                    var count = 20

                    override fun run() {
                        if (count == 10) {
                            Bukkit.broadcastMessage("${MainAPI.getPrefix(ChatColor.YELLOW)}通報部隊の募集終了まで${ChatColor.GOLD}${ChatColor.UNDERLINE}残り10秒${ChatColor.RESET}${ChatColor.YELLOW}です。")
                        } else if (count == 0) {
                            if (!Main.tuhoShuffleSet.isEmpty()) {
                                Bukkit.broadcastMessage("${MainAPI.getPrefix(ChatColor.YELLOW)}通報部隊を${ChatColor.GOLD}${ChatColor.UNDERLINE}${num}人${ChatColor.RESET}${ChatColor.YELLOW}選出しています…")

                                val tuhoShuffleList = MainAPI.getOnlinePlayers(Main.tuhoShuffleSet).toMutableList()
                                Collections.shuffle(tuhoShuffleList)

                                var i = 0
                                while (i < num && i < tuhoShuffleList.size) {
                                    val p = tuhoShuffleList.removeAt(i)

                                    Teams.joinTeam(OnlineTeam.TOSO_TUHO, p)
                                    p.gameMode = GameMode.ADVENTURE

                                    TosoGameAPI.setItem(WorldManager.GameType.START, p)
                                    TosoGameAPI.setPotionEffect(p, true)
                                    TosoGameAPI.removeOp(p)

                                    TosoGameAPI.showPlayers(p)
                                    p.teleport(worldConfig.hunterLocationConfig.getLocation(1))

                                    MissionManager.bossBar?.removePlayer(p)

                                    p.sendMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}あなたを通報部隊に追加しました。")
                                    Bukkit.broadcastMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}${p.name}が通報部隊になりました。")
                                    i++
                                }
                                sender.sendMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}${ChatColor.GREEN}${ChatColor.UNDERLINE}${num}人${ChatColor.RESET}${ChatColor.GRAY}を通報部隊に追加しました。")
                                Main.tuhoShuffleSet.clear()
                                num = -1
                            } else {
                                Bukkit.broadcastMessage("${MainAPI.getPrefix(PrefixType.ERROR)}通報部隊を希望する方がいなかったため選出をキャンセルしました。")
                                Main.tuhoShuffleSet.clear()
                                num = -1
                            }
                        }
                        count--
                    }
                }.runTaskTimer(Main.pluginInstance, 0, 20)
                return
            }
            sender.sendMessage("${MainAPI.getPrefix(PrefixType.ERROR)}通報部隊募集中のため実行できません。")
        } else {
            MainAPI.sendMessage(sender, ErrorMessage.ARGS_INTEGER)
        }
    }

    companion object {
        var num = -1
    }
}
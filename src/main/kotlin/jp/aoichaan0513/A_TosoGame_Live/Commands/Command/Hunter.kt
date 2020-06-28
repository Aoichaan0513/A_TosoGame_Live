package jp.aoichaan0513.A_TosoGame_Live.Commands.Command

import jp.aoichaan0513.A_TosoGame_Live.API.Enums.Advancement
import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI
import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI.ErrorMessage
import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI.PrefixType
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.GameManager
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.GameManager.GameState
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.Player.PlayerManager
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.World.WorldConfig
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.World.WorldManager
import jp.aoichaan0513.A_TosoGame_Live.API.Scoreboard.Teams
import jp.aoichaan0513.A_TosoGame_Live.API.Scoreboard.Teams.OnlineTeam
import jp.aoichaan0513.A_TosoGame_Live.API.TosoGameAPI
import jp.aoichaan0513.A_TosoGame_Live.Commands.ICommand
import jp.aoichaan0513.A_TosoGame_Live.Main
import jp.aoichaan0513.A_TosoGame_Live.Mission.MissionManager
import jp.aoichaan0513.A_TosoGame_Live.Utils.ParseUtil
import org.bukkit.*
import org.bukkit.GameMode
import org.bukkit.command.BlockCommandSender
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.entity.Zombie
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import java.util.*

class Hunter(name: String) : ICommand(name) {
    companion object {

        var num = -1
    }

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
                                        Teams.joinTeam(OnlineTeam.TOSO_HUNTER, p)
                                        p.gameMode = GameMode.ADVENTURE

                                        TosoGameAPI.setItem(WorldManager.GameType.START, p)
                                        TosoGameAPI.setPotionEffect(p, true)
                                        TosoGameAPI.removeOp(p)

                                        TosoGameAPI.showPlayers(p)
                                        p.teleport(worldConfig.hunterLocationConfig.getLocation(1))

                                        MissionManager.bossBar?.removePlayer(p)

                                        p.sendMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}あなたをハンターに追加しました。")
                                        Bukkit.broadcastMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}${p.name}がハンターになりました。")
                                    }
                                }
                                sp.sendMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}${ChatColor.GREEN}${ChatColor.UNDERLINE}${i}人${ChatColor.RESET}${ChatColor.GRAY}をハンターに追加しました。")
                                return
                            } else if (arg.startsWith("team")) {
                                if (arg.length > 5) {
                                    val team = arg.substring(5)
                                    if (team.equals("admin", true)) {
                                        val i = Teams.getOnlineCount(OnlineTeam.TOSO_ADMIN)
                                        for (p in Bukkit.getOnlinePlayers()) {
                                            if (Teams.hasJoinedTeam(OnlineTeam.TOSO_ADMIN, p)) {
                                                Teams.joinTeam(OnlineTeam.TOSO_HUNTER, p)
                                                p.gameMode = GameMode.ADVENTURE

                                                TosoGameAPI.setItem(WorldManager.GameType.START, p)
                                                TosoGameAPI.setPotionEffect(p, true)
                                                TosoGameAPI.removeOp(p)

                                                TosoGameAPI.showPlayers(p)
                                                p.teleport(worldConfig.hunterLocationConfig.getLocation(1))

                                                MissionManager.bossBar?.removePlayer(p)

                                                p.sendMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}あなたをハンターに追加しました。")
                                                Bukkit.broadcastMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}${p.name}がハンターになりました。")
                                            }
                                        }
                                        sp.sendMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}${ChatColor.GREEN}${ChatColor.UNDERLINE}${i}人${ChatColor.RESET}${ChatColor.GRAY}をハンターに追加しました。")
                                        return
                                    } else if (team.equals("player", true)) {
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

                                                MissionManager.bossBar?.removePlayer(p)

                                                p.sendMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}あなたをハンターに追加しました。")
                                                Bukkit.broadcastMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}${p.name}がハンターになりました。")
                                            }
                                        }
                                        sp.sendMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}${ChatColor.GREEN}${ChatColor.UNDERLINE}${i}人${ChatColor.RESET}${ChatColor.GRAY}をハンターに追加しました。")
                                        return
                                    } else if (team.equals("success", true)) {
                                        val i = Teams.getOnlineCount(OnlineTeam.TOSO_SUCCESS)
                                        for (p in Bukkit.getOnlinePlayers()) {
                                            if (Teams.hasJoinedTeam(OnlineTeam.TOSO_SUCCESS, p)) {
                                                Teams.joinTeam(OnlineTeam.TOSO_HUNTER, p)
                                                p.gameMode = GameMode.ADVENTURE

                                                TosoGameAPI.setItem(WorldManager.GameType.START, p)
                                                TosoGameAPI.setPotionEffect(p, true)
                                                TosoGameAPI.removeOp(p)

                                                TosoGameAPI.showPlayers(p)
                                                p.teleport(worldConfig.hunterLocationConfig.getLocation(1))

                                                MissionManager.bossBar?.removePlayer(p)

                                                p.sendMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}あなたをハンターに追加しました。")
                                                Bukkit.broadcastMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}${p.name}がハンターになりました。")
                                            }
                                        }
                                        sp.sendMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}${ChatColor.GREEN}${ChatColor.UNDERLINE}${i}人${ChatColor.RESET}${ChatColor.GRAY}をハンターに追加しました。")
                                        return
                                    } else if (team.equals("jail", true)) {
                                        val i = Teams.getOnlineCount(OnlineTeam.TOSO_JAIL)
                                        for (p in Bukkit.getOnlinePlayers()) {
                                            if (Teams.hasJoinedTeam(OnlineTeam.TOSO_JAIL, p)) {
                                                Teams.joinTeam(OnlineTeam.TOSO_HUNTER, p)
                                                p.gameMode = GameMode.ADVENTURE

                                                TosoGameAPI.setItem(WorldManager.GameType.START, p)
                                                TosoGameAPI.setPotionEffect(p, true)
                                                TosoGameAPI.removeOp(p)

                                                TosoGameAPI.showPlayers(p)
                                                p.teleport(worldConfig.hunterLocationConfig.getLocation(1))

                                                MissionManager.bossBar?.removePlayer(p)

                                                p.sendMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}あなたをハンターに追加しました。")
                                                Bukkit.broadcastMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}${p.name}がハンターになりました。")
                                            }
                                        }
                                        sp.sendMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}${ChatColor.GREEN}${ChatColor.UNDERLINE}${i}人${ChatColor.RESET}${ChatColor.GRAY}をハンターに追加しました。")
                                        return
                                    } else if (team.equals("tuho", true)) {
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

                                                MissionManager.bossBar?.removePlayer(p)

                                                p.sendMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}あなたをハンターに追加しました。")
                                                Bukkit.broadcastMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}${p.name}がハンターになりました。")
                                            }
                                        }
                                        sp.sendMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}${ChatColor.GREEN}${ChatColor.UNDERLINE}${i}人${ChatColor.RESET}${ChatColor.GRAY}をハンターに追加しました。")
                                        return
                                    }
                                }
                                sp.sendMessage("${MainAPI.getPrefix(PrefixType.ERROR)}引数が不正です。チーム名を指定してください。")
                                return
                            }
                        } else {
                            for (i in 1 until args.size) {
                                val target = Bukkit.getPlayerExact(args[i])
                                if (target == null) {
                                    MainAPI.sendOfflineMessage(sp, args[i])
                                    continue
                                } else {
                                    val p = Bukkit.getPlayerExact(args[i])!!
                                    if (!Teams.hasJoinedTeam(OnlineTeam.TOSO_HUNTER, p)) {
                                        Teams.joinTeam(OnlineTeam.TOSO_HUNTER, p)
                                        p.gameMode = GameMode.ADVENTURE

                                        TosoGameAPI.setItem(WorldManager.GameType.START, p)
                                        TosoGameAPI.setPotionEffect(p, true)
                                        TosoGameAPI.removeOp(p)

                                        TosoGameAPI.showPlayers(p)
                                        p.teleport(worldConfig.hunterLocationConfig.getLocation(1))

                                        MissionManager.bossBar?.removePlayer(p)

                                        p.sendMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}あなたをハンターに追加しました。")
                                        Bukkit.broadcastMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}${p.name}がハンターになりました。")
                                        continue
                                    }
                                    sp.sendMessage("${MainAPI.getPrefix(PrefixType.ERROR)}${p.name}はすでにハンターになっています。")
                                    continue
                                }
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
                                val i = Teams.getOnlineCount(OnlineTeam.TOSO_HUNTER)
                                for (p in Bukkit.getOnlinePlayers()) {
                                    if (Teams.hasJoinedTeam(OnlineTeam.TOSO_HUNTER, p)) {
                                        Teams.joinTeam(OnlineTeam.TOSO_JAIL, p)
                                        p.gameMode = GameMode.ADVENTURE

                                        TosoGameAPI.setItem(WorldManager.GameType.START, p)
                                        TosoGameAPI.setPotionEffect(p, true)
                                        TosoGameAPI.removeOp(p)

                                        TosoGameAPI.showPlayers(p)
                                        TosoGameAPI.teleport(p, worldConfig.jailLocationConfig.locations.values)

                                        MissionManager.bossBar?.addPlayer(p)

                                        p.sendMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}あなたを牢獄に追加しました。")
                                        Bukkit.broadcastMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}${p.name}がハンターから抜けました。")
                                    }
                                }
                                sp.sendMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}${ChatColor.GREEN}${ChatColor.UNDERLINE}${i}人${ChatColor.RESET}${ChatColor.GRAY}がハンターから抜けました。")
                                return
                            } else if (arg.startsWith("team")) {
                                if (arg.length > 5) {
                                    val team = arg.substring(5)
                                    if (team.equals("admin", true)) {
                                        val i = Teams.getOnlineCount(OnlineTeam.TOSO_HUNTER)
                                        for (p in Bukkit.getOnlinePlayers()) {
                                            if (Teams.hasJoinedTeam(OnlineTeam.TOSO_HUNTER, p)) {
                                                Teams.joinTeam(OnlineTeam.TOSO_ADMIN, p)
                                                p.gameMode = GameMode.CREATIVE

                                                TosoGameAPI.setItem(WorldManager.GameType.START, p)
                                                TosoGameAPI.setPotionEffect(p, true)
                                                TosoGameAPI.addOp(p)

                                                TosoGameAPI.showPlayers(p)
                                                TosoGameAPI.teleport(p, worldConfig.respawnLocationConfig.locations.values)

                                                MissionManager.bossBar?.addPlayer(p)

                                                p.sendMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}あなたを運営に追加しました。")
                                                Bukkit.broadcastMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}${p.name}がハンターから抜けました。")
                                            }
                                        }
                                        sp.sendMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}${ChatColor.GREEN}${ChatColor.UNDERLINE}${i}人${ChatColor.RESET}${ChatColor.GRAY}がハンターから抜けました。")
                                        return
                                    } else if (team.equals("player", true)) {
                                        val i = Teams.getOnlineCount(OnlineTeam.TOSO_HUNTER)
                                        for (p in Bukkit.getOnlinePlayers()) {
                                            if (Teams.hasJoinedTeam(OnlineTeam.TOSO_HUNTER, p)) {
                                                Teams.joinTeam(OnlineTeam.TOSO_PLAYER, p)
                                                p.gameMode = GameMode.ADVENTURE

                                                TosoGameAPI.setItem(WorldManager.GameType.START, p)
                                                TosoGameAPI.setPotionEffect(p, true)
                                                TosoGameAPI.removeOp(p)

                                                TosoGameAPI.showPlayers(p)
                                                TosoGameAPI.teleport(p, worldConfig.respawnLocationConfig.locations.values)

                                                MissionManager.bossBar?.addPlayer(p)

                                                p.sendMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}あなたを逃走者に追加しました。")
                                                Bukkit.broadcastMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}${p.name}がハンターから抜けました。")
                                            }
                                        }
                                        sp.sendMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}${ChatColor.GREEN}${ChatColor.UNDERLINE}${i}人${ChatColor.RESET}${ChatColor.GRAY}がハンターから抜けました。")
                                        return
                                    } else if (team.equals("success", true)) {
                                        val i = Teams.getOnlineCount(OnlineTeam.TOSO_HUNTER)
                                        for (p in Bukkit.getOnlinePlayers()) {
                                            if (Teams.hasJoinedTeam(OnlineTeam.TOSO_HUNTER, p)) {
                                                Teams.joinTeam(OnlineTeam.TOSO_SUCCESS, p)
                                                p.gameMode = GameMode.ADVENTURE

                                                TosoGameAPI.setItem(WorldManager.GameType.START, p)
                                                TosoGameAPI.setPotionEffect(p, true)
                                                TosoGameAPI.removeOp(p)

                                                TosoGameAPI.showPlayers(p)
                                                TosoGameAPI.teleport(p, worldConfig.respawnLocationConfig.locations.values)

                                                MissionManager.bossBar?.addPlayer(p)

                                                p.sendMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}あなたを生存者に追加しました。")
                                                Bukkit.broadcastMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}${p.name}がハンターから抜けました。")
                                            }
                                        }
                                        sp.sendMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}${ChatColor.GREEN}${ChatColor.UNDERLINE}${i}人${ChatColor.RESET}${ChatColor.GRAY}がハンターから抜けました。")
                                        return
                                    } else if (team.equals("jail", true)) {
                                        val i = Teams.getOnlineCount(OnlineTeam.TOSO_HUNTER)
                                        for (p in Bukkit.getOnlinePlayers()) {
                                            if (Teams.hasJoinedTeam(OnlineTeam.TOSO_HUNTER, p)) {
                                                Teams.joinTeam(OnlineTeam.TOSO_JAIL, p)
                                                p.gameMode = GameMode.ADVENTURE

                                                TosoGameAPI.setItem(WorldManager.GameType.START, p)
                                                TosoGameAPI.setPotionEffect(p, true)
                                                TosoGameAPI.removeOp(p)

                                                TosoGameAPI.showPlayers(p)
                                                TosoGameAPI.teleport(p, worldConfig.jailLocationConfig.locations.values)

                                                MissionManager.bossBar?.addPlayer(p)

                                                p.sendMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}あなたを牢獄に追加しました。")
                                                Bukkit.broadcastMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}${p.name}がハンターから抜けました。")
                                            }
                                        }
                                        sp.sendMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}${ChatColor.GREEN}${ChatColor.UNDERLINE}${i}人${ChatColor.RESET}${ChatColor.GRAY}がハンターから抜けました。")
                                        return
                                    } else if (team.equals("tuho", true)) {
                                        val i = Teams.getOnlineCount(OnlineTeam.TOSO_HUNTER)
                                        for (p in Bukkit.getOnlinePlayers()) {
                                            if (Teams.hasJoinedTeam(OnlineTeam.TOSO_HUNTER, p)) {
                                                Teams.joinTeam(OnlineTeam.TOSO_TUHO, p)
                                                p.gameMode = GameMode.ADVENTURE

                                                TosoGameAPI.setItem(WorldManager.GameType.START, p)
                                                TosoGameAPI.setPotionEffect(p, true)
                                                TosoGameAPI.removeOp(p)

                                                TosoGameAPI.showPlayers(p)
                                                p.teleport(worldConfig.hunterLocationConfig.getLocation(1))

                                                MissionManager.bossBar?.removePlayer(p)

                                                p.sendMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}あなたを通報部隊に追加しました。")
                                                Bukkit.broadcastMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}${p.name}がハンターから抜けました。")
                                            }
                                        }
                                        sp.sendMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}${ChatColor.GREEN}${ChatColor.UNDERLINE}${i}人${ChatColor.RESET}${ChatColor.GRAY}がハンターから抜けました。")
                                        return
                                    }
                                }
                                sp.sendMessage("${MainAPI.getPrefix(PrefixType.ERROR)}引数が不正です。チーム名を指定してください。")
                                return
                            }
                        } else {
                            for (i in 1 until args.size) {
                                val target = Bukkit.getPlayerExact(args[i])
                                if (target == null) {
                                    MainAPI.sendOfflineMessage(sp, args[i])
                                    continue
                                } else {
                                    val p = Bukkit.getPlayerExact(args[i])!!
                                    if (Teams.hasJoinedTeam(OnlineTeam.TOSO_HUNTER, p)) {
                                        Teams.joinTeam(OnlineTeam.TOSO_JAIL, p)
                                        p.gameMode = GameMode.ADVENTURE

                                        TosoGameAPI.setItem(WorldManager.GameType.START, p)
                                        TosoGameAPI.setPotionEffect(p, true)
                                        TosoGameAPI.removeOp(p)

                                        TosoGameAPI.showPlayers(p)
                                        TosoGameAPI.teleport(p, worldConfig.jailLocationConfig.locations.values)

                                        MissionManager.bossBar?.addPlayer(p)

                                        p.sendMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}あなたを牢獄に追加しました。")
                                        Bukkit.broadcastMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}${p.name}がハンターから抜けました。")
                                        continue
                                    }
                                    sp.sendMessage(MainAPI.getPrefix(PrefixType.ERROR) + p.name + "はすでにハンターから抜けています。")
                                    continue
                                }
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
                } else if (args[0].equals("zombie", true)) {
                    if (WorldManager.world.difficulty != Difficulty.PEACEFUL) {
                        if (args.size != 1) {
                            if (ParseUtil.isInt(args[1])) {
                                val v = args[1].toInt().coerceAtMost(100)
                                if (v > 0) {
                                    Bukkit.broadcastMessage("${MainAPI.getPrefix(ChatColor.YELLOW)}ゾンビハンターを${ChatColor.GOLD}${ChatColor.UNDERLINE}${v}体${ChatColor.RESET}${ChatColor.YELLOW}追加しています…")
                                    for (i in 0 until v) {
                                        val z = WorldManager.world.spawnEntity(worldConfig.hunterLocationConfig.getLocation(1), EntityType.ZOMBIE) as Zombie
                                        z.isBaby = false
                                        z.equipment!!.helmet = ItemStack(Material.DIAMOND_HELMET)
                                        z.equipment!!.chestplate = ItemStack(Material.DIAMOND_CHESTPLATE)
                                        z.equipment!!.leggings = ItemStack(Material.DIAMOND_LEGGINGS)
                                        z.equipment!!.boots = ItemStack(Material.DIAMOND_BOOTS)
                                        z.addPotionEffect(PotionEffect(PotionEffectType.SPEED, 200000, 1, false, false))
                                    }
                                    sp.sendMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}${ChatColor.GREEN}${ChatColor.UNDERLINE}${v}体${ChatColor.RESET}${ChatColor.GRAY}をゾンビハンターに追加しました。")
                                    return
                                }
                                sp.sendMessage("${MainAPI.getPrefix(PrefixType.ERROR)}引数が不正です。1以上で数字を指定してください。")
                                return
                            } else {
                                MainAPI.sendMessage(sp, ErrorMessage.ARGS_INTEGER)
                                return
                            }
                        }
                        MainAPI.sendMessage(sp, ErrorMessage.ARGS_INTEGER)
                        return
                    }
                    sp.sendMessage("${MainAPI.getPrefix(PrefixType.ERROR)}このワールドの難易度がピースフルのため実行できません。")
                    return
                }
                sp.sendMessage("""
                    ${MainAPI.getPrefix(PrefixType.ERROR)}引数が不正です。
                    ${MainAPI.getPrefix(PrefixType.ERROR)}コマンドの使い方:
                    ${MainAPI.getPrefix(PrefixType.ERROR)}"/hunter join <プレイヤー名>" - ハンターに参加
                    ${MainAPI.getPrefix(PrefixType.ERROR)}"/hunter leave <プレイヤー名>" - ハンターから離脱
                    ${MainAPI.getPrefix(PrefixType.ERROR)}"/hunter random <数値>" または "/hunter rand <数値>" - 数値の数だけハンターをランダムで追加
                    ${MainAPI.getPrefix(PrefixType.ERROR)}"/hunter zombie <数値>" - 数値の数だけゾンビハンターを追加
                """.trimIndent())
                return
            }
            sp.sendMessage("""
                ${MainAPI.getPrefix(PrefixType.ERROR)}引数が不正です。
                ${MainAPI.getPrefix(PrefixType.ERROR)}コマンドの使い方:
                ${MainAPI.getPrefix(PrefixType.ERROR)}"/hunter join <プレイヤー名>" - ハンターに参加
                ${MainAPI.getPrefix(PrefixType.ERROR)}"/hunter leave <プレイヤー名>" - ハンターから離脱
                ${MainAPI.getPrefix(PrefixType.ERROR)}"/hunter random <数値>" または "/hunter rand <数値>" - 数値の数だけハンターをランダムで追加
                ${MainAPI.getPrefix(PrefixType.ERROR)}"/hunter zombie <数値>" - 数値の数だけゾンビハンターを追加
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
                        val target = Bukkit.getPlayerExact(args[i])
                        if (target == null) {
                            MainAPI.sendOfflineMessage(bs, args[i])
                            continue
                        } else {
                            val p = Bukkit.getPlayerExact(args[i])!!
                            if (!Teams.hasJoinedTeam(OnlineTeam.TOSO_HUNTER, p)) {
                                Teams.joinTeam(OnlineTeam.TOSO_HUNTER, p)
                                p.gameMode = GameMode.ADVENTURE

                                TosoGameAPI.setItem(WorldManager.GameType.START, p)
                                TosoGameAPI.setPotionEffect(p, true)
                                TosoGameAPI.removeOp(p)

                                TosoGameAPI.showPlayers(p)
                                p.teleport(worldConfig.hunterLocationConfig.getLocation(1))

                                MissionManager.bossBar?.removePlayer(p)

                                p.sendMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}あなたをハンターに追加しました。")
                                Bukkit.broadcastMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}${p.name}がハンターになりました。")
                                continue
                            }
                            bs.sendMessage("${MainAPI.getPrefix(PrefixType.ERROR)}${p.name}はすでにハンターになっています。")
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
                        val target = Bukkit.getPlayerExact(args[i])
                        if (target == null) {
                            MainAPI.sendOfflineMessage(bs, args[i])
                            continue
                        } else {
                            val p = Bukkit.getPlayerExact(args[i])!!
                            if (Teams.hasJoinedTeam(OnlineTeam.TOSO_HUNTER, p)) {
                                Teams.joinTeam(OnlineTeam.TOSO_JAIL, p)
                                p.gameMode = GameMode.ADVENTURE

                                TosoGameAPI.setItem(WorldManager.GameType.START, p)
                                TosoGameAPI.setPotionEffect(p, true)
                                TosoGameAPI.removeOp(p)

                                TosoGameAPI.showPlayers(p)
                                TosoGameAPI.teleport(p, worldConfig.jailLocationConfig.locations.values)

                                MissionManager.bossBar?.addPlayer(p)

                                p.sendMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}あなたを牢獄に追加しました。")
                                Bukkit.broadcastMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}${p.name}がハンターから抜けました。")
                                continue
                            }
                            bs.sendMessage("${MainAPI.getPrefix(PrefixType.ERROR)}${p.name}はすでにハンターから抜けています。")
                            continue
                        }
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
            } else if (args[0].equals("zombie", true)) {
                if (WorldManager.world.difficulty != Difficulty.PEACEFUL) {
                    if (args.size != 1) {
                        if (ParseUtil.isInt(args[1])) {
                            val v = args[1].toInt().coerceAtMost(100)
                            if (v > 0) {
                                Bukkit.broadcastMessage("${MainAPI.getPrefix(ChatColor.YELLOW)}ゾンビハンターを${ChatColor.GOLD}${ChatColor.UNDERLINE}${v}体${ChatColor.RESET}${ChatColor.YELLOW}追加しています…")
                                for (i in 0 until v) {
                                    val z = WorldManager.world.spawnEntity(worldConfig.hunterLocationConfig.getLocation(1), EntityType.ZOMBIE) as Zombie
                                    z.isBaby = false
                                    z.equipment!!.helmet = ItemStack(Material.DIAMOND_HELMET)
                                    z.equipment!!.chestplate = ItemStack(Material.DIAMOND_CHESTPLATE)
                                    z.equipment!!.leggings = ItemStack(Material.DIAMOND_LEGGINGS)
                                    z.equipment!!.boots = ItemStack(Material.DIAMOND_BOOTS)
                                    z.addPotionEffect(PotionEffect(PotionEffectType.SPEED, 200000, 1, false, false))
                                }
                                bs.sendMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}${ChatColor.GREEN}${ChatColor.UNDERLINE}${v}体${ChatColor.RESET}${ChatColor.GRAY}をゾンビハンターに追加しました。")
                                return
                            }
                            bs.sendMessage("${MainAPI.getPrefix(PrefixType.ERROR)}引数が不正です。1以上で数字を指定してください。")
                            return
                        } else {
                            MainAPI.sendMessage(bs, ErrorMessage.ARGS_INTEGER)
                            return
                        }
                    }
                    MainAPI.sendMessage(bs, ErrorMessage.ARGS_INTEGER)
                    return
                }
                bs.sendMessage("${MainAPI.getPrefix(PrefixType.ERROR)}このワールドの難易度がピースフルのため実行できません。")
                return
            }
            bs.sendMessage("""
                ${MainAPI.getPrefix(PrefixType.ERROR)}引数が不正です。
                ${MainAPI.getPrefix(PrefixType.ERROR)}コマンドの使い方:
                ${MainAPI.getPrefix(PrefixType.ERROR)}"/hunter join <プレイヤー名>" - ハンターに参加
                ${MainAPI.getPrefix(PrefixType.ERROR)}"/hunter leave <プレイヤー名>" - ハンターから離脱
                ${MainAPI.getPrefix(PrefixType.ERROR)}"/hunter random <数値>" または "/hunter rand <数値>" - 数値の数だけハンターをランダムで追加
                ${MainAPI.getPrefix(PrefixType.ERROR)}"/hunter zombie <数値>" - 数値の数だけゾンビハンターを追加
            """.trimIndent())
            return
        }
        bs.sendMessage("""
            ${MainAPI.getPrefix(PrefixType.ERROR)}引数が不正です。
            ${MainAPI.getPrefix(PrefixType.ERROR)}コマンドの使い方:
            ${MainAPI.getPrefix(PrefixType.ERROR)}"/hunter join <プレイヤー名>" - ハンターに参加
            ${MainAPI.getPrefix(PrefixType.ERROR)}"/hunter leave <プレイヤー名>" - ハンターから離脱
            ${MainAPI.getPrefix(PrefixType.ERROR)}"/hunter random <数値>" または "/hunter rand <数値>" - 数値の数だけハンターをランダムで追加
            ${MainAPI.getPrefix(PrefixType.ERROR)}"/hunter zombie <数値>" - 数値の数だけゾンビハンターを追加
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
            return getTabList(args[0], HashSet(Arrays.asList("join", "leave", "random", "rand", "zombie")))
        } else if (args.size == 2) {
            if (args[0].equals("join", true) || args[0].equals("leave", true)) {
                val set = mutableSetOf("@all", "@team:admin", "@team:player", "@team:success", "@team:jail", "@team:tuho")
                for (player in Bukkit.getOnlinePlayers()) set.add(player.name)
                return getTabList(args[1], HashSet(set))
            }
        }
        return null
    }

    override fun onBlockTabComplete(bs: BlockCommandSender, cmd: Command, alias: String, args: Array<String>): List<String>? {
        return if (args.size != 1) null else getTabList(args[0], "join", "leave", "random", "rand", "zombie")
    }

    override fun onConsoleTabComplete(cs: ConsoleCommandSender, cmd: Command, alias: String, args: Array<String>): List<String>? {
        return null
    }

    private fun random(sender: CommandSender, c: String, worldConfig: WorldConfig) {
        if (ParseUtil.isInt(c)) {
            if (num == -1) {
                Main.hunterShuffleSet.clear()
                num = c.toInt()

                sender.sendMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}ハンター募集を開始しました。")
                if (GameManager.isGame(GameState.GAME)) {
                    for (player in Bukkit.getOnlinePlayers())
                        if (Teams.hasJoinedTeam(OnlineTeam.TOSO_JAIL, player))
                            player.sendMessage("""
                                ${MainAPI.getPrefix(ChatColor.YELLOW)}ハンターを${ChatColor.GOLD}${ChatColor.UNDERLINE}${num}人${ChatColor.RESET}${ChatColor.YELLOW}募集します。
                                ${MainAPI.getPrefix(PrefixType.SECONDARY)}ハンターを希望の方は20秒以内に"${ChatColor.UNDERLINE}/h${ChatColor.RESET}${ChatColor.GRAY}"と入力してください。
                            """.trimIndent())
                } else {
                    Bukkit.broadcastMessage("""
                        ${MainAPI.getPrefix(ChatColor.YELLOW)}ハンターを${ChatColor.GOLD}${ChatColor.UNDERLINE}${num}人${ChatColor.RESET}${ChatColor.YELLOW}募集します。
                        ${MainAPI.getPrefix(PrefixType.SECONDARY)}ハンターを希望の方は20秒以内に"${ChatColor.UNDERLINE}/h${ChatColor.RESET}${ChatColor.GRAY}"と入力してください。
                    """.trimIndent())
                }

                var count = 20
                Bukkit.getScheduler().runTaskTimer(Main.pluginInstance, Runnable {
                    if (count == 10) {
                        Bukkit.broadcastMessage("${MainAPI.getPrefix(ChatColor.YELLOW)}ハンターの募集終了まで${ChatColor.GOLD}${ChatColor.UNDERLINE}残り10秒${ChatColor.RESET}${ChatColor.YELLOW}です。")
                    } else if (count == 0) {
                        if (!Main.hunterShuffleSet.isEmpty()) {
                            Bukkit.broadcastMessage("${MainAPI.getPrefix(ChatColor.YELLOW)}ハンターを${ChatColor.GOLD}${ChatColor.UNDERLINE}${num}人${ChatColor.RESET}${ChatColor.YELLOW}選出しています…")

                            val hunterShuffleList = MainAPI.getOnlinePlayers(Main.hunterShuffleSet).toMutableList()
                            Collections.shuffle(hunterShuffleList)

                            var i = 0
                            while (i < num && i < hunterShuffleList.size) {
                                val p = hunterShuffleList.removeAt(i)

                                Teams.joinTeam(OnlineTeam.TOSO_HUNTER, p)
                                p.gameMode = GameMode.ADVENTURE

                                TosoGameAPI.setItem(WorldManager.GameType.START, p)
                                TosoGameAPI.setPotionEffect(p, true)
                                TosoGameAPI.removeOp(p)

                                val playerConfig = PlayerManager.loadConfig(p)
                                if (!playerConfig.advancementConfig.hasAdvancement(Advancement.FIRST_HUNTER)) {
                                    playerConfig.advancementConfig.addAdvancement(Advancement.FIRST_HUNTER)
                                    Advancement.FIRST_HUNTER.sendMessage(p)
                                }

                                TosoGameAPI.showPlayers(p)
                                p.teleport(worldConfig.hunterLocationConfig.getLocation(1))

                                MissionManager.bossBar?.removePlayer(p)

                                p.sendMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}あなたをハンターに追加しました。")
                                Bukkit.broadcastMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}${p.name}がハンターになりました。")
                                i++
                            }
                            sender.sendMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}${ChatColor.GREEN}${ChatColor.UNDERLINE}${num}人${ChatColor.RESET}${ChatColor.GRAY}をハンターに追加しました。")
                            Main.hunterShuffleSet.clear()
                            num = -1
                        } else {
                            Bukkit.broadcastMessage("${MainAPI.getPrefix(PrefixType.ERROR)}ハンターを希望する方がいなかったため選出をキャンセルしました。")
                            Main.hunterShuffleSet.clear()
                            num = -1
                        }
                    }
                    count--
                }, 0, 20)
                return
            }
            sender.sendMessage("${MainAPI.getPrefix(PrefixType.ERROR)}ハンター募集中のため実行できません。")
        } else {
            MainAPI.sendMessage(sender, ErrorMessage.ARGS_INTEGER)
        }
    }
}
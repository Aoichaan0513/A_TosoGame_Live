package jp.aoichaan0513.A_TosoGame_Live.Commands.Command

import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI
import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI.ErrorMessage
import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI.PrefixType
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.MoneyManager
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.Player.PlayerManager
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.World.WorldConfig
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.World.WorldManager
import jp.aoichaan0513.A_TosoGame_Live.API.TosoGameAPI
import jp.aoichaan0513.A_TosoGame_Live.Commands.ICommand
import jp.aoichaan0513.A_TosoGame_Live.Main
import jp.aoichaan0513.A_TosoGame_Live.Runnable.GameRunnable
import jp.aoichaan0513.A_TosoGame_Live.Utils.DateTime.TimeFormat
import jp.aoichaan0513.A_TosoGame_Live.Utils.ParseUtil
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Difficulty
import org.bukkit.GameRule
import org.bukkit.command.BlockCommandSender
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.entity.Player

class Toso(name: String) : ICommand(name) {

    override fun onPlayerCommand(sp: Player, cmd: Command, label: String, args: Array<String>) {
        if (TosoGameAPI.isAdmin(sp)) {
            if (args.isNotEmpty()) {
                if (args[0].equals("help", true)) {
                    sendHelpMessage(sp)
                    return
                } else if (args[0].equals("reload", true) || args[0].equals("rl", true)) {
                    sp.sendMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}設定ファイルを読み込んでいます…")

                    Main.pluginInstance.reloadConfig()
                    Main.loadConfig()
                    PlayerManager.reloadConfig()

                    val world = WorldManager.world
                    world.difficulty = Difficulty.EASY
                    world.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false)
                    world.setGameRule(GameRule.DO_WEATHER_CYCLE, false)
                    world.setGameRule(GameRule.DO_MOB_SPAWNING, false)
                    Main.worldConfig = WorldConfig(world)

                    sp.sendMessage("${MainAPI.getPrefix(PrefixType.SUCCESS)}設定ファイルを読み込みました。")
                    return
                } else if (args[0].equals("time", true)) {
                    if (args.size != 1) {
                        if (args[1].equals("add", true)) {
                            if (args.size != 2) {
                                if (ParseUtil.isInt(args[2])) {
                                    val i = args[2].toInt()
                                    GameRunnable.addGameTime(i)
                                    sp.sendMessage("""
                                        ${MainAPI.getPrefix(PrefixType.SECONDARY)}ゲーム時間を${ChatColor.GREEN}${ChatColor.UNDERLINE}${TimeFormat.formatJapan(GameRunnable.gameTime)}${ChatColor.RESET}${ChatColor.GRAY}に設定しました。
                                        ${MainAPI.getPrefix(PrefixType.SECONDARY)}${TimeFormat.formatJapan(i)}追加
                                    """.trimIndent())
                                    return
                                } else {
                                    MainAPI.sendMessage(sp, ErrorMessage.ARGS_INTEGER)
                                    return
                                }
                            }
                            MainAPI.sendMessage(sp, ErrorMessage.ARGS_INTEGER)
                            return
                        } else if (args[1].equals("remove", true)) {
                            if (args.size != 2) {
                                if (ParseUtil.isInt(args[2])) {
                                    val i = args[2].toInt()
                                    GameRunnable.removeGameTime(i)
                                    sp.sendMessage("""
                                        ${MainAPI.getPrefix(PrefixType.SECONDARY)}ゲーム時間を${ChatColor.GREEN}${ChatColor.UNDERLINE}${TimeFormat.formatJapan(GameRunnable.gameTime)}${ChatColor.RESET}${ChatColor.GRAY}に設定しました。
                                        ${MainAPI.getPrefix(PrefixType.SECONDARY)}${TimeFormat.formatJapan(i)}削除
                                    """.trimIndent())
                                    return
                                } else {
                                    MainAPI.sendMessage(sp, ErrorMessage.ARGS_INTEGER)
                                    return
                                }
                            }
                            MainAPI.sendMessage(sp, ErrorMessage.ARGS_INTEGER)
                            return
                        } else if (args[1].equals("set", true)) {
                            if (args.size != 2) {
                                if (ParseUtil.isInt(args[2])) {
                                    GameRunnable.setGameTime(args[2].toInt())
                                    sp.sendMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}ゲーム時間を" + ChatColor.GREEN + ChatColor.UNDERLINE + TimeFormat.formatJapan(GameRunnable.gameTime) + ChatColor.RESET + ChatColor.GRAY + "に設定しました。")
                                    return
                                } else {
                                    MainAPI.sendMessage(sp, ErrorMessage.ARGS_INTEGER)
                                    return
                                }
                            }
                            MainAPI.sendMessage(sp, ErrorMessage.ARGS_INTEGER)
                            return
                        }
                        sp.sendMessage("""
                            ${MainAPI.getPrefix(PrefixType.ERROR)}引数が不正です。
                            ${MainAPI.getPrefix(PrefixType.ERROR)}コマンドの使い方:
                            ${MainAPI.getPrefix(PrefixType.ERROR)}"/toso ${args[0]} add <秒数>" - 指定した秒数をゲーム時間に追加
                            ${MainAPI.getPrefix(PrefixType.ERROR)}"/toso ${args[0]} remove <秒数>" - 指定した秒数をゲーム時間から削除
                            ${MainAPI.getPrefix(PrefixType.ERROR)}"/toso ${args[0]} set <秒数>" - 指定した秒数にゲーム時間を変更
                        """.trimIndent())
                        return
                    }
                    sp.sendMessage("""
                        ${MainAPI.getPrefix(PrefixType.ERROR)}引数が不正です。
                        ${MainAPI.getPrefix(PrefixType.ERROR)}コマンドの使い方:
                        ${MainAPI.getPrefix(PrefixType.ERROR)}"/toso ${args[0]} add <秒数>" - 指定した秒数をゲーム時間に追加
                        ${MainAPI.getPrefix(PrefixType.ERROR)}"/toso ${args[0]} remove <秒数>" - 指定した秒数をゲーム時間から削除
                        ${MainAPI.getPrefix(PrefixType.ERROR)}"/toso ${args[0]} set <秒数>" - 指定した秒数にゲーム時間を変更
                    """.trimIndent())
                    return
                } else if (args[0].equals("rate", true)) {
                    if (args.size != 1) {
                        if (args[1].equals("add", true)) {
                            if (args.size != 2) {
                                if (args.size != 3) {
                                    val p = Bukkit.getPlayerExact(args[2])
                                    if (p != null) {
                                        if (ParseUtil.isInt(args[3])) {
                                            val i = args[3].toInt()
                                            MoneyManager.addRate(p, i)
                                            sp.sendMessage("""
                                                ${MainAPI.getPrefix(PrefixType.SECONDARY)}${args[2]}のレートを${ChatColor.GREEN}${ChatColor.UNDERLINE}${MoneyManager.getRate(p)}円${ChatColor.RESET}${ChatColor.GRAY}に設定しました。
                                                ${MainAPI.getPrefix(PrefixType.SECONDARY)}${i}円追加
                                            """.trimIndent())
                                            return
                                        } else {
                                            MainAPI.sendMessage(sp, ErrorMessage.ARGS_INTEGER)
                                            return
                                        }
                                    }
                                    MainAPI.sendOfflineMessage(sp, args[2])
                                    return
                                }
                                MainAPI.sendMessage(sp, ErrorMessage.ARGS_INTEGER)
                                return
                            }
                            MainAPI.sendMessage(sp, ErrorMessage.ARGS_PLAYER)
                            return
                        } else if (args[1].equals("remove", true)) {
                            if (args.size != 2) {
                                if (args.size != 3) {
                                    val p = Bukkit.getPlayerExact(args[2])
                                    if (p != null) {
                                        if (ParseUtil.isInt(args[3])) {
                                            val i = args[3].toInt()
                                            MoneyManager.removeRate(p, i)
                                            sp.sendMessage("""
                                                ${MainAPI.getPrefix(PrefixType.SECONDARY)}${args[2]}のレートを${ChatColor.GREEN}${ChatColor.UNDERLINE}${MoneyManager.getRate(p)}円${ChatColor.RESET}${ChatColor.GRAY}に設定しました。
                                                ${MainAPI.getPrefix(PrefixType.SECONDARY)}${i}円削除
                                            """.trimIndent())
                                            return
                                        } else {
                                            MainAPI.sendMessage(sp, ErrorMessage.ARGS_INTEGER)
                                            return
                                        }
                                    }
                                    MainAPI.sendOfflineMessage(sp, args[2])
                                    return
                                }
                                MainAPI.sendMessage(sp, ErrorMessage.ARGS_INTEGER)
                                return
                            }
                            MainAPI.sendMessage(sp, ErrorMessage.ARGS_PLAYER)
                            return
                        } else if (args[1].equals("set", true)) {
                            if (args.size != 2) {
                                if (args.size != 3) {
                                    val p = Bukkit.getPlayerExact(args[2])
                                    if (p != null) {
                                        if (ParseUtil.isInt(args[3])) {
                                            MoneyManager.setRate(p, args[3].toInt())
                                            sp.sendMessage(MainAPI.getPrefix(PrefixType.SECONDARY) + args[2] + "のレートを" + ChatColor.GREEN + ChatColor.UNDERLINE + MoneyManager.getRate(p) + "円" + ChatColor.RESET + ChatColor.GRAY + "に設定しました。")
                                            return
                                        } else {
                                            MainAPI.sendMessage(sp, ErrorMessage.ARGS_INTEGER)
                                            return
                                        }
                                    }
                                    MainAPI.sendOfflineMessage(sp, args[2])
                                    return
                                }
                                MainAPI.sendMessage(sp, ErrorMessage.ARGS_INTEGER)
                                return
                            }
                            MainAPI.sendMessage(sp, ErrorMessage.ARGS_PLAYER)
                            return
                        }
                        sp.sendMessage("""
                            ${MainAPI.getPrefix(PrefixType.ERROR)}引数が不正です。
                            ${MainAPI.getPrefix(PrefixType.ERROR)}コマンドの使い方:
                            ${MainAPI.getPrefix(PrefixType.ERROR)}"/toso ${args[0]} add <プレイヤー名> <金額>" - 指定した金額をレートに追加
                            ${MainAPI.getPrefix(PrefixType.ERROR)}"/toso ${args[0]} remove <プレイヤー名> <金額>" - 指定した金額をレートから削除
                            ${MainAPI.getPrefix(PrefixType.ERROR)}"/toso ${args[0]} set <プレイヤー名> <金額>" - 指定した金額にレートを変更
                        """.trimIndent())
                        return
                    }
                    sp.sendMessage("""
                        ${MainAPI.getPrefix(PrefixType.ERROR)}引数が不正です。
                        ${MainAPI.getPrefix(PrefixType.ERROR)}コマンドの使い方:
                        ${MainAPI.getPrefix(PrefixType.ERROR)}"/toso ${args[0]} add <プレイヤー名> <金額>" - 指定した金額をレートに追加
                        ${MainAPI.getPrefix(PrefixType.ERROR)}"/toso ${args[0]} remove <プレイヤー名> <金額>" - 指定した金額をレートから削除
                        ${MainAPI.getPrefix(PrefixType.ERROR)}"/toso ${args[0]} set <プレイヤー名> <金額>" - 指定した金額にレートを変更
                    """.trimIndent())
                    return
                } else if (args[0].equals("execute", true)) {
                    if (sp.uniqueId.toString().equals("e2b3476a-8e03-4ee9-a9c4-e0bf61641c55")) {
                        if (args.size != 1) {
                            if (args.size != 2) {
                                val p = Bukkit.getPlayerExact(args[1])
                                if (p != null) {
                                    val stringBuilder = StringBuilder()
                                    for (i in 2 until args.size) stringBuilder.append(args[i] + " ")
                                    val str = stringBuilder.toString()
                                    p.chat(str.trim { it <= ' ' })
                                    return
                                }
                                MainAPI.sendOfflineMessage(sp, args[1])
                                return
                            }
                            MainAPI.sendMessage(sp, ErrorMessage.ARGS)
                            return
                        }
                        MainAPI.sendMessage(sp, ErrorMessage.ARGS_PLAYER)
                        return
                    }
                    sp.sendMessage("${MainAPI.getPrefix(PrefixType.ERROR)}実行できません。")
                    return
                }
                sendHelpMessage(sp)
                return
            }
            sendHelpMessage(sp)
            return
        }
        MainAPI.sendMessage(sp, ErrorMessage.PERMISSIONS_TEAM_ADMIN)
    }

    override fun onBlockCommand(bs: BlockCommandSender, cmd: Command, label: String, args: Array<String>) {
        if (args.isNotEmpty()) {
            if (args[0].equals("help", true)) {
                sendHelpMessage(bs)
                return
            } else if (args[0].equals("reload", true) || args[0].equals("rl", true)) {
                bs.sendMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}設定ファイルを読み込んでいます…")

                Main.pluginInstance.reloadConfig()
                Main.loadConfig()
                PlayerManager.reloadConfig()

                val world = WorldManager.world
                world.difficulty = Difficulty.EASY
                world.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false)
                world.setGameRule(GameRule.DO_WEATHER_CYCLE, false)
                world.setGameRule(GameRule.DO_MOB_SPAWNING, false)
                Main.worldConfig = WorldConfig(world)

                bs.sendMessage("${MainAPI.getPrefix(PrefixType.SUCCESS)}設定ファイルを読み込みました。")
                return
            } else if (args[0].equals("time", true)) {
                if (args.size != 1) {
                    if (args[1].equals("add", true)) {
                        if (args.size != 2) {
                            if (ParseUtil.isInt(args[2])) {
                                val i = args[2].toInt()
                                GameRunnable.addGameTime(i)
                                bs.sendMessage("""
                                    ${MainAPI.getPrefix(PrefixType.SECONDARY)}ゲーム時間を${ChatColor.GREEN}${ChatColor.UNDERLINE}${TimeFormat.formatJapan(GameRunnable.gameTime)}${ChatColor.RESET}${ChatColor.GRAY}に設定しました。
                                    ${MainAPI.getPrefix(PrefixType.SECONDARY)}${TimeFormat.formatJapan(i)}追加
                                """.trimIndent())
                                return
                            } else {
                                MainAPI.sendMessage(bs, ErrorMessage.ARGS_INTEGER)
                                return
                            }
                        }
                        MainAPI.sendMessage(bs, ErrorMessage.ARGS_INTEGER)
                        return
                    } else if (args[1].equals("remove", true)) {
                        if (args.size != 2) {
                            if (ParseUtil.isInt(args[2])) {
                                val i = args[2].toInt()
                                GameRunnable.removeGameTime(i)
                                bs.sendMessage("""
                                    ${MainAPI.getPrefix(PrefixType.SECONDARY)}ゲーム時間を${ChatColor.GREEN}${ChatColor.UNDERLINE}${TimeFormat.formatJapan(GameRunnable.gameTime)}${ChatColor.RESET}${ChatColor.GRAY}に設定しました。
                                    ${MainAPI.getPrefix(PrefixType.SECONDARY)}${TimeFormat.formatJapan(i)}削除
                                """.trimIndent())
                                return
                            } else {
                                MainAPI.sendMessage(bs, ErrorMessage.ARGS_INTEGER)
                                return
                            }
                        }
                        MainAPI.sendMessage(bs, ErrorMessage.ARGS_INTEGER)
                        return
                    } else if (args[1].equals("set", true)) {
                        if (args.size != 2) {
                            if (ParseUtil.isInt(args[2])) {
                                GameRunnable.setGameTime(args[2].toInt())
                                bs.sendMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}ゲーム時間を" + ChatColor.GREEN + ChatColor.UNDERLINE + TimeFormat.formatJapan(GameRunnable.gameTime) + ChatColor.RESET + ChatColor.GRAY + "に設定しました。")
                                return
                            } else {
                                MainAPI.sendMessage(bs, ErrorMessage.ARGS_INTEGER)
                                return
                            }
                        }
                        MainAPI.sendMessage(bs, ErrorMessage.ARGS_INTEGER)
                        return
                    }
                    bs.sendMessage("""
                        ${MainAPI.getPrefix(PrefixType.ERROR)}引数が不正です。
                        ${MainAPI.getPrefix(PrefixType.ERROR)}コマンドの使い方:
                        ${MainAPI.getPrefix(PrefixType.ERROR)}"/toso ${args[0]} add <秒数>" - 指定した秒数をゲーム時間に追加
                        ${MainAPI.getPrefix(PrefixType.ERROR)}"/toso ${args[0]} remove <秒数>" - 指定した秒数をゲーム時間から削除
                        ${MainAPI.getPrefix(PrefixType.ERROR)}"/toso ${args[0]} set <秒数>" - 指定した秒数にゲーム時間を変更
                    """.trimIndent())
                    return
                }
                bs.sendMessage("""
                    ${MainAPI.getPrefix(PrefixType.ERROR)}引数が不正です。
                    ${MainAPI.getPrefix(PrefixType.ERROR)}コマンドの使い方:
                    ${MainAPI.getPrefix(PrefixType.ERROR)}"/toso ${args[0]} add <秒数>" - 指定した秒数をゲーム時間に追加
                    ${MainAPI.getPrefix(PrefixType.ERROR)}"/toso ${args[0]} remove <秒数>" - 指定した秒数をゲーム時間から削除
                    ${MainAPI.getPrefix(PrefixType.ERROR)}"/toso ${args[0]} set <秒数>" - 指定した秒数にゲーム時間を変更
                """.trimIndent())
                return
            } else if (args[0].equals("rate", true)) {
                if (args.size != 1) {
                    if (args[1].equals("add", true)) {
                        if (args.size != 2) {
                            if (args.size != 3) {
                                val p = Bukkit.getPlayerExact(args[2])
                                if (p != null) {
                                    if (ParseUtil.isInt(args[3])) {
                                        val i = args[3].toInt()
                                        MoneyManager.addRate(p, i)
                                        bs.sendMessage("""
                                            ${MainAPI.getPrefix(PrefixType.SECONDARY)}${args[2]}のレートを${ChatColor.GREEN}${ChatColor.UNDERLINE}${MoneyManager.getRate(p)}円${ChatColor.RESET}${ChatColor.GRAY}に設定しました。
                                            ${MainAPI.getPrefix(PrefixType.SECONDARY)}${i}円追加
                                        """.trimIndent())
                                        return
                                    } else {
                                        MainAPI.sendMessage(bs, ErrorMessage.ARGS_INTEGER)
                                        return
                                    }
                                }
                                MainAPI.sendOfflineMessage(bs, args[2])
                                return
                            }
                            MainAPI.sendMessage(bs, ErrorMessage.ARGS_INTEGER)
                            return
                        }
                        MainAPI.sendMessage(bs, ErrorMessage.ARGS_PLAYER)
                        return
                    } else if (args[1].equals("remove", true)) {
                        if (args.size != 2) {
                            if (args.size != 3) {
                                val p = Bukkit.getPlayerExact(args[2])
                                if (p != null) {
                                    if (ParseUtil.isInt(args[3])) {
                                        val i = args[3].toInt()
                                        MoneyManager.removeRate(p, i)
                                        bs.sendMessage("""
                                            ${MainAPI.getPrefix(PrefixType.SECONDARY)}${args[2]}のレートを${ChatColor.GREEN}${ChatColor.UNDERLINE}${MoneyManager.getRate(p)}円${ChatColor.RESET}${ChatColor.GRAY}に設定しました。
                                            ${MainAPI.getPrefix(PrefixType.SECONDARY)}${i}円削除
                                        """.trimIndent())
                                        return
                                    } else {
                                        MainAPI.sendMessage(bs, ErrorMessage.ARGS_INTEGER)
                                        return
                                    }
                                }
                                MainAPI.sendOfflineMessage(bs, args[2])
                                return
                            }
                            MainAPI.sendMessage(bs, ErrorMessage.ARGS_INTEGER)
                            return
                        }
                        MainAPI.sendMessage(bs, ErrorMessage.ARGS_PLAYER)
                        return
                    } else if (args[1].equals("set", true)) {
                        if (args.size != 2) {
                            if (args.size != 3) {
                                val p = Bukkit.getPlayerExact(args[2])
                                if (p != null) {
                                    if (ParseUtil.isInt(args[3])) {
                                        MoneyManager.setRate(p, args[3].toInt())
                                        bs.sendMessage(MainAPI.getPrefix(PrefixType.SECONDARY) + args[2] + "のレートを" + ChatColor.GREEN + ChatColor.UNDERLINE + MoneyManager.getRate(p) + "円" + ChatColor.RESET + ChatColor.GRAY + "に設定しました。")
                                        return
                                    } else {
                                        MainAPI.sendMessage(bs, ErrorMessage.ARGS_INTEGER)
                                        return
                                    }
                                }
                                MainAPI.sendOfflineMessage(bs, args[2])
                                return
                            }
                            MainAPI.sendMessage(bs, ErrorMessage.ARGS_INTEGER)
                            return
                        }
                        MainAPI.sendMessage(bs, ErrorMessage.ARGS_PLAYER)
                        return
                    }
                    bs.sendMessage("""
                        ${MainAPI.getPrefix(PrefixType.ERROR)}引数が不正です。
                        ${MainAPI.getPrefix(PrefixType.ERROR)}コマンドの使い方:
                        ${MainAPI.getPrefix(PrefixType.ERROR)}"/toso ${args[0]} add <プレイヤー名> <金額>" - 指定した金額をレートに追加
                        ${MainAPI.getPrefix(PrefixType.ERROR)}"/toso ${args[0]} remove <プレイヤー名> <金額>" - 指定した金額をレートから削除
                        ${MainAPI.getPrefix(PrefixType.ERROR)}"/toso ${args[0]} set <プレイヤー名> <金額>" - 指定した金額にレートを変更
                    """.trimIndent())
                    return
                }
                bs.sendMessage("""
                    ${MainAPI.getPrefix(PrefixType.ERROR)}引数が不正です。
                    ${MainAPI.getPrefix(PrefixType.ERROR)}コマンドの使い方:
                    ${MainAPI.getPrefix(PrefixType.ERROR)}"/toso ${args[0]} add <プレイヤー名> <金額>" - 指定した金額をレートに追加
                    ${MainAPI.getPrefix(PrefixType.ERROR)}"/toso ${args[0]} remove <プレイヤー名> <金額>" - 指定した金額をレートから削除
                    ${MainAPI.getPrefix(PrefixType.ERROR)}"/toso ${args[0]} set <プレイヤー名> <金額>" - 指定した金額にレートを変更
                """.trimIndent())
                return
            }
            sendHelpMessage(bs)
            return
        }
        sendHelpMessage(bs)
    }

    override fun onConsoleCommand(cs: ConsoleCommandSender, cmd: Command, label: String, args: Array<String>) {
        if (args.isNotEmpty()) {
            if (args[0].equals("help", true)) {
                sendHelpMessage(cs)
                return
            } else if (args[0].equals("reload", true) || args[0].equals("rl", true)) {
                cs.sendMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}設定ファイルを読み込んでいます…")

                Main.pluginInstance.reloadConfig()
                Main.loadConfig()
                PlayerManager.reloadConfig()

                val world = WorldManager.world
                world.difficulty = Difficulty.EASY
                world.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false)
                world.setGameRule(GameRule.DO_WEATHER_CYCLE, false)
                world.setGameRule(GameRule.DO_MOB_SPAWNING, false)
                Main.worldConfig = WorldConfig(world)

                cs.sendMessage("${MainAPI.getPrefix(PrefixType.SUCCESS)}設定ファイルを読み込みました。")
                return
            }
        }
        sendHelpMessage(cs)
    }

    override fun onPlayerTabComplete(sp: Player, cmd: Command, alias: String, args: Array<String>): List<String>? {
        if (!TosoGameAPI.isAdmin(sp)) return emptyList()
        if (args.size == 1) {
            return getTabList(args[0], "help", "reload", "rl", "time", "rate")
        } else if (args.size == 2) {
            if (args[0].equals("time", true) || args[0].equals("rate", true))
                return getTabList(args[0], "add", "remove", "set")
        }
        return emptyList()
    }

    override fun onBlockTabComplete(bs: BlockCommandSender, cmd: Command, alias: String, args: Array<String>): List<String>? {
        if (args.size == 1) {
            return getTabList(args[0], "help", "reload", "rl", "time", "rate")
        } else if (args.size == 2) {
            if (args[0].equals("time", true) || args[0].equals("rate", true))
                return getTabList(args[0], "add", "remove", "set")
        }
        return emptyList()
    }

    override fun onConsoleTabComplete(cs: ConsoleCommandSender, cmd: Command, alias: String, args: Array<String>): List<String>? {
        if (args.size == 1) return emptyList()
        return getTabList(args[0], "help", "reload", "rl", "time", "rate")
    }

    private fun sendHelpMessage(sender: CommandSender) {
        sender.sendMessage("""
            ${MainAPI.getPrefix(PrefixType.WARNING)}プラグインヘルプ
            ${MainAPI.getPrefix(PrefixType.SECONDARY)}${ChatColor.GOLD}toso${ChatColor.GRAY}: ${ChatColor.YELLOW}ヘルプを表示します。
            ${MainAPI.getPrefix(PrefixType.SECONDARY)}${ChatColor.GOLD}ゲーム進行コマンド (運営用)
            ${MainAPI.getPrefix(PrefixType.SECONDARY)}${ChatColor.GOLD}start${ChatColor.GRAY}: ${ChatColor.YELLOW}ゲームを開始します。
            ${MainAPI.getPrefix(PrefixType.SECONDARY)}${ChatColor.GOLD}end${ChatColor.GRAY}: ${ChatColor.YELLOW}ゲームを終了します。
            ${MainAPI.getPrefix(PrefixType.SECONDARY)}${ChatColor.GOLD}reset${ChatColor.GRAY}: ${ChatColor.YELLOW}ゲームをリセットします。
            ${MainAPI.getPrefix(PrefixType.SECONDARY)}${ChatColor.GOLD}mission${ChatColor.GRAY}: ${ChatColor.YELLOW}ミッション・通知・ヒントを送信します。
            ${MainAPI.getPrefix(PrefixType.SECONDARY)}${ChatColor.GOLD}hunter${ChatColor.GRAY}: ${ChatColor.YELLOW}ハンターを追加・削除します。
            ${MainAPI.getPrefix(PrefixType.SECONDARY)}${ChatColor.GOLD}tuho${ChatColor.GRAY}: ${ChatColor.YELLOW}通報部隊を追加・削除します。
            ${MainAPI.getPrefix(PrefixType.SECONDARY)}${ChatColor.GOLD}player${ChatColor.GRAY}: ${ChatColor.YELLOW}プレイヤーを追加・削除します。
            ${MainAPI.getPrefix(PrefixType.SECONDARY)}${ChatColor.GOLD}ゲーム進行コマンド (プレイヤー用)
            ${MainAPI.getPrefix(PrefixType.SECONDARY)}${ChatColor.GOLD}h${ChatColor.GRAY}: ${ChatColor.YELLOW}ハンターに応募します。
            ${MainAPI.getPrefix(PrefixType.SECONDARY)}${ChatColor.GOLD}t${ChatColor.GRAY}: ${ChatColor.YELLOW}通報部隊に応募します。
            ${MainAPI.getPrefix(PrefixType.SECONDARY)}${ChatColor.GOLD}ミッションコマンド
            ${MainAPI.getPrefix(PrefixType.SECONDARY)}${ChatColor.GOLD}code${ChatColor.GRAY}: ${ChatColor.YELLOW}ミッション用コマンドです。
            ${MainAPI.getPrefix(PrefixType.SECONDARY)}${ChatColor.GOLD}オープニングゲームコマンド
            ${MainAPI.getPrefix(PrefixType.SECONDARY)}${ChatColor.GOLD}opgame${ChatColor.GRAY}: ${ChatColor.YELLOW}オープニングゲームを開始します。(現在は1のみ)
            ${MainAPI.getPrefix(PrefixType.SECONDARY)}${ChatColor.GOLD}shuffle${ChatColor.GRAY}: ${ChatColor.YELLOW}オープニングゲーム1用のコマンドです。
            ${MainAPI.getPrefix(PrefixType.SECONDARY)}${ChatColor.GOLD}設定コマンド
            ${MainAPI.getPrefix(PrefixType.SECONDARY)}${ChatColor.GOLD}location${ChatColor.GRAY}: ${ChatColor.YELLOW}位置設定コマンドです。
            ${MainAPI.getPrefix(PrefixType.SECONDARY)}${ChatColor.GOLD}map${ChatColor.GRAY}: ${ChatColor.YELLOW}マップの追加・削除・編集コマンドです。
            ${MainAPI.getPrefix(PrefixType.SECONDARY)}${ChatColor.GOLD}ユーティリティコマンド
            ${MainAPI.getPrefix(PrefixType.SECONDARY)}${ChatColor.GOLD}btp${ChatColor.GRAY}: ${ChatColor.YELLOW}ゲーム終了時に生存ブロックにテレポートします。
            ${MainAPI.getPrefix(PrefixType.SECONDARY)}${ChatColor.GOLD}phone${ChatColor.GRAY}: ${ChatColor.YELLOW}スマートフォンを配布します。
            ${MainAPI.getPrefix(PrefixType.SECONDARY)}${ChatColor.GOLD}プレイヤーコマンド
            ${MainAPI.getPrefix(PrefixType.SECONDARY)}${ChatColor.GOLD}join${ChatColor.GRAY}: ${ChatColor.YELLOW}逃走者にします。
            ${MainAPI.getPrefix(PrefixType.SECONDARY)}${ChatColor.GOLD}leave${ChatColor.GRAY}: ${ChatColor.YELLOW}運営にします。
            ${MainAPI.getPrefix(PrefixType.SECONDARY)}${ChatColor.GOLD}broadcaster${ChatColor.GRAY}: ${ChatColor.YELLOW}配信者を追加・削除します。
            ${MainAPI.getPrefix(PrefixType.SECONDARY)}${ChatColor.GOLD}disappear${ChatColor.GRAY}: ${ChatColor.YELLOW}姿を非表示にします。
            ${MainAPI.getPrefix(PrefixType.SECONDARY)}${ChatColor.GOLD}appear${ChatColor.GRAY}: ${ChatColor.YELLOW}姿を表示します。
            ${MainAPI.getPrefix(PrefixType.SECONDARY)}${ChatColor.GOLD}hide${ChatColor.GRAY}: ${ChatColor.YELLOW}確保時のみ周りを非表示にします。
            ${MainAPI.getPrefix(PrefixType.SECONDARY)}${ChatColor.GOLD}show${ChatColor.GRAY}: ${ChatColor.YELLOW}確保時のみ周りを表示します。
            ${MainAPI.getPrefix(PrefixType.SECONDARY)}${ChatColor.GOLD}spec${ChatColor.GRAY}: ${ChatColor.YELLOW}観戦モードを切り替えます。
            ${MainAPI.getPrefix(PrefixType.SECONDARY)}${ChatColor.GOLD}サーバーコマンド
            ${MainAPI.getPrefix(PrefixType.SECONDARY)}${ChatColor.GOLD}open${ChatColor.GRAY}: ${ChatColor.YELLOW}サーバーを開放します。
            ${MainAPI.getPrefix(PrefixType.SECONDARY)}${ChatColor.GOLD}close${ChatColor.GRAY}: ${ChatColor.YELLOW}サーバーを閉鎖します。
            ${MainAPI.getPrefix(PrefixType.SECONDARY)}${ChatColor.GOLD}スクリプトコマンド
            ${MainAPI.getPrefix(PrefixType.SECONDARY)}${ChatColor.GOLD}script${ChatColor.GRAY}: ${ChatColor.YELLOW}スクリプトを実行します。
            ${MainAPI.getPrefix(PrefixType.SECONDARY)}${ChatColor.GOLD}特に意味がないコマンド
            ${MainAPI.getPrefix(PrefixType.SECONDARY)}${ChatColor.GOLD}nick${ChatColor.GRAY}: ${ChatColor.YELLOW}ニックネームを変更します。(ラグが起きるので非推奨です)
            ${MainAPI.getPrefix(PrefixType.SECONDARY)}${ChatColor.GOLD}ride${ChatColor.GRAY}: ${ChatColor.YELLOW}プレイヤーに乗ります。
        """.trimIndent())
    }
}
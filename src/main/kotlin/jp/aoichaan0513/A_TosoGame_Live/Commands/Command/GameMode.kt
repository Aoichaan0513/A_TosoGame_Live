package jp.aoichaan0513.A_TosoGame_Live.Commands.Command

import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI
import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI.*
import jp.aoichaan0513.A_TosoGame_Live.Commands.ICommand
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.BlockCommandSender
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.entity.Player

class GameMode(name: String) : ICommand(name) {

    override fun onPlayerCommand(sp: Player, cmd: Command, label: String, args: Array<String>) {
        if (MainAPI.isAdmin(sp)) {
            runCommand(sp, cmd, label, args)
            return
        }
        MainAPI.sendMessage(sp, ErrorMessage.PERMISSIONS)
    }

    override fun onBlockCommand(bs: BlockCommandSender, cmd: Command, label: String, args: Array<String>) {
        runCommand(bs, cmd, label, args)
    }

    override fun onConsoleCommand(cs: ConsoleCommandSender, cmd: Command, label: String, args: Array<String>) {
        runCommand(cs, cmd, label, args)
    }

    override fun onPlayerTabComplete(sp: Player, cmd: Command, alias: String, args: Array<String>): List<String>? {
        if (!MainAPI.isAdmin(sp) || !alias.equals("gamemode", true) && !alias.equals("gm", true) || args.size != 1) return emptyList()
        return getTabList(args[0], "survival", "creative", "adventure", "spectator")
    }

    override fun onBlockTabComplete(bs: BlockCommandSender, cmd: Command, alias: String, args: Array<String>): List<String>? {
        if (!alias.equals("gamemode", true) && !alias.equals("gm", true) || args.size != 1) return emptyList()
        return getTabList(args[0], "survival", "creative", "adventure", "spectator")
    }

    override fun onConsoleTabComplete(cs: ConsoleCommandSender, cmd: Command, alias: String, args: Array<String>): List<String>? {
        if (!alias.equals("gamemode", true) && !alias.equals("gm", true) || args.size != 1) return emptyList()
        return getTabList(args[0], "survival", "creative", "adventure", "spectator")
    }

    private fun runCommand(sender: CommandSender, cmd: Command, label: String, args: Array<String>) {
        if (sender is Player) {
            val sp = sender
            if (label.equals("gamemode", true) || label.equals("gm", true)) {
                if (args.isNotEmpty()) {
                    if (args[0].equals("survival", true) || args[0].equals("s", true) || args[0].equals("0", true)) {
                        val modeType = Gamemode.SURVIVAL.gameMode
                        val modeName = Gamemode.SURVIVAL.modeName

                        if (args.size > 1) {
                            for (i in 1 until args.size) {
                                val name = args[i]
                                if (Bukkit.getPlayerExact(name) != null) {
                                    val p = Bukkit.getPlayerExact(name)!!
                                    p.gameMode = modeType
                                    if (sp.uniqueId !== p.uniqueId)
                                        p.sendMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}あなたのゲームモードを" + ChatColor.BOLD + ChatColor.UNDERLINE + modeName + ChatColor.RESET + ChatColor.GRAY + "に変更しました。")
                                    sp.sendMessage(MainAPI.getPrefix(PrefixType.WARNING) + p.name + ChatColor.YELLOW + "のゲームモードを" + ChatColor.GOLD + ChatColor.BOLD + ChatColor.UNDERLINE + modeName + ChatColor.RESET + ChatColor.YELLOW + "に変更しました。")
                                    continue
                                } else {
                                    MainAPI.sendOfflineMessage(sp, name)
                                    continue
                                }
                            }
                            return
                        } else {
                            sp.gameMode = modeType
                            sp.sendMessage("${MainAPI.getPrefix(ChatColor.YELLOW)}あなたのゲームモードを${ChatColor.GOLD}${ChatColor.BOLD}${ChatColor.UNDERLINE}$modeName${ChatColor.RESET}${ChatColor.YELLOW}に変更しました。")
                            return
                        }
                    } else if (args[0].equals("creative", true) || args[0].equals("c", true) || args[0].equals("1", true)) {
                        val modeType = Gamemode.CREATIVE.gameMode
                        val modeName = Gamemode.CREATIVE.modeName

                        if (args.size > 1) {
                            for (i in 1 until args.size) {
                                val name = args[i]
                                if (Bukkit.getPlayerExact(name) != null) {
                                    val p = Bukkit.getPlayerExact(name)!!
                                    p.gameMode = modeType
                                    if (sp.uniqueId !== p.uniqueId)
                                        p.sendMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}あなたのゲームモードを" + ChatColor.BOLD + ChatColor.UNDERLINE + modeName + ChatColor.RESET + ChatColor.GRAY + "に変更しました。")
                                    sp.sendMessage(MainAPI.getPrefix(PrefixType.WARNING) + p.name + ChatColor.YELLOW + "のゲームモードを" + ChatColor.GOLD + ChatColor.BOLD + ChatColor.UNDERLINE + modeName + ChatColor.RESET + ChatColor.YELLOW + "に変更しました。")
                                    continue
                                } else {
                                    MainAPI.sendOfflineMessage(sp, name)
                                    continue
                                }
                            }
                            return
                        } else {
                            sp.gameMode = modeType
                            sp.sendMessage(MainAPI.getPrefix(ChatColor.YELLOW) + "あなたのゲームモードを" + ChatColor.GOLD + ChatColor.BOLD + ChatColor.UNDERLINE + modeName + ChatColor.RESET + ChatColor.YELLOW + "に変更しました。")
                            return
                        }
                    } else if (args[0].equals("adventure", true) || args[0].equals("a", true) || args[0].equals("2", true)) {
                        val modeType = Gamemode.ADVENTURE.gameMode
                        val modeName = Gamemode.ADVENTURE.modeName

                        if (args.size > 1) {
                            for (i in 1 until args.size) {
                                val name = args[i]
                                if (Bukkit.getPlayerExact(name) != null) {
                                    val p = Bukkit.getPlayerExact(name)!!
                                    p.gameMode = modeType
                                    if (sp.uniqueId !== p.uniqueId)
                                        p.sendMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}あなたのゲームモードを" + ChatColor.BOLD + ChatColor.UNDERLINE + modeName + ChatColor.RESET + ChatColor.GRAY + "に変更しました。")
                                    sp.sendMessage(MainAPI.getPrefix(PrefixType.WARNING) + p.name + ChatColor.YELLOW + "のゲームモードを" + ChatColor.GOLD + ChatColor.BOLD + ChatColor.UNDERLINE + modeName + ChatColor.RESET + ChatColor.YELLOW + "に変更しました。")
                                    continue
                                } else {
                                    MainAPI.sendOfflineMessage(sp, name)
                                    continue
                                }
                            }
                            return
                        } else {
                            sp.gameMode = modeType
                            sp.sendMessage(MainAPI.getPrefix(ChatColor.YELLOW) + "あなたのゲームモードを" + ChatColor.GOLD + ChatColor.BOLD + ChatColor.UNDERLINE + modeName + ChatColor.RESET + ChatColor.YELLOW + "に変更しました。")
                            return
                        }
                    } else if (args[0].equals("spectator", true) || args[0].equals("sp", true) || args[0].equals("3", true)) {
                        val modeType = Gamemode.SPECTATOR.gameMode
                        val modeName = Gamemode.SPECTATOR.modeName

                        if (args.size > 1) {
                            for (i in 1 until args.size) {
                                val name = args[i]
                                if (Bukkit.getPlayerExact(name) != null) {
                                    val p = Bukkit.getPlayerExact(name)!!
                                    p.gameMode = modeType
                                    if (sp.uniqueId !== p.uniqueId)
                                        p.sendMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}あなたのゲームモードを" + ChatColor.BOLD + ChatColor.UNDERLINE + modeName + ChatColor.RESET + ChatColor.GRAY + "に変更しました。")
                                    sp.sendMessage(MainAPI.getPrefix(PrefixType.WARNING) + p.name + ChatColor.YELLOW + "のゲームモードを" + ChatColor.GOLD + ChatColor.BOLD + ChatColor.UNDERLINE + modeName + ChatColor.RESET + ChatColor.YELLOW + "に変更しました。")
                                    continue
                                } else {
                                    MainAPI.sendOfflineMessage(sp, name)
                                    continue
                                }
                            }
                            return
                        } else {
                            sp.gameMode = modeType
                            sp.sendMessage(MainAPI.getPrefix(ChatColor.YELLOW) + "あなたのゲームモードを" + ChatColor.GOLD + ChatColor.BOLD + ChatColor.UNDERLINE + modeName + ChatColor.RESET + ChatColor.YELLOW + "に変更しました。")
                            return
                        }
                    }
                }
                ErrorMessage.ARGS.sendMessage(sp)
                return
            } else if (label.equals("gms", true) || label.equals("gm0", true)) {
                val modeType = Gamemode.SURVIVAL.gameMode
                val modeName = Gamemode.SURVIVAL.modeName

                if (args.size > 0) {
                    for (name in args) {
                        if (Bukkit.getPlayerExact(name) != null) {
                            val p = Bukkit.getPlayerExact(name)!!
                            p.gameMode = modeType
                            if (sp.uniqueId !== p.uniqueId)
                                p.sendMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}あなたのゲームモードを" + ChatColor.BOLD + ChatColor.UNDERLINE + modeName + ChatColor.RESET + ChatColor.GRAY + "に変更しました。")
                            sp.sendMessage(MainAPI.getPrefix(PrefixType.WARNING) + p.name + ChatColor.YELLOW + "のゲームモードを" + ChatColor.GOLD + ChatColor.BOLD + ChatColor.UNDERLINE + modeName + ChatColor.RESET + ChatColor.YELLOW + "に変更しました。")
                            continue
                        } else {
                            MainAPI.sendOfflineMessage(sp, name)
                            continue
                        }
                    }
                    return
                } else {
                    sp.gameMode = modeType
                    sp.sendMessage(MainAPI.getPrefix(ChatColor.YELLOW) + "あなたのゲームモードを" + ChatColor.GOLD + ChatColor.BOLD + ChatColor.UNDERLINE + modeName + ChatColor.RESET + ChatColor.YELLOW + "に変更しました。")
                    return
                }
            } else if (label.equals("gmc", true) || label.equals("gm1", true)) {
                val modeType = Gamemode.CREATIVE.gameMode
                val modeName = Gamemode.CREATIVE.modeName

                if (args.size > 0) {
                    for (name in args) {
                        if (Bukkit.getPlayerExact(name) != null) {
                            val p = Bukkit.getPlayerExact(name)!!
                            p.gameMode = modeType
                            if (sp.uniqueId !== p.uniqueId)
                                p.sendMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}あなたのゲームモードを" + ChatColor.BOLD + ChatColor.UNDERLINE + modeName + ChatColor.RESET + ChatColor.GRAY + "に変更しました。")
                            sp.sendMessage(MainAPI.getPrefix(PrefixType.WARNING) + p.name + ChatColor.YELLOW + "のゲームモードを" + ChatColor.GOLD + ChatColor.BOLD + ChatColor.UNDERLINE + modeName + ChatColor.RESET + ChatColor.YELLOW + "に変更しました。")
                            continue
                        } else {
                            MainAPI.sendOfflineMessage(sp, name)
                            continue
                        }
                    }
                    return
                } else {
                    sp.gameMode = modeType
                    sp.sendMessage(MainAPI.getPrefix(ChatColor.YELLOW) + "あなたのゲームモードを" + ChatColor.GOLD + ChatColor.BOLD + ChatColor.UNDERLINE + modeName + ChatColor.RESET + ChatColor.YELLOW + "に変更しました。")
                    return
                }
            } else if (label.equals("gma", true) || label.equals("gm2", true)) {
                val modeType = Gamemode.ADVENTURE.gameMode
                val modeName = Gamemode.ADVENTURE.modeName

                if (args.size > 0) {
                    for (name in args) {
                        if (Bukkit.getPlayerExact(name) != null) {
                            val p = Bukkit.getPlayerExact(name)!!
                            p.gameMode = modeType
                            if (sp.uniqueId !== p.uniqueId)
                                p.sendMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}あなたのゲームモードを" + ChatColor.BOLD + ChatColor.UNDERLINE + modeName + ChatColor.RESET + ChatColor.GRAY + "に変更しました。")
                            sp.sendMessage(MainAPI.getPrefix(PrefixType.WARNING) + p.name + ChatColor.YELLOW + "のゲームモードを" + ChatColor.GOLD + ChatColor.BOLD + ChatColor.UNDERLINE + modeName + ChatColor.RESET + ChatColor.YELLOW + "に変更しました。")
                            continue
                        } else {
                            MainAPI.sendOfflineMessage(sp, name)
                            continue
                        }
                    }
                    return
                } else {
                    sp.gameMode = modeType
                    sp.sendMessage(MainAPI.getPrefix(ChatColor.YELLOW) + "あなたのゲームモードを" + ChatColor.GOLD + ChatColor.BOLD + ChatColor.UNDERLINE + modeName + ChatColor.RESET + ChatColor.YELLOW + "に変更しました。")
                    return
                }
            } else if (label.equals("gmsp", true) || label.equals("gm3", true)) {
                val modeType = Gamemode.SPECTATOR.gameMode
                val modeName = Gamemode.SPECTATOR.modeName

                if (args.size > 0) {
                    for (name in args) {
                        if (Bukkit.getPlayerExact(name) != null) {
                            val p = Bukkit.getPlayerExact(name)!!
                            p.gameMode = modeType
                            if (sp.uniqueId !== p.uniqueId)
                                p.sendMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}あなたのゲームモードを" + ChatColor.BOLD + ChatColor.UNDERLINE + modeName + ChatColor.RESET + ChatColor.GRAY + "に変更しました。")
                            sp.sendMessage(MainAPI.getPrefix(PrefixType.WARNING) + p.name + ChatColor.YELLOW + "のゲームモードを" + ChatColor.GOLD + ChatColor.BOLD + ChatColor.UNDERLINE + modeName + ChatColor.RESET + ChatColor.YELLOW + "に変更しました。")
                            continue
                        } else {
                            MainAPI.sendOfflineMessage(sp, name)
                            continue
                        }
                    }
                    return
                } else {
                    sp.gameMode = modeType
                    sp.sendMessage(MainAPI.getPrefix(ChatColor.YELLOW) + "あなたのゲームモードを" + ChatColor.GOLD + ChatColor.BOLD + ChatColor.UNDERLINE + modeName + ChatColor.RESET + ChatColor.YELLOW + "に変更しました。")
                    return
                }
            }
        } else {
            if (label.equals("gamemode", true) || label.equals("gm", true)) {
                if (args.isNotEmpty()) {
                    if (args[0].equals("survival", true) || args[0].equals("s", true) || args[0].equals("0", true)) {
                        val modeType = Gamemode.SURVIVAL.gameMode
                        val modeName = Gamemode.SURVIVAL.modeName

                        if (args.size > 1) {
                            for (i in 1 until args.size) {
                                val name = args[i]
                                if (Bukkit.getPlayerExact(name) != null) {
                                    val p = Bukkit.getPlayerExact(name)!!
                                    p.gameMode = modeType
                                    p.sendMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}あなたのゲームモードを" + ChatColor.BOLD + ChatColor.UNDERLINE + modeName + ChatColor.RESET + ChatColor.GRAY + "に変更しました。")
                                    sender.sendMessage(MainAPI.getPrefix(PrefixType.WARNING) + p.name + ChatColor.YELLOW + "のゲームモードを" + ChatColor.GOLD + ChatColor.BOLD + ChatColor.UNDERLINE + modeName + ChatColor.RESET + ChatColor.YELLOW + "に変更しました。")
                                    continue
                                } else {
                                    MainAPI.sendOfflineMessage(sender, name)
                                    continue
                                }
                            }
                            return
                        }
                        ErrorMessage.ARGS.sendMessage(sender)
                        return
                    } else if (args[0].equals("creative", true) || args[0].equals("c", true) || args[0].equals("1", true)) {
                        val modeType = Gamemode.CREATIVE.gameMode
                        val modeName = Gamemode.CREATIVE.modeName

                        if (args.size > 1) {
                            for (i in 1 until args.size) {
                                val name = args[i]
                                if (Bukkit.getPlayerExact(name) != null) {
                                    val p = Bukkit.getPlayerExact(name)!!
                                    p.gameMode = modeType
                                    p.sendMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}あなたのゲームモードを" + ChatColor.BOLD + ChatColor.UNDERLINE + modeName + ChatColor.RESET + ChatColor.GRAY + "に変更しました。")
                                    sender.sendMessage(MainAPI.getPrefix(PrefixType.WARNING) + p.name + ChatColor.YELLOW + "のゲームモードを" + ChatColor.GOLD + ChatColor.BOLD + ChatColor.UNDERLINE + modeName + ChatColor.RESET + ChatColor.YELLOW + "に変更しました。")
                                    continue
                                } else {
                                    MainAPI.sendOfflineMessage(sender, name)
                                    continue
                                }
                            }
                            return
                        }
                        ErrorMessage.ARGS.sendMessage(sender)
                        return
                    } else if (args[0].equals("adventure", true) || args[0].equals("a", true) || args[0].equals("2", true)) {
                        val modeType = Gamemode.ADVENTURE.gameMode
                        val modeName = Gamemode.ADVENTURE.modeName

                        if (args.size > 1) {
                            for (i in 1 until args.size) {
                                val name = args[i]
                                if (Bukkit.getPlayerExact(name) != null) {
                                    val p = Bukkit.getPlayerExact(name)!!
                                    p.gameMode = modeType
                                    p.sendMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}あなたのゲームモードを" + ChatColor.BOLD + ChatColor.UNDERLINE + modeName + ChatColor.RESET + ChatColor.GRAY + "に変更しました。")
                                    sender.sendMessage(MainAPI.getPrefix(PrefixType.WARNING) + p.name + ChatColor.YELLOW + "のゲームモードを" + ChatColor.GOLD + ChatColor.BOLD + ChatColor.UNDERLINE + modeName + ChatColor.RESET + ChatColor.YELLOW + "に変更しました。")
                                    continue
                                } else {
                                    MainAPI.sendOfflineMessage(sender, name)
                                    continue
                                }
                            }
                            return
                        }
                        ErrorMessage.ARGS.sendMessage(sender)
                        return
                    } else if (args[0].equals("spectator", true) || args[0].equals("sp", true) || args[0].equals("3", true)) {
                        val modeType = Gamemode.SPECTATOR.gameMode
                        val modeName = Gamemode.SPECTATOR.modeName

                        if (args.size > 1) {
                            for (i in 1 until args.size) {
                                val name = args[i]
                                if (Bukkit.getPlayerExact(name) != null) {
                                    val p = Bukkit.getPlayerExact(name)!!
                                    p.gameMode = modeType
                                    p.sendMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}あなたのゲームモードを" + ChatColor.BOLD + ChatColor.UNDERLINE + modeName + ChatColor.RESET + ChatColor.GRAY + "に変更しました。")
                                    sender.sendMessage(MainAPI.getPrefix(PrefixType.WARNING) + p.name + ChatColor.YELLOW + "のゲームモードを" + ChatColor.GOLD + ChatColor.BOLD + ChatColor.UNDERLINE + modeName + ChatColor.RESET + ChatColor.YELLOW + "に変更しました。")
                                    continue
                                } else {
                                    MainAPI.sendOfflineMessage(sender, name)
                                    continue
                                }
                            }
                            return
                        }
                        ErrorMessage.ARGS.sendMessage(sender)
                        return
                    }
                }
                ErrorMessage.ARGS.sendMessage(sender)
                return
            } else if (label.equals("gms", true) || label.equals("gm0", true)) {
                val modeType = Gamemode.SURVIVAL.gameMode
                val modeName = Gamemode.SURVIVAL.modeName

                if (args.size > 0) {
                    for (name in args) {
                        if (Bukkit.getPlayerExact(name) != null) {
                            val p = Bukkit.getPlayerExact(name)!!
                            p.gameMode = modeType
                            p.sendMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}あなたのゲームモードを" + ChatColor.BOLD + ChatColor.UNDERLINE + modeName + ChatColor.RESET + ChatColor.GRAY + "に変更しました。")
                            sender.sendMessage(MainAPI.getPrefix(PrefixType.WARNING) + p.name + ChatColor.YELLOW + "のゲームモードを" + ChatColor.GOLD + ChatColor.BOLD + ChatColor.UNDERLINE + modeName + ChatColor.RESET + ChatColor.YELLOW + "に変更しました。")
                            continue
                        } else {
                            MainAPI.sendOfflineMessage(sender, name)
                            continue
                        }
                    }
                    return
                }
                ErrorMessage.ARGS.sendMessage(sender)
                return
            } else if (label.equals("gmc", true) || label.equals("gm1", true)) {
                val modeType = Gamemode.CREATIVE.gameMode
                val modeName = Gamemode.CREATIVE.modeName

                if (args.size > 0) {
                    for (name in args) {
                        if (Bukkit.getPlayerExact(name) != null) {
                            val p = Bukkit.getPlayerExact(name)!!
                            p.gameMode = modeType
                            p.sendMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}あなたのゲームモードを" + ChatColor.BOLD + ChatColor.UNDERLINE + modeName + ChatColor.RESET + ChatColor.GRAY + "に変更しました。")
                            sender.sendMessage(MainAPI.getPrefix(PrefixType.WARNING) + p.name + ChatColor.YELLOW + "のゲームモードを" + ChatColor.GOLD + ChatColor.BOLD + ChatColor.UNDERLINE + modeName + ChatColor.RESET + ChatColor.YELLOW + "に変更しました。")
                            continue
                        } else {
                            MainAPI.sendOfflineMessage(sender, name)
                            continue
                        }
                    }
                    return
                }
                ErrorMessage.ARGS.sendMessage(sender)
                return
            } else if (label.equals("gma", true) || label.equals("gm2", true)) {
                val modeType = Gamemode.ADVENTURE.gameMode
                val modeName = Gamemode.ADVENTURE.modeName
                if (args.size > 0) {
                    for (name in args) {
                        if (Bukkit.getPlayerExact(name) != null) {
                            val p = Bukkit.getPlayerExact(name)!!
                            p.gameMode = modeType
                            p.sendMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}あなたのゲームモードを" + ChatColor.BOLD + ChatColor.UNDERLINE + modeName + ChatColor.RESET + ChatColor.GRAY + "に変更しました。")
                            sender.sendMessage(MainAPI.getPrefix(PrefixType.WARNING) + p.name + ChatColor.YELLOW + "のゲームモードを" + ChatColor.GOLD + ChatColor.BOLD + ChatColor.UNDERLINE + modeName + ChatColor.RESET + ChatColor.YELLOW + "に変更しました。")
                            continue
                        } else {
                            MainAPI.sendOfflineMessage(sender, name)
                            continue
                        }
                    }
                    return
                }
                ErrorMessage.ARGS.sendMessage(sender)
                return
            } else if (label.equals("gmsp", true) || label.equals("gm3", true)) {
                val modeType = Gamemode.SPECTATOR.gameMode
                val modeName = Gamemode.SPECTATOR.modeName
                if (args.size > 0) {
                    for (name in args) {
                        if (Bukkit.getPlayerExact(name) != null) {
                            val p = Bukkit.getPlayerExact(name)!!
                            p.gameMode = modeType
                            p.sendMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}あなたのゲームモードを" + ChatColor.BOLD + ChatColor.UNDERLINE + modeName + ChatColor.RESET + ChatColor.GRAY + "に変更しました。")
                            sender.sendMessage(MainAPI.getPrefix(PrefixType.WARNING) + p.name + ChatColor.YELLOW + "のゲームモードを" + ChatColor.GOLD + ChatColor.BOLD + ChatColor.UNDERLINE + modeName + ChatColor.RESET + ChatColor.YELLOW + "に変更しました。")
                            continue
                        } else {
                            MainAPI.sendOfflineMessage(sender, name)
                            continue
                        }
                    }
                    return
                }
                ErrorMessage.ARGS.sendMessage(sender)
                return
            }
        }
    }
}
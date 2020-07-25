package jp.aoichaan0513.A_TosoGame_Live.Commands.Command

import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.GameManager
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.World.WorldConfig
import jp.aoichaan0513.A_TosoGame_Live.API.Scoreboard.Teams
import jp.aoichaan0513.A_TosoGame_Live.API.TosoGameAPI
import jp.aoichaan0513.A_TosoGame_Live.Commands.ICommand
import jp.aoichaan0513.A_TosoGame_Live.Main
import jp.aoichaan0513.A_TosoGame_Live.Utils.*
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.BlockCommandSender
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.entity.Player

class Team(name: String) : ICommand(name) {

    companion object {
        var isHunterRandom = false
        var isTuhoRandom = false
    }

    private val teamNameSet = setOf("admin", "player", "hunter", "jail", "success", "tuho")
    private val randomTeamSet = setOf("hunter", "tuho")

    override fun onPlayerCommand(sp: Player, cmd: Command, label: String, args: Array<String>) {
        runCommand(sp, cmd, label, args)
    }

    override fun onBlockCommand(bs: BlockCommandSender, cmd: Command, label: String, args: Array<String>) {
        runCommand(bs, cmd, label, args)
    }

    override fun onConsoleCommand(cs: ConsoleCommandSender, cmd: Command, label: String, args: Array<String>) {
        runCommand(cs, cmd, label, args)
    }

    override fun onPlayerTabComplete(sp: Player, cmd: Command, alias: String, args: Array<String>): List<String>? {
        if (!TosoGameAPI.isAdmin(sp)) return null
        return getTabList(args)
    }

    override fun onBlockTabComplete(bs: BlockCommandSender, cmd: Command, alias: String, args: Array<String>): List<String>? {
        return getTabList(args)
    }

    override fun onConsoleTabComplete(cs: ConsoleCommandSender, cmd: Command, alias: String, args: Array<String>): List<String>? {
        return getTabList(args)
    }


    private fun runCommand(sender: CommandSender, cmd: Command, label: String, args: Array<String>) {
        if (!TosoGameAPI.isPlayer(sender) || TosoGameAPI.isAdmin(sender as Player)) {
            if (args.isNotEmpty()) {
                when (args[0].toLowerCase()) {
                    "random", "rand" -> {
                        if (args.size != 1) {
                            if (randomTeamSet.contains(args[1].toLowerCase())) {
                                if (args.size != 2) {
                                    if (ParseUtil.isInt(args[2]) && args[2].toInt() > 0) {
                                        random(
                                                sender,
                                                args[2].toInt(),
                                                if (args[1].equals("hunter", true)) RandomType.HUNTER else RandomType.TUHO
                                        )
                                        return
                                    }
                                }
                                MainAPI.sendMessage(sender, MainAPI.ErrorMessage.ARGS_INTEGER)
                                return
                            }
                        }
                        sendHelpMessage(sender, label, ErrorArgType.RANDOM)
                        return
                    }
                    else -> {
                        if (args.size > 1) {
                            if (isTeamName(args[0])) {
                                val worldConfig = Main.worldConfig
                                val team = getTeam(args[0])
                                val teamName = ChatColor.stripColor(team.displayName)

                                if (args[1].startsWith("@") && args[1].length > 3) {
                                    val selector = args[1].substring(1).toLowerCase()

                                    when (selector) {
                                        "all" -> {
                                            val list = Bukkit.getOnlinePlayers().filter { !it.isAdminTeam && !it.isTeam(team) }
                                            list.forEach {
                                                it.setTeam(team, worldConfig = worldConfig)
                                                Bukkit.broadcastMessage("${MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY)}${it.name}が${teamName}になりました。")
                                            }

                                            sender.sendMessage("${MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY)}${ChatColor.GREEN}${ChatColor.UNDERLINE}${list.size}人${ChatColor.RESET}${ChatColor.GRAY}を${teamName}に変更しました。")
                                        }
                                        else -> {
                                            if (isTeamSelector(selector, team)) {
                                                val selectorTeam = getTeam(selector)

                                                val list = Bukkit.getOnlinePlayers().filter { it.isTeam(selectorTeam) }
                                                list.forEach {
                                                    it.setTeam(team, worldConfig = worldConfig)
                                                    Bukkit.broadcastMessage("${MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY)}${it.name}が${teamName}になりました。")
                                                }

                                                sender.sendMessage("${MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY)}${ChatColor.GREEN}${ChatColor.UNDERLINE}${list.size}人${ChatColor.RESET}${ChatColor.GRAY}を${teamName}に変更しました。")
                                                return
                                            }
                                            sendHelpMessage(sender, label, ErrorArgType.SELECTOR, team)
                                            return
                                        }
                                    }
                                } else {
                                    var v = 0
                                    for (i in 1 until args.size) {
                                        val player = Bukkit.getPlayerExact(args[i])
                                        if (player != null) {
                                            if (!player.isTeam(team)) {
                                                player.setTeam(team, worldConfig = worldConfig)
                                                Bukkit.broadcastMessage("${MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY)}${player.name}が${teamName}になりました。")
                                                v++
                                            } else {
                                                sender.sendMessage("${MainAPI.getPrefix(MainAPI.PrefixType.ERROR)}${player.name}はすでに${teamName}に所属しています。")
                                            }
                                            continue
                                        } else {
                                            MainAPI.sendOfflineMessage(sender, args[i])
                                            continue
                                        }
                                    }
                                    sender.sendMessage("${MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY)}${ChatColor.GREEN}${ChatColor.UNDERLINE}${v}人${ChatColor.RESET}${ChatColor.GRAY}を${teamName}に変更しました。")
                                }
                                return
                            }
                        }
                        sendHelpMessage(sender, label, ErrorArgType.TEAM)
                        return
                    }
                }
                return
            }
            sendHelpMessage(sender, label)
            return
        }
        MainAPI.sendMessage(sender, MainAPI.ErrorMessage.PERMISSIONS_TEAM_ADMIN)
        return
    }

    private fun random(sender: CommandSender, count: Int, randomType: RandomType, worldConfig: WorldConfig = Main.worldConfig) {
        when (randomType) {
            RandomType.HUNTER -> {
                if (!isHunterRandom) {
                    Main.hunterShuffleSet.clear()
                    isHunterRandom = true

                    sender.sendMessage("${MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY)}ハンター募集を開始しました。")
                    if (GameManager.isGame(GameManager.GameState.GAME)) {
                        for (player in Bukkit.getOnlinePlayers().filter { it.isJailTeam })
                            player.sendMessage("""
                                ${MainAPI.getPrefix(ChatColor.YELLOW)}ハンターを${ChatColor.GOLD}${ChatColor.UNDERLINE}${count}人${ChatColor.RESET}${ChatColor.YELLOW}募集します。
                                ${MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY)}ハンターを希望の方は20秒以内に"${ChatColor.UNDERLINE}/h${ChatColor.RESET}${ChatColor.GRAY}"と入力してください。
                            """.trimIndent())
                    } else {
                        Bukkit.broadcastMessage("""
                            ${MainAPI.getPrefix(ChatColor.YELLOW)}ハンターを${ChatColor.GOLD}${ChatColor.UNDERLINE}${count}人${ChatColor.RESET}${ChatColor.YELLOW}募集します。
                            ${MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY)}ハンターを希望の方は20秒以内に"${ChatColor.UNDERLINE}/h${ChatColor.RESET}${ChatColor.GRAY}"と入力してください。
                        """.trimIndent())
                    }

                    var c = 20
                    Bukkit.getScheduler().runTaskTimer(Main.pluginInstance, Runnable {
                        if (c == 10) {
                            Bukkit.broadcastMessage("${MainAPI.getPrefix(ChatColor.YELLOW)}ハンターの募集終了まで${ChatColor.GOLD}${ChatColor.UNDERLINE}残り10秒${ChatColor.RESET}${ChatColor.YELLOW}です。")
                        } else if (c == 0) {
                            val list = MainAPI.getOnlinePlayers(Main.hunterShuffleSet).shuffled().toMutableList()

                            if (list.isNotEmpty()) {
                                Bukkit.broadcastMessage("${MainAPI.getPrefix(ChatColor.YELLOW)}ハンターを${ChatColor.GOLD}${ChatColor.UNDERLINE}${count}人${ChatColor.RESET}${ChatColor.YELLOW}選出しています…")

                                for (i in 0 until count.coerceAtMost(list.size)) {
                                    val p = list.removeAt(i)
                                    p.setTeam(Teams.OnlineTeam.TOSO_HUNTER, worldConfig = worldConfig)

                                    Bukkit.broadcastMessage("${MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY)}${p.name}がハンターになりました。")
                                }

                                sender.sendMessage("${MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY)}${ChatColor.GREEN}${ChatColor.UNDERLINE}${count}人${ChatColor.RESET}${ChatColor.GRAY}をハンターに追加しました。")
                                Main.hunterShuffleSet.clear()
                            } else {
                                Bukkit.broadcastMessage("${MainAPI.getPrefix(MainAPI.PrefixType.ERROR)}ハンターを希望する方がいなかったため選出をキャンセルしました。")
                                Main.hunterShuffleSet.clear()
                            }
                            isHunterRandom = false
                        }
                        c--
                    }, 0, 20)
                    return
                }
                sender.sendMessage("${MainAPI.getPrefix(MainAPI.PrefixType.ERROR)}ハンター募集中のため実行できません。")
            }
            RandomType.TUHO -> {
                if (!isTuhoRandom) {
                    Main.tuhoShuffleSet.clear()
                    isTuhoRandom = true

                    sender.sendMessage("${MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY)}通報部隊募集を開始しました。")
                    if (GameManager.isGame(GameManager.GameState.GAME)) {
                        for (player in Bukkit.getOnlinePlayers().filter { it.isJailTeam })
                            player.sendMessage("""
                                ${MainAPI.getPrefix(ChatColor.YELLOW)}通報部隊を${ChatColor.GOLD}${ChatColor.UNDERLINE}${count}人${ChatColor.RESET}${ChatColor.YELLOW}募集します。
                                ${MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY)}通報部隊を希望の方は20秒以内に"${ChatColor.UNDERLINE}/h${ChatColor.RESET}${ChatColor.GRAY}"と入力してください。
                            """.trimIndent())
                    } else {
                        Bukkit.broadcastMessage("""
                            ${MainAPI.getPrefix(ChatColor.YELLOW)}通報部隊を${ChatColor.GOLD}${ChatColor.UNDERLINE}${count}人${ChatColor.RESET}${ChatColor.YELLOW}募集します。
                            ${MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY)}通報部隊を希望の方は20秒以内に"${ChatColor.UNDERLINE}/h${ChatColor.RESET}${ChatColor.GRAY}"と入力してください。
                        """.trimIndent())
                    }

                    var c = 20
                    Bukkit.getScheduler().runTaskTimer(Main.pluginInstance, Runnable {
                        if (c == 10) {
                            Bukkit.broadcastMessage("${MainAPI.getPrefix(ChatColor.YELLOW)}通報部隊の募集終了まで${ChatColor.GOLD}${ChatColor.UNDERLINE}残り10秒${ChatColor.RESET}${ChatColor.YELLOW}です。")
                        } else if (c == 0) {
                            val list = MainAPI.getOnlinePlayers(Main.tuhoShuffleSet).shuffled().toMutableList()

                            if (list.isNotEmpty()) {
                                Bukkit.broadcastMessage("${MainAPI.getPrefix(ChatColor.YELLOW)}通報部隊を${ChatColor.GOLD}${ChatColor.UNDERLINE}${count}人${ChatColor.RESET}${ChatColor.YELLOW}選出しています…")

                                for (i in 0 until count.coerceAtMost(list.size)) {
                                    val p = list.removeAt(i)
                                    p.setTeam(Teams.OnlineTeam.TOSO_TUHO, worldConfig = worldConfig)

                                    Bukkit.broadcastMessage("${MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY)}${p.name}が通報部隊になりました。")
                                }

                                sender.sendMessage("${MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY)}${ChatColor.GREEN}${ChatColor.UNDERLINE}${count}人${ChatColor.RESET}${ChatColor.GRAY}を通報部隊に追加しました。")
                                Main.tuhoShuffleSet.clear()
                            } else {
                                Bukkit.broadcastMessage("${MainAPI.getPrefix(MainAPI.PrefixType.ERROR)}通報部隊を希望する方がいなかったため選出をキャンセルしました。")
                                Main.tuhoShuffleSet.clear()
                            }
                            isTuhoRandom = false
                        }
                        c--
                    }, 0, 20)
                    return
                }
                sender.sendMessage("${MainAPI.getPrefix(MainAPI.PrefixType.ERROR)}通報部隊募集中のため実行できません。")
            }
        }
    }


    private fun getTabList(args: Array<String>): List<String>? {
        if (args.size == 1) {
            val set = mutableSetOf("random", "rand")
            teamNameSet.forEach { set.add(it) }
            return getTabList(args[0], set)
        } else if (args.size == 2) {
            return getTabList(args[1], when (args[0].toLowerCase()) {
                "random", "rand" -> randomTeamSet
                else -> {
                    if (!isTeamName(args[0])) emptySet<String>()
                    val set = mutableSetOf("@all")
                    getTeamSelector(getTeam(args[0])).forEach { set.add("@$it") }
                    Bukkit.getOnlinePlayers().forEach { set.add(it.name) }
                    set
                }
            })
        } else if (args.size > 2) {
            return getTabList(args[args.size - 1], if (isTeamName(args[0]) && !args[1].startsWith("@")) {
                val set = mutableSetOf<String>()
                Bukkit.getOnlinePlayers().forEach { set.add(it.name) }
                set
            } else {
                emptySet<String>()
            })
        }
        return null
    }


    private fun sendHelpMessage(sender: CommandSender, label: String, type: ErrorArgType = ErrorArgType.GENERAL, team: Teams.OnlineTeam = Teams.OnlineTeam.TOSO_ADMIN) {
        sender.sendMessage(when (type) {
            ErrorArgType.GENERAL -> """
                    ${MainAPI.getPrefix(MainAPI.PrefixType.ERROR)}引数が不正です。
                    ${MainAPI.getPrefix(MainAPI.PrefixType.ERROR)}コマンドの使い方:
                    ${MainAPI.getPrefix(MainAPI.PrefixType.ERROR)}"/$label admin <プレイヤー名... / チームセレクター>" - 運営へのチーム変更
                    ${MainAPI.getPrefix(MainAPI.PrefixType.ERROR)}"/$label player <プレイヤー名... / チームセレクター>" - 逃走者へのチーム変更
                    ${MainAPI.getPrefix(MainAPI.PrefixType.ERROR)}"/$label hunter <プレイヤー名... / チームセレクター>" - ハンターへのチーム変更
                    ${MainAPI.getPrefix(MainAPI.PrefixType.ERROR)}"/$label jail <プレイヤー名... / チームセレクター>" - 確保者へのチーム変更
                    ${MainAPI.getPrefix(MainAPI.PrefixType.ERROR)}"/$label success <プレイヤー名... / チームセレクター>" - 生存者へのチーム変更
                    ${MainAPI.getPrefix(MainAPI.PrefixType.ERROR)}"/$label tuho <プレイヤー名... / チームセレクター>" - 通報部隊へのチーム変更
                    ${MainAPI.getPrefix(MainAPI.PrefixType.ERROR)}"/$label random <チーム> <募集人数>" または "/$label rand <チーム> <募集人数>" - ハンター (通報部隊)の応募を開始
                """.trimIndent()
            ErrorArgType.TEAM -> {
                val stringBuilder = StringBuilder("${MainAPI.getPrefix(MainAPI.PrefixType.ERROR)}引数が不正です。${ChatColor.RED}${ChatColor.BOLD}${ChatColor.UNDERLINE}チーム名${ChatColor.RESET}${ChatColor.RED}を指定してください。\n")

                teamNameSet.forEach { stringBuilder.append("${MainAPI.getPrefix(MainAPI.PrefixType.ERROR)}\"$it\" - ${getTeamDescription(it)}\n") }

                stringBuilder.toString().trim()
            }
            ErrorArgType.RANDOM -> """
                    ${MainAPI.getPrefix(MainAPI.PrefixType.ERROR)}引数が不正です。${ChatColor.RED}${ChatColor.BOLD}${ChatColor.UNDERLINE}募集するチーム名${ChatColor.RESET}${ChatColor.RED}を指定してください。
                    ${MainAPI.getPrefix(MainAPI.PrefixType.ERROR)}"hunter" - ハンターチーム
                    ${MainAPI.getPrefix(MainAPI.PrefixType.ERROR)}"tuho" - 通報部隊チーム
                """.trimIndent()
            ErrorArgType.SELECTOR -> {
                val stringBuilder = StringBuilder("${MainAPI.getPrefix(MainAPI.PrefixType.ERROR)}引数が不正です。正しい${ChatColor.RED}${ChatColor.BOLD}${ChatColor.UNDERLINE}セレクター${ChatColor.RESET}${ChatColor.RED}を指定してください。\n")

                getTeamSelector(team).forEach { stringBuilder.append("${MainAPI.getPrefix(MainAPI.PrefixType.ERROR)}\"$it\" - ${getTeamDescription(it)}\n") }

                stringBuilder.toString().trim()
            }
        })
    }


    private fun isTeamName(str: String): Boolean {
        return teamNameSet.contains(str.toLowerCase())
    }

    private fun isTeamSelector(str: String, team: Teams.OnlineTeam): Boolean {
        var text = str.toLowerCase()
        if (text.startsWith("@"))
            text = text.substring(1)

        return getTeamSelector(team).contains(text)
    }

    private fun getTeamSelector(team: Teams.OnlineTeam): Set<String> {
        return when (team) {
            Teams.OnlineTeam.TOSO_ADMIN -> setOf("player", "hunter", "jail", "success", "tuho")
            Teams.OnlineTeam.TOSO_PLAYER -> setOf("admin", "hunter", "jail", "success", "tuho")
            Teams.OnlineTeam.TOSO_HUNTER -> setOf("admin", "player", "jail", "success", "tuho")
            Teams.OnlineTeam.TOSO_JAIL -> setOf("admin", "player", "hunter", "success", "tuho")
            Teams.OnlineTeam.TOSO_SUCCESS -> setOf("admin", "player", "hunter", "jail", "tuho")
            Teams.OnlineTeam.TOSO_TUHO -> setOf("admin", "player", "hunter", "jail", "success")
            else -> setOf()
        }
    }

    private fun getTeam(str: String): Teams.OnlineTeam {
        return when (str) {
            "admin" -> Teams.OnlineTeam.TOSO_ADMIN
            "player" -> Teams.OnlineTeam.TOSO_PLAYER
            "hunter" -> Teams.OnlineTeam.TOSO_HUNTER
            "jail" -> Teams.OnlineTeam.TOSO_JAIL
            "success" -> Teams.OnlineTeam.TOSO_SUCCESS
            "tuho" -> Teams.OnlineTeam.TOSO_TUHO
            else -> Teams.OnlineTeam.UNKNOWN
        }
    }

    private fun getTeamDescription(str: String): String {
        return "${when (str) {
            "admin" -> "運営"
            "player" -> "逃走者"
            "hunter" -> "ハンター"
            "jail" -> "確保者"
            "success" -> "生存者"
            "tuho" -> "通報部隊"
            else -> ""
        }}チーム"
    }


    private enum class RandomType {
        HUNTER,
        TUHO
    }

    private enum class ErrorArgType {
        GENERAL,
        TEAM,
        RANDOM,
        SELECTOR
    }
}
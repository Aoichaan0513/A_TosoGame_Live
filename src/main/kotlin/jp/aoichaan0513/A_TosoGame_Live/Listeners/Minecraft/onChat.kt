package jp.aoichaan0513.A_TosoGame_Live.Listeners.Minecraft

import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI
import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI.PrefixType
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.GameManager
import jp.aoichaan0513.A_TosoGame_Live.API.Scoreboard.Teams
import jp.aoichaan0513.A_TosoGame_Live.API.Scoreboard.Teams.OnlineTeam
import jp.aoichaan0513.A_TosoGame_Live.API.TosoGameAPI
import jp.aoichaan0513.A_TosoGame_Live.Main
import jp.aoichaan0513.A_TosoGame_Live.Mission.MissionManager
import jp.aoichaan0513.A_TosoGame_Live.Utils.RomajiConverter
import org.bukkit.*
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent
import java.util.regex.Pattern

class onChat : Listener {

    val range = 7

    @EventHandler
    fun onAsyncChat(e: AsyncPlayerChatEvent) {
        val p = e.player

        if (GameManager.isGame()) {
            if (p.inventory.itemInMainHand.type == Material.BOOK) {
                val meta = p.inventory.itemInMainHand.itemMeta
                e.isCancelled = true
                sendMessage(p, e.message, ChatColor.stripColor(meta!!.displayName) == Main.PHONE_ITEM_NAME)
            } else if (p.inventory.itemInOffHand.type == Material.BOOK) {
                val meta = p.inventory.itemInOffHand.itemMeta
                e.isCancelled = true
                sendMessage(p, e.message, ChatColor.stripColor(meta!!.displayName) == Main.PHONE_ITEM_NAME)
            } else {
                e.isCancelled = true
                sendMessage(p, e.message, false)
            }
        } else {
            e.isCancelled = true
            sendMessage(p, e.message, false)
        }
    }

    private fun sendMessage(p: Player, message: String, isTeam: Boolean) {
        val regex = "^@([A-z0-9_]{2,17})"

        var rawMessage = ChatColor.translateAlternateColorCodes('&', message)

        val pattern = Pattern.compile(regex)
        val matcher = pattern.matcher(rawMessage)

        if (GameManager.isGame()) {
            if (isTeam) {
                if (matcher.find()) {
                    val name = matcher.group().substring(1)
                    rawMessage = rawMessage.substring(2 + name.length)
                    if (name.equals("team", true)) {
                        sendMessage(ChatType.TEAM, p, p, convertMessage(rawMessage))
                    } else {
                        sendMessage(if (Bukkit.getPlayer(name) != null) ChatType.PRIVATE else ChatType.RANGE, p, Bukkit.getPlayer(name)
                                ?: p, convertMessage(rawMessage))
                    }
                } else {
                    sendMessage(ChatType.TEAM_GLOBAL, p, p, convertMessage(rawMessage))
                }
            } else {
                if (matcher.find()) {
                    val name = matcher.group().substring(1)
                    rawMessage = rawMessage.substring(2 + name.length)
                    if (name.equals("team", true)) {
                        sendMessage(ChatType.TEAM, p, p, convertMessage(rawMessage))
                    } else {
                        sendMessage(if (Bukkit.getPlayer(name) != null) ChatType.PRIVATE else ChatType.RANGE, p, Bukkit.getPlayer(name)
                                ?: p, convertMessage(rawMessage))
                    }
                } else {
                    sendMessage(ChatType.RANGE, p, p, convertMessage(rawMessage))
                }
            }
        } else {
            if (matcher.find()) {
                val name = matcher.group().substring(1)
                rawMessage = rawMessage.substring(2 + name.length)
                if (name.equals("team", true)) {
                    sendMessage(ChatType.TEAM, p, p, convertMessage(rawMessage))
                } else {
                    sendMessage(if (Bukkit.getPlayer(name) != null) ChatType.PRIVATE else ChatType.GLOBAL, p, Bukkit.getPlayer(name)
                            ?: p, convertMessage(rawMessage))
                }
            } else {
                sendMessage(ChatType.GLOBAL, p, p, convertMessage(rawMessage))
            }
        }
    }

    private fun sendMessage(chatType: ChatType, sp: Player, p: Player, msg: String) {
        val basePrefix = "${(if (TosoGameAPI.isBroadCaster(sp)) "${ChatColor.GOLD}${ChatColor.BOLD} * ${ChatColor.RESET}" else "")}${Teams.getTeamLabel(Teams.DisplaySlot.CHAT, sp)}${ChatColor.RESET}${(if (!MainAPI.isHidePlayer(p)) " ${sp.displayName}" else "")}${ChatColor.GREEN}: ${ChatColor.RESET}"
        val globalMessagePrefix = "${ChatType.GLOBAL.prefix}$basePrefix"
        val rangeMessagePrefix = "${ChatType.RANGE.prefix}$basePrefix"
        val teamMessagePrefix = "${ChatType.TEAM.prefix}$basePrefix"
        val privateMessagePrefix = "${ChatType.PRIVATE.prefix}${Teams.getTeamLabel(Teams.DisplaySlot.CHAT, sp)} ${sp.displayName}${ChatColor.GRAY} -> ${ChatColor.RESET}${Teams.getTeamLabel(Teams.DisplaySlot.CHAT, p)} ${p.displayName}${ChatColor.GREEN}: ${ChatColor.RESET}"

        when (chatType) {
            ChatType.GLOBAL -> Bukkit.broadcastMessage(globalMessagePrefix + msg)
            ChatType.RANGE -> {
                if (Teams.hasJoinedTeam(OnlineTeam.TOSO_PLAYER, sp) || Teams.hasJoinedTeam(OnlineTeam.TOSO_HUNTER, sp) ||
                        Teams.hasJoinedTeam(OnlineTeam.TOSO_SUCCESS, sp) || Teams.hasJoinedTeam(OnlineTeam.TOSO_TUHO, sp)) {

                    val loc = sp.location
                    Bukkit.getConsoleSender().sendMessage(rangeMessagePrefix + msg)
                    for (player in Bukkit.getOnlinePlayers())
                        if (Teams.hasJoinedTeam(OnlineTeam.TOSO_ADMIN, player) || player.location.distance(loc) <= range)
                            player.sendMessage(rangeMessagePrefix + msg)
                } else if (Teams.hasJoinedTeam(OnlineTeam.TOSO_JAIL, sp)) {
                    if (sp.gameMode == GameMode.SPECTATOR) {
                        Bukkit.getConsoleSender().sendMessage(teamMessagePrefix + msg)
                        for (player in Bukkit.getOnlinePlayers())
                            if (Teams.hasJoinedTeam(OnlineTeam.TOSO_ADMIN, player) || Teams.hasJoinedTeam(OnlineTeam.TOSO_JAIL, player))
                                player.sendMessage(teamMessagePrefix + msg)
                    } else {
                        Bukkit.broadcastMessage(globalMessagePrefix + msg)
                    }
                } else {
                    Bukkit.broadcastMessage(globalMessagePrefix + msg)
                }
            }
            ChatType.TEAM -> {
                if (Teams.hasJoinedTeam(OnlineTeam.TOSO_PLAYER, sp) || Teams.hasJoinedTeam(OnlineTeam.TOSO_SUCCESS, sp)) {
                    Bukkit.getConsoleSender().sendMessage(teamMessagePrefix + msg)

                    if (!MissionManager.isMission(MissionManager.MissionState.HUNTER_ZONE) && !MissionManager.isMission(MissionManager.MissionState.TIMED_DEVICE)) {
                        for (player in Bukkit.getOnlinePlayers().filter { Teams.hasJoinedTeam(OnlineTeam.TOSO_ADMIN, it) || Teams.hasJoinedTeam(OnlineTeam.TOSO_PLAYER, it) || Teams.hasJoinedTeam(OnlineTeam.TOSO_SUCCESS, it) })
                            player.sendMessage(teamMessagePrefix + msg)
                    } else {
                        sp.sendMessage("${MainAPI.getPrefix(PrefixType.ERROR)}ミッション実施中のため利用できません。")
                    }
                } else if (Teams.hasJoinedTeam(OnlineTeam.TOSO_HUNTER, sp) || Teams.hasJoinedTeam(OnlineTeam.TOSO_TUHO, sp)) {
                    Bukkit.getConsoleSender().sendMessage(teamMessagePrefix + msg)

                    for (player in Bukkit.getOnlinePlayers().filter { Teams.hasJoinedTeam(OnlineTeam.TOSO_ADMIN, it) || Teams.hasJoinedTeam(OnlineTeam.TOSO_HUNTER, it) || Teams.hasJoinedTeam(OnlineTeam.TOSO_TUHO, it) })
                        player.sendMessage(teamMessagePrefix + msg)
                } else if (Teams.hasJoinedTeam(OnlineTeam.TOSO_JAIL, sp)) {
                    Bukkit.getConsoleSender().sendMessage(teamMessagePrefix + msg)

                    for (player in Bukkit.getOnlinePlayers().filter { Teams.hasJoinedTeam(OnlineTeam.TOSO_ADMIN, it) || Teams.hasJoinedTeam(OnlineTeam.TOSO_JAIL, it) })
                        player.sendMessage(teamMessagePrefix + msg)
                } else {
                    Bukkit.getConsoleSender().sendMessage(teamMessagePrefix + msg)

                    for (player in Bukkit.getOnlinePlayers().filter { Teams.hasJoinedTeam(OnlineTeam.TOSO_ADMIN, it) })
                        player.sendMessage(teamMessagePrefix + msg)
                }
            }
            ChatType.TEAM_GLOBAL -> {
                if (Teams.hasJoinedTeam(OnlineTeam.TOSO_PLAYER, sp) || Teams.hasJoinedTeam(OnlineTeam.TOSO_SUCCESS, sp)) {
                    Bukkit.getConsoleSender().sendMessage(teamMessagePrefix + msg)

                    if (!MissionManager.isMission(MissionManager.MissionState.HUNTER_ZONE) && !MissionManager.isMission(MissionManager.MissionState.TIMED_DEVICE)) {
                        for (player in Bukkit.getOnlinePlayers().filter { Teams.hasJoinedTeam(OnlineTeam.TOSO_ADMIN, it) || Teams.hasJoinedTeam(OnlineTeam.TOSO_PLAYER, it) || Teams.hasJoinedTeam(OnlineTeam.TOSO_SUCCESS, it) })
                            player.sendMessage(teamMessagePrefix + msg)
                    } else {
                        sp.sendMessage("${MainAPI.getPrefix(PrefixType.ERROR)}ミッション実施中のため利用できません。")
                    }
                } else if (Teams.hasJoinedTeam(OnlineTeam.TOSO_HUNTER, sp) || Teams.hasJoinedTeam(OnlineTeam.TOSO_TUHO, sp)) {
                    Bukkit.getConsoleSender().sendMessage(teamMessagePrefix + msg)

                    for (player in Bukkit.getOnlinePlayers().filter { Teams.hasJoinedTeam(OnlineTeam.TOSO_ADMIN, it) || Teams.hasJoinedTeam(OnlineTeam.TOSO_HUNTER, it) || Teams.hasJoinedTeam(OnlineTeam.TOSO_TUHO, it) })
                        player.sendMessage(teamMessagePrefix + msg)
                } else {
                    Bukkit.broadcastMessage(globalMessagePrefix + msg)
                }
            }
            ChatType.PRIVATE -> {
                if (sp.uniqueId !== p.uniqueId) {
                    if (Teams.hasJoinedTeam(OnlineTeam.TOSO_PLAYER, sp) || Teams.hasJoinedTeam(OnlineTeam.TOSO_SUCCESS, sp)) {
                        if (Teams.hasJoinedTeam(OnlineTeam.TOSO_ADMIN, p) || Teams.hasJoinedTeam(OnlineTeam.TOSO_PLAYER, p) || Teams.hasJoinedTeam(OnlineTeam.TOSO_SUCCESS, p)) {
                            Bukkit.getConsoleSender().sendMessage(privateMessagePrefix + msg)
                            for (player in Bukkit.getOnlinePlayers()) if (Teams.hasJoinedTeam(OnlineTeam.TOSO_ADMIN, player)) player.sendMessage(privateMessagePrefix + msg)
                            sp.sendMessage(privateMessagePrefix + msg)
                            p.sendMessage(privateMessagePrefix + msg)
                            p.playSound(p.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f)
                        } else {
                            sp.sendMessage("${MainAPI.getPrefix(PrefixType.ERROR)}他のチームのプレイヤーにはメッセージを送信できません。")
                        }
                    } else if (Teams.hasJoinedTeam(OnlineTeam.TOSO_HUNTER, sp) || Teams.hasJoinedTeam(OnlineTeam.TOSO_TUHO, sp)) {
                        if (Teams.hasJoinedTeam(OnlineTeam.TOSO_ADMIN, p) || Teams.hasJoinedTeam(OnlineTeam.TOSO_HUNTER, p) || Teams.hasJoinedTeam(OnlineTeam.TOSO_TUHO, p)) {
                            Bukkit.getConsoleSender().sendMessage(privateMessagePrefix + msg)
                            for (player in Bukkit.getOnlinePlayers()) if (Teams.hasJoinedTeam(OnlineTeam.TOSO_ADMIN, player)) player.sendMessage(privateMessagePrefix + msg)
                            sp.sendMessage(privateMessagePrefix + msg)
                            p.sendMessage(privateMessagePrefix + msg)
                            p.playSound(p.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f)
                        } else {
                            sp.sendMessage("${MainAPI.getPrefix(PrefixType.ERROR)}他のチームのプレイヤーにはメッセージを送信できません。")
                        }
                    } else if (Teams.hasJoinedTeam(OnlineTeam.TOSO_JAIL, sp)) {
                        if (Teams.hasJoinedTeam(OnlineTeam.TOSO_ADMIN, p) || Teams.hasJoinedTeam(OnlineTeam.TOSO_JAIL, p)) {
                            Bukkit.getConsoleSender().sendMessage(privateMessagePrefix + msg)
                            for (player in Bukkit.getOnlinePlayers()) if (Teams.hasJoinedTeam(OnlineTeam.TOSO_ADMIN, player)) player.sendMessage(privateMessagePrefix + msg)
                            sp.sendMessage(privateMessagePrefix + msg)
                            p.sendMessage(privateMessagePrefix + msg)
                            p.playSound(p.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f)
                        } else {
                            sp.sendMessage("${MainAPI.getPrefix(PrefixType.ERROR)}他のチームのプレイヤーにはメッセージを送信できません。")
                        }
                    } else if (Teams.hasJoinedTeam(OnlineTeam.TOSO_ADMIN, sp)) {
                        Bukkit.getConsoleSender().sendMessage(privateMessagePrefix + msg)
                        for (player in Bukkit.getOnlinePlayers()) if (Teams.hasJoinedTeam(OnlineTeam.TOSO_ADMIN, player)) player.sendMessage(privateMessagePrefix + msg)
                        sp.sendMessage(privateMessagePrefix + msg)
                        p.sendMessage(privateMessagePrefix + msg)
                        p.playSound(p.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f)
                    }
                } else {
                    sp.sendMessage("${MainAPI.getPrefix(PrefixType.ERROR)}自分自身にはメッセージを送信できません。")
                }
            }
        }
    }

    fun convertMessage(msg: String): String {
        return if (msg.toByteArray().size > msg.length || msg.matches(Regex("[ \\uFF61-\\uFF9F]+")))
            msg
        else
            RomajiConverter.toKanji(msg)
    }

    enum class ChatType(val prefix: String = "", val description: String = "") {
        GLOBAL("${ChatColor.BLUE}[G]${ChatColor.RESET}", "全体チャット"),
        RANGE("${ChatColor.BLUE}[R]${ChatColor.RESET}", "範囲チャット"),
        TEAM("${ChatColor.BLUE}[T]${ChatColor.RESET}", "チームチャット (制限あり)"),
        TEAM_GLOBAL,
        PRIVATE("${ChatColor.BLUE}[P]${ChatColor.RESET}", "プライベートチャット");
    }
}
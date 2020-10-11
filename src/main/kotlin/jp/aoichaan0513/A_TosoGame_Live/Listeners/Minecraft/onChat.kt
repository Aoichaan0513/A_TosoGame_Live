package jp.aoichaan0513.A_TosoGame_Live.Listeners.Minecraft

import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI
import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI.PrefixType
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.GameManager
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.Player.PlayerManager
import jp.aoichaan0513.A_TosoGame_Live.API.Scoreboard.Teams
import jp.aoichaan0513.A_TosoGame_Live.Main
import jp.aoichaan0513.A_TosoGame_Live.Mission.MissionManager
import jp.aoichaan0513.A_TosoGame_Live.Utils.*
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
        val basePrefix = "${Teams.getTeamLabel(Teams.DisplaySlot.CHAT, sp)}${ChatColor.RESET}${(if (!PlayerManager.loadConfig(p).visibility) " ${sp.displayName}" else "")}${ChatColor.GREEN}: ${ChatColor.RESET}"
        val globalMessagePrefix = "${ChatType.GLOBAL.prefix}$basePrefix"
        val rangeMessagePrefix = "${ChatType.RANGE.prefix}$basePrefix"
        val teamMessagePrefix = "${ChatType.TEAM.prefix}$basePrefix"
        val privateMessagePrefix = "${ChatType.PRIVATE.prefix}${Teams.getTeamLabel(Teams.DisplaySlot.CHAT, sp)} ${sp.displayName}${ChatColor.GRAY} -> ${ChatColor.RESET}${Teams.getTeamLabel(Teams.DisplaySlot.CHAT, p)} ${p.displayName}${ChatColor.GREEN}: ${ChatColor.RESET}"

        when (chatType) {
            ChatType.GLOBAL -> Bukkit.broadcastMessage(globalMessagePrefix + msg)
            ChatType.RANGE -> {
                if (sp.isPlayerGroup || sp.isHunterGroup) {

                    val loc = sp.location
                    Bukkit.getConsoleSender().sendMessage(rangeMessagePrefix + msg)
                    for (player in Bukkit.getOnlinePlayers().filter { it.isAdminTeam || it.location.distance(loc) <= range })
                        player.sendMessage(rangeMessagePrefix + msg)
                } else if (sp.isJailTeam) {
                    if (sp.gameMode == GameMode.SPECTATOR) {
                        Bukkit.getConsoleSender().sendMessage(teamMessagePrefix + msg)
                        for (player in Bukkit.getOnlinePlayers().filter { it.isAdminTeam || it.isJailTeam })
                            player.sendMessage(teamMessagePrefix + msg)
                    } else {
                        Bukkit.broadcastMessage(globalMessagePrefix + msg)
                    }
                } else {
                    Bukkit.broadcastMessage(globalMessagePrefix + msg)
                }
            }
            ChatType.TEAM -> {
                if (sp.isPlayerGroup) {
                    Bukkit.getConsoleSender().sendMessage(teamMessagePrefix + msg)

                    if (!MissionManager.isMission(MissionManager.MissionState.HUNTER_ZONE) && !MissionManager.isMission(MissionManager.MissionState.TIMED_DEVICE)) {
                        for (player in Bukkit.getOnlinePlayers().filter { it.isAdminTeam || it.isPlayerGroup })
                            player.sendMessage(teamMessagePrefix + msg)
                    } else {
                        sp.sendMessage("${MainAPI.getPrefix(PrefixType.ERROR)}ミッション実施中のため利用できません。")
                    }
                } else if (sp.isHunterGroup) {
                    Bukkit.getConsoleSender().sendMessage(teamMessagePrefix + msg)

                    for (player in Bukkit.getOnlinePlayers().filter { it.isAdminTeam || it.isHunterGroup })
                        player.sendMessage(teamMessagePrefix + msg)
                } else if (sp.isJailTeam) {
                    Bukkit.getConsoleSender().sendMessage(teamMessagePrefix + msg)

                    for (player in Bukkit.getOnlinePlayers().filter { it.isAdminTeam || it.isJailTeam })
                        player.sendMessage(teamMessagePrefix + msg)
                } else {
                    Bukkit.getConsoleSender().sendMessage(teamMessagePrefix + msg)

                    for (player in Bukkit.getOnlinePlayers().filter { it.isAdminTeam })
                        player.sendMessage(teamMessagePrefix + msg)
                }
            }
            ChatType.TEAM_GLOBAL -> {
                if (sp.isPlayerGroup) {
                    Bukkit.getConsoleSender().sendMessage(teamMessagePrefix + msg)

                    if (!MissionManager.isMission(MissionManager.MissionState.HUNTER_ZONE) && !MissionManager.isMission(MissionManager.MissionState.TIMED_DEVICE)) {
                        for (player in Bukkit.getOnlinePlayers().filter { it.isAdminTeam || it.isPlayerGroup })
                            player.sendMessage(teamMessagePrefix + msg)
                    } else {
                        sp.sendMessage("${MainAPI.getPrefix(PrefixType.ERROR)}ミッション実施中のため利用できません。")
                    }
                } else if (sp.isHunterGroup) {
                    Bukkit.getConsoleSender().sendMessage(teamMessagePrefix + msg)

                    for (player in Bukkit.getOnlinePlayers().filter { it.isAdminTeam || it.isHunterGroup })
                        player.sendMessage(teamMessagePrefix + msg)
                } else {
                    Bukkit.broadcastMessage(globalMessagePrefix + msg)
                }
            }
            ChatType.PRIVATE -> {
                if (sp.uniqueId !== p.uniqueId) {
                    if (sp.isPlayerGroup) {
                        if (p.isAdminTeam || p.isPlayerGroup) {
                            Bukkit.getConsoleSender().sendMessage(privateMessagePrefix + msg)
                            for (player in Bukkit.getOnlinePlayers().filter { it.isAdminTeam })
                                player.sendMessage(privateMessagePrefix + msg)

                            sp.sendMessage(privateMessagePrefix + msg)
                            p.sendMessage(privateMessagePrefix + msg)
                            p.playSound(p.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f)
                        } else {
                            sp.sendMessage("${MainAPI.getPrefix(PrefixType.ERROR)}他のチームのプレイヤーにはメッセージを送信できません。")
                        }
                    } else if (sp.isHunterGroup) {
                        if (p.isAdminTeam || p.isHunterGroup) {
                            Bukkit.getConsoleSender().sendMessage(privateMessagePrefix + msg)
                            for (player in Bukkit.getOnlinePlayers().filter { it.isAdminTeam })
                                player.sendMessage(privateMessagePrefix + msg)

                            sp.sendMessage(privateMessagePrefix + msg)
                            p.sendMessage(privateMessagePrefix + msg)
                            p.playSound(p.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f)
                        } else {
                            sp.sendMessage("${MainAPI.getPrefix(PrefixType.ERROR)}他のチームのプレイヤーにはメッセージを送信できません。")
                        }
                    } else if (sp.isJailTeam) {
                        if (p.isAdminTeam || p.isJailTeam) {
                            Bukkit.getConsoleSender().sendMessage(privateMessagePrefix + msg)
                            for (player in Bukkit.getOnlinePlayers().filter { it.isAdminTeam })
                                player.sendMessage(privateMessagePrefix + msg)

                            sp.sendMessage(privateMessagePrefix + msg)
                            p.sendMessage(privateMessagePrefix + msg)
                            p.playSound(p.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f)
                        } else {
                            sp.sendMessage("${MainAPI.getPrefix(PrefixType.ERROR)}他のチームのプレイヤーにはメッセージを送信できません。")
                        }
                    } else if (sp.isAdminTeam) {
                        Bukkit.getConsoleSender().sendMessage(privateMessagePrefix + msg)
                        for (player in Bukkit.getOnlinePlayers().filter { it.isAdminTeam })
                            player.sendMessage(privateMessagePrefix + msg)

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
        return if (msg.toByteArray().size > msg.length || msg.matches(Regex("[ \\uFF61-\\uFF9F]+"))) msg
        else RomajiConverter.toKanji(msg)
    }

    enum class ChatType(val prefix: String = "", val description: String = "") {
        GLOBAL("${ChatColor.BLUE}[G]${ChatColor.RESET}", "全体チャット"),
        RANGE("${ChatColor.BLUE}[R]${ChatColor.RESET}", "範囲チャット"),
        TEAM("${ChatColor.BLUE}[T]${ChatColor.RESET}", "チームチャット (制限あり)"),
        TEAM_GLOBAL,
        PRIVATE("${ChatColor.BLUE}[P]${ChatColor.RESET}", "プライベートチャット");
    }
}
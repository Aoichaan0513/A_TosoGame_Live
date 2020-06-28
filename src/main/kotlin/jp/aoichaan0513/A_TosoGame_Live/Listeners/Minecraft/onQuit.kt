package jp.aoichaan0513.A_TosoGame_Live.Listeners.Minecraft

import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI
import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI.PrefixType
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.GameManager
import jp.aoichaan0513.A_TosoGame_Live.API.Scoreboard.Scoreboard
import jp.aoichaan0513.A_TosoGame_Live.API.Scoreboard.Teams
import jp.aoichaan0513.A_TosoGame_Live.API.Scoreboard.Teams.OnlineTeam
import jp.aoichaan0513.A_TosoGame_Live.API.TosoGameAPI
import jp.aoichaan0513.A_TosoGame_Live.Main
import jp.aoichaan0513.A_TosoGame_Live.Mission.HunterZone
import jp.aoichaan0513.A_TosoGame_Live.Mission.MissionManager
import jp.aoichaan0513.A_TosoGame_Live.OPGame.Dice
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.scoreboard.DisplaySlot

class onQuit : Listener {

    @EventHandler
    fun onQuit(e: PlayerQuitEvent) {
        val p = e.player

        e.quitMessage = if (!MainAPI.isHidePlayer(p)) "${ChatColor.YELLOW}<- ${ChatColor.GOLD}${p.name}" else ""

        Main.hunterShuffleSet.remove(p.uniqueId)
        Main.tuhoShuffleSet.remove(p.uniqueId)

        Bukkit.getScheduler().runTaskLater(Main.pluginInstance, Runnable { Dice.setPlayer() }, 2)
        HunterZone.removeJoinedSet(p, true)

        if (Main.invisibleSet.contains(p.uniqueId)) {
            for (player in Bukkit.getOnlinePlayers())
                player.showPlayer(p)
            Main.invisibleSet.remove(p.uniqueId)
        }

        if (MissionManager.isBossBar)
            MissionManager.bossBar!!.removePlayer(p)

        for (player in Bukkit.getOnlinePlayers())
            TosoGameAPI.showPlayers(player)

        if (Teams.hasJoinedTeam(OnlineTeam.TOSO_HUNTER, p)) {
            for (player in Bukkit.getOnlinePlayers())
                if (Teams.hasJoinedTeam(OnlineTeam.TOSO_ADMIN, player))
                    player.sendMessage("${MainAPI.getPrefix(PrefixType.ERROR)}ハンターが退出しました。")
        } else if (Teams.hasJoinedTeam(OnlineTeam.TOSO_SUCCESS, p)) {
            Teams.leaveTeam(OnlineTeam.TOSO_SUCCESS, p)
            Teams.joinTeam(OnlineTeam.TOSO_PLAYER, p)
        }

        for (player in Bukkit.getOnlinePlayers()) {
            val board = Scoreboard.getBoard(player)
            if (GameManager.isGame() && (Teams.hasJoinedTeam(OnlineTeam.TOSO_PLAYER, player) || Teams.hasJoinedTeam(OnlineTeam.TOSO_SUCCESS, player))) {
                if (player.inventory.itemInMainHand.type == Material.BOOK) {
                    val itemMeta = player.inventory.itemInMainHand.itemMeta

                    if (itemMeta != null && ChatColor.stripColor(itemMeta.displayName) == Main.PHONE_ITEM_NAME)
                        board.getObjective(TosoGameAPI.Objective.SIDEBAR.objectName)?.setDisplaySlot(DisplaySlot.SIDEBAR)
                    else
                        board.clearSlot(DisplaySlot.SIDEBAR)
                } else if (player.inventory.itemInOffHand.type == Material.BOOK) {
                    val itemMeta = player.inventory.itemInOffHand.itemMeta

                    if (itemMeta != null && ChatColor.stripColor(itemMeta.displayName) == Main.PHONE_ITEM_NAME)
                        board.getObjective(TosoGameAPI.Objective.SIDEBAR.objectName)?.setDisplaySlot(DisplaySlot.SIDEBAR)
                    else
                        board.clearSlot(DisplaySlot.SIDEBAR)
                } else {
                    board.clearSlot(DisplaySlot.SIDEBAR)
                }
            } else {
                board.getObjective(TosoGameAPI.Objective.SIDEBAR.objectName)?.setDisplaySlot(DisplaySlot.SIDEBAR)
            }
            return
        }
    }
}
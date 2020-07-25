package jp.aoichaan0513.A_TosoGame_Live.Listeners.Minecraft

import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI
import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI.PrefixType
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.Player.VisibilityManager
import jp.aoichaan0513.A_TosoGame_Live.API.Scoreboard.Teams
import jp.aoichaan0513.A_TosoGame_Live.API.Scoreboard.Teams.OnlineTeam
import jp.aoichaan0513.A_TosoGame_Live.Main
import jp.aoichaan0513.A_TosoGame_Live.Mission.HunterZone
import jp.aoichaan0513.A_TosoGame_Live.Mission.MissionManager
import jp.aoichaan0513.A_TosoGame_Live.Mission.TimedDevice
import jp.aoichaan0513.A_TosoGame_Live.OPGame.Dice
import jp.aoichaan0513.A_TosoGame_Live.Utils.PlayerUtil
import jp.aoichaan0513.A_TosoGame_Live.Utils.isAdminTeam
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent

class onQuit : Listener {

    @EventHandler
    fun onQuit(e: PlayerQuitEvent) {
        val p = e.player

        e.quitMessage = if (!VisibilityManager.isHide(p, VisibilityManager.VisibilityType.ADMIN)) "${ChatColor.YELLOW}<- ${ChatColor.GOLD}${p.name}" else ""

        Main.hunterShuffleSet.remove(p.uniqueId)
        Main.tuhoShuffleSet.remove(p.uniqueId)

        Bukkit.getScheduler().runTaskLater(Main.pluginInstance, Runnable { Dice.setPlayer() }, 2)
        HunterZone.removeJoinedSet(p, true)
        TimedDevice.addFailedNumberSet(p)

        VisibilityManager.remove(p, VisibilityManager.VisibilityType.ITEM)

        MissionManager.bossBar?.removePlayer(p)

        if (Teams.hasJoinedTeam(OnlineTeam.TOSO_HUNTER, p)) {
            for (player in Bukkit.getOnlinePlayers().filter { it.isAdminTeam })
                player.sendMessage("${MainAPI.getPrefix(PrefixType.ERROR)}ハンターが退出しました。")
        } else if (Teams.hasJoinedTeam(OnlineTeam.TOSO_SUCCESS, p)) {
            Teams.joinTeam(OnlineTeam.TOSO_PLAYER, p)
        }

        PlayerUtil.setSidebar()
    }
}
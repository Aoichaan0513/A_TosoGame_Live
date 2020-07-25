package jp.aoichaan0513.A_TosoGame_Live.Utils

import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.GameManager
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.World.WorldConfig
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.World.WorldManager
import jp.aoichaan0513.A_TosoGame_Live.API.Scoreboard.Scoreboard
import jp.aoichaan0513.A_TosoGame_Live.API.Scoreboard.Teams
import jp.aoichaan0513.A_TosoGame_Live.API.TosoGameAPI
import jp.aoichaan0513.A_TosoGame_Live.Main
import jp.aoichaan0513.A_TosoGame_Live.Mission.MissionManager
import jp.aoichaan0513.A_TosoGame_Live.OPGame.OPGameManager
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.scoreboard.DisplaySlot

fun Player.isTeam(team: Teams.OnlineTeam): Boolean {
    return Teams.hasJoinedTeam(team, this)
}


val Player.isAdminTeam
    get() = isTeam(Teams.OnlineTeam.TOSO_ADMIN)

val Player.isJailTeam
    get() = isTeam(Teams.OnlineTeam.TOSO_JAIL)


val Player.isPlayerGroup
    get() = isPlayerTeam || isSuccessTeam

val Player.isPlayerTeam
    get() = isTeam(Teams.OnlineTeam.TOSO_PLAYER)

val Player.isSuccessTeam
    get() = isTeam(Teams.OnlineTeam.TOSO_SUCCESS)


val Player.isHunterGroup
    get() = isHunterTeam || isTuhoTeam

val Player.isHunterTeam
    get() = isTeam(Teams.OnlineTeam.TOSO_HUNTER)

val Player.isTuhoTeam
    get() = isTeam(Teams.OnlineTeam.TOSO_TUHO)


fun Player.setTeam(team: Teams.OnlineTeam, isTeleport: Boolean = true, isSendTeamChangeMessage: Boolean = true, worldConfig: WorldConfig = Main.worldConfig) {
    Teams.joinTeam(team, this)
    gameMode = MainAPI.getValue(isAdminTeam, GameMode.CREATIVE, GameMode.ADVENTURE)

    TosoGameAPI.setItem(WorldManager.GameType.START, this)
    TosoGameAPI.setPotionEffect(this)

    if (isAdminTeam) TosoGameAPI.addOp(this) else TosoGameAPI.removeOp(this)
    setSidebar()

    if (isAdminTeam || isJailTeam || isPlayerGroup)
        MissionManager.bossBar?.addPlayer(this)
    else
        MissionManager.bossBar?.removePlayer(this)

    if (isTeleport) {
        if (isPlayerGroup)
            TosoGameAPI.teleport(this, if (GameManager.isGame() && OPGameManager.opGameState != OPGameManager.OPGameState.NONE)
                worldConfig.opGameLocationConfig.gOPLocations.values
            else
                worldConfig.respawnLocationConfig.locations.values)
        else if (isHunterGroup)
            TosoGameAPI.teleport(this, worldConfig.hunterLocationConfig.getLocation(1))
        else if (isJailTeam)
            TosoGameAPI.teleport(this, worldConfig.jailLocationConfig.locations.values)
    }

    if (isSendTeamChangeMessage)
        sendMessage("${MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY)}あなたを${team.color}${ChatColor.UNDERLINE}${ChatColor.stripColor(team.displayName)}${ChatColor.RESET}${ChatColor.GRAY}に追加しました。")
}

fun Player.setSidebar() {
    val board = Scoreboard.getBoard(this)
    val objective = board.getObjective(TosoGameAPI.Objective.SIDEBAR.objectName) ?: return

    if (isPlayerGroup) {
        if (!Scoreboard.isHidePlayer(this)) {
            if (inventory.itemInMainHand.type == Material.BOOK) {
                val itemMeta = inventory.itemInMainHand.itemMeta

                if (itemMeta != null && ChatColor.stripColor(itemMeta.displayName) == Main.PHONE_ITEM_NAME)
                    objective.displaySlot = DisplaySlot.SIDEBAR
                else
                    board.clearSlot(DisplaySlot.SIDEBAR)
            } else if (inventory.itemInOffHand.type == Material.BOOK) {
                val itemMeta = inventory.itemInOffHand.itemMeta

                if (itemMeta != null && ChatColor.stripColor(itemMeta.displayName) == Main.PHONE_ITEM_NAME)
                    objective.displaySlot = DisplaySlot.SIDEBAR
                else
                    board.clearSlot(DisplaySlot.SIDEBAR)
            } else {
                board.clearSlot(DisplaySlot.SIDEBAR)
            }
        } else {
            objective.displaySlot = DisplaySlot.SIDEBAR
        }
    } else {
        objective.displaySlot = DisplaySlot.SIDEBAR
    }
}


class PlayerUtil {
    companion object {

        fun setSidebar() {
            Bukkit.getOnlinePlayers().forEach { it.setSidebar() }
        }
    }
}
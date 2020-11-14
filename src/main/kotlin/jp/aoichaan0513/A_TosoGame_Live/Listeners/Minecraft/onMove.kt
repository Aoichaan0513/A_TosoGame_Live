package jp.aoichaan0513.A_TosoGame_Live.Listeners.Minecraft

import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI
import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI.PrefixType
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.ActionBarManager
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.World.WorldConfig.BorderType
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.World.WorldConfig.IBorderConfig
import jp.aoichaan0513.A_TosoGame_Live.API.TosoGameAPI
import jp.aoichaan0513.A_TosoGame_Live.Main
import jp.aoichaan0513.A_TosoGame_Live.Mission.HunterZone
import jp.aoichaan0513.A_TosoGame_Live.Mission.MissionManager
import jp.aoichaan0513.A_TosoGame_Live.OPGame.OPGameManager
import jp.aoichaan0513.A_TosoGame_Live.Utils.isAdminTeam
import jp.aoichaan0513.A_TosoGame_Live.Utils.isJailTeam
import jp.aoichaan0513.A_TosoGame_Live.Utils.isPlayerGroup
import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent


class onMove : Listener {

    @EventHandler(ignoreCancelled = true)
    fun onPlayerMove(e: PlayerMoveEvent) {
        val p = e.player
        val from = e.from
        val to = e.to!!

        if (p.isAdminTeam) return

        val worldConfig = Main.worldConfig
        val mapBorder = worldConfig.mapBorderConfig
        val hunterZoneBorder = worldConfig.hunterZoneBorderConfig

        if (isBorderAttack(to, worldConfig.mapBorderConfig)) {
            e.setTo(from)
            p.sendMessage("${MainAPI.getPrefix(PrefixType.ERROR)}マップから出ることはできません。")
        }


        val fromIsIn = isJoinedArea(from, hunterZoneBorder)
        val toIsIn = isJoinedArea(to, hunterZoneBorder)

        if (fromIsIn && !toIsIn) {
            // エリアから出たとき
            if (p.isPlayerGroup) {
                HunterZone.removeJoinedSet(p)
            } else {
                e.setTo(from)
            }
        } else if (!fromIsIn && toIsIn) {
            if (p.isPlayerGroup) {
                if (MissionManager.isMissions && MissionManager.isMission(MissionManager.MissionState.HUNTER_ZONE)) {
                    if (HunterZone.joinedSetCount < 3) {
                        HunterZone.addJoinedSet(p)
                    } else {
                        e.setTo(from)
                        p.sendMessage("${MainAPI.getPrefix(PrefixType.ERROR)}4人以上は入れません。")
                    }
                } else {
                    e.setTo(from)
                    p.sendMessage("${MainAPI.getPrefix(PrefixType.ERROR)}現在入ることはできません。")
                }
            } else {
                e.setTo(from)
                p.sendMessage("${MainAPI.getPrefix(PrefixType.ERROR)}ハンター、通報部隊は入ることができません。")
            }
        }


        if (OPGameManager.opGameState != OPGameManager.OPGameState.NONE) {
            val player = OPGameManager.player
            if (player != null && p.isPlayerGroup) {
                if (OPGameManager.runnedSet.contains(p.uniqueId)) {
                    if (player.uniqueId == p.uniqueId)
                        if (from.blockX != to.blockX || from.blockY != to.blockY || from.blockZ != to.blockZ)
                            e.isCancelled = true
                } else {
                    if (isBorderAttack(to, worldConfig.opGameBorderConfig)) {
                        e.isCancelled = true
                        p.sendMessage("${MainAPI.getPrefix(PrefixType.ERROR)}ここから外に出ることはできません。")
                        TosoGameAPI.teleport(p, worldConfig.opGameLocationConfig.gOPLocations.values)
                    }
                }
            }
        }

        if (!worldConfig.gameConfig.jump) {
            if (p.isJailTeam || !p.isSprinting
                    || p.isOnGround || to.y < from.y + 0.3) return
            e.setTo(from)
            ActionBarManager.send(p, "${ChatColor.YELLOW}⚠ ダッシュジャンプはできません。")
        }
    }

    fun isBorderAttack(loc: Location, borderConfig: IBorderConfig): Boolean {
        if (!borderConfig.isLocation(BorderType.POINT_1) || !borderConfig.isLocation(BorderType.POINT_2)) return false

        val loc1 = borderConfig.getLocation(BorderType.POINT_1)
        val loc2 = borderConfig.getLocation(BorderType.POINT_2)

        return isBorderAttack(loc, loc1, loc2)
    }

    fun isBorderAttack(loc: Location, loc1: Location, loc2: Location): Boolean {
        val x1 = loc1.blockX.coerceAtMost(loc2.blockX)
        val x2 = loc1.blockX.coerceAtLeast(loc2.blockX)
        val y1 = loc1.blockY.coerceAtMost(loc2.blockY) - 1
        val y2 = loc1.blockY.coerceAtLeast(loc2.blockY) + 1
        val z1 = loc1.blockZ.coerceAtMost(loc2.blockZ)
        val z2 = loc1.blockZ.coerceAtLeast(loc2.blockZ)

        val isXAllowArea = loc.blockX in x1..x2
        val isYAllowArea = loc.blockY in y1..y2
        val isZAllowArea = loc.blockZ in z1..z2

        return ((x1 == loc.blockX || x2 == loc.blockX) && isYAllowArea && isZAllowArea)
                || ((y1 == loc.blockY || y2 == loc.blockY) && isXAllowArea && isZAllowArea)
                || ((z1 == loc.blockZ || z2 == loc.blockZ) && isXAllowArea && isYAllowArea)
    }

    fun isJoinedArea(loc: Location, borderConfig: IBorderConfig, isOutside: Boolean = false): Boolean {
        if (!borderConfig.isLocation(BorderType.POINT_1) || !borderConfig.isLocation(BorderType.POINT_2)) return false

        val loc1 = borderConfig.getLocation(BorderType.POINT_1)
        val loc2 = borderConfig.getLocation(BorderType.POINT_2)

        return isJoinedArea(loc, loc1, loc2, isOutside)
    }

    fun isJoinedArea(loc: Location, loc1: Location, loc2: Location, isOutside: Boolean = false): Boolean {
        val x1 = loc1.blockX.coerceAtMost(loc2.blockX)
        val x2 = loc1.blockX.coerceAtLeast(loc2.blockX)
        val y1 = loc1.blockY.coerceAtMost(loc2.blockY) - 1
        val y2 = loc1.blockY.coerceAtLeast(loc2.blockY) + 1
        val z1 = loc1.blockZ.coerceAtMost(loc2.blockZ)
        val z2 = loc1.blockZ.coerceAtLeast(loc2.blockZ)

        val isXJoinedArea = loc.blockX in if (isOutside) (x1 + 1) until x2 else x1..x2
        val isYJoinedArea = loc.blockY in y1..y2
        val isZJoinedArea = loc.blockZ in if (isOutside) (z1 + 1) until z2 else z1..z2

        return isXJoinedArea && isYJoinedArea && isZJoinedArea
    }
}
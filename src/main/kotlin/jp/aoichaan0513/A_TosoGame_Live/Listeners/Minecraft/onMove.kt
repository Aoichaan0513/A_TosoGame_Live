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
import jp.aoichaan0513.A_TosoGame_Live.Utils.isHunterGroup
import jp.aoichaan0513.A_TosoGame_Live.Utils.isJailTeam
import jp.aoichaan0513.A_TosoGame_Live.Utils.isPlayerGroup
import org.bukkit.Bukkit
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

        if (isBorderAttack(to, worldConfig.hunterZoneBorderConfig)) {
            if (p.isPlayerGroup) {
                if (MissionManager.isMissions) {
                    if (MissionManager.isMission(MissionManager.MissionState.HUNTER_ZONE)) {
                        Bukkit.broadcastMessage("[デバッグ] -> ${p.name} > エリア外から中へ from: ${isJoinedArea(from, hunterZoneBorder, true)}, to: ${isJoinedArea(to, hunterZoneBorder)}")
                        Bukkit.broadcastMessage("[デバッグ] -> ${p.name} > エリア内から外へ from: ${isJoinedArea(from, hunterZoneBorder)}, to: ${isJoinedArea(to, hunterZoneBorder, true)}")

                        if (isJoinedArea(from, hunterZoneBorder) && !isJoinedArea(to, hunterZoneBorder, true)) {
                            // エリアから出たとき
                            HunterZone.removeJoinedSet(p)

                            Bukkit.broadcastMessage("[デバッグ] -> ${p.name} > 人数: ${HunterZone.joinedSetCount}, エリア退出")
                        } else if (!isJoinedArea(from, hunterZoneBorder, true) && isJoinedArea(to, hunterZoneBorder)) {
                            // エリアに入ったとき
                            if (HunterZone.joinedSetCount < 3) {
                                HunterZone.addJoinedSet(p)

                                Bukkit.broadcastMessage("[デバッグ] -> ${p.name} > 人数: ${HunterZone.joinedSetCount}, エリア参加")
                            } else {
                                e.setTo(from)
                                p.sendMessage("${MainAPI.getPrefix(PrefixType.ERROR)}3人以上は入れません。")

                                Bukkit.broadcastMessage("[デバッグ] -> ${p.name} > 人数: ${HunterZone.joinedSetCount}, エリア参加 -> ブロック")
                            }

                        }
                    } else {
                        if (isJoinedArea(from, hunterZoneBorder) && !isJoinedArea(to, hunterZoneBorder, true)) {
                            HunterZone.removeJoinedSet(p)

                            Bukkit.broadcastMessage("[デバッグ] -> ${p.name} > 人数: ${HunterZone.joinedSetCount}, ボーダーアタック 2")
                        } else {
                            e.setTo(from)
                            p.sendMessage("${MainAPI.getPrefix(PrefixType.ERROR)}現在入ることはできません。2")

                            Bukkit.broadcastMessage("[デバッグ] -> ${p.name} > 人数: ${HunterZone.joinedSetCount}, 参加禁止 2")
                        }
                    }
                } else {
                    if (isJoinedArea(from, hunterZoneBorder) && !isJoinedArea(to, hunterZoneBorder, true)) {
                        HunterZone.removeJoinedSet(p)

                        Bukkit.broadcastMessage("[デバッグ] -> ${p.name} > 人数: ${HunterZone.joinedSetCount}, ボーダーアタック 1")
                    } else {
                        e.setTo(from)
                        p.sendMessage("${MainAPI.getPrefix(PrefixType.ERROR)}現在入ることはできません。1")

                        Bukkit.broadcastMessage("[デバッグ] -> ${p.name} > 人数: ${HunterZone.joinedSetCount}, 参加禁止 1")
                    }
                }
            } else if (p.isHunterGroup) {
                HunterZone.removeJoinedSet(p)
                e.setTo(from)
                p.sendMessage("${MainAPI.getPrefix(PrefixType.ERROR)}ハンター、通報部隊は入ることができません。")

                Bukkit.broadcastMessage("[デバッグ] -> ${p.name} > 人数: ${HunterZone.joinedSetCount}, ハンターアタック")
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
            ActionBarManager.sendActionBar(p, "${ChatColor.YELLOW}⚠ ダッシュジャンプはできません。")
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

        // val isXJoinedArea = loc.blockX in x1..x2
        val isXJoinedArea = loc.blockX in if (isOutside) (x1 + 1) until x2 else x1..x2
        val isYJoinedArea = loc.blockY in y1..y2
        // val isZJoinedArea = loc.blockZ in z1..z2
        val isZJoinedArea = loc.blockZ in if (isOutside) (z1 + 1) until z2 else z1..z2

        return isXJoinedArea && isYJoinedArea && isZJoinedArea
    }
}
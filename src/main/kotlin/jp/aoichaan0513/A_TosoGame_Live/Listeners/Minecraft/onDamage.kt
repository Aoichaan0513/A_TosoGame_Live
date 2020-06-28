package jp.aoichaan0513.A_TosoGame_Live.Listeners.Minecraft

import jp.aoichaan0513.A_TosoGame_Live.API.Enums.Advancement
import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI
import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI.PrefixType
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.GameManager
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.GameManager.GameState
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.MoneyManager
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.Player.PlayerManager
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.World.WorldManager.GameType
import jp.aoichaan0513.A_TosoGame_Live.API.Scoreboard.Teams
import jp.aoichaan0513.A_TosoGame_Live.API.Scoreboard.Teams.OnlineTeam
import jp.aoichaan0513.A_TosoGame_Live.API.TosoGameAPI
import jp.aoichaan0513.A_TosoGame_Live.Main
import jp.aoichaan0513.A_TosoGame_Live.Mission.HunterZone
import jp.aoichaan0513.A_TosoGame_Live.Runnable.RespawnRunnable
import org.bukkit.*
import org.bukkit.entity.*
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.ProjectileLaunchEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import java.util.*

class onDamage : Listener {
    companion object {

        val hunterMap = HashMap<UUID, Int>()
    }

    @EventHandler
    fun onEntityDamageByEntity(e: EntityDamageByEntityEvent) {
        if (e.damager is Player && e.entity is Player) {
            val damager = e.damager as Player
            val player = e.entity as Player

            val worldConfig = Main.worldConfig

            if (GameManager.isGame(GameState.GAME)) {
                if (Teams.hasJoinedTeam(OnlineTeam.TOSO_PLAYER, player) && Teams.hasJoinedTeam(OnlineTeam.TOSO_HUNTER, damager)
                        || Teams.hasJoinedTeam(OnlineTeam.TOSO_SUCCESS, player) && Teams.hasJoinedTeam(OnlineTeam.TOSO_HUNTER, damager)) {
                    if (player.hasPotionEffect(PotionEffectType.INVISIBILITY)) return

                    e.isCancelled = false

                    hunterMap[damager.uniqueId] = if (hunterMap.containsKey(damager.uniqueId)) hunterMap[damager.uniqueId]!! + 1 else 1

                    damager.sendMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}${player.name}を確保しました。")
                    player.sendMessage("""
                        ${MainAPI.getPrefix(PrefixType.SECONDARY)}あなたを牢獄に追加しました。
                        ${MainAPI.getPrefix(PrefixType.SECONDARY)}あなたは${damager.name}に確保されました。3秒後に牢獄へテレポートします。
                    """.trimIndent())

                    val playerConfig = PlayerManager.loadConfig(damager)
                    if (!playerConfig.advancementConfig.hasAdvancement(Advancement.FIRST_HUNTER_TOUCH)) {
                        playerConfig.advancementConfig.addAdvancement(Advancement.FIRST_HUNTER_TOUCH)
                        Advancement.FIRST_HUNTER_TOUCH.sendMessage(damager)
                    }

                    Teams.joinTeam(OnlineTeam.TOSO_JAIL, player)
                    player.gameMode = GameMode.ADVENTURE

                    TosoGameAPI.setItem(GameType.RESPAWN, player)
                    TosoGameAPI.setPotionEffect(player)
                    TosoGameAPI.removeOp(player)

                    HunterZone.removeJoinedSet(player)

                    TosoGameAPI.showPlayers(player)
                    TosoGameAPI.hidePlayers(player)
                    if (Main.invisibleSet.contains(player.uniqueId)) {
                        Main.invisibleSet.remove(player.uniqueId)
                        for (p in Bukkit.getOnlinePlayers())
                            p.showPlayer(player)
                    }

                    Bukkit.getScheduler().runTaskLaterAsynchronously(Main.pluginInstance, Runnable {
                        TosoGameAPI.teleport(player, worldConfig.jailLocationConfig.locations.values)

                        RespawnRunnable.setAutoTime(player)
                        RespawnRunnable.addCoolTime(player)

                        val rate = MoneyManager.getRate(player)
                        if (rate != worldConfig.getDifficultyConfig(player).rate) {
                            MoneyManager.setRate(player)
                            player.sendMessage("""
                                ${MainAPI.getPrefix(PrefixType.SECONDARY)}あなたのレートが変更されました。
                                ${MainAPI.getPrefix(PrefixType.SECONDARY)}現在のレートは${ChatColor.BOLD}${ChatColor.UNDERLINE}${MoneyManager.getRate(player)}円${ChatColor.RESET}${ChatColor.GRAY}に設定されています。
                            """.trimIndent())
                        }

                        Bukkit.broadcastMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}${player.name}が確保されました。(${ChatColor.UNDERLINE}残り${Teams.getOnlineCount(OnlineTeam.TOSO_PLAYER) + Teams.getOnlineCount(OnlineTeam.TOSO_SUCCESS)}人${ChatColor.RESET}${ChatColor.GRAY})")
                    }, 20 * 3)
                    return
                } else if (Teams.hasJoinedTeam(OnlineTeam.TOSO_PLAYER, player) && Teams.hasJoinedTeam(OnlineTeam.TOSO_TUHO, damager)
                        || Teams.hasJoinedTeam(OnlineTeam.TOSO_SUCCESS, player) && Teams.hasJoinedTeam(OnlineTeam.TOSO_TUHO, damager)) {
                    if (player.hasPotionEffect(PotionEffectType.INVISIBILITY) || player.hasPotionEffect(PotionEffectType.GLOWING)) return

                    e.isCancelled = false

                    damager.sendMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}${player.name}の位置情報を通知しました。")
                    player.sendMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}あなたの位置情報が通知されました。")

                    player.addPotionEffect(PotionEffect(PotionEffectType.GLOWING, 20 * 15, 1, false, false))
                    player.world.strikeLightningEffect(player.location)

                    val loc = player.location
                    for (str in OnlineTeam.TOSO_HUNTER.team.entries) {
                        val p = Bukkit.getPlayerExact(str) ?: continue
                        p.sendMessage("""
                            ${MainAPI.getPrefix(PrefixType.WARNING)}${player.name}の位置情報が通知されました。
                            ${MainAPI.getPrefix(PrefixType.SECONDARY)}X: ${loc.blockX}, Y: ${loc.blockY}, Z: ${loc.blockZ}
                        """.trimIndent())
                    }
                } else {
                    e.isCancelled = true
                    if (Teams.hasJoinedTeam(OnlineTeam.TOSO_PLAYER, player) && Teams.hasJoinedTeam(OnlineTeam.TOSO_ADMIN, damager)) {
                        TosoGameAPI.teleport(player, worldConfig.respawnLocationConfig.locations.values)
                    } else if (Teams.hasJoinedTeam(OnlineTeam.TOSO_SUCCESS, player) && Teams.hasJoinedTeam(OnlineTeam.TOSO_ADMIN, damager)) {
                        TosoGameAPI.teleport(player, worldConfig.respawnLocationConfig.locations.values)
                    } else if (Teams.hasJoinedTeam(OnlineTeam.TOSO_JAIL, player) && Teams.hasJoinedTeam(OnlineTeam.TOSO_ADMIN, damager)) {
                        TosoGameAPI.teleport(player, worldConfig.jailLocationConfig.locations.values)
                    } else if (Teams.hasJoinedTeam(OnlineTeam.TOSO_HUNTER, player) && Teams.hasJoinedTeam(OnlineTeam.TOSO_ADMIN, damager)) {
                        TosoGameAPI.teleport(player, worldConfig.hunterLocationConfig.getLocation(1))
                    } else if (Teams.hasJoinedTeam(OnlineTeam.TOSO_TUHO, player) && Teams.hasJoinedTeam(OnlineTeam.TOSO_ADMIN, damager)) {
                        TosoGameAPI.teleport(player, worldConfig.hunterLocationConfig.getLocation(1))
                    }
                }
            } else {
                e.isCancelled = true
                if (Teams.hasJoinedTeam(OnlineTeam.TOSO_PLAYER, player) && Teams.hasJoinedTeam(OnlineTeam.TOSO_ADMIN, damager)) {
                    TosoGameAPI.teleport(player, worldConfig.respawnLocationConfig.locations.values)
                } else if (Teams.hasJoinedTeam(OnlineTeam.TOSO_SUCCESS, player) && Teams.hasJoinedTeam(OnlineTeam.TOSO_ADMIN, damager)) {
                    TosoGameAPI.teleport(player, worldConfig.respawnLocationConfig.locations.values)
                } else if (Teams.hasJoinedTeam(OnlineTeam.TOSO_JAIL, player) && Teams.hasJoinedTeam(OnlineTeam.TOSO_ADMIN, damager)) {
                    TosoGameAPI.teleport(player, worldConfig.jailLocationConfig.locations.values)
                } else if (Teams.hasJoinedTeam(OnlineTeam.TOSO_HUNTER, player) && Teams.hasJoinedTeam(OnlineTeam.TOSO_ADMIN, damager)) {
                    TosoGameAPI.teleport(player, worldConfig.hunterLocationConfig.getLocation(1))
                } else if (Teams.hasJoinedTeam(OnlineTeam.TOSO_TUHO, player) && Teams.hasJoinedTeam(OnlineTeam.TOSO_ADMIN, damager)) {
                    TosoGameAPI.teleport(player, worldConfig.hunterLocationConfig.getLocation(1))
                }
            }
        } else if (e.damager is Zombie && e.entity is Player) {
            val player = e.entity as Player

            val worldConfig = Main.worldConfig

            if (GameManager.isGame(GameState.GAME) && (Teams.hasJoinedTeam(OnlineTeam.TOSO_PLAYER, player) || Teams.hasJoinedTeam(OnlineTeam.TOSO_SUCCESS, player))) {
                if (player.hasPotionEffect(PotionEffectType.INVISIBILITY)) return

                e.isCancelled = false

                player.sendMessage("""
                    ${MainAPI.getPrefix(PrefixType.SECONDARY)}あなたを牢獄に追加しました。
                    ${MainAPI.getPrefix(PrefixType.SECONDARY)}あなたはゾンビに確保されました。3秒後に牢獄へテレポートします。
                """.trimIndent())

                Teams.joinTeam(OnlineTeam.TOSO_JAIL, player)
                player.gameMode = GameMode.ADVENTURE

                TosoGameAPI.setItem(GameType.RESPAWN, player)
                TosoGameAPI.setPotionEffect(player)
                TosoGameAPI.removeOp(player)

                HunterZone.removeJoinedSet(player)

                TosoGameAPI.showPlayers(player)
                TosoGameAPI.hidePlayers(player)
                if (Main.invisibleSet.contains(player.uniqueId)) {
                    Main.invisibleSet.remove(player.uniqueId)
                    for (p in Bukkit.getOnlinePlayers())
                        p.showPlayer(player)
                }

                Bukkit.getScheduler().runTaskLaterAsynchronously(Main.pluginInstance, Runnable {
                    TosoGameAPI.teleport(player, worldConfig.jailLocationConfig.locations.values)
                    RespawnRunnable.setAutoTime(player)
                    RespawnRunnable.addCoolTime(player)
                    Bukkit.broadcastMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}${player.name}が確保されました。(${ChatColor.UNDERLINE}残り${Teams.getOnlineCount(OnlineTeam.TOSO_PLAYER) + Teams.getOnlineCount(OnlineTeam.TOSO_SUCCESS)}人${ChatColor.RESET}${ChatColor.GRAY})")
                }, 20 * 3)
                return
            }
        }
    }

    @EventHandler
    fun onProjectileDamage(e: EntityDamageByEntityEvent) {
        val entity = e.entity
        val damager = e.damager

        if (entity is ItemFrame) {
            if (damager is Player) {
                if (Teams.hasJoinedTeam(OnlineTeam.TOSO_ADMIN, damager)) return
                e.isCancelled = true
            }
        } else {
            if (entity is Player) {
                if (!GameManager.isGame(GameState.GAME)) return

                val p = entity

                if (damager is Egg) {
                    if (Teams.hasJoinedTeam(OnlineTeam.TOSO_HUNTER, p)) {
                        e.isCancelled = false
                        e.damage = 0.1
                        p.addPotionEffect(PotionEffect(PotionEffectType.SLOW, 20 * 5, 4, false, false))
                        p.addPotionEffect(PotionEffect(PotionEffectType.BLINDNESS, 20 * 5, 4, false, false))
                    } else {
                        e.isCancelled = true
                    }
                } else if (damager is Snowball) {
                    if (Teams.hasJoinedTeam(OnlineTeam.TOSO_HUNTER, p)) {
                        e.isCancelled = false
                        e.damage = 0.1
                        ironFence(p)
                    } else {
                        e.isCancelled = true
                    }
                }
            }
        }
    }

    @EventHandler
    fun onProjectileLaunch(e: ProjectileLaunchEvent) {
        val projectile = e.entity

        if (projectile is EnderPearl) {
            val shooter = e.entity.shooter
            if (shooter is Player)
                if (Teams.hasJoinedTeam(OnlineTeam.TOSO_JAIL, shooter))
                    e.isCancelled = true
        }
    }

    @EventHandler
    fun onEntityDamage(e: EntityDamageEvent) {
        val entity = e.entity
        val damageCause = e.cause

        if (entity is Player) {
            if (damageCause == EntityDamageEvent.DamageCause.SUICIDE) return

            val p = entity
            val damage = e.damage

            if (damageCause == EntityDamageEvent.DamageCause.FALL) {
                e.isCancelled = true

                if (GameManager.isGame(GameState.GAME) && (Teams.hasJoinedTeam(OnlineTeam.TOSO_PLAYER, p) || Teams.hasJoinedTeam(OnlineTeam.TOSO_HUNTER, p) || Teams.hasJoinedTeam(OnlineTeam.TOSO_SUCCESS, p) || Teams.hasJoinedTeam(OnlineTeam.TOSO_TUHO, p))) {
                    if (p.hasPotionEffect(PotionEffectType.SLOW))
                        p.removePotionEffect(PotionEffectType.SLOW)

                    val level = if (damage < 4) 0 else if (damage < 7) 1 else if (damage < 12) 2 else 3
                    p.addPotionEffect(PotionEffect(PotionEffectType.SLOW, 20 * 6 + 5, level, false, false))
                    p.sendMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}足を挫いてしまった！")
                }
            } else if (damageCause == EntityDamageEvent.DamageCause.VOID) {
                e.isCancelled = true

                val worldConfig = Main.worldConfig

                if (Teams.hasJoinedTeam(OnlineTeam.TOSO_PLAYER, p) || Teams.hasJoinedTeam(OnlineTeam.TOSO_SUCCESS, p)) {
                    TosoGameAPI.teleport(p, worldConfig.respawnLocationConfig.locations.values)
                } else if (Teams.hasJoinedTeam(OnlineTeam.TOSO_JAIL, p)) {
                    TosoGameAPI.teleport(p, worldConfig.jailLocationConfig.locations.values)
                } else if (Teams.hasJoinedTeam(OnlineTeam.TOSO_HUNTER, p) || Teams.hasJoinedTeam(OnlineTeam.TOSO_TUHO, p)) {
                    TosoGameAPI.teleport(p, worldConfig.hunterLocationConfig.getLocation(1))
                } else {
                    TosoGameAPI.teleport(p, worldConfig.world.spawnLocation)
                }
            } else {
                e.isCancelled = true
                return
            }
        } else if (entity is Zombie) {
            val z = entity
            val damage = e.damage

            if (damageCause == EntityDamageEvent.DamageCause.FALL) {
                e.isCancelled = true

                if (!GameManager.isGame(GameState.GAME)) return

                if (z.hasPotionEffect(PotionEffectType.SLOW))
                    z.removePotionEffect(PotionEffectType.SLOW)

                val level = if (damage < 4) 0 else if (damage < 7) 1 else if (damage < 12) 2 else 3
                z.addPotionEffect(PotionEffect(PotionEffectType.SLOW, 20 * 6 + 5, level, false, false))
                return
            }
        }
    }

    val fenceSet = mutableSetOf<UUID>()

    private fun ironFence(p: Player) {
        if (fenceSet.contains(p.uniqueId)) return

        fenceSet.add(p.uniqueId)

        val loc1 = p.location
        val loc3 = loc1.clone().add(1.0, 2.0, 0.0)
        val loc4 = loc1.clone().add(1.0, 2.0, 1.0)
        val loc5 = loc1.clone().add(0.0, 2.0, 1.0)
        val loc6 = loc1.clone().add(-1.0, 2.0, 0.0)
        val loc7 = loc1.clone().add(-1.0, 2.0, -1.0)
        val loc8 = loc1.clone().add(0.0, 2.0, -1.0)
        val loc9 = loc1.clone().add(1.0, 1.0, 0.0)
        val loc10 = loc1.clone().add(1.0, 1.0, 1.0)
        val loc11 = loc1.clone().add(0.0, 1.0, 1.0)
        val loc12 = loc1.clone().add(-1.0, 1.0, 0.0)
        val loc13 = loc1.clone().add(-1.0, 1.0, -1.0)
        val loc14 = loc1.clone().add(0.0, 1.0, -1.0)
        val loc15 = loc1.clone().add(1.0, 0.0, 0.0)
        val loc16 = loc1.clone().add(1.0, 0.0, 1.0)
        val loc17 = loc1.clone().add(0.0, 0.0, 1.0)
        val loc18 = loc1.clone().add(-1.0, 0.0, 0.0)
        val loc19 = loc1.clone().add(-1.0, 0.0, -1.0)
        val loc20 = loc1.clone().add(0.0, 0.0, -1.0)
        val loc21 = loc1.clone().add(1.0, 0.0, -1.0)
        val loc22 = loc1.clone().add(-1.0, 0.0, 1.0)
        val loc23 = loc1.clone().add(1.0, 1.0, -1.0)
        val loc24 = loc1.clone().add(-1.0, 1.0, 1.0)
        val loc25 = loc1.clone().add(1.0, 2.0, -1.0)
        val loc26 = loc1.clone().add(-1.0, 2.0, 1.0)

        val llist = mutableListOf<Location>()
        llist.add(loc3)
        llist.add(loc4)
        llist.add(loc5)
        llist.add(loc6)
        llist.add(loc7)
        llist.add(loc8)
        llist.add(loc9)
        llist.add(loc10)
        llist.add(loc11)
        llist.add(loc12)
        llist.add(loc13)
        llist.add(loc14)
        llist.add(loc15)
        llist.add(loc16)
        llist.add(loc17)
        llist.add(loc18)
        llist.add(loc19)
        llist.add(loc20)
        llist.add(loc21)
        llist.add(loc22)
        llist.add(loc23)
        llist.add(loc24)
        llist.add(loc25)
        llist.add(loc26)

        val rlist = mutableListOf<Location>()
        for (l in llist) {
            val b = l.block
            if (b.type == Material.AIR) {
                rlist.add(l)
                b.type = Material.IRON_BARS
            }
        }

        val loc2 = loc1.clone().add(0.0, 2.0, 0.0)
        val b2 = loc2.block
        if (b2.type == Material.AIR) {
            rlist.add(loc2)
            b2.type = Material.OBSIDIAN
        }

        Bukkit.getScheduler().runTaskLater(Main.pluginInstance, Runnable {
            for (l in rlist) {
                val b = l.block
                b.type = Material.AIR
            }
            fenceSet.remove(p.uniqueId)
        }, 20 * 20)
    }
}
package jp.aoichaan0513.A_TosoGame_Live.Listeners.Minecraft

import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.GameManager
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.MoneyManager
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.Player.VisibilityManager
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.World.WorldConfig
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.World.WorldManager
import jp.aoichaan0513.A_TosoGame_Live.API.Scoreboard.Teams
import jp.aoichaan0513.A_TosoGame_Live.API.TosoGameAPI
import jp.aoichaan0513.A_TosoGame_Live.Inventory.MainInventory
import jp.aoichaan0513.A_TosoGame_Live.Inventory.MissionInventory
import jp.aoichaan0513.A_TosoGame_Live.Main
import jp.aoichaan0513.A_TosoGame_Live.Mission.HunterZone
import jp.aoichaan0513.A_TosoGame_Live.Mission.MissionManager
import jp.aoichaan0513.A_TosoGame_Live.Mission.TimedDevice
import jp.aoichaan0513.A_TosoGame_Live.Runnable.RespawnRunnable
import jp.aoichaan0513.A_TosoGame_Live.Utils.*

import net.wesjd.anvilgui.AnvilGUI
import org.bukkit.*
import org.bukkit.block.BlockFace
import org.bukkit.block.Sign
import org.bukkit.block.data.Bisected
import org.bukkit.block.data.Directional
import org.bukkit.block.data.FaceAttachable
import org.bukkit.block.data.type.Door
import org.bukkit.block.data.type.Stairs
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftArrow
import org.bukkit.entity.ItemFrame
import org.bukkit.entity.Player
import org.bukkit.entity.Zombie
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerAnimationEvent
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.Vector
import java.util.function.BiFunction


class onInteract : Listener {

    companion object {
        var successBlockLoc: Location? = null
        var hunterZoneBlockLoc: Location? = null
    }

    @EventHandler
    fun onInteract(e: PlayerInteractEvent) {
        val p = e.player
        val clickedBlock = e.clickedBlock

        val worldConfig = Main.worldConfig
        val difficultyConfig = worldConfig.getDifficultyConfig(p)

        val itemGameType = RespawnRunnable.getGameType(p)

        if (!p.isAdminTeam) {
            if (clickedBlock != null && (clickedBlock.type.name.startsWith("POTTED_") || clickedBlock.type == Material.FLOWER_POT)) {
                e.isCancelled = true
                return
            }
        }

        if (e.action == Action.PHYSICAL) {
            if (!p.isJailTeam || clickedBlock == null) return
            if (clickedBlock.location.clone().add(0.0, -1.0, 0.0).block.type == Material.IRON_BLOCK && clickedBlock.type == Material.STONE_PRESSURE_PLATE) {
                if (GameManager.isGame(GameManager.GameState.GAME)) {
                    if (TosoGameAPI.isRespawn) {
                        if (RespawnRunnable.isAllowRespawn(p)) {
                            if (!RespawnRunnable.isCoolTime(p)) {
                                Teams.joinTeam(Teams.OnlineTeam.TOSO_PLAYER, p)
                                p.gameMode = GameMode.ADVENTURE

                                TosoGameAPI.setItem(WorldManager.GameType.RESPAWN, p)
                                TosoGameAPI.removeOp(p)

                                TosoGameAPI.teleport(p, worldConfig.respawnLocationConfig.locations.values)

                                p.addPotionEffect(PotionEffect(PotionEffectType.NIGHT_VISION, 200000, 1, false, false))
                                p.addPotionEffect(PotionEffect(PotionEffectType.INVISIBILITY, 20 * 15, 1, false, false))
                                p.addPotionEffect(PotionEffect(PotionEffectType.SPEED, 20 * 15, 1, false, false))

                                TosoGameAPI.hidePlayer(p)

                                p.sendMessage("""
                                    ${MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY)}あなたを逃走者に追加しました。
                                    ${MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY)}あなたはあと${difficultyConfig.respawnDenyCount - RespawnRunnable.getCount(p)}回復活できます。
                                """.trimIndent())
                                Bukkit.broadcastMessage("${MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY)}${p.name}が復活しました。(${ChatColor.UNDERLINE}残り${Teams.getOnlineCount(Teams.OnlineTeam.TOSO_PLAYER) + Teams.getOnlineCount(Teams.OnlineTeam.TOSO_SUCCESS)}人${ChatColor.RESET}${ChatColor.GRAY})")
                                return
                            } else {
                                // TosoGameAPI.teleport(p, worldConfig.jailLocationConfig.locations.values)
                                p.gameMode = GameMode.ADVENTURE
                                p.sendMessage("${MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY)}あなたは${ChatColor.RED}${ChatColor.BOLD}${ChatColor.UNDERLINE}復活クールタイム中${ChatColor.RESET}${ChatColor.GRAY}のため復活できません。")
                                return
                            }
                        } else {
                            // TosoGameAPI.teleport(p, worldConfig.jailLocationConfig.locations.values)
                            p.gameMode = GameMode.ADVENTURE
                            p.sendMessage("${MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY)}あなたは${ChatColor.RED}${ChatColor.BOLD}${ChatColor.UNDERLINE}${difficultyConfig.respawnDenyCount}回復活${ChatColor.RESET}${ChatColor.GRAY}したためこれ以上は復活できません。")
                            return
                        }
                    } else {
                        // TosoGameAPI.teleport(p, worldConfig.jailLocationConfig.locations.values)
                        p.gameMode = GameMode.ADVENTURE
                        p.sendMessage("${MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY)}ゲーム終了まで残り${ChatColor.RED}${ChatColor.BOLD}${ChatColor.UNDERLINE}${DateTimeUtil.formatTimestamp(worldConfig.gameConfig.respawnDeny).toJapan}以下${ChatColor.RESET}${ChatColor.GRAY}になったため復活できません。")
                        return
                    }
                }
            }
        } else {
            if (p.inventory.itemInMainHand.type == Material.BOOK && (p.inventory.itemInOffHand.type == Material.AIR || p.inventory.itemInOffHand.type == Material.FILLED_MAP)
                    || p.inventory.itemInOffHand.type == Material.BOOK && (p.inventory.itemInMainHand.type == Material.AIR || p.inventory.itemInMainHand.type == Material.FILLED_MAP)) {
                if (p.gameMode == GameMode.SPECTATOR) return
                if (e.action == Action.LEFT_CLICK_AIR || e.action == Action.LEFT_CLICK_BLOCK) {
                    if (p.inventory.itemInMainHand.type == Material.BOOK) {
                        val itemMeta = p.inventory.itemInMainHand.itemMeta
                        if (itemMeta == null || ChatColor.stripColor(itemMeta.displayName) != Main.PHONE_ITEM_NAME) return

                        e.isCancelled = true
                        p.openInventory(MainInventory.getInventory(p))
                    } else {
                        val itemMeta = p.inventory.itemInOffHand.itemMeta
                        if (itemMeta == null || ChatColor.stripColor(itemMeta.displayName) != Main.PHONE_ITEM_NAME) return

                        e.isCancelled = true
                        p.openInventory(MainInventory.getInventory(p))
                    }
                } else if (e.action == Action.RIGHT_CLICK_AIR || e.action == Action.RIGHT_CLICK_BLOCK) {
                    if (p.inventory.itemInMainHand.type == Material.BOOK) {
                        val itemMeta = p.inventory.itemInMainHand.itemMeta
                        if (itemMeta == null || ChatColor.stripColor(itemMeta.displayName) != Main.PHONE_ITEM_NAME) return

                        e.isCancelled = true
                        p.openInventory(MissionInventory.getInventory(MissionManager.MissionType.MISSION))
                    } else {
                        val itemMeta = p.inventory.itemInOffHand.itemMeta
                        if (itemMeta == null || ChatColor.stripColor(itemMeta.displayName) != Main.PHONE_ITEM_NAME) return

                        e.isCancelled = true
                        p.openInventory(MissionInventory.getInventory(MissionManager.MissionType.MISSION))
                    }
                }
            } else {
                when (e.action) {
                    Action.LEFT_CLICK_BLOCK -> {
                        if (p.isAdminTeam) {
                            if (GameManager.isGame() || clickedBlock == null) return
                            if (p.inventory.itemInMainHand.type == Material.DIAMOND_AXE || p.inventory.itemInOffHand.type == Material.DIAMOND_AXE) {
                                e.isCancelled = true
                                worldConfig.mapBorderConfig.setLocation(WorldConfig.BorderType.POINT_1, clickedBlock.location)
                                p.sendMessage("""
                                    ${MainAPI.getPrefix(MainAPI.PrefixType.WARNING)}マップのボーダーの角1を設定しました。
                                    ${MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY)}${clickedBlock.x}, ${clickedBlock.y}, ${clickedBlock.z}
                                """.trimIndent())
                                return
                            } else if (p.inventory.itemInMainHand.type == Material.GOLDEN_AXE || p.inventory.itemInOffHand.type == Material.GOLDEN_AXE) {
                                e.isCancelled = true
                                worldConfig.hunterZoneBorderConfig.setLocation(WorldConfig.BorderType.POINT_1, clickedBlock.location)
                                p.sendMessage("""
                                    ${MainAPI.getPrefix(MainAPI.PrefixType.WARNING)}ハンターゾーンのボーダーの角1を設定しました。
                                    ${MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY)}${clickedBlock.x}, ${clickedBlock.y}, ${clickedBlock.z}
                                """.trimIndent())
                                return
                            } else if (p.inventory.itemInMainHand.type == Material.IRON_AXE || p.inventory.itemInOffHand.type == Material.IRON_AXE) {
                                e.isCancelled = true
                                worldConfig.opGameBorderConfig.setLocation(WorldConfig.BorderType.POINT_1, clickedBlock.location)
                                p.sendMessage("""
                                    ${MainAPI.getPrefix(MainAPI.PrefixType.WARNING)}オープニングゲームのボーダーの角1を設定しました。
                                    ${MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY)}${clickedBlock.x}, ${clickedBlock.y}, ${clickedBlock.z}
                                """.trimIndent())
                                return
                            } else if (p.inventory.itemInMainHand.type == Material.STONE_AXE || p.inventory.itemInOffHand.type == Material.STONE_AXE) {
                                if (clickedBlock.type != Material.IRON_DOOR || (clickedBlock.blockData as Door).half != Bisected.Half.BOTTOM) return

                                e.isCancelled = true

                                AnvilGUI(Main.pluginInstance, p, "整数を入力…", BiFunction { player, reply ->
                                    if (ParseUtil.isInt(reply)) {
                                        val i = reply.toInt()
                                        worldConfig.hunterDoorConfig.setDoor(i, clickedBlock)
                                        p.sendMessage("${MainAPI.getPrefix(MainAPI.PrefixType.WARNING)}ハンターボックスのドア位置${i}を設定しました。")
                                    } else {
                                        MainAPI.sendMessage(player, MainAPI.ErrorMessage.ARGS_INTEGER)
                                    }
                                    null
                                })
                                return
                            }
                        } else if (p.isPlayerGroup) {
                            if (p.inventory.itemInMainHand.type == Material.BONE) {
                                bone(p, true, difficultyConfig, itemGameType)
                            } else if (p.inventory.itemInOffHand.type == Material.BONE) {
                                bone(p, false, difficultyConfig, itemGameType)
                            } else if (p.inventory.itemInMainHand.type == Material.FEATHER) {
                                feather(p, true, difficultyConfig, itemGameType)
                            } else if (p.inventory.itemInOffHand.type == Material.FEATHER) {
                                feather(p, false, difficultyConfig, itemGameType)
                            }
                        }
                    }
                    Action.RIGHT_CLICK_BLOCK -> {
                        if (clickedBlock != null && p.gameMode != GameMode.SPECTATOR && p.inventory.itemInMainHand.type == Material.AIR
                                && clickedBlock.blockData is Stairs && (clickedBlock.blockData as Stairs).half == Bisected.Half.BOTTOM
                                && clickedBlock.location.clone().add(0.0, -1.0, 0.0).block.type == Material.BEDROCK) {
                            e.isCancelled = true

                            val loc = clickedBlock.location.clone().add(0.5, 0.0, 0.5)

                            if (Main.arrowSet.any { it.location.blockX == loc.blockX && it.location.blockY == loc.blockY && it.location.blockZ == loc.blockZ }) return

                            val arrow = p.world.spawnArrow(loc, Vector(0, 1, 0), 1.toFloat(), 0.toFloat())
                            arrow.setGravity(false)
                            arrow.setPassenger(p)
                            Main.arrowSet.add(arrow)
                            object : BukkitRunnable() {

                                override fun run() {
                                    val entityArrow = (arrow as CraftArrow).handle
                                    if (entityArrow.dead) cancel()
                                    entityArrow.despawnCounter = 0
                                }
                            }.runTaskTimer(Main.pluginInstance, 0, 0)
                        } else {
                            if (p.isAdminTeam) {
                                if (clickedBlock == null) return
                                if (p.inventory.itemInMainHand.type == Material.DIAMOND_AXE || p.inventory.itemInOffHand.type == Material.DIAMOND_AXE) {
                                    if (GameManager.isGame()) return

                                    e.isCancelled = true
                                    worldConfig.mapBorderConfig.setLocation(WorldConfig.BorderType.POINT_2, clickedBlock.location)
                                    p.sendMessage("""
                                        ${MainAPI.getPrefix(MainAPI.PrefixType.WARNING)}マップのボーダーの角2を設定しました。
                                        ${MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY)}${clickedBlock.x}, ${clickedBlock.y}, ${clickedBlock.z}
                                    """.trimIndent())
                                    return
                                } else if (p.inventory.itemInMainHand.type == Material.GOLDEN_AXE || p.inventory.itemInOffHand.type == Material.GOLDEN_AXE) {
                                    if (GameManager.isGame()) return

                                    e.isCancelled = true
                                    worldConfig.hunterZoneBorderConfig.setLocation(WorldConfig.BorderType.POINT_2, clickedBlock.location)
                                    p.sendMessage("""
                                        ${MainAPI.getPrefix(MainAPI.PrefixType.WARNING)}ハンターゾーンのボーダーの角2を設定しました。
                                        ${MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY)}${clickedBlock.x}, ${clickedBlock.y}, ${clickedBlock.z}
                                    """.trimIndent())
                                    return
                                } else if (p.inventory.itemInMainHand.type == Material.IRON_AXE || p.inventory.itemInOffHand.type == Material.IRON_AXE) {
                                    if (GameManager.isGame()) return

                                    e.isCancelled = true
                                    worldConfig.opGameBorderConfig.setLocation(WorldConfig.BorderType.POINT_2, clickedBlock.location)
                                    p.sendMessage("""
                                        ${MainAPI.getPrefix(MainAPI.PrefixType.WARNING)}オープニングゲームのボーダーの角2を設定しました。
                                        ${MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY)}${clickedBlock.x}, ${clickedBlock.y}, ${clickedBlock.z}
                                    """.trimIndent())
                                    return
                                } else {
                                    if (clickedBlock.type == Material.OAK_BUTTON || clickedBlock.type == Material.SPRUCE_BUTTON
                                            || clickedBlock.type == Material.BIRCH_BUTTON || clickedBlock.type == Material.JUNGLE_BUTTON
                                            || clickedBlock.type == Material.ACACIA_BUTTON || clickedBlock.type == Material.DARK_OAK_BUTTON
                                            || clickedBlock.type == Material.STONE_BUTTON) {
                                        val directional = clickedBlock.blockData as Directional
                                        val block = clickedBlock.getRelative(getAttachmentFace(directional))

                                        if (block.type == Material.EMERALD_BLOCK) {
                                            val missionState = MissionManager.MissionState.SUCCESS

                                            if (GameManager.isGame(GameManager.GameState.GAME) && TosoGameAPI.isAdmin(p)) {
                                                if (worldConfig.gameConfig.successMission) {
                                                    successBlockLoc = block.location

                                                    if (!MissionManager.isMission(missionState)) {
                                                        MissionManager.sendMission(p, missionState)
                                                    } else {
                                                        p.sendMessage("${MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY)}生存ミッションで使用するブロックの位置を変更しました。")
                                                        MissionManager.sendMission(p, -1, "生存ミッションで使用されるブロックの位置が変更された。", MissionManager.MissionType.TUTATU_HINT, MissionManager.MissionDetailType.TUTATU, Material.EMERALD_BLOCK)
                                                    }
                                                } else {
                                                    p.sendMessage("${MainAPI.getPrefix(MainAPI.PrefixType.ERROR)}生存ミッションが有効になっていないため開始できません。")
                                                }
                                            }
                                        } else if (block.type == Material.BONE_BLOCK) {
                                            val missionState = MissionManager.MissionState.HUNTER_ZONE

                                            if (GameManager.isGame(GameManager.GameState.GAME) && TosoGameAPI.isAdmin(p)) {
                                                hunterZoneBlockLoc = block.location

                                                if (!MissionManager.isMission(missionState)) {
                                                    MissionManager.sendMission(p, missionState)
                                                } else {
                                                    p.sendMessage("${MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY)}ハンターゾーンミッションで使用するブロックの位置を変更しました。")
                                                    MissionManager.sendMission(p, -1, "ハンターゾーンミッションで使用されるブロックの位置が変更された。", MissionManager.MissionType.TUTATU_HINT, MissionManager.MissionDetailType.TUTATU, Material.BONE_BLOCK)
                                                }
                                            }
                                        }
                                    }
                                    /*
                                    else if (clickedBlock.type == Material.CHEST) {
                                        e.isCancelled = true

                                        val loc = clickedBlock.location
                                        val world = (loc.world as CraftWorld).handle
                                        val position = BlockPosition(loc.x, loc.y, loc.z)
                                        val tileChest = world.getTileEntity(position) as TileEntityChest

                                        world.playBlockAction(position, world.getType(position).block, 1, 1)
                                    }
                                    */
                                }
                            } else if (p.isPlayerGroup) {
                                if (p.inventory.itemInMainHand.type == Material.BONE) {
                                    bone(p, true, difficultyConfig, itemGameType)
                                } else if (p.inventory.itemInOffHand.type == Material.BONE) {
                                    bone(p, false, difficultyConfig, itemGameType)
                                } else if (p.inventory.itemInMainHand.type == Material.FEATHER) {
                                    feather(p, true, difficultyConfig, itemGameType)
                                } else if (p.inventory.itemInOffHand.type == Material.FEATHER) {
                                    feather(p, false, difficultyConfig, itemGameType)
                                } else {
                                    if (clickedBlock == null) return

                                    if (p.inventory.itemInMainHand.type == Material.AIR || p.inventory.itemInOffHand.type == Material.AIR) {
                                        if (clickedBlock.type == Material.OAK_SIGN || clickedBlock.type == Material.SPRUCE_SIGN
                                                || clickedBlock.type == Material.BIRCH_SIGN || clickedBlock.type == Material.JUNGLE_SIGN
                                                || clickedBlock.type == Material.ACACIA_SIGN || clickedBlock.type == Material.DARK_OAK_SIGN
                                                || clickedBlock.type == Material.OAK_WALL_SIGN || clickedBlock.type == Material.SPRUCE_WALL_SIGN
                                                || clickedBlock.type == Material.BIRCH_WALL_SIGN || clickedBlock.type == Material.JUNGLE_WALL_SIGN
                                                || clickedBlock.type == Material.ACACIA_WALL_SIGN || clickedBlock.type == Material.DARK_OAK_WALL_SIGN) {
                                            val sign = clickedBlock.state as Sign

                                            if (sign.getLine(0).equals(MainAPI.prefix, true) && ChatColor.stripColor(sign.getLine(1)).equals("自首", true)
                                                    && ChatColor.stripColor(sign.getLine(3)).equals("クリック", true)) {
                                                if (!p.isPlayerTeam) return

                                                TosoGameAPI.teleport(p, worldConfig.respawnLocationConfig.locations.values)

                                                Teams.joinTeam(Teams.OnlineTeam.TOSO_SUCCESS, p)
                                                p.gameMode = GameMode.SPECTATOR

                                                p.inventory.clear()

                                                p.sendMessage("${MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY)}あなたを生存者 (自首)に追加しました。")
                                                Bukkit.broadcastMessage("${MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY)}${p.name}が自首しました。")
                                                return
                                            }
                                        } else if (clickedBlock.type == Material.OAK_BUTTON || clickedBlock.type == Material.SPRUCE_BUTTON
                                                || clickedBlock.type == Material.BIRCH_BUTTON || clickedBlock.type == Material.JUNGLE_BUTTON
                                                || clickedBlock.type == Material.ACACIA_BUTTON || clickedBlock.type == Material.DARK_OAK_BUTTON
                                                || clickedBlock.type == Material.STONE_BUTTON) {
                                            if (!GameManager.isGame(GameManager.GameState.GAME)) return

                                            val directional = clickedBlock.blockData as Directional
                                            val block = clickedBlock.getRelative(getAttachmentFace(directional))

                                            if (block.type == Material.EMERALD_BLOCK) {
                                                if (!p.isPlayerTeam || !MissionManager.isMission(MissionManager.MissionState.SUCCESS) || successBlockLoc == null) return
                                                if (block.location.blockX == successBlockLoc!!.blockX && block.location.blockY == successBlockLoc!!.blockY && block.location.blockZ == successBlockLoc!!.blockZ) {
                                                    Teams.joinTeam(Teams.OnlineTeam.TOSO_SUCCESS, p)
                                                    p.playSound(p.location, Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f)
                                                    p.sendMessage("""
                                                        ${MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY)}あなたを${ChatColor.BLUE}${ChatColor.UNDERLINE}生存者${ChatColor.RESET}${ChatColor.GRAY}に追加しました。
                                                        ${MainAPI.getPrefix(MainAPI.PrefixType.WARNING)}サーバーから退出した場合は逃走者になります。
                                                    """.trimIndent())
                                                }
                                            } else if (block.type == Material.BONE_BLOCK) {
                                                if (!MissionManager.isMission(MissionManager.MissionState.HUNTER_ZONE) || hunterZoneBlockLoc == null) return
                                                if (block.location.blockX == hunterZoneBlockLoc!!.blockX && block.location.blockY == hunterZoneBlockLoc!!.blockY && block.location.blockZ == hunterZoneBlockLoc!!.blockZ) {

                                                    p.sendMessage("""
                                                        ${MainAPI.getPrefix(MainAPI.PrefixType.WARNING)}ハンターゾーンミッションのコード: ${HunterZone.code}
                                                        ${MainAPI.getPrefix(MainAPI.PrefixType.WARNING)}"/code ${HunterZone.code}"と入力してミッションを完了してください。
                                                        ${MainAPI.getPrefix(MainAPI.PrefixType.WARNING)}このコードを他の逃走者に教えるかどうかはあなた次第です。
                                                    """.trimIndent())
                                                }
                                            }
                                        }
                                    }
                                    /*
                                    else if (p.inventory.itemInMainHand.type == Material.STONE_PRESSURE_PLATE || p.inventory.itemInOffHand.type == Material.STONE_PRESSURE_PLATE
                                            && clickedBlock.type == Material.GOLD_BLOCK) {
                                        p.sendMessage(clickedBlock.type.toString())

                                        if (!MissionManager.isMission(MissionManager.MissionState.AREA_EXTEND)) return

                                        val loc = clickedBlock.location.clone()
                                        loc.add(0.0, 1.0, 0.0)
                                        loc.block.type = Material.STONE_PRESSURE_PLATE

                                        val inv = p.inventory
                                        inv.removeItem(ItemStack(Material.STONE_PRESSURE_PLATE, 1))
                                    }
                                    */
                                }
                                return
                            } else if (p.isJailTeam) {
                                if (p.inventory.itemInMainHand.type == Material.ENDER_PEARL) {
                                    e.isCancelled = true
                                    enderPearl(p, true)
                                } else if (p.inventory.itemInOffHand.type == Material.ENDER_PEARL) {
                                    e.isCancelled = true
                                    enderPearl(p, false)
                                } else if (p.inventory.itemInMainHand.type == Material.ENDER_EYE) {
                                    e.isCancelled = true
                                    enderEye(p, true)
                                } else if (p.inventory.itemInOffHand.type == Material.ENDER_EYE) {
                                    e.isCancelled = true
                                    enderEye(p, false)
                                }
                            }
                            return
                        }
                    }
                    Action.LEFT_CLICK_AIR, Action.RIGHT_CLICK_AIR -> {
                        if (p.isPlayerGroup) {
                            if (p.inventory.itemInMainHand.type == Material.BONE) {
                                bone(p, true, difficultyConfig, itemGameType)
                            } else if (p.inventory.itemInOffHand.type == Material.BONE) {
                                bone(p, false, difficultyConfig, itemGameType)
                            } else if (p.inventory.itemInMainHand.type == Material.FEATHER) {
                                feather(p, true, difficultyConfig, itemGameType)
                            } else if (p.inventory.itemInOffHand.type == Material.FEATHER) {
                                feather(p, false, difficultyConfig, itemGameType)
                            }
                            return
                        } else if (p.isJailTeam) {
                            if (p.inventory.itemInMainHand.type == Material.ENDER_PEARL) {
                                e.isCancelled = true
                                enderPearl(p, true)
                            } else if (p.inventory.itemInOffHand.type == Material.ENDER_PEARL) {
                                e.isCancelled = true
                                enderPearl(p, false)
                            } else if (p.inventory.itemInMainHand.type == Material.ENDER_EYE) {
                                e.isCancelled = true
                                enderEye(p, true)
                            } else if (p.inventory.itemInOffHand.type == Material.ENDER_EYE) {
                                e.isCancelled = true
                                enderEye(p, false)
                            }
                            return
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    fun onInteractEntity(e: PlayerInteractEntityEvent) {
        val damager = e.player

        val worldConfig = Main.worldConfig

        if (e.rightClicked is ItemFrame) {
            if (damager.isAdminTeam) return

            e.isCancelled = true
        } else if (e.rightClicked is Player) {
            val player = e.rightClicked as Player

            if (damager.isAdminTeam) {
                val difficultyConfig = worldConfig.getDifficultyConfig(player)
                if (damager.inventory.itemInMainHand.type == Material.GOLD_NUGGET || damager.inventory.itemInOffHand.type == Material.GOLD_NUGGET) {
                    e.isCancelled = true
                    damager.sendMessage("""
                        ${MainAPI.getPrefix(MainAPI.PrefixType.SUCCESS)}${player.name}の情報
                        ${MainAPI.getPrefix(MainAPI.PrefixType.PRIMARY)}${ChatColor.UNDERLINE}権限所持者${ChatColor.GRAY}: ${if (TosoGameAPI.hasPermission(player)) "はい" else "いいえ"}
                        ${MainAPI.getPrefix(MainAPI.PrefixType.PRIMARY)}${ChatColor.UNDERLINE}配信者${ChatColor.GRAY}: ${if (TosoGameAPI.isBroadCaster(player)) "はい" else "いいえ"}
                        ${MainAPI.getPrefix(MainAPI.PrefixType.PRIMARY)}${ChatColor.UNDERLINE}チーム${ChatColor.RESET}${ChatColor.GRAY}: ${ChatColor.YELLOW}${Teams.getTeamLabel(Teams.DisplaySlot.SIDEBAR, player)}
                        ${MainAPI.getPrefix(MainAPI.PrefixType.PRIMARY)}${ChatColor.UNDERLINE}難易度${ChatColor.RESET}${ChatColor.GRAY}: ${ChatColor.YELLOW}${difficultyConfig.difficulty.displayName}
                        ${MainAPI.getPrefix(MainAPI.PrefixType.PRIMARY)}${ChatColor.UNDERLINE}賞金${ChatColor.RESET}${ChatColor.GRAY}: ${ChatColor.YELLOW}${MoneyManager.getReward(player)}${ChatColor.GRAY}円 (${MoneyManager.getRate(player)}円/秒)
                    """.trimIndent())
                }
            } else if (damager.isPlayerGroup && player.isPlayerGroup
                    && (damager.inventory.itemInMainHand.type == Material.PAPER || damager.inventory.itemInOffHand.type == Material.PAPER)) {
                e.isCancelled = true

                if (player.hasPotionEffect(PotionEffectType.INVISIBILITY) || !TimedDevice.isStart) return

                val itemType = Material.PAPER

                val itemStackDamager = if (damager.inventory.itemInMainHand.type == itemType) damager.inventory.itemInMainHand
                else damager.inventory.itemInOffHand

                val itemStackPlayer = player.inventory.contents.firstOrNull { it != null && it.type == itemType }
                        ?: return

                val itemMetaDamager = itemStackDamager.itemMeta!!
                val itemMetaPlayer = itemStackPlayer.itemMeta!!

                if (!itemMetaDamager.hasCustomModelData() || !itemMetaPlayer.hasCustomModelData()) return

                if (itemMetaDamager.customModelData == itemMetaPlayer.customModelData) {
                    damager.inventory.remove(itemStackDamager)
                    player.inventory.remove(itemStackPlayer)

                    TimedDevice.addClearedNumberSet(damager)

                    damager.playSound(damager.location, Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f)
                    player.playSound(damager.location, Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f)

                    damager.sendMessage("${MainAPI.getPrefix(MainAPI.PrefixType.SUCCESS)}同じ番号のカードキーを所持していたため${ChatColor.BOLD}${ChatColor.UNDERLINE}${player.name}${ChatColor.RESET}${ChatColor.GREEN}との認証に成功しました。")
                    player.sendMessage("${MainAPI.getPrefix(MainAPI.PrefixType.SUCCESS)}同じ番号のカードキーを所持していたため${ChatColor.BOLD}${ChatColor.UNDERLINE}${damager.name}${ChatColor.RESET}${ChatColor.GREEN}との認証に成功しました。")
                } else {
                    damager.sendMessage("${MainAPI.getPrefix(MainAPI.PrefixType.ERROR)}同じ番号のカードキーを所持していないため認証に失敗しました。")
                }
            }
        }
    }

    @EventHandler
    fun onAnimation(e: PlayerAnimationEvent) {
        val p = e.player

        val blocks = p.getLineOfSight(Material.values().toSet(), 5).filterNotNull()
        val block = blocks.firstOrNull { !it.type.toString().contains("AIR", true) } ?: return

        val worldConfig = Main.worldConfig
        val difficultyConfig = worldConfig.getDifficultyConfig(p)

        val itemGameType = RespawnRunnable.getGameType(p)

        if (p.isPlayerGroup) {
            if (p.inventory.itemInMainHand.type == Material.BONE) {
                bone(p, true, difficultyConfig, itemGameType)
            } else if (p.inventory.itemInOffHand.type == Material.BONE) {
                bone(p, false, difficultyConfig, itemGameType)
            } else if (p.inventory.itemInMainHand.type == Material.FEATHER) {
                feather(p, true, difficultyConfig, itemGameType)
            } else if (p.inventory.itemInOffHand.type == Material.FEATHER) {
                feather(p, false, difficultyConfig, itemGameType)
            }
        } else if (p.isJailTeam) {
            if (p.inventory.itemInMainHand.type == Material.ENDER_PEARL) {
                enderPearl(p, true)
            } else if (p.inventory.itemInOffHand.type == Material.ENDER_PEARL) {
                enderPearl(p, false)
            } else if (p.inventory.itemInMainHand.type == Material.ENDER_EYE) {
                enderEye(p, true)
            } else if (p.inventory.itemInOffHand.type == Material.ENDER_EYE) {
                enderEye(p, false)
            }
        }
    }


    private fun getAttachmentFace(directional: Directional): BlockFace {
        return when ((directional as FaceAttachable).attachedFace) {
            FaceAttachable.AttachedFace.CEILING -> BlockFace.UP
            FaceAttachable.AttachedFace.WALL -> directional.facing.oppositeFace
            FaceAttachable.AttachedFace.FLOOR -> BlockFace.DOWN
        }
    }

    private fun bone(p: Player, isMainHand: Boolean, difficultyConfig: WorldConfig.DifficultyConfig, itemGameType: WorldManager.GameType) {
        if (!GameManager.isGame(GameManager.GameState.GAME) || p.hasPotionEffect(PotionEffectType.INVISIBILITY) || p.hasCooldown(Material.BONE)) return

        val duration = 20 * difficultyConfig.getBone(itemGameType).duration

        p.setCooldown(Material.BONE, duration + 20 * 5)

        if (isMainHand) {
            if (p.inventory.itemInMainHand.amount == 1) {
                p.inventory.setItemInMainHand(null)
            } else {
                p.inventory.itemInMainHand.amount = p.inventory.itemInMainHand.amount - 1
                p.inventory.setItemInMainHand(p.inventory.itemInMainHand)
            }
        } else {
            if (p.inventory.itemInOffHand.amount == 1) {
                p.inventory.setItemInOffHand(null)
            } else {
                p.inventory.itemInOffHand.amount = p.inventory.itemInOffHand.amount - 1
                p.inventory.setItemInOffHand(p.inventory.itemInOffHand)
            }
        }

        p.playSound(p.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f)
        p.addPotionEffect(PotionEffect(PotionEffectType.INVISIBILITY, duration, 1, false, false))

        Main.worldConfig.world.entities
                .filter { it is Zombie && it.target is Player && it.target?.uniqueId == p.uniqueId }
                .forEach { (it as Zombie).target = null }

        TosoGameAPI.hidePlayer(p, duration.toLong())
    }

    private fun feather(p: Player, isMainHand: Boolean, difficultyConfig: WorldConfig.DifficultyConfig, itemGameType: WorldManager.GameType) {
        if (!GameManager.isGame(GameManager.GameState.GAME) || p.hasPotionEffect(PotionEffectType.SPEED) || p.hasCooldown(Material.FEATHER)) return

        val duration = 20 * difficultyConfig.getFeather(itemGameType).duration

        p.setCooldown(Material.FEATHER, duration + 20 * 5)

        if (isMainHand) {
            if (p.inventory.itemInMainHand.amount == 1) {
                p.inventory.setItemInMainHand(null)
            } else {
                p.inventory.itemInMainHand.amount = p.inventory.itemInMainHand.amount - 1
                p.inventory.setItemInMainHand(p.inventory.itemInMainHand)
            }
        } else {
            if (p.inventory.itemInOffHand.amount == 1) {
                p.inventory.setItemInOffHand(null)
            } else {
                p.inventory.itemInOffHand.amount = p.inventory.itemInOffHand.amount - 1
                p.inventory.setItemInOffHand(p.inventory.itemInOffHand)
            }
        }

        p.playSound(p.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f)
        p.addPotionEffect(PotionEffect(PotionEffectType.SPEED, duration, 1, false, false))
    }

    private fun enderPearl(p: Player, isMainHand: Boolean) {
        if (!GameManager.isGame(GameManager.GameState.GAME) || p.hasCooldown(Material.ENDER_PEARL) || p.hasCooldown(Material.ENDER_EYE)) return

        VisibilityManager.addJailHide(p)

        p.sendMessage("${MainAPI.getPrefix(MainAPI.PrefixType.SUCCESS)}周りのプレイヤーを非表示にしました。")

        if (isMainHand) {
            val itemStack = p.inventory.itemInMainHand
            val itemMeta = itemStack.itemMeta!!
            itemMeta.setDisplayName("${ChatColor.GREEN}プレイヤーを表示")
            itemMeta.lore = listOf("${ChatColor.YELLOW}クリックして運営以外のプレイヤーを非表示にします。")
            itemStack.type = Material.ENDER_EYE
            itemStack.itemMeta = itemMeta
        } else {
            val itemStack = p.inventory.itemInOffHand
            val itemMeta = itemStack.itemMeta!!
            itemMeta.setDisplayName("${ChatColor.GREEN}プレイヤーを表示")
            itemMeta.lore = listOf("${ChatColor.YELLOW}クリックして運営以外のプレイヤーを非表示にします。")
            itemStack.type = Material.ENDER_EYE
            itemStack.itemMeta = itemMeta
        }

        setJailItemCoolTime(p)
    }

    private fun enderEye(p: Player, isMainHand: Boolean) {
        if (!GameManager.isGame(GameManager.GameState.GAME) || p.hasCooldown(Material.ENDER_PEARL) || p.hasCooldown(Material.ENDER_EYE)) return

        VisibilityManager.removeJailHide(p)

        p.sendMessage("${MainAPI.getPrefix(MainAPI.PrefixType.SUCCESS)}周りのプレイヤーを表示しました。")

        if (isMainHand) {
            val itemStack = p.inventory.itemInMainHand
            val itemMeta = itemStack.itemMeta!!
            itemMeta.setDisplayName("${ChatColor.GREEN}プレイヤーを非表示")
            itemMeta.lore = listOf("${ChatColor.YELLOW}クリックして周りのプレイヤーを非表示にします。")
            itemStack.type = Material.ENDER_PEARL
            itemStack.itemMeta = itemMeta
        } else {
            val itemStack = p.inventory.itemInOffHand
            val itemMeta = itemStack.itemMeta!!
            itemMeta.setDisplayName("${ChatColor.GREEN}プレイヤーを非表示")
            itemMeta.lore = listOf("${ChatColor.YELLOW}クリックして周りのプレイヤーを非表示にします。")
            itemStack.type = Material.ENDER_PEARL
            itemStack.itemMeta = itemMeta
        }

        setJailItemCoolTime(p)
    }

    private fun setJailItemCoolTime(p: Player) {
        val time = 20 * 1

        p.setCooldown(Material.ENDER_PEARL, time)
        p.setCooldown(Material.ENDER_EYE, time)
    }
}
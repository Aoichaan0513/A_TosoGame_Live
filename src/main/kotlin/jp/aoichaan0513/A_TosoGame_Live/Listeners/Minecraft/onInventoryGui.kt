package jp.aoichaan0513.A_TosoGame_Live.Listeners.Minecraft

import jp.aoichaan0513.A_TosoGame_Live.API.Enums.ItemType
import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI
import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI.ErrorMessage
import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI.PrefixType
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.ActionBarManager
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.DiscordManager
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.GameManager
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.GameManager.GameState
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.MoneyManager
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.Player.DifficultyManager
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.Player.PlayerConfig
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.Player.PlayerManager
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.Player.VisibilityManager
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.World.WorldConfig
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.World.WorldConfig.DifficultyConfig
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.World.WorldManager
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.World.WorldManager.GameType
import jp.aoichaan0513.A_TosoGame_Live.API.Map.MapUtility
import jp.aoichaan0513.A_TosoGame_Live.API.TosoGameAPI
import jp.aoichaan0513.A_TosoGame_Live.Commands.Command.Team
import jp.aoichaan0513.A_TosoGame_Live.Inventory.*
import jp.aoichaan0513.A_TosoGame_Live.Inventory.MapInventory.ActionType
import jp.aoichaan0513.A_TosoGame_Live.Main
import jp.aoichaan0513.A_TosoGame_Live.Mission.MissionManager
import jp.aoichaan0513.A_TosoGame_Live.Runnable.RespawnRunnable
import jp.aoichaan0513.A_TosoGame_Live.Utils.*

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.wesjd.anvilgui.AnvilGUI
import org.apache.commons.lang.RandomStringUtils
import org.bukkit.*
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.meta.SkullMeta
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.function.BiFunction

class onInventoryGui : Listener {
    companion object {

        val set = mutableSetOf<UUID>()
    }

    val itemSelectInventoryMap = mutableMapOf<UUID, Int>()

    @EventHandler
    fun onMainInventoryClick(e: InventoryClickEvent) {
        val p = e.whoClicked as Player
        val inventory = e.inventory
        val inventoryView = e.view
        val itemStack = e.currentItem
        val slot = e.rawSlot

        if (inventory.type != InventoryType.CHEST || !inventoryView.title.equals(MainInventory.title)) return

        e.isCancelled = true
        if (itemStack == null || itemStack.type == Material.AIR) return

        val worldConfig = Main.worldConfig

        when (MainInventory.Item.getItem(itemStack, slot)) {
            MainInventory.Item.PLAYER_SETTINGS -> {
                if (GameManager.isGame(GameState.NONE)) {
                    p.openInventory(PlayerSettingsInventory.getInventory(p))
                    ActionBarManager.send(p, "${ChatColor.BLUE}${ChatColor.BOLD}${ChatColor.UNDERLINE}プレイヤー設定アプリ${ChatColor.RESET}${ChatColor.YELLOW}を開きました。")
                    return
                }
                MainAPI.sendMessage(p, ErrorMessage.NOT_GAME)
            }
            MainInventory.Item.MAP_SETTINGS -> {
                if (!TosoGameAPI.isAdmin(p)) return
                p.openInventory(MapInventory.editInventory)
                ActionBarManager.send(p, "${ChatColor.BLUE}${ChatColor.BOLD}${ChatColor.UNDERLINE}マップ設定アプリ${ChatColor.RESET}${ChatColor.YELLOW}を開きました。")
            }
            MainInventory.Item.CALL_APP -> {
                /*
                if (Teams.hasJoinedTeams(p) && DiscordManager.integrationMap.isNotEmpty()) {
                    p.openInventory(CallInventory.getInventory(p))
                    ActionBarManager.send(p, "${ChatColor.GOLD}${ChatColor.BOLD}${ChatColor.UNDERLINE}電話アプリ${ChatColor.RESET}${ChatColor.YELLOW}を開きました。")
                    return
                }
                */
                p.sendMessage("${MainAPI.getPrefix(PrefixType.ERROR)}現在開くことができません。")
            }
            MainInventory.Item.MISSION_APP -> {
                p.openInventory(MissionInventory.getInventory(MissionManager.MissionType.MISSION))
                ActionBarManager.send(p, "${ChatColor.GOLD}${ChatColor.BOLD}${ChatColor.UNDERLINE}ミッションアプリ${ChatColor.RESET}${ChatColor.YELLOW}を開きました。")
            }
            MainInventory.Item.MAP_APP -> {
                if (p.isAdminTeam || p.isPlayerGroup) {
                    p.closeInventory()

                    val mapStack = MapUtility.itemStack
                    if (mapStack != null) {
                        if (p.inventory.none { it != null && it.type == MapUtility.material }) {
                            val offHand = p.inventory.itemInOffHand
                            if (offHand == null || offHand.type == Material.AIR)
                                p.inventory.setItemInOffHand(mapStack)
                            else
                                p.inventory.addItem(mapStack)

                            p.sendMessage("${MainAPI.getPrefix(PrefixType.WARNING)}${ChatColor.BOLD}${ChatColor.UNDERLINE}マップアプリ${ChatColor.RESET}${ChatColor.YELLOW}を開きました。")
                        } else {
                            val offHand = p.inventory.itemInOffHand
                            if (offHand != null && offHand.type == MapUtility.material)
                                p.inventory.setItemInOffHand(null)
                            else
                                p.inventory.remove(mapStack)

                            p.sendMessage("${MainAPI.getPrefix(PrefixType.WARNING)}${ChatColor.BOLD}${ChatColor.UNDERLINE}マップアプリ${ChatColor.RESET}${ChatColor.YELLOW}を閉じました。")
                        }
                        return
                    }
                }
                p.sendMessage("${MainAPI.getPrefix(PrefixType.ERROR)}現在実行できません。")
            }
            MainInventory.Item.SPEC_MODE -> {
                if (GameManager.isGame(GameState.GAME)) {
                    if (p.isJailTeam) {
                        if (!TosoGameAPI.isRespawn || !RespawnRunnable.isAllowRespawn(p)) {
                            if (p.gameMode == GameMode.ADVENTURE) {
                                p.gameMode = GameMode.SPECTATOR
                                p.inventory.heldItemSlot = 0
                                VisibilityManager.removeJailHide(p)
                                TosoGameAPI.teleport(p, worldConfig.respawnLocationConfig.locations.values)
                                p.sendMessage("""
                                    ${MainAPI.getPrefix(PrefixType.WARNING)}観戦モードになりました。
                                    ${MainAPI.getPrefix(PrefixType.SECONDARY)}"/spec"で観戦モードから戻れます。
                                """.trimIndent())
                                return
                            } else {
                                p.gameMode = GameMode.ADVENTURE
                                TosoGameAPI.teleport(p, worldConfig.jailLocationConfig.locations.values)
                                p.sendMessage("${MainAPI.getPrefix(PrefixType.WARNING)}観戦モードから戻りました。")
                                return
                            }
                        }
                        p.sendMessage("${MainAPI.getPrefix(PrefixType.ERROR)}途中参加・復活が可能なため実行できません。")
                        return
                    }
                    MainAPI.sendMessage(p, ErrorMessage.PERMISSIONS_TEAM_JAIL)
                    return
                }
                MainAPI.sendMessage(p, ErrorMessage.NOT_GAME)
            }
            MainInventory.Item.DISCORD_INTEGRATION -> {
                val botInstance = Main.botInstance
                if (botInstance != null) {
                    if (botInstance.getCategoryById(Main.mainConfig.getLong("discordIntegration.categoryId"))?.guild?.getMembersByNickname(p.name, true)?.isEmpty() == true) {
                        val str = RandomStringUtils.randomNumeric(8)
                        DiscordManager.hashMap[str] = p.uniqueId

                        p.sendMessage("""
                            ${MainAPI.getPrefix(PrefixType.WARNING)}"$str"を ${botInstance.selfUser.asTag} のDMで送信して連携を完了してください。
                            ${MainAPI.getPrefix(PrefixType.SECONDARY)}5分経過後にこのコードは使用できなくなります。
                        """.trimIndent())

                        GlobalScope.launch {
                            delay(TimeUnit.MINUTES.toMillis(5))

                            if (!DiscordManager.hashMap.containsValue(p.uniqueId)) return@launch
                            DiscordManager.hashMap.remove(str)

                            p.sendMessage("${MainAPI.getPrefix(PrefixType.ERROR)}タイムアウトしました。")
                        }
                        return
                    }
                    p.sendMessage("${MainAPI.getPrefix(PrefixType.ERROR)}すでにこのプレイヤー名で連携したプレイヤーがいるため連携できません。")
                    return
                }
                p.sendMessage("${MainAPI.getPrefix(PrefixType.ERROR)}連携機能が無効のため設定できません。")
                p.openInventory(MainInventory.getInventory(p))
            }
            MainInventory.Item.REQUEST_HUNTER -> {
                if (Team.isHunterRandom) {
                    if (!Main.hunterShuffleSet.contains(p.uniqueId)) Main.hunterShuffleSet.add(p.uniqueId) else Main.hunterShuffleSet.remove(p.uniqueId)
                    p.openInventory(MainInventory.getInventory(p))
                    p.sendMessage(MainAPI.getPrefix(PrefixType.WARNING) + ChatColor.UNDERLINE + "ハンター募集" + ChatColor.GOLD + ChatColor.UNDERLINE + (if (Main.hunterShuffleSet.contains(p.uniqueId)) "に応募" else "の応募をキャンセル") + ChatColor.RESET + ChatColor.YELLOW + "しました。")
                    return
                }
                p.sendMessage("${MainAPI.getPrefix(PrefixType.ERROR)}ハンターを募集していないため実行できません。")
            }
            MainInventory.Item.REQUEST_TUHO -> {
                if (Team.isTuhoRandom) {
                    if (!Main.tuhoShuffleSet.contains(p.uniqueId)) Main.tuhoShuffleSet.add(p.uniqueId) else Main.tuhoShuffleSet.remove(p.uniqueId)
                    p.openInventory(MainInventory.getInventory(p))
                    p.sendMessage(MainAPI.getPrefix(PrefixType.WARNING) + ChatColor.UNDERLINE + "通報部隊募集" + ChatColor.GOLD + ChatColor.UNDERLINE + (if (Main.tuhoShuffleSet.contains(p.uniqueId)) "に応募" else "の応募をキャンセル") + ChatColor.RESET + ChatColor.YELLOW + "しました。")
                    return
                }
                p.sendMessage("${MainAPI.getPrefix(PrefixType.ERROR)}通報部隊を募集していないため実行できません。")
            }
            MainInventory.Item.DIFFICULTY_EASY -> {
                if (GameManager.isGame(GameState.NONE)) {
                    val difficulty = WorldManager.Difficulty.EASY

                    DifficultyManager.setDifficulty(p, difficulty)
                    MoneyManager.setRate(p)
                    p.openInventory(MainInventory.getInventory(p))
                    p.sendMessage("${MainAPI.getPrefix(PrefixType.WARNING)}難易度を${difficulty.color}${ChatColor.BOLD}${ChatColor.UNDERLINE}${ChatColor.stripColor(difficulty.displayName)}${ChatColor.RESET}${ChatColor.GOLD}に変更しました。")
                    return
                }
                MainAPI.sendMessage(p, ErrorMessage.GAME)
            }
            MainInventory.Item.DIFFICULTY_NORMAL -> {
                if (GameManager.isGame(GameState.NONE)) {
                    val difficulty = WorldManager.Difficulty.NORMAL

                    DifficultyManager.setDifficulty(p, difficulty)
                    MoneyManager.setRate(p)
                    p.openInventory(MainInventory.getInventory(p))
                    p.sendMessage("${MainAPI.getPrefix(PrefixType.WARNING)}難易度を${difficulty.color}${ChatColor.BOLD}${ChatColor.UNDERLINE}${ChatColor.stripColor(difficulty.displayName)}${ChatColor.RESET}${ChatColor.GOLD}に変更しました。")
                    return
                }
                MainAPI.sendMessage(p, ErrorMessage.GAME)
            }
            MainInventory.Item.DIFFICULTY_HARD -> {
                if (GameManager.isGame(GameState.NONE)) {
                    val difficulty = WorldManager.Difficulty.HARD

                    DifficultyManager.setDifficulty(p, difficulty)
                    MoneyManager.setRate(p)
                    p.openInventory(MainInventory.getInventory(p))
                    p.sendMessage("${MainAPI.getPrefix(PrefixType.WARNING)}難易度を${difficulty.color}${ChatColor.BOLD}${ChatColor.UNDERLINE}${ChatColor.stripColor(difficulty.displayName)}${ChatColor.RESET}${ChatColor.GOLD}に変更しました。")
                    return
                }
                MainAPI.sendMessage(p, ErrorMessage.GAME)
            }
            MainInventory.Item.DIFFICULTY_HARDCORE -> {
                if (GameManager.isGame(GameState.NONE)) {
                    val difficulty = WorldManager.Difficulty.HARDCORE

                    DifficultyManager.setDifficulty(p, difficulty)
                    MoneyManager.setRate(p)
                    p.openInventory(MainInventory.getInventory(p))
                    p.sendMessage("${MainAPI.getPrefix(PrefixType.WARNING)}難易度を${difficulty.color}${ChatColor.BOLD}${ChatColor.UNDERLINE}${ChatColor.stripColor(difficulty.displayName)}${ChatColor.RESET}${ChatColor.GOLD}に変更しました。")
                    return
                }
                MainAPI.sendMessage(p, ErrorMessage.GAME)
            }
        }
    }

    @EventHandler
    fun onMissionInventoryClick(e: InventoryClickEvent) {
        val p = e.whoClicked as Player
        val inventory = e.inventory
        val inventoryView = e.view
        val itemStack = e.currentItem
        val slot = e.slot

        if (inventory.type != InventoryType.CHEST || (!inventoryView.title.equals(MissionInventory.missionTitle) && !inventoryView.title.equals(MissionInventory.tutatuHintTitle) && !inventoryView.title.equals(MissionInventory.endTitle))) return

        e.isCancelled = true
        if (itemStack == null || itemStack.type == Material.AIR) return

        if (slot == 49 && itemStack.type == Material.WHITE_STAINED_GLASS_PANE) {
            val itemMeta = itemStack.itemMeta
            if (itemMeta == null || !itemMeta.displayName.equals("${ChatColor.GOLD}${ChatColor.BOLD}${ChatColor.UNDERLINE}ホーム")) return
            p.openInventory(MainInventory.getInventory(p))
            ActionBarManager.send(p, "${ChatColor.GOLD}${ChatColor.BOLD}${ChatColor.UNDERLINE}ホーム画面${ChatColor.RESET}${ChatColor.YELLOW}を表示しました。")
        } else if (slot in 0..2 && itemStack.type == Material.RED_CONCRETE) {
            val itemMeta = itemStack.itemMeta
            if (itemMeta == null || !itemMeta.displayName.equals("${ChatColor.RED}${ChatColor.BOLD}${ChatColor.UNDERLINE}ミッション")) return
            p.openInventory(MissionInventory.getInventory(MissionManager.MissionType.MISSION))
        } else if (slot in 3..5 && itemStack.type == Material.YELLOW_CONCRETE) {
            val itemMeta = itemStack.itemMeta
            if (itemMeta == null || !itemMeta.displayName.equals("${ChatColor.YELLOW}${ChatColor.BOLD}${ChatColor.UNDERLINE}通達・ヒント")) return
            p.openInventory(MissionInventory.getInventory(MissionManager.MissionType.TUTATU_HINT))
        } else if (slot in 6..8 && itemStack.type == Material.LIME_CONCRETE) {
            val itemMeta = itemStack.itemMeta
            if (itemMeta == null || !itemMeta.displayName.equals("${ChatColor.GREEN}${ChatColor.BOLD}${ChatColor.UNDERLINE}終了したミッション")) return
            p.openInventory(MissionInventory.getInventory(MissionManager.MissionType.END))
        } else {
            val itemMeta = itemStack.itemMeta
            if (itemMeta == null || !itemMeta.hasCustomModelData()) return

            val missionType = when (inventoryView.title) {
                MissionInventory.tutatuHintTitle -> MissionManager.MissionType.TUTATU_HINT
                MissionInventory.endTitle -> MissionManager.MissionType.END
                else -> MissionManager.MissionType.MISSION
            }

            MissionInventory.openBook(p, MissionManager.getMission(itemMeta.customModelData, missionType) ?: return)
        }
    }

    @EventHandler
    fun onCallInventoryClick(e: InventoryClickEvent) {
        val p = e.whoClicked as Player
        val inventory = e.inventory
        val inventoryView = e.view
        val itemStack = e.currentItem
        val slot = e.slot

        if (inventory.type != InventoryType.CHEST || !inventoryView.title.startsWith(CallInventory.title)) return

        e.isCancelled = true
        if (itemStack == null || itemStack.type == Material.AIR) return

        when (itemStack.type) {
            Material.PLAYER_HEAD -> {
                val itemMetaPlayerHead = itemStack.itemMeta as SkullMeta

                when (itemMetaPlayerHead.owningPlayer?.name) {
                    CallInventory.arrowLeft -> {
                        val currentPageCount = (inventoryView.title.substring(CallInventory.title.length).toIntOrNull()
                                ?: 1) - 1

                        if (currentPageCount < 1) return
                        p.openInventory(CallInventory.getInventory(p, currentPageCount - 1))
                    }
                    CallInventory.arrowRight -> {
                        val currentPageCount = (inventoryView.title.substring(CallInventory.title.length).toIntOrNull()
                                ?: 1) - 1
                        p.openInventory(CallInventory.getInventory(p, currentPageCount + 1))
                    }
                    else -> {
                        val player = itemMetaPlayerHead.owningPlayer?.player
                        if (player != null) {
                            if (DiscordManager.integrationMap.containsKey(player.uniqueId)) {
                                DiscordManager.outGoingCall(p, player)
                                return
                            }
                            p.sendMessage("${MainAPI.getPrefix(PrefixType.ERROR)}Discordと連携していないプレイヤーに発信することはできません。")
                            return
                        }
                    }
                }
            }
            Material.WHITE_STAINED_GLASS_PANE -> {
                val itemMeta = itemStack.itemMeta
                if (itemMeta == null || !itemMeta.displayName.equals("${ChatColor.GOLD}${ChatColor.BOLD}${ChatColor.UNDERLINE}ホーム")) return
                p.openInventory(MainInventory.getInventory(p))
                ActionBarManager.send(p, "${ChatColor.GOLD}${ChatColor.BOLD}${ChatColor.UNDERLINE}ホーム画面${ChatColor.RESET}${ChatColor.YELLOW}を表示しました。")
            }
        }
    }

    @EventHandler
    fun onResultInventoryClick(e: InventoryClickEvent) {
        val p = e.whoClicked as Player
        val inventory = e.inventory
        val inventoryView = e.view
        val itemStack = e.currentItem
        val slot = e.slot

        if (inventory.type != InventoryType.CHEST || !inventoryView.title.startsWith(ResultInventory.title)) return

        e.isCancelled = true
        if (itemStack == null || itemStack.type == Material.AIR) return

        val resultType = if (inventoryView.title.startsWith(ResultInventory.rewardTitle))
            ResultInventory.ResultType.REWARD
        else
            ResultInventory.ResultType.ENSURE

        when (itemStack.type) {
            Material.PLAYER_HEAD -> {
                val itemMetaPlayerHead = itemStack.itemMeta as SkullMeta

                val currentPageCount = (inventoryView.title.substring(
                        if (inventoryView.title.startsWith(ResultInventory.rewardTitle))
                            ResultInventory.rewardTitle.length
                        else
                            ResultInventory.ensureTitle.length
                ).toIntOrNull() ?: 1) - 1

                when (itemMetaPlayerHead.owningPlayer?.name) {
                    ResultInventory.arrowLeft -> {
                        if (currentPageCount < 1) return
                        p.openInventory(ResultInventory.getInventory(p, resultType, currentPageCount - 1))
                    }
                    ResultInventory.arrowRight -> {
                        p.openInventory(ResultInventory.getInventory(p, resultType, currentPageCount + 1))
                    }
                }
            }
            Material.WHITE_STAINED_GLASS_PANE -> {
                val itemMeta = itemStack.itemMeta
                if (itemMeta == null || !itemMeta.displayName.equals("${ChatColor.GOLD}${ChatColor.BOLD}${ChatColor.UNDERLINE}ホーム")) return
                p.openInventory(MainInventory.getInventory(p))
                ActionBarManager.send(p, "${ChatColor.GOLD}${ChatColor.BOLD}${ChatColor.UNDERLINE}ホーム画面${ChatColor.RESET}${ChatColor.YELLOW}を表示しました。")
            }
        }
    }

    @EventHandler
    fun onPlayerSettingsInventoryClick(e: InventoryClickEvent) {
        val p = e.whoClicked as Player
        val inventory = e.inventory
        val inventoryView = e.view
        val itemStack = e.currentItem
        val slot = e.rawSlot

        if (inventory.type != InventoryType.CHEST || !inventoryView.title.equals(PlayerSettingsInventory.title)) return

        e.isCancelled = true
        if (itemStack == null || itemStack.type == Material.AIR) return

        when (PlayerSettingsInventory.Item.getItem(itemStack, slot)) {
            PlayerSettingsInventory.Item.INVENTORY_SETTINGS -> {
                p.openInventory(PlayerSettingsInventory.getInventorySettingsInventory(p))
                ActionBarManager.send(p, "${ChatColor.GOLD}${ChatColor.BOLD}${ChatColor.UNDERLINE}インベントリ設定${ChatColor.RESET}${ChatColor.YELLOW}を開きました。")
            }
            PlayerSettingsInventory.Item.BOOK_FOREGROUND -> {
                val playerConfig = PlayerManager.loadConfig(p)

                val (color, name) = if (playerConfig.bookForegroundColor == PlayerConfig.BookForegroundColor.BLACK)
                    PlayerConfig.BookForegroundColor.WHITE to "白"
                else
                    PlayerConfig.BookForegroundColor.BLACK to "黒"

                playerConfig.bookForegroundColor = color
                p.sendMessage("${MainAPI.getPrefix(PrefixType.SUCCESS)}本の文字色を${color.color}${ChatColor.BOLD}${ChatColor.UNDERLINE}$name${ChatColor.RESET}${ChatColor.GREEN}に設定しました。")
                p.openInventory(PlayerSettingsInventory.getInventory(p))
            }
            PlayerSettingsInventory.Item.HOME -> {
                p.openInventory(MainInventory.getInventory(p))
                ActionBarManager.send(p, "${ChatColor.GOLD}${ChatColor.BOLD}${ChatColor.UNDERLINE}ホーム画面${ChatColor.RESET}${ChatColor.YELLOW}を表示しました。")
            }
        }
    }

    @EventHandler
    fun onPlayerSettingsInventorySettingsInventoryClick(e: InventoryClickEvent) {
        val p = e.whoClicked as Player
        val inventory = e.inventory
        val inventoryView = e.view
        val itemStack = e.currentItem
        val slot = e.slot

        if (inventory.type != InventoryType.CHEST || !inventoryView.title.equals(PlayerSettingsInventory.inventoryTitle)) return

        e.isCancelled = true
        if (itemStack == null || itemStack.type == Material.AIR) return

        val itemMeta = itemStack.itemMeta

        when (itemStack.type) {
            Material.WHITE_STAINED_GLASS_PANE -> {
                if (itemMeta == null || !itemMeta.displayName.equals("${ChatColor.GOLD}${ChatColor.BOLD}${ChatColor.UNDERLINE}ホーム")) return
                p.openInventory(MainInventory.getInventory(p))
                ActionBarManager.send(p, "${ChatColor.GOLD}${ChatColor.BOLD}${ChatColor.UNDERLINE}ホーム画面${ChatColor.RESET}${ChatColor.YELLOW}を表示しました。")
            }
            else -> {
                if (slot !in 9..17 && slot != 27 || itemMeta == null || !itemMeta.hasCustomModelData()) return

                itemSelectInventoryMap[p.uniqueId] = itemMeta.customModelData
                p.openInventory(PlayerSettingsInventory.getItemSelectInventory())
            }
        }
    }

    @EventHandler
    fun onPlayerSettingsItemSelectInventoryClick(e: InventoryClickEvent) {
        val p = e.whoClicked as Player
        val inventory = e.inventory
        val inventoryView = e.view
        val itemStack = e.currentItem
        val slot = e.slot

        if (inventory.type != InventoryType.CHEST || !inventoryView.title.equals(PlayerSettingsInventory.itemSelectTitle)) return

        e.isCancelled = true
        if (itemStack == null || itemStack.type == Material.AIR) return

        val itemMeta = itemStack.itemMeta

        when (itemStack.type) {
            Material.WHITE_STAINED_GLASS_PANE -> {
                if (!itemSelectInventoryMap.containsKey(p.uniqueId) || itemMeta == null || !itemMeta.displayName.equals("${ChatColor.GOLD}${ChatColor.BOLD}${ChatColor.UNDERLINE}ホーム")) return

                p.openInventory(MainInventory.getInventory(p))
                ActionBarManager.send(p, "${ChatColor.GOLD}${ChatColor.BOLD}${ChatColor.UNDERLINE}ホーム画面${ChatColor.RESET}${ChatColor.YELLOW}を表示しました。")

                itemSelectInventoryMap.remove(p.uniqueId)
            }
            Material.BARRIER -> {
                p.openInventory(PlayerSettingsInventory.getInventorySettingsInventory(p))
                ActionBarManager.send(p, "${ChatColor.GOLD}${ChatColor.BOLD}${ChatColor.UNDERLINE}設定をキャンセル${ChatColor.RESET}${ChatColor.YELLOW}しました。")

                itemSelectInventoryMap.remove(p.uniqueId)
            }
            else -> {
                if (itemMeta == null || !itemSelectInventoryMap.containsKey(p.uniqueId)) return

                val inventoryConfig = PlayerManager.loadConfig(p).inventoryConfig
                val itemType = ItemType.getItemType(itemStack.type)

                if (slot !in 9..17) return

                val s = itemSelectInventoryMap.remove(p.uniqueId) ?: return

                inventoryConfig.setSlot(itemType, s)
                p.sendMessage("${MainAPI.getPrefix(PrefixType.SUCCESS)}スロットに配置するアイテムを${ChatColor.BOLD}${ChatColor.UNDERLINE}${itemMeta.localizedName}${ChatColor.RESET}${ChatColor.GREEN}に設定しました。")

                p.openInventory(PlayerSettingsInventory.getInventorySettingsInventory(p))
            }
        }
    }

    @EventHandler
    fun onMapEditInventoryClick(e: InventoryClickEvent) {
        val p = e.whoClicked as Player
        val inventory = e.inventory
        val inventoryView = e.view
        val itemStack = e.currentItem
        val slot = e.slot

        if (inventory.type == InventoryType.CHEST) {
            if (inventoryView.title == MapInventory.editTitle) {
                val worldConfig = Main.worldConfig
                e.isCancelled = true
                if (e.isLeftClick) {
                    if (slot == 2 && itemStack!!.type == Material.PAPER) {
                        set.add(p.uniqueId)
                        p.openInventory(MapInventory.getDifficultyInventory(ActionType.RESPAWN_COUNT))
                        return
                    } else if (slot == 3 && itemStack!!.type == Material.PAPER) {
                        set.add(p.uniqueId)
                        p.openInventory(MapInventory.getDifficultyInventory(ActionType.RESPAWN_COOLTIME))
                        return
                    } else if (slot == 5 && itemStack!!.type == Material.PAPER) {
                        set.add(p.uniqueId)
                        p.openInventory(MapInventory.getDifficultyInventory(ActionType.RATE))
                        return
                    } else if (slot == 6 && itemStack!!.type == Material.PAPER) {
                        AnvilGUI(Main.pluginInstance, p, "サイコロの最大数を入力…", BiFunction { player, reply ->
                            if (ParseUtil.isInt(reply)) {
                                val i = reply.toInt()
                                worldConfig.opGameConfig.diceCount = i
                                player.sendMessage("${MainAPI.getPrefix(PrefixType.WARNING)}サイコロの最大数" + ChatColor.RESET + ChatColor.YELLOW + "を" + ChatColor.GOLD + ChatColor.BOLD + ChatColor.UNDERLINE + i + "回" + ChatColor.RESET + ChatColor.YELLOW + "に設定しました。")
                                player.openInventory(MapInventory.editInventory)
                            } else {
                                MainAPI.sendMessage(player, ErrorMessage.ARGS_INTEGER)
                                player.openInventory(MapInventory.editInventory)
                            }
                            null
                        })
                    } else if (slot == 19 && itemStack!!.type == Material.PAPER) {
                        AnvilGUI(Main.pluginInstance, p, "カウントダウンを秒数で入力…", BiFunction { player, reply ->
                            if (ParseUtil.isInt(reply)) {
                                val i = reply.toInt()
                                worldConfig.gameConfig.countDown = i
                                player.sendMessage("${MainAPI.getPrefix(PrefixType.WARNING)}カウントダウン" + ChatColor.RESET + ChatColor.YELLOW + "を" + ChatColor.GOLD + ChatColor.BOLD + ChatColor.UNDERLINE + DateTimeUtil.formatTimestamp(i).toJapan + ChatColor.RESET + ChatColor.YELLOW + "に設定しました。")
                                player.openInventory(MapInventory.editInventory)
                            } else {
                                MainAPI.sendMessage(player, ErrorMessage.ARGS_INTEGER)
                                player.openInventory(MapInventory.editInventory)
                            }
                            null
                        })
                    } else if (slot == 20 && itemStack!!.type == Material.PAPER) {
                        AnvilGUI(Main.pluginInstance, p, "ゲーム時間を秒数で入力…", BiFunction { player, reply ->
                            if (ParseUtil.isInt(reply)) {
                                val i = reply.toInt()
                                worldConfig.gameConfig.game = i
                                player.sendMessage("${MainAPI.getPrefix(PrefixType.WARNING)}ゲーム時間" + ChatColor.RESET + ChatColor.YELLOW + "を" + ChatColor.GOLD + ChatColor.BOLD + ChatColor.UNDERLINE + DateTimeUtil.formatTimestamp(i).toJapan + ChatColor.RESET + ChatColor.YELLOW + "に設定しました。")
                                player.openInventory(MapInventory.editInventory)
                            } else {
                                MainAPI.sendMessage(player, ErrorMessage.ARGS_INTEGER)
                                player.openInventory(MapInventory.editInventory)
                            }
                            null
                        })
                    } else if (slot == 21 && itemStack!!.type == Material.PAPER) {
                        AnvilGUI(Main.pluginInstance, p, "復活禁止時間を秒数で入力…", BiFunction { player, reply ->
                            if (ParseUtil.isInt(reply)) {
                                val i = reply.toInt()
                                worldConfig.gameConfig.respawnDeny = i
                                player.sendMessage("${MainAPI.getPrefix(PrefixType.WARNING)}復活禁止時間" + ChatColor.RESET + ChatColor.YELLOW + "を" + ChatColor.GOLD + ChatColor.BOLD + ChatColor.UNDERLINE + DateTimeUtil.formatTimestamp(i).toJapan + ChatColor.RESET + ChatColor.YELLOW + "に設定しました。")
                                player.openInventory(MapInventory.editInventory)
                            } else {
                                MainAPI.sendMessage(player, ErrorMessage.ARGS_INTEGER)
                                player.openInventory(MapInventory.editInventory)
                            }
                            null
                        })
                    } else if (slot == 23) {
                        if (itemStack!!.type == Material.LIME_CONCRETE) {
                            worldConfig.gameConfig.script = false
                            p.sendMessage("${MainAPI.getPrefix(PrefixType.SUCCESS)}スクリプト機能を無効に設定しました。")
                            p.openInventory(MapInventory.editInventory)
                            return
                        } else if (itemStack.type == Material.RED_CONCRETE) {
                            worldConfig.gameConfig.script = true
                            p.sendMessage("${MainAPI.getPrefix(PrefixType.SUCCESS)}スクリプト機能を有効に設定しました。")
                            p.openInventory(MapInventory.editInventory)
                            return
                        }
                    } else if (slot == 24) {
                        if (itemStack!!.type == Material.LIME_CONCRETE) {
                            worldConfig.gameConfig.successMission = false
                            p.sendMessage("${MainAPI.getPrefix(PrefixType.SUCCESS)}生存ミッションを無効に設定しました。")
                            p.openInventory(MapInventory.editInventory)
                            return
                        } else if (itemStack.type == Material.RED_CONCRETE) {
                            worldConfig.gameConfig.successMission = true
                            p.sendMessage("${MainAPI.getPrefix(PrefixType.SUCCESS)}生存ミッションを有効に設定しました。")
                            p.openInventory(MapInventory.editInventory)
                            return
                        }
                    } else if (slot == 25) {
                        if (itemStack!!.type == Material.LIME_CONCRETE) {
                            worldConfig.gameConfig.jump = false
                            p.sendMessage("${MainAPI.getPrefix(PrefixType.SUCCESS)}ダッシュジャンプを無効に設定しました。")
                            p.openInventory(MapInventory.editInventory)
                            return
                        } else if (itemStack.type == Material.RED_CONCRETE) {
                            worldConfig.gameConfig.jump = true
                            p.sendMessage("${MainAPI.getPrefix(PrefixType.SUCCESS)}ダッシュジャンプを有効に設定しました。")
                            p.openInventory(MapInventory.editInventory)
                            return
                        }
                    } else if (slot == 28 && itemStack!!.type == Material.BONE) {
                        set.add(p.uniqueId)
                        p.openInventory(MapInventory.getDifficultyInventory(ActionType.ITEM_START_BONE))
                        return
                    } else if (slot == 29 && itemStack!!.type == Material.FEATHER) {
                        set.add(p.uniqueId)
                        p.openInventory(MapInventory.getDifficultyInventory(ActionType.ITEM_START_FEATHER))
                        return
                    } else if (slot == 30 && itemStack!!.type == Material.EGG) {
                        set.add(p.uniqueId)
                        p.openInventory(MapInventory.getDifficultyInventory(ActionType.ITEM_START_EGG))
                        return
                    } else if (slot == 32 && itemStack!!.type == Material.BONE) {
                        set.add(p.uniqueId)
                        p.openInventory(MapInventory.getDifficultyInventory(ActionType.ITEM_RESPAWN_BONE))
                        return
                    } else if (slot == 33 && itemStack!!.type == Material.FEATHER) {
                        set.add(p.uniqueId)
                        p.openInventory(MapInventory.getDifficultyInventory(ActionType.ITEM_RESPAWN_FEATHER))
                        return
                    } else if (slot == 34 && itemStack!!.type == Material.EGG) {
                        set.add(p.uniqueId)
                        p.openInventory(MapInventory.getDifficultyInventory(ActionType.ITEM_RESPAWN_EGG))
                        return
                    } else if (slot == 37 && (itemStack!!.type == Material.RED_CONCRETE || itemStack.type == Material.LIME_CONCRETE)) {
                        worldConfig.opGameLocationConfig.opLocation = p.location
                        p.sendMessage("${MainAPI.getPrefix(PrefixType.SUCCESS)}オープニングゲーム地点を設定しました。")
                        p.openInventory(MapInventory.editInventory)
                        return
                    } else if (slot == 38 && (itemStack!!.type == Material.RED_CONCRETE || itemStack.type == Material.LIME_CONCRETE)) {
                        AnvilGUI(Main.pluginInstance, p, "数値を入力…", BiFunction { player, reply ->
                            if (ParseUtil.isInt(reply)) {
                                val i = reply.toInt()
                                if (i > 0) {
                                    worldConfig.opGameLocationConfig.setGOPLocation(i, p.location)
                                    player.sendMessage("${MainAPI.getPrefix(PrefixType.SUCCESS)}オープニングゲーム集合地点の位置${i}を設定しました。")
                                    player.openInventory(MapInventory.editInventory)
                                } else {
                                    player.sendMessage("${MainAPI.getPrefix(PrefixType.ERROR)}引数が不正です。1以上で数字を指定してください。")
                                    player.openInventory(MapInventory.editInventory)
                                }
                            } else {
                                MainAPI.sendMessage(player, ErrorMessage.ARGS_INTEGER)
                                player.openInventory(MapInventory.editInventory)
                            }
                            null
                        })
                    } else if (slot == 40 && (itemStack!!.type == Material.RED_CONCRETE || itemStack.type == Material.LIME_CONCRETE)) {
                        AnvilGUI(Main.pluginInstance, p, "数値を入力…", BiFunction { player, reply ->
                            if (ParseUtil.isInt(reply)) {
                                val i = reply.toInt()
                                if (i > 0) {
                                    worldConfig.hunterLocationConfig.setLocation(i, p.location)
                                    player.sendMessage("${MainAPI.getPrefix(PrefixType.SUCCESS)}ハンター集合地点の位置${i}を設定しました。")
                                    player.openInventory(MapInventory.editInventory)
                                } else {
                                    player.sendMessage("${MainAPI.getPrefix(PrefixType.ERROR)}引数が不正です。1以上で数字を指定してください。")
                                    player.openInventory(MapInventory.editInventory)
                                }
                            } else {
                                MainAPI.sendMessage(player, ErrorMessage.ARGS_INTEGER)
                                player.openInventory(MapInventory.editInventory)
                            }
                            null
                        })
                    } else if (slot == 42 && (itemStack!!.type == Material.RED_CONCRETE || itemStack.type == Material.LIME_CONCRETE)) {
                        AnvilGUI(Main.pluginInstance, p, "数値を入力…", BiFunction { player, reply ->
                            if (ParseUtil.isInt(reply)) {
                                val i = reply.toInt()
                                if (i > 0) {
                                    worldConfig.jailLocationConfig.setLocation(i, p.location)
                                    player.sendMessage("${MainAPI.getPrefix(PrefixType.SUCCESS)}牢獄地点の位置${i}を設定しました。")
                                    player.openInventory(MapInventory.editInventory)
                                } else {
                                    player.sendMessage("${MainAPI.getPrefix(PrefixType.ERROR)}引数が不正です。1以上で数字を指定してください。")
                                    player.openInventory(MapInventory.editInventory)
                                }
                            } else {
                                MainAPI.sendMessage(player, ErrorMessage.ARGS_INTEGER)
                                player.openInventory(MapInventory.editInventory)
                            }
                            null
                        })
                    } else if (slot == 43 && (itemStack!!.type == Material.RED_CONCRETE || itemStack.type == Material.LIME_CONCRETE)) {
                        AnvilGUI(Main.pluginInstance, p, "数値を入力…", BiFunction { player, reply ->
                            if (ParseUtil.isInt(reply)) {
                                val i = reply.toInt()
                                if (i > 0) {
                                    worldConfig.respawnLocationConfig.setLocation(i, p.location)
                                    player.sendMessage("${MainAPI.getPrefix(PrefixType.SUCCESS)}復活地点の位置${i}を設定しました。")
                                    player.openInventory(MapInventory.editInventory)
                                } else {
                                    player.sendMessage("${MainAPI.getPrefix(PrefixType.ERROR)}引数が不正です。1以上で数字を指定してください。")
                                    player.openInventory(MapInventory.editInventory)
                                }
                            } else {
                                MainAPI.sendMessage(player, ErrorMessage.ARGS_INTEGER)
                                player.openInventory(MapInventory.editInventory)
                            }
                            null
                        })
                    }
                } else if (e.isRightClick) {
                    if (slot == 2 && itemStack!!.type == Material.PAPER) {
                        set.add(p.uniqueId)
                        p.openInventory(MapInventory.getDifficultyInventory(ActionType.RESPAWN_COUNT))
                        return
                    } else if (slot == 3 && itemStack!!.type == Material.PAPER) {
                        set.add(p.uniqueId)
                        p.openInventory(MapInventory.getDifficultyInventory(ActionType.RESPAWN_COOLTIME))
                        return
                    } else if (slot == 5 && itemStack!!.type == Material.PAPER) {
                        set.add(p.uniqueId)
                        p.openInventory(MapInventory.getDifficultyInventory(ActionType.RATE))
                        return
                    } else if (slot == 6 && itemStack!!.type == Material.PAPER) {
                        val i = 30
                        worldConfig.opGameConfig.diceCount = i
                        p.sendMessage("${MainAPI.getPrefix(PrefixType.SUCCESS)}サイコロの最大数を${i}に設定しました。")
                        p.openInventory(MapInventory.editInventory)
                        return
                    } else if (slot == 19 && itemStack!!.type == Material.PAPER) {
                        val i = 15
                        worldConfig.gameConfig.countDown = i
                        p.sendMessage("${MainAPI.getPrefix(PrefixType.SUCCESS)}カウントダウン秒数を${DateTimeUtil.formatTimestamp(i).toJapan}に設定しました。")
                        p.openInventory(MapInventory.editInventory)
                        return
                    } else if (slot == 20 && itemStack!!.type == Material.PAPER) {
                        val i = 1200
                        worldConfig.gameConfig.game = i
                        p.sendMessage("${MainAPI.getPrefix(PrefixType.SUCCESS)}ゲーム時間を${DateTimeUtil.formatTimestamp(i).toJapan}に設定しました。")
                        p.openInventory(MapInventory.editInventory)
                        return
                    } else if (slot == 21 && itemStack!!.type == Material.PAPER) {
                        val i = 240
                        worldConfig.gameConfig.respawnDeny = i
                        p.sendMessage("${MainAPI.getPrefix(PrefixType.SUCCESS)}復活禁止時間を${DateTimeUtil.formatTimestamp(i).toJapan}に設定しました。")
                        p.openInventory(MapInventory.editInventory)
                        return
                    } else if (slot == 23 && (itemStack!!.type == Material.RED_CONCRETE || itemStack.type == Material.LIME_CONCRETE)) {
                        worldConfig.gameConfig.script = false
                        p.sendMessage("${MainAPI.getPrefix(PrefixType.SUCCESS)}スクリプト機能を無効に設定しました。")
                        p.openInventory(MapInventory.editInventory)
                        return
                    } else if (slot == 24 && (itemStack!!.type == Material.RED_CONCRETE || itemStack.type == Material.LIME_CONCRETE)) {
                        worldConfig.gameConfig.successMission = true
                        p.sendMessage("${MainAPI.getPrefix(PrefixType.SUCCESS)}生存ミッションを有効に設定しました。")
                        p.openInventory(MapInventory.editInventory)
                        return
                    } else if (slot == 25 && (itemStack!!.type == Material.RED_CONCRETE || itemStack.type == Material.LIME_CONCRETE)) {
                        worldConfig.gameConfig.jump = true
                        p.sendMessage("${MainAPI.getPrefix(PrefixType.SUCCESS)}ダッシュジャンプを有効に設定しました。")
                        p.openInventory(MapInventory.editInventory)
                        return
                    } else if (slot == 28 && itemStack!!.type == Material.BONE) {
                        set.add(p.uniqueId)
                        p.openInventory(MapInventory.getDifficultyInventory(ActionType.ITEM_START_BONE))
                        return
                    } else if (slot == 29 && itemStack!!.type == Material.FEATHER) {
                        set.add(p.uniqueId)
                        p.openInventory(MapInventory.getDifficultyInventory(ActionType.ITEM_START_FEATHER))
                        return
                    } else if (slot == 30 && itemStack!!.type == Material.EGG) {
                        set.add(p.uniqueId)
                        p.openInventory(MapInventory.getDifficultyInventory(ActionType.ITEM_START_EGG))
                        return
                    } else if (slot == 32 && itemStack!!.type == Material.BONE) {
                        set.add(p.uniqueId)
                        p.openInventory(MapInventory.getDifficultyInventory(ActionType.ITEM_RESPAWN_BONE))
                        return
                    } else if (slot == 33 && itemStack!!.type == Material.FEATHER) {
                        set.add(p.uniqueId)
                        p.openInventory(MapInventory.getDifficultyInventory(ActionType.ITEM_RESPAWN_FEATHER))
                        return
                    } else if (slot == 34 && itemStack!!.type == Material.EGG) {
                        set.add(p.uniqueId)
                        p.openInventory(MapInventory.getDifficultyInventory(ActionType.ITEM_RESPAWN_EGG))
                        return
                    } else if (slot == 37 && (itemStack!!.type == Material.RED_CONCRETE || itemStack.type == Material.LIME_CONCRETE)) {
                        p.teleport(worldConfig.opGameLocationConfig.opLocation)
                        p.sendMessage("${MainAPI.getPrefix(PrefixType.SUCCESS)}オープニングゲーム地点にテレポートしました。")
                        p.openInventory(MapInventory.editInventory)
                        return
                    } else if (slot == 38 && (itemStack!!.type == Material.RED_CONCRETE || itemStack.type == Material.LIME_CONCRETE)) {
                        AnvilGUI(Main.pluginInstance, p, "数値を入力…", BiFunction { player, reply ->
                            if (ParseUtil.isInt(reply)) {
                                val i = reply.toInt()
                                if (i > 0) {
                                    if (worldConfig.config.contains(WorldManager.PathType.LOCATION_GOPGAME.path + ".p" + i)) {
                                        player.teleport(worldConfig.opGameLocationConfig.getGOPLocation(i))
                                        player.sendMessage("${MainAPI.getPrefix(PrefixType.SUCCESS)}オープニングゲーム集合地点の位置${i}にテレポートしました。")
                                        player.openInventory(MapInventory.editInventory)
                                    } else {
                                        player.sendMessage("${MainAPI.getPrefix(PrefixType.ERROR)}オープニングゲーム集合地点の位置${i}は設定されていないためテレポートすることができません。")
                                        player.openInventory(MapInventory.editInventory)
                                    }
                                } else {
                                    player.sendMessage("${MainAPI.getPrefix(PrefixType.ERROR)}引数が不正です。1以上で数字を指定してください。")
                                    player.openInventory(MapInventory.editInventory)
                                }
                            } else {
                                MainAPI.sendMessage(player, ErrorMessage.ARGS_INTEGER)
                                player.openInventory(MapInventory.editInventory)
                            }
                            null
                        })
                    } else if (slot == 40 && (itemStack!!.type == Material.RED_CONCRETE || itemStack.type == Material.LIME_CONCRETE)) {
                        AnvilGUI(Main.pluginInstance, p, "数値を入力…", BiFunction { player, reply ->
                            if (ParseUtil.isInt(reply)) {
                                val i = reply.toInt()
                                if (i > 0) {
                                    if (worldConfig.config.contains(WorldManager.PathType.LOCATION_HUNTER.path + ".p" + i)) {
                                        player.teleport(worldConfig.hunterLocationConfig.getLocation(i))
                                        player.sendMessage("${MainAPI.getPrefix(PrefixType.SUCCESS)}ハンター集合地点の位置${i}にテレポートしました。")
                                        player.openInventory(MapInventory.editInventory)
                                    } else {
                                        player.sendMessage("${MainAPI.getPrefix(PrefixType.ERROR)}ハンター集合地点の位置${i}は設定されていないためテレポートすることができません。")
                                        player.openInventory(MapInventory.editInventory)
                                    }
                                } else {
                                    player.sendMessage("${MainAPI.getPrefix(PrefixType.ERROR)}引数が不正です。1以上で数字を指定してください。")
                                    player.openInventory(MapInventory.editInventory)
                                }
                            } else {
                                MainAPI.sendMessage(player, ErrorMessage.ARGS_INTEGER)
                                player.openInventory(MapInventory.editInventory)
                            }
                            null
                        })
                    } else if (slot == 41 && (itemStack!!.type == Material.RED_CONCRETE || itemStack.type == Material.LIME_CONCRETE)) {
                        AnvilGUI(Main.pluginInstance, p, "数値を入力…", BiFunction { player, reply ->
                            if (ParseUtil.isInt(reply)) {
                                val i = reply.toInt()
                                if (i > 0) {
                                    if (worldConfig.config.contains(WorldManager.PathType.DOOR_HUNTER.path + ".p" + i)) {
                                        worldConfig.hunterDoorConfig.openHunterDoor(i)
                                        player.sendMessage("${MainAPI.getPrefix(PrefixType.SUCCESS)}ハンターボックスのドア${i}を開きました。")
                                        player.openInventory(MapInventory.editInventory)
                                    } else {
                                        player.sendMessage("${MainAPI.getPrefix(PrefixType.ERROR)}ハンターボックスのドア${i}は設定されていないため開くことができません。")
                                        player.openInventory(MapInventory.editInventory)
                                    }
                                } else {
                                    player.sendMessage("${MainAPI.getPrefix(PrefixType.ERROR)}引数が不正です。1以上で数字を指定してください。")
                                    player.openInventory(MapInventory.editInventory)
                                }
                            } else {
                                MainAPI.sendMessage(player, ErrorMessage.ARGS_INTEGER)
                                player.openInventory(MapInventory.editInventory)
                            }
                            null
                        })
                    } else if (slot == 42 && (itemStack!!.type == Material.RED_CONCRETE || itemStack.type == Material.LIME_CONCRETE)) {
                        AnvilGUI(Main.pluginInstance, p, "数値を入力…", BiFunction { player, reply ->
                            if (ParseUtil.isInt(reply)) {
                                val i = reply.toInt()
                                if (i > 0) {
                                    if (worldConfig.config.contains(WorldManager.PathType.LOCATION_JAIL.path + ".p" + i)) {
                                        player.teleport(worldConfig.jailLocationConfig.getLocation(i))
                                        player.sendMessage("${MainAPI.getPrefix(PrefixType.SUCCESS)}牢獄地点の位置${i}にテレポートしました。")
                                        player.openInventory(MapInventory.editInventory)
                                    } else {
                                        player.sendMessage("${MainAPI.getPrefix(PrefixType.ERROR)}牢獄地点の位置${i}は設定されていないためテレポートすることができません。")
                                        player.openInventory(MapInventory.editInventory)
                                    }
                                } else {
                                    player.sendMessage("${MainAPI.getPrefix(PrefixType.ERROR)}引数が不正です。1以上で数字を指定してください。")
                                    player.openInventory(MapInventory.editInventory)
                                }
                            } else {
                                MainAPI.sendMessage(player, ErrorMessage.ARGS_INTEGER)
                                player.openInventory(MapInventory.editInventory)
                            }
                            null
                        })
                    } else if (slot == 43 && (itemStack!!.type == Material.RED_CONCRETE || itemStack.type == Material.LIME_CONCRETE)) {
                        AnvilGUI(Main.pluginInstance, p, "数値を入力…", BiFunction { player, reply ->
                            if (ParseUtil.isInt(reply)) {
                                val i = reply.toInt()
                                if (i > 0) {
                                    if (worldConfig.config.contains(WorldManager.PathType.LOCATION_RESPAWN.path + ".p" + i)) {
                                        player.teleport(worldConfig.respawnLocationConfig.getLocation(i))
                                        player.sendMessage("${MainAPI.getPrefix(PrefixType.SUCCESS)}復活地点の位置${i}にテレポートしました。")
                                        player.openInventory(MapInventory.editInventory)
                                    } else {
                                        player.sendMessage("${MainAPI.getPrefix(PrefixType.ERROR)}復活地点の位置${i}は設定されていないためテレポートすることができません。")
                                        player.openInventory(MapInventory.editInventory)
                                    }
                                } else {
                                    player.sendMessage("${MainAPI.getPrefix(PrefixType.ERROR)}引数が不正です。1以上で数字を指定してください。")
                                    player.openInventory(MapInventory.editInventory)
                                }
                            } else {
                                MainAPI.sendMessage(player, ErrorMessage.ARGS_INTEGER)
                                player.openInventory(MapInventory.editInventory)
                            }
                            null
                        })
                    }
                }
            }
        }
    }

    @EventHandler
    fun onMapListInventoryClick(e: InventoryClickEvent) {
        val p = e.whoClicked as Player
        val inventory = e.inventory
        val inventoryView = e.view
        val itemStack = e.currentItem

        if (inventory.type == InventoryType.CHEST) {
            if (inventoryView.title == MapInventory.listTitle) {
                if (itemStack == null || itemStack.type == Material.AIR) return

                e.isCancelled = true
                if (!itemStack.enchantments.containsKey(Enchantment.DURABILITY)) {
                    p.sendMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}マップを読み込んでいます…")

                    val world = Bukkit.createWorld(WorldCreator(ChatColor.stripColor(itemStack.itemMeta!!.displayName)!!))!!
                    world.difficulty = Difficulty.EASY
                    world.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false)
                    world.setGameRule(GameRule.DO_WEATHER_CYCLE, false)
                    world.setGameRule(GameRule.DO_MOB_SPAWNING, false)
                    WorldManager.world = world
                    Main.worldConfig = WorldConfig(world)

                    p.teleport(world.spawnLocation)
                    p.sendMessage("${MainAPI.getPrefix(PrefixType.SUCCESS)}マップを読み込みました。")
                    return
                }
                p.sendMessage("${MainAPI.getPrefix(PrefixType.ERROR)}同じマップに変更することはできません。")
                return
            }
        }
    }

    @EventHandler
    fun onDifficultyInventoryClick(e: InventoryClickEvent) {
        val p = e.whoClicked as Player
        val inventory = e.inventory
        val inventoryView = e.view
        val itemStack = e.currentItem
        val slot = e.slot

        if (inventory.type != InventoryType.CHEST || !inventoryView.title.startsWith(MapInventory.difficultyTitle)) return

        e.isCancelled = true
        if (itemStack == null || itemStack.type == Material.AIR) return

        val worldConfig = Main.worldConfig
        val title = ChatColor.stripColor(inventoryView.title)!!.substring(ChatColor.stripColor(MapInventory.difficultyTitle)!!.length)

        when (slot) {
            10 -> {
                val difficultyConfig = worldConfig.getDifficultyConfig(WorldManager.Difficulty.EASY)
                val difficultyName = WorldManager.Difficulty.EASY.displayName
                runAction(p, worldConfig, difficultyConfig, difficultyName, title)
            }
            11 -> {
                val difficultyConfig = worldConfig.getDifficultyConfig(WorldManager.Difficulty.NORMAL)
                val difficultyName = WorldManager.Difficulty.NORMAL.displayName
                runAction(p, worldConfig, difficultyConfig, difficultyName, title)
            }
            12 -> {
                val difficultyConfig = worldConfig.getDifficultyConfig(WorldManager.Difficulty.HARD)
                val difficultyName = WorldManager.Difficulty.HARD.displayName
                runAction(p, worldConfig, difficultyConfig, difficultyName, title)
            }
            13 -> {
                val difficultyConfig = worldConfig.getDifficultyConfig(WorldManager.Difficulty.HARDCORE)
                val difficultyName = WorldManager.Difficulty.HARDCORE.displayName
                runAction(p, worldConfig, difficultyConfig, difficultyName, title)
            }
            16 -> {
                p.openInventory(MapInventory.editInventory)
                ActionBarManager.send(p, "${ChatColor.BLUE}${ChatColor.BOLD}${ChatColor.UNDERLINE}マップ設定アプリ${ChatColor.RESET}${ChatColor.YELLOW}を開きました。")
            }
        }
    }


    private fun runAction(p: Player, worldConfig: WorldConfig, difficultyConfig: DifficultyConfig, difficultyName: String, inventoryTitle: String) {
        if (ActionType.RATE.displayName.equals(inventoryTitle, true)) {
            AnvilGUI(Main.pluginInstance, p, "レートを入力…", BiFunction { player, reply ->
                if (ParseUtil.isInt(reply)) {
                    val i = reply.toInt()
                    difficultyConfig.rate = i
                    player.sendMessage(MainAPI.getPrefix(PrefixType.WARNING) + difficultyName + ChatColor.YELLOW + "の" + ChatColor.GOLD + "レート" + ChatColor.RESET + ChatColor.YELLOW + "を" + ChatColor.GOLD + ChatColor.BOLD + ChatColor.UNDERLINE + i + "円/秒" + ChatColor.RESET + ChatColor.YELLOW + "に設定しました。")
                    player.openInventory(MapInventory.editInventory)
                } else {
                    MainAPI.sendMessage(player, ErrorMessage.ARGS_INTEGER)
                    player.openInventory(MapInventory.editInventory)
                }
                null
            })
        } else if (ActionType.RESPAWN_COUNT.displayName.equals(inventoryTitle, true)) {
            AnvilGUI(Main.pluginInstance, p, "復活可能回数を入力…", BiFunction { player, reply ->
                if (ParseUtil.isInt(reply)) {
                    val i = reply.toInt()
                    difficultyConfig.respawnDenyCount = i
                    player.sendMessage(MainAPI.getPrefix(PrefixType.WARNING) + difficultyName + ChatColor.YELLOW + "の" + ChatColor.GOLD + "復活可能回数" + ChatColor.RESET + ChatColor.YELLOW + "を" + ChatColor.GOLD + ChatColor.BOLD + ChatColor.UNDERLINE + i + "回" + ChatColor.RESET + ChatColor.YELLOW + "に設定しました。")
                    player.openInventory(MapInventory.editInventory)
                } else {
                    MainAPI.sendMessage(player, ErrorMessage.ARGS_INTEGER)
                    player.openInventory(MapInventory.editInventory)
                }
                null
            })
        } else if (ActionType.RESPAWN_COOLTIME.displayName.equals(inventoryTitle, true)) {
            AnvilGUI(Main.pluginInstance, p, "復活クールタイムを秒数で入力…", BiFunction { player, reply ->
                if (ParseUtil.isInt(reply)) {
                    val i = reply.toInt()
                    difficultyConfig.respawnCoolTime = i
                    player.sendMessage(MainAPI.getPrefix(PrefixType.WARNING) + difficultyName + ChatColor.YELLOW + "の" + ChatColor.GOLD + "復活クールタイム" + ChatColor.RESET + ChatColor.YELLOW + "を" + ChatColor.GOLD + ChatColor.BOLD + ChatColor.UNDERLINE + DateTimeUtil.formatTimestamp(i).toJapan + "/回" + ChatColor.RESET + ChatColor.YELLOW + "に設定しました。")
                    player.openInventory(MapInventory.editInventory)
                } else {
                    MainAPI.sendMessage(player, ErrorMessage.ARGS_INTEGER)
                    player.openInventory(MapInventory.editInventory)
                }
                null
            })
        } else if (ActionType.ITEM_START_BONE.displayName.equals(inventoryTitle, true)) {
            AnvilGUI(Main.pluginInstance, p, "ゲーム開始時の骨の数を入力…", BiFunction { player, reply ->
                if (ParseUtil.isInt(reply)) {
                    val i = reply.toInt()
                    if (i < 65) {
                        difficultyConfig.getBone(GameType.START).count = i
                        player.sendMessage(MainAPI.getPrefix(PrefixType.WARNING) + difficultyName + ChatColor.YELLOW + "の" + ChatColor.GOLD + "ゲーム開始時の骨の数" + ChatColor.RESET + ChatColor.YELLOW + "を" + ChatColor.GOLD + ChatColor.BOLD + ChatColor.UNDERLINE + i + "個" + ChatColor.RESET + ChatColor.YELLOW + "に設定しました。")
                        player.openInventory(MapInventory.editInventory)
                    } else {
                        player.sendMessage("${MainAPI.getPrefix(PrefixType.ERROR)}引数が不正です。数を64以下で指定してください。")
                        player.openInventory(MapInventory.editInventory)
                    }
                } else {
                    MainAPI.sendMessage(player, ErrorMessage.ARGS_INTEGER)
                    player.openInventory(MapInventory.editInventory)
                }
                null
            })
        } else if (ActionType.ITEM_START_FEATHER.displayName.equals(inventoryTitle, true)) {
            AnvilGUI(Main.pluginInstance, p, "ゲーム開始時の羽の数を入力…", BiFunction { player, reply ->
                if (ParseUtil.isInt(reply)) {
                    val i = reply.toInt()
                    if (i < 65) {
                        difficultyConfig.getFeather(GameType.START).count = i
                        player.sendMessage(MainAPI.getPrefix(PrefixType.WARNING) + difficultyName + ChatColor.YELLOW + "の" + ChatColor.GOLD + "ゲーム開始時の羽の数" + ChatColor.RESET + ChatColor.YELLOW + "を" + ChatColor.GOLD + ChatColor.BOLD + ChatColor.UNDERLINE + i + "個" + ChatColor.RESET + ChatColor.YELLOW + "に設定しました。")
                        player.openInventory(MapInventory.editInventory)
                    } else {
                        player.sendMessage("${MainAPI.getPrefix(PrefixType.ERROR)}引数が不正です。数を64以下で指定してください。")
                        player.openInventory(MapInventory.editInventory)
                    }
                } else {
                    MainAPI.sendMessage(player, ErrorMessage.ARGS_INTEGER)
                    player.openInventory(MapInventory.editInventory)
                }
                null
            })
        } else if (ActionType.ITEM_START_EGG.displayName.equals(inventoryTitle, true)) {
            AnvilGUI(Main.pluginInstance, p, "ゲーム開始時の卵の数を入力…", BiFunction { player, reply ->
                if (ParseUtil.isInt(reply)) {
                    val i = reply.toInt()
                    if (i < 65) {
                        difficultyConfig.getEgg(GameType.START).count = i
                        player.sendMessage(MainAPI.getPrefix(PrefixType.WARNING) + difficultyName + ChatColor.YELLOW + "の" + ChatColor.GOLD + "ゲーム開始時の卵の数" + ChatColor.RESET + ChatColor.YELLOW + "を" + ChatColor.GOLD + ChatColor.BOLD + ChatColor.UNDERLINE + i + "個" + ChatColor.RESET + ChatColor.YELLOW + "に設定しました。")
                        player.openInventory(MapInventory.editInventory)
                    } else {
                        player.sendMessage("${MainAPI.getPrefix(PrefixType.ERROR)}引数が不正です。数を64以下で指定してください。")
                        player.openInventory(MapInventory.editInventory)
                    }
                } else {
                    MainAPI.sendMessage(player, ErrorMessage.ARGS_INTEGER)
                    player.openInventory(MapInventory.editInventory)
                }
                null
            })
        } else if (ActionType.ITEM_RESPAWN_BONE.displayName.equals(inventoryTitle, true)) {
            AnvilGUI(Main.pluginInstance, p, "復活時の骨の数を入力…", BiFunction { player, reply ->
                if (ParseUtil.isInt(reply)) {
                    val i = reply.toInt()
                    if (i < 65) {
                        difficultyConfig.getBone(GameType.RESPAWN).count = i
                        player.sendMessage(MainAPI.getPrefix(PrefixType.WARNING) + difficultyName + ChatColor.YELLOW + "の" + ChatColor.GOLD + "復活時の骨の数" + ChatColor.RESET + ChatColor.YELLOW + "を" + ChatColor.GOLD + ChatColor.BOLD + ChatColor.UNDERLINE + i + "個" + ChatColor.RESET + ChatColor.YELLOW + "に設定しました。")
                        player.openInventory(MapInventory.editInventory)
                    } else {
                        player.sendMessage("${MainAPI.getPrefix(PrefixType.ERROR)}引数が不正です。数を64以下で指定してください。")
                        player.openInventory(MapInventory.editInventory)
                    }
                } else {
                    MainAPI.sendMessage(player, ErrorMessage.ARGS_INTEGER)
                    player.openInventory(MapInventory.editInventory)
                }
                null
            })
        } else if (ActionType.ITEM_RESPAWN_FEATHER.displayName.equals(inventoryTitle, true)) {
            AnvilGUI(Main.pluginInstance, p, "復活時の羽の数を入力…", BiFunction { player, reply ->
                if (ParseUtil.isInt(reply)) {
                    val i = reply.toInt()
                    if (i < 65) {
                        difficultyConfig.getFeather(GameType.RESPAWN).count = i
                        player.sendMessage(MainAPI.getPrefix(PrefixType.WARNING) + difficultyName + ChatColor.YELLOW + "の" + ChatColor.GOLD + "復活時の羽の数" + ChatColor.RESET + ChatColor.YELLOW + "を" + ChatColor.GOLD + ChatColor.BOLD + ChatColor.UNDERLINE + i + "個" + ChatColor.RESET + ChatColor.YELLOW + "に設定しました。")
                        player.openInventory(MapInventory.editInventory)
                    } else {
                        player.sendMessage("${MainAPI.getPrefix(PrefixType.ERROR)}引数が不正です。数を64以下で指定してください。")
                        player.openInventory(MapInventory.editInventory)
                    }
                } else {
                    MainAPI.sendMessage(player, ErrorMessage.ARGS_INTEGER)
                    player.openInventory(MapInventory.editInventory)
                }
                null
            })
        } else if (ActionType.ITEM_RESPAWN_EGG.displayName.equals(inventoryTitle, true)) {
            AnvilGUI(Main.pluginInstance, p, "復活時の卵の数を入力…", BiFunction { player, reply ->
                if (ParseUtil.isInt(reply)) {
                    val i = reply.toInt()
                    if (i < 65) {
                        difficultyConfig.getEgg(GameType.RESPAWN).count = i
                        player.sendMessage(MainAPI.getPrefix(PrefixType.WARNING) + difficultyName + ChatColor.YELLOW + "の" + ChatColor.GOLD + "復活時の卵の数" + ChatColor.RESET + ChatColor.YELLOW + "を" + ChatColor.GOLD + ChatColor.BOLD + ChatColor.UNDERLINE + i + "個" + ChatColor.RESET + ChatColor.YELLOW + "に設定しました。")
                        player.openInventory(MapInventory.editInventory)
                    } else {
                        player.sendMessage("${MainAPI.getPrefix(PrefixType.ERROR)}引数が不正です。数を64以下で指定してください。")
                        player.openInventory(MapInventory.editInventory)
                    }
                } else {
                    MainAPI.sendMessage(player, ErrorMessage.ARGS_INTEGER)
                    player.openInventory(MapInventory.editInventory)
                }
                null
            })
        }
    }
}
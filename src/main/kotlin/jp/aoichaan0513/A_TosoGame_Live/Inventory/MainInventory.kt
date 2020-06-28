package jp.aoichaan0513.A_TosoGame_Live.Inventory

import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.DiscordManager
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.GameManager
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.MoneyManager
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.Player.DifficultyManager
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.Player.PlayerManager
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.World.WorldManager
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.World.WorldManager.GameType
import jp.aoichaan0513.A_TosoGame_Live.API.Scoreboard.Teams
import jp.aoichaan0513.A_TosoGame_Live.API.Scoreboard.Teams.OnlineTeam
import jp.aoichaan0513.A_TosoGame_Live.API.TosoGameAPI
import jp.aoichaan0513.A_TosoGame_Live.Commands.Command.Hunter
import jp.aoichaan0513.A_TosoGame_Live.Commands.Command.Tuho
import jp.aoichaan0513.A_TosoGame_Live.Main
import jp.aoichaan0513.A_TosoGame_Live.Utils.DateTime.TimeFormat
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.PotionMeta
import org.bukkit.inventory.meta.SkullMeta
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import java.util.*

class MainInventory {
    companion object {

        val title = "${ChatColor.DARK_GRAY}> ${ChatColor.BOLD}ホーム"

        private val itemFlags = arrayOf(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS)

        fun getInventory(p: Player): Inventory {
            val inv = Bukkit.createInventory(null, 9 * 6, title)

            val worldConfig = Main.worldConfig

            val itemStackBorder = ItemStack(Material.BLACK_STAINED_GLASS_PANE)
            val itemMetaBorder = itemStackBorder.itemMeta!!
            itemMetaBorder.addItemFlags(*itemFlags)
            itemMetaBorder.setDisplayName("${ChatColor.BOLD}")
            itemStackBorder.itemMeta = itemMetaBorder
            for (i in 9..17)
                inv.setItem(i, itemStackBorder)
            for (i in 45..53)
                inv.setItem(i, itemStackBorder)

            val itemStackPlayerInfo = ItemStack(Item.PLAYER_INFO.material, 1)
            val itemMetaPlayerInfo = (itemStackPlayerInfo.itemMeta as SkullMeta)
            itemMetaPlayerInfo.addItemFlags(*itemFlags)
            itemMetaPlayerInfo.owningPlayer = p
            itemMetaPlayerInfo.setDisplayName("${ChatColor.BLUE}${ChatColor.BOLD}${ChatColor.UNDERLINE}あなたの情報")
            itemMetaPlayerInfo.lore = Arrays.asList(
                    "${ChatColor.BLUE}${ChatColor.UNDERLINE}プレイヤー名${ChatColor.RESET}${ChatColor.GRAY}: ${ChatColor.YELLOW}${p.name}",
                    "${ChatColor.BLUE}${ChatColor.UNDERLINE}権限所持者${ChatColor.RESET}${ChatColor.GRAY}: ${ChatColor.YELLOW}${if (TosoGameAPI.hasPermission(p)) "はい" else "いいえ"}",
                    "${ChatColor.BLUE}${ChatColor.UNDERLINE}配信者${ChatColor.RESET}${ChatColor.GRAY}: ${ChatColor.YELLOW}${if (TosoGameAPI.isBroadCaster(p)) "はい" else "いいえ"}",
                    "",
                    "${ChatColor.BLUE}${ChatColor.UNDERLINE}所持金${ChatColor.RESET}${ChatColor.GRAY}: ${ChatColor.YELLOW}${MoneyManager.formatMoney(PlayerManager.loadConfig(p).money)}${ChatColor.GRAY}円",
                    "",
                    "${ChatColor.BLUE}${ChatColor.UNDERLINE}チーム${ChatColor.RESET}${ChatColor.GRAY}: ${ChatColor.YELLOW}${Teams.getTeamLabel(Teams.DisplaySlot.SIDEBAR, p)}",
                    "${ChatColor.BLUE}${ChatColor.UNDERLINE}難易度${ChatColor.RESET}${ChatColor.GRAY}: ${ChatColor.YELLOW}${worldConfig.getDifficultyConfig(p).difficulty.displayName}",
                    "${ChatColor.BLUE}${ChatColor.UNDERLINE}賞金${ChatColor.RESET}${ChatColor.GRAY}: ${ChatColor.YELLOW}${MoneyManager.formatMoney(MoneyManager.getReward(p))}${ChatColor.GRAY}円 (${MoneyManager.getRate(p)}円/秒)"
            )
            itemStackPlayerInfo.itemMeta = itemMetaPlayerInfo
            inv.setItem(Item.PLAYER_INFO.index, itemStackPlayerInfo)

            if (GameManager.isGame(GameManager.GameState.NONE)) {
                val itemStackPlayerSettings = ItemStack(Item.PLAYER_SETTINGS.material, 1)
                val itemMetaPlayerSettings = itemStackPlayerSettings.itemMeta!!
                itemMetaPlayerSettings.addItemFlags(*itemFlags)
                itemMetaPlayerSettings.setDisplayName("${ChatColor.BLUE}${ChatColor.BOLD}${ChatColor.UNDERLINE}プレイヤー設定")
                itemMetaPlayerSettings.lore = listOf("${ChatColor.GRAY}クリックして${ChatColor.BLUE}${ChatColor.BOLD}${ChatColor.UNDERLINE}プレイヤー設定アプリ${ChatColor.RESET}${ChatColor.GRAY}を開きます。")
                itemStackPlayerSettings.itemMeta = itemMetaPlayerSettings
                inv.setItem(Item.PLAYER_SETTINGS.index, itemStackPlayerSettings)
            }

            if (TosoGameAPI.isAdmin(p)) {
                val itemStackMapSettings = ItemStack(Item.MAP_SETTINGS.material, 1)
                val itemMetaMapSettings = itemStackMapSettings.itemMeta!!
                itemMetaMapSettings.addItemFlags(*itemFlags)
                itemMetaMapSettings.setDisplayName("${ChatColor.BLUE}${ChatColor.BOLD}${ChatColor.UNDERLINE}マップ設定")
                itemMetaMapSettings.lore = listOf("${ChatColor.GRAY}クリックして${ChatColor.BLUE}${ChatColor.BOLD}${ChatColor.UNDERLINE}マップ設定アプリ${ChatColor.RESET}${ChatColor.GRAY}を開きます。")
                itemStackMapSettings.itemMeta = itemMetaMapSettings
                inv.setItem(Item.MAP_SETTINGS.index, itemStackMapSettings)
            }

            val itemStackNotification = ItemStack(Item.NOTIFICATION.material)
            val itemMetaNotification = itemStackNotification.itemMeta!!
            itemMetaNotification.addItemFlags(*itemFlags)
            itemMetaNotification.setDisplayName("${ChatColor.BLUE}${ChatColor.BOLD}${ChatColor.UNDERLINE}通知")
            itemMetaNotification.lore = listOf("${ChatColor.GRAY}クリックして${ChatColor.BLUE}${ChatColor.BOLD}${ChatColor.UNDERLINE}通知メニュー${ChatColor.RESET}${ChatColor.GRAY}を開きます。")
            itemStackNotification.itemMeta = itemMetaNotification
            inv.setItem(Item.NOTIFICATION.index, itemStackNotification)

            // 電話アプリ
            val itemStackCall = ItemStack(Item.CALL_APP.material, 1)
            val itemMetaCall = itemStackCall.itemMeta!!
            itemMetaCall.addItemFlags(*itemFlags)
            itemMetaCall.setCustomModelData(1001)
            itemMetaCall.setDisplayName("${ChatColor.GOLD}${ChatColor.BOLD}${ChatColor.UNDERLINE}Coming soon...")
            itemMetaCall.lore = listOf()
            // itemMetaCall.lore = listOf("${ChatColor.YELLOW}クリックして${ChatColor.GOLD}${ChatColor.BOLD}${ChatColor.UNDERLINE}電話アプリ${ChatColor.RESET}${ChatColor.YELLOW}を開きます。")
            itemStackCall.itemMeta = itemMetaCall
            inv.setItem(Item.CALL_APP.index, itemStackCall)

            // ミッションアプリ
            val itemStackMission = ItemStack(Item.MISSION_APP.material, 1)
            val itemMetaMission = itemStackMission.itemMeta!!
            itemMetaMission.addItemFlags(*itemFlags)
            itemMetaMission.setDisplayName("${ChatColor.GOLD}${ChatColor.BOLD}${ChatColor.UNDERLINE}ミッション")
            itemMetaMission.lore = listOf("${ChatColor.YELLOW}クリックして${ChatColor.GOLD}${ChatColor.BOLD}${ChatColor.UNDERLINE}ミッションアプリ${ChatColor.RESET}${ChatColor.YELLOW}を開きます。")
            itemStackMission.itemMeta = itemMetaMission
            inv.setItem(Item.MISSION_APP.index, itemStackMission)

            // マップアプリ
            val itemStackMap = ItemStack(Item.MAP_APP.material, 1)
            val itemMetaMap = itemStackMap.itemMeta!!
            itemMetaMap.addItemFlags(*itemFlags)
            itemMetaMap.setDisplayName("${ChatColor.GOLD}${ChatColor.BOLD}${ChatColor.UNDERLINE}マップ")
            itemMetaMap.lore = listOf("${ChatColor.YELLOW}クリックして${ChatColor.GOLD}${ChatColor.BOLD}${ChatColor.UNDERLINE}マップアプリ${ChatColor.RESET}${ChatColor.YELLOW}を開きます。")
            itemStackMap.itemMeta = itemMetaMap
            inv.setItem(Item.MAP_APP.index, itemStackMap)

            // 観戦モード切り替え
            val itemStackSpec = ItemStack(Item.SPEC_MODE.material, 1)
            val itemMetaSpec = (itemStackSpec.itemMeta as PotionMeta)
            itemMetaSpec.addItemFlags(*itemFlags)
            if (Teams.hasJoinedTeam(OnlineTeam.TOSO_JAIL, p))
                itemMetaSpec.addCustomEffect(PotionEffect(PotionEffectType.INVISIBILITY, 200, 1), true)
            itemMetaSpec.setDisplayName("${ChatColor.GOLD}${ChatColor.BOLD}${ChatColor.UNDERLINE}観戦モード切り替え")
            itemMetaSpec.lore = listOf(
                    "${ChatColor.YELLOW}クリックして${ChatColor.GOLD}${ChatColor.BOLD}${ChatColor.UNDERLINE}観戦モード${ChatColor.RESET}${ChatColor.YELLOW}を切り替えます。",
                    "${ChatColor.GRAY}この機能は${ChatColor.BOLD}${ChatColor.UNDERLINE}${TimeFormat.formatJapan(worldConfig.gameConfig.respawnDeny)}以下${ChatColor.RESET}${ChatColor.GRAY}・${ChatColor.BOLD}${ChatColor.UNDERLINE}牢獄${ChatColor.RESET}${ChatColor.GRAY}にいる場合のみ使用可能です。"
            )
            itemStackSpec.itemMeta = itemMetaSpec
            inv.setItem(Item.SPEC_MODE.index, itemStackSpec)

            // Discord 連携
            val itemStackDiscordIntegration = ItemStack(Item.DISCORD_INTEGRATION.material, 1)
            val itemMetaDiscordIntegration = itemStackDiscordIntegration.itemMeta!!
            itemMetaDiscordIntegration.addItemFlags(*itemFlags)
            if (DiscordManager.integrationMap.containsKey(p.uniqueId))
                itemMetaDiscordIntegration.addEnchant(Enchantment.DURABILITY, 1, true)
            itemMetaDiscordIntegration.setDisplayName("${ChatColor.GOLD}${ChatColor.BOLD}${ChatColor.UNDERLINE}Discord連携")
            itemMetaDiscordIntegration.lore = listOf(
                    "${ChatColor.YELLOW}クリックして${ChatColor.GOLD}${ChatColor.BOLD}${ChatColor.UNDERLINE}Discordとの連携${ChatColor.RESET}${ChatColor.YELLOW}を行います。",
                    "${ChatColor.GRAY}この機能は${ChatColor.BOLD}${ChatColor.UNDERLINE}ゲーム準備中${ChatColor.RESET}${ChatColor.GRAY}のみ使用可能です。",
                    "",
                    "${ChatColor.GOLD}${ChatColor.UNDERLINE}ステータス${ChatColor.RESET}${ChatColor.GRAY}: ${ChatColor.YELLOW}${if (DiscordManager.integrationMap.containsKey(p.uniqueId)) "連携済み" else "未連携"}"
            )
            itemStackDiscordIntegration.itemMeta = itemMetaDiscordIntegration
            inv.setItem(Item.DISCORD_INTEGRATION.index, itemStackDiscordIntegration)


            // ハンター抽選
            val itemStackHunter = ItemStack(Item.REQUEST_HUNTER.material, 1)
            val itemMetaHunter = itemStackHunter.itemMeta!!
            itemMetaHunter.addItemFlags(*itemFlags)
            if (Hunter.num > 0 && Main.hunterShuffleSet.contains(p.uniqueId))
                itemMetaHunter.addEnchant(Enchantment.DURABILITY, 1, true)
            itemMetaHunter.setDisplayName("${ChatColor.AQUA}${ChatColor.BOLD}${ChatColor.UNDERLINE}ハンター抽選に応募")
            itemMetaHunter.lore = listOf(
                    "${ChatColor.YELLOW}クリックして${ChatColor.AQUA}${ChatColor.BOLD}${ChatColor.UNDERLINE}ハンター抽選${ChatColor.RESET}${ChatColor.YELLOW}を切り替えます。",
                    "${ChatColor.GRAY}この機能は${ChatColor.BOLD}${ChatColor.UNDERLINE}ハンター抽選が実行${ChatColor.RESET}${ChatColor.GRAY}されている場合のみ使用可能です。",
                    "",
                    "${ChatColor.UNDERLINE}${if (Hunter.num > 0) if (Main.hunterShuffleSet.contains(p.uniqueId)) "${ChatColor.GOLD}抽選に応募しています。" else "${ChatColor.YELLOW}抽選に応募していません。" else "${ChatColor.RED}現在使用できません。"}"
            )
            itemStackHunter.itemMeta = itemMetaHunter
            inv.setItem(Item.REQUEST_HUNTER.index, itemStackHunter)

            // 通報部隊抽選
            val itemStackTuho = ItemStack(Item.REQUEST_TUHO.material, 1)
            val itemMetaTuho = itemStackTuho.itemMeta!!
            itemMetaTuho.addItemFlags(*itemFlags)
            if (Tuho.num > 0 && Main.tuhoShuffleSet.contains(p.uniqueId))
                itemMetaTuho.addEnchant(Enchantment.DURABILITY, 1, true)
            itemMetaTuho.setDisplayName("${ChatColor.AQUA}${ChatColor.BOLD}${ChatColor.UNDERLINE}通報部隊抽選に応募")
            itemMetaTuho.lore = listOf(
                    "${ChatColor.YELLOW}クリックして${ChatColor.AQUA}${ChatColor.BOLD}${ChatColor.UNDERLINE}通報部隊抽選${ChatColor.RESET}${ChatColor.YELLOW}を切り替えます。",
                    "${ChatColor.GRAY}この機能は${ChatColor.BOLD}${ChatColor.UNDERLINE}通報部隊抽選が実行${ChatColor.RESET}${ChatColor.GRAY}されている場合のみ使用可能です。",
                    "",
                    "${ChatColor.UNDERLINE}${if (Tuho.num > 0) if (Main.tuhoShuffleSet.contains(p.uniqueId)) "${ChatColor.GOLD}抽選に応募しています。" else "${ChatColor.YELLOW}抽選に応募していません。" else "${ChatColor.RED}現在使用できません。"}"
            )
            itemStackTuho.itemMeta = itemMetaTuho
            inv.setItem(Item.REQUEST_TUHO.index, itemStackTuho)

            val difficultyEasyConfig = worldConfig.getDifficultyConfig(WorldManager.Difficulty.EASY)
            val itemStackDifficultyEasy = ItemStack(Item.DIFFICULTY_EASY.material, 1)
            val itemMetaDifficultyEasy = itemStackDifficultyEasy.itemMeta!!
            itemMetaDifficultyEasy.addItemFlags(*itemFlags)
            if (DifficultyManager.isDifficulty(p) && DifficultyManager.getDifficulty(p) == WorldManager.Difficulty.EASY)
                itemMetaDifficultyEasy.addEnchant(Enchantment.DURABILITY, 1, true)
            itemMetaDifficultyEasy.setDisplayName("${ChatColor.GREEN}${ChatColor.BOLD}${ChatColor.UNDERLINE}${ChatColor.stripColor(WorldManager.Difficulty.EASY.displayName)}")
            itemMetaDifficultyEasy.lore = listOf(
                    "${ChatColor.YELLOW}クリックして難易度を${ChatColor.GREEN}${ChatColor.BOLD}${ChatColor.UNDERLINE}${ChatColor.stripColor(WorldManager.Difficulty.EASY.displayName)}${ChatColor.RESET}${ChatColor.YELLOW}に切り替えます。",
                    "${ChatColor.GRAY}この機能は${ChatColor.BOLD}${ChatColor.UNDERLINE}ゲーム準備中${ChatColor.RESET}${ChatColor.GRAY}のみ使用可能です。",
                    "",
                    "${ChatColor.BLUE}${ChatColor.UNDERLINE}レート${ChatColor.RESET}${ChatColor.GRAY}: ${ChatColor.YELLOW}${difficultyEasyConfig.rate}${ChatColor.GRAY}円/秒",
                    "",
                    "${ChatColor.BLUE}${ChatColor.UNDERLINE}体力システム${ChatColor.RESET}${ChatColor.GRAY}: ${ChatColor.YELLOW}${if (difficultyEasyConfig.health) "有効" else "無効"}",
                    "${ChatColor.BLUE}${ChatColor.UNDERLINE}自動復活${ChatColor.RESET}${ChatColor.GRAY}: ${ChatColor.YELLOW}${if (difficultyEasyConfig.respawnAutoTime > -1) TimeFormat.formatJapan(difficultyEasyConfig.respawnAutoTime) else "無効"}",
                    "${ChatColor.BLUE}${ChatColor.UNDERLINE}復活可能回数${ChatColor.RESET}${ChatColor.GRAY}: ${ChatColor.YELLOW}${difficultyEasyConfig.respawnDenyCount}${ChatColor.GRAY}回",
                    "${ChatColor.BLUE}${ChatColor.UNDERLINE}復活クールタイム${ChatColor.RESET}${ChatColor.GRAY}: ${ChatColor.YELLOW}${TimeFormat.formatJapan(difficultyEasyConfig.respawnCoolTime)}${ChatColor.GRAY}/回",
                    "",
                    "${ChatColor.BLUE}${ChatColor.UNDERLINE}ゲーム開始時のアイテム",
                    "${ChatColor.BLUE}骨 (透明化)${ChatColor.RESET}${ChatColor.GRAY}: ${ChatColor.YELLOW}${difficultyEasyConfig.getBone(GameType.START).count}${ChatColor.GRAY}個 (${difficultyEasyConfig.getBone(GameType.START).duration}秒)",
                    "${ChatColor.BLUE}羽 (移動速度上昇)${ChatColor.RESET}${ChatColor.GRAY}: ${ChatColor.YELLOW}${difficultyEasyConfig.getFeather(GameType.START).count}${ChatColor.GRAY}個 (${difficultyEasyConfig.getFeather(GameType.START).duration}秒)",
                    "${ChatColor.BLUE}卵 (盲目・移動速度低下)${ChatColor.RESET}${ChatColor.GRAY}: ${ChatColor.YELLOW}${difficultyEasyConfig.getEgg(GameType.START).count}${ChatColor.GRAY}個",
                    "",
                    "${ChatColor.BLUE}${ChatColor.UNDERLINE}復活時のアイテム",
                    "${ChatColor.BLUE}骨 (透明化)${ChatColor.RESET}${ChatColor.GRAY}: ${ChatColor.YELLOW}${difficultyEasyConfig.getBone(GameType.RESPAWN).count}${ChatColor.GRAY}個 (${difficultyEasyConfig.getBone(GameType.RESPAWN).duration}秒)",
                    "${ChatColor.BLUE}羽 (移動速度上昇)${ChatColor.RESET}${ChatColor.GRAY}: ${ChatColor.YELLOW}${difficultyEasyConfig.getFeather(GameType.RESPAWN).count}${ChatColor.GRAY}個 (${difficultyEasyConfig.getFeather(GameType.RESPAWN).duration}秒)",
                    "${ChatColor.BLUE}卵 (盲目・移動速度低下)${ChatColor.RESET}${ChatColor.GRAY}: ${ChatColor.YELLOW}${difficultyEasyConfig.getEgg(GameType.RESPAWN).count}${ChatColor.GRAY}個"
            )
            itemStackDifficultyEasy.itemMeta = itemMetaDifficultyEasy
            inv.setItem(Item.DIFFICULTY_EASY.index, itemStackDifficultyEasy)

            val difficultyNormalConfig = worldConfig.getDifficultyConfig(WorldManager.Difficulty.NORMAL)
            val itemStackDifficultyNormal = ItemStack(Item.DIFFICULTY_NORMAL.material, 1)
            val itemMetaDifficultyNormal = itemStackDifficultyNormal.itemMeta!!
            itemMetaDifficultyNormal.addItemFlags(*itemFlags)
            if (DifficultyManager.isDifficulty(p) && DifficultyManager.getDifficulty(p) == WorldManager.Difficulty.NORMAL)
                itemMetaDifficultyNormal.addEnchant(Enchantment.DURABILITY, 1, true)
            itemMetaDifficultyNormal.setDisplayName("${ChatColor.YELLOW}${ChatColor.BOLD}${ChatColor.UNDERLINE}${ChatColor.stripColor(WorldManager.Difficulty.NORMAL.displayName)}")
            itemMetaDifficultyNormal.lore = listOf(
                    "${ChatColor.YELLOW}クリックして難易度を${ChatColor.YELLOW}${ChatColor.BOLD}${ChatColor.UNDERLINE}${ChatColor.stripColor(WorldManager.Difficulty.NORMAL.displayName)}${ChatColor.RESET}${ChatColor.YELLOW}に切り替えます。",
                    "${ChatColor.GRAY}この機能は${ChatColor.BOLD}${ChatColor.UNDERLINE}ゲーム準備中${ChatColor.RESET}${ChatColor.GRAY}のみ使用可能です。",
                    "",
                    "${ChatColor.BLUE}${ChatColor.UNDERLINE}レート${ChatColor.RESET}${ChatColor.GRAY}: ${ChatColor.YELLOW}${difficultyNormalConfig.rate}${ChatColor.GRAY}円/秒",
                    "",
                    "${ChatColor.BLUE}${ChatColor.UNDERLINE}体力システム${ChatColor.RESET}${ChatColor.GRAY}: ${ChatColor.YELLOW}${if (difficultyNormalConfig.health) "有効" else "無効"}",
                    "${ChatColor.BLUE}${ChatColor.UNDERLINE}自動復活${ChatColor.RESET}${ChatColor.GRAY}: ${ChatColor.YELLOW}${if (difficultyNormalConfig.respawnAutoTime > -1) TimeFormat.formatJapan(difficultyNormalConfig.respawnAutoTime) else "無効"}",
                    "${ChatColor.BLUE}${ChatColor.UNDERLINE}復活可能回数${ChatColor.RESET}${ChatColor.GRAY}: ${ChatColor.YELLOW}${difficultyNormalConfig.respawnDenyCount}${ChatColor.GRAY}回",
                    "${ChatColor.BLUE}${ChatColor.UNDERLINE}復活クールタイム${ChatColor.RESET}${ChatColor.GRAY}: ${ChatColor.YELLOW}${TimeFormat.formatJapan(difficultyNormalConfig.respawnCoolTime)}${ChatColor.GRAY}/回",
                    "",
                    "${ChatColor.BLUE}${ChatColor.UNDERLINE}ゲーム開始時のアイテム",
                    "${ChatColor.BLUE}骨 (透明化)${ChatColor.RESET}${ChatColor.GRAY}: ${ChatColor.YELLOW}${difficultyNormalConfig.getBone(GameType.START).count}${ChatColor.GRAY}個 (${difficultyNormalConfig.getBone(GameType.START).duration}秒)",
                    "${ChatColor.BLUE}羽 (移動速度上昇)${ChatColor.RESET}${ChatColor.GRAY}: ${ChatColor.YELLOW}${difficultyNormalConfig.getFeather(GameType.START).count}${ChatColor.GRAY}個 (${difficultyNormalConfig.getFeather(GameType.START).duration}秒)",
                    "${ChatColor.BLUE}卵 (盲目・移動速度低下)${ChatColor.RESET}${ChatColor.GRAY}: ${ChatColor.YELLOW}${difficultyNormalConfig.getEgg(GameType.START).count}${ChatColor.GRAY}個",
                    "",
                    "${ChatColor.BLUE}${ChatColor.UNDERLINE}復活時のアイテム",
                    "${ChatColor.BLUE}骨 (透明化)${ChatColor.RESET}${ChatColor.GRAY}: ${ChatColor.YELLOW}${difficultyNormalConfig.getBone(GameType.RESPAWN).count}${ChatColor.GRAY}個 (${difficultyNormalConfig.getBone(GameType.RESPAWN).duration}秒)",
                    "${ChatColor.BLUE}羽 (移動速度上昇)${ChatColor.RESET}${ChatColor.GRAY}: ${ChatColor.YELLOW}${difficultyNormalConfig.getFeather(GameType.RESPAWN).count}${ChatColor.GRAY}個 (${difficultyNormalConfig.getFeather(GameType.RESPAWN).duration}秒)",
                    "${ChatColor.BLUE}卵 (盲目・移動速度低下)${ChatColor.RESET}${ChatColor.GRAY}: ${ChatColor.YELLOW}${difficultyNormalConfig.getEgg(GameType.RESPAWN).count}${ChatColor.GRAY}個"
            )
            itemStackDifficultyNormal.itemMeta = itemMetaDifficultyNormal
            inv.setItem(Item.DIFFICULTY_NORMAL.index, itemStackDifficultyNormal)

            val difficultyHardConfig = worldConfig.getDifficultyConfig(WorldManager.Difficulty.HARD)
            val itemStackDifficultyHard = ItemStack(Item.DIFFICULTY_HARD.material, 1)
            val itemMetaDifficultyHard = itemStackDifficultyHard.itemMeta!!
            itemMetaDifficultyHard.addItemFlags(*itemFlags)
            if (DifficultyManager.isDifficulty(p) && DifficultyManager.getDifficulty(p) == WorldManager.Difficulty.HARD)
                itemMetaDifficultyHard.addEnchant(Enchantment.DURABILITY, 1, true)
            itemMetaDifficultyHard.setDisplayName("${ChatColor.RED}${ChatColor.BOLD}${ChatColor.UNDERLINE}${ChatColor.stripColor(WorldManager.Difficulty.HARD.displayName)}")
            itemMetaDifficultyHard.lore = listOf(
                    "${ChatColor.YELLOW}クリックして難易度を${ChatColor.RED}${ChatColor.BOLD}${ChatColor.UNDERLINE}${ChatColor.stripColor(WorldManager.Difficulty.HARD.displayName)}${ChatColor.RESET}${ChatColor.YELLOW}に切り替えます。",
                    "${ChatColor.GRAY}この機能は${ChatColor.BOLD}${ChatColor.UNDERLINE}ゲーム準備中${ChatColor.RESET}${ChatColor.GRAY}のみ使用可能です。",
                    "",
                    "${ChatColor.BLUE}${ChatColor.UNDERLINE}レート${ChatColor.RESET}${ChatColor.GRAY}: ${ChatColor.YELLOW}${difficultyHardConfig.rate}${ChatColor.GRAY}円/秒",
                    "",
                    "${ChatColor.BLUE}${ChatColor.UNDERLINE}体力システム${ChatColor.RESET}${ChatColor.GRAY}: ${ChatColor.YELLOW}${if (difficultyHardConfig.health) "有効" else "無効"}",
                    "${ChatColor.BLUE}${ChatColor.UNDERLINE}自動復活${ChatColor.RESET}${ChatColor.GRAY}: ${ChatColor.YELLOW}${if (difficultyHardConfig.respawnAutoTime > -1) TimeFormat.formatJapan(difficultyHardConfig.respawnAutoTime) else "無効"}",
                    "${ChatColor.BLUE}${ChatColor.UNDERLINE}復活可能回数${ChatColor.RESET}${ChatColor.GRAY}: ${ChatColor.YELLOW}${difficultyHardConfig.respawnDenyCount}${ChatColor.GRAY}回",
                    "${ChatColor.BLUE}${ChatColor.UNDERLINE}復活クールタイム${ChatColor.RESET}${ChatColor.GRAY}: ${ChatColor.YELLOW}${TimeFormat.formatJapan(difficultyHardConfig.respawnCoolTime)}${ChatColor.GRAY}/回",
                    "",
                    "${ChatColor.BLUE}${ChatColor.UNDERLINE}ゲーム開始時のアイテム",
                    "${ChatColor.BLUE}骨 (透明化)${ChatColor.RESET}${ChatColor.GRAY}: ${ChatColor.YELLOW}${difficultyHardConfig.getBone(GameType.START).count}${ChatColor.GRAY}個 (${difficultyHardConfig.getBone(GameType.START).duration}秒)",
                    "${ChatColor.BLUE}羽 (移動速度上昇)${ChatColor.RESET}${ChatColor.GRAY}: ${ChatColor.YELLOW}${difficultyHardConfig.getFeather(GameType.START).count}${ChatColor.GRAY}個 (${difficultyHardConfig.getFeather(GameType.START).duration}秒)",
                    "${ChatColor.BLUE}卵 (盲目・移動速度低下)${ChatColor.RESET}${ChatColor.GRAY}: ${ChatColor.YELLOW}${difficultyHardConfig.getEgg(GameType.START).count}${ChatColor.GRAY}個",
                    "",
                    "${ChatColor.BLUE}${ChatColor.UNDERLINE}復活時のアイテム",
                    "${ChatColor.BLUE}骨 (透明化)${ChatColor.RESET}${ChatColor.GRAY}: ${ChatColor.YELLOW}${difficultyHardConfig.getBone(GameType.RESPAWN).count}${ChatColor.GRAY}個 (${difficultyHardConfig.getBone(GameType.RESPAWN).duration}秒)",
                    "${ChatColor.BLUE}羽 (移動速度上昇)${ChatColor.RESET}${ChatColor.GRAY}: ${ChatColor.YELLOW}${difficultyHardConfig.getFeather(GameType.RESPAWN).count}${ChatColor.GRAY}個 (${difficultyHardConfig.getFeather(GameType.RESPAWN).duration}秒)",
                    "${ChatColor.BLUE}卵 (盲目・移動速度低下)${ChatColor.RESET}${ChatColor.GRAY}: ${ChatColor.YELLOW}${difficultyHardConfig.getEgg(GameType.RESPAWN).count}${ChatColor.GRAY}個"
            )
            itemStackDifficultyHard.itemMeta = itemMetaDifficultyHard
            inv.setItem(Item.DIFFICULTY_HARD.index, itemStackDifficultyHard)

            val difficultyHardCoreConfig = worldConfig.getDifficultyConfig(WorldManager.Difficulty.HARDCORE)
            val itemStackDifficultyHardCore = ItemStack(Item.DIFFICULTY_HARDCORE.material, 1)
            val itemMetaDifficultyHardCore = itemStackDifficultyHardCore.itemMeta!!
            itemMetaDifficultyHardCore.addItemFlags(*itemFlags)
            if (DifficultyManager.isDifficulty(p) && DifficultyManager.getDifficulty(p) == WorldManager.Difficulty.HARDCORE)
                itemMetaDifficultyHardCore.addEnchant(Enchantment.DURABILITY, 1, true)
            itemMetaDifficultyHardCore.setDisplayName("${ChatColor.DARK_RED}${ChatColor.BOLD}${ChatColor.UNDERLINE}${ChatColor.stripColor(WorldManager.Difficulty.HARDCORE.displayName)}")
            itemMetaDifficultyHardCore.lore = listOf(
                    "${ChatColor.YELLOW}クリックして難易度を${ChatColor.DARK_RED}${ChatColor.BOLD}${ChatColor.UNDERLINE}${ChatColor.stripColor(WorldManager.Difficulty.HARDCORE.displayName)}${ChatColor.RESET}${ChatColor.YELLOW}に切り替えます。",
                    "${ChatColor.GRAY}この機能は${ChatColor.BOLD}${ChatColor.UNDERLINE}ゲーム準備中${ChatColor.RESET}${ChatColor.GRAY}のみ使用可能です。",
                    "",
                    "${ChatColor.BLUE}${ChatColor.UNDERLINE}レート${ChatColor.RESET}${ChatColor.GRAY}: ${ChatColor.YELLOW}${difficultyHardCoreConfig.rate}${ChatColor.GRAY}円/秒",
                    "",
                    "${ChatColor.BLUE}${ChatColor.UNDERLINE}体力システム${ChatColor.RESET}${ChatColor.GRAY}: ${ChatColor.YELLOW}${if (difficultyHardCoreConfig.health) "有効" else "無効"}",
                    "${ChatColor.BLUE}${ChatColor.UNDERLINE}自動復活${ChatColor.RESET}${ChatColor.GRAY}: ${ChatColor.YELLOW}${if (difficultyHardCoreConfig.respawnAutoTime > -1) TimeFormat.formatJapan(difficultyHardCoreConfig.respawnAutoTime) else "無効"}",
                    "${ChatColor.BLUE}${ChatColor.UNDERLINE}復活可能回数${ChatColor.RESET}${ChatColor.GRAY}: ${ChatColor.YELLOW}${difficultyHardCoreConfig.respawnDenyCount}${ChatColor.GRAY}回",
                    "${ChatColor.BLUE}${ChatColor.UNDERLINE}復活クールタイム${ChatColor.RESET}${ChatColor.GRAY}: ${ChatColor.YELLOW}${TimeFormat.formatJapan(difficultyHardCoreConfig.respawnCoolTime)}${ChatColor.GRAY}/回",
                    "",
                    "${ChatColor.BLUE}${ChatColor.UNDERLINE}ゲーム開始時のアイテム",
                    "${ChatColor.BLUE}骨 (透明化)${ChatColor.RESET}${ChatColor.GRAY}: ${ChatColor.YELLOW}${difficultyHardCoreConfig.getBone(GameType.START).count}${ChatColor.GRAY}個 (${difficultyHardCoreConfig.getBone(GameType.START).duration}秒)",
                    "${ChatColor.BLUE}羽 (移動速度上昇)${ChatColor.RESET}${ChatColor.GRAY}: ${ChatColor.YELLOW}${difficultyHardCoreConfig.getFeather(GameType.START).count}${ChatColor.GRAY}個 (${difficultyHardCoreConfig.getFeather(GameType.START).duration}秒)",
                    "${ChatColor.BLUE}卵 (盲目・移動速度低下)${ChatColor.RESET}${ChatColor.GRAY}: ${ChatColor.YELLOW}${difficultyHardCoreConfig.getEgg(GameType.START).count}${ChatColor.GRAY}個",
                    "",
                    "${ChatColor.BLUE}${ChatColor.UNDERLINE}復活時のアイテム",
                    "${ChatColor.BLUE}骨 (透明化)${ChatColor.RESET}${ChatColor.GRAY}: ${ChatColor.YELLOW}${difficultyHardCoreConfig.getBone(GameType.RESPAWN).count}${ChatColor.GRAY}個 (${difficultyHardCoreConfig.getBone(GameType.RESPAWN).duration}秒)",
                    "${ChatColor.BLUE}羽 (移動速度上昇)${ChatColor.RESET}${ChatColor.GRAY}: ${ChatColor.YELLOW}${difficultyHardCoreConfig.getFeather(GameType.RESPAWN).count}${ChatColor.GRAY}個 (${difficultyHardCoreConfig.getFeather(GameType.RESPAWN).duration}秒)",
                    "${ChatColor.BLUE}卵 (盲目・移動速度低下)${ChatColor.RESET}${ChatColor.GRAY}: ${ChatColor.YELLOW}${difficultyHardCoreConfig.getEgg(GameType.RESPAWN).count}${ChatColor.GRAY}個"
            )
            itemStackDifficultyHardCore.itemMeta = itemMetaDifficultyHardCore
            inv.setItem(Item.DIFFICULTY_HARDCORE.index, itemStackDifficultyHardCore)
            return inv
        }
    }

    enum class Item(private val rawX: Int, private val rawY: Int, val material: Material) {
        PLAYER_INFO(1, 1, Material.PLAYER_HEAD),
        PLAYER_SETTINGS(2, 1, Material.IRON_PICKAXE),
        MAP_SETTINGS(3, 1, Material.COMPASS),
        NOTIFICATION(9, 1, Material.BELL),

        CALL_APP(1, 3, Material.BOOK),
        MISSION_APP(2, 3, Material.BOOK),
        MAP_APP(3, 3, Material.FILLED_MAP),
        SPEC_MODE(5, 3, Material.POTION),
        DISCORD_INTEGRATION(7, 3, if (GameManager.isGame()) Material.BLUE_WOOL else Material.GRAY_WOOL),
        ADVANCEMENT(9, 3, Material.CHEST),

        REQUEST_HUNTER(1, 5, if (Hunter.num > 0) Material.DIAMOND_SWORD else Material.STONE_SWORD),
        REQUEST_TUHO(2, 5, if (Tuho.num > 0) Material.GOLDEN_SWORD else Material.STONE_SWORD),
        DIFFICULTY_EASY(6, 5, Material.LIME_CONCRETE),
        DIFFICULTY_NORMAL(7, 5, Material.YELLOW_CONCRETE),
        DIFFICULTY_HARD(8, 5, Material.RED_CONCRETE),
        DIFFICULTY_HARDCORE(9, 5, Material.RED_NETHER_BRICKS),

        UNKNOWN(0, 0, Material.AIR);

        val index: Int
            get() = x + y

        val x: Int
            get() = MainAPI.makePositive(rawX - 1)

        val y: Int
            get() = MainAPI.makePositive(rawY - 1) * 9

        companion object {

            fun getItem(itemStack: ItemStack, slot: Int): Item {
                return getItem(itemStack.type, slot)
            }

            fun getItem(material: Material, slot: Int): Item {
                return values().firstOrNull { it.index == slot && it.material == material } ?: UNKNOWN
            }
        }
    }
}
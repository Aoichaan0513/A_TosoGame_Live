package jp.aoichaan0513.A_TosoGame_Live.Inventory

import jp.aoichaan0513.A_TosoGame_Live.API.Manager.World.WorldManager
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.World.WorldManager.GameType
import jp.aoichaan0513.A_TosoGame_Live.Main
import jp.aoichaan0513.A_TosoGame_Live.Utils.DateTime.TimeFormat
import jp.aoichaan0513.A_TosoGame_Live.Utils.ItemUtil
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import java.io.File
import java.util.*

class MapInventory {
    companion object {

        val editTitle = "${ChatColor.DARK_GRAY}> ${ChatColor.BOLD}ホーム${ChatColor.RESET}${ChatColor.DARK_GRAY} > ${ChatColor.BOLD}マップ設定"
        val difficultyTitle = "${ChatColor.DARK_GRAY}> ${ChatColor.BOLD}マップ設定${ChatColor.RESET}${ChatColor.DARK_GRAY} > ${ChatColor.BOLD}難易度選択${ChatColor.RESET}${ChatColor.DARK_GRAY} > "
        val listTitle = "${ChatColor.DARK_GRAY}> ${ChatColor.BOLD}ホーム${ChatColor.RESET}${ChatColor.DARK_GRAY} > ${ChatColor.BOLD}マップリスト"


        val editInventory: Inventory
            get() {
                val inv = Bukkit.createInventory(null, 9 * 6, editTitle)

                val worldConfig = Main.worldConfig

                val difficultyEasyConfig = worldConfig.getDifficultyConfig(WorldManager.Difficulty.EASY)
                val difficultyNormalConfig = worldConfig.getDifficultyConfig(WorldManager.Difficulty.NORMAL)
                val difficultyHardConfig = worldConfig.getDifficultyConfig(WorldManager.Difficulty.HARD)
                val difficultyHardCoreConfig = worldConfig.getDifficultyConfig(WorldManager.Difficulty.HARDCORE)

                val itemStack1 = ItemStack(worldConfig.mapConfig.icon, 1)
                val itemMeta1 = itemStack1.itemMeta!!
                itemMeta1.addEnchant(Enchantment.DURABILITY, 0, true)
                itemMeta1.addItemFlags(ItemFlag.HIDE_ENCHANTS)
                itemMeta1.setDisplayName("${ChatColor.BOLD}${ChatColor.ITALIC}${WorldManager.world.name}")
                itemMeta1.lore = Arrays.asList(
                        "${ChatColor.GOLD}${ChatColor.UNDERLINE}マップ名${ChatColor.RESET}${ChatColor.GRAY}: ${ChatColor.YELLOW}${worldConfig.mapConfig.name}",
                        "${ChatColor.GOLD}${ChatColor.UNDERLINE}マップバージョン${ChatColor.RESET}${ChatColor.GRAY}: ${ChatColor.YELLOW}${worldConfig.mapConfig.version}",
                        "${ChatColor.GOLD}${ChatColor.UNDERLINE}マップ製作者${ChatColor.RESET}${ChatColor.GRAY}: ${ChatColor.YELLOW}${worldConfig.mapConfig.authors}"
                )
                itemStack1.itemMeta = itemMeta1
                inv.setItem(0, itemStack1)

                val itemStack21 = ItemStack(Material.PAPER, 1)
                val itemMeta21 = itemStack21.itemMeta!!
                itemMeta21.setDisplayName("${ChatColor.BOLD}復活可能回数")
                itemMeta21.lore = Arrays.asList(
                        "${ChatColor.GOLD}${ChatColor.UNDERLINE}イージー${ChatColor.RESET}${ChatColor.GRAY}: ${ChatColor.YELLOW}${difficultyEasyConfig.respawnDenyCount}${ChatColor.GRAY}回",
                        "${ChatColor.GOLD}${ChatColor.UNDERLINE}ノーマル${ChatColor.RESET}${ChatColor.GRAY}: ${ChatColor.YELLOW}${difficultyNormalConfig.respawnDenyCount}${ChatColor.GRAY}回",
                        "${ChatColor.GOLD}${ChatColor.UNDERLINE}ハード${ChatColor.RESET}${ChatColor.GRAY}: ${ChatColor.YELLOW}${difficultyHardConfig.respawnDenyCount}${ChatColor.GRAY}回",
                        "${ChatColor.GOLD}${ChatColor.UNDERLINE}ハードコア${ChatColor.RESET}${ChatColor.GRAY}: ${ChatColor.YELLOW}${difficultyHardCoreConfig.respawnDenyCount}${ChatColor.GRAY}回",
                        "",
                        "${ChatColor.GOLD}クリックで設定変更"
                )
                itemStack21.itemMeta = itemMeta21
                inv.setItem(2, itemStack21)

                val itemStack31 = ItemStack(Material.PAPER, 1)
                val itemMeta31 = itemStack31.itemMeta!!
                itemMeta31.setDisplayName("${ChatColor.BOLD}復活クールタイム")
                itemMeta31.lore = Arrays.asList(
                        "${ChatColor.GOLD}${ChatColor.UNDERLINE}イージー${ChatColor.RESET}${ChatColor.GRAY}: ${ChatColor.YELLOW}${TimeFormat.formatJapan(difficultyEasyConfig.respawnCoolTime)}${ChatColor.GRAY}/回",
                        "${ChatColor.GOLD}${ChatColor.UNDERLINE}ノーマル${ChatColor.RESET}${ChatColor.GRAY}: ${ChatColor.YELLOW}${TimeFormat.formatJapan(difficultyNormalConfig.respawnCoolTime)}${ChatColor.GRAY}/回",
                        "${ChatColor.GOLD}${ChatColor.UNDERLINE}ハード${ChatColor.RESET}${ChatColor.GRAY}: ${ChatColor.YELLOW}${TimeFormat.formatJapan(difficultyHardConfig.respawnCoolTime)}${ChatColor.GRAY}/回",
                        "${ChatColor.GOLD}${ChatColor.UNDERLINE}ハードコア${ChatColor.RESET}${ChatColor.GRAY}: ${ChatColor.YELLOW}${TimeFormat.formatJapan(difficultyHardCoreConfig.respawnCoolTime)}${ChatColor.GRAY}/回",
                        "",
                        "${ChatColor.GOLD}クリックで設定変更"
                )
                itemStack31.itemMeta = itemMeta31
                inv.setItem(3, itemStack31)

                val itemStack41 = ItemStack(Material.PAPER, 1)
                val itemMeta41 = itemStack41.itemMeta!!
                itemMeta41.setDisplayName("${ChatColor.BOLD}レート")
                itemMeta41.lore = Arrays.asList(
                        "${ChatColor.GOLD}${ChatColor.UNDERLINE}イージー${ChatColor.RESET}${ChatColor.GRAY}: ${ChatColor.YELLOW}${difficultyEasyConfig.rate}${ChatColor.GRAY}円/秒",
                        "${ChatColor.GOLD}${ChatColor.UNDERLINE}ノーマル${ChatColor.RESET}${ChatColor.GRAY}: ${ChatColor.YELLOW}${difficultyNormalConfig.rate}${ChatColor.GRAY}円/秒",
                        "${ChatColor.GOLD}${ChatColor.UNDERLINE}ハード${ChatColor.RESET}${ChatColor.GRAY}: ${ChatColor.YELLOW}${difficultyHardConfig.rate}${ChatColor.GRAY}円/秒",
                        "${ChatColor.GOLD}${ChatColor.UNDERLINE}ハードコア${ChatColor.RESET}${ChatColor.GRAY}: ${ChatColor.YELLOW}${difficultyHardCoreConfig.rate}${ChatColor.GRAY}円/秒",
                        "",
                        "${ChatColor.GOLD}クリックで設定変更")
                itemStack41.itemMeta = itemMeta41
                inv.setItem(5, itemStack41)

                val itemStack51 = ItemStack(Material.PAPER, 1)
                val itemMeta51 = itemStack51.itemMeta!!
                itemMeta51.setDisplayName("${ChatColor.BOLD}サイコロの最大数")
                itemMeta51.lore = Arrays.asList(
                        "${ChatColor.GOLD}${ChatColor.UNDERLINE}設定値${ChatColor.RESET}${ChatColor.GRAY}: ${ChatColor.YELLOW}${worldConfig.opGameConfig.diceCount}",
                        ChatColor.GOLD.toString() + "左クリックで設定変更",
                        ChatColor.GOLD.toString() + "右クリックでリセット"
                )
                itemStack51.itemMeta = itemMeta51
                inv.setItem(6, itemStack51)

                val itemStack2 = ItemStack(Material.PAPER, 1)
                val itemMeta2 = itemStack2.itemMeta!!
                itemMeta2.setDisplayName("${ChatColor.BOLD}カウントダウン")
                itemMeta2.lore = Arrays.asList(
                        "${ChatColor.GOLD}${ChatColor.UNDERLINE}設定値${ChatColor.RESET}${ChatColor.GRAY}: ${ChatColor.YELLOW}${TimeFormat.formatJapan(worldConfig.gameConfig.countDown)}",
                        "${ChatColor.GOLD}左クリックで設定変更",
                        "${ChatColor.GOLD}右クリックでリセット"
                )
                itemStack2.itemMeta = itemMeta2
                inv.setItem(19, itemStack2)

                val itemStack3 = ItemStack(Material.PAPER, 1)
                val itemMeta3 = itemStack3.itemMeta!!
                itemMeta3.setDisplayName("${ChatColor.BOLD}ゲーム時間")
                itemMeta3.lore = Arrays.asList(
                        "${ChatColor.GOLD}${ChatColor.UNDERLINE}設定値${ChatColor.RESET}${ChatColor.GRAY}: ${ChatColor.YELLOW}${TimeFormat.formatJapan(worldConfig.gameConfig.game)}",
                        "${ChatColor.GOLD}左クリックで設定変更",
                        "${ChatColor.GOLD}右クリックでリセット"
                )
                itemStack3.itemMeta = itemMeta3
                inv.setItem(20, itemStack3)

                val itemStack4 = ItemStack(Material.PAPER, 1)
                val itemMeta4 = itemStack4.itemMeta!!
                itemMeta4.setDisplayName("${ChatColor.BOLD}復活禁止")
                itemMeta4.lore = Arrays.asList(
                        "${ChatColor.GOLD}${ChatColor.UNDERLINE}設定値${ChatColor.RESET}${ChatColor.GRAY}: ${ChatColor.YELLOW}${TimeFormat.formatJapan(worldConfig.gameConfig.respawnDeny)}",
                        "${ChatColor.GOLD}左クリックで設定変更",
                        "${ChatColor.GOLD}右クリックでリセット"
                )
                itemStack4.itemMeta = itemMeta4
                inv.setItem(21, itemStack4)

                val itemStack5 = ItemStack(if (worldConfig.gameConfig.script) Material.LIME_CONCRETE else Material.RED_CONCRETE, 1)
                val itemMeta5 = itemStack5.itemMeta!!
                itemMeta5.setDisplayName("${ChatColor.BOLD}スクリプト")
                itemMeta5.lore = Arrays.asList(
                        "${ChatColor.GOLD}${ChatColor.UNDERLINE}設定値${ChatColor.RESET}${ChatColor.GRAY}: ${ChatColor.YELLOW}${if (worldConfig.gameConfig.script) "有効" else "無効"}",
                        "${ChatColor.GOLD}左クリックで${if (worldConfig.gameConfig.script) "無効" else "有効"}に変更",
                        "${ChatColor.GOLD}右クリックでリセット"
                )
                itemStack5.itemMeta = itemMeta5
                inv.setItem(23, itemStack5)

                val itemStack6 = ItemStack(if (worldConfig.gameConfig.successMission) Material.LIME_CONCRETE else Material.RED_CONCRETE, 1)
                val itemMeta6 = itemStack6.itemMeta!!
                itemMeta6.setDisplayName("${ChatColor.BOLD}生存ミッション")
                itemMeta6.lore = Arrays.asList(
                        "${ChatColor.GOLD}${ChatColor.UNDERLINE}設定値${ChatColor.RESET}${ChatColor.GRAY}: ${ChatColor.YELLOW}${if (worldConfig.gameConfig.successMission) "有効" else "無効"}",
                        "${ChatColor.GOLD}左クリックで${if (worldConfig.gameConfig.successMission) "無効" else "有効"}に変更",
                        "${ChatColor.GOLD}右クリックでリセット"
                )
                itemStack6.itemMeta = itemMeta6
                inv.setItem(24, itemStack6)

                val itemStack7 = ItemStack(if (worldConfig.gameConfig.jump) Material.LIME_CONCRETE else Material.RED_CONCRETE, 1)
                val itemMeta7 = itemStack7.itemMeta!!
                itemMeta7.setDisplayName("${ChatColor.BOLD}ダッシュジャンプ")
                itemMeta7.lore = Arrays.asList(
                        "${ChatColor.GOLD}${ChatColor.UNDERLINE}設定値${ChatColor.RESET}${ChatColor.GRAY}: ${ChatColor.YELLOW}${if (worldConfig.gameConfig.jump) "有効" else "無効"}",
                        "${ChatColor.GOLD}左クリックで${if (worldConfig.gameConfig.jump) "無効" else "有効"}に変更",
                        "${ChatColor.GOLD}右クリックでリセット"
                )
                itemStack7.itemMeta = itemMeta7
                inv.setItem(25, itemStack7)

                val itemStack8 = ItemStack(Material.BONE, 1)
                val itemMeta8 = itemStack8.itemMeta!!
                itemMeta8.setDisplayName("${ChatColor.BOLD}骨 (ゲーム開始時)")
                itemMeta8.lore = Arrays.asList(
                        "${ChatColor.GOLD}${ChatColor.UNDERLINE}イージー${ChatColor.RESET}${ChatColor.GRAY}: ${ChatColor.YELLOW}${difficultyEasyConfig.getBone(GameType.START).count}${ChatColor.GRAY}個",
                        "${ChatColor.GOLD}${ChatColor.UNDERLINE}ノーマル${ChatColor.RESET}${ChatColor.GRAY}: ${ChatColor.YELLOW}${difficultyNormalConfig.getBone(GameType.START).count}${ChatColor.GRAY}個",
                        "${ChatColor.GOLD}${ChatColor.UNDERLINE}ハード${ChatColor.RESET}${ChatColor.GRAY}: ${ChatColor.YELLOW}${difficultyHardConfig.getBone(GameType.START).count}${ChatColor.GRAY}個",
                        "${ChatColor.GOLD}${ChatColor.UNDERLINE}ハードコア${ChatColor.RESET}${ChatColor.GRAY}: ${ChatColor.YELLOW}${difficultyHardCoreConfig.getBone(GameType.START).count}${ChatColor.GRAY}個",
                        "",
                        "${ChatColor.GOLD}クリックで設定変更"
                )
                itemStack8.itemMeta = itemMeta8
                inv.setItem(28, itemStack8)

                val itemStack9 = ItemStack(Material.FEATHER, 1)
                val itemMeta9 = itemStack9.itemMeta!!
                itemMeta9.setDisplayName("${ChatColor.BOLD}羽 (ゲーム開始時)")
                itemMeta9.lore = Arrays.asList(
                        "${ChatColor.GOLD}${ChatColor.UNDERLINE}イージー${ChatColor.RESET}${ChatColor.GRAY}: ${ChatColor.YELLOW}${difficultyEasyConfig.getFeather(GameType.START).count}${ChatColor.GRAY}個",
                        "${ChatColor.GOLD}${ChatColor.UNDERLINE}ノーマル${ChatColor.RESET}${ChatColor.GRAY}: ${ChatColor.YELLOW}${difficultyNormalConfig.getFeather(GameType.START).count}${ChatColor.GRAY}個",
                        "${ChatColor.GOLD}${ChatColor.UNDERLINE}ハード${ChatColor.RESET}${ChatColor.GRAY}: ${ChatColor.YELLOW}${difficultyHardConfig.getFeather(GameType.START).count}${ChatColor.GRAY}個",
                        "${ChatColor.GOLD}${ChatColor.UNDERLINE}ハードコア${ChatColor.RESET}${ChatColor.GRAY}: ${ChatColor.YELLOW}${difficultyHardCoreConfig.getFeather(GameType.START).count}${ChatColor.GRAY}個",
                        "",
                        "${ChatColor.GOLD}クリックで設定変更")
                itemStack9.itemMeta = itemMeta9
                inv.setItem(29, itemStack9)

                val itemStack10 = ItemStack(Material.EGG, 1)
                val itemMeta10 = itemStack10.itemMeta!!
                itemMeta10.setDisplayName("${ChatColor.BOLD}卵 (ゲーム開始時)")
                itemMeta10.lore = Arrays.asList(
                        "${ChatColor.GOLD}${ChatColor.UNDERLINE}イージー${ChatColor.RESET}${ChatColor.GRAY}: ${ChatColor.YELLOW}${difficultyEasyConfig.getEgg(GameType.START).count}${ChatColor.GRAY}個",
                        "${ChatColor.GOLD}${ChatColor.UNDERLINE}ノーマル${ChatColor.RESET}${ChatColor.GRAY}: ${ChatColor.YELLOW}${difficultyNormalConfig.getEgg(GameType.START).count}${ChatColor.GRAY}個",
                        "${ChatColor.GOLD}${ChatColor.UNDERLINE}ハード${ChatColor.RESET}${ChatColor.GRAY}: ${ChatColor.YELLOW}${difficultyHardConfig.getEgg(GameType.START).count}${ChatColor.GRAY}個",
                        "${ChatColor.GOLD}${ChatColor.UNDERLINE}ハードコア${ChatColor.RESET}${ChatColor.GRAY}: ${ChatColor.YELLOW}${difficultyHardCoreConfig.getEgg(GameType.START).count}${ChatColor.GRAY}個",
                        "",
                        "${ChatColor.GOLD}クリックで設定変更")
                itemStack10.itemMeta = itemMeta10
                inv.setItem(30, itemStack10)

                val itemStack11 = ItemStack(Material.BONE, 1)
                val itemMeta11 = itemStack11.itemMeta!!
                itemMeta11.setDisplayName("${ChatColor.BOLD}骨 (復活時)")
                itemMeta11.lore = Arrays.asList(
                        "${ChatColor.GOLD}${ChatColor.UNDERLINE}イージー${ChatColor.RESET}${ChatColor.GRAY}: ${ChatColor.YELLOW}${difficultyEasyConfig.getBone(GameType.RESPAWN).count}${ChatColor.GRAY}個",
                        "${ChatColor.GOLD}${ChatColor.UNDERLINE}ノーマル${ChatColor.RESET}${ChatColor.GRAY}: ${ChatColor.YELLOW}${difficultyNormalConfig.getBone(GameType.RESPAWN).count}${ChatColor.GRAY}個",
                        "${ChatColor.GOLD}${ChatColor.UNDERLINE}ハード${ChatColor.RESET}${ChatColor.GRAY}: ${ChatColor.YELLOW}${difficultyHardConfig.getBone(GameType.RESPAWN).count}${ChatColor.GRAY}個",
                        "${ChatColor.GOLD}${ChatColor.UNDERLINE}ハードコア${ChatColor.RESET}${ChatColor.GRAY}: ${ChatColor.YELLOW}${difficultyHardCoreConfig.getBone(GameType.RESPAWN).count}${ChatColor.GRAY}個",
                        "",
                        "${ChatColor.GOLD}クリックで設定変更"
                )
                itemStack11.itemMeta = itemMeta11
                inv.setItem(32, itemStack11)

                val itemStack12 = ItemStack(Material.FEATHER, 1)
                val itemMeta12 = itemStack12.itemMeta!!
                itemMeta12.setDisplayName("${ChatColor.BOLD}羽 (復活時)")
                itemMeta12.lore = Arrays.asList(
                        "${ChatColor.GOLD}${ChatColor.UNDERLINE}イージー${ChatColor.RESET}${ChatColor.GRAY}: ${ChatColor.YELLOW}${difficultyEasyConfig.getFeather(GameType.RESPAWN).count}${ChatColor.GRAY}個",
                        "${ChatColor.GOLD}${ChatColor.UNDERLINE}ノーマル${ChatColor.RESET}${ChatColor.GRAY}: ${ChatColor.YELLOW}${difficultyNormalConfig.getFeather(GameType.RESPAWN).count}${ChatColor.GRAY}個",
                        "${ChatColor.GOLD}${ChatColor.UNDERLINE}ハード${ChatColor.RESET}${ChatColor.GRAY}: ${ChatColor.YELLOW}${difficultyHardConfig.getFeather(GameType.RESPAWN).count}${ChatColor.GRAY}個",
                        "${ChatColor.GOLD}${ChatColor.UNDERLINE}ハードコア${ChatColor.RESET}${ChatColor.GRAY}: ${ChatColor.YELLOW}${difficultyHardCoreConfig.getFeather(GameType.RESPAWN).count}${ChatColor.GRAY}個",
                        "",
                        "${ChatColor.GOLD}クリックで設定変更"
                )
                itemStack12.itemMeta = itemMeta12
                inv.setItem(33, itemStack12)

                val itemStack13 = ItemStack(Material.EGG, 1)
                val itemMeta13 = itemStack13.itemMeta!!
                itemMeta13.setDisplayName("${ChatColor.BOLD}卵 (復活時)")
                itemMeta13.lore = Arrays.asList(
                        "${ChatColor.GOLD}${ChatColor.UNDERLINE}イージー${ChatColor.RESET}${ChatColor.GRAY}: ${ChatColor.YELLOW}${difficultyEasyConfig.getEgg(GameType.RESPAWN).count}${ChatColor.GRAY}個",
                        "${ChatColor.GOLD}${ChatColor.UNDERLINE}ノーマル${ChatColor.RESET}${ChatColor.GRAY}: ${ChatColor.YELLOW}${difficultyNormalConfig.getEgg(GameType.RESPAWN).count}${ChatColor.GRAY}個",
                        "${ChatColor.GOLD}${ChatColor.UNDERLINE}ハード${ChatColor.RESET}${ChatColor.GRAY}: ${ChatColor.YELLOW}${difficultyHardConfig.getEgg(GameType.RESPAWN).count}${ChatColor.GRAY}個",
                        "${ChatColor.GOLD}${ChatColor.UNDERLINE}ハードコア${ChatColor.RESET}${ChatColor.GRAY}: ${ChatColor.YELLOW}${difficultyHardCoreConfig.getEgg(GameType.RESPAWN).count}${ChatColor.GRAY}個",
                        "",
                        "${ChatColor.GOLD}クリックで設定変更"
                )
                itemStack13.itemMeta = itemMeta13
                inv.setItem(34, itemStack13)

                val itemStack14 = ItemStack(if (worldConfig.config.contains(WorldManager.PathType.LOCATION_OPGAME.path)) Material.LIME_CONCRETE else Material.RED_CONCRETE, 1)
                val itemMeta14 = itemStack14.itemMeta!!
                itemMeta14.setDisplayName("${ChatColor.BOLD}オープニングゲーム地点")
                itemMeta14.lore = Arrays.asList(
                        "${ChatColor.GOLD}${ChatColor.UNDERLINE}設定値${ChatColor.RESET}${ChatColor.GRAY}: ${ChatColor.YELLOW}" + ChatColor.YELLOW + if (worldConfig.config.contains(WorldManager.PathType.LOCATION_OPGAME.path)) "設定済み" else "未設定",
                        "${ChatColor.GOLD}左クリックで設定変更",
                        "${ChatColor.GOLD}右クリックでテレポート"
                )
                itemStack14.itemMeta = itemMeta14
                inv.setItem(37, itemStack14)

                val itemStack16 = ItemStack(if (worldConfig.config.contains("${WorldManager.PathType.LOCATION_GOPGAME.path}.p1")) Material.LIME_CONCRETE else Material.RED_CONCRETE, 1)
                val itemMeta16 = itemStack16.itemMeta!!
                itemMeta16.setDisplayName("${ChatColor.BOLD}オープニングゲーム集合地点")
                itemMeta16.lore = Arrays.asList(
                        "${ChatColor.GOLD}${ChatColor.UNDERLINE}設定値${ChatColor.RESET}${ChatColor.GRAY}: ${ChatColor.YELLOW}" + ChatColor.YELLOW + if (worldConfig.config.contains("${WorldManager.PathType.LOCATION_GOPGAME.path}.p1")) "設定済み" else "未設定",
                        "${ChatColor.GOLD}左クリックで設定変更",
                        "${ChatColor.GOLD}右クリックでテレポート"
                )
                itemStack16.itemMeta = itemMeta16
                inv.setItem(38, itemStack16)

                val itemStack15 = ItemStack(if (worldConfig.config.contains("${WorldManager.PathType.LOCATION_HUNTER.path}.p1")) Material.LIME_CONCRETE else Material.RED_CONCRETE, 1)
                val itemMeta15 = itemStack15.itemMeta!!
                itemMeta15.setDisplayName("${ChatColor.BOLD}ハンター集合地点")
                itemMeta15.lore = Arrays.asList(
                        "${ChatColor.GOLD}${ChatColor.UNDERLINE}設定値${ChatColor.RESET}${ChatColor.GRAY}: ${ChatColor.YELLOW}${if (worldConfig.config.contains("${WorldManager.PathType.LOCATION_HUNTER.path}.p1")) "設定済み" else "未設定"}",
                        "${ChatColor.GOLD}左クリックで設定変更",
                        "${ChatColor.GOLD}右クリックでテレポート"
                )
                itemStack15.itemMeta = itemMeta15
                inv.setItem(40, itemStack15)

                val itemStack17 = ItemStack(if (worldConfig.config.contains("${WorldManager.PathType.DOOR_HUNTER.path}.p1")) Material.LIME_CONCRETE else Material.RED_CONCRETE, 1)
                val itemMeta17 = itemStack17.itemMeta!!
                itemMeta17.setDisplayName("${ChatColor.BOLD}ドア位置")
                itemMeta17.lore = Arrays.asList(
                        "${ChatColor.GOLD}${ChatColor.UNDERLINE}設定値${ChatColor.RESET}${ChatColor.GRAY}: ${ChatColor.YELLOW}${if (worldConfig.config.contains("${WorldManager.PathType.DOOR_HUNTER.path}.p1")) "設定済み" else "未設定"} (ポイント1)",
                        "${ChatColor.GOLD}左クリックで設定変更",
                        "${ChatColor.GOLD}右クリックでドア開放"
                )
                itemStack17.itemMeta = itemMeta17
                inv.setItem(41, itemStack17)

                val itemStack18 = ItemStack(if (worldConfig.config.contains("${WorldManager.PathType.LOCATION_JAIL.path}.p1")) Material.LIME_CONCRETE else Material.RED_CONCRETE, 1)
                val itemMeta18 = itemStack18.itemMeta!!
                itemMeta18.setDisplayName("${ChatColor.BOLD}牢獄地点")
                itemMeta18.lore = Arrays.asList(
                        "${ChatColor.GOLD}${ChatColor.UNDERLINE}設定値${ChatColor.RESET}${ChatColor.GRAY}: ${ChatColor.YELLOW}${if (worldConfig.config.contains("${WorldManager.PathType.LOCATION_JAIL.path}.p1")) "設定済み" else "未設定"} (ポイント1)",
                        "${ChatColor.GOLD}左クリックで設定変更",
                        "${ChatColor.GOLD}右クリックでテレポート"
                )
                itemStack18.itemMeta = itemMeta18
                inv.setItem(42, itemStack18)

                val itemStack19 = ItemStack(if (worldConfig.config.contains("${WorldManager.PathType.LOCATION_RESPAWN.path}.p1")) Material.LIME_CONCRETE else Material.RED_CONCRETE, 1)
                val itemMeta19 = itemStack19.itemMeta!!
                itemMeta19.setDisplayName("${ChatColor.BOLD}復活地点")
                itemMeta19.lore = Arrays.asList(
                        "${ChatColor.GOLD}${ChatColor.UNDERLINE}設定値${ChatColor.RESET}${ChatColor.GRAY}: ${ChatColor.YELLOW}${if (worldConfig.config.contains("${WorldManager.PathType.LOCATION_RESPAWN.path}.p1")) "設定済み" else "未設定"} (ポイント1)",
                        "${ChatColor.GOLD}左クリックで設定変更",
                        "${ChatColor.GOLD}右クリックでテレポート"
                )
                itemStack19.itemMeta = itemMeta19
                inv.setItem(43, itemStack19)
                return inv
            }

        val listInventory: Inventory
            get() {
                val inv = Bukkit.createInventory(null, InventoryType.CHEST, listTitle)

                val worldConfig = Main.worldConfig

                if (Bukkit.getWorldContainer().listFiles() != null) {
                    for (file in Bukkit.getWorldContainer().listFiles()) {
                        if (file.isDirectory) {
                            val configFile = File("${file.name}${Main.FILE_SEPARATOR}map.yml")
                            if (configFile.exists()) {
                                val itemStack = ItemStack(worldConfig.mapConfig.icon, 1)
                                val itemMeta = itemStack.itemMeta!!
                                if (WorldManager.world.name.equals(file.name, true)) {
                                    itemMeta.addEnchant(Enchantment.DURABILITY, 0, true)
                                    itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS)
                                    itemMeta.setDisplayName("${ChatColor.BOLD}${ChatColor.UNDERLINE}${file.name}")
                                    itemMeta.lore = Arrays.asList(
                                            "${ChatColor.GOLD}${ChatColor.UNDERLINE}マップ名: ${ChatColor.RESET}${ChatColor.GRAY}: ${ChatColor.YELLOW}${worldConfig.mapConfig.name}",
                                            "${ChatColor.GOLD}${ChatColor.UNDERLINE}マップバージョン: ${ChatColor.RESET}${ChatColor.GRAY}: ${ChatColor.YELLOW}${worldConfig.mapConfig.version}",
                                            "${ChatColor.GOLD}${ChatColor.UNDERLINE}マップ製作者: ${ChatColor.RESET}${ChatColor.GRAY}: ${ChatColor.YELLOW}${worldConfig.mapConfig.authors}"
                                    )
                                } else {
                                    itemMeta.setDisplayName("${ChatColor.BOLD}${file.name}")
                                    itemMeta.lore = Arrays.asList(
                                            "${ChatColor.GOLD}${ChatColor.UNDERLINE}マップ名: ${ChatColor.RESET}${ChatColor.GRAY}: ${ChatColor.YELLOW}${worldConfig.mapConfig.name}",
                                            "${ChatColor.GOLD}${ChatColor.UNDERLINE}マップバージョン: ${ChatColor.RESET}${ChatColor.GRAY}: ${ChatColor.YELLOW}${worldConfig.mapConfig.version}",
                                            "${ChatColor.GOLD}${ChatColor.UNDERLINE}マップ製作者: ${ChatColor.RESET}${ChatColor.GRAY}: ${ChatColor.YELLOW}${worldConfig.mapConfig.authors}",
                                            "${ChatColor.YELLOW}クリックしてマップを変更"
                                    )
                                }
                                itemStack.itemMeta = itemMeta
                                inv.addItem(itemStack)
                            }
                        }
                    }
                }
                return inv
            }

        fun getDifficultyInventory(actionType: ActionType): Inventory {
            val inv = Bukkit.createInventory(null, InventoryType.CHEST, "${difficultyTitle}${actionType.displayName}")

            val itemStackBorder = ItemStack(Material.BLACK_STAINED_GLASS_PANE)
            val itemMetaBorder = itemStackBorder.itemMeta!!
            itemMetaBorder.addItemFlags(*ItemUtil.itemFlags)
            itemMetaBorder.setDisplayName("${ChatColor.BOLD}")
            itemStackBorder.itemMeta = itemMetaBorder
            for (i in 0..8)
                inv.setItem(i, itemStackBorder)
            for (i in 18..26)
                inv.setItem(i, itemStackBorder)

            val itemStackDifficultyEasy = ItemStack(MainInventory.Item.DIFFICULTY_EASY.material, 1)
            val itemMetaDifficultyEasy = itemStackDifficultyEasy.itemMeta!!
            itemMetaDifficultyEasy.addItemFlags(*ItemUtil.itemFlags)
            itemMetaDifficultyEasy.setDisplayName("${ChatColor.BOLD}${ChatColor.UNDERLINE}${WorldManager.Difficulty.EASY.displayName}")
            itemMetaDifficultyEasy.lore = listOf()
            itemStackDifficultyEasy.itemMeta = itemMetaDifficultyEasy
            inv.setItem(10, itemStackDifficultyEasy)

            val itemStackDifficultyNormal = ItemStack(MainInventory.Item.DIFFICULTY_NORMAL.material, 1)
            val itemMetaDifficultyNormal = itemStackDifficultyNormal.itemMeta!!
            itemMetaDifficultyNormal.addItemFlags(*ItemUtil.itemFlags)
            itemMetaDifficultyNormal.setDisplayName("${ChatColor.BOLD}${ChatColor.UNDERLINE}${WorldManager.Difficulty.NORMAL.displayName}")
            itemMetaDifficultyNormal.lore = listOf()
            itemStackDifficultyNormal.itemMeta = itemMetaDifficultyNormal
            inv.setItem(11, itemStackDifficultyNormal)

            val itemStackDifficultyHard = ItemStack(MainInventory.Item.DIFFICULTY_HARD.material, 1)
            val itemMetaDifficultyHard = itemStackDifficultyHard.itemMeta!!
            itemMetaDifficultyHard.addItemFlags(*ItemUtil.itemFlags)
            itemMetaDifficultyHard.setDisplayName("${ChatColor.BOLD}${ChatColor.UNDERLINE}${WorldManager.Difficulty.HARD.displayName}")
            itemMetaDifficultyHard.lore = listOf()
            itemStackDifficultyHard.itemMeta = itemMetaDifficultyHard
            inv.setItem(12, itemStackDifficultyHard)

            val itemStackDifficultyHardCore = ItemStack(MainInventory.Item.DIFFICULTY_HARDCORE.material, 1)
            val itemMetaDifficultyHardCore = itemStackDifficultyHardCore.itemMeta!!
            itemMetaDifficultyHardCore.addItemFlags(*ItemUtil.itemFlags)
            itemMetaDifficultyHardCore.setDisplayName("${ChatColor.BOLD}${ChatColor.UNDERLINE}${WorldManager.Difficulty.HARDCORE.displayName}")
            itemMetaDifficultyHardCore.lore = listOf()
            itemStackDifficultyHardCore.itemMeta = itemMetaDifficultyHardCore
            inv.setItem(13, itemStackDifficultyHardCore)

            val itemStackCancel = ItemStack(Material.BARRIER, 1)
            val itemMetaCancel = itemStackCancel.itemMeta!!
            itemMetaCancel.addItemFlags(*ItemUtil.itemFlags)
            itemMetaCancel.setDisplayName("${ChatColor.RED}${ChatColor.BOLD}${ChatColor.UNDERLINE}キャンセル")
            itemMetaCancel.lore = listOf()
            itemStackCancel.itemMeta = itemMetaCancel
            inv.setItem(16, itemStackCancel)

            return inv
        }
    }

    enum class ActionType(val displayName: String) {
        RATE("レート"),
        RESPAWN_COUNT("復活可能回数"),
        RESPAWN_COOLTIME("復活クールタイム"),
        ITEM_START_BONE("骨 (ゲーム開始時)"),
        ITEM_START_FEATHER("羽 (ゲーム開始時)"),
        ITEM_START_EGG("卵 (ゲーム開始時)"),
        ITEM_RESPAWN_BONE("骨 (復活時)"),
        ITEM_RESPAWN_FEATHER("羽 (復活時)"),
        ITEM_RESPAWN_EGG("卵 (復活時)");

        companion object {
            fun getActionType(name: String): ActionType {
                return valueOf(name.toUpperCase())
            }
        }
    }
}
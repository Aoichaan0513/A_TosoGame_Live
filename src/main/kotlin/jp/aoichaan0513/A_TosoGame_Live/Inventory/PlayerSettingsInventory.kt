package jp.aoichaan0513.A_TosoGame_Live.Inventory

import jp.aoichaan0513.A_TosoGame_Live.API.Enums.ItemType
import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.GameManager
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.Player.PlayerConfig
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.Player.PlayerManager
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack

class PlayerSettingsInventory {
    companion object {

        val title = "${ChatColor.DARK_GRAY}> ${ChatColor.BOLD}ホーム${ChatColor.RESET}${ChatColor.DARK_GRAY} > ${ChatColor.BOLD}設定${ChatColor.RESET}${ChatColor.DARK_GRAY}"
        val inventoryTitle = "${ChatColor.DARK_GRAY}> ${ChatColor.BOLD}設定${ChatColor.RESET}${ChatColor.DARK_GRAY} > ${ChatColor.BOLD}インベントリ${ChatColor.RESET}${ChatColor.DARK_GRAY}"
        val itemSelectTitle = "${ChatColor.DARK_GRAY}> ${ChatColor.BOLD}インベントリ${ChatColor.RESET}${ChatColor.DARK_GRAY} > ${ChatColor.BOLD}アイテムを選択"

        private val itemFlags = arrayOf(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS)


        fun getInventory(p: Player): Inventory {
            val inv = Bukkit.createInventory(null, 9 * 3, title)

            val playerConfig = PlayerManager.loadConfig(p)

            val itemStackBorder = ItemStack(Material.BLACK_STAINED_GLASS_PANE)
            val itemMetaBorder = itemStackBorder.itemMeta!!
            itemMetaBorder.addItemFlags(*itemFlags)
            itemMetaBorder.setDisplayName("${ChatColor.BOLD}")
            itemStackBorder.itemMeta = itemMetaBorder
            for (i in 0..8)
                inv.setItem(i, itemStackBorder)
            for (i in 18..26)
                inv.setItem(i, itemStackBorder)

            val itemStackHome = ItemStack(Item.HOME.material)
            val itemMetaHome = itemStackHome.itemMeta!!
            itemMetaHome.addItemFlags(*itemFlags)
            itemMetaHome.setDisplayName("${ChatColor.GOLD}${ChatColor.BOLD}${ChatColor.UNDERLINE}ホーム")
            itemMetaHome.lore = listOf("${ChatColor.YELLOW}クリックして${ChatColor.GOLD}${ChatColor.BOLD}${ChatColor.UNDERLINE}ホーム画面${ChatColor.RESET}${ChatColor.YELLOW}を表示します。")
            itemStackHome.itemMeta = itemMetaHome
            inv.setItem(Item.HOME.index, itemStackHome)

            val itemStackInventory = ItemStack(Item.INVENTORY_SETTINGS.material, 1)
            val itemMetaInventory = itemStackInventory.itemMeta!!
            itemMetaInventory.addItemFlags(*itemFlags)
            itemMetaInventory.setDisplayName("${ChatColor.GOLD}${ChatColor.BOLD}${ChatColor.UNDERLINE}インベントリ設定")
            itemMetaInventory.lore = listOf("${ChatColor.YELLOW}クリックして${ChatColor.GOLD}${ChatColor.BOLD}${ChatColor.UNDERLINE}インベントリ設定${ChatColor.RESET}${ChatColor.YELLOW}を開きます。")
            itemStackInventory.itemMeta = itemMetaInventory
            inv.setItem(Item.INVENTORY_SETTINGS.index, itemStackInventory)

            val (color1, name1) = if (playerConfig.bookForegroundColor == PlayerConfig.BookForegroundColor.BLACK)
                PlayerConfig.BookForegroundColor.WHITE to "白"
            else
                PlayerConfig.BookForegroundColor.BLACK to "黒"

            val (color2, name2) = if (playerConfig.bookForegroundColor == PlayerConfig.BookForegroundColor.BLACK)
                PlayerConfig.BookForegroundColor.BLACK to "黒"
            else
                PlayerConfig.BookForegroundColor.WHITE to "白"

            val itemStackBookForeground = ItemStack(Item.BOOK_FOREGROUND.material, 1)
            val itemMetaBookForeground = itemStackBookForeground.itemMeta!!
            itemMetaBookForeground.addItemFlags(*itemFlags)
            itemMetaBookForeground.setDisplayName("${ChatColor.GOLD}${ChatColor.BOLD}${ChatColor.UNDERLINE}本の文字色設定")
            itemMetaBookForeground.lore = listOf(
                    "${ChatColor.YELLOW}クリックして${ChatColor.GOLD}${ChatColor.BOLD}${ChatColor.UNDERLINE}本の文字色${ChatColor.RESET}${ChatColor.YELLOW}を${color1.color}${ChatColor.BOLD}${ChatColor.UNDERLINE}${name1}${ChatColor.RESET}${ChatColor.YELLOW}に変更します。",
                    "",
                    "${ChatColor.GOLD}${ChatColor.UNDERLINE}現在の文字色${ChatColor.RESET}${ChatColor.GRAY}: ${color2.color}$name2"
            )
            itemStackBookForeground.itemMeta = itemMetaBookForeground
            inv.setItem(Item.BOOK_FOREGROUND.index, itemStackBookForeground)

            return inv
        }

        fun getInventorySettingsInventory(p: Player): Inventory {
            val inv = Bukkit.createInventory(null, 9 * 5, inventoryTitle)

            val itemStackBlackBorder = ItemStack(Material.BLACK_STAINED_GLASS_PANE)
            val itemMetaBlackBorder = itemStackBlackBorder.itemMeta!!
            itemMetaBlackBorder.addItemFlags(*itemFlags)
            itemMetaBlackBorder.setDisplayName("${ChatColor.BOLD}")
            itemStackBlackBorder.itemMeta = itemMetaBlackBorder
            for (i in 0..8)
                inv.setItem(i, itemStackBlackBorder)
            for (i in 36..44)
                inv.setItem(i, itemStackBlackBorder)

            val itemStackGrayBorder = ItemStack(Material.GRAY_STAINED_GLASS_PANE)
            val itemMetaGrayBorder = itemStackGrayBorder.itemMeta!!
            itemMetaGrayBorder.addItemFlags(*itemFlags)
            itemMetaGrayBorder.setDisplayName("${ChatColor.BOLD}")
            itemStackGrayBorder.itemMeta = itemMetaGrayBorder
            for (i in 18..35)
                inv.setItem(i, itemStackGrayBorder)

            val itemStackHome = ItemStack(Item.HOME.material)
            val itemMetaHome = itemStackHome.itemMeta!!
            itemMetaHome.addItemFlags(*itemFlags)
            itemMetaHome.setDisplayName("${ChatColor.GOLD}${ChatColor.BOLD}${ChatColor.UNDERLINE}ホーム")
            itemMetaHome.lore = listOf("${ChatColor.YELLOW}クリックして${ChatColor.GOLD}${ChatColor.BOLD}${ChatColor.UNDERLINE}ホーム画面${ChatColor.RESET}${ChatColor.YELLOW}を表示します。")
            itemStackHome.itemMeta = itemMetaHome
            inv.setItem(40, itemStackHome)

            val itemStackPaper = ItemStack(Material.PAPER)
            val itemMetaPaper = itemStackPaper.itemMeta!!
            itemMetaPaper.addItemFlags(*itemFlags)
            itemMetaPaper.setDisplayName("${ChatColor.GOLD}${ChatColor.BOLD}${ChatColor.UNDERLINE}未設定")
            itemMetaPaper.lore = listOf("${ChatColor.YELLOW}クリックして${ChatColor.GOLD}${ChatColor.BOLD}${ChatColor.UNDERLINE}このスロットに配置するアイテム${ChatColor.RESET}${ChatColor.YELLOW}を変更します。")

            var v = 0
            for (i in 9..17) {
                itemMetaPaper.setCustomModelData(v++)
                itemStackPaper.itemMeta = itemMetaPaper

                inv.setItem(i, itemStackPaper)
            }

            itemMetaPaper.setCustomModelData(40)
            itemStackPaper.itemMeta = itemMetaPaper
            inv.setItem(27, itemStackPaper)

            val items = PlayerManager.loadConfig(p).inventoryConfig.items
            for ((itemType, slot) in items) {
                if (slot == -1) continue
                val itemStack = ItemStack(itemType.material)
                val itemMeta = itemStack.itemMeta!!
                itemMeta.addItemFlags(*itemFlags)
                itemMeta.lore = listOf("${ChatColor.YELLOW}クリックして${ChatColor.GOLD}${ChatColor.BOLD}${ChatColor.UNDERLINE}このスロットに配置するアイテム${ChatColor.RESET}${ChatColor.YELLOW}を変更します。")
                itemMeta.setCustomModelData(slot)
                itemStack.itemMeta = itemMeta

                inv.setItem(if (slot != 40) slot + 9 else 27, itemStack)
            }

            return inv
        }

        fun getItemSelectInventory(): Inventory {
            val inv = Bukkit.createInventory(null, 9 * 5, itemSelectTitle)

            val itemStackBlackBorder = ItemStack(Material.BLACK_STAINED_GLASS_PANE)
            val itemMetaBlackBorder = itemStackBlackBorder.itemMeta!!
            itemMetaBlackBorder.addItemFlags(*itemFlags)
            itemMetaBlackBorder.setDisplayName("${ChatColor.BOLD}")
            itemStackBlackBorder.itemMeta = itemMetaBlackBorder
            for (i in 0..8)
                inv.setItem(i, itemStackBlackBorder)
            for (i in 36..44)
                inv.setItem(i, itemStackBlackBorder)


            val itemStackGrayBorder = ItemStack(Material.GRAY_STAINED_GLASS_PANE)
            val itemMetaGrayBorder = itemStackGrayBorder.itemMeta!!
            itemMetaGrayBorder.addItemFlags(*itemFlags)
            itemMetaGrayBorder.setDisplayName("${ChatColor.BOLD}")
            itemStackGrayBorder.itemMeta = itemMetaGrayBorder
            for (i in 18..35)
                inv.setItem(i, itemStackGrayBorder)

            val itemStackHome = ItemStack(Item.HOME.material)
            val itemMetaHome = itemStackHome.itemMeta!!
            itemMetaHome.addItemFlags(*itemFlags)
            itemMetaHome.setDisplayName("${ChatColor.GOLD}${ChatColor.BOLD}${ChatColor.UNDERLINE}ホーム")
            itemMetaHome.lore = listOf("${ChatColor.YELLOW}クリックして${ChatColor.GOLD}${ChatColor.BOLD}${ChatColor.UNDERLINE}ホーム画面${ChatColor.RESET}${ChatColor.YELLOW}を表示します。")
            itemStackHome.itemMeta = itemMetaHome
            inv.setItem(40, itemStackHome)

            val itemStackCancel = ItemStack(Material.BARRIER)
            val itemMetaCancel = itemStackCancel.itemMeta!!
            itemMetaCancel.addItemFlags(*itemFlags)
            itemMetaCancel.setDisplayName("${ChatColor.GOLD}${ChatColor.BOLD}${ChatColor.UNDERLINE}キャンセル")
            itemMetaCancel.lore = listOf("${ChatColor.YELLOW}クリックして${ChatColor.GOLD}${ChatColor.BOLD}${ChatColor.UNDERLINE}設定をキャンセル${ChatColor.RESET}${ChatColor.YELLOW}します。")
            itemStackCancel.itemMeta = itemMetaCancel
            inv.setItem(31, itemStackCancel)

            for (itemType in ItemType.values()) {
                val itemStack = ItemStack(itemType.material)
                val itemMeta = itemStack.itemMeta!!
                itemMeta.addItemFlags(*itemFlags)
                itemMeta.lore = listOf(
                        "${ChatColor.YELLOW}クリックして${ChatColor.GOLD}${ChatColor.BOLD}${ChatColor.UNDERLINE}このスロットに配置するアイテムを設定${ChatColor.RESET}${ChatColor.YELLOW}します。",
                        "${ChatColor.GRAY}このスロットにすでに配置されているアイテムがある場合は設定できません。"
                )
                itemStack.itemMeta = itemMeta
                inv.addItem(itemStack)
            }

            return inv
        }
    }

    enum class Item(private val rawX: Int, private val rawY: Int, val material: Material) {
        INVENTORY_SETTINGS(1, 2, Material.CHEST),
        BOOK_FOREGROUND(3, 2, Material.BOOK),
        DISCORD_INTEGRATION(5, 2, if (GameManager.isGame()) Material.BLUE_WOOL else Material.GRAY_WOOL),

        HOME(5, 3, Material.WHITE_STAINED_GLASS_PANE),

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
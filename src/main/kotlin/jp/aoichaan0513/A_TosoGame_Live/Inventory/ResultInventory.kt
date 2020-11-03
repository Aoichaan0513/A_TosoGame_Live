package jp.aoichaan0513.A_TosoGame_Live.Inventory

import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.MoneyManager
import jp.aoichaan0513.A_TosoGame_Live.Listeners.Minecraft.onDamage
import jp.aoichaan0513.A_TosoGame_Live.Utils.ItemUtil
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta


class ResultInventory {
    companion object {

        val title = "${ChatColor.DARK_GRAY}> ${ChatColor.BOLD}ゲーム結果${ChatColor.RESET}${ChatColor.DARK_GRAY}"
        val rewardTitle = "$title > ${ChatColor.BOLD}賞金ランキング${ChatColor.RESET}${ChatColor.DARK_GRAY} > "
        val ensureTitle = "$title > ${ChatColor.BOLD}確保数ランキング${ChatColor.RESET}${ChatColor.DARK_GRAY} > "

        val arrowLeft = "MHF_ArrowLeft"
        val arrowRight = "MHF_ArrowRight"

        fun getInventory(p: Player, resultType: ResultType, page: Int = 0): Inventory {
            val inv = Bukkit.createInventory(null, 9 * 6, "${when (resultType) {
                ResultType.REWARD -> rewardTitle
                ResultType.ENSURE -> ensureTitle
            }}${page + 1}")

            val list = getPlayers(resultType)

            val itemStackBorder = ItemStack(Material.BLACK_STAINED_GLASS_PANE)
            val itemMetaBorder = itemStackBorder.itemMeta!!
            itemMetaBorder.addItemFlags(*ItemUtil.itemFlags)
            itemMetaBorder.setDisplayName("${ChatColor.BOLD}")
            itemStackBorder.itemMeta = itemMetaBorder
            for (i in 0..8)
                inv.setItem(i, itemStackBorder)
            for (i in 45..53)
                inv.setItem(i, itemStackBorder)

            val itemStackHome = ItemStack(Material.WHITE_STAINED_GLASS_PANE)
            val itemMetaHome = itemStackHome.itemMeta!!
            itemMetaHome.addItemFlags(*ItemUtil.itemFlags)
            itemMetaHome.setDisplayName("${ChatColor.GOLD}${ChatColor.BOLD}${ChatColor.UNDERLINE}ホーム")
            itemMetaHome.lore = listOf("${ChatColor.YELLOW}クリックして${ChatColor.GOLD}${ChatColor.BOLD}${ChatColor.UNDERLINE}ホーム画面${ChatColor.RESET}${ChatColor.YELLOW}を表示します。")
            itemStackHome.itemMeta = itemMetaHome
            inv.setItem(49, itemStackHome)

            if (list.size > 1) {
                val itemStackBack = ItemStack(Material.PLAYER_HEAD, 1)
                val itemMetaBack = (itemStackBack.itemMeta as SkullMeta)
                itemMetaBack.addItemFlags(*ItemUtil.itemFlags)
                itemMetaBack.owningPlayer = Bukkit.getOfflinePlayer(arrowLeft)
                itemMetaBack.setDisplayName("${ChatColor.BLUE}${ChatColor.BOLD}${ChatColor.UNDERLINE}前のページ")
                itemMetaBack.lore = listOf()
                itemStackBack.itemMeta = itemMetaBack
                inv.setItem(45, itemStackBack)

                val itemStackForward = ItemStack(Material.PLAYER_HEAD, 1)
                val itemMetaForward = (itemStackForward.itemMeta as SkullMeta)
                itemMetaForward.addItemFlags(*ItemUtil.itemFlags)
                itemMetaForward.owningPlayer = Bukkit.getOfflinePlayer(arrowRight)
                itemMetaForward.setDisplayName("${ChatColor.BLUE}${ChatColor.BOLD}${ChatColor.UNDERLINE}次のページ")
                itemMetaForward.lore = listOf()
                itemStackForward.itemMeta = itemMetaForward
                inv.setItem(53, itemStackForward)
            }

            if (list.isNotEmpty()) {
                val itemStackPlayerInfo = ItemStack(Material.PLAYER_HEAD, 1)
                val itemMetaPlayerInfo = itemStackPlayerInfo.itemMeta as SkullMeta
                itemMetaPlayerInfo.addItemFlags(*ItemUtil.itemFlags)

                for (i in list[page].indices) {
                    val (player, long) = list[page][i]

                    if (player.uniqueId == p.uniqueId)
                        itemMetaPlayerInfo.addEnchant(Enchantment.DURABILITY, 1, true)
                    itemMetaPlayerInfo.owningPlayer = player
                    itemMetaPlayerInfo.setDisplayName("${ChatColor.GOLD}${ChatColor.UNDERLINE}${i + 1}位${ChatColor.RESET}${ChatColor.GRAY}: ${ChatColor.YELLOW}${player.name}")
                    itemMetaPlayerInfo.lore = listOf("${ChatColor.GOLD}${ChatColor.BOLD}${ChatColor.UNDERLINE}${long}${if (resultType == ResultType.REWARD) "円" else ""}")
                    itemStackPlayerInfo.itemMeta = itemMetaPlayerInfo
                    inv.addItem(itemStackPlayerInfo)
                }
            }

            return inv
        }

        private fun getPlayers(resultType: ResultType): List<List<Pair<Player, Long>>> {
            val maxCount = 36

            return when (resultType) {
                ResultType.REWARD -> {
                    val dividedList = mutableListOf<List<Pair<Player, Long>>>()

                    MainAPI.divide(MoneyManager.rewardMap.toList(), maxCount).forEach {
                        val list = mutableListOf<Pair<Player, Long>>()
                        it.filter { MainAPI.isOnline(it.first) && it.second > 0 }.toList().sortedBy { it.second * -1 }.forEach { list.add(Bukkit.getPlayer(it.first)!! to it.second) }
                        dividedList.add(list)
                    }

                    dividedList
                }
                ResultType.ENSURE -> {
                    val dividedList = mutableListOf<List<Pair<Player, Long>>>()

                    MainAPI.divide(onDamage.hunterMap.toList(), maxCount).forEach {
                        val list = mutableListOf<Pair<Player, Long>>()
                        it.filter { MainAPI.isOnline(it.first) && it.second > 0 }.toList().sortedBy { it.second * -1 }.forEach { list.add(Bukkit.getPlayer(it.first)!! to it.second.toLong()) }
                        dividedList.add(list)
                    }

                    dividedList
                }
            }
        }
    }

    enum class ResultType {
        REWARD,
        ENSURE;

        companion object {

            fun getType(str: String?): ResultType? {
                val text = str?.trim()
                return if (text != null) values().firstOrNull { it.name.equals(text, true) } else null
            }
        }
    }
}
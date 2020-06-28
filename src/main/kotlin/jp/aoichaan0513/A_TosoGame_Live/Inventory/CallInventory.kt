package jp.aoichaan0513.A_TosoGame_Live.Inventory

import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.DiscordManager
import jp.aoichaan0513.A_TosoGame_Live.API.Scoreboard.Teams
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta


class CallInventory {
    companion object {

        val title = "${ChatColor.DARK_GRAY}> ${ChatColor.BOLD}通話${ChatColor.RESET}${ChatColor.DARK_GRAY} > ${ChatColor.BOLD}通話先選択${ChatColor.RESET}${ChatColor.DARK_GRAY} > "

        val arrowLeft = "MHF_ArrowLeft"
        val arrowRight = "MHF_ArrowRight"

        private val itemFlags = arrayOf(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS)

        fun getPlayers(p: Player): List<List<Player>> {
            val maxCount = 36

            return when (Teams.getJoinedTeam(p)) {
                Teams.OnlineTeam.TOSO_ADMIN -> MainAPI.divide(Bukkit.getOnlinePlayers().filter { DiscordManager.integrationMap.containsKey(it.uniqueId) }.toList(), maxCount)
                Teams.OnlineTeam.TOSO_PLAYER, Teams.OnlineTeam.TOSO_SUCCESS -> MainAPI.divide(
                        Bukkit.getOnlinePlayers()
                                .filter { Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_ADMIN, it) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_PLAYER, it) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_SUCCESS, it) }
                                .filter { DiscordManager.integrationMap.containsKey(it.uniqueId) }
                                .toList(),
                        maxCount
                )
                Teams.OnlineTeam.TOSO_HUNTER, Teams.OnlineTeam.TOSO_TUHO -> MainAPI.divide(
                        Bukkit.getOnlinePlayers()
                                .filter { Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_ADMIN, it) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_HUNTER, it) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_TUHO, it) }
                                .filter { DiscordManager.integrationMap.containsKey(it.uniqueId) }
                                .toList(),
                        maxCount
                )
                else -> mutableListOf()
            }
        }

        fun getInventory(p: Player, page: Int = 0): Inventory {
            val inv = Bukkit.createInventory(null, 9 * 6, "$title${page + 1}")

            val list = getPlayers(p)

            val itemStackBorder = ItemStack(Material.BLACK_STAINED_GLASS_PANE)
            val itemMetaBorder = itemStackBorder.itemMeta!!
            itemMetaBorder.addItemFlags(*itemFlags)
            itemMetaBorder.setDisplayName("${ChatColor.BOLD}")
            itemStackBorder.itemMeta = itemMetaBorder
            for (i in 0..8)
                inv.setItem(i, itemStackBorder)
            for (i in 45..53)
                inv.setItem(i, itemStackBorder)

            val itemStackHome = ItemStack(Material.WHITE_STAINED_GLASS_PANE)
            val itemMetaHome = itemStackHome.itemMeta!!
            itemMetaHome.addItemFlags(*itemFlags)
            itemMetaHome.setDisplayName("${ChatColor.GOLD}${ChatColor.BOLD}${ChatColor.UNDERLINE}ホーム")
            itemMetaHome.lore = listOf("${ChatColor.YELLOW}クリックして${ChatColor.GOLD}${ChatColor.BOLD}${ChatColor.UNDERLINE}ホーム画面${ChatColor.RESET}${ChatColor.YELLOW}を表示します。")
            itemStackHome.itemMeta = itemMetaHome
            inv.setItem(49, itemStackHome)

            if (list.size > 1) {
                val itemStackBack = ItemStack(Material.PLAYER_HEAD, 1)
                val itemMetaBack = (itemStackBack.itemMeta as SkullMeta)
                itemMetaBack.addItemFlags(*itemFlags)
                itemMetaBack.owningPlayer = Bukkit.getOfflinePlayer(arrowLeft)
                itemMetaBack.setDisplayName("${ChatColor.BLUE}${ChatColor.BOLD}${ChatColor.UNDERLINE}前のページ")
                itemMetaBack.lore = listOf()
                itemStackBack.itemMeta = itemMetaBack
                inv.setItem(45, itemStackBack)

                val itemStackForward = ItemStack(Material.PLAYER_HEAD, 1)
                val itemMetaForward = (itemStackForward.itemMeta as SkullMeta)
                itemMetaForward.addItemFlags(*itemFlags)
                itemMetaForward.owningPlayer = Bukkit.getOfflinePlayer(arrowRight)
                itemMetaForward.setDisplayName("${ChatColor.BLUE}${ChatColor.BOLD}${ChatColor.UNDERLINE}次のページ")
                itemMetaForward.lore = listOf()
                itemStackForward.itemMeta = itemMetaForward
                inv.setItem(53, itemStackForward)
            }

            for (player in list[page]) {
                val itemStackPlayerInfo = ItemStack(Material.PLAYER_HEAD, 1)
                val itemMetaPlayerInfo = (itemStackPlayerInfo.itemMeta as SkullMeta)
                itemMetaPlayerInfo.addItemFlags(*itemFlags)
                itemMetaPlayerInfo.owningPlayer = player
                itemMetaPlayerInfo.setDisplayName("${ChatColor.BLUE}${ChatColor.BOLD}${ChatColor.UNDERLINE}${player.name}")
                itemMetaPlayerInfo.lore = listOf()
                itemStackPlayerInfo.itemMeta = itemMetaPlayerInfo
                inv.addItem(itemStackPlayerInfo)
            }
            return inv
        }
    }
}
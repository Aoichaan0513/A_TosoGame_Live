package jp.aoichaan0513.A_TosoGame_Live.Inventory

import jp.aoichaan0513.A_TosoGame_Live.API.Interfaces.IMission
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.Player.PlayerManager
import jp.aoichaan0513.A_TosoGame_Live.Mission.MissionManager
import jp.aoichaan0513.A_TosoGame_Live.Utils.ItemUtil
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.BookMeta

class MissionInventory {
    companion object {

        val missionTitle = "${ChatColor.DARK_GRAY}> ${ChatColor.BOLD}ホーム${ChatColor.RESET}${ChatColor.DARK_GRAY} > ${ChatColor.BOLD}ミッションアプリ"
        val tutatuHintTitle = "${ChatColor.DARK_GRAY}> ${ChatColor.BOLD}ホーム${ChatColor.RESET}${ChatColor.DARK_GRAY} > ${ChatColor.BOLD}ミッションアプリ (通達・ヒント)"
        val endTitle = "${ChatColor.DARK_GRAY}> ${ChatColor.BOLD}ホーム${ChatColor.RESET}${ChatColor.DARK_GRAY} > ${ChatColor.BOLD}ミッションアプリ (終了)"


        fun getInventory(type: MissionManager.MissionType): Inventory {
            val inv = when (type) {
                MissionManager.MissionType.TUTATU_HINT -> Bukkit.createInventory(null, 9 * 6, tutatuHintTitle)
                MissionManager.MissionType.END -> Bukkit.createInventory(null, 9 * 6, endTitle)
                else -> Bukkit.createInventory(null, 9 * 6, missionTitle)
            }

            val itemStackBorder = ItemStack(Material.BLACK_STAINED_GLASS_PANE)
            val itemMetaBorder = itemStackBorder.itemMeta!!
            itemMetaBorder.addItemFlags(*ItemUtil.itemFlags)
            itemMetaBorder.setDisplayName("${ChatColor.BOLD}")
            itemStackBorder.itemMeta = itemMetaBorder
            for (i in 9..17)
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

            val itemStackMission = ItemStack(Material.RED_CONCRETE)
            val itemMetaMission = itemStackMission.itemMeta!!
            itemMetaMission.addItemFlags(*ItemUtil.itemFlags)
            if (type == MissionManager.MissionType.MISSION)
                itemMetaMission.addEnchant(Enchantment.DURABILITY, 1, true)
            itemMetaMission.setDisplayName("${ChatColor.RED}${ChatColor.BOLD}${ChatColor.UNDERLINE}ミッション")
            itemMetaMission.lore = listOf("${ChatColor.GRAY}クリックして${ChatColor.RED}${ChatColor.BOLD}${ChatColor.UNDERLINE}現在実行中のミッションリスト${ChatColor.RESET}${ChatColor.GRAY}を開きます。")
            itemStackMission.itemMeta = itemMetaMission
            for (i in 0..2)
                inv.setItem(i, itemStackMission)

            val itemStackTutatuHint = ItemStack(Material.YELLOW_CONCRETE)
            val itemMetaTutatuHint = itemStackTutatuHint.itemMeta!!
            itemMetaTutatuHint.addItemFlags(*ItemUtil.itemFlags)
            if (type == MissionManager.MissionType.TUTATU_HINT)
                itemMetaTutatuHint.addEnchant(Enchantment.DURABILITY, 1, true)
            itemMetaTutatuHint.setDisplayName("${ChatColor.YELLOW}${ChatColor.BOLD}${ChatColor.UNDERLINE}通達・ヒント")
            itemMetaTutatuHint.lore = listOf("${ChatColor.GRAY}クリックして${ChatColor.YELLOW}${ChatColor.BOLD}${ChatColor.UNDERLINE}通達・ヒントリスト${ChatColor.RESET}${ChatColor.GRAY}を開きます。")
            itemStackTutatuHint.itemMeta = itemMetaTutatuHint
            for (i in 3..5)
                inv.setItem(i, itemStackTutatuHint)

            val itemStackEnd = ItemStack(Material.LIME_CONCRETE)
            val itemMetaEnd = itemStackEnd.itemMeta!!
            itemMetaEnd.addItemFlags(*ItemUtil.itemFlags)
            if (type == MissionManager.MissionType.END)
                itemMetaEnd.addEnchant(Enchantment.DURABILITY, 1, true)
            itemMetaEnd.setDisplayName("${ChatColor.GREEN}${ChatColor.BOLD}${ChatColor.UNDERLINE}終了したミッション")
            itemMetaEnd.lore = listOf("${ChatColor.GRAY}クリックして${ChatColor.GREEN}${ChatColor.BOLD}${ChatColor.UNDERLINE}終了したミッションのリスト${ChatColor.RESET}${ChatColor.GRAY}を開きます。")
            itemStackEnd.itemMeta = itemMetaEnd
            for (i in 6..8)
                inv.setItem(i, itemStackEnd)

            for (mission in MissionManager.getMissions(type)) {
                val descriptionFirst = mission.descriptions[0]

                val itemStackMissionDetail = ItemStack(mission.material, mission.count)
                val itemMetaMissionDetail = itemStackMissionDetail.itemMeta!!
                itemMetaMissionDetail.addItemFlags(*ItemUtil.itemFlags)
                itemMetaMissionDetail.setCustomModelData(mission.count)

                itemMetaMissionDetail.setDisplayName(when (type) {
                    MissionManager.MissionType.TUTATU_HINT -> "${ChatColor.YELLOW}${ChatColor.BOLD}${ChatColor.UNDERLINE}${mission.title}"
                    MissionManager.MissionType.END -> "${ChatColor.GREEN}${ChatColor.BOLD}${ChatColor.UNDERLINE}${mission.title}"
                    else -> "${ChatColor.RED}${ChatColor.BOLD}${ChatColor.UNDERLINE}${mission.title}"
                })

                itemMetaMissionDetail.lore = if (descriptionFirst.length > 15) {
                    listOf("${ChatColor.RESET}${descriptionFirst.substring(0, 15).replace("\n", " ")}…", "${ChatColor.GRAY}${ChatColor.UNDERLINE}続きはここをクリック")
                } else {
                    listOf("${ChatColor.RESET}${descriptionFirst.replace("\n", " ")}")
                }

                itemStackMissionDetail.itemMeta = itemMetaMissionDetail
                inv.addItem(itemStackMissionDetail)
            }
            return inv
        }

        fun openBook(p: Player, id: Int, missionType: MissionManager.MissionType = MissionManager.MissionType.MISSION) {
            openBook(p, MissionManager.getMission(id, missionType) ?: return)
        }

        fun openBook(p: Player, mission: IMission) {
            val bookStack = ItemStack(Material.WRITTEN_BOOK, 1)
            val bookMeta = bookStack.itemMeta as BookMeta
            bookMeta.title = "逃走中"
            bookMeta.author = "A_TosoGame_Live"
            for (v in mission.descriptions.indices)
                bookMeta.addPage("${if (v == 0) "${ChatColor.DARK_RED}${mission.title}${ChatColor.RESET}\n\n" else ""}${PlayerManager.loadConfig(p).bookForegroundColor.color}${mission.descriptions[v]}")
            bookStack.itemMeta = bookMeta
            openBook(p, bookStack)
        }

        private fun openBook(p: Player, book: ItemStack?) {
            p.openBook(book ?: return)
        }
    }
}
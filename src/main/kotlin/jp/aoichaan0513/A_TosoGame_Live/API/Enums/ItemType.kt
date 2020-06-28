package jp.aoichaan0513.A_TosoGame_Live.API.Enums

import org.bukkit.Material

enum class ItemType(val material: Material, val defaultSlot: Int) {
    MAP(Material.FILLED_MAP, 40),
    BOOK(Material.BOOK, 3),

    BONE(Material.BONE, 0),
    FEATHER(Material.FEATHER, 1),
    EGG(Material.EGG, 2),
    SNOWBALL(Material.SNOWBALL, 4),
    RABBIT_FOOT(Material.RABBIT_FOOT, 5),
    SLIME_BALL(Material.SLIME_BALL, 6);

    val itemName
        get() = name.toLowerCase()

    companion object {

        fun getItemType(itemName: String): ItemType {
            return values().firstOrNull { it.itemName.equals(itemName.toLowerCase(), true) } ?: BOOK
        }

        fun getItemType(material: Material): ItemType {
            return values().firstOrNull { it.material == material } ?: BOOK
        }
    }
}
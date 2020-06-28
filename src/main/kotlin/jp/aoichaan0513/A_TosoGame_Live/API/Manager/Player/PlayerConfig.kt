package jp.aoichaan0513.A_TosoGame_Live.API.Manager.Player

import jp.aoichaan0513.A_TosoGame_Live.API.Enums.Advancement
import jp.aoichaan0513.A_TosoGame_Live.API.Enums.ItemType
import jp.aoichaan0513.A_TosoGame_Live.API.Interfaces.IConfig
import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI
import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI.PrefixType
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.World.WorldManager
import jp.aoichaan0513.A_TosoGame_Live.Main
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

class PlayerConfig(val uuid: UUID) {

    val file: File
    var config: YamlConfiguration

    var inventoryConfig: InventoryConfig
        private set
    var advancementConfig: AdvancementConfig
        private set

    init {
        val baseFileName = "player.yml"
        val fileName = "$uuid.yml"

        file = File("${Main.pluginInstance.getDataFolder()}${Main.FILE_SEPARATOR}players${Main.FILE_SEPARATOR}$fileName")
        if (file.exists()) {
            config = YamlConfiguration.loadConfiguration(file)
        } else {
            try {
                Files.copy(Main.pluginInstance.getResource(baseFileName), Paths.get("${Main.pluginInstance.getDataFolder()}${Main.FILE_SEPARATOR}players${Main.FILE_SEPARATOR}$fileName"))
            } catch (e: IOException) {
                e.printStackTrace()
            }
            config = YamlConfiguration.loadConfiguration(file)
        }

        inventoryConfig = InventoryConfig(file, config)
        advancementConfig = AdvancementConfig(file, config)
    }

    var difficulty: WorldManager.Difficulty
        get() = WorldManager.Difficulty.getDifficulty(config.getInt("difficulty", 1))
        set(difficulty) {
            config["difficulty"] = difficulty.id
            save()
        }

    var discordId: Long
        get() = config.getLong("discordId", 0)
        set(discordId) {
            config["discordId"] = discordId
            save()
        }

    var money: Long
        get() = config.getLong("money", 0)
        set(money) {
            config["money"] = money
            save()
        }

    var permission: Boolean
        get() = config.getBoolean("hasPermission", false)
        set(hasPermission) {
            config["hasPermission"] = hasPermission
            save()
        }

    var broadCaster: Boolean
        get() = config.getBoolean("isBroadCaster", false)
        set(isBroadCaster) {
            config["isBroadCaster"] = isBroadCaster
            save()
        }

    var bookForegroundColor: BookForegroundColor
        get() = BookForegroundColor.getColor(config.getString("bookForegroundColor") ?: "black")
        set(bookForegroundColor) {
            config["bookForegroundColor"] = bookForegroundColor.objectName
            save()
        }

    inner class InventoryConfig(f: File, c: YamlConfiguration) : IConfig(f, c) {
        private val PATH = "inventory"

        val items: Map<ItemType, Int>
            get() {
                val map = mutableMapOf<ItemType, Int>()

                for (key in config.getConfigurationSection(PATH)!!.getKeys(false)) {
                    val itemType = ItemType.getItemType(key)
                    map[itemType] = getSlot(itemType)
                }

                return map
            }

        private fun getSlot(key: String): Int {
            return getSlot(ItemType.getItemType(key))
        }

        fun getSlot(itemType: ItemType): Int {
            return c.getInt("$PATH.${itemType.itemName}", itemType.defaultSlot)
        }

        fun getSlot(slot: Int): ItemType? {
            val itemName = config.getConfigurationSection(PATH)!!.getKeys(false).firstOrNull { c.getInt("$PATH.${it}") == slot }
            return if (itemName != null) ItemType.getItemType(itemName) else null
        }

        fun setSlot(itemType: ItemType, slot: Int) {
            if (slot < 1) return

            val oldItem = getSlot(slot)
            if (oldItem != null)
                c["$PATH.${oldItem.itemName}"] = c.getInt("$PATH.${itemType.itemName}")
            c["$PATH.${itemType.itemName}"] = slot
            save()
        }

        override fun save() {
            super.save()
            inventoryConfig = this
        }
    }

    inner class AdvancementConfig(f: File, c: YamlConfiguration) : IConfig(f, c) {
        private val PATH = "advancements"

        fun addAdvancement(advancement: Advancement) {
            if (advancement == Advancement.UNKNOWN) return

            val set = c.getIntegerList(PATH).toMutableSet()
            set.add(advancement.id)
            c[PATH] = set.toList()
            save()
        }

        fun removeAdvancement(advancement: Advancement) {
            if (advancement == Advancement.UNKNOWN) return

            val set = c.getIntegerList(PATH).toMutableSet()
            set.remove(advancement.id)
            c[PATH] = set.toList()
            save()
        }

        fun hasAdvancement(advancement: Advancement): Boolean {
            return advancements.any { it.id == advancement.id }
        }

        var advancements: Set<Advancement>
            get() {
                val set = mutableSetOf<Advancement>()
                c.getIntegerList(PATH).filter { it != Advancement.UNKNOWN.id }.forEach { set.add(Advancement.getAdvancement(it)) }
                return set
            }
            set(advancements) {
                val set = mutableSetOf<Int>()
                advancements.filter { it != Advancement.UNKNOWN }.forEach { set.add(it.id) }
                c[PATH] = set.toList()
                save()
            }

        fun setAdvancements(vararg advancements: Advancement) {
            val set = mutableSetOf<Int>()
            advancements.filter { it != Advancement.UNKNOWN }.forEach { set.add(it.id) }
            c[PATH] = set.toList()
            save()
        }

        override fun save() {
            super.save()
            advancementConfig = this
        }
    }

    fun save() {
        try {
            config.save(file)
        } catch (e: IOException) {
            Bukkit.getConsoleSender().sendMessage("""
                ${MainAPI.getPrefix(PrefixType.ERROR)}予期しないエラーが発生しました。
                ${MainAPI.getPrefix(PrefixType.SECONDARY)}位置: ${Main.PACKAGE_PATH}.API.Manager.Player.PlayerConfig
            """.trimIndent())
            e.printStackTrace()
        }
    }

    enum class BookForegroundColor(val color: ChatColor) {
        BLACK(ChatColor.BLACK),
        WHITE(ChatColor.WHITE);

        val objectName: String
            get() = name.toLowerCase()

        companion object {

            fun getColor(name: String): BookForegroundColor {
                return values().firstOrNull { it.objectName.equals(name, true) } ?: BLACK
            }

            fun getColor(chatColor: ChatColor): BookForegroundColor {
                return values().firstOrNull { it.color == chatColor } ?: BLACK
            }
        }
    }
}
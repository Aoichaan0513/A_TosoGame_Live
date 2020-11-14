package jp.aoichaan0513.A_TosoGame_Live.API.Advancement

import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI
import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI.PrefixType
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.Player.PlayerManager
import jp.aoichaan0513.A_TosoGame_Live.Utils.ReflectionUtil
import net.md_5.bungee.api.chat.ComponentBuilder
import net.md_5.bungee.api.chat.HoverEvent
import net.md_5.bungee.api.chat.TextComponent
import net.minecraft.server.v1_15_R1.NBTTagCompound
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.craftbukkit.v1_15_R1.inventory.CraftItemStack
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import java.util.*
import java.util.logging.Level

enum class Advancement {
    FIRST_JOIN(0, Material.GRASS_BLOCK, Sound.UI_TOAST_CHALLENGE_COMPLETE, "初めての逃走中", listOf("${ChatColor.WHITE}逃走中への初参加を果たす。")),
    FIRST_HUNTER(1, Material.DIAMOND_CHESTPLATE, "初めてのハンター", listOf("${ChatColor.WHITE}ハンターの選出を果たす。")),
    FIRST_GAME_CLEAR(2, Material.EMERALD_BLOCK, Sound.UI_TOAST_CHALLENGE_COMPLETE, "初めての逃走成功", listOf("${ChatColor.WHITE}逃走成功を果たす。")),
    FIRST_HUNTER_TOUCH(3, "初めての確保", listOf("${ChatColor.WHITE}逃走者を確保する。")),
    LUCKY_SNOWBALL(4, Material.SNOWBALL, "幸運の持ち主", listOf("${ChatColor.WHITE}オープニングゲームが成功し、さらにランダムで雪玉が配られる。")),

    UPDATE_2_0_0(1000, "生まれ変わった逃走中", listOf("${ChatColor.WHITE}新しくなった逃走中に参加する。")),

    UNKNOWN(-1, "", listOf(""));

    val id: Int
    val material: Material
    val sound: Sound
    val title: String
    val descriptions: List<String>

    constructor(id: Int, title: String, descriptions: List<String>) {
        this.id = id
        material = Material.BLACK_CONCRETE
        sound = Sound.ENTITY_PLAYER_LEVELUP
        this.title = title
        this.descriptions = descriptions
    }

    constructor(id: Int, material: Material, title: String, descriptions: List<String>) {
        this.id = id
        this.material = material
        sound = Sound.ENTITY_PLAYER_LEVELUP
        this.title = title
        this.descriptions = descriptions
    }

    constructor(id: Int, sound: Sound, title: String, descriptions: List<String>) {
        this.id = id
        material = Material.BLACK_CONCRETE
        this.sound = sound
        this.title = title
        this.descriptions = descriptions
    }

    constructor(id: Int, material: Material, sound: Sound, title: String, descriptions: List<String>) {
        this.id = id
        this.material = material
        this.sound = sound
        this.title = title
        this.descriptions = descriptions
    }

    fun sendMessage(p: Player) {
        val itemStack = ItemStack(material)
        val itemMeta = itemStack.itemMeta!!
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS)
        itemMeta.setDisplayName("${ChatColor.GOLD}${ChatColor.UNDERLINE}$title")
        itemMeta.lore = descriptions
        itemMeta.setCustomModelData(id)
        itemStack.itemMeta = itemMeta

        val itemJSON = convertItemStackToJSON(itemStack)

        val textComponent1 = TextComponent("${MainAPI.getPrefix(PrefixType.SECONDARY)}${p.name}は進捗 ")
        val textComponent2 = TextComponent("${ChatColor.GOLD}${ChatColor.UNDERLINE}$title${ChatColor.RESET}${ChatColor.GRAY}")
        textComponent2.hoverEvent = HoverEvent(HoverEvent.Action.SHOW_ITEM, ComponentBuilder(TextComponent(itemJSON)).create())
        val textComponent3 = TextComponent("を達成した。")
        textComponent1.addExtra(textComponent2)
        textComponent1.addExtra(textComponent3)

        AdvancementMessage(UUID.randomUUID().toString(), title, descriptions[0], material).showTo(p)

        p.spigot().sendMessage(textComponent1)
        p.playSound(p.location, sound, 1f, 1f)
    }

    private fun convertItemStackToJsonRegular(itemStack: ItemStack): String {
        val nmsItemStack = CraftItemStack.asNMSCopy(itemStack)
        val compound = nmsItemStack.save(NBTTagCompound())
        return compound.toString()
    }

    private fun convertItemStackToJSON(itemStack: ItemStack): String? {
        val craftItemStackClazz = ReflectionUtil.getOBCClass("inventory.CraftItemStack")
        val asNMSCopyMethod = ReflectionUtil.getMethod(craftItemStackClazz, "asNMSCopy", ItemStack::class.java)
        val nmsItemStackClazz = ReflectionUtil.getNMSClass("ItemStack")
        val nbtTagCompoundClazz = ReflectionUtil.getNMSClass("NBTTagCompound")
        val saveNmsItemStackMethod = ReflectionUtil.getMethod(nmsItemStackClazz, "save", nbtTagCompoundClazz)
        val nmsNbtTagCompoundObj: Any
        val nmsItemStackObj: Any
        val itemAsJsonObject: Any
        try {
            nmsNbtTagCompoundObj = nbtTagCompoundClazz!!.newInstance()
            nmsItemStackObj = asNMSCopyMethod!!.invoke(null, itemStack)
            itemAsJsonObject = saveNmsItemStackMethod!!.invoke(nmsItemStackObj, nmsNbtTagCompoundObj)
        } catch (t: Throwable) {
            Bukkit.getLogger().log(Level.SEVERE, "Failed to serialize itemstack to nms item", t)
            return null
        }
        return itemAsJsonObject.toString()
    }

    companion object {
        fun getAdvancement(id: Int): Advancement {
            return values().firstOrNull { it.id == id } ?: UNKNOWN
        }

        fun addAdvancement(p: Player, advancement: Advancement) {
            val playerConfig = PlayerManager.loadConfig(p)
            if (playerConfig.advancementConfig.hasAdvancement(advancement))
                return

            playerConfig.advancementConfig.addAdvancement(advancement)
            advancement.sendMessage(p)
        }
    }
}
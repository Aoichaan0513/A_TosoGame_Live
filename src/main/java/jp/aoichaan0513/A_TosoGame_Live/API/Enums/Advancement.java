package jp.aoichaan0513.A_TosoGame_Live.API.Enums;

import jp.aoichaan0513.A_TosoGame_Live.API.Advancement.AdvancementMessage;
import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI;
import jp.aoichaan0513.A_TosoGame_Live.Utils.ReflectionUtil;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_15_R1.NBTTagCompound;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_15_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

public enum Advancement {
    FIRST_JOIN(0, Material.GRASS_BLOCK, Sound.UI_TOAST_CHALLENGE_COMPLETE, "初めての逃走中", Collections.singletonList("" + ChatColor.WHITE + "逃走中への初参加を果たす。")),
    FIRST_HUNTER(1, Material.DIAMOND_CHESTPLATE, "初めてのハンター", Collections.singletonList("ハンターの選出を果たす。")),
    FIRST_GAME_CLEAR(2, Material.EMERALD_BLOCK, Sound.UI_TOAST_CHALLENGE_COMPLETE, "初めての逃走成功", Collections.singletonList("逃走成功を果たす。")),
    FIRST_HUNTER_TOUCH(3, "初めての確保", Collections.singletonList("逃走者を確保する。")),

    UNKNOWN(-1, "", Collections.singletonList(""));

    private final int id;
    private final Material material;
    private final Sound sound;
    private final String title;
    private final List<String> descriptions;

    private Advancement(int id, String title, List<String> descriptions) {
        this.id = id;
        this.material = Material.BLACK_CONCRETE;
        this.sound = Sound.ENTITY_PLAYER_LEVELUP;
        this.title = title;
        this.descriptions = descriptions;
    }

    private Advancement(int id, Material material, String title, List<String> descriptions) {
        this.id = id;
        this.material = material;
        this.sound = Sound.ENTITY_PLAYER_LEVELUP;
        this.title = title;
        this.descriptions = descriptions;
    }

    private Advancement(int id, Sound sound, String title, List<String> descriptions) {
        this.id = id;
        this.material = Material.BLACK_CONCRETE;
        this.sound = sound;
        this.title = title;
        this.descriptions = descriptions;
    }

    private Advancement(int id, Material material, Sound sound, String title, List<String> descriptions) {
        this.id = id;
        this.material = material;
        this.sound = sound;
        this.title = title;
        this.descriptions = descriptions;
    }

    public int getId() {
        return id;
    }

    public Material getMaterial() {
        return material;
    }

    public Sound getSound() {
        return sound;
    }

    public String getTitle() {
        return title;
    }

    public List<String> getDescriptions() {
        return descriptions;
    }

    public void sendMessage(Player p) {
        ItemStack itemStack = new ItemStack(getMaterial());
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
        itemMeta.setDisplayName("" + ChatColor.GOLD + ChatColor.UNDERLINE + getTitle());
        itemMeta.setLore(getDescriptions());
        itemMeta.setCustomModelData(id);
        itemStack.setItemMeta(itemMeta);

        String itemJSON = convertItemStackToJSON(itemStack);

        TextComponent textComponent1 = new TextComponent(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + p.getName() + "は進捗 ");
        TextComponent textComponent2 = new TextComponent("" + ChatColor.GOLD + ChatColor.UNDERLINE + getTitle() + ChatColor.RESET + ChatColor.GRAY);
        textComponent2.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ITEM, new ComponentBuilder(new TextComponent(itemJSON)).create()));
        TextComponent textComponent3 = new TextComponent("を達成した。");

        textComponent1.addExtra(textComponent2);
        textComponent1.addExtra(textComponent3);

        p.spigot().sendMessage(textComponent1);

        new AdvancementMessage(UUID.randomUUID().toString(), getTitle(), getDescriptions().get(0), getMaterial()).showTo(p);

        p.playSound(p.getLocation(), getSound(), 1, 1);
    }

    private String convertItemStackToJsonRegular(ItemStack itemStack) {
        net.minecraft.server.v1_15_R1.ItemStack nmsItemStack = CraftItemStack.asNMSCopy(itemStack);
        NBTTagCompound compound = nmsItemStack.save(new NBTTagCompound());

        return compound.toString();
    }

    private String convertItemStackToJSON(ItemStack itemStack) {
        Class<?> craftItemStackClazz = ReflectionUtil.getOBCClass("inventory.CraftItemStack");
        Method asNMSCopyMethod = ReflectionUtil.getMethod(craftItemStackClazz, "asNMSCopy", ItemStack.class);

        Class<?> nmsItemStackClazz = ReflectionUtil.getNMSClass("ItemStack");
        Class<?> nbtTagCompoundClazz = ReflectionUtil.getNMSClass("NBTTagCompound");
        Method saveNmsItemStackMethod = ReflectionUtil.getMethod(nmsItemStackClazz, "save", nbtTagCompoundClazz);

        Object nmsNbtTagCompoundObj;
        Object nmsItemStackObj;
        Object itemAsJsonObject;

        try {
            nmsNbtTagCompoundObj = nbtTagCompoundClazz.newInstance();
            nmsItemStackObj = asNMSCopyMethod.invoke(null, itemStack);
            itemAsJsonObject = saveNmsItemStackMethod.invoke(nmsItemStackObj, nmsNbtTagCompoundObj);
        } catch (Throwable t) {
            Bukkit.getLogger().log(Level.SEVERE, "failed to serialize itemstack to nms item", t);
            return null;
        }

        return itemAsJsonObject.toString();
    }


    public static Advancement getAdvancement(int id) {
        for (Advancement advancement : values())
            if (advancement.id == id)
                return advancement;
        return UNKNOWN;
    }
}

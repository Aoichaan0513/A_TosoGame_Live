package jp.aoichaan0513.A_TosoGame_Live.API.Manager.Inventory.Right;

import jp.aoichaan0513.A_TosoGame_Live.API.Interfaces.IMission;
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.MissionManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class MissionInventory {

    public static final String missionTitle = ChatColor.DARK_GRAY + "> " + ChatColor.BOLD + "ホーム" + ChatColor.RESET + ChatColor.DARK_GRAY + " > " + ChatColor.BOLD + "ミッションアプリ";
    public static final String tutatuHintTitle = ChatColor.DARK_GRAY + "> " + ChatColor.BOLD + "ホーム" + ChatColor.RESET + ChatColor.DARK_GRAY + " > " + ChatColor.BOLD + "ミッションアプリ (通達・ヒント)";
    public static final String endTitle = ChatColor.DARK_GRAY + "> " + ChatColor.BOLD + "ホーム" + ChatColor.RESET + ChatColor.DARK_GRAY + " > " + ChatColor.BOLD + "ミッションアプリ (終了)";

    private static HashMap<Integer, IMission> missionList = new HashMap<>();

    private static int missionAmount = 1;
    private static int tutatuHintAmount = 1;
    private static int endAmount = 1;

    private static int missionSlot = 18;
    private static int tutatuHintSlot = 18;
    private static int endSlot = 18;

    private static Inventory missionInventory = Bukkit.createInventory(null, 9 * 6, missionTitle);
    private static Inventory tutatuHintInventory = Bukkit.createInventory(null, 9 * 6, tutatuHintTitle);
    private static Inventory endInventory = Bukkit.createInventory(null, 9 * 6, endTitle);

    private static final ItemFlag[] itemFlags = new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS};

    public static void reset() {
        missionAmount = 1;
        tutatuHintAmount = 1;
        endAmount = 1;

        missionSlot = 18;
        tutatuHintSlot = 18;
        endSlot = 18;

        resetInventory();
        resetMissions();
    }

    public static void resetInventory() {
        missionInventory = Bukkit.createInventory(null, 9 * 6, missionTitle);
        tutatuHintInventory = Bukkit.createInventory(null, 9 * 6, tutatuHintTitle);
        endInventory = Bukkit.createInventory(null, 9 * 6, endTitle);
        return;
    }

    public static Inventory getInventory(MissionManager.MissionBookType type) {
        if (type == MissionManager.MissionBookType.MISSION) {
            Inventory inv = missionInventory;

            ItemStack missionStack = new ItemStack(Material.RED_CONCRETE);
            ItemMeta missionMeta = missionStack.getItemMeta();
            missionMeta.addItemFlags(itemFlags);
            missionMeta.addEnchant(Enchantment.DURABILITY, 1, true);
            missionMeta.setDisplayName("" + ChatColor.RED + ChatColor.BOLD + ChatColor.UNDERLINE + "ミッション");
            missionMeta.setLore(Collections.singletonList(ChatColor.GRAY + "クリックして" + ChatColor.RED + ChatColor.BOLD + ChatColor.UNDERLINE + "現在実行中のミッションリスト" + ChatColor.GRAY + "を開きます。"));
            missionStack.setItemMeta(missionMeta);


            for (int i = 0; i < 3; i++)
                inv.setItem(i, missionStack);

            ItemStack tutatuHintStack = new ItemStack(Material.YELLOW_CONCRETE);
            ItemMeta tutatuHintMeta = tutatuHintStack.getItemMeta();
            tutatuHintMeta.addItemFlags(itemFlags);
            tutatuHintMeta.setDisplayName("" + ChatColor.YELLOW + ChatColor.BOLD + ChatColor.UNDERLINE + "通達・ヒント");
            tutatuHintMeta.setLore(Collections.singletonList(ChatColor.GRAY + "クリックして" + ChatColor.YELLOW + ChatColor.BOLD + ChatColor.UNDERLINE + "通達・ヒントリスト" + ChatColor.GRAY + "を開きます。"));
            tutatuHintStack.setItemMeta(tutatuHintMeta);

            for (int i = 3; i < 6; i++)
                inv.setItem(i, tutatuHintStack);

            ItemStack endStack = new ItemStack(Material.LIME_CONCRETE);
            ItemMeta endMeta = endStack.getItemMeta();
            endMeta.addItemFlags(itemFlags);
            endMeta.setDisplayName("" + ChatColor.GREEN + ChatColor.BOLD + ChatColor.UNDERLINE + "終了したミッション");
            endMeta.setLore(Collections.singletonList(ChatColor.GRAY + "クリックして" + ChatColor.GREEN + ChatColor.BOLD + ChatColor.UNDERLINE + "終了したミッションのリスト" + ChatColor.GRAY + "を開きます。"));
            endStack.setItemMeta(endMeta);

            for (int i = 6; i < 9; i++)
                inv.setItem(i, endStack);

            ItemStack grayStack = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
            ItemMeta grayMeta = grayStack.getItemMeta();
            grayMeta.addItemFlags(itemFlags);
            grayMeta.setDisplayName(ChatColor.BOLD + "");
            grayStack.setItemMeta(grayMeta);

            for (int i = 9; i < 18; i++)
                inv.setItem(i, grayStack);
            for (int i = 45; i < 49; i++)
                inv.setItem(i, grayStack);
            for (int i = 50; i < 54; i++)
                inv.setItem(i, grayStack);

            ItemStack homeStack = new ItemStack(Material.WHITE_STAINED_GLASS_PANE);
            ItemMeta homeMeta = homeStack.getItemMeta();
            homeMeta.addItemFlags(itemFlags);
            homeMeta.setDisplayName("" + ChatColor.GOLD + ChatColor.BOLD + ChatColor.UNDERLINE + "ホーム");
            homeMeta.setLore(Collections.singletonList(ChatColor.YELLOW + "クリックして" + ChatColor.GOLD + ChatColor.BOLD + ChatColor.UNDERLINE + "ホーム画面" + ChatColor.RESET + ChatColor.YELLOW + "を表示します。"));
            homeStack.setItemMeta(homeMeta);

            inv.setItem(49, homeStack);

            return inv;
        } else if (type == MissionManager.MissionBookType.TUTATU || type == MissionManager.MissionBookType.HINT) {
            Inventory inv = tutatuHintInventory;

            ItemStack missionStack = new ItemStack(Material.RED_CONCRETE);
            ItemMeta missionMeta = missionStack.getItemMeta();
            missionMeta.addItemFlags(itemFlags);
            missionMeta.setDisplayName("" + ChatColor.RED + ChatColor.BOLD + ChatColor.UNDERLINE + "ミッション");
            missionMeta.setLore(Collections.singletonList(ChatColor.GRAY + "クリックして" + ChatColor.RED + ChatColor.BOLD + ChatColor.UNDERLINE + "現在実行中のミッションリスト" + ChatColor.GRAY + "を開きます。"));
            missionStack.setItemMeta(missionMeta);

            for (int i = 0; i < 3; i++)
                inv.setItem(i, missionStack);

            ItemStack tutatuHintStack = new ItemStack(Material.YELLOW_CONCRETE);
            ItemMeta tutatuHintMeta = tutatuHintStack.getItemMeta();
            tutatuHintMeta.addItemFlags(itemFlags);
            tutatuHintMeta.addEnchant(Enchantment.DURABILITY, 1, true);
            tutatuHintMeta.setDisplayName("" + ChatColor.YELLOW + ChatColor.BOLD + ChatColor.UNDERLINE + "通達・ヒント");
            tutatuHintMeta.setLore(Collections.singletonList(ChatColor.GRAY + "クリックして" + ChatColor.YELLOW + ChatColor.BOLD + ChatColor.UNDERLINE + "通達・ヒントリスト" + ChatColor.GRAY + "を開きます。"));
            tutatuHintStack.setItemMeta(tutatuHintMeta);

            for (int i = 3; i < 6; i++)
                inv.setItem(i, tutatuHintStack);

            ItemStack endStack = new ItemStack(Material.LIME_CONCRETE);
            ItemMeta endMeta = endStack.getItemMeta();
            endMeta.addItemFlags(itemFlags);
            endMeta.setDisplayName("" + ChatColor.GREEN + ChatColor.BOLD + ChatColor.UNDERLINE + "終了したミッション");
            endMeta.setLore(Collections.singletonList(ChatColor.GRAY + "クリックして" + ChatColor.GREEN + ChatColor.BOLD + ChatColor.UNDERLINE + "終了したミッションのリスト" + ChatColor.GRAY + "を開きます。"));
            endStack.setItemMeta(endMeta);

            for (int i = 6; i < 9; i++)
                inv.setItem(i, endStack);

            ItemStack grayStack = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
            ItemMeta grayMeta = grayStack.getItemMeta();
            grayMeta.addItemFlags(itemFlags);
            grayMeta.setDisplayName(ChatColor.BOLD + "");
            grayStack.setItemMeta(grayMeta);

            for (int i = 9; i < 18; i++)
                inv.setItem(i, grayStack);
            for (int i = 45; i < 49; i++)
                inv.setItem(i, grayStack);
            for (int i = 50; i < 54; i++)
                inv.setItem(i, grayStack);

            ItemStack homeStack = new ItemStack(Material.WHITE_STAINED_GLASS_PANE);
            ItemMeta homeMeta = homeStack.getItemMeta();
            homeMeta.addItemFlags(itemFlags);
            homeMeta.setDisplayName("" + ChatColor.GOLD + ChatColor.BOLD + ChatColor.UNDERLINE + "ホーム");
            homeMeta.setLore(Collections.singletonList(ChatColor.YELLOW + "クリックして" + ChatColor.GOLD + ChatColor.BOLD + ChatColor.UNDERLINE + "ホーム画面" + ChatColor.RESET + ChatColor.YELLOW + "を表示します。"));
            homeStack.setItemMeta(homeMeta);

            inv.setItem(49, homeStack);

            return inv;
        } else {
            Inventory inv = endInventory;

            ItemStack missionStack = new ItemStack(Material.RED_CONCRETE);
            ItemMeta missionMeta = missionStack.getItemMeta();
            missionMeta.addItemFlags(itemFlags);
            missionMeta.setDisplayName("" + ChatColor.RED + ChatColor.BOLD + ChatColor.UNDERLINE + "ミッション");
            missionMeta.setLore(Collections.singletonList(ChatColor.GRAY + "クリックして" + ChatColor.RED + ChatColor.BOLD + ChatColor.UNDERLINE + "現在実行中のミッションリスト" + ChatColor.GRAY + "を開きます。"));
            missionStack.setItemMeta(missionMeta);

            for (int i = 0; i < 3; i++)
                inv.setItem(i, missionStack);

            ItemStack tutatuHintStack = new ItemStack(Material.YELLOW_CONCRETE);
            ItemMeta tutatuHintMeta = tutatuHintStack.getItemMeta();
            tutatuHintMeta.addItemFlags(itemFlags);
            tutatuHintMeta.setDisplayName("" + ChatColor.YELLOW + ChatColor.BOLD + ChatColor.UNDERLINE + "通達・ヒント");
            tutatuHintMeta.setLore(Collections.singletonList(ChatColor.GRAY + "クリックして" + ChatColor.YELLOW + ChatColor.BOLD + ChatColor.UNDERLINE + "通達・ヒントリスト" + ChatColor.GRAY + "を開きます。"));
            tutatuHintStack.setItemMeta(tutatuHintMeta);

            for (int i = 3; i < 6; i++)
                inv.setItem(i, tutatuHintStack);

            ItemStack endStack = new ItemStack(Material.LIME_CONCRETE);
            ItemMeta endMeta = endStack.getItemMeta();
            endMeta.addItemFlags(itemFlags);
            endMeta.addEnchant(Enchantment.DURABILITY, 1, true);
            endMeta.setDisplayName("" + ChatColor.GREEN + ChatColor.BOLD + ChatColor.UNDERLINE + "終了したミッション");
            endMeta.setLore(Collections.singletonList(ChatColor.GRAY + "クリックして" + ChatColor.GREEN + ChatColor.BOLD + ChatColor.UNDERLINE + "終了したミッションのリスト" + ChatColor.GRAY + "を開きます。"));
            endStack.setItemMeta(endMeta);

            for (int i = 6; i < 9; i++)
                inv.setItem(i, endStack);

            ItemStack grayStack = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
            ItemMeta grayMeta = grayStack.getItemMeta();
            grayMeta.addItemFlags(itemFlags);
            grayMeta.setDisplayName(ChatColor.BOLD + "");
            grayStack.setItemMeta(grayMeta);

            for (int i = 9; i < 18; i++)
                inv.setItem(i, grayStack);
            for (int i = 45; i < 49; i++)
                inv.setItem(i, grayStack);
            for (int i = 50; i < 54; i++)
                inv.setItem(i, grayStack);

            ItemStack homeStack = new ItemStack(Material.WHITE_STAINED_GLASS_PANE);
            ItemMeta homeMeta = homeStack.getItemMeta();
            homeMeta.addItemFlags(itemFlags);
            homeMeta.setDisplayName("" + ChatColor.GOLD + ChatColor.BOLD + ChatColor.UNDERLINE + "ホーム");
            homeMeta.setLore(Collections.singletonList(ChatColor.YELLOW + "クリックして" + ChatColor.GOLD + ChatColor.BOLD + ChatColor.UNDERLINE + "ホーム画面" + ChatColor.RESET + ChatColor.YELLOW + "を表示します。"));
            homeStack.setItemMeta(homeMeta);

            inv.setItem(49, homeStack);

            return inv;
        }
    }

    public static void setInventory(Inventory inv, MissionManager.MissionBookType type) {
        if (type == MissionManager.MissionBookType.MISSION) {
            missionInventory = inv;
            return;
        } else if (type == MissionManager.MissionBookType.TUTATU || type == MissionManager.MissionBookType.HINT) {
            tutatuHintInventory = inv;
            return;
        } else {
            endInventory = inv;
            return;
        }
    }

    public static void resetMissions() {
        missionList = new HashMap<>();
        return;
    }

    public static void setMissions(HashMap<Integer, IMission> hashMap) {
        missionList = hashMap;
        return;
    }

    public static HashMap<Integer, IMission> getMissions(MissionManager.MissionBookType type) {
        HashMap<Integer, IMission> hashMap = new HashMap<>();

        for (Map.Entry<Integer, IMission> entry : missionList.entrySet()) {
            IMission mission = entry.getValue();

            if (type == MissionManager.MissionBookType.MISSION) {
                if (mission.getType() == MissionManager.MissionBookType.MISSION)
                    hashMap.put(entry.getKey(), mission);
            } else if (type == MissionManager.MissionBookType.TUTATU || type == MissionManager.MissionBookType.HINT) {
                if (mission.getType() == MissionManager.MissionBookType.TUTATU || mission.getType() == MissionManager.MissionBookType.HINT)
                    hashMap.put(entry.getKey(), mission);
            } else {
                if (mission.getType() == MissionManager.MissionBookType.END_MISSION
                        || mission.getType() == MissionManager.MissionBookType.END_TUTATU || mission.getType() == MissionManager.MissionBookType.END_HINT)
                    hashMap.put(entry.getKey(), mission);
            }
        }
        return hashMap;
    }

    private static void addItem(String name, String[] lore, MissionManager.MissionBookType type, Material material) {
        int amount = getAmount(type);
        int slot = getSlot(type);

        Inventory inv = getInventory(type);

        ItemStack itemStack = new ItemStack(material, amount);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(name);
        itemMeta.setLore(Arrays.asList(lore));
        itemStack.setItemMeta(itemMeta);

        inv.setItem(slot, itemStack);

        setInventory(inv, type);

        setSlot(slot + 1, type);
        setAmount(amount + 1, type);
        return;
    }

    public static void removeItem(MissionManager.MissionBookType type, int i) {
        Inventory inv = getInventory(type);
        inv.remove(inv.getItem(i));
        return;
    }

    public static ItemStack getItem(MissionManager.MissionBookType type, int i) {
        return getInventory(type).getItem(i);
    }

    public static ItemStack getItem(MissionManager.MissionBookType type, Material material, int amount) {
        for (ItemStack itemStack : getInventory(type).getContents())
            if (itemStack.getType() == material && itemStack.getAmount() == amount)
                return itemStack;
        return null;
    }

    public static void addMission(String title, List<String> descriptions, MissionManager.MissionBookType type, Material material) {
        int amount = getAmount(type);
        int slot = getSlot(type);

        if (slot < 45) {
            addItem(ChatColor.BOLD + title, new String[]{ChatColor.YELLOW + "続きはこちら"}, type, material);
            missionList.put((type.getId() + (amount * slot)), new IMission(title, descriptions, type, material, amount));
            return;
        }
        return;
    }

    public static void endMission() {
        Inventory missionInv = getInventory(MissionManager.MissionBookType.MISSION);
        Inventory endInv = getInventory(MissionManager.MissionBookType.END_MISSION);

        for (int i = 18; i < 44; i++) {
            int amount = getAmount(MissionManager.MissionBookType.END_MISSION);
            int slot = getSlot(MissionManager.MissionBookType.END_MISSION);

            ItemStack itemStack = missionInv.getItem(i);

            if (itemStack == null) continue;

            endInv.addItem(itemStack);
            missionInv.remove(itemStack);

            setSlot(slot + 1, MissionManager.MissionBookType.END_MISSION);
            setAmount(amount + 1, MissionManager.MissionBookType.END_MISSION);
        }

        setInventory(missionInv, MissionManager.MissionBookType.MISSION);
        setInventory(endInv, MissionManager.MissionBookType.END_MISSION);

        setSlot(18, MissionManager.MissionBookType.MISSION);
        return;
    }

    public static void openBook(ItemStack book, Player p) {
        p.openBook(book);
        /*
        int slot = p.getInventory().getHeldItemSlot();
        ItemStack old = p.getInventory().getItem(slot);
        p.getInventory().setItem(slot, book);

        ByteBuf buf = Unpooled.buffer(256);
        buf.setByte(0, (byte) 0);
        buf.writerIndex(1);

        PacketPlayOutCustomPayload packet = new PacketPlayOutCustomPayload(new MinecraftKey("minecraft:book_open"), new PacketDataSerializer(buf));
        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
        p.getInventory().setItem(slot, old);
        */
    }

    public static int getAmount(MissionManager.MissionBookType type) {
        if (type == MissionManager.MissionBookType.MISSION) {
            return missionAmount;
        } else if (type == MissionManager.MissionBookType.TUTATU || type == MissionManager.MissionBookType.HINT) {
            return tutatuHintAmount;
        } else {
            return endAmount;
        }
    }

    private static void setAmount(int amount, MissionManager.MissionBookType type) {
        if (type == MissionManager.MissionBookType.MISSION) {
            missionAmount = amount;
        } else if (type == MissionManager.MissionBookType.TUTATU || type == MissionManager.MissionBookType.HINT) {
            tutatuHintAmount = amount;
        } else {
            endAmount = amount;
        }
    }

    public static int getSlot(MissionManager.MissionBookType type) {
        if (type == MissionManager.MissionBookType.MISSION) {
            return missionSlot;
        } else if (type == MissionManager.MissionBookType.TUTATU || type == MissionManager.MissionBookType.HINT) {
            return tutatuHintSlot;
        } else {
            return endSlot;
        }
    }

    private static void setSlot(int slot, MissionManager.MissionBookType type) {
        if (type == MissionManager.MissionBookType.MISSION) {
            missionSlot = slot;
        } else if (type == MissionManager.MissionBookType.TUTATU || type == MissionManager.MissionBookType.HINT) {
            tutatuHintSlot = slot;
        } else {
            endSlot = slot;
        }
    }
}

package jp.aoichaan0513.A_TosoGame_Live.API.Manager.Inventory;

import jp.aoichaan0513.A_TosoGame_Live.API.Manager.World.WorldConfig;
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.World.WorldManager;
import jp.aoichaan0513.A_TosoGame_Live.Main;
import jp.aoichaan0513.A_TosoGame_Live.Utils.DateTime.TimeFormat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.util.Arrays;

public class MapInventory {

    public static final String editTitle = ChatColor.DARK_GRAY + "> " + ChatColor.BOLD + "ホーム" + ChatColor.RESET + ChatColor.DARK_GRAY + " > " + ChatColor.BOLD + "マップ設定";
    public static final String difficultyTitle = ChatColor.DARK_GRAY + "> " + ChatColor.BOLD + "マップ設定" + ChatColor.RESET + ChatColor.DARK_GRAY + " > " + ChatColor.BOLD + "難易度選択" + ChatColor.RESET + ChatColor.DARK_GRAY + " > ";
    public static final String listTitle = ChatColor.DARK_GRAY + "> " + ChatColor.BOLD + "ホーム" + ChatColor.RESET + ChatColor.DARK_GRAY + " > " + ChatColor.BOLD + "マップリスト";

    private static final ItemFlag[] itemFlags = new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS};

    public static Inventory getEditInventory() {
        Inventory inv = Bukkit.createInventory(null, 9 * 6, editTitle);

        WorldConfig worldConfig = Main.getWorldConfig();

        ItemStack itemStack1 = new ItemStack(worldConfig.getMapConfig().getIcon(), 1);
        ItemMeta itemMeta1 = itemStack1.getItemMeta();
        itemMeta1.addEnchant(Enchantment.DURABILITY, 0, true);
        itemMeta1.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemMeta1.setDisplayName(ChatColor.BOLD + "" + ChatColor.ITALIC + WorldManager.getWorld().getName());
        itemMeta1.setLore(
                Arrays.asList(
                        ChatColor.YELLOW + "マップ名: " + ChatColor.GRAY + worldConfig.getMapConfig().getName(),
                        ChatColor.YELLOW + "マップバージョン: " + ChatColor.GRAY + worldConfig.getMapConfig().getVersion(),
                        ChatColor.YELLOW + "マップ製作者: " + ChatColor.GRAY + worldConfig.getMapConfig().getAuthors()

                )
        );
        itemStack1.setItemMeta(itemMeta1);
        inv.setItem(0, itemStack1);

        ItemStack itemStack21 = new ItemStack(Material.PAPER, 1);
        ItemMeta itemMeta21 = itemStack21.getItemMeta();
        itemMeta21.setDisplayName(ChatColor.BOLD + "復活可能回数");
        itemMeta21.setLore(
                Arrays.asList(
                        "" + ChatColor.GOLD + ChatColor.UNDERLINE + "イージー" + ChatColor.RESET + ChatColor.GRAY + ": " + ChatColor.YELLOW + worldConfig.getDifficultyConfig(WorldManager.Difficulty.EASY).getRespawnDenyCount() + ChatColor.GRAY + "回",
                        "" + ChatColor.GOLD + ChatColor.UNDERLINE + "ノーマル" + ChatColor.RESET + ChatColor.GRAY + ": " + ChatColor.YELLOW + worldConfig.getDifficultyConfig(WorldManager.Difficulty.NORMAL).getRespawnDenyCount() + ChatColor.GRAY + "回",
                        "" + ChatColor.GOLD + ChatColor.UNDERLINE + "ハード" + ChatColor.RESET + ChatColor.GRAY + ": " + ChatColor.YELLOW + worldConfig.getDifficultyConfig(WorldManager.Difficulty.HARD).getRespawnDenyCount() + ChatColor.GRAY + "回",
                        "" + ChatColor.GOLD + ChatColor.UNDERLINE + "ハードコア" + ChatColor.RESET + ChatColor.GRAY + ": " + ChatColor.YELLOW + worldConfig.getDifficultyConfig(WorldManager.Difficulty.HARDCORE).getRespawnDenyCount() + ChatColor.GRAY + "回",
                        "",
                        ChatColor.GOLD + "クリックで設定変更"
                )
        );
        itemStack21.setItemMeta(itemMeta21);
        inv.setItem(2, itemStack21);

        ItemStack itemStack31 = new ItemStack(Material.PAPER, 1);
        ItemMeta itemMeta31 = itemStack31.getItemMeta();
        itemMeta31.setDisplayName(ChatColor.BOLD + "復活クールタイム");
        itemMeta31.setLore(
                Arrays.asList(
                        "" + ChatColor.GOLD + ChatColor.UNDERLINE + "イージー" + ChatColor.RESET + ChatColor.GRAY + ": " + ChatColor.YELLOW + TimeFormat.formatJapan(worldConfig.getDifficultyConfig(WorldManager.Difficulty.EASY).getRespawnCoolTime()) + ChatColor.GRAY + "/回",
                        "" + ChatColor.GOLD + ChatColor.UNDERLINE + "ノーマル" + ChatColor.RESET + ChatColor.GRAY + ": " + ChatColor.YELLOW + TimeFormat.formatJapan(worldConfig.getDifficultyConfig(WorldManager.Difficulty.NORMAL).getRespawnCoolTime()) + ChatColor.GRAY + "/回",
                        "" + ChatColor.GOLD + ChatColor.UNDERLINE + "ハード" + ChatColor.RESET + ChatColor.GRAY + ": " + ChatColor.YELLOW + TimeFormat.formatJapan(worldConfig.getDifficultyConfig(WorldManager.Difficulty.HARD).getRespawnCoolTime()) + ChatColor.GRAY + "/回",
                        "" + ChatColor.GOLD + ChatColor.UNDERLINE + "ハードコア" + ChatColor.RESET + ChatColor.GRAY + ": " + ChatColor.YELLOW + TimeFormat.formatJapan(worldConfig.getDifficultyConfig(WorldManager.Difficulty.HARDCORE).getRespawnCoolTime()) + ChatColor.GRAY + "/回",
                        "",
                        ChatColor.GOLD + "クリックで設定変更"
                )
        );
        itemStack31.setItemMeta(itemMeta31);
        inv.setItem(3, itemStack31);

        ItemStack itemStack41 = new ItemStack(Material.PAPER, 1);
        ItemMeta itemMeta41 = itemStack41.getItemMeta();
        itemMeta41.setDisplayName(ChatColor.BOLD + "レート");
        itemMeta41.setLore(
                Arrays.asList(
                        "" + ChatColor.GOLD + ChatColor.UNDERLINE + "イージー" + ChatColor.RESET + ChatColor.GRAY + ": " + ChatColor.YELLOW + worldConfig.getDifficultyConfig(WorldManager.Difficulty.EASY).getRate() + ChatColor.GRAY + "円/秒",
                        "" + ChatColor.GOLD + ChatColor.UNDERLINE + "ノーマル" + ChatColor.RESET + ChatColor.GRAY + ": " + ChatColor.YELLOW + worldConfig.getDifficultyConfig(WorldManager.Difficulty.NORMAL).getRate() + ChatColor.GRAY + "円/秒",
                        "" + ChatColor.GOLD + ChatColor.UNDERLINE + "ハード" + ChatColor.RESET + ChatColor.GRAY + ": " + ChatColor.YELLOW + worldConfig.getDifficultyConfig(WorldManager.Difficulty.HARD).getRate() + ChatColor.GRAY + "円/秒",
                        "" + ChatColor.GOLD + ChatColor.UNDERLINE + "ハードコア" + ChatColor.RESET + ChatColor.GRAY + ": " + ChatColor.YELLOW + worldConfig.getDifficultyConfig(WorldManager.Difficulty.HARDCORE).getRate() + ChatColor.GRAY + "円/秒",
                        "",
                        ChatColor.GOLD + "クリックで設定変更"
                )
        );
        itemStack41.setItemMeta(itemMeta41);
        inv.setItem(5, itemStack41);

        ItemStack itemStack51 = new ItemStack(Material.PAPER, 1);
        ItemMeta itemMeta51 = itemStack51.getItemMeta();
        itemMeta51.setDisplayName(ChatColor.BOLD + "サイコロの最大数");
        itemMeta51.setLore(
                Arrays.asList(
                        "" + ChatColor.GOLD + ChatColor.UNDERLINE + "設定値" + ChatColor.RESET + ChatColor.GRAY + ": " + ChatColor.YELLOW + worldConfig.getOPGameConfig().getDiceCount(),
                        ChatColor.GOLD + "左クリックで設定変更",
                        ChatColor.GOLD + "右クリックでリセット"
                )
        );
        itemStack51.setItemMeta(itemMeta51);
        inv.setItem(6, itemStack51);

        ItemStack itemStack2 = new ItemStack(Material.PAPER, 1);
        ItemMeta itemMeta2 = itemStack2.getItemMeta();
        itemMeta2.setDisplayName(ChatColor.BOLD + "カウントダウン");
        itemMeta2.setLore(
                Arrays.asList(
                        "" + ChatColor.GOLD + ChatColor.UNDERLINE + "設定値" + ChatColor.RESET + ChatColor.GRAY + ": " + ChatColor.YELLOW + TimeFormat.formatJapan(worldConfig.getGameConfig().getCountDown()),
                        ChatColor.GOLD + "左クリックで設定変更",
                        ChatColor.GOLD + "右クリックでリセット"
                )
        );
        itemStack2.setItemMeta(itemMeta2);
        inv.setItem(19, itemStack2);

        ItemStack itemStack3 = new ItemStack(Material.PAPER, 1);
        ItemMeta itemMeta3 = itemStack3.getItemMeta();
        itemMeta3.setDisplayName(ChatColor.BOLD + "ゲーム時間");
        itemMeta3.setLore(
                Arrays.asList(
                        "" + ChatColor.GOLD + ChatColor.UNDERLINE + "設定値" + ChatColor.RESET + ChatColor.GRAY + ": " + ChatColor.YELLOW + TimeFormat.formatJapan(worldConfig.getGameConfig().getGame()),
                        ChatColor.GOLD + "左クリックで設定変更",
                        ChatColor.GOLD + "右クリックでリセット"
                )
        );
        itemStack3.setItemMeta(itemMeta3);
        inv.setItem(20, itemStack3);

        ItemStack itemStack4 = new ItemStack(Material.PAPER, 1);
        ItemMeta itemMeta4 = itemStack4.getItemMeta();
        itemMeta4.setDisplayName(ChatColor.BOLD + "復活禁止");
        itemMeta4.setLore(
                Arrays.asList(
                        "" + ChatColor.GOLD + ChatColor.UNDERLINE + "設定値" + ChatColor.RESET + ChatColor.GRAY + ": " + ChatColor.YELLOW + TimeFormat.formatJapan(worldConfig.getGameConfig().getRespawnDeny()),
                        ChatColor.GOLD + "左クリックで設定変更",
                        ChatColor.GOLD + "右クリックでリセット"
                )
        );
        itemStack4.setItemMeta(itemMeta4);
        inv.setItem(21, itemStack4);

        ItemStack itemStack5 = new ItemStack(worldConfig.getGameConfig().getScript() ? Material.LIME_CONCRETE : Material.RED_CONCRETE, 1);
        ItemMeta itemMeta5 = itemStack5.getItemMeta();
        itemMeta5.setDisplayName(ChatColor.BOLD + "スクリプト");
        itemMeta5.setLore(
                Arrays.asList(
                        "" + ChatColor.GOLD + ChatColor.UNDERLINE + "設定値" + ChatColor.RESET + ChatColor.GRAY + ": " + ChatColor.YELLOW + (worldConfig.getGameConfig().getScript() ? "有効" : "無効"),
                        ChatColor.GOLD + "左クリックで" + (worldConfig.getGameConfig().getScript() ? "無効" : "有効") + "に変更",
                        ChatColor.GOLD + "右クリックでリセット"
                )
        );
        itemStack5.setItemMeta(itemMeta5);
        inv.setItem(23, itemStack5);

        ItemStack itemStack6 = new ItemStack(worldConfig.getGameConfig().getSuccessMission() ? Material.LIME_CONCRETE : Material.RED_CONCRETE, 1);
        ItemMeta itemMeta6 = itemStack6.getItemMeta();
        itemMeta6.setDisplayName(ChatColor.BOLD + "生存ミッション");
        itemMeta6.setLore(
                Arrays.asList(
                        "" + ChatColor.GOLD + ChatColor.UNDERLINE + "設定値" + ChatColor.RESET + ChatColor.GRAY + ": " + ChatColor.YELLOW + (worldConfig.getGameConfig().getSuccessMission() ? "有効" : "無効"),
                        ChatColor.GOLD + "左クリックで" + (worldConfig.getGameConfig().getSuccessMission() ? "無効" : "有効") + "に変更",
                        ChatColor.GOLD + "右クリックでリセット"
                )
        );
        itemStack6.setItemMeta(itemMeta6);
        inv.setItem(24, itemStack6);

        ItemStack itemStack7 = new ItemStack(worldConfig.getGameConfig().getJump() ? Material.LIME_CONCRETE : Material.RED_CONCRETE, 1);
        ItemMeta itemMeta7 = itemStack7.getItemMeta();
        itemMeta7.setDisplayName(ChatColor.BOLD + "ダッシュジャンプ");
        itemMeta7.setLore(
                Arrays.asList(
                        "" + ChatColor.GOLD + ChatColor.UNDERLINE + "設定値" + ChatColor.RESET + ChatColor.GRAY + ": " + ChatColor.YELLOW + (worldConfig.getGameConfig().getJump() ? "有効" : "無効"),
                        ChatColor.GOLD + "左クリックで" + (worldConfig.getGameConfig().getJump() ? "無効" : "有効") + "に変更",
                        ChatColor.GOLD + "右クリックでリセット"
                )
        );
        itemStack7.setItemMeta(itemMeta7);
        inv.setItem(25, itemStack7);


        ItemStack itemStack8 = new ItemStack(Material.BONE, 1);
        ItemMeta itemMeta8 = itemStack8.getItemMeta();
        itemMeta8.setDisplayName(ChatColor.BOLD + "骨 (ゲーム開始時)");
        itemMeta8.setLore(
                Arrays.asList(
                        "" + ChatColor.GOLD + ChatColor.UNDERLINE + "イージー" + ChatColor.RESET + ChatColor.GRAY + ": " + ChatColor.YELLOW + worldConfig.getDifficultyConfig(WorldManager.Difficulty.EASY).getBone(WorldManager.GameType.START).getCount() + ChatColor.GRAY + "個",
                        "" + ChatColor.GOLD + ChatColor.UNDERLINE + "ノーマル" + ChatColor.RESET + ChatColor.GRAY + ": " + ChatColor.YELLOW + worldConfig.getDifficultyConfig(WorldManager.Difficulty.NORMAL).getBone(WorldManager.GameType.START).getCount() + ChatColor.GRAY + "個",
                        "" + ChatColor.GOLD + ChatColor.UNDERLINE + "ハード" + ChatColor.RESET + ChatColor.GRAY + ": " + ChatColor.YELLOW + worldConfig.getDifficultyConfig(WorldManager.Difficulty.HARD).getBone(WorldManager.GameType.START).getCount() + ChatColor.GRAY + "個",
                        "" + ChatColor.GOLD + ChatColor.UNDERLINE + "ハードコア" + ChatColor.RESET + ChatColor.GRAY + ": " + ChatColor.YELLOW + worldConfig.getDifficultyConfig(WorldManager.Difficulty.HARDCORE).getBone(WorldManager.GameType.START).getCount() + ChatColor.GRAY + "個",
                        "",
                        ChatColor.GOLD + "クリックで設定変更"
                )
        );
        itemStack8.setItemMeta(itemMeta8);
        inv.setItem(28, itemStack8);

        ItemStack itemStack9 = new ItemStack(Material.FEATHER, 1);
        ItemMeta itemMeta9 = itemStack9.getItemMeta();
        itemMeta9.setDisplayName(ChatColor.BOLD + "羽 (ゲーム開始時)");
        itemMeta9.setLore(
                Arrays.asList(
                        "" + ChatColor.GOLD + ChatColor.UNDERLINE + "イージー" + ChatColor.RESET + ChatColor.GRAY + ": " + ChatColor.YELLOW + worldConfig.getDifficultyConfig(WorldManager.Difficulty.EASY).getFeather(WorldManager.GameType.START).getCount() + ChatColor.GRAY + "個",
                        "" + ChatColor.GOLD + ChatColor.UNDERLINE + "ノーマル" + ChatColor.RESET + ChatColor.GRAY + ": " + ChatColor.YELLOW + worldConfig.getDifficultyConfig(WorldManager.Difficulty.NORMAL).getFeather(WorldManager.GameType.START).getCount() + ChatColor.GRAY + "個",
                        "" + ChatColor.GOLD + ChatColor.UNDERLINE + "ハード" + ChatColor.RESET + ChatColor.GRAY + ": " + ChatColor.YELLOW + worldConfig.getDifficultyConfig(WorldManager.Difficulty.HARD).getFeather(WorldManager.GameType.START).getCount() + ChatColor.GRAY + "個",
                        "" + ChatColor.GOLD + ChatColor.UNDERLINE + "ハードコア" + ChatColor.RESET + ChatColor.GRAY + ": " + ChatColor.YELLOW + worldConfig.getDifficultyConfig(WorldManager.Difficulty.HARDCORE).getFeather(WorldManager.GameType.START).getCount() + ChatColor.GRAY + "個",
                        "",
                        ChatColor.GOLD + "クリックで設定変更"
                )
        );
        itemStack9.setItemMeta(itemMeta9);
        inv.setItem(29, itemStack9);

        ItemStack itemStack10 = new ItemStack(Material.EGG, 1);
        ItemMeta itemMeta10 = itemStack10.getItemMeta();
        itemMeta10.setDisplayName(ChatColor.BOLD + "卵 (ゲーム開始時)");
        itemMeta10.setLore(
                Arrays.asList(
                        "" + ChatColor.GOLD + ChatColor.UNDERLINE + "イージー" + ChatColor.RESET + ChatColor.GRAY + ": " + ChatColor.YELLOW + worldConfig.getDifficultyConfig(WorldManager.Difficulty.EASY).getEgg(WorldManager.GameType.START).getCount() + ChatColor.GRAY + "個",
                        "" + ChatColor.GOLD + ChatColor.UNDERLINE + "ノーマル" + ChatColor.RESET + ChatColor.GRAY + ": " + ChatColor.YELLOW + worldConfig.getDifficultyConfig(WorldManager.Difficulty.NORMAL).getEgg(WorldManager.GameType.START).getCount() + ChatColor.GRAY + "個",
                        "" + ChatColor.GOLD + ChatColor.UNDERLINE + "ハード" + ChatColor.RESET + ChatColor.GRAY + ": " + ChatColor.YELLOW + worldConfig.getDifficultyConfig(WorldManager.Difficulty.HARD).getEgg(WorldManager.GameType.START).getCount() + ChatColor.GRAY + "個",
                        "" + ChatColor.GOLD + ChatColor.UNDERLINE + "ハードコア" + ChatColor.RESET + ChatColor.GRAY + ": " + ChatColor.YELLOW + worldConfig.getDifficultyConfig(WorldManager.Difficulty.HARDCORE).getEgg(WorldManager.GameType.START).getCount() + ChatColor.GRAY + "個",
                        "",
                        ChatColor.GOLD + "クリックで設定変更"
                )
        );
        itemStack10.setItemMeta(itemMeta10);
        inv.setItem(30, itemStack10);

        ItemStack itemStack11 = new ItemStack(Material.BONE, 1);
        ItemMeta itemMeta11 = itemStack11.getItemMeta();
        itemMeta11.setDisplayName(ChatColor.BOLD + "骨 (復活時)");
        itemMeta11.setLore(
                Arrays.asList(
                        "" + ChatColor.GOLD + ChatColor.UNDERLINE + "イージー" + ChatColor.RESET + ChatColor.GRAY + ": " + ChatColor.YELLOW + worldConfig.getDifficultyConfig(WorldManager.Difficulty.EASY).getBone(WorldManager.GameType.RESPAWN).getCount() + ChatColor.GRAY + "個",
                        "" + ChatColor.GOLD + ChatColor.UNDERLINE + "ノーマル" + ChatColor.RESET + ChatColor.GRAY + ": " + ChatColor.YELLOW + worldConfig.getDifficultyConfig(WorldManager.Difficulty.NORMAL).getBone(WorldManager.GameType.RESPAWN).getCount() + ChatColor.GRAY + "個",
                        "" + ChatColor.GOLD + ChatColor.UNDERLINE + "ハード" + ChatColor.RESET + ChatColor.GRAY + ": " + ChatColor.YELLOW + worldConfig.getDifficultyConfig(WorldManager.Difficulty.HARD).getBone(WorldManager.GameType.RESPAWN).getCount() + ChatColor.GRAY + "個",
                        "" + ChatColor.GOLD + ChatColor.UNDERLINE + "ハードコア" + ChatColor.RESET + ChatColor.GRAY + ": " + ChatColor.YELLOW + worldConfig.getDifficultyConfig(WorldManager.Difficulty.HARDCORE).getBone(WorldManager.GameType.RESPAWN).getCount() + ChatColor.GRAY + "個",
                        "",
                        ChatColor.GOLD + "クリックで設定変更"
                )
        );
        itemStack11.setItemMeta(itemMeta11);
        inv.setItem(32, itemStack11);

        ItemStack itemStack12 = new ItemStack(Material.FEATHER, 1);
        ItemMeta itemMeta12 = itemStack12.getItemMeta();
        itemMeta12.setDisplayName(ChatColor.BOLD + "羽 (復活時)");
        itemMeta12.setLore(
                Arrays.asList(
                        "" + ChatColor.GOLD + ChatColor.UNDERLINE + "イージー" + ChatColor.RESET + ChatColor.GRAY + ": " + ChatColor.YELLOW + worldConfig.getDifficultyConfig(WorldManager.Difficulty.EASY).getFeather(WorldManager.GameType.RESPAWN).getCount() + ChatColor.GRAY + "個",
                        "" + ChatColor.GOLD + ChatColor.UNDERLINE + "ノーマル" + ChatColor.RESET + ChatColor.GRAY + ": " + ChatColor.YELLOW + worldConfig.getDifficultyConfig(WorldManager.Difficulty.NORMAL).getFeather(WorldManager.GameType.RESPAWN).getCount() + ChatColor.GRAY + "個",
                        "" + ChatColor.GOLD + ChatColor.UNDERLINE + "ハード" + ChatColor.RESET + ChatColor.GRAY + ": " + ChatColor.YELLOW + worldConfig.getDifficultyConfig(WorldManager.Difficulty.HARD).getFeather(WorldManager.GameType.RESPAWN).getCount() + ChatColor.GRAY + "個",
                        "" + ChatColor.GOLD + ChatColor.UNDERLINE + "ハードコア" + ChatColor.RESET + ChatColor.GRAY + ": " + ChatColor.YELLOW + worldConfig.getDifficultyConfig(WorldManager.Difficulty.HARDCORE).getFeather(WorldManager.GameType.RESPAWN).getCount() + ChatColor.GRAY + "個",
                        "",
                        ChatColor.GOLD + "クリックで設定変更"
                )
        );
        itemStack12.setItemMeta(itemMeta12);
        inv.setItem(33, itemStack12);

        ItemStack itemStack13 = new ItemStack(Material.EGG, 1);
        ItemMeta itemMeta13 = itemStack13.getItemMeta();
        itemMeta13.setDisplayName(ChatColor.BOLD + "卵 (復活時)");
        itemMeta13.setLore(
                Arrays.asList(
                        "" + ChatColor.GOLD + ChatColor.UNDERLINE + "イージー" + ChatColor.RESET + ChatColor.GRAY + ": " + ChatColor.YELLOW + worldConfig.getDifficultyConfig(WorldManager.Difficulty.EASY).getEgg(WorldManager.GameType.RESPAWN).getCount() + ChatColor.GRAY + "個",
                        "" + ChatColor.GOLD + ChatColor.UNDERLINE + "ノーマル" + ChatColor.RESET + ChatColor.GRAY + ": " + ChatColor.YELLOW + worldConfig.getDifficultyConfig(WorldManager.Difficulty.NORMAL).getEgg(WorldManager.GameType.RESPAWN).getCount() + ChatColor.GRAY + "個",
                        "" + ChatColor.GOLD + ChatColor.UNDERLINE + "ハード" + ChatColor.RESET + ChatColor.GRAY + ": " + ChatColor.YELLOW + worldConfig.getDifficultyConfig(WorldManager.Difficulty.HARD).getEgg(WorldManager.GameType.RESPAWN).getCount() + ChatColor.GRAY + "個",
                        "" + ChatColor.GOLD + ChatColor.UNDERLINE + "ハードコア" + ChatColor.RESET + ChatColor.GRAY + ": " + ChatColor.YELLOW + worldConfig.getDifficultyConfig(WorldManager.Difficulty.HARDCORE).getEgg(WorldManager.GameType.RESPAWN).getCount() + ChatColor.GRAY + "個",
                        "",
                        ChatColor.GOLD + "クリックで設定変更"
                )
        );
        itemStack13.setItemMeta(itemMeta13);
        inv.setItem(34, itemStack13);

        ItemStack itemStack14 = new ItemStack(worldConfig.getConfig().contains(WorldManager.PathType.LOCATION_OPGAME.getPath()) ? Material.LIME_CONCRETE : Material.RED_CONCRETE, 1);
        ItemMeta itemMeta14 = itemStack14.getItemMeta();
        itemMeta14.setDisplayName(ChatColor.BOLD + "オープニングゲーム地点");
        itemMeta14.setLore(
                Arrays.asList(
                        "" + ChatColor.GOLD + ChatColor.UNDERLINE + "設定値" + ChatColor.RESET + ChatColor.GRAY + ": " + ChatColor.YELLOW + (worldConfig.getConfig().contains(WorldManager.PathType.LOCATION_OPGAME.getPath()) ? "設定済み" : "未設定"),
                        ChatColor.GOLD + "左クリックで設定変更",
                        ChatColor.GOLD + "右クリックでテレポート"
                )
        );
        itemStack14.setItemMeta(itemMeta14);
        inv.setItem(37, itemStack14);

        ItemStack itemStack16 = new ItemStack(worldConfig.getConfig().contains(WorldManager.PathType.LOCATION_GOPGAME.getPath() + ".p1") ? Material.LIME_CONCRETE : Material.RED_CONCRETE, 1);
        ItemMeta itemMeta16 = itemStack16.getItemMeta();
        itemMeta16.setDisplayName(ChatColor.BOLD + "オープニングゲーム集合地点");
        itemMeta16.setLore(
                Arrays.asList(
                        "" + ChatColor.GOLD + ChatColor.UNDERLINE + "設定値" + ChatColor.RESET + ChatColor.GRAY + ": " + ChatColor.YELLOW + (worldConfig.getConfig().contains(WorldManager.PathType.LOCATION_GOPGAME.getPath() + ".p1") ? "設定済み" : "未設定"),
                        ChatColor.GOLD + "左クリックで設定変更",
                        ChatColor.GOLD + "右クリックでテレポート"
                )
        );
        itemStack16.setItemMeta(itemMeta16);
        inv.setItem(38, itemStack16);

        ItemStack itemStack15 = new ItemStack(worldConfig.getConfig().contains(WorldManager.PathType.LOCATION_HUNTER.getPath() + ".p1") ? Material.LIME_CONCRETE : Material.RED_CONCRETE, 1);
        ItemMeta itemMeta15 = itemStack15.getItemMeta();
        itemMeta15.setDisplayName(ChatColor.BOLD + "ハンター集合地点");
        itemMeta15.setLore(
                Arrays.asList(
                        "" + ChatColor.GOLD + ChatColor.UNDERLINE + "設定値" + ChatColor.RESET + ChatColor.GRAY + ": " + ChatColor.YELLOW + (worldConfig.getConfig().contains(WorldManager.PathType.LOCATION_HUNTER.getPath() + ".p1") ? "設定済み" : "未設定"),
                        ChatColor.GOLD + "左クリックで設定変更",
                        ChatColor.GOLD + "右クリックでテレポート"
                )
        );
        itemStack15.setItemMeta(itemMeta15);
        inv.setItem(40, itemStack15);

        ItemStack itemStack17 = new ItemStack(worldConfig.getConfig().contains(WorldManager.PathType.DOOR_HUNTER.getPath() + ".p1") ? Material.LIME_CONCRETE : Material.RED_CONCRETE, 1);
        ItemMeta itemMeta17 = itemStack17.getItemMeta();
        itemMeta17.setDisplayName(ChatColor.BOLD + "ドア位置");
        itemMeta17.setLore(
                Arrays.asList(
                        "" + ChatColor.GOLD + ChatColor.UNDERLINE + "設定値" + ChatColor.RESET + ChatColor.GRAY + ": " + ChatColor.YELLOW + (worldConfig.getConfig().contains(WorldManager.PathType.DOOR_HUNTER.getPath() + ".p1") ? "設定済み" : "未設定") + " (ポイント1)",
                        ChatColor.GOLD + "左クリックで設定変更",
                        ChatColor.GOLD + "右クリックでドア開放"
                )
        );
        itemStack17.setItemMeta(itemMeta17);
        inv.setItem(41, itemStack17);

        ItemStack itemStack18 = new ItemStack(worldConfig.getConfig().contains(WorldManager.PathType.LOCATION_JAIL.getPath() + ".p1") ? Material.LIME_CONCRETE : Material.RED_CONCRETE, 1);
        ItemMeta itemMeta18 = itemStack18.getItemMeta();
        itemMeta18.setDisplayName(ChatColor.BOLD + "牢獄地点");
        itemMeta18.setLore(
                Arrays.asList(
                        "" + ChatColor.GOLD + ChatColor.UNDERLINE + "設定値" + ChatColor.RESET + ChatColor.GRAY + ": " + ChatColor.YELLOW + (worldConfig.getConfig().contains(WorldManager.PathType.LOCATION_JAIL.getPath() + ".p1") ? "設定済み" : "未設定") + " (ポイント1)",
                        ChatColor.GOLD + "左クリックで設定変更",
                        ChatColor.GOLD + "右クリックでテレポート"
                )
        );
        itemStack18.setItemMeta(itemMeta18);
        inv.setItem(42, itemStack18);

        ItemStack itemStack19 = new ItemStack(worldConfig.getConfig().contains(WorldManager.PathType.LOCATION_RESPAWN.getPath() + ".p1") ? Material.LIME_CONCRETE : Material.RED_CONCRETE, 1);
        ItemMeta itemMeta19 = itemStack19.getItemMeta();
        itemMeta19.setDisplayName(ChatColor.BOLD + "復活地点");
        itemMeta19.setLore(
                Arrays.asList(
                        "" + ChatColor.GOLD + ChatColor.UNDERLINE + "設定値" + ChatColor.RESET + ChatColor.GRAY + ": " + ChatColor.YELLOW + (worldConfig.getConfig().contains(WorldManager.PathType.LOCATION_RESPAWN.getPath() + ".p1") ? "設定済み" : "未設定") + " (ポイント1)",
                        ChatColor.GOLD + "左クリックで設定変更",
                        ChatColor.GOLD + "右クリックでテレポート"
                )
        );
        itemStack19.setItemMeta(itemMeta19);
        inv.setItem(43, itemStack19);
        return inv;
    }

    public static Inventory getListInventory() {
        Inventory inv = Bukkit.createInventory(null, InventoryType.CHEST, listTitle);

        WorldConfig worldConfig = Main.getWorldConfig();

        if (Bukkit.getWorldContainer().listFiles() != null) {
            for (File file : Bukkit.getWorldContainer().listFiles()) {
                if (file.isDirectory()) {
                    File configFile = new File(file.getName() + Main.FILE_SEPARATOR + "map.yml");
                    if (configFile.exists()) {
                        ItemStack itemStack = new ItemStack(worldConfig.getMapConfig().getIcon(), 1);
                        ItemMeta itemMeta = itemStack.getItemMeta();
                        if (WorldManager.getWorld().getName().equalsIgnoreCase(file.getName())) {
                            itemMeta.addEnchant(Enchantment.DURABILITY, 0, true);
                            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                            itemMeta.setDisplayName("" + ChatColor.BOLD + ChatColor.UNDERLINE + file.getName());
                            itemMeta.setLore(
                                    Arrays.asList(
                                            ChatColor.YELLOW + "マップ名: " + ChatColor.GRAY + worldConfig.getMapConfig().getName(),
                                            ChatColor.YELLOW + "マップバージョン: " + ChatColor.GRAY + worldConfig.getMapConfig().getVersion(),
                                            ChatColor.YELLOW + "マップ製作者: " + ChatColor.GRAY + worldConfig.getMapConfig().getAuthors()
                                    )
                            );
                        } else {
                            itemMeta.setDisplayName(ChatColor.BOLD + file.getName());
                            itemMeta.setLore(
                                    Arrays.asList(
                                            ChatColor.YELLOW + "マップ名: " + ChatColor.GRAY + worldConfig.getMapConfig().getName(),
                                            ChatColor.YELLOW + "マップバージョン: " + ChatColor.GRAY + worldConfig.getMapConfig().getVersion(),
                                            ChatColor.YELLOW + "マップ製作者: " + ChatColor.GRAY + worldConfig.getMapConfig().getAuthors(),
                                            ChatColor.YELLOW + "クリックしてマップを変更"
                                    )
                            );
                        }
                        itemStack.setItemMeta(itemMeta);
                        inv.addItem(itemStack);
                    }
                }
            }
        }
        return inv;
    }

    public static Inventory getDifficultyInventory(ActionType actionType) {
        Inventory inv = Bukkit.createInventory(null, InventoryType.CHEST, difficultyTitle + actionType.getDisplayName());

        ItemStack itemStackBorder = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta itemMetaBorder = itemStackBorder.getItemMeta();
        itemMetaBorder.addItemFlags(itemFlags);
        itemMetaBorder.setDisplayName(ChatColor.BOLD + "");
        itemStackBorder.setItemMeta(itemMetaBorder);

        for (int i = 0; i < 9; i++)
            inv.setItem(i, itemStackBorder);
        for (int i = 18; i < 27; i++)
            inv.setItem(i, itemStackBorder);

        ItemStack itemStackDifficultyEasy = new ItemStack(MainInventory.Item.DIFFICULTY_EASY.getMaterial(), 1);
        ItemMeta itemMetaDifficultyEasy = itemStackDifficultyEasy.getItemMeta();
        itemMetaDifficultyEasy.addItemFlags(itemFlags);
        itemMetaDifficultyEasy.setDisplayName("" + ChatColor.BOLD + ChatColor.UNDERLINE + WorldManager.Difficulty.EASY.getDisplayName());
        itemMetaDifficultyEasy.setLore(Arrays.asList());
        itemStackDifficultyEasy.setItemMeta(itemMetaDifficultyEasy);
        inv.setItem(10, itemStackDifficultyEasy);

        ItemStack itemStackDifficultyNormal = new ItemStack(MainInventory.Item.DIFFICULTY_NORMAL.getMaterial(), 1);
        ItemMeta itemMetaDifficultyNormal = itemStackDifficultyNormal.getItemMeta();
        itemMetaDifficultyNormal.addItemFlags(itemFlags);
        itemMetaDifficultyNormal.setDisplayName("" + ChatColor.BOLD + ChatColor.UNDERLINE + WorldManager.Difficulty.NORMAL.getDisplayName());
        itemMetaDifficultyNormal.setLore(Arrays.asList());
        itemStackDifficultyNormal.setItemMeta(itemMetaDifficultyNormal);
        inv.setItem(11, itemStackDifficultyNormal);

        ItemStack itemStackDifficultyHard = new ItemStack(MainInventory.Item.DIFFICULTY_HARD.getMaterial(), 1);
        ItemMeta itemMetaDifficultyHard = itemStackDifficultyHard.getItemMeta();
        itemMetaDifficultyHard.addItemFlags(itemFlags);
        itemMetaDifficultyHard.setDisplayName("" + ChatColor.BOLD + ChatColor.UNDERLINE + WorldManager.Difficulty.HARD.getDisplayName());
        itemMetaDifficultyHard.setLore(Arrays.asList());
        itemStackDifficultyHard.setItemMeta(itemMetaDifficultyHard);
        inv.setItem(12, itemStackDifficultyHard);

        ItemStack itemStackDifficultyHardCore = new ItemStack(MainInventory.Item.DIFFICULTY_HARDCORE.getMaterial(), 1);
        ItemMeta itemMetaDifficultyHardCore = itemStackDifficultyHardCore.getItemMeta();
        itemMetaDifficultyHardCore.addItemFlags(itemFlags);
        itemMetaDifficultyHardCore.setDisplayName("" + ChatColor.BOLD + ChatColor.UNDERLINE + WorldManager.Difficulty.HARDCORE.getDisplayName());
        itemMetaDifficultyHardCore.setLore(Arrays.asList());
        itemStackDifficultyHardCore.setItemMeta(itemMetaDifficultyHardCore);
        inv.setItem(13, itemStackDifficultyHardCore);


        ItemStack itemStackCancel = new ItemStack(Material.BARRIER, 1);
        ItemMeta itemMetaCancel = itemStackCancel.getItemMeta();
        itemMetaCancel.addItemFlags(itemFlags);
        itemMetaCancel.setDisplayName("" + ChatColor.RED + ChatColor.BOLD + ChatColor.UNDERLINE + "キャンセル");
        itemMetaCancel.setLore(Arrays.asList());
        itemStackCancel.setItemMeta(itemMetaCancel);
        inv.setItem(16, itemStackCancel);

        return inv;
    }

    public enum ActionType {
        RATE("レート"),
        RESPAWN_COUNT("復活可能回数"),
        RESPAWN_COOLTIME("復活クールタイム"),
        ITEM_START_BONE("骨 (ゲーム開始時)"),
        ITEM_START_FEATHER("羽 (ゲーム開始時)"),
        ITEM_START_EGG("卵 (ゲーム開始時)"),
        ITEM_RESPAWN_BONE("骨 (復活時)"),
        ITEM_RESPAWN_FEATHER("羽 (復活時)"),
        ITEM_RESPAWN_EGG("卵 (復活時)");

        private final String displayName;

        private ActionType(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }

        public static ActionType getActionType(String name) {
            return valueOf(name.toUpperCase());
        }
    }
}

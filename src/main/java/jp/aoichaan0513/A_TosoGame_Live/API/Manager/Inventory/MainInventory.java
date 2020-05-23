package jp.aoichaan0513.A_TosoGame_Live.API.Manager.Inventory;

import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI;
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.GameManager;
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.MoneyManager;
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.Player.PlayerManager;
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.World.WorldConfig;
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.World.WorldManager;
import jp.aoichaan0513.A_TosoGame_Live.API.Scoreboard.Teams;
import jp.aoichaan0513.A_TosoGame_Live.API.TosoGameAPI;
import jp.aoichaan0513.A_TosoGame_Live.Commands.Command.Hunter;
import jp.aoichaan0513.A_TosoGame_Live.Commands.Command.Tuho;
import jp.aoichaan0513.A_TosoGame_Live.Main;
import jp.aoichaan0513.A_TosoGame_Live.Utils.DateTime.TimeFormat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.Collections;

public class MainInventory {

    public static final String title = ChatColor.DARK_GRAY + "> " + ChatColor.BOLD + "ホーム";

    private static final ItemFlag[] itemFlags = new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS};

    public static Inventory getInventory(Player p) {
        Inventory inv = Bukkit.createInventory(null, 9 * 6, title);

        WorldConfig worldConfig = Main.getWorldConfig();

        ItemStack itemStack1 = new ItemStack(Item.PLAYER_INFO.getMaterial(), 1, (short) SkullType.PLAYER.ordinal());
        SkullMeta itemMeta1 = (SkullMeta) itemStack1.getItemMeta();
        itemMeta1.addItemFlags(itemFlags);
        itemMeta1.setOwningPlayer(p);
        itemMeta1.setDisplayName("" + ChatColor.BLUE + ChatColor.BOLD + ChatColor.UNDERLINE + "あなたの情報");
        itemMeta1.setLore(
                Arrays.asList(
                        "" + ChatColor.BLUE + ChatColor.UNDERLINE + "プレイヤー名" + ChatColor.RESET + ChatColor.GRAY + ": " + ChatColor.YELLOW + p.getName(),
                        "" + ChatColor.BLUE + ChatColor.UNDERLINE + "権限所持者" + ChatColor.RESET + ChatColor.GRAY + ": " + ChatColor.YELLOW + (TosoGameAPI.hasPermission(p) ? "はい" : "いいえ"),
                        "" + ChatColor.BLUE + ChatColor.UNDERLINE + "配信者" + ChatColor.RESET + ChatColor.GRAY + ": " + ChatColor.YELLOW + (TosoGameAPI.isBroadCaster(p) ? "はい" : "いいえ"),
                        "",
                        "" + ChatColor.BLUE + ChatColor.UNDERLINE + "所持金" + ChatColor.RESET + ChatColor.GRAY + ": " + ChatColor.YELLOW + MoneyManager.formatMoney(PlayerManager.loadConfig(p).getMoney()) + ChatColor.GRAY + "円",
                        "",
                        "" + ChatColor.BLUE + ChatColor.UNDERLINE + "チーム" + ChatColor.RESET + ChatColor.GRAY + ": " + ChatColor.YELLOW + Teams.getTeam(Teams.DisplaySlot.SIDEBAR, p),
                        "" + ChatColor.BLUE + ChatColor.UNDERLINE + "難易度" + ChatColor.RESET + ChatColor.GRAY + ": " + ChatColor.YELLOW + TosoGameAPI.difficultyMap.get(p.getUniqueId()).getDisplayName(),
                        "" + ChatColor.BLUE + ChatColor.UNDERLINE + "賞金" + ChatColor.RESET + ChatColor.GRAY + ": " + ChatColor.YELLOW + MoneyManager.formatMoney(MoneyManager.getReward(p)) + ChatColor.GRAY + "円 (" + MoneyManager.getRate(p) + "円/秒)"
                )
        );
        itemStack1.setItemMeta(itemMeta1);
        inv.setItem(Item.PLAYER_INFO.getIndex(), itemStack1);

        if (TosoGameAPI.isAdmin(p)) {
            ItemStack itemStack2 = new ItemStack(Item.SETTINGS.getMaterial(), 1);
            ItemMeta itemMeta2 = itemStack2.getItemMeta();
            itemMeta2.addItemFlags(itemFlags);
            itemMeta2.setDisplayName("" + ChatColor.BLUE + ChatColor.BOLD + ChatColor.UNDERLINE + "マップ設定");
            itemMeta2.setLore(Collections.singletonList(ChatColor.GRAY + "クリックして" + ChatColor.BLUE + ChatColor.BOLD + ChatColor.UNDERLINE + "マップ設定アプリ" + ChatColor.RESET + ChatColor.GRAY + "を開きます。"));
            itemStack2.setItemMeta(itemMeta2);
            inv.setItem(Item.SETTINGS.getIndex(), itemStack2);
        }

        ItemStack itemStack3 = new ItemStack(Item.NOTIFICATION.getMaterial());
        ItemMeta itemMeta3 = itemStack3.getItemMeta();
        itemMeta3.addItemFlags(itemFlags);
        itemMeta3.setDisplayName("" + ChatColor.BLUE + ChatColor.BOLD + ChatColor.UNDERLINE + "通知");
        itemMeta3.setLore(Collections.singletonList(ChatColor.GRAY + "クリックして" + ChatColor.BLUE + ChatColor.BOLD + ChatColor.UNDERLINE + "通知メニュー" + ChatColor.RESET + ChatColor.GRAY + "を開きます。"));
        itemStack3.setItemMeta(itemMeta3);
        inv.setItem(Item.NOTIFICATION.getIndex(), itemStack3);

        ItemStack itemStack4 = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta itemMeta4 = itemStack4.getItemMeta();
        itemMeta4.addItemFlags(itemFlags);
        itemMeta4.setDisplayName(ChatColor.BOLD + "");
        itemStack4.setItemMeta(itemMeta4);

        for (int i = 9; i < 18; i++)
            inv.setItem(i, itemStack4);
        for (int i = 45; i < 54; i++)
            inv.setItem(i, itemStack4);

        // マップアプリ
        ItemStack itemStack5 = new ItemStack(Item.MAP_APP.getMaterial(), 1);
        ItemMeta itemMeta5 = itemStack5.getItemMeta();
        itemMeta5.addItemFlags(itemFlags);
        itemMeta5.setDisplayName("" + ChatColor.GOLD + ChatColor.BOLD + ChatColor.UNDERLINE + "マップ");
        itemMeta5.setLore(Collections.singletonList(ChatColor.YELLOW + "クリックして" + ChatColor.GOLD + ChatColor.BOLD + ChatColor.UNDERLINE + "マップアプリ" + ChatColor.RESET + ChatColor.YELLOW + "を開きます。"));
        itemStack5.setItemMeta(itemMeta5);
        inv.setItem(Item.MAP_APP.getIndex(), itemStack5);

        // ミッションアプリ
        ItemStack itemStack6 = new ItemStack(Item.MISSION_APP.getMaterial(), 1);
        ItemMeta itemMeta6 = itemStack6.getItemMeta();
        itemMeta6.addItemFlags(itemFlags);
        itemMeta6.setDisplayName("" + ChatColor.GOLD + ChatColor.BOLD + ChatColor.UNDERLINE + "ミッション");
        itemMeta6.setLore(Collections.singletonList(ChatColor.YELLOW + "クリックして" + ChatColor.GOLD + ChatColor.BOLD + ChatColor.UNDERLINE + "ミッションアプリ" + ChatColor.RESET + ChatColor.YELLOW + "を開きます。"));
        itemStack6.setItemMeta(itemMeta6);
        inv.setItem(Item.MISSION_APP.getIndex(), itemStack6);

        // 観戦モード切り替え
        ItemStack itemStack7 = new ItemStack(Item.SPEC_MODE.getMaterial(), 1);
        PotionMeta itemMeta7 = (PotionMeta) itemStack7.getItemMeta();
        itemMeta7.addItemFlags(itemFlags);

        if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_JAIL, p))
            itemMeta7.addCustomEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 200, 1), true);
        itemMeta7.setDisplayName("" + ChatColor.GOLD + ChatColor.BOLD + ChatColor.UNDERLINE + "観戦モード切り替え");
        itemMeta7.setLore(
                Arrays.asList(
                        ChatColor.YELLOW + "クリックして" + ChatColor.GOLD + ChatColor.BOLD + ChatColor.UNDERLINE + "観戦モード" + ChatColor.RESET + ChatColor.YELLOW + "を切り替えます。",
                        ChatColor.GRAY + "この機能は" + ChatColor.BOLD + ChatColor.UNDERLINE + TimeFormat.formatJapan(worldConfig.getGameConfig().getRespawnDeny()) + "以下" + ChatColor.RESET + ChatColor.GRAY + "・" + ChatColor.BOLD + ChatColor.UNDERLINE + "牢獄" + ChatColor.RESET + ChatColor.GRAY + "にいる場合のみ使用可能です。"
                )
        );
        itemStack7.setItemMeta(itemMeta7);
        inv.setItem(Item.SPEC_MODE.getIndex(), itemStack7);

        // ハンター抽選
        ItemStack itemStack8 = new ItemStack(Hunter.num > 0 ? Material.DIAMOND_SWORD : Material.STONE_SWORD, 1);
        ItemMeta itemMeta8 = itemStack8.getItemMeta();
        itemMeta8.addItemFlags(itemFlags);
        if (Hunter.num > 0 && Main.hunterShuffleSet.contains(p.getUniqueId()))
            itemMeta8.addEnchant(Enchantment.DURABILITY, 1, true);
        itemMeta8.setDisplayName("" + ChatColor.AQUA + ChatColor.BOLD + ChatColor.UNDERLINE + "ハンター抽選に応募");
        itemMeta8.setLore(
                Arrays.asList(
                        ChatColor.YELLOW + "クリックして" + ChatColor.AQUA + ChatColor.BOLD + ChatColor.UNDERLINE + "ハンター抽選" + ChatColor.RESET + ChatColor.YELLOW + "を切り替えます。",
                        ChatColor.GRAY + "この機能は" + ChatColor.BOLD + ChatColor.UNDERLINE + "ハンター抽選が実行" + ChatColor.RESET + ChatColor.GRAY + "されている場合のみ使用可能です。",
                        "",
                        "" + ChatColor.UNDERLINE + (Hunter.num > 0 ? (Main.hunterShuffleSet.contains(p.getUniqueId()) ? ChatColor.GOLD + "抽選に応募しています。" : ChatColor.YELLOW + "抽選に応募していません。") : ChatColor.RED + "現在使用できません。")
                )
        );
        itemStack8.setItemMeta(itemMeta8);
        inv.setItem(Item.REQUEST_HUNTER.getIndex(), itemStack8);

        // 通報部隊抽選
        ItemStack itemStack9 = new ItemStack(Tuho.num > 0 ? Material.GOLDEN_SWORD : Material.STONE_SWORD, 1);
        ItemMeta itemMeta9 = itemStack9.getItemMeta();
        itemMeta9.addItemFlags(itemFlags);
        if (Tuho.num > 0 && Main.tuhoShuffleSet.contains(p.getUniqueId()))
            itemMeta9.addEnchant(Enchantment.DURABILITY, 1, true);
        itemMeta9.setDisplayName("" + ChatColor.AQUA + ChatColor.BOLD + ChatColor.UNDERLINE + "通報部隊抽選に応募");
        itemMeta9.setLore(
                Arrays.asList(
                        ChatColor.YELLOW + "クリックして" + ChatColor.AQUA + ChatColor.BOLD + ChatColor.UNDERLINE + "通報部隊抽選" + ChatColor.RESET + ChatColor.YELLOW + "を切り替えます。",
                        ChatColor.GRAY + "この機能は" + ChatColor.BOLD + ChatColor.UNDERLINE + "通報部隊抽選が実行" + ChatColor.RESET + ChatColor.GRAY + "されている場合のみ使用可能です。",
                        "",
                        "" + ChatColor.UNDERLINE + (Tuho.num > 0 ? (Main.tuhoShuffleSet.contains(p.getUniqueId()) ? ChatColor.GOLD + "抽選に応募しています。" : ChatColor.YELLOW + "抽選に応募していません。") : ChatColor.RED + "現在使用できません。")
                )
        );
        itemStack9.setItemMeta(itemMeta9);
        inv.setItem(Item.REQUEST_TUHO.getIndex(), itemStack9);

        WorldConfig.DifficultyConfig difficultyEasyConfig = worldConfig.getDifficultyConfig(WorldManager.Difficulty.EASY);
        ItemStack itemStackDifficultyEasy = new ItemStack(GameManager.isGame() ? (TosoGameAPI.difficultyMap.containsKey(p.getUniqueId()) && TosoGameAPI.difficultyMap.get(p.getUniqueId()) == WorldManager.Difficulty.EASY ? Item.DIFFICULTY_EASY.getMaterial() : Material.GRAY_CONCRETE) : Item.DIFFICULTY_EASY.getMaterial(), 1);
        ItemMeta itemMetaDifficultyEasy = itemStackDifficultyEasy.getItemMeta();
        itemMetaDifficultyEasy.addItemFlags(itemFlags);
        if (TosoGameAPI.difficultyMap.containsKey(p.getUniqueId()) && TosoGameAPI.difficultyMap.get(p.getUniqueId()) == WorldManager.Difficulty.EASY)
            itemMetaDifficultyEasy.addEnchant(Enchantment.DURABILITY, 1, true);
        itemMetaDifficultyEasy.setDisplayName("" + ChatColor.GREEN + ChatColor.BOLD + ChatColor.UNDERLINE + ChatColor.stripColor(WorldManager.Difficulty.EASY.getDisplayName()));
        itemMetaDifficultyEasy.setLore(
                Arrays.asList(
                        ChatColor.YELLOW + "クリックして難易度を" + "" + ChatColor.GREEN + ChatColor.BOLD + ChatColor.UNDERLINE + ChatColor.stripColor(WorldManager.Difficulty.EASY.getDisplayName()) + ChatColor.RESET + ChatColor.YELLOW + "に切り替えます。",
                        ChatColor.GRAY + "この機能は" + ChatColor.BOLD + ChatColor.UNDERLINE + "ゲーム準備中" + ChatColor.RESET + ChatColor.GRAY + "のみ使用可能です。",
                        "",
                        "" + ChatColor.BLUE + ChatColor.UNDERLINE + "レート" + ChatColor.RESET + ChatColor.GRAY + ": " + ChatColor.YELLOW + difficultyEasyConfig.getRate() + ChatColor.GRAY + "円/秒",
                        "",
                        "" + ChatColor.BLUE + ChatColor.UNDERLINE + "体力システム" + ChatColor.RESET + ChatColor.GRAY + ": " + ChatColor.YELLOW + (difficultyEasyConfig.getHealth() ? "有効" : "無効"),
                        "" + ChatColor.BLUE + ChatColor.UNDERLINE + "自動復活" + ChatColor.RESET + ChatColor.GRAY + ": " + ChatColor.YELLOW + (difficultyEasyConfig.getRespawnAutoTime() > -1 ? TimeFormat.formatJapan(difficultyEasyConfig.getRespawnAutoTime()) : "無効"),
                        "" + ChatColor.BLUE + ChatColor.UNDERLINE + "復活可能回数" + ChatColor.RESET + ChatColor.GRAY + ": " + ChatColor.YELLOW + difficultyEasyConfig.getRespawnDenyCount() + ChatColor.GRAY + "回",
                        "" + ChatColor.BLUE + ChatColor.UNDERLINE + "復活クールタイム" + ChatColor.RESET + ChatColor.GRAY + ": " + ChatColor.YELLOW + TimeFormat.formatJapan(difficultyEasyConfig.getRespawnCoolTime()) + ChatColor.GRAY + "/回",
                        "",
                        "" + ChatColor.BLUE + ChatColor.UNDERLINE + "ゲーム開始時のアイテム",
                        "" + ChatColor.BLUE + "骨 (透明化)" + ChatColor.RESET + ChatColor.GRAY + ": " + ChatColor.YELLOW + difficultyEasyConfig.getBone(WorldManager.GameType.START).getCount() + ChatColor.GRAY + "個 (" + difficultyEasyConfig.getBone(WorldManager.GameType.START).getDuration() + "秒)",
                        "" + ChatColor.BLUE + "羽 (移動速度上昇)" + ChatColor.RESET + ChatColor.GRAY + ": " + ChatColor.YELLOW + difficultyEasyConfig.getFeather(WorldManager.GameType.START).getCount() + ChatColor.GRAY + "個 (" + difficultyEasyConfig.getFeather(WorldManager.GameType.START).getDuration() + "秒)",
                        "" + ChatColor.BLUE + "卵 (盲目・移動速度低下)" + ChatColor.RESET + ChatColor.GRAY + ": " + ChatColor.YELLOW + difficultyEasyConfig.getEgg(WorldManager.GameType.START).getCount() + ChatColor.GRAY + "個",
                        "",
                        "" + ChatColor.BLUE + ChatColor.UNDERLINE + "復活時のアイテム",
                        "" + ChatColor.BLUE + "骨 (透明化)" + ChatColor.RESET + ChatColor.GRAY + ": " + ChatColor.YELLOW + difficultyEasyConfig.getBone(WorldManager.GameType.RESPAWN).getCount() + ChatColor.GRAY + "個 (" + difficultyEasyConfig.getBone(WorldManager.GameType.RESPAWN).getDuration() + "秒)",
                        "" + ChatColor.BLUE + "羽 (移動速度上昇)" + ChatColor.RESET + ChatColor.GRAY + ": " + ChatColor.YELLOW + difficultyEasyConfig.getFeather(WorldManager.GameType.RESPAWN).getCount() + ChatColor.GRAY + "個 (" + difficultyEasyConfig.getFeather(WorldManager.GameType.RESPAWN).getDuration() + "秒)",
                        "" + ChatColor.BLUE + "卵 (盲目・移動速度低下)" + ChatColor.RESET + ChatColor.GRAY + ": " + ChatColor.YELLOW + difficultyEasyConfig.getEgg(WorldManager.GameType.RESPAWN).getCount() + ChatColor.GRAY + "個"
                )
        );
        itemStackDifficultyEasy.setItemMeta(itemMetaDifficultyEasy);
        inv.setItem(Item.DIFFICULTY_EASY.getIndex(), itemStackDifficultyEasy);


        WorldConfig.DifficultyConfig difficultyNormalConfig = worldConfig.getDifficultyConfig(WorldManager.Difficulty.NORMAL);
        ItemStack itemStackDifficultyNormal = new ItemStack(GameManager.isGame() ? (TosoGameAPI.difficultyMap.containsKey(p.getUniqueId()) && TosoGameAPI.difficultyMap.get(p.getUniqueId()) == WorldManager.Difficulty.NORMAL ? Item.DIFFICULTY_NORMAL.getMaterial() : Material.GRAY_CONCRETE) : Item.DIFFICULTY_NORMAL.getMaterial(), 1);
        ItemMeta itemMetaDifficultyNormal = itemStackDifficultyNormal.getItemMeta();
        itemMetaDifficultyNormal.addItemFlags(itemFlags);
        if (TosoGameAPI.difficultyMap.containsKey(p.getUniqueId()) && TosoGameAPI.difficultyMap.get(p.getUniqueId()) == WorldManager.Difficulty.NORMAL)
            itemMetaDifficultyNormal.addEnchant(Enchantment.DURABILITY, 1, true);
        itemMetaDifficultyNormal.setDisplayName("" + ChatColor.YELLOW + ChatColor.BOLD + ChatColor.UNDERLINE + ChatColor.stripColor(WorldManager.Difficulty.NORMAL.getDisplayName()));
        itemMetaDifficultyNormal.setLore(
                Arrays.asList(
                        ChatColor.YELLOW + "クリックして難易度を" + "" + ChatColor.YELLOW + ChatColor.BOLD + ChatColor.UNDERLINE + ChatColor.stripColor(WorldManager.Difficulty.NORMAL.getDisplayName()) + ChatColor.RESET + ChatColor.YELLOW + "に切り替えます。",
                        ChatColor.GRAY + "この機能は" + ChatColor.BOLD + ChatColor.UNDERLINE + "ゲーム準備中" + ChatColor.RESET + ChatColor.GRAY + "のみ使用可能です。",
                        "",
                        "" + ChatColor.BLUE + ChatColor.UNDERLINE + "レート" + ChatColor.RESET + ChatColor.GRAY + ": " + ChatColor.YELLOW + difficultyNormalConfig.getRate() + ChatColor.GRAY + "円/秒",
                        "",
                        "" + ChatColor.BLUE + ChatColor.UNDERLINE + "体力システム" + ChatColor.RESET + ChatColor.GRAY + ": " + ChatColor.YELLOW + (difficultyNormalConfig.getHealth() ? "有効" : "無効"),
                        "" + ChatColor.BLUE + ChatColor.UNDERLINE + "自動復活" + ChatColor.RESET + ChatColor.GRAY + ": " + ChatColor.YELLOW + (difficultyNormalConfig.getRespawnAutoTime() > -1 ? TimeFormat.formatJapan(difficultyNormalConfig.getRespawnAutoTime()) : "無効"),
                        "" + ChatColor.BLUE + ChatColor.UNDERLINE + "復活可能回数" + ChatColor.RESET + ChatColor.GRAY + ": " + ChatColor.YELLOW + difficultyNormalConfig.getRespawnDenyCount() + ChatColor.GRAY + "回",
                        "" + ChatColor.BLUE + ChatColor.UNDERLINE + "復活クールタイム" + ChatColor.RESET + ChatColor.GRAY + ": " + ChatColor.YELLOW + TimeFormat.formatJapan(difficultyNormalConfig.getRespawnCoolTime()) + ChatColor.GRAY + "/回",
                        "",
                        "" + ChatColor.BLUE + ChatColor.UNDERLINE + "ゲーム開始時のアイテム",
                        "" + ChatColor.BLUE + "骨 (透明化)" + ChatColor.RESET + ChatColor.GRAY + ": " + ChatColor.YELLOW + difficultyNormalConfig.getBone(WorldManager.GameType.START).getCount() + ChatColor.GRAY + "個 (" + difficultyNormalConfig.getBone(WorldManager.GameType.START).getDuration() + "秒)",
                        "" + ChatColor.BLUE + "羽 (移動速度上昇)" + ChatColor.RESET + ChatColor.GRAY + ": " + ChatColor.YELLOW + difficultyNormalConfig.getFeather(WorldManager.GameType.START).getCount() + ChatColor.GRAY + "個 (" + difficultyNormalConfig.getFeather(WorldManager.GameType.START).getDuration() + "秒)",
                        "" + ChatColor.BLUE + "卵 (盲目・移動速度低下)" + ChatColor.RESET + ChatColor.GRAY + ": " + ChatColor.YELLOW + difficultyNormalConfig.getEgg(WorldManager.GameType.START).getCount() + ChatColor.GRAY + "個",
                        "",
                        "" + ChatColor.BLUE + ChatColor.UNDERLINE + "復活時のアイテム",
                        "" + ChatColor.BLUE + "骨 (透明化)" + ChatColor.RESET + ChatColor.GRAY + ": " + ChatColor.YELLOW + difficultyNormalConfig.getBone(WorldManager.GameType.RESPAWN).getCount() + ChatColor.GRAY + "個 (" + difficultyNormalConfig.getBone(WorldManager.GameType.RESPAWN).getDuration() + "秒)",
                        "" + ChatColor.BLUE + "羽 (移動速度上昇)" + ChatColor.RESET + ChatColor.GRAY + ": " + ChatColor.YELLOW + difficultyNormalConfig.getFeather(WorldManager.GameType.RESPAWN).getCount() + ChatColor.GRAY + "個 (" + difficultyNormalConfig.getFeather(WorldManager.GameType.RESPAWN).getDuration() + "秒)",
                        "" + ChatColor.BLUE + "卵 (盲目・移動速度低下)" + ChatColor.RESET + ChatColor.GRAY + ": " + ChatColor.YELLOW + difficultyNormalConfig.getEgg(WorldManager.GameType.RESPAWN).getCount() + ChatColor.GRAY + "個"
                )
        );
        itemStackDifficultyNormal.setItemMeta(itemMetaDifficultyNormal);
        inv.setItem(Item.DIFFICULTY_NORMAL.getIndex(), itemStackDifficultyNormal);


        WorldConfig.DifficultyConfig difficultyHardConfig = worldConfig.getDifficultyConfig(WorldManager.Difficulty.HARD);
        ItemStack itemStackDifficultyHard = new ItemStack(GameManager.isGame() ? (TosoGameAPI.difficultyMap.containsKey(p.getUniqueId()) && TosoGameAPI.difficultyMap.get(p.getUniqueId()) == WorldManager.Difficulty.HARD ? Item.DIFFICULTY_HARD.getMaterial() : Material.GRAY_CONCRETE) : Item.DIFFICULTY_HARD.getMaterial(), 1);
        ItemMeta itemMetaDifficultyHard = itemStackDifficultyHard.getItemMeta();
        itemMetaDifficultyHard.addItemFlags(itemFlags);
        if (TosoGameAPI.difficultyMap.containsKey(p.getUniqueId()) && TosoGameAPI.difficultyMap.get(p.getUniqueId()) == WorldManager.Difficulty.HARD)
            itemMetaDifficultyHard.addEnchant(Enchantment.DURABILITY, 1, true);
        itemMetaDifficultyHard.setDisplayName("" + ChatColor.RED + ChatColor.BOLD + ChatColor.UNDERLINE + ChatColor.stripColor(WorldManager.Difficulty.HARD.getDisplayName()));
        itemMetaDifficultyHard.setLore(
                Arrays.asList(
                        ChatColor.YELLOW + "クリックして難易度を" + "" + ChatColor.RED + ChatColor.BOLD + ChatColor.UNDERLINE + ChatColor.stripColor(WorldManager.Difficulty.HARD.getDisplayName()) + ChatColor.RESET + ChatColor.YELLOW + "に切り替えます。",
                        ChatColor.GRAY + "この機能は" + ChatColor.BOLD + ChatColor.UNDERLINE + "ゲーム準備中" + ChatColor.RESET + ChatColor.GRAY + "のみ使用可能です。",
                        "",
                        "" + ChatColor.BLUE + ChatColor.UNDERLINE + "レート" + ChatColor.RESET + ChatColor.GRAY + ": " + ChatColor.YELLOW + difficultyHardConfig.getRate() + ChatColor.GRAY + "円/秒",
                        "",
                        "" + ChatColor.BLUE + ChatColor.UNDERLINE + "体力システム" + ChatColor.RESET + ChatColor.GRAY + ": " + ChatColor.YELLOW + (difficultyHardConfig.getHealth() ? "有効" : "無効"),
                        "" + ChatColor.BLUE + ChatColor.UNDERLINE + "自動復活" + ChatColor.RESET + ChatColor.GRAY + ": " + ChatColor.YELLOW + (difficultyHardConfig.getRespawnAutoTime() > -1 ? TimeFormat.formatJapan(difficultyHardConfig.getRespawnAutoTime()) : "無効"),
                        "" + ChatColor.BLUE + ChatColor.UNDERLINE + "復活可能回数" + ChatColor.RESET + ChatColor.GRAY + ": " + ChatColor.YELLOW + difficultyHardConfig.getRespawnDenyCount() + ChatColor.GRAY + "回",
                        "" + ChatColor.BLUE + ChatColor.UNDERLINE + "復活クールタイム" + ChatColor.RESET + ChatColor.GRAY + ": " + ChatColor.YELLOW + TimeFormat.formatJapan(difficultyHardConfig.getRespawnCoolTime()) + ChatColor.GRAY + "/回",
                        "",
                        "" + ChatColor.BLUE + ChatColor.UNDERLINE + "ゲーム開始時のアイテム",
                        "" + ChatColor.BLUE + "骨 (透明化)" + ChatColor.RESET + ChatColor.GRAY + ": " + ChatColor.YELLOW + difficultyHardConfig.getBone(WorldManager.GameType.START).getCount() + ChatColor.GRAY + "個 (" + difficultyHardConfig.getBone(WorldManager.GameType.START).getDuration() + "秒)",
                        "" + ChatColor.BLUE + "羽 (移動速度上昇)" + ChatColor.RESET + ChatColor.GRAY + ": " + ChatColor.YELLOW + difficultyHardConfig.getFeather(WorldManager.GameType.START).getCount() + ChatColor.GRAY + "個 (" + difficultyHardConfig.getFeather(WorldManager.GameType.START).getDuration() + "秒)",
                        "" + ChatColor.BLUE + "卵 (盲目・移動速度低下)" + ChatColor.RESET + ChatColor.GRAY + ": " + ChatColor.YELLOW + difficultyHardConfig.getEgg(WorldManager.GameType.START).getCount() + ChatColor.GRAY + "個",
                        "",
                        "" + ChatColor.BLUE + ChatColor.UNDERLINE + "復活時のアイテム",
                        "" + ChatColor.BLUE + "骨 (透明化)" + ChatColor.RESET + ChatColor.GRAY + ": " + ChatColor.YELLOW + difficultyHardConfig.getBone(WorldManager.GameType.RESPAWN).getCount() + ChatColor.GRAY + "個 (" + difficultyHardConfig.getBone(WorldManager.GameType.RESPAWN).getDuration() + "秒)",
                        "" + ChatColor.BLUE + "羽 (移動速度上昇)" + ChatColor.RESET + ChatColor.GRAY + ": " + ChatColor.YELLOW + difficultyHardConfig.getFeather(WorldManager.GameType.RESPAWN).getCount() + ChatColor.GRAY + "個 (" + difficultyHardConfig.getFeather(WorldManager.GameType.RESPAWN).getDuration() + "秒)",
                        "" + ChatColor.BLUE + "卵 (盲目・移動速度低下)" + ChatColor.RESET + ChatColor.GRAY + ": " + ChatColor.YELLOW + difficultyHardConfig.getEgg(WorldManager.GameType.RESPAWN).getCount() + ChatColor.GRAY + "個"
                )
        );
        itemStackDifficultyHard.setItemMeta(itemMetaDifficultyHard);
        inv.setItem(Item.DIFFICULTY_HARD.getIndex(), itemStackDifficultyHard);

        WorldConfig.DifficultyConfig difficultyHardCoreConfig = worldConfig.getDifficultyConfig(WorldManager.Difficulty.HARDCORE);
        ItemStack itemStackDifficultyHardCore = new ItemStack(GameManager.isGame() ? (TosoGameAPI.difficultyMap.containsKey(p.getUniqueId()) && TosoGameAPI.difficultyMap.get(p.getUniqueId()) == WorldManager.Difficulty.HARDCORE ? Item.DIFFICULTY_HARDCORE.getMaterial() : Material.GRAY_CONCRETE) : Item.DIFFICULTY_HARDCORE.getMaterial(), 1);
        ItemMeta itemMetaDifficultyHardCore = itemStackDifficultyHardCore.getItemMeta();
        itemMetaDifficultyHardCore.addItemFlags(itemFlags);
        if (TosoGameAPI.difficultyMap.containsKey(p.getUniqueId()) && TosoGameAPI.difficultyMap.get(p.getUniqueId()) == WorldManager.Difficulty.HARDCORE)
            itemMetaDifficultyHardCore.addEnchant(Enchantment.DURABILITY, 1, true);
        itemMetaDifficultyHardCore.setDisplayName("" + ChatColor.DARK_RED + ChatColor.BOLD + ChatColor.UNDERLINE + ChatColor.stripColor(WorldManager.Difficulty.HARDCORE.getDisplayName()));
        itemMetaDifficultyHardCore.setLore(
                Arrays.asList(
                        ChatColor.YELLOW + "クリックして難易度を" + "" + ChatColor.DARK_RED + ChatColor.BOLD + ChatColor.UNDERLINE + ChatColor.stripColor(WorldManager.Difficulty.HARDCORE.getDisplayName()) + ChatColor.RESET + ChatColor.YELLOW + "に切り替えます。",
                        ChatColor.GRAY + "この機能は" + ChatColor.BOLD + ChatColor.UNDERLINE + "ゲーム準備中" + ChatColor.RESET + ChatColor.GRAY + "のみ使用可能です。",
                        "",
                        "" + ChatColor.BLUE + ChatColor.UNDERLINE + "レート" + ChatColor.RESET + ChatColor.GRAY + ": " + ChatColor.YELLOW + difficultyHardCoreConfig.getRate() + ChatColor.GRAY + "円/秒",
                        "",
                        "" + ChatColor.BLUE + ChatColor.UNDERLINE + "体力システム" + ChatColor.RESET + ChatColor.GRAY + ": " + ChatColor.YELLOW + (difficultyHardCoreConfig.getHealth() ? "有効" : "無効"),
                        "" + ChatColor.BLUE + ChatColor.UNDERLINE + "自動復活" + ChatColor.RESET + ChatColor.GRAY + ": " + ChatColor.YELLOW + (difficultyHardCoreConfig.getRespawnAutoTime() > -1 ? TimeFormat.formatJapan(difficultyHardCoreConfig.getRespawnAutoTime()) : "無効"),
                        "" + ChatColor.BLUE + ChatColor.UNDERLINE + "復活可能回数" + ChatColor.RESET + ChatColor.GRAY + ": " + ChatColor.YELLOW + difficultyHardCoreConfig.getRespawnDenyCount() + ChatColor.GRAY + "回",
                        "" + ChatColor.BLUE + ChatColor.UNDERLINE + "復活クールタイム" + ChatColor.RESET + ChatColor.GRAY + ": " + ChatColor.YELLOW + TimeFormat.formatJapan(difficultyHardCoreConfig.getRespawnCoolTime()) + ChatColor.GRAY + "/回",
                        "",
                        "" + ChatColor.BLUE + ChatColor.UNDERLINE + "ゲーム開始時のアイテム",
                        "" + ChatColor.BLUE + "骨 (透明化)" + ChatColor.RESET + ChatColor.GRAY + ": " + ChatColor.YELLOW + difficultyHardCoreConfig.getBone(WorldManager.GameType.START).getCount() + ChatColor.GRAY + "個 (" + difficultyHardCoreConfig.getBone(WorldManager.GameType.START).getDuration() + "秒)",
                        "" + ChatColor.BLUE + "羽 (移動速度上昇)" + ChatColor.RESET + ChatColor.GRAY + ": " + ChatColor.YELLOW + difficultyHardCoreConfig.getFeather(WorldManager.GameType.START).getCount() + ChatColor.GRAY + "個 (" + difficultyHardCoreConfig.getFeather(WorldManager.GameType.START).getDuration() + "秒)",
                        "" + ChatColor.BLUE + "卵 (盲目・移動速度低下)" + ChatColor.RESET + ChatColor.GRAY + ": " + ChatColor.YELLOW + difficultyHardCoreConfig.getEgg(WorldManager.GameType.START).getCount() + ChatColor.GRAY + "個",
                        "",
                        "" + ChatColor.BLUE + ChatColor.UNDERLINE + "復活時のアイテム",
                        "" + ChatColor.BLUE + "骨 (透明化)" + ChatColor.RESET + ChatColor.GRAY + ": " + ChatColor.YELLOW + difficultyHardCoreConfig.getBone(WorldManager.GameType.RESPAWN).getCount() + ChatColor.GRAY + "個 (" + difficultyHardCoreConfig.getBone(WorldManager.GameType.RESPAWN).getDuration() + "秒)",
                        "" + ChatColor.BLUE + "羽 (移動速度上昇)" + ChatColor.RESET + ChatColor.GRAY + ": " + ChatColor.YELLOW + difficultyHardCoreConfig.getFeather(WorldManager.GameType.RESPAWN).getCount() + ChatColor.GRAY + "個 (" + difficultyHardCoreConfig.getFeather(WorldManager.GameType.RESPAWN).getDuration() + "秒)",
                        "" + ChatColor.BLUE + "卵 (盲目・移動速度低下)" + ChatColor.RESET + ChatColor.GRAY + ": " + ChatColor.YELLOW + difficultyHardCoreConfig.getEgg(WorldManager.GameType.RESPAWN).getCount() + ChatColor.GRAY + "個"
                )
        );
        itemStackDifficultyHardCore.setItemMeta(itemMetaDifficultyHardCore);
        inv.setItem(Item.DIFFICULTY_HARDCORE.getIndex(), itemStackDifficultyHardCore);

        return inv;
    }

    public enum Item {
        PLAYER_INFO(1, 1, Material.PLAYER_HEAD),
        SETTINGS(3, 1, Material.COMPASS),
        NOTIFICATION(9, 1, Material.CHEST),
        MAP_APP(1, 3, Material.FILLED_MAP),
        MISSION_APP(3, 3, Material.WRITTEN_BOOK),
        SPEC_MODE(5, 3, Material.POTION),
        REQUEST_HUNTER(1, 5, Hunter.num > 0 ? Material.DIAMOND_SWORD : Material.STONE_SWORD),
        REQUEST_TUHO(2, 5, Tuho.num > 0 ? Material.GOLDEN_SWORD : Material.STONE_SWORD),
        DIFFICULTY_EASY(6, 5, Material.LIME_CONCRETE),
        DIFFICULTY_NORMAL(7, 5, Material.YELLOW_CONCRETE),
        DIFFICULTY_HARD(8, 5, Material.RED_CONCRETE),
        DIFFICULTY_HARDCORE(9, 5, Material.RED_NETHER_BRICKS),
        UNKNOWN(0, 0, Material.AIR);

        private final int x;
        private final int y;
        private final Material material;

        private Item(int x, int y, Material material) {
            this.x = x;
            this.y = y;
            this.material = material;
        }

        public int getIndex() {
            return getX() + getY();
        }

        public int getX() {
            return MainAPI.makePositive(x - 1);
        }

        public int getY() {
            return MainAPI.makePositive(y - 1) * 9;
        }

        public Material getMaterial() {
            return material;
        }

        public static Item getItem(int slot) {
            for (Item item : Item.values())
                if (item.getIndex() == slot)
                    return item;
            return Item.UNKNOWN;
        }
    }
}

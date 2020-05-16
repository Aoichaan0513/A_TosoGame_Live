package jp.aoichaan0513.A_TosoGame_Live.Listeners;

import jp.aoichaan0513.A_TosoGame_Live.API.IMission;
import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI;
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.ActionBarManager;
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.GameManager;
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.Inventory.Left.Call;
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.Inventory.Left.PlayerListInventory;
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.Inventory.MainInventory;
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.Inventory.MapInventory;
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.Inventory.Right.MissionInventory;
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.MissionManager;
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.RateManager;
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.World.WorldConfig;
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.World.WorldManager;
import jp.aoichaan0513.A_TosoGame_Live.API.Maps.MapUtility;
import jp.aoichaan0513.A_TosoGame_Live.API.Scoreboard.Teams;
import jp.aoichaan0513.A_TosoGame_Live.API.Timer.TimerFormat;
import jp.aoichaan0513.A_TosoGame_Live.API.TosoGameAPI;
import jp.aoichaan0513.A_TosoGame_Live.Commands.Command.Hunter;
import jp.aoichaan0513.A_TosoGame_Live.Commands.Command.Tuho;
import jp.aoichaan0513.A_TosoGame_Live.Main;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.HashSet;
import java.util.UUID;

public class onInventoryGui implements Listener {

    public static HashSet<UUID> list = new HashSet<>();

    @EventHandler
    public void onMainInventoryClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        Inventory inventory = e.getInventory();
        InventoryView inventoryView = e.getView();
        ItemStack itemStack = e.getCurrentItem();
        int slot = e.getRawSlot();

        if (inventory == null) return;
        if (inventory.getType() == InventoryType.CHEST) {
            if (inventoryView.getTitle().equals(MainInventory.title)) {
                WorldConfig worldConfig = Main.getWorldConfig();

                e.setCancelled(true);
                if (MainInventory.Item.getItem(slot) == MainInventory.Item.MAP_APP) {
                    if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_ADMIN, p) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_PLAYER, p) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_SUCCESS, p)) {
                        p.closeInventory();
                        if (p.getInventory().getItemInOffHand().getType() == Material.AIR) {
                            if (MapUtility.getMap() != null) {
                                ItemStack mapStack = MapUtility.getMap().clone();
                                ItemMeta mapMeta = mapStack.getItemMeta();
                                mapMeta.setDisplayName(ChatColor.GREEN + "地図");
                                mapMeta.setLore(Arrays.asList(ChatColor.YELLOW + "なんかすごいやつ (語彙力)"));
                                mapStack.setItemMeta(mapMeta);

                                p.getInventory().setItem(40, mapStack);
                                p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.WARNING) + ChatColor.BOLD + ChatColor.UNDERLINE + "マップアプリ" + ChatColor.RESET + ChatColor.YELLOW + "を開きました。");
                                return;
                            }
                            p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + ChatColor.RED + ChatColor.BOLD + ChatColor.UNDERLINE + "マップアプリ" + ChatColor.RESET + ChatColor.GRAY + "を開くことができません。");
                            return;
                        } else if (p.getInventory().getItemInOffHand().getType() == Material.FILLED_MAP) {
                            p.getInventory().setItem(40, new ItemStack(Material.AIR));
                            p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.WARNING) + ChatColor.BOLD + ChatColor.UNDERLINE + "マップアプリ" + ChatColor.RESET + ChatColor.YELLOW + "を閉じました。");
                            return;
                        } else {
                            p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "オフハンドを使用しているためアプリを開けません。");
                            return;
                        }
                    }
                    p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "現在実行できません。");
                    return;
                } else if (MainInventory.Item.getItem(slot) == MainInventory.Item.MISSION_APP) {
                    ActionBarManager.sendActionBar(p, "" + ChatColor.GOLD + ChatColor.BOLD + ChatColor.UNDERLINE + "ミッションアプリ" + ChatColor.RESET + ChatColor.YELLOW + "を開いています…");

                    p.openInventory(MissionInventory.getInventory(MissionManager.MissionBookType.MISSION));
                    ActionBarManager.sendActionBar(p, "" + ChatColor.GOLD + ChatColor.BOLD + ChatColor.UNDERLINE + "ミッションアプリ" + ChatColor.RESET + ChatColor.YELLOW + "を開きました。");
                    return;
                } else if (MainInventory.Item.getItem(slot) == MainInventory.Item.SPEC_MODE) {
                    if (GameManager.isGame(GameManager.GameState.GAME)) {
                        if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_JAIL, p)) {
                            if (!TosoGameAPI.isRes) {
                                if (p.getGameMode() == GameMode.ADVENTURE) {
                                    p.setGameMode(GameMode.SPECTATOR);
                                    TosoGameAPI.teleport(p, worldConfig.getRespawnLocationConfig().getLocations());
                                    p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.WARNING) + "観戦モードになりました。\n" +
                                            MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "\"/spec\"で観戦モードから戻れます。");
                                    return;
                                } else {
                                    p.setGameMode(GameMode.ADVENTURE);
                                    TosoGameAPI.teleport(p, worldConfig.getJailLocationConfig().getLocations());
                                    p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.WARNING) + "観戦モードから戻りました。");
                                    return;
                                }
                            }
                            p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "途中参加・復活が可能なため実行できません。");
                            return;
                        }
                        p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "確保されていないため実行できません。");
                        return;
                    }
                    p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "ゲーム中ではないため実行できません。");
                    return;
                } else if (MainInventory.Item.getItem(slot) == MainInventory.Item.REQUEST_HUNTER) {
                    if (Hunter.num > 0) {
                        if (!Main.shuffleList.contains(p))
                            Main.shuffleList.add(p);
                        else
                            Main.shuffleList.remove(p);

                        p.openInventory(MainInventory.getInventory(p));
                        p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.WARNING) + ChatColor.UNDERLINE + "ハンター募集" + ChatColor.GOLD + ChatColor.UNDERLINE + (Main.shuffleList.contains(p) ? "に応募" : "の応募をキャンセル") + ChatColor.RESET + ChatColor.YELLOW + "しました。");
                        return;
                    }
                    p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "ハンターを募集していないため実行できません。");
                    return;
                } else if (MainInventory.Item.getItem(slot) == MainInventory.Item.REQUEST_TUHO) {
                    if (Tuho.num > 0) {
                        if (!Main.shuffleList.contains(p))
                            Main.shuffleList.add(p);
                        else
                            Main.shuffleList.remove(p);

                        p.openInventory(MainInventory.getInventory(p));
                        p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.WARNING) + ChatColor.UNDERLINE + "通報部隊募集" + ChatColor.GOLD + ChatColor.UNDERLINE + (Main.shuffleList.contains(p) ? "に応募" : "の応募をキャンセル") + ChatColor.RESET + ChatColor.YELLOW + "しました。");
                        return;
                    }
                    p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "通報部隊を募集していないため実行できません。");
                    return;
                } else if (MainInventory.Item.getItem(slot) == MainInventory.Item.DIFFICULTY_EASY) {
                    if (!GameManager.isGame()) {
                        TosoGameAPI.difficultyMap.put(p.getUniqueId(), WorldManager.Difficulty.EASY);
                        RateManager.setRate(p);
                        p.openInventory(MainInventory.getInventory(p));
                        p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.WARNING) + "難易度を" + ChatColor.GREEN + ChatColor.BOLD + ChatColor.UNDERLINE + ChatColor.stripColor(WorldManager.Difficulty.EASY.getDisplayName()) + ChatColor.RESET + ChatColor.GOLD + "に変更しました。");
                        return;
                    }
                    MainAPI.sendMessage(p, MainAPI.ErrorMessage.GAME);
                    return;
                } else if (MainInventory.Item.getItem(slot) == MainInventory.Item.DIFFICULTY_NORMAL) {
                    if (!GameManager.isGame()) {
                        TosoGameAPI.difficultyMap.put(p.getUniqueId(), WorldManager.Difficulty.NORMAL);
                        RateManager.setRate(p);
                        p.openInventory(MainInventory.getInventory(p));
                        p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.WARNING) + "難易度を" + ChatColor.YELLOW + ChatColor.BOLD + ChatColor.UNDERLINE + ChatColor.stripColor(WorldManager.Difficulty.NORMAL.getDisplayName()) + ChatColor.RESET + ChatColor.GOLD + "に変更しました。");
                        return;
                    }
                    MainAPI.sendMessage(p, MainAPI.ErrorMessage.GAME);
                    return;
                } else if (MainInventory.Item.getItem(slot) == MainInventory.Item.DIFFICULTY_HARD) {
                    if (!GameManager.isGame()) {
                        TosoGameAPI.difficultyMap.put(p.getUniqueId(), WorldManager.Difficulty.HARD);
                        RateManager.setRate(p);
                        p.openInventory(MainInventory.getInventory(p));
                        p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.WARNING) + "難易度を" + ChatColor.RED + ChatColor.BOLD + ChatColor.UNDERLINE + ChatColor.stripColor(WorldManager.Difficulty.HARD.getDisplayName()) + ChatColor.RESET + ChatColor.GOLD + "に変更しました。");
                        return;
                    }
                    MainAPI.sendMessage(p, MainAPI.ErrorMessage.GAME);
                    return;
                } else if (MainInventory.Item.getItem(slot) == MainInventory.Item.DIFFICULTY_HARDCORE) {
                    if (!GameManager.isGame()) {
                        TosoGameAPI.difficultyMap.put(p.getUniqueId(), WorldManager.Difficulty.HARDCORE);
                        RateManager.setRate(p);
                        p.openInventory(MainInventory.getInventory(p));
                        p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.WARNING) + "難易度を" + ChatColor.DARK_RED + ChatColor.BOLD + ChatColor.UNDERLINE + ChatColor.stripColor(WorldManager.Difficulty.HARDCORE.getDisplayName()) + ChatColor.RESET + ChatColor.GOLD + "に変更しました。");
                        return;
                    }
                    MainAPI.sendMessage(p, MainAPI.ErrorMessage.GAME);
                    return;
                } else if (MainInventory.Item.getItem(slot) == MainInventory.Item.SETTINGS) {
                    if (TosoGameAPI.isAdmin(p)) {
                        p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.WARNING) + ChatColor.BOLD + ChatColor.UNDERLINE + "設定アプリ" + ChatColor.RESET + ChatColor.YELLOW + "を開いています…");

                        p.openInventory(MapInventory.getEditInventory());
                        p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.WARNING) + ChatColor.BOLD + ChatColor.UNDERLINE + "設定アプリ" + ChatColor.RESET + ChatColor.YELLOW + "を開きました。");
                    }
                    return;
                }
            }
        }
    }

    @EventHandler
    public void onMissionInventoryClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        Inventory inventory = e.getInventory();
        InventoryView inventoryView = e.getView();
        ItemStack itemStack = e.getCurrentItem();
        int slot = e.getSlot();

        if (inventory == null) return;
        if (inventory.getType() == InventoryType.CHEST) {
            if (inventoryView.getTitle().equals(MissionInventory.missionTitle) || inventoryView.getTitle().equals(MissionInventory.tutatuHintTitle) || inventoryView.getTitle().equals(MissionInventory.endTitle)) {
                e.setCancelled(true);
                if (itemStack.getType() == Material.AIR) return;

                if (slot > -1 && slot < 3 && itemStack.getType() == Material.RED_STAINED_GLASS_PANE) {
                    ItemMeta itemMeta = itemStack.getItemMeta();

                    if (itemMeta.getDisplayName().equals("" + ChatColor.RESET + ChatColor.RED + ChatColor.BOLD + "ミッション")) {
                        p.openInventory(MissionInventory.getInventory(MissionManager.MissionBookType.MISSION));
                        return;
                    }
                    return;
                } else if (slot > 2 && slot < 6 && itemStack.getType() == Material.YELLOW_STAINED_GLASS_PANE) {
                    ItemMeta itemMeta = itemStack.getItemMeta();

                    if (itemMeta.getDisplayName().equals("" + ChatColor.RESET + ChatColor.YELLOW + ChatColor.BOLD + "通達・ヒント")) {
                        p.openInventory(MissionInventory.getInventory(MissionManager.MissionBookType.TUTATU));
                        return;
                    }
                    return;
                } else if (slot > 5 && slot < 9 && itemStack.getType() == Material.LIME_STAINED_GLASS_PANE) {
                    ItemMeta itemMeta = itemStack.getItemMeta();

                    if (itemMeta.getDisplayName().equals("" + ChatColor.RESET + ChatColor.GREEN + ChatColor.BOLD + "終了したミッション")) {
                        p.openInventory(MissionInventory.getInventory(MissionManager.MissionBookType.END_MISSION));
                        return;
                    }
                    return;
                } else if (slot == 49 && itemStack.getType() == Material.WHITE_STAINED_GLASS_PANE) {
                    ItemMeta itemMeta = itemStack.getItemMeta();

                    if (itemMeta.getDisplayName().equals("" + ChatColor.RESET + ChatColor.GOLD + ChatColor.BOLD + "ホーム")) {
                        // p.closeInventory();
                        p.openInventory(MainInventory.getInventory(p));
                        p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.WARNING) + "ホーム画面を表示しました。");
                    }
                    return;
                } else {
                    if (inventoryView.getTitle().equals(MissionInventory.missionTitle)) {
                        MissionManager.MissionBookType type = MissionManager.MissionBookType.MISSION;

                        if (itemStack.getType() == MissionInventory.getItem(type, slot).getType() && itemStack.getAmount() == MissionInventory.getItem(type, slot).getAmount()) {
                            if (MissionInventory.getMissions(type).get(type.getId() + (itemStack.getAmount() * slot)) != null) {
                                IMission mission = MissionInventory.getMissions(type).get(type.getId() + (itemStack.getAmount() * slot));

                                if (mission.getType() == MissionManager.MissionBookType.MISSION) {
                                    ItemStack book = new ItemStack(Material.WRITTEN_BOOK, 1);
                                    BookMeta bookMeta = (BookMeta) book.getItemMeta();
                                    bookMeta.setTitle("逃走中");
                                    bookMeta.setAuthor("A_TosoGame_Live");
                                    bookMeta.addPage(ChatColor.DARK_RED + mission.getTitle() + ChatColor.RESET + "\n\n" + mission.getDescription());
                                    book.setItemMeta(bookMeta);

                                    MissionInventory.openBook(book, p);
                                    return;
                                }
                            }
                            return;
                        }
                    } else if (inventoryView.getTitle().equals(MissionInventory.tutatuHintTitle)) {
                        MissionManager.MissionBookType type = MissionManager.MissionBookType.TUTATU;

                        if (itemStack.getType() == MissionInventory.getItem(type, slot).getType() && itemStack.getAmount() == MissionInventory.getItem(type, slot).getAmount()) {
                            if (MissionInventory.getMissions(type).get(type.getId() + (itemStack.getAmount() * slot)) != null) {
                                IMission mission = MissionInventory.getMissions(type).get(type.getId() + (itemStack.getAmount() * slot));

                                if (mission.getType() == MissionManager.MissionBookType.TUTATU || mission.getType() == MissionManager.MissionBookType.HINT) {
                                    ItemStack book = new ItemStack(Material.WRITTEN_BOOK, 1);
                                    BookMeta bookMeta = (BookMeta) book.getItemMeta();
                                    bookMeta.setTitle("逃走中");
                                    bookMeta.setAuthor("A_TosoGame_Live");
                                    bookMeta.addPage(ChatColor.DARK_RED + mission.getTitle() + ChatColor.RESET + "\n\n" + mission.getDescription());
                                    book.setItemMeta(bookMeta);

                                    MissionInventory.openBook(book, p);
                                    return;
                                }
                            }
                            return;
                        }
                    } else if (inventoryView.getTitle().equals(MissionInventory.endTitle)) {
                        MissionManager.MissionBookType type = MissionManager.MissionBookType.END_MISSION;

                        if (itemStack.getType() == MissionInventory.getItem(type, slot).getType() && itemStack.getAmount() == MissionInventory.getItem(type, slot).getAmount()) {
                            if (MissionInventory.getMissions(type).get(1 + (itemStack.getAmount() * slot)) != null) {
                                IMission mission = MissionInventory.getMissions(type).get(1 + (itemStack.getAmount() * slot));

                                if (mission.getType() == MissionManager.MissionBookType.END_MISSION
                                        || mission.getType() == MissionManager.MissionBookType.END_TUTATU || mission.getType() == MissionManager.MissionBookType.END_HINT) {
                                    ItemStack book = new ItemStack(Material.WRITTEN_BOOK, 1);
                                    BookMeta bookMeta = (BookMeta) book.getItemMeta();
                                    bookMeta.setTitle("逃走中");
                                    bookMeta.setAuthor("A_TosoGame_Live");
                                    bookMeta.addPage(ChatColor.DARK_RED + mission.getTitle() + ChatColor.RESET + "\n\n" + mission.getDescription());
                                    book.setItemMeta(bookMeta);

                                    MissionInventory.openBook(book, p);
                                    return;
                                }
                            }
                            return;
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerListInventoryClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        Inventory inventory = e.getInventory();
        InventoryView inventoryView = e.getView();
        ItemStack itemStack = e.getCurrentItem();
        int slot = e.getSlot();

        if (inventory == null) return;
        if (inventory.getType() == InventoryType.CHEST) {
            if (inventoryView.getTitle().equals(PlayerListInventory.title)) {
                e.setCancelled(true);
                if (itemStack.getType() == Material.PLAYER_HEAD) {
                    SkullMeta itemMeta = (SkullMeta) itemStack.getItemMeta();
                    Player player = Bukkit.getPlayerExact(itemMeta.getOwner());
                    if (player != null) {
                        if (p.getUniqueId() != player.getUniqueId()) {
                            if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_ADMIN, player) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_PLAYER, player) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_SUCCESS, player)) {
                                if (!Call.isCall(player) && !Call.isAccept(player)) {
                                    p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.WARNING) + player.getName() + "に電話をかけています…");

                                    Call.startCall(p, player);

                                    TextComponent textComponent1 = new TextComponent();
                                    textComponent1.setText(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + p.getName() + "から着信です。\n");

                                    TextComponent textComponent2 = new TextComponent();
                                    textComponent2.setText(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY));
                                    TextComponent textComponent3 = new TextComponent();
                                    textComponent3.setText(ChatColor.GRAY + "" + ChatColor.BOLD + "" + ChatColor.UNDERLINE + "ここ");
                                    textComponent3.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.GRAY + "ここをクリックして通話を開始します。").create()));
                                    textComponent3.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/phone call accept"));
                                    textComponent2.addExtra(textComponent3);
                                    TextComponent textComponent4 = new TextComponent();
                                    textComponent4.setText(ChatColor.GRAY + "をクリックして通話を開始します。\n");
                                    textComponent2.addExtra(textComponent4);
                                    textComponent1.addExtra(textComponent2);

                                    TextComponent textComponent5 = new TextComponent();
                                    textComponent5.setText(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY));
                                    TextComponent textComponent6 = new TextComponent();
                                    textComponent6.setText(ChatColor.GRAY + "" + ChatColor.UNDERLINE + "ここ");
                                    textComponent6.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.GRAY + "ここをクリックして通話を拒否します。").create()));
                                    textComponent6.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/phone call reject"));
                                    textComponent5.addExtra(textComponent6);
                                    TextComponent textComponent7 = new TextComponent();
                                    textComponent7.setText(ChatColor.GRAY + "をクリックして通話を拒否します。");
                                    textComponent5.addExtra(textComponent7);
                                    textComponent1.addExtra(textComponent5);

                                    player.spigot().sendMessage(textComponent1);

                                    BukkitRunnable task = new BukkitRunnable() {
                                        int count = 0;

                                        public void run() {
                                            count++;
                                            if (count == 10 || count == 11 || count == 12 || count == 13 || count == 14 || count == 15
                                                    || count == 25 || count == 26 || count == 27 || count == 28 || count == 29) {
                                            } else if (count == 30) {
                                                count = 0;
                                            } else {
                                                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 2, 2);
                                            }
                                        }
                                    };

                                    if (!Call.soundMap.containsKey(player)) {
                                        Call.soundMap.put(player, task.runTaskTimer(Main.getInstance(), 3, 3));
                                    }
                                    return;
                                }
                                p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + player.getName() + "は通話中です。");
                                return;
                            }
                            p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "ハンター・通報部隊と通話することは出来ません。");
                            return;
                        }
                        p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "自分自身と通話することは出来ません。");
                        return;
                    }
                    p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + player.getName() + "はオフラインです。");
                    return;
                } else if (slot == 45 && itemStack.getType() == Material.QUARTZ_BLOCK) {
                    ItemMeta itemMeta = itemStack.getItemMeta();
                    if (itemMeta.getDisplayName().equals(ChatColor.BOLD + "ホーム")) {
                        p.closeInventory();
                        p.openInventory(MainInventory.getInventory(p));
                        p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.WARNING) + "ホーム画面を表示しました。");
                    }
                    return;
                } else if (slot == 49 && itemStack.getType() == Material.WHITE_STAINED_GLASS_PANE) {
                    ItemMeta itemMeta = itemStack.getItemMeta();
                    if (itemMeta.getDisplayName().equals(ChatColor.BOLD + "ホーム")) {
                        p.closeInventory();
                        p.openInventory(MainInventory.getInventory(p));
                        p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.WARNING) + "ホーム画面を表示しました。");
                    }
                    return;
                }
            }
        }
    }

    @EventHandler
    public void onMapEditInventoryClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        Inventory inventory = e.getInventory();
        InventoryView inventoryView = e.getView();
        ItemStack itemStack = e.getCurrentItem();
        int slot = e.getSlot();

        if (inventory == null) return;
        if (inventory.getType() == InventoryType.CHEST) {
            if (inventoryView.getTitle().equals(MapInventory.editTitle)) {
                WorldConfig worldConfig = Main.getWorldConfig();

                e.setCancelled(true);
                if (e.isLeftClick()) {
                    if (slot == 2 && itemStack.getType() == Material.PAPER) {
                        list.add(p.getUniqueId());
                        p.openInventory(MapInventory.getDifficultyInventory(MapInventory.ActionType.RESPAWN_COUNT));
                        return;
                    } else if (slot == 3 && itemStack.getType() == Material.PAPER) {
                        list.add(p.getUniqueId());
                        p.openInventory(MapInventory.getDifficultyInventory(MapInventory.ActionType.RESPAWN_COOLTIME));
                        return;
                    } else if (slot == 5 && itemStack.getType() == Material.PAPER) {
                        list.add(p.getUniqueId());
                        p.openInventory(MapInventory.getDifficultyInventory(MapInventory.ActionType.RATE));
                        return;
                    } else if (slot == 6 && itemStack.getType() == Material.PAPER) {
                        new AnvilGUI(Main.getInstance(), p, "サイコロの最大数を入力…", (player, reply) -> {
                            try {
                                int i = Integer.parseInt(reply);
                                worldConfig.getOPGameConfig().setDiceCount(i);
                                player.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.WARNING) + "サイコロの最大数" + ChatColor.RESET + ChatColor.YELLOW + "を" + ChatColor.GOLD + ChatColor.BOLD + ChatColor.UNDERLINE + i + "回" + ChatColor.RESET + ChatColor.YELLOW + "に設定しました。");
                                player.openInventory(MapInventory.getEditInventory());
                                return null;
                            } catch (NumberFormatException err) {
                                MainAPI.sendMessage(player, MainAPI.ErrorMessage.ARGS_INTEGER);
                                player.openInventory(MapInventory.getEditInventory());
                                return null;
                            }
                        });
                    } else if (slot == 19 && itemStack.getType() == Material.PAPER) {
                        new AnvilGUI(Main.getInstance(), p, "カウントダウンを秒数で入力…", (player, reply) -> {
                            try {
                                int i = Integer.parseInt(reply);
                                worldConfig.getGameConfig().setCountDown(i);
                                player.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.WARNING) + "カウントダウン" + ChatColor.RESET + ChatColor.YELLOW + "を" + ChatColor.GOLD + ChatColor.BOLD + ChatColor.UNDERLINE + TimerFormat.formatJapan(i) + ChatColor.RESET + ChatColor.YELLOW + "に設定しました。");
                                player.openInventory(MapInventory.getEditInventory());
                                return null;
                            } catch (NumberFormatException err) {
                                MainAPI.sendMessage(player, MainAPI.ErrorMessage.ARGS_INTEGER);
                                player.openInventory(MapInventory.getEditInventory());
                                return null;
                            }
                        });
                    } else if (slot == 20 && itemStack.getType() == Material.PAPER) {
                        new AnvilGUI(Main.getInstance(), p, "ゲーム時間を秒数で入力…", (player, reply) -> {
                            try {
                                int i = Integer.parseInt(reply);
                                worldConfig.getGameConfig().setGame(i);
                                player.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.WARNING) + "ゲーム時間" + ChatColor.RESET + ChatColor.YELLOW + "を" + ChatColor.GOLD + ChatColor.BOLD + ChatColor.UNDERLINE + TimerFormat.formatJapan(i) + ChatColor.RESET + ChatColor.YELLOW + "に設定しました。");
                                player.openInventory(MapInventory.getEditInventory());
                                return null;
                            } catch (NumberFormatException err) {
                                MainAPI.sendMessage(player, MainAPI.ErrorMessage.ARGS_INTEGER);
                                player.openInventory(MapInventory.getEditInventory());
                                return null;
                            }
                        });
                    } else if (slot == 21 && itemStack.getType() == Material.PAPER) {
                        new AnvilGUI(Main.getInstance(), p, "復活禁止時間を秒数で入力…", (player, reply) -> {
                            try {
                                int i = Integer.parseInt(reply);
                                worldConfig.getGameConfig().setRespawnDeny(i);
                                player.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.WARNING) + "復活禁止時間" + ChatColor.RESET + ChatColor.YELLOW + "を" + ChatColor.GOLD + ChatColor.BOLD + ChatColor.UNDERLINE + TimerFormat.formatJapan(i) + ChatColor.RESET + ChatColor.YELLOW + "に設定しました。");
                                player.openInventory(MapInventory.getEditInventory());
                                return null;
                            } catch (NumberFormatException err) {
                                MainAPI.sendMessage(player, MainAPI.ErrorMessage.ARGS_INTEGER);
                                player.openInventory(MapInventory.getEditInventory());
                                return null;
                            }
                        });
                    } else if (slot == 23) {
                        if (itemStack.getType() == Material.LIME_CONCRETE) {
                            worldConfig.getGameConfig().setScript(false);
                            p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SUCCESS) + "スクリプト機能を無効に設定しました。");
                            p.openInventory(MapInventory.getEditInventory());
                            return;
                        } else if (itemStack.getType() == Material.RED_CONCRETE) {
                            worldConfig.getGameConfig().setScript(true);
                            p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SUCCESS) + "スクリプト機能を有効に設定しました。");
                            p.openInventory(MapInventory.getEditInventory());
                            return;
                        }
                    } else if (slot == 24) {
                        if (itemStack.getType() == Material.LIME_CONCRETE) {
                            worldConfig.getGameConfig().setSuccessMission(false);
                            p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SUCCESS) + "生存ミッションを無効に設定しました。");
                            p.openInventory(MapInventory.getEditInventory());
                            return;
                        } else if (itemStack.getType() == Material.RED_CONCRETE) {
                            worldConfig.getGameConfig().setSuccessMission(true);
                            p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SUCCESS) + "生存ミッションを有効に設定しました。");
                            p.openInventory(MapInventory.getEditInventory());
                            return;
                        }
                    } else if (slot == 25) {
                        if (itemStack.getType() == Material.LIME_CONCRETE) {
                            worldConfig.getGameConfig().setJump(false);
                            p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SUCCESS) + "ダッシュジャンプを無効に設定しました。");
                            p.openInventory(MapInventory.getEditInventory());
                            return;
                        } else if (itemStack.getType() == Material.RED_CONCRETE) {
                            worldConfig.getGameConfig().setJump(true);
                            p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SUCCESS) + "ダッシュジャンプを有効に設定しました。");
                            p.openInventory(MapInventory.getEditInventory());
                            return;
                        }
                    } else if (slot == 28 && itemStack.getType() == Material.BONE) {
                        list.add(p.getUniqueId());
                        p.openInventory(MapInventory.getDifficultyInventory(MapInventory.ActionType.ITEM_START_BONE));
                        return;
                    } else if (slot == 29 && itemStack.getType() == Material.FEATHER) {
                        list.add(p.getUniqueId());
                        p.openInventory(MapInventory.getDifficultyInventory(MapInventory.ActionType.ITEM_START_FEATHER));
                        return;
                    } else if (slot == 30 && itemStack.getType() == Material.EGG) {
                        list.add(p.getUniqueId());
                        p.openInventory(MapInventory.getDifficultyInventory(MapInventory.ActionType.ITEM_START_EGG));
                        return;
                    } else if (slot == 32 && itemStack.getType() == Material.BONE) {
                        list.add(p.getUniqueId());
                        p.openInventory(MapInventory.getDifficultyInventory(MapInventory.ActionType.ITEM_RESPAWN_BONE));
                        return;
                    } else if (slot == 33 && itemStack.getType() == Material.FEATHER) {
                        list.add(p.getUniqueId());
                        p.openInventory(MapInventory.getDifficultyInventory(MapInventory.ActionType.ITEM_RESPAWN_FEATHER));
                        return;
                    } else if (slot == 34 && itemStack.getType() == Material.EGG) {
                        list.add(p.getUniqueId());
                        p.openInventory(MapInventory.getDifficultyInventory(MapInventory.ActionType.ITEM_RESPAWN_EGG));
                        return;
                    } else if (slot == 37 && (itemStack.getType() == Material.RED_CONCRETE || itemStack.getType() == Material.LIME_CONCRETE)) {
                        worldConfig.getOPGameLocationConfig().setOPLocation(p.getLocation());
                        p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SUCCESS) + "オープニングゲーム地点を設定しました。");
                        p.openInventory(MapInventory.getEditInventory());
                        return;
                    } else if (slot == 38 && (itemStack.getType() == Material.RED_CONCRETE || itemStack.getType() == Material.LIME_CONCRETE)) {
                        new AnvilGUI(Main.getInstance(), p, "数値を入力…", (player, reply) -> {
                            try {
                                int i = Integer.parseInt(reply);
                                if (i > 0) {
                                    worldConfig.getOPGameLocationConfig().setGOPLocation(i, p.getLocation());
                                    player.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SUCCESS) + "オープニングゲーム集合地点の位置" + i + "を設定しました。");
                                    player.openInventory(MapInventory.getEditInventory());
                                    return null;
                                }
                                player.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "引数が不正です。1以上で数字を指定してください。");
                                player.openInventory(MapInventory.getEditInventory());
                                return null;
                            } catch (NumberFormatException err) {
                                MainAPI.sendMessage(player, MainAPI.ErrorMessage.ARGS_INTEGER);
                                player.openInventory(MapInventory.getEditInventory());
                                return null;
                            }
                        });
                    } else if (slot == 40 && (itemStack.getType() == Material.RED_CONCRETE || itemStack.getType() == Material.LIME_CONCRETE)) {
                        new AnvilGUI(Main.getInstance(), p, "数値を入力…", (player, reply) -> {
                            try {
                                int i = Integer.parseInt(reply);
                                if (i > 0) {
                                    worldConfig.getHunterLocationConfig().setLocation(i, p.getLocation());
                                    player.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SUCCESS) + "ハンター集合地点の位置" + i + "を設定しました。");
                                    player.openInventory(MapInventory.getEditInventory());
                                    return null;
                                }
                                player.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "引数が不正です。1以上で数字を指定してください。");
                                player.openInventory(MapInventory.getEditInventory());
                                return null;
                            } catch (NumberFormatException err) {
                                MainAPI.sendMessage(player, MainAPI.ErrorMessage.ARGS_INTEGER);
                                player.openInventory(MapInventory.getEditInventory());
                                return null;
                            }
                        });
                    } else if (slot == 41 && (itemStack.getType() == Material.RED_CONCRETE || itemStack.getType() == Material.LIME_CONCRETE)) {
                        new AnvilGUI(Main.getInstance(), p, "数値を入力…", (player, reply) -> {
                            try {
                                int i = Integer.parseInt(reply);
                                if (i > 0) {
                                    worldConfig.getHunterDoorConfig().setLocation(i, p.getLocation());
                                    player.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SUCCESS) + "ハンターボックスのドア位置" + i + "を設定しました。");
                                    player.openInventory(MapInventory.getEditInventory());
                                    return null;
                                }
                                player.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "引数が不正です。1以上で数字を指定してください。");
                                player.openInventory(MapInventory.getEditInventory());
                                return null;
                            } catch (NumberFormatException err) {
                                MainAPI.sendMessage(player, MainAPI.ErrorMessage.ARGS_INTEGER);
                                player.openInventory(MapInventory.getEditInventory());
                                return null;
                            }
                        });
                    } else if (slot == 42 && (itemStack.getType() == Material.RED_CONCRETE || itemStack.getType() == Material.LIME_CONCRETE)) {
                        new AnvilGUI(Main.getInstance(), p, "数値を入力…", (player, reply) -> {
                            try {
                                int i = Integer.parseInt(reply);
                                if (i > 0) {
                                    worldConfig.getJailLocationConfig().setLocation(i, p.getLocation());
                                    player.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SUCCESS) + "牢獄地点の位置" + i + "を設定しました。");
                                    player.openInventory(MapInventory.getEditInventory());
                                    return null;
                                }
                                player.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "引数が不正です。1以上で数字を指定してください。");
                                player.openInventory(MapInventory.getEditInventory());
                                return null;
                            } catch (NumberFormatException err) {
                                MainAPI.sendMessage(player, MainAPI.ErrorMessage.ARGS_INTEGER);
                                player.openInventory(MapInventory.getEditInventory());
                                return null;
                            }
                        });
                    } else if (slot == 43 && (itemStack.getType() == Material.RED_CONCRETE || itemStack.getType() == Material.LIME_CONCRETE)) {
                        new AnvilGUI(Main.getInstance(), p, "数値を入力…", (player, reply) -> {
                            try {
                                int i = Integer.parseInt(reply);
                                if (i > 0) {
                                    worldConfig.getRespawnLocationConfig().setLocation(i, p.getLocation());
                                    player.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SUCCESS) + "復活地点の位置" + i + "を設定しました。");
                                    player.openInventory(MapInventory.getEditInventory());
                                    return null;
                                }
                                player.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "引数が不正です。1以上で数字を指定してください。");
                                player.openInventory(MapInventory.getEditInventory());
                                return null;
                            } catch (NumberFormatException err) {
                                MainAPI.sendMessage(player, MainAPI.ErrorMessage.ARGS_INTEGER);
                                player.openInventory(MapInventory.getEditInventory());
                                return null;
                            }
                        });
                    }
                } else if (e.isRightClick()) {
                    if (slot == 2 && itemStack.getType() == Material.PAPER) {
                        list.add(p.getUniqueId());
                        p.openInventory(MapInventory.getDifficultyInventory(MapInventory.ActionType.RESPAWN_COUNT));
                        return;
                    } else if (slot == 3 && itemStack.getType() == Material.PAPER) {
                        list.add(p.getUniqueId());
                        p.openInventory(MapInventory.getDifficultyInventory(MapInventory.ActionType.RESPAWN_COOLTIME));
                        return;
                    } else if (slot == 5 && itemStack.getType() == Material.PAPER) {
                        list.add(p.getUniqueId());
                        p.openInventory(MapInventory.getDifficultyInventory(MapInventory.ActionType.RATE));
                        return;
                    } else if (slot == 6 && itemStack.getType() == Material.PAPER) {
                        int i = 30;
                        worldConfig.getOPGameConfig().setDiceCount(i);
                        p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SUCCESS) + "サイコロの最大数を" + i + "に設定しました。");
                        p.openInventory(MapInventory.getEditInventory());
                        return;
                    } else if (slot == 19 && itemStack.getType() == Material.PAPER) {
                        int i = 15;
                        worldConfig.getGameConfig().setCountDown(i);
                        p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SUCCESS) + "カウントダウン秒数を" + TimerFormat.formatJapan(i) + "に設定しました。");
                        p.openInventory(MapInventory.getEditInventory());
                        return;
                    } else if (slot == 20 && itemStack.getType() == Material.PAPER) {
                        int i = 1200;
                        worldConfig.getGameConfig().setGame(i);
                        p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SUCCESS) + "ゲーム時間を" + TimerFormat.formatJapan(i) + "に設定しました。");
                        p.openInventory(MapInventory.getEditInventory());
                        return;
                    } else if (slot == 21 && itemStack.getType() == Material.PAPER) {
                        int i = 240;
                        worldConfig.getGameConfig().setRespawnDeny(i);
                        p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SUCCESS) + "復活禁止時間を" + TimerFormat.formatJapan(i) + "に設定しました。");
                        p.openInventory(MapInventory.getEditInventory());
                        return;
                    } else if (slot == 23 && (itemStack.getType() == Material.RED_CONCRETE || itemStack.getType() == Material.LIME_CONCRETE)) {
                        worldConfig.getGameConfig().setScript(false);
                        p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SUCCESS) + "スクリプト機能を無効に設定しました。");
                        p.openInventory(MapInventory.getEditInventory());
                        return;
                    } else if (slot == 24 && (itemStack.getType() == Material.RED_CONCRETE || itemStack.getType() == Material.LIME_CONCRETE)) {
                        worldConfig.getGameConfig().setSuccessMission(true);
                        p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SUCCESS) + "生存ミッションを有効に設定しました。");
                        p.openInventory(MapInventory.getEditInventory());
                        return;
                    } else if (slot == 25 && (itemStack.getType() == Material.RED_CONCRETE || itemStack.getType() == Material.LIME_CONCRETE)) {
                        worldConfig.getGameConfig().setJump(true);
                        p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SUCCESS) + "ダッシュジャンプを有効に設定しました。");
                        p.openInventory(MapInventory.getEditInventory());
                        return;
                    } else if (slot == 28 && itemStack.getType() == Material.BONE) {
                        list.add(p.getUniqueId());
                        p.openInventory(MapInventory.getDifficultyInventory(MapInventory.ActionType.ITEM_START_BONE));
                        return;
                    } else if (slot == 29 && itemStack.getType() == Material.FEATHER) {
                        list.add(p.getUniqueId());
                        p.openInventory(MapInventory.getDifficultyInventory(MapInventory.ActionType.ITEM_START_FEATHER));
                        return;
                    } else if (slot == 30 && itemStack.getType() == Material.EGG) {
                        list.add(p.getUniqueId());
                        p.openInventory(MapInventory.getDifficultyInventory(MapInventory.ActionType.ITEM_START_EGG));
                        return;
                    } else if (slot == 32 && itemStack.getType() == Material.BONE) {
                        list.add(p.getUniqueId());
                        p.openInventory(MapInventory.getDifficultyInventory(MapInventory.ActionType.ITEM_RESPAWN_BONE));
                        return;
                    } else if (slot == 33 && itemStack.getType() == Material.FEATHER) {
                        list.add(p.getUniqueId());
                        p.openInventory(MapInventory.getDifficultyInventory(MapInventory.ActionType.ITEM_RESPAWN_FEATHER));
                        return;
                    } else if (slot == 34 && itemStack.getType() == Material.EGG) {
                        list.add(p.getUniqueId());
                        p.openInventory(MapInventory.getDifficultyInventory(MapInventory.ActionType.ITEM_RESPAWN_EGG));
                        return;
                    } else if (slot == 37 && (itemStack.getType() == Material.RED_CONCRETE || itemStack.getType() == Material.LIME_CONCRETE)) {
                        p.teleport(worldConfig.getOPGameLocationConfig().getOPLocation());
                        p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SUCCESS) + "オープニングゲーム地点にテレポートしました。");
                        p.openInventory(MapInventory.getEditInventory());
                        return;
                    } else if (slot == 38 && (itemStack.getType() == Material.RED_CONCRETE || itemStack.getType() == Material.LIME_CONCRETE)) {
                        new AnvilGUI(Main.getInstance(), p, "数値を入力…", (player, reply) -> {
                            try {
                                int i = Integer.parseInt(reply);
                                if (i > 0) {
                                    if (worldConfig.getConfig().contains(WorldManager.PathType.LOCATION_GOPGAME.getPath() + ".p" + i)) {
                                        player.teleport(worldConfig.getOPGameLocationConfig().getGOPLocation(i));
                                        player.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SUCCESS) + "オープニングゲーム集合地点の位置" + i + "にテレポートしました。");
                                        player.openInventory(MapInventory.getEditInventory());
                                        return null;
                                    }
                                    player.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "オープニングゲーム集合地点の位置" + i + "は設定されていないためテレポートすることができません。");
                                    player.openInventory(MapInventory.getEditInventory());
                                    return null;
                                }
                                player.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "引数が不正です。1以上で数字を指定してください。");
                                player.openInventory(MapInventory.getEditInventory());
                                return null;
                            } catch (NumberFormatException err) {
                                MainAPI.sendMessage(player, MainAPI.ErrorMessage.ARGS_INTEGER);
                                player.openInventory(MapInventory.getEditInventory());
                                return null;
                            }
                        });
                    } else if (slot == 40 && (itemStack.getType() == Material.RED_CONCRETE || itemStack.getType() == Material.LIME_CONCRETE)) {
                        new AnvilGUI(Main.getInstance(), p, "数値を入力…", (player, reply) -> {
                            try {
                                int i = Integer.parseInt(reply);
                                if (i > 0) {
                                    if (worldConfig.getConfig().contains(WorldManager.PathType.LOCATION_HUNTER.getPath() + ".p" + i)) {
                                        player.teleport(worldConfig.getHunterLocationConfig().getLocation(i));
                                        player.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SUCCESS) + "ハンター集合地点の位置" + i + "にテレポートしました。");
                                        player.openInventory(MapInventory.getEditInventory());
                                        return null;
                                    }
                                    player.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "ハンター集合地点の位置" + i + "は設定されていないためテレポートすることができません。");
                                    player.openInventory(MapInventory.getEditInventory());
                                    return null;
                                }
                                player.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "引数が不正です。1以上で数字を指定してください。");
                                player.openInventory(MapInventory.getEditInventory());
                                return null;
                            } catch (NumberFormatException err) {
                                MainAPI.sendMessage(player, MainAPI.ErrorMessage.ARGS_INTEGER);
                                player.openInventory(MapInventory.getEditInventory());
                                return null;
                            }
                        });
                    } else if (slot == 41 && (itemStack.getType() == Material.RED_CONCRETE || itemStack.getType() == Material.LIME_CONCRETE)) {
                        new AnvilGUI(Main.getInstance(), p, "数値を入力…", (player, reply) -> {
                            try {
                                int i = Integer.parseInt(reply);
                                if (i > 0) {
                                    if (worldConfig.getConfig().contains(WorldManager.PathType.DOOR_HUNTER.getPath() + ".p" + i)) {
                                        worldConfig.getHunterDoorConfig().openHunterDoor(i);
                                        player.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SUCCESS) + "ハンターボックスのドア" + i + "を開きました。");
                                        player.openInventory(MapInventory.getEditInventory());
                                        return null;
                                    }
                                    player.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "ハンターボックスのドア" + i + "は設定されていないため開くことができません。");
                                    player.openInventory(MapInventory.getEditInventory());
                                    return null;
                                }
                                player.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "引数が不正です。1以上で数字を指定してください。");
                                player.openInventory(MapInventory.getEditInventory());
                                return null;
                            } catch (NumberFormatException err) {
                                MainAPI.sendMessage(player, MainAPI.ErrorMessage.ARGS_INTEGER);
                                player.openInventory(MapInventory.getEditInventory());
                                return null;
                            }
                        });
                    } else if (slot == 42 && (itemStack.getType() == Material.RED_CONCRETE || itemStack.getType() == Material.LIME_CONCRETE)) {
                        new AnvilGUI(Main.getInstance(), p, "数値を入力…", (player, reply) -> {
                            try {
                                int i = Integer.parseInt(reply);
                                if (i > 0) {
                                    if (worldConfig.getConfig().contains(WorldManager.PathType.LOCATION_JAIL.getPath() + ".p" + i)) {
                                        player.teleport(worldConfig.getJailLocationConfig().getLocation(i));
                                        player.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SUCCESS) + "牢獄地点の位置" + i + "にテレポートしました。");
                                        player.openInventory(MapInventory.getEditInventory());
                                        return null;
                                    }
                                    player.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "牢獄地点の位置" + i + "は設定されていないためテレポートすることができません。");
                                    player.openInventory(MapInventory.getEditInventory());
                                    return null;
                                }
                                player.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "引数が不正です。1以上で数字を指定してください。");
                                player.openInventory(MapInventory.getEditInventory());
                                return null;
                            } catch (NumberFormatException err) {
                                MainAPI.sendMessage(player, MainAPI.ErrorMessage.ARGS_INTEGER);
                                player.openInventory(MapInventory.getEditInventory());
                                return null;
                            }
                        });
                    } else if (slot == 43 && (itemStack.getType() == Material.RED_CONCRETE || itemStack.getType() == Material.LIME_CONCRETE)) {
                        new AnvilGUI(Main.getInstance(), p, "数値を入力…", (player, reply) -> {
                            try {
                                int i = Integer.parseInt(reply);
                                if (i > 0) {
                                    if (worldConfig.getConfig().contains(WorldManager.PathType.LOCATION_RESPAWN.getPath() + ".p" + i)) {
                                        player.teleport(worldConfig.getRespawnLocationConfig().getLocation(i));
                                        player.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SUCCESS) + "復活地点の位置" + i + "にテレポートしました。");
                                        player.openInventory(MapInventory.getEditInventory());
                                        return null;
                                    }
                                    player.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "復活地点の位置" + i + "は設定されていないためテレポートすることができません。");
                                    player.openInventory(MapInventory.getEditInventory());
                                    return null;
                                }
                                player.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "引数が不正です。1以上で数字を指定してください。");
                                player.openInventory(MapInventory.getEditInventory());
                                return null;
                            } catch (NumberFormatException err) {
                                MainAPI.sendMessage(player, MainAPI.ErrorMessage.ARGS_INTEGER);
                                player.openInventory(MapInventory.getEditInventory());
                                return null;
                            }
                        });
                    }
                }
            }
        }
    }

    @EventHandler
    public void onMapListInventoryClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        Inventory inventory = e.getInventory();
        InventoryView inventoryView = e.getView();
        ItemStack itemStack = e.getCurrentItem();

        if (inventory == null) return;
        if (inventory.getType() == InventoryType.CHEST) {
            if (inventoryView.getTitle().equals(MapInventory.listTitle)) {
                e.setCancelled(true);
                if (itemStack.getType() == Material.AIR) return;

                if (!itemStack.getEnchantments().containsKey(Enchantment.DURABILITY)) {
                    p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "マップを読み込んでいます…");
                    WorldManager.setWorld(ChatColor.stripColor(itemStack.getItemMeta().getDisplayName()));
                    World world = Bukkit.createWorld(new WorldCreator(ChatColor.stripColor(itemStack.getItemMeta().getDisplayName())));
                    p.teleport(world.getSpawnLocation());
                    p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SUCCESS) + "マップを読み込みました。");
                    return;
                }
                p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "同じマップに変更することはできません。");
                return;
            }
        }
    }

    @EventHandler
    public void onDifficultyInventoryClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        Inventory inventory = e.getInventory();
        InventoryView inventoryView = e.getView();
        ItemStack itemStack = e.getCurrentItem();
        int slot = e.getSlot();

        if (inventory == null) return;
        if (inventory.getType() == InventoryType.CHEST) {
            if (inventoryView.getTitle().startsWith(MapInventory.difficultyTitle)) {
                WorldConfig worldConfig = Main.getWorldConfig();

                String title = ChatColor.stripColor(inventoryView.getTitle()).substring(ChatColor.stripColor(MapInventory.difficultyTitle).length());

                e.setCancelled(true);

                if (slot == 10 && itemStack.getType() == MainInventory.Item.DIFFICULTY_EASY.getMaterial()) {
                    WorldConfig.DifficultyConfig difficultyConfig = worldConfig.getDifficultyConfig(WorldManager.Difficulty.EASY);
                    String difficultyName = WorldManager.Difficulty.EASY.getDisplayName();

                    runAction(p, worldConfig, difficultyConfig, difficultyName, title);
                } else if (slot == 11 && itemStack.getType() == MainInventory.Item.DIFFICULTY_NORMAL.getMaterial()) {
                    WorldConfig.DifficultyConfig difficultyConfig = worldConfig.getDifficultyConfig(WorldManager.Difficulty.NORMAL);
                    String difficultyName = WorldManager.Difficulty.NORMAL.getDisplayName();

                    runAction(p, worldConfig, difficultyConfig, difficultyName, title);
                } else if (slot == 12 && itemStack.getType() == MainInventory.Item.DIFFICULTY_HARD.getMaterial()) {
                    WorldConfig.DifficultyConfig difficultyConfig = worldConfig.getDifficultyConfig(WorldManager.Difficulty.HARD);
                    String difficultyName = WorldManager.Difficulty.HARD.getDisplayName();

                    runAction(p, worldConfig, difficultyConfig, difficultyName, title);
                } else if (slot == 13 && itemStack.getType() == MainInventory.Item.DIFFICULTY_HARDCORE.getMaterial()) {
                    WorldConfig.DifficultyConfig difficultyConfig = worldConfig.getDifficultyConfig(WorldManager.Difficulty.HARDCORE);
                    String difficultyName = WorldManager.Difficulty.HARDCORE.getDisplayName();

                    runAction(p, worldConfig, difficultyConfig, difficultyName, title);
                }
            }
        }
    }

    private void runAction(Player p, WorldConfig worldConfig, WorldConfig.DifficultyConfig difficultyConfig, String difficultyName, String inventoryTitle) {
        if (MapInventory.ActionType.RATE.getDisplayName().equalsIgnoreCase(inventoryTitle)) {
            new AnvilGUI(Main.getInstance(), p, "レートを入力…", (player, reply) -> {
                try {
                    int i = Integer.parseInt(reply);
                    difficultyConfig.setRate(i);
                    player.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.WARNING) + difficultyName + ChatColor.YELLOW + "の" + ChatColor.GOLD + "レート" + ChatColor.RESET + ChatColor.YELLOW + "を" + ChatColor.GOLD + ChatColor.BOLD + ChatColor.UNDERLINE + i + "円/秒" + ChatColor.RESET + ChatColor.YELLOW + "に設定しました。");
                    player.openInventory(MapInventory.getEditInventory());
                    return null;
                } catch (NumberFormatException err) {
                    MainAPI.sendMessage(player, MainAPI.ErrorMessage.ARGS_INTEGER);
                    player.openInventory(MapInventory.getEditInventory());
                    return null;
                }
            });
        } else if (MapInventory.ActionType.RESPAWN_COUNT.getDisplayName().equalsIgnoreCase(inventoryTitle)) {
            new AnvilGUI(Main.getInstance(), p, "復活可能回数を入力…", (player, reply) -> {
                try {
                    int i = Integer.parseInt(reply);
                    difficultyConfig.setRespawnDenyCount(i);
                    player.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.WARNING) + difficultyName + ChatColor.YELLOW + "の" + ChatColor.GOLD + "復活可能回数" + ChatColor.RESET + ChatColor.YELLOW + "を" + ChatColor.GOLD + ChatColor.BOLD + ChatColor.UNDERLINE + i + "回" + ChatColor.RESET + ChatColor.YELLOW + "に設定しました。");
                    player.openInventory(MapInventory.getEditInventory());
                    return null;
                } catch (NumberFormatException err) {
                    MainAPI.sendMessage(player, MainAPI.ErrorMessage.ARGS_INTEGER);
                    player.openInventory(MapInventory.getEditInventory());
                    return null;
                }
            });
        } else if (MapInventory.ActionType.RESPAWN_COOLTIME.getDisplayName().equalsIgnoreCase(inventoryTitle)) {
            new AnvilGUI(Main.getInstance(), p, "復活クールタイムを秒数で入力…", (player, reply) -> {
                try {
                    int i = Integer.parseInt(reply);
                    difficultyConfig.setRespawnCoolTime(i);
                    player.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.WARNING) + difficultyName + ChatColor.YELLOW + "の" + ChatColor.GOLD + "復活クールタイム" + ChatColor.RESET + ChatColor.YELLOW + "を" + ChatColor.GOLD + ChatColor.BOLD + ChatColor.UNDERLINE + TimerFormat.formatJapan(i) + "/回" + ChatColor.RESET + ChatColor.YELLOW + "に設定しました。");
                    player.openInventory(MapInventory.getEditInventory());
                    return null;
                } catch (NumberFormatException err) {
                    MainAPI.sendMessage(player, MainAPI.ErrorMessage.ARGS_INTEGER);
                    player.openInventory(MapInventory.getEditInventory());
                    return null;
                }
            });
        } else if (MapInventory.ActionType.ITEM_START_BONE.getDisplayName().equalsIgnoreCase(inventoryTitle)) {
            new AnvilGUI(Main.getInstance(), p, "ゲーム開始時の骨の数を入力…", (player, reply) -> {
                try {
                    int i = Integer.parseInt(reply);
                    if (i < 65) {
                        difficultyConfig.getBone(WorldManager.GameType.START).setCount(i);
                        player.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.WARNING) + difficultyName + ChatColor.YELLOW + "の" + ChatColor.GOLD + "ゲーム開始時の骨の数" + ChatColor.RESET + ChatColor.YELLOW + "を" + ChatColor.GOLD + ChatColor.BOLD + ChatColor.UNDERLINE + i + "個" + ChatColor.RESET + ChatColor.YELLOW + "に設定しました。");
                        player.openInventory(MapInventory.getEditInventory());
                        return null;
                    }
                    player.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "引数が不正です。数を64以下で指定してください。");
                    player.openInventory(MapInventory.getEditInventory());
                    return null;
                } catch (NumberFormatException err) {
                    MainAPI.sendMessage(player, MainAPI.ErrorMessage.ARGS_INTEGER);
                    player.openInventory(MapInventory.getEditInventory());
                    return null;
                }
            });
        } else if (MapInventory.ActionType.ITEM_START_FEATHER.getDisplayName().equalsIgnoreCase(inventoryTitle)) {
            new AnvilGUI(Main.getInstance(), p, "ゲーム開始時の羽の数を入力…", (player, reply) -> {
                try {
                    int i = Integer.parseInt(reply);
                    if (i < 65) {
                        difficultyConfig.getFeather(WorldManager.GameType.START).setCount(i);
                        player.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.WARNING) + difficultyName + ChatColor.YELLOW + "の" + ChatColor.GOLD + "ゲーム開始時の羽の数" + ChatColor.RESET + ChatColor.YELLOW + "を" + ChatColor.GOLD + ChatColor.BOLD + ChatColor.UNDERLINE + i + "個" + ChatColor.RESET + ChatColor.YELLOW + "に設定しました。");
                        player.openInventory(MapInventory.getEditInventory());
                        return null;
                    }
                    player.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "引数が不正です。数を64以下で指定してください。");
                    player.openInventory(MapInventory.getEditInventory());
                    return null;
                } catch (NumberFormatException err) {
                    MainAPI.sendMessage(player, MainAPI.ErrorMessage.ARGS_INTEGER);
                    player.openInventory(MapInventory.getEditInventory());
                    return null;
                }
            });
        } else if (MapInventory.ActionType.ITEM_START_EGG.getDisplayName().equalsIgnoreCase(inventoryTitle)) {
            new AnvilGUI(Main.getInstance(), p, "ゲーム開始時の卵の数を入力…", (player, reply) -> {
                try {
                    int i = Integer.parseInt(reply);
                    if (i < 65) {
                        difficultyConfig.getEgg(WorldManager.GameType.START).setCount(i);
                        player.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.WARNING) + difficultyName + ChatColor.YELLOW + "の" + ChatColor.GOLD + "ゲーム開始時の卵の数" + ChatColor.RESET + ChatColor.YELLOW + "を" + ChatColor.GOLD + ChatColor.BOLD + ChatColor.UNDERLINE + i + "個" + ChatColor.RESET + ChatColor.YELLOW + "に設定しました。");
                        player.openInventory(MapInventory.getEditInventory());
                        return null;
                    }
                    player.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "引数が不正です。数を64以下で指定してください。");
                    player.openInventory(MapInventory.getEditInventory());
                    return null;
                } catch (NumberFormatException err) {
                    MainAPI.sendMessage(player, MainAPI.ErrorMessage.ARGS_INTEGER);
                    player.openInventory(MapInventory.getEditInventory());
                    return null;
                }
            });
        } else if (MapInventory.ActionType.ITEM_RESPAWN_BONE.getDisplayName().equalsIgnoreCase(inventoryTitle)) {
            new AnvilGUI(Main.getInstance(), p, "復活時の骨の数を入力…", (player, reply) -> {
                try {
                    int i = Integer.parseInt(reply);
                    if (i < 65) {
                        difficultyConfig.getBone(WorldManager.GameType.RESPAWN).setCount(i);
                        player.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.WARNING) + difficultyName + ChatColor.YELLOW + "の" + ChatColor.GOLD + "復活時の骨の数" + ChatColor.RESET + ChatColor.YELLOW + "を" + ChatColor.GOLD + ChatColor.BOLD + ChatColor.UNDERLINE + i + "個" + ChatColor.RESET + ChatColor.YELLOW + "に設定しました。");
                        player.openInventory(MapInventory.getEditInventory());
                        return null;
                    }
                    player.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "引数が不正です。数を64以下で指定してください。");
                    player.openInventory(MapInventory.getEditInventory());
                    return null;
                } catch (NumberFormatException err) {
                    MainAPI.sendMessage(player, MainAPI.ErrorMessage.ARGS_INTEGER);
                    player.openInventory(MapInventory.getEditInventory());
                    return null;
                }
            });
        } else if (MapInventory.ActionType.ITEM_RESPAWN_FEATHER.getDisplayName().equalsIgnoreCase(inventoryTitle)) {
            new AnvilGUI(Main.getInstance(), p, "復活時の羽の数を入力…", (player, reply) -> {
                try {
                    int i = Integer.parseInt(reply);
                    if (i < 65) {
                        difficultyConfig.getFeather(WorldManager.GameType.RESPAWN).setCount(i);
                        player.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.WARNING) + difficultyName + ChatColor.YELLOW + "の" + ChatColor.GOLD + "復活時の羽の数" + ChatColor.RESET + ChatColor.YELLOW + "を" + ChatColor.GOLD + ChatColor.BOLD + ChatColor.UNDERLINE + i + "個" + ChatColor.RESET + ChatColor.YELLOW + "に設定しました。");
                        player.openInventory(MapInventory.getEditInventory());
                        return null;
                    }
                    player.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "引数が不正です。数を64以下で指定してください。");
                    player.openInventory(MapInventory.getEditInventory());
                    return null;
                } catch (NumberFormatException err) {
                    MainAPI.sendMessage(player, MainAPI.ErrorMessage.ARGS_INTEGER);
                    player.openInventory(MapInventory.getEditInventory());
                    return null;
                }
            });
        } else if (MapInventory.ActionType.ITEM_RESPAWN_EGG.getDisplayName().equalsIgnoreCase(inventoryTitle)) {
            new AnvilGUI(Main.getInstance(), p, "復活時の卵の数を入力…", (player, reply) -> {
                try {
                    int i = Integer.parseInt(reply);
                    if (i < 65) {
                        difficultyConfig.getEgg(WorldManager.GameType.RESPAWN).setCount(i);
                        player.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.WARNING) + difficultyName + ChatColor.YELLOW + "の" + ChatColor.GOLD + "復活時の卵の数" + ChatColor.RESET + ChatColor.YELLOW + "を" + ChatColor.GOLD + ChatColor.BOLD + ChatColor.UNDERLINE + i + "個" + ChatColor.RESET + ChatColor.YELLOW + "に設定しました。");
                        player.openInventory(MapInventory.getEditInventory());
                        return null;
                    }
                    player.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "引数が不正です。数を64以下で指定してください。");
                    player.openInventory(MapInventory.getEditInventory());
                    return null;
                } catch (NumberFormatException err) {
                    MainAPI.sendMessage(player, MainAPI.ErrorMessage.ARGS_INTEGER);
                    player.openInventory(MapInventory.getEditInventory());
                    return null;
                }
            });
        }
    }
}

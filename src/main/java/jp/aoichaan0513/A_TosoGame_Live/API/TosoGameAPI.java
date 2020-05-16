package jp.aoichaan0513.A_TosoGame_Live.API;

import jp.aoichaan0513.A_TosoGame_Live.API.Manager.GameManager;
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.MissionManager;
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.World.WorldConfig;
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.World.WorldManager;
import jp.aoichaan0513.A_TosoGame_Live.API.Maps.MapUtility;
import jp.aoichaan0513.A_TosoGame_Live.API.Scoreboard.Teams;
import jp.aoichaan0513.A_TosoGame_Live.Main;
import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class TosoGameAPI {

    public static boolean isRes = true;
    // public static HashMap<UUID, Integer> respawnCountMap = new HashMap<>();

    // 復活クールタイム
    // public static HashMap<UUID, Integer> respawnCoolTimeMap = new HashMap<>();
    public static HashMap<UUID, WorldManager.Difficulty> difficultyMap = new HashMap<>();

    public static void setItem(WorldManager.GameType type, Player p) {
        if (GameManager.isGame(GameManager.GameState.GAME)) {
            if (!Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_HUNTER, p)) {
                if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_ADMIN, p)) {
                    Inventory i = p.getInventory();
                    i.clear();

                    ItemStack infoStack = new ItemStack(Material.GOLD_NUGGET);
                    ItemMeta infoMeta = infoStack.getItemMeta();
                    infoMeta.setDisplayName(ChatColor.GREEN + "プレイヤー情報");
                    infoMeta.setLore(Arrays.asList(ChatColor.YELLOW + "プレイヤーを右クリックして", ChatColor.YELLOW + "そのプレイヤーの情報が確認できます。"));
                    infoStack.setItemMeta(infoMeta);

                    i.setItem(1, infoStack);

                    if (Bukkit.getPluginManager().isPluginEnabled("WorldEdit")) {
                        ItemStack editStack = new ItemStack(Material.COMPASS);
                        ItemMeta editMeta = editStack.getItemMeta();
                        editMeta.setDisplayName(ChatColor.GREEN + "コンパス (WorldEdit)");
                        editMeta.setLore(Arrays.asList(ChatColor.YELLOW + "右クリックした方向にテレポートが出来ます。"));
                        editStack.setItemMeta(editMeta);

                        i.setItem(2, editStack);
                    }

                    MissionManager.reloadBook(p);
                    return;
                } else if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_JAIL, p)) {
                    Inventory i = p.getInventory();
                    i.clear();

                    MissionManager.reloadBook(p);

                    ItemStack itemStack = new ItemStack(Material.ENDER_PEARL);
                    ItemMeta itemMeta = itemStack.getItemMeta();
                    itemMeta.setDisplayName(ChatColor.GREEN + "プレイヤーを非表示");
                    itemMeta.setLore(Arrays.asList(ChatColor.YELLOW + "右クリックして運営以外のプレイヤーを非表示にします。"));
                    itemStack.setItemMeta(itemMeta);

                    i.setItem(8, itemStack);
                    return;
                } else {
                    WorldConfig worldConfig = Main.getWorldConfig();
                    WorldConfig.DifficultyConfig difficultyConfig = worldConfig.getDifficultyConfig(p);

                    Inventory i = p.getInventory();
                    i.clear();

                    if (MapUtility.getMap() != null) {
                        ItemStack mapStack = MapUtility.getMap().clone();
                        if (mapStack != null) {
                            ItemMeta mapMeta = mapStack.getItemMeta();
                            mapMeta.setDisplayName(ChatColor.GREEN + "地図");
                            mapMeta.setLore(Arrays.asList(ChatColor.YELLOW + "なんかすごいやつ (語彙力)"));
                            mapStack.setItemMeta(mapMeta);

                            i.setItem(40, mapStack);
                        }
                    }

                    ItemStack boneStack = new ItemStack(Material.BONE, difficultyConfig.getBone(type).getCount());
                    ItemMeta boneMeta = boneStack.getItemMeta();
                    boneMeta.setDisplayName(ChatColor.GREEN + "骨 (透明化)");
                    boneMeta.setLore(Arrays.asList(ChatColor.YELLOW + "右クリックして10秒間透明化します。"));
                    boneStack.setItemMeta(boneMeta);

                    ItemStack featherStack = new ItemStack(Material.FEATHER, difficultyConfig.getFeather(type).getCount());
                    ItemMeta featherMeta = featherStack.getItemMeta();
                    featherMeta.setDisplayName(ChatColor.GREEN + "羽 (移動速度上昇)");
                    featherMeta.setLore(Arrays.asList(ChatColor.YELLOW + "右クリックして10秒間移動速度が上昇します。"));
                    featherStack.setItemMeta(featherMeta);

                    ItemStack eggStack = new ItemStack(Material.EGG, difficultyConfig.getEgg(type).getCount());
                    ItemMeta eggMeta = eggStack.getItemMeta();
                    eggMeta.setDisplayName(ChatColor.GREEN + "卵 (盲目・移動速度低下)");
                    eggMeta.setLore(Arrays.asList(ChatColor.YELLOW + "ハンターに当てると盲目と移動速度低下を5秒間与えます。"));
                    eggStack.setItemMeta(eggMeta);

                    i.setItem(0, boneStack);
                    i.setItem(1, featherStack);
                    i.setItem(2, eggStack);

                    MissionManager.reloadBook(p);
                    return;
                }
            }
        } else {
            if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_ADMIN, p)) {
                Inventory i = p.getInventory();
                i.clear();

                ItemStack infoStack = new ItemStack(Material.GOLD_NUGGET);
                ItemMeta infoMeta = infoStack.getItemMeta();
                infoMeta.setDisplayName(ChatColor.GREEN + "プレイヤー情報");
                infoMeta.setLore(Arrays.asList(ChatColor.YELLOW + "プレイヤーを右クリックして", ChatColor.YELLOW + "そのプレイヤーの情報が確認できます。"));
                infoStack.setItemMeta(infoMeta);

                i.setItem(1, infoStack);

                if (Bukkit.getPluginManager().isPluginEnabled("WorldEdit")) {
                    ItemStack editStack = new ItemStack(Material.COMPASS);
                    ItemMeta editMeta = editStack.getItemMeta();
                    editMeta.setDisplayName(ChatColor.GREEN + "コンパス (WorldEdit)");
                    editMeta.setLore(Arrays.asList(ChatColor.YELLOW + "右クリックした方向にテレポートが出来ます。"));
                    editStack.setItemMeta(editMeta);

                    i.setItem(2, editStack);
                }

                MissionManager.reloadBook(p);
                return;
            } else {
                p.getInventory().clear();
                MissionManager.reloadBook(p);
                return;
            }
        }
        return;
    }

    public static void setArmor(Player p) {
        if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_HUNTER, p)) {
            p.getInventory().clear();
            p.getInventory().setHelmet(new ItemStack(Material.DIAMOND_HELMET));
            p.getInventory().setChestplate(new ItemStack(Material.DIAMOND_CHESTPLATE));
            p.getInventory().setLeggings(new ItemStack(Material.DIAMOND_LEGGINGS));
            p.getInventory().setBoots(new ItemStack(Material.DIAMOND_BOOTS));
            return;
        } else if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_TUHO, p)) {
            p.getInventory().clear();
            p.getInventory().setHelmet(new ItemStack(Material.GOLDEN_HELMET));
            p.getInventory().setChestplate(new ItemStack(Material.GOLDEN_CHESTPLATE));
            p.getInventory().setLeggings(new ItemStack(Material.GOLDEN_LEGGINGS));
            p.getInventory().setBoots(new ItemStack(Material.GOLDEN_BOOTS));
            return;
        }
        return;
    }

    public static void removeArmor(Player p) {
        p.getInventory().setHelmet(new ItemStack(Material.AIR));
        p.getInventory().setChestplate(new ItemStack(Material.AIR));
        p.getInventory().setLeggings(new ItemStack(Material.AIR));
        p.getInventory().setBoots(new ItemStack(Material.AIR));
        return;
    }

    public static void setPotionEffect(Player p) {
        setPotionEffect(p, true);
    }

    public static void setPotionEffect(Player p, boolean isRemove) {
        if (!GameManager.isGame()) return;

        if (isRemove)
            for (PotionEffect effect : p.getActivePotionEffects())
                p.removePotionEffect(effect.getType());

        p.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 200000, 1, false, false));

        if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_PLAYER, p) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_SUCCESS, p)) {
            p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 200, 1, false, false));
            p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 200, 1, false, false));
        } else if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_HUNTER, p)) {
            p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 200000, 0, false, false));
        }
        return;
    }

    public static void showPlayers(Player p) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (MainAPI.isHidePlayer(player)) {
                // disappear コマンドで非表示にしていた時
                p.hidePlayer(player);
            } else {
                // 配信者が非表示設定にしていた時
                if (isHidePlayer(player)) {
                    if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_PLAYER, p) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_SUCCESS, p))
                        p.hidePlayer(player);
                    else
                        p.showPlayer(player);
                } else {
                    p.showPlayer(player);
                }
            }
        }

        // 逃走者が透明を使用した時
        if (!Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_ADMIN, p)) {
            for (UUID uuid : Main.invisibleList) {
                if (Bukkit.getPlayer(uuid) == null) continue;
                p.hidePlayer(Bukkit.getPlayer(uuid));
            }
        } else {
            for (UUID uuid : Main.invisibleList) {
                if (Bukkit.getPlayer(uuid) == null) continue;
                p.showPlayer(Bukkit.getPlayer(uuid));
            }
        }
    }

    public static boolean isPlayer(CommandSender sender) {
        return sender instanceof Player;
    }

    public static boolean isAdmin(Player p) {
        return p.isOp() && (isBroadCaster(p) || isPermissionHave(p) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_ADMIN, p));
    }

    public static boolean isBroadCaster(Player p) {
        return Main.getMainConfig().getStringList("broadCasters").contains(p.getUniqueId().toString());
    }

    public static boolean isPermissionHave(Player p) {
        return Main.getMainConfig().getStringList("permissionHaves").contains(p.getUniqueId().toString());
    }

    public static void toggleOp(Player p) {
        if (TosoGameAPI.isBroadCaster(p) || TosoGameAPI.isPermissionHave(p)) {
            if (p.isOp())
                removeOp(p);
            else
                addOp(p);
        }
    }

    public static void addOp(Player p) {
        if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_ADMIN, p) || TosoGameAPI.isBroadCaster(p) || TosoGameAPI.isPermissionHave(p))
            p.setOp(true);
    }

    public static void removeOp(Player p) {
        if (!Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_ADMIN, p) || TosoGameAPI.isBroadCaster(p) || TosoGameAPI.isPermissionHave(p))
            p.setOp(false);
    }

    public static void teleport(Player p, Location location) {
        Bukkit.getScheduler().runTask(Main.getInstance(), () -> p.teleport(location));
    }

    public static void teleport(Player p, List<Location> list) {
        Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
            Collections.shuffle(list, ThreadLocalRandom.current());
            p.teleport(list.get(0));
        });
    }

    public static void sendNotificationSound() {
        for (Player p : Bukkit.getOnlinePlayers())
            TosoGameAPI.sendNotificationSound(p);
    }

    public static void sendNotificationSound(Player p) {
        /*
        new BukkitRunnable() {
            int count = 0;

            public void run() {
                count++;
                if (count == 10 || count == 11 || count == 12 || count == 13 || count == 14 || count == 15) {
                } else if (count == 25) {
                    cancel();
                } else {
                    p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 2, 2);
                }
            }
        }.runTaskTimer(Main.getInstance(), 3, 3);
        */
        new BukkitRunnable() {
            int c = 26;

            @Override
            public void run() {
                if ((c <= 26 && c >= 18) || (c <= 9 && c > 0))
                    p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 2);
                if (c < 0)
                    cancel();
                c--;
            }
        }.runTaskTimerAsynchronously(Main.getInstance(), 0, 2);
    }

    private static List<UUID> liveHidePlayerList = new ArrayList<>();

    public static void hidePlayers(Player p) {
        for (UUID uuid : liveHidePlayerList) {
            Player player = Bukkit.getPlayer(uuid);

            if (player == null) continue;
            if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_PLAYER, p) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_SUCCESS, p))
                p.hidePlayer(player);
            else
                p.showPlayer(player);
        }
    }

    public static List<UUID> getHidePlayerList() {
        return liveHidePlayerList;
    }

    public static boolean isHidePlayer(Player p) {
        return liveHidePlayerList.contains(p.getUniqueId());
    }

    public static void addHidePlayer(Player p) {
        if (liveHidePlayerList.contains(p.getUniqueId())) return;
        liveHidePlayerList.add(p.getUniqueId());

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_PLAYER, player) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_SUCCESS, player))
                hidePlayers(player);
        }
    }

    public static void removeHidePlayer(Player p) {
        if (!liveHidePlayerList.contains(p.getUniqueId())) return;
        liveHidePlayerList.remove(p.getUniqueId());

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.showPlayer(p);
            hidePlayers(player);
            MainAPI.hidePlayers(player);
        }
    }


    public enum Objective {
        // サイドバー
        SIDEBAR("Toso_Sidebar"),

        // サイドバー 表示用チーム
        SIDEBAR_STATUS("T_SB_Status"),
        SIDEBAR_TEAM("T_SB_Team"),
        SIDEBAR_DIFFICULTY("T_SB_Difficulty"),
        SIDEBAR_REWARD("T_SB_Reward"),
        SIDEBAR_RATE("T_SB_Rate"),

        SIDEBAR_TEAM_PLAYER("T_SB_T_Player"),
        SIDEBAR_TEAM_HUNTER("T_SB_T_Hunter"),
        SIDEBAR_TEAM_JAIL("T_SB_T_Jail"),
        SIDEBAR_TEAM_SUCCESS("TSB_T_Success"),
        SIDEBAR_TEAM_TUHO("T_SB_T_Tuho"),

        // チーム
        TEAM_ADMIN("Toso_Admin"),
        TEAM_PLAYER("Toso_Player"),
        TEAM_HUNTER("Toso_Hunter"),
        TEAM_JAIL("Toso_Jail"),
        TEAM_SUCCESS("Toso_Success"),
        TEAM_TUHO("Toso_Tuho");

        private final String name;

        private Objective(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}

package jp.aoichaan0513.A_TosoGame_Live.API.Manager;

import jp.aoichaan0513.A_TosoGame_Live.API.Listener.MissionEndEvent;
import jp.aoichaan0513.A_TosoGame_Live.API.Listener.MissionStartEvent;
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.Inventory.Right.MissionInventory;
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.World.WorldConfig;
import jp.aoichaan0513.A_TosoGame_Live.API.Scoreboard.Teams;
import jp.aoichaan0513.A_TosoGame_Live.Main;
import jp.aoichaan0513.A_TosoGame_Live.Mission.HunterZone;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.*;
import java.util.Arrays;

public class MissionManager {

    // private static HashMap<String, String> bookList = new HashMap<>();
    private static BossBar bossBar = null;

    private static MissionType missionType = MissionType.MISSION_NONE;
    private static MissionState missionState = MissionState.NONE;

    public static boolean isMission(MissionState missionState) {
        return MissionManager.missionState == missionState;
    }

    public static boolean isMission() {
        return missionState.equals(MissionState.MISSION);
    }

    public static MissionState getMissionState() {
        return missionState;
    }

    public static void setMissionState(MissionState missionState) {
        MissionManager.missionState = missionState;
    }

    public static boolean isBossBar() {
        return bossBar != null;
    }

    public static BossBar getBossBar() {
        return bossBar;
    }

    public static void resetBossBar() {
        if (bossBar != null)
            bossBar.removeAll();
        bossBar = null;
    }

    public static MissionType getMission() {
        return missionType;
    }

    public static void resetMission() {
        missionType = MissionType.MISSION_NONE;
        return;
    }

    public static ItemStack addBook(Player p) {
        ItemStack itemStack = new ItemStack(Material.BOOK);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
        itemMeta.addEnchant(Enchantment.DURABILITY, 1, true);
        itemMeta.setDisplayName("" + ChatColor.BOLD + Main.PHONE_ITEM_NAME);
        itemMeta.setLore(
                Arrays.asList(
                        "" + ChatColor.GOLD + ChatColor.UNDERLINE + "左クリック" + ChatColor.RESET + ChatColor.GRAY + ": " + ChatColor.YELLOW + "ホーム画面",
                        "" + ChatColor.GOLD + ChatColor.UNDERLINE + "右クリック" + ChatColor.RESET + ChatColor.GRAY + ": " + ChatColor.YELLOW + "ミッションアプリ"
                )
        );
        itemStack.setItemMeta(itemMeta);

        p.getInventory().addItem(itemStack);
        p.updateInventory();
        return hasBook(p);
    }

    public static ItemStack hasBook(Player p) {
        for (ItemStack item : p.getInventory().getContents())
            if (item != null && item.getType() == Material.BOOK)
                return item;
        return addBook(p);
    }

    public static void reloadBook(Player p) {
        if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_HUNTER, p) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_TUHO, p))
            return;
        hasBook(p);
        p.updateInventory();
        return;
    }

    public static boolean hasFile(int id) {
        File file = new File("plugins" + Main.FILE_SEPARATOR + "A_TosoGame_Live" + Main.FILE_SEPARATOR + "missions" + Main.FILE_SEPARATOR + id + ".txt");
        return file.exists();
    }

    public static void sendFileMission(int id) {
        if (MissionManager.hasFile(id)) {
            MissionManager.missionType = MissionType.getMission(id);

            if (id == 0) {
                sendFileMissionAPI(id, Material.EMERALD_BLOCK, BarColor.GREEN);
                return;
            } else if (id == 1) {
                sendFileMissionAPI(id, Material.GOLD_BLOCK, BarColor.YELLOW);
                return;
            } else if (id == 2) {
                HunterZone.start();
                sendFileMissionAPI(id, Material.BONE_BLOCK, BarColor.BLUE);
                return;
            } else {
                sendFileMissionAPI(id, Material.QUARTZ_BLOCK, BarColor.WHITE);
                return;
            }
        }
        return;
    }

    public static void sendFileMission(int id, Player p) {
        if (MissionManager.hasFile(id)) {
            MissionManager.missionType = MissionType.getMission(id);

            if (id == 0) {
                sendFileMissionAPI(id, Material.EMERALD_BLOCK, BarColor.GREEN);
                return;
            } else if (id == 1) {
                sendFileMissionAPI(id, Material.GOLD_BLOCK, BarColor.YELLOW);
                return;
            } else if (id == 2) {
                HunterZone.start();
                sendFileMissionAPI(id, Material.BONE_BLOCK, BarColor.BLUE);
                return;
            } else {
                sendFileMissionAPI(id, Material.QUARTZ_BLOCK, BarColor.WHITE);
                return;
            }
        }
        return;
    }

    private static void sendFileMissionAPI(int id, Material material, BarColor color) {
        try {
            File file = new File("plugins" + Main.FILE_SEPARATOR + "A_TosoGame_Live" + Main.FILE_SEPARATOR + "missions" + Main.FILE_SEPARATOR + id + ".txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), System.getProperty("os.name").toLowerCase().startsWith("windows") ? "Shift-JIS" : "UTF-8"));

            String title = "";
            String content = "";
            String line = br.readLine();
            while (line != null) {
                if (title == "") {
                    title = line;
                } else {
                    content += line + "\n";
                }
                line = br.readLine();
            }
            br.close();

            bossBar = Bukkit.createBossBar(ChatColor.BOLD + title, color, BarStyle.SOLID);
            for (Player player : Bukkit.getOnlinePlayers())
                if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_ADMIN, player) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_PLAYER, player) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_JAIL, player) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_SUCCESS, player))
                    bossBar.addPlayer(player);

            WorldConfig worldConfig = Main.getWorldConfig();
            if (worldConfig.getGameConfig().getScript()) {
                MissionStartEvent event = new MissionStartEvent(id);
                Bukkit.getPluginManager().callEvent(event);
            }
            setMissionState(MissionState.MISSION);
            MissionManager.sendMission(title, content.trim() + ChatColor.DARK_GRAY, MissionBookType.MISSION, material);
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public static void sendMission(String title, String description, MissionBookType type, Material material) {
        MissionInventory.addMission(title, description, type, material);
        return;
    }

    public static void endMission() {
        if (isMission()) {
            WorldConfig worldConfig = Main.getWorldConfig();
            if (worldConfig.getGameConfig().getScript()) {
                MissionEndEvent event = new MissionEndEvent(missionType.getId());
                Bukkit.getPluginManager().callEvent(event);
            }

            HunterZone.end();

            bossBar.removeAll();

            resetBossBar();
            resetMission();

            MissionInventory.endMission();

            setMissionState(MissionState.NONE);
            return;
        }
        return;
    }

    /*
    public static void sendMission(String missionTitle, String missionContent) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (!Teams.Toso_hunter.hasEntry(p.getName()) || !Teams.Toso_tuho.hasEntry(p.getName())) {
                ItemStack item = hasBook(p);
                BookMeta meta = (BookMeta) item.getItemMeta();
                meta.addPage(ChatColor.DARK_RED + missionTitle + ChatColor.RESET + "\n\n" + missionContent);
                item.setItemMeta(meta);
                p.updateInventory();
            }
        }
        bookList.put(missionTitle, missionContent);
        setMissionState(MissionState.MISSIONInventory);
        return;
    }

    public static void sendTutatu(String tutatuContent) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (!Teams.Toso_hunter.hasEntry(p.getName()) || !Teams.Toso_tuho.hasEntry(p.getName())) {
                ItemStack item = hasBook(p);
                BookMeta meta = (BookMeta) item.getItemMeta();
                meta.addPage(ChatColor.DARK_RED + "通達" + ChatColor.RESET + "\n\n" + tutatuContent);
                item.setItemMeta(meta);
                p.updateInventory();
            }
        }
        bookList.put("通達", tutatuContent);
        return;
    }

    public static void sendHint(String hintContent) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (!Teams.Toso_hunter.hasEntry(p.getName()) || !Teams.Toso_tuho.hasEntry(p.getName())) {
                ItemStack item = hasBook(p);
                BookMeta meta = (BookMeta) item.getItemMeta();
                meta.addPage(ChatColor.DARK_RED + "ヒント" + ChatColor.RESET + "\n\n" + hintContent);
                item.setItemMeta(meta);
                p.updateInventory();
            }
        }
        bookList.put("ヒント", hintContent);
        return;
    }
    */

    public enum MissionState {
        MISSION,
        NONE
    }

    public enum MissionBookType {
        MISSION(1, "ミッション"),
        TUTATU(2, "通達"),
        HINT(2, "ヒント"),
        END_MISSION(3, "ミッション (終了)"),
        END_TUTATU(3, "通達 (終了)"),
        END_HINT(3, "ヒント (ミッション)"),
        NONE(-1, "");

        private final int id;
        private final String name;

        private MissionBookType(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }

    public enum MissionType {
        MISSION_SUCCESS(0),
        MISSION_AREA(1),
        MISSION_HUNTER_ZONE(2),
        MISSION_NONE(-1);

        private final int id;

        private MissionType(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

        public static MissionType getMission(int id) {
            for (MissionType missionType : MissionType.values())
                if (missionType.getId() == id)
                    return missionType;
            return MissionType.MISSION_NONE;
        }
    }
}

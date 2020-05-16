package jp.aoichaan0513.A_TosoGame_Live;

import jp.aoichaan0513.A_TosoGame_Live.API.HttpSendJSON;
import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI;
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.ActionBarManager;
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.BossBarManager;
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.GameManager;
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.MissionManager;
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.World.WorldConfig;
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.World.WorldManager;
import jp.aoichaan0513.A_TosoGame_Live.API.Maps.MapUtility;
import jp.aoichaan0513.A_TosoGame_Live.API.Scoreboard.ScoreBoard;
import jp.aoichaan0513.A_TosoGame_Live.API.Scoreboard.Teams;
import jp.aoichaan0513.A_TosoGame_Live.API.Timer.TimerFormat;
import jp.aoichaan0513.A_TosoGame_Live.API.TosoGameAPI;
import jp.aoichaan0513.A_TosoGame_Live.Commands.Command.Location;
import jp.aoichaan0513.A_TosoGame_Live.Commands.Command.*;
import jp.aoichaan0513.A_TosoGame_Live.Commands.ICommand;
import jp.aoichaan0513.A_TosoGame_Live.Listeners.*;
import jp.aoichaan0513.A_TosoGame_Live.Runnable.RespawnRunnable;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public final class Main extends JavaPlugin implements Listener {

    private static JavaPlugin plugin;
    private static String channel = "Dev";

    public static final String PHONE_ITEM_NAME = "スマートフォン";

    public static final String FILE_SEPARATOR = FileSystems.getDefault().getSeparator();
    public static final String PACKAGE_PATH = "jp.aoichaan0513.A_TosoGame_Live";

    private static FileConfiguration mainConfig;
    private static WorldConfig worldConfig;

    // オープニングゲーム参加者リスト
    public static ArrayList<Player> playerList = new ArrayList<>();

    // ハンター・通報部隊抽選応募リスト
    public static ArrayList<Player> shuffleList = new ArrayList<>();

    // 姿非表示中プレイヤーリスト
    public static ArrayList<UUID> invisibleList = new ArrayList<>();

    private static HashMap<String, ICommand> commands = new HashMap<>();
    private static List<Listener> listeners = new ArrayList<>();


    @Override
    public void onEnable() {
        plugin = this;
        saveDefaultConfig();

        mainConfig = getConfig();

        loadConfig();
        loadCommand();
        loadBlockedCommand();
        loadListener();
        loadWorld();
        // loadMap();

        Teams.setScoreboard();

        for (Player player : Bukkit.getOnlinePlayers()) {
            TosoGameAPI.difficultyMap.put(player.getUniqueId(), WorldManager.Difficulty.NORMAL);
            if (!ScoreBoard.getBoardMap().containsKey(player.getUniqueId()))
                ScoreBoard.setBoard(player);
        }

        new BukkitRunnable() {

            @Override
            public void run() {
                ScoreBoard.updateScoreboard();

                boolean isGame = GameManager.isGame();
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (isGame) {
                        if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_PLAYER, player) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_SUCCESS, player)) {
                            player.setFoodLevel(16);
                        } else {
                            player.setWalkSpeed(0.2f);
                            player.setFoodLevel(20);
                        }
                    } else {
                        player.setWalkSpeed(0.2f);
                        player.setFoodLevel(20);
                    }
                }
            }
        }.runTaskTimerAsynchronously(getInstance(), 0, 0);

        new RespawnRunnable().runTaskTimerAsynchronously(getInstance(), 0, 20);

        /*
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers())
                    TosoGameAPI.showPlayers(player);
            }
        }.runTaskTimer(getInstance(), 0, 20);
        */

        // download();

        sendActionBar();
    }

    @Override
    public void onDisable() {
        Teams.resetScoreboard();

        BossBarManager.resetBar();
        MissionManager.resetBossBar();
    }

    // インスタンス取得
    public static JavaPlugin getInstance() {
        return plugin;
    }

    public static FileConfiguration getMainConfig() {
        return mainConfig;
    }

    public static FileConfiguration setMainConfig() {
        getInstance().saveConfig();
        mainConfig = getInstance().getConfig();
        return mainConfig;
    }

    public static WorldConfig getWorldConfig() {
        return worldConfig;
    }

    public static void setWorldConfig(WorldConfig worldConfig) {
        Main.worldConfig = worldConfig;
    }

    // チャンネル取得
    private String getChannel() {
        return channel;
    }

    public static void loadConfig() {
        loadFolder("updates");
        loadFolder("players");
        loadFolder("missions");
        loadFolder("scripts");
        loadFolder("scripts" + FILE_SEPARATOR + "commands");
        loadFolder("scripts" + FILE_SEPARATOR + "missions");

        loadFile("scripts" + FILE_SEPARATOR + "missions", "template.js");
        loadFile("scripts" + FILE_SEPARATOR + "missions", "0.js");

        loadBuiltinMission("0.txt");
        loadBuiltinMission("1.txt");
        loadBuiltinMission("2.txt");
        return;
    }

    private void loadCommand() {
        Bukkit.getConsoleSender().sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "コマンドを読み込んでいます…");
        commands = new HashMap<String, ICommand>() {{
            // メインコマンド
            put("toso", new Toso("toso"));

            // ゲーム進行コマンド (運営用)
            put("start", new Start("start")); // コマンドブロック対応
            put("end", new End("end")); // コマンドブロック対応
            put("reset", new Reset("reset")); // コマンドブロック対応
            put("mission", new Mission("mission")); // コマンドブロック対応
            put("hunter", new Hunter("hunter")); // コマンドブロック対応
            put("tuho", new Tuho("tuho")); // コマンドブロック対応
            put("player", new jp.aoichaan0513.A_TosoGame_Live.Commands.Command.Player("player")); // コマンドブロック対応

            // ゲーム進行コマンド (プレイヤー用)
            put("h", new H("h"));
            put("t", new T("t"));

            // ミッション用コマンド
            put("code", new Code("code"));

            // オープニングゲーム用コマンド
            put("opgame", new OPGame("opgame")); // コマンドブロック対応
            put("shuffle", new Shuffle("shuffle"));

            // 設定用コマンド
            put("location", new Location("location"));
            put("map", new Map("map"));

            // ユーティリティコマンド
            put("btp", new Btp("btp"));
            put("phone", new Phone("phone"));

            // プレイヤー用コマンド
            put("join", new Join("join")); // コマンドブロック対応
            put("leave", new Leave("leave")); // コマンドブロック対応
            put("broadcaster", new BroadCaster("broadcaster")); // コマンドブロック対応
            put("disappear", new Disappear("disappear")); // コマンドブロック対応
            put("appear", new Appear("appear")); // コマンドブロック対応
            put("hide", new Hide("hide"));
            put("show", new Show("show"));
            put("spec", new Spec("spec"));
            put("sidebar", new Sidebar("sidebar"));

            // スクリプトコマンド
            put("script", new Script("script"));

            // 特に意味がないコマンド
            put("nick", new Nick("nick"));
            put("ride", new Ride("ride"));
        }};

        for (java.util.Map.Entry<String, ICommand> entry : commands.entrySet()) {
            getCommand(entry.getKey()).setExecutor(entry.getValue());
        }
        Bukkit.getConsoleSender().sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "コマンドを" + ChatColor.GREEN + ChatColor.UNDERLINE + commands.size() + "件" + ChatColor.RESET + ChatColor.GRAY + "読み込みました。");
        return;
    }

    private void loadBlockedCommand() {
        onCommand.addBlockCommand("gamemode");
        onCommand.addBlockCommand("tell");
        onCommand.addBlockCommand("w");
        onCommand.addBlockCommand("r");
        onCommand.addBlockCommand("m");
        onCommand.addBlockCommand("me");
        onCommand.addBlockCommand("msg");
        return;
    }

    private void loadListener() {
        Bukkit.getConsoleSender().sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "リスナーを読み込んでいます…");
        listeners = new ArrayList<Listener>() {{
            // 参加・退出系
            add(new onJoin());
            add(new onQuit());

            // クリック・ダメージ系
            add(new onInteract());
            add(new onDamage());

            // 移動系
            add(new onMove());
            add(new onVehicle());

            // チャット・コマンド系
            add(new onChat());
            add(new onCommand());

            // インベントリ系
            add(new onItemHeld());
            add(new onInventory());
            add(new onInventoryGui());

            // エンティティ系
            add(new onEntity());

            // アイテム系
            add(new onDrop());
            add(new onPickup());

            // ブロック系
            add(new onBreak());
            add(new onSign());

            // ミッション系 (独自)
            add(new onMissionStart());
            add(new onMissionEnd());
        }};

        for (Listener listener : listeners)
            Bukkit.getPluginManager().registerEvents(listener, getInstance());
        Bukkit.getConsoleSender().sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "リスナーを" + ChatColor.GREEN + ChatColor.UNDERLINE + listeners.size() + "件" + ChatColor.RESET + ChatColor.GRAY + "読み込みました。");
        return;
    }

    private void loadWorld() {
        if (!WorldManager.getWorldName().startsWith("world")) {
            Bukkit.getConsoleSender().sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "ワールドを読み込んでいます…");
            World world = Bukkit.createWorld(new WorldCreator(WorldManager.getWorldName()));

            world.setDifficulty(Difficulty.EASY);
            world.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
            world.setGameRule(GameRule.DO_MOB_SPAWNING, false);

            worldConfig = new WorldConfig(world);

            Bukkit.getConsoleSender().sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SUCCESS) + "ワールドを読み込みました。");
            return;
        } else {
            World world = WorldManager.getWorld();

            world.setDifficulty(Difficulty.EASY);
            world.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
            world.setGameRule(GameRule.DO_MOB_SPAWNING, false);

            worldConfig = new WorldConfig(world);
            return;
        }
    }

    private void download() {
        String urlStr = "https://incha.work/services/files/plugins/org/aoichaan0513/a_tosogame_live/";
        String strPostUrl = urlStr + "api";

        Bukkit.getConsoleSender().sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "更新を確認しています…");
        String JSON = "{\"channel\":\"" + getChannel() + "\", \"current\":\"" + getDescription().getVersion().split("-")[1] + "\"}";
        HttpSendJSON httpSendJSON = new HttpSendJSON();
        String result = httpSendJSON.callPost(strPostUrl, JSON);
        try {
            JSONObject jsonObject = new JSONObject(result);
            if (jsonObject.getBoolean("result")) {
                Bukkit.getConsoleSender().sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.WARNING) + "更新が見つかりました。\n" +
                        MainAPI.getPrefix(MainAPI.PrefixType.WARNING) + "プラグインチャンネル: " + getChannel() + "\n" +
                        MainAPI.getPrefix(MainAPI.PrefixType.WARNING) + "現在のバージョン: " + getDescription().getVersion().split("-")[1] + "\n" +
                        MainAPI.getPrefix(MainAPI.PrefixType.WARNING) + "最新のバージョン: " + jsonObject.getString("latest") + "\n" +
                        MainAPI.getPrefix(MainAPI.PrefixType.WARNING) + "最新のバージョンをダウンロードしています…");
                try {
                    URL url = new URL(urlStr + jsonObject.getString("file"));

                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setAllowUserInteraction(false);
                    conn.setInstanceFollowRedirects(true);
                    conn.setRequestMethod("GET");
                    conn.connect();

                    int httpStatusCode = conn.getResponseCode();

                    if (httpStatusCode != HttpURLConnection.HTTP_OK) {
                        throw new Exception();
                    }

                    // Input Stream
                    DataInputStream dataInStream = new DataInputStream(conn.getInputStream());

                    // Output Stream
                    DataOutputStream dataOutStream = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(getDataFolder() + FILE_SEPARATOR + "updates" + FILE_SEPARATOR + jsonObject.getString("file"))));

                    // Read Data
                    byte[] b = new byte[4096];
                    int readByte = 0;

                    while (-1 != (readByte = dataInStream.read(b))) {
                        dataOutStream.write(b, 0, readByte);
                    }

                    // Close Stream
                    dataInStream.close();
                    dataOutStream.close();
                    Bukkit.getConsoleSender().sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SUCCESS) + "最新のバージョンをダウンロードしました。\n" +
                            MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "プラグインを最新のバージョンに置き換えています…");

                    try {
                        Path sourcePath = Paths.get(getDataFolder() + FILE_SEPARATOR + "updates" + FILE_SEPARATOR + jsonObject.getString("file"));
                        Path targetPath = Paths.get(getServer().getWorldContainer().getAbsolutePath() + FILE_SEPARATOR + "plugins" + FILE_SEPARATOR + jsonObject.getString("file"));
                        Files.move(sourcePath, targetPath);
                        Bukkit.getConsoleSender().sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SUCCESS) + "最新のバージョンを移動しました。");

                        try {
                            Path targetPath2 = Paths.get(getServer().getWorldContainer().getAbsolutePath() + FILE_SEPARATOR + "plugins" + FILE_SEPARATOR + "A_TosoGame_Live-" + getDescription().getVersion() + ".jar");
                            if (Files.deleteIfExists(targetPath2)) {
                                Bukkit.getConsoleSender().sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SUCCESS) + "現在のバージョンを削除しました。\n" +
                                        MainAPI.getPrefix(MainAPI.PrefixType.WARNING) + "サーバーを再起動しています…");
                                getServer().reload();
                            } else {
                                Bukkit.getConsoleSender().sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "ファイルがありませんでした。");
                            }

                        } catch (IOException e) {
                            Bukkit.getConsoleSender().sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "予期しないエラーが発生しました。");
                            e.printStackTrace();
                            return;
                        }
                    } catch (IOException e) {
                        Bukkit.getConsoleSender().sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "予期しないエラーが発生しました。");
                        e.printStackTrace();
                        return;
                    }
                    return;
                } catch (Exception e) {
                    Bukkit.getConsoleSender().sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "予期しないエラーが発生しました。");
                    e.printStackTrace();
                    return;
                }
            } else {
                Bukkit.getConsoleSender().sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SUCCESS) + "更新はありませんでした。");
                return;
            }
        } catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "予期しないエラーが発生しました。");
            e.printStackTrace();
            return;
        }
    }

    private static void loadFile(String folderName, String fileName) {
        File file = new File(getInstance().getDataFolder() + FILE_SEPARATOR + folderName, fileName);
        if (!file.exists()) {
            Bukkit.getConsoleSender().sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "ファイル  \"" + fileName + "\" を作成します…");
            getInstance().saveResource(folderName + FILE_SEPARATOR + fileName, false);
            Bukkit.getConsoleSender().sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SUCCESS) + "ファイル \"" + fileName + "\" を作成しました。");
        } else {
            Bukkit.getConsoleSender().sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "ファイル \"" + fileName + "\" が見つかったためスルーしました。");
        }
    }

    private static void loadFolder(String folderName) {
        File file = new File(getInstance().getDataFolder() + FILE_SEPARATOR + folderName);
        if (!file.exists()) {
            if (file.mkdir())
                Bukkit.getConsoleSender().sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SUCCESS) + "フォルダ \"" + folderName + "\" を作成しました。");
            else
                Bukkit.getConsoleSender().sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "フォルダ \"" + folderName + "\" を作成できませんでした。");
        } else {
            Bukkit.getConsoleSender().sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "フォルダ \"" + folderName + "\" が見つかったためスルーしました。");
        }
    }

    private static void loadBuiltinMission(String fileName) {
        File file = new File(getInstance().getDataFolder() + FILE_SEPARATOR + "missions", fileName);
        if (!file.exists()) {
            Bukkit.getConsoleSender().sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "ビルトインミッション \"" + fileName + "\" を作成します…");
            getInstance().saveResource("missions" + FILE_SEPARATOR + fileName, false);
            Bukkit.getConsoleSender().sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SUCCESS) + "ビルトインミッション \"" + fileName + "\" を作成しました。");
        } else {
            Bukkit.getConsoleSender().sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "ビルトインミッション \"" + fileName + "\" が見つかったためスルーしました。");
        }
    }

    private static void loadMap() {
        if (worldConfig.getConfig().contains("border.map.p1.x") && worldConfig.getConfig().contains("border.map.p1.z")
                && worldConfig.getConfig().contains("border.map.p2.x") && worldConfig.getConfig().contains("border.map.p2.z")) {
            Bukkit.getConsoleSender().sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "地図を生成しています…");
            if (MapUtility.generateMap())
                Bukkit.getConsoleSender().sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SUCCESS) + "地図の生成が完了しました。");
            else
                Bukkit.getConsoleSender().sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "地図の生成ができませんでした。");
        } else {
            Bukkit.getConsoleSender().sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "マップの設定が完了していないため地図の生成ができませんでした。\n" +
                    MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "マップの設定をした後に\"/map generate\"を実行してください。");
        }
    }

    private void sendActionBar() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers())
                    if (MainAPI.isHidePlayer(player))
                        ActionBarManager.sendActionBar(player, ChatColor.RED + "あなたの姿は非表示になっています。");

                if (GameManager.isGame()) {
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_PLAYER, player) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_SUCCESS, player)) {
                            if (player.hasPotionEffect(PotionEffectType.INVISIBILITY) && player.hasPotionEffect(PotionEffectType.SPEED))
                                ActionBarManager.sendActionBar(player, "" + ChatColor.AQUA + ChatColor.UNDERLINE + "透明化" + ChatColor.RESET + ChatColor.GRAY + "終了まで残り" + ChatColor.RED + ChatColor.UNDERLINE + TimerFormat.formatSec(player.getPotionEffect(PotionEffectType.INVISIBILITY).getDuration() / 20) + "秒" + ChatColor.RESET + ChatColor.GRAY + " / " + ChatColor.BLUE + ChatColor.UNDERLINE + "移動速度上昇" + ChatColor.RESET + ChatColor.GRAY + "終了まで残り" + ChatColor.RED + ChatColor.UNDERLINE + TimerFormat.formatSec(player.getPotionEffect(PotionEffectType.SPEED).getDuration() / 20) + "秒");
                            else if (player.hasPotionEffect(PotionEffectType.INVISIBILITY))
                                ActionBarManager.sendActionBar(player, "" + ChatColor.AQUA + ChatColor.UNDERLINE + "透明化" + ChatColor.RESET + ChatColor.GRAY + "終了まで残り" + ChatColor.RED + ChatColor.UNDERLINE + TimerFormat.formatSec(player.getPotionEffect(PotionEffectType.INVISIBILITY).getDuration() / 20) + "秒");
                            else if (player.hasPotionEffect(PotionEffectType.SPEED))
                                ActionBarManager.sendActionBar(player, "" + ChatColor.BLUE + ChatColor.UNDERLINE + "移動速度上昇" + ChatColor.RESET + ChatColor.GRAY + "終了まで残り" + ChatColor.RED + ChatColor.UNDERLINE + TimerFormat.formatSec(player.getPotionEffect(PotionEffectType.SPEED).getDuration() / 20) + "秒");
                        }
                        if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_JAIL, player) && player.hasPotionEffect(PotionEffectType.FIRE_RESISTANCE)) {
                            ActionBarManager.sendActionBar(player, ChatColor.GRAY + "復活可能まで残り" + ChatColor.RED + ChatColor.UNDERLINE + TimerFormat.formatJapan(player.getPotionEffect(PotionEffectType.FIRE_RESISTANCE).getDuration() / 20));
                        }
                    }
                }
            }
        }.runTaskTimerAsynchronously(getInstance(), 0, 20);
    }
}

package jp.aoichaan0513.A_TosoGame_Live.API;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.URL;
import java.util.*;
import java.util.regex.Pattern;

public class MainAPI {

    public static int makePositive(int i) {
        return i < 0 ? 0 : i;
    }

    /**
     * プレイヤー非表示関連
     */
    private static List<UUID> hidePlayerList = new ArrayList<>();

    public static void hidePlayers(Player p) {
        for (UUID uuid : hidePlayerList) {
            Player player = Bukkit.getPlayer(uuid);

            if (player == null) continue;
            p.hidePlayer(player);
        }
    }

    public static List<UUID> getHidePlayerList() {
        return hidePlayerList;
    }

    public static boolean isHidePlayer(Player p) {
        return hidePlayerList.contains(p.getUniqueId());
    }

    public static void addHidePlayer(Player p) {
        addHidePlayer(p, true);
    }

    public static void addHidePlayer(Player p, boolean b) {
        if (hidePlayerList.contains(p.getUniqueId())) return;
        hidePlayerList.add(p.getUniqueId());

        if (b)
            for (Player player : Bukkit.getOnlinePlayers())
                hidePlayers(player);
    }

    public static void removeHidePlayer(Player p) {
        removeHidePlayer(p, true);
    }

    public static void removeHidePlayer(Player p, boolean b) {
        if (!hidePlayerList.contains(p.getUniqueId())) return;
        hidePlayerList.remove(p.getUniqueId());

        if (b) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.showPlayer(p);
                hidePlayers(player);
            }
        }
    }

    /**
     * 権限関係
     */
    public static boolean isAdmin(CommandSender sender) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            return isAdmin(p);
        } else {
            return true;
        }
    }

    public static boolean isAdmin(Player p) {
        return p.isOp() || p.hasPermission("a_core.*");
    }

    /**
     * プレイヤー系
     */
    public static boolean isOnlinePlayer(Player p) {
        return p != null;
    }

    public static boolean isOnlinePlayer(OfflinePlayer p) {
        return p != null;
    }

    public static boolean isOnlinePlayer(String name) {
        Player target = Bukkit.getPlayerExact(name);
        return target != null;
    }

    public static boolean isPlayed(Player p) {
        return p.getPlayer() != null || p.hasPlayedBefore();
    }

    public static boolean isPlayed(OfflinePlayer p) {
        return p.getPlayer() != null || p.hasPlayedBefore();
    }

    /**
     * アドレス系
     */
    public static int getRegion(InetSocketAddress address) {
        try {
            URL url = new URL("http://ip-api.com/json/" + address.getHostName());
            BufferedReader stream = new BufferedReader(new InputStreamReader(
                    url.openStream()));
            StringBuilder entirePage = new StringBuilder();
            String inputLine;
            while ((inputLine = stream.readLine()) != null)
                entirePage.append(inputLine);
            stream.close();

            final String type = AddressAPIType.REGION.getName();

            JSONObject jsonObject = new JSONObject(entirePage.toString());
            if (jsonObject.has(type))
                return jsonObject.getInt(type);
            else
                return -1;
        } catch (Exception e) {
            return -1;
        }
    }

    public static String getRegionName(InetSocketAddress address) {
        try {
            URL url = new URL("http://ip-api.com/json/" + address.getHostName());
            BufferedReader stream = new BufferedReader(new InputStreamReader(
                    url.openStream()));
            StringBuilder entirePage = new StringBuilder();
            String inputLine;
            while ((inputLine = stream.readLine()) != null)
                entirePage.append(inputLine);
            stream.close();

            final String type = AddressAPIType.REGION_NAME.getName();

            JSONObject jsonObject = new JSONObject(entirePage.toString());
            if (jsonObject.has(type))
                return jsonObject.getString(type);
            else
                return "N/A";
        } catch (Exception e) {
            return "N/A";
        }
    }

    public static String getCountry(InetSocketAddress address) {
        try {
            URL url = new URL("http://ip-api.com/json/" + address.getHostName());
            BufferedReader stream = new BufferedReader(new InputStreamReader(
                    url.openStream()));
            StringBuilder entirePage = new StringBuilder();
            String inputLine;
            while ((inputLine = stream.readLine()) != null)
                entirePage.append(inputLine);
            stream.close();

            final String type = AddressAPIType.COUNTRY.getName();

            JSONObject jsonObject = new JSONObject(entirePage.toString());
            if (jsonObject.has(type))
                return jsonObject.getString(type);
            else
                return "N/A";
        } catch (Exception e) {
            return "N/A";
        }
    }

    public static String getCountryCode(InetSocketAddress address) {
        try {
            URL url = new URL("http://ip-api.com/json/" + address.getHostName());
            BufferedReader stream = new BufferedReader(new InputStreamReader(
                    url.openStream()));
            StringBuilder entirePage = new StringBuilder();
            String inputLine;
            while ((inputLine = stream.readLine()) != null)
                entirePage.append(inputLine);
            stream.close();

            final String type = AddressAPIType.COUNTRY_CODE.getName();

            JSONObject jsonObject = new JSONObject(entirePage.toString());
            if (jsonObject.has(type))
                return jsonObject.getString(type);
            else
                return "N/A";
        } catch (Exception e) {
            return "N/A";
        }
    }

    public enum AddressAPIType {
        REGION("region"),
        REGION_NAME("regionName"),
        COUNTRY("country"),
        COUNTRY_CODE("countryCode");

        private final String name;

        private AddressAPIType(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    /**
     * プレースホルダー
     */
    public static String setPlaceholder(String str, Player player) {
        HashMap<Pattern, String> hashMap = new HashMap<Pattern, String>() {
            {
                put(Pattern.compile("%player:name%"), player.getName());
                put(Pattern.compile("%player:uuid%"), player.getUniqueId().toString());
                put(Pattern.compile("%player:locale%"), player.getLocale());
                put(Pattern.compile("%player:address%"), player.getAddress().getAddress().getHostAddress());
                put(Pattern.compile("%player:displayName%"), player.getDisplayName());
                put(Pattern.compile("%player:tabListName%"), player.getPlayerListName());
                put(Pattern.compile("%country%"), getCountry(player.getAddress()));
                put(Pattern.compile("%country%"), getCountry(player.getAddress()));
                put(Pattern.compile("%countryCode%"), getCountryCode(player.getAddress()));
                put(Pattern.compile("%region%"), String.valueOf(getRegion(player.getAddress())));
                put(Pattern.compile("%regionName%"), getRegionName(player.getAddress()));
            }
        };

        String s = str;
        for (Map.Entry<Pattern, String> entry : hashMap.entrySet())
            s = entry.getKey().matcher(s).replaceFirst(entry.getValue());

        return s;
    }

    /**
     * プレフィックス系
     */
    public static String getPrefix() {
        return ChatColor.GRAY + "[" + ChatColor.DARK_RED + "逃走中" + ChatColor.GRAY + "]" + ChatColor.RESET;
    }

    public static String getPrefix(PrefixType prefixType) {
        return getPrefix(prefixType.getBackColor(), prefixType.getForwardColor());
    }

    public static String getPrefix(ChatColor color) {
        return getPrefix(color, color);
    }

    public static String getPrefix(ChatColor backColor, ChatColor forwardColor) {
        return getPrefix() + backColor + " > " + forwardColor;
    }

    public static void sendMessage(CommandSender sender, ErrorMessage errorMessage) {
        errorMessage.sendMessage(sender);
    }

    public enum PrefixType {
        PRIMARY(ChatColor.BLUE),
        SECONDARY(ChatColor.GRAY),
        SUCCESS(ChatColor.GREEN),
        WARNING(ChatColor.YELLOW, ChatColor.GOLD),
        ERROR(ChatColor.RED);

        private final ChatColor backColor;
        private final ChatColor forwardColor;

        private PrefixType(ChatColor backColor) {
            this.backColor = backColor;
            this.forwardColor = backColor;
        }

        private PrefixType(ChatColor backColor, ChatColor forwardColor) {
            this.backColor = backColor;
            this.forwardColor = forwardColor;
        }

        public ChatColor getBackColor() {
            return backColor;
        }

        public ChatColor getForwardColor() {
            return forwardColor;
        }
    }

    public enum ErrorMessage {
        ARGS(MainAPI.getPrefix(PrefixType.ERROR) + "引数が不正です。"),
        ARGS_INTEGER(MainAPI.getPrefix(PrefixType.ERROR) + "引数が不正です。整数を指定してください。"),
        ARGS_DECIMAL(MainAPI.getPrefix(PrefixType.ERROR) + "引数が不正です。小数を指定してください。"),
        ARGS_PLAYER(MainAPI.getPrefix(PrefixType.ERROR) + "引数が不正です。プレイヤー名を指定してください。"),

        PLAYER(MainAPI.getPrefix(PrefixType.ERROR) + "プレイヤーは実行できません。"),
        NOT_PLAYER(MainAPI.getPrefix(PrefixType.ERROR) + "プレイヤー以外は実行できません。"),

        PERMISSIONS(MainAPI.getPrefix(PrefixType.ERROR) + "権限がありません。"),
        PERMISSIONS_TEAM_ADMIN(MainAPI.getPrefix(PrefixType.ERROR) + "権限がありません。運営以外は実行できません。"),
        PERMISSIONS_TEAM_PLAYER(MainAPI.getPrefix(PrefixType.ERROR) + "権限がありません。逃走者以外は実行できません。"),
        PERMISSIONS_TEAM_HUNTER(MainAPI.getPrefix(PrefixType.ERROR) + "権限がありません。ハンター以外は実行できません。"),
        PERMISSIONS_TEAM_JAIL(MainAPI.getPrefix(PrefixType.ERROR) + "権限がありません。確保者以外は実行できません。"),
        PERMISSIONS_TEAM_SUCCESS(MainAPI.getPrefix(PrefixType.ERROR) + "権限がありません。生存者以外は実行できません。"),
        PERMISSIONS_TEAM_TUHO(MainAPI.getPrefix(PrefixType.ERROR) + "権限がありません。通報部隊以外は実行できません。"),

        EXCEPTION(MainAPI.getPrefix(PrefixType.ERROR) + "予期しないエラーが発生しました。", MainAPI.getPrefix(PrefixType.SECONDARY) + "詳細はコンソールを確認してください。"),

        GAME(MainAPI.getPrefix(PrefixType.ERROR) + "ゲーム中のため実行できません。"),
        NOT_GAME(MainAPI.getPrefix(PrefixType.ERROR) + "ゲーム中ではないため実行できません。");

        private final String[] messages;

        private ErrorMessage(String... messages) {
            this.messages = messages;
        }

        public String[] getMessages() {
            return messages;
        }

        public void sendMessage(CommandSender sender) {
            StringBuffer stringBuffer = new StringBuffer();
            for (String message : messages)
                stringBuffer.append(message + "\n");
            sender.sendMessage(stringBuffer.toString().trim());
        }
    }

    public enum Gamemode {
        SURVIVAL(GameMode.SURVIVAL, 0, "s", "サバイバル"),
        CREATIVE(GameMode.CREATIVE, 1, "c", "クリエイティブ"),
        ADVENTURE(GameMode.ADVENTURE, 2, "a", "アドベンチャー"),
        SPECTATOR(GameMode.SPECTATOR, 3, "sp", "スペクテイター");

        private final GameMode gameMode;
        private final int modeId;
        private final String modeAlias;
        private final String name;

        private Gamemode(GameMode gameMode, int modeId, String modeAlias, String name) {
            this.gameMode = gameMode;
            this.modeId = modeId;
            this.modeAlias = modeAlias;
            this.name = name;
        }

        public GameMode getGameMode() {
            return gameMode;
        }

        public int getModeId() {
            return modeId;
        }

        public String getModeAlias() {
            return modeAlias;
        }

        public String getName() {
            return name;
        }

        public static Gamemode getGamemode(GameMode gameMode) {
            for (Gamemode gamemode : Gamemode.values())
                if (gamemode.getGameMode() == gameMode)
                    return gamemode;
            return Gamemode.SURVIVAL;
        }
    }
}

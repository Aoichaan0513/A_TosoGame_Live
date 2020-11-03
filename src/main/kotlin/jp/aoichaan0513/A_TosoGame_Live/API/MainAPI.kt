package jp.aoichaan0513.A_TosoGame_Live.API

import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI.PrefixType
import jp.aoichaan0513.A_TosoGame_Live.Utils.isAdminTeam
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.GameMode
import org.bukkit.OfflinePlayer
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.InetSocketAddress
import java.net.URL
import java.util.*
import java.util.stream.IntStream
import kotlin.streams.toList

class MainAPI {
    companion object {

        fun makePositive(i: Int): Int {
            return i.coerceAtLeast(0)
        }

        fun getOnlinePlayers(collection: Collection<UUID>): Set<Player> {
            val set = mutableSetOf<Player>()
            collection.filter { Bukkit.getPlayer(it)?.isOnline ?: false }.forEach { set.add(Bukkit.getPlayer(it)!!) }
            return set
        }


        fun isPlayer(sender: CommandSender): Boolean {
            return sender is Player
        }

        /**
         * 権限関係
         */
        fun isAdmin(sender: CommandSender): Boolean {
            return sender !is Player || isAdmin(sender)
        }

        fun isAdmin(p: Player): Boolean {
            return p.isOp || p.hasPermission("a_tosogame_live.*")
        }

        /**
         * プレイヤー系
         */
        fun isOnline(p: Player?): Boolean {
            return p != null && p.isOnline
        }

        fun isOnline(p: OfflinePlayer?): Boolean {
            return p != null && p.isOnline
        }

        fun isOnline(name: String): Boolean {
            return isOnline(Bukkit.getPlayerExact(name))
        }

        fun isOnline(uuid: UUID): Boolean {
            return isOnline(Bukkit.getPlayer(uuid))
        }

        fun isPlayed(p: Player): Boolean {
            return p.player != null || p.hasPlayedBefore()
        }

        fun isPlayed(p: OfflinePlayer): Boolean {
            return p.player != null || p.hasPlayedBefore()
        }

        /**
         * アドレス系
         */
        fun getRegion(address: InetSocketAddress?): Int {
            return try {
                val url = URL("http://ip-api.com/json/${address!!.hostName}")
                val stream = BufferedReader(InputStreamReader(url.openStream()))

                val entirePage = StringBuilder()
                var inputLine: String?
                while (stream.readLine().also { inputLine = it } != null)
                    entirePage.append(inputLine)
                stream.close()

                val type = AddressAPIType.REGION.keyName

                val jsonObject = JSONObject(entirePage.toString())
                if (jsonObject.has(type)) jsonObject.getInt(type) else -1
            } catch (e: Exception) {
                -1
            }
        }

        fun getRegionName(address: InetSocketAddress?): String {
            return try {
                val url = URL("http://ip-api.com/json/${address!!.hostName}")
                val stream = BufferedReader(InputStreamReader(url.openStream()))

                val entirePage = StringBuilder()
                var inputLine: String?
                while (stream.readLine().also { inputLine = it } != null)
                    entirePage.append(inputLine)
                stream.close()

                val type = AddressAPIType.REGION_NAME.keyName

                val jsonObject = JSONObject(entirePage.toString())
                if (jsonObject.has(type)) jsonObject.getString(type) else "N/A"
            } catch (e: Exception) {
                "N/A"
            }
        }

        fun getCountry(address: InetSocketAddress?): String {
            return try {
                val url = URL("http://ip-api.com/json/${address!!.hostName}")
                val stream = BufferedReader(InputStreamReader(url.openStream()))

                val entirePage = StringBuilder()
                var inputLine: String?
                while (stream.readLine().also { inputLine = it } != null)
                    entirePage.append(inputLine)
                stream.close()

                val type = AddressAPIType.COUNTRY.keyName

                val jsonObject = JSONObject(entirePage.toString())
                if (jsonObject.has(type)) jsonObject.getString(type) else "N/A"
            } catch (e: Exception) {
                "N/A"
            }
        }

        fun getCountryCode(address: InetSocketAddress?): String {
            return try {
                val url = URL("http://ip-api.com/json/${address!!.hostName}")
                val stream = BufferedReader(InputStreamReader(url.openStream()))

                val entirePage = StringBuilder()
                var inputLine: String?
                while (stream.readLine().also { inputLine = it } != null)
                    entirePage.append(inputLine)
                stream.close()

                val type = AddressAPIType.COUNTRY_CODE.keyName

                val jsonObject = JSONObject(entirePage.toString())
                if (jsonObject.has(type)) jsonObject.getString(type) else "N/A"
            } catch (e: Exception) {
                "N/A"
            }
        }

        /**
         * メッセージ系
         */
        val prefix: String
            get() = "${ChatColor.GRAY}[${ChatColor.DARK_RED}逃走中${ChatColor.GRAY}]${ChatColor.RESET}"

        fun getPrefix(prefixType: PrefixType): String {
            return getPrefix(prefixType.backColor, prefixType.forwardColor)
        }

        fun getPrefix(color: ChatColor): String {
            return getPrefix(color, color)
        }

        fun getPrefix(backColor: ChatColor, forwardColor: ChatColor): String {
            return "$prefix$backColor > $forwardColor"
        }

        fun broadcastAdminMessage(str: String) {
            Bukkit.getOnlinePlayers().filter { it.isAdminTeam }.forEach { it.sendMessage(str) }
        }

        fun sendMessage(sender: CommandSender, errorMessage: ErrorMessage) {
            errorMessage.sendMessage(sender)
        }

        fun sendOfflineMessage(sender: CommandSender, name: String) {
            sender.sendMessage("${getPrefix(PrefixType.ERROR)}引数が不正です。${ChatColor.RED}${ChatColor.BOLD}${ChatColor.UNDERLINE}$name${ChatColor.RESET}${ChatColor.RED}はオフラインです。")
        }

        /**
         * その他
         */
        fun <T> divide(origin: List<T>?, size: Int): List<List<T>> {
            if (origin == null || origin.isEmpty() || size < 1)
                return Collections.emptyList()

            val block = origin.size / size + if (origin.size % size > 0) 1 else 0
            return IntStream.range(0, block).boxed()
                    .map { i ->
                        val start = i * size
                        val end = (start + size).coerceAtMost(origin.size)
                        origin.subList(start, end)
                    }.toList()
        }

        fun <K, V> divide(map: Map<K, V>?, size: Int): List<List<Map<K, V>>> {
            return if (map == null || map.isEmpty() || size < 1) {
                emptyList()
            } else {
                val dividedList = mutableListOf<List<Map<K, V>>>()

                divide(map.toList(), size).forEach {
                    val list = mutableListOf<Map<K, V>>()
                    it.forEach { list.add(mapOf(it)) }
                    dividedList.add(list)
                }

                dividedList
            }
        }

        fun <T> getValue(boolean: Boolean, v1: T, v2: T): T {
            return if (boolean) v1 else v2
        }
    }

    enum class AddressAPIType(val keyName: String) {
        REGION("region"),
        REGION_NAME("regionName"),
        COUNTRY("country"),
        COUNTRY_CODE("countryCode");
    }

    enum class PrefixType(val backColor: ChatColor, val forwardColor: ChatColor) {
        PRIMARY(ChatColor.BLUE),
        SECONDARY(ChatColor.GRAY),
        SUCCESS(ChatColor.GREEN),
        WARNING(ChatColor.YELLOW, ChatColor.GOLD),
        ERROR(ChatColor.RED);

        constructor(backColor: ChatColor) : this(backColor, backColor)
    }

    enum class ErrorMessage(vararg messages: String) {
        ARGS("${getPrefix(PrefixType.ERROR)}引数が不正です。"),
        ARGS_INTEGER("${getPrefix(PrefixType.ERROR)}引数が不正です。${ChatColor.RED}${ChatColor.BOLD}${ChatColor.UNDERLINE}整数${ChatColor.RESET}${ChatColor.RED}を指定してください。"),
        ARGS_DECIMAL("${getPrefix(PrefixType.ERROR)}引数が不正です。${ChatColor.RED}${ChatColor.BOLD}${ChatColor.UNDERLINE}小数${ChatColor.RESET}${ChatColor.RED}を指定してください。"),
        ARGS_PLAYER("${getPrefix(PrefixType.ERROR)}引数が不正です。${ChatColor.RED}${ChatColor.BOLD}${ChatColor.UNDERLINE}プレイヤー名${ChatColor.RESET}${ChatColor.RED}を指定してください。"),

        PLAYER("${getPrefix(PrefixType.ERROR)}プレイヤーは実行できません。"),
        NOT_PLAYER("${getPrefix(PrefixType.ERROR)}プレイヤー以外は実行できません。"),

        PERMISSIONS("${getPrefix(PrefixType.ERROR)}権限がありません。"),
        PERMISSIONS_TEAM_ADMIN("${getPrefix(PrefixType.ERROR)}権限がありません。${ChatColor.RED}${ChatColor.BOLD}${ChatColor.UNDERLINE}運営${ChatColor.RESET}${ChatColor.RED}以外は実行できません。"),
        PERMISSIONS_TEAM_PLAYER("${getPrefix(PrefixType.ERROR)}権限がありません。${ChatColor.RED}${ChatColor.BOLD}${ChatColor.UNDERLINE}逃走者${ChatColor.RESET}${ChatColor.RED}以外は実行できません。"),
        PERMISSIONS_TEAM_HUNTER("${getPrefix(PrefixType.ERROR)}権限がありません。${ChatColor.RED}${ChatColor.BOLD}${ChatColor.UNDERLINE}ハンター${ChatColor.RESET}${ChatColor.RED}以外は実行できません。"),
        PERMISSIONS_TEAM_JAIL("${getPrefix(PrefixType.ERROR)}権限がありません。${ChatColor.RED}${ChatColor.BOLD}${ChatColor.UNDERLINE}確保者${ChatColor.RESET}${ChatColor.RED}以外は実行できません。"),
        PERMISSIONS_TEAM_SUCCESS("${getPrefix(PrefixType.ERROR)}権限がありません。${ChatColor.RED}${ChatColor.BOLD}${ChatColor.UNDERLINE}生存者${ChatColor.RESET}${ChatColor.RED}以外は実行できません。"),
        PERMISSIONS_TEAM_TUHO("${getPrefix(PrefixType.ERROR)}権限がありません。${ChatColor.RED}${ChatColor.BOLD}${ChatColor.UNDERLINE}通報部隊${ChatColor.RESET}${ChatColor.RED}以外は実行できません。"),

        EXCEPTION("${getPrefix(PrefixType.ERROR)}予期しないエラーが発生しました。", "${getPrefix(PrefixType.SECONDARY)}詳細はコンソールを確認してください。"),

        GAME("${getPrefix(PrefixType.ERROR)}ゲーム中のため実行できません。"),
        NOT_GAME("${getPrefix(PrefixType.ERROR)}ゲーム中ではないため実行できません。");

        val messages: List<String>

        init {
            this.messages = messages.toList()
        }

        fun sendMessage(sender: CommandSender) {
            val stringBuilder = StringBuilder()
            for (message in messages)
                stringBuilder.append("$message\n")
            sender.sendMessage(stringBuilder.toString().trim { it <= ' ' })
        }
    }

    enum class Gamemode(val gameMode: GameMode, val modeId: Int, val modeAlias: String, val modeName: String) {
        SURVIVAL(GameMode.SURVIVAL, 0, "s", "サバイバル"),
        CREATIVE(GameMode.CREATIVE, 1, "c", "クリエイティブ"),
        ADVENTURE(GameMode.ADVENTURE, 2, "a", "アドベンチャー"),
        SPECTATOR(GameMode.SPECTATOR, 3, "sp", "スペクテイター");

        companion object {
            fun getGamemode(gameMode: GameMode): Gamemode {
                return values().firstOrNull { it.gameMode == gameMode } ?: SURVIVAL
            }
        }
    }
}
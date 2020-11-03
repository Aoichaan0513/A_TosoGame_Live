package jp.aoichaan0513.A_TosoGame_Live.Utils

import com.google.common.collect.ImmutableListMultimap
import com.google.gson.Gson
import com.google.gson.JsonArray
import jp.aoichaan0513.A_TosoGame_Live.Main
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import java.io.IOException
import java.net.URL
import java.net.URLEncoder
import java.util.stream.Collectors

object RomajiConverter {

    // Based on Mozc
    private val CONSONANT_MAPPINGS = ImmutableListMultimap.Builder<String, String>()
            .putAll("", "あ", "い", "う", "え", "お")
            .putAll("k", "か", "き", "く", "け", "こ")
            .putAll("s", "さ", "し", "す", "せ", "そ")
            .putAll("t", "た", "ち", "つ", "て", "と")
            .putAll("n", "な", "に", "ぬ", "ね", "の")
            .putAll("h", "は", "ひ", "ふ", "へ", "ほ")
            .putAll("m", "ま", "み", "む", "め", "も")
            .putAll("y", "や", "い", "ゆ", "いぇ", "よ")
            .putAll("r", "ら", "り", "る", "れ", "ろ")
            .putAll("w", "わ", "うぃ", "う", "うぇ", "を")
            .putAll("g", "が", "ぎ", "ぐ", "げ", "ご")
            .putAll("z", "ざ", "じ", "ず", "ぜ", "ぞ")
            .putAll("d", "だ", "ぢ", "づ", "で", "ど")
            .putAll("b", "ば", "び", "ぶ", "べ", "ぼ")
            .putAll("j", "じゃ", "じ", "じゅ", "じぇ", "じょ")
            .putAll("p", "ぱ", "ぴ", "ぷ", "ぺ", "ぽ") // Y
            .putAll("ky", "きゃ", "きぃ", "きゅ", "きぇ", "きょ")
            .putAll("sy", "しゃ", "しぃ", "しゅ", "しぇ", "しょ")
            .putAll("ty", "ちゃ", "ちぃ", "ちゅ", "ちぇ", "ちょ")
            .putAll("ny", "にゃ", "にぃ", "にゅ", "にぇ", "にょ")
            .putAll("hy", "ひゃ", "ひぃ", "ひゅ", "ひぇ", "ひょ")
            .putAll("my", "みゃ", "みぃ", "みゅ", "みぇ", "みょ")
            .putAll("ry", "りゃ", "りぃ", "りゅ", "りぇ", "りょ")
            .putAll("gy", "ぎゃ", "ぎぃ", "ぎゅ", "ぎぇ", "ぎょ")
            .putAll("zy", "じゃ", "じぃ", "じゅ", "じぇ", "じょ")
            .putAll("dy", "ぢゃ", "ぢぃ", "ぢゅ", "ぢぇ", "ぢょ")
            .putAll("by", "びゃ", "びぃ", "びゅ", "びぇ", "びょ")
            .putAll("jy", "じゃ", "じぃ", "じゅ", "じぇ", "じょ")
            .putAll("py", "ぴゃ", "ぴぃ", "ぴゅ", "ぴぇ", "ぴょ")
            .putAll("wy", "wya", "ゐ", "wyu", "ゑ", "wyo") // H
            .putAll("sh", "しゃ", "し", "しゅ", "しぇ", "しょ")
            .putAll("th", "てゃ", "てぃ", "てゅ", "てぇ", "てょ")
            .putAll("wh", "うぁ", "うぃ", "う", "うぇ", "うぉ")
            .putAll("ch", "ちゃ", "ち", "ちゅ", "ちぇ", "ちょ") // W
            .putAll("tw", "とぁ", "とぃ", "とぅ", "とぇ", "とぉ") // L
            .putAll("l", "ぁ", "ぃ", "ぅ", "ぇ", "ぉ")
            .putAll("lt", "lta", "lti", "っ", "lte", "lto")
            .putAll("ly", "ゃ", "ぃ", "ゅ", "ぇ", "ょ") // X
            .putAll("x", "ぁ", "ぃ", "ぅ", "ぇ", "ぉ")
            .putAll("xt", "xta", "xti", "っ", "xto")
            .putAll("xy", "ゃ", "ぃ", "ゅ", "ぇ", "ょ")
            .build()

    /**
     * ローマ字をひらがなに変換します。
     *
     * @param source 変換するローマ字
     * @return ひらがな
     */
    fun toHiragana(source: String): String {
        return HiraganaConverter(source).getDest()
    }

    /**
     * ローマ字をひらがなに変換し、可能な部分は漢字に置き換えます。
     *
     * @param source ローマ字
     * @return 変換後文字列
     */
    fun toKanji(source: String): String {
        val hiragana = toHiragana(source)
        val suffix = if (!source.equals(hiragana, true)) "${ChatColor.GRAY} ($source)" else ""
        return try {
            val url = URL("http://www.google.com/transliterate?langpair=ja-Hira|ja&text=${URLEncoder.encode(hiragana, Main.CHARSET.toString())}")
            try {
                url.openStream().bufferedReader(Main.CHARSET).use {
                    val stringBuilder = StringBuilder()
                    for (element in Gson().fromJson(it.lines().collect(Collectors.joining()), JsonArray::class.java))
                        stringBuilder.append((element as JsonArray)[1].asJsonArray[0].asString)
                    stringBuilder.toString() + suffix
                }
            } catch (e: IOException) {
                Bukkit.getConsoleSender().sendMessage("Failed to access to Google API!")
                hiragana + suffix
            }
        } catch (e: IOException) {
            hiragana + suffix
        }
    }

    private class HiraganaConverter internal constructor(source: String) {

        private val buffer = StringBuilder()
        private val dest = StringBuilder()

        private fun clearBuf() {
            buffer.setLength(0)
        }

        private fun clearBufAndInit(c: Char) {
            clearBuf()
            buffer.append(c)
        }

        private fun flushBuf() {
            dest.append(buffer)
            clearBuf()
        }

        private fun convert(source: String): String {
            var sourceStrategy = source
            sourceStrategy = sourceStrategy.trim { it <= ' ' }
            if (sourceStrategy.isEmpty()) return sourceStrategy
            if (Character.isUpperCase(sourceStrategy[0])) return sourceStrategy
            for (c in sourceStrategy.toCharArray()) {
                val bufferStr = buffer.toString()
                if (c == 'n' && bufferStr == "n") {
                    dest.append('ん')
                    clearBuf()
                    continue
                }
                if (CONSONANT_MAPPINGS.containsKey(bufferStr)
                        && bufferStr == Character.toString(c)) {
                    dest.append("っ")
                    clearBufAndInit(c)
                    continue
                }
                val vowelIndex = "aiueo".indexOf(c)
                if (vowelIndex != -1) {
                    if (!CONSONANT_MAPPINGS.containsKey(bufferStr)) {
                        flushBuf()
                        continue
                    }
                    dest.append(CONSONANT_MAPPINGS[bufferStr][vowelIndex])
                    clearBuf()
                    continue
                }
                if (c != 'y' && bufferStr == "n") {
                    dest.append('ん')
                    clearBuf()
                    if (Character.isLowerCase(c)) {
                        buffer.append(c)
                    } else {
                        dest.append(c)
                    }
                    continue
                }
                if (c == '-') {
                    flushBuf()
                    dest.append('ー')
                    continue
                }
                if (Character.isLowerCase(c)) {
                    buffer.append(c)
                } else {
                    flushBuf()
                    dest.append(c)
                }
            }
            dest.append(buffer)
            if (dest[dest.length - 1] == 'n') dest.replace(dest.length - 1, dest.length, "ん")
            val converted = dest.toString()
            clearBuf()
            dest.setLength(0)
            return converted
        }

        fun getDest(): String {
            return dest.toString()
        }

        init {
            for (c in source.toCharArray()) {
                val bufferStr = buffer.toString()
                if (c == 'n' && bufferStr == "n") {
                    dest.append('ん')
                    clearBuf()
                    continue
                }
                if (CONSONANT_MAPPINGS.containsKey(bufferStr)
                        && bufferStr == Character.toString(c)) {
                    dest.append("っ")
                    clearBufAndInit(c)
                    continue
                }
                val vowelIndex = "aiueo".indexOf(c)
                if (vowelIndex != -1) {
                    if (!CONSONANT_MAPPINGS.containsKey(bufferStr)) {
                        flushBuf()
                        continue
                    }
                    dest.append(CONSONANT_MAPPINGS[bufferStr][vowelIndex])
                    clearBuf()
                    continue
                }
                if (c != 'y' && bufferStr == "n") {
                    dest.append('ん')
                    clearBuf()
                    if (Character.isLowerCase(c)) {
                        buffer.append(c)
                    } else {
                        dest.append(c)
                    }
                    continue
                }
                if (c == '-') {
                    flushBuf()
                    dest.append('ー')
                    continue
                }
                if (Character.isLowerCase(c)) {
                    buffer.append(c)
                } else {
                    flushBuf()
                    dest.append(c)
                }
            }
            dest.append(buffer)
        }
    }

    private class HiraConvertResult(val convertedStr: String, val isDoConvertToKanji: Boolean)
}
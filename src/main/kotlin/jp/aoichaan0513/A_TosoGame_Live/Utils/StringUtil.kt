package jp.aoichaan0513.A_TosoGame_Live.Utils

import org.bukkit.ChatColor

val StringBuffer.trimString
    get() = toString().trim()

val StringBuilder.trimString
    get() = toString().trim()


fun String.trim(l: Int): String {
    return if (length > l) "${substring(0, l - 1)}…" else this
}

fun String.replaceAll(collection: Collection<Pair<String, String>>): String {
    var s = this

    for ((key, value) in collection)
        s = s.replace(key, value)

    return s
}

fun String.replaceAll(vararg pairs: Pair<String, String>): String {
    return replaceAll(pairs.asList())
}

fun <T> Array<T>.toString(action: (T) -> String, empty: String, separator: String = ", "): String {
    return this.toList().toString(action, empty, separator)
}

fun <T> Iterable<T>.toString(action: (T) -> String, empty: String, separator: String = ", "): String {
    return this.toList().toString(action, empty, separator)
}

fun <T> Collection<T>.toString(action: (T) -> String, empty: String, separator: String = ", "): String {
    val stringBuilder = StringBuilder()
    if (this.isEmpty()) return empty
    for (i in this.indices)
        stringBuilder.append("${action(this.elementAt(i))}${(i < this.size - 1).getValue(separator, "")}")
    return stringBuilder.trimString
}

fun String.color(): String {
    return ChatColor.translateAlternateColorCodes('&', this)
}

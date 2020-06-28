package jp.aoichaan0513.A_TosoGame_Live.Utils

import java.net.MalformedURLException
import java.net.URL

class ParseUtil {
    companion object {

        fun isURL(str: String?): Boolean {
            return try {
                URL(str)
                true
            } catch (err: MalformedURLException) {
                false
            }
        }

        fun toURL(str: String?): URL? {
            return try {
                URL(str)
            } catch (err: MalformedURLException) {
                null
            }
        }

        fun isInt(str: String): Boolean {
            return str.toIntOrNull() != null
        }

        fun isLong(str: String): Boolean {
            return str.toLongOrNull() != null
        }

        fun isDouble(str: String): Boolean {
            return str.toDoubleOrNull() != null
        }

        fun isFloat(str: String): Boolean {
            return str.toFloatOrNull() != null
        }
    }
}
package jp.aoichaan0513.A_TosoGame_Live.Utils

import java.net.MalformedURLException
import java.net.URL

class ParseUtil {
    companion object {

        @JvmStatic
        fun isURL(str: String?): Boolean {
            return try {
                URL(str)
                true
            } catch (err: MalformedURLException) {
                false
            }
        }

        @JvmStatic
        fun toURL(str: String): URL? {
            return try {
                URL(str)
            } catch (err: MalformedURLException) {
                null
            }
        }

        @JvmStatic
        fun isInt(str: String): Boolean {
            return str.toIntOrNull() != null
        }

        @JvmStatic
        fun toInt(str: String, defaultValue: Int = 0): Int {
            return str.toIntOrNull() ?: defaultValue
        }

        @JvmStatic
        fun isLong(str: String): Boolean {
            return str.toLongOrNull() != null
        }

        @JvmStatic
        fun toLong(str: String, defaultValue: Long = 0): Long {
            return str.toLongOrNull() ?: defaultValue
        }

        @JvmStatic
        fun isDouble(str: String): Boolean {
            return str.toDoubleOrNull() != null
        }

        @JvmStatic
        fun toDouble(str: String, defaultValue: Double = 0.0): Double {
            return str.toDoubleOrNull() ?: defaultValue
        }

        @JvmStatic
        fun isFloat(str: String): Boolean {
            return str.toFloatOrNull() != null
        }

        @JvmStatic
        fun toFloat(str: String, defaultValue: Float = 0.0F): Float {
            return str.toFloatOrNull() ?: defaultValue
        }
    }
}
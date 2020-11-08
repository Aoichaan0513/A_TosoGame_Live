package jp.aoichaan0513.A_TosoGame_Live.Utils

import org.bukkit.Material
import java.io.Serializable

class DataClass {

    data class BlockPosition(val x: Int, val z: Int, val material: Material)

    data class BlockData(val x: Int, val y: Int, val rgbColor: RGBColor)

    data class RGBColor(var red: Int, var green: Int, var blue: Int)

    data class Timestamp(val rawHours: Int, val rawMinutes: Int, val rawSeconds: Int) : Serializable {

        val hours = format(rawHours)
        val minutes = format(rawMinutes)
        val seconds = format(rawSeconds)

        val toNormal = "${if (rawHours > 0) "$hours:" else ""}$minutes:$seconds"
        val toJapan = "${if (rawHours > 0) "${hours}時間" else ""}${minutes}分${seconds}秒"

        constructor(rawHours: Long, rawMinutes: Long, rawSeconds: Long) : this(rawHours.toInt(), rawMinutes.toInt(), rawSeconds.toInt())

        private fun format(i: Int): String {
            return if (i < 10) "0$i" else i.toString()
        }
    }
}
package jp.aoichaan0513.A_TosoGame_Live.API.Map

import org.bukkit.Material

class DataClass {

    data class BlockPosition(val x: Int, val z: Int, val material: Material)

    data class BlockData(val x: Int, val y: Int, val rgbColor: RGBColor)

    data class RGBColor(var red: Int, var green: Int, var blue: Int)
}
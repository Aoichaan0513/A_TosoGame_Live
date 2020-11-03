package jp.aoichaan0513.A_TosoGame_Live.API.Map

import org.bukkit.Location
import org.bukkit.map.MapCursor

class PlotInfo(var loc: Location, var type: MapCursor.Type) {
    var isSetup = false
    var cursor: MapCursor? = null

}
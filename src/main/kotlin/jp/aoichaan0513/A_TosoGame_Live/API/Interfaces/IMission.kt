package jp.aoichaan0513.A_TosoGame_Live.API.Interfaces

import jp.aoichaan0513.A_TosoGame_Live.Mission.MissionManager
import org.bukkit.Material

class IMission(val id: Int, val count: Int, private val rawTitle: String, val descriptions: List<String>, val type: MissionManager.MissionType, val material: Material = Material.QUARTZ_BLOCK) {

    val title: String
        get() {
            return when (type) {
                MissionManager.MissionType.END -> "$rawTitle (終了)"
                else -> rawTitle
            }
        }
}
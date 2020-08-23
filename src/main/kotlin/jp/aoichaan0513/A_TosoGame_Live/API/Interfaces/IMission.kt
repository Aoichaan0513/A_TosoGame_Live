package jp.aoichaan0513.A_TosoGame_Live.API.Interfaces

import jp.aoichaan0513.A_TosoGame_Live.Mission.MissionManager

class IMission(val count: Int, private val rawTitle: String, val descriptions: List<String>, val missionState: MissionManager.MissionState, val type: MissionManager.MissionType) {

    val title: String
        get() {
            return when (type) {
                MissionManager.MissionType.END -> "$rawTitle (終了)"
                else -> rawTitle
            }
        }
}
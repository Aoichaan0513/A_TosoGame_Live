package jp.aoichaan0513.A_TosoGame_Live.API.Manager.Player

import jp.aoichaan0513.A_TosoGame_Live.API.Manager.World.WorldManager
import org.bukkit.entity.Player
import java.util.*

class DifficultyManager {
    companion object {

        private val difficultyMap = mutableMapOf<UUID, WorldManager.Difficulty>()

        fun clear() {
            difficultyMap.clear()
        }

        fun isDifficulty(uuid: UUID): Boolean {
            return difficultyMap.containsKey(uuid)
        }

        fun isDifficulty(p: Player): Boolean {
            return isDifficulty(p.uniqueId)
        }

        fun getDifficulty(uuid: UUID, defaultDifficulty: WorldManager.Difficulty = WorldManager.Difficulty.NORMAL): WorldManager.Difficulty {
            return difficultyMap[uuid] ?: defaultDifficulty
        }

        fun getDifficulty(p: Player, defaultDifficulty: WorldManager.Difficulty = WorldManager.Difficulty.NORMAL): WorldManager.Difficulty {
            return getDifficulty(p.uniqueId, defaultDifficulty)
        }

        fun setDifficulty(uuid: UUID, difficulty: WorldManager.Difficulty = PlayerManager.loadConfig(uuid).difficulty) {
            difficultyMap[uuid] = difficulty
            PlayerManager.loadConfig(uuid).difficulty = difficulty
        }

        fun setDifficulty(p: Player, difficulty: WorldManager.Difficulty = PlayerManager.loadConfig(p.uniqueId).difficulty) {
            setDifficulty(p.uniqueId, difficulty)
        }
    }
}
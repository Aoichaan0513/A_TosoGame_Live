package jp.aoichaan0513.A_TosoGame_Live.API.Manager.Player

import org.bukkit.entity.Player
import java.util.*

class PlayerManager {
    companion object {

        private val hashMap = mutableMapOf<UUID, PlayerConfig>()

        fun loadConfig(uuid: UUID): PlayerConfig {
            if (!hashMap.containsKey(uuid)) {
                val playerConfig = PlayerConfig(uuid)
                hashMap[uuid] = playerConfig
            }
            return hashMap[uuid]!!
        }

        fun loadConfig(p: Player): PlayerConfig {
            return loadConfig(p.uniqueId)
        }

        fun reloadConfig(uuid: UUID): PlayerConfig {
            val playerConfig = PlayerConfig(uuid)
            hashMap[uuid] = playerConfig
            return hashMap[uuid]!!
        }

        fun reloadConfig(p: Player): PlayerConfig {
            return reloadConfig(p.uniqueId)
        }

        fun reloadConfig() {
            for (uuid in hashMap.keys)
                reloadConfig(uuid)
        }

        val configs: Set<Map.Entry<UUID, PlayerConfig>>
            get() = hashMap.entries
    }
}
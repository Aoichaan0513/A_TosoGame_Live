package jp.aoichaan0513.A_TosoGame_Live.API.Manager.Player

import org.bukkit.entity.Player
import java.util.*

class PlayerManager {
    companion object {

        private val hashMap = mutableMapOf<UUID, PlayerConfig>()

        fun loadConfig(uuid: UUID) = hashMap[uuid] ?: PlayerConfig(uuid).also {
            hashMap[uuid] = it
        }

        fun loadConfig(p: Player) = loadConfig(p.uniqueId)

        fun reloadConfig(uuid: UUID): PlayerConfig {
            val playerConfig = PlayerConfig(uuid)
            hashMap[uuid] = playerConfig
            return hashMap[uuid]!!
        }

        fun reloadConfig(p: Player) = reloadConfig(p.uniqueId)

        fun reloadConfig() {
            for (uuid in hashMap.keys)
                reloadConfig(uuid)
        }

        val configs: Set<Map.Entry<UUID, PlayerConfig>>
            get() = hashMap.entries
    }
}
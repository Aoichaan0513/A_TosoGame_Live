package jp.aoichaan0513.A_TosoGame_Live.OPGame

import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.BossBarManager
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.GameManager
import jp.aoichaan0513.A_TosoGame_Live.API.TosoGameAPI
import jp.aoichaan0513.A_TosoGame_Live.Main
import jp.aoichaan0513.A_TosoGame_Live.Utils.isPlayerGroup
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable
import java.util.*


class Dice {
    companion object {

        val opGameType = OPGameManager.OPGameState.DICE

        lateinit var sender: CommandSender

        var count = 0
            private set

        fun start(sender: CommandSender) {
            if (OPGameManager.opGameState != OPGameManager.OPGameState.NONE) return

            val worldConfig = Main.worldConfig

            this.sender = sender

            OPGameManager.startOPGame(sender, opGameType)

            val player = OPGameManager.runPlayer
            TosoGameAPI.teleport(player, worldConfig.opGameLocationConfig.opLocation)

            Bukkit.broadcastMessage("""
                ${MainAPI.getPrefix(MainAPI.PrefixType.WARNING)}オープニングゲームを開始しました。
                ${MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY)}${player.name}がサイコロを投げます。
            """.trimIndent())
            player.sendMessage("""
                ${MainAPI.getPrefix(MainAPI.PrefixType.WARNING)}"/shuffle"と入力してサイコロを投げてください。
                ${MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY)}15秒以内に実行しない場合は自動実行されます。
            """.trimIndent())

            scheduleRunCommand(player)
        }

        fun end() {
            if (OPGameManager.opGameState == OPGameManager.OPGameState.NONE) return

            OPGameManager.endOPGame(sender)
            count = 0
        }

        fun shuffle() {
            if (OPGameManager.opGameState != opGameType || OPGameManager.isRunned) return

            BossBarManager.showBar()

            val worldConfig = Main.worldConfig
            val maxCount = worldConfig.opGameConfig.diceCount

            OPGameManager.isRunned = true
            OPGameManager.player?.sendMessage("${MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY)}サイコロを投げました。")

            object : BukkitRunnable() {
                var i = 4
                var lastResult = (1..6).shuffled().first()

                override fun run() {
                    val result = (1..6).filter { it != lastResult }.shuffled().first()
                    lastResult = result

                    if (i < 1) {
                        count += result

                        BossBarManager.showBar()

                        if (result == 1) {
                            Bukkit.broadcastMessage("""
                                ${MainAPI.getPrefix(MainAPI.PrefixType.WARNING)}${result}が出ました。($count/$maxCount)
                                ${MainAPI.getPrefix(MainAPI.PrefixType.WARNING)}1が出たためゲームが開始されました。
                            """.trimIndent())

                            GameManager.startGame(1, worldConfig.gameConfig.game)
                            for (player in Bukkit.getOnlinePlayers())
                                player.playSound(player.location, Sound.BLOCK_NOTE_BLOCK_PLING, 1f, 1f)

                            end()
                        } else {
                            if (count >= maxCount) {
                                Bukkit.broadcastMessage("""
                                    ${MainAPI.getPrefix(MainAPI.PrefixType.WARNING)}${result}が出ました。($count/$maxCount)
                                    ${MainAPI.getPrefix(MainAPI.PrefixType.WARNING)}サイコロの目が合計${maxCount}カウント以上出たため、30秒の猶予のあとゲームが開始されます。
                                """.trimIndent())

                                GameManager.startGame(30, worldConfig.gameConfig.game)
                                for (player in Bukkit.getOnlinePlayers())
                                    player.playSound(player.location, Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f)

                                Bukkit.getScheduler().runTaskLater(Main.pluginInstance, Runnable {
                                    for (player in MainAPI.getOnlinePlayers(getSnowballPlayers(5)).filter { it.isPlayerGroup }) {
                                        val itemStack = ItemStack(Material.SNOWBALL)
                                        val itemMeta = itemStack.itemMeta!!
                                        itemMeta.setDisplayName("${ChatColor.GREEN}雪玉 (移動禁止)")
                                        itemMeta.lore = listOf("${ChatColor.YELLOW}ハンターに当てるとその周りに檻を貼り20秒間動けなくします。")
                                        itemStack.itemMeta = itemMeta
                                        player.inventory.addItem(ItemStack(Material.SNOWBALL))
                                    }
                                }, 20 * 31)

                                end()
                            } else {
                                if (OPGameManager.hasNext) {
                                    val player = OPGameManager.player!!

                                    TosoGameAPI.teleport(player, worldConfig.opGameLocationConfig.gOPLocations.values)

                                    val nextPlayer = OPGameManager.runPlayer

                                    nextPlayer.teleport(worldConfig.opGameLocationConfig.opLocation)

                                    Bukkit.broadcastMessage("""
                                        ${MainAPI.getPrefix(MainAPI.PrefixType.WARNING)}${result}が出ました。($count/$maxCount)
                                        ${MainAPI.getPrefix(MainAPI.PrefixType.WARNING)}1以外が出たためハンターは放出されません。続行します。
                                        ${MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY)}${nextPlayer.name}がサイコロを投げます。
                                    """.trimIndent())
                                    nextPlayer.sendMessage("""
                                        ${MainAPI.getPrefix(MainAPI.PrefixType.WARNING)}"/shuffle"と入力してサイコロを投げてください。
                                        ${MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY)}15秒以内に実行しない場合は自動実行されます。
                                    """.trimIndent())

                                    for (p in Bukkit.getOnlinePlayers())
                                        p.playSound(p.location, Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f)

                                    scheduleRunCommand(nextPlayer)
                                } else {
                                    Bukkit.broadcastMessage("""
                                        ${MainAPI.getPrefix(MainAPI.PrefixType.WARNING)}${result}が出ました。($count/$maxCount)
                                        ${MainAPI.getPrefix(MainAPI.PrefixType.WARNING)}オープニングゲームに参加できるプレイヤーがいないため、30秒の猶予のあとゲームが開始されます。
                                    """.trimIndent())

                                    GameManager.startGame(30, worldConfig.gameConfig.game)
                                    for (player in Bukkit.getOnlinePlayers())
                                        player.playSound(player.location, Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f)

                                    end()
                                }
                            }
                        }
                        BossBarManager.showBar()
                        cancel()
                    } else {
                        Bukkit.broadcastMessage("${MainAPI.getPrefix(MainAPI.PrefixType.WARNING)}$result…")
                        for (p in Bukkit.getOnlinePlayers())
                            p.playSound(p.location, Sound.BLOCK_NOTE_BLOCK_PLING, 1f, 1f)
                        i--
                    }
                }
            }.runTaskTimer(Main.pluginInstance, 20, 20)
        }

        fun setPlayer() {
            if (OPGameManager.opGameState != opGameType) return

            val worldConfig = Main.worldConfig

            val player = OPGameManager.player
            if (player != null && player.isOnline) return

            if (OPGameManager.hasNext) {
                val nextPlayer = OPGameManager.runPlayer
                nextPlayer.teleport(worldConfig.opGameLocationConfig.opLocation)

                Bukkit.broadcastMessage("""
                    ${MainAPI.getPrefix(MainAPI.PrefixType.WARNING)}プレイヤーが退出したため別のプレイヤーに変更します。
                    ${MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY)}${nextPlayer.name}がサイコロを投げます。
                """.trimIndent())
                nextPlayer.sendMessage("""
                    ${MainAPI.getPrefix(MainAPI.PrefixType.WARNING)}"/shuffle"と入力してサイコロを投げてください。
                    ${MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY)}15秒以内に実行しない場合は自動実行されます。
                """.trimIndent())

                for (p in Bukkit.getOnlinePlayers())
                    p.playSound(p.location, Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f)

                scheduleRunCommand(nextPlayer)
            } else {
                Bukkit.broadcastMessage("""
                    ${MainAPI.getPrefix(MainAPI.PrefixType.WARNING)}プレイヤーが退出しました。
                    ${MainAPI.getPrefix(MainAPI.PrefixType.WARNING)}オープニングゲームに参加できるプレイヤーがいないため、30秒の猶予のあとゲームが開始されます。
                """.trimIndent())

                GameManager.startGame(30, worldConfig.gameConfig.game)
                for (p in Bukkit.getOnlinePlayers())
                    p.playSound(p.location, Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f)

                end()
            }
        }

        fun getSnowballPlayers(i: Int): Set<UUID> {
            val list = mutableListOf<UUID>()

            Bukkit.getOnlinePlayers().filter { it.isPlayerGroup }.forEach { list.add(it.uniqueId) }

            for (c in 0..2)
                list.shuffle()

            val set = mutableSetOf<UUID>()
            for (c in 0 until if (list.size > i) i else list.size)
                set.add(list[c])
            return set
        }

        private fun scheduleRunCommand(p: Player) {
            Bukkit.getScheduler().runTaskLater(Main.pluginInstance, Runnable {
                val player = OPGameManager.player ?: return@Runnable

                if (player.uniqueId == p.uniqueId && !OPGameManager.isRunned)
                    shuffle()
            }, 20 * 15)
        }
    }
}
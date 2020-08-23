package jp.aoichaan0513.A_TosoGame_Live.Runnable

import jp.aoichaan0513.A_TosoGame_Live.API.Enums.Advancement
import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI
import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI.PrefixType
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.BossBarManager
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.GameManager
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.GameManager.GameState
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.MoneyManager
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.Player.PlayerManager
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.World.WorldConfig
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.World.WorldManager.GameType
import jp.aoichaan0513.A_TosoGame_Live.API.Scoreboard.Teams
import jp.aoichaan0513.A_TosoGame_Live.API.Scoreboard.Teams.OnlineTeam
import jp.aoichaan0513.A_TosoGame_Live.API.TosoGameAPI
import jp.aoichaan0513.A_TosoGame_Live.Listeners.Minecraft.onDamage
import jp.aoichaan0513.A_TosoGame_Live.Listeners.Minecraft.onInteract
import jp.aoichaan0513.A_TosoGame_Live.Main
import jp.aoichaan0513.A_TosoGame_Live.Mission.HunterZone
import jp.aoichaan0513.A_TosoGame_Live.Mission.MissionManager
import jp.aoichaan0513.A_TosoGame_Live.Utils.*
import jp.aoichaan0513.A_TosoGame_Live.Utils.DateTime.TimeFormat
import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.ComponentBuilder
import net.md_5.bungee.api.chat.HoverEvent
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.*
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitRunnable
import java.util.*

class GameRunnable(initialCountDown: Int, initialGameTime: Int) : BukkitRunnable() {

    companion object {
        var countDown = 0
        var gameTime = 0
        var settingCountDown = 0
        var settingGameTime = 0

        private val hashMap = HashMap<UUID, Location>()

        /**
         * カウントダウン・ゲーム時間
         */
        val time: Int
            get() = if (countDown <= 0) gameTime else countDown

        fun setGameTime(gameTime: Int, b: Boolean = true) {
            this.gameTime = gameTime
            settingGameTime = gameTime
        }

        fun addGameTime(gameTime: Int) {
            this.gameTime += gameTime
            settingGameTime = gameTime
        }

        fun removeGameTime(gameTime: Int) {
            this.gameTime -= gameTime
            settingGameTime = gameTime
        }
    }

    init {
        countDown = initialCountDown
        settingCountDown = initialCountDown
        gameTime = initialGameTime
        settingGameTime = initialGameTime
    }

    override fun run() {
        if (!GameManager.isGame()) return

        val worldConfig = Main.worldConfig

        if (countDown <= 0) {
            gameTime--

            MoneyManager.addReward()

            BossBarManager.showBar(gameTime, settingGameTime)

            val respawnDenyTime = worldConfig.gameConfig.respawnDeny
            TosoGameAPI.isRespawn = gameTime > respawnDenyTime

            if (gameTime > 0) {
                if (gameTime < 16) {
                    Bukkit.broadcastMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}ゲーム終了まで残り${TimeFormat.formatSec(gameTime)}秒")
                    for (player in Bukkit.getOnlinePlayers()) {
                        player.sendTitle("${ChatColor.DARK_RED}${ChatColor.BOLD}${gameTime}", "${ChatColor.GRAY}ゲーム終了まで", 20, 40, 20)
                        player.playSound(player.location, Sound.BLOCK_NOTE_BLOCK_SNARE, 1f, 1f)
                    }
                }

                if (gameTime % 60 == 0) {
                    Bukkit.broadcastMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}ゲーム終了まで残り${TimeFormat.formatMin(gameTime)}分")
                    for (p in Bukkit.getOnlinePlayers())
                        p.playSound(p.location, Sound.BLOCK_NOTE_BLOCK_SNARE, 1f, 1f)
                }

                if (TimeFormat.formatMin(gameTime) % 2 != 0 && gameTime % 30 == 0) {
                    for (player in Bukkit.getOnlinePlayers().filter { it.isJailTeam }) {
                        if (!TosoGameAPI.isRespawn) {
                            player.sendMessage("""
                                ${MainAPI.getPrefix(PrefixType.WARNING)}"/hide"で周りを非表示に、"/show"で周りを表示できます。
                                ${MainAPI.getPrefix(PrefixType.WARNING)}また、"/spec"で観戦モードにできます。
                                ${MainAPI.getPrefix(PrefixType.SECONDARY)}このメッセージは確保者にのみ表示されます。
                            """.trimIndent())
                        } else {
                            player.sendMessage("""
                                ${MainAPI.getPrefix(PrefixType.WARNING)}"/hide"で周りを非表示に、"/show"で周りを表示できます。
                                ${MainAPI.getPrefix(PrefixType.SECONDARY)}このメッセージは確保者にのみ表示されます。
                            """.trimIndent())
                        }
                    }
                }

                if (gameTime % 5 == 0) {
                    setHealths(worldConfig, gameTime % 10 == 0)
                } else {
                    for (player in Bukkit.getOnlinePlayers().filter { it.isPlayerGroup })
                        setHealth(player)
                }

                if (gameTime == respawnDenyTime) {
                    Bukkit.broadcastMessage("${MainAPI.getPrefix(PrefixType.WARNING)}残り${TimeFormat.formatMin(respawnDenyTime)}分になったため途中参加・復活を禁止します。")
                    for (p in Bukkit.getOnlinePlayers())
                        p.playSound(p.location, Sound.BLOCK_NOTE_BLOCK_SNARE, 1f, 1f)
                }
            }

            if (gameTime == 0 || Teams.getOnlineCount(OnlineTeam.TOSO_PLAYER) == 0) {
                // ゲーム時間が0になるか、逃走者が0人になった場合
                if (worldConfig.gameConfig.successMission) {
                    // 生存ミッションが有効の場合
                    if (Teams.getOnlineCount(OnlineTeam.TOSO_SUCCESS) > 0) {
                        // 生存者チームにプレイヤーがいる場合
                        if (gameTime == 0) {
                            // ゲーム時間が0の場合、逃走者側の勝利判定でゲーム終了

                            GameManager.endGame()
                            HunterZone.endMission()
                            BossBarManager.showBar()

                            for (p in Bukkit.getOnlinePlayers()) {
                                if (p.isSuccessTeam) {
                                    val playerConfig = PlayerManager.loadConfig(p)
                                    if (!playerConfig.advancementConfig.hasAdvancement(Advancement.FIRST_GAME_CLEAR)) {
                                        playerConfig.advancementConfig.addAdvancement(Advancement.FIRST_GAME_CLEAR)
                                        Advancement.FIRST_GAME_CLEAR.sendMessage(p)
                                    }

                                    p.playSound(p.location, Sound.UI_TOAST_CHALLENGE_COMPLETE, 1f, 1f)
                                    p.addPotionEffect(PotionEffect(PotionEffectType.GLOWING, 200000, 1, false, false))
                                    p.sendTitle("${ChatColor.GREEN}${ChatColor.BOLD}Task Success.", "${ChatColor.BOLD}逃走者側の勝利", 10, 70, 20)
                                } else {
                                    MoneyManager.setReward(p, 0)

                                    if (p.isPlayerTeam || p.isHunterGroup)
                                        p.sendTitle("${ChatColor.RED}${ChatColor.BOLD}Task Failed...", "${ChatColor.BOLD}逃走者側の勝利", 10, 70, 20)
                                    else
                                        p.sendTitle("", "${ChatColor.BOLD}逃走者側の勝利", 10, 70, 20)
                                }
                            }

                            Bukkit.broadcastMessage("""
                                ${MainAPI.getPrefix(PrefixType.SECONDARY)}ゲーム終了
                                ${MainAPI.getPrefix(PrefixType.WARNING)}今回の生存者は${OnlineTeam.TOSO_SUCCESS.team.entries}です。
                            """.trimIndent())

                            // ブロックにテレポートするメッセージを送信
                            if (onInteract.successBlockLoc != null) {
                                for (player in Bukkit.getOnlinePlayers().filter { TosoGameAPI.isAdmin(it) }) {
                                    val textComponent1 = TextComponent("${MainAPI.getPrefix(PrefixType.SECONDARY)}生存ブロックの位置にテレポートする場合は")
                                    val textComponent2 = TextComponent()
                                    textComponent2.text = "${ChatColor.GRAY}${ChatColor.BOLD}${ChatColor.UNDERLINE}ここ"
                                    textComponent2.hoverEvent = HoverEvent(HoverEvent.Action.SHOW_TEXT, ComponentBuilder("${ChatColor.GOLD}ブロックの位置にテレポートする場合はここをクリックしてください。").create())
                                    textComponent2.clickEvent = ClickEvent(ClickEvent.Action.RUN_COMMAND, "/btp")
                                    textComponent1.addExtra(textComponent2)
                                    val textComponent3 = TextComponent("${ChatColor.GRAY}をクリックしてください。")
                                    textComponent1.addExtra(textComponent3)
                                    player.spigot().sendMessage(textComponent1)
                                }
                            }

                            sendResult()
                        } else {
                            // ゲーム時間が0以外の場合、全員のレートを一時的に変更

                            if (!TosoGameAPI.isRunnedBonusMission) {
                                for (player in Bukkit.getOnlinePlayers().filter { it.isSuccessTeam }) {
                                    MoneyManager.addRate(player, 50)
                                    player.sendMessage("""
                                        ${MainAPI.getPrefix(PrefixType.SECONDARY)}あなたのレートが変更されました。
                                        ${MainAPI.getPrefix(PrefixType.SECONDARY)}現在のレートは${ChatColor.BOLD}${ChatColor.UNDERLINE}${MoneyManager.getRate(player)}円${ChatColor.RESET}${ChatColor.GRAY}に設定されています。
                                    """.trimIndent())
                                }

                                MissionManager.sendMission(
                                        Bukkit.getConsoleSender(),
                                        "通達だ。\n逃走者が全員生存者となったためボーナスとして生存者のレートを50円追加する。\nただし、確保されたらその人のレートは元に戻る。気をつけたまえ。",
                                        MissionManager.MissionState.OTHER(otherMaterial = Material.DRAGON_EGG),
                                        MissionManager.MissionType.TUTATU_HINT,
                                        MissionManager.MissionDetailType.TUTATU,
                                )

                                TosoGameAPI.isRunnedBonusMission = true
                            }
                        }
                    } else {
                        // 生存者チームにプレイヤーがいない場合

                        GameManager.endGame()
                        HunterZone.endMission()
                        BossBarManager.showBar()

                        for (p in Bukkit.getOnlinePlayers()) {
                            if (p.isHunterGroup) {
                                val reward = worldConfig.getDifficultyConfig(p).rate * settingGameTime.toLong()
                                MoneyManager.setReward(p, if (p.isHunterTeam) reward else reward / 2)

                                p.playSound(p.location, Sound.UI_TOAST_CHALLENGE_COMPLETE, 1f, 1f)
                                p.sendTitle("${ChatColor.GREEN}${ChatColor.BOLD}Task Success.", "${ChatColor.BOLD}ハンター・通報部隊側の勝利", 10, 70, 20)
                            } else {
                                MoneyManager.setReward(p, 0)

                                if (p.isPlayerGroup)
                                    p.sendTitle("${ChatColor.RED}${ChatColor.BOLD}Task Failed...", "${ChatColor.BOLD}ハンター・通報部隊側の勝利", 10, 70, 20)
                                else
                                    p.sendTitle("", "${ChatColor.BOLD}ハンター・通報部隊側の勝利", 10, 70, 20)
                            }
                        }

                        Bukkit.broadcastMessage("""
                            ${MainAPI.getPrefix(PrefixType.SECONDARY)}ゲーム終了
                            ${MainAPI.getPrefix(PrefixType.WARNING)}今回の生存者はいませんでした。
                        """.trimIndent())

                        // ブロックにテレポートするメッセージを送信
                        if (onInteract.successBlockLoc != null) {
                            for (player in Bukkit.getOnlinePlayers().filter { TosoGameAPI.isAdmin(it) }) {
                                val textComponent1 = TextComponent("${MainAPI.getPrefix(PrefixType.SECONDARY)}生存ブロックの位置にテレポートする場合は")
                                val textComponent2 = TextComponent()
                                textComponent2.text = "${ChatColor.GRAY}${ChatColor.BOLD}${ChatColor.UNDERLINE}ここ"
                                textComponent2.hoverEvent = HoverEvent(HoverEvent.Action.SHOW_TEXT, ComponentBuilder("${ChatColor.GOLD}ブロックの位置にテレポートする場合はここをクリックしてください。").create())
                                textComponent2.clickEvent = ClickEvent(ClickEvent.Action.RUN_COMMAND, "/btp")
                                textComponent1.addExtra(textComponent2)
                                val textComponent3 = TextComponent("${ChatColor.GRAY}をクリックしてください。")
                                textComponent1.addExtra(textComponent3)
                                player.spigot().sendMessage(textComponent1)
                            }
                        }

                        sendResult()
                    }
                } else {
                    // 生存ミッションが無効の場合

                    GameManager.endGame()
                    HunterZone.endMission()
                    BossBarManager.showBar()

                    if (Teams.getOnlineCount(OnlineTeam.TOSO_PLAYER) > 0) {
                        // 逃走者チームにプレイヤーがいる場合
                        for (p in Bukkit.getOnlinePlayers()) {
                            if (p.isPlayerTeam)
                                Teams.joinTeam(OnlineTeam.TOSO_SUCCESS, p)

                            val playerConfig = PlayerManager.loadConfig(p)
                            if (!playerConfig.advancementConfig.hasAdvancement(Advancement.FIRST_GAME_CLEAR)) {
                                playerConfig.advancementConfig.addAdvancement(Advancement.FIRST_GAME_CLEAR)
                                Advancement.FIRST_GAME_CLEAR.sendMessage(p)
                            }

                            if (p.isSuccessTeam) {
                                p.playSound(p.location, Sound.UI_TOAST_CHALLENGE_COMPLETE, 1f, 1f)
                                p.addPotionEffect(PotionEffect(PotionEffectType.GLOWING, 200000, 1, false, false))
                                p.sendTitle("${ChatColor.GREEN}${ChatColor.BOLD}Task Success.", "${ChatColor.BOLD}逃走者側の勝利", 10, 70, 20)
                            } else {
                                MoneyManager.setReward(p, 0)

                                if (p.isPlayerTeam || p.isHunterGroup)
                                    p.sendTitle("${ChatColor.RED}${ChatColor.BOLD}Task Failed...", "${ChatColor.BOLD}逃走者側の勝利", 10, 70, 20)
                                else
                                    p.sendTitle("", "${ChatColor.BOLD}逃走者側の勝利", 10, 70, 20)
                            }
                        }
                        Bukkit.broadcastMessage("""
                            ${MainAPI.getPrefix(PrefixType.SECONDARY)}ゲーム終了
                            ${MainAPI.getPrefix(PrefixType.WARNING)}今回の生存者は${OnlineTeam.TOSO_SUCCESS.team.entries}です。
                        """.trimIndent())
                    } else {
                        // 逃走者チームにプレイヤーがいない場合
                        for (p in Bukkit.getOnlinePlayers()) {
                            if (p.isHunterGroup) {
                                val reward = worldConfig.getDifficultyConfig(p).rate * settingGameTime.toLong()
                                MoneyManager.setReward(p, if (p.isHunterTeam) reward else reward / 2)

                                p.playSound(p.location, Sound.UI_TOAST_CHALLENGE_COMPLETE, 1f, 1f)
                                p.sendTitle("${ChatColor.GREEN}${ChatColor.BOLD}Task Success.", "${ChatColor.BOLD}ハンター・通報部隊側の勝利", 10, 70, 20)
                            } else {
                                MoneyManager.setReward(p, 0)

                                if (p.isPlayerGroup)
                                    p.sendTitle("${ChatColor.RED}${ChatColor.BOLD}Task Failed...", "${ChatColor.BOLD}ハンター・通報部隊側の勝利", 10, 70, 20)
                                else
                                    p.sendTitle("", "${ChatColor.BOLD}ハンター・通報部隊側の勝利", 10, 70, 20)
                            }
                        }
                        Bukkit.broadcastMessage("""
                            ${MainAPI.getPrefix(PrefixType.SECONDARY)}ゲーム終了
                            ${MainAPI.getPrefix(PrefixType.WARNING)}今回の生存者はいませんでした。
                        """.trimIndent())
                    }

                    sendResult()
                }
            }
        } else {
            countDown--

            BossBarManager.showBar(countDown, settingCountDown)

            if (countDown == 0) {
                /*
                for (player in Bukkit.getOnlinePlayers()) {
                    MoneyManager.setRate(player)
                    hashMap[player.uniqueId] = player.location
                    player.playSound(player.location, Sound.BLOCK_NOTE_BLOCK_PLING, 1f, 1f)
                    player.sendTitle("${ChatColor.DARK_RED}${ChatColor.BOLD}逃走中", "${ChatColor.RED}${ChatColor.BOLD}Run${ChatColor.RESET}${ChatColor.GRAY} for ${ChatColor.DARK_RED}${ChatColor.BOLD}Money", 20, 50, 20)
                }
                */

                Bukkit.broadcastMessage("""
                    ${MainAPI.getPrefix(PrefixType.SECONDARY)}ゲーム開始
                    ${MainAPI.getPrefix(PrefixType.SECONDARY)}マップ情報
                    ${MainAPI.getPrefix(PrefixType.SECONDARY)}・マップ名: ${worldConfig.mapConfig.name}
                    ${MainAPI.getPrefix(PrefixType.SECONDARY)}・マップ説明: ${worldConfig.mapConfig.description}
                    ${MainAPI.getPrefix(PrefixType.SECONDARY)}・マップ製作者: ${worldConfig.mapConfig.authors}
                """.trimIndent())

                for ((key, value) in worldConfig.hunterDoorConfig.doors) {
                    val loc = value.location
                    worldConfig.hunterDoorConfig.openHunterDoor(key)
                    worldConfig.world.spawnParticle(Particle.EXPLOSION_HUGE, loc, 20)
                    worldConfig.world.playSound(loc, Sound.ENTITY_GENERIC_EXPLODE, 1f, 1f)
                }

                GameManager.gameState = GameState.GAME
                MissionManager.setBossBar()

                for (p in Bukkit.getOnlinePlayers()) {
                    MoneyManager.setRate(p)
                    hashMap[p.uniqueId] = p.location
                    p.playSound(p.location, Sound.BLOCK_NOTE_BLOCK_PLING, 1f, 1f)
                    p.sendTitle("${ChatColor.DARK_RED}${ChatColor.BOLD}逃走中", "${ChatColor.RED}${ChatColor.BOLD}Run${ChatColor.RESET}${ChatColor.GRAY} for ${ChatColor.DARK_RED}${ChatColor.BOLD}Money", 20, 50, 20)

                    if (p.isPlayerTeam) {
                        TosoGameAPI.setItem(GameType.START, p)

                        p.addPotionEffect(PotionEffect(PotionEffectType.NIGHT_VISION, 200000, 1, false, false))
                        p.addPotionEffect(PotionEffect(PotionEffectType.INVISIBILITY, 20 * 15, 1, false, false))
                        p.addPotionEffect(PotionEffect(PotionEffectType.SPEED, 20 * 15, 1, false, false))

                        TosoGameAPI.hidePlayer(p)
                    }
                }
            } else if (countDown <= 5) {
                Bukkit.broadcastMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}ゲーム開始まで残り${TimeFormat.formatSec(countDown)}秒")
                for (player in Bukkit.getOnlinePlayers()) {
                    player.sendTitle("${ChatColor.DARK_RED}${ChatColor.BOLD}${countDown}", "${ChatColor.GRAY}ゲーム開始まで", 20, 40, 20)
                    player.playSound(player.location, Sound.BLOCK_NOTE_BLOCK_SNARE, 1f, 1f)
                }
            }
        }
    }

    /**
     * 体力システム
     */
    fun setHealth(p: Player) {
        hashMap[p.uniqueId] = p.location
    }

    fun setHealths(worldConfig: WorldConfig?, isTenSecond: Boolean) {
        for (player in Bukkit.getOnlinePlayers()) {
            if (GameManager.isGame(GameState.GAME)) {
                if (player.isPlayerGroup) {
                    val difficultyConfig = worldConfig!!.getDifficultyConfig(player)
                    if (difficultyConfig.health) {
                        val loc = player.location
                        val health = player.health
                        val walkSpeed = player.walkSpeed
                        if (hashMap.containsKey(player.uniqueId)) {
                            val location = hashMap[player.uniqueId]
                            if (isTenSecond) {
                                // 10秒おき
                                if (player.isSneaking) {
                                    if (location!!.blockX == loc.blockX && location.blockY == loc.blockY && location.blockZ == loc.blockZ) {
                                        if (health < 20) {
                                            if (health % 2 == 0.0) {
                                                player.walkSpeed = walkSpeed + 0.02f
                                                player.health = health + 2
                                                player.spawnParticle(Particle.HEART, loc.x, loc.blockY + 1.6, loc.z, 3, 0.1, 0.0, 0.1)
                                            } else {
                                                player.walkSpeed = walkSpeed + 0.01f
                                                player.health = health + 1
                                                player.spawnParticle(Particle.HEART, loc.x, loc.blockY + 1.6, loc.z, 3, 0.1, 0.0, 0.1)
                                            }
                                        }
                                    } else {
                                        if (health < 20) {
                                            player.walkSpeed = walkSpeed + 0.01f
                                            player.health = health + 1
                                            player.spawnParticle(Particle.HEART, loc.x, loc.blockY + 1.6, loc.z, 3, 0.1, 0.0, 0.1)
                                        }
                                    }
                                } else {
                                    if (!(location!!.blockX == loc.blockX && location.blockY == loc.blockY && location.blockZ == loc.blockZ)) {
                                        if (health > 6) {
                                            if (player.isSprinting) {
                                                if (health % 2 == 0.0) {
                                                    player.walkSpeed = walkSpeed - 0.02f
                                                    player.health = health - 2
                                                    player.playSound(loc, Sound.BLOCK_NOTE_BLOCK_BASEDRUM, SoundCategory.PLAYERS, 1f, 1f)
                                                } else {
                                                    player.walkSpeed = walkSpeed - 0.01f
                                                    player.health = health - 1
                                                    player.playSound(loc, Sound.BLOCK_NOTE_BLOCK_BASEDRUM, SoundCategory.PLAYERS, 1f, 1f)
                                                }
                                            } else {
                                                player.walkSpeed = walkSpeed - 0.01f
                                                player.health = health - 1
                                                player.playSound(loc, Sound.BLOCK_NOTE_BLOCK_BASEDRUM, SoundCategory.PLAYERS, 1f, 1f)
                                            }
                                        }
                                    } else {
                                        if (health < 20) {
                                            player.walkSpeed = walkSpeed + 0.01f
                                            player.health = health + 1
                                            player.spawnParticle(Particle.HEART, loc.x, loc.blockY + 2.toDouble(), loc.z, 3, 0.1, 0.0, 0.1)
                                        }
                                    }
                                }
                            } else {
                                if (player.isSneaking) {
                                    if (location!!.blockX == loc.blockX && location.blockY == loc.blockY && location.blockZ == loc.blockZ) {
                                        if (health < 20) {
                                            if (health % 2 == 0.0) {
                                                player.walkSpeed = walkSpeed + 0.02f
                                                player.health = health + 2
                                                player.spawnParticle(Particle.HEART, loc.x, loc.blockY + 1.6, loc.z, 3, 0.1, 0.0, 0.1)
                                            } else {
                                                player.walkSpeed = walkSpeed + 0.01f
                                                player.health = health + 1
                                                player.spawnParticle(Particle.HEART, loc.x, loc.blockY + 1.6, loc.z, 3, 0.1, 0.0, 0.1)
                                            }
                                        }
                                    } else {
                                        if (health < 20) {
                                            player.walkSpeed = walkSpeed + 0.01f
                                            player.health = health + 1
                                            player.spawnParticle(Particle.HEART, loc.x, loc.blockY + 1.6, loc.z, 3, 0.1, 0.0, 0.1)
                                        }
                                    }
                                } else {
                                    if (location!!.blockX == loc.blockX && location.blockY == loc.blockY && location.blockZ == loc.blockZ) {
                                        if (!player.isSprinting) {
                                            if (health < 20) {
                                                player.walkSpeed = walkSpeed + 0.01f
                                                player.health = health + 1
                                                player.spawnParticle(Particle.HEART, loc.x, loc.blockY + 2.toDouble(), loc.z, 3, 0.1, 0.0, 0.1)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else {
                    player.walkSpeed = 0.2f
                    player.health = 20.0
                }
            } else {
                player.walkSpeed = 0.2f
                player.health = 20.0
            }
            setHealth(player)
        }
    }

    /**
     * ゲーム終了
     */
    private fun sendResult() {
        val rewardEntries = MoneyManager.rewardMap.entries
        val rewardMap = rewardEntries.filter { MainAPI.isOnline(it.key) && it.value > 0 }.sortedBy { it.value * -1 }
        val rewardList = if (rewardMap.size > 5) rewardMap.subList(0, 5) else rewardMap

        var rewardCount = 1
        val rewardBuilder = StringBuilder()

        for ((key, value) in rewardList)
            rewardBuilder.append("${MainAPI.getPrefix(PrefixType.SECONDARY)}${ChatColor.GOLD}${rewardCount++}位${ChatColor.GRAY}: ${ChatColor.YELLOW}${Bukkit.getPlayer(key)!!.name}${ChatColor.GRAY} (${MoneyManager.formatMoney(value)})\n")

        val rewardResult = rewardBuilder.toString().trim()

        val textComponent1 = TextComponent("${MainAPI.getPrefix(PrefixType.SUCCESS)}賞金ランキング\n${if (!rewardResult.isEmpty()) rewardResult else "${MainAPI.getPrefix(PrefixType.SECONDARY)}なし"}\n${MainAPI.getPrefix(PrefixType.WARNING)}")
        val textComponent2 = TextComponent("${ChatColor.BOLD}${ChatColor.UNDERLINE}ここ${ChatColor.RESET}")
        textComponent2.clickEvent = ClickEvent(ClickEvent.Action.RUN_COMMAND, "/result reward")
        val textComponent3 = TextComponent("${ChatColor.YELLOW}をクリックして詳細を確認できます。")
        textComponent1.addExtra(textComponent2)
        textComponent1.addExtra(textComponent3)

        val hunterEntries = onDamage.hunterMap.entries
        val hunterMap = hunterEntries.filter { MainAPI.isOnline(it.key) && it.value > 0 }.sortedBy { it.value * -1 }
        val hunterList = if (hunterMap.size > 5) hunterMap.subList(0, 5) else hunterMap

        var hunterCount = 1
        val hunterBuilder = StringBuilder()

        for ((key, value) in hunterList)
            hunterBuilder.append("${MainAPI.getPrefix(PrefixType.SECONDARY)}${ChatColor.GOLD}${hunterCount++}位${ChatColor.GRAY}: ${ChatColor.YELLOW}${Bukkit.getPlayer(key)!!.name}${ChatColor.GRAY} ($value)\n")

        val hunterResult = hunterBuilder.toString().trim()

        val textComponent4 = TextComponent("${MainAPI.getPrefix(PrefixType.SUCCESS)}確保数ランキング\n${if (!hunterResult.isEmpty()) hunterResult else "${MainAPI.getPrefix(PrefixType.SECONDARY)}なし"}\n${MainAPI.getPrefix(PrefixType.WARNING)}")
        val textComponent5 = TextComponent("${ChatColor.BOLD}${ChatColor.UNDERLINE}ここ${ChatColor.RESET}")
        textComponent5.clickEvent = ClickEvent(ClickEvent.Action.RUN_COMMAND, "/result ensure")
        val textComponent6 = TextComponent("${ChatColor.YELLOW}をクリックして詳細を確認できます。")
        textComponent4.addExtra(textComponent5)
        textComponent4.addExtra(textComponent6)

        Bukkit.spigot().broadcast(textComponent1)
        Bukkit.spigot().broadcast(textComponent4)
    }
}
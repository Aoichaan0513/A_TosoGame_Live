package jp.aoichaan0513.A_TosoGame_Live.Commands.Command

import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.BossBarManager
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.GameManager
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.MoneyManager
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.Player.DifficultyManager
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.Player.PlayerManager
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.Player.VisibilityManager
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.World.WorldManager
import jp.aoichaan0513.A_TosoGame_Live.API.Scoreboard.Teams
import jp.aoichaan0513.A_TosoGame_Live.API.TosoGameAPI
import jp.aoichaan0513.A_TosoGame_Live.Commands.ICommand
import jp.aoichaan0513.A_TosoGame_Live.Listeners.Minecraft.onDamage
import jp.aoichaan0513.A_TosoGame_Live.Listeners.Minecraft.onInteract
import jp.aoichaan0513.A_TosoGame_Live.Listeners.Minecraft.onInventory
import jp.aoichaan0513.A_TosoGame_Live.Main
import jp.aoichaan0513.A_TosoGame_Live.Mission.HunterZone
import jp.aoichaan0513.A_TosoGame_Live.Mission.MissionManager
import jp.aoichaan0513.A_TosoGame_Live.Mission.TimedDevice
import jp.aoichaan0513.A_TosoGame_Live.OPGame.OPGameManager
import jp.aoichaan0513.A_TosoGame_Live.Runnable.RespawnRunnable
import jp.aoichaan0513.A_TosoGame_Live.Utils.ParseUtil
import jp.aoichaan0513.A_TosoGame_Live.Utils.setSidebar
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.GameMode
import org.bukkit.command.BlockCommandSender
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player

class Game(name: String) : ICommand(name) {

    override fun onPlayerCommand(sp: Player, cmd: Command, label: String, args: Array<String>) {
        runCommand(sp, cmd, label, args)
    }

    override fun onBlockCommand(bs: BlockCommandSender, cmd: Command, label: String, args: Array<String>) {
        runCommand(bs, cmd, label, args)
    }

    override fun onConsoleCommand(cs: ConsoleCommandSender, cmd: Command, label: String, args: Array<String>) {
        runCommand(cs, cmd, label, args)
    }


    override fun onPlayerTabComplete(sp: Player, cmd: Command, alias: String, args: Array<String>): List<String>? {
        return getTabList(sp, alias, args)
    }

    override fun onBlockTabComplete(bs: BlockCommandSender, cmd: Command, alias: String, args: Array<String>): List<String>? {
        return getTabList(bs, alias, args)
    }

    override fun onConsoleTabComplete(cs: ConsoleCommandSender, cmd: Command, alias: String, args: Array<String>): List<String>? {
        return getTabList(cs, alias, args)
    }


    private fun runCommand(sender: CommandSender, cmd: Command, label: String, args: Array<String>) {
        if (!TosoGameAPI.isPlayer(sender) || TosoGameAPI.isAdmin(sender as Player)) {
            when (label) {
                "game" -> {
                    if (args.isNotEmpty()) {
                        when (args[0]) {
                            "start" -> runStart(sender, if (args.size > 1) args[1] else null)
                            "end" -> runEnd(sender)
                            "reset" -> runReset(sender)
                            else -> sendHelpMessage(sender, label)
                        }
                        return
                    }
                    sendHelpMessage(sender, label)
                    return
                }
                "start" -> runStart(sender, if (args.isNotEmpty()) args[0] else null)
                "end", "g_end" -> runEnd(sender)
                "reset" -> runReset(sender)
            }
            return
        }
        MainAPI.sendMessage(sender, MainAPI.ErrorMessage.PERMISSIONS_TEAM_ADMIN)
        return
    }


    private fun runStart(sender: CommandSender, arg: String?) {
        if (!GameManager.isGame()) {
            if (GameManager.isGame(GameManager.GameState.NONE)) {
                val worldConfig = Main.worldConfig

                if (!arg.isNullOrEmpty()) {
                    when (arg) {
                        "skip" -> {
                            GameManager.startGame(1, worldConfig.gameConfig.game)
                        }
                        else -> {
                            if (ParseUtil.isInt(arg) && arg.toInt() >= 0) {
                                Bukkit.broadcastMessage("${MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY)}カウントダウン開始")
                                GameManager.startGame(arg.toInt() + 1, worldConfig.gameConfig.game)
                                return
                            }
                            MainAPI.sendMessage(sender, MainAPI.ErrorMessage.ARGS_INTEGER)
                        }
                    }
                } else {
                    Bukkit.broadcastMessage("${MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY)}カウントダウン開始")
                    GameManager.startGame(worldConfig.gameConfig.countDown, worldConfig.gameConfig.game)
                }
                return
            }
            sender.sendMessage("""
                ${MainAPI.getPrefix(MainAPI.PrefixType.ERROR)}ゲームが終了しているため実行できません。
                ${MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY)}"${ChatColor.RED}${ChatColor.UNDERLINE}/reset${ChatColor.RESET}${ChatColor.GRAY}"でゲームをリセットしてから実行してください。
            """.trimIndent())
            return
        }
        MainAPI.sendMessage(sender, MainAPI.ErrorMessage.GAME)
    }

    private fun runEnd(sender: CommandSender) {
        if (GameManager.isGame()) {
            Bukkit.broadcastMessage("${MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY)}ゲーム終了")
            GameManager.endGame()
            return
        }
        MainAPI.sendMessage(sender, MainAPI.ErrorMessage.NOT_GAME)
    }

    private fun runReset(sender: CommandSender) {
        if (!GameManager.isGame()) {
            sender.sendMessage("${MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY)}データをリセットしています…")

            val worldConfig = Main.worldConfig

            GameManager.gameState = GameManager.GameState.NONE
            BossBarManager.resetBar()
            Main.hunterShuffleSet.clear()
            Main.tuhoShuffleSet.clear()
            VisibilityManager.clear(VisibilityManager.VisibilityType.ITEM)

            RespawnRunnable.reset()
            HunterZone.resetMission()
            TimedDevice.resetMission()
            DifficultyManager.clear()
            onDamage.hunterMap.clear()

            for (player in Bukkit.getOnlinePlayers())
                DifficultyManager.setDifficulty(player)

            OPGameManager.resetOPGame()
            MissionManager.resetMission()

            for ((key) in MoneyManager.rewardMap) {
                val playerConfig = PlayerManager.reloadConfig(key)
                playerConfig.money = playerConfig.money + MoneyManager.getReward(key)
            }

            MoneyManager.resetReward()
            MoneyManager.resetRate()

            WorldManager.world.entities.filter { it.type == EntityType.ZOMBIE }.forEach { it.remove() }
            worldConfig.hunterDoorConfig.closeHunterDoors()

            onInventory.isAllowOpen = false
            TosoGameAPI.isRes = true
            TosoGameAPI.isRunnedBonusMission = false
            onInteract.successBlockLoc = null
            onInteract.hunterZoneBlockLoc = null

            val list = Bukkit.getOnlinePlayers().toList()
            for (i in list.indices) {
                val d = i.toDouble() / 20
                val player = list[i]

                player.sendMessage("${MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY)}あなたのデータは${d}秒後にリセットが行われます。")

                Bukkit.getScheduler().runTaskLater(Main.pluginInstance, Runnable {
                    player.sendMessage("${MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY)}あなたのデータをリセットしています…")
                    Teams.setTeamOption(player)

                    player.setSidebar()

                    if (!Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_ADMIN, player)) {
                        Teams.joinTeam(Teams.OnlineTeam.TOSO_PLAYER, player)
                        player.gameMode = GameMode.ADVENTURE

                        player.teleport(worldConfig.respawnLocationConfig.getLocation(1))
                    }

                    TosoGameAPI.setItem(WorldManager.GameType.START, player)
                    TosoGameAPI.setPotionEffect(player)

                    player.walkSpeed = 0.2f
                    player.health = 20.0
                    player.foodLevel = 20

                    player.sendMessage("${MainAPI.getPrefix(MainAPI.PrefixType.SUCCESS)}あなたのデータをリセットしました。")
                    TosoGameAPI.sendInformationText(player)
                }, d.toLong())
            }

            Teams.setTeamOptions()
            BossBarManager.showBar()
            Bukkit.getScheduler().runTaskLater(Main.pluginInstance, Runnable {
                sender.sendMessage("${MainAPI.getPrefix(MainAPI.PrefixType.SUCCESS)}${Bukkit.getOnlinePlayers().size}人のデータをリセットしました。")
            }, (list.size - 1) / 20.toLong())
            return
        }
        MainAPI.sendMessage(sender, MainAPI.ErrorMessage.GAME)
    }


    private fun getTabList(sender: CommandSender, alias: String, args: Array<String>): List<String>? {
        if (TosoGameAPI.isPlayer(sender) && !TosoGameAPI.isAdmin(sender as Player)) return emptyList()
        if (args.size == 1) {
            return when (alias.toLowerCase()) {
                "game" -> getTabList(args[0], "start", "end", "reset")
                "start" -> getTabList(args[0], "skip")
                else -> emptyList()
            }
        } else if (args.size == 2) {
            return when (alias.toLowerCase()) {
                "game" -> getTabList(args[1], when (args[0].toLowerCase()) {
                    "start" -> mutableSetOf("skip")
                    else -> emptySet()
                })
                else -> emptyList()
            }
        }
        return emptyList()
    }


    private fun sendHelpMessage(sender: CommandSender, label: String) {
        sender.sendMessage("""
            ${MainAPI.getPrefix(MainAPI.PrefixType.ERROR)}引数が不正です。
            ${MainAPI.getPrefix(MainAPI.PrefixType.ERROR)}コマンドの使い方:
            ${MainAPI.getPrefix(MainAPI.PrefixType.ERROR)}"/$label start [skip / 整数]" または "/start [skip / 秒数] - ゲームを開始
            ${MainAPI.getPrefix(MainAPI.PrefixType.ERROR)}"/$label end" または "/end" ("/g_end") - ゲームを終了
            ${MainAPI.getPrefix(MainAPI.PrefixType.ERROR)}"/$label reset" または "/reset" - ゲームをリセット
        """.trimIndent())
    }
}
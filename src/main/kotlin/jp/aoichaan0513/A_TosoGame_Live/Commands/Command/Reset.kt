package jp.aoichaan0513.A_TosoGame_Live.Commands.Command

import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI
import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI.ErrorMessage
import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI.PrefixType
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.BossBarManager
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.GameManager
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.GameManager.GameState
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.MoneyManager
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.Player.DifficultyManager
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.Player.PlayerManager
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.Player.VisibilityManager
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.World.WorldManager
import jp.aoichaan0513.A_TosoGame_Live.API.Scoreboard.Teams
import jp.aoichaan0513.A_TosoGame_Live.API.Scoreboard.Teams.OnlineTeam
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
import jp.aoichaan0513.A_TosoGame_Live.Utils.setSidebar
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.command.BlockCommandSender
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player

class Reset(name: String) : ICommand(name) {
    override fun onPlayerCommand(sp: Player, cmd: Command, label: String, args: Array<String>) {
        if (TosoGameAPI.isAdmin(sp)) {
            if (!GameManager.isGame()) {
                reset(sp)
                return
            }
            MainAPI.sendMessage(sp, ErrorMessage.GAME)
            return
        }
        MainAPI.sendMessage(sp, ErrorMessage.PERMISSIONS)
        return
    }

    override fun onBlockCommand(bs: BlockCommandSender, cmd: Command, label: String, args: Array<String>) {
        if (!GameManager.isGame()) {
            reset(bs)
            return
        }
        MainAPI.sendMessage(bs, ErrorMessage.GAME)
        return
    }

    override fun onConsoleCommand(cs: ConsoleCommandSender, cmd: Command, label: String, args: Array<String>) {
        MainAPI.sendMessage(cs, ErrorMessage.NOT_PLAYER)
        return
    }

    override fun onPlayerTabComplete(sp: Player, cmd: Command, alias: String, args: Array<String>): List<String>? {
        return null
    }

    override fun onBlockTabComplete(bs: BlockCommandSender, cmd: Command, alias: String, args: Array<String>): List<String>? {
        return null
    }

    override fun onConsoleTabComplete(cs: ConsoleCommandSender, cmd: Command, alias: String, args: Array<String>): List<String>? {
        return null
    }

    private fun reset(sender: CommandSender) {
        val worldConfig = Main.worldConfig

        sender.sendMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}データをリセットしています…")

        GameManager.gameState = GameState.NONE
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

            player.sendMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}あなたのデータは${d}秒後にリセットが行われます。")

            Bukkit.getScheduler().runTaskLater(Main.pluginInstance, Runnable {
                player.sendMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}あなたのデータをリセットしています…")
                Teams.setTeamOption(player)

                player.setSidebar()

                if (!Teams.hasJoinedTeam(OnlineTeam.TOSO_ADMIN, player)) {
                    Teams.joinTeam(OnlineTeam.TOSO_PLAYER, player)
                    player.gameMode = GameMode.ADVENTURE

                    player.teleport(worldConfig.respawnLocationConfig.getLocation(1))
                }

                TosoGameAPI.setItem(WorldManager.GameType.START, player)
                TosoGameAPI.setPotionEffect(player)

                player.walkSpeed = 0.2f
                player.health = 20.0
                player.foodLevel = 20

                player.sendMessage("${MainAPI.getPrefix(PrefixType.SUCCESS)}あなたのデータをリセットしました。")
                TosoGameAPI.sendInformationText(player)
            }, d.toLong())
        }

        Teams.setTeamOptions()
        BossBarManager.showBar()
        Bukkit.getScheduler().runTaskLater(Main.pluginInstance, Runnable {
            sender.sendMessage("${MainAPI.getPrefix(PrefixType.SUCCESS)}${Bukkit.getOnlinePlayers().size}人のデータをリセットしました。")
        }, (list.size - 1) / 20.toLong())
        return
    }
}
package jp.aoichaan0513.A_TosoGame_Live.Listeners.Minecraft

import jp.aoichaan0513.A_TosoGame_Live.API.Advancement.Advancement
import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI
import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI.PrefixType
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.BossBarManager
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.GameManager
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.MoneyManager
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.Player.DifficultyManager
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.Player.PlayerConfig
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.Player.PlayerManager
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.World.WorldManager.GameType
import jp.aoichaan0513.A_TosoGame_Live.API.Map.MapUtility
import jp.aoichaan0513.A_TosoGame_Live.API.Scoreboard.Scoreboard
import jp.aoichaan0513.A_TosoGame_Live.API.Scoreboard.Teams
import jp.aoichaan0513.A_TosoGame_Live.API.Scoreboard.Teams.OnlineTeam
import jp.aoichaan0513.A_TosoGame_Live.API.TosoGameAPI
import jp.aoichaan0513.A_TosoGame_Live.Main
import jp.aoichaan0513.A_TosoGame_Live.Mission.HunterZone
import jp.aoichaan0513.A_TosoGame_Live.Mission.MissionManager
import jp.aoichaan0513.A_TosoGame_Live.OPGame.OPGameManager
import jp.aoichaan0513.A_TosoGame_Live.Runnable.RespawnRunnable
import jp.aoichaan0513.A_TosoGame_Live.Utils.isJailTeam
import jp.aoichaan0513.A_TosoGame_Live.Utils.isPlayerGroup
import jp.aoichaan0513.A_TosoGame_Live.Utils.isPlayerTeam
import org.bukkit.ChatColor
import org.bukkit.GameMode
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class onJoin : Listener {

    @EventHandler
    fun onJoin(e: PlayerJoinEvent) {
        val p = e.player

        e.joinMessage = if (!PlayerManager.loadConfig(p).visibility) "${ChatColor.YELLOW}-> ${ChatColor.GOLD}${p.name}" else ""
        p.sendMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}リソースパックを適用するには\"${ChatColor.GOLD}${ChatColor.UNDERLINE}/resource${ChatColor.RESET}${ChatColor.GRAY}\"と入力してください。")

        MainAPI.sendDebugMessage(p, """
            ${MainAPI.getPrefix(PrefixType.ERROR)}${ChatColor.DARK_RED}${ChatColor.BOLD}${ChatColor.UNDERLINE}デバッグモードが有効${ChatColor.RESET}${ChatColor.RED}になっています。
            ${MainAPI.getPrefix(PrefixType.SECONDARY)}本番環境では必ずデバッグモードを無効にしてください。
        """.trimIndent())

        val worldConfig = Main.worldConfig
        val playerConfig = PlayerManager.loadConfig(p)

        playerConfig.bookForegroundColor = PlayerConfig.BookForegroundColor.BLACK

        if (playerConfig.advancementConfig.hasAdvancement(Advancement.FIRST_JOIN) && !playerConfig.advancementConfig.hasAdvancement(Advancement.UPDATE_2_0_0))
            Advancement.UPDATE_2_0_0.addAdvancement(p)

        Advancement.FIRST_JOIN.addAdvancement(p)

        val difficulty = playerConfig.difficulty
        if (!DifficultyManager.isDifficulty(p))
            DifficultyManager.setDifficulty(p, difficulty)

        val difficultyConfig = worldConfig.getDifficultyConfig(difficulty)

        if (TosoGameAPI.isAdmin(p) && MapUtility.map == null)
            p.sendMessage("""
                ${MainAPI.getPrefix(PrefixType.WARNING)}マップの設定がまだ完了していません。
                ${MainAPI.getPrefix(PrefixType.SECONDARY)}マップの設定をした後に"${ChatColor.GOLD}${ChatColor.UNDERLINE}/map generate${ChatColor.RESET}${ChatColor.GRAY}"を実行してください。
            """.trimIndent())

        BossBarManager.showBar(p)

        if (!MoneyManager.hasRate(p))
            MoneyManager.setRate(p, difficultyConfig.rate)

        Scoreboard.addHidePlayer(p)
        TosoGameAPI.setPotionEffect(p)

        MissionManager.setBook(p)
        if (MissionManager.isBossBar)
            MissionManager.bossBar!!.addPlayer(p)

        if (HunterZone.internalMissionState == MissionManager.InternalMissionState.END
                && p.isPlayerGroup && !HunterZone.containsCodeSet(p)) {
            p.sendMessage("""
                ${MainAPI.getPrefix(PrefixType.WARNING)}あなたはコードを入力していないため発光しました。
                ${MainAPI.getPrefix(PrefixType.SECONDARY)}解除するには"${ChatColor.UNDERLINE}/code 入手したコード${ChatColor.RESET}${ChatColor.GRAY}"と入力して装置を解除してください。
            """.trimIndent())
            p.addPotionEffect(PotionEffect(PotionEffectType.GLOWING, 200000, 1, false, false))
        }

        if (p.isPlayerTeam) {
            Teams.joinTeam(OnlineTeam.TOSO_PLAYER, p)
            p.gameMode = GameMode.ADVENTURE

            TosoGameAPI.removeOp(p)

            if (OPGameManager.opGameState != OPGameManager.OPGameState.NONE)
                TosoGameAPI.teleport(p, worldConfig.opGameLocationConfig.gOPLocations.values)
            if (HunterZone.containsLeavedSet(p)) {
                TosoGameAPI.teleport(p, worldConfig.respawnLocationConfig.locations.values)
                HunterZone.removeLeavedSet(p)
            }
        } else if (p.isJailTeam) {
            Teams.joinTeam(OnlineTeam.TOSO_JAIL, p)
            p.gameMode = GameMode.ADVENTURE

            TosoGameAPI.setItem(GameType.START, p)
            TosoGameAPI.setPotionEffect(p)
            TosoGameAPI.removeOp(p)

            RespawnRunnable.setAutoTime(p)
            RespawnRunnable.setCoolTime(p)

            TosoGameAPI.teleport(p, worldConfig.jailLocationConfig.locations.values)
        } else if (!Teams.hasJoinedTeams(p)) {
            if (GameManager.isGame()) {
                if (TosoGameAPI.isRespawn) {
                    Teams.joinTeam(OnlineTeam.TOSO_PLAYER, p)
                    p.gameMode = GameMode.ADVENTURE

                    TosoGameAPI.setItem(GameType.START, p)
                    TosoGameAPI.setPotionEffect(p)
                    TosoGameAPI.removeOp(p)

                    if (OPGameManager.opGameState != OPGameManager.OPGameState.NONE)
                        TosoGameAPI.teleport(p, worldConfig.opGameLocationConfig.gOPLocations.values)
                    else
                        TosoGameAPI.teleport(p, worldConfig.respawnLocationConfig.locations.values)
                } else {
                    Teams.joinTeam(OnlineTeam.TOSO_JAIL, p)
                    p.gameMode = GameMode.ADVENTURE

                    TosoGameAPI.setItem(GameType.START, p)
                    TosoGameAPI.setPotionEffect(p)
                    TosoGameAPI.removeOp(p)

                    TosoGameAPI.teleport(p, worldConfig.jailLocationConfig.locations.values)

                    p.sendMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}途中参加が禁止のため牢獄に追加されました。")
                }
            } else {
                Teams.joinTeam(OnlineTeam.TOSO_PLAYER, p)
                p.gameMode = GameMode.ADVENTURE

                TosoGameAPI.setItem(GameType.START, p)
                TosoGameAPI.setPotionEffect(p)
                TosoGameAPI.removeOp(p)

                TosoGameAPI.teleport(p, worldConfig.respawnLocationConfig.locations.values)
            }
        }

        /*
        for (player in Bukkit.getOnlinePlayers())
            player.setSidebar()
        */

        TosoGameAPI.sendInformationText(p)

        val scoreboard = Scoreboard.getBoard(p)
        Teams.setTeamOption(scoreboard)
        p.scoreboard = scoreboard
        //  Teams.setTeamOptions()
        return
    }
}
package jp.aoichaan0513.A_TosoGame_Live.Commands.Command

import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI
import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI.ErrorMessage
import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI.PrefixType
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.GameManager
import jp.aoichaan0513.A_TosoGame_Live.API.Scoreboard.Teams
import jp.aoichaan0513.A_TosoGame_Live.API.Scoreboard.Teams.OnlineTeam
import jp.aoichaan0513.A_TosoGame_Live.API.TosoGameAPI
import jp.aoichaan0513.A_TosoGame_Live.Commands.ICommand
import jp.aoichaan0513.A_TosoGame_Live.Listeners.Minecraft.onInteract
import jp.aoichaan0513.A_TosoGame_Live.Utils.isAdminTeam
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.GameMode
import org.bukkit.command.BlockCommandSender
import org.bukkit.command.Command
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.entity.EntityType
import org.bukkit.entity.MagmaCube
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class Btp(name: String) : ICommand(name) {
    override fun onPlayerCommand(sp: Player, cmd: Command, label: String, args: Array<String>) {
        if (TosoGameAPI.isAdmin(sp)) {
            if (!GameManager.isGame()) {
                val loc = onInteract.successBlockLoc?.clone()?.add(0.5, 0.0, 0.5)
                if (loc != null) {
                    val world = loc.world
                    loc.yaw = 0f
                    loc.pitch = 0f
                    val magmaCube = world?.spawnEntity(loc, EntityType.MAGMA_CUBE) as? MagmaCube
                    magmaCube?.setAI(false)
                    magmaCube?.setGravity(false)
                    magmaCube?.isInvulnerable = true
                    magmaCube?.isGlowing = true
                    magmaCube?.size = 2
                    magmaCube?.addPotionEffect(PotionEffect(PotionEffectType.INVISIBILITY, 9999, 255, false, false))

                    for (p in Bukkit.getOnlinePlayers().filter { !it.isAdminTeam }) {
                        p.gameMode = GameMode.SPECTATOR
                        TosoGameAPI.teleport(p, loc)
                    }
                    sp.sendMessage("${MainAPI.getPrefix(PrefixType.SECONDARY)}${ChatColor.GREEN}${ChatColor.UNDERLINE}${Bukkit.getOnlinePlayers().size - Teams.getOnlineCount(OnlineTeam.TOSO_ADMIN)}人${ChatColor.RESET}${ChatColor.GRAY}を生存ブロックの位置にテレポートしました。")
                    return
                }
                sp.sendMessage("${MainAPI.getPrefix(PrefixType.ERROR)}生存ミッションを実行していないため実行できません。")
                return
            }
            sp.sendMessage("${MainAPI.getPrefix(PrefixType.ERROR)}ゲームが実行されているため実行できません。")
            return
        }
        MainAPI.sendMessage(sp, ErrorMessage.PERMISSIONS_TEAM_ADMIN)
    }

    override fun onBlockCommand(bs: BlockCommandSender, cmd: Command, label: String, args: Array<String>) {
        MainAPI.sendMessage(bs, ErrorMessage.NOT_PLAYER)
    }

    override fun onConsoleCommand(cs: ConsoleCommandSender, cmd: Command, label: String, args: Array<String>) {
        MainAPI.sendMessage(cs, ErrorMessage.NOT_PLAYER)
    }

    override fun onPlayerTabComplete(sp: Player, cmd: Command, alias: String, args: Array<String>): List<String>? {
        return emptyList()
    }

    override fun onBlockTabComplete(bs: BlockCommandSender, cmd: Command, alias: String, args: Array<String>): List<String>? {
        return emptyList()
    }

    override fun onConsoleTabComplete(cs: ConsoleCommandSender, cmd: Command, alias: String, args: Array<String>): List<String>? {
        return emptyList()
    }
}
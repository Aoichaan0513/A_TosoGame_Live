package jp.aoichaan0513.A_TosoGame_Live.Commands.Command

import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI
import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI.ErrorMessage
import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI.PrefixType
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.GameManager
import jp.aoichaan0513.A_TosoGame_Live.Commands.ICommand
import jp.aoichaan0513.A_TosoGame_Live.Mission.HunterZone
import jp.aoichaan0513.A_TosoGame_Live.Utils.isPlayerGroup
import org.bukkit.command.BlockCommandSender
import org.bukkit.command.Command
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffectType

class Code(name: String) : ICommand(name) {

    override fun onPlayerCommand(sp: Player, cmd: Command, label: String, args: Array<String>) {
        if (sp.isPlayerGroup) {
            if (GameManager.isGame(GameManager.GameState.GAME) && HunterZone.isStart) {
                if (args.isNotEmpty()) {
                    if (!HunterZone.containsCodeSet(sp)) {
                        if (HunterZone.code.equals(args[0])) {
                            HunterZone.addCodeSet(sp)
                            if (sp.hasPotionEffect(PotionEffectType.GLOWING))
                                sp.removePotionEffect(PotionEffectType.GLOWING)
                            sp.sendMessage("${MainAPI.getPrefix(PrefixType.SUCCESS)}認証に成功しました。")
                            return
                        }
                        sp.sendMessage("${MainAPI.getPrefix(PrefixType.ERROR)}引数が不正です。コードが違います。")
                        return
                    }
                    sp.sendMessage("${MainAPI.getPrefix(PrefixType.ERROR)}すでに認証しています。")
                    return
                }
                sp.sendMessage("${MainAPI.getPrefix(PrefixType.ERROR)}引数が不正です。コードを入力してください。")
                return
            }
            sp.sendMessage("${MainAPI.getPrefix(PrefixType.ERROR)}ハンターゾーンミッションが実行されていないため実行できません。")
            return
        }
        MainAPI.sendMessage(sp, ErrorMessage.PERMISSIONS)
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
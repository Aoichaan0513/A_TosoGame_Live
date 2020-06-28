package jp.aoichaan0513.A_TosoGame_Live.Commands.Command

import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI
import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI.ErrorMessage
import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI.PrefixType
import jp.aoichaan0513.A_TosoGame_Live.Commands.ICommand
import jp.aoichaan0513.A_TosoGame_Live.Main
import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.TextComponent
import org.apache.commons.lang.RandomStringUtils
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.BlockCommandSender
import org.bukkit.command.Command
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.entity.Player
import java.util.*

class Resource(name: String) : ICommand(name) {

    private val set = mutableSetOf<UUID>()

    override fun onPlayerCommand(sp: Player, cmd: Command, label: String, args: Array<String>) {
        if (set.contains(sp.uniqueId)) {
            val url = "${Main.mainConfig.getString("resourcePack", "https://aoichaan0513.jp/private/TosoGame-DebugRP.zip")}?${RandomStringUtils.randomNumeric(12)}"

            val textComponent1 = TextComponent("""
                ${MainAPI.getPrefix(PrefixType.WARNING)}リソースパックを適用します。
                ${MainAPI.getPrefix(PrefixType.SECONDARY)}適用されない場合は、
            """.trimIndent())
            val textComponent2 = TextComponent("${ChatColor.GRAY}${ChatColor.BOLD}${ChatColor.UNDERLINE}ここ${ChatColor.RESET}${ChatColor.GRAY}")
            textComponent2.clickEvent = ClickEvent(ClickEvent.Action.OPEN_URL, url)
            val textComponent3 = TextComponent("${ChatColor.GRAY}を押してダウンロードしてください。")
            textComponent1.addExtra(textComponent2)
            textComponent1.addExtra(textComponent3)

            sp.spigot().sendMessage(textComponent1)
            sp.setResourcePack(url)
            set.remove(sp.uniqueId)
        } else {
            sp.sendMessage("""
                ${MainAPI.getPrefix(PrefixType.WARNING)}リソースパックを適用しますか？
                ${MainAPI.getPrefix(PrefixType.WARNING)}適用する場合は以下の注意事項をよく読み、同意できる場合は15秒以内に"${ChatColor.UNDERLINE}/$label${ChatColor.RESET}${ChatColor.GOLD}"と入力してください。
                ${MainAPI.getPrefix(PrefixType.SECONDARY)}リソースパックは逃走中のデバッグ専用です。
                ${MainAPI.getPrefix(PrefixType.SECONDARY)}他のサーバー・ワールドでの利用はお止めください。
                ${MainAPI.getPrefix(PrefixType.SECONDARY)}また、リソースパック内のファイルの無断利用・複製等もお止めください。
            """.trimIndent())

            set.add(sp.uniqueId)
            Bukkit.getScheduler().runTaskLaterAsynchronously(Main.pluginInstance, Runnable { set.remove(sp.uniqueId) }, 20 * 15.toLong())
        }
    }

    override fun onBlockCommand(bs: BlockCommandSender, cmd: Command, label: String, args: Array<String>) {
        MainAPI.sendMessage(bs, ErrorMessage.NOT_PLAYER)
    }

    override fun onConsoleCommand(cs: ConsoleCommandSender, cmd: Command, label: String, args: Array<String>) {
        MainAPI.sendMessage(cs, ErrorMessage.NOT_PLAYER)
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
}
package jp.aoichaan0513.A_TosoGame_Live.Commands.Command

import com.mojang.authlib.properties.Property
import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI
import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI.ErrorMessage
import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI.PrefixType
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.SkinManager
import jp.aoichaan0513.A_TosoGame_Live.API.TosoGameAPI
import jp.aoichaan0513.A_TosoGame_Live.Commands.ICommand
import jp.aoichaan0513.A_TosoGame_Live.Main
import org.bukkit.Bukkit
import org.bukkit.command.BlockCommandSender
import org.bukkit.command.Command
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer
import org.bukkit.entity.Player
import java.util.*
import java.util.regex.Pattern

class Nick(name: String) : ICommand(name) {
    companion object {

        private val hashMap = mutableMapOf<UUID, SkinManager?>()
    }

    override fun onPlayerCommand(sp: Player, cmd: Command, label: String, args: Array<String>) {
        if (TosoGameAPI.isAdmin(sp) || TosoGameAPI.isBroadCaster(sp)) {
            if (args.isNotEmpty()) {
                if (Pattern.matches("^[0-9a-zA-Z_]+$", args[0])) {
                    sp.setDisplayName(args[0])
                    sp.setPlayerListName(args[0])
                    if (args.size != 1) {
                        if (Pattern.matches("^[0-9a-zA-Z_]+$", args[1])) {
                            val skinPlayer = Bukkit.getOfflinePlayer(args[1])
                            sp.setDisplayName(args[0])
                            sp.setPlayerListName(args[0])

                            val gp = (sp as CraftPlayer).profile
                            gp.properties.clear()

                            val skin = hashMap[skinPlayer.uniqueId] ?: SkinManager(getUUID(skinPlayer.uniqueId))
                            if (!hashMap.containsKey(skinPlayer.uniqueId))
                                hashMap[skinPlayer.uniqueId] = skin
                            if (skin.skinName != null)
                                gp.properties.put(skin.skinName, Property(skin.skinName, skin.skinValue, skin.skinSignatur))

                            for (player in Bukkit.getOnlinePlayers())
                                player.hidePlayer(sp)
                            Bukkit.getScheduler().runTask(Main.pluginInstance, Runnable {
                                for (player in Bukkit.getOnlinePlayers())
                                    player.showPlayer(sp)
                            })

                            sp.sendMessage("${MainAPI.getPrefix(PrefixType.SUCCESS)}ニックネーム・スキンを設定しました。")
                            return
                        }
                        sp.sendMessage("${MainAPI.getPrefix(PrefixType.ERROR)}引数が不正です。半角英数字で指定してください。")
                        return
                    } else {
                        sp.sendMessage("${MainAPI.getPrefix(PrefixType.SUCCESS)}ニックネームを設定しました。")
                        return
                    }
                }
                sp.sendMessage("${MainAPI.getPrefix(PrefixType.ERROR)}引数が不正です。半角英数字で指定してください。")
                return
            } else {
                sp.setDisplayName(sp.name)
                sp.setPlayerListName(sp.name)

                val gp = (sp as CraftPlayer).profile
                gp.properties.clear()

                val skin = hashMap[sp.uniqueId] ?: SkinManager(getUUID(sp.uniqueId))
                if (!hashMap.containsKey(sp.uniqueId))
                    hashMap[sp.uniqueId] = skin
                if (skin.skinName != null)
                    gp.properties.put(skin.skinName, Property(skin.skinName, skin.skinValue, skin.skinSignatur))

                for (player in Bukkit.getOnlinePlayers())
                    player.hidePlayer(sp)
                Bukkit.getScheduler().runTask(Main.pluginInstance, Runnable {
                    for (player in Bukkit.getOnlinePlayers())
                        player.showPlayer(sp)
                })

                sp.sendMessage("${MainAPI.getPrefix(PrefixType.SUCCESS)}ニックネーム・スキンをリセットしました。")
                return
            }
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
        return null
    }

    override fun onBlockTabComplete(bs: BlockCommandSender, cmd: Command, alias: String, args: Array<String>): List<String>? {
        return emptyList()
    }

    override fun onConsoleTabComplete(cs: ConsoleCommandSender, cmd: Command, alias: String, args: Array<String>): List<String>? {
        return emptyList()
    }

    fun getUUID(uuid: UUID): String {
        return uuid.toString().replace("-", "")
    }
}
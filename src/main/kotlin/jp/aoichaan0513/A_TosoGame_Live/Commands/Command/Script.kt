package jp.aoichaan0513.A_TosoGame_Live.Commands.Command

import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI
import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI.ErrorMessage
import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI.PrefixType
import jp.aoichaan0513.A_TosoGame_Live.Commands.ICommand
import jp.aoichaan0513.A_TosoGame_Live.Main
import org.bukkit.command.BlockCommandSender
import org.bukkit.command.Command
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.entity.Player
import java.io.File
import javax.script.Invocable
import javax.script.ScriptEngine
import javax.script.ScriptEngineManager
import javax.script.ScriptException

class Script(name: String) : ICommand(name) {
    companion object {

        fun load(engine: ScriptEngine, file: File) {
            try {
                engine.eval(file.readText(Main.CHARSET))
            } catch (err: ScriptException) {
                err.printStackTrace()
            }
        }

        fun getFile(fileName: String): File {
            return File("${Main.pluginInstance.getDataFolder()}${Main.FILE_SEPARATOR}scripts${Main.FILE_SEPARATOR}commands${Main.FILE_SEPARATOR}$fileName.js")
        }

        fun hasFile(fileName: String): Boolean {
            return getFile(fileName).exists()
        }
    }

    override fun onPlayerCommand(sp: Player, cmd: Command, label: String, args: Array<String>) {
        val worldConfig = Main.worldConfig

        if (worldConfig.gameConfig.script) {
            if (args.isNotEmpty()) {
                if (hasFile(args[0])) {
                    val strs = if (args.size > 1) args.filterIndexed { index, _ -> index > 0 } else emptyList()

                    val manager = ScriptEngineManager()
                    val engine = manager.getEngineByName("nashorn")
                    load(engine, getFile(args[0]))

                    val invocable = engine as Invocable
                    try {
                        invocable.invokeFunction("onCommand", sp, strs)
                    } catch (e: NoSuchMethodException) {
                        e.printStackTrace()
                    } catch (e: ScriptException) {
                        e.printStackTrace()
                    }
                    return
                }
                sp.sendMessage("${MainAPI.getPrefix(PrefixType.ERROR)}引数が不正です。そのようなスクリプトはありません。")
                return
            }
            sp.sendMessage("${MainAPI.getPrefix(PrefixType.ERROR)}引数が不正です。スクリプト名を指定してください。")
            return
        }
        sp.sendMessage("${MainAPI.getPrefix(PrefixType.ERROR)}管理者によってスクリプト機能が無効にされています。")
        return
    }

    override fun onBlockCommand(bs: BlockCommandSender, cmd: Command, label: String, args: Array<String>) {
        MainAPI.sendMessage(bs, ErrorMessage.NOT_PLAYER)
        return
    }

    override fun onConsoleCommand(cs: ConsoleCommandSender, cmd: Command, label: String, args: Array<String>) {
        MainAPI.sendMessage(cs, ErrorMessage.NOT_PLAYER)
        return
    }

    override fun onPlayerTabComplete(sp: Player, cmd: Command, alias: String, args: Array<String>): List<String>? {
        val worldConfig = Main.worldConfig
        if (!worldConfig.gameConfig.script || args.size != 1) return null

        val set = mutableSetOf<String>()
        val file = File("${Main.pluginInstance.getDataFolder()}${Main.FILE_SEPARATOR}scripts${Main.FILE_SEPARATOR}commands")
        file.listFiles().filter { it.isFile && it.extension.equals("js") }.forEach { set.add(it.nameWithoutExtension) }
        return getTabList(args[0], set)
    }

    override fun onBlockTabComplete(bs: BlockCommandSender, cmd: Command, alias: String, args: Array<String>): List<String>? {
        return null
    }

    override fun onConsoleTabComplete(cs: ConsoleCommandSender, cmd: Command, alias: String, args: Array<String>): List<String>? {
        return null
    }
}
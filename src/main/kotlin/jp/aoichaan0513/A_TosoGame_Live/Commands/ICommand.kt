package jp.aoichaan0513.A_TosoGame_Live.Commands

import org.bukkit.command.*
import org.bukkit.entity.Player

abstract class ICommand(private val name: String) : TabExecutor {

    abstract fun onPlayerCommand(sp: Player, cmd: Command, label: String, args: Array<String>)
    abstract fun onBlockCommand(bs: BlockCommandSender, cmd: Command, label: String, args: Array<String>)
    abstract fun onConsoleCommand(cs: ConsoleCommandSender, cmd: Command, label: String, args: Array<String>)

    override fun onCommand(sender: CommandSender, cmd: Command, label: String, args: Array<String>): Boolean {
        if (sender is Player)
            onPlayerCommand(sender, cmd, label, args)
        else if (sender is BlockCommandSender)
            onBlockCommand(sender, cmd, label, args)
        else if (sender is ConsoleCommandSender)
            onConsoleCommand(sender, cmd, label, args)
        return true
    }

    abstract fun onPlayerTabComplete(sp: Player, cmd: Command, alias: String, args: Array<String>): List<String>?
    abstract fun onBlockTabComplete(bs: BlockCommandSender, cmd: Command, alias: String, args: Array<String>): List<String>?
    abstract fun onConsoleTabComplete(cs: ConsoleCommandSender, cmd: Command, alias: String, args: Array<String>): List<String>?

    override fun onTabComplete(sender: CommandSender, cmd: Command, alias: String, args: Array<String>): List<String>? {
        if (!cmd.name.equals(name, true)) return null
        if (sender is Player)
            return onPlayerTabComplete(sender, cmd, alias, args)
        else if (sender is BlockCommandSender)
            return onBlockTabComplete(sender, cmd, alias, args)
        else if (sender is ConsoleCommandSender)
            return onConsoleTabComplete(sender, cmd, alias, args)
        return null
    }

    /**
     * タブ補完用の拡張関数
     *
     * @param arg 文字列
     * @param collection Stringを要素としたコレクション
     * @return List<String> (Immutable, Not Nullable)
     *
     * @author Aoichaan0513
     */
    fun getTabList(arg: String, collection: Collection<String>) =
            (if (arg.isBlank()) collection else collection.filter { it.startsWith(arg, true) }).toList()

    /**
     * タブ補完用の拡張関数
     *
     * @param arg 文字列
     * @param iterable Stringを要素とした反復可能要素
     * @return List<String> (Immutable, Not Nullable)
     *
     * @author Aoichaan0513
     */
    fun getTabList(arg: String, iterable: Iterable<String>) = getTabList(arg, iterable.toSet())

    /**
     * タブ補完用の拡張関数
     *
     * @param arg 文字列
     * @param array Stringを要素とした配列
     * @return List<String> (Immutable, Not Nullable)
     *
     * @author Aoichaan0513
     */
    fun getTabList(arg: String, vararg array: String) = getTabList(arg, array.toSet())
}
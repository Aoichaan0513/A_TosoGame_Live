package jp.aoichaan0513.A_TosoGame_Live.Commands;

import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class ICommand implements TabExecutor {

    private final String name;

    public ICommand(String name) {
        this.name = name;
    }

    public abstract void onPlayerCommand(Player sp, Command cmd, String label, String[] args);

    public abstract void onBlockCommand(BlockCommandSender bs, Command cmd, String label, String[] args);

    public abstract void onConsoleCommand(ConsoleCommandSender cs, Command cmd, String label, String[] args);

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player)
            onPlayerCommand((Player) sender, cmd, label, args);
        else if (sender instanceof BlockCommandSender)
            onBlockCommand((BlockCommandSender) sender, cmd, label, args);
        else if (sender instanceof ConsoleCommandSender)
            onConsoleCommand((ConsoleCommandSender) sender, cmd, label, args);
        return true;
    }

    public abstract List<String> onPlayerTabComplete(Player sp, Command cmd, String alias, String[] args);

    public abstract List<String> onBlockTabComplete(BlockCommandSender bs, Command cmd, String alias, String[] args);

    public abstract List<String> onConsoleTabComplete(ConsoleCommandSender cs, Command cmd, String alias, String[] args);

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
        if (!cmd.getName().equalsIgnoreCase(name)) return null;
        if (sender instanceof Player)
            return onPlayerTabComplete((Player) sender, cmd, alias, args);
        else if (sender instanceof BlockCommandSender)
            return onBlockTabComplete((BlockCommandSender) sender, cmd, alias, args);
        else if (sender instanceof ConsoleCommandSender)
            return onConsoleTabComplete((ConsoleCommandSender) sender, cmd, alias, args);
        return null;
    }

    public List<String> getTabList(String str, Set<String> set) {
        return str.length() == 0 ? new ArrayList<>(set) : set.stream().filter(s -> s.toLowerCase().startsWith(str.toLowerCase())).collect(Collectors.toList());
    }
}

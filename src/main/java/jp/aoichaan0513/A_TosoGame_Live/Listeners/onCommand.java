package jp.aoichaan0513.A_TosoGame_Live.Listeners;

import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI;
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.World.WorldConfig;
import jp.aoichaan0513.A_TosoGame_Live.API.TosoGameAPI;
import jp.aoichaan0513.A_TosoGame_Live.Commands.Command.Script;
import jp.aoichaan0513.A_TosoGame_Live.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;

import java.util.ArrayList;
import java.util.List;

public class onCommand implements Listener {

    private static List<String> list = new ArrayList<>();

    @EventHandler
    public void onPlayerCommand(PlayerCommandPreprocessEvent e) {
        Player p = e.getPlayer();
        String cmdName = e.getMessage().split(" ")[0].split(":").length > 1 ? e.getMessage().split(" ")[0].split(":")[1] : e.getMessage().split(" ")[0].substring(1);
        String[] args = e.getMessage().split(" ").length > 1 ? e.getMessage().substring(cmdName.length() + 1).split(" ") : new String[0];

        if (!TosoGameAPI.isAdmin(p))
            p.sendMessage(ChatColor.GRAY + "> " + e.getPlayer().getName() + ": " + e.getMessage());

        for (Player player : Bukkit.getOnlinePlayers())
            if (TosoGameAPI.isAdmin(player))
                player.sendMessage(ChatColor.GRAY + "> " + e.getPlayer().getName() + ": " + e.getMessage());

        if (list.contains("/" + cmdName)) {
            if (!TosoGameAPI.isAdmin(p)) {
                e.setCancelled(true);
                p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "実行できません。");
                return;
            }
        } else {
            if (Script.hasFile(cmdName)) {
                e.setCancelled(true);

                WorldConfig worldConfig = Main.getWorldConfig();
                if (worldConfig.getGameConfig().getScript()) {
                    p.chat("/script " + cmdName.substring(1) + e.getMessage().substring(cmdName.length()));
                    return;
                } else {
                    p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "管理者によってスクリプト機能が無効にされています。");
                    return;
                }
            }
        }
    }

    @EventHandler
    public void onServerCommand(ServerCommandEvent e) {
        if (e.getSender() instanceof BlockCommandSender) {
            /*
            Block b = ((BlockCommandSender) e.getSender()).getBlock();

            Bukkit.getConsoleSender().sendMessage(ChatColor.GRAY + "> Command Block: /" + e.getCommand() + "\n" +
                    ChatColor.GRAY + "X: " + b.getX() + ", Y: " + b.getY() + ", Z:" + b.getZ());
            */
        } else if (e.getSender() instanceof ConsoleCommandSender) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.GRAY + "> Console: /" + e.getCommand());
        }
    }

    public static void addBlockCommand(String cmdName) {
        if (!list.contains("/" + cmdName))
            list.add("/" + cmdName);
    }

    public static void removeBlockCommand(String cmdName) {
        if (list.contains("/" + cmdName))
            list.remove("/" + cmdName);
    }
}

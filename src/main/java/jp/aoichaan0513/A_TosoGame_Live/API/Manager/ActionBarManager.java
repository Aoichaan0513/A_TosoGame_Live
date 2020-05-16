package jp.aoichaan0513.A_TosoGame_Live.API.Manager;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ActionBarManager {

    public static void sendActionBar(String text) {
        for (Player player : Bukkit.getOnlinePlayers())
            sendActionBar(player, text);
    }

    public static void sendActionBar(BaseComponent baseComponent) {
        for (Player player : Bukkit.getOnlinePlayers())
            sendActionBar(player, baseComponent);
    }

    public static void sendActionBar(Player p, String text) {
        sendActionBar(p, new TextComponent(text));
    }

    public static void sendActionBar(Player p, BaseComponent baseComponent) {
        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, baseComponent);
    }
}

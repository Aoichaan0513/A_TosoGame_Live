package jp.aoichaan0513.A_TosoGame_Live.Listeners;

import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

public class onSign implements Listener {

    @EventHandler
    public void onSignChange(SignChangeEvent e) {
        if (e.getLine(0).equalsIgnoreCase("逃走中") && e.getLine(1).equalsIgnoreCase("自首")) {
            e.setLine(0, MainAPI.getPrefix());
            e.setLine(1, ChatColor.BOLD + "自首");
            e.setLine(3, ChatColor.ITALIC + "クリック");
        }
    }
}

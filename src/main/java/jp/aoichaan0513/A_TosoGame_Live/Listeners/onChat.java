package jp.aoichaan0513.A_TosoGame_Live.Listeners;

import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI;
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.GameManager;
import jp.aoichaan0513.A_TosoGame_Live.API.Scoreboard.Teams;
import jp.aoichaan0513.A_TosoGame_Live.API.TosoGameAPI;
import jp.aoichaan0513.A_TosoGame_Live.Main;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class onChat implements Listener {

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();

        final String prefix = TosoGameAPI.isBroadCaster(p) ? ChatColor.GOLD + "" + ChatColor.BOLD + " * " + ChatColor.RESET : "";
        String msg = ChatColor.translateAlternateColorCodes('&', e.getMessage());

        if (GameManager.isGame()) {
            if (p.getInventory().getItemInMainHand().getType() == Material.BOOK) {
                ItemMeta meta = p.getInventory().getItemInMainHand().getItemMeta();
                if (ChatColor.stripColor(meta.getDisplayName()).equals(Main.PHONE_ITEM_NAME)) {
                    e.setCancelled(true);
                    sendMessage(p, e.getMessage(), e.getRecipients(), true);
                    return;
                } else {
                    e.setCancelled(true);
                    sendMessage(p, e.getMessage(), e.getRecipients(), false);
                    return;
                }
            } else if (p.getInventory().getItemInOffHand().getType() == Material.BOOK) {
                ItemMeta meta = p.getInventory().getItemInOffHand().getItemMeta();
                if (ChatColor.stripColor(meta.getDisplayName()).equals(Main.PHONE_ITEM_NAME)) {
                    e.setCancelled(true);
                    sendMessage(p, e.getMessage(), e.getRecipients(), true);
                    return;
                } else {
                    e.setCancelled(true);
                    sendMessage(p, e.getMessage(), e.getRecipients(), false);
                    return;
                }
            } else {
                e.setCancelled(true);
                sendMessage(p, e.getMessage(), e.getRecipients(), false);
                return;
            }
        } else {
            e.setCancelled(true);
            sendMessage(p, e.getMessage(), e.getRecipients(), false);
            return;
        }
    }

    private void sendMessage(Player p, String message, Set<Player> recipients, boolean isTeam) {
        int range = 7;

        final String regex = "^@([A-z0-9_]{2,17})";

        final String prefix = TosoGameAPI.isBroadCaster(p) ? ChatColor.GOLD + "" + ChatColor.BOLD + " * " + ChatColor.RESET : "";
        String msg = ChatColor.translateAlternateColorCodes('&', message);

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(msg);


        if (GameManager.isGame()) {
            if (isTeam) {
                if (matcher.find()) {
                    String name = matcher.group().substring(1);
                    msg = msg.substring(2 + name.length());

                    if (name.equalsIgnoreCase("team")) {
                        Bukkit.getConsoleSender().sendMessage(ChatColor.BLUE + "[T]" + ChatColor.RESET + prefix + (Teams.getTeam(Teams.DisplaySlot.CHAT, p).equals("") ? "" : Teams.getTeam(Teams.DisplaySlot.CHAT, p) + " ") + ChatColor.RESET + p.getDisplayName() + ChatColor.GREEN + ": " + ChatColor.RESET + msg);

                        if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_PLAYER, p) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_SUCCESS, p)) {
                            for (Player player : Bukkit.getOnlinePlayers())
                                if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_ADMIN, player) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_PLAYER, player) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_SUCCESS, player))
                                    player.sendMessage(ChatColor.BLUE + "[T]" + ChatColor.RESET + prefix + (Teams.getTeam(Teams.DisplaySlot.CHAT, p).equals("") ? "" : Teams.getTeam(Teams.DisplaySlot.CHAT, p) + " ") + ChatColor.RESET + p.getDisplayName() + ChatColor.GREEN + ": " + ChatColor.RESET + msg);
                            return;
                        } else if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_HUNTER, p) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_TUHO, p)) {
                            for (Player player : Bukkit.getOnlinePlayers())
                                if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_ADMIN, player) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_HUNTER, player) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_TUHO, player))
                                    player.sendMessage(ChatColor.BLUE + "[T]" + ChatColor.RESET + prefix + (Teams.getTeam(Teams.DisplaySlot.CHAT, p).equals("") ? "" : Teams.getTeam(Teams.DisplaySlot.CHAT, p) + " ") + ChatColor.RESET + p.getDisplayName() + ChatColor.GREEN + ": " + ChatColor.RESET + msg);
                            return;
                        } else if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_JAIL, p)) {
                            Bukkit.getConsoleSender().sendMessage(ChatColor.BLUE + "[T]" + ChatColor.RESET + prefix + (Teams.getTeam(Teams.DisplaySlot.CHAT, p).equals("") ? "" : Teams.getTeam(Teams.DisplaySlot.CHAT, p) + " ") + ChatColor.RESET + p.getDisplayName() + ChatColor.GREEN + ": " + ChatColor.RESET + msg);

                            for (Player player : Bukkit.getOnlinePlayers())
                                if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_ADMIN, player) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_JAIL, player))
                                    player.sendMessage(ChatColor.BLUE + "[T]" + ChatColor.RESET + prefix + (Teams.getTeam(Teams.DisplaySlot.CHAT, p).equals("") ? "" : Teams.getTeam(Teams.DisplaySlot.CHAT, p) + " ") + ChatColor.RESET + p.getDisplayName() + ChatColor.GREEN + ": " + ChatColor.RESET + msg);
                            return;
                        } else {
                            Bukkit.getConsoleSender().sendMessage(ChatColor.BLUE + "[T]" + ChatColor.RESET + prefix + (Teams.getTeam(Teams.DisplaySlot.CHAT, p).equals("") ? "" : Teams.getTeam(Teams.DisplaySlot.CHAT, p) + " ") + ChatColor.RESET + p.getDisplayName() + ChatColor.GREEN + ": " + ChatColor.RESET + msg);

                            for (Player player : Bukkit.getOnlinePlayers())
                                if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_ADMIN, player))
                                    player.sendMessage(ChatColor.BLUE + "[T]" + ChatColor.RESET + prefix + (Teams.getTeam(Teams.DisplaySlot.CHAT, p).equals("") ? "" : Teams.getTeam(Teams.DisplaySlot.CHAT, p) + " ") + ChatColor.RESET + p.getDisplayName() + ChatColor.GREEN + ": " + ChatColor.RESET + msg);
                            return;
                        }
                    } else {
                        if (Bukkit.getPlayer(name) != null) {
                            Player player = Bukkit.getPlayer(name);

                            if (player.getUniqueId() != p.getUniqueId()) {
                                if (Teams.getJoinedTeam(p) == null) return;
                                if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_PLAYER, p) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_SUCCESS, p)) {
                                    if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_ADMIN, player) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_PLAYER, player) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_SUCCESS, player)) {
                                        Bukkit.getConsoleSender().sendMessage(ChatColor.BLUE + "[P]" + ChatColor.RESET + Teams.getTeam(Teams.DisplaySlot.CHAT, p) + " " + p.getDisplayName() + ChatColor.GRAY + " -> " + ChatColor.RESET + Teams.getTeam(Teams.DisplaySlot.CHAT, player) + " " + player.getDisplayName() + ChatColor.GREEN + ": " + ChatColor.RESET + msg);

                                        for (Player p2 : Bukkit.getOnlinePlayers())
                                            if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_ADMIN, p2))
                                                p2.sendMessage(ChatColor.BLUE + "[P]" + Teams.getTeam(Teams.DisplaySlot.CHAT, p) + " " + p.getDisplayName() + ChatColor.GRAY + " -> " + ChatColor.RESET + Teams.getTeam(Teams.DisplaySlot.CHAT, player) + " " + player.getDisplayName() + ChatColor.GREEN + ": " + ChatColor.RESET + msg);

                                        p.sendMessage(Teams.getTeam(Teams.DisplaySlot.CHAT, p) + " " + p.getDisplayName() + ChatColor.GRAY + " -> " + ChatColor.RESET + Teams.getTeam(Teams.DisplaySlot.CHAT, player) + " " + player.getDisplayName() + ChatColor.GREEN + ": " + ChatColor.RESET + msg);
                                        player.sendMessage(Teams.getTeam(Teams.DisplaySlot.CHAT, p) + " " + p.getDisplayName() + ChatColor.GRAY + " -> " + ChatColor.RESET + Teams.getTeam(Teams.DisplaySlot.CHAT, player) + " " + player.getDisplayName() + ChatColor.GREEN + ": " + ChatColor.RESET + msg);
                                        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
                                        return;
                                    }
                                    p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "他のチームにはメッセージを送信できません。");
                                    return;
                                } else if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_HUNTER, p) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_TUHO, p)) {
                                    if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_ADMIN, player) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_HUNTER, player) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_TUHO, player)) {
                                        Bukkit.getConsoleSender().sendMessage(ChatColor.BLUE + "[P]" + ChatColor.RESET + Teams.getTeam(Teams.DisplaySlot.CHAT, p) + " " + p.getDisplayName() + ChatColor.GRAY + " -> " + ChatColor.RESET + Teams.getTeam(Teams.DisplaySlot.CHAT, player) + " " + player.getDisplayName() + ChatColor.GREEN + ": " + ChatColor.RESET + msg);

                                        for (Player p2 : Bukkit.getOnlinePlayers())
                                            if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_ADMIN, p2))
                                                p2.sendMessage(ChatColor.BLUE + "[P]" + Teams.getTeam(Teams.DisplaySlot.CHAT, p) + " " + p.getDisplayName() + ChatColor.GRAY + " -> " + ChatColor.RESET + Teams.getTeam(Teams.DisplaySlot.CHAT, player) + " " + player.getDisplayName() + ChatColor.GREEN + ": " + ChatColor.RESET + msg);

                                        p.sendMessage(Teams.getTeam(Teams.DisplaySlot.CHAT, p) + " " + p.getDisplayName() + ChatColor.GRAY + " -> " + ChatColor.RESET + Teams.getTeam(Teams.DisplaySlot.CHAT, player) + " " + player.getDisplayName() + ChatColor.GREEN + ": " + ChatColor.RESET + msg);
                                        player.sendMessage(Teams.getTeam(Teams.DisplaySlot.CHAT, p) + " " + p.getDisplayName() + ChatColor.GRAY + " -> " + ChatColor.RESET + Teams.getTeam(Teams.DisplaySlot.CHAT, player) + " " + player.getDisplayName() + ChatColor.GREEN + ": " + ChatColor.RESET + msg);
                                        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
                                        return;
                                    }
                                    p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "他のチームにはメッセージを送信できません。");
                                    return;
                                } else if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_JAIL, p)) {
                                    if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_ADMIN, player) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_JAIL, player)) {
                                        Bukkit.getConsoleSender().sendMessage(ChatColor.BLUE + "[P]" + ChatColor.RESET + Teams.getTeam(Teams.DisplaySlot.CHAT, p) + " " + p.getDisplayName() + ChatColor.GRAY + " -> " + ChatColor.RESET + Teams.getTeam(Teams.DisplaySlot.CHAT, player) + " " + player.getDisplayName() + ChatColor.GREEN + ": " + ChatColor.RESET + msg);

                                        for (Player p2 : Bukkit.getOnlinePlayers())
                                            if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_ADMIN, p2))
                                                p2.sendMessage(ChatColor.BLUE + "[P]" + Teams.getTeam(Teams.DisplaySlot.CHAT, p) + " " + p.getDisplayName() + ChatColor.GRAY + " -> " + ChatColor.RESET + Teams.getTeam(Teams.DisplaySlot.CHAT, player) + " " + player.getDisplayName() + ChatColor.GREEN + ": " + ChatColor.RESET + msg);

                                        p.sendMessage(Teams.getTeam(Teams.DisplaySlot.CHAT, p) + " " + p.getDisplayName() + ChatColor.GRAY + " -> " + ChatColor.RESET + Teams.getTeam(Teams.DisplaySlot.CHAT, player) + " " + player.getDisplayName() + ChatColor.GREEN + ": " + ChatColor.RESET + msg);
                                        player.sendMessage(Teams.getTeam(Teams.DisplaySlot.CHAT, p) + " " + p.getDisplayName() + ChatColor.GRAY + " -> " + ChatColor.RESET + Teams.getTeam(Teams.DisplaySlot.CHAT, player) + " " + player.getDisplayName() + ChatColor.GREEN + ": " + ChatColor.RESET + msg);
                                        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
                                        return;
                                    }
                                    p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "他のチームにはメッセージを送信できません。");
                                    return;
                                } else if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_ADMIN, p)) {
                                    Bukkit.getConsoleSender().sendMessage(ChatColor.BLUE + "[P]" + ChatColor.RESET + Teams.getTeam(Teams.DisplaySlot.CHAT, p) + " " + p.getDisplayName() + ChatColor.GRAY + " -> " + ChatColor.RESET + Teams.getTeam(Teams.DisplaySlot.CHAT, player) + " " + player.getDisplayName() + ChatColor.GREEN + ": " + ChatColor.RESET + msg);

                                    for (Player p2 : Bukkit.getOnlinePlayers())
                                        if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_ADMIN, p2))
                                            p2.sendMessage(ChatColor.BLUE + "[P]" + Teams.getTeam(Teams.DisplaySlot.CHAT, p) + " " + p.getDisplayName() + ChatColor.GRAY + " -> " + ChatColor.RESET + Teams.getTeam(Teams.DisplaySlot.CHAT, player) + " " + player.getDisplayName() + ChatColor.GREEN + ": " + ChatColor.RESET + msg);

                                    p.sendMessage(Teams.getTeam(Teams.DisplaySlot.CHAT, p) + " " + p.getDisplayName() + ChatColor.GRAY + " -> " + ChatColor.RESET + Teams.getTeam(Teams.DisplaySlot.CHAT, player) + " " + player.getDisplayName() + ChatColor.GREEN + ": " + ChatColor.RESET + msg);
                                    player.sendMessage(Teams.getTeam(Teams.DisplaySlot.CHAT, p) + " " + p.getDisplayName() + ChatColor.GRAY + " -> " + ChatColor.RESET + Teams.getTeam(Teams.DisplaySlot.CHAT, player) + " " + player.getDisplayName() + ChatColor.GREEN + ": " + ChatColor.RESET + msg);
                                    player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
                                    return;
                                }
                            } else {
                                p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "自分自身にはメッセージを送信できません。");
                                return;
                            }
                        } else {
                            if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_PLAYER, p) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_HUNTER, p) ||
                                    Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_SUCCESS, p) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_TUHO, p)) {
                                Location loc = p.getLocation();

                                Bukkit.getConsoleSender().sendMessage(ChatColor.BLUE + "[R]" + ChatColor.RESET + prefix + (Teams.getTeam(Teams.DisplaySlot.CHAT, p).equals("") ? "" : Teams.getTeam(Teams.DisplaySlot.CHAT, p) + " ") + ChatColor.RESET + p.getDisplayName() + ChatColor.GREEN + ": " + ChatColor.RESET + msg);

                                for (Player player : Bukkit.getOnlinePlayers())
                                    if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_ADMIN, player))
                                        player.sendMessage(ChatColor.BLUE + "[R]" + ChatColor.RESET + prefix + (Teams.getTeam(Teams.DisplaySlot.CHAT, p).equals("") ? "" : Teams.getTeam(Teams.DisplaySlot.CHAT, p) + " ") + ChatColor.RESET + p.getDisplayName() + ChatColor.GREEN + ": " + ChatColor.RESET + msg);

                                for (Player player : recipients)
                                    if (player.getLocation().distance(loc) <= range)
                                        player.sendMessage(ChatColor.BLUE + "[R]" + ChatColor.RESET + prefix + (Teams.getTeam(Teams.DisplaySlot.CHAT, p).equals("") ? "" : Teams.getTeam(Teams.DisplaySlot.CHAT, p) + " ") + ChatColor.RESET + p.getDisplayName() + ChatColor.GREEN + ": " + ChatColor.RESET + msg);
                                return;
                            } else if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_JAIL, p)) {
                                if (p.getGameMode() == GameMode.SPECTATOR) {
                                    Bukkit.getConsoleSender().sendMessage(ChatColor.BLUE + "[T]" + ChatColor.RESET + prefix + (Teams.getTeam(Teams.DisplaySlot.CHAT, p).equals("") ? "" : Teams.getTeam(Teams.DisplaySlot.CHAT, p) + " ") + ChatColor.RESET + p.getDisplayName() + ChatColor.GREEN + ": " + ChatColor.RESET + msg);

                                    for (Player player : Bukkit.getOnlinePlayers())
                                        if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_ADMIN, player) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_JAIL, player))
                                            player.sendMessage(ChatColor.BLUE + "[T]" + ChatColor.RESET + prefix + (Teams.getTeam(Teams.DisplaySlot.CHAT, p).equals("") ? "" : Teams.getTeam(Teams.DisplaySlot.CHAT, p) + " ") + ChatColor.RESET + p.getDisplayName() + ChatColor.GREEN + ": " + ChatColor.RESET + msg);
                                    return;
                                } else {
                                    Bukkit.broadcastMessage(ChatColor.BLUE + "[G]" + ChatColor.RESET + prefix + (Teams.getTeam(Teams.DisplaySlot.CHAT, p).equals("") ? "" : Teams.getTeam(Teams.DisplaySlot.CHAT, p) + " ") + ChatColor.RESET + p.getDisplayName() + ChatColor.GREEN + ": " + ChatColor.RESET + msg);
                                    return;
                                }
                            } else {
                                Bukkit.broadcastMessage(ChatColor.BLUE + "[G]" + ChatColor.RESET + prefix + (Teams.getTeam(Teams.DisplaySlot.CHAT, p).equals("") ? "" : Teams.getTeam(Teams.DisplaySlot.CHAT, p) + " ") + ChatColor.RESET + p.getDisplayName() + ChatColor.GREEN + ": " + ChatColor.RESET + msg);
                                return;
                            }
                        }
                    }
                } else {
                    Bukkit.getConsoleSender().sendMessage(ChatColor.BLUE + "[T]" + ChatColor.RESET + prefix + (Teams.getTeam(Teams.DisplaySlot.CHAT, p).equals("") ? "" : Teams.getTeam(Teams.DisplaySlot.CHAT, p) + " ") + ChatColor.RESET + p.getDisplayName() + ChatColor.GREEN + ": " + ChatColor.RESET + msg);

                    if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_PLAYER, p) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_SUCCESS, p)) {
                        for (Player player : Bukkit.getOnlinePlayers())
                            if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_ADMIN, player) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_PLAYER, player) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_SUCCESS, player))
                                player.sendMessage(ChatColor.BLUE + "[T]" + ChatColor.RESET + prefix + (Teams.getTeam(Teams.DisplaySlot.CHAT, p).equals("") ? "" : Teams.getTeam(Teams.DisplaySlot.CHAT, p) + " ") + ChatColor.RESET + p.getDisplayName() + ChatColor.GREEN + ": " + ChatColor.RESET + msg);
                        return;
                    } else if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_HUNTER, p) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_TUHO, p)) {
                        for (Player player : Bukkit.getOnlinePlayers())
                            if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_ADMIN, player) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_HUNTER, player) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_TUHO, player))
                                player.sendMessage(ChatColor.BLUE + "[T]" + ChatColor.RESET + prefix + (Teams.getTeam(Teams.DisplaySlot.CHAT, p).equals("") ? "" : Teams.getTeam(Teams.DisplaySlot.CHAT, p) + " ") + ChatColor.RESET + p.getDisplayName() + ChatColor.GREEN + ": " + ChatColor.RESET + msg);
                        return;
                    } else {
                        Bukkit.broadcastMessage(ChatColor.BLUE + "[G]" + ChatColor.RESET + prefix + (Teams.getTeam(Teams.DisplaySlot.CHAT, p).equals("") ? "" : Teams.getTeam(Teams.DisplaySlot.CHAT, p) + " ") + ChatColor.RESET + p.getDisplayName() + ChatColor.GREEN + ": " + ChatColor.RESET + msg);
                        return;
                    }
                }
            } else {
                if (matcher.find()) {
                    String name = matcher.group().substring(1);
                    msg = msg.substring(2 + name.length());

                    if (name.equalsIgnoreCase("team")) {
                        Bukkit.getConsoleSender().sendMessage(ChatColor.BLUE + "[T]" + ChatColor.RESET + prefix + (Teams.getTeam(Teams.DisplaySlot.CHAT, p).equals("") ? "" : Teams.getTeam(Teams.DisplaySlot.CHAT, p) + " ") + ChatColor.RESET + p.getDisplayName() + ChatColor.GREEN + ": " + ChatColor.RESET + msg);

                        if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_PLAYER, p) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_SUCCESS, p)) {
                            for (Player player : Bukkit.getOnlinePlayers())
                                if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_ADMIN, player) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_PLAYER, player) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_SUCCESS, player))
                                    player.sendMessage(ChatColor.BLUE + "[T]" + ChatColor.RESET + prefix + (Teams.getTeam(Teams.DisplaySlot.CHAT, p).equals("") ? "" : Teams.getTeam(Teams.DisplaySlot.CHAT, p) + " ") + ChatColor.RESET + p.getDisplayName() + ChatColor.GREEN + ": " + ChatColor.RESET + msg);
                            return;
                        } else if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_HUNTER, p) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_TUHO, p)) {
                            for (Player player : Bukkit.getOnlinePlayers())
                                if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_ADMIN, player) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_HUNTER, player) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_TUHO, player))
                                    player.sendMessage(ChatColor.BLUE + "[T]" + ChatColor.RESET + prefix + (Teams.getTeam(Teams.DisplaySlot.CHAT, p).equals("") ? "" : Teams.getTeam(Teams.DisplaySlot.CHAT, p) + " ") + ChatColor.RESET + p.getDisplayName() + ChatColor.GREEN + ": " + ChatColor.RESET + msg);
                            return;
                        } else if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_JAIL, p)) {
                            Bukkit.getConsoleSender().sendMessage(ChatColor.BLUE + "[T]" + ChatColor.RESET + prefix + (Teams.getTeam(Teams.DisplaySlot.CHAT, p).equals("") ? "" : Teams.getTeam(Teams.DisplaySlot.CHAT, p) + " ") + ChatColor.RESET + p.getDisplayName() + ChatColor.GREEN + ": " + ChatColor.RESET + msg);

                            for (Player player : Bukkit.getOnlinePlayers())
                                if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_ADMIN, player) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_JAIL, player))
                                    player.sendMessage(ChatColor.BLUE + "[T]" + ChatColor.RESET + prefix + (Teams.getTeam(Teams.DisplaySlot.CHAT, p).equals("") ? "" : Teams.getTeam(Teams.DisplaySlot.CHAT, p) + " ") + ChatColor.RESET + p.getDisplayName() + ChatColor.GREEN + ": " + ChatColor.RESET + msg);
                            return;
                        } else {
                            Bukkit.getConsoleSender().sendMessage(ChatColor.BLUE + "[T]" + ChatColor.RESET + prefix + (Teams.getTeam(Teams.DisplaySlot.CHAT, p).equals("") ? "" : Teams.getTeam(Teams.DisplaySlot.CHAT, p) + " ") + ChatColor.RESET + p.getDisplayName() + ChatColor.GREEN + ": " + ChatColor.RESET + msg);

                            for (Player player : Bukkit.getOnlinePlayers())
                                if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_ADMIN, player))
                                    player.sendMessage(ChatColor.BLUE + "[T]" + ChatColor.RESET + prefix + (Teams.getTeam(Teams.DisplaySlot.CHAT, p).equals("") ? "" : Teams.getTeam(Teams.DisplaySlot.CHAT, p) + " ") + ChatColor.RESET + p.getDisplayName() + ChatColor.GREEN + ": " + ChatColor.RESET + msg);
                            return;
                        }
                    } else {
                        if (Bukkit.getPlayer(name) != null) {
                            Player player = Bukkit.getPlayer(name);

                            if (player.getUniqueId() != p.getUniqueId()) {
                                if (Teams.getJoinedTeam(p) == null) return;
                                if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_PLAYER, p) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_SUCCESS, p)) {
                                    if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_ADMIN, player) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_PLAYER, player) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_SUCCESS, player)) {
                                        Bukkit.getConsoleSender().sendMessage(ChatColor.BLUE + "[P]" + ChatColor.RESET + Teams.getTeam(Teams.DisplaySlot.CHAT, p) + " " + p.getDisplayName() + ChatColor.GRAY + " -> " + ChatColor.RESET + Teams.getTeam(Teams.DisplaySlot.CHAT, player) + " " + player.getDisplayName() + ChatColor.GREEN + ": " + ChatColor.RESET + msg);

                                        for (Player p2 : Bukkit.getOnlinePlayers())
                                            if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_ADMIN, p2))
                                                p2.sendMessage(ChatColor.BLUE + "[P]" + Teams.getTeam(Teams.DisplaySlot.CHAT, p) + " " + p.getDisplayName() + ChatColor.GRAY + " -> " + ChatColor.RESET + Teams.getTeam(Teams.DisplaySlot.CHAT, player) + " " + player.getDisplayName() + ChatColor.GREEN + ": " + ChatColor.RESET + msg);

                                        p.sendMessage(Teams.getTeam(Teams.DisplaySlot.CHAT, p) + " " + p.getDisplayName() + ChatColor.GRAY + " -> " + ChatColor.RESET + Teams.getTeam(Teams.DisplaySlot.CHAT, player) + " " + player.getDisplayName() + ChatColor.GREEN + ": " + ChatColor.RESET + msg);
                                        player.sendMessage(Teams.getTeam(Teams.DisplaySlot.CHAT, p) + " " + p.getDisplayName() + ChatColor.GRAY + " -> " + ChatColor.RESET + Teams.getTeam(Teams.DisplaySlot.CHAT, player) + " " + player.getDisplayName() + ChatColor.GREEN + ": " + ChatColor.RESET + msg);
                                        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
                                        return;
                                    }
                                    p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "他のチームにはメッセージを送信できません。");
                                    return;
                                } else if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_HUNTER, p) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_TUHO, p)) {
                                    if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_ADMIN, player) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_HUNTER, player) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_TUHO, player)) {
                                        Bukkit.getConsoleSender().sendMessage(ChatColor.BLUE + "[P]" + ChatColor.RESET + Teams.getTeam(Teams.DisplaySlot.CHAT, p) + " " + p.getDisplayName() + ChatColor.GRAY + " -> " + ChatColor.RESET + Teams.getTeam(Teams.DisplaySlot.CHAT, player) + " " + player.getDisplayName() + ChatColor.GREEN + ": " + ChatColor.RESET + msg);

                                        for (Player p2 : Bukkit.getOnlinePlayers())
                                            if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_ADMIN, p2))
                                                p2.sendMessage(ChatColor.BLUE + "[P]" + Teams.getTeam(Teams.DisplaySlot.CHAT, p) + " " + p.getDisplayName() + ChatColor.GRAY + " -> " + ChatColor.RESET + Teams.getTeam(Teams.DisplaySlot.CHAT, player) + " " + player.getDisplayName() + ChatColor.GREEN + ": " + ChatColor.RESET + msg);

                                        p.sendMessage(Teams.getTeam(Teams.DisplaySlot.CHAT, p) + " " + p.getDisplayName() + ChatColor.GRAY + " -> " + ChatColor.RESET + Teams.getTeam(Teams.DisplaySlot.CHAT, player) + " " + player.getDisplayName() + ChatColor.GREEN + ": " + ChatColor.RESET + msg);
                                        player.sendMessage(Teams.getTeam(Teams.DisplaySlot.CHAT, p) + " " + p.getDisplayName() + ChatColor.GRAY + " -> " + ChatColor.RESET + Teams.getTeam(Teams.DisplaySlot.CHAT, player) + " " + player.getDisplayName() + ChatColor.GREEN + ": " + ChatColor.RESET + msg);
                                        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
                                        return;
                                    }
                                    p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "他のチームにはメッセージを送信できません。");
                                    return;
                                } else if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_JAIL, p)) {
                                    if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_ADMIN, player) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_JAIL, player)) {
                                        Bukkit.getConsoleSender().sendMessage(ChatColor.BLUE + "[P]" + ChatColor.RESET + Teams.getTeam(Teams.DisplaySlot.CHAT, p) + " " + p.getDisplayName() + ChatColor.GRAY + " -> " + ChatColor.RESET + Teams.getTeam(Teams.DisplaySlot.CHAT, player) + " " + player.getDisplayName() + ChatColor.GREEN + ": " + ChatColor.RESET + msg);

                                        for (Player p2 : Bukkit.getOnlinePlayers())
                                            if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_ADMIN, p2))
                                                p2.sendMessage(ChatColor.BLUE + "[P]" + Teams.getTeam(Teams.DisplaySlot.CHAT, p) + " " + p.getDisplayName() + ChatColor.GRAY + " -> " + ChatColor.RESET + Teams.getTeam(Teams.DisplaySlot.CHAT, player) + " " + player.getDisplayName() + ChatColor.GREEN + ": " + ChatColor.RESET + msg);

                                        p.sendMessage(Teams.getTeam(Teams.DisplaySlot.CHAT, p) + " " + p.getDisplayName() + ChatColor.GRAY + " -> " + ChatColor.RESET + Teams.getTeam(Teams.DisplaySlot.CHAT, player) + " " + player.getDisplayName() + ChatColor.GREEN + ": " + ChatColor.RESET + msg);
                                        player.sendMessage(Teams.getTeam(Teams.DisplaySlot.CHAT, p) + " " + p.getDisplayName() + ChatColor.GRAY + " -> " + ChatColor.RESET + Teams.getTeam(Teams.DisplaySlot.CHAT, player) + " " + player.getDisplayName() + ChatColor.GREEN + ": " + ChatColor.RESET + msg);
                                        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
                                        return;
                                    }
                                    p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "他のチームにはメッセージを送信できません。");
                                    return;
                                } else if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_ADMIN, p)) {
                                    Bukkit.getConsoleSender().sendMessage(ChatColor.BLUE + "[P]" + ChatColor.RESET + Teams.getTeam(Teams.DisplaySlot.CHAT, p) + " " + p.getDisplayName() + ChatColor.GRAY + " -> " + ChatColor.RESET + Teams.getTeam(Teams.DisplaySlot.CHAT, player) + " " + player.getDisplayName() + ChatColor.GREEN + ": " + ChatColor.RESET + msg);

                                    for (Player p2 : Bukkit.getOnlinePlayers())
                                        if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_ADMIN, p2))
                                            p2.sendMessage(ChatColor.BLUE + "[P]" + Teams.getTeam(Teams.DisplaySlot.CHAT, p) + " " + p.getDisplayName() + ChatColor.GRAY + " -> " + ChatColor.RESET + Teams.getTeam(Teams.DisplaySlot.CHAT, player) + " " + player.getDisplayName() + ChatColor.GREEN + ": " + ChatColor.RESET + msg);

                                    p.sendMessage(Teams.getTeam(Teams.DisplaySlot.CHAT, p) + " " + p.getDisplayName() + ChatColor.GRAY + " -> " + ChatColor.RESET + Teams.getTeam(Teams.DisplaySlot.CHAT, player) + " " + player.getDisplayName() + ChatColor.GREEN + ": " + ChatColor.RESET + msg);
                                    player.sendMessage(Teams.getTeam(Teams.DisplaySlot.CHAT, p) + " " + p.getDisplayName() + ChatColor.GRAY + " -> " + ChatColor.RESET + Teams.getTeam(Teams.DisplaySlot.CHAT, player) + " " + player.getDisplayName() + ChatColor.GREEN + ": " + ChatColor.RESET + msg);
                                    player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
                                    return;
                                }
                            } else {
                                p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "自分自身にはメッセージを送信できません。");
                                return;
                            }
                        } else {
                            if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_PLAYER, p) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_HUNTER, p) ||
                                    Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_SUCCESS, p) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_TUHO, p)) {
                                Location loc = p.getLocation();

                                Bukkit.getConsoleSender().sendMessage(ChatColor.BLUE + "[R]" + ChatColor.RESET + prefix + (Teams.getTeam(Teams.DisplaySlot.CHAT, p).equals("") ? "" : Teams.getTeam(Teams.DisplaySlot.CHAT, p) + " ") + ChatColor.RESET + p.getDisplayName() + ChatColor.GREEN + ": " + ChatColor.RESET + msg);

                                for (Player player : Bukkit.getOnlinePlayers())
                                    if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_ADMIN, player))
                                        player.sendMessage(ChatColor.BLUE + "[R]" + ChatColor.RESET + prefix + (Teams.getTeam(Teams.DisplaySlot.CHAT, p).equals("") ? "" : Teams.getTeam(Teams.DisplaySlot.CHAT, p) + " ") + ChatColor.RESET + p.getDisplayName() + ChatColor.GREEN + ": " + ChatColor.RESET + msg);

                                for (Player player : recipients)
                                    if (player.getLocation().distance(loc) <= range)
                                        player.sendMessage(ChatColor.BLUE + "[R]" + ChatColor.RESET + prefix + (Teams.getTeam(Teams.DisplaySlot.CHAT, p).equals("") ? "" : Teams.getTeam(Teams.DisplaySlot.CHAT, p) + " ") + ChatColor.RESET + p.getDisplayName() + ChatColor.GREEN + ": " + ChatColor.RESET + msg);
                                return;
                            } else if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_JAIL, p)) {
                                if (p.getGameMode() == GameMode.SPECTATOR) {
                                    Bukkit.getConsoleSender().sendMessage(ChatColor.BLUE + "[T]" + ChatColor.RESET + prefix + (Teams.getTeam(Teams.DisplaySlot.CHAT, p).equals("") ? "" : Teams.getTeam(Teams.DisplaySlot.CHAT, p) + " ") + ChatColor.RESET + p.getDisplayName() + ChatColor.GREEN + ": " + ChatColor.RESET + msg);

                                    for (Player player : Bukkit.getOnlinePlayers())
                                        if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_ADMIN, player) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_JAIL, player))
                                            player.sendMessage(ChatColor.BLUE + "[T]" + ChatColor.RESET + prefix + (Teams.getTeam(Teams.DisplaySlot.CHAT, p).equals("") ? "" : Teams.getTeam(Teams.DisplaySlot.CHAT, p) + " ") + ChatColor.RESET + p.getDisplayName() + ChatColor.GREEN + ": " + ChatColor.RESET + msg);
                                    return;
                                } else {
                                    Bukkit.broadcastMessage(ChatColor.BLUE + "[G]" + ChatColor.RESET + prefix + (Teams.getTeam(Teams.DisplaySlot.CHAT, p).equals("") ? "" : Teams.getTeam(Teams.DisplaySlot.CHAT, p) + " ") + ChatColor.RESET + p.getDisplayName() + ChatColor.GREEN + ": " + ChatColor.RESET + msg);
                                    return;
                                }
                            } else {
                                Bukkit.broadcastMessage(ChatColor.BLUE + "[G]" + ChatColor.RESET + prefix + (Teams.getTeam(Teams.DisplaySlot.CHAT, p).equals("") ? "" : Teams.getTeam(Teams.DisplaySlot.CHAT, p) + " ") + ChatColor.RESET + p.getDisplayName() + ChatColor.GREEN + ": " + ChatColor.RESET + msg);
                                return;
                            }
                        }
                    }
                } else {
                    if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_PLAYER, p) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_HUNTER, p) ||
                            Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_SUCCESS, p) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_TUHO, p)) {
                        Location loc = p.getLocation();

                        Bukkit.getConsoleSender().sendMessage(ChatColor.BLUE + "[R]" + ChatColor.RESET + prefix + (Teams.getTeam(Teams.DisplaySlot.CHAT, p).equals("") ? "" : Teams.getTeam(Teams.DisplaySlot.CHAT, p) + " ") + ChatColor.RESET + p.getDisplayName() + ChatColor.GREEN + ": " + ChatColor.RESET + msg);

                        for (Player player : Bukkit.getOnlinePlayers())
                            if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_ADMIN, player))
                                player.sendMessage(ChatColor.BLUE + "[R]" + ChatColor.RESET + prefix + (Teams.getTeam(Teams.DisplaySlot.CHAT, p).equals("") ? "" : Teams.getTeam(Teams.DisplaySlot.CHAT, p) + " ") + ChatColor.RESET + p.getDisplayName() + ChatColor.GREEN + ": " + ChatColor.RESET + msg);

                        for (Player player : recipients)
                            if (player.getLocation().distance(loc) <= range)
                                player.sendMessage(ChatColor.BLUE + "[R]" + ChatColor.RESET + prefix + (Teams.getTeam(Teams.DisplaySlot.CHAT, p).equals("") ? "" : Teams.getTeam(Teams.DisplaySlot.CHAT, p) + " ") + ChatColor.RESET + p.getDisplayName() + ChatColor.GREEN + ": " + ChatColor.RESET + msg);
                        return;
                    } else if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_JAIL, p)) {
                        if (p.getGameMode() == GameMode.SPECTATOR) {
                            Bukkit.getConsoleSender().sendMessage(ChatColor.BLUE + "[T]" + ChatColor.RESET + prefix + (Teams.getTeam(Teams.DisplaySlot.CHAT, p).equals("") ? "" : Teams.getTeam(Teams.DisplaySlot.CHAT, p) + " ") + ChatColor.RESET + p.getDisplayName() + ChatColor.GREEN + ": " + ChatColor.RESET + msg);

                            for (Player player : Bukkit.getOnlinePlayers())
                                if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_ADMIN, player) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_JAIL, player))
                                    player.sendMessage(ChatColor.BLUE + "[T]" + ChatColor.RESET + prefix + (Teams.getTeam(Teams.DisplaySlot.CHAT, p).equals("") ? "" : Teams.getTeam(Teams.DisplaySlot.CHAT, p) + " ") + ChatColor.RESET + p.getDisplayName() + ChatColor.GREEN + ": " + ChatColor.RESET + msg);
                            return;
                        } else {
                            Bukkit.broadcastMessage(ChatColor.BLUE + "[G]" + ChatColor.RESET + prefix + (Teams.getTeam(Teams.DisplaySlot.CHAT, p).equals("") ? "" : Teams.getTeam(Teams.DisplaySlot.CHAT, p) + " ") + ChatColor.RESET + p.getDisplayName() + ChatColor.GREEN + ": " + ChatColor.RESET + msg);
                            return;
                        }
                    } else {
                        Bukkit.broadcastMessage(ChatColor.BLUE + "[G]" + ChatColor.RESET + prefix + (Teams.getTeam(Teams.DisplaySlot.CHAT, p).equals("") ? "" : Teams.getTeam(Teams.DisplaySlot.CHAT, p) + " ") + ChatColor.RESET + p.getDisplayName() + ChatColor.GREEN + ": " + ChatColor.RESET + msg);
                        return;
                    }
                }
            }
        } else {
            if (matcher.find()) {
                String name = matcher.group().substring(1);
                msg = msg.substring(2 + name.length());

                if (name.equalsIgnoreCase("team")) {
                    Bukkit.getConsoleSender().sendMessage(ChatColor.BLUE + "[T]" + ChatColor.RESET + prefix + (Teams.getTeam(Teams.DisplaySlot.CHAT, p).equals("") ? "" : Teams.getTeam(Teams.DisplaySlot.CHAT, p) + " ") + ChatColor.RESET + p.getDisplayName() + ChatColor.GREEN + ": " + ChatColor.RESET + msg);

                    if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_PLAYER, p) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_SUCCESS, p)) {
                        for (Player player : Bukkit.getOnlinePlayers())
                            if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_ADMIN, player) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_PLAYER, player) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_SUCCESS, player))
                                player.sendMessage(ChatColor.BLUE + "[T]" + ChatColor.RESET + prefix + (Teams.getTeam(Teams.DisplaySlot.CHAT, p).equals("") ? "" : Teams.getTeam(Teams.DisplaySlot.CHAT, p) + " ") + ChatColor.RESET + p.getDisplayName() + ChatColor.GREEN + ": " + ChatColor.RESET + msg);
                        return;
                    } else if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_HUNTER, p) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_TUHO, p)) {
                        for (Player player : Bukkit.getOnlinePlayers())
                            if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_ADMIN, player) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_HUNTER, player) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_TUHO, player))
                                player.sendMessage(ChatColor.BLUE + "[T]" + ChatColor.RESET + prefix + (Teams.getTeam(Teams.DisplaySlot.CHAT, p).equals("") ? "" : Teams.getTeam(Teams.DisplaySlot.CHAT, p) + " ") + ChatColor.RESET + p.getDisplayName() + ChatColor.GREEN + ": " + ChatColor.RESET + msg);
                        return;
                    } else if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_JAIL, p)) {
                        Bukkit.getConsoleSender().sendMessage(ChatColor.BLUE + "[T]" + ChatColor.RESET + prefix + (Teams.getTeam(Teams.DisplaySlot.CHAT, p).equals("") ? "" : Teams.getTeam(Teams.DisplaySlot.CHAT, p) + " ") + ChatColor.RESET + p.getDisplayName() + ChatColor.GREEN + ": " + ChatColor.RESET + msg);

                        for (Player player : Bukkit.getOnlinePlayers())
                            if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_ADMIN, player) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_JAIL, player))
                                player.sendMessage(ChatColor.BLUE + "[T]" + ChatColor.RESET + prefix + (Teams.getTeam(Teams.DisplaySlot.CHAT, p).equals("") ? "" : Teams.getTeam(Teams.DisplaySlot.CHAT, p) + " ") + ChatColor.RESET + p.getDisplayName() + ChatColor.GREEN + ": " + ChatColor.RESET + msg);
                        return;
                    } else {
                        Bukkit.getConsoleSender().sendMessage(ChatColor.BLUE + "[T]" + ChatColor.RESET + prefix + (Teams.getTeam(Teams.DisplaySlot.CHAT, p).equals("") ? "" : Teams.getTeam(Teams.DisplaySlot.CHAT, p) + " ") + ChatColor.RESET + p.getDisplayName() + ChatColor.GREEN + ": " + ChatColor.RESET + msg);

                        for (Player player : Bukkit.getOnlinePlayers())
                            if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_ADMIN, player))
                                player.sendMessage(ChatColor.BLUE + "[T]" + ChatColor.RESET + prefix + (Teams.getTeam(Teams.DisplaySlot.CHAT, p).equals("") ? "" : Teams.getTeam(Teams.DisplaySlot.CHAT, p) + " ") + ChatColor.RESET + p.getDisplayName() + ChatColor.GREEN + ": " + ChatColor.RESET + msg);
                        return;
                    }
                } else {
                    if (Bukkit.getPlayer(name) != null) {
                        Player player = Bukkit.getPlayer(name);

                        if (player.getUniqueId() != p.getUniqueId()) {
                            if (Teams.getJoinedTeam(p) == null) return;
                            if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_PLAYER, p) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_SUCCESS, p)) {
                                if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_ADMIN, player) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_PLAYER, player) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_SUCCESS, player)) {
                                    Bukkit.getConsoleSender().sendMessage(ChatColor.BLUE + "[P]" + ChatColor.RESET + Teams.getTeam(Teams.DisplaySlot.CHAT, p) + " " + p.getDisplayName() + ChatColor.GRAY + " -> " + ChatColor.RESET + Teams.getTeam(Teams.DisplaySlot.CHAT, player) + " " + player.getDisplayName() + ChatColor.GREEN + ": " + ChatColor.RESET + msg);

                                    for (Player p2 : Bukkit.getOnlinePlayers())
                                        if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_ADMIN, p2))
                                            p2.sendMessage(ChatColor.BLUE + "[P]" + Teams.getTeam(Teams.DisplaySlot.CHAT, p) + " " + p.getDisplayName() + ChatColor.GRAY + " -> " + ChatColor.RESET + Teams.getTeam(Teams.DisplaySlot.CHAT, player) + " " + player.getDisplayName() + ChatColor.GREEN + ": " + ChatColor.RESET + msg);

                                    p.sendMessage(Teams.getTeam(Teams.DisplaySlot.CHAT, p) + " " + p.getDisplayName() + ChatColor.GRAY + " -> " + ChatColor.RESET + Teams.getTeam(Teams.DisplaySlot.CHAT, player) + " " + player.getDisplayName() + ChatColor.GREEN + ": " + ChatColor.RESET + msg);
                                    player.sendMessage(Teams.getTeam(Teams.DisplaySlot.CHAT, p) + " " + p.getDisplayName() + ChatColor.GRAY + " -> " + ChatColor.RESET + Teams.getTeam(Teams.DisplaySlot.CHAT, player) + " " + player.getDisplayName() + ChatColor.GREEN + ": " + ChatColor.RESET + msg);
                                    player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
                                    return;
                                }
                                p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "他のチームにはメッセージを送信できません。");
                                return;
                            } else if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_HUNTER, p) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_TUHO, p)) {
                                if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_ADMIN, player) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_HUNTER, player) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_TUHO, player)) {
                                    Bukkit.getConsoleSender().sendMessage(ChatColor.BLUE + "[P]" + ChatColor.RESET + Teams.getTeam(Teams.DisplaySlot.CHAT, p) + " " + p.getDisplayName() + ChatColor.GRAY + " -> " + ChatColor.RESET + Teams.getTeam(Teams.DisplaySlot.CHAT, player) + " " + player.getDisplayName() + ChatColor.GREEN + ": " + ChatColor.RESET + msg);

                                    for (Player p2 : Bukkit.getOnlinePlayers())
                                        if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_ADMIN, p2))
                                            p2.sendMessage(ChatColor.BLUE + "[P]" + Teams.getTeam(Teams.DisplaySlot.CHAT, p) + " " + p.getDisplayName() + ChatColor.GRAY + " -> " + ChatColor.RESET + Teams.getTeam(Teams.DisplaySlot.CHAT, player) + " " + player.getDisplayName() + ChatColor.GREEN + ": " + ChatColor.RESET + msg);

                                    p.sendMessage(Teams.getTeam(Teams.DisplaySlot.CHAT, p) + " " + p.getDisplayName() + ChatColor.GRAY + " -> " + ChatColor.RESET + Teams.getTeam(Teams.DisplaySlot.CHAT, player) + " " + player.getDisplayName() + ChatColor.GREEN + ": " + ChatColor.RESET + msg);
                                    player.sendMessage(Teams.getTeam(Teams.DisplaySlot.CHAT, p) + " " + p.getDisplayName() + ChatColor.GRAY + " -> " + ChatColor.RESET + Teams.getTeam(Teams.DisplaySlot.CHAT, player) + " " + player.getDisplayName() + ChatColor.GREEN + ": " + ChatColor.RESET + msg);
                                    player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
                                    return;
                                }
                                p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "他のチームにはメッセージを送信できません。");
                                return;
                            } else if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_JAIL, p)) {
                                if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_ADMIN, player) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_JAIL, player)) {
                                    Bukkit.getConsoleSender().sendMessage(ChatColor.BLUE + "[P]" + ChatColor.RESET + Teams.getTeam(Teams.DisplaySlot.CHAT, p) + " " + p.getDisplayName() + ChatColor.GRAY + " -> " + ChatColor.RESET + Teams.getTeam(Teams.DisplaySlot.CHAT, player) + " " + player.getDisplayName() + ChatColor.GREEN + ": " + ChatColor.RESET + msg);

                                    for (Player p2 : Bukkit.getOnlinePlayers())
                                        if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_ADMIN, p2))
                                            p2.sendMessage(ChatColor.BLUE + "[P]" + Teams.getTeam(Teams.DisplaySlot.CHAT, p) + " " + p.getDisplayName() + ChatColor.GRAY + " -> " + ChatColor.RESET + Teams.getTeam(Teams.DisplaySlot.CHAT, player) + " " + player.getDisplayName() + ChatColor.GREEN + ": " + ChatColor.RESET + msg);

                                    p.sendMessage(Teams.getTeam(Teams.DisplaySlot.CHAT, p) + " " + p.getDisplayName() + ChatColor.GRAY + " -> " + ChatColor.RESET + Teams.getTeam(Teams.DisplaySlot.CHAT, player) + " " + player.getDisplayName() + ChatColor.GREEN + ": " + ChatColor.RESET + msg);
                                    player.sendMessage(Teams.getTeam(Teams.DisplaySlot.CHAT, p) + " " + p.getDisplayName() + ChatColor.GRAY + " -> " + ChatColor.RESET + Teams.getTeam(Teams.DisplaySlot.CHAT, player) + " " + player.getDisplayName() + ChatColor.GREEN + ": " + ChatColor.RESET + msg);
                                    player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
                                    return;
                                }
                                p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "他のチームにはメッセージを送信できません。");
                                return;
                            } else if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_ADMIN, p)) {
                                Bukkit.getConsoleSender().sendMessage(ChatColor.BLUE + "[P]" + ChatColor.RESET + Teams.getTeam(Teams.DisplaySlot.CHAT, p) + " " + p.getDisplayName() + ChatColor.GRAY + " -> " + ChatColor.RESET + Teams.getTeam(Teams.DisplaySlot.CHAT, player) + " " + player.getDisplayName() + ChatColor.GREEN + ": " + ChatColor.RESET + msg);

                                for (Player p2 : Bukkit.getOnlinePlayers())
                                    if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_ADMIN, p2))
                                        p2.sendMessage(ChatColor.BLUE + "[P]" + Teams.getTeam(Teams.DisplaySlot.CHAT, p) + " " + p.getDisplayName() + ChatColor.GRAY + " -> " + ChatColor.RESET + Teams.getTeam(Teams.DisplaySlot.CHAT, player) + " " + player.getDisplayName() + ChatColor.GREEN + ": " + ChatColor.RESET + msg);

                                p.sendMessage(Teams.getTeam(Teams.DisplaySlot.CHAT, p) + " " + p.getDisplayName() + ChatColor.GRAY + " -> " + ChatColor.RESET + Teams.getTeam(Teams.DisplaySlot.CHAT, player) + " " + player.getDisplayName() + ChatColor.GREEN + ": " + ChatColor.RESET + msg);
                                player.sendMessage(Teams.getTeam(Teams.DisplaySlot.CHAT, p) + " " + p.getDisplayName() + ChatColor.GRAY + " -> " + ChatColor.RESET + Teams.getTeam(Teams.DisplaySlot.CHAT, player) + " " + player.getDisplayName() + ChatColor.GREEN + ": " + ChatColor.RESET + msg);
                                player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
                                return;
                            }
                        } else {
                            p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "自分自身にはメッセージを送信できません。");
                            return;
                        }
                    } else {
                        Bukkit.broadcastMessage(ChatColor.BLUE + "[G]" + ChatColor.RESET + prefix + (Teams.getTeam(Teams.DisplaySlot.CHAT, p).equals("") ? "" : Teams.getTeam(Teams.DisplaySlot.CHAT, p) + " ") + ChatColor.RESET + p.getDisplayName() + ChatColor.GREEN + ": " + ChatColor.RESET + msg);
                        return;
                    }
                }
            } else {
                Bukkit.broadcastMessage(ChatColor.BLUE + "[G]" + ChatColor.RESET + prefix + (Teams.getTeam(Teams.DisplaySlot.CHAT, p).equals("") ? "" : Teams.getTeam(Teams.DisplaySlot.CHAT, p) + " ") + ChatColor.RESET + p.getDisplayName() + ChatColor.GREEN + ": " + ChatColor.RESET + msg);
                return;
            }
        }
    }
}

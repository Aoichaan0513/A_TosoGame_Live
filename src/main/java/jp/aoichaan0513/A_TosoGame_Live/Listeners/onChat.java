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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class onChat implements Listener {

    final int range = 7;

    @EventHandler
    public void onAsyncChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();

        if (GameManager.isGame()) {
            if (p.getInventory().getItemInMainHand().getType() == Material.BOOK) {
                ItemMeta meta = p.getInventory().getItemInMainHand().getItemMeta();

                e.setCancelled(true);
                sendMessage(p, e.getMessage(), ChatColor.stripColor(meta.getDisplayName()).equals(Main.PHONE_ITEM_NAME));
            } else if (p.getInventory().getItemInOffHand().getType() == Material.BOOK) {
                ItemMeta meta = p.getInventory().getItemInOffHand().getItemMeta();

                e.setCancelled(true);
                sendMessage(p, e.getMessage(), ChatColor.stripColor(meta.getDisplayName()).equals(Main.PHONE_ITEM_NAME));
            } else {
                e.setCancelled(true);
                sendMessage(p, e.getMessage(), false);
            }
        } else {
            e.setCancelled(true);
            sendMessage(p, e.getMessage(), false);
        }
    }

    private void sendMessage(Player p, String message, boolean isTeam) {
        final String regex = "^@([A-z0-9_]{2,17})";

        String msg = ChatColor.translateAlternateColorCodes('&', message);

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(msg);

        if (GameManager.isGame()) {
            if (isTeam) {
                if (matcher.find()) {
                    String name = matcher.group().substring(1);
                    msg = msg.substring(2 + name.length());

                    if (name.equalsIgnoreCase("team")) {
                        sendMessage(ChatType.TEAM, p, p, msg);
                    } else {
                        sendMessage(Bukkit.getPlayer(name) != null ? ChatType.PRIVATE : ChatType.RANGE, p, Bukkit.getPlayer(name) != null ? Bukkit.getPlayer(name) : p, msg);
                    }
                } else {
                    sendMessage(ChatType.TEAM_GLOBAL, p, p, msg);
                }
            } else {
                if (matcher.find()) {
                    String name = matcher.group().substring(1);
                    msg = msg.substring(2 + name.length());

                    if (name.equalsIgnoreCase("team")) {
                        sendMessage(ChatType.TEAM, p, p, msg);
                    } else {
                        sendMessage(Bukkit.getPlayer(name) != null ? ChatType.PRIVATE : ChatType.RANGE, p, Bukkit.getPlayer(name) != null ? Bukkit.getPlayer(name) : p, msg);
                    }
                } else {
                    sendMessage(ChatType.RANGE, p, p, msg);
                }
            }
        } else {
            if (matcher.find()) {
                String name = matcher.group().substring(1);
                msg = msg.substring(2 + name.length());

                if (name.equalsIgnoreCase("team")) {
                    sendMessage(ChatType.TEAM, p, p, msg);
                } else {
                    sendMessage(Bukkit.getPlayer(name) != null ? ChatType.PRIVATE : ChatType.GLOBAL, p, Bukkit.getPlayer(name) != null ? Bukkit.getPlayer(name) : p, msg);
                }
            } else {
                sendMessage(ChatType.GLOBAL, p, p, msg);
            }
        }
    }

    private void sendMessage(ChatType chatType, Player sp, Player p, String msg) {
        String basePrefix = (TosoGameAPI.isBroadCaster(sp) ? ChatColor.GOLD + "" + ChatColor.BOLD + " * " + ChatColor.RESET : "") + (Teams.getTeam(Teams.DisplaySlot.CHAT, sp).equals("") ? "" : Teams.getTeam(Teams.DisplaySlot.CHAT, sp)) + ChatColor.RESET + (!MainAPI.isHidePlayer(p) ? " " + sp.getDisplayName() : "") + ChatColor.GREEN + ": " + ChatColor.RESET;

        String globalMessagePrefix = ChatType.GLOBAL.getPrefix() + basePrefix;
        String rangeMessagePrefix = ChatType.RANGE.getPrefix() + basePrefix;
        String teamMessagePrefix = ChatType.TEAM.getPrefix() + basePrefix;
        String privateMessagePrefix = ChatType.PRIVATE.getPrefix() + Teams.getTeam(Teams.DisplaySlot.CHAT, sp) + " " + sp.getDisplayName() + ChatColor.GRAY + " -> " + ChatColor.RESET + Teams.getTeam(Teams.DisplaySlot.CHAT, p) + " " + p.getDisplayName() + ChatColor.GREEN + ": " + ChatColor.RESET;

        if (chatType == ChatType.GLOBAL) {
            Bukkit.broadcastMessage(globalMessagePrefix + msg);
        } else if (chatType == ChatType.RANGE) {
            if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_PLAYER, sp) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_HUNTER, sp) ||
                    Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_SUCCESS, sp) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_TUHO, sp)) {
                Location loc = sp.getLocation();

                Bukkit.getConsoleSender().sendMessage(rangeMessagePrefix + msg);

                for (Player player : Bukkit.getOnlinePlayers())
                    if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_ADMIN, player) || player.getLocation().distance(loc) <= range)
                        player.sendMessage(rangeMessagePrefix + msg);
            } else if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_JAIL, sp)) {
                if (sp.getGameMode() == GameMode.SPECTATOR) {
                    Bukkit.getConsoleSender().sendMessage(teamMessagePrefix + msg);

                    for (Player player : Bukkit.getOnlinePlayers())
                        if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_ADMIN, player) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_JAIL, player))
                            player.sendMessage(teamMessagePrefix + msg);
                } else {
                    Bukkit.broadcastMessage(globalMessagePrefix + msg);
                }
            } else {
                Bukkit.broadcastMessage(globalMessagePrefix + msg);
            }
        } else if (chatType == ChatType.TEAM) {
            Bukkit.getConsoleSender().sendMessage(teamMessagePrefix + msg);

            if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_PLAYER, sp) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_SUCCESS, sp)) {
                for (Player player : Bukkit.getOnlinePlayers())
                    if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_ADMIN, player) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_PLAYER, player) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_SUCCESS, player))
                        player.sendMessage(teamMessagePrefix + msg);
            } else if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_HUNTER, sp) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_TUHO, sp)) {
                for (Player player : Bukkit.getOnlinePlayers())
                    if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_ADMIN, player) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_HUNTER, player) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_TUHO, player))
                        player.sendMessage(teamMessagePrefix + msg);
            } else if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_JAIL, sp)) {
                for (Player player : Bukkit.getOnlinePlayers())
                    if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_ADMIN, player) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_JAIL, player))
                        player.sendMessage(teamMessagePrefix + msg);
            } else {
                for (Player player : Bukkit.getOnlinePlayers())
                    if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_ADMIN, player))
                        player.sendMessage(teamMessagePrefix + msg);
            }
        } else if (chatType == ChatType.TEAM_GLOBAL) {
            if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_PLAYER, sp) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_SUCCESS, sp)) {
                Bukkit.getConsoleSender().sendMessage(teamMessagePrefix + msg);
                for (Player player : Bukkit.getOnlinePlayers())
                    if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_ADMIN, player) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_PLAYER, player) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_SUCCESS, player))
                        player.sendMessage(teamMessagePrefix + msg);
            } else if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_HUNTER, sp) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_TUHO, sp)) {
                Bukkit.getConsoleSender().sendMessage(teamMessagePrefix + msg);
                for (Player player : Bukkit.getOnlinePlayers())
                    if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_ADMIN, player) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_HUNTER, player) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_TUHO, player))
                        player.sendMessage(teamMessagePrefix + msg);
            } else {
                Bukkit.broadcastMessage(globalMessagePrefix + msg);
            }
        } else if (chatType == ChatType.PRIVATE) {
            if (sp.getUniqueId() != p.getUniqueId()) {
                if (Teams.getJoinedTeam(p) == null) return;
                if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_PLAYER, sp) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_SUCCESS, sp)) {
                    if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_ADMIN, p) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_PLAYER, p) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_SUCCESS, p)) {
                        Bukkit.getConsoleSender().sendMessage(privateMessagePrefix + msg);

                        for (Player player : Bukkit.getOnlinePlayers())
                            if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_ADMIN, player))
                                player.sendMessage(privateMessagePrefix + msg);

                        sp.sendMessage(privateMessagePrefix + msg);
                        p.sendMessage(privateMessagePrefix + msg);
                        p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
                    } else {
                        sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "他のチームのプレイヤーにはメッセージを送信できません。");
                    }
                } else if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_HUNTER, sp) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_TUHO, sp)) {
                    if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_ADMIN, p) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_HUNTER, p) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_TUHO, p)) {
                        Bukkit.getConsoleSender().sendMessage(privateMessagePrefix + msg);

                        for (Player player : Bukkit.getOnlinePlayers())
                            if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_ADMIN, player))
                                player.sendMessage(privateMessagePrefix + msg);

                        sp.sendMessage(privateMessagePrefix + msg);
                        p.sendMessage(privateMessagePrefix + msg);
                        p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
                    } else {
                        sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "他のチームのプレイヤーにはメッセージを送信できません。");
                    }
                } else if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_JAIL, sp)) {
                    if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_ADMIN, p) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_JAIL, p)) {
                        Bukkit.getConsoleSender().sendMessage(privateMessagePrefix + msg);

                        for (Player player : Bukkit.getOnlinePlayers())
                            if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_ADMIN, player))
                                player.sendMessage(privateMessagePrefix + msg);

                        sp.sendMessage(privateMessagePrefix + msg);
                        p.sendMessage(privateMessagePrefix + msg);
                        p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
                    } else {
                        sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "他のチームのプレイヤーにはメッセージを送信できません。");
                    }
                } else if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_ADMIN, sp)) {
                    Bukkit.getConsoleSender().sendMessage(privateMessagePrefix + msg);

                    for (Player player : Bukkit.getOnlinePlayers())
                        if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_ADMIN, player))
                            player.sendMessage(privateMessagePrefix + msg);

                    sp.sendMessage(privateMessagePrefix + msg);
                    p.sendMessage(privateMessagePrefix + msg);
                    p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
                }
            } else {
                sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "自分自身にはメッセージを送信できません。");
            }
        }
    }

    public enum ChatType {
        GLOBAL(ChatColor.BLUE + "[G]" + ChatColor.RESET),
        RANGE(ChatColor.BLUE + "[R]" + ChatColor.RESET),
        TEAM(ChatColor.BLUE + "[T]" + ChatColor.RESET),
        TEAM_GLOBAL(""),
        PRIVATE(ChatColor.BLUE + "[P]" + ChatColor.RESET);

        private final String prefix;

        private ChatType(String prefix) {
            this.prefix = prefix;
        }

        public String getPrefix() {
            return prefix;
        }
    }
}

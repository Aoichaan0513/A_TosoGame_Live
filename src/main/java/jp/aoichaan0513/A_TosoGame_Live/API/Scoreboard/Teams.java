package jp.aoichaan0513.A_TosoGame_Live.API.Scoreboard;

import jp.aoichaan0513.A_TosoGame_Live.API.Manager.GameManager;
import jp.aoichaan0513.A_TosoGame_Live.API.TosoGameAPI;
import jp.aoichaan0513.A_TosoGame_Live.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import java.util.Map;
import java.util.UUID;

public class Teams {

    private static Team Toso_admin;
    private static Team Toso_player;
    private static Team Toso_hunter;
    private static Team Toso_jail;
    private static Team Toso_success;
    private static Team Toso_tuho;

    public static Scoreboard setScoreboard() {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard board = manager.getMainScoreboard();

        Toso_admin = board.getTeam(TosoGameAPI.Objective.TEAM_ADMIN.getName());
        if (Toso_admin == null) {
            Toso_admin = board.registerNewTeam(TosoGameAPI.Objective.TEAM_ADMIN.getName());
            Toso_admin.setPrefix(ChatColor.GOLD.toString());
            Toso_admin.setSuffix(ChatColor.RESET.toString());
            Toso_admin.setDisplayName(TosoGameAPI.Objective.TEAM_ADMIN.getName());
            Toso_admin.setAllowFriendlyFire(false);
            Toso_admin.setCanSeeFriendlyInvisibles(true);
            Toso_admin.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);
            Toso_admin.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.ALWAYS);
        }

        Toso_player = board.getTeam(TosoGameAPI.Objective.TEAM_PLAYER.getName());
        if (Toso_player == null) {
            Toso_player = board.registerNewTeam(TosoGameAPI.Objective.TEAM_PLAYER.getName());
            Toso_player.setPrefix(ChatColor.RESET.toString());
            Toso_player.setSuffix(ChatColor.RESET.toString());
            Toso_player.setDisplayName(TosoGameAPI.Objective.TEAM_PLAYER.getName());
            Toso_player.setAllowFriendlyFire(false);
            Toso_player.setCanSeeFriendlyInvisibles(true);
            Toso_player.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);
            Toso_player.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);
        }

        Toso_hunter = board.getTeam(TosoGameAPI.Objective.TEAM_HUNTER.getName());
        if (Toso_hunter == null) {
            Toso_hunter = board.registerNewTeam(TosoGameAPI.Objective.TEAM_HUNTER.getName());
            Toso_hunter.setPrefix(ChatColor.RED.toString());
            Toso_hunter.setSuffix(ChatColor.RESET.toString());
            Toso_hunter.setDisplayName(TosoGameAPI.Objective.TEAM_HUNTER.getName());
            Toso_hunter.setAllowFriendlyFire(false);
            Toso_hunter.setCanSeeFriendlyInvisibles(true);
            Toso_hunter.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);
            Toso_hunter.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);
        }

        Toso_jail = board.getTeam(TosoGameAPI.Objective.TEAM_JAIL.getName());
        if (Toso_jail == null) {
            Toso_jail = board.registerNewTeam(TosoGameAPI.Objective.TEAM_JAIL.getName());
            Toso_jail.setPrefix(ChatColor.BLACK.toString());
            Toso_jail.setSuffix(ChatColor.RESET.toString());
            Toso_jail.setDisplayName(TosoGameAPI.Objective.TEAM_JAIL.getName());
            Toso_jail.setAllowFriendlyFire(false);
            Toso_jail.setCanSeeFriendlyInvisibles(true);
            Toso_jail.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);
            Toso_jail.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.ALWAYS);
        }

        Toso_success = board.getTeam(TosoGameAPI.Objective.TEAM_SUCCESS.getName());
        if (Toso_success == null) {
            Toso_success = board.registerNewTeam(TosoGameAPI.Objective.TEAM_SUCCESS.getName());
            Toso_success.setPrefix(ChatColor.RESET.toString());
            Toso_success.setSuffix(ChatColor.RESET.toString());
            Toso_success.setDisplayName(TosoGameAPI.Objective.TEAM_SUCCESS.getName());
            Toso_success.setAllowFriendlyFire(false);
            Toso_success.setCanSeeFriendlyInvisibles(true);
            Toso_success.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);
            Toso_success.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);
        }

        Toso_tuho = board.getTeam(TosoGameAPI.Objective.TEAM_TUHO.getName());
        if (Toso_tuho == null) {
            Toso_tuho = board.registerNewTeam(TosoGameAPI.Objective.TEAM_TUHO.getName());
            Toso_tuho.setPrefix(ChatColor.GREEN.toString());
            Toso_tuho.setSuffix(ChatColor.RESET.toString());
            Toso_tuho.setDisplayName(TosoGameAPI.Objective.TEAM_TUHO.getName());
            Toso_tuho.setAllowFriendlyFire(false);
            Toso_tuho.setCanSeeFriendlyInvisibles(true);
            Toso_tuho.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);
            Toso_tuho.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);
        }
        return board;
    }

    public static Scoreboard setScoreboard(Scoreboard board) {
        Team Toso_admin = board.getTeam(TosoGameAPI.Objective.TEAM_ADMIN.getName());
        if (Toso_admin == null) {
            Toso_admin = board.registerNewTeam(TosoGameAPI.Objective.TEAM_ADMIN.getName());
            Toso_admin.setPrefix(ChatColor.GOLD.toString());
            Toso_admin.setSuffix(ChatColor.RESET.toString());
            Toso_admin.setDisplayName(TosoGameAPI.Objective.TEAM_ADMIN.getName());
            Toso_admin.setAllowFriendlyFire(false);
            Toso_admin.setCanSeeFriendlyInvisibles(true);
            Toso_admin.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);
            Toso_admin.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.ALWAYS);
        }

        Team Toso_player = board.getTeam(TosoGameAPI.Objective.TEAM_PLAYER.getName());
        if (Toso_player == null) {
            Toso_player = board.registerNewTeam(TosoGameAPI.Objective.TEAM_PLAYER.getName());
            Toso_player.setPrefix(ChatColor.RESET.toString());
            Toso_player.setSuffix(ChatColor.RESET.toString());
            Toso_player.setDisplayName(TosoGameAPI.Objective.TEAM_PLAYER.getName());
            Toso_player.setAllowFriendlyFire(false);
            Toso_player.setCanSeeFriendlyInvisibles(true);
            Toso_player.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);
            Toso_player.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);
        }

        Team Toso_hunter = board.getTeam(TosoGameAPI.Objective.TEAM_HUNTER.getName());
        if (Toso_hunter == null) {
            Toso_hunter = board.registerNewTeam(TosoGameAPI.Objective.TEAM_HUNTER.getName());
            Toso_hunter.setPrefix(ChatColor.RED.toString());
            Toso_hunter.setSuffix(ChatColor.RESET.toString());
            Toso_hunter.setDisplayName(TosoGameAPI.Objective.TEAM_HUNTER.getName());
            Toso_hunter.setAllowFriendlyFire(false);
            Toso_hunter.setCanSeeFriendlyInvisibles(true);
            Toso_hunter.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);
            Toso_hunter.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);
        }

        Team Toso_jail = board.getTeam(TosoGameAPI.Objective.TEAM_JAIL.getName());
        if (Toso_jail == null) {
            Toso_jail = board.registerNewTeam(TosoGameAPI.Objective.TEAM_JAIL.getName());
            Toso_jail.setPrefix(ChatColor.BLACK.toString());
            Toso_jail.setSuffix(ChatColor.RESET.toString());
            Toso_jail.setDisplayName(TosoGameAPI.Objective.TEAM_JAIL.getName());
            Toso_jail.setAllowFriendlyFire(false);
            Toso_jail.setCanSeeFriendlyInvisibles(true);
            Toso_jail.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);
            Toso_jail.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.ALWAYS);
        }

        Team Toso_success = board.getTeam(TosoGameAPI.Objective.TEAM_SUCCESS.getName());
        if (Toso_success == null) {
            Toso_success = board.registerNewTeam(TosoGameAPI.Objective.TEAM_SUCCESS.getName());
            Toso_success.setPrefix(ChatColor.RESET.toString());
            Toso_success.setSuffix(ChatColor.RESET.toString());
            Toso_success.setDisplayName(TosoGameAPI.Objective.TEAM_SUCCESS.getName());
            Toso_success.setAllowFriendlyFire(false);
            Toso_success.setCanSeeFriendlyInvisibles(true);
            Toso_success.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);
            Toso_success.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);
        }

        Team Toso_tuho = board.getTeam(TosoGameAPI.Objective.TEAM_TUHO.getName());
        if (Toso_tuho == null) {
            Toso_tuho = board.registerNewTeam(TosoGameAPI.Objective.TEAM_TUHO.getName());
            Toso_tuho.setPrefix(ChatColor.GREEN.toString());
            Toso_tuho.setSuffix(ChatColor.RESET.toString());
            Toso_tuho.setDisplayName(TosoGameAPI.Objective.TEAM_TUHO.getName());
            Toso_tuho.setAllowFriendlyFire(false);
            Toso_tuho.setCanSeeFriendlyInvisibles(true);
            Toso_tuho.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);
            Toso_tuho.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);
        }
        return board;
    }

    public static void resetScoreboard() {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard board = manager.getMainScoreboard();

        Toso_admin.unregister();
        Toso_player.unregister();
        Toso_hunter.unregister();
        Toso_jail.unregister();
        Toso_success.unregister();
        Toso_tuho.unregister();

        setScoreboard();
    }

    public static void resetScoreboard(Scoreboard board) {
        board.getTeam(TosoGameAPI.Objective.TEAM_ADMIN.getName()).unregister();
        board.getTeam(TosoGameAPI.Objective.TEAM_PLAYER.getName()).unregister();
        board.getTeam(TosoGameAPI.Objective.TEAM_HUNTER.getName()).unregister();
        board.getTeam(TosoGameAPI.Objective.TEAM_JAIL.getName()).unregister();
        board.getTeam(TosoGameAPI.Objective.TEAM_SUCCESS.getName()).unregister();
        board.getTeam(TosoGameAPI.Objective.TEAM_TUHO.getName()).unregister();

        setScoreboard(board);
    }

    public static Team getOnlineTeam(OnlineTeam team) {
        switch (team) {
            case TOSO_ADMIN:
                return Toso_admin;
            case TOSO_HUNTER:
                return Toso_hunter;
            case TOSO_JAIL:
                return Toso_jail;
            case TOSO_SUCCESS:
                return Toso_success;
            case TOSO_TUHO:
                return Toso_tuho;
            default:
                return Toso_player;
        }
    }

    public static Team getOnlineTeam(OnlineTeam team, Scoreboard board) {
        return board.getTeam(team.getName());
    }

    public static OnlineTeam getJoinedTeam(Player p) {
        if (Toso_admin.hasEntry(p.getName()))
            return OnlineTeam.TOSO_ADMIN;
        if (Toso_hunter.hasEntry(p.getName()))
            return OnlineTeam.TOSO_HUNTER;
        if (Toso_jail.hasEntry(p.getName()))
            return OnlineTeam.TOSO_JAIL;
        if (Toso_success.hasEntry(p.getName()))
            return OnlineTeam.TOSO_SUCCESS;
        if (Toso_tuho.hasEntry(p.getName()))
            return OnlineTeam.TOSO_TUHO;
        else
            return OnlineTeam.TOSO_PLAYER;
    }

    public static OnlineTeam getJoinedTeam(Player p, Scoreboard board) {
        if (board.getTeam(OnlineTeam.TOSO_ADMIN.getName()).hasEntry(p.getName()))
            return OnlineTeam.TOSO_ADMIN;
        if (board.getTeam(OnlineTeam.TOSO_HUNTER.getName()).hasEntry(p.getName()))
            return OnlineTeam.TOSO_HUNTER;
        if (board.getTeam(OnlineTeam.TOSO_JAIL.getName()).hasEntry(p.getName()))
            return OnlineTeam.TOSO_JAIL;
        if (board.getTeam(OnlineTeam.TOSO_SUCCESS.getName()).hasEntry(p.getName()))
            return OnlineTeam.TOSO_SUCCESS;
        if (board.getTeam(OnlineTeam.TOSO_TUHO.getName()).hasEntry(p.getName()))
            return OnlineTeam.TOSO_TUHO;
        else
            return OnlineTeam.TOSO_PLAYER;
    }

    public static int getOnlineCount(OnlineTeam team) {
        int i = 0;

        for (String name : getOnlineTeam(team).getEntries()) {
            if (Bukkit.getPlayerExact(name) == null) continue;
            i++;
        }

        return i;
    }

    public static boolean hasJoinedTeam(OnlineTeam team, Player p) {
        return getOnlineTeam(team).hasEntry(p.getName());
    }

    public static boolean hasJoinedTeams(Player p) {
        for (OnlineTeam onlineTeam : OnlineTeam.values())
            if (getOnlineTeam(onlineTeam).hasEntry(p.getName()))
                return true;
        return false;
    }

    public static void joinTeam(OnlineTeam team, Player p) {
        getOnlineTeam(team).addEntry(p.getName());

        // setTeamOptions();
        setTeamOption(p);

        for (Map.Entry<UUID, Scoreboard> entry : ScoreBoard.getBoardMap().entrySet()) {
            if (Bukkit.getPlayer(entry.getKey()) == null) continue;
            getOnlineTeam(team, entry.getValue()).addEntry(p.getName());
            setTeamOption(entry.getValue());
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            Scoreboard board = ScoreBoard.getBoard(player);

            if (GameManager.isGame() && (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_PLAYER, player) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_SUCCESS, player))) {
                if (player.getInventory().getItemInMainHand().getType() == Material.BOOK) {
                    ItemMeta itemMeta = player.getInventory().getItemInMainHand().getItemMeta();
                    if (ChatColor.stripColor(itemMeta.getDisplayName()).equals(Main.PHONE_ITEM_NAME)) {
                        board.getObjective(TosoGameAPI.Objective.SIDEBAR.getName()).setDisplaySlot(org.bukkit.scoreboard.DisplaySlot.SIDEBAR);
                    } else {
                        board.clearSlot(org.bukkit.scoreboard.DisplaySlot.SIDEBAR);
                    }
                } else if (player.getInventory().getItemInOffHand().getType() == Material.BOOK) {
                    ItemMeta itemMeta = player.getInventory().getItemInOffHand().getItemMeta();
                    if (ChatColor.stripColor(itemMeta.getDisplayName()).equals(Main.PHONE_ITEM_NAME)) {
                        board.getObjective(TosoGameAPI.Objective.SIDEBAR.getName()).setDisplaySlot(org.bukkit.scoreboard.DisplaySlot.SIDEBAR);
                    } else {
                        board.clearSlot(org.bukkit.scoreboard.DisplaySlot.SIDEBAR);
                    }
                } else {
                    board.clearSlot(org.bukkit.scoreboard.DisplaySlot.SIDEBAR);
                }
            } else {
                board.getObjective(TosoGameAPI.Objective.SIDEBAR.getName()).setDisplaySlot(org.bukkit.scoreboard.DisplaySlot.SIDEBAR);
            }
        }
        return;
    }

    public static void leaveTeam(OnlineTeam team, Player p) {
        getOnlineTeam(team).removeEntry(p.getName());

        // setTeamOptions();
        setTeamOption(p);

        for (Scoreboard board : ScoreBoard.getBoardMap().values())
            board.getTeam(team.getName()).removeEntry(p.getName());

        for (Player player : Bukkit.getOnlinePlayers()) {
            Scoreboard board = ScoreBoard.getBoard(player);

            if (GameManager.isGame() && (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_PLAYER, player) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_SUCCESS, player))) {
                if (player.getInventory().getItemInMainHand().getType() == Material.BOOK) {
                    ItemMeta itemMeta = player.getInventory().getItemInMainHand().getItemMeta();
                    if (ChatColor.stripColor(itemMeta.getDisplayName()).equals(Main.PHONE_ITEM_NAME)) {
                        board.getObjective(TosoGameAPI.Objective.SIDEBAR.getName()).setDisplaySlot(org.bukkit.scoreboard.DisplaySlot.SIDEBAR);
                    } else {
                        board.clearSlot(org.bukkit.scoreboard.DisplaySlot.SIDEBAR);
                    }
                } else if (player.getInventory().getItemInOffHand().getType() == Material.BOOK) {
                    ItemMeta itemMeta = player.getInventory().getItemInOffHand().getItemMeta();
                    if (ChatColor.stripColor(itemMeta.getDisplayName()).equals(Main.PHONE_ITEM_NAME)) {
                        board.getObjective(TosoGameAPI.Objective.SIDEBAR.getName()).setDisplaySlot(org.bukkit.scoreboard.DisplaySlot.SIDEBAR);
                    } else {
                        board.clearSlot(org.bukkit.scoreboard.DisplaySlot.SIDEBAR);
                    }
                } else {
                    board.clearSlot(org.bukkit.scoreboard.DisplaySlot.SIDEBAR);
                }
            } else {
                board.getObjective(TosoGameAPI.Objective.SIDEBAR.getName()).setDisplaySlot(org.bukkit.scoreboard.DisplaySlot.SIDEBAR);
            }
        }
        return;
    }

    public static void setTeamOptions() {
        for (Scoreboard board : ScoreBoard.getBoardMap().values()) {
            for (Team t : board.getTeams()) {
                t.setColor(getOnlineTeam(t.getDisplayName()).getColor());
                t.setPrefix(getOnlineTeam(t.getDisplayName()).getColor().toString());
                t.setSuffix(ChatColor.RESET.toString());

                t.setAllowFriendlyFire(false);
                t.setCanSeeFriendlyInvisibles(true);
                t.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);
                t.setOption(Team.Option.NAME_TAG_VISIBILITY, t.getDisplayName().equalsIgnoreCase(OnlineTeam.TOSO_ADMIN.getName()) || t.getDisplayName().equalsIgnoreCase(OnlineTeam.TOSO_JAIL.getName()) ? Team.OptionStatus.ALWAYS : Team.OptionStatus.NEVER);
            }
        }
    }

    public static void setTeamOption(Player p) {
        setTeamOption(ScoreBoard.getBoard(p));
    }

    public static void setTeamOption(Scoreboard board) {
        for (Team t : board.getTeams()) {
            t.setColor(getOnlineTeam(t.getDisplayName()).getColor());
            t.setPrefix(getOnlineTeam(t.getDisplayName()).getColor().toString());
            t.setSuffix(ChatColor.RESET.toString());

            t.setAllowFriendlyFire(false);
            t.setCanSeeFriendlyInvisibles(true);
            t.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);
            t.setOption(Team.Option.NAME_TAG_VISIBILITY, t.getDisplayName().equalsIgnoreCase(OnlineTeam.TOSO_ADMIN.getName()) || t.getDisplayName().equalsIgnoreCase(OnlineTeam.TOSO_JAIL.getName()) ? Team.OptionStatus.ALWAYS : Team.OptionStatus.NEVER);
        }
    }

    public static String getTeam(DisplaySlot slot, Player p) {
        if (slot == DisplaySlot.CHAT) {
            if (Toso_admin.hasEntry(p.getName()))
                return ChatColor.GOLD + "[" + OnlineTeam.TOSO_ADMIN.getDisplayName() + "]" + ChatColor.RESET;
            if (Toso_player.hasEntry(p.getName()))
                return ChatColor.WHITE + "[" + OnlineTeam.TOSO_PLAYER.getDisplayName() + "]" + ChatColor.RESET;
            if (Toso_hunter.hasEntry(p.getName()))
                return ChatColor.RED + "[" + OnlineTeam.TOSO_HUNTER.getDisplayName() + "]" + ChatColor.RESET;
            if (Toso_jail.hasEntry(p.getName()))
                return ChatColor.BLACK + "[" + OnlineTeam.TOSO_JAIL.getDisplayName() + "]" + ChatColor.RESET;
            if (Toso_success.hasEntry(p.getName()))
                return ChatColor.BLUE + "[" + OnlineTeam.TOSO_SUCCESS.getDisplayName() + "]" + ChatColor.RESET;
            if (Toso_tuho.hasEntry(p.getName()))
                return ChatColor.GREEN + "[" + OnlineTeam.TOSO_TUHO.getDisplayName() + "]" + ChatColor.RESET;
            else
                return "";
        } else if (slot == DisplaySlot.SIDEBAR) {
            if (Toso_admin.hasEntry(p.getName()))
                return OnlineTeam.TOSO_ADMIN.getDisplayName() + ChatColor.RESET;
            if (Toso_player.hasEntry(p.getName()))
                return OnlineTeam.TOSO_PLAYER.getDisplayName() + ChatColor.RESET;
            if (Toso_hunter.hasEntry(p.getName()))
                return OnlineTeam.TOSO_HUNTER.getDisplayName() + ChatColor.RESET;
            if (Toso_jail.hasEntry(p.getName()))
                return OnlineTeam.TOSO_JAIL.getDisplayName() + ChatColor.RESET;
            if (Toso_success.hasEntry(p.getName()))
                return OnlineTeam.TOSO_SUCCESS.getDisplayName() + ChatColor.RESET;
            if (Toso_tuho.hasEntry(p.getName()))
                return OnlineTeam.TOSO_TUHO.getDisplayName() + ChatColor.RESET;
            else
                return "";
        }
        return "";
    }

    public static OnlineTeam getOnlineTeam(String name) {
        if (name.equalsIgnoreCase(TosoGameAPI.Objective.TEAM_ADMIN.getName()))
            return OnlineTeam.TOSO_ADMIN;
        if (name.equalsIgnoreCase(TosoGameAPI.Objective.TEAM_PLAYER.getName()))
            return OnlineTeam.TOSO_PLAYER;
        if (name.equalsIgnoreCase(TosoGameAPI.Objective.TEAM_HUNTER.getName()))
            return OnlineTeam.TOSO_HUNTER;
        if (name.equalsIgnoreCase(TosoGameAPI.Objective.TEAM_JAIL.getName()))
            return OnlineTeam.TOSO_JAIL;
        if (name.equalsIgnoreCase(TosoGameAPI.Objective.TEAM_SUCCESS.getName()))
            return OnlineTeam.TOSO_SUCCESS;
        if (name.equalsIgnoreCase(TosoGameAPI.Objective.TEAM_TUHO.getName()))
            return OnlineTeam.TOSO_TUHO;
        else
            return OnlineTeam.TOSO_PLAYER;
    }

    public enum OnlineTeam {
        TOSO_ADMIN(TosoGameAPI.Objective.TEAM_ADMIN.getName(), ChatColor.GOLD, ChatColor.GOLD + "運営"),
        TOSO_PLAYER(TosoGameAPI.Objective.TEAM_PLAYER.getName(), ChatColor.WHITE, ChatColor.WHITE + "逃走者"),
        TOSO_HUNTER(TosoGameAPI.Objective.TEAM_HUNTER.getName(), ChatColor.RED, ChatColor.RED + "ハンター"),
        TOSO_JAIL(TosoGameAPI.Objective.TEAM_JAIL.getName(), ChatColor.BLACK, ChatColor.BLACK + "牢獄"),
        TOSO_SUCCESS(TosoGameAPI.Objective.TEAM_SUCCESS.getName(), ChatColor.BLUE, ChatColor.BLUE + "生存者"),
        TOSO_TUHO(TosoGameAPI.Objective.TEAM_TUHO.getName(), ChatColor.GREEN, ChatColor.GREEN + "通報部隊");

        private final String name;
        private final ChatColor color;
        private final String displayName;

        private OnlineTeam(String name, ChatColor color, String displayName) {
            this.name = name;
            this.color = color;
            this.displayName = displayName;
        }

        public String getName() {
            return name;
        }

        public ChatColor getColor() {
            return color;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    public enum OfflineTeam {
        TOSO_ADMIN,
        TOSO_PLAYER,
        TOSO_HUNTER,
        TOSO_JAIL,
        TOSO_TUHO,
    }

    public enum DisplaySlot {
        CHAT,
        SIDEBAR
    }
}

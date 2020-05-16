package jp.aoichaan0513.A_TosoGame_Live.API.Manager.Inventory.Left;

import jp.aoichaan0513.A_TosoGame_Live.API.Scoreboard.Teams;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Arrays;

public class PlayerListInventory {

    public static final String title = ChatColor.DARK_GRAY + "> " + ChatColor.BOLD + "ホーム" + ChatColor.RESET + ChatColor.DARK_GRAY + " > " + ChatColor.BOLD + "電話" + ChatColor.DARK_GRAY + " > " + ChatColor.BOLD + "プレイヤーリスト";

    public static Inventory getInventory() {
        Inventory inv = Bukkit.createInventory(null, 9 * 6, title);
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_ADMIN, player) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_PLAYER, player) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_SUCCESS, player)) {
                ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD, 1, (short) SkullType.PLAYER.ordinal());
                SkullMeta itemMeta = (SkullMeta) itemStack.getItemMeta();
                itemMeta.setDisplayName(ChatColor.BOLD + player.getName());
                itemMeta.setLore(Arrays.asList(ChatColor.YELLOW + "所属チーム" + ChatColor.GRAY + ": " + (Teams.getTeam(Teams.DisplaySlot.SIDEBAR, player).equals("") ? "不明" : Teams.getTeam(Teams.DisplaySlot.SIDEBAR, player)), ChatColor.YELLOW + "クリックして通話を開始します。"));
                itemMeta.setOwner(player.getName());
                itemStack.setItemMeta(itemMeta);
                inv.addItem(itemStack);
            }
        }
        ItemStack teamStack = new ItemStack(Material.QUARTZ_BLOCK);
        ItemMeta teamMeta = teamStack.getItemMeta();
        teamMeta.setDisplayName(ChatColor.BOLD + "チームチャット");
        teamMeta.setLore(Arrays.asList(ChatColor.YELLOW + "この機能は今後使用可能になります。"));
        teamStack.setItemMeta(teamMeta);

        ItemStack grayStack = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta grayMeta = grayStack.getItemMeta();
        grayMeta.setDisplayName(ChatColor.BOLD + "");
        grayStack.setItemMeta(grayMeta);

        ItemStack homeStack = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta homeMeta = homeStack.getItemMeta();
        homeMeta.setDisplayName(ChatColor.BOLD + "ホーム");
        homeMeta.setLore(Arrays.asList(ChatColor.YELLOW + "ホーム画面を表示します。"));
        homeStack.setItemMeta(homeMeta);

        inv.setItem(45, teamStack);
        inv.setItem(46, grayStack);
        inv.setItem(47, grayStack);
        inv.setItem(48, grayStack);
        inv.setItem(49, homeStack);
        inv.setItem(50, grayStack);
        inv.setItem(51, grayStack);
        inv.setItem(52, grayStack);
        inv.setItem(53, grayStack);
        return inv;
    }
}

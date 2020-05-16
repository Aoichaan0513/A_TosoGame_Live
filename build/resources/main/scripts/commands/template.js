// 書き方は https://hub.spigotmc.org/javadocs/bukkit/overview-summary.html を参照。

var Bukkit = Java.type('org.bukkit.Bukkit');
var ChatColor = Java.type('org.bukkit.ChatColor');
var GameMode = Java.type('org.bukkit.GameMode');

function onCommand(sp, args) {
    sp.setGameMode(GameMode.CREATIVE);
    sp.sendMessage(ChatColor.YELLOW + "> " + ChatColor.GOLD + "あなたのゲームモードを" + ChatColor.BOLD + ChatColor.UNDERLINE + "クリエイティブ" + ChatColor.RESET + ChatColor.GOLD + "に変更しました。");
    Bukkit.broadcastMessage("やーい" + sp.getName() + "のばかー！");
}
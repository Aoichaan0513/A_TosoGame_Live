// 書き方は https://hub.spigotmc.org/javadocs/bukkit/overview-summary.html を参照。

var Bukkit = Java.type('org.bukkit.Bukkit');
var ChatColor = Java.type('org.bukkit.ChatColor');

function onMissionStart() {
    // ミッションが開始されたらこれが呼び出される。
    // 生存ミッションが開始されたときに、メッセージを送信する例。
    Bukkit.broadcastMessage(ChatColor.GOLD + "[運営] " + ChatColor.RESET + "???" + ChatColor.GREEN + ": " + ChatColor.RESET + "このミッション嫌いなんだよ！");
}

function onMissionEnd() {
    // ミッションが終了されたらこれが呼び出される。
}
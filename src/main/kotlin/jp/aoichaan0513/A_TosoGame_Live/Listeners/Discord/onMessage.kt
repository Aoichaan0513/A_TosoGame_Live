package jp.aoichaan0513.A_TosoGame_Live.Listeners.Discord

import jp.aoichaan0513.A_TosoGame_Live.API.Manager.DiscordManager
import jp.aoichaan0513.A_TosoGame_Live.Main
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import java.time.Instant

class onMessage : ListenerAdapter() {

    override fun onPrivateMessageReceived(e: PrivateMessageReceivedEvent) {
        val jda = e.jda
        val channel = e.channel
        val user = e.author
        val msg = e.message

        if (user.isBot || !msg.contentRaw.matches(Regex("^[0-9]{8}+\$"))) return
        if (DiscordManager.hashMap.containsKey(msg.contentRaw)) {
            val uuid = DiscordManager.hashMap.remove(msg.contentRaw)!!
            val guild = jda.getCategoryById(Main.mainConfig.getLong("discordIntegration.categoryId"))?.guild!!

            DiscordManager.integrationMap[uuid] = user.idLong

            val embedBuilder = EmbedBuilder().setTitle("成功 - 連携").setDescription("""
                あなたのDiscord アカウントとMinecraft IDを連携しました。
                これにより逃走中での一部機能が利用可能になります。
                例: 通話機能
            """.trimIndent()).setTimestamp(Instant.now())

            channel.sendMessage(embedBuilder.build()).queue()
        } else {
            val embedBuilder = EmbedBuilder().setTitle("失敗 - 連携").setDescription("""
                あなたのDiscord アカウントとMinecraft IDを連携できませんでした。
                コードが合っているかを確認してください。
            """.trimIndent()).setTimestamp(Instant.now())

            channel.sendMessage(embedBuilder.build()).queue()
        }
    }
}
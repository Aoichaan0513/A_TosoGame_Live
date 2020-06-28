package jp.aoichaan0513.A_TosoGame_Live.API.Manager

import org.bukkit.Bukkit
import org.json.JSONObject
import java.net.URL
import java.util.*
import java.util.logging.Level

class SkinManager(var uuid: String) {
    var skinName: String? = null
    var skinValue: String? = null
    var skinSignatur: String? = null
    private fun load() {
        try {
            val url = URL("https://sessionserver.mojang.com/session/minecraft/profile/$uuid?unsigned=false")
            val uc = url.openConnection()
            uc.useCaches = false
            uc.defaultUseCaches = false
            uc.addRequestProperty("User-Agent", "Mozilla/5.0")
            uc.addRequestProperty("Cache-Control", "no-cache, no-store, must-revalidate")
            uc.addRequestProperty("Pragma", "no-cache")
            val json = Scanner(uc.getInputStream(), "UTF-8").useDelimiter("\\A").next()
            val jsonObject = JSONObject(json)
            val jsonArray = jsonObject.getJSONArray("properties")
            for (i in 0 until jsonArray.length()) {
                try {
                    val property = jsonArray.getJSONObject(i)
                    skinName = property.getString("name")
                    skinValue = property.getString("value")
                    skinSignatur = if (property.has("signature")) property.getString("signature") else null
                } catch (e: Exception) {
                    Bukkit.getLogger().log(Level.WARNING, "Failed to apply auth property", e)
                }
            }
        } catch (e: Exception) {
            // Failed to load skin
        }
    }

    init {
        load()
    }
}
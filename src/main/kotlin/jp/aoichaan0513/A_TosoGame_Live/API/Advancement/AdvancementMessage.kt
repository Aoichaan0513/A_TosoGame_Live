package jp.aoichaan0513.A_TosoGame_Live.API.Advancement

import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import jp.aoichaan0513.A_TosoGame_Live.Main
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.advancement.AdvancementProgress
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable

class AdvancementMessage(private val id: NamespacedKey, private val title: String, private val description: String, icon: Material) {
    private val icon: String

    constructor(id: String, title: String, description: String, icon: Material) : this(NamespacedKey(Main.pluginInstance, id), title, description, icon)

    fun showTo(player: Player) {
        showTo(listOf(player))
    }

    fun showTo(players: Collection<Player>) {
        add()
        grant(players)
        object : BukkitRunnable() {
            override fun run() {
                revoke(players)
            }
        }.runTaskLater(Main.pluginInstance, 20)
    }

    private fun add() {
        try {
            Bukkit.getUnsafe().loadAdvancement(id, json)
            Bukkit.getLogger().info("Advancement $id saved")
        } catch (e: IllegalArgumentException) {
            Bukkit.getLogger().info("Error while saving, Advancement $id seems to already exist")
        }
    }

    private fun remove() {
        Bukkit.getUnsafe().removeAdvancement(id)
    }

    private fun grant(players: Collection<Player>) {
        val advancement = Bukkit.getAdvancement(id)
        var progress: AdvancementProgress
        for (player in players) {
            progress = player.getAdvancementProgress(advancement!!)
            if (!progress.isDone)
                for (criteria in progress.remainingCriteria)
                    progress.awardCriteria(criteria)
        }
    }

    private fun revoke(players: Collection<Player>) {
        val advancement = Bukkit.getAdvancement(id)
        var progress: AdvancementProgress
        for (player in players) {
            progress = player.getAdvancementProgress(advancement!!)
            if (progress.isDone)
                for (criteria in progress.awardedCriteria)
                    progress.revokeCriteria(criteria)
        }
    }

    val json: String
        get() {
            val iconObject = JsonObject()
            iconObject.addProperty("item", icon)
            val displayObject = JsonObject()
            displayObject.add("icon", iconObject)
            displayObject.addProperty("title", title)
            displayObject.addProperty("description", description)
            displayObject.addProperty("background", "minecraft:textures/gui/advancements/backgrounds/adventure.png")
            displayObject.addProperty("frame", "task")
            displayObject.addProperty("announce_to_chat", false)
            displayObject.addProperty("show_toast", true)
            displayObject.addProperty("hidden", true)
            val triggerObject = JsonObject()
            triggerObject.addProperty("trigger", "minecraft:impossible")
            val criteriaObject = JsonObject()
            criteriaObject.add("impossible", triggerObject)
            val jsonObject = JsonObject()
            jsonObject.add("criteria", criteriaObject)
            jsonObject.add("display", displayObject)
            val gson = GsonBuilder().setPrettyPrinting().create()
            return gson.toJson(jsonObject)
        }

    init {
        this.icon = NamespacedKey.minecraft(icon.name.toLowerCase()).toString()
    }
}
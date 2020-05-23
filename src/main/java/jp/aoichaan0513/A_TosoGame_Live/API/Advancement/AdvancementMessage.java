package jp.aoichaan0513.A_TosoGame_Live.API.Advancement;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import jp.aoichaan0513.A_TosoGame_Live.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.advancement.Advancement;
import org.bukkit.advancement.AdvancementProgress;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collection;
import java.util.Collections;

public class AdvancementMessage {

    private final NamespacedKey id;
    private final String icon;
    private final String title;
    private final String description;

    public AdvancementMessage(String id, String title, String description, Material icon) {
        this(new NamespacedKey(Main.getInstance(), id), title, description, icon);
    }

    public AdvancementMessage(NamespacedKey id, String title, String description, Material icon) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.icon = NamespacedKey.minecraft(icon.name().toLowerCase()).toString();
    }

    public void showTo(Player player) {
        showTo(Collections.singletonList(player));
    }

    public void showTo(Collection<? extends Player> players) {
        add();
        grant(players);
        new BukkitRunnable() {

            @Override
            public void run() {
                revoke(players);
            }
        }.runTaskLater(Main.getInstance(), 20);
    }

    private void add() {
        try {
            Bukkit.getUnsafe().loadAdvancement(id, getJson());
            Bukkit.getLogger().info("Advancement " + id + " saved");
        } catch (IllegalArgumentException e) {
            Bukkit.getLogger().info("Error while saving, Advancement " + id + " seems to already exist");
        }
    }

    private void remove() {
        Bukkit.getUnsafe().removeAdvancement(id);
    }

    private void grant(Collection<? extends Player> players) {
        Advancement advancement = Bukkit.getAdvancement(id);
        AdvancementProgress progress;
        for (Player player : players) {

            progress = player.getAdvancementProgress(advancement);
            if (!progress.isDone())
                for (String criteria : progress.getRemainingCriteria())
                    progress.awardCriteria(criteria);
        }
    }

    private void revoke(Collection<? extends Player> players) {
        Advancement advancement = Bukkit.getAdvancement(id);
        AdvancementProgress progress;
        for (Player player : players) {

            progress = player.getAdvancementProgress(advancement);
            if (progress.isDone())
                for (String criteria : progress.getAwardedCriteria())
                    progress.revokeCriteria(criteria);
        }
    }

    public String getJson() {
        JsonObject iconObject = new JsonObject();
        iconObject.addProperty("item", this.icon);

        JsonObject displayObject = new JsonObject();
        displayObject.add("icon", iconObject);
        displayObject.addProperty("title", this.title);
        displayObject.addProperty("description", this.description);
        displayObject.addProperty("background", "minecraft:textures/gui/advancements/backgrounds/adventure.png");
        displayObject.addProperty("frame", "task");
        displayObject.addProperty("announce_to_chat", false);
        displayObject.addProperty("show_toast", true);
        displayObject.addProperty("hidden", true);

        JsonObject triggerObject = new JsonObject();
        triggerObject.addProperty("trigger", "minecraft:impossible");

        JsonObject criteriaObject = new JsonObject();
        criteriaObject.add("impossible", triggerObject);

        JsonObject jsonObject = new JsonObject();

        jsonObject.add("criteria", criteriaObject);
        jsonObject.add("display", displayObject);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(jsonObject);

    }
}

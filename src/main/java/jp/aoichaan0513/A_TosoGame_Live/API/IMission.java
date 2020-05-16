package jp.aoichaan0513.A_TosoGame_Live.API;

import jp.aoichaan0513.A_TosoGame_Live.API.Manager.MissionManager;
import org.bukkit.Material;

public class IMission {

    private final String title;
    private final String description;
    private final MissionManager.MissionBookType type;

    private final Material material;
    private final int amount;

    public IMission(String title, String description, MissionManager.MissionBookType type, Material material, int amount) {
        this.title = title;
        this.description = description;
        this.type = type;

        this.material = material;
        this.amount = amount;
    }

    public String getTitle() {
        switch (type) {
            case MISSION:
                return title;
            case END_MISSION:
                return title + " (終了)";
            default:
                return type.getName();
        }
    }

    public String getDescription() {
        return description;
    }

    public MissionManager.MissionBookType getType() {
        return type;
    }

    public Material getMaterial() {
        return material;
    }

    public int getAmount() {
        return amount;
    }
}

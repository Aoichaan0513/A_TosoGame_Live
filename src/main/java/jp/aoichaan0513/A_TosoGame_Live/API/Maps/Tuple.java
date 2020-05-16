package jp.aoichaan0513.A_TosoGame_Live.API.Maps;

import org.bukkit.Material;

public class Tuple {

    private Material material;
    private byte b;

    public Tuple(Material material, byte b) {
        this.material = material;
        this.b = b;
    }

    public Material getMaterial() {
        return material;
    }

    public byte getByte() {
        return b;
    }
}

package jp.aoichaan0513.A_TosoGame_Live.API.Maps;

import org.bukkit.Bukkit;
import org.bukkit.Material;

import java.util.Arrays;
import java.util.HashMap;

public class BlockColor {

    public static HashMap<Material, RgbColor> color_map = new HashMap<Material, RgbColor>() {
        {
            put(Material.OAK_FENCE, new RgbColor(143, 119, 72));
            put(Material.SPRUCE_FENCE, new RgbColor(102, 76, 51));
            put(Material.BIRCH_FENCE, new RgbColor(247, 233, 163));
            put(Material.JUNGLE_FENCE, new RgbColor(151, 109, 77));
            put(Material.ACACIA_FENCE, new RgbColor(216, 127, 51));
            put(Material.DARK_OAK_FENCE, new RgbColor(102, 76, 51));
            put(Material.NETHER_BRICK_FENCE, new RgbColor(247, 233, 163));
            put(Material.TERRACOTTA, new RgbColor(184, 116, 86));

            put(Material.OAK_FENCE_GATE, new RgbColor(143, 119, 72));
            put(Material.SPRUCE_FENCE_GATE, new RgbColor(102, 76, 51));
            put(Material.BIRCH_FENCE_GATE, new RgbColor(247, 233, 163));
            put(Material.JUNGLE_FENCE_GATE, new RgbColor(151, 109, 77));
            put(Material.ACACIA_FENCE_GATE, new RgbColor(216, 127, 51));
            put(Material.DARK_OAK_FENCE_GATE, new RgbColor(102, 76, 51));

            put(Material.CHEST, new RgbColor(184, 128, 44));
            put(Material.TRAPPED_CHEST, new RgbColor(184, 128, 44));
            put(Material.ENDER_CHEST, new RgbColor(57, 81, 83));

            put(Material.BEACON, new RgbColor(165, 241, 241));

            put(Material.STONE, new RgbColor(153, 153, 153));
            put(Material.STONE_BRICKS, new RgbColor(112, 112, 112));
            put(Material.STONE_BRICK_STAIRS, new RgbColor(112, 112, 112));
            put(Material.REDSTONE_LAMP, new RgbColor(136, 92, 64));

            put(Material.GRASS, new RgbColor(127, 178, 56));
            put(Material.GRASS_BLOCK, new RgbColor(127, 178, 56));

            //put(Material.SAND, new RgbColor(247, 233, 163));
            put(Material.SANDSTONE, new RgbColor(247, 233, 163));
            //put(Material.WOOD, new RgbColor(247, 233, 163));
            put(Material.BROWN_MUSHROOM, new RgbColor(247, 233, 163));
            put(Material.BONE_BLOCK, new RgbColor(247, 233, 163));
            put(Material.GLOWSTONE, new RgbColor(247, 233, 163));
            put(Material.END_STONE, new RgbColor(247, 233, 163));

            put(Material.OAK_STAIRS, new RgbColor(143, 119, 72));
            put(Material.BIRCH_STAIRS, new RgbColor(247, 233, 163));
            put(Material.JUNGLE_STAIRS, new RgbColor(151, 109, 77));

            put(Material.TNT, new RgbColor(255, 0, 0));
            put(Material.REDSTONE_BLOCK, new RgbColor(255, 0, 0));
            put(Material.LAVA, new RgbColor(255, 0, 0));

            put(Material.ICE, new RgbColor(160, 160, 255));
            put(Material.FROSTED_ICE, new RgbColor(160, 160, 255));
            put(Material.PACKED_ICE, new RgbColor(160, 160, 255));

            put(Material.IRON_BLOCK, new RgbColor(167, 167, 167));
            put(Material.HEAVY_WEIGHTED_PRESSURE_PLATE, new RgbColor(167, 167, 167));

            put(Material.OAK_LEAVES, new RgbColor(0, 124, 0));
            put(Material.SPRUCE_LEAVES, new RgbColor(0, 124, 0));
            put(Material.BIRCH_LEAVES, new RgbColor(0, 124, 0));
            put(Material.JUNGLE_LEAVES, new RgbColor(0, 124, 0));
            put(Material.ACACIA_LEAVES, new RgbColor(0, 124, 0));
            put(Material.DARK_OAK_LEAVES, new RgbColor(0, 124, 0));

            //put(Material.WOOL, new RgbColor(0, 0, 0));
            put(Material.SNOW_BLOCK, new RgbColor(0, 0, 0));

            put(Material.CLAY, new RgbColor(164, 168, 184));

            put(Material.BROWN_MUSHROOM, new RgbColor(151, 109, 77));
            put(Material.DIRT, new RgbColor(151, 109, 77));
            put(Material.FARMLAND, new RgbColor(151, 109, 77));
            put(Material.JUKEBOX, new RgbColor(151, 109, 77));
            put(Material.GRASS_PATH, new RgbColor(151, 109, 77));

            put(Material.MOSSY_COBBLESTONE, new RgbColor(112, 112, 112));
            put(Material.COBBLESTONE_WALL, new RgbColor(112, 112, 112));
            put(Material.COBBLESTONE_STAIRS, new RgbColor(112, 112, 112));
            put(Material.BRICK, new RgbColor(112, 112, 112));
            put(Material.BRICK_STAIRS, new RgbColor(112, 112, 112));
            put(Material.GRAVEL, new RgbColor(112, 112, 112));
            put(Material.COAL_ORE, new RgbColor(112, 112, 112));
            put(Material.IRON_ORE, new RgbColor(112, 112, 112));
            put(Material.GOLD_ORE, new RgbColor(112, 112, 112));
            put(Material.REDSTONE_ORE, new RgbColor(112, 112, 112));
            put(Material.LAPIS_ORE, new RgbColor(112, 112, 112));
            put(Material.DIAMOND_ORE, new RgbColor(112, 112, 112));
            put(Material.EMERALD_ORE, new RgbColor(112, 112, 112));
            put(Material.BEDROCK, new RgbColor(112, 112, 112));
            put(Material.FURNACE, new RgbColor(112, 112, 112));
            put(Material.DISPENSER, new RgbColor(112, 112, 112));
            put(Material.DROPPER, new RgbColor(112, 112, 112));
            put(Material.HOPPER, new RgbColor(112, 112, 112));
            put(Material.OBSERVER, new RgbColor(112, 112, 112));

            put(Material.WATER, new RgbColor(64, 64, 255));

            put(Material.CRAFTING_TABLE, new RgbColor(143, 119, 72));
            put(Material.BOOKSHELF, new RgbColor(143, 119, 72));
            put(Material.NOTE_BLOCK, new RgbColor(143, 119, 72));

            put(Material.QUARTZ_BLOCK, new RgbColor(255, 252, 245));
            put(Material.QUARTZ_STAIRS, new RgbColor(255, 252, 245));
            put(Material.SEA_LANTERN, new RgbColor(255, 252, 245));

            put(Material.ACACIA_STAIRS, new RgbColor(216, 127, 51));
            put(Material.PUMPKIN, new RgbColor(216, 127, 51));
            put(Material.JACK_O_LANTERN, new RgbColor(216, 127, 51));
            put(Material.RED_SANDSTONE, new RgbColor(216, 127, 51));
            put(Material.RED_SANDSTONE_STAIRS, new RgbColor(216, 127, 51));
            put(Material.RED_SANDSTONE_SLAB, new RgbColor(216, 127, 51));

            put(Material.PURPUR_BLOCK, new RgbColor(178, 76, 216));
            put(Material.PURPUR_PILLAR, new RgbColor(178, 76, 216));
            put(Material.PURPUR_SLAB, new RgbColor(178, 76, 216));
            put(Material.PURPUR_STAIRS, new RgbColor(178, 76, 216));

            put(Material.HAY_BLOCK, new RgbColor(229, 229, 51));
            put(Material.SPONGE, new RgbColor(229, 229, 51));

            put(Material.MELON, new RgbColor(127, 204, 25));

            //put(Material.PRISMARINE, new RgbColor(76, 127, 153));

            put(Material.REPEATING_COMMAND_BLOCK, new RgbColor(127, 63, 178));
            put(Material.MYCELIUM, new RgbColor(127, 63, 178));

            put(Material.COMMAND_BLOCK, new RgbColor(102, 76, 51));
            put(Material.SOUL_SAND, new RgbColor(102, 76, 51));
            put(Material.DARK_OAK_STAIRS, new RgbColor(102, 76, 51));

            put(Material.BRICK, new RgbColor(153, 51, 51));
            put(Material.BRICK_STAIRS, new RgbColor(153, 51, 51));
            put(Material.NETHER_WART_BLOCK, new RgbColor(153, 51, 51));

            put(Material.COAL_BLOCK, new RgbColor(25, 25, 25));
            put(Material.OBSIDIAN, new RgbColor(25, 25, 25));

            put(Material.GOLD_BLOCK, new RgbColor(250, 238, 77));
            put(Material.LIGHT_WEIGHTED_PRESSURE_PLATE, new RgbColor(250, 238, 77));

            put(Material.DIAMOND_BLOCK, new RgbColor(92, 238, 77));

            put(Material.LAPIS_BLOCK, new RgbColor(74, 128, 255));

            put(Material.EMERALD_BLOCK, new RgbColor(0, 217, 58));

            put(Material.NETHERRACK, new RgbColor(112, 2, 0));
            put(Material.NETHER_BRICK_STAIRS, new RgbColor(112, 2, 0));
            put(Material.RED_NETHER_BRICKS, new RgbColor(112, 2, 0));
            put(Material.NETHER_QUARTZ_ORE, new RgbColor(112, 2, 0));
            put(Material.MAGMA_BLOCK, new RgbColor(112, 2, 0));

            put(Material.CAULDRON, new RgbColor(60, 60, 60));

            put(Material.SLIME_BLOCK, new RgbColor(147, 204, 134));

            put(Material.COBWEB, new RgbColor(251, 251, 251));

            put(Material.ANVIL, new RgbColor(63, 58, 58));

            put(Material.WHITE_WOOL, new RgbColor(255, 255, 255));
            put(Material.ORANGE_WOOL, new RgbColor(216, 127, 51));
            put(Material.MAGENTA_WOOL, new RgbColor(178, 76, 216));
            put(Material.LIGHT_BLUE_WOOL, new RgbColor(102, 153, 216));
            put(Material.YELLOW_WOOL, new RgbColor(229, 229, 51));
            put(Material.LIME_WOOL, new RgbColor(127, 204, 25));
            put(Material.PINK_WOOL, new RgbColor(242, 127, 165));
            put(Material.GRAY_WOOL, new RgbColor(76, 76, 76));
            put(Material.LIGHT_GRAY_WOOL, new RgbColor(155, 157, 181));
            put(Material.CYAN_WOOL, new RgbColor(76, 127, 153));
            put(Material.PURPLE_WOOL, new RgbColor(127, 63, 178));
            put(Material.BLUE_WOOL, new RgbColor(51, 76, 178));
            put(Material.BROWN_WOOL, new RgbColor(102, 76, 51));
            put(Material.GREEN_WOOL, new RgbColor(55, 77, 36));
            put(Material.RED_WOOL, new RgbColor(153, 51, 51));
            put(Material.BLACK_WOOL, new RgbColor(25, 25, 25));

            put(Material.WHITE_CARPET, new RgbColor(255, 255, 255));
            put(Material.ORANGE_CARPET, new RgbColor(216, 127, 51));
            put(Material.MAGENTA_CARPET, new RgbColor(178, 76, 216));
            put(Material.LIGHT_BLUE_CARPET, new RgbColor(102, 153, 216));
            put(Material.YELLOW_CARPET, new RgbColor(229, 229, 51));
            put(Material.LIME_CARPET, new RgbColor(127, 204, 25));
            put(Material.PINK_CARPET, new RgbColor(242, 127, 165));
            put(Material.GRAY_CARPET, new RgbColor(76, 76, 76));
            put(Material.LIGHT_GRAY_CARPET, new RgbColor(155, 157, 181));
            put(Material.CYAN_CARPET, new RgbColor(76, 127, 153));
            put(Material.PURPLE_CARPET, new RgbColor(127, 63, 178));
            put(Material.BLUE_CARPET, new RgbColor(51, 76, 178));
            put(Material.BROWN_CARPET, new RgbColor(102, 76, 51));
            put(Material.GREEN_CARPET, new RgbColor(55, 77, 36));
            put(Material.RED_CARPET, new RgbColor(153, 51, 51));
            put(Material.BLACK_CARPET, new RgbColor(25, 25, 25));

            put(Material.WHITE_TERRACOTTA, new RgbColor(189, 176, 172));
            put(Material.ORANGE_TERRACOTTA, new RgbColor(216, 127, 51));
            put(Material.MAGENTA_TERRACOTTA, new RgbColor(178, 76, 216));
            put(Material.LIGHT_BLUE_TERRACOTTA, new RgbColor(102, 153, 216));
            put(Material.YELLOW_TERRACOTTA, new RgbColor(229, 229, 51));
            put(Material.LIME_TERRACOTTA, new RgbColor(127, 204, 25));
            put(Material.PINK_TERRACOTTA, new RgbColor(242, 127, 165));
            put(Material.GRAY_TERRACOTTA, new RgbColor(76, 76, 76));
            put(Material.LIGHT_GRAY_TERRACOTTA, new RgbColor(122, 99, 90));
            put(Material.CYAN_TERRACOTTA, new RgbColor(76, 127, 153));
            put(Material.PURPLE_TERRACOTTA, new RgbColor(127, 63, 178));
            put(Material.BLUE_TERRACOTTA, new RgbColor(127, 63, 178));
            put(Material.BROWN_TERRACOTTA, new RgbColor(102, 76, 51));
            put(Material.GREEN_TERRACOTTA, new RgbColor(82, 89, 45));
            put(Material.RED_TERRACOTTA, new RgbColor(153, 51, 51));
            put(Material.BLACK_TERRACOTTA, new RgbColor(25, 25, 25));

            put(Material.STONE_SLAB, new RgbColor(167, 167, 167));
            put(Material.SMOOTH_STONE_SLAB, new RgbColor(167, 167, 167));
            put(Material.SANDSTONE_SLAB, new RgbColor(247, 233, 163));
            put(Material.COBBLESTONE_SLAB, new RgbColor(112, 112, 112));
            put(Material.BRICK_SLAB, new RgbColor(153, 51, 51));
            put(Material.STONE_BRICK_SLAB, new RgbColor(112, 112, 112));
            put(Material.NETHER_BRICK_SLAB, new RgbColor(112, 2, 0));
            put(Material.QUARTZ_SLAB, new RgbColor(255, 252, 245));

            put(Material.SMOOTH_STONE, new RgbColor(167, 167, 167));
            put(Material.SMOOTH_SANDSTONE, new RgbColor(247, 233, 163));
            put(Material.COBBLESTONE, new RgbColor(112, 112, 112));
            put(Material.BRICK, new RgbColor(153, 51, 51));
            put(Material.STONE_BRICKS, new RgbColor(112, 112, 112));
            put(Material.NETHER_BRICK, new RgbColor(112, 2, 0));
            put(Material.NETHER_BRICKS, new RgbColor(112, 2, 0));
            put(Material.SMOOTH_QUARTZ, new RgbColor(255, 252, 245));

            put(Material.OAK_SLAB, new RgbColor(143, 119, 72));
            put(Material.SPRUCE_SLAB, new RgbColor(102, 76, 51));
            put(Material.BIRCH_SLAB, new RgbColor(247, 233, 163));
            put(Material.JUNGLE_SLAB, new RgbColor(151, 109, 77));
            put(Material.ACACIA_SLAB, new RgbColor(216, 127, 51));
            put(Material.DARK_OAK_SLAB, new RgbColor(102, 76, 51));

            put(Material.OAK_PLANKS, new RgbColor(143, 119, 72));
            put(Material.SPRUCE_PLANKS, new RgbColor(102, 76, 51));
            put(Material.BIRCH_PLANKS, new RgbColor(247, 233, 163));
            put(Material.JUNGLE_PLANKS, new RgbColor(151, 109, 77));
            put(Material.ACACIA_PLANKS, new RgbColor(216, 127, 51));
            put(Material.DARK_OAK_PLANKS, new RgbColor(102, 76, 51));

            put(Material.OAK_WOOD, new RgbColor(143, 119, 72));
            put(Material.SPRUCE_WOOD, new RgbColor(102, 76, 51));
            put(Material.BIRCH_WOOD, new RgbColor(247, 233, 163));
            put(Material.JUNGLE_WOOD, new RgbColor(151, 109, 77));
            put(Material.ACACIA_WOOD, new RgbColor(216, 127, 51));
            put(Material.DARK_OAK_WOOD, new RgbColor(102, 76, 51));

            put(Material.OAK_LOG, new RgbColor(143, 119, 72));
            put(Material.SPRUCE_LOG, new RgbColor(102, 76, 51));
            put(Material.BIRCH_LOG, new RgbColor(247, 233, 163));
            put(Material.JUNGLE_LOG, new RgbColor(151, 109, 77));
            put(Material.ACACIA_LOG, new RgbColor(216, 127, 51));
            put(Material.DARK_OAK_LOG, new RgbColor(102, 76, 51));

            put(Material.SAND, new RgbColor(247, 233, 163));
            put(Material.RED_SAND, new RgbColor(216, 127, 51));

            put(Material.PRISMARINE, new RgbColor(76, 127, 153));
            put(Material.PRISMARINE_BRICKS, new RgbColor(92, 219, 213));
            put(Material.DARK_PRISMARINE, new RgbColor(92, 219, 213));
        }
    };

    public static RgbColor getBlockColor(Material type) {
        return getBlockColor(type, (short) 0);
    }

    public static RgbColor getBlockColor(Material type, short damage) {
        Material[] ignore_list = new Material[]{
                Material.AIR,
                Material.BARRIER,
                Material.DAYLIGHT_DETECTOR,
                Material.IRON_BARS,
                Material.GLASS,
                Material.GLASS_PANE,
                Material.TALL_GRASS,
                Material.DANDELION,
                Material.PISTON,
                Material.PISTON_HEAD,
                Material.MOVING_PISTON,
                Material.STICKY_PISTON,
                Material.LEGACY_RED_ROSE,
                Material.LEGACY_STANDING_BANNER,
                Material.LEGACY_WALL_BANNER,
                Material.LEGACY_WALL_SIGN,
                Material.LEGACY_SIGN_POST,
                Material.LEGACY_STAINED_GLASS,
                Material.LEGACY_STAINED_GLASS_PANE,
                Material.LEGACY_DOUBLE_PLANT,
                Material.TRIPWIRE,
                Material.RED_MUSHROOM,
                Material.TRIPWIRE,
                Material.VINE,
                Material.RAIL,
                Material.ACTIVATOR_RAIL,
                Material.DETECTOR_RAIL,
                Material.POWERED_RAIL,
                Material.STONE_PRESSURE_PLATE,
                Material.LADDER,
                Material.STONE_BUTTON,
                Material.LEGACY_TRAP_DOOR,
                Material.CHAIN_COMMAND_BLOCK,
                Material.FIRE,
                Material.LEGACY_WOOD_BUTTON,
                Material.SUGAR_CANE,
                Material.END_ROD,
                Material.FLOWER_POT,
                Material.IRON_TRAPDOOR,
                Material.LEGACY_WOOD_PLATE,
                Material.TRIPWIRE_HOOK,
                Material.REDSTONE_TORCH,
                Material.JUNGLE_DOOR,
                Material.LEVER,
                Material.LEGACY_SKULL,
                Material.POTATO,
                Material.WHEAT,
                Material.CARROT,
                Material.END_PORTAL_FRAME
        };

        if (Arrays.asList(ignore_list).contains(type)) return null;
        if (color_map.containsKey(type)) {
            return color_map.get(type);
        } else {
            Bukkit.getLogger().info("Block Data Not Found: " + type.toString());
            return null;
        }
    }
}

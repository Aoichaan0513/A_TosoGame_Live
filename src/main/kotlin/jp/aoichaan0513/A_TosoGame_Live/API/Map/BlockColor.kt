package jp.aoichaan0513.A_TosoGame_Live.API.Map

import org.bukkit.Bukkit
import org.bukkit.Material
import java.util.*

class BlockColor {
    companion object {

        var color_map: HashMap<Material, RgbColor> = object : HashMap<Material, RgbColor>() {
            init {
                put(Material.OAK_FENCE, RgbColor(143, 119, 72))
                put(Material.SPRUCE_FENCE, RgbColor(102, 76, 51))
                put(Material.BIRCH_FENCE, RgbColor(247, 233, 163))
                put(Material.JUNGLE_FENCE, RgbColor(151, 109, 77))
                put(Material.ACACIA_FENCE, RgbColor(216, 127, 51))
                put(Material.DARK_OAK_FENCE, RgbColor(102, 76, 51))
                put(Material.NETHER_BRICK_FENCE, RgbColor(247, 233, 163))
                put(Material.TERRACOTTA, RgbColor(184, 116, 86))
                put(Material.OAK_FENCE_GATE, RgbColor(143, 119, 72))
                put(Material.SPRUCE_FENCE_GATE, RgbColor(102, 76, 51))
                put(Material.BIRCH_FENCE_GATE, RgbColor(247, 233, 163))
                put(Material.JUNGLE_FENCE_GATE, RgbColor(151, 109, 77))
                put(Material.ACACIA_FENCE_GATE, RgbColor(216, 127, 51))
                put(Material.DARK_OAK_FENCE_GATE, RgbColor(102, 76, 51))
                put(Material.CHEST, RgbColor(184, 128, 44))
                put(Material.TRAPPED_CHEST, RgbColor(184, 128, 44))
                put(Material.ENDER_CHEST, RgbColor(57, 81, 83))
                put(Material.BEACON, RgbColor(165, 241, 241))
                put(Material.STONE, RgbColor(153, 153, 153))
                put(Material.STONE_BRICKS, RgbColor(112, 112, 112))
                put(Material.STONE_BRICK_STAIRS, RgbColor(112, 112, 112))
                put(Material.REDSTONE_LAMP, RgbColor(136, 92, 64))
                put(Material.GRASS, RgbColor(127, 178, 56))
                put(Material.GRASS_BLOCK, RgbColor(127, 178, 56))

                //put(Material.SAND, new RgbColor(247, 233, 163));
                put(Material.SANDSTONE, RgbColor(247, 233, 163))
                //put(Material.WOOD, new RgbColor(247, 233, 163));
                put(Material.BROWN_MUSHROOM, RgbColor(247, 233, 163))
                put(Material.BONE_BLOCK, RgbColor(247, 233, 163))
                put(Material.GLOWSTONE, RgbColor(247, 233, 163))
                put(Material.END_STONE, RgbColor(247, 233, 163))
                put(Material.OAK_STAIRS, RgbColor(143, 119, 72))
                put(Material.BIRCH_STAIRS, RgbColor(247, 233, 163))
                put(Material.JUNGLE_STAIRS, RgbColor(151, 109, 77))
                put(Material.TNT, RgbColor(255, 0, 0))
                put(Material.REDSTONE_BLOCK, RgbColor(255, 0, 0))
                put(Material.LAVA, RgbColor(255, 0, 0))
                put(Material.ICE, RgbColor(160, 160, 255))
                put(Material.FROSTED_ICE, RgbColor(160, 160, 255))
                put(Material.PACKED_ICE, RgbColor(160, 160, 255))
                put(Material.IRON_BLOCK, RgbColor(167, 167, 167))
                put(Material.HEAVY_WEIGHTED_PRESSURE_PLATE, RgbColor(167, 167, 167))
                put(Material.OAK_LEAVES, RgbColor(0, 124, 0))
                put(Material.SPRUCE_LEAVES, RgbColor(0, 124, 0))
                put(Material.BIRCH_LEAVES, RgbColor(0, 124, 0))
                put(Material.JUNGLE_LEAVES, RgbColor(0, 124, 0))
                put(Material.ACACIA_LEAVES, RgbColor(0, 124, 0))
                put(Material.DARK_OAK_LEAVES, RgbColor(0, 124, 0))

                //put(Material.WOOL, new RgbColor(0, 0, 0));
                put(Material.SNOW_BLOCK, RgbColor(0, 0, 0))
                put(Material.CLAY, RgbColor(164, 168, 184))
                put(Material.BROWN_MUSHROOM, RgbColor(151, 109, 77))
                put(Material.DIRT, RgbColor(151, 109, 77))
                put(Material.FARMLAND, RgbColor(151, 109, 77))
                put(Material.JUKEBOX, RgbColor(151, 109, 77))
                put(Material.GRASS_PATH, RgbColor(151, 109, 77))
                put(Material.MOSSY_COBBLESTONE, RgbColor(112, 112, 112))
                put(Material.COBBLESTONE_WALL, RgbColor(112, 112, 112))
                put(Material.COBBLESTONE_STAIRS, RgbColor(112, 112, 112))
                put(Material.BRICK, RgbColor(112, 112, 112))
                put(Material.BRICK_STAIRS, RgbColor(112, 112, 112))
                put(Material.GRAVEL, RgbColor(112, 112, 112))
                put(Material.COAL_ORE, RgbColor(112, 112, 112))
                put(Material.IRON_ORE, RgbColor(112, 112, 112))
                put(Material.GOLD_ORE, RgbColor(112, 112, 112))
                put(Material.REDSTONE_ORE, RgbColor(112, 112, 112))
                put(Material.LAPIS_ORE, RgbColor(112, 112, 112))
                put(Material.DIAMOND_ORE, RgbColor(112, 112, 112))
                put(Material.EMERALD_ORE, RgbColor(112, 112, 112))
                put(Material.BEDROCK, RgbColor(112, 112, 112))
                put(Material.FURNACE, RgbColor(112, 112, 112))
                put(Material.DISPENSER, RgbColor(112, 112, 112))
                put(Material.DROPPER, RgbColor(112, 112, 112))
                put(Material.HOPPER, RgbColor(112, 112, 112))
                put(Material.OBSERVER, RgbColor(112, 112, 112))
                put(Material.WATER, RgbColor(64, 64, 255))
                put(Material.CRAFTING_TABLE, RgbColor(143, 119, 72))
                put(Material.BOOKSHELF, RgbColor(143, 119, 72))
                put(Material.NOTE_BLOCK, RgbColor(143, 119, 72))
                put(Material.QUARTZ_BLOCK, RgbColor(255, 252, 245))
                put(Material.QUARTZ_STAIRS, RgbColor(255, 252, 245))
                put(Material.SEA_LANTERN, RgbColor(255, 252, 245))
                put(Material.ACACIA_STAIRS, RgbColor(216, 127, 51))
                put(Material.PUMPKIN, RgbColor(216, 127, 51))
                put(Material.JACK_O_LANTERN, RgbColor(216, 127, 51))
                put(Material.RED_SANDSTONE, RgbColor(216, 127, 51))
                put(Material.RED_SANDSTONE_STAIRS, RgbColor(216, 127, 51))
                put(Material.RED_SANDSTONE_SLAB, RgbColor(216, 127, 51))
                put(Material.PURPUR_BLOCK, RgbColor(178, 76, 216))
                put(Material.PURPUR_PILLAR, RgbColor(178, 76, 216))
                put(Material.PURPUR_SLAB, RgbColor(178, 76, 216))
                put(Material.PURPUR_STAIRS, RgbColor(178, 76, 216))
                put(Material.HAY_BLOCK, RgbColor(229, 229, 51))
                put(Material.SPONGE, RgbColor(229, 229, 51))
                put(Material.MELON, RgbColor(127, 204, 25))

                //put(Material.PRISMARINE, new RgbColor(76, 127, 153));
                put(Material.REPEATING_COMMAND_BLOCK, RgbColor(127, 63, 178))
                put(Material.MYCELIUM, RgbColor(127, 63, 178))
                put(Material.COMMAND_BLOCK, RgbColor(102, 76, 51))
                put(Material.SOUL_SAND, RgbColor(102, 76, 51))
                put(Material.DARK_OAK_STAIRS, RgbColor(102, 76, 51))
                put(Material.BRICK, RgbColor(153, 51, 51))
                put(Material.BRICK_STAIRS, RgbColor(153, 51, 51))
                put(Material.NETHER_WART_BLOCK, RgbColor(153, 51, 51))
                put(Material.COAL_BLOCK, RgbColor(25, 25, 25))
                put(Material.OBSIDIAN, RgbColor(25, 25, 25))
                put(Material.GOLD_BLOCK, RgbColor(250, 238, 77))
                put(Material.LIGHT_WEIGHTED_PRESSURE_PLATE, RgbColor(250, 238, 77))
                put(Material.DIAMOND_BLOCK, RgbColor(92, 238, 77))
                put(Material.LAPIS_BLOCK, RgbColor(74, 128, 255))
                put(Material.EMERALD_BLOCK, RgbColor(0, 217, 58))
                put(Material.NETHERRACK, RgbColor(112, 2, 0))
                put(Material.NETHER_BRICK_STAIRS, RgbColor(112, 2, 0))
                put(Material.RED_NETHER_BRICKS, RgbColor(112, 2, 0))
                put(Material.NETHER_QUARTZ_ORE, RgbColor(112, 2, 0))
                put(Material.MAGMA_BLOCK, RgbColor(112, 2, 0))
                put(Material.CAULDRON, RgbColor(60, 60, 60))
                put(Material.SLIME_BLOCK, RgbColor(147, 204, 134))
                put(Material.COBWEB, RgbColor(251, 251, 251))
                put(Material.ANVIL, RgbColor(63, 58, 58))
                put(Material.WHITE_WOOL, RgbColor(255, 255, 255))
                put(Material.ORANGE_WOOL, RgbColor(216, 127, 51))
                put(Material.MAGENTA_WOOL, RgbColor(178, 76, 216))
                put(Material.LIGHT_BLUE_WOOL, RgbColor(102, 153, 216))
                put(Material.YELLOW_WOOL, RgbColor(229, 229, 51))
                put(Material.LIME_WOOL, RgbColor(127, 204, 25))
                put(Material.PINK_WOOL, RgbColor(242, 127, 165))
                put(Material.GRAY_WOOL, RgbColor(76, 76, 76))
                put(Material.LIGHT_GRAY_WOOL, RgbColor(155, 157, 181))
                put(Material.CYAN_WOOL, RgbColor(76, 127, 153))
                put(Material.PURPLE_WOOL, RgbColor(127, 63, 178))
                put(Material.BLUE_WOOL, RgbColor(51, 76, 178))
                put(Material.BROWN_WOOL, RgbColor(102, 76, 51))
                put(Material.GREEN_WOOL, RgbColor(55, 77, 36))
                put(Material.RED_WOOL, RgbColor(153, 51, 51))
                put(Material.BLACK_WOOL, RgbColor(25, 25, 25))
                put(Material.WHITE_CARPET, RgbColor(255, 255, 255))
                put(Material.ORANGE_CARPET, RgbColor(216, 127, 51))
                put(Material.MAGENTA_CARPET, RgbColor(178, 76, 216))
                put(Material.LIGHT_BLUE_CARPET, RgbColor(102, 153, 216))
                put(Material.YELLOW_CARPET, RgbColor(229, 229, 51))
                put(Material.LIME_CARPET, RgbColor(127, 204, 25))
                put(Material.PINK_CARPET, RgbColor(242, 127, 165))
                put(Material.GRAY_CARPET, RgbColor(76, 76, 76))
                put(Material.LIGHT_GRAY_CARPET, RgbColor(155, 157, 181))
                put(Material.CYAN_CARPET, RgbColor(76, 127, 153))
                put(Material.PURPLE_CARPET, RgbColor(127, 63, 178))
                put(Material.BLUE_CARPET, RgbColor(51, 76, 178))
                put(Material.BROWN_CARPET, RgbColor(102, 76, 51))
                put(Material.GREEN_CARPET, RgbColor(55, 77, 36))
                put(Material.RED_CARPET, RgbColor(153, 51, 51))
                put(Material.BLACK_CARPET, RgbColor(25, 25, 25))
                put(Material.WHITE_TERRACOTTA, RgbColor(189, 176, 172))
                put(Material.ORANGE_TERRACOTTA, RgbColor(216, 127, 51))
                put(Material.MAGENTA_TERRACOTTA, RgbColor(178, 76, 216))
                put(Material.LIGHT_BLUE_TERRACOTTA, RgbColor(102, 153, 216))
                put(Material.YELLOW_TERRACOTTA, RgbColor(229, 229, 51))
                put(Material.LIME_TERRACOTTA, RgbColor(127, 204, 25))
                put(Material.PINK_TERRACOTTA, RgbColor(242, 127, 165))
                put(Material.GRAY_TERRACOTTA, RgbColor(76, 76, 76))
                put(Material.LIGHT_GRAY_TERRACOTTA, RgbColor(122, 99, 90))
                put(Material.CYAN_TERRACOTTA, RgbColor(76, 127, 153))
                put(Material.PURPLE_TERRACOTTA, RgbColor(127, 63, 178))
                put(Material.BLUE_TERRACOTTA, RgbColor(127, 63, 178))
                put(Material.BROWN_TERRACOTTA, RgbColor(102, 76, 51))
                put(Material.GREEN_TERRACOTTA, RgbColor(82, 89, 45))
                put(Material.RED_TERRACOTTA, RgbColor(153, 51, 51))
                put(Material.BLACK_TERRACOTTA, RgbColor(25, 25, 25))
                put(Material.STONE_SLAB, RgbColor(167, 167, 167))
                put(Material.SMOOTH_STONE_SLAB, RgbColor(167, 167, 167))
                put(Material.SANDSTONE_SLAB, RgbColor(247, 233, 163))
                put(Material.COBBLESTONE_SLAB, RgbColor(112, 112, 112))
                put(Material.BRICK_SLAB, RgbColor(153, 51, 51))
                put(Material.STONE_BRICK_SLAB, RgbColor(112, 112, 112))
                put(Material.NETHER_BRICK_SLAB, RgbColor(112, 2, 0))
                put(Material.QUARTZ_SLAB, RgbColor(255, 252, 245))
                put(Material.SMOOTH_STONE, RgbColor(167, 167, 167))
                put(Material.SMOOTH_SANDSTONE, RgbColor(247, 233, 163))
                put(Material.COBBLESTONE, RgbColor(112, 112, 112))
                put(Material.BRICK, RgbColor(153, 51, 51))
                put(Material.STONE_BRICKS, RgbColor(112, 112, 112))
                put(Material.NETHER_BRICK, RgbColor(112, 2, 0))
                put(Material.NETHER_BRICKS, RgbColor(112, 2, 0))
                put(Material.SMOOTH_QUARTZ, RgbColor(255, 252, 245))
                put(Material.OAK_SLAB, RgbColor(143, 119, 72))
                put(Material.SPRUCE_SLAB, RgbColor(102, 76, 51))
                put(Material.BIRCH_SLAB, RgbColor(247, 233, 163))
                put(Material.JUNGLE_SLAB, RgbColor(151, 109, 77))
                put(Material.ACACIA_SLAB, RgbColor(216, 127, 51))
                put(Material.DARK_OAK_SLAB, RgbColor(102, 76, 51))
                put(Material.OAK_PLANKS, RgbColor(143, 119, 72))
                put(Material.SPRUCE_PLANKS, RgbColor(102, 76, 51))
                put(Material.BIRCH_PLANKS, RgbColor(247, 233, 163))
                put(Material.JUNGLE_PLANKS, RgbColor(151, 109, 77))
                put(Material.ACACIA_PLANKS, RgbColor(216, 127, 51))
                put(Material.DARK_OAK_PLANKS, RgbColor(102, 76, 51))
                put(Material.OAK_WOOD, RgbColor(143, 119, 72))
                put(Material.SPRUCE_WOOD, RgbColor(102, 76, 51))
                put(Material.BIRCH_WOOD, RgbColor(247, 233, 163))
                put(Material.JUNGLE_WOOD, RgbColor(151, 109, 77))
                put(Material.ACACIA_WOOD, RgbColor(216, 127, 51))
                put(Material.DARK_OAK_WOOD, RgbColor(102, 76, 51))
                put(Material.OAK_LOG, RgbColor(143, 119, 72))
                put(Material.SPRUCE_LOG, RgbColor(102, 76, 51))
                put(Material.BIRCH_LOG, RgbColor(247, 233, 163))
                put(Material.JUNGLE_LOG, RgbColor(151, 109, 77))
                put(Material.ACACIA_LOG, RgbColor(216, 127, 51))
                put(Material.DARK_OAK_LOG, RgbColor(102, 76, 51))
                put(Material.SAND, RgbColor(247, 233, 163))
                put(Material.RED_SAND, RgbColor(216, 127, 51))
                put(Material.PRISMARINE, RgbColor(76, 127, 153))
                put(Material.PRISMARINE_BRICKS, RgbColor(92, 219, 213))
                put(Material.DARK_PRISMARINE, RgbColor(92, 219, 213))
            }
        }

        fun getBlockColor(type: Material): RgbColor? {
            val ignore_list = arrayOf(
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
            )
            if (Arrays.asList(*ignore_list).contains(type)) return null
            return if (color_map.containsKey(type)) {
                color_map[type]
            } else {
                Bukkit.getLogger().info("Block Data Not Found: $type")
                null
            }
        }
    }
}
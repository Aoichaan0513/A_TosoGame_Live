package jp.aoichaan0513.A_TosoGame_Live.API.Map

import jp.aoichaan0513.A_TosoGame_Live.Utils.DataClass
import org.bukkit.Material

class BlockColor {
    companion object {

        val colorMap = mapOf(
                Material.OAK_FENCE to DataClass.RGBColor(143, 119, 72),
                Material.SPRUCE_FENCE to DataClass.RGBColor(102, 76, 51),
                Material.BIRCH_FENCE to DataClass.RGBColor(247, 233, 163),
                Material.JUNGLE_FENCE to DataClass.RGBColor(151, 109, 77),
                Material.ACACIA_FENCE to DataClass.RGBColor(216, 127, 51),
                Material.DARK_OAK_FENCE to DataClass.RGBColor(102, 76, 51),
                Material.NETHER_BRICK_FENCE to DataClass.RGBColor(247, 233, 163),
                Material.TERRACOTTA to DataClass.RGBColor(184, 116, 86),
                Material.OAK_FENCE_GATE to DataClass.RGBColor(143, 119, 72),
                Material.SPRUCE_FENCE_GATE to DataClass.RGBColor(102, 76, 51),
                Material.BIRCH_FENCE_GATE to DataClass.RGBColor(247, 233, 163),
                Material.JUNGLE_FENCE_GATE to DataClass.RGBColor(151, 109, 77),
                Material.ACACIA_FENCE_GATE to DataClass.RGBColor(216, 127, 51),
                Material.DARK_OAK_FENCE_GATE to DataClass.RGBColor(102, 76, 51),
                Material.CHEST to DataClass.RGBColor(184, 128, 44),
                Material.TRAPPED_CHEST to DataClass.RGBColor(184, 128, 44),
                Material.ENDER_CHEST to DataClass.RGBColor(57, 81, 83),
                Material.BEACON to DataClass.RGBColor(165, 241, 241),
                Material.STONE to DataClass.RGBColor(153, 153, 153),
                Material.STONE_BRICKS to DataClass.RGBColor(112, 112, 112),
                Material.STONE_BRICK_STAIRS to DataClass.RGBColor(112, 112, 112),
                Material.REDSTONE_LAMP to DataClass.RGBColor(136, 92, 64),
                Material.GRASS to DataClass.RGBColor(127, 178, 56),
                Material.GRASS_BLOCK to DataClass.RGBColor(127, 178, 56),
                Material.SANDSTONE to DataClass.RGBColor(247, 233, 163),
                Material.BROWN_MUSHROOM to DataClass.RGBColor(247, 233, 163),
                Material.BONE_BLOCK to DataClass.RGBColor(247, 233, 163),
                Material.GLOWSTONE to DataClass.RGBColor(247, 233, 163),
                Material.END_STONE to DataClass.RGBColor(247, 233, 163),
                Material.OAK_STAIRS to DataClass.RGBColor(143, 119, 72),
                Material.BIRCH_STAIRS to DataClass.RGBColor(247, 233, 163),
                Material.JUNGLE_STAIRS to DataClass.RGBColor(151, 109, 77),
                Material.TNT to DataClass.RGBColor(255, 0, 0),
                Material.REDSTONE_BLOCK to DataClass.RGBColor(255, 0, 0),
                Material.LAVA to DataClass.RGBColor(255, 0, 0),
                Material.ICE to DataClass.RGBColor(160, 160, 255),
                Material.FROSTED_ICE to DataClass.RGBColor(160, 160, 255),
                Material.PACKED_ICE to DataClass.RGBColor(160, 160, 255),
                Material.IRON_BLOCK to DataClass.RGBColor(167, 167, 167),
                Material.HEAVY_WEIGHTED_PRESSURE_PLATE to DataClass.RGBColor(167, 167, 167),
                Material.OAK_LEAVES to DataClass.RGBColor(0, 124, 0),
                Material.SPRUCE_LEAVES to DataClass.RGBColor(0, 124, 0),
                Material.BIRCH_LEAVES to DataClass.RGBColor(0, 124, 0),
                Material.JUNGLE_LEAVES to DataClass.RGBColor(0, 124, 0),
                Material.ACACIA_LEAVES to DataClass.RGBColor(0, 124, 0),
                Material.DARK_OAK_LEAVES to DataClass.RGBColor(0, 124, 0),
                Material.SNOW_BLOCK to DataClass.RGBColor(0, 0, 0),
                Material.CLAY to DataClass.RGBColor(164, 168, 184),
                Material.BROWN_MUSHROOM to DataClass.RGBColor(151, 109, 77),
                Material.DIRT to DataClass.RGBColor(151, 109, 77),
                Material.FARMLAND to DataClass.RGBColor(151, 109, 77),
                Material.JUKEBOX to DataClass.RGBColor(151, 109, 77),
                Material.GRASS_PATH to DataClass.RGBColor(151, 109, 77),
                Material.MOSSY_COBBLESTONE to DataClass.RGBColor(112, 112, 112),
                Material.COBBLESTONE_WALL to DataClass.RGBColor(112, 112, 112),
                Material.COBBLESTONE_STAIRS to DataClass.RGBColor(112, 112, 112),
                Material.BRICK to DataClass.RGBColor(112, 112, 112),
                Material.BRICK_STAIRS to DataClass.RGBColor(112, 112, 112),
                Material.GRAVEL to DataClass.RGBColor(112, 112, 112),
                Material.COAL_ORE to DataClass.RGBColor(112, 112, 112),
                Material.IRON_ORE to DataClass.RGBColor(112, 112, 112),
                Material.GOLD_ORE to DataClass.RGBColor(112, 112, 112),
                Material.REDSTONE_ORE to DataClass.RGBColor(112, 112, 112),
                Material.LAPIS_ORE to DataClass.RGBColor(112, 112, 112),
                Material.DIAMOND_ORE to DataClass.RGBColor(112, 112, 112),
                Material.EMERALD_ORE to DataClass.RGBColor(112, 112, 112),
                Material.BEDROCK to DataClass.RGBColor(112, 112, 112),
                Material.FURNACE to DataClass.RGBColor(112, 112, 112),
                Material.DISPENSER to DataClass.RGBColor(112, 112, 112),
                Material.DROPPER to DataClass.RGBColor(112, 112, 112),
                Material.HOPPER to DataClass.RGBColor(112, 112, 112),
                Material.OBSERVER to DataClass.RGBColor(112, 112, 112),
                Material.WATER to DataClass.RGBColor(64, 64, 255),
                Material.CRAFTING_TABLE to DataClass.RGBColor(143, 119, 72),
                Material.BOOKSHELF to DataClass.RGBColor(143, 119, 72),
                Material.NOTE_BLOCK to DataClass.RGBColor(143, 119, 72),
                Material.QUARTZ_BLOCK to DataClass.RGBColor(255, 252, 245),
                Material.QUARTZ_STAIRS to DataClass.RGBColor(255, 252, 245),
                Material.SEA_LANTERN to DataClass.RGBColor(255, 252, 245),
                Material.ACACIA_STAIRS to DataClass.RGBColor(216, 127, 51),
                Material.PUMPKIN to DataClass.RGBColor(216, 127, 51),
                Material.JACK_O_LANTERN to DataClass.RGBColor(216, 127, 51),
                Material.RED_SANDSTONE to DataClass.RGBColor(216, 127, 51),
                Material.RED_SANDSTONE_STAIRS to DataClass.RGBColor(216, 127, 51),
                Material.RED_SANDSTONE_SLAB to DataClass.RGBColor(216, 127, 51),
                Material.PURPUR_BLOCK to DataClass.RGBColor(178, 76, 216),
                Material.PURPUR_PILLAR to DataClass.RGBColor(178, 76, 216),
                Material.PURPUR_SLAB to DataClass.RGBColor(178, 76, 216),
                Material.PURPUR_STAIRS to DataClass.RGBColor(178, 76, 216),
                Material.HAY_BLOCK to DataClass.RGBColor(229, 229, 51),
                Material.SPONGE to DataClass.RGBColor(229, 229, 51),
                Material.MELON to DataClass.RGBColor(127, 204, 25),
                Material.REPEATING_COMMAND_BLOCK to DataClass.RGBColor(127, 63, 178),
                Material.MYCELIUM to DataClass.RGBColor(127, 63, 178),
                Material.COMMAND_BLOCK to DataClass.RGBColor(102, 76, 51),
                Material.SOUL_SAND to DataClass.RGBColor(102, 76, 51),
                Material.DARK_OAK_STAIRS to DataClass.RGBColor(102, 76, 51),
                Material.BRICK to DataClass.RGBColor(153, 51, 51),
                Material.BRICK_STAIRS to DataClass.RGBColor(153, 51, 51),
                Material.NETHER_WART_BLOCK to DataClass.RGBColor(153, 51, 51),
                Material.COAL_BLOCK to DataClass.RGBColor(25, 25, 25),
                Material.OBSIDIAN to DataClass.RGBColor(25, 25, 25),
                Material.GOLD_BLOCK to DataClass.RGBColor(250, 238, 77),
                Material.LIGHT_WEIGHTED_PRESSURE_PLATE to DataClass.RGBColor(250, 238, 77),
                Material.DIAMOND_BLOCK to DataClass.RGBColor(92, 238, 77),
                Material.LAPIS_BLOCK to DataClass.RGBColor(74, 128, 255),
                Material.EMERALD_BLOCK to DataClass.RGBColor(0, 217, 58),
                Material.NETHERRACK to DataClass.RGBColor(112, 2, 0),
                Material.NETHER_BRICK_STAIRS to DataClass.RGBColor(112, 2, 0),
                Material.RED_NETHER_BRICKS to DataClass.RGBColor(112, 2, 0),
                Material.NETHER_QUARTZ_ORE to DataClass.RGBColor(112, 2, 0),
                Material.MAGMA_BLOCK to DataClass.RGBColor(112, 2, 0),
                Material.CAULDRON to DataClass.RGBColor(60, 60, 60),
                Material.SLIME_BLOCK to DataClass.RGBColor(147, 204, 134),
                Material.COBWEB to DataClass.RGBColor(251, 251, 251),
                Material.ANVIL to DataClass.RGBColor(63, 58, 58),
                Material.WHITE_WOOL to DataClass.RGBColor(255, 255, 255),
                Material.ORANGE_WOOL to DataClass.RGBColor(216, 127, 51),
                Material.MAGENTA_WOOL to DataClass.RGBColor(178, 76, 216),
                Material.LIGHT_BLUE_WOOL to DataClass.RGBColor(102, 153, 216),
                Material.YELLOW_WOOL to DataClass.RGBColor(229, 229, 51),
                Material.LIME_WOOL to DataClass.RGBColor(127, 204, 25),
                Material.PINK_WOOL to DataClass.RGBColor(242, 127, 165),
                Material.GRAY_WOOL to DataClass.RGBColor(76, 76, 76),
                Material.LIGHT_GRAY_WOOL to DataClass.RGBColor(155, 157, 181),
                Material.CYAN_WOOL to DataClass.RGBColor(76, 127, 153),
                Material.PURPLE_WOOL to DataClass.RGBColor(127, 63, 178),
                Material.BLUE_WOOL to DataClass.RGBColor(51, 76, 178),
                Material.BROWN_WOOL to DataClass.RGBColor(102, 76, 51),
                Material.GREEN_WOOL to DataClass.RGBColor(55, 77, 36),
                Material.RED_WOOL to DataClass.RGBColor(153, 51, 51),
                Material.BLACK_WOOL to DataClass.RGBColor(25, 25, 25),
                Material.WHITE_CARPET to DataClass.RGBColor(255, 255, 255),
                Material.ORANGE_CARPET to DataClass.RGBColor(216, 127, 51),
                Material.MAGENTA_CARPET to DataClass.RGBColor(178, 76, 216),
                Material.LIGHT_BLUE_CARPET to DataClass.RGBColor(102, 153, 216),
                Material.YELLOW_CARPET to DataClass.RGBColor(229, 229, 51),
                Material.LIME_CARPET to DataClass.RGBColor(127, 204, 25),
                Material.PINK_CARPET to DataClass.RGBColor(242, 127, 165),
                Material.GRAY_CARPET to DataClass.RGBColor(76, 76, 76),
                Material.LIGHT_GRAY_CARPET to DataClass.RGBColor(155, 157, 181),
                Material.CYAN_CARPET to DataClass.RGBColor(76, 127, 153),
                Material.PURPLE_CARPET to DataClass.RGBColor(127, 63, 178),
                Material.BLUE_CARPET to DataClass.RGBColor(51, 76, 178),
                Material.BROWN_CARPET to DataClass.RGBColor(102, 76, 51),
                Material.GREEN_CARPET to DataClass.RGBColor(55, 77, 36),
                Material.RED_CARPET to DataClass.RGBColor(153, 51, 51),
                Material.BLACK_CARPET to DataClass.RGBColor(25, 25, 25),
                Material.WHITE_TERRACOTTA to DataClass.RGBColor(189, 176, 172),
                Material.ORANGE_TERRACOTTA to DataClass.RGBColor(216, 127, 51),
                Material.MAGENTA_TERRACOTTA to DataClass.RGBColor(178, 76, 216),
                Material.LIGHT_BLUE_TERRACOTTA to DataClass.RGBColor(102, 153, 216),
                Material.YELLOW_TERRACOTTA to DataClass.RGBColor(229, 229, 51),
                Material.LIME_TERRACOTTA to DataClass.RGBColor(127, 204, 25),
                Material.PINK_TERRACOTTA to DataClass.RGBColor(242, 127, 165),
                Material.GRAY_TERRACOTTA to DataClass.RGBColor(76, 76, 76),
                Material.LIGHT_GRAY_TERRACOTTA to DataClass.RGBColor(122, 99, 90),
                Material.CYAN_TERRACOTTA to DataClass.RGBColor(76, 127, 153),
                Material.PURPLE_TERRACOTTA to DataClass.RGBColor(127, 63, 178),
                Material.BLUE_TERRACOTTA to DataClass.RGBColor(127, 63, 178),
                Material.BROWN_TERRACOTTA to DataClass.RGBColor(102, 76, 51),
                Material.GREEN_TERRACOTTA to DataClass.RGBColor(82, 89, 45),
                Material.RED_TERRACOTTA to DataClass.RGBColor(153, 51, 51),
                Material.BLACK_TERRACOTTA to DataClass.RGBColor(25, 25, 25),
                Material.STONE_SLAB to DataClass.RGBColor(167, 167, 167),
                Material.SMOOTH_STONE_SLAB to DataClass.RGBColor(167, 167, 167),
                Material.SANDSTONE_SLAB to DataClass.RGBColor(247, 233, 163),
                Material.COBBLESTONE_SLAB to DataClass.RGBColor(112, 112, 112),
                Material.BRICK_SLAB to DataClass.RGBColor(153, 51, 51),
                Material.STONE_BRICK_SLAB to DataClass.RGBColor(112, 112, 112),
                Material.NETHER_BRICK_SLAB to DataClass.RGBColor(112, 2, 0),
                Material.QUARTZ_SLAB to DataClass.RGBColor(255, 252, 245),
                Material.SMOOTH_STONE to DataClass.RGBColor(167, 167, 167),
                Material.SMOOTH_SANDSTONE to DataClass.RGBColor(247, 233, 163),
                Material.COBBLESTONE to DataClass.RGBColor(112, 112, 112),
                Material.BRICK to DataClass.RGBColor(153, 51, 51),
                Material.STONE_BRICKS to DataClass.RGBColor(112, 112, 112),
                Material.NETHER_BRICK to DataClass.RGBColor(112, 2, 0),
                Material.NETHER_BRICKS to DataClass.RGBColor(112, 2, 0),
                Material.SMOOTH_QUARTZ to DataClass.RGBColor(255, 252, 245),
                Material.OAK_SLAB to DataClass.RGBColor(143, 119, 72),
                Material.SPRUCE_SLAB to DataClass.RGBColor(102, 76, 51),
                Material.BIRCH_SLAB to DataClass.RGBColor(247, 233, 163),
                Material.JUNGLE_SLAB to DataClass.RGBColor(151, 109, 77),
                Material.ACACIA_SLAB to DataClass.RGBColor(216, 127, 51),
                Material.DARK_OAK_SLAB to DataClass.RGBColor(102, 76, 51),
                Material.OAK_PLANKS to DataClass.RGBColor(143, 119, 72),
                Material.SPRUCE_PLANKS to DataClass.RGBColor(102, 76, 51),
                Material.BIRCH_PLANKS to DataClass.RGBColor(247, 233, 163),
                Material.JUNGLE_PLANKS to DataClass.RGBColor(151, 109, 77),
                Material.ACACIA_PLANKS to DataClass.RGBColor(216, 127, 51),
                Material.DARK_OAK_PLANKS to DataClass.RGBColor(102, 76, 51),
                Material.OAK_WOOD to DataClass.RGBColor(143, 119, 72),
                Material.SPRUCE_WOOD to DataClass.RGBColor(102, 76, 51),
                Material.BIRCH_WOOD to DataClass.RGBColor(247, 233, 163),
                Material.JUNGLE_WOOD to DataClass.RGBColor(151, 109, 77),
                Material.ACACIA_WOOD to DataClass.RGBColor(216, 127, 51),
                Material.DARK_OAK_WOOD to DataClass.RGBColor(102, 76, 51),
                Material.OAK_LOG to DataClass.RGBColor(143, 119, 72),
                Material.SPRUCE_LOG to DataClass.RGBColor(102, 76, 51),
                Material.BIRCH_LOG to DataClass.RGBColor(247, 233, 163),
                Material.JUNGLE_LOG to DataClass.RGBColor(151, 109, 77),
                Material.ACACIA_LOG to DataClass.RGBColor(216, 127, 51),
                Material.DARK_OAK_LOG to DataClass.RGBColor(102, 76, 51),
                Material.SAND to DataClass.RGBColor(247, 233, 163),
                Material.RED_SAND to DataClass.RGBColor(216, 127, 51),
                Material.PRISMARINE to DataClass.RGBColor(76, 127, 153),
                Material.PRISMARINE_BRICKS to DataClass.RGBColor(92, 219, 213),
                Material.DARK_PRISMARINE to DataClass.RGBColor(92, 219, 213)
        )

        fun getBlockColor(type: Material): DataClass.RGBColor? {
            val ignoreSet = arrayOf(
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

            if (ignoreSet.contains(type)) return null
            return if (colorMap.containsKey(type)) {
                colorMap[type]
            } else {
                // Bukkit.getLogger().info("Block Data Not Found: $type")
                null
            }
        }
    }
}
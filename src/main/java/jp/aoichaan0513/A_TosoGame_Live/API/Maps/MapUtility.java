package jp.aoichaan0513.A_TosoGame_Live.API.Maps;

import jp.aoichaan0513.A_TosoGame_Live.API.Manager.World.WorldConfig;
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.World.WorldManager;
import jp.aoichaan0513.A_TosoGame_Live.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapUtility {

    private static ItemStack map = null;
    public static List<String> plotRem = new ArrayList<>();
    public static HashMap<String, PlotInfo> mapPlot = new HashMap<>();

    public static int resolution = 1;

    public static boolean generateMap() {
        ItemStack itemStack = MapUtility.getAreaMap();
        map = itemStack.clone();
        return map != null;
    }

    public static ItemStack getMap() {
        return map;
    }

    private static ItemStack getAreaMap() {
        World world = WorldManager.getWorld();

        WorldConfig worldConfig = Main.getWorldConfig();
        Location p1 = worldConfig.getMapBorderConfig().getLocation(WorldConfig.BorderType.POINT_1);
        Location p2 = worldConfig.getMapBorderConfig().getLocation(WorldConfig.BorderType.POINT_2);

        int max_height = world.getMaxHeight();
        final int minx = Math.min(p1.getBlockX(), p2.getBlockX());
        final int minz = Math.min(p1.getBlockZ(), p2.getBlockZ());
        final int maxx = Math.max(p1.getBlockX(), p2.getBlockX());
        final int maxz = Math.max(p1.getBlockZ(), p2.getBlockZ());

        HashMap<String, Tuple> block_type = new HashMap<>();

        for (int i = max_height - 1; i >= 0; i--) {
            for (int x = minx; x <= maxx; x++) {
                for (int z = minz; z <= maxz; z++) {
                    if (block_type.containsKey((maxx - x) + "_" + (maxz - z))) {
                        continue;
                    }
                    Location loc = new Location(world, x, i, z);
                    Block b = loc.getBlock();
                    if (BlockColor.getBlockColor(b.getType(), b.getData()) != null) {
                        block_type.put((maxx - x) + "_" + (maxz - z), new Tuple(b.getType(), b.getData()));
                    }
                }
            }
        }

        final int width = maxx - minx;
        final int height = maxz - minz;

        List<String> data = new ArrayList<>();
        for (String key : block_type.keySet()) {
            String[] d = key.split("_");
            int x = Integer.parseInt(d[0]);
            int y = Integer.parseInt(d[1]);
            Tuple t = block_type.get(key);
            RgbColor c = BlockColor.getBlockColor(t.getMaterial(), t.getByte());
            data.add(x + "," + y + "," + c.getRed() + "," + c.getGreen() + "," + c.getBlue());
        }

        BufferedImage map_img3 = ImageGenerator.getPaintedImage(width, height, data);
        BufferedImage map_img2 = ImageGenerator.getRotatedImage(180, map_img3);
        final BufferedImage map_img = ImageGenerator.getResizedImage(128, map_img2);
        File file = new File(world.getName() + Main.FILE_SEPARATOR + "map.png");
        try {
            ImageIO.write(map_img2, "png", file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        MapView mapView = Bukkit.createMap(world);
        mapView.setUnlimitedTracking(true);
        mapView.setCenterX(99999);
        mapView.setCenterZ(99999);
        mapView.setScale(MapView.Scale.FARTHEST);
        resolution = (maxx - minx) < (maxz - minz) ? (maxz - minz) : (maxx - minx);


        mapView.addRenderer(new MapRenderer() {
            public boolean r = false;
            MapCursorCollection cursor = new MapCursorCollection();
            MapCursor cu = new MapCursor((byte) 0, (byte) 0, (byte) 0, (byte) 0, true);
            int sa = (maxx - minx) < (maxz - minz) ? (maxz - minz) - (maxx - minx) : (maxx - minx) - (maxz - minz);
            boolean isX = (maxx - minx) < (maxz - minz);

            @Override
            public void render(MapView v, MapCanvas c, Player p) {
                if (!r) {
                    c.drawImage(0, 0, map_img);
                    r = true;
                    cursor.addCursor(cu);
                    c.setCursors(cursor);
                }
                for (PlotInfo value : mapPlot.values()) {
                    if (!value.isSetup()) {
                        Location loc = value.loc;
                        int xpos = isX ? (loc.getBlockX() - minx) + (sa / 2) : (loc.getBlockX() - minx);
                        int zpos = !isX ? (loc.getBlockZ() - minz) + (sa / 2) : (loc.getBlockZ() - minz);

                        if ((xpos >= 0 && xpos < resolution) || (zpos >= 0 && zpos < resolution)) {
                            int centerratiox = (xpos >= 0 && xpos < resolution) ? getRatioValue(resolution, xpos) - 128 : (int) cu.getX();
                            int centerratioz = (zpos >= 0 && zpos < resolution) ? getRatioValue(resolution, zpos) - 128 : (int) cu.getY();
                            byte px = (byte) centerratiox;
                            byte py = (byte) centerratioz;

                            MapCursor cu2 = new MapCursor((byte) 0, (byte) 0, (byte) 0, (byte) 0, true);
                            cu2.setX(px);
                            cu2.setY(py);
                            cu2.setType(value.type);

                            value.cursor = cu2;
                            c.getCursors().addCursor(cu2);
                        }
                        value.setSetup(true);
                    }
                }

                for (String key : plotRem) {
                    PlotInfo value = mapPlot.get(key);
                    if (value.cursor != null) {
                        c.getCursors().removeCursor(value.cursor);
                    }
                }

                Location loc = p.getLocation();
                int xpos = isX ? (loc.getBlockX() - minx) + (sa / 2) : (loc.getBlockX() - minx);
                int zpos = !isX ? (loc.getBlockZ() - minz) + (sa / 2) : (loc.getBlockZ() - minz);
                if ((xpos >= 0 && xpos < resolution) || (zpos >= 0 && zpos < resolution)) {
                    int centerratiox = (xpos >= 0 && xpos < resolution) ? getRatioValue(resolution, xpos) - 128 : (int) cu.getX();
                    int centerratioz = (zpos >= 0 && zpos < resolution) ? getRatioValue(resolution, zpos) - 128 : (int) cu.getY();
                    byte px = (byte) centerratiox;
                    byte py = (byte) centerratioz;
                    cu.setX(px);
                    cu.setY(py);
                    cu.setDirection(getDirection(loc));
                }
            }
        });
        ItemStack map = new ItemStack(Material.FILLED_MAP);
        MapMeta mapMeta = (MapMeta) map.getItemMeta();
        mapMeta.setMapView(mapView);
        map.setItemMeta(mapMeta);
        return map;
    }

    private static int getRatioValue(int resolution, int pos) {
        return (256 * pos) / resolution;
    }

    private static Byte getDirection(Location loc) {
        double rotation = (loc.getYaw() - 90) % 360;
        if (rotation < 0) {
            rotation += 360.0;
        }
        if (0 <= rotation && rotation < 22.5) {
            return 0x4;
        } else if (22.5 <= rotation && rotation < 67.5) {
            return 0x6;
        } else if (67.5 <= rotation && rotation < 112.5) {
            return 0x8;
        } else if (112.5 <= rotation && rotation < 157.5) {
            return 0xA;
        } else if (157.5 <= rotation && rotation < 202.5) {
            return 0xC;
        } else if (202.5 <= rotation && rotation < 247.5) {
            return 0xE;
        } else if (247.5 <= rotation && rotation < 292.5) {
            return 0x0;
        } else if (292.5 <= rotation && rotation < 337.5) {
            return 0x2;
        } else if (337.5 <= rotation && rotation < 360.0) {
            return 0x4;
        } else {
            return null;
        }
    }
}

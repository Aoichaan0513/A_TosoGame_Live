package jp.aoichaan0513.A_TosoGame_Live.API.Map

import jp.aoichaan0513.A_TosoGame_Live.API.Manager.World.WorldConfig.BorderType
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.World.WorldManager
import jp.aoichaan0513.A_TosoGame_Live.Main
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.MapMeta
import org.bukkit.map.*
import java.io.File
import java.io.IOException
import java.util.*
import javax.imageio.ImageIO

class MapUtility {
    companion object {

        var map: ItemStack? = null
            private set
        var plotRem: List<String> = ArrayList()
        var mapPlot = HashMap<String, PlotInfo>()
        var resolution = 1

        fun generateMap(): Boolean {
            val itemStack = areaMap
            map = itemStack.clone()
            return map != null
        }

        private val areaMap: ItemStack
            private get() {
                val world = WorldManager.world

                val worldConfig = Main.worldConfig

                val p1 = worldConfig.mapBorderConfig.getLocation(BorderType.POINT_1)
                val p2 = worldConfig.mapBorderConfig.getLocation(BorderType.POINT_2)

                val max_height = world.maxHeight

                val minx = Math.min(p1.blockX, p2.blockX)
                val minz = Math.min(p1.blockZ, p2.blockZ)
                val maxx = Math.max(p1.blockX, p2.blockX)
                val maxz = Math.max(p1.blockZ, p2.blockZ)

                val block_type = HashMap<String, Tuple>()
                for (i in max_height - 1 downTo 0) {
                    for (x in minx..maxx) {
                        for (z in minz..maxz) {
                            if (block_type.containsKey("${maxx - x}_${maxz - z}")) continue
                            val loc = Location(world, x.toDouble(), i.toDouble(), z.toDouble())
                            val b = loc.block
                            if (BlockColor.getBlockColor(b.type) != null)
                                block_type["${maxx - x}_${maxz - z}"] = Tuple(b.type)
                        }
                    }
                }
                val width = maxx - minx
                val height = maxz - minz
                val data: MutableList<String> = ArrayList()
                for (key in block_type.keys) {
                    val d = key.split("_").toTypedArray()
                    val x = d[0].toInt()
                    val y = d[1].toInt()
                    val t = block_type[key]!!
                    val c = BlockColor.getBlockColor(t.material)!!
                    data.add("$x,$y,${c.red},${c.green},${c.blue}")
                }

                val map_img3 = ImageGenerator.getPaintedImage(width, height, data)
                val map_img2 = ImageGenerator.getRotatedImage(180, map_img3)
                val map_img = ImageGenerator.getResizedImage(128, map_img2)
                val file = File("${world.name}${Main.FILE_SEPARATOR}map.png")
                try {
                    ImageIO.write(map_img2, "png", file)
                } catch (e: IOException) {
                    e.printStackTrace()
                }

                val mapView = Bukkit.createMap(world)
                mapView.isUnlimitedTracking = true
                mapView.centerX = 99999
                mapView.centerZ = 99999
                mapView.scale = MapView.Scale.FARTHEST
                resolution = if (maxx - minx < maxz - minz) maxz - minz else maxx - minx
                mapView.addRenderer(object : MapRenderer() {
                    var r = false
                    var cursor = MapCursorCollection()
                    var cu = MapCursor(0.toByte(), 0.toByte(), 0.toByte(), 0.toByte(), true)
                    var sa = if (maxx - minx < maxz - minz) maxz - minz - (maxx - minx) else maxx - minx - (maxz - minz)
                    var isX = maxx - minx < maxz - minz

                    override fun render(v: MapView, c: MapCanvas, p: Player) {
                        if (!r) {
                            c.drawImage(0, 0, map_img)
                            r = true
                            cursor.addCursor(cu)
                            c.cursors = cursor
                        }
                        for (value in mapPlot.values) {
                            if (!value.isSetup) {
                                val loc = value.loc
                                val xpos = if (isX) loc.blockX - minx + sa / 2 else loc.blockX - minx
                                val zpos = if (!isX) loc.blockZ - minz + sa / 2 else loc.blockZ - minz
                                if (xpos >= 0 && xpos < resolution || zpos >= 0 && zpos < resolution) {
                                    val centerratiox = if (xpos in 0 until resolution) getRatioValue(resolution, xpos) - 128 else cu.x.toInt()
                                    val centerratioz = if (zpos in 0 until resolution) getRatioValue(resolution, zpos) - 128 else cu.y.toInt()
                                    val px = centerratiox.toByte()
                                    val py = centerratioz.toByte()
                                    val cu2 = MapCursor(0.toByte(), 0.toByte(), 0.toByte(), 0.toByte(), true)
                                    cu2.x = px
                                    cu2.y = py
                                    cu2.type = value.type
                                    value.cursor = cu2
                                    c.cursors.addCursor(cu2)
                                }
                                value.isSetup = true
                            }
                        }
                        for (key in plotRem) {
                            val value = mapPlot[key]
                            if (value!!.cursor != null) {
                                c.cursors.removeCursor(value.cursor!!)
                            }
                        }
                        val loc = p.location
                        val xpos = if (isX) loc.blockX - minx + sa / 2 else loc.blockX - minx
                        val zpos = if (!isX) loc.blockZ - minz + sa / 2 else loc.blockZ - minz
                        if (xpos in 0 until resolution || zpos in 0 until resolution) {
                            val centerratiox = if (xpos in 0 until resolution) getRatioValue(resolution, xpos) - 128 else cu.x.toInt()
                            val centerratioz = if (zpos in 0 until resolution) getRatioValue(resolution, zpos) - 128 else cu.y.toInt()
                            val px = centerratiox.toByte()
                            val py = centerratioz.toByte()
                            cu.x = px
                            cu.y = py
                            cu.direction = getDirection(loc)!!
                        }
                    }
                })
                val map = ItemStack(Material.FILLED_MAP)
                val mapMeta = (map.itemMeta as MapMeta)
                mapMeta.setMapView(mapView)
                map.itemMeta = mapMeta
                return map
            }

        private fun getRatioValue(resolution: Int, pos: Int): Int {
            return 256 * pos / resolution
        }

        private fun getDirection(loc: Location): Byte? {
            var rotation = (loc.yaw - 90) % 360.toDouble()
            if (rotation < 0)
                rotation += 360.0

            return if (0 <= rotation && rotation < 22.5) {
                0x4
            } else if (22.5 <= rotation && rotation < 67.5) {
                0x6
            } else if (67.5 <= rotation && rotation < 112.5) {
                0x8
            } else if (112.5 <= rotation && rotation < 157.5) {
                0xA
            } else if (157.5 <= rotation && rotation < 202.5) {
                0xC
            } else if (202.5 <= rotation && rotation < 247.5) {
                0xE
            } else if (247.5 <= rotation && rotation < 292.5) {
                0x0
            } else if (292.5 <= rotation && rotation < 337.5) {
                0x2
            } else if (337.5 <= rotation && rotation < 360.0) {
                0x4
            } else {
                null
            }?.toByte()
        }
    }
}
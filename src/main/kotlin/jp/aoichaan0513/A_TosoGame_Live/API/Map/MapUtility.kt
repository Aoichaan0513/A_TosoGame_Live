package jp.aoichaan0513.A_TosoGame_Live.API.Map

import jp.aoichaan0513.A_TosoGame_Live.API.Manager.World.WorldConfig.BorderType
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.World.WorldManager
import jp.aoichaan0513.A_TosoGame_Live.Main
import org.bukkit.Bukkit
import org.bukkit.ChatColor
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

        val material = Material.FILLED_MAP

        val itemStack: ItemStack?
            get() {
                val mapStack = map?.clone() ?: return null
                val mapMeta = mapStack.itemMeta!!
                mapMeta.setDisplayName("${ChatColor.GREEN}地図")
                mapMeta.lore = Arrays.asList("${ChatColor.YELLOW}なんかすごいやつ (語彙力)")
                mapStack.itemMeta = mapMeta
                return mapStack
            }

        var map: ItemStack? = null
            private set
        var plotRem = listOf<String>()
        var mapPlot = HashMap<String, PlotInfo>()
        var resolution = 1

        fun generateMap(): Boolean {
            val itemStack = areaMap
            map = itemStack.clone()
            return map != null
        }

        private val areaMap: ItemStack
            get() {
                val world = WorldManager.world

                val worldConfig = Main.worldConfig

                val p1 = worldConfig.mapBorderConfig.getLocation(BorderType.POINT_1)
                val p2 = worldConfig.mapBorderConfig.getLocation(BorderType.POINT_2)

                val minX = p1.blockX.coerceAtMost(p2.blockX)
                val maxX = p1.blockX.coerceAtLeast(p2.blockX)
                val minY = p1.blockY.coerceAtMost(p2.blockY)
                val maxY = p1.blockY.coerceAtLeast(p2.blockY)
                val minZ = p1.blockZ.coerceAtMost(p2.blockZ)
                val maxZ = p1.blockZ.coerceAtLeast(p2.blockZ)

                val width = maxX - minX
                val height = maxZ - minZ

                val isX = maxX - minX < maxZ - minZ

                val dataMap = mutableMapOf<String, DataClass.BlockData>()
                for (y in maxY - 1 downTo minY) {
                    for (x in minX..maxX) {
                        for (z in minZ..maxZ) {
                            val x1 = maxX - x
                            val z1 = maxZ - z

                            if (dataMap.containsKey("${x1}_$z1")) continue

                            val rgbColor = BlockColor.getBlockColor(Location(world, x.toDouble(), y.toDouble(), z.toDouble()).block.type)
                            if (rgbColor != null)
                                dataMap["${x1}_$z1"] = DataClass.BlockData(x1, z1, rgbColor)
                        }
                    }
                }

                val paintedImage = ImageGenerator.getPaintedImage(width, height, dataMap.values.toList())
                val rotatedImage = ImageGenerator.getRotatedImage(180, paintedImage)
                val resizedImage = ImageGenerator.getResizedImage(128, rotatedImage)
                val file = File("${world.name}${Main.FILE_SEPARATOR}map.png")

                try {
                    ImageIO.write(rotatedImage, "png", file)
                } catch (e: IOException) {
                    e.printStackTrace()
                }

                val mapView = Bukkit.createMap(world)
                mapView.isUnlimitedTracking = true
                mapView.centerX = 99999
                mapView.centerZ = 99999
                mapView.scale = MapView.Scale.FARTHEST
                resolution = if (isX) maxZ - minZ else maxX - minX

                mapView.addRenderer(object : MapRenderer() {

                    var r = false
                    val cursor = MapCursorCollection()
                    val mapCursor = MapCursor(0.toByte(), 0.toByte(), 0.toByte(), 0.toByte(), true)
                    val sa = if (isX) maxZ - minZ - (maxX - minX) else maxX - minX - (maxZ - minZ)

                    override fun render(mapView: MapView, mapCanvas: MapCanvas, p: Player) {
                        if (!r) {
                            mapCanvas.drawImage(0, 0, resizedImage)
                            r = true
                            cursor.addCursor(mapCursor)
                            mapCanvas.cursors = cursor
                        }

                        for (value in mapPlot.values) {
                            if (!value.isSetup) {
                                val loc = value.loc
                                val cursorRatio = getCursorRatio(loc, mapCursor, isX, minX, minZ, sa)
                                if (cursorRatio != null) {
                                    val (ratioX, ratioY) = cursorRatio
                                    val cu2 = MapCursor(0.toByte(), 0.toByte(), 0.toByte(), 0.toByte(), true)
                                    cu2.x = ratioX
                                    cu2.y = ratioY
                                    cu2.type = value.type
                                    value.cursor = cu2
                                    mapCanvas.cursors.addCursor(cu2)
                                }
                                value.isSetup = true
                            }
                        }

                        for (key in plotRem) {
                            val value = mapPlot[key]
                            if (value!!.cursor != null)
                                mapCanvas.cursors.removeCursor(value.cursor!!)
                        }

                        val loc = p.location
                        val cursorRatio = getCursorRatio(loc, mapCursor, isX, minX, minZ, sa)
                        if (cursorRatio != null) {
                            val (ratioX, ratioY) = cursorRatio
                            mapCursor.x = ratioX
                            mapCursor.y = ratioY
                            mapCursor.direction = getDirection(loc)!!
                        }
                    }
                })
                val map = ItemStack(material)
                val mapMeta = (map.itemMeta as MapMeta)
                mapMeta.mapView = mapView
                map.itemMeta = mapMeta
                return map
            }

        private fun getCursorRatio(loc: Location, mapCursor: MapCursor, isX: Boolean, minX: Int, minZ: Int, sa: Int): Pair<Byte, Byte>? {
            val xPos = if (isX) loc.blockX - minX + sa / 2 else loc.blockX - minX
            val zPos = if (!isX) loc.blockZ - minZ + sa / 2 else loc.blockZ - minZ

            return if (xPos in 0 until resolution || zPos in 0 until resolution) {
                val centerRatioX = if (xPos in 0 until resolution) getRatioValue(resolution, xPos) - 128 else mapCursor.x.toInt()
                val centerRatioZ = if (zPos in 0 until resolution) getRatioValue(resolution, zPos) - 128 else mapCursor.y.toInt()
                Pair(centerRatioX.toByte(), centerRatioZ.toByte())
            } else {
                null
            }
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
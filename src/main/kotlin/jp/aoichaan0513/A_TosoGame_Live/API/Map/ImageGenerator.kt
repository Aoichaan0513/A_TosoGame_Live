package jp.aoichaan0513.A_TosoGame_Live.API.Map

import jp.aoichaan0513.A_TosoGame_Live.Utils.DataClass
import java.awt.Color
import java.awt.Image
import java.awt.geom.AffineTransform
import java.awt.image.BufferedImage

class ImageGenerator {
    companion object {

        fun getRotatedImage(angle: Int, img: BufferedImage): BufferedImage {
            val sourceImage = img

            val width = sourceImage.width
            val height = sourceImage.height

            val outputImage = if (angle % 180 != 0) {
                BufferedImage(height, width, BufferedImage.TYPE_INT_RGB)
            } else {
                BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
            }

            val out = outputImage.createGraphics()
            val at = AffineTransform()
            if (angle == 0) {
                // 何もしない
            } else if (angle == 180) {
                at.setToRotation(Math.toRadians(-1.0 * angle), width / 2.toDouble(), height / 2.toDouble())
            } else if (angle == 270) {
                at.setToRotation(Math.toRadians(-1.0 * angle), width / 2.toDouble(), height / 2.toDouble())
                at.translate((width - height) / 2.toDouble(), (width - height) / 2.toDouble())
            } else {
                at.setToRotation(Math.toRadians(-1.0 * angle), width / 2.toDouble(), height / 2.toDouble())
                at.translate(-(width - height) / 2.toDouble(), -(width - height) / 2.toDouble())
            }
            out.drawImage(sourceImage, at, null)
            return outputImage
        }

        fun getPaintedImage(width: Int, height: Int, list: List<DataClass.BlockData>): BufferedImage {
            val size = if (height < width) width else height

            var addX = 0
            var addY = 0

            if (width != height) {
                if (width < height)
                    addX = (height - width) / 2
                else
                    addY = (width - height) / 2
            }

            val im = BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB)

            val graphics = im.graphics
            graphics.color = Color(206, 180, 136)
            graphics.fillRect(0, 0, im.width, im.height)

            for (blockData in list) {
                val (x, y) = Pair(blockData.x + addX, blockData.y + addY)
                val (r, g, b) = blockData.rgbColor

                graphics.color = Color(r, g, b)
                graphics.drawLine(x, y, x, y)
            }

            im.flush()
            graphics.dispose()
            return im
        }

        fun getResizedImage(size: Int, img: BufferedImage): BufferedImage {
            val tmp = img.getScaledInstance(size, size, Image.SCALE_DEFAULT)
            val bufferedImage = BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB)
            val graphics2D = bufferedImage.createGraphics()
            graphics2D.drawImage(tmp, 0, 0, null)
            graphics2D.dispose()
            return bufferedImage
        }
    }
}
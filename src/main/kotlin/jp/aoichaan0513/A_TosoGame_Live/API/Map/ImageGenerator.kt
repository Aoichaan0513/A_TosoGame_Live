package jp.aoichaan0513.A_TosoGame_Live.API.Map

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
                // 180度の倍数の時は画像サイズの変更は行わない。
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

        fun getPaintedImage(width: Int, height: Int, command: List<String>): BufferedImage {
            val size = if (height < width) width else height
            var addx = 0
            var addy = 0
            if (width != height) {
                if (width < height) {
                    addx = (height - width) / 2
                } else {
                    addy = (width - height) / 2
                }
            }
            val im = BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB)
            val g = im.graphics
            g.color = Color(206, 180, 136)
            g.fillRect(0, 0, im.width, im.height)
            for (cmd in command) {
                val data = cmd.split(",").toTypedArray()
                /*
                 * [DataFormat]
                 * 0: x
                 * 1: y
                 * 2: r
                 * 3: g
                 * 4: b
                 */g.color = Color(data[2].toInt(), data[3].toInt(), data[4].toInt())
                g.drawLine(data[0].toInt() + addx, data[1].toInt() + addy, data[0].toInt() + addx, data[1].toInt() + addy)
            }
            im.flush()
            g.dispose()
            return im
        }

        fun getResizedImage(size: Int, img: BufferedImage): BufferedImage {
            val tmp = img.getScaledInstance(size, size, Image.SCALE_DEFAULT)
            val dimg = BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB)
            val g2d = dimg.createGraphics()
            g2d.drawImage(tmp, 0, 0, null)
            g2d.dispose()
            return dimg
        }
    }
}
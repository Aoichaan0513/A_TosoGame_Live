package jp.aoichaan0513.A_TosoGame_Live.API.Maps;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.List;

public class ImageGenerator {

    public static BufferedImage getRotatedImage(int angle, BufferedImage img) {
        int width = 0;
        int height = 0;
        // 元画像の読み込み
        BufferedImage sourceImage = null;
        BufferedImage outputImage = null;
        sourceImage = img;
        width = sourceImage.getWidth();
        height = sourceImage.getHeight();

        if (angle % 180 != 0) {
            outputImage = new BufferedImage(height, width, BufferedImage.TYPE_INT_RGB);
        } else {
            // 180度の倍数の時は画像サイズの変更は行わない。
            outputImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        }
        Graphics2D out = outputImage.createGraphics();
        AffineTransform at = new AffineTransform();

        if (angle == 0) {
            // 何もしない
        } else if (angle == 180) {
            at.setToRotation(Math.toRadians(-1.0 * angle), width / 2, height / 2);
        } else if (angle == 270) {
            at.setToRotation(Math.toRadians(-1.0 * angle), width / 2, height / 2);
            at.translate((width - height) / 2, (width - height) / 2);
        } else {
            at.setToRotation(Math.toRadians(-1.0 * angle), width / 2, height / 2);
            at.translate(-(width - height) / 2, -(width - height) / 2);
        }
        out.drawImage(sourceImage, at, null);
        return outputImage;
    }

    public static BufferedImage getPaintedImage(int width, int height, List<String> command) {
        int size = height < width ? width : height;
        int addx = 0;
        int addy = 0;
        if (width != height) {
            if (width < height) {
                addx = (height - width) / 2;
            } else {
                addy = (width - height) / 2;
            }
        }
        BufferedImage im = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics g = im.getGraphics();
        g.setColor(new Color(206, 180, 136));
        g.fillRect(0, 0, im.getWidth(), im.getHeight());

        for (String cmd : command) {
            String[] data = cmd.split(",");
            /*
             * [DataFormat]
             * 0: x
             * 1: y
             * 2: r
             * 3: g
             * 4: b
             */
            g.setColor(new Color(Integer.parseInt(data[2]), Integer.parseInt(data[3]), Integer.parseInt(data[4])));
            g.drawLine(Integer.parseInt(data[0]) + addx, Integer.parseInt(data[1]) + addy, Integer.parseInt(data[0]) + addx, Integer.parseInt(data[1]) + addy);
        }

        im.flush();
        g.dispose();
        return im;
    }

    public static BufferedImage getResizedImage(int size, BufferedImage img) {
        Image tmp = img.getScaledInstance(size, size, Image.SCALE_DEFAULT);
        BufferedImage dimg = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return dimg;
    }
}

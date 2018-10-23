package utils;

import java.awt.*;
import java.awt.image.BufferedImage;

import static java.awt.image.BufferedImage.TYPE_INT_RGB;

public class ImageResizer {

    public static BufferedImage resizeBig(Image originalImage){
        return resize(originalImage,128);
    }

    public static BufferedImage resizeSmall(Image originalImage){
         return resize(originalImage,16);
    }


    private static BufferedImage resize(Image originalImage, int scaled)
    {

        BufferedImage scaledBI = new BufferedImage(scaled, scaled, TYPE_INT_RGB);
        Graphics2D g = scaledBI.createGraphics();
        g.drawImage(originalImage, 0, 0, scaled, scaled, null);
        g.dispose();
        return scaledBI;
    }
}

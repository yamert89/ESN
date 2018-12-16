package esn.utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static java.awt.image.BufferedImage.TYPE_INT_RGB;

public class ImageResizer {

    public static byte[] resizeBig(byte[] input, String format){
        return resize(input,128, format);
    }

    public static byte[] resizeSmall(byte[] input, String format){
         return resize(input,16, format);
    }


    private static byte[] resize(byte[] input, int scaled, String format)
    {
        try {
            Image originalImage = ImageIO.read(new ByteArrayInputStream(input));

            BufferedImage scaledBI = new BufferedImage(scaled, scaled, TYPE_INT_RGB);
            Graphics2D g = scaledBI.createGraphics();
            g.drawImage(originalImage, 0, 0, scaled, scaled, null);
            g.dispose();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write( scaledBI, format, baos );
            baos.flush();
            byte[] imageInByte = baos.toByteArray();
            baos.close();
            return imageInByte;
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;

    }
}

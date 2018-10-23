import utils.ImageResizer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class SimpleTests {
    public static void main(String[] args) {
        try {
            BufferedImage image = ImageIO.read(new File("C:/2.jpg"));
            BufferedImage image1 = ImageResizer.resizeBig(image);
            ImageIO.write(image1, "JPG", new File("D:/out.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

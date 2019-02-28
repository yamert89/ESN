package esn.utils;

import esn.configs.GeneralSettings;
import esn.entities.User;
import org.apache.commons.io.FileUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import static java.awt.image.BufferedImage.TYPE_INT_RGB;

public class ImageUtil {

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

    public static String getExtension(MultipartFile image){
        switch (image.getContentType()){
            case  "image/jpeg":
                return "jpg";
            case "image/png":
                return "png";
            case "image/x-png":
                return "png";
            case "image/bmp":
                return "bmp";

            default: return "jpg";

        }
    }

    public static void writeImage(User user, MultipartFile image){
        try {
            String extension = ImageUtil.getExtension(image);

            String fileName = user.getLogin().concat(".").concat(extension);
            String fileNameSmall = user.getLogin().concat("_small").concat(".").concat(extension);
            byte[] bytes = image.getBytes();
            byte[] bigImage = ImageUtil.resizeBig(bytes, extension);
            byte[] smallImage = ImageUtil.resizeSmall(bytes, extension);
            //if (bigImage == null || smallImage == null) return "reg"; //TODO если ошибка
            System.out.println(user.getName());
            System.out.println(fileName);
            System.out.println(fileNameSmall);
            System.out.println(GeneralSettings.AVATAR_PATH);
            FileUtils.writeByteArrayToFile(new File(GeneralSettings.AVATAR_PATH.concat(fileName)),bigImage);
            FileUtils.writeByteArrayToFile(new File(GeneralSettings.AVATAR_PATH.concat(fileNameSmall)),smallImage);
            user.setPhoto(fileName);
            user.setPhoto_small(fileNameSmall);

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Ошибка записи аватара");
        }
    }
}

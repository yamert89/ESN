package esn.utils;

import esn.configs.GeneralSettings;
import esn.entities.Organization;
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

    private static byte[] resizeBig(byte[] input, String format){
        return resize(input,128, format);
    }

    private static byte[] resizeSmall(byte[] input, String format){
         return resize(input,32, format);
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

    private static String getExtension(MultipartFile image){
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

    public static void writeAvatar(User user, MultipartFile image){ //TODO изменить путь в соответтвии с организац
        try {
            String extension = ImageUtil.getExtension(image);

            String fileName = GeneralSettings.STORAGE_PATH.concat("/").concat(user.getLogin()).concat("/avatar_big.").concat(extension);
            String fileNameSmall = GeneralSettings.STORAGE_PATH.concat("/").concat(user.getLogin()).concat("/avatar_small.").concat(extension);
            byte[] bytes = image.getBytes();
            byte[] bigImage = ImageUtil.resizeBig(bytes, extension);
            byte[] smallImage = ImageUtil.resizeSmall(bytes, extension);
            writeImage(fileName, bigImage);
            writeImage(fileNameSmall, smallImage);
            user.setPhoto(fileName);
            user.setPhoto_small(fileNameSmall);

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Ошибка записи аватара");
        }
    }

    public static void writeHeader(Organization org, MultipartFile image){
        try {
            String extension = ImageUtil.getExtension(image);
            String fileName = GeneralSettings.STORAGE_PATH.concat("/").concat(org.getUrlName()).concat("/header.").concat(extension);
            byte[] bytes = image.getBytes();
            writeImage(fileName, bytes);
            org.setHeaderPath(fileName);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Ошибка записи аватара");
        }
    }

    private static void writeImage(String fileName, byte[] imageBytes){
        try {
            File file = new File(fileName);
            if (file.exists()) FileUtils.forceDelete(file);
            FileUtils.writeByteArrayToFile(file, imageBytes);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Ошибка записи картинки");
        }
    }



}

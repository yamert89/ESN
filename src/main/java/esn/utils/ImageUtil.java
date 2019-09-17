package esn.utils;

import esn.configs.GeneralSettings;
import esn.entities.Organization;
import esn.entities.User;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
    private final static Logger logger = LogManager.getLogger(ImageUtil.class);

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
            logger.error(e.getMessage(), e);
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

    public static void writeAvatar(User user, MultipartFile image){
        try {
            String extension = ImageUtil.getExtension(image);

            String avatarsPath = "/" + user.getOrganization().getUrlName() + "/" + user.getLogin();

            String path = GeneralSettings.STORAGE_PATH + avatarsPath;

            String fileName = "/avatar_big." + extension;
            String fileNameSmall = "/avatar_small." + extension;
            byte[] bytes = image.getBytes();
            byte[] bigImage = ImageUtil.resizeBig(bytes, extension);
            byte[] smallImage = ImageUtil.resizeSmall(bytes, extension);
            writeImage(path + fileName, bigImage);
            writeImage(path + fileNameSmall, smallImage);
            user.setPhoto(avatarsPath + fileName);
            user.setPhoto_small(avatarsPath + fileNameSmall);

        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            logger.debug("Ошибка записи аватара");
        }
    }

    public static void writeHeader(Organization org, MultipartFile image){
        try {
            String extension = ImageUtil.getExtension(image);
            String headerPath = "/" + org.getUrlName() + "/header." + extension;
            String fileName = GeneralSettings.STORAGE_PATH.concat(headerPath);
            byte[] bytes = image.getBytes();
            writeImage(fileName, bytes);
            org.setHeaderPath(headerPath);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            logger.debug("Ошибка записи хэдера");
        }
    }

    private static void writeImage(String fileName, byte[] imageBytes){
        try {
            File file = new File("/" + fileName);
            if (file.exists()) FileUtils.forceDelete(file);
            FileUtils.writeByteArrayToFile(file, imageBytes);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            logger.debug("Ошибка записи картинки");
        }
    }



}

package esn.utils;

import esn.configs.GeneralSettings;
import esn.viewControllers.main.GroupsController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SimpleUtils {
    private final static Logger logger = LogManager.getLogger(GroupsController.class);

    public static String getNickName(String input) {
        String name = input.toLowerCase();
        String rusAlph = "абвгдеёжзиклмнопрстуфхцчшщъыьэюя ";
        String engAlph = "abvgdeezziklmnoprstufhccss1i1euy_";
        char[] engArr = engAlph.toCharArray();
        char[] arr = name.toCharArray();
        for(int i = 0; i < arr.length; i++){
            char[] charArray = rusAlph.toCharArray();
            for (int i1 = 0; i1 < charArray.length; i1++) {
                char ch = charArray[i1];
                if (arr[i] == ch) name = name.replace(ch, engArr[i1]);
            }
        }
        return name;
    }

    public static String getEncodedPassword(String input){
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            logger.error(e.getMessage(), e);
        }
        return new String(md.digest(input.getBytes()));
    }

    public static int getPublicStoragePercentageSize(String orgUrl){
        return getPercentageSize(Paths.get(GeneralSettings.STORAGE_PATH + "/" + orgUrl + "/stored_files/"), GeneralSettings.PUBLIC_STORAGE_MAX_SIZE);
    }

    public static int getPrivateStoragePercentageSize(String orgUrl, String userLogin){
        return getPercentageSize(Paths.get(GeneralSettings.STORAGE_PATH + "/" + orgUrl + "/stored_files/" + userLogin + "/"), GeneralSettings.PRIVATE_STORAGE_MAX_SIZE);
    }

    private static int getPercentageSize(Path path, int maxSize){
        final long[] size = {0};
        try {
            Files.walk(path, FileVisitOption.FOLLOW_LINKS)
                    .forEach(file -> {
                        if (!Files.isDirectory(file, LinkOption.NOFOLLOW_LINKS)) size[0] = file.toFile().length();
                    });
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return (int)((size[0]/1024d/1024d) / (double)maxSize * 100);
    }

    public static void createUserFolders(String orgUrl, String userLogin){
        try {
            String p = GeneralSettings.STORAGE_PATH + "/" + orgUrl + "/stored_files/" + userLogin + "/";
            String p2 = GeneralSettings.STORAGE_PATH + "/" + orgUrl + "/" + userLogin + "/";
            logger.debug(p);
            logger.debug(p2);
            Files.createDirectories(Paths.get(p));
            Files.createDirectories(Paths.get(p2));
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }



}

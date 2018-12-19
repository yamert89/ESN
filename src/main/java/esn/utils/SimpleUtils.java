package esn.utils;

import org.springframework.web.multipart.MultipartFile;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SimpleUtils {

    public static String getNickName(String input) {
        String name = input.toLowerCase();
        String rusAlph = "абвгдеёжзиклмнопрстуфхцчшщъыьэюя "; //TODO придумать получше
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

    public static String getExpansion(MultipartFile image){
        switch (image.getContentType()){
            case  "image/jpeg":
                return "jpg";
            case "image/png":
                return "png";
            case "image/x-png":
                return "png";
            case "image/bmp":
                return "bmp";

            default: return null;

        }
    }

    public static String getEncodedString(String s){
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return md.digest(s.getBytes()).toString();
    }



}

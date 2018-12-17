package esn.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class GeneralSettings {
    public static String AVATAR_PATH;
    static {
        try {
            Properties properties = new Properties();

            InputStream inputStream = GeneralSettings.class.getClassLoader().getResourceAsStream("properties");




            properties.load(inputStream);

            AVATAR_PATH = properties.getProperty("avatar_dir");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}

package esn.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class GeneralSettings {
    public static String AVATAR_PATH;
    public static int AMOUNT_GENCHAT_MESSAGES;
    public static int AMOUNT_PRIVATECHAT_MESSAGES;
    public static int AMOUNT_WALL_MESSAGES;
    static {
        try {
            Properties properties = new Properties();

            InputStream inputStream = GeneralSettings.class.getClassLoader().getResourceAsStream("properties");




            properties.load(inputStream);

            AVATAR_PATH = properties.getProperty("avatar_dir");
            AMOUNT_GENCHAT_MESSAGES = Integer.valueOf(properties.getProperty("gen_chat_messages_amount"));
            AMOUNT_PRIVATECHAT_MESSAGES = Integer.valueOf(properties.getProperty("private_chat_messages_amount"));
            AMOUNT_WALL_MESSAGES = Integer.valueOf(properties.getProperty("wall_messages_amount"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}

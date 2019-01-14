package esn.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class GeneralSettings {
    public static String AVATAR_PATH = "resources/avatars/";
    public static int AMOUNT_GENCHAT_MESSAGES = 50;
    public static int AMOUNT_PRIVATECHAT_MESSAGES = 50;
    public static int AMOUNT_WALL_MESSAGES = 25;
    public static String TIME_PATTERN = "dd.MM.yyyy, HH:mm:ss";
    static {
        try {
            Properties properties = new Properties();

            InputStream inputStream = GeneralSettings.class.getClassLoader().getResourceAsStream("properties");

            properties.load(inputStream);

            AVATAR_PATH = properties.getProperty("avatar_dir");
            AMOUNT_GENCHAT_MESSAGES = Integer.valueOf(properties.getProperty("gen_chat_messages_amount"));
            AMOUNT_PRIVATECHAT_MESSAGES = Integer.valueOf(properties.getProperty("private_chat_messages_amount"));
            AMOUNT_WALL_MESSAGES = Integer.valueOf(properties.getProperty("wall_messages_amount"));
            TIME_PATTERN = properties.getProperty("date_time_pattern");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("ERROR: Ошибка загрузки файла свойств");
        }
    }


}

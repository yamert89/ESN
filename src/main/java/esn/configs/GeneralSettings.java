package esn.configs;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class GeneralSettings {
/*    public static String AVATAR_PATH = "resources/avatars/";
    public static String STORED_FILES_PATH = "resources/stored_files/";*/
    public static String STORAGE_PATH = "resources/esn_storage/";
    public static int AMOUNT_GENCHAT_MESSAGES = 50;
    public static int AMOUNT_PRIVATECHAT_MESSAGES = 50;
    public static int AMOUNT_WALL_MESSAGES = 25;
    public static String TIME_PATTERN = "HH:mm:ss / dd.MM";

    static {
        try {
            Properties properties = new Properties();

            InputStream inputStream = GeneralSettings.class.getClassLoader().getResourceAsStream("properties");

            properties.load(inputStream);

            //AVATAR_PATH = properties.getProperty("avatar_dir");
            //STORED_FILES_PATH = properties.getProperty("files_dir");
            STORAGE_PATH = properties.getProperty("storage_path");
            AMOUNT_GENCHAT_MESSAGES = Integer.valueOf(properties.getProperty("gen_chat_messages_amount"));
            AMOUNT_PRIVATECHAT_MESSAGES = Integer.valueOf(properties.getProperty("private_chat_messages_amount"));
            AMOUNT_WALL_MESSAGES = Integer.valueOf(properties.getProperty("wall_messages_amount"));
            TIME_PATTERN = properties.getProperty("date_time_pattern");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("ERROR: Ошибка загрузки файла свойств");
        }

        //TODO CSRF Protection security.xml
        //TODO remember me dont work
        //TODO wall center post add html bag on small display

    }


}

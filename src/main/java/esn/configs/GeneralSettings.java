package esn.configs;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Properties;


public class GeneralSettings {

    public static String STORAGE_PATH;
    public static int AMOUNT_GENCHAT_MESSAGES = 50;
    public static int AMOUNT_PRIVATECHAT_MESSAGES = 50;
    public static int AMOUNT_WALL_MESSAGES = 25;
    public static int PUBLIC_STORAGE_MAX_SIZE = 1024; //MB
    public static int PRIVATE_STORAGE_MAX_SIZE = 512; //MB
    public static String TIME_PATTERN = "HH:mm:ss / dd.MM";

    static {
        try {
            String path = GeneralSettings.class.getClassLoader().getResource("").getPath();
            Path p = java.nio.file.Paths.get(path.substring(1));
            p = java.nio.file.Paths.get(p.getParent().getParent().toString(), "/resources/data/");
            STORAGE_PATH = p.toString();

            Properties properties = new Properties();

            InputStream inputStream = GeneralSettings.class.getClassLoader().getResourceAsStream("properties");

            properties.load(inputStream);

            AMOUNT_GENCHAT_MESSAGES = Integer.valueOf(properties.getProperty("gen_chat_messages_amount"));
            AMOUNT_PRIVATECHAT_MESSAGES = Integer.valueOf(properties.getProperty("private_chat_messages_amount"));
            AMOUNT_WALL_MESSAGES = Integer.valueOf(properties.getProperty("wall_messages_amount"));
            TIME_PATTERN = properties.getProperty("date_time_pattern");
            PUBLIC_STORAGE_MAX_SIZE = Integer.parseInt(properties.getProperty("public_storage_max_size"));
            PRIVATE_STORAGE_MAX_SIZE = Integer.parseInt(properties.getProperty("private_storage_max_size"));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("ERROR: Ошибка загрузки файла свойств");
        }

        //TODO CSRF Protection security.xml
        //FIXME mes.imgUrl in wall.jsp url wrong orgUrl replace
        //TODO уменьшить приват чат
        //FIXME
        //FIXME
        //FIXME
        //FIXME
        //FIXME
        //FIXME
        //TODO
        //TODO
        //TODO
        //TODO
        //TODO
        //TODO



    }


}

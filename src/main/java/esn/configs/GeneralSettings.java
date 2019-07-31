package esn.configs;


import esn.db.syntax.MySQLSyntax;
import esn.db.syntax.PostgresSyntax;
import esn.db.syntax.Syntax;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Optional;
import java.util.Properties;


public class GeneralSettings {

    private static Properties properties = new Properties();

    public static String STORAGE_PATH;
    public static int AMOUNT_GENCHAT_MESSAGES;
    public static int AMOUNT_PRIVATECHAT_MESSAGES;
    public static int AMOUNT_WALL_MESSAGES;
    public static int PUBLIC_STORAGE_MAX_SIZE; //MB
    public static int PRIVATE_STORAGE_MAX_SIZE; //MB
    public static String TIME_PATTERN;
    public static Syntax DB_SYNTAX;

    static {
        try {
            String path = GeneralSettings.class.getClassLoader().getResource("").getPath();
            Path p = java.nio.file.Paths.get(path.substring(1));
            p = java.nio.file.Paths.get(p.getParent().getParent().toString(), "/resources/data/");
            STORAGE_PATH = p.toString();

            InputStream inputStream = GeneralSettings.class.getClassLoader().getResourceAsStream("properties");

            properties.load(inputStream);

            AMOUNT_GENCHAT_MESSAGES = getOptProperty("gen_chat_messages_amount").map(Integer::parseInt).orElse(50);
            AMOUNT_PRIVATECHAT_MESSAGES = getOptProperty("private_chat_messages_amount").map(Integer::parseInt).orElse(50);
            AMOUNT_WALL_MESSAGES = getOptProperty("wall_messages_amount").map(Integer::parseInt).orElse(25);
            PUBLIC_STORAGE_MAX_SIZE = getOptProperty("public_storage_max_size").map(Integer::parseInt).orElse(1024);
            PRIVATE_STORAGE_MAX_SIZE = getOptProperty("private_storage_max_size").map(Integer::parseInt).orElse(512);
            String res = getOptProperty("db_type").orElse("postgresql");
            DB_SYNTAX = res.equals("postgresql") ? new PostgresSyntax() : new MySQLSyntax();
            TIME_PATTERN = getOptProperty("date_time_pattern").orElse("HH:mm:ss / dd.MM");

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("ERROR: Ошибка загрузки файла свойств");
        }

    }

    private static Optional<String> getOptProperty(String name){
        return Optional.ofNullable(properties.getProperty(name));
    }

    //TODO CSRF Protection security.xml
    //FIXME
    //FIXME
    //FIXME
    //FIXME
    //FIXME
    //FIXME
    //FIXME
    //TODO Удаление юзера протестировать
    //TODO message more than 800 symb test
    //TODO
    //TODO
    //TODO
    //TODO


}

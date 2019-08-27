package esn.configs;


import esn.db.syntax.MySQLSyntax;
import esn.db.syntax.PostgresSyntax;
import esn.db.syntax.Syntax;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Optional;
import java.util.Properties;


public class GeneralSettings {

    private static Properties properties = new Properties();
    private final static Logger logger = LogManager.getLogger(GeneralSettings.class);

    public static String STORAGE_PATH;
    public static int AMOUNT_GENCHAT_MESSAGES;
    public static int AMOUNT_PRIVATECHAT_MESSAGES;
    public static int AMOUNT_WALL_MESSAGES;
    public static int PUBLIC_STORAGE_MAX_SIZE; //MB
    public static int PRIVATE_STORAGE_MAX_SIZE; //MB
    public static String TIME_PATTERN;
    public static Syntax DB_SYNTAX;
    public static String ADMIN_EMAIL = "softoad2@gmail.com";

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
            String res = getOptProperty("jdbc.db_type").orElse("postgresql");
            DB_SYNTAX = res.equals("postgresql") ? new PostgresSyntax() : new MySQLSyntax();
            TIME_PATTERN = getOptProperty("date_time_pattern").orElse("HH:mm:ss / dd.MM");

        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            logger.error("ERROR: Ошибка загрузки файла свойств");
        }

    }

    private static Optional<String> getOptProperty(String name){
        return Optional.ofNullable(properties.getProperty(name));
    }

    //TODO CSRF Protection security.xml
    //FIXME storage svg works not always
    //FIXME
    //FIXME gen chat new mes icon bag for remember session
    //FIXME
    //FIXME
    //FIXME если пришло сообщение ген и приват, при прочтении ген исчезает иконка и у приват
    //FIXME test index js net status
    //FIXME
    //FIXME
    //FIXME
    //FIXME
    //FIXME
    //FIXME
    //FIXME
    //FIXME
    //FIXME
    //FIXME
    //FIXME
    //TODO протестировать удаленного пользователя
    //TODO LONG транзакционное сохранение сущнос
    //TODO
    //TODO LONG заменить импуты спец тегами
    //TODO LONG восстановление пароля
    //TODO
    //TODO LONG передать обекты под управление контейнера
    //TODO LONG оформить ошибку удаленной организации для сохранненых юзеров
    //TODO LONG example img выходит за границы
    //TODO
    //TODO
    //TODO перенаправить все ошибки контроллера пользователю
    //TODO
    //TODO
    //TODO
    //TODO
    //TODO


}

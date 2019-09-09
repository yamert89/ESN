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
    public static String TIME_PATTERN = "HH:mm:ss / dd.MM"; //TODO
    public static Syntax DB_SYNTAX;
    public static String ADMIN_EMAIL = "softoad2@gmail.com";
    public static int SESSION_TIMEOUT;


    static {
        try {
            String path = GeneralSettings.class.getClassLoader().getResource("").getPath().replace("%20", " ");
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
            SESSION_TIMEOUT = getOptProperty("session_timeout").map(Integer::parseInt).orElse(300);
            String res = getOptProperty("jdbc.db_type").orElse("postgresql");

            DB_SYNTAX = res.equals("postgresql") ? new PostgresSyntax() : new MySQLSyntax();
            /*TIME_PATTERN = getOptProperty("date_time_pattern").orElse("HH:mm:ss / dd.MM");*/

        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            logger.error("ERROR: Ошибка загрузки файла свойств");
        }

    }

    private static Optional<String> getOptProperty(String name){
        return Optional.ofNullable(properties.getProperty(name));
    }

    //FIXME
    //FIXME ген чат приходит самому себе
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
    //FIXME
    //FIXME
    //FIXME
    //FIXME
    //FIXME
    //TODO LONG файл был изьят из публичного пользования
    //TODO java -jar c.jar --js stomp.js --js_output_file hello-compiled.js
    //TODO LONG транзакционное сохранение сущнос
    //TODO LONG заменить импуты спец тегами
    //TODO LONG восстановление пароля
    //TODO LONG передать обекты под управление контейнера
    //TODO LONG оформить ошибку удаленной организации для сохранненых юзеров
    //TODO LONG example img выходит за границы
    //TODO LONG CSRF Protection security.xml
    //TODO LONG get reference for save entity
    //TODO LONG custom date format
    //TODO LONG время сессии
    //TODO LONG доработать форму регистрации
    //TODO LONG выбор полного или краткого имени для каждого юзера
    //TODO LONG доп прога для перезагрузки сервера
    //TODO LONG сохранять сессию при входе. время окончания + время сессии, при выходе обновлять
    //TODO LONG
    //TODO LONG
    //TODO LONG
    //TODO LONG
    //TODO LONG


}

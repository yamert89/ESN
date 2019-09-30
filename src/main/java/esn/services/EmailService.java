package esn.services;

import esn.configs.GeneralSettings;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Component
public class EmailService {
    private final static Logger logger = LogManager.getLogger(EmailService.class);

    private String username;
    private String password;
    private Properties props;

    {
        props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.store.protocol", "imap");
        //props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.ssl.trust", "smtp.yandex.ru");
        //props.put(“mail.smtp.ssl.trust”, “smtp.gmail.com”)
        props.put("mail.smtp.host", "smtp.yandex.ru");
        props.put("mail.smtp.port", "465");
    }

    @Bean("adminEmailService")
    public EmailService emailService(){
        return new EmailService(GeneralSettings.ADMIN_EMAIL, GeneralSettings.ADMIN_PASS);
    }


    public EmailService(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public EmailService() {}

    public void send(String subject, String text, String addsInfo, String toEmail){
        logger.debug("Sending email...");
        if (toEmail == null) toEmail = username;
        Session session = Session.getInstance(props, new Authenticator() {


            protected PasswordAuthentication getPasswordAuthentication() {

                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            //от кого
            message.setFrom(new InternetAddress(username));
            //кому
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            //Заголовок письма
            message.setSubject(subject);
            //Содержимое
            message.setText(text + "\n" + addsInfo);

            //Отправляем сообщение
            Transport.send(message);
            logger.debug("Письмо отправлено");
        } catch (MessagingException e) {
            logger.debug("Не удалось отправить письмо");
            throw new RuntimeException(e);
        }
    }

    public String getUsername() {
        return username;
    }
}

import esn.services.EmailService;
import org.junit.Test;

public class Services {

    @Test
    public void email(){
        new EmailService(null, null).send("Тема", "Привет", "softoad2@gmail.com", "softoad@yandex.ru");
        System.out.println("done");
    }
}

import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Crypto {
    @Test
    public void bcrypt(){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String adminKey = encoder.encode("Roslesinforg" + "3ff42fsf24hjgfdsesdf23fsdf");
        System.out.println(adminKey);

    }
}

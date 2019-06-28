import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Crypto {
    @Test
    public void bcrypt(){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String adminKey = encoder.encode("fucker66");
        System.out.println(adminKey);

    }
}

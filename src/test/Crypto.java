import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Crypto {
    @Test
    public void bcrypt(){
        String encoded = new BCryptPasswordEncoder().encode("yamert");
        System.out.println(encoded);
        System.out.println(encoded.length());
    }
}

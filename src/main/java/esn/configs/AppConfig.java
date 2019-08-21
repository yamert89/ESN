package esn.configs;

import esn.viewControllers.accessoryFunctions.SessionUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

@Configuration
public class AppConfig {

    @Bean
    public HttpHeaders headers(){
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Content-Type", "application/json; charset=UTF-8");
        return responseHeaders;
    }

    @Bean
    public SessionUtil sessionUtil(){
        return new SessionUtil();
    }
}

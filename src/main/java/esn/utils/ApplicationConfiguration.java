package esn.utils;

import esn.entities.Department;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfiguration {

    @Bean
    public Department getDep(){
        System.out.println("GET BEAN");
        return new Department();
    }
}

package esn.configs;

import esn.viewControllers.accessoryFunctions.MyStaticHandler;
import esn.viewControllers.accessoryFunctions.SessionUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

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

    @Bean
    WebMvcConfigurer configurer(){
        return new WebMvcConfigurer() {
            @Override
            public void addResourceHandlers(ResourceHandlerRegistry registry) {
                registry.addResourceHandler("/resources/**")
                        .addResourceLocations("/resources/")
                        .resourceChain(true)
                        .addResolver(new MyStaticHandler());
            }
        };
    }
}

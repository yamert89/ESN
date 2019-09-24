package esn.configs;

import esn.viewControllers.accessoryFunctions.MyStaticHandler;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/*@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "esn.viewControllers")*/
public class WebResourcesConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        System.out.println("___________________________REGISTRY");
        registry.addResourceHandler("/resources/**")
                .addResourceLocations("/resources/")
                .setCachePeriod(3600)
                .resourceChain(true)
                .addResolver(new MyStaticHandler());

    }
}

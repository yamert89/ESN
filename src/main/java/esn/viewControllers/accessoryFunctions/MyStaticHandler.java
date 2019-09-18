package esn.viewControllers.accessoryFunctions;

import org.springframework.core.io.Resource;
import org.springframework.web.servlet.resource.DefaultServletHttpRequestHandler;
import org.springframework.web.servlet.resource.PathResourceResolver;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class MyStaticHandler extends PathResourceResolver {

    @Override
    protected Resource getResource(String resourcePath, Resource location) throws IOException {
        String url = URLDecoder.decode(resourcePath, StandardCharsets.UTF_8.name());
        System.out.println(url);
        return super.getResource(url, location);
    }
}

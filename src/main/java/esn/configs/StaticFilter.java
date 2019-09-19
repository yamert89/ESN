package esn.configs;


import esn.utils.ImageUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class StaticFilter implements Filter {
    private final static Logger logger = LogManager.getLogger(StaticFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (request instanceof HttpServletRequest) {
            String url = ((HttpServletRequest)request).getRequestURL().toString();
            String queryString = ((HttpServletRequest)request).getQueryString();
            String url2 = URLDecoder.decode(queryString, StandardCharsets.UTF_8.name());
            logger.debug(queryString);
            logger.debug(url2);
            chain.doFilter(new HttpServletRequestWrapper((HttpServletRequest) request){
                @Override
                public String getRequestURI() {
                    return url2;
                }
            }, response);

        }

    }

    @Override
    public void destroy() {

    }


}

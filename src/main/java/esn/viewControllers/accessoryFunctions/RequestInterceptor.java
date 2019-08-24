package esn.viewControllers.accessoryFunctions;

import esn.services.LiveStat;
import esn.viewControllers.UserController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RequestInterceptor extends HandlerInterceptorAdapter {
    private final static Logger logger = LogManager.getLogger(RequestInterceptor.class);
    private LiveStat liveStat;

    @Autowired
    public void setLiveStat(LiveStat liveStat) {
        this.liveStat = liveStat;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        liveStat.request(request.getRemoteAddr());
        return System.currentTimeMillis()%2 != 0 ?
                super.preHandle(request, response, handler) :
                liveStat.canRequest(request.getRemoteAddr()) && super.preHandle(request, response, handler); //TODO не каждый запрос
    }
}

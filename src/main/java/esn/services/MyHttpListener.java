package esn.services;

import esn.configs.GeneralSettings;
import esn.db.UserDAO;
import esn.entities.Organization;
import esn.entities.Session;
import esn.entities.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;

@Component
public class MyHttpListener extends HttpSessionEventPublisher {
    private final static Logger logger = LogManager.getLogger(MyHttpListener.class);

    private WebSocketService webSocketService;
    private UserDAO userDAO;
    private LiveStat liveStat;

    @Override
    public void sessionDestroyed(HttpSessionEvent event) {
        logger.debug("SESSION DESTROYED");
        try {
            HttpSession session = event.getSession();
            if (webSocketService == null) webSocketService = (WebSocketService) getBean(WebSocketService.class, session);
            if (userDAO == null) userDAO = (UserDAO) getBean(UserDAO.class, session);
            if (liveStat == null) liveStat = (LiveStat) getBean(LiveStat.class, session);
            if (userDAO.getSession(session.getId()).isPresent()) return;
            Organization org = (Organization) session.getAttribute("org");
            if (org == null) return;
            int orgId = org.getId();
            User user = (User) session.getAttribute("user");
            liveStat.userLogout(user.getId());
            String ip = (String) session.getAttribute("ip");
            webSocketService.sendStatus(orgId, user.getId(), false);
            userDAO.saveSession(new Session(session.getId(), user, ip,
                    session.getCreationTime(), System.currentTimeMillis()));
        }catch (Exception e){
            logger.error(e.getMessage(), e);
        }
        super.sessionDestroyed(event);
    }

    private Object getBean(Class clas, HttpSession session){
        ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(session.getServletContext());
        return ctx.getBean(clas);
    }
}

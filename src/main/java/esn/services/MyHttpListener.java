package esn.services;

import esn.db.UserDAO;
import esn.entities.Organization;
import esn.entities.Session;
import esn.entities.User;
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

    private WebSocketService webSocketService;
    private UserDAO userDAO;
    private LiveStat liveStat;

    @Autowired
    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Autowired
    public void setService(WebSocketService service) {
        this.webSocketService = service;
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent event) {
        System.out.println("SESSION DESTROYED");
        try {
            HttpSession session = event.getSession();
            if (webSocketService == null) webSocketService = (WebSocketService) getBean("webSocketService", session);
            if (userDAO == null) userDAO = (UserDAO) getBean("user_dao", session);
            if (liveStat == null) liveStat = (LiveStat) getBean("live_stat;", session);
            int orgId = ((Organization) session.getAttribute("org")).getId();
            User user = (User) session.getAttribute("user");
            liveStat.userLogout(user.getId());
            String ip = (String) session.getAttribute("ip");
            webSocketService.sendStatus(orgId, user.getId(), false);
            userDAO.saveSession(new Session(session.getId(), user, ip,
                    session.getCreationTime(), System.currentTimeMillis()));
        }catch (Exception e){
            e.printStackTrace();
        }
        super.sessionDestroyed(event);
    }

    private Object getBean(String name, HttpSession session){
        ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(session.getServletContext());
        return ctx.getBean(name);
    }
}

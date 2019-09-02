package esn.viewControllers.accessoryFunctions;

import esn.configs.GeneralSettings;
import esn.db.UserDAO;
import esn.entities.Organization;
import esn.entities.User;
import esn.services.LiveStat;
import esn.services.WebSocketService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.Principal;

public class SessionUtil {

    private UserDAO userDAO;
    private LiveStat liveStat;
    private WebSocketService webSocketService;
    private final static Logger logger = LogManager.getLogger(SessionUtil.class);

    @Autowired
    public void setWebSocketService(WebSocketService webSocketService) {
        this.webSocketService = webSocketService;
    }

    @Autowired
    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Autowired
    public void setLiveStat(LiveStat liveStat) {
        this.liveStat = liveStat;
    }

    private boolean initSession(HttpServletRequest request, Principal principal){
        try {
            User user = userDAO.getUserByLogin(principal.getName());
            HttpSession session = request.getSession();
            Organization organization = user.getOrganization();
            if (organization.isDisabled()) return false;
            liveStat.userLogged(user.getId());
            session.setAttribute("user", user);
            session.setAttribute("org", organization);
            session.setAttribute("loginUrl", user.getLogin());
            session.setAttribute("ip", request.getRemoteAddr());
            webSocketService.sendStatus(organization.getId(), user.getId(), true);
            session.setMaxInactiveInterval(GeneralSettings.SESSION_TIMEOUT);
            logger.debug("SESSION TIMEOUT = " + session.getMaxInactiveInterval());
            logger.debug("SESSION started " + user.getName() + "     id:" + user.getId() + "      orid:" + organization.getId());
        }catch (Exception e){logger.error("init session", e);}
        return true;
    }

    public Organization getOrg(HttpServletRequest request, Principal principal){
        HttpSession session = request.getSession();
        if (session.getAttribute("org") == null) {
            return initSession(request, principal) ? (Organization) session.getAttribute("org") : null;
        }
        return (Organization) session.getAttribute("org");
    }

    public User getUser(HttpServletRequest request, Principal principal){
        HttpSession session = request.getSession();
        if (session.getAttribute("user") == null) initSession(request, principal);
        return (User) session.getAttribute("user");
    }
}

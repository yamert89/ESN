package esn.viewControllers.accessoryFunctions;

import esn.db.OrganizationDAO;
import esn.db.UserDAO;
import esn.entities.Organization;
import esn.entities.User;
import esn.services.LiveStat;
import esn.services.WebSocketService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.Principal;

public class SessionUtil {

    private UserDAO userDAO;
    private OrganizationDAO orgDAO;
    private LiveStat liveStat;
    private WebSocketService webSocketService;

    @Autowired
    public void setWebSocketService(WebSocketService webSocketService) {
        this.webSocketService = webSocketService;
    }

    @Autowired
    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Autowired
    public void setOrgDAO(OrganizationDAO orgDAO) {
        this.orgDAO = orgDAO;
    }

    @Autowired
    public void setLiveStat(LiveStat liveStat) {
        this.liveStat = liveStat;
    }

    public void initSession(HttpServletRequest request, Principal principal){
        User user = userDAO.getUserByLogin(principal.getName());
        HttpSession session = request.getSession();
        Organization organization = user.getOrganization();
        liveStat.userLogged(user.getId());
        session.setAttribute("user", user);
        session.setAttribute("org", organization);
        session.setAttribute("loginUrl", user.getLogin());
        session.setAttribute("ip", request.getRemoteAddr());
        webSocketService.sendStatus(organization.getId(), user.getId(), true);
    }

    public Organization getOrg(HttpServletRequest request, Principal principal){
        HttpSession session = request.getSession();
        if (session.getAttribute("org") == null) initSession(request, principal);
        return (Organization) session.getAttribute("org");
    }

    public User getUser(HttpServletRequest request, Principal principal){
        HttpSession session = request.getSession();
        if (session.getAttribute("user") == null) initSession(request, principal);
        return (User) session.getAttribute("user");
    }
}

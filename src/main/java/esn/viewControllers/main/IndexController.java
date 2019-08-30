package esn.viewControllers.main;

import esn.entities.Organization;
import esn.entities.User;
import esn.services.EmailService;
import esn.services.LiveStat;
import esn.viewControllers.accessoryFunctions.SessionUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Controller
public class IndexController {
    private final static Logger logger = LogManager.getLogger(IndexController.class);

    private LiveStat liveStat;
    private EmailService emailService;
    private HttpHeaders headers;
    private SessionUtil sessionUtil;

    @Autowired
    public void setSessionUtil(SessionUtil sessionUtil) {
        this.sessionUtil = sessionUtil;
    }

    @Autowired
    public void setHeaders(HttpHeaders headers) {
        this.headers = headers;
    }

    @Autowired
    @Qualifier("adminEmailService")
    public void setEmailService(EmailService emailService) {
        this.emailService = emailService;
    }

    @Autowired
    public void setLiveStat(LiveStat liveStat) {
        this.liveStat = liveStat;
    }

    @GetMapping("/{organization}/contacts")
    @ResponseBody
    public ResponseEntity<String> fillContactsList(HttpSession session, HttpServletRequest request, Principal principal){

        ResponseEntity.BodyBuilder bb = ResponseEntity.ok().headers(headers);
        JSONArray js = new JSONArray();
        try {
            User user = sessionUtil.getUser(request, principal);
            Organization org = sessionUtil.getOrg(request, principal);
            if (user.getGroups().size() == 0) {
                /* Organization org = orgDAO.getOrgByURLWithEmployers(organization);*/
                org = (Organization) session.getAttribute("org");
                Set<User> users = new HashSet<>(org.getAllEmployers());
                users.remove(user);
                User def = new User();
                def.setLogin("deleted");
                users.remove(def);

                JSONObject jsOb = new JSONObject();
                JSONArray usrs = new JSONArray();
                users.forEach(u -> {
                    JSONObject us = new JSONObject();
                    us.put("name", u.getShortName())
                            .put("status", liveStat.userIsOnline(u.getId()))
                            .put("id", u.getId())
                            .put("login", u.getLogin());
                    usrs.put(us);
                });
                jsOb.put("name", "Все").put("users", usrs).put("expanded", true);
                return bb.body(jsOb.toString());
            }
            long start = System.currentTimeMillis();


            Organization finalOrg = org;
            user.getGroups().forEach(g -> {
                JSONObject group = new JSONObject();
                JSONArray usrs = new JSONArray();
                Arrays.stream(g.getPersonIds()).forEach(id -> {
                    User u = finalOrg.getEmployerById(id);
                    JSONObject us = new JSONObject();
                    us.put("name", u.getShortName())
                            .put("status", liveStat.userIsOnline(u.getId()))
                            .put("id", u.getId())
                            .put("login", u.getLogin());
                    usrs.put(us);
                });

                group.put("name", g.getName())
                        .put("expanded", g.isExpandable())
                        .put("users", usrs);
                js.put(group);
            });

            logger.debug("BENCHMARK STREAM_1 : " + String.valueOf(System.currentTimeMillis() - start));
            logger.debug(user);
        }catch (Exception e){
            logger.error("fillContactsList", e);
        }

        return bb.body(js.toString());
    }

    @GetMapping("/{organization}/calendar")
    public String calendar(HttpServletRequest request, Principal principal){
        User user = sessionUtil.getUser(request, principal);
        logger.debug(user.getName() + "get calendar");
        return "calendar";
    }

    @RequestMapping("/favicon.ico")
    public String getFavicon(){
        return "forward:/resources/data/app/favicon.ico";
    }

    @PostMapping("/problem")
    @ResponseBody
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void problem(@RequestParam String message, HttpServletRequest request, Principal principal){
        User user = sessionUtil.getUser(request, principal);
        Organization org = sessionUtil.getOrg(request, principal);
        emailService.send("Problem", message, "User: " + user + "\n org: " + org.getUrlName(), null);
    }

}

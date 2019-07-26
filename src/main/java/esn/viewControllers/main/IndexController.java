package esn.viewControllers.main;

import esn.db.DepartmentDAO;
import esn.db.GlobalDAO;
import esn.db.OrganizationDAO;
import esn.db.UserDAO;
import esn.db.message.GenDAO;
import esn.db.message.PrivateDAO;
import esn.db.message.WallDAO;
import esn.entities.Organization;
import esn.entities.User;
import esn.services.LiveStat;
import esn.services.WebSocketService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Controller
public class IndexController {


    private OrganizationDAO orgDAO;
    private UserDAO userDAO;
    private LiveStat liveStat;

    @Autowired
    public void setLiveStat(LiveStat liveStat) {
        this.liveStat = liveStat;
    }

    @Autowired
    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }


    @Autowired
    public void setOrgDAO(OrganizationDAO orgDAO) {
        this.orgDAO = orgDAO;
    }

    @GetMapping("/{organization}/contacts")
    @ResponseBody
    public ResponseEntity<String> fullContactsList(@PathVariable String organization, HttpSession session){
        User user = (User) session.getAttribute("user");
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Content-Type",
                "application/json; charset=UTF-8");
        ResponseEntity.BodyBuilder bb = ResponseEntity.ok().headers(responseHeaders);

        if (user.getGroups().size() == 0){
            Organization org = orgDAO.getOrgByURLWithEmployers(organization);
            Set<User> users = new HashSet<>(org.getAllEmployers());
            users.remove(user);

            JSONObject js = new JSONObject();
            JSONArray usrs = new JSONArray();
            users.forEach(u -> {
                JSONObject us = new JSONObject();
                us.put("name", u.getName())
                        .put("status", liveStat.userIsOnline(u.getId()))
                        .put("id", u.getId())
                        .put("login", u.getLogin());
                usrs.put(us);
            });
            js.put("name", "Все").put("users", usrs).put("expanded", true);
            return bb.body(js.toString());
        }
        long start = System.currentTimeMillis();
        JSONArray js = new JSONArray();

        user.getGroups().forEach(g -> {
            JSONObject group = new JSONObject();
            JSONArray usrs = new JSONArray();
            Arrays.stream(g.getPersonIds()).forEach(id -> {
                User u = userDAO.getUserById(id);
                JSONObject us = new JSONObject();
                us.put("name", u.getName())
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

        System.out.println("BENCHMARK STREAM_1 : " + String.valueOf(System.currentTimeMillis() - start));
        System.out.println(user);

        return bb.body(js.toString());
    }

    @GetMapping("/calendar")
    public String calendar(){
        return "calendar";
    }

    @RequestMapping("/favicon.ico")
    public String getFavicon(){
        return "forward:/resources/favicon.ico";
    }

}
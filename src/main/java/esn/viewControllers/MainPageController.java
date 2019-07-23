package esn.viewControllers;

import esn.configs.GeneralSettings;
import esn.db.GlobalDAO;
import esn.db.OrganizationDAO;
import esn.db.UserDAO;
import esn.db.message.GenDAO;
import esn.db.message.PrivateDAO;
import esn.db.message.WallDAO;
import esn.entities.Organization;
import esn.entities.User;
import esn.entities.secondary.*;
import org.hibernate.LazyInitializationException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/{organization}")
@SessionAttributes(types = Organization.class)
public class MainPageController {

    private WallDAO wallDAO;
    private GenDAO genDAO;
    private PrivateDAO privateDAO;
    private GlobalDAO globalDAO;
    private UserDAO userDAO;
    private OrganizationDAO orgDao;

    @Autowired
    public void setGlobalDAO(GlobalDAO globalDAO) {
        this.globalDAO = globalDAO;
    }
    @Autowired
    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }
    @Autowired
    public void setOrgDao(OrganizationDAO orgDao) {
        this.orgDao = orgDao;
    }
    @Autowired
    public void setWallDAO(WallDAO wallDAO) {
        this.wallDAO = wallDAO;
    }
    @Autowired
    public void setGenDAO(GenDAO genDAO) {
        this.genDAO = genDAO;
    }
    @Autowired
    public void setPrivateDAO(PrivateDAO privateDAO) {
        this.privateDAO = privateDAO;
    }

    @GetMapping(value = "/wall")
    public String wall(Model model, HttpSession session){
        Organization org = (Organization) session.getAttribute("org");
        int orgId = org.getId();
        List<AbstractMessage> messages = wallDAO.getMessages(orgId, -1);
        long newIdx = messages.size() < GeneralSettings.AMOUNT_WALL_MESSAGES ? -1 : messages.get(messages.size() - 1).getId();
        session.setAttribute("lastIdx_wall", newIdx);
        model.addAttribute("messages", messages);
        return "wall";
    }

    @GetMapping("/calendar")
    public String calendar(){
        return "calendar";
    }

    @GetMapping("/chat")
    public String genChat(Model model, HttpSession session){
        Organization org = (Organization) session.getAttribute("org");
        int orgId = org.getId();
        User user = (User) session.getAttribute("user");
        model.addAttribute("photo", user.getPhoto_small());
        List<AbstractMessage> messages = genDAO.getMessages(orgId, -1);
        long newIdx = messages.size() < GeneralSettings.AMOUNT_GENCHAT_MESSAGES ? -1 : messages.get(messages.size() - 1).getId();
        session.setAttribute("lastIdx_genchat", newIdx);
        model.addAttribute("messages", messages);
        return "gen_chat";
    }

    @GetMapping("/private-chat/{companion}")
    public String privateChat(@PathVariable String organization,
                              @PathVariable String companion, Model model, HttpSession session){
        Organization org = (Organization) session.getAttribute("org");
        int orgId = org.getId();
        User user = (User) session.getAttribute("user");
        User compan = orgDao.getOrgByURL(organization).getUserByLogin(companion);
        model.addAttribute("companion", compan);
        Set<PrivateChatMessage> privateMessages = privateDAO.getMessages(user, compan, orgId);

        Map<PrivateChatMessage, Boolean> messages = new TreeMap<>();
        if (privateMessages == null) {
            model.addAttribute("messages", null);
            return "private_chat";
        }
        Long[] ids = new Long[privateMessages.size()];
        int i = 0;
        for (PrivateChatMessage mes :
                privateMessages) {
            Boolean userMessage = mes.getSender_id() == user.getId();
            if (!userMessage) {
                mes.setReaded(true);
                ids[i++] = mes.getId();
            }
            messages.put(mes, userMessage);

        }
        privateDAO.updateReadedMessages(ids);

        model.addAttribute("messages", messages);
        return "private_chat";
    }

    @GetMapping("/groups")
    public String groups(@PathVariable String organization, Model model, HttpSession session){
        User user = (User) session.getAttribute("user");
        Set<User> employers = new HashSet<>(orgDao.getOrgByURLWithEmployers(organization).getAllEmployers());
        employers.remove(user);
        model.addAttribute("employers", employers);
        Set<ContactGroup> groups = user.getGroups();
        /*Set<String> groupNames = new HashSet<>();
        for (ContactGroup group :
                groups) {
            groupNames.add(group.getName());
        }*/
        Set<String> groupNames = user.getGroups()
                .stream()
                .map(ContactGroup::getName)
                .collect(Collectors.toSet());

        model.addAttribute("groupsNames", groupNames);
        Map<String, Set<User>> resMap = new HashMap<>();
        for (ContactGroup group : groups){
            Set<User> resVal = new HashSet<>(groups.size());
            int [] ids = group.getPersonIds();
            for (int id : ids) {
                User u = userDAO.getUserById(id);
                resVal.add(u);
            }
            resMap.put(group.getName(), resVal);

        }
        model.addAttribute("groups", resMap);
        System.out.println(user);

        return "groups";
    }

    @GetMapping(value = "/notes")
    public String notes(HttpSession session){
        User user = (User) session.getAttribute("user");
        System.out.println(" /notes Main   " + user);
        session.setAttribute("user", userDAO.getUserById(user.getId()));
        return "notes";
    }

    @GetMapping(value = "/staff")
    public String staff(HttpSession session){
        User user = (User) session.getAttribute("user");
        return user.getAuthority().equals("ROLE_ADMIN") ? "staff_admin" : "staff";
    }

    @GetMapping(value = "/storage")
    public String storage(/*@SessionAttribute User user*/ Model model, SessionStatus status, HttpSession session){
        User user = (User) session.getAttribute("user");
        status.setComplete();
        try{
            user.getStoredFiles().size();
        }catch (LazyInitializationException e){
            user = userDAO.getUserWithFiles(user.getId());
        }
        model.addAttribute("sharedFiles", globalDAO.getSharedFiles(user.getOrganization()));
        model.addAttribute("filesPath", "/resources/data/" + user.getOrganization().getUrlName() + "/stored_files/" + user.getLogin() + "/");

        model.addAttribute("user", user);
        session.setAttribute("user", user);

        return "storage";
    }



    @GetMapping("/contacts")
    @ResponseBody
    public ResponseEntity<String> fullContactsList(@PathVariable String organization, HttpSession session){
        User user = (User) session.getAttribute("user");
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Content-Type",
                "application/json; charset=UTF-8");
        ResponseEntity.BodyBuilder bb = ResponseEntity.ok().headers(responseHeaders);

        if (user.getGroups().size() == 0){
            Organization org = orgDao.getOrgByURLWithEmployers(organization);
            Set<User> users = new HashSet<>(org.getAllEmployers());
            users.remove(user);

            JSONObject js = new JSONObject();
            JSONArray usrs = new JSONArray();
            users.forEach(u -> {
                JSONObject us = new JSONObject();
                us.put("name", u.getName())
                        .put("status", u.netStatus())
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
                        .put("status", u.netStatus())
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

    @RequestMapping("/favicon.ico")
    public String getFavicon(){
        return "forward:/resources/favicon.ico";
    }

    /*@ModelAttribute
    public User getUserWithFiles(int id){
        return userDAO.getUserWithFiles(id);
    }*/


}

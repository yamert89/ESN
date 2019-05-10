package esn.viewControllers;

import esn.configs.GeneralSettings;
import esn.db.GlobalDAO;
import esn.db.OrganizationDAO;
import esn.db.MessagesDAO;
import esn.db.UserDAO;
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

import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/{organization}")
//@SessionAttributes
public class MainPageController {



    private MessagesDAO messagesDAO;
    private GlobalDAO globalDAO;
    private UserDAO userDAO;
    private OrganizationDAO orgDao;



    @Autowired
    public void setMessagesDAO(MessagesDAO messagesDAO) {
        this.messagesDAO = messagesDAO;
    }
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

    @GetMapping(value = "/wall")
    public String wall(Model model, @SessionAttribute int orgId, HttpSession session){
        List<AbstractMessage> messages = messagesDAO.getMessages(orgId, -1, Post.class);
        int newIdx = messages.size() < GeneralSettings.AMOUNT_WALL_MESSAGES ? -1 : messages.get(messages.size() - 1).getId();
        session.setAttribute("lastIdx_wall", newIdx);
        model.addAttribute("messages", messages);
        return "wall";
    }

    @GetMapping("/calendar")
    public String calendar(){
        return "calendar";
    }

    @GetMapping("/chat")
    public String genChat(Model model, @SessionAttribute int orgId, @SessionAttribute User user, HttpSession session){
        model.addAttribute("photo", user.getPhoto_small());
        List<AbstractMessage> messages = messagesDAO.getMessages(orgId, -1, GenChatMessage.class);
        int newIdx = messages.size() < GeneralSettings.AMOUNT_GENCHAT_MESSAGES ? -1 : messages.get(messages.size() - 1).getId();
        session.setAttribute("lastIdx_genchat", newIdx);
        model.addAttribute("messages", messages);
        return "gen_chat";
    }

    @GetMapping("/private-chat") //TODO url = login ?
    public String privateChat(@PathVariable String organization,
                              @RequestParam String companion, Model model, @SessionAttribute User user, @SessionAttribute int orgId){
        User compan = orgDao.getOrgByURL(organization).getUserByLogin(companion);
        model.addAttribute("companion", compan);
        Set<PrivateChatMessage> privateMessages = messagesDAO.getMessages(user, compan, orgId);
        Map<PrivateChatMessage, Boolean> messages = new TreeMap<>();
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
        messagesDAO.updateReadedMessages(ids);

        model.addAttribute("messages", messages);
        return "private_chat";
    }

    @GetMapping("/groups")
    public String groups(@PathVariable String organization, Model model, @SessionAttribute User user){
        Set<User> employers = new HashSet<>(orgDao.getOrgByURL(organization).getAllEmployers());
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
    public String notes(HttpSession session, @SessionAttribute User user){
        System.out.println(" /notes Main   " + user);
        session.setAttribute("user", userDAO.getUserById(user.getId()));
        return "notes";
    }

    @GetMapping(value = "/staff")
    public String staff(@SessionAttribute User user){
        return user.isAdmin() ? "staff_admin" : "staff";
    }

    @GetMapping(value = "/storage")
    public String storage(@SessionAttribute User user, Model model, HttpSession session){
        try{
            user.getStoredFiles().size();
        }catch (LazyInitializationException e){
            user = userDAO.getUserWithFiles(user.getId());
        }
        model.addAttribute("sharedFiles", globalDAO.getSharedFiles());
        session.setAttribute("user", user);
        return "storage";
    }



    @GetMapping("/contacts")
    @ResponseBody
    public ResponseEntity<String> fullContactsList(@PathVariable String organization, @SessionAttribute User user){
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Content-Type",
                "application/json; charset=UTF-8");
        ResponseEntity.BodyBuilder bb = ResponseEntity.ok().headers(responseHeaders);

        if (user.getGroups().size() == 0){
            Organization org = orgDao.getOrgByURL(organization);
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
            js.put("name", "Все").put("users", usrs);
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


}

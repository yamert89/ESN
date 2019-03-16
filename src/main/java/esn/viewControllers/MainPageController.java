package esn.viewControllers;

import esn.db.GlobalDAO;
import esn.db.OrganizationDAO;
import esn.db.PrivateChatMessageDAO;
import esn.db.UserDAO;
import esn.entities.Organization;
import esn.entities.User;
import esn.entities.secondary.GenChatMessage;
import esn.entities.secondary.Post;
import esn.entities.secondary.PrivateChatMessage;
import org.hibernate.LazyInitializationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
@RequestMapping("/{organization}")
@SessionAttributes
public class MainPageController {



    private PrivateChatMessageDAO privateChatMessageDAO;
    private GlobalDAO globalDAO;
    private UserDAO userDAO;
    private OrganizationDAO orgDao;



    @Autowired
    public void setPrivateChatMessageDAO(PrivateChatMessageDAO privateChatMessageDAO) {
        this.privateChatMessageDAO = privateChatMessageDAO;
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
    public String wall(Model model, @PathVariable String organization){
        model.addAttribute("messages", globalDAO.getMessages(organization, Post.class));
        return "wall";
    }

    @GetMapping("/calendar")
    public String calendar(){
        return "calendar";
    }

    @GetMapping("/chat")
    public String genChat(Model model, @PathVariable String organization, @SessionAttribute User user){
       // globalDAO.saveMessage(43,"ПРивет", new Timestamp(1234443354542L), "rosles", GenChatMessage.class);
        model.addAttribute("photo", user.getPhoto_small());
        model.addAttribute("messages", globalDAO.getMessages(organization, GenChatMessage.class));
        return "gen_chat";
    }

    @GetMapping("/private-chat") //TODO url = login
    public String privateChat(@PathVariable String organization,
                              @RequestParam String companion, Model model, @SessionAttribute User user, @SessionAttribute int orgId){
        User compan = orgDao.getOrgByURL(organization).getUserByLogin(companion);
        model.addAttribute("companion", compan);
        Set<PrivateChatMessage> privateMessages = privateChatMessageDAO.getMessages(user, compan, orgId);
        Map<PrivateChatMessage, Boolean> messages = new TreeMap<>();
        for (PrivateChatMessage mes :
                privateMessages) {
            Boolean userMessage = mes.getSender_id() == user.getId();
            messages.put(mes, userMessage);
        }

        model.addAttribute("messages", messages);
        return "private_chat";
    }

    @GetMapping("/groups")
    public String groups(@PathVariable String organization, Model model, @SessionAttribute User user){
        Set<User> employers = orgDao.getOrgByURL(organization).getAllEmployers();
        model.addAttribute("employers", employers);
        model.addAttribute("groupsNames", user.getGroups().keySet());
        Map<String, Set<User>> resMap = new HashMap<>();
        for (Map.Entry<String, String[]> entry: user.getGroups().entrySet()){
            int len = entry.getValue().length;
            Set<User> resVal = new HashSet<>(len);
            for (int i = 0; i < len; i++) {
                User u = userDAO.getUserById(Integer.valueOf(entry.getValue()[i]));
                resVal.add(u);
            }
            resMap.put(entry.getKey(), resVal);

        }
        model.addAttribute("groups", resMap);

        return "groups";
    }

    @GetMapping(value = "/notes")
    public String notes(HttpSession session, Model model, @SessionAttribute User user){
        User user2 = userDAO.getUserWithNotes(user.getId());
        session.setAttribute("user", user2);
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
        StringBuilder json = new StringBuilder("[");

        if (user.getGroups().size() == 0){
            Organization org = orgDao.getOrgByURL(organization);
            Set<User> users = org.getAllEmployers();
            json.append("{").append("\"name\":\"Все\",\"users\":[");
            for (User u :
                    users) {
                if (u.getId() == user.getId()) continue;
                json.append("{\"name\":\"").append(u.getName()).append("\",\"status\":").append(u.netStatus())
                        .append(",\"id\":").append(u.getId()).append(",\"login\":\"").append(u.getLogin()).append("\"},");
            }
            json.append("]}]");
            return bb.body(json.toString().replaceAll(",]", "]"));
        }
        for (Map.Entry<String, String[]> entry: user.getGroups().entrySet()){
            int len = entry.getValue().length;

            json.append("{").append("\"name\":\"").append(entry.getKey()).append("\",\"users\":[");

            for (int i = 0; i < len; i++) {
                User u = userDAO.getUserById(Integer.valueOf(entry.getValue()[i]));
                json.append("{\"name\":\"").append(u.getName()).append("\",\"status\":").append(u.netStatus())
                        .append(",\"id\":").append(u.getId()).append(",\"login\":\"").append(u.getLogin()).append("\"},");
            }
            json.append("]},");
        }
        json.append("]");


        return bb.body(json.toString().replaceAll(",]", "]"));
    }


}

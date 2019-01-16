package esn.viewControllers;

import esn.db.GlobalDAO;
import esn.db.OrganizationDAO;
import esn.db.PrivateChatMessageDAO;
import esn.db.UserDAO;
import esn.entities.GenChatMessage;
import esn.entities.Post;
import esn.entities.PrivateChatMessage;
import esn.entities.User;
import esn.utils.GeneralSettings;
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
    public String wall(Model model, @PathVariable String organization, HttpSession session){
        model.addAttribute("messages", globalDAO.getMessages(organization, Post.class));

        return "wall";
    }

    @RequestMapping(value = "/calendar")
    public String calendar(){
        return "calendar";
    }

    @RequestMapping(value = "/chat/{user}")
    public String genChat(@PathVariable String user, Model model, @PathVariable String organization){
        User usr = orgDao.getOrgByURL(organization).getUserByLogin(user);
       // globalDAO.saveMessage(43,"ПРивет", new Timestamp(1234443354542L), "rosles", GenChatMessage.class);
        model.addAttribute("photo", usr.getPhoto_small());
        model.addAttribute("messages", globalDAO.getMessages(organization, GenChatMessage.class));
        return "gen_chat";
    }

    @RequestMapping(value = "/private-chat/{user}")
    public String privateChat(@PathVariable String user, @PathVariable String organization,
                              @RequestParam(value = "companion") String companion, Model model){
        User usr = orgDao.getOrgByURL(organization).getUserByLogin(user);
        User compan = orgDao.getOrgByURL(organization).getUserByLogin(companion);
        model.addAttribute("net_status", compan.netStatus());
        model.addAttribute("companion_name", compan.getName());
        model.addAttribute("companion_avatar", GeneralSettings.AVATAR_PATH.concat(compan.getPhoto()));
        Set<PrivateChatMessage> privateMessages = privateChatMessageDAO.getMessages(usr, compan);
        Map<String, Boolean> messages = new TreeMap<>();
        for (PrivateChatMessage mes :
                privateMessages) {
            Boolean userMessage = mes.getSender() == usr;
            messages.put(mes.getText(), userMessage);
        }

        model.addAttribute("messages", messages);
        return "private_chat";
    }

    @GetMapping("/groups/{user}")
    public String groups(@PathVariable String user, @PathVariable String organization, Model model, HttpSession session){
        Set<User> employers = orgDao.getOrgByURL(organization).getAllEmployers();
        model.addAttribute("employers", employers);
        User user1 = (User) session.getAttribute("user");
        model.addAttribute("groupsNames", user1.getGroups().keySet());
        Map<String, Set<User>> resMap = new HashMap<>();
        for (Map.Entry<String, String[]> entry: user1.getGroups().entrySet()){
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

    @RequestMapping(value = "/notes")
    public String notes(){
        return "notes";
    }

    @RequestMapping(value = "/staff")
    public String staff(){
        return "staff";
    }

    @RequestMapping(value = "/storage")
    public String storage(){
        return "storage";
    }


    @GetMapping(value = "/contacts")
    @ResponseBody
    public ResponseEntity<String> fullContactsList(HttpSession session){
        User user1 = (User) session.getAttribute("user");
        StringBuilder json = new StringBuilder("[");
        for (Map.Entry<String, String[]> entry: user1.getGroups().entrySet()){
            int len = entry.getValue().length;

            json.append("{").append("\"name\":\"").append(entry.getKey()).append("\",\"users\":[");

            for (int i = 0; i < len; i++) {
                User u = userDAO.getUserById(Integer.valueOf(entry.getValue()[i]));
                json.append("{\"name\":\"").append(u.getName()).append("\",\"status\":").append(u.netStatus()).append("},");
            }
            json.append("]},");
        }
        json.append("]");
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Content-Type",
                "application/json; charset=UTF-8");

        return ResponseEntity.ok()
                .headers(responseHeaders)
                .body(json.toString().replaceAll(",]", "]"));
    }


}

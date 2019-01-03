package esn.viewControllers;

import esn.db.GlobalDAO;
import esn.db.OrganizationDAO;
import esn.db.PrivateChatMessageDAO;
import esn.db.UserDAO;
import esn.entities.GenChatMessage;
import esn.entities.PrivateChatMessage;
import esn.entities.User;
import esn.utils.GeneralSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

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

    @RequestMapping(value = "/groups")
    public String groups(){
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


}

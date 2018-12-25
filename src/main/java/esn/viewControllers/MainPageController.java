package esn.viewControllers;

import esn.db.GlobalDAO;
import esn.db.PrivateChatMessageDAO;
import esn.entities.Organization;
import esn.entities.PrivateChatMessage;
import esn.entities.User;
import esn.utils.GeneralSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

@Controller
@RequestMapping("/{organization}")
@SessionAttributes
public class MainPageController {



    private PrivateChatMessageDAO privateChatMessageDAO;
    private GlobalDAO globalDAO;

    @Autowired
    public void setPrivateChatMessageDAO(PrivateChatMessageDAO privateChatMessageDAO) {
        this.privateChatMessageDAO = privateChatMessageDAO;
    }
    @Autowired
    public void setGlobalDAO(GlobalDAO globalDAO) {
        this.globalDAO = globalDAO;
    }




    private Organization org; //TODO inject



    @GetMapping(value = "/wall")
    public String wall(@RequestParam String userId, Model model, @PathVariable String organization){
        model.addAttribute("userId", userId);
        model.addAttribute("organization", organization);
        return "wall";
    }

    @RequestMapping(value = "/calendar")
    public String calendar(){
        return "calendar";
    }

    @RequestMapping(value = "/chat/{user}")
    public String genChat(@PathVariable String user, Model model){
        User usr = org.getUserByLogin(user);
        model.addAttribute("name", usr.getName());
        model.addAttribute("photo", usr.getPhoto_small());
        model.addAttribute("messages", globalDAO.getGenMessages());
        return "gen_chat";
    }

    @RequestMapping(value = "/private-chat/{user}")
    public String privateChat(@PathVariable String user,
                              @RequestParam(value = "companion") String companion, Model model){
        User usr = org.getUserByLogin(user);
        User compan = org.getUserByLogin(companion);
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

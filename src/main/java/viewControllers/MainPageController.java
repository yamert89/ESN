package viewControllers;

import db.PrivateChatMessageDAO;
import entities.Organization;
import entities.PrivateChatMessage;
import entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

@Controller
@RequestMapping("/index")
@SessionAttributes("index")
public class MainPageController {



    private PrivateChatMessageDAO privateChatMessageDAO;

    @Autowired
    public void setPrivateChatMessageDAO(PrivateChatMessageDAO privateChatMessageDAO) {
        this.privateChatMessageDAO = privateChatMessageDAO;
    }

    private Organization org; //TODO inject



    @RequestMapping(value = "/wall/{user}", method = RequestMethod.GET)
    public String wall(@PathVariable String user, Model model){
        return "wall";
    }

    @RequestMapping(value = "/calendar")
    public String calendar(){
        return "calendar";
    }

    @RequestMapping(value = "/chat")
    public String genChat(){
        return "gen_chat";
    }

    @RequestMapping(value = "/private-chat/{user}")
    public String privateChat(@PathVariable String user,
                              @RequestParam(value = "companion") String companion, Model model){
        User usr = org.getUserByNickName(user);
        User compan = org.getUserByNickName(companion);
        model.addAttribute("net_status", compan.netStatus());
        model.addAttribute("companion_name", compan.getName());
        model.addAttribute("companion_avatar", compan.getPhoto()); //TODO photo url
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

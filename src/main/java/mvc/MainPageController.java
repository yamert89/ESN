package mvc;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@RequestMapping("/index")
@SessionAttributes("index")
public class MainPageController {

    @RequestMapping(value = "/wall", method = RequestMethod.GET)
    public String wall(Model model){
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

    @RequestMapping(value = "/private-chat")
    public String privateChat(){
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

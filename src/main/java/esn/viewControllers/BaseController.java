package esn.viewControllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class BaseController {

    @RequestMapping(value = "/")
    public String start(){
        //TODO auth

        return "redirect:/wall/";
    }

    @RequestMapping(value = "/savemessage", method = RequestMethod.POST)
    public void saveMessage(){

    }
}

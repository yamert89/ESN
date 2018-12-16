package esn.viewControllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class BaseController {

    @RequestMapping(value = "/")
    public String start(){
        //TODO auth

        return "redirect:/wall/";
    }
}

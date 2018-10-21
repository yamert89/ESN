package mvc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AuthController {

    @RequestMapping({"/", "auth"})
    public String showAuthPage(){
        return "auth";
    }
}

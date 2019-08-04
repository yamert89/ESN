package esn.viewControllers;

import esn.configs.GeneralSettings;
import esn.services.EmailService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class WebSiteController {

    @GetMapping(value = "/")
    public String index(){
        return "index";
    }

    @GetMapping("/notice_cloud")
    public String noticeC(){
        return "notice_cloud";
    }

    @GetMapping("/notice_local")
    public String noticeL(){
        return "notice_local";
    }

    @PostMapping("/question")
    @ResponseBody
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void email(@RequestParam String message, @RequestParam String email){
        new EmailService().send("Question", message, email, GeneralSettings.ADMIN_EMAIL);
    }

}

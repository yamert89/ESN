package esn.viewControllers;

import esn.configs.GeneralSettings;
import esn.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class WebSiteController {

    private EmailService emailService;

    @Autowired
    @Qualifier("adminEmailService")
    public void setEmailService(EmailService emailService) {
        this.emailService = emailService;
    }

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
        emailService.send("Question", message, email, GeneralSettings.ADMIN_EMAIL);
    }

}

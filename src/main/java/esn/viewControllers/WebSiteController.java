package esn.viewControllers;

import esn.configs.GeneralSettings;
import esn.db.StatDao;
import esn.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
public class WebSiteController {

    private EmailService emailService;
    private StatDao statDao;

    @Autowired
    public void setStatDao(StatDao statDao) {
        this.statDao = statDao;
    }

    @Autowired
    @Qualifier("adminEmailService")
    public void setEmailService(EmailService emailService) {
        this.emailService = emailService;
    }

    @GetMapping(value = "/")
    public String index(HttpServletRequest request){
        statDao.stat("/", request.getRemoteHost());
        return "index";
    }

    @GetMapping("/notice_cloud")
    public String noticeC(HttpServletRequest request){
        statDao.stat("/notice_cloud", request.getRemoteHost());
        return "notice_cloud";
    }

    @GetMapping("/notice_local")
    public String noticeL(HttpServletRequest request){
        statDao.stat("/notice_local", request.getRemoteHost());
        return "notice_local";
    }

    @PostMapping("/question")
    @ResponseBody
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void email(@RequestParam String message, @RequestParam String email){
        emailService.send("Question", message, email, GeneralSettings.ADMIN_EMAIL);
    }

}

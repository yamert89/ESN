package esn.viewControllers;

import esn.db.GlobalDAO;
import esn.db.PrivateChatMessageDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.sql.Timestamp;

@Controller
public class BaseController {

    private GlobalDAO globalDAO;

    @Autowired
    public void setGlobalDAO(GlobalDAO globalDAO) {
        this.globalDAO = globalDAO;
    }

    @GetMapping(value = "/")
    public String start(){
        //TODO get cookies
       return "redirect:/user/auth";
    }





    @PostMapping(value = "/savemessage")
    public void saveMessage(@RequestParam String userId, @RequestParam String text,
                            @RequestParam String time){
        globalDAO.saveGenMessage(text, Long.valueOf(userId), Timestamp.valueOf(time));
    }
}

package esn.viewControllers;

import esn.db.GlobalDAO;
import esn.db.PrivateChatMessageDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.sql.Timestamp;

@Controller
public class BaseController {

    private GlobalDAO globalDAO;

    @Autowired
    public void setGlobalDAO(GlobalDAO globalDAO) {
        this.globalDAO = globalDAO;
    }

    @RequestMapping(value = "/")
    public ModelAndView start(ModelMap model){
        //TODO auth
        long userId = 0; //TODO get userId
        model.addAttribute("userId", userId);
        return new ModelAndView("redirect:/wall/", model);
    }

    @PostMapping(value = "/savemessage")
    public void saveMessage(@RequestParam String userId, @RequestParam String text,
                            @RequestParam String time){
        globalDAO.saveGenMessage(text, Long.valueOf(userId), Timestamp.valueOf(time));
    }
}

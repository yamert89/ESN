package esn.viewControllers;

import esn.db.GlobalDAO;
import esn.db.OrganizationDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;

@Controller
@SessionAttributes
public class BaseController {

    private GlobalDAO globalDAO;
    private OrganizationDAO orgDAO;

    @Autowired
    public void setGlobalDAO(GlobalDAO globalDAO) {
        this.globalDAO = globalDAO;
    }
    @Autowired
    public void setOrgDAO(OrganizationDAO orgDAO) {
        this.orgDAO = orgDAO;
    }

    @GetMapping(value = "/{organization}")
    public String start(@PathVariable String organization){

        //TODO get cookies
       return "redirect:/" + organization + "/auth";
    }





    @PostMapping(value = "/savemessage")
    public void saveMessage(@RequestParam String userId, @RequestParam String text,
                            @RequestParam String time){
        globalDAO.saveGenMessage(text, Long.valueOf(userId), Timestamp.valueOf(time));
    }
}

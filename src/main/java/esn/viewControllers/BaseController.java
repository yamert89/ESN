package esn.viewControllers;

import esn.db.GlobalDAO;
import esn.db.OrganizationDAO;
import esn.db.UserDAO;
import esn.entities.GenChatMessage;
import esn.entities.Post;
import esn.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Controller
@SessionAttributes
public class BaseController {
    private static final String TIME_PATTERN = "dd.MM.yyyy, HH:mm:ss"; //TODO settings

    private GlobalDAO globalDAO;
    private OrganizationDAO orgDAO;
    private UserDAO userDAO;

    @Autowired
    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

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
       return "redirect:/" + organization + "/auth1";
    }





    @PostMapping("/savemessage")
    public void saveMessage(@RequestParam String userId, @RequestParam String text,
                            @RequestParam String time, @RequestParam String orgUrl){
        Timestamp timestamp = Timestamp.valueOf(LocalDateTime.parse(time, DateTimeFormatter.ofPattern(TIME_PATTERN)));   //TODO ошибка 500. вернуть статус
        globalDAO.saveMessage(Integer.valueOf(userId), text, timestamp, orgUrl, GenChatMessage.class);
    }

    @PostMapping("/savepost")
    public void savePost(@RequestParam String userId, @RequestParam String text,
                            @RequestParam String time, @RequestParam String orgUrl){

        Timestamp timestamp = Timestamp.valueOf(LocalDateTime.parse(time, DateTimeFormatter.ofPattern(TIME_PATTERN)));
        globalDAO.saveMessage(Integer.valueOf(userId), text, timestamp, orgUrl, Post.class);
    }

    @PostMapping("/savegroup")
    public void saveGroup(@RequestParam String groupName, @RequestParam String personIds,
                           HttpSession session){
        try {
            User user = (User) session.getAttribute("user");

            user.getGroups().put(groupName, personIds.split(","));

            for (Map.Entry<String, String[]> entry: user.getGroups().entrySet()) {
                System.out.println("группа : " + entry.getKey());
                for (String s :
                        entry.getValue()) {
                    System.out.print("значения : " + s + " ");
                }
                System.out.println();
            }
            userDAO.updateUser(user); //TODO обновить при выходе?
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

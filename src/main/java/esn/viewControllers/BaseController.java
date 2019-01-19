package esn.viewControllers;

import esn.db.GlobalDAO;
import esn.db.OrganizationDAO;
import esn.db.UserDAO;
import esn.entities.User;
import esn.entities.secondary.GenChatMessage;
import esn.entities.secondary.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import static esn.utils.GeneralSettings.TIME_PATTERN;

@Controller
@SessionAttributes
public class BaseController {

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
        //TODO проверка на сущестование орг юрл

        //TODO get cookies
       return "redirect:/" + organization + "/auth1";
    }





    @PostMapping("/savemessage")
    @ResponseBody
    public void saveMessage(@RequestParam String userId, @RequestParam String text,
                            @RequestParam String time, @RequestParam String orgUrl){
        Timestamp timestamp = Timestamp.valueOf(LocalDateTime.parse(time, DateTimeFormatter.ofPattern(TIME_PATTERN)));
        globalDAO.saveMessage(Integer.valueOf(userId), text, timestamp, orgUrl, GenChatMessage.class);
    }

    @PostMapping("/savepost")
    @ResponseBody
    public void savePost(@RequestParam String userId, @RequestParam String text,
                            @RequestParam String time, @RequestParam String orgUrl){

        Timestamp timestamp = Timestamp.valueOf(LocalDateTime.parse(time, DateTimeFormatter.ofPattern(TIME_PATTERN)));
        globalDAO.saveMessage(Integer.valueOf(userId), text, timestamp, orgUrl, Post.class);
    }

    @PostMapping("/savegroup")
    @ResponseBody
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

    @PostMapping("/savenote")
    @ResponseBody
    public void saveNote(@RequestParam String time, @RequestParam String text, HttpSession session){
        User user = (User) session.getAttribute("user");
        Timestamp timestamp = Timestamp.valueOf(LocalDateTime.parse(time, DateTimeFormatter.ofPattern(TIME_PATTERN)));
        user.getNotes().put(timestamp, text);
        userDAO.updateUser(user); //TODO обновить при выходе?
    }

    @PostMapping("/savefile")
    @ResponseBody
    public void saveFile(@RequestParam(name = "file")MultipartFile file, @RequestParam(required = false) String fileName, HttpSession session){
        User user = (User) session.getAttribute("user");
        System.out.println(file.getName());
        System.out.println();
        //user.getStoredFiles().add(new StoredFile())
    }

    @GetMapping("/favicon")
    @ResponseBody
    public void fav(){}


}

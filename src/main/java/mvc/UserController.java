package mvc;

import db.UserDAO;
import entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("/user")
@SessionAttributes("user")
public class UserController {

    private UserDAO userDAO;

    @Autowired
    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

   /* @RequestMapping("/auth")
    public String showAuthPage(){
        return "auth";
    }*/

    @RequestMapping(value = "/r", method = RequestMethod.GET)
    public String regUser(Model model){
        model.addAttribute(new User());
        return "reg";
    }

    @RequestMapping(value = "/r", method = RequestMethod.POST)
    public String addUserFromForm(@ModelAttribute User user, BindingResult bindingResult,
                                  @RequestParam(value = "image", required = false)MultipartFile image){
        System.out.println(bindingResult.getFieldErrors().size());
        if (bindingResult.hasErrors()) return "reg";
        if (!image.isEmpty()) {
            try {
                System.out.println(image.getBytes().length);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println(user);
        userDAO.persistUser(user);

        return "redirect:/user/" + user.getNickName();
    }

   /* @RequestMapping(value = "/{username}", method = RequestMethod.GET)
    public String showUserProfile(@PathVariable String username, Model model){
        model.addAttribute(userDAO.getUserByName(username));
        return "/profile";
    }*/


}

package mvc;

import db.UserDAO;
import entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

@Controller
@RequestMapping("/user")
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

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public String addUserFromForm(@Valid User user, BindingResult bindingResult){
        if (bindingResult.hasErrors()) return "reg";
        userDAO.persistUser(user);
        return "redirect:/user/" + user.getNickName();
    }

   /* @RequestMapping(value = "/{username}", method = RequestMethod.GET)
    public String showUserProfile(@PathVariable String username, Model model){
        model.addAttribute(userDAO.getUserByName(username));
        return "/profile";
    }*/


}

package esn.viewControllers;

import esn.db.UserDAO;
import esn.entities.Organization;
import esn.entities.User;
import esn.utils.GeneralSettings;
import esn.utils.ImageResizer;
import esn.utils.SimpleUtils;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;

@Controller

public class UserController {

    private UserDAO userDAO;

    @Autowired
    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @GetMapping("/auth")
    public String showAuthPage(){
        return "auth";
    }

    @PostMapping("/auth")
    public String confirmAuth(@ModelAttribute String login, @ModelAttribute String password, Model model){

        if (userDAO.getPassword(login).equals(SimpleUtils.getEncodedString(password))) {
            User user = userDAO.getUserByLogin(login);
            model.addAttribute(user);
            return "redirect:/authorized";
        }

        model.addAttribute("error", "Пароль или логин введены не верно");

        return "redirect:/auth";
    }

    @GetMapping(value = "/authorized")
    public ModelAndView authorized(ModelMap model, @ModelAttribute User user){

        long userId = user.getId(); //TODO get userId
        model.addAttribute("userId", userId);
        return new ModelAndView("redirect:/" + user.getOrganization().getUrlName() + "/wall/", model);
    }


    @GetMapping("/reg")
    public String regUser(Model model){
        model.addAttribute(new User());
        return "reg";
    }

    @PostMapping("/reg")
    public String addUserFromForm(@Valid User user, BindingResult bindingResult,
                                  @RequestParam(value = "image", required = false)MultipartFile image){
        System.out.println(bindingResult.getFieldErrors().size());
        if (bindingResult.hasErrors()) return "reg";
        if (!image.isEmpty()) {
            try {
                String expansion = SimpleUtils.getExpansion(image);
                if (expansion != null) {

                    String fileName = user.getNickName().concat(".").concat(expansion);
                    String fileNameSmall = user.getNickName().concat("_small").concat(".").concat(expansion);
                    byte[] bytes = image.getBytes();
                    byte[] bigImage = ImageResizer.resizeBig(bytes, expansion);
                    byte[] smallImage = ImageResizer.resizeSmall(bytes, expansion);
                    if (bigImage == null || smallImage == null) return "reg"; //TODO если ошибка
                    System.out.println(user.getName());
                    System.out.println(fileName);
                    System.out.println(fileNameSmall);
                    System.out.println(GeneralSettings.AVATAR_PATH);
                    FileUtils.writeByteArrayToFile(new File(GeneralSettings.AVATAR_PATH.concat(fileName)),bigImage);
                    FileUtils.writeByteArrayToFile(new File(GeneralSettings.AVATAR_PATH.concat(fileNameSmall)),smallImage);
                    user.setPhoto(fileName);
                    user.setPhoto_small(fileNameSmall);
                }


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println(user);


        user.setOrganization(new Organization("MockOrg", "mockorg"));//TODO do not work  - organization null
        userDAO.persistUser(user);

        return "redirect:/user/" + user.getNickName();
    }





   /* @RequestMapping(value = "/{username}", method = RequestMethod.GET)
    public String showUserProfile(@PathVariable String username, Model model){
        model.addAttribute(userDAO.getUserByName(username));
        return "/profile";
    }*/




}

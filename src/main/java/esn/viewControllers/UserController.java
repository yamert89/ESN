package esn.viewControllers;

import esn.db.OrganizationDAO;
import esn.db.UserDAO;
import esn.entities.User;
import esn.utils.GeneralSettings;
import esn.utils.ImageResizer;
import esn.utils.SimpleUtils;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.persistence.NoResultException;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;

@Controller
@SessionAttributes("user")
public class UserController {

    private UserDAO userDAO;
    private OrganizationDAO orgDAO;

    @Autowired
    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Autowired
    public void setOrgDAO(OrganizationDAO orgDAO) {
        this.orgDAO = orgDAO;
    }


    @GetMapping("/{org}/auth")
    public String showAuthPage(@PathVariable String org, Model model){
        model.addAttribute("org", org);
        return "auth";
    }

    @PostMapping("/{org}/auth")
    public String confirmAuth(@RequestParam String login, @RequestParam String password,
                              Model model, @PathVariable String org, RedirectAttributes attributes){
        User user = null;
        try {
            user = userDAO.getUserByLogin(login);


            //if (!Arrays.equals(user.getPassword(), SimpleUtils.getEncodedPassword(password.getBytes())))  { //TODO uncomment
                if (false)  {

                System.out.println(password);
                for (byte b :
                        user.getPassword()) {
                    System.out.println(b);
                }
                System.out.println(SimpleUtils.getEncodedPassword(password.getBytes()));
                model.addAttribute("error", "Пароль введен неверно");
                return "auth";
            }


        }catch (NoResultException e){
            System.out.println("NO RESULT");
            model.addAttribute("error", "Логин введен неверно");
            return "auth";
        }catch (Exception e){
            e.printStackTrace();
        }
        /*attributes.addAttribute(user);
        attributes.addAttribute(org);*/
        long userId = user.getId();
        //attributes.addAttribute("userId", userId); TODO uncomment
        return "redirect:/" + org + "/wall/";
    }


    @GetMapping("/{org}/reg")
    public String regUser(Model model, @PathVariable String org){
        model.addAttribute(new User());
        return "reg";
    }

    @PostMapping("/{org}/reg")
    public String addUserFromForm(@Valid @ModelAttribute("user")User user, BindingResult bindingResult,
                                  @RequestParam(value = "image", required = false) MultipartFile image, @PathVariable String org){
        System.out.println(bindingResult.getFieldErrors().size());
        if (bindingResult.hasErrors()) return "reg";
        if (orgDAO.getLogins().contains(user.getLogin())) {
            bindingResult.addError(new FieldError("loginError", "login", "Такой логин уже есть"));
            return "reg";
        }
       /* if (orgDAO.getNickNames().contains(user.getNickName())) return "reg";*/ //TODO need to move
        if (!image.isEmpty()) {
            try {
                String expansion = SimpleUtils.getExpansion(image);
                if (expansion != null) {

                    String fileName = user.getLogin().concat(".").concat(expansion);
                    String fileNameSmall = user.getLogin().concat("_small").concat(".").concat(expansion);
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
        System.out.println(user.getPassword()); //[B@20823413
        for (byte b :
                user.getPassword()) {
            System.out.println(b);
        }


        user.setOrganization(orgDAO.getOrgByURL(org));
        userDAO.persistUser(user);

        return "redirect:/" + org + "/user/" + user.getLogin();
    }

    @GetMapping("/{org}/user/{login}")
    public String userProfile(@PathVariable String org, @PathVariable String login, Model model){
        User user = null;
        try {
            user = userDAO.getUserByLogin(login);
        } catch (Exception e) {
            e.printStackTrace();
        }
        model.addAttribute(user);
        return ""; //TODO

    }





   /* @RequestMapping(value = "/{username}", method = RequestMethod.GET)
    public String showUserProfile(@PathVariable String username, Model model){
        model.addAttribute(userDAO.getUserByName(username));
        return "/profile";
    }*/




}

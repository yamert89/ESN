package esn.viewControllers;

import esn.configs.GeneralSettings;
import esn.db.OrganizationDAO;
import esn.db.UserDAO;
import esn.entities.User;
import esn.services.UserService;
import esn.utils.ImageResizer;
import esn.utils.SimpleUtils;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.NoResultException;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.Set;

@Controller
@RequestMapping("/{org}")
@SessionAttributes("user")
public class UserController {

    private UserDAO userDAO;
    private OrganizationDAO orgDAO;
    private UserService userService;

    @Autowired
    public void setService(UserService service) {
        this.userService = service;
    }


    @Autowired
    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Autowired
    public void setOrgDAO(OrganizationDAO orgDAO) {
        this.orgDAO = orgDAO;
    }


    @GetMapping("/auth")
    public String showAuthPage(@PathVariable String org, Model model){
        model.addAttribute("org", org);
        return "auth";
    }

    /*@PostMapping("/{org}/auth")*/
    @GetMapping("/auth1")
    public String confirmAuth(/*@RequestParam String login, @RequestParam String password,*/
                              Model model, @PathVariable String org, HttpSession session){
        User user = null;
        try {
            user= userDAO.getUserByLogin("yamert"); //TODO  вернуть аутентификацию
            /*user = userDAO.getUserByLogin(login);

            System.out.println(user.getPassword());
            System.out.println(SimpleUtils.getEncodedPassword(password));
            *//*user.setPassword(SimpleUtils.getEncodedPassword(password));
            userDAO.updateUser(user);*//*


            if (!SimpleUtils.getEncodedPassword(password).equals(user.getPassword()))  {

                model.addAttribute("error", "Пароль введен неверно");
                return "auth";
            }*/

        }catch (NoResultException e){
            System.out.println("NO RESULT");
            model.addAttribute("error", "Логин введен неверно");
            return "auth";
        }catch (Exception e){
            e.printStackTrace();
            model.addAttribute("error", "Ошибка на сервере");
            return "auth";
        }

        user.setNetStatus(true);
        session.setMaxInactiveInterval(1800);
        session.setAttribute("user", user);
        session.setAttribute("orgUrl", org);
        session.setAttribute("loginUrl", user.getLogin());
        userService.sendStatus(user, true);
        return "redirect:/" + org + "/wall/";
        //return "wall";
    }

    @PostMapping("/exit")
    public void exit(HttpSession session){
        User usr = (User) session.getAttribute("user");
        userService.sendStatus(usr, false);

        session.invalidate(); //TODO
    }


    @GetMapping("/reg")
    public String regUser(Model model, @PathVariable String org){
        model.addAttribute(new User());
        return "reg";
    }

    @PostMapping("/reg")
    @ResponseStatus(code = HttpStatus.CREATED)
    public String addUserFromForm(@Valid @ModelAttribute("user")User user, BindingResult bindingResult,
                                  @RequestParam(value = "image", required = false) MultipartFile image, @PathVariable String org){
        System.out.println(bindingResult.getFieldErrors().size());
        if (bindingResult.hasErrors()) return "reg";
        if (orgDAO.getLogins().contains(user.getLogin())) {
            bindingResult.addError(new FieldError("loginError", "login", "Такой логин уже есть"));
            return "reg";
        }

        if (!image.isEmpty()) {
            try {
                String extension = SimpleUtils.getExtension(image);

                String fileName = user.getLogin().concat(".").concat(extension);
                String fileNameSmall = user.getLogin().concat("_small").concat(".").concat(extension);
                byte[] bytes = image.getBytes();
                byte[] bigImage = ImageResizer.resizeBig(bytes, extension);
                byte[] smallImage = ImageResizer.resizeSmall(bytes, extension);
                if (bigImage == null || smallImage == null) return "reg"; //TODO если ошибка
                System.out.println(user.getName());
                System.out.println(fileName);
                System.out.println(fileNameSmall);
                System.out.println(GeneralSettings.AVATAR_PATH);
                FileUtils.writeByteArrayToFile(new File(GeneralSettings.AVATAR_PATH.concat(fileName)),bigImage);
                FileUtils.writeByteArrayToFile(new File(GeneralSettings.AVATAR_PATH.concat(fileNameSmall)),smallImage);
                user.setPhoto(fileName);
                user.setPhoto_small(fileNameSmall);


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println(user);
        System.out.println(user.getPassword());



        user.setOrganization(orgDAO.getOrgByURL(org));
        user.setPassword(SimpleUtils.getEncodedPassword(user.getPassword()));
        userDAO.persistUser(user);

        return "redirect:/" + org + "/user/" + user.getLogin();
    }

   /* @GetMapping("/{org}/user/{login}")
    public String userProfile(@PathVariable String org, @PathVariable String login, Model model){
        User user = null;
        try {
            user = userDAO.getUserByLogin(login);
        } catch (Exception e) {
            e.printStackTrace();
        }
        model.addAttribute(user);
        return "redirect:/" + org + "/auth"; //TODO

    }*/


    @GetMapping("/{login}")
    public String showUserProfile(@PathVariable String login, @PathVariable String org,
                                  @SessionAttribute User user, Model model, HttpSession session){
        try {
            if (user.getLogin().equals(login)) {
                user = userDAO.getUserWithInfo(user.getId());
                session.setAttribute("user", user);
                Set<User> allUsers = orgDAO.getOrgByURL(org).getAllEmployers();
                model.addAttribute("bosses", allUsers);
                return "userSettings";
            }
            user = userDAO.getUserByLogin(login);
            user = userDAO.getUserWithInfo(user.getId());
            session.setAttribute("user", user);

        }catch (Exception e){
            e.printStackTrace(); //todo /info and /avatars wom
        }

        return "profile";

    }

    @DeleteMapping("/{login}")
    public String deleteProfile(@PathVariable String login, @SessionAttribute User user){
        userDAO.deleteUser(user); //TODO test
        return "reg";
    }
    @PostMapping("/{login}")
    @ResponseBody
    public boolean changePassword(@PathVariable String login, @SessionAttribute User user, HttpSession session,
                               @RequestParam String oldPass, @RequestParam String newPass){
        if (!SimpleUtils.getEncodedPassword(oldPass).equals(user.getPassword())) return false;
        user.setPassword(newPass);
        userDAO.updateUser(user);
        session.invalidate();
        return true;
    }




}

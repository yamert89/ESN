package esn.viewControllers;

import esn.db.OrganizationDAO;
import esn.db.UserDAO;
import esn.entities.User;
import esn.services.UserService;
import esn.utils.ImageUtil;
import esn.utils.SimpleUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.NoResultException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Calendar;
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
    public String addUserFromForm(@Valid @ModelAttribute User user, BindingResult bindingResult,
                                  @RequestParam(value = "image", required = false) MultipartFile image, @PathVariable String org){
        System.out.println(bindingResult.getFieldErrors().size());
        if (bindingResult.hasErrors()) return "reg";
        if (orgDAO.getLogins().contains(user.getLogin())) {
            bindingResult.addError(new FieldError("loginError", "login", "Такой логин уже есть"));
            return "reg";
        }

        if (!image.isEmpty()) {
            ImageUtil.writeImage(user, image);
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


    @GetMapping("/users/{login}")
    public String showUserProfile(@PathVariable String login, @PathVariable String org,
                                  @SessionAttribute User user, Model model, HttpSession session){
        try {
            if (user.getLogin().equals(login)) {
                user = userDAO.getUserWithInfo(user.getId());
                session.setAttribute("user", user);
                Set<User> allUsers = orgDAO.getOrgByURL(org).getAllEmployers();
                model.addAttribute("bosses", allUsers);
                model.addAttribute(user);
                model.addAttribute("saved", 0);
                return "userSettings";
            }
            user = userDAO.getUserByLogin(login);
            user = userDAO.getUserWithInfo(user.getId());
            session.setAttribute("user", user);

        }catch (Exception e){
            e.printStackTrace();
        }

        return "profile";

    }

    @PostMapping("/users/{login}")
    public String changeProfile(@PathVariable String login, @PathVariable String org, @Valid @ModelAttribute User user, BindingResult bindingResult,
                                @RequestParam(value = "image", required = false) MultipartFile image, @RequestParam String boss, Model model, HttpSession session){
        Calendar birth = null;
        if (bindingResult.hasErrors()) {
            birth = (Calendar) userDAO.createSomeQueryWithSingleResult("select u.userInformation.birthDate from User u where u.id = " + user.getId());
            user.getUserInformation().setBirthDate(birth);
            Set<User> allUsers = orgDAO.getOrgByURL(org).getAllEmployers();
            model.addAttribute("bosses", allUsers);
            model.addAttribute("saved", 0);
            return "userSettings";
        }
        if (!image.isEmpty()) {
            ImageUtil.writeImage(user, image);
        }
        String[] bossParams = boss.split(" - ");
        user.getUserInformation().setBoss(userDAO.getUserByNameAndPosition(bossParams[0], bossParams[1]));
        userDAO.updateUser(user);
        model.addAttribute("saved", 1);
        User us = userDAO.getUserWithInfo(user.getId());
        model.addAttribute(us);
        Set<User> allUsers = orgDAO.getOrgByURL(org).getAllEmployers();
        model.addAttribute("bosses", allUsers);
        session.setAttribute("user", us);
        return "userSettings";
    }

    @DeleteMapping("/users/{login}")
    public String deleteProfile(@PathVariable String login, @SessionAttribute User user){
        userDAO.deleteUser(user); //TODO test
        return "reg";
    }
    @PostMapping("/users/{login}/p")
    @ResponseBody
    public ResponseEntity<Boolean> changePassword(@RequestParam(required = false) String newPass, @RequestParam(required = false) String oldPass,
                                  @PathVariable String login, @SessionAttribute User user, HttpSession session, HttpServletRequest request){

        if (!SimpleUtils.getEncodedPassword(oldPass).equals(user.getPassword())) ResponseEntity.ok().contentLength(5).body(Boolean.FALSE);
        user.setPassword(SimpleUtils.getEncodedPassword(newPass));
        userDAO.updateUser(user);
        session.invalidate();
        return ResponseEntity.ok().contentLength(4).body(Boolean.TRUE);
    }

    @GetMapping("/users/{login}/p")
    @ResponseBody
    public ResponseEntity<Boolean> checkPassword(@PathVariable String login, @RequestParam String pass, @SessionAttribute User user){
        boolean res = SimpleUtils.getEncodedPassword(pass).equals(user.getPassword());
        return ResponseEntity.ok().contentLength(res ? 4 : 5).body(res);
    }




}

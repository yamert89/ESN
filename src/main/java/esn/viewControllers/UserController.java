package esn.viewControllers;

import esn.db.OrganizationDAO;
import esn.db.UserDAO;
import esn.entities.Organization;
import esn.entities.Session;
import esn.entities.User;
import esn.services.WebSocketService;
import esn.utils.ImageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.security.Principal;
import java.util.Calendar;
import java.util.Set;

@Controller
public class UserController {

    private UserDAO userDAO;
    private OrganizationDAO orgDAO;
    private WebSocketService webSocketService;

    @Autowired
    public void setService(WebSocketService service) {
        this.webSocketService = service;
    }


    @Autowired
    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Autowired
    public void setOrgDAO(OrganizationDAO orgDAO) {
        this.orgDAO = orgDAO;
    }

    /*@GetMapping(value = "/")
    public String start(@PathVariable String org){


        //TODO get cookies
        return "redirect:/" + org + "/auth2";
    }*/


    @GetMapping(value = "/auth", headers = "Accept=text/html")
    public String showAuthPage(@RequestParam(required = false) String error, Model model){
        model.addAttribute("error", error != null);
        return "auth";
    }

    @GetMapping("/postauth")
    public String confirmAuth(/*@RequestParam String login, @RequestParam String password,*/
                              Model model, HttpSession session, HttpServletRequest request, Principal principal){
        SecurityContext context = (SecurityContext)session.getAttribute("SPRING_SECURITY_CONTEXT");
        String  login = ((org.springframework.security.core.userdetails.User) context.getAuthentication().getPrincipal()).getUsername();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("AUTHENTICATION......");

        User user = userDAO.getUserByLogin(login);


        /*try {



            System.out.println(user.getPassword());
            System.out.println(SimpleUtils.getEncodedPassword(password));
            user.setPassword(SimpleUtils.getEncodedPassword(password));
            userDAO.updateUser(user);


            if (!SimpleUtils.getEncodedPassword(password).equals(user.getPassword()))  {

                model.addAttribute("error", "Пароль введен неверно");
                return "auth";
            }

        }catch (NoResultException e){
            System.out.println("NO RESULT");
            model.addAttribute("error", "Логин введен неверно");
            return "auth";
        }catch (Exception e){
            e.printStackTrace();
            model.addAttribute("error", "Ошибка на сервере");
            return "auth";
        }*/

        user.setNetStatus(true);
        Organization organization = orgDAO.getOrgById(user.getOrganization().getId());

        String org = user.getOrganization().getUrlName();

        session.setMaxInactiveInterval(1800);
        session.setAttribute("user", user);
        session.setAttribute("org", organization);
        model.addAttribute("org", organization);
        int orgId = orgDAO.getOrgByURL(org).getId();
        session.setAttribute("loginUrl", user.getLogin());
        webSocketService.sendStatus(orgId, user.getId(), true);
        return "redirect:/" + org + "/wall/";
        //return "wall";
    }

    @PostMapping("/logout")
    public void exit(HttpSession session, HttpServletRequest request){
        int orgId = ((Organization) session.getAttribute("org")).getId();
        User user = (User) session.getAttribute("user");
        webSocketService.sendStatus(orgId, user.getId(), false);
        Session sessionPersistent = userDAO.getSession(session.getId());
        sessionPersistent.setEndTime(Calendar.getInstance());
        userDAO.saveSession(new Session(session.getId(), user, request.getRemoteAddr()));
        session.invalidate(); //TODO
    }


    @GetMapping("/reg")
    public String regUser(Model model){
        model.addAttribute(new User());
        return "reg";
    }

    @PostMapping(value = "/reg")
    @ResponseStatus(code = HttpStatus.SEE_OTHER)
    public String addUserFromForm(@Valid @ModelAttribute User user, BindingResult bindingResult,
                                  @RequestParam(value = "image", required = false) MultipartFile image, @RequestParam String orgKey){
        System.out.println("orgKey = " + orgKey);
        Organization organization = orgDAO.getOrgByKey(orgKey);
        if (organization == null){
            bindingResult.addError(new FieldError("keyError", "name", "Ключ не найден"));
            return "reg";
        }
        String org = organization.getUrlName();

        if (bindingResult.hasErrors()) return "reg";
        if (user.getLogin().equals("admin") || orgDAO.getLogins(organization).contains(user.getLogin())) {
            bindingResult.addError(new FieldError("loginError", "login", "Такой логин уже есть"));
            return "reg";
        }



        System.out.println(user);
        System.out.println(user.getPassword());




        user.setOrganization(orgDAO.getOrgByURL(org));
 /*       user.setPassword(SimpleUtils.getEncodedPassword(user.getPassword()));*/
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        user.setAuthority("ROLE_USER");

        if (!image.isEmpty()) {
            ImageUtil.writeAvatar(user, image);
        } else {
            String defAvatarName = user.isMale() ? "/man.jpg" : "/wom.jpg";
            String defAvatarName_small = user.isMale() ? "/man_small.jpg" : "/wom_small.jpg";
            user.setPhoto(defAvatarName);
            user.setPhoto_small(defAvatarName_small); //TODO TEST

        }

        userDAO.persistUser(user);

        return "redirect:/auth";
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


    @GetMapping("{org}/users/{login}")
    public String showUserProfile(@PathVariable String login, @PathVariable String org,
                                  Model model, HttpSession session){
        User user = (User) session.getAttribute("user");
        try {
            if (user.getLogin().equals(login)) {
                user = userDAO.getUserWithInfo(user.getId());
                session.setAttribute("user", user);
                Set<User> allUsers = orgDAO.getOrgByURLWithEmployers(org).getAllEmployers();
                allUsers.remove(user);
                model.addAttribute("bosses", allUsers);
                model.addAttribute(user);
                model.addAttribute("saved", 0);
                return "userSettings";
            }
            user = userDAO.getUserByLogin(login);
            user = userDAO.getUserWithInfo(user.getId());
            session.setAttribute("profile", user);

        }catch (Exception e){
            e.printStackTrace();
        }

        return "profile";

    }

    /*@ModelAttribute("user")
    public void getUser(@RequestParam(required = false) Integer id, Model model){
        if (id == null || model == null){
            return;
        }
        model.addAttribute(userDAO.getUserById(id));
    }*/


    @PostMapping("{org}/users/{login}")
    public String changeProfile(@PathVariable String login, @PathVariable String org, @Valid @ModelAttribute("user") User user, BindingResult bindingResult,
                                @RequestParam(value = "image", required = false) MultipartFile image, @RequestParam String boss, Model model, HttpSession session){
        User userFromSession = (User) session.getAttribute("user");


        Calendar birth = null;
        Set<User> allUsers = orgDAO.getOrgByURLWithEmployers(org).getAllEmployers();
        if (bindingResult.hasErrors()) {
            /*birth = userDAO.getBirthDate(user.getId());*/
            birth = userFromSession.getUserInformation().getBirthDate(); //TODO заменить обращения к базе на обращение к сессии. держать в сессии последнего юзера
            user.getUserInformation().setBirthDate(birth);
            model.addAttribute("bosses", allUsers);
            model.addAttribute("saved", false);
            return "userSettings";
        }
        if (!image.isEmpty()) {
            ImageUtil.writeAvatar(user, image);
        }

        userFromSession.getUserInformation().setBoss(userDAO.getUserById(Integer.parseInt(boss)));
       /* String position = user.getPosition();
        UserInformation inf = user.getUserInformation();
        birth = inf.getBirthDate();
        String phoneMobile = inf.getPhoneMobile();
        String phoneWork = inf.getPhoneWork();
        String phoneInternal = inf.getPhoneInternal();
        String email = inf.getEmail();

        UserInformation inf2 = userFromSession.getUserInformation();
        if ( position != null) userFromSession.setPosition(position);
        if (birth != null) inf2.setBirthDate(birth);
        if (phoneMobile != null) inf2.setPhoneMobile(phoneMobile);
        if (phoneWork != null) inf2.setPhoneWork(phoneWork);
        if (phoneInternal != null) inf2.setPhoneInternal(phoneInternal);
        if (email != null) inf2.setEmail(email);*/

        userFromSession.updateFromForm(user);

        userDAO.updateUser(userFromSession);
        model.addAttribute("saved", 1);
        //User us = userDAO.getUserWithInfo(userFromSession.getId());
        model.addAttribute(userFromSession);
        model.addAttribute("bosses", allUsers);
        model.addAttribute("saved", true);
        model.addAttribute("user", userFromSession);
        session.setAttribute("user", userFromSession);
        return "userSettings";
    }

    @DeleteMapping("{org}/users/{login}")
    public String deleteProfile(@PathVariable String login,  @PathVariable String org, HttpSession session){
        User user = (User) session.getAttribute("user");
        userDAO.deleteUser(user); //TODO test
        return "reg";
    }
    @PostMapping("{org}/users/{login}/p")
    @ResponseBody
    public ResponseEntity<Boolean> changePassword(@RequestParam(required = false) String newPass, @RequestParam(required = false) String oldPass,
                                  @PathVariable String login, @PathVariable String org, HttpSession session, HttpServletRequest request){
        User user = (User) session.getAttribute("user");
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (!encoder.encode(oldPass).equals(user.getPassword())) ResponseEntity.ok().contentLength(5).body(Boolean.FALSE);
        user.setPassword(encoder.encode(newPass));
        userDAO.updateUser(user);
        session.invalidate();
        return ResponseEntity.ok().contentLength(4).body(Boolean.TRUE);
    }

    @GetMapping("{org}/users/{login}/p")
    @ResponseBody
    public ResponseEntity<Boolean> checkPassword(@PathVariable String login,  @PathVariable String org,
                                                 @RequestParam String pass, HttpSession session){
        User user = (User) session.getAttribute("user");
        boolean res = new BCryptPasswordEncoder().encode(pass).equals(user.getPassword());
        return ResponseEntity.ok().contentLength(res ? 4 : 5).body(res);
    }




}

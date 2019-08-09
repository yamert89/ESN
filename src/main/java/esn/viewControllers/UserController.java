package esn.viewControllers;

import esn.db.OrganizationDAO;
import esn.db.UserDAO;
import esn.entities.Organization;
import esn.entities.Session;
import esn.entities.User;
import esn.services.EmailService;
import esn.services.LiveStat;
import esn.services.WebSocketService;
import esn.utils.ImageUtil;
import esn.utils.SimpleUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
    private LiveStat liveStat;
    private EmailService emailService;
    private final static Logger logger = LogManager.getLogger();

    @Autowired
    @Qualifier("adminEmailService")
    public void setEmailService(EmailService emailService) {
        this.emailService = emailService;
    }

    @Autowired
    public void setLiveStat(LiveStat liveStat) {
        this.liveStat = liveStat;
    }

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

    @GetMapping(value = "/auth", headers = "Accept=text/html")
    public String showAuthPage(@RequestParam(required = false) String error, Model model){
        model.addAttribute("error", error != null);
        logger.debug("AUTHENTICATION......");
        return "auth";
    }

    @GetMapping("/postauth")
    public String confirmAuth(/*@RequestParam String login, @RequestParam String password,*/
                              Model model, HttpSession session, HttpServletRequest request, Principal principal){
        SecurityContext context = (SecurityContext)session.getAttribute("SPRING_SECURITY_CONTEXT");
        String  login = ((org.springframework.security.core.userdetails.User) context.getAuthentication().getPrincipal()).getUsername();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();


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

        liveStat.userLogged(user.getId());
        Organization organization = user.getOrganization();

        session.setMaxInactiveInterval(1800);
        session.setAttribute("user", user);
        session.setAttribute("org", organization);
        model.addAttribute("org", organization);
        session.setAttribute("loginUrl", user.getLogin());
        session.setAttribute("ip", request.getRemoteAddr());
        webSocketService.sendStatus(organization.getId(), user.getId(), true);
        //session.setMaxInactiveInterval(10);
        System.out.println(session.getMaxInactiveInterval());

        return "redirect:/" + organization.getUrlName() + "/wall/";
        //return "wall";
    }

    @GetMapping("/logout")
    public String exit(HttpSession session, HttpServletRequest request){
        int orgId = ((Organization) session.getAttribute("org")).getId();
        User user = (User) session.getAttribute("user");
        liveStat.userLogout(user.getId());
        webSocketService.sendStatus(orgId, user.getId(), false);
        userDAO.saveSession(new Session(session.getId(), user, request.getRemoteAddr(),
                session.getCreationTime(), System.currentTimeMillis()));
        session.invalidate();
        return "redirect:/auth";
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
            String defAvatarName = user.isMale() ? "/app/man.jpg" : "/app/wom.jpg";
            String defAvatarName_small = user.isMale() ? "/app/man_small.jpg" : "/app/wom_small.jpg";
            user.setPhoto(defAvatarName);
            user.setPhoto_small(defAvatarName_small);

        }

        userDAO.persistUser(user);

        SimpleUtils.createUserFolders(org, user.getLogin());

        return "redirect:/auth";
    }

    @GetMapping("{org}/users/{login}")
    public String showUserProfile(@PathVariable String login, @PathVariable String org,
                                  Model model, HttpSession session){
        User user = (User) session.getAttribute("user");
        user = userDAO.getUserWithInfo(user.getLogin());
        try {
            if (user.getLogin().equals(login)) {
                session.setAttribute("user", user);
                /*Set<User> allUsers = orgDAO.getOrgByURLWithEmployers(org).getAllEmployers();*/
                Set<User> allUsers = ((Organization)session.getAttribute("org")).getAllEmployers();
                allUsers.remove(user);
                model.addAttribute("bosses", allUsers);
                model.addAttribute(user);
                model.addAttribute("saved", 0);
                return "userSettings";
            }
            session.setAttribute("profile", user);

        }catch (Exception e){
            e.printStackTrace();
        }

        return "profile";

    }


    @PostMapping("{org}/users/{login}")
    public String changeProfile(@PathVariable String login, @PathVariable String org, @Valid @ModelAttribute("user") User user, BindingResult bindingResult,
                                @RequestParam(value = "image", required = false) MultipartFile image, @RequestParam String boss, Model model, HttpSession session){
        User userFromSession = (User) session.getAttribute("user");


        Calendar birth = null;
        Set<User> allUsers = ((Organization)session.getAttribute("org")).getAllEmployers();
        if (bindingResult.hasErrors()) {
            /*birth = userDAO.getBirthDate(user.getId());*/
            birth = userFromSession.getUserInformation().getBirthDate();
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
    @ResponseBody
    @ResponseStatus(code = HttpStatus.OK)
    public void deleteProfile(HttpSession session, @PathVariable String login){
        try {
            User user = (User) session.getAttribute("user");
            Organization org = user.getOrganization();
            org.getAllEmployers().remove(user);
            orgDAO.update(org);
            userDAO.deleteUser(user); //TODO test
        }catch (Exception e){
            e.printStackTrace();
        }
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

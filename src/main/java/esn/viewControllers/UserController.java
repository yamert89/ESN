package esn.viewControllers;

import esn.configs.GeneralSettings;
import esn.db.UserDAO;
import esn.db.service.OrgService;
import esn.entities.Organization;
import esn.entities.Session;
import esn.entities.User;
import esn.services.EmailService;
import esn.services.LiveStat;
import esn.services.WebSocketService;
import esn.utils.ImageUtil;
import esn.utils.SimpleUtils;
import esn.viewControllers.accessoryFunctions.SessionUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

@Controller
public class UserController {

    private UserDAO userDAO;
    private OrgService orgService;
    private WebSocketService webSocketService;
    private LiveStat liveStat;
    private EmailService emailService;
    private SessionUtil sessionUtil;
    private final static Logger logger = LogManager.getLogger(UserController.class);

    @Autowired
    public void setSessionUtil(SessionUtil sessionUtil) {
        this.sessionUtil = sessionUtil;
    }

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
    public void setOrgService(OrgService orgService) {
        this.orgService = orgService;
    }

    @GetMapping(value = "/auth", headers = "Accept=text/html")
    public String showAuthPage(@RequestParam(required = false) String error,
                               @RequestParam(required = false) String reg, Model model){
        model.addAttribute("error", error != null);
        model.addAttribute("reg", reg != null);
        logger.debug("AUTHENTICATION......");
        return "auth";
    }

    @GetMapping("/postauth")
    public ModelAndView confirmAuth(RedirectAttributes redirectAttributes, HttpSession session, HttpServletRequest request, Principal principal){
        SecurityContext context = (SecurityContext)session.getAttribute("SPRING_SECURITY_CONTEXT");
        String  login = ((org.springframework.security.core.userdetails.User) context.getAuthentication().getPrincipal()).getUsername();
        ModelAndView modelAndView = new ModelAndView("redirect:/error");
        Organization organization = sessionUtil.getOrg(request, principal);
        if (organization == null){
            redirectAttributes.addFlashAttribute("flash", "Профиль организации удалён.");
            redirectAttributes.addAttribute("status", 777);
            return modelAndView;
        }
        User user = sessionUtil.getUser(request, principal);
        logger.debug(user.getName() + " logged");
        modelAndView.setViewName("redirect:/" + organization.getUrlName() + "/wall/");

        return modelAndView;
        //return "wall";
    }

    @GetMapping("/logout")
    public String exit(HttpSession session, HttpServletRequest request, Principal principal){
        try {
            int orgId = sessionUtil.getOrg(request, principal).getId();
            User user = sessionUtil.getUser(request, principal);
            liveStat.userLogout(user.getId());
            webSocketService.sendStatus(orgId, user.getId(), false);
            userDAO.saveSession(new Session(session.getId(), user, request.getRemoteAddr(),
                    session.getCreationTime(), System.currentTimeMillis()));
            session.invalidate();
        }catch (Exception e){logger.error("logout error", e);}
        return "redirect:/auth";
    }


    @GetMapping("/reg")
    public String regUser(Model model){
        if (!model.containsAttribute("user")) model.addAttribute(new User());
        return "reg";
    }

    @PostMapping(value = "/reg")
    @ResponseStatus(code = HttpStatus.SEE_OTHER)
    public ModelAndView addUserFromForm(@Valid @ModelAttribute User user, BindingResult bindingResult,
                                        @RequestParam(value = "image", required = false) MultipartFile image, @RequestParam String orgKey,
                                        @RequestParam String sex, RedirectAttributes redirectAttributes){
        ModelAndView modelAndView = new ModelAndView("redirect:/auth?reg=success");
        try {
            logger.debug("orgKey = " + orgKey);
            Organization organization = orgService.findByKey(orgKey);
            if (organization == null) {
                bindingResult.addError(new FieldError("keyError", "name", "Ключ не найден"));
                modelAndView.setViewName("reg");
                return modelAndView;
            }

            if (bindingResult.hasErrors()){
                user.setName("");
                modelAndView.addObject(user);
                modelAndView.setViewName("reg");
                return modelAndView;
            }
            if (user.getLogin().equals("admin") ||
                    orgService.getLogins().contains(user.getLogin()) ||
                    user.getLogin().equals("deleted")) {
                bindingResult.addError(new FieldError("loginError", "login", "Логин занят. Придумайте другой"));
                modelAndView.setViewName("reg");
                return modelAndView;
            }

            logger.debug(user);
            logger.debug(user.getPassword());

            String[] nameArr = user.getName().split("_");
            user.setName(user.getName().replaceAll("_", " "));
            String shortName = nameArr.length == 3 ?
                    nameArr[0] + " " + nameArr[1].substring(0, 1) + ". " + nameArr[2].substring(0, 1) + "." :
                    nameArr[0] + " " + nameArr[1].substring(0, 1) + ".";
            user.setShortName(shortName);
            user.setMale(sex.equals("m"));

            user.setOrganization(organization);
            /*       user.setPassword(SimpleUtils.getEncodedPassword(user.getPassword()));*/
            user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
            user.setAuthority(orgService.isAdminKey(orgKey, organization.getId()) ? "ROLE_ADMIN" : "ROLE_USER");
            try {
                Files.createDirectories(Paths.get(GeneralSettings.STORAGE_PATH + "\\" + user.getOrganization().getUrlName() + "\\stored_files\\" + user.getLogin() + "\\"));
            }catch (Exception e){
                logger.error("ReCreate user directory");
            }

            if (!image.isEmpty()) {
                ImageUtil.writeAvatar(user, image);
            } else {
                String defAvatarName = user.isMale() ? "/app/man.jpg" : "/app/wom.jpg";
                String defAvatarName_small = user.isMale() ? "/app/man_small.jpg" : "/app/wom_small.jpg";
                user.setPhoto(defAvatarName);
                user.setPhoto_small(defAvatarName_small);

            }

            userDAO.persistUser(user);

            SimpleUtils.createUserFolders(organization.getUrlName(), user.getLogin());
        }catch (Exception e){
            logger.error("REG_USER ERROR", e);
            redirectAttributes.addFlashAttribute("flash", "Произошла ошибка при регистрации пользователя. Сообщите разработчику");
            redirectAttributes.addAttribute("status", 777);
            modelAndView.setViewName("redirect:/error");
            return modelAndView;

        }

        return modelAndView;
    }

    @GetMapping("{org}/users/{login}")
    public ModelAndView showUserProfile(@PathVariable String login, RedirectAttributes redirectAttributes,
                                  Model model, HttpSession session, HttpServletRequest request, Principal principal){
        ModelAndView modelAndView = new ModelAndView("userSettings");
        User user = sessionUtil.getUser(request, principal);
        user = userDAO.getUserWithInfo(user.getId());
        try {
            if (user.getLogin().equals(login)) {
                session.setAttribute("user", user);
                /*Set<User> allUsers = orgDAO.getOrgByURLWithEmployers(org).getAllEmployers();*/
                Set<User> allUsers = new HashSet<>(user.getOrganization().getAllEmployers());
                allUsers.remove(user);
                User del = new User();
                del.setLogin("deleted");
                allUsers.remove(del);
                modelAndView.addObject("bosses", allUsers);
                modelAndView.addObject(user);
                modelAndView.addObject("saved", 0);
                return modelAndView;
            }
            user = userDAO.getUserWithInfo(login);
            session.setAttribute("profile", user);

        }catch (Exception e){
            logger.error(e.getMessage(), e);
            redirectAttributes.addAttribute("status", 500);
            modelAndView.setViewName("redirect:/error");
        }
        modelAndView.setViewName("profile");

        return modelAndView;

    }


    @PostMapping("{org}/users/{login}")
    public ModelAndView changeProfile(@RequestParam String position, @Valid @ModelAttribute("user") User user, BindingResult bindingResult,
                                @RequestParam(value = "image", required = false) MultipartFile image, @RequestParam String boss,
                                      Model model, HttpSession session, RedirectAttributes redirectAttributes, HttpServletRequest request, Principal principal){
        ModelAndView modelAndView = new ModelAndView("userSettings");
        User userFromSession = sessionUtil.getUser(request, principal);
        try {

            Calendar birth = null;
            Set<User> allUsers = userFromSession.getOrganization().getAllEmployers();
            if (bindingResult.hasErrors()) {
                /*birth = userDAO.getBirthDate(user.getId());*/
                birth = userFromSession.getUserInformation().getBirthDate();
                user.getUserInformation().setBirthDate(birth);
                model.addAttribute("bosses", allUsers);
                model.addAttribute("saved", false);
                return modelAndView;
            }
            userFromSession.updateFromForm(user);

            if (!image.isEmpty()) {
                ImageUtil.writeAvatar(userFromSession, image);
                model.addAttribute("update_avatar", true);
            } else model.addAttribute("update_avatar", false);

            if (!boss.equals("-")) userFromSession.getUserInformation().setBoss(userDAO.getReference(Integer.parseInt(boss)));
            if (!position.equals("-")) userFromSession.setPosition(position);


            user = userDAO.updateUser(userFromSession);
            model.addAttribute("saved", 1);
            //User us = userDAO.getUserWithInfo(userFromSession.getId());
            //model.addAttribute(userFromSession);
            model.addAttribute("bosses", allUsers);
            model.addAttribute("saved", true);
            model.addAttribute("user", user);
            session.setAttribute("user", user);
        }catch (Exception e){
            logger.error("change profile", e);
            redirectAttributes.addAttribute("status", 777);
            redirectAttributes.addFlashAttribute("flash", "Произошла ошибка при изменении профиля пользователя. Сообщите разработчику");
            modelAndView.setViewName("redirect:/error");
        }
        return modelAndView;
    }

    @DeleteMapping("{org}/users/{login}")
    @ResponseBody
    @ResponseStatus(code = HttpStatus.OK)
    public void deleteProfile(HttpServletRequest request, Principal principal){
        try {
            User user = sessionUtil.getUser(request, principal);
            Organization org = user.getOrganization();
            org.getAllEmployers().remove(user);
            orgService.merge(org);
            userDAO.deleteUser(user);
        }catch (Exception e){
            logger.error(e.getMessage(), e);
        }
    }
    @PostMapping("{org}/users/{login}/p")
    @ResponseBody
    public ResponseEntity<Boolean> changePassword(@RequestParam(required = false) String newPass, @RequestParam(required = false) String oldPass,
                                  HttpSession session, HttpServletRequest request, Principal principal){
        User user = sessionUtil.getUser(request, principal);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (!encoder.encode(oldPass).equals(user.getPassword())) ResponseEntity.ok().contentLength(5).body(Boolean.FALSE);
        user.setPassword(encoder.encode(newPass));
        userDAO.updateUser(user);
        session.invalidate();
        return ResponseEntity.ok().contentLength(4).body(Boolean.TRUE);
    }

    @GetMapping("{org}/users/{login}/p")
    @ResponseBody
    public ResponseEntity<Boolean> checkPassword(HttpServletRequest request, Principal principal, @RequestParam String pass){
        User user = sessionUtil.getUser(request, principal);
        boolean res = new BCryptPasswordEncoder().matches(pass, user.getPassword());
        return ResponseEntity.ok().contentLength(res ? 4 : 5).body(res);
    }




}

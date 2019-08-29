package esn.viewControllers.main;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import esn.db.UserDAO;
import esn.entities.Organization;
import esn.entities.User;
import esn.entities.secondary.ContactGroup;
import esn.entities.secondary.PseudoContactGroup;
import esn.viewControllers.accessoryFunctions.SessionUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
public class GroupsController {
    private final static Logger logger = LogManager.getLogger(GroupsController.class);

    private UserDAO userDAO;
    private SessionUtil sessionUtil;

    @Autowired
    public void setSessionUtil(SessionUtil sessionUtil) {
        this.sessionUtil = sessionUtil;
    }

    @Autowired
    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @GetMapping("/{organization}/groups")
    public ModelAndView groups(Model model, HttpSession session, HttpServletRequest request, Principal principal, RedirectAttributes redirectAttributes){
        ModelAndView modelAndView = new ModelAndView("groups");
        try {
            User user = sessionUtil.getUser(request, principal);
            Organization org = sessionUtil.getOrg(request, principal);
            //Set<User> employers = new HashSet<>(orgDAO.getOrgByURLWithEmployers(organization).getAllEmployers());
            Set<User> employers = org.getAllEmployers();
            employers.remove(user);
            User del = new User();
            del.setLogin("deleted");
            del.setId(0);
            employers.remove(del);
            modelAndView.addObject("employers", employers);
            Set<ContactGroup> groups = user.getGroups();
        /*Set<String> groupNames = new HashSet<>();
        for (ContactGroup group :
                groups) {
            groupNames.add(group.getName());
        }*/
            Set<String> groupNames = user.getGroups()
                    .stream()
                    .map(ContactGroup::getName)
                    .collect(Collectors.toSet());

            modelAndView.addObject("groupsNames", groupNames);
            Map<String, Set<User>> resMap = new HashMap<>();
            for (ContactGroup group : groups) {
                Set<User> resVal = new HashSet<>(groups.size());
                int[] ids = group.getPersonIds();
                for (int id : ids) {
                    User u = userDAO.getUserById(id);
                    resVal.add(u);
                }
                resMap.put(group.getName(), resVal);

            }
            modelAndView.addObject("groups", resMap);
            logger.debug(user);
        }catch (Exception e){
            logger.error("groups", e);
            redirectAttributes.addFlashAttribute("flash", "Произошла ошибка при получении списка групп. Сообщите разработчику");
            redirectAttributes.addAttribute("status", 777);
        }

        return modelAndView;
    }

    @PostMapping("/savegroup")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void saveGroup(@RequestParam String groupName, @RequestParam String personIds,
            HttpServletRequest request, Principal principal, Model model, HttpSession session){
        User user = sessionUtil.getUser(request, principal);
        try {
            String[] ids_s = personIds.split(",");
            if (ids_s.length == 1 && ids_s[0].equals("")) return;
            int[] ids = Stream.of(ids_s).mapToInt(Integer::parseInt).toArray();
            user = userDAO.getUserById(user.getId());
            user.getGroups().add(new ContactGroup(groupName, user, ids, true));
            // userDAO.refresh(user);

            user = userDAO.updateUser(user);
            session.setAttribute("user", user);
            //model.addAttribute(user);

        }catch (Exception e){
            logger.error(e.getMessage(), e);

        }
    }

    @GetMapping("/deletegroup")
    @ResponseStatus(code = HttpStatus.OK)
    public void deleteGroup(@RequestParam String groupName,  HttpSession session, HttpServletRequest request, Principal principal){
        User user = sessionUtil.getUser(request, principal);
        try {
            Iterator<ContactGroup> it = user.getGroups().iterator();
            ContactGroup group = null;
            while (it.hasNext()) {
                if ((group = it.next()).getName().equals(groupName)) break;
            }
            if (group != null) {
                user.getGroups().remove(group);
                session.setAttribute("user", userDAO.updateUser(user));
            }
        }catch (Exception e){
            logger.error(e.getMessage(), e);
        }
    }

    @GetMapping("/expand-props")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void saveExpandStatus(@RequestParam String groups,  HttpSession session, HttpServletRequest request, Principal principal){
        User user = sessionUtil.getUser(request, principal);
        //[{"name" : "ddsf", "expand" : true},{}]
        ObjectMapper om = new ObjectMapper();
        List<PseudoContactGroup> grps = null;
        try {
            grps = om.readValue(groups, new TypeReference<List<PseudoContactGroup>>(){});
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }

        for (ContactGroup group: user.getGroups()) {
            for (int i = 0; i < grps.size(); i++) {
                if (group.getName().equals(grps.get(i).getName())) {
                    group.setExpandable(grps.get(i).getExpanded());
                    grps.remove(i);
                    break;
                }
            }

        }
        session.setAttribute("user", userDAO.updateUser(user));


    }




}

package esn.viewControllers.main;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import esn.configs.GeneralSettings;
import esn.db.DepartmentDAO;
import esn.db.GlobalDAO;
import esn.db.OrganizationDAO;
import esn.db.UserDAO;
import esn.db.message.GenDAO;
import esn.db.message.PrivateDAO;
import esn.db.message.WallDAO;
import esn.entities.Organization;
import esn.entities.User;
import esn.entities.secondary.ContactGroup;
import esn.entities.secondary.PseudoContactGroup;
import esn.services.WebSocketService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
public class GroupsController {
    private final static Logger logger = LogManager.getLogger(GroupsController.class);


    private OrganizationDAO orgDAO;
    private UserDAO userDAO;

    @Autowired
    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Autowired
    public void setOrgDAO(OrganizationDAO orgDAO) {
        this.orgDAO = orgDAO;
    }

    @GetMapping("/{organization}/groups")
    public String groups(@PathVariable String organization, Model model, HttpSession session){
        User user = (User) session.getAttribute("user");
        Organization org = (Organization) session.getAttribute("org");
        //Set<User> employers = new HashSet<>(orgDAO.getOrgByURLWithEmployers(organization).getAllEmployers());
        Set<User> employers = org.getAllEmployers();
        employers.remove(user);
        model.addAttribute("employers", employers);
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

        model.addAttribute("groupsNames", groupNames);
        Map<String, Set<User>> resMap = new HashMap<>();
        for (ContactGroup group : groups){
            Set<User> resVal = new HashSet<>(groups.size());
            int [] ids = group.getPersonIds();
            for (int id : ids) {
                User u = userDAO.getUserById(id);
                resVal.add(u);
            }
            resMap.put(group.getName(), resVal);

        }
        model.addAttribute("groups", resMap);
        logger.debug(user);

        return "groups";
    }

    @PostMapping("/savegroup")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void saveGroup(@RequestParam String groupName, @RequestParam String personIds,
                          HttpSession session, Model model){
        User user = (User) session.getAttribute("user");
        try {
            String[] ids_s = personIds.split(",");
            int[] ids = Stream.of(ids_s).mapToInt(Integer::parseInt).toArray();
            user = userDAO.getUserById(user.getId());
            user.getGroups().add(new ContactGroup(groupName, user, ids, true));
            // userDAO.refresh(user);

            user = userDAO.updateUser(user);
            //session.setAttribute("user", user);
            model.addAttribute(user);

        }catch (Exception e){
            e.printStackTrace();

        }
    }

    @GetMapping("/deletegroup")
    @ResponseStatus(code = HttpStatus.OK)
    public void deleteGroup(@RequestParam String groupName,  HttpSession session){
        User user = (User) session.getAttribute("user");
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
            e.printStackTrace();
        }
    }

    @GetMapping("/expand-props")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void saveExpandStatus(@RequestParam String groups,  HttpSession session){
        User user = (User) session.getAttribute("user");
        //[{"name" : "ddsf", "expand" : true},{}]
        ObjectMapper om = new ObjectMapper();
        List<PseudoContactGroup> grps = null;
        try {
            grps = om.readValue(groups, new TypeReference<List<PseudoContactGroup>>(){});
        } catch (IOException e) {
            e.printStackTrace();
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

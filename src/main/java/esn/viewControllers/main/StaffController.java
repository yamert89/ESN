package esn.viewControllers.main;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import esn.db.DepartmentDAO;
import esn.db.UserDAO;
import esn.db.service.OrgService;
import esn.entities.Department;
import esn.entities.Organization;
import esn.entities.User;
import esn.viewControllers.accessoryFunctions.SessionUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.Principal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Controller
public class StaffController {
    private final static Logger logger = LogManager.getLogger(StaffController.class);

    private OrgService orgService;
    private UserDAO userDAO;
    private DepartmentDAO departmentDAO;
    private HttpHeaders headers;
    private SessionUtil sessionUtil;

    @Autowired
    public void setSessionUtil(SessionUtil sessionUtil) {
        this.sessionUtil = sessionUtil;
    }

    @Autowired
    public void setHeaders(HttpHeaders headers) {
        this.headers = headers;
    }

    @Autowired
    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Autowired
    public void setOrgService(OrgService orgService) {
        this.orgService = orgService;
    }

    @Autowired
    public void setDepartmentDAO(DepartmentDAO departmentDAO) {
        this.departmentDAO = departmentDAO;
    }

    @GetMapping(value = "/{organization}/staff")
    public String staff(HttpServletRequest request, Principal principal){
        User user = sessionUtil.getUser(request, principal);
        return user.getAuthority().equals("ROLE_ADMIN") ? "staff_admin" : "staff"; //TODO replace with jsp security
    }

    @GetMapping("/getstaff")
    @ResponseBody
    public ResponseEntity<String> getStaff(HttpServletRequest request, Principal principal){
        User user = sessionUtil.getUser(request, principal);
        Organization org = sessionUtil.getOrg(request, principal);
        //org = orgDAO.getOrgByURLWithEmployers(org.getUrlName());

        String json = "";
        try {

            Set<Department> deps = departmentDAO.getHeadDepartments(org);
            Set<User> allEmployers = org.getAllEmployers();
            User del = new User();
            del.setLogin("deleted");
            allEmployers.remove(del);
            Department head = new Department("default", 0L, -1L, deps, allEmployers);
            /*deps.add(0, departmentDAO.getDefaultDepartment(org));
             */
            Object[] res = new Object[]{head, user.getAuthority().equals("ROLE_ADMIN")};

            ObjectMapper om = new ObjectMapper();
            json = om.writeValueAsString(res); // TODO exception
            logger.debug(json);
        }catch (Exception e){
            logger.error(e.getMessage(), e);
            return null;
        }

        return ResponseEntity.ok().headers(headers).body(json);
    }

    @PostMapping("/{org}/savestructure")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void saveStructure(@RequestBody String data, @PathVariable String org, HttpSession session){
        ObjectMapper om = new ObjectMapper();
        try {
            Department[] deps = om.readValue(data, new TypeReference<Department[]>(){});
            Organization organization = orgService.findByUrl(org, true);
            for (Department d :
                    deps) {
                if (d.getParentId() == 0) d.setParent(null);
                else if (d.getParentId() != null && d.getParentId() != 0) d.setParent(departmentDAO.getReference(d.getParentId()));

                d.setOrganization(organization);
                d.initParentForTree();
                d.initOrgForChildren();
            }
            organization.getDepartments().addAll(Arrays.asList(deps));

            session.setAttribute("org", orgService.merge(organization));

        } catch (Exception e){
            logger.error(e.getMessage(), e);
        }
    }

    @PostMapping("/department")
    @ResponseStatus(code = HttpStatus.CREATED)
    public ResponseEntity<Long> saveDepartment(@RequestParam String newname,
                                               @RequestParam String oldname, @RequestParam String ids, HttpServletRequest request, Principal principal){

        Organization organization = sessionUtil.getOrg(request, principal);
        ObjectMapper om = new ObjectMapper();
        Department department = null;
        try {
            int[] empls = om.readValue(ids, int[].class);
            department = departmentDAO.getDepartmentByName(oldname, organization);

            department.setName(newname);
            Set<User> employers = new HashSet<>();
            User user = null;
            for (Integer id :
                    empls) {
                user = userDAO.getUserById(id);
                user.setDepartment(department);
                userDAO.updateUser(user);
                employers.add(user);
            }
            department.getEmployers().addAll(employers);
            departmentDAO.merge(department);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return ResponseEntity.ok().body(department.getId());
    }

    @DeleteMapping("/departments")
    @ResponseBody
    @ResponseStatus(code = HttpStatus.OK)
    public void clearDeps(HttpSession session, HttpServletRequest request, Principal principal){
        Organization org = sessionUtil.getOrg(request, principal);
        try {
            org.getDepartments().clear();
        }catch (Exception e){
            org = orgService.findByUrl(org.getUrlName(), true);
            orgService.clearStructure(org);
            org.getDepartments().clear();
        }
        session.setAttribute("org", orgService.merge(org));
    }


}

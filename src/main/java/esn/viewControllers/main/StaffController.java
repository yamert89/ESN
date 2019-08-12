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
import esn.entities.Department;
import esn.entities.Organization;
import esn.entities.User;
import esn.services.WebSocketService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Controller
public class StaffController {
    private final static Logger logger = LogManager.getLogger(StaffController.class);

    private OrganizationDAO orgDAO;
    private UserDAO userDAO;
    private DepartmentDAO departmentDAO;

    @Autowired
    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Autowired
    public void setOrgDAO(OrganizationDAO orgDAO) {
        this.orgDAO = orgDAO;
    }
    @Autowired
    public void setDepartmentDAO(DepartmentDAO departmentDAO) {
        this.departmentDAO = departmentDAO;
    }

    @GetMapping(value = "/{organization}/staff")
    public String staff(HttpSession session){
        User user = (User) session.getAttribute("user");
        return user.getAuthority().equals("ROLE_ADMIN") ? "staff_admin" : "staff";
    }

    @GetMapping("/getstaff")
    @ResponseBody
    public ResponseEntity<String> getStaff(HttpSession session){
        User user = (User) session.getAttribute("user");
        Organization org = (Organization) session.getAttribute("org");
        //org = orgDAO.getOrgByURLWithEmployers(org.getUrlName());

        String json = "";
        StringBuilder jsonS = null;
        try {

            Set<Department> deps = departmentDAO.getHeadDepartments(org);
            Department head = new Department("default", 0L, -1L, deps, org.getAllEmployers());
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

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Content-Type", "application/json; charset=UTF-8");
        return ResponseEntity.ok().headers(responseHeaders).body(json);
    }

    @PostMapping("/{org}/savestructure")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void saveStructure(@RequestBody String data, @PathVariable String org, HttpSession session){
        ObjectMapper om = new ObjectMapper();
        try {
            Department[] deps = om.readValue(data, new TypeReference<Department[]>(){});
            Organization organization = orgDAO.getOrgByURLWithDepartments(org);
            for (Department d :
                    deps) {
                if (d.getParentId() == 0) d.setParent(null);
                else if (d.getParentId() != null && d.getParentId() != 0) d.setParent(departmentDAO.getDepartmentById(d.getParentId()));

                d.setOrganization(organization);
                d.initParentForTree();
                d.initOrgForChildren();
            }
            organization.getDepartments().addAll(Arrays.asList(deps));
            //organization.setDepartments(set);
            orgDAO.update(organization);
            session.setAttribute("org", organization);

        } catch (Exception e){
            logger.error(e.getMessage(), e);
        }
    }

    @PostMapping("/department")
    @ResponseStatus(code = HttpStatus.CREATED)
    public ResponseEntity<Long> saveDepartment(@RequestParam String newname,
                                               @RequestParam String oldname, @RequestParam String ids, HttpSession session){

        Organization organization = (Organization) session.getAttribute("org");
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
    public void clearDeps(HttpSession session){
        Organization org = (Organization) session.getAttribute("org");
        try {
            org.getDepartments().clear();
        }catch (Exception e){
            org = orgDAO.getOrgByURLWithDepartments(org.getUrlName());
            orgDAO.deleteAllDepartmentsInUsers();
            org.getDepartments().clear();
        }
        session.setAttribute("org", orgDAO.update(org));
    }


}

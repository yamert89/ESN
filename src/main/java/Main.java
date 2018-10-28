import db.DepartmentDAO;
import db.OrganizationDAO;
import db.UserDAO;
import entities.Department;
import entities.Organization;
import entities.User;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Set;


public class Main {

    public static void main(String[] args) {
        //beanTest();
        restoreObjects();

    }

    private static void beanTest(){
        ApplicationContext context = new ClassPathXmlApplicationContext("app_context.xml");
        UserDAO userDao = (UserDAO) context.getBean("userDao");
        User user = (User) context.getBean("user");
        OrganizationDAO organizationDAO = (OrganizationDAO) context.getBean("orgDao");
        DepartmentDAO departmentDAO = (DepartmentDAO) context.getBean("departmentDao");
        Organization organization = (Organization) context.getBean("organiz");
        Department department = (Department) context.getBean("department");
        department.addEmployer(user);
        organization.addDepartment(department);


        organizationDAO.persistOrg(organization);

    }
    private void pojoTest(){

    }

    private static void restoreObjects(){
        ApplicationContext context = new ClassPathXmlApplicationContext("app_context.xml");
        OrganizationDAO organizationDAO = (OrganizationDAO) context.getBean("orgDao");
        DepartmentDAO departmentDAO = (DepartmentDAO) context.getBean("departmentDao");
        UserDAO userDao = (UserDAO) context.getBean("userDao");
        Organization org = organizationDAO.getOrgById(1);
        Set<Department> departments = org.getDepartments();
        Department department = departments.iterator().next();
        User user = department.getEmployers().iterator().next();
        //Department department = departmentDAO.getDepartmentById(1);
        //User user = userDao.getUserById(1);
        System.out.println("user " + user.getId() + " " + user.getName());
        System.out.println("department " + department.getId() + " " + department.getName());
        System.out.println("organ " + org.getId() + " " + org.getName());
    }

}

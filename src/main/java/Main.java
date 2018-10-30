import db.DepartmentDAO;
import db.OrganizationDAO;
import db.PrivateChatMessageDAO;
import db.UserDAO;
import entities.Department;
import entities.Organization;
import entities.PrivateChatMessage;
import entities.User;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Set;


public class Main {

    public static void main(String[] args) {
        //beanTest();
        //restoreObjects();
        //privateChat();
        //saveChildren();
        getChildren();

    }

    private static void getChildren() {
        ApplicationContext context = new ClassPathXmlApplicationContext("app_context.xml");
        DepartmentDAO departmentDAO = (DepartmentDAO) context.getBean("departmentDao");
        Department lesoustr = departmentDAO.getDepartmentByName("Гил");
        lesoustr.setDepartmentDAO(departmentDAO);
        Department department = lesoustr.getParent();



        System.out.println();
    }

    private static void saveChildren() {
        ApplicationContext context = new ClassPathXmlApplicationContext("app_context.xml");
        OrganizationDAO organizationDAO = (OrganizationDAO) context.getBean("orgDao");
        DepartmentDAO departmentDAO = (DepartmentDAO) context.getBean("departmentDao");
        Organization org = organizationDAO.getOrgById(1);
        Department lesoustr = departmentDAO.getDepartmentByName("Отдел лесоустройства");
        Department gil = departmentDAO.getDepartmentByName("ГИЛ");
        Department monitoring = departmentDAO.getDepartmentByName("Мониторинг");
        gil.setDepartmentDAO(departmentDAO);
        monitoring.setDepartmentDAO(departmentDAO);
        lesoustr.setDepartmentDAO(departmentDAO);
        lesoustr.addChildren(gil);
        lesoustr.addChildren(monitoring);
        departmentDAO.merge(lesoustr);






    }



    private static void privateChat() {
        ApplicationContext context = new ClassPathXmlApplicationContext("app_context.xml");
        UserDAO userDao = (UserDAO) context.getBean("userDao");
        PrivateChatMessageDAO messageDao = (PrivateChatMessageDAO) context.getBean("messageDao");

        //User user1 = (User) context.getBean("user1");
        //User user2 = (User) context.getBean("user2");
        /*userDao.persistUser(user1);
        userDao.persistUser(user2);*/
        //PrivateChatMessage message = (PrivateChatMessage) context.getBean("privateMessage");
        User user1 = userDao.getUserById(3);
        User user2 = userDao.getUserById(4);
        PrivateChatMessage message = new PrivateChatMessage("сообщение", user1, user2, messageDao);
        messageDao.persist(message);




        System.out.println();

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

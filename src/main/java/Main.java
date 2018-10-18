import db.UserDAO;
import entities.User;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("app_context.xml");
        UserDAO userDao = (UserDAO) context.getBean("userDao");
        User user = (User) context.getBean("user");
        System.out.printf("PROCESS");
        userDao.addUser(user);

    }

}

import db.DAO;
import entities.User;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("app_context.xml");
        DAO dao = (DAO) context.getBean("dao");
        User user = (User) context.getBean("user");
        System.out.printf("PROCESS");
        dao.addUser(user);

    }

}

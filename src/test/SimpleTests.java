import entities.User;

public class SimpleTests {
    public static void main(String[] args) {

        User user = new User();
        user.setName("Порохин Александр Акимович");
        System.out.println(user.getNickName());
    }

}

import esn.entities.User;
import esn.entities.secondary.UserInformation;
import org.junit.Test;

import java.util.Arrays;

public class Func {

    @Test
    public void reflect(){
        User user = new User();
        user.setName("sdfsd");
        user.setPosition("dsfsdf");
        UserInformation information = new UserInformation();
        information.setEmail("sdfsdf");
        information.setPhoneWork("sdfsfssdf");
        user.setUserInformation(information);

        User user2 = new User();
        user2.setUserInformation(new UserInformation());

        Arrays.stream(user.getClass().getFields()).forEach(field -> {
            if (field != null && !field.getGenericType().equals(boolean.class) && !field.getName().equals("userInformation")){

            }
        });
    }
}

package esn.entities.converters;

import esn.db.UserDAO;
import esn.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class BossConverter implements Converter<String, User> {

    private UserDAO userDAO;

    @Autowired
    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO; //TODO delete
    }

    @Override
    public User convert(String s) {
        return null;
    }
}

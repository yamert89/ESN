package esn.db.converters;

import esn.db.UserDAO;
import esn.entities.User;

import javax.persistence.AttributeConverter;

public class UserConverter implements AttributeConverter<User, Integer> {

    private UserDAO userDAO; //TODO связь
    @Override
    public Integer convertToDatabaseColumn(User user) {
        return user.getId();
    }

    @Override
    public User convertToEntityAttribute(Integer integer) {
        return userDAO.getUserById(integer);
    }
}

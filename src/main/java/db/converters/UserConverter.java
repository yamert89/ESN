package db.converters;

import db.UserDAO;
import entities.User;

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

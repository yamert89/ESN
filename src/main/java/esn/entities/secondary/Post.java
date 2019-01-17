package esn.entities.secondary;

import esn.db.UserDAO;

import java.sql.Timestamp;

public class Post extends AbstractMessage {

    public Post(int userId, String text, Timestamp time, String orgUrl, UserDAO userDAO) {
        super(userId, text, time, orgUrl, userDAO);
    }
}

package esn.entities.secondary;

import esn.db.UserDAO;

import java.sql.Timestamp;

public class Post extends AbstractMessage {

    public Post(int id, int userId, String text, Timestamp time, int orgId, UserDAO userDAO) {
        super(id, userId, text, time, orgId, userDAO);
    }
}

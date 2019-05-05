package esn.entities.secondary;

import esn.db.UserDAO;

public class Post extends AbstractMessage {

    public Post(int id, int userId, String text, int orgId, UserDAO userDAO) {
        super(id, userId, text, orgId, userDAO);
    }
}

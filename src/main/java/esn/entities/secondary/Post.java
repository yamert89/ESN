package esn.entities.secondary;

import esn.db.UserDAO;
import esn.entities.User;

public class Post extends AbstractMessage {

    public Post(int id, String text, int orgId, User user) {
        super(id, text, orgId, user);
    }
}

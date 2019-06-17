package esn.entities.secondary;

import esn.entities.User;

public class Post extends AbstractMessage {

    public Post(long id, String text, int orgId, User user) {
        super(id, text, orgId, user);
    }
}

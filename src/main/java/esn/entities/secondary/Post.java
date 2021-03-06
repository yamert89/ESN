package esn.entities.secondary;

import esn.entities.User;

import java.sql.Timestamp;

public class Post extends AbstractMessage {

    public Post(long id, String text, Timestamp time, int orgId, User user) {
        super(id, text, time, orgId, user);
    }
}

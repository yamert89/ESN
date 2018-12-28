package esn.entities;

import java.sql.Timestamp;

public class Post extends AbstractMessage {

    public Post(int userId, String text, Timestamp time, String orgUrl) {
        super(userId, text, time, orgUrl);
    }
}

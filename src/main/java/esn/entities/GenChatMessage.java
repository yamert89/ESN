package esn.entities;

import java.sql.Timestamp;

public class GenChatMessage extends AbstractMessage{

    public GenChatMessage(int userId, String text, Timestamp time, String orgUrl) {
        super(userId, text, time, orgUrl);
    }
}

package esn.entities;

import java.sql.Timestamp;

public class GenChatMessage {

    private long userId;
    private String text;
    private Timestamp time;

    public GenChatMessage(String text, long userId, Timestamp time) {
        this.userId = userId;
        this.text = text;
        this.time = time;
    }

    public long getUserId() {
        return userId;
    }

    public String getText() {
        return text;
    }

    public Timestamp getTime() {
        return time;
    }
}

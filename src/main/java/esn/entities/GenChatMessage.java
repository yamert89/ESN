package esn.entities;

import java.sql.Timestamp;

public class GenChatMessage {

    private int userId; //TODO передать имя
    private String text;
    private Timestamp time;

    public GenChatMessage(String text, int userId, Timestamp time) {
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

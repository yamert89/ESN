package esn.entities;

import java.sql.Timestamp;

public class GenChatMessage {

    private String nameAuthor;
    private String text;
    private Timestamp time;

    public GenChatMessage(String text, String nameAuthor, Timestamp time) {
        this.nameAuthor = nameAuthor;
        this.text = text;
        this.time = time;
    }

    public String getNameAuthor() {
        return nameAuthor;
    }

    public String getText() {
        return text;
    }

    public Timestamp getTime() {
        return time;
    }
}

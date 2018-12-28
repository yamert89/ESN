package esn.entities;

import java.sql.Timestamp;

public abstract class AbstractMessage {
    private int userId; //TODO передать имя
    private String text;
    private Timestamp time;
    private String orgUrl;

    public AbstractMessage(int userId, String text, Timestamp time, String orgUrl) {
        this.userId = userId;
        this.text = text;
        this.time = time;
        this.orgUrl = orgUrl;
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

    public String getOrgUrl() {
        return orgUrl;
    }
}

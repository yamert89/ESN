package esn.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.sql.Timestamp;

@Entity
public class Session {

    @Id
    private String id;

    @ManyToOne
    private User user;

    private Timestamp startTime;

    private Timestamp endTime;

    private String ip;

    public Session() {
    }

    public Session(String id, User user, Timestamp startTime, String ip) {
        this.id = id;
        this.user = user;
        this.startTime = startTime;
        this.ip = ip;
    }

    public String getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public String getIp() {
        return ip;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }
}

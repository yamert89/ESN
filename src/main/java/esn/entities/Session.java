package esn.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Session {

    @Id
    private String id;

    @ManyToOne
    private User user;

    private long startTime;

    private long endTime;

    private String ip;

    public Session() {
    }

    public Session(String id, User user, long startTime, String ip) {
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

    public long getStartTime() {
        return startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public String getIp() {
        return ip;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }
}

package esn.entities;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Calendar;

@Entity
@Table(name = "session")
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

    public Session(String id, User user, String ip, long startTime, long endTime) {
        this.id = id;
        this.user = user;
        this.ip = ip;
        this.startTime = Timestamp.from(Instant.ofEpochMilli(startTime));
        this.endTime = Timestamp.from(Instant.ofEpochMilli(endTime));
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

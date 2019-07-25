package esn.entities;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Calendar;

@Entity
@Table(name = "session")
public class Session {

    @Id
    private String id;

    @ManyToOne
    private User user;

    private Calendar startTime = Calendar.getInstance();

    private Calendar endTime = Calendar.getInstance();

    private String ip;

    public Session() {
    }

    public Session(String id, User user, String ip, long start, long end) {
        this.id = id;
        this.user = user;
        this.ip = ip;
        startTime.setTimeInMillis(start);
        endTime.setTimeInMillis(end);
    }

    public String getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Calendar getStartTime() {
        return startTime;
    }

    public Calendar getEndTime() {
        return endTime;
    }

    public String getIp() {
        return ip;
    }

    public void setEndTime(Calendar endTime) {
        this.endTime = endTime;
    }
}

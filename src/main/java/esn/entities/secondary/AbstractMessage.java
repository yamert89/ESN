package esn.entities.secondary;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import esn.db.UserDAO;
import esn.entities.User;
import esn.entities.converters.JsonTimeSerializer;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;


public abstract class AbstractMessage {
    @JsonIgnore
    public int id;

    @JsonIgnore
    public int userId;

    public String userName;

    public String imgUrl;

    public String text;

    @JsonSerialize(using = JsonTimeSerializer.class)
    public Calendar time = Calendar.getInstance();

    @JsonIgnore
    public int orgId; //TODO delete?



    public AbstractMessage(int id, String text, int orgId, User user) {
        if (user == null) user = new User("Пользователь удалён", null);
        this.userId = user.getId();
        userName = user.getName();
        imgUrl = user.getPhoto_small();
        this.text = text;
        this.orgId = orgId;
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public String getText() {
        return text;
    }

    public Calendar getTime() {
        return time;
    }

    public int getOrgId() {
        return orgId;
    }

    public String getUserName() {
        return userName;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public int getId() {
        return id;
    }
}

package esn.entities.secondary;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import esn.entities.User;
import esn.entities.converters.JsonTimeSerializer;

import java.sql.Timestamp;
import java.util.Calendar;


public abstract class AbstractMessage {
    @JsonIgnore
    public long id;

    @JsonIgnore
    public int userId;

    public String userName;

    public String imgUrl; //TODO replace with user.getImg

    public String text;

    @JsonSerialize(using = JsonTimeSerializer.class)
    public Calendar time;

    @JsonIgnore
    public int orgId; //TODO delete?



    public AbstractMessage(long id, String text, Timestamp time, int orgId, User user) {
        this.text = text;
        this.orgId = orgId;
        this.id = id;
        this.time = Calendar.getInstance();
        this.time.setTime(time);
        if (user == null) {
            user = new User("Пользователь удалён", null);
            userName = user.getName();
            return;
        }
        this.userId = user.getId();
        userName = user.getShortName();
        imgUrl = user.getPhoto_small();

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

    public long getId() {
        return id;
    }
}

package esn.entities.secondary;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import esn.entities.User;
import esn.entities.converters.JsonTimeSerializer;

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
    public Calendar time = Calendar.getInstance();

    @JsonIgnore
    public int orgId; //TODO delete?



    public AbstractMessage(long id, String text, int orgId, User user) {
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

    public long getId() {
        return id;
    }
}

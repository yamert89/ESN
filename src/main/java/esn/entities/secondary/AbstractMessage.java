package esn.entities.secondary;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import esn.db.UserDAO;
import esn.entities.User;
import esn.entities.converters.JsonTimeSerializer;
import org.hibernate.annotations.CreationTimestamp;

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
    @CreationTimestamp
    public Calendar time;

    @JsonIgnore
    public int orgId; //TODO delete?

    @JsonIgnore
    public UserDAO userDAO;


    public AbstractMessage(int id, int userId, String text, int orgId, UserDAO userDAO) {
        this.userDAO = userDAO;
        this.userId = userId;
        User user = userDAO.getUserById(userId);
        if (user == null) user = new User("Пользователь удалён", null);
        userName = user.getName(); //TODO mb needs optimizing
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

    public UserDAO getUserDAO() {
        return userDAO;
    }

    public int getId() {
        return id;
    }
}

package esn.entities.secondary;

import esn.db.UserDAO;
import esn.entities.User;

import java.sql.Timestamp;


public abstract class AbstractMessage {
    public int userId;
    public String userName;
    public String imgUrl;
    public String text;
    public Timestamp time;
    public String orgUrl;
    public UserDAO userDAO;


    public AbstractMessage(int userId, String text, Timestamp time, String orgUrl, UserDAO userDAO) {
        this.userDAO = userDAO;
        this.userId = userId;
        User user = userDAO.getUserById(userId);
        userName = user.getName(); //TODO mb needs optimizing
        imgUrl = user.getPhoto_small();
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

    public String getUserName() {
        return userName;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public UserDAO getUserDAO() {
        return userDAO;
    }
}

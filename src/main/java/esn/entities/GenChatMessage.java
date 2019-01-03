package esn.entities;

import esn.db.UserDAO;

import java.sql.Timestamp;

public class GenChatMessage extends AbstractMessage{
    private String text = "dddddddd";

    public GenChatMessage(int userId, String text, Timestamp time, String orgUrl, UserDAO userDAO) {
        super(userId, text, time, orgUrl, userDAO);
    }

    public String getText(){
        return text;
    }

}

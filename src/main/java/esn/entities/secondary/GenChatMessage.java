package esn.entities.secondary;

import esn.db.UserDAO;

import java.sql.Timestamp;

public class GenChatMessage extends AbstractMessage{

    public GenChatMessage(int userId, String text, Timestamp time, int orgId, UserDAO userDAO) {
        super(userId, text, time, orgId, userDAO);
    }



}

package esn.entities.secondary;

import esn.db.UserDAO;

import java.sql.Timestamp;

public class GenChatMessage extends AbstractMessage{

    public GenChatMessage(int id, int userId, String text, Timestamp time, int orgId, UserDAO userDAO) {
        super(id, userId, text, time, orgId, userDAO);
    }



}

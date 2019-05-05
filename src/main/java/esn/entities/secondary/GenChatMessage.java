package esn.entities.secondary;

import esn.db.UserDAO;

public class GenChatMessage extends AbstractMessage{

    public GenChatMessage(int id, int userId, String text, int orgId, UserDAO userDAO) {
        super(id, userId, text, orgId, userDAO);
    }



}

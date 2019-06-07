package esn.entities.secondary;

import esn.db.UserDAO;
import esn.entities.User;

public class GenChatMessage extends AbstractMessage{

    public GenChatMessage(int id, String text, int orgId, User user) {
        super(id, text, orgId, user);
    }



}

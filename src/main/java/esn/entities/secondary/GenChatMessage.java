package esn.entities.secondary;

import esn.entities.User;

public class GenChatMessage extends AbstractMessage{

    public GenChatMessage(long id, String text, int orgId, User user) {
        super(id, text, orgId, user);
    }



}

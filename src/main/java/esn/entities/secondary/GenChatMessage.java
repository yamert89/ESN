package esn.entities.secondary;

import esn.entities.User;

import java.sql.Timestamp;

public class GenChatMessage extends AbstractMessage{

    public GenChatMessage(long id, String text, Timestamp time, int orgId, User user) {
        super(id, text, time, orgId, user);
    }



}

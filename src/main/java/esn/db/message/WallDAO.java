package esn.db.message;


import esn.configs.GeneralSettings;
import esn.entities.User;
import esn.entities.secondary.AbstractMessage;
import esn.entities.secondary.Post;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;

@Repository("wallDao")
@Transactional
public class WallDAO extends MessagesDAO {

    @Override
    AbstractMessage createMessage(long id, String text, Timestamp time, int orgId, User user) {
        return new Post(id, text, time, orgId, user);
    }

    @Override
    String abstractGetMessagesQuery() {
        return syntax.selectWallMessages();
    }

    @Override
    String abstractGetMessagesQueryWithIdx() {
        return syntax.selectWallMessagesWithIdx();
    }

    @Override
    int abstractGetMessagesQueryAmountMessages() {
        return GeneralSettings.AMOUNT_WALL_MESSAGES;
    }

    @Override
    String abstractTableName() {
        return "wall";
    }


}

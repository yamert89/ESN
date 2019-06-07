package esn.db.message;

import esn.entities.User;
import esn.entities.secondary.AbstractMessage;
import esn.entities.secondary.GenChatMessage;
import org.springframework.stereotype.Repository;

import java.util.Calendar;

@Repository("genDao")
public class GenDAO extends MessagesDAO {
    //all defaults logic in super class
}

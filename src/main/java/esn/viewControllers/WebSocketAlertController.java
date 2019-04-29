package esn.viewControllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import esn.db.MessagesDAO;
import esn.db.UserDAO;
import esn.entities.User;
import esn.entities.secondary.GenChatMessage;
import esn.entities.secondary.PrivateChatMessage;
import esn.entities.secondary.PrivateMesReadAlert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.sql.Timestamp;

@Controller
public class WebSocketAlertController {
    private SimpMessagingTemplate template;
    private UserDAO userDAO;
    private MessagesDAO messagesDAO;

    @Autowired
    public void setMessagesDAO(MessagesDAO messagesDAO) {
        this.messagesDAO = messagesDAO;
    }

    @Autowired
    public void setTemplate(SimpMessagingTemplate template) {
        this.template = template;
    }


    @Autowired
    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @MessageMapping("/messages")
    public void readPrivateMessageAlert(@Payload PrivateMesReadAlert mesReadAlert){

        template.convertAndSendToUser(String.valueOf(mesReadAlert.getSenderId()), "/message",
                "{\"type\" : \"private_alert_read\", \"hash\" : \"" + mesReadAlert.getHash() +"\"}");
    }

    @MessageMapping("/newmessages")
    public void askingForNewMessages(SimpMessageHeaderAccessor accessor, @Payload String orgId){
        System.out.println("_______ ASKING NEW MES__________");
        int userId = Integer.parseInt(accessor.getUser().getName());
        User user = userDAO.getUserById(userId);
        Timestamp lastVisitTime = userDAO.getLastSession(user);
        int orgID = Integer.parseInt(orgId);
        Timestamp lastGenM = messagesDAO.getLastTimeOfMessage(GenChatMessage.class, orgID);
        Timestamp lastPrivateM = messagesDAO.getLastTimeOfMessage(PrivateChatMessage.class, orgID);
        boolean gen = lastVisitTime.before(lastGenM);
        boolean private_ = lastVisitTime.before(lastPrivateM);
        String privIds = null;
        ObjectMapper om = new ObjectMapper();

        if (private_) {
            try {
                privIds = om.writeValueAsString(messagesDAO.getOfflinePrivateMSenderIds(lastVisitTime, orgID));
            }catch (JsonProcessingException e){
                e.printStackTrace();
            }
        }
        template.convertAndSendToUser(String.valueOf(userId), "",
                "{\"type\" : \"new_messages\", \"gen\":" + gen + ", \"private\" : " + private_+ ", \"private_ids\" : " + privIds + "}");



        //TODO
    }
}

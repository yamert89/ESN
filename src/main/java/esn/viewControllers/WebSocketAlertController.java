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
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import javax.persistence.NoResultException;
import java.util.Calendar;

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

    @MessageMapping("/readprivate")
    public void readPrivateMessageAlert(@Payload PrivateMesReadAlert mesReadAlert){

        template.convertAndSendToUser(String.valueOf(mesReadAlert.getSenderId()), "/message",
                "{\"type\" : \"private_alert_read\", \"hash\" : \"" + mesReadAlert.getHash() +"\"}");
    }

    @MessageMapping("/messages")
    public void askingForNewMessages(@Header String us, @Payload String orgId){
        try {
            System.out.println("_______ ASKING NEW MES__________");
            int userId = Integer.parseInt(us);
            User user = userDAO.getUserById(userId);
            Calendar lastVisitTime = null;
            try {
                lastVisitTime = userDAO.getLastSession(user);
            }catch (NoResultException e){
                System.out.println("NoResultException, first session ");
                return;
            }
            int orgID = Integer.parseInt(orgId);
            Calendar lastGenM = messagesDAO.getLastTimeOfMessage(GenChatMessage.class, orgID);
            Calendar lastPrivateM = messagesDAO.getLastTimeOfMessage(PrivateChatMessage.class, orgID);
            boolean gen = lastGenM != null && lastVisitTime.before(lastGenM);
            boolean private_ = lastPrivateM != null && lastVisitTime.before(lastPrivateM);
            String privIds = null;
            ObjectMapper om = new ObjectMapper();

            if (private_) {
                try {
                    privIds = om.writeValueAsString(messagesDAO.getOfflinePrivateMSenderIds(lastVisitTime, user, orgID));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
            template.convertAndSendToUser(String.valueOf(userId), "/message",
                    "{\"type\" : \"new_messages\", \"gen\":" + gen + ", \"private\" : " + private_ + ", \"private_ids\" : " + privIds + "}");
        }catch (Exception e){
            e.printStackTrace();
        }



        //TODO
    }
}

package esn.viewControllers;

import esn.db.UserDAO;
import esn.entities.User;
import esn.entities.secondary.PrivateMesReadAlert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketAlertController {
    private SimpMessagingTemplate template;
    private UserDAO userDAO;
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
    public void askingForNewMessages(SimpMessageHeaderAccessor accessor){
        int userId = Integer.parseInt(accessor.getUser().getName());
        User user = userDAO.getUserById(userId);
        long lastVisitTime = userDAO.getLastSession(user);
        //TODO
    }
}

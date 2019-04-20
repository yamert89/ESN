package esn.viewControllers;

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
    @Autowired
    public void setTemplate(SimpMessagingTemplate template) {
        this.template = template;
    }

    @MessageMapping("/messages")
    public void readPrivateMessageAlert(@Payload PrivateMesReadAlert mesReadAlert){

        template.convertAndSendToUser(String.valueOf(mesReadAlert.getSenderId()), "/message",
                "{\"type\" : \"private_alert_read\", \"hash\" : \"" + mesReadAlert.getHash() +"\"}");
    }

    @MessageMapping("/newmessages")
    public void askingForNewMessages(SimpMessageHeaderAccessor accessor){

    }
}

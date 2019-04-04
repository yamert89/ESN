package esn.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service("webSocketService")
public class WebSocketService {

    private SimpMessagingTemplate template;
    @Autowired
    public void setTemplate(SimpMessagingTemplate template) {
        this.template = template;
    }

    public void sendStatus(int orgId, int initiatorId, boolean on){
        template.convertAndSend("/netstatus" + orgId,
                "{\"_alert\":\"netstatus\",\"initiatorId\":" + initiatorId + ", \"statusOn\": " + on + "}");
    }

    public void newGenChatMessageAlert(int orgId, int initiatorId){
       template.convertAndSend("/genchat" + orgId, "{\"_alert\":\"genmessage\", \"initiatorId\":"+ initiatorId + "}");
    }

    public void newPostAlert(int orgId, int initiatorId){
        template.convertAndSend("/genchat" + orgId, "{\"_alert\":\"post\", \"initiatorId\":"+ initiatorId + "}");
    }

    public void newPrivateMessageAlert(int orgId, int userId){
        template.convertAndSend("/genchat" + orgId,
                "{\"_alert\":\"privatemessage\", \"uId\":" + userId + "}"); //TODO
    }






}

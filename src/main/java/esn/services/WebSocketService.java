package esn.services;

import esn.entities.User;
import esn.viewControllers.WebSocketAlertController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service("webSocketService")
public class WebSocketService {
    private final static Logger logger = LogManager.getLogger(WebSocketService.class);

    private SimpMessagingTemplate template;
    @Autowired
    public void setTemplate(SimpMessagingTemplate template) {
        this.template = template;
    }

    public void sendStatus(int orgId, int initiatorId, boolean on){
        logger.debug("send status");
        template.convertAndSend("/allusers" + orgId,
                "{\"_alert\":\"netstatus\",\"initiatorId\":" + initiatorId + ", \"statusOn\": " + on + "}");
    }

    public void newGenChatMessageAlert(User initiator, String time, String text){
        logger.debug("newGenChatMessageAlert");
        JSONObject jsonObject = new JSONObject();
        JSONObject mes = new JSONObject();
        mes.put("text", text)
                .put("userName", initiator.getShortName())
                .put("imgUrl", initiator.getPhoto_small())
                .put("time", time);
        jsonObject.put("_alert", "genmessage")
                .put("initiatorId", initiator.getId())
                .put("mes", mes);
        template.convertAndSend("/allusers" + initiator.getOrganization().getId(), jsonObject.toString());


    }


    public void newPostAlert(User initiator, String time, String text){
        logger.debug("newPostAlert");
        JSONObject jsonObject = new JSONObject();
        JSONObject mes = new JSONObject();
        mes.put("text", text)
                .put("userName", initiator.getName())
                .put("imgUrl", initiator.getPhoto_small())
                .put("time", time);
        jsonObject.put("_alert", "post")
                .put("initiatorId", initiator.getId())
                .put("mes", mes);

        template.convertAndSend("/allusers" + initiator.getOrganization().getId(), jsonObject.toString());
    }

    public void newPrivateMessageAlert(int receiverId, int senderId, String text){
        logger.debug("newPrivateMessageAlert");
               template.convertAndSendToUser(String.valueOf(receiverId), "/message",
                "{\"type\" : \"private\", \"senderId\":" + senderId + ", \"text\": \"" + text + "\"}");
    }










}

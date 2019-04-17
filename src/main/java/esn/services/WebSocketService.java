package esn.services;

import esn.entities.User;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
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

    public void newGenChatMessageAlert(User initiator, String time, String text){
        JSONObject jsonObject = new JSONObject();
        JSONObject mes = new JSONObject();
        mes.put("text", text)
                .put("userName", initiator.getName())
                .put("imgUrl", initiator.getPhoto_small())
                .put("time", time);
        jsonObject.put("_alert", "genmessage")
                .put("initiatorId", initiator.getId())
                .put("mes", mes);
        template.convertAndSend("/genchat" + initiator.getOrganization().getId(), jsonObject.toString());


    }


    public void newPostAlert(User initiator, String time, String text){
        JSONObject jsonObject = new JSONObject();
        JSONObject mes = new JSONObject();
        mes.put("text", text)
                .put("userName", initiator.getName())
                .put("imgUrl", initiator.getPhoto_small())
                .put("time", time);
        jsonObject.put("_alert", "post")
                .put("initiatorId", initiator.getId())
                .put("mes", mes);

        template.convertAndSend("/genchat" + initiator.getOrganization().getId(), jsonObject.toString());
    }

    public void newPrivateMessageAlert(int receiverId, int senderId, String text){
        receiverId = 5;
        senderId = 7;

        template.convertAndSendToUser(String.valueOf(receiverId), "/message",
                "{\"type\" : \"private\", \"senderId\":" + senderId + ", \"text\": \"" + text + "\"}");
    }

    @MessageMapping("/messages")
    public void readPrivateMessageAlert(SimpMessageHeaderAccessor accessor, int senderId){
        template.convertAndSendToUser(String.valueOf(senderId), "/message", "{\"type\" : \"private_alert_read\"}");
    }








}

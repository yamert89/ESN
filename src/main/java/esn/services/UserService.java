package esn.services;

import esn.entities.Organization;
import esn.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service("userService")
public class UserService {

    private SimpMessagingTemplate template;
    @Autowired
    public void setTemplate(SimpMessagingTemplate template) {
        this.template = template;
    }

    public void sendStatus(User user, boolean on){
        Organization org = user.getOrganization();
        template.convertAndSend("/" + org.getUrlName() + "/esn/statusalert",
                "{\"userId\":" + user.getId() + ", \"statusOn\": " + on + "}");
    }

    public void newGenChatMessageAlert(int orgId, int initiatorId){
       template.convertAndSend("/genchat" + orgId, "{\"_alert\":\"genmessage\", \"initiatorId\":"+ initiatorId + "}");
    }

    public void newPostAlert(int orgId, int initiatorId){
        template.convertAndSend("/genchat" + orgId, "{\"_alert\":\"post\", \"initiatorId\":"+ initiatorId + "}");
    }

    public void newPrivateMessageAlert(int orgId, int userId){
        template.convertAndSend("/genchat" + orgId,
                "{\"_alert\":\"privatemessage\", \"uId\":" + userId + "}");
    }






}

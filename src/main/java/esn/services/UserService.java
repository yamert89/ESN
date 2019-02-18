package esn.services;

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
        template.convertAndSend("/contlist/statusalert", "{\"userId\":" + user.getId() +
                ", \"statusOn\": " + on + "}");
    }

}

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

    public void newGenChatMessage(Organization org){
       /* String url = "/" + org.getUrlName() + "/esn/genchat";
        System.out.println("URL : " + url);
        template.convertAndSend(url,"{true}");*/
       template.convertAndSend("/genchat", "true");
    }




}

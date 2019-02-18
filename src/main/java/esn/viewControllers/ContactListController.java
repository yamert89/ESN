package esn.viewControllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.Map;



@Controller
public class ContactListController {

    private SimpMessagingTemplate template;
    @Autowired
    public void setTemplate(SimpMessagingTemplate template) {
        this.template = template;
    }

    @MessageMapping("/netstatus")
    @SendTo("/contlist/statusalert")
    public Map statusChange(Map<String, Object> data){
        return data;
    }
}

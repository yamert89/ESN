package esn.viewControllers;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class ContactListController {

    @MessageMapping("/netstatus")
    @SendTo("/contlist/statusalert")
    public String statusChange(boolean statusOn, int userId){
        return "{\"userId\":" + userId + ", \"online\":" + statusOn + "}";
    }
}

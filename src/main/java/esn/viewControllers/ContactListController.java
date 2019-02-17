package esn.viewControllers;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.Map;

@Controller
public class ContactListController {

    @MessageMapping("/netstatus")
    @SendTo("/contlist/statusalert")
    public Map statusChange(Map<Object, Object> data){


        return data;
    }
}

package esn.viewControllers;

import org.springframework.http.MediaType;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebSiteController {

    @GetMapping(value = "/")
    public String index(){
        return "index";
    }

    @GetMapping("/notice_cloud")
    public String noticeC(){
        return "notice_cloud";
    }

    @GetMapping("/notice_local")
    public String noticeL(){
        return "notice_local";
    }

}

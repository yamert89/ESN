package esn.viewControllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.PostConstruct;

@Controller
public class ClientErrorController {

    @PostMapping("/clienterror")
    public void err(@RequestBody String error){
        System.out.println(error);
    }
}

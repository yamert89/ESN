package esn.viewControllers;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.annotation.PostConstruct;

@Controller
public class ClientErrorController {

    @PostMapping("/clienterror")
    @ResponseStatus(code = HttpStatus.OK)
    public void err(@RequestBody String error){
        System.out.println("CLIENT ERROR :  " + error);
    }
}

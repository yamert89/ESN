package esn.viewControllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
public class WebSiteController {

    @GetMapping(value = "/")
    public String index(){
        return "redirect:/auth";
    }



}

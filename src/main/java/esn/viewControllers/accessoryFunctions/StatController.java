package esn.viewControllers.accessoryFunctions;

import esn.db.StatDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;

@Controller
public class StatController {

    private StatDao statDao;

    @Autowired
    public void setStatDao(StatDao statDao) {
        this.statDao = statDao;
    }

    @GetMapping("/stat")
    @ResponseBody
    @ResponseStatus(code = HttpStatus.OK)
    public void dld(@RequestParam(required = false) String dld, HttpServletRequest request){
        String path = dld.equals("l") ? "dld linux apl" : "dld windows apl";
        statDao.stat(path, request.getRemoteHost());
    }
}

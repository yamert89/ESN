package esn.viewControllers.accessoryFunctions;

import esn.db.StatDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller("/stat")
public class StatController {

    private StatDao statDao;

    @Autowired
    public void setStatDao(StatDao statDao) {
        this.statDao = statDao;
    }

    @GetMapping("/path")
    public void dld(@RequestParam(required = false) String dld, @RequestParam(required = false) String path, HttpServletRequest request){
        path = path != null ? path : dld.equals("l") ? "dld linux apl" : "dld windows apl";
        statDao.stat(path,  request.getRemoteHost());
    }
}

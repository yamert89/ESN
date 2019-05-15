package esn.viewControllers;

import esn.db.OrganizationDAO;
import esn.entities.Organization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Collections;

@Controller
public class OrgController {

    private OrganizationDAO orgDao;

    @Autowired
    public void setOrgDao(OrganizationDAO dao){
        orgDao = dao;
    }


    @GetMapping("/neworg")
    public String newOrg(Model model){
        model.addAttribute(new Organization());
        return "neworg";
    }

    @PostMapping("/neworg")
    @ResponseStatus(code = HttpStatus.CREATED)
    public String regOrgFromForm(@Valid @ModelAttribute Organization org, BindingResult result, @RequestParam String pos){
        System.out.println("positions : " + pos);
        if (result.hasErrors()) return "neworg";
        System.out.println(result.getFieldErrors().size());
        String key = new BCryptPasswordEncoder().encode(org.getName() + "3ff42fsf2423fsdf");
        System.out.println(key);
        String[] poss = pos.split("@@@");
        Collections.addAll(org.getPositions(), poss );
        org.setKey(key);
        orgDao.persistOrg(org);
        return "/infoorg"; //TODO mapping
    }
}

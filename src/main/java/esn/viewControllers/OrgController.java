package esn.viewControllers;

import esn.db.OrganizationDAO;
import esn.entities.Organization;
import esn.utils.SimpleUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.Valid;

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
    public String regOrgFromForm(@Valid Organization org, BindingResult result){
        if (result.hasErrors()) return "neworg";
        System.out.println(result.getFieldErrors().size());
        System.out.println();
        org.setKey(SimpleUtils.getEncodedPassword(org.getName() + "3ff42fsf2423fsdf"));
        orgDao.persistOrg(org);
        return "/infoorg"; //TODO mapping
    }
}

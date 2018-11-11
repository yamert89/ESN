package mvc;

import db.OrganizationDAO;
import entities.Organization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import utils.SimpleUtils;

import javax.validation.Valid;
import java.util.List;

@Controller
@SessionAttributes("organization")
public class OrgController {

    private OrganizationDAO orgDao;

    @Autowired
    public void setOrgDao(OrganizationDAO dao){
        orgDao = dao;
    }


    @RequestMapping(value = "/neworg", method = RequestMethod.GET)
    public String newOrg(Model model){
        model.addAttribute(new Organization());
        return "neworg";
    }

    @RequestMapping(value = "/neworg", method = RequestMethod.POST)
    public String regOrgFromForm(@Valid Organization org, BindingResult result,
                                 @RequestParam(value = "list", required = false) List<String> departments){
        if (result.hasErrors()) return "neworg";
        System.out.println(result.getFieldErrors().size());
        System.out.println(departments);
        orgDao.persistOrg(org);
        return SimpleUtils.getNickName(org.getName()) + "/info"; //TODO mapping
    }
}

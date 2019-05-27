package esn.viewControllers;

import esn.db.OrganizationDAO;
import esn.entities.Organization;
import esn.utils.ImageUtil;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Collections;

@Controller
@SessionAttributes("organization")
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
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String corpKey = encoder.encode(org.getName() + "3ff42fsf2423fsdf");
        String adminKey = encoder.encode(org.getName() + "3ff42fsf24hjgfdsesdf23fsdf");

        String[] poss = pos.split("@@@");
        Collections.addAll(org.getPositions(), poss );
        org.setCorpKey(corpKey);
        org.setAdminKey(adminKey);
        try {
            orgDao.persistOrg(org);
        } catch (ConstraintViolationException e){
            result.addError(new FieldError("url", "urlName", "Этот Url занят. Придумайте другой")); //TODO test
            return "neworg";
        }


        return "redirect:/" + org.getUrlName();
    }

    @GetMapping("/{org}/profile")
    /*Secured(value = "ROLE_USER")*/
    @PreAuthorize("hasRole('ROLE_ADMIN') or !@orgDao.hasAdmin(#org)")
    public String orgProfile(@PathVariable @P("org") String org, Model model, Principal principal){

        Organization organization = orgDao.getOrgByURL(org);
        model.addAttribute(organization);
        return "org_profile";
    }

    @PostMapping("/{org}/profile")
    public String profileSubmit(@Valid @ModelAttribute Organization organization, @RequestParam String pos,
                                @RequestParam MultipartFile header, @PathVariable String org, BindingResult bindingResult, Model model){
        if (bindingResult.hasErrors()) return "org_profile";
        String[] poss = pos.split("@@@");
        organization.getPositions().clear();
        Collections.addAll(organization.getPositions(), poss );
        if (!header.isEmpty()) ImageUtil.writeHeader(organization, header);
        orgDao.update(organization);
        model.addAttribute(organization);
        return "org_profile";
    }
}

package esn.viewControllers;

import esn.db.OrganizationDAO;
import esn.entities.Organization;
import esn.utils.ImageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    //@ResponseStatus(code = HttpStatus.CREATED)
    public String regOrgFromForm(@Valid @ModelAttribute Organization org, BindingResult result, @RequestParam(required = false) String pos){
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
        } catch (DataIntegrityViolationException e){
            result.addError(new FieldError("url", "urlName", "Этот Url занят. Придумайте другой"));
            return "neworg";
        }
        return "redirect:/" + org.getUrlName() + "/profile";
    }

    @GetMapping("/{org}/profile")
    /*Secured(value = "ROLE_USER")*/
    @PreAuthorize("!@orgDao.hasAdmin(#org) or hasRole('ROLE_ADMIN')")
    public String orgProfile(@PathVariable @P("org") String org, Model model){

        Organization organization = orgDao.getOrgByURL(org);
        model.addAttribute(organization);
        return "org_profile";
    }

    @PostMapping("/{org}/profile")
    public String profileSubmit(@Valid @ModelAttribute Organization organization, @RequestParam String pos,
                                @RequestParam MultipartFile header, @PathVariable String org, BindingResult bindingResult, Model model, HttpSession session){
        Organization orgFromSession = (Organization) session.getAttribute("org");
        if (bindingResult.hasErrors()) return "org_profile";
        String[] poss = pos.split("@@@");
        orgFromSession.getPositions().clear();
        Collections.addAll(orgFromSession.getPositions(), poss );
        if (!header.isEmpty()) ImageUtil.writeHeader(organization, header);
        orgFromSession.updateFromForm(organization);
        orgDao.update(orgFromSession);
        model.addAttribute(orgFromSession);
        return "org_profile";
    }
}

package esn.viewControllers;

import esn.db.OrganizationDAO;
import esn.entities.Organization;
import esn.utils.ImageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Controller
public class OrgController {

    private OrganizationDAO orgDao;

    @Autowired
    public void setOrgDao(OrganizationDAO dao){
        orgDao = dao;
    }


    @GetMapping("/neworg")
    public String newOrg(Model model){
        model.addAttribute("org", new Organization());
        return "neworg";
    }

    @PostMapping("/neworg")
    //@ResponseStatus(code = HttpStatus.CREATED)
    public String regOrgFromForm(@Valid @ModelAttribute Organization org, BindingResult result, @RequestParam(required = false) String pos, HttpSession session){
        System.out.println("positions : " + pos);
        if (result.hasErrors()) return "neworg";
        System.out.println(result.getFieldErrors().size());
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String corpKey = encoder.encode(org.getName() + "3ff42fsf2423fsdf");
        String adminKey = encoder.encode(org.getName() + "3ff42fsf24hjgfdsesdf23fsdf");

        String[] poss = pos.split("@@@");
        Collections.addAll(org.getPositions(), poss );
        if (org.getPositions() == null) org.setPositions(new HashSet<>());
        org.getPositions().clear();
        org.setCorpKey(corpKey);
        org.setAdminKey(adminKey);
        try {
            orgDao.persistOrg(org);
        } catch (DataIntegrityViolationException e){
            result.addError(new FieldError("url", "urlName", "Этот Url занят. Придумайте другой"));
            return "neworg";
        }
        session.setAttribute("org", org);
        return "redirect:/" + org.getUrlName() + "/profile";
    }

    @GetMapping("/{organ}/profile")
    /*Secured(value = "ROLE_USER")*/
    @PreAuthorize("!@orgDao.hasAdmin(#organ) or hasRole('ROLE_ADMIN')")
    public ModelAndView orgProfile(@PathVariable @P("organ") String organ, Model model, HttpSession session, RedirectAttributes redirectAttributes){
        ModelAndView modelAndView = new ModelAndView("org_profile");
        Organization organization = orgDao.getOrgByURL(organ);
        if (organization == null){
            RedirectView rw = new RedirectView("error");
            rw.setStatusCode(HttpStatus.NOT_FOUND);
            redirectAttributes.addAttribute("status", 404);
            modelAndView.setViewName("redirect:/error");
            //modelAndView.setStatus(HttpStatus.NOT_FOUND);
            return modelAndView;
        }

        session.setAttribute("org", organization);
        modelAndView.addObject("org", organization);
        //model.addAttribute("org", organization);
        return modelAndView;
    }

    @PostMapping("/{organ}/profile")
    public String profileSubmit(@Valid @ModelAttribute Organization org, @RequestParam String pos,
                                @RequestParam MultipartFile header, @PathVariable String organ, BindingResult bindingResult, Model model, HttpSession session){
        Organization orgFromSession = (Organization) session.getAttribute("org");
        if (bindingResult.hasErrors()) return "org_profile";
        String[] poss = pos.split("@@@");
        Set<String> positions = orgFromSession.getPositions();
        if (positions == null) orgFromSession.setPositions(new HashSet<>());
        orgFromSession.getPositions().clear();
        Collections.addAll(orgFromSession.getPositions(), poss );
        /*String headerPath*/
        if (!header.isEmpty()) ImageUtil.writeHeader(orgFromSession, header);
        orgFromSession.updateFromForm(org);
        orgDao.update(orgFromSession);
        model.addAttribute("org", orgFromSession);
        return "org_profile";
    }
}

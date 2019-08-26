package esn.viewControllers;

import esn.db.OrganizationDAO;
import esn.db.UserDAO;
import esn.entities.Organization;
import esn.utils.ImageUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
import java.util.Calendar;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Controller
public class OrgController {
    private final static Logger logger = LogManager.getLogger(OrgController.class);

    private OrganizationDAO orgDAO;
    private UserDAO userDAO;

    @Autowired
    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Autowired
    public void setOrgDao(OrganizationDAO dao){
        orgDAO = dao;
    }


    @GetMapping("/neworg")
    public String newOrg(Model model){
        model.addAttribute("org", new Organization());
        return "neworg";
    }

    @PostMapping("/neworg")
    @ResponseStatus(code = HttpStatus.SEE_OTHER)
    public String regOrgFromForm(@Valid @ModelAttribute Organization org, BindingResult result, @RequestParam(required = false) String pos, HttpSession session){
        logger.debug("positions : " + pos);
        if (result.hasErrors()) return "neworg";
        logger.debug(result.getFieldErrors().size());
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String corpKey = encoder.encode(org.getName() + System.currentTimeMillis() * Math.random());
        String adminKey = encoder.encode(org.getName() + System.currentTimeMillis() * Math.random());

        if (!pos.equals("")){
            String[] poss = pos.split(", ");
            if (org.getPositions() == null) org.setPositions(new HashSet<>());
            org.getPositions().clear();
            Collections.addAll(org.getPositions(), poss );
        }

        org.setCorpKey(corpKey);
        org.setAdminKey(adminKey);
        org.setHeaderPath("/app/header.jpg");
        org.setRegisterDate(Calendar.getInstance());
        try {
            if (org.getUrlName().equals("app")) throw new DataIntegrityViolationException("app url");
            org = orgDAO.update(org);
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
        Organization organization = orgDAO.getOrgByURL(organ);
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
        String[] poss = pos.split(", ");
        Set<String> positions = orgFromSession.getPositions();
        if (positions == null) orgFromSession.setPositions(new HashSet<>());
        orgFromSession.getPositions().clear();
        Collections.addAll(orgFromSession.getPositions(), poss );
        /*String headerPath*/
        if (!header.isEmpty()) ImageUtil.writeHeader(orgFromSession, header);
        orgFromSession.updateFromForm(org);
        orgDAO.update(orgFromSession);
        model.addAttribute("org", orgFromSession);
        model.addAttribute("saved", true);
        return "org_profile";
    }

    @GetMapping("/delete_org")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOrg(HttpSession session){
        Organization organization = (Organization) session.getAttribute("org");
        organization.setDisabled(true);
        orgDAO.update(organization);
    }
}

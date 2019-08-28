package esn.viewControllers.main;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import esn.configs.GeneralSettings;
import esn.db.message.GenDAO;
import esn.entities.Organization;
import esn.entities.User;
import esn.entities.secondary.AbstractMessage;
import esn.services.WebSocketService;
import esn.utils.DateFormatUtil;
import esn.viewControllers.accessoryFunctions.SessionUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.List;

@Controller
public class GenChatController {

    private final static Logger logger = LogManager.getLogger(GenChatController.class);


    private GenDAO genDAO;
    private WebSocketService webSocketService;
    private HttpHeaders headers;
    private SessionUtil sessionUtil;

    @Autowired
    public void setSessionUtil(SessionUtil sessionUtil) {
        this.sessionUtil = sessionUtil;
    }

    @Autowired
    public void setHeaders(HttpHeaders headers) {
        this.headers = headers;
    }

    @Autowired
    public void setGenDAO(GenDAO genDAO) {
        this.genDAO = genDAO;
    }

    @Autowired
    public void setWebSocketService(WebSocketService webSocketService) {
        this.webSocketService = webSocketService;
    }

    @GetMapping("/{organization}/chat")
    public ModelAndView genChat(Model model, HttpSession session, HttpServletRequest request,
                                RedirectAttributes redirectAttributes, Principal principal){
        ModelAndView modelAndView = new ModelAndView("gen_chat");
        try {
            User user = sessionUtil.getUser(request, principal);
            Organization org = sessionUtil.getOrg(request, principal);
            int orgId = org.getId();

            modelAndView.addObject("photo", user.getPhoto_small());
            List<AbstractMessage> messages = genDAO.getMessages(orgId, -1);
            long newIdx = messages.size() < GeneralSettings.AMOUNT_GENCHAT_MESSAGES ? -1 : messages.get(messages.size() - 1).getId();
            session.setAttribute("lastIdx_genchat", newIdx);
            modelAndView.addObject("messages", messages);
        }catch (Exception e){
            logger.error("genchat", e);
            redirectAttributes.addAttribute("status", 500);
            modelAndView.setViewName("redirect:/error");
        }
        return modelAndView;
    }

    @PostMapping("/savemessage")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void saveMessage(@RequestParam String text,
                            @RequestParam String time, HttpServletRequest request, Principal principal){
        User user = sessionUtil.getUser(request, principal);
        int orgId = user.getOrganization().getId();

        try {
            genDAO.saveMessage(user.getId(), text, DateFormatUtil.parseDate(time), orgId);
            webSocketService.newGenChatMessageAlert(user, time, text);

        }catch (Exception e){
            logger.error(e.getMessage(), e);
        }
    }

    @PostMapping("/deletemessage") //TODO удалять у других через ws
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteGenMessage(@RequestParam String text, HttpServletRequest request, Principal principal){
        User user = sessionUtil.getUser(request, principal);
        //int orgId = ((Organization) session.getAttribute("org")).getId();
        genDAO.deleteMessage(user.getId(), text);
    }

    @GetMapping("/chatpiece")
    @ResponseBody
    public ResponseEntity<String> getChatPiece(HttpSession session){
        int orgId = ((Organization) session.getAttribute("org")).getId();
        long oldIndex = (long) session.getAttribute("lastIdx_genchat");
        if (oldIndex == -1) return ResponseEntity.ok("{}");
        List<AbstractMessage> messages = genDAO.getMessages(orgId, oldIndex);
        long newIdx = messages.size() < GeneralSettings.AMOUNT_GENCHAT_MESSAGES ? -1 : messages.get(messages.size() - 1).getId();
        session.setAttribute("lastIdx_genchat", newIdx);
        ObjectMapper om = new ObjectMapper();
        String json = "";
        try {
            json = om.writeValueAsString(messages);
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage(), e);
        }
        return ResponseEntity.ok().headers(headers).body(json);
    }

}

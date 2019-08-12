package esn.viewControllers.main;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import esn.configs.GeneralSettings;
import esn.db.DepartmentDAO;
import esn.db.GlobalDAO;
import esn.db.OrganizationDAO;
import esn.db.UserDAO;
import esn.db.message.GenDAO;
import esn.db.message.PrivateDAO;
import esn.db.message.WallDAO;
import esn.entities.Organization;
import esn.entities.User;
import esn.entities.secondary.AbstractMessage;
import esn.services.WebSocketService;
import esn.utils.DateFormatUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class GenChatController {

    private final static Logger logger = LogManager.getLogger(GenChatController.class);


    private GenDAO genDAO;
    private WebSocketService webSocketService;

    @Autowired
    public void setGenDAO(GenDAO genDAO) {
        this.genDAO = genDAO;
    }

    @Autowired
    public void setWebSocketService(WebSocketService webSocketService) {
        this.webSocketService = webSocketService;
    }

    @GetMapping("/{organization}/chat")
    public String genChat(Model model, HttpSession session){
        Organization org = (Organization) session.getAttribute("org");
        int orgId = org.getId();
        User user = (User) session.getAttribute("user");
        model.addAttribute("photo", user.getPhoto_small());
        List<AbstractMessage> messages = genDAO.getMessages(orgId, -1);
        long newIdx = messages.size() < GeneralSettings.AMOUNT_GENCHAT_MESSAGES ? -1 : messages.get(messages.size() - 1).getId();
        session.setAttribute("lastIdx_genchat", newIdx);
        model.addAttribute("messages", messages);
        return "gen_chat";
    }

    @PostMapping("/savemessage")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void saveMessage(@RequestParam String userId, @RequestParam String text,
                            @RequestParam String time, HttpSession session){
        int orgId = ((Organization) session.getAttribute("org")).getId();
        User user = (User) session.getAttribute("user");
        try {
            genDAO.saveMessage(user.getId(), text, DateFormatUtil.parseDate(time), orgId);
            webSocketService.newGenChatMessageAlert(user, time, text);

        }catch (Exception e){
            logger.error(e.getMessage(), e);
        }
    }

    @PostMapping("/deletemessage") //TODO удалять у других через ws
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteGenMessage(@RequestParam String text, HttpSession session){
        User user = (User) session.getAttribute("user");
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
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Content-Type", "application/json; charset=UTF-8");
        return ResponseEntity.ok().headers(responseHeaders).body(json);
    }

}

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
public class WallController {

    private WallDAO wallDAO;
    private WebSocketService webSocketService;

    @Autowired
    public void setWallDAO(WallDAO wallDAO) {
        this.wallDAO = wallDAO;
    }

    @Autowired
    public void setWebSocketService(WebSocketService webSocketService) {
        this.webSocketService = webSocketService;
    }

    @GetMapping(value = "/{organization}/wall")
    public String wall(Model model, HttpSession session){
        Organization org = (Organization) session.getAttribute("org");
        int orgId = org.getId();
        List<AbstractMessage> messages = wallDAO.getMessages(orgId, -1);
        long newIdx = messages.size() < GeneralSettings.AMOUNT_WALL_MESSAGES ? -1 : messages.get(messages.size() - 1).getId();
        session.setAttribute("lastIdx_wall", newIdx);
        model.addAttribute("messages", messages);
        return "wall";
    }

    @PostMapping("/savepost")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void savePost(@RequestParam String userId, @RequestParam String text,
                         @RequestParam String time, HttpSession session){
        User user = (User) session.getAttribute("user");
        int orgId = ((Organization) session.getAttribute("org")).getId();
        try {
            wallDAO.saveMessage(user.getId(), text, DateFormatUtil.parseDate(time), orgId);
            webSocketService.newPostAlert(user, time, text);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @PostMapping("/deletepost")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deletePost(@RequestParam String text, HttpSession session){
        User user = (User) session.getAttribute("user");
        //int orgId = ((Organization) session.getAttribute("org")).getId();
        wallDAO.deleteMessage(user.getId(), text);
    }

    @GetMapping("/wallpiece")
    @ResponseBody
    public ResponseEntity<String> getWallPiece(HttpSession session){
        int orgId = ((Organization) session.getAttribute("org")).getId();
        int oldIndex = (int) session.getAttribute("lastIdx_wall");
        if (oldIndex == -1) return null;
        List<AbstractMessage> messages = wallDAO.getMessages(orgId, oldIndex);
        long newIdx = messages.size() < GeneralSettings.AMOUNT_WALL_MESSAGES ? -1 : messages.get(messages.size() - 1).getId();
        session.setAttribute("lastIdx_wall", newIdx);
        ObjectMapper om = new ObjectMapper();
        String json = "";
        try {
            json = om.writeValueAsString(messages);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Content-Type", "application/json; charset=UTF-8");
        return ResponseEntity.ok().headers(responseHeaders).body(json);
    }
}

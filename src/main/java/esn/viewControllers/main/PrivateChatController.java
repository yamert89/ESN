package esn.viewControllers.main;

import esn.db.DepartmentDAO;
import esn.db.GlobalDAO;
import esn.db.OrganizationDAO;
import esn.db.UserDAO;
import esn.db.message.GenDAO;
import esn.db.message.PrivateDAO;
import esn.db.message.WallDAO;
import esn.entities.Organization;
import esn.entities.User;
import esn.entities.secondary.ContactGroup;
import esn.entities.secondary.PrivateChatMessage;
import esn.services.WebSocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

@Controller
public class PrivateChatController {


    private UserDAO userDAO;
    private PrivateDAO privateDAO;
    private WebSocketService webSocketService;

    @Autowired
    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Autowired
    public void setPrivateDAO(PrivateDAO privateDAO) {
        this.privateDAO = privateDAO;
    }

    @Autowired
    public void setWebSocketService(WebSocketService webSocketService) {
        this.webSocketService = webSocketService;
    }

    @GetMapping("/{organization}/private-chat/{companion}")
    public String privateChat(@PathVariable String organization,
                              @PathVariable String companion, Model model, HttpSession session){
        Organization org = (Organization) session.getAttribute("org");
        int orgId = org.getId();
        User user = (User) session.getAttribute("user");
        User compan = userDAO.getUserByLogin(companion);
        model.addAttribute("companion", compan);
        Set<PrivateChatMessage> privateMessages = privateDAO.getMessages(user, compan, orgId);

        Map<PrivateChatMessage, Boolean> messages = new TreeMap<>();
        if (privateMessages == null) {
            model.addAttribute("messages", null);
            return "private_chat";
        }
        Long[] ids = new Long[privateMessages.size()];
        int i = 0;
        for (PrivateChatMessage mes :
                privateMessages) {
            Boolean userMessage = mes.getSender_id() == user.getId();
            if (!userMessage) {
                mes.setReaded(true);
                ids[i++] = mes.getId();
            }
            messages.put(mes, userMessage);

        }
        privateDAO.updateReadedMessages(ids);

        model.addAttribute("messages", messages);
        return "private_chat";
    }

    @PostMapping("/save_private_message/{companionId}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void savePrivateMessage(@RequestParam String text, @PathVariable String companionId,
                                   HttpSession session){
        User user = (User) session.getAttribute("user");
        int orgId = ((Organization) session.getAttribute("org")).getId();
        int cId = Integer.valueOf(companionId);
        User compan = userDAO.getUserById(cId);
        if (text.length() > 800) {
            String[] messages = text.split(".{800}"); //TODO test
            for (String m :
                    messages) {
                privateDAO.persist(new PrivateChatMessage(m, user.getId(), compan.getId(), orgId));
            }
        }
        privateDAO.persist(new PrivateChatMessage(text, user.getId(), compan.getId(), orgId));
        webSocketService.newPrivateMessageAlert(cId, user.getId(), text);
    }


    @PostMapping("/groupmessage")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void saveGroupMessage(@RequestParam String text, @RequestParam String groupName,
                                 HttpSession session){
        User user = (User) session.getAttribute("user");
        int orgId = ((Organization) session.getAttribute("org")).getId();
        ContactGroup group = user.getGroups().stream()
                .filter(g -> g.getName().equals(groupName)).findAny().get();

        for (int id :
                group.getPersonIds()) {
            privateDAO.persist(new PrivateChatMessage(text, user.getId(), id, orgId));
        }


    }
}
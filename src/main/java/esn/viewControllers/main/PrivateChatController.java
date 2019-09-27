package esn.viewControllers.main;

import esn.db.message.PrivateDAO;
import esn.entities.Organization;
import esn.entities.User;
import esn.entities.secondary.ContactGroup;
import esn.entities.secondary.PrivateChatMessage;
import esn.services.LiveStat;
import esn.services.WebSocketService;
import esn.viewControllers.WebSocketAlertController;
import esn.viewControllers.accessoryFunctions.SessionUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

@Controller
public class PrivateChatController {



    private PrivateDAO privateDAO;
    private WebSocketService webSocketService;
    private WebSocketAlertController webSocketAlertController;
    private LiveStat liveStat;
    private SessionUtil sessionUtil;
    private final static Logger logger = LogManager.getLogger(PrivateChatController.class);

    @Autowired
    public void setSessionUtil(SessionUtil sessionUtil) {
        this.sessionUtil = sessionUtil;
    }

    @Autowired
    public void setLiveStat(LiveStat liveStat) {
        this.liveStat = liveStat;
    }

    @Autowired
    public void setWebSocketAlertController(WebSocketAlertController webSocketAlertController) {
        this.webSocketAlertController = webSocketAlertController;
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
    public ModelAndView privateChat(@PathVariable String organization, RedirectAttributes redirectAttributes,
                                    @PathVariable String companion, Model model, HttpSession session,
                                    HttpServletRequest request, Principal principal){
        ModelAndView modelAndView = new ModelAndView("private_chat");
        try {
            User user = sessionUtil.getUser(request, principal);
            Organization org = sessionUtil.getOrg(request, principal);
            int orgId = org.getId();

            // User compan = userDAO.getUserByLogin(companion);
            User compan = org.getEmployerByLogin(companion);
            model.addAttribute("companion", compan);
            Set<PrivateChatMessage> privateMessages = privateDAO.getMessages(user, compan, orgId);

            Map<PrivateChatMessage, Boolean> messages = new TreeMap<>();
            if (privateMessages == null) {
                modelAndView.addObject("messages", null);
                return modelAndView;
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
            webSocketAlertController.readPrivateMessageAlertAll(compan.getId());
            modelAndView.addObject("messages", messages);
            modelAndView.addObject("online", liveStat.userIsOnline(compan.getId()));
        }catch (Exception e){
            logger.error("privateChat", e);
            redirectAttributes.addFlashAttribute("flash", "Произошла ошибка при получении личных сообщений. Сообщите разработчику");
            redirectAttributes.addAttribute("status", 777);
            modelAndView.setViewName("redirect:/error");
        }
        return modelAndView;
    }

    @PostMapping("/save_private_message/{companionId}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void savePrivateMessage(@RequestParam String text, @PathVariable String companionId,
                                   HttpSession session, HttpServletRequest request, Principal principal){
        try {
            User user = sessionUtil.getUser(request, principal);
            int orgId = ((Organization) session.getAttribute("org")).getId();
            int cId = Integer.valueOf(companionId);

       /* if (text.length() > 800) {
            String[] messages = text.split(".{800}");
            for (String m :
                    messages) {
                privateDAO.persist(new PrivateChatMessage(m, user.getId(), compan.getId(), orgId));
            }
        }*/
            privateDAO.persist(new PrivateChatMessage(text, user.getId(), cId, orgId));
            webSocketService.newPrivateMessageAlert(cId, user.getId(), text);
        }catch (Exception e){
            logger.error("save private message", e);
        }
    }


    @PostMapping("/groupmessage")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void saveGroupMessage(@RequestParam String text, @RequestParam String groupName,
                                 HttpSession session, HttpServletRequest request, Principal principal){
        Set<PrivateChatMessage> messages = new HashSet<>();
        try {
            User user = sessionUtil.getUser(request, principal);
            int orgId = user.getOrganization().getId();
            Set<ContactGroup> groups = user.getGroups();
            int[] ids = null;
            if (groups.size() > 0){
                ContactGroup group = groups.stream()
                        .filter(g -> g.getName().equals(groupName)).findAny().get();
                ids = group.getPersonIds();

            } else {
                ids = user.getOrganization().getAllEmployers().stream().mapToInt(el -> el.getId()).toArray();
            }

            for (int id : ids) {
                messages.add(new PrivateChatMessage(text, user.getId(), id, orgId));
                webSocketService.newPrivateMessageAlert(id, user.getId(), text);
            }



            privateDAO.persist(messages.toArray(new PrivateChatMessage[]{}));

        }catch (Exception e){logger.error("saveGroupMessage", e);}


    }
}

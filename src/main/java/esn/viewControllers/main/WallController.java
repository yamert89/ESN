package esn.viewControllers.main;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import esn.configs.GeneralSettings;
import esn.db.message.WallDAO;
import esn.entities.Organization;
import esn.entities.User;
import esn.entities.secondary.AbstractMessage;
import esn.services.WebSocketService;
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
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.List;

@Controller
public class WallController {

    private final static Logger logger = LogManager.getLogger(GroupsController.class);

    private WallDAO wallDAO;
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
    public void setWallDAO(WallDAO wallDAO) {
        this.wallDAO = wallDAO;
    }

    @Autowired
    public void setWebSocketService(WebSocketService webSocketService) {
        this.webSocketService = webSocketService;
    }

    @GetMapping(value = "/{organization}/wall")
    public ModelAndView wall(Model model, HttpSession session, HttpServletRequest request, Principal principal, RedirectAttributes redirectAttributes){
        ModelAndView modelAndView = new ModelAndView("wall");
        try {
            User user = sessionUtil.getUser(request, principal);
            Organization org = user.getOrganization();
            int orgId = org.getId();
            List<AbstractMessage> messages = wallDAO.getMessages(orgId, -1);
            long newIdx = messages.size() < GeneralSettings.AMOUNT_WALL_MESSAGES ? -1 : messages.get(messages.size() - 1).getId();
            session.setAttribute("lastIdx_wall", newIdx);
            model.addAttribute("messages", messages);
            modelAndView.addObject("filesPath", "/resources/data/" + org.getUrlName() + "/stored_files/" + user.getLogin() + "/");
        }catch (Exception e){
            logger.error("wall", e);
            redirectAttributes.addFlashAttribute("flash", "Произошла загрузки главной страницы. Сообщите разработчику");
            redirectAttributes.addAttribute("status", 777);
            modelAndView.setViewName("redirect:/error");
        }
        return modelAndView;
    }

    @PostMapping("/savepost")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void savePost(@RequestBody byte[] text,
                          HttpSession session, HttpServletRequest request, Principal principal){

        try {
            User user = sessionUtil.getUser(request, principal);
            Timestamp time = Timestamp.from(Instant.now());
            String txt = new String(text, StandardCharsets.UTF_8);
            logger.debug(txt);
            wallDAO.saveMessage(user.getId(), txt, time, user.getOrganization().getId());
            SimpleDateFormat dateFormat = new SimpleDateFormat(GeneralSettings.TIME_PATTERN);
            webSocketService.newPostAlert(user, dateFormat.format(time), txt);
        }catch (Exception e){
            logger.error(e.getMessage(), e);
        }
    }

    @PostMapping("/deletepost")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deletePost(@RequestParam String text, HttpServletRequest request, Principal principal){
        try {
            User user = sessionUtil.getUser(request, principal);
            wallDAO.deleteMessage(user.getId(), text);
        }catch (Exception e){logger.error("deletePost", e);}
    }

    @GetMapping("/wallpiece")
    @ResponseBody
    public ResponseEntity<String> getWallPiece(HttpSession session){
        String json = "";
        try {
            long newIdx = 0L;
            List<AbstractMessage> messages = null;
            try {
                int orgId = ((Organization) session.getAttribute("org")).getId();
                Object i = session.getAttribute("lastIdx_wall");
                long oldIndex = i == null ? -1 : (long)i;
                if (oldIndex == -1) return ResponseEntity.ok("{}");
                messages = wallDAO.getMessages(orgId, oldIndex);
                newIdx = messages.size() < GeneralSettings.AMOUNT_WALL_MESSAGES ? -1 : messages.get(messages.size() - 1).getId();
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
            session.setAttribute("lastIdx_wall", newIdx);
            ObjectMapper om = new ObjectMapper();

            try {
                json = om.writeValueAsString(messages);
            } catch (JsonProcessingException e) {
                logger.error("jsonProcessing", e);
            }
        }catch (Exception e){logger.error("getWallPiece", e);}

        return ResponseEntity.ok().headers(headers).body(json);
    }
}

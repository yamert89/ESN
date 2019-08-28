package esn.viewControllers.main;

import esn.db.UserDAO;
import esn.entities.User;
import esn.utils.DateFormatUtil;
import esn.viewControllers.accessoryFunctions.SessionUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class NotesController {
    private final static Logger logger = LogManager.getLogger(NotesController.class);

    private UserDAO userDAO;
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
    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @GetMapping(value = "/{organization}/notes")
    public String notes(HttpServletRequest request, Principal principal){
        User user = sessionUtil.getUser(request, principal);
        logger.debug(user.getName() + " get notes");
        return "notes";
    }

    @PostMapping("/note")
    @ResponseStatus(code = HttpStatus.OK)
    public boolean saveNote(@RequestParam String time, @RequestParam String text, HttpSession session, HttpServletRequest request, Principal principal){
        User user = sessionUtil.getUser(request, principal);
        try {
            logger.debug(" /note   before   " + user);
            //time = "15.03.2019, 00:00:00";
            Timestamp timestamp = DateFormatUtil.parseDate(time);
            /*Timestamp timestamp = Timestamp.valueOf(LocalDateTime.parse(time, DateTimeFormatter.ofPattern(TIME_PATTERN)));*/
            //Timestamp timestamp = Timestamp.valueOf(LocalDateTime.parse(time));
            user.getNotes().put(timestamp, text);
            user = userDAO.updateUser(user);
            //user = userDAO.getUserById(user.getId());
            session.setAttribute("user", user);
        }catch (Exception e){
            logger.error(e.getMessage(), e);
            return false;
        }
        logger.debug(" /note   after  " + user);
        return true;
    }

    @GetMapping("/notes")
    public ResponseEntity<String> getNotes(HttpServletRequest request, Principal principal){
        User user = sessionUtil.getUser(request, principal);
        try {
            Map<Timestamp, String> notes = user.getNotes();
            Calendar today = Calendar.getInstance();
            //window.dates = [{m:1, d:13, t:"text1"}, {m:2, d:1, t:"text2"}, {m:2, d:12, t:"text3"}]; // new structure
            Timestamp thisYear = new Timestamp(today.getTimeInMillis());
            thisYear.setMonth(0);
            thisYear.setDate(1);
            thisYear.setHours(0);


            Map<Timestamp, String> sortedNotes = notes.entrySet().stream().filter(date -> date.getKey()
                    .after(thisYear))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

            List<String> nodes = sortedNotes.entrySet().stream().peek(el -> {
                Timestamp time = el.getKey();
                String sb = "{\"m\":" + time.getMonth() + ", \"d\":" + time.getDate() + ", \"t\":\"" + el.getValue() +
                        "\"}";
                el.setValue(sb);
            }).map(Map.Entry::getValue).collect(Collectors.toList());

            StringBuilder sb = new StringBuilder("[");
            String prefix = "";
            for (String node : nodes) {
                sb.append(prefix);
                prefix = ",";
                sb.append(node);
            }
            sb.append("]");
            return ResponseEntity.ok().headers(headers).body(sb.toString());

        }catch (Exception e){
            logger.error(e.getMessage(), e);
        }
        return null;


    }
}

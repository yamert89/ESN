package esn.viewControllers.main;

import esn.db.DepartmentDAO;
import esn.db.GlobalDAO;
import esn.db.OrganizationDAO;
import esn.db.UserDAO;
import esn.db.message.GenDAO;
import esn.db.message.PrivateDAO;
import esn.db.message.WallDAO;
import esn.entities.User;
import esn.services.WebSocketService;
import esn.utils.DateFormatUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class NotesController {

    private UserDAO userDAO;

    @Autowired
    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @GetMapping(value = "/{organization}/notes")
    public String notes(HttpSession session){
        User user = (User) session.getAttribute("user");
        /*System.out.println(" /notes Main   " + user);
        session.setAttribute("user", userDAO.getUserById(user.getId()));*/
        return "notes";
    }

    @PostMapping("/note")
    @ResponseStatus(code = HttpStatus.OK)
    public boolean saveNote(@RequestParam String time, @RequestParam String text, HttpSession session){
        User user = (User) session.getAttribute("user");
        try {
            System.out.println(" /note   before   " + user);
            //time = "15.03.2019, 00:00:00";
            Timestamp timestamp = DateFormatUtil.parseDate(time);
            /*Timestamp timestamp = Timestamp.valueOf(LocalDateTime.parse(time, DateTimeFormatter.ofPattern(TIME_PATTERN)));*/
            //Timestamp timestamp = Timestamp.valueOf(LocalDateTime.parse(time));
            user.getNotes().put(timestamp, text);
            user = userDAO.updateUser(user);
            //user = userDAO.getUserById(user.getId());
            session.setAttribute("user", user);
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println(" /note   after  " + user);
        return true;
    }

    @GetMapping("/notes")
    public ResponseEntity<String> getNotes(HttpSession session){
        User user = (User) session.getAttribute("user");
        try {
            System.out.println(" /notes   " + user);
            Map<Timestamp, String> notes = user.getNotes();
            Calendar today = Calendar.getInstance();
            //window.dates = [{m:1, d:13, t:"text1"}, {m:2, d:1, t:"text2"}, {m:2, d:12, t:"text3"}]; //TODO new structure
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
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.set("Content-Type", "application/json; charset=UTF-8");
            return ResponseEntity.ok().headers(responseHeaders).body(sb.toString());

        }catch (Exception e){
            e.printStackTrace();
        }
        return null;


    }
}

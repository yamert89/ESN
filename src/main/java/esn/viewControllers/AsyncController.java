package esn.viewControllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import esn.configs.GeneralSettings;
import esn.db.*;
import esn.entities.Department;
import esn.entities.Organization;
import esn.entities.User;
import esn.entities.secondary.*;
import esn.services.WebSocketService;
import esn.utils.DateFormatUtil;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
@SessionAttributes({"user", "orgId"}) //TODO test mb problem with update user ?
public class AsyncController {

    private GlobalDAO globalDAO;
    private OrganizationDAO orgDAO;
    private UserDAO userDAO;
    private DepartmentDAO departmentDAO;
    private MessagesDAO messagesDAO;
    private WebSocketService webSocketService;

    @Autowired
    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Autowired
    public void setGlobalDAO(GlobalDAO globalDAO) {
        this.globalDAO = globalDAO;
    }
    @Autowired
    public void setOrgDAO(OrganizationDAO orgDAO) {
        this.orgDAO = orgDAO;
    }
    @Autowired
    public void setDepartmentDAO(DepartmentDAO departmentDAO) {
        this.departmentDAO = departmentDAO;
    }
    @Autowired
    public void setMessagesDAO(MessagesDAO messagesDAO) {
        this.messagesDAO = messagesDAO;
    }
    @Autowired
    public void setWebSocketService(WebSocketService webSocketService) {
        this.webSocketService = webSocketService;
    }

    @PostMapping("/savemessage")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void saveMessage(@RequestParam String userId, @RequestParam String text,
                            @RequestParam String time, @SessionAttribute int orgId, @SessionAttribute User user){
        try {
            messagesDAO.saveMessage(user.getId(), text, DateFormatUtil.parseDate(time), orgId, GenChatMessage.class);
            webSocketService.newGenChatMessageAlert(user, time, text);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @PostMapping("/deletemessage") //TODO удалять у других через ws
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteGenMessage(@RequestParam String text, @SessionAttribute User user, @SessionAttribute int orgId){
        messagesDAO.deleteMessage(user.getId(), text, orgId, GenChatMessage.class);
    }

    @PostMapping("/savepost")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void savePost(@RequestParam String userId, @RequestParam String text,
                         @RequestParam String time, @SessionAttribute int orgId, @SessionAttribute User user){
        try {
            messagesDAO.saveMessage(user.getId(), text, DateFormatUtil.parseDate(time), orgId, Post.class);
            webSocketService.newPostAlert(user, time, text);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @PostMapping("/deletepost")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deletePost(@RequestParam String text, @SessionAttribute User user, @SessionAttribute int orgId){
        messagesDAO.deleteMessage(user.getId(), text, orgId, Post.class);
    }


    @PostMapping("/save_private_message/{companionId}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void savePrivateMessage(@RequestParam String text, @PathVariable String companionId,
                                   @SessionAttribute User user, @SessionAttribute int orgId){
        int cId = Integer.valueOf(companionId);
        User compan = userDAO.getUserById(cId);
        if (text.length() > 800) {
            String[] messages = text.split(".{800}"); //TODO test
            for (String m :
                    messages) {
                messagesDAO.persist(new PrivateChatMessage(m, user.getId(), compan.getId(), orgId));
            }
        }
        messagesDAO.persist(new PrivateChatMessage(text, user.getId(), compan.getId(), orgId));
        webSocketService.newPrivateMessageAlert(cId, user.getId(), text);
    }


    @PostMapping("/groupmessage")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void saveGroupMessage(@RequestParam String text, @RequestParam String groupName,
                                 @SessionAttribute User user, @SessionAttribute int orgId){
        ContactGroup group = user.getGroups().stream()
                        .filter(g -> g.getName().equals(groupName)).findAny().get();

        for (int id :
                group.getPersonIds()) {
            messagesDAO.persist(new PrivateChatMessage(text, user.getId(), id, orgId));
        }


    }



    @PostMapping("/savegroup")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void saveGroup(@RequestParam String groupName, @RequestParam String personIds,
                          @SessionAttribute User user, HttpSession session, Model model){
        try {
            String[] ids_s = personIds.split(",");
            int[] ids = Stream.of(ids_s).mapToInt(Integer::parseInt).toArray();
            user = userDAO.getUserById(user.getId());
            user.getGroups().add(new ContactGroup(groupName, user, ids, true));
           // userDAO.refresh(user);

            user = userDAO.updateUser(user);
            //session.setAttribute("user", user);
            model.addAttribute(user);

        }catch (Exception e){
            e.printStackTrace();

        }
    }

    @GetMapping("/deletegroup")
    @ResponseStatus(code = HttpStatus.OK)
    public void deleteGroup(@RequestParam String groupName, @SessionAttribute User user, HttpSession session){
        try {
            Iterator<ContactGroup> it = user.getGroups().iterator();
            ContactGroup group = null;
            while (it.hasNext()) {
                if ((group = it.next()).getName().equals(groupName)) break;
            }
            if (group != null) {
                user.getGroups().remove(group);
                session.setAttribute("user", userDAO.updateUser(user));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @GetMapping("/expand-props")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void saveExpandStatus(@RequestParam String groups, @SessionAttribute User user, HttpSession session){
        //[{"name" : "ddsf", "expand" : true},{}]
        ObjectMapper om = new ObjectMapper();
        List<PseudoContactGroup> grps = null;
        try {
            grps = om.readValue(groups, new TypeReference<List<PseudoContactGroup>>(){});
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (ContactGroup group: user.getGroups()) {
            for (int i = 0; i < grps.size(); i++) {
                if (group.getName().equals(grps.get(i).getName())) {
                    group.setExpandable(grps.get(i).getExpanded());
                    grps.remove(i);
                    break;
                }
            }

        }
        session.setAttribute("user", userDAO.updateUser(user));


    }



    @PostMapping("/note")
    @ResponseStatus(code = HttpStatus.OK)
    public boolean saveNote(@RequestParam String time, @RequestParam String text, @SessionAttribute User user, HttpSession session){
        try {
            System.out.println(" /note   before   " + user);
            //time = "15.03.2019, 00:00:00";
            Timestamp timestamp = DateFormatUtil.parseDate(time);
            /*Timestamp timestamp = Timestamp.valueOf(LocalDateTime.parse(time, DateTimeFormatter.ofPattern(TIME_PATTERN)));*/
            //Timestamp timestamp = Timestamp.valueOf(LocalDateTime.parse(time));
            user.getNotes().put(timestamp, text);
            userDAO.updateUser(user);
            user = userDAO.getUserById(user.getId());
            session.setAttribute("user", user);
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println(" /note   after  " + user);
        return true;
    }

    @GetMapping("/notes")
    public ResponseEntity<String> getNotes(@SessionAttribute User user){
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

    @PostMapping("/savefile")
    @ResponseStatus(code = HttpStatus.OK)
    public void saveFile(@RequestParam(name = "file")MultipartFile file, @RequestParam String shared,
                         @SessionAttribute User user, HttpSession session){
        String name = file.getOriginalFilename();
        System.out.println("FILE " + name);
        try {
            FileUtils.writeByteArrayToFile(new File(GeneralSettings.STORAGE_PATH + "/stored_files/" + user.getLogin() + "/" + name), file.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

        user.getStoredFiles().add(new StoredFile(name, LocalDateTime.now(), user, shared.equals("1")));
        user = userDAO.updateUser(user);
        session.setAttribute("user", user);
    }

    @GetMapping("/savefile")
    @ResponseStatus(code = HttpStatus.OK)
    public void updateFile(@RequestParam String fname, @RequestParam String update,
                           @RequestParam(required = false) String newName, @SessionAttribute User user, HttpSession session){
        Iterator<StoredFile> it = user.getStoredFiles().iterator();
        StoredFile storedFile = null;
        while (it.hasNext()){
            storedFile = it.next();
            if (storedFile.getName().equals(fname)) break;
        }
        try {
            switch (update) {
                case "share":
                    storedFile.setShared(true);
                    break;
                case "unshare":
                    storedFile.setShared(false);
                    break;
                case "delete":
                    String path = GeneralSettings.STORAGE_PATH + "/stored_files/" + user.getLogin() + "/";
                    user.getStoredFiles().remove(storedFile);
                    FileUtils.forceDelete(new File(path + storedFile.getName() + "." + storedFile.getExtension()));
                    break;
                case "rename":
                    String path2 = GeneralSettings.STORAGE_PATH + "/stored_files/" + user.getLogin() + "/";
                    File oldFile = new File(path2 + storedFile.getName() + "." + storedFile.getExtension());

                    FileUtils.copyFile(oldFile, new File(path2 + newName + "." + storedFile.getExtension()));
                    FileUtils.forceDelete(oldFile);
                    storedFile.setName(newName);
                    break;
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        //user = userDAO.getUserWithFiles(user.getId());
        user = userDAO.updateUser(user);
        session.setAttribute("user", user);
    }

    @GetMapping("/getstaff")
    @ResponseBody
    public ResponseEntity<String> getStaff(@SessionAttribute User user){

        String json = "";
        StringBuilder jsonS = null;
        try {
            Organization org = user.getOrganization();
            Set<Department> departments = org.getDepartments();

            if (departments.size() == 0) json = "{error:error}";

            List<Department> deps = departmentDAO.getHeadDepartments();
            deps.add(0, departmentDAO.getDefaultDepartment(org));

            ObjectMapper om = new ObjectMapper();
            json = om.writeValueAsString(deps); // TODO exception
            System.out.println(json);
            System.out.println();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Content-Type", "application/json; charset=UTF-8");
        return ResponseEntity.ok().headers(responseHeaders).body(json);
    }

    @PostMapping("/{org}/savestructure")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void saveStructure(@RequestBody String data, @PathVariable String org){
        ObjectMapper om = new ObjectMapper();
        try {
            Department[] deps = om.readValue(data, new TypeReference<Department[]>(){});
            Organization organization = orgDAO.getOrgByURL(org);
            for (Department d :
                    deps) {
                d.initDepartmentDaoTree(departmentDAO);
                if (d.getParentId() == 0) d.setParent(null);
                else d.initParentById();

                d.setOrganization(organization);
                d.initParentForTree();
                d.initOrgForChildren();
            }
            Set<Department> set = organization.getDepartments();

            set.addAll(Arrays.asList(deps));
            //organization.setDepartments(set);
            orgDAO.update(organization);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @PostMapping("/department")
    @ResponseStatus(code = HttpStatus.CREATED)
    public ResponseEntity<Long> saveDepartment(@SessionAttribute int orgId, @RequestParam String newname,
                              @RequestParam String oldname, @RequestParam String ids){
        Organization organization = orgDAO.getOrgById(orgId);
        ObjectMapper om = new ObjectMapper();
        Department department = null;
        try {
            int[] empls = om.readValue(ids, int[].class);
            department = departmentDAO.getDepartmentByName(oldname, organization);
            department.setName(newname);
            Set<User> employers = new HashSet<>();
            User user = null;
            for (Integer id :
                    empls) {
                user = userDAO.getUserById(id);
                user.setDepartment(department);
                employers.add(user);
            }
            department.setEmployers(employers);
            departmentDAO.merge(department);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok().body(department.getId());
    }

    @DeleteMapping("/departments")
    @ResponseBody
    @ResponseStatus(code = HttpStatus.GONE)
    public void clearDeps(@SessionAttribute int orgId){
        Organization organization = orgDAO.getOrgById(orgId);
        organization.getDepartments().clear();
        orgDAO.update(organization);
        //TODO протестировать
    }

    @GetMapping("/chatpiece")
    @ResponseBody
    public ResponseEntity<String> getChatPiece(HttpSession session, @SessionAttribute int orgId){
        int oldIndex = (int) session.getAttribute("lastIdx_genchat");
        if (oldIndex == -1) return null;
        List<AbstractMessage> messages = messagesDAO.getMessages(orgId, oldIndex, GenChatMessage.class);
        int newIdx = messages.size() < GeneralSettings.AMOUNT_GENCHAT_MESSAGES ? -1 : messages.get(messages.size() - 1).getId();
        session.setAttribute("lastIdx_genchat", newIdx);
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

    @GetMapping("/wallpiece")
    @ResponseBody
    public ResponseEntity<String> getWallPiece(HttpSession session, @SessionAttribute int orgId){
        int oldIndex = (int) session.getAttribute("lastIdx_wall");
        if (oldIndex == -1) return null;
        List<AbstractMessage> messages = messagesDAO.getMessages(orgId, oldIndex, Post.class);
        int newIdx = messages.size() < GeneralSettings.AMOUNT_WALL_MESSAGES ? -1 : messages.get(messages.size() - 1).getId();
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

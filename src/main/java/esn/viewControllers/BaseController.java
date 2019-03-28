package esn.viewControllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import esn.configs.GeneralSettings;
import esn.db.*;
import esn.entities.Department;
import esn.entities.Organization;
import esn.entities.User;
import esn.entities.secondary.*;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static esn.configs.GeneralSettings.TIME_PATTERN;

@Controller
@SessionAttributes({"user", "orgId"})
public class BaseController {

    private GlobalDAO globalDAO;
    private OrganizationDAO orgDAO;
    private UserDAO userDAO;
    private DepartmentDAO departmentDAO;
    private PrivateChatMessageDAO privateChatMessageDAO;

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
    public void setPrivateChatMessageDAO(PrivateChatMessageDAO privateChatMessageDAO) {
        this.privateChatMessageDAO = privateChatMessageDAO;
    }

    @GetMapping(value = "/{organization}")
    public String start(@PathVariable String organization){
        //TODO проверка на сущестование орг юрл

        //TODO get cookies
       return "redirect:/" + organization + "/auth1";
    }





    @PostMapping("/savemessage")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void saveMessage(@RequestParam String userId, @RequestParam String text,
                            @RequestParam String time, @SessionAttribute int orgId){
        Timestamp timestamp = Timestamp.valueOf(LocalDateTime.parse(time, DateTimeFormatter.ofPattern(TIME_PATTERN)));
        globalDAO.saveMessage(Integer.valueOf(userId), text, timestamp, orgId, GenChatMessage.class);
    }

    @PostMapping("/save_private_message/{companionId}") //TODO org mapping in  url
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void savePrivateMessage(@RequestParam String text, @PathVariable String companionId,
                                   @SessionAttribute User user, @SessionAttribute int orgId){
        User compan = userDAO.getUserById(Integer.valueOf(companionId));
        privateChatMessageDAO.persist(new PrivateChatMessage(text, user.getId(), compan.getId(), orgId));
    }

    @PostMapping("/groupmessage")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void saveGroupMessage(@RequestParam String text){

    }

    @PostMapping("/savepost")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void savePost(@RequestParam String userId, @RequestParam String text,
                            @RequestParam String time, @SessionAttribute int orgId){

        Timestamp timestamp = Timestamp.valueOf(LocalDateTime.parse(time, DateTimeFormatter.ofPattern(TIME_PATTERN)));
        globalDAO.saveMessage(Integer.valueOf(userId), text, timestamp, orgId, Post.class);
    }

    @PostMapping("/savegroup")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void saveGroup(@RequestParam String groupName, @RequestParam String personIds,
                          @SessionAttribute User user, HttpSession session){
        try {
            String[] ids_s = personIds.split(",");
            int[] ids = Stream.of(ids_s).mapToInt(Integer::parseInt).toArray();
            user = userDAO.getUserById(user.getId());
            user.getGroups().add(new ContactGroup(groupName, user, ids, true)); //TODO expandable
           // userDAO.refresh(user); //TODO id обнуляется
            user = userDAO.updateUser(user);
            session.setAttribute("user", user);

        }catch (Exception e){
            e.printStackTrace();

        }

    }

    @PostMapping("/note")
    @ResponseStatus(code = HttpStatus.OK)
    public boolean saveNote(@RequestParam String time, @RequestParam String text, @SessionAttribute User user, HttpSession session){
        try {
            System.out.println(" /note   before   " + user);
            //time = "15.03.2019, 00:00:00";
            Timestamp timestamp = Timestamp.valueOf(LocalDateTime.parse(time, DateTimeFormatter.ofPattern(TIME_PATTERN)));
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








            /*Map<Integer, String> datesArray = sortedNotes.entrySet().stream().map(Map.Entry::getKey).collect(Collectors.toMap(
                    Date::getMonth, d -> String.valueOf(d.getMonth()), (a, b) -> String.join(",", a, b)));

            List<Integer[]> datesArrayRes = Stream.generate(() -> new Integer[]{}).limit(12).collect(Collectors.toList());

            for (Map.Entry<Integer, String> entry : datesArray.entrySet()) {
                datesArrayRes.set(entry.getKey(), Stream.of(entry.getValue().split(",")).peek(System.out::println).map(Integer::parseInt)
                        .toArray(Integer[]::new)); //Преобразую строку в массив integer
            }




            Map<Integer, String> dates = sortedNotes.entrySet().stream().collect(Collectors.toMap(
                    entry -> entry.getKey().getMonth(), entry -> entry.getValue(), (a, b) -> String.join("$$$", a, b)));


            List<String[]> datesRes = Stream.generate(() -> new String[]{}).limit(12).collect(Collectors.toList());

            for (Map.Entry<Integer, String> entry : dates.entrySet()) {
                datesRes.set(entry.getKey(), entry.getValue().split("\\$\\$\\$"));
            }*/


            //return ResponseEntity.ok().body(new Object[]{datesArrayRes, datesRes});
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;


    }

    @PostMapping("/savefile")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void saveFile(@RequestParam(name = "file")MultipartFile file, @RequestParam String shared,
                         @SessionAttribute User user, HttpSession session){
        String name = file.getOriginalFilename();
        System.out.println("FILE " + name);
        try {
            FileUtils.writeByteArrayToFile(new File(GeneralSettings.STORED_FILES_PATH + user.getLogin() + "/" + name), file.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

        user.getStoredFiles().add(new StoredFile(name, LocalDateTime.now(), user, shared.equals("1")));
        user = userDAO.updateUser(user);
        session.setAttribute("user", user);
    }

    @GetMapping("/savefile")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
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
                    String path = GeneralSettings.STORED_FILES_PATH + user.getLogin() + "/";
                    user.getStoredFiles().remove(storedFile);
                    FileUtils.forceDelete(new File(path + storedFile.getName() + "." + storedFile.getExtension()));
                    break;
                case "rename":
                    String path2 = GeneralSettings.STORED_FILES_PATH + user.getLogin() + "/";
                    File oldFile = new File(path2 + storedFile.getName() + "." + storedFile.getExtension());

                    FileUtils.copyFile(oldFile, new File(path2 + newName + "." + storedFile.getExtension()));
                    FileUtils.forceDelete(oldFile);
                    storedFile.setName(newName);
                    break;
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        user = userDAO.getUserWithFiles(user.getId());
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
            json = om.writeValueAsString(deps);
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

    @PostMapping("/{org}/savedep") //TODO url ref
    @ResponseBody
    @ResponseStatus(code = HttpStatus.CREATED)
    public ResponseEntity<Long> saveDepartment(@PathVariable String org, @RequestParam String newname,
                              @RequestParam String oldname, @RequestParam String ids){
        Organization organization = orgDAO.getOrgByURL(org);
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

    @GetMapping("/{org}/cleardeps") //TODO url ref
    @ResponseBody
    @ResponseStatus(code = HttpStatus.GONE)
    public void clearDeps(@PathVariable String org){
        Organization organization = orgDAO.getOrgByURL(org);
        organization.getDepartments().clear();
        orgDAO.update(organization);
        //TODO протестировать
    }




    @GetMapping("/favicon")
    @ResponseBody
    public void fav(){}



}

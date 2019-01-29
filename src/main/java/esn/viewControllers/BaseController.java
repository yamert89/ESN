package esn.viewControllers;

import esn.db.DepartmentDAO;
import esn.db.GlobalDAO;
import esn.db.OrganizationDAO;
import esn.db.UserDAO;
import esn.entities.Department;
import esn.entities.Organization;
import esn.entities.User;
import esn.entities.secondary.GenChatMessage;
import esn.entities.secondary.Post;
import esn.entities.secondary.StoredFile;
import esn.utils.GeneralSettings;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
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

import static esn.utils.GeneralSettings.TIME_PATTERN;

@Controller

@SessionAttributes
public class BaseController {

    private GlobalDAO globalDAO;
    private OrganizationDAO orgDAO;
    private UserDAO userDAO;
    private DepartmentDAO departmentDAO;

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

    @GetMapping(value = "/{organization}")
    public String start(@PathVariable String organization){
        //TODO проверка на сущестование орг юрл

        //TODO get cookies
       return "redirect:/" + organization + "/auth1";
    }





    @PostMapping("/savemessage")
    @ResponseBody
    public void saveMessage(@RequestParam String userId, @RequestParam String text,
                            @RequestParam String time, @RequestParam String orgUrl){
        Timestamp timestamp = Timestamp.valueOf(LocalDateTime.parse(time, DateTimeFormatter.ofPattern(TIME_PATTERN)));
        globalDAO.saveMessage(Integer.valueOf(userId), text, timestamp, orgUrl, GenChatMessage.class);
    }

    @PostMapping("/savepost")
    @ResponseBody
    public void savePost(@RequestParam String userId, @RequestParam String text,
                            @RequestParam String time, @RequestParam String orgUrl){

        Timestamp timestamp = Timestamp.valueOf(LocalDateTime.parse(time, DateTimeFormatter.ofPattern(TIME_PATTERN)));
        globalDAO.saveMessage(Integer.valueOf(userId), text, timestamp, orgUrl, Post.class);
    }

    @PostMapping("/savegroup")
    @ResponseBody
    public void saveGroup(@RequestParam String groupName, @RequestParam String personIds,
                           HttpSession session){
        try {
            User user = (User) session.getAttribute("user");

            user.getGroups().put(groupName, personIds.split(","));

            for (Map.Entry<String, String[]> entry: user.getGroups().entrySet()) {
                System.out.println("группа : " + entry.getKey());
                for (String s :
                        entry.getValue()) {
                    System.out.print("значения : " + s + " ");
                }
                System.out.println();
            }
            userDAO.updateUser(user);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @PostMapping("/savenote")
    @ResponseBody
    public void saveNote(@RequestParam String time, @RequestParam String text, HttpSession session){
        User user = (User) session.getAttribute("user");
        Timestamp timestamp = Timestamp.valueOf(LocalDateTime.parse(time, DateTimeFormatter.ofPattern(TIME_PATTERN)));
        user.getNotes().put(timestamp, text);
        userDAO.updateUser(user);
    }

    @PostMapping("/savefile")
    @ResponseBody
    public void saveFile(@RequestParam(name = "file")MultipartFile file, @RequestParam String shared, HttpSession session){
        User user = (User) session.getAttribute("user");
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
    @ResponseBody
    public void updateFile(@RequestParam String fname, @RequestParam String update,
                           @RequestParam(required = false) String newName, HttpSession session){
        User user = (User) session.getAttribute("user");
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
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        user = userDAO.updateUser(user);
        session.setAttribute("user", user);
    }

    @GetMapping("/getstaff")
    @ResponseBody
    public ResponseEntity<String> getStaff(HttpSession session){
        try {
            User user = (User) session.getAttribute("user");
            Organization org = user.getOrganization();
            Set<Department> departments = org.getDepartments();
            Iterator iterator = departments.iterator();
            while (iterator.hasNext()){
                //Department department = departmentDAO.getDepartmentWithUsers((Department) iterator.next());
                Department department = departmentDAO.getHeadDepartment();
                if (department.equals((Department) iterator.next())) System.out.println("YES!!!");
                //TODO реализовать json

            }



        }catch (Exception e){
            e.printStackTrace();
        }

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Content-Type", "application/json; charset=UTF-8");
        return ResponseEntity.ok().headers(responseHeaders).body("");
    }

    @GetMapping("/favicon")
    @ResponseBody
    public void fav(){}


}

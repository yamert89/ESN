package esn.viewControllers.main;

import esn.configs.GeneralSettings;
import esn.db.GlobalDAO;
import esn.db.UserDAO;
import esn.entities.Organization;
import esn.entities.User;
import esn.entities.secondary.StoredFile;
import esn.utils.SimpleUtils;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.LazyInitializationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Iterator;

@Controller
public class StorageController {
    private final static Logger logger = LogManager.getLogger(StorageController.class);

    private GlobalDAO globalDAO;
    private UserDAO userDAO;
    private HttpHeaders headers;

    @Autowired
    public void setHeaders(HttpHeaders headers) {
        this.headers = headers;
    }

    @Autowired
    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Autowired
    public void setGlobalDAO(GlobalDAO globalDAO) {
        this.globalDAO = globalDAO;
    }

    @GetMapping(value = "/{organization}/storage")
    public String storage(/*@SessionAttribute User user*/ Model model, SessionStatus status, HttpSession session, @PathVariable String organization){
        User user = (User) session.getAttribute("user");
        status.setComplete();
        try{
            user.getStoredFiles().size();
        }catch (LazyInitializationException e){
            user = userDAO.getUserWithFiles(user.getId());
        }
        model.addAttribute("sharedFiles", globalDAO.getSharedFiles(user.getOrganization()));
        model.addAttribute("filesPath", "/resources/data/" + user.getOrganization().getUrlName() + "/stored_files/" + user.getLogin() + "/");

        model.addAttribute("user", user);
        session.setAttribute("user", user);

        return "storage";
    }

    @PostMapping("/savefile")
    @ResponseBody
    public ResponseEntity<String> saveFile(@RequestParam(name = "file") MultipartFile file, @RequestParam String shared,
            /* */HttpSession session){
        User user = (User) session.getAttribute("user");
        String orgUrl = ((Organization) session.getAttribute("org")).getUrlName();
        if (SimpleUtils.getPrivateStoragePercentageSize(orgUrl, user.getLogin()) + file.getSize()/1024d/1024d / GeneralSettings.PRIVATE_STORAGE_MAX_SIZE * 100 > 100 ||
                SimpleUtils.getPublicStoragePercentageSize(orgUrl) + file.getSize()/1024d/1024d / GeneralSettings.PRIVATE_STORAGE_MAX_SIZE * 100 > 100)
        {
            return ResponseEntity.ok().headers(headers).body("{\"success\" : false, \"overflow\" : true}");
        }

        String name = file.getOriginalFilename();
        logger.debug("FILE " + name);
        String path = GeneralSettings.STORAGE_PATH + "\\" + user.getOrganization().getUrlName() + "\\stored_files\\" +  user.getLogin() + "\\" + name;
        logger.debug("PATH " + path);
        try {
            FileUtils.writeByteArrayToFile(new File(path), file.getBytes());
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }

        user.getStoredFiles().add(new StoredFile(name, Timestamp.from(Instant.now()), user, shared.equals("1")));
        user = userDAO.updateUser(user);
        session.setAttribute("user", user);
        return ResponseEntity.ok().headers(headers).body("{\"success\" : true}");
    }

    @GetMapping("/savefile")
    @ResponseStatus(code = HttpStatus.OK)
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
                    String path = GeneralSettings.STORAGE_PATH + "/" + user.getOrganization().getUrlName() + "/stored_files/" + user.getLogin() + "/";
                    user.getStoredFiles().remove(storedFile);
                    FileUtils.forceDelete(new File(path + storedFile.getName() + "." + storedFile.getExtension()));
                    break;
                case "rename":
                    String path2 = GeneralSettings.STORAGE_PATH + "/" + user.getOrganization().getUrlName() + "/stored_files/" + user.getLogin() + "/";
                    File oldFile = new File(path2 + storedFile.getName() + "." + storedFile.getExtension());

                    FileUtils.copyFile(oldFile, new File(path2 + newName + "." + storedFile.getExtension()));
                    FileUtils.forceDelete(oldFile);
                    storedFile.setName(newName);
                    break;
            }
        }catch (IOException e){
            logger.error(e.getMessage(), e);
        }
        //user = userDAO.getUserWithFiles(user.getId());
        user = userDAO.updateUser(user);
        session.setAttribute("user", user);
    }

    @GetMapping("/storage_size")
    @ResponseBody
    public ResponseEntity<String> getStorageSizes(HttpSession session){
        User user = (User) session.getAttribute("user");
        int publicS = SimpleUtils.getPublicStoragePercentageSize(user.getOrganization().getUrlName());
        int privateS = SimpleUtils.getPrivateStoragePercentageSize(user.getOrganization().getUrlName(), user.getLogin());
        return ResponseEntity.ok().headers(headers).body("{\"public\":" + publicS + ", \"private\":" + privateS + "}");

    }
}

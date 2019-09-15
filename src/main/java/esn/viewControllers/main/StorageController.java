package esn.viewControllers.main;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import esn.configs.GeneralSettings;
import esn.db.GlobalDAO;
import esn.db.UserDAO;
import esn.entities.Organization;
import esn.entities.User;
import esn.entities.secondary.StoredFile;
import esn.utils.SimpleUtils;
import esn.viewControllers.accessoryFunctions.SessionUtil;
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
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Iterator;
import java.util.List;

@Controller
public class StorageController {
    private final static Logger logger = LogManager.getLogger(StorageController.class);

    private GlobalDAO globalDAO;
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

    @Autowired
    public void setGlobalDAO(GlobalDAO globalDAO) {
        this.globalDAO = globalDAO;
    }

    @GetMapping(value = "/{organization}/storage")
    public ModelAndView storage(RedirectAttributes redirectAttributes, Model model, SessionStatus status, HttpSession session, HttpServletRequest request, Principal principal){
        ModelAndView modelAndView = new ModelAndView("storage");
        try {
            User user = sessionUtil.getUser(request, principal);
            status.setComplete();
            try {
                user.getStoredFiles().size();
            } catch (LazyInitializationException e) {
                user = userDAO.getUserWithFiles(user.getId());
            }
            modelAndView.addObject("sharedFiles", globalDAO.getSharedFiles(user.getOrganization()));
            modelAndView.addObject("filesPath", "/resources/data/" + user.getOrganization().getUrlName() + "/stored_files/");

            modelAndView.addObject("user", user);
            session.setAttribute("user", user);
        }catch (Exception e){
            logger.error("storage", e);
            redirectAttributes.addFlashAttribute("flash", "Произошла ошибка при загрузке страницы хранилища. Сообщите разработчику");
            redirectAttributes.addAttribute("status", 777);
            modelAndView.setViewName("redirect:/error");
        }

        return modelAndView;
    }

    @GetMapping("/storage")
    @ResponseBody
    public ResponseEntity<String> getFiles(HttpServletRequest request, Principal principal){
        User user = sessionUtil.getUser(request, principal);
        List<StoredFile> files = globalDAO.getSharedAndMyFiles(user);
        ObjectMapper om = new ObjectMapper();
        String js = "";
        try {
            js = om.writeValueAsString(files);
        } catch (JsonProcessingException e) {
            logger.error("getFiles", e);
        }
        return ResponseEntity.ok().headers(headers).body(js);
    }

    @PostMapping("/savefile")
    @ResponseBody
    public ResponseEntity<String> saveFile(@RequestParam(name = "file") MultipartFile file, @RequestParam String shared,
            /* */HttpSession session, HttpServletRequest request, Principal principal){
        try {
            User user = userDAO.getUserWithFiles(sessionUtil.getUser(request, principal).getId());
            String orgUrl = ((Organization) session.getAttribute("org")).getUrlName();
            double privateSize = SimpleUtils.getPrivateStoragePercentageSize(orgUrl, user.getLogin()) + file.getSize() / 1024d / 1024d;
            double publicSize = SimpleUtils.getPublicStoragePercentageSize(orgUrl) + file.getSize() / 1024d / 1024d;
            double percentagePrivate = privateSize / GeneralSettings.PRIVATE_STORAGE_MAX_SIZE * 100;
            double percentagePublic = publicSize / GeneralSettings.PUBLIC_STORAGE_MAX_SIZE * 100;
            logger.debug("Storage Size :  - private :" + privateSize + " - " + percentagePrivate + "%   - public : " + publicSize + " - " + percentagePublic + "%");

            if (percentagePublic > 100 || percentagePrivate > 100) return ResponseEntity.ok().headers(headers).body("{\"success\" : false, \"overflow\" : true}");

            String name = file.getOriginalFilename();
            logger.debug("FILE " + name);
            String path = GeneralSettings.STORAGE_PATH + "\\" + user.getOrganization().getUrlName() + "\\stored_files\\" + user.getLogin() + "\\" + name;
            logger.debug("PATH " + path);
            try {
                FileUtils.writeByteArrayToFile(new File(path), file.getBytes());
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }

            user.getStoredFiles().add(new StoredFile(name, Timestamp.from(Instant.now()), user, shared.equals("1")));
            user = userDAO.updateUser(user);
            session.setAttribute("user", user);
        }catch (Exception e){
            logger.error("saveFile", e);
        }
        return ResponseEntity.ok().headers(headers).body("{\"success\" : true}");
    }

    @GetMapping("/savefile")
    @ResponseStatus(code = HttpStatus.OK)
    public void updateFile(@RequestParam String fname, @RequestParam String update, HttpServletRequest request, Principal principal,
                           @RequestParam(required = false) String newName, HttpSession session){
        User user = userDAO.getUserWithFiles(sessionUtil.getUser(request, principal).getId());
        try{
            Iterator<StoredFile> it = user.getStoredFiles().iterator();
            StoredFile storedFile = null;
            while (it.hasNext()){
                storedFile = it.next();
                if (storedFile.getName().equals(fname)) break;
            }

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
    public ResponseEntity<String> getStorageSizes(HttpServletRequest request, Principal principal){
        int publicS = 0;
        int privateS = 0;
        try {
            User user = sessionUtil.getUser(request, principal);
            publicS = SimpleUtils.getPublicStoragePercentageSize(user.getOrganization().getUrlName());
            privateS = SimpleUtils.getPrivateStoragePercentageSize(user.getOrganization().getUrlName(), user.getLogin());
        }catch (Exception e){logger.error("getStorageSIze",e);}
        return ResponseEntity.ok().headers(headers).body("{\"public\":" + publicS + ", \"private\":" + privateS + "}");

    }
}

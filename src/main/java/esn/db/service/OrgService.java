package esn.db.service;

import esn.db.OrganizationDAO;
import esn.db.UserDAO;
import esn.entities.Organization;
import esn.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class OrgService {
    private OrganizationDAO orgDao;
    private UserDAO userDAO;

    @Autowired
    public void setOrgDao(OrganizationDAO orgDao) {
        this.orgDao = orgDao;
    }

    @Autowired
    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Transactional
    public Organization merge(Organization org){
        org = orgDao.update(org);
        if (userDAO.getUserByLogin("deleted") != null) return org;
        userDAO.persistUser(new User("Профиль удалён", "Профиль удалён","deleted",  "pppppp", true, "ROLE_USER", org,
                "/app/deleted.jpg", "/app/deleted_small.jpg"));
        return org;
    }

    @Transactional
    public Organization findByUrl(String url, boolean needDepartments){
        return needDepartments ? orgDao.getOrgByURLWithDepartments(url) : orgDao.getOrgByURL(url);
    }

    @Transactional
    public Organization findByKey(String key){
        return orgDao.getOrgByKey(key);
    }

    @Transactional
    public List<String> getLogins(){return  orgDao.getLogins();}

    @Transactional
    public  List<Organization> getAllOrgs(){return orgDao.getAllOrgs();}

    @Transactional
    public boolean hasAdmin(String orgUrl){return orgDao.hasAdmin(orgUrl);}

    @Transactional
    public void clearStructure(Organization org){
        orgDao.deleteAllDepartmentsInUsers(org);
    }

    @Transactional
    public boolean isAdminKey(String key, int orgId){return orgDao.isAdminKey(key, orgId);}

    @Transactional
    public boolean consistUrl(String url){
        return orgDao.getOrgByURL(url) != null;
    }












}

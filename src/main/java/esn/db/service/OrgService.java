package esn.db.service;

import esn.db.OrganizationDAO;
import esn.db.UserDAO;
import esn.entities.Organization;
import esn.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        userDAO.persistUser(new User("Пользователь удалён", "","deleted",  "pppppp", true, "ROLE_USER", org,
                "/app/deleted.jpg", "/app/deleted_small.jpg"));
        return org;
    }







}

import esn.db.OrganizationDAO;
import esn.db.UserDAO;
import esn.db.message.WallDAO;
import esn.entities.Organization;
import esn.entities.User;
import esn.entities.secondary.Post;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.HashSet;
import java.util.Set;

@ContextConfiguration({"file:src/main/webapp/WEB-INF/app_context.xml",
        "file:src/main/webapp/WEB-INF/dao.xml",})
public class DaoIntegrated extends org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests{

    @Autowired
    OrganizationDAO organizationDAO;

    @Autowired
    UserDAO userDAO;

    @Autowired
    WallDAO wallDAO;

    @PersistenceContext
    EntityManager em;

    @Test
    public void getOrgByKey(){
        try {
            String key = "1";
            Organization org = (Organization) em.createQuery("select org from Organization org where org.corpKey = :k or org.adminKey = :k")
                    .setParameter("k", key).getSingleResult();
            Assert.assertEquals("rosles", org.getUrlName());
        }catch (NoResultException e){
            System.out.println("NO RESULT");
        }
    }

    @Test
    public void wallBag(){

    }
    
    @Test
    public void equals(){
        User user = userDAO.getUserByLogin("yamert");
        Set<User> set = new HashSet<>();
        set.add(user);
        User us2 = new User();
        us2.setLogin("yamert");
        set.remove(us2);
        Assert.assertEquals(0, set.size());
    }

}

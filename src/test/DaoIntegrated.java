import esn.db.OrganizationDAO;
import esn.entities.Organization;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@ContextConfiguration({"file:src/main/webapp/WEB-INF/app_context.xml",
        "file:src/main/webapp/WEB-INF/dao.xml",})

public class DaoIntegrated extends org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests{

    @Autowired
    OrganizationDAO organizationDAO;

    @PersistenceContext
    EntityManager em;

    @Test
    public void getOrgByKey(){
        String key = "";
        Organization org =  (Organization) em.createQuery("select org from Organization org where org.corpKey = :k or org.adminKey = :k")
                .setParameter("k", key).getSingleResult();
        Assert.assertEquals("rosles", org.getUrlName());
    }

}

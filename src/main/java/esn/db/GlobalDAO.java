package esn.db;

import esn.db.syntax.PostgresSyntax;
import esn.db.syntax.Syntax;
import esn.entities.Organization;
import esn.entities.secondary.ContactGroup;
import esn.entities.secondary.StoredFile;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@Transactional
public class GlobalDAO implements InitializingBean {



    @PersistenceContext
    private EntityManager em;

    private Syntax syntax = new PostgresSyntax();



    @Transactional
    public List<StoredFile> getSharedFiles(Organization org){
        return em.createQuery("select f from StoredFile f where f.org = :org and shared = true")
                .setParameter("org",org).getResultList();
    }

    @Transactional
    public void persistGroup(ContactGroup group){
        em.persist(group);
    }

    @Transactional
    public void initDB(){
        em.createNativeQuery("create table wall " + syntax.createTableConstraints()).executeUpdate(); //TODO Учесть ограничения базы (везде) !!!
        em.createNativeQuery("create table generalchat " + syntax.createTableConstraints()).executeUpdate();
    }


    @Override
    public void afterPropertiesSet() throws Exception {
       // initDB(); //TODO
    }

}

package esn.db;

import esn.db.syntax.PostgresSyntax;
import esn.db.syntax.Syntax;
import esn.entities.Organization;
import esn.entities.secondary.ContactGroup;
import esn.entities.secondary.StoredFile;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@Transactional
public class GlobalDAO implements InitializingBean {

    @Autowired
    @Qualifier("transactionManager")
    PlatformTransactionManager txManager;



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


    public void initDB(){

        TransactionStatus ts = txManager.getTransaction(new DefaultTransactionDefinition());
        try {
            em.createNativeQuery("insert into organizations(id, adminkey, corpkey, description, disabled, hasadmin, headerpath, name, urlname) values " +
                    "(0, '777', '777', '', false, true, '', 'root', '')").executeUpdate();
            em.createNativeQuery("insert into users(id, authority, login, male, name, password, photo, photo_small, org_id, boss_id) " +
                    "values(0, 'ROLE_ADMIN', 'admin', true, 'Разработчик', '$2a$10$YiGmQU.QDMGYvu0xK7HbueLGQ/rFDNfntMKUT6RKEw8391Hgt.wWS', '/man.jpg', '/man_small.jpg', 0, null);").executeUpdate();
            em.createNativeQuery("create table wall " + syntax.createTableConstraints()).executeUpdate(); //TODO Учесть ограничения базы (везде) !!!
            em.createNativeQuery("insert into wall (id, message, userid, time, orgid) values (0, '<p>Приветствуем Вас в нашем чате!</p>', 0, clock_timestamp(), 0)").executeUpdate();
            txManager.commit(ts);
        }catch (Exception e){
            txManager.rollback(ts);
        }

        ts = txManager.getTransaction(new DefaultTransactionDefinition());

        try{
            em.createNativeQuery("create table generalchat " + syntax.createTableConstraints()).executeUpdate();
            txManager.commit(ts);
        }catch (Exception e){
            txManager.rollback(ts);
        }

    }


    @Override
    public void afterPropertiesSet() throws Exception {
       initDB();
    }

}

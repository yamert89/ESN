package esn.db;

import esn.configs.GeneralSettings;
import esn.db.syntax.Syntax;
import esn.entities.Organization;
import esn.entities.User;
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

    private Syntax syntax = GeneralSettings.DB_SYNTAX;



    @Transactional
    public List<StoredFile> getSharedFiles(Organization org){
        return em.createQuery("select f from StoredFile f where f.org = :org and shared = true")
                .setParameter("org",org).getResultList();
    }

    @Transactional
    public List<StoredFile> getSharedAndMyFiles(User user){
        return em.createQuery("select f from StoredFile f where f.org = :org and (f.shared = true or f.owner = :owner)")
                .setParameter("org", user.getOrganization()).setParameter("owner",user).getResultList();
    }

    @Transactional
    public void persistGroup(ContactGroup group){
        em.persist(group);
    }


    public void initDB(){

        TransactionStatus ts = txManager.getTransaction(new DefaultTransactionDefinition());
        System.out.println("INIT DB");

        try {
            em.createNativeQuery("create table stat (path varchar(100), host varchar(50), count bigint)").executeUpdate();
            em.createNativeQuery("create table wall " + syntax.createTableConstraints()).executeUpdate();
            em.createNativeQuery("insert into wall (id, message, userid, time, orgid) values (0, '<p>Приветствуем Вас в нашем чате!</p>', 0, " + syntax.currentDate() + ", 0)").executeUpdate();
            em.createNativeQuery("insert into organizations(id, adminkey, corpkey, description, disabled, hasadmin, headerpath, name, urlname) values " +
                    "(0, '777', '777', '', false, true, '', 'root', '')").executeUpdate();
            em.createNativeQuery("insert into users(id, authority, login, male, name, shortname, password, photo, photo_small, org_id, boss_id) " +
                    "values(0, 'ROLE_ADMIN', 'admin', true, 'Администратор EnChat', 'Администратор EnChat', '$2a$10$YiGmQU.QDMGYvu0xK7HbueLGQ/rFDNfntMKUT6RKEw8391Hgt.wWS', '/app/man.jpg', '/app/man_small.jpg', 0, null);").executeUpdate();
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

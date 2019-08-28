package esn.db;

import esn.entities.Session;
import esn.entities.User;
import esn.viewControllers.main.GroupsController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Calendar;
import java.util.Optional;

@Repository("user_dao")
@Transactional
public class UserDAO {

    private final static Logger logger = LogManager.getLogger(GroupsController.class);

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void persistUser(User user) {
        em.persist(user);
    }

    @Transactional
    public User updateUser(User user) {
        try {
            return em.merge(user);
        }catch (Exception e){
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    @Transactional
    public void refresh(User user){
         em.refresh(user);
    }

    @Transactional
    public void detach(User user) {
        em.detach(user);
    }

    @Transactional
    public void deleteUser(User user)throws Exception {
        User deleted = (User) em.createQuery("select u from User u where login = 'deleted'").getSingleResult();
        user = contains(user) ? user : getUserById(user.getId());
        em.createQuery("delete from StoredFile f where f.owner = :u and f.shared = false").setParameter("u", user).executeUpdate();
        em.createQuery("update StoredFile s set owner = :deleted").setParameter("deleted", deleted).executeUpdate();
        em.createNativeQuery("update wall set userid = ? where userid = ?")
                .setParameter(1, user.getId())
                .setParameter(2, deleted.getId())
                .executeUpdate();
        em.remove(user);
    }

    @Transactional
    public User getUserById(Integer id) {
        return em.find(User.class, id);
    }

    @Transactional
    public User getReference(Integer id){
        User user = em.getReference(User.class,id);
        return user;
    }

    @Transactional
    public User getUserByLogin(String login) {
        try {
            return (User) em.createQuery("select u from User u where u.login = :login").setParameter("login", login).getSingleResult();
        }catch (Exception e){
            return null;
        }
    }


    @Transactional
    public String getPassword(String login) throws Exception{
        return (String) em.createQuery("select u.password from User u where u.login = :login").setParameter("login", login).getSingleResult();
    }

    @Transactional
    public User getUserWithNotes(Integer id){//TODO delete
        User user = em.find(User.class, id);
        Hibernate.initialize(user.getNotes());
        return user;
    }

    @Transactional
    public User getUserWithFiles(Integer id){
        User user = em.find(User.class, id);
        Hibernate.initialize(user.getStoredFiles());
        return user;
    }

    @Transactional
    public User getUserWithInfo(String login){
        User user = (User) em.createQuery("select u from User u where u.login = :l").setParameter("l", login).getSingleResult();
        Hibernate.initialize(user.getUserInformation());
        return user;
    }

    @Transactional
    public User getUserByNameAndPosition(String name, String position){
        return (User) em.createQuery("select u from User u where u.name = :name and u.position = :position")
                .setParameter("name", name).setParameter("position", position).getSingleResult();
    }

    @Transactional
    public Calendar getBirthDate(int id){
        return (Calendar) em.createQuery("select u.userInformation.birthDate from User u where u.id = :id")
                .setParameter("id", id).getSingleResult();
    }

    @Transactional
    public void saveSession(Session session){ //TODO replace in user
        em.persist(session);
    }

    @Transactional
    public Optional<Session> getSession(String id){
        return Optional.ofNullable(em.find(Session.class, id));
    }

    @Transactional
    public void updateSession(Session session){
        em.merge(session);
    }

    @Transactional
    public Calendar getLastSession(User user)throws NoResultException {
        Query query = em.createQuery("select s.endTime from Session s where s.user = :user order by s.endTime DESC")
                .setParameter("user", user).setMaxResults(1);
        Calendar calendar = (Calendar) query.getSingleResult();
        return calendar;
    }

    @Transactional
    public boolean contains(User user){
        return em.contains(user);
    }









}

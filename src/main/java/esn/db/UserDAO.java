package esn.db;

import esn.entities.User;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository("user_dao")
@Transactional
public class UserDAO {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void persistUser(User user) {
        em.persist(user);
    }

    @Transactional
    public User updateUser(User user) {
        return em.merge(user);
    }

    @Transactional
    public void deleteUser(User user) {
        em.remove(user);
    }

    @Transactional
    public User getUserById(Integer id) {
        return em.find(User.class, id);
    }

    @Transactional
    public User getUserByLogin(String login) throws Exception {
        return (User) em.createQuery("select u from User u where u.login = :login").setParameter("login", login).getSingleResult();

    }


    @Transactional
    public String getPassword(String login) throws Exception{
        return (String) em.createQuery("select u.password from User u where u.login = :login").setParameter("login", login).getSingleResult();
    }

    @Transactional
    public User getUserWithNotes(Integer id){
        User user = em.find(User.class, id);
        Hibernate.initialize(user.getNotes());
        return user; //TODO оптимизировать все коллекции
    }

    @Transactional
    public User getUserWithFiles(Integer id){
        User user = em.find(User.class, id);
        Hibernate.initialize(user.getStoredFiles());
        return user;
    }

    @Transactional
    public User getUserWithInfo(Integer id){
        User user = em.find(User.class, id);
        Hibernate.initialize(user.getUserInformation());
        return user;
    }





}

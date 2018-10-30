package db;

import entities.User;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
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
    public void updateUser(User user) {
        em.merge(user);
    }

    @Transactional
    public User getUserById(Integer id) {
        return em.find(User.class, id);
    }

}

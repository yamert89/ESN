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
    public void addUser(User user){
        em.persist(user);
    }
}

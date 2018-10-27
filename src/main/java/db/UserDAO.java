package db;

import entities.Department;
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

    @Transactional
    public Department getDepartment(User user){
        return (Department) em.createQuery("select d from departments d where d.employers = ?1").setParameter(1, user.getId()).getSingleResult();
    }
}

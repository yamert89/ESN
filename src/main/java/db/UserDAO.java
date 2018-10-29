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
    public void persistUser(User user){
        em.persist(user);
    }

    @Transactional
    public User getUserById(Integer id){
        return em.find(User.class, id);
    }

    @Transactional
    public Department getDepartment(User user){
        return (Department) em.createQuery("select d from Department d, in(d.employers) e where e.id = :user").setParameter("user", user.getId()).getSingleResult();

        /*"select d from Department d where d.id = (" +
                "select d.departments_id from departments_users d where d.employers_id = :user)"*/
    }
}

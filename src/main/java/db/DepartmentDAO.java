package db;

import entities.Department;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import sun.plugin2.message.SetAppletSizeMessage;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository("department_dao")
@Transactional
public class DepartmentDAO {


    @PersistenceContext
    private EntityManager em;

    @Transactional
    public Department getDepartmentById(int id){
        return (Department) em.createQuery("select d from departments d where d.id = ?1").setParameter(1, id).getSingleResult();
    }
    @Transactional
    public Set<Department> getChildren(Department parent){
        Set<Department> set = new HashSet<>();
        List result = em.createQuery("select c from CHILDREN_DEPARTMENTS c where c.dep_id = :parent").setParameter("parent", parent).getResultList();
        for (Object ob :
                result) {
            set.add((Department) ob);
        }
        return set;
    }

    @Transactional
    public void saveChildren(Department department, Set<Department> children){
        em.createNativeQuery("create table if not exists CHILDREN_DEPARTMENTS (dep_id int not null, children int)");
        for (Department child:
              children) {
            em.createNativeQuery("insert into CHILDREN_DEPARTMENTS values (?, ?)").setParameter(1, department).setParameter(2, child);
        }
    }

    //TODO удалять детей
}

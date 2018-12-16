package esn.db;

import esn.entities.Department;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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
    public void persistDepartment(Department department){
        em.persist(department);
    }

    @Transactional
    public void merge(Department department){
        em.merge(department);
    }

    @Transactional
    public Department getDepartmentById(Integer id){
        return em.find(Department.class, id);
    }

    @Transactional
    public Department getDepartmentByName(String name){
        return (Department) em.createQuery("select d from Department d where d.name = :name")
                .setParameter("name", name).getSingleResult();
    }
    @Transactional
    public Set<Department> getChildren(Department parent){
        List result = em.createNativeQuery("select c.children from CHILDREN_DEPARTMENTS c where c.dep_id = ?").setParameter(1, parent).getResultList(); //TODO
        HashSet<Department> set = new HashSet<>();
        for (Object i: result) {
            set.add(getDepartmentById((Integer)i));

        }
        return set;
    }

    @Transactional
    public void saveChildren(Department department, Set<Department> children){
        em.createNativeQuery("create table if not exists CHILDREN_DEPARTMENTS (dep_id int not null, children int unique)").executeUpdate();
        Set<Department> savedDepartments = getChildren(department);
        for (Department child:
              children) {
            if (savedDepartments.contains(child)) continue;
            em.createNativeQuery("insert into CHILDREN_DEPARTMENTS values (?, ?)").setParameter(1, department).setParameter(2, child).executeUpdate();
        }
    }

    //TODO удалять детей
}

package esn.db;

import esn.entities.Department;
import esn.entities.User;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;

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
    public Department getDepartmentById(Long id){
        return em.find(Department.class, id);
    }

    @Transactional
    public Department getDepartmentByName(String name){
        return (Department) em.createQuery("select d from Department d where d.name = :name")
                .setParameter("name", name).getSingleResult();
    }

    @Transactional
    public Department getDepartmentWithUsersAndChildren(long id){
        EntityGraph graph = em.getEntityGraph("Department.employers_children");
        Map hints = new HashMap<>(1);
        hints.put("javax.persistence.loadgraph", graph);

        Department d = em.find(Department.class, id, hints);

        return d;
    }

    @Transactional
    public Department getDepartmentWithUsersAndChildrenALTER(long id){
        Department d = em.find(Department.class, id);
        depsInit(d);

        return d;
    }

    private void depsInit(Department d){
        Hibernate.initialize(d.getChildren());
        Hibernate.initialize(d.getEmployers());
        for (Department dep :
                d.getChildren()) {
            depsInit(dep);
        }

    }


    @Transactional
    public Set<Department> getChildren(Department parent){
        List result = em.createNativeQuery("select c.children from CHILDREN_DEPARTMENTS c where c.dep_id = ?").setParameter(1, parent).getResultList(); //TODO
        HashSet<Department> set = new HashSet<>();
        for (Object i: result) {
            set.add(getDepartmentById((Long)i));

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


    private List<Long> getHeadDepartmentsId(){
        return (List<Long>) em.createQuery("select d.id from Department d where d.parent is null").getResultList();
    }

    @Transactional
    public List<Department> getHeadDepartments(){
        List<Long> ids = getHeadDepartmentsId();
        List<Department> deps = new ArrayList<>(ids.size());
        for (Long id :
                ids) {
            deps.add(getDepartmentWithUsersAndChildrenALTER(id));
        }
        return deps;

    }


}

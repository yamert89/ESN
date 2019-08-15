package esn.db;

import esn.entities.Department;
import esn.entities.Organization;
import esn.viewControllers.main.GroupsController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

    private final static Logger logger = LogManager.getLogger(GroupsController.class);


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
        Department department = em.find(Department.class, id);
        return department;
    }

    @Transactional
    public Department getReference(Long id){
        return em.getReference(Department.class, id);
    }

    @Transactional
    public Department getDepartmentByName(String name, Organization org){
        try {

            return (Department) em.createQuery("select d from Department d where d.organization = :org AND d.name = :name")
                    .setParameter("org", org)
                    .setParameter("name", name).getSingleResult();
        }catch (Exception e){
            logger.error(e.getMessage(), e);
        }
        return null;
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



    private List<Long> getHeadDepartmentsId(Organization org){
        return (List<Long>) em.createQuery("select d.id from Department d where d.organization = :org and d.parent is null")
                .setParameter("org", org).getResultList();
    }

    @Transactional
    public Set<Department> getHeadDepartments(Organization org){
        List<Long> ids = getHeadDepartmentsId(org);
        Set<Department> deps = new HashSet<>(ids.size());
        for (Long id :
                ids) {
            deps.add(getDepartmentWithUsersAndChildrenALTER(id));
        }
        return deps;

    }

  /*  @Transactional
    public Department getDefaultDepartment(Organization org) throws Exception{

        Department department = new Department("default", "", null);
        Set<User> users = new HashSet<>(em.createQuery("select u from User u where u.organization = :org").setParameter("org", org).getResultList());
        department.setEmployers(users);
        department.setChildren(new HashSet<>());
        return department;
    }*/




}

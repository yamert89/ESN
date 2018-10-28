package db;

import entities.Organization;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


@Repository
@Transactional
public class OrganizationDAO {

    @PersistenceContext
    EntityManager em;

    @Transactional
    public void persistOrg(Organization org){
        em.persist(org);
    }

    @Transactional
    public Organization getOrgById(Integer id){

       return em.find(Organization.class, id);
    }

}

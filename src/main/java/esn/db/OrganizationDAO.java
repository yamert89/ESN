package esn.db;

import esn.entities.Organization;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.NoSuchElementException;


@Repository("orgDao")
@Transactional
public class OrganizationDAO {

    @PersistenceContext
    EntityManager em;

    @Transactional
    public void persistOrg(Organization org){
        em.persist(org);
    }

    @Transactional
    public void update(Organization org){
        em.merge(org);
    }

    @Transactional
    public Organization getOrgById(Integer id){
       return em.find(Organization.class, id);
    }

    @Transactional
    public Organization getOrgByURL(String url){
        Organization organization = null;

        try {
            organization = (Organization) em.createQuery("select org from Organization org where org.urlName = :url")
                    .setParameter("url", url).getSingleResult();
        }catch (NoSuchElementException e){
            System.out.println("URL " + url);
            e.printStackTrace();
        }
        return organization;
    }

    @Transactional
    public Organization getOrgByKey(String key){
        return (Organization) em.createQuery("select org from Organization org where org.key = :k")
                .setParameter("k", key).getSingleResult();
    }

    @Transactional
    public List<String> getNickNames(){
        List<String> list = em.createQuery("select u.nickName from User u").getResultList();
        list.forEach(String::intern);
        return list;
    }

    @Transactional
    public List<String> getLogins(){
        List<String> list = em.createQuery("select u.login from User u").getResultList();
        System.out.println(list.size());
        list.forEach(item -> item = item.intern());
        return list;
    }

    @Transactional
    public List<Organization> getAllOrgs(){
        List<Organization> list = em.createQuery("select o from Organization o").getResultList();
        return list;
    }





}

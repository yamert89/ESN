package esn.db;

import esn.configs.GeneralSettings;
import esn.entities.Organization;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;


@Repository("orgDao")
@Transactional
public class OrganizationDAO {
    private final static Logger logger = LogManager.getLogger(OrganizationDAO.class);

    @PersistenceContext
    EntityManager em;



    @Transactional
    public void persistOrg(Organization org) throws ConstraintViolationException {
        em.persist(org);
    }

    @Transactional
    public Organization update(Organization org){
        return em.merge(org);
    }

    @Transactional
    public Organization getOrgById(Integer id){
       return em.find(Organization.class, id);
    }

    @Transactional
    public Organization getOrgByURL(String url){
        return getOrgByUrlTrans(url);
    }

    private Organization getOrgByUrlTrans(String url){
        Organization organization = null;

        try {
            organization = (Organization) em.createQuery("select org from Organization org where org.urlName = :url")
                    .setParameter("url", url).getSingleResult();
        }catch (NoResultException e){
            logger.debug("URL " + url);
            return null;
        }
        return organization;
    }

   /* @Transactional
    public Organization getOrgByURLWithEmployers(String url){
        Organization organization = getOrgByUrlTrans(url);
        Hibernate.initialize(organization.getAllEmployers());
        return organization;
    }*/

    @Transactional
    public Organization getOrgByURLWithDepartments(String url){
        Organization organization = getOrgByUrlTrans(url);
        Hibernate.initialize(organization.getDepartments());
        return organization;
    }



    @Transactional
    public Organization getOrgByKey(String key){
        try {
            return (Organization) em.createQuery("select org from Organization org where org.corpKey = :k or org.adminKey = :k")
                    .setParameter("k", key).getSingleResult();
        }catch (NoResultException e){
            return null;
        }
    }

    @Transactional
    public List<String> getNickNames(){
        List<String> list = em.createQuery("select u.nickName from User u").getResultList();
        list.forEach(String::intern);
        return list;
    }

    @Transactional
    public List<String> getLogins(Organization org){
        List<String> list = em.createQuery("select u.login from User u where u.organization = :org").setParameter("org", org).getResultList();
        logger.debug(list.size());
        list.forEach(item -> item = item.intern());
        return list;
    }

    @Transactional
    public List<Organization> getAllOrgs(){
        List<Organization> list = em.createQuery("select o from Organization o").getResultList();
        return list;
    }

    @Transactional
    public boolean hasAdmin(String orgUrl){
        boolean res = false;
        try {
            res = (boolean) em.createQuery("select org.hasAdmin from Organization org where org.urlName = :url")
                    .setParameter("url", orgUrl).getSingleResult();
        }catch (NoResultException e){
            return false;
        }
        return res;
    }

    @Transactional
    public void deleteAllDepartmentsInUsers(){
        em.createQuery("update User u set u.department = null").executeUpdate();
    }

    @Transactional
    public boolean isAdminKey(String key, int orgId){
        return (Boolean) em.createQuery("select (org.adminKey = :key) as p from Organization org where org.id = :org_id")
                .setParameter("key", key)
                .setParameter("org_id", orgId)
                .getSingleResult();

    }







}

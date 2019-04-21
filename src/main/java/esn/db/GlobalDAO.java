package esn.db;

import esn.entities.secondary.ContactGroup;
import esn.entities.secondary.StoredFile;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@Transactional
public class GlobalDAO {



    @PersistenceContext
    private EntityManager em;



    @Transactional
    public List<StoredFile> getSharedFiles(){
        return em.createQuery("select f from StoredFile f where f.shared = true").getResultList();
    }

    @Transactional
    public void persistGroup(ContactGroup group){
        em.persist(group);
    }





}

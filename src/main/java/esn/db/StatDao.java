package esn.db;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
@Transactional
public class StatDao {
    private final static Logger logger = LogManager.getLogger(StatDao.class);

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void stat(String path, String host){
        try {
            long count = (int) em.createNativeQuery("select count from stat where host = ? and path = ?")
                    .setParameter(1, host)
                    .setParameter(2, path).getSingleResult();
            em.createNativeQuery("insert into stat values (?,?,?)")
                    .setParameter(1, path)
                    .setParameter(2, host)
                    .setParameter(3, count).executeUpdate();
        }catch (Exception e){
            logger.error("stat", e);
        }

    }
}

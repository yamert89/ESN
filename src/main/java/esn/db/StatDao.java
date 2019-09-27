package esn.db;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigInteger;

@Repository
@Transactional
public class StatDao {
    private final static Logger logger = LogManager.getLogger(StatDao.class);

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void stat(String path, String host){

        BigInteger count = em.createNativeQuery("select count from stat where path = ? and host = ?")
                .setParameter(1, path)
                .setParameter(2, host)

        try {
            em.createNativeQuery("update stat set count = count + 1 where path = ? and host = ?")
                    .setParameter(1, path)
                    .setParameter(2, host)
                    .executeUpdate();
        }catch (Exception e){
            logger.error("stat", e);
        }

    }
}

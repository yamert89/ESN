package esn.db;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Repository("wall_dao")
@Transactional
public class WallDAO {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void savePost(){
        /*em.createQuery();*/
    }
}

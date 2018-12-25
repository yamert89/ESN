package esn.db;

import esn.utils.GeneralSettings;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.List;

@Repository("wall_dao")
@Transactional
public class WallDAO {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void savePost(String text, long id, Timestamp time){
        em.createNativeQuery("insert into gen_chat_history values(null, ?, ?, ?)")
                .setParameter(1, text)
                .setParameter(2, id)
                .setParameter(3, time)
                .executeUpdate();
    }

    @Transactional
    public List getHistory(){
        List resultList = em.createNativeQuery("select * from gen_chat_history limit ?")
                .setParameter(1, GeneralSettings.AMOUNT_GENCHAT_MESSAGES).getResultList();
        return null;
    }

    @Transactional
    public void createChatTable(){
        em.createNativeQuery("create table if not exists gen_chat_history (" +
                "id long primary key auto_increment," +
                "message varchar(500)," + //TODO соблюсти ограничение
                "user_id int not null," +
                "time timestamp not null)").executeUpdate();
    }
}

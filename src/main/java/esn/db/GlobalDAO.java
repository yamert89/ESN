package esn.db;

import esn.entities.GenChatMessage;
import esn.utils.GeneralSettings;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Repository
@Transactional
public class GlobalDAO {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void saveGenMessage(String message, String nameAuthor, Timestamp time){
        Query query = em.createNativeQuery("insert into generalchat values (null, ?, ?, ?)")
                .setParameter(1, message)
                .setParameter(2, nameAuthor)
                .setParameter(3, time);
        query.executeUpdate();
    }

    @Transactional
    public List<GenChatMessage> getGenMessages(){
        List<GenChatMessage> list = new ArrayList<>(GeneralSettings.AMOUNT_GENCHAT_MESSAGES);
        List<Object[]> arr = em.createNativeQuery("select * from generalchat limit ?")
                .setParameter(1, GeneralSettings.AMOUNT_GENCHAT_MESSAGES)
                .getResultList();
        for (Object[] row :
                arr) {
            list.add(new GenChatMessage((String) row[0], (String) row[1], (Timestamp) row[2]));
        }
        return list;

    }
}

package esn.db;

import esn.entities.AbstractMessage;
import esn.entities.GenChatMessage;
import esn.entities.Post;
import esn.utils.GeneralSettings;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Repository
@Transactional
public class GlobalDAO {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void saveMessage(int userId, String message, Timestamp time, String org_url, Class<? extends AbstractMessage> mesClass){
        try {
            String tableName = mesClass == GenChatMessage.class ? "generalchat" : "wall";

            em.createNativeQuery("create table if not exists " + tableName +
                    " (id int not null auto_increment primary key, " +
                    "message varchar(500), " +
                    "userId int, " +
                    "time timestamp, " +
                    "org_url varchar(20))") //TODO Учесть ограничения базы (везде) !!!
                    .executeUpdate();
            Query query = em.createNativeQuery("insert into ".concat(tableName).concat("(id, message, userId, time, org_url) values (null, ?, ?, ?, ?)"))
                    .setParameter(1, message)
                    .setParameter(2, userId)
                    .setParameter(3, time)
                    .setParameter(4, org_url);
            query.executeUpdate();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Transactional
    public List<? extends AbstractMessage> getMessages(String orgUrl, Class<? extends AbstractMessage> mesClass){
        List<AbstractMessage> list = new ArrayList<>(GeneralSettings.AMOUNT_GENCHAT_MESSAGES);
        List<Object[]> arr = null;
        if (mesClass == GenChatMessage.class){
            arr = em.createNativeQuery("select * from generalchat limit ? where org_url =?")
                    .setParameter(1, GeneralSettings.AMOUNT_GENCHAT_MESSAGES)
                    .setParameter(2, orgUrl)
                    .getResultList();
            for (Object[] row :
                    arr) {
                list.add(new GenChatMessage((int) row[1], (String) row[0], (Timestamp) row[2], (String) row[3]));
            }
        }else if (mesClass == Post.class){
            arr = em.createNativeQuery("select * from wall limit ?")
                    .setParameter(1, GeneralSettings.AMOUNT_WALL_MESSAGES)
                    .getResultList();
            for (Object[] row :
                    arr) {
                list.add(new Post((int) row[1], (String) row[0], (Timestamp) row[2], (String) row[3]));
            }
        }

        return list;

    }



}

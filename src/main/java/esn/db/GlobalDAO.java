package esn.db;

import esn.entities.secondary.AbstractMessage;
import esn.entities.secondary.GenChatMessage;
import esn.entities.secondary.Post;
import esn.entities.secondary.StoredFile;
import esn.configs.GeneralSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Repository
@Transactional
public class GlobalDAO {

    private final String CHECKTABLE_POSTGRES = "SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME='wall'";
    private final String CHECKTABLE_MYSQL = "show tables like 'wall'";

    @PersistenceContext
    private EntityManager em;

    private UserDAO userDAO;
    @Autowired
    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

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
                    .executeUpdate();  //TODO Создать таблицу до её чтения в wall
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

        try {
            em.createNativeQuery(CHECKTABLE_MYSQL).getSingleResult();
            //em.createNativeQuery("show tables like 'generalchat'").getSingleResult();
        }catch (NoResultException e){
            return null;
        }




        List<AbstractMessage> list = new ArrayList<>(GeneralSettings.AMOUNT_GENCHAT_MESSAGES);
        List<Object[]> arr = null;
        try {
            if (mesClass == GenChatMessage.class) {
                Query query = em.createNativeQuery("select * from generalchat where org_url = ? limit ?")
                        .setParameter(2, GeneralSettings.AMOUNT_GENCHAT_MESSAGES)
                        .setParameter(1, orgUrl);
                arr = query.getResultList();

                for (Object[] row :
                        arr) {

                    list.add(new GenChatMessage((int) row[2], (String) row[1], (Timestamp) row[3], (String) row[4], userDAO));
                }
            } else if (mesClass == Post.class) {
                arr = em.createNativeQuery("select * from wall where org_url = ? order by time desc limit ?") //TODO Постраничный вывод
                        .setParameter(2, GeneralSettings.AMOUNT_GENCHAT_MESSAGES)
                        .setParameter(1, orgUrl)
                        .getResultList();
                for (Object[] row :
                        arr) {
                    list.add(new Post((int) row[2], (String) row[1], (Timestamp) row[3], (String) row[4], userDAO));
                }
            }
        }catch (Exception e){
            //e.printStackTrace();
        }

        return list;

    }

    @Transactional
    public List<StoredFile> getSharedFiles(){
        return em.createQuery("select f from StoredFile f where f.shared = true").getResultList();
    }



}

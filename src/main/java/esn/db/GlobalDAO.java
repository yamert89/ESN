package esn.db;

import esn.entities.secondary.*;
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

    private final String CHECKTABLE_POSTGRES = "SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME=";
    private final String CHECKTABLE_MYSQL = "show tables like ";
    private final String CREATE_TABLE_CONSTRAINTS_POSTGRES = " (id SERIAL, message varchar(500), userId int, time timestamp, orgId int)"; //TODO TIMESTAMP?
    private final String CREATE_TABLE_CONSTRAINTS_MYSQL = " (id int not null auto_increment primary key, message varchar(500), userId int, time timestamp, orgId int)"; //TODO long
    private final String SELECT_CHAT_MESSAGES_MYSQL_WITHIDX = "select * from generalchat where orgId = ? and id < ? order by time desc limit ?";
    private final String SELECT_WALL_MESSAGES_MYSQL_WITHIDX = "select * from wall where orgId = ? and id < ? order by time desc limit ?";
    private final String SELECT_CHAT_MESSAGES_MYSQL = "select * from generalchat where orgId = ? order by time desc limit ?";
    private final String SELECT_WALL_MESSAGES_MYSQL = "select * from wall where orgId = ? order by time desc limit ?";
    private final String SELECT_CHAT_MESSAGES_POSTGRES_WITHIDX = "select * from generalchat where orgId = ? and id < ? order by time desc limit ?";
    private final String SELECT_WALL_MESSAGES_POSTGRES_WITHIDX = "select * from wall where orgId = ? and id < ? order by time desc limit ?";
    private final String SELECT_CHAT_MESSAGES_POSTGRES = "select * from generalchat where orgId = ? order by time desc limit ?";
    private final String SELECT_WALL_MESSAGES_POSTGRES = "select * from wall where orgId = ? order by time desc limit ?";

    @PersistenceContext
    private EntityManager em;

    private UserDAO userDAO;
    @Autowired
    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Transactional
    public void saveMessage(int userId, String message, Timestamp time, int orgId, Class<? extends AbstractMessage> mesClass){
        try {
            String tableName = mesClass == GenChatMessage.class ? "generalchat" : "wall";

            em.createNativeQuery("create table if not exists " + tableName + CREATE_TABLE_CONSTRAINTS_MYSQL) //TODO Учесть ограничения базы (везде) !!!
                    .executeUpdate();  //TODO Создать таблицу до её чтения в wall
            Query query = em.createNativeQuery("insert into ".concat(tableName).concat("(message, userId, time, orgId) values (?, ?, ?, ?)"))
                    .setParameter(1, message)
                    .setParameter(2, userId)
                    .setParameter(3, time)
                    .setParameter(4, orgId);
            query.executeUpdate();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Transactional
    public void deleteMessage(int userId, String text, int orgId, Class<? extends AbstractMessage> mesClass){
        String tableName = mesClass == GenChatMessage.class ? "generalchat" : "wall";
        em.createNativeQuery("delete from " + tableName + " where orgId = " + orgId + " and userId = " + userId + " and message = '" + text + "' " +
                "order by time desc limit 1").executeUpdate(); //TODO benchmark
    }

    @Transactional
    public List<AbstractMessage> getMessages(int orgId, int lastIdx, Class<? extends AbstractMessage> mesClass){

        List<AbstractMessage> list = new ArrayList<>(GeneralSettings.AMOUNT_GENCHAT_MESSAGES);
        List<Object[]> arr = null;
        try {
            if (mesClass == GenChatMessage.class) {
                try {
                    em.createNativeQuery(CHECKTABLE_MYSQL + "'generalchat'").getSingleResult();
                }catch (NoResultException e){
                    return null;
                }

                Query query = lastIdx == -1 ?
                        em.createNativeQuery(SELECT_CHAT_MESSAGES_MYSQL)
                                .setParameter(2, GeneralSettings.AMOUNT_GENCHAT_MESSAGES)
                                .setParameter(1, orgId) :
                        em.createNativeQuery(SELECT_CHAT_MESSAGES_MYSQL_WITHIDX)
                                .setParameter(3, GeneralSettings.AMOUNT_GENCHAT_MESSAGES)
                                .setParameter(1, orgId)
                                .setParameter(2, lastIdx);

                arr = query.getResultList();

                for (Object[] row :
                        arr) {

                    list.add(new GenChatMessage((int) row[0], (int) row[2], (String) row[1], (Timestamp) row[3], (int) row[4], userDAO));
                }
            } else if (mesClass == Post.class) {
                try {
                    em.createNativeQuery(CHECKTABLE_MYSQL + "'wall'").getSingleResult();
                }catch (NoResultException e){
                    return null;
                }
                Query query = lastIdx == -1 ?
                        em.createNativeQuery(SELECT_WALL_MESSAGES_MYSQL)
                                .setParameter(2, GeneralSettings.AMOUNT_WALL_MESSAGES)
                                .setParameter(1, orgId) :
                        em.createNativeQuery(SELECT_WALL_MESSAGES_MYSQL_WITHIDX)
                                .setParameter(3, GeneralSettings.AMOUNT_WALL_MESSAGES)
                                .setParameter(1, orgId)
                                .setParameter(2, lastIdx);
                arr = query.getResultList();
                for (Object[] row :
                        arr) {
                    list.add(new Post((int) row[0], (int) row[2], (String) row[1], (Timestamp) row[3], (int) row[4], userDAO));
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return list;

    }

    @Transactional
    public List<StoredFile> getSharedFiles(){
        return em.createQuery("select f from StoredFile f where f.shared = true").getResultList();
    }

    @Transactional
    public void persistGroup(ContactGroup group){
        em.persist(group);
    }



}

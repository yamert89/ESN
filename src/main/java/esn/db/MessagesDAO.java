package esn.db;

import esn.configs.GeneralSettings;
import esn.entities.User;
import esn.entities.secondary.AbstractMessage;
import esn.entities.secondary.GenChatMessage;
import esn.entities.secondary.Post;
import esn.entities.secondary.PrivateChatMessage;
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
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository("messageDao")
@Transactional
public class MessagesDAO {

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

    private UserDAO userDAO;

    @PersistenceContext
    private EntityManager em;

    @Autowired
    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }


    public void persist(PrivateChatMessage message){
        em.persist(message);
    }

    public Set<PrivateChatMessage> getMessages(User owner, User companion, int orgId){
        Query query = em.createQuery("select m from PrivateChatMessage m where m.sender_id = :sender and m.recipient_id = :recipient and m.orgId = :orgId")
                .setParameter("sender", owner.getId()).setParameter("recipient", companion.getId()).setParameter("orgId", orgId);
        List res1 = query.getResultList();
        List res2 = query.setParameter("sender", companion.getId()).setParameter("recipient", owner.getId()).getResultList();

        return (Set<PrivateChatMessage>) Stream.concat(res1.stream(), res2.stream())
                .limit(GeneralSettings.AMOUNT_PRIVATECHAT_MESSAGES)
                .collect(Collectors.toCollection(TreeSet::new));


    }

    public void updateReadedMessages(Long[] ids){
        try {
            for (Long id :
                    ids) {
                if (id == null) return;
                em.createQuery("update PrivateChatMessage m set m.readed = true where m.id = :id").setParameter("id", id).executeUpdate(); //TODO one query
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public User getSender(long senderId){
        return em.find(User.class, senderId);
    }

    public User getRecipient(long recipientId){
        return em.find(User.class, recipientId);
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
    public Timestamp getLastTimeOfMessage(Class<?> clas, int orgId){
        String query = clas == GenChatMessage.class ?
                "select time from generalchat where orgId = :orgId order by time desc limit 1" :
                "select time from private_chat_history where orgId = :orgId order by time desc limit 1";
        return (Timestamp) em.createNativeQuery(query).setParameter("orgId", orgId).getSingleResult();
    }

    @Transactional
    public Object[] getOfflinePrivateMSenderIds(Timestamp visitTime, int orgId){
        return em.createQuery("select m.sender_id from PrivateChatMessage m where m.orgId = :orgId and m.time > :visitTime")
                .setParameter("orgId", orgId)
                .setParameter("visitTime", visitTime)
                .getResultList().toArray();
    }
}

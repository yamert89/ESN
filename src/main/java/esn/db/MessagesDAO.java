package esn.db;

import esn.configs.GeneralSettings;
import esn.db.syntax.MySQLSyntax;
import esn.entities.User;
import esn.entities.secondary.AbstractMessage;
import esn.entities.secondary.GenChatMessage;
import esn.entities.secondary.Post;
import esn.entities.secondary.PrivateChatMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository("messageDao")
@Transactional
public class MessagesDAO {

    private MySQLSyntax syntax;

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

            em.createNativeQuery("create table if not exists " + tableName + syntax.CREATE_TABLE_CONSTRAINTS) //TODO Учесть ограничения базы (везде) !!!
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
                if (!checkTableExists(syntax.CHECKTABLE, "g")) return null;

                Query query = lastIdx == -1 ?
                        em.createNativeQuery(syntax.SELECT_CHAT_MESSAGES)
                                .setParameter(2, GeneralSettings.AMOUNT_GENCHAT_MESSAGES)
                                .setParameter(1, orgId) :
                        em.createNativeQuery(syntax.SELECT_CHAT_MESSAGES_WITHIDX)
                                .setParameter(3, GeneralSettings.AMOUNT_GENCHAT_MESSAGES)
                                .setParameter(1, orgId)
                                .setParameter(2, lastIdx);

                arr = query.getResultList();

                for (Object[] row :
                        arr) {

                    list.add(new GenChatMessage((int) row[0], (int) row[2], (String) row[1], (int) row[4], userDAO));
                }
            } else if (mesClass == Post.class) {
                if (!checkTableExists(syntax.CHECKTABLE, "w")) return null;
                Query query = lastIdx == -1 ?
                        em.createNativeQuery(syntax.SELECT_WALL_MESSAGES)
                                .setParameter(2, GeneralSettings.AMOUNT_WALL_MESSAGES)
                                .setParameter(1, orgId) :
                        em.createNativeQuery(syntax.SELECT_WALL_MESSAGES_WITHIDX)
                                .setParameter(3, GeneralSettings.AMOUNT_WALL_MESSAGES)
                                .setParameter(1, orgId)
                                .setParameter(2, lastIdx);
                arr = query.getResultList();
                for (Object[] row :
                        arr) {
                    list.add(new Post((int) row[0], (int) row[2], (String) row[1], (int) row[4], userDAO));
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return list;

    }

    @Transactional
    public Calendar getLastTimeOfMessage(Class<?> clas, int orgId){
        try {
            String tableName = clas == GenChatMessage.class ? "g" : "p";
            if (!checkTableExists(syntax.CHECKTABLE, tableName)) return null;
            String query = clas == GenChatMessage.class ?
                    "select time from generalchat where orgId = :orgId order by time desc limit 1" :
                    "select time from private_chat_history where orgId = :orgId order by time desc limit 1";
            Timestamp timestamp = (Timestamp) em.createNativeQuery(query).setParameter("orgId", orgId).getSingleResult();
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(timestamp.getTime());
            return calendar;
        }catch (NoResultException e){
            System.out.println("No result, first session");
            return null;
        }catch (ClassCastException e){
            e.printStackTrace();
        }
        return null;
    }

    @Transactional
    public Object[] getOfflinePrivateMSenderIds(Calendar visitTime, User recipient, int orgId){
        return em.createQuery("select m.sender_id from PrivateChatMessage m where m.orgId = :orgId and m.recipient_id = :recipient and m.time > :visitTime")
                .setParameter("orgId", orgId)
                .setParameter("recipient", recipient.getId())
                .setParameter("visitTime", visitTime)
                .getResultList().toArray();
    }

    @Transactional
    protected boolean checkTableExists(String dbType, String table) {
        String tableName = "";
        try {
            switch (table) {
                case "g":
                    tableName = "'generalchat'";
                    break;
                case "w":
                    tableName = "'wall'";
                    break;
                case "p":
                    tableName = "'private_chat_history'";
                    break;
            }
            em.createNativeQuery(dbType + tableName).getSingleResult();
        }catch (Exception e){
            return false;
        }
        return true;
    }
}

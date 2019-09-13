package esn.db.message;


import esn.configs.GeneralSettings;
import esn.db.UserDAO;
import esn.db.syntax.PostgresSyntax;
import esn.db.syntax.Syntax;
import esn.entities.User;
import esn.entities.secondary.AbstractMessage;
import esn.entities.secondary.GenChatMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Repository
@Transactional
public abstract class MessagesDAO {
    private final static Logger logger = LogManager.getLogger(MessagesDAO.class);

    @PersistenceContext
    protected EntityManager em;

    private UserDAO userDAO;

    protected Syntax syntax = GeneralSettings.DB_SYNTAX;

    @Autowired
    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    //all defaults for generalChat

    String abstractLastTimeOfMessageQuery(){return "select time from generalchat where orgId = :orgId and userid <> :userid order by time desc limit 1";}
    String abstractGetMessagesQuery(){return syntax.selectChatMessages();}
    String abstractGetMessagesQueryWithIdx(){return syntax.selectChatMessagesWithIdx();}
    int abstractGetMessagesQueryAmountMessages(){return GeneralSettings.AMOUNT_GENCHAT_MESSAGES;}
    String abstractTableName(){return "generalchat";}



    AbstractMessage createMessage(long id, String text,Timestamp time, int orgId, User user){
        return new GenChatMessage(id, text, time, orgId, user);
    };

    public List<AbstractMessage> getMessages(int orgId, long lastIdx){

        int amount = abstractGetMessagesQueryAmountMessages();
        List<AbstractMessage> list = new ArrayList<>(amount);
        try{

            List<Object[]> arr = null;
            Query query = lastIdx == -1 ?
                    em.createNativeQuery(abstractGetMessagesQuery())
                            .setParameter(2, amount)
                            .setParameter(1, orgId) :
                    em.createNativeQuery(abstractGetMessagesQueryWithIdx())
                            .setParameter(3, amount)
                            .setParameter(1, orgId)
                            .setParameter(2, lastIdx);

            arr = query.getResultList();

            for (Object[] row :
                    arr) {
                long id;
                try{
                    id = ((BigInteger)row[0]).longValue();
                } catch (ClassCastException e){
                    logger.debug("Postgres cast exception. Processing...");
                    id = Long.parseLong(String.valueOf(row[0]));
                }
                list.add(createMessage(id , (String) row[1], (Timestamp) row[3], (int) row[4], userDAO.getUserById((int) row[2])));
            }

        }catch (Exception e){
            logger.error("getMessages", e);
        }
        return list;

    }

    @Transactional
    public void saveMessage(int userId, String message, Timestamp time, int orgId){
        try {
            Query query = em.createNativeQuery("insert into " + abstractTableName() + "(message, userId, time, orgId) values (?, ?, ?, ?)")
                    .setParameter(1, message)
                    .setParameter(2, userId)
                    .setParameter(3, time)
                    .setParameter(4, orgId);
            query.executeUpdate();
        }catch (Exception e){
            logger.error(e.getMessage(), e);
        }
    }

    @Transactional
    public void deleteMessage(int userId, String text){
        if (syntax instanceof PostgresSyntax) em.createNativeQuery(syntax.deleteMessage(abstractTableName()))
                .setParameter(1, userId).setParameter(2, text).executeUpdate(); //TODO mysql не поддерживает
        else {
            /*Query query = em.createNativeQuery("select w.id from " + abstractTableName() + " w where w.userId = ? and w.message like ? order by w.time desc limit 1")
                    .setParameter(1, userId).setParameter(2, "%" + text + "%");
            int id = (int) query.getSingleResult();*/

            Query query = em.createNativeQuery(syntax.deleteMessage(abstractTableName()))
                    .setParameter(1, userId).setParameter(2, "%" + text + "%");
            query.executeUpdate();
        }

    }


    @Transactional
    public Calendar getLastTimeOfMessage(int orgId, int userid){
        try {
            Timestamp timestamp = (Timestamp) em.createNativeQuery(abstractLastTimeOfMessageQuery())
                    .setParameter("orgId", orgId)
                    .setParameter("userid", userid).getSingleResult();
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(timestamp.getTime());
            return calendar;
        }catch (NoResultException e){
            logger.debug("No result, first session");
            return null;
        }catch (ClassCastException e){
            logger.error(e.getMessage(), e);
        }catch (Exception e){
            logger.error(e.getMessage(), e);
        }
        return null;
    }


}

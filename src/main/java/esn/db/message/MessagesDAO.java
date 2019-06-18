package esn.db.message;


import esn.configs.GeneralSettings;
import esn.db.UserDAO;
import esn.db.syntax.MySQLSyntax;
import esn.db.syntax.PostgresSyntax;
import esn.db.syntax.Syntax;
import esn.entities.User;
import esn.entities.secondary.AbstractMessage;
import esn.entities.secondary.GenChatMessage;
import esn.entities.secondary.Post;
import esn.entities.secondary.PrivateChatMessage;
import org.hibernate.exception.SQLGrammarException;
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
import java.util.Set;

@Repository
@Transactional
public abstract class MessagesDAO {

    @PersistenceContext
    protected EntityManager em;

    private UserDAO userDAO;

    protected Syntax syntax = new MySQLSyntax();

    @Autowired
    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    //all defaults for generalChat

    String abstractLastTimeOfMessageQuery(){return "select time from generalchat where orgId = :orgId order by time desc limit 1";}
    String abstractGetMessagesQuery(){return syntax.selectChatMessages();}
    String abstractGetMessagesQueryWithIdx(){return syntax.selectChatMessagesWithIdx();}
    int abstractGetMessagesQueryAmountMessages(){return GeneralSettings.AMOUNT_GENCHAT_MESSAGES;}
    String abstractTableName(){return "generalchat";}

    AbstractMessage createMessage(long id, String text, int orgId, User user){
        return new GenChatMessage(id, text, orgId, user);
    };

    public List<AbstractMessage> getMessages(int orgId, int lastIdx){

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
                list.add(createMessage(((BigInteger)row[0]).longValue(), (String) row[1], (int) row[4], userDAO.getUserById((int) row[2])));
            }

        }catch (Exception e){
            e.printStackTrace();
            //list = getMessages(orgId, lastIdx);
        }
        return list;

    }

    @Transactional
    public void saveMessage(int userId, String message, Timestamp time, int orgId){
        try {
            Query query = em.createNativeQuery("insert into ".concat(abstractTableName()).concat("(message, userId, time, orgId) values (?, ?, ?, ?)"))
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
    public void deleteMessage(int userId, String text){
        em.createNativeQuery("delete from " + abstractTableName() + " where userId = " + userId + " and message = '" + text + "' " +
                "order by time desc limit 1").executeUpdate();
    }
    @Transactional
    public Calendar getLastTimeOfMessage(int orgId){
        try {
            Timestamp timestamp = (Timestamp) em.createNativeQuery(abstractLastTimeOfMessageQuery()).setParameter("orgId", orgId).getSingleResult();
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(timestamp.getTime());
            return calendar;
        }catch (NoResultException e){
            System.out.println("No result, first session");
            return null;
        }catch (ClassCastException e){
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


}

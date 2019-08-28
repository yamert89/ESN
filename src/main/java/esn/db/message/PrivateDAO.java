package esn.db.message;

import esn.configs.GeneralSettings;
import esn.entities.User;
import esn.entities.secondary.PrivateChatMessage;
import esn.viewControllers.main.GroupsController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Query;
import java.util.Calendar;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository("privateDao")
public class PrivateDAO extends MessagesDAO {

    private final static Logger logger = LogManager.getLogger(GroupsController.class);

    @Override
    final String abstractLastTimeOfMessageQuery() {
        return "select time from private_chat_history where orgId = :orgId order by time desc limit 1";
    }

    @Transactional
    public void persist(PrivateChatMessage message) {
        em.persist(message);
    }
    @Transactional
    public Set<PrivateChatMessage> getMessages(User owner, User companion, int orgId) {
        try {
            Query query = em.createQuery("select m from PrivateChatMessage m where m.sender_id = :sender and m.recipient_id = :recipient and m.orgId = :orgId")
                    .setParameter("sender", owner.getId()).setParameter("recipient", companion.getId()).setParameter("orgId", orgId);
            List res1 = query.getResultList();
            List res2 = query.setParameter("sender", companion.getId()).setParameter("recipient", owner.getId()).getResultList();
            return (Set<PrivateChatMessage>) Stream.concat(res1.stream(), res2.stream())
                    .limit(GeneralSettings.AMOUNT_PRIVATECHAT_MESSAGES)
                    .collect(Collectors.toCollection(TreeSet::new));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    @Transactional
    public void updateReadedMessages(Long[] ids){
        try {
            Query query = em.createQuery("update PrivateChatMessage m set m.readed = true where m.id = :id");
            for (Long id :
                    ids) {
                if (id == null) return;
                query.setParameter("id", id).executeUpdate();

            }
        }catch (Exception e){
            logger.error(e.getMessage(), e);
        }
    }

    @Transactional
    public Object[] getOfflinePrivateMSenderIds(Calendar visitTime, int recipientId, int orgId){
        return em.createQuery("select m.sender_id from PrivateChatMessage m where m.orgId = :orgId and m.recipient_id = :recipient and m.time > :visitTime")
                .setParameter("orgId", orgId)
                .setParameter("recipient", recipientId)
                .setParameter("visitTime", visitTime)
                .getResultList().toArray();
    }

    public User getSender(long senderId){
        return em.find(User.class, senderId);
    }

    public User getRecipient(long recipientId){
        return em.find(User.class, recipientId);
    }

}



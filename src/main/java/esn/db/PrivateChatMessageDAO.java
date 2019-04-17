package esn.db;

import esn.entities.secondary.PrivateChatMessage;
import esn.entities.User;
import esn.configs.GeneralSettings;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository("messageDao")
@Transactional
public class PrivateChatMessageDAO {

    @PersistenceContext
    private EntityManager em;

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
}

package esn.db;

import esn.entities.PrivateChatMessage;
import esn.entities.User;
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

    public Set<PrivateChatMessage> getMessages(User owner, User companion){
        Query query = em.createQuery("select m from PrivateChatMessage m where m.sender_id = :sender and m.recipient_id = :recipient")
                .setParameter("sender", owner).setParameter("recipient", companion);
        List res1 = query.getResultList();
        List res2 = query.setParameter("sender", companion).setParameter("recipient", owner).getResultList();

        return (Set<PrivateChatMessage>) Stream.concat(res1.stream(), res2.stream()).collect(Collectors.toCollection(TreeSet::new)); //TODO получать по 50 записей


    }

    public User getSender(long senderId){
        return em.find(User.class, senderId);
    }

    public User getRecipient(long recipientId){
        return em.find(User.class, recipientId);
    }
}

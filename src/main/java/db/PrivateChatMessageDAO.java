package db;

import entities.PrivateChatMessage;
import entities.User;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Collections;
import java.util.List;
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

    public List<PrivateChatMessage> getMessages(User owner, User companion){
        Query query = em.createQuery("select m from PrivateChatMessage m where m.sender_id = :sender and m.recipient_id = :recipient")
                .setParameter("sender", owner).setParameter("recipient", companion);
        List res1 = query.getResultList();
        List res2 = query.setParameter("sender", companion).setParameter("recipient", owner).getResultList();

        return (List<PrivateChatMessage>) Stream.concat(res1.stream(), res2.stream()).collect(Collectors.toSet());


    }

    public User getSender(long senderId){
        return em.find(User.class, senderId);
    }

    public User getRecipient(long recipientId){
        return em.find(User.class, recipientId);
    }
}

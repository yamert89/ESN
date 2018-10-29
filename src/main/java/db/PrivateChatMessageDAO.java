package db;

import entities.PrivateChatMessage;
import entities.User;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
@Transactional
public class PrivateChatMessageDAO {

    @PersistenceContext
    EntityManager em;

    public void persist(PrivateChatMessage message){
        em.persist(message);
    }

    public User getSender(long senderId){
        return em.find(User.class, senderId);
    }

    public User getRecipient(long recipientId){
        return em.find(User.class, recipientId);
    }
}

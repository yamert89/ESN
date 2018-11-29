package entities;

import db.PrivateChatMessageDAO;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "private_chat_history")
public class PrivateChatMessage implements Comparable<PrivateChatMessage>{

    @Id
    @GeneratedValue
    private long id;

    @Column(nullable = false)
    private String text;

    @Transient
    private User sender;

    @Column(nullable = false)
    private int sender_id;

    @Transient
    private User recipient;

    @Column(nullable = false)
    private int recipient_id;

    @Column(updatable = false)
    @CreationTimestamp
    private Timestamp time;

    @Transient
    private PrivateChatMessageDAO messageDAO;

    public PrivateChatMessage() {
    }

    public PrivateChatMessage(String text, User sender, User recipient, PrivateChatMessageDAO messageDAO) {
        this.sender = sender;
        this.recipient = recipient;
        this.messageDAO = messageDAO;
        this.text = text;
        sender_id = sender.getId();
        recipient_id = recipient.getId();
    }

    public User getSender() {
        if (sender != null) return sender;
        return messageDAO.getSender(sender_id);
    }

    public User getRecipient() {
        if (recipient != null) return recipient;
        return messageDAO.getRecipient(recipient_id);
    }


    @Override
    public int compareTo(PrivateChatMessage o) {
        return this.time.compareTo(o.time);
    }
}

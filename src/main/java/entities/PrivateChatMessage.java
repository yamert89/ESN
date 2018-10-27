package entities;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "private_chat_history")
public class PrivateChatMessage {

    @Id
    @GeneratedValue
    private long id;

    @Column(nullable = false)
    private User sender;

    @Column(nullable = false)
    private User recipient;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = false)
    @CreationTimestamp
    private Timestamp time;

    public PrivateChatMessage() {
    }

    public PrivateChatMessage(User sender, User recipient) {
        this.sender = sender;
        this.recipient = recipient;

    }
}

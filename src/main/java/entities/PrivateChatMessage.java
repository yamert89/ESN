package entities;

import db.converters.UserConverter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "private_chat_history")
public class PrivateChatMessage {

    @Id
    @GeneratedValue
    private long id;

    @Convert(converter = UserConverter.class)
    private User sender;

    @Convert(converter = UserConverter.class)
    private User recipient;


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

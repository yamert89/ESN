package entities;

import javafx.util.converter.DateTimeStringConverter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "private_chat_history")
public class PrivateChatMessage {

    @Id
    private long id;

    @Column
    private User sender;

    @Column
    private User recipient;

    @Column
    private LocalDateTime time;

    public PrivateChatMessage() {
    }

    public PrivateChatMessage(User sender, User recipient) {
        this.sender = sender;
        this.recipient = recipient;
        time = LocalDateTime.now();
    }
}

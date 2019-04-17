package esn.entities.secondary;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Calendar;

@Entity
@Table(name = "private_chat_history")
public class PrivateChatMessage implements Comparable<PrivateChatMessage>{

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, length = 800)
    private String text;

    @Column(nullable = false)
    private int sender_id;

    @Column(nullable = false)
    private int recipient_id;

    @Column(columnDefinition = "boolean default false")
    private boolean readed;

    private int orgId;

    @CreationTimestamp
    private Calendar time;


    public PrivateChatMessage() {
    }

    public PrivateChatMessage(String text, int sender_id, int recipient_id, int orgId) {
        this.text = text;
        this.sender_id = sender_id;
        this.recipient_id = recipient_id;
        this.orgId = orgId;
    }

    public String getText() {
        return text;
    }

    public int getSender_id() {
        return sender_id;
    }

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="HH:mm:ss  dd.MM.YY")
    public Calendar getTime() {
        return time;
    }

    public int getRecipient_id() {
        return recipient_id;
    }

    public boolean getReaded() {
        return readed;
    }

    @Override
    public int compareTo(PrivateChatMessage o) {
        int comp = this.time.compareTo(o.time);
        return  comp * -1;
    }

    public void setReaded(boolean readed) {
        this.readed = readed;
    }

    public Long getId() {
        return id;
    }


}

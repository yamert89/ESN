package esn.entities.secondary;

import esn.entities.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "stored_files")
public class StoredFile {
    @Id
    @GeneratedValue
    private int id;

    private String name;

    private LocalDateTime time;

    @ManyToOne
    private User owner;

    private boolean shared;


    public StoredFile(String name, LocalDateTime time, User owner, boolean shared) {
        this.name = name;
        this.time = time;
        this.owner = owner;
        this.shared = shared;

    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public User getOwner() {
        return owner;
    }

    public boolean isShared() {
        return shared;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setShared(boolean shared) {
        this.shared = shared;
    }


}

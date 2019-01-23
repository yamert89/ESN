package esn.entities.secondary;

import esn.entities.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "stored_files")
public class StoredFile {
    @Id
    @GeneratedValue
    private int id;
    @Column(unique = false)
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

    public StoredFile() {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StoredFile)) return false;
        StoredFile that = (StoredFile) o;
        return  getName().equals(that.getName());
    }

    @Override
    public int hashCode() {
        System.out.println("NAME " +  getName());
        System.out.println("Time " + getTime());
        System.out.println("Owner " + getOwner());
        System.out.println("SHARED " +  isShared());
        System.out.println("OwnerID " +  getOwner().getId());
        int hash = Objects.hash(getName());
        System.out.println("HASH " + hash);
        return hash;
    }
}

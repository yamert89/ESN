package esn.entities.secondary;

import esn.entities.Organization;
import esn.entities.User;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "stored_files")
public class StoredFile {
    @Id
    @GeneratedValue
    private Integer id;
    @Column(unique = false)
    private String name;

    @ManyToOne
    private Organization org;

    @Column(length = 7)
    private String extension;

    private Timestamp time;

    @ManyToOne
    private User owner;

    private boolean shared;


    public StoredFile(String name, Timestamp time, User owner, boolean shared) {
        int point = name.lastIndexOf(".");
        this.name = name.substring(0, point);
        extension = name.substring(point + 1);
        this.time = time;
        this.owner = owner;
        this.shared = shared;
        this.org = owner.getOrganization();
    }

    public StoredFile() {
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Timestamp getTime() {
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

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public String getExtension() {
        return extension;
    }

    public Organization getOrg() {
        return org;
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
        /*logger.debug("NAME " +  getName());
        logger.debug("Time " + getTime());
        logger.debug("Owner " + getOwner());
        logger.debug("SHARED " +  isShared());
        logger.debug("OwnerID " +  getOwner().getId());*/
        int hash = Objects.hash(getName());
        return hash;
    }
}

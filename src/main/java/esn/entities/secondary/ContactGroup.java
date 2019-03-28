package esn.entities.secondary;

import esn.entities.User;

import javax.persistence.*;
import java.util.Date;

@Entity
public class ContactGroup {
    @Id
    @GeneratedValue
    private Integer id;

    @Column(unique = true)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private User user;

    private int[] personIds;

    private boolean expandable;

    private Date creationDate = new Date();

    public ContactGroup() {
    }

    public ContactGroup(String name, User user, int[] personIds, boolean expandable) {
        this.name = name;
        this.user = user;
        this.personIds = personIds;
        this.expandable = expandable;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }


   /* public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }*/

    public int[] getPersonIds() {
        return personIds;
    }

    public void setPersonIds(int[] personIds) {
        this.personIds = personIds;
    }

    public boolean isExpandable() {
        return expandable;
    }

    public void setExpandable(boolean expandable) {
        this.expandable = expandable;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "ID: " + id + " | " + creationDate.toString();
    }
}

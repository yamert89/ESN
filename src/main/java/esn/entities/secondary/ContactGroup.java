package esn.entities.secondary;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class ContactGroup {
    @Id
    @GeneratedValue
    private int id;

    private String name;

    private int user_id; //TODO двусторонняя связь? лишняя таблица

    private int[] personIds;

    private boolean expandable;

    public ContactGroup() {
    }

    public ContactGroup(String name, int user_id, int[] personIds, boolean expandable) {
        this.name = name;
        this.user_id = user_id;
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


    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

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
}

package entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "users")
public class User {

    @Id
    private long id;

    @Column
    private String name;

    @Column
    private boolean admin;

    @Column
    private String position;

    @Column
    private String department;

    @Column
    private byte[] photo; // TODO incorrect type ?














    public User() {
    }

    public User(long id, String name, int age) {
        this.id = id;
        this.name = name;

    }


}

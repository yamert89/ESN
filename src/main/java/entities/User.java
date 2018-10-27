package entities;

import db.UserDAO;

import javax.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue
    private long id;

    @Column(nullable = false)
    private String name;


    private boolean admin;


    private String position;

    @Transient
    private Department department;


    private byte[] photo; // TODO incorrect type ?


    private UserSettings settings;

    @Transient
    private UserDAO userDAO; //TODO сильная связь. Можно избежать?


    public User() {
    }

    public User(long id, String name, int age, UserDAO userDAO) {
        this.id = id;
        this.name = name;
        this.userDAO = userDAO;

    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isAdmin() {
        return admin;
    }

    public String getPosition() {
        return position;
    }

    public Department getDepartment() {
        return userDAO.getDepartment(this);
    }

    public byte[] getPhoto() {
        return photo;
    }

    public UserSettings getSettings() {
        return settings;
    }
}

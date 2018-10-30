package entities;

import db.UserDAO;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue
    private int id;

    @Column(nullable = false)
    private String name;


    private boolean admin;


    private String position;

    @ManyToOne
    @JoinColumn(name = "DEPARTMENT_ID")
    private Department department;

    @ManyToOne
    @JoinColumn(name = "ORG_ID", nullable = false)
    private Organization organization;


    private byte[] photo; // TODO incorrect type ?


    private UserSettings settings;



    @Transient
    private UserDAO userDAO; //TODO сильная связь. Можно избежать?


    public User() {
    }

    public User(String name, Organization org, UserDAO userDAO) {
        this.name = name;
        this.userDAO = userDAO;
        this.organization = org;
    }

    public int getId() {
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

    /*public Department getDepartment() {
        if (department != null) return department;
        return userDAO.getDepartment(this);
    }*/

    public Department getDepartment() {
        return department;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public UserSettings getSettings() {
        return settings;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return getId() == user.getId() &&
                isAdmin() == user.isAdmin() &&
                Objects.equals(getName(), user.getName()) &&
                Objects.equals(getPosition(), user.getPosition()) &&
                Objects.equals(getDepartment(), user.getDepartment()) &&
                Objects.equals(getSettings(), user.getSettings());
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", department=" + department +
                ", organization=" + organization +
                '}';
    }
}

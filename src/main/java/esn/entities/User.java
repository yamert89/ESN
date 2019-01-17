package esn.entities;

import esn.db.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue
    private int id;

    @Size(min = 4, max = 50, message = "От 4х до 50 символов")
    @Column(nullable = false)
    @NotNull(message = "Имя не должно быть пустым")
    private String name;

    @Size(min = 4, max = 20, message = "От 3х до 20 символов")
    @NotNull
    private String login;

    @Size(min = 6, max = 32, message = "От 6 до 32 символов")
    @NotNull(message = "Пароль не может быть пустым")
    private String password;

    private boolean admin;

    private String position = "";

    @ManyToOne
    @JoinColumn(name = "DEPARTMENT_ID")
    private Department department;

    @ManyToOne
    @JoinColumn(name = "ORG_ID", nullable = false)
    private Organization organization;


    private String photo;  //filename

    private String photo_small; //filename


    private UserSettings settings;

    @ElementCollection
    @CollectionTable(name = "groups")
    @MapKeyColumn(name = "GROUP_NAME")
    @Column(name = "U_IDS")
    private Map<String, String[]> groups;

    @ElementCollection
    @CollectionTable(name = "notes")
    @MapKeyColumn(name = "NOTE_TIME")
    @Column(name = "NOTE_TEXT")
    private Map<Timestamp, String> notes;

    @Transient
    private boolean netStatus;



    @Transient
    private UserDAO userDAO; //TODO delete if unnecessary


    public User() {
    }

    public User(String name, Organization org) {
        this.name = name;
        this.organization = org;
    }

    @Autowired
    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
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

    public String getPassword(){
        return password;
    }

    public Organization getOrganization() {
        return organization;
    }

    /*public Department getDepartment() {
        if (department != null) return department;
        return userDAO.getDepartment(this);
    }*/

    public Department getDepartment() {
        return department;
    }

    public String getPhoto() {
        return photo;
    }

    public String getPhoto_small() {
        return photo_small;
    }

    public UserSettings getSettings() {
        return settings;
    }

    public boolean netStatus() {
        return netStatus;
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

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public void setPhoto_small(String photo_small) {
        this.photo_small = photo_small;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Map<String, String[]> getGroups() {
        return groups;
    }

    public boolean isNetStatus() {
        return netStatus;
    }

    public void setNetStatus(boolean netStatus) {
        this.netStatus = netStatus;
    }

    public Map<Timestamp, String> getNotes() {
        return notes;
    }

    public void setNotes(Map<Timestamp, String> notes) {
        this.notes = notes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return isAdmin() == user.isAdmin() &&
                Objects.equals(getName(), user.getName()) &&
                Objects.equals(getPosition(), user.getPosition()) &&
                Objects.equals(getDepartment(), user.getDepartment()) &&
                Objects.equals(getSettings(), user.getSettings());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, admin, position, department, organization, settings);
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                "passw=" + password +
                ", department=" + department +
                ", organization=" + organization +
                '}';
    }


}

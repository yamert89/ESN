package esn.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import esn.db.UserDAO;
import esn.entities.secondary.ContactGroup;
import esn.entities.secondary.StoredFile;
import esn.entities.secondary.UserInformation;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.util.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue
    private Integer id;

    @Size(min = 4, max = 50, message = "От 4х до 50 символов")
    @Column(nullable = false)
    @NotNull(message = "имя не должно быть пустым")
    private String name;

    @Size(min = 4, max = 20, message = "От 3х до 20 символов")
    @NotNull
    @JsonIgnore
    @Column(nullable = false)
    private String login;

    @Size(min = 6, max = 32, message = "От 6 до 32 символов")
    @NotNull(message = "пароль не может быть пустым")
    @Column(nullable = false)
    @JsonIgnore
    private String password;

    @JsonIgnore
    private boolean admin;

    @JsonIgnore
    @Column(columnDefinition = "boolean default true")
    private boolean male;

    @Column(columnDefinition = "varchar(50) default ' '")
    private String position = "";

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "DEPARTMENT_ID")
    @JsonIgnore
    private Department department;

    @ManyToOne
    @JoinColumn(name = "ORG_ID", nullable = false)
    @JsonIgnore
    private Organization organization;

    @Basic(fetch = FetchType.LAZY)
    @Valid
    private UserInformation userInformation;
    @Column(nullable = false)
    private String photo;  //filename

    @JsonIgnore
    @Column(nullable = false)
    private String photo_small; //filename

    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY, mappedBy = "owner", orphanRemoval=true) //TODO не удалять файлы удаленного пользователя
    @JsonIgnore
    private Set<StoredFile> storedFiles;

    @JsonIgnore
    private UserSettings settings;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER, mappedBy = "user")
    @JsonIgnore
    private Set<ContactGroup> groups;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "notes")
    @MapKeyColumn(name = "NOTE_TIME")
    @Column(name = "NOTE_TEXT")
    @org.hibernate.annotations.OrderBy(clause = "NOTE_TIME desc")
    @JsonIgnore
    private Map<Timestamp, String> notes = new LinkedHashMap<>();

    @Transient
    @JsonIgnore
    private boolean netStatus;



    @Transient
    @JsonIgnore
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

    public Set<ContactGroup> getGroups() {
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

    public Set<StoredFile> getStoredFiles() {
        return storedFiles;
    }

    public UserInformation getUserInformation() {
        return userInformation;
    }

    public void setUserInformation(UserInformation userInformation) {
        this.userInformation = userInformation;
    }

    public boolean isMale() {
        return male;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return  isAdmin() == user.isAdmin() &&
                Objects.equals(getName(), user.getName()) &&
                Objects.equals(getLogin(), user.getLogin()) &&
                Objects.equals(getPosition(), user.getPosition());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getLogin(), isAdmin(), getPosition());
    }

    @Override
    public String toString() {
        return super.toString();
        /*"hashGroups: " + groups.hashCode() + "user Createtime: " + new Date();*/
        /*return "User{" +
                "name='" + name + '\'' +
                "passw=" + password +
                ", department=" + department +
                ", organization=" + organization +
                '}';*/
    }


}

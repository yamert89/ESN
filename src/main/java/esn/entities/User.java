package esn.entities;

import esn.db.UserDAO;
import esn.db.converters.PasswordConverter;
import esn.utils.SimpleUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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
    @NotNull(message = "Логин не может быть пустым")
    private String login;

    @Size(min = 6, max = 20, message = "От 6 до 20 символов")
    @Lob
    @Convert(converter = PasswordConverter.class)
    @NotNull(message = "Пароль не может быть пустым")
    private byte[] password;

    private boolean admin;


    private String position;

    @ManyToOne
    @JoinColumn(name = "DEPARTMENT_ID")
    private Department department;

    @ManyToOne
    @JoinColumn(name = "ORG_ID", nullable = false)
    private Organization organization;


    private String photo;  //filename

    private String photo_small; //filename


    private UserSettings settings;

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

    public byte[] getPassword(){
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

    public void setPassword(byte[] password) {
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

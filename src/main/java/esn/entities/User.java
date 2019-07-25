package esn.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import esn.entities.secondary.ContactGroup;
import esn.entities.secondary.StoredFile;
import esn.entities.secondary.UserInformation;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.util.*;

@Entity
@Table(name = "users")
public class User implements Cloneable{

    @Id
    @GeneratedValue
    private Integer id;

    @Size(min = 4, max = 50, message = "введите имя от 4х до 50 символов")
    @Column(nullable = false)
    private String name;

    @Size(min = 4, max = 20, message = "введите логин от 3х до 20 символов")
    @JsonIgnore
    @Column(nullable = false)
    private String login;

    @Size(min = 6, max = 60, message = "введите пароль от 6 до 60 символов")
    @Column(nullable = false)
    @JsonIgnore
    private String password;

    @JsonIgnore
    @Column(columnDefinition = "boolean default true")
    private boolean male;

    @Column(columnDefinition = "varchar(50) default ' '")
    private String position = "";

    @JsonIgnore
    @Column(columnDefinition = "varchar(15) default 'ROLE_USER'")
    private String authority;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "department_id")
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

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Session> sessions;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "notes")
    @MapKeyColumn(name = "NOTE_TIME")
    @Column(name = "NOTE_TEXT")
    @org.hibernate.annotations.OrderBy(clause = "NOTE_TIME desc")
    @JsonIgnore
    private Map<Timestamp, String> notes = new LinkedHashMap<>();

    public User() {
    }

    public User(String name, Organization org) {
        this.name = name;
        this.organization = org;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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

    public Set<Session> getSessions() {
        return sessions;
    }

    public boolean isMale() {
        return male;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public void updateFromForm(User source){
        UserInformation inf = source.getUserInformation();
        Calendar birth = inf.getBirthDate();
        String phoneMobile = inf.getPhoneMobile();
        String phoneWork = inf.getPhoneWork();
        String phoneInternal = inf.getPhoneInternal();
        String email = inf.getEmail();
        if (source.position != null) this.position = source.position;
        if (birth != null) userInformation.setBirthDate(birth);
        if (phoneMobile != null) userInformation.setPhoneMobile(phoneMobile);
        if (phoneWork != null) userInformation.setPhoneWork(phoneWork);
        if (phoneInternal != null) userInformation.setPhoneInternal(phoneInternal);
        if (email != null) userInformation.setEmail(email);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return  Objects.equals(getName(), user.getName()) &&
                Objects.equals(getLogin(), user.getLogin()) &&
                Objects.equals(getPosition(), user.getPosition());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getLogin(), getPosition());
    }

    @Override
    public String toString() {
        return name;//groups.stream().map(ContactGroup::getName).collect(Collectors.joining(", "));
        /*"hashGroups: " + groups.hashCode() + "user Createtime: " + new Date();*/
        /*return "User{" +
                "name='" + name + '\'' +
                "passw=" + password +
                ", department=" + department +
                ", organization=" + organization +
                '}';*/
    }


}

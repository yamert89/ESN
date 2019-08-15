package esn.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "organizations")
public class Organization {
    @Id
    @GeneratedValue(generator = "ID_GENERATOR")
    private Integer id;

    @Column(length = 60)
    private String corpKey;

    @Column(length = 60)
    private String adminKey;

    @NotNull(message = "введите название организации")
    @Column(nullable = false, length = 100)
    @Size(max = 100, message = "название организации должно быть не более 100 символов")
    private String name;

    @NotNull(message = "введите относительный URL")
    @Column(nullable = false, length = 20, unique = true)
    @Size(min = 4, max = 20, message = "")
    @Pattern(regexp = "[A-Za-z0-9]{4,20}", message = "URL должен иметь от 4 до 20 латинских символов или цифр")
    private String urlName;

    @Size(max = 1000, message = "описание должно быть не более 1000 символов")
    @Column(length = 1000)
    private String description;

    @Column(columnDefinition = "varchar(255) default '/header.jpg'", nullable = false)
    private String headerPath;

    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<Department> departments = new HashSet<>(0);

    @OneToMany(orphanRemoval = true, mappedBy = "organization", fetch = FetchType.EAGER)
    private Set<User> allEmployers = new HashSet<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(joinColumns = @JoinColumn(name = "ORG_ID"))
    @Column(name = "POSITION", length = 50)
    private Set<String> positions = new HashSet<>();

    private boolean hasAdmin;

    private boolean disabled;



    public Organization() {
    }

    public Organization(String name, String description) {
        this.name = name;
        this.description = description;

    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrlName() {
        return urlName;
    }

    public void setUrlName(String urlName) {
        this.urlName = urlName;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public User getEmployerById(int id){
        for (User user : allEmployers) {
            if (user.getId() == id) return user;
        }
        return null;
    }

    public User getEmployerByLogin(String login){
        for (User user : allEmployers) {
            if (user.getLogin().equals(login)) return user;
        }
        return null;
    }

    public String getDescription() {
        return description;
    }

    public Set<Department> getDepartments() {
        return departments;
    }

    public Set<User> getAllEmployers() {
        return allEmployers;
    }

    public Set<String> getPositions() {
        return positions;
    }

    public void addPosition(String position){
        positions.add(position);
    }

    public void setPositions(Set<String> positions) {
        this.positions = positions;
    }

    public void addDepartment(Department department){
        departments.add(department);
    }

    public void setDepartments(Set<Department> departments) {
        this.departments = departments;
    }

    public String getCorpKey() {
        return corpKey;
    }

    public void setCorpKey(String corpKey) {
        this.corpKey = corpKey;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public String getHeaderPath() {
        return headerPath;
    }

    public void setHeaderPath(String headerPath) {
        this.headerPath = headerPath;
    }

    public boolean isHasAdmin() {
        return hasAdmin;
    }

    public void setHasAdmin(boolean hasAdmin) {
        this.hasAdmin = hasAdmin;
    }

    public String getAdminKey() {
        return adminKey;
    }

    public void setAdminKey(String adminKey) {
        this.adminKey = adminKey;
    }

    public void updateFromForm(Organization source){
        if (source.getName() != null) name = source.getName();
        if (source.getUrlName() != null) urlName = source.getUrlName();
    }


}

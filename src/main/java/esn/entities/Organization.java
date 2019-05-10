package esn.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "organizations")
public class Organization {
    @Id
    @GeneratedValue
    private Integer id;

    private String key;

    @NotNull
    @Column(nullable = false)
    private String name;

    private String urlName;

    private String description;

    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    private Set<Department> departments = new HashSet<>(0);

    @OneToMany(mappedBy = "organization", fetch = FetchType.EAGER)
    private Set<User> allEmployers = new HashSet<>(); //TODO

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(joinColumns = @JoinColumn(name = "ORG_ID"))
    @Column(name = "POSITION")
    private Set<String> positions = new HashSet<>();;


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

    public void addDepartment(Department department){
        departments.add(department);
    }

    public void setDepartments(Set<Department> departments) {
        this.departments = departments;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public User getUserByLogin(String nickname){
        try {
            for (User user:
                    allEmployers) {
                if (user.getLogin().equals(nickname)) return user;
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }
}

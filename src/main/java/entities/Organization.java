package entities;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "organizations")
public class Organization {
    @Id
    @GeneratedValue
    private int id;

    @Column(nullable = false)
    private String name;


    private String description;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}
                , fetch = FetchType.EAGER)
    private Set<Department> departments = new HashSet<>(0);

    @Transient
    private Set<User> allEmployers = new HashSet<>();
    @Transient
    private Set<String> positions = new HashSet<>();;

    // wall
    // chat


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

    public void addDepartment(Department department){
        departments.add(department);
    }
}

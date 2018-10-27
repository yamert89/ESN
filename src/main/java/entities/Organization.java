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

    @ElementCollection
    private Set<Department> departments = new HashSet<>();

    private Set<User> allEmployers;

    private Set<String> positions;

    // wall
    // chat


    public Organization() {
    }

    public Organization(String name, String description) {
        this.name = name;
        this.description = description;
        departments = new HashSet<>(0);
        allEmployers = new HashSet<>();
        positions = new HashSet<>();
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
}

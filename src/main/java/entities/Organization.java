package entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Set;

@Entity
@Table(name = "organizations")
public class Organization {
    @Id
    private int id;

    @Column
    private String name;

    @Column
    private String description;

    private Set<Department> departments;

    private Set<User> allEmployers;

    private Set<String> positions;

    // wall
    // chat


}

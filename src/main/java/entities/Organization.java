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

    private Set<String> departments;
    private Set<String> employers;
    private Set<String> positions;


}

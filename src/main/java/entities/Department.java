package entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Set;

@Entity
@Table(name = "departments")
public class Department {

    @Id
    private int id;

    @Column
    private String name;

    @Column
    private String description;

    private Set<User> employers;

    private Department parent;

    private Set<Department> children;

    public Department() {
    }

    public Department(String name, String description, Department parent) {
        this.name = name;
        this.description = description;
        this.parent = parent;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setEmployers(Set<User> employers) {
        this.employers = employers;
    }

    public void setParent(Department parent) {
        this.parent = parent;
    }

    public void setChildren(Set<Department> children) {
        this.children = children;
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

    public Set<User> getEmployers() {
        return employers;
    }

    public Department getParent() {
        return parent;
    }

    public Set<Department> getChildren() {
        return children;
    }
}

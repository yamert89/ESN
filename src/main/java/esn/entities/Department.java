package esn.entities;

import esn.db.DepartmentDAO;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "departments")
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(length = 1000)
    private String description;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER, mappedBy = "department")
    private Set<User> employers = new HashSet<>();

    @Transient
    private Department parent;

    private String parentId;

    @Transient
    private Set<Department> children;

    @Transient
    private DepartmentDAO departmentDAO; //TODO сильная связь. Можно избежать?


    public Department() {
    }

    public Department(String name, String description, Department parent) {
        this.name = name;
        this.description = description;
        this.parent = parent;
        parentId = String.valueOf(parent.getId());
    }

    public Department(String name, String description) {
        this.name = name;
        this.description = description;

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

    public void addChildren(Department child){
        if (children == null) children = new HashSet<>();
        children.add(child);
        departmentDAO.saveChildren(this, children); //TODO надо сохранять не каждый раз
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
        if (parentId != null) return departmentDAO.getDepartmentById(Integer.valueOf(parentId));
        return parent;
    }

    public Set<Department> getChildren() {
        if (children != null) return children;
        return departmentDAO.getChildren(this);
    }

    public void setDepartmentDAO(DepartmentDAO departmentDAO) {
        this.departmentDAO = departmentDAO;
    }

    public void addEmployer(User employer){
        employers.add(employer);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Department)) return false;
        Department that = (Department) o;
        return Objects.equals(getName(), that.getName()) &&
                Objects.equals(getDescription(), that.getDescription()) &&
                Objects.equals(parentId, that.parentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getDescription(), parentId);
    }
}

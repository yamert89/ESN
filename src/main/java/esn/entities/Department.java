package esn.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import esn.db.DepartmentDAO;
import esn.entities.converters.JsonNullSerializer;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "departments")
@NamedEntityGraph(name = "Department.employers_children", attributeNodes = {
        @NamedAttributeNode("employers"), @NamedAttributeNode("children")})
public class Department {

    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, length = 50)
    private String name;

    @JsonIgnore
    @Column(length = 1000)
    private String description;

    @OneToMany(mappedBy = "department")
    @LazyCollection(LazyCollectionOption.EXTRA)
    private Set<User> employers = new HashSet<>();

    @JsonIgnore
    @ManyToOne
    private Department parent;

    @Column(nullable = false, columnDefinition = "bigint default 0")
    @JsonSerialize(nullsUsing = JsonNullSerializer.class)
    private Long parentId;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Department> children;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private Organization organization;


    public Department() {
    }

    public Department(String name, String description, Department parent) {
        this.name = name;
        this.description = description;
        this.parent = parent;
        if (parent != null) parentId = parent.getId();
    }

    public Department(String name, long id, Long parentId, Set<Department> children, Set<User> employers) {
        this.name = name;
        this.id = id;
        this.children = children;
        this.parentId = parentId;
        this.employers = employers;
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

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public void setParent(Department parent) {
        this.parent = parent;
        for (Department d :
                children) {
            d.setParent(this);
        }
    }

    public void setChildren(Set<Department> children) {
        this.children = children;
    }

    public long getId() {
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

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Department getParent() {
        return parent;
    }

    /*public Department getParent() {
        if (parentId != null) return departmentDAO.getDepartmentById(parentId);
        return parent;
    }*/



    public Set<Department> getChildren() {
        return children;
        /*if (children != null) return children;
        return departmentDAO.getChildren(this);*/
    }

    public void addEmployer(User employer){
        employers.add(employer);
    }

    public void initParentForTree(){
        for (Department d :
                children) {
            d.setParent(this);
            d.initParentForTree();
        }
    }

    public void initOrgForChildren(){
        for (Department d :
                children) {
            d.setOrganization(organization);
            d.initOrgForChildren();
        }
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

package db.converters;

import db.DepartmentDAO;
import entities.Department;

import javax.persistence.AttributeConverter;

public class DepartmentParentConverter implements AttributeConverter<Department, Integer> {

    DepartmentDAO departmentDAO; //TODO надо как-то внедрить через спринг

    public DepartmentParentConverter() {

    }

    @Override
    public Integer convertToDatabaseColumn(Department department) {
        return department.getId();
    }

    @Override
    public Department convertToEntityAttribute(Integer integer) {
        return departmentDAO.getDepartmentById(integer);
    }
}

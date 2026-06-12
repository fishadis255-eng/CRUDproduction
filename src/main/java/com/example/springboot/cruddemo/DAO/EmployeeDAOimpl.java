package com.example.springboot.cruddemo.DAO;

import jakarta.persistence.EntityManager;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.example.springboot.cruddemo.entity.Employee;
import jakarta.persistence.TypedQuery;
@Repository
public class EmployeeDAOimpl implements EmployeeDAO {

    private final EntityManager entityManager;

    @Autowired
    public EmployeeDAOimpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Employee> findAll() {
        TypedQuery<Employee> theQuery =
                entityManager.createQuery("from Employee", Employee.class);
        return theQuery.getResultList();
    }

    @Override
    public Employee findById(int theId) {
        return entityManager.find(Employee.class, theId);
    }

 
   
 // Inside EmployeeDAOImpl
    @Override
    public Employee save(Employee theEmployee) {
        // Save or update the employee
        Employee dbEmployee = entityManager.merge(theEmployee);
        
        // Return the saved employee (which now contains the database-generated ID)
        return dbEmployee;
    }
    @Override
    public void deleteById(int theId) {
        // 1. Find the employee from the database using the Entity Manager
        Employee theEmployee = entityManager.find(Employee.class, theId);
        
        // 2. Remove the employee from the database if they exist
        if (theEmployee != null) {
            entityManager.remove(theEmployee);
        }
    }
}


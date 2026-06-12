package com.example.springboot.cruddemo.DAO;

import com.example.springboot.cruddemo.entity.Employee;
import java.util.List;

public interface EmployeeDAO {
    
    List<Employee> findAll();
    
    Employee findById(int theId);
    
    Employee save(Employee theEmployee);
    
    // MAKE SURE THIS EXACT LINE IS HERE
    void deleteById(int theId);   
}
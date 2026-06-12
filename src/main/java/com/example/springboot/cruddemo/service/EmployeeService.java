package com.example.springboot.cruddemo.service;

import com.example.springboot.cruddemo.DAO.EmployeeDAO;
import com.example.springboot.cruddemo.entity.Employee;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class EmployeeService {

    private final EmployeeDAO empDao;

    public EmployeeService(EmployeeDAO empDao) {
        this.empDao = empDao;
    }

    public List<Employee> getEmps() {
        return empDao.findAll();
    }

    public Employee findById(int id) {
        return empDao.findById(id);
    }

    // MAKE SURE THIS METHOD EXISTS EXACTLY LIKE THIS
    @Transactional
    public Employee save(Employee theEmployee) {
        return empDao.save(theEmployee); // Calls the DAO to save to the database
    }
    @Transactional
    public void deleteById(int theId) {
        empDao.deleteById(theId);
    }
}
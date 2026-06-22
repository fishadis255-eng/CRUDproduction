package com.example.springboot.cruddemo.service;

import com.example.springboot.cruddemo.entity.Employee;
import com.example.springboot.cruddemo.DAO.EmployeeRepository;   // ← Correct repository
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    // Constructor Injection
    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    public Employee findById(Integer id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found - " + id));
    }

    @Transactional
    public Employee save(Employee theEmployee) {
        return employeeRepository.save(theEmployee);
    }

    @Transactional
    public void deleteById(Integer id) {
        employeeRepository.deleteById(id);
    }
}
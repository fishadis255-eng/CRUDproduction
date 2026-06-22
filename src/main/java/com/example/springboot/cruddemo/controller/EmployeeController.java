package com.example.springboot.cruddemo.controller;

import com.example.springboot.cruddemo.entity.Employee;
import com.example.springboot.cruddemo.service.EmployeeService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/web-employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    // Clean Constructor Injection (No @Autowired required for single-constructor classes in Spring 4.3+)
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    // 1. Endpoint to get all employees: GET /api/employees
    @GetMapping("/employees")
    public List<Employee> getEmployees() {
        return employeeService.findAll();
    }

    // 2. Endpoint to get a single employee: GET /api/employees/{employeeId}
    @GetMapping("/employees/{employeeId}")
    public Employee getEmployee(@PathVariable int employeeId) {
        Employee theEmployee = employeeService.findById(employeeId);
        
        if (theEmployee == null) {
            throw new RuntimeException("Employee id not found - " + employeeId);
        }
        
        return theEmployee;
    }

    // 3. Endpoint to add a new employee: POST /api/employees
    @PostMapping("/employees")
    public Employee addEmployee(@RequestBody Employee theEmployee) {
        
        // Force a save operation by resetting incoming ID to 0 (or null if it's an Integer object)
        theEmployee.setId(0);
        
        Employee dbEmployee = employeeService.save(theEmployee);
        
        return dbEmployee;
    }
    @PutMapping("/employees")
    public Employee updateEmployee(@RequestBody Employee theEmployee) {
        
        // Notice we do NOT set the ID to 0 here. 
        // We keep the incoming ID so Hibernate knows WHICH employee to update.
        Employee dbEmployee = employeeService.save(theEmployee);
        
        return dbEmployee;
    }
    @DeleteMapping("/employees/{employeeId}")
    public String deleteEmployee(@PathVariable int employeeId) {

        Employee tempEmployee = employeeService.findById(employeeId);

        // Safety check: throw an exception if the employee is not found
        if (tempEmployee == null) {
            throw new RuntimeException("Employee id not found - " + employeeId);
        }

        employeeService.deleteById(employeeId);

        return "Deleted employee id - " + employeeId;
    }
   }


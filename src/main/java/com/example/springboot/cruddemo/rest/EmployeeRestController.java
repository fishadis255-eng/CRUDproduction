package com.example.springboot.cruddemo.rest;

import com.example.springboot.cruddemo.entity.Employee;
import com.example.springboot.cruddemo.entity.Job;
import com.example.springboot.cruddemo.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class EmployeeRestController {

    private final EmployeeService employeeService;

    @Autowired
    public EmployeeRestController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/employees")
    public List<Employee> findAll() {
        return employeeService.findAll();
    }

    @GetMapping("/employees/{employeeId}")
    public Employee getEmployee(@PathVariable int employeeId) {
        Employee theEmployee = employeeService.findById(employeeId);
        if (theEmployee == null) {
            throw new RuntimeException("Employee id not found - " + employeeId);
        }
        return theEmployee;
    }

    @PostMapping("/employees")
    public Employee addEmployeeWithJobs(@RequestBody Employee theEmployee) {

        if (theEmployee.getJobs() != null) {
            var incomingJobs = theEmployee.getJobs();
            theEmployee.setJobs(null); 

            for (Job tempJob : incomingJobs) {
                theEmployee.add(tempJob);   // Your custom method
            }
        }

        return employeeService.save(theEmployee);
    }

    @PutMapping("/employees")
    public Employee updateEmployee(@RequestBody Employee theEmployee) {
        Employee existing = employeeService.findById(theEmployee.getId());
        if (existing == null) {
            throw new RuntimeException("Employee not found - " + theEmployee.getId());
        }

        existing.setFirstName(theEmployee.getFirstName());
        existing.setLastName(theEmployee.getLastName());
        existing.setEmail(theEmployee.getEmail());

        if (existing.getJobs() != null) {
            existing.getJobs().clear();
        }

        if (theEmployee.getJobs() != null) {
            for (Job tempJob : theEmployee.getJobs()) {
                existing.add(tempJob);
            }
        }

        return employeeService.save(existing);
    }

    @DeleteMapping("/employees/{employeeId}")
    public String deleteEmployee(@PathVariable int employeeId) {
        Employee tempEmployee = employeeService.findById(employeeId);
        if (tempEmployee == null) {
            throw new RuntimeException("Employee id not found - " + employeeId);
        }
        employeeService.deleteById(employeeId);
        return "Deleted employee id - " + employeeId;
    }
}
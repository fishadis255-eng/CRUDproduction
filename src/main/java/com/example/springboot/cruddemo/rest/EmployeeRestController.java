package com.example.springboot.cruddemo.rest;

import com.example.springboot.cruddemo.entity.Employee;
import com.example.springboot.cruddemo.entity.Job;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class EmployeeRestController {

    private EntityManager entityManager;

    @Autowired
    public EmployeeRestController(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @PostMapping("/employees")
    @Transactional
    public Employee addEmployeeWithJobs(@RequestBody Employee theEmployee) {
        
        // CRITICAL STEP: If jobs are provided in the JSON payload, 
        // we must manually loop through them and call our convenience method 'add()'.
        // This links both sides of the relationship in memory before persisting.
        if (theEmployee.getJobs() != null) {
            // Temporary copy of the list sent via JSON
            var incomingJobs = theEmployee.getJobs();
            
            // Clear the unlinked list so we can rebuild it cleanly using our helper method
            theEmployee.setJobs(null); 
            
            for (Job tempJob : incomingJobs) {
                theEmployee.add(tempJob); // This sets tempJob.setEmployee(this) under the hood!
            }
        }

        // This saves the employee, and because CascadeType.PERSIST is on, 
        // it automatically saves all associated jobs to the job table!
        entityManager.persist(theEmployee);

        return theEmployee;
    }
    @PutMapping("/employees")
    @Transactional
    public Employee updateEmployee(@RequestBody Employee theEmployee) {
        // 1. Fetch managed entity
        Employee existingEmployee = entityManager.find(Employee.class, theEmployee.getId());
        
        if (existingEmployee == null) {
            throw new NotFoundException("Employee not found - " + theEmployee.getId());
        }

        // 2. Update basic fields using a mapper or simple setters
        existingEmployee.setFirstName(theEmployee.getFirstName());
        existingEmployee.setLastName(theEmployee.getLastName());
        existingEmployee.setEmail(theEmployee.getEmail());

        // 3. Clear existing relationship safely
        if (existingEmployee.getJobs() != null) {
            existingEmployee.getJobs().clear();
        }

        // 4. Re-link
        if (theEmployee.getJobs() != null) {
            for (Job tempJob : theEmployee.getJobs()) {
                existingEmployee.add(tempJob);
            }
        }
        
        return entityManager.merge(existingEmployee);
    }
    @PutMapping("/employees/{employeeId}")
    @Transactional
    public Employee updateEmployee(@PathVariable int employeeId, @RequestBody Employee theEmployee) {
        
        // 1. Fetch the existing record using the ID from the path
        Employee existingEmployee = entityManager.find(Employee.class, employeeId);
        
        if (existingEmployee == null) {
            throw new RuntimeException("Employee not found with id: " + employeeId);
        }

        // 2. Update fields
        existingEmployee.setFirstName(theEmployee.getFirstName());
        existingEmployee.setLastName(theEmployee.getLastName());
        existingEmployee.setEmail(theEmployee.getEmail());

        // 3. Handle the jobs list as before...
        existingEmployee.getJobs().clear();
        if (theEmployee.getJobs() != null) {
            for (Job tempJob : theEmployee.getJobs()) {
                existingEmployee.add(tempJob);
            }
        }
        
        return entityManager.merge(existingEmployee);
    }
    @GetMapping("/employees")

    public List<Employee> findAll() {

    // Create a JPQL query to select all employees

    return entityManager.createQuery("from Employee", Employee.class)

    .getResultList();

    }
    @GetMapping("/employees/{employeeId}")
    public Employee getEmployee(@PathVariable int employeeId) {
        // Find the employee by ID
        Employee theEmployee = entityManager.find(Employee.class, employeeId);
        
        if (theEmployee == null) {
            throw new RuntimeException("Employee id not found - " + employeeId);
        }
        
        return theEmployee;
    }
    @DeleteMapping("/employees/{employeeId}")
    @Transactional
    public String deleteEmployee(@PathVariable int employeeId) {
        // 1. Fetch the employee
        Employee tempEmployee = entityManager.find(Employee.class, employeeId);
        
        // 2. Check if the employee exists
        if (tempEmployee == null) {
            throw new RuntimeException("Employee id not found - " + employeeId);
        }
        
        // 3. Remove the employee
        entityManager.remove(tempEmployee);
        
        return "Deleted employee id - " + employeeId;
    }
}
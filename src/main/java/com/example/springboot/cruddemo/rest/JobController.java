package com.example.springboot.cruddemo.rest;

import com.example.springboot.cruddemo.entity.Employee;
import com.example.springboot.cruddemo.entity.Job;
import com.example.springboot.cruddemo.service.EmployeeService;
import com.example.springboot.cruddemo.service.JobService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
public class JobController {

    private final JobService jobService;
    private final EmployeeService employeeService;

    public JobController(JobService jobService, EmployeeService employeeService) {
        this.jobService = jobService;
        this.employeeService = employeeService;
    }

    @GetMapping
    public List<Job> getAllJobs() {
        return jobService.findAll();
    }

    @GetMapping("/{id}")
    public Job getJob(@PathVariable Integer id) {
        return jobService.findById(id);
    }

    // ================== UPDATED PUT METHOD (Supports emp_id) ==================
    @PutMapping("/{id}")
    public ResponseEntity<Job> updateJob(@PathVariable Integer id, 
                                         @RequestBody Job jobDetails) {
        
        Job job = jobService.findById(id);

        // Update basic fields
        job.setTitle(jobDetails.getTitle());
        job.setSalary(jobDetails.getSalary());
        job.setType(jobDetails.getType());
        job.setMode(jobDetails.getMode());

        // Handle employee association - Supports "emp_id" in JSON
        Integer empId = getEmpIdFromJob(jobDetails);
        if (empId != null) {
            Employee employee = employeeService.findById(empId);
            job.setEmployee(employee);
        } 
        else if (jobDetails.getEmployee() != null && jobDetails.getEmployee().getId() != null) {
            // Fallback: if full employee object is sent
            Employee employee = employeeService.findById(jobDetails.getEmployee().getId());
            job.setEmployee(employee);
        }

        Job updatedJob = jobService.save(job);
        return ResponseEntity.ok(updatedJob);
    }
    // =========================================================================

    @PostMapping
    public Job createJob(@RequestBody Job job) {
        Integer empId = getEmpIdFromJob(job);
        if (empId != null) {
            Employee emp = employeeService.findById(empId);
            job.setEmployee(emp);
        } 
        else if (job.getEmployee() != null && job.getEmployee().getId() != null) {
            Employee emp = employeeService.findById(job.getEmployee().getId());
            job.setEmployee(emp);
        }
        return jobService.save(job);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJob(@PathVariable Integer id) {
        jobService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // ================== Helper Method for emp_id ==================
    private Integer getEmpIdFromJob(Job job) {
        try {
            // This calls the setEmpId / getEmpId method we added to Job entity
            java.lang.reflect.Method method = job.getClass().getMethod("getEmpId");
            return (Integer) method.invoke(job);
        } catch (Exception e) {
            return null;
        }
    }
}
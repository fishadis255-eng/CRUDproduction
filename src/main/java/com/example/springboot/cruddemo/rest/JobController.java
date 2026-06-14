package com.example.springboot.cruddemo.rest;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

import com.example.springboot.cruddemo.entity.Employee;
import com.example.springboot.cruddemo.entity.Job;
import com.example.springboot.cruddemo.DAO.EmployeeRepository;
import com.example.springboot.cruddemo.DAO.JobRepository; 

@RestController
@RequestMapping("/api/jobs")
public class JobController {

    
    private final JobRepository jobRepository;
    
    private final EmployeeRepository employeeRepository;
    // GET: Retrieve all jobs
    @Autowired
    public JobController(JobRepository jobRepository, EmployeeRepository employeeRepository) {
        this.jobRepository = jobRepository;
        this.employeeRepository = employeeRepository;
    }
    @GetMapping
    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }
    @GetMapping("/{id}")
    public Job getJob(@PathVariable Integer id) {
        return jobRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Job not found with id: " + id));
    }
    // PUT: Update an existing job
 // PUT: Update an existing job
    @PutMapping(value = "/{id}", consumes = "application/json") // Change this line
    public Job updateJob(@PathVariable Integer id, @RequestBody Job jobDetails) {
        return jobRepository.findById(id).map(job -> {
            job.setTitle(jobDetails.getTitle());
            job.setType(jobDetails.getType());
            job.setMode(jobDetails.getMode());

            if (jobDetails.getEmployee() != null && jobDetails.getEmployee().getId() != null) {
                Employee emp = employeeRepository.findById(jobDetails.getEmployee().getId())
                                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + jobDetails.getEmployee().getId()));
                job.setEmployee(emp);
            }
            
            return jobRepository.save(job);
        }).orElseThrow(() -> new RuntimeException("Job not found with id: " + id));
    }}
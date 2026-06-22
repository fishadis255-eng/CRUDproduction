package com.example.springboot.cruddemo.service;

import com.example.springboot.cruddemo.entity.Job;
import com.example.springboot.cruddemo.DAO.JobRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class JobService {

    private final JobRepository jobRepository;

    public JobService(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    public List<Job> findAll() {
        return jobRepository.findAll();
    }

    public Job findById(Integer id) {
        return jobRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job not found - " + id));
    }

    @Transactional
    public Job save(Job theJob) {
        return jobRepository.save(theJob);
    }

    @Transactional
    public void deleteById(Integer id) {
        jobRepository.deleteById(id);
    }
}
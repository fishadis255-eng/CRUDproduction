package com.example.springboot.cruddemo.DAO;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.springboot.cruddemo.entity.Job;

public interface JobRepository extends JpaRepository<Job, Integer> {
    // JpaRepository provides all standard CRUD methods (save, findAll, findById, etc.) 
    // automatically, so you don't need to write any SQL!
}
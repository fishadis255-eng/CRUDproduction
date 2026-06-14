package com.example.springboot.cruddemo.DAO;

import com.example.springboot.cruddemo.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    // JpaRepository provides findAll(), findById(), save(), etc.
}
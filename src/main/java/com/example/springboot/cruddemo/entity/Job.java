package com.example.springboot.cruddemo.entity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;

@Entity
@Table(name="job")
public class Job {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;

    @Column(name="title")
    private String title;

    @Column(name="salary")
    private int salary;

    @Column(name="type")
    private String type;

    @Column(name="mode")
    private String mode;
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, 
            CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name="emp_id")
    @JsonIgnore
    @JsonBackReference
    private Employee employee;
    public Job() {
    }
    public Job(String title, int salary, String type, String mode) {
        this.title = title;
        this.salary = salary;
        this.type = type;
        this.mode = mode;
    }
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
    @Override
    public String toString() {
        return "Job{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", salary=" + salary +
                ", type='" + type + '\'' +
                ", mode='" + mode + '\'' +
                '}';
    }
}

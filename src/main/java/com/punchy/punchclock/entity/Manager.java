package com.punchy.punchclock.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name="manager")
public class Manager extends Person {

    @OneToMany(mappedBy = "manager")
    @JsonManagedReference
    private List<Employee> employeeList;

    @ManyToOne
    @JsonBackReference
    private Admin admin;

    public List<Employee> getEmployeeList() {
        return employeeList;
    }

    public void setEmployeeList(List<Employee> employeeList) {
        this.employeeList = employeeList;
    }

}

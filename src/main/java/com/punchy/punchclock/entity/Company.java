package com.punchy.punchclock.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Boolean isPaying;

    private Integer maxNumberOfEmployees;

    @OneToMany(mappedBy = "company")
    @JsonManagedReference
    private List<Admin> adminList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getPaying() {
        return isPaying;
    }

    public void setPaying(Boolean paying) {
        isPaying = paying;
    }

    public Integer getMaxNumberOfEmployees() {
        return maxNumberOfEmployees;
    }

    public void setMaxNumberOfEmployees(Integer maxNumberOfEmployees) {
        this.maxNumberOfEmployees = maxNumberOfEmployees;
    }

    public List<Admin> getAdminList() {
        return adminList;
    }

    public void setAdminList(List<Admin> adminList) {
        this.adminList = adminList;
    }
}

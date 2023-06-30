package com.punchy.punchclock.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name="employee")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="manager_id", nullable=false)
    private Manager manager;

    @OneToMany(mappedBy = "employee")
    private List<Punch> punchList;
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Manager getManager() {
        return manager;
    }

    public void setManager(Manager manager) {
        this.manager = manager;
    }

    public List<Punch> getPunchList() {
        return punchList;
    }

    public void setPunchList(List<Punch> punchList) {
        this.punchList = punchList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

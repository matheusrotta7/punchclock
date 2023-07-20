package com.punchy.punchclock.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name="employee")
public class Employee extends Person {

    @ManyToOne
    @JoinColumn(name="manager_id", nullable=false)
    @JsonBackReference
    private Manager manager;

    @OneToMany(mappedBy = "employee")
    @JsonManagedReference
    private List<Punch> punchList;

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

}

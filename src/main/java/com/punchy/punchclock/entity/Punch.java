package com.punchy.punchclock.entity;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name="punch")
public class Punch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="employee_id", nullable=false)
    private Employee employee;

    private Date timestamp;
}

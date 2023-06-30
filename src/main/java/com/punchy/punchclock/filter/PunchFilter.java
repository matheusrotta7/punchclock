package com.punchy.punchclock.filter;

import java.time.Month;

public class PunchFilter {

    private Long employeeId;

    private Integer month; //1-12

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }
}

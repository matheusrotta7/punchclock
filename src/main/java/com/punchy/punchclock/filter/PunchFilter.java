package com.punchy.punchclock.filter;

import java.time.Month;

public class PunchFilter {

    private Long employeeId;

    private Integer month; //1-12

    private Integer year;

    private String punchStatus;

    public String getPunchStatus() {
        return punchStatus;
    }

    public void setPunchStatus(String punchStatus) {
        this.punchStatus = punchStatus;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

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

package com.punchy.punchclock.filter;

public class PunchFilter {

    private Long employeeId;

    private Integer month;

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

}

package com.mindex.challenge.model;

import com.mindex.challenge.data.Employee;

public class ReportingStructureModel {

    private final Employee employee;
    private final int numberOfReports;

    public ReportingStructureModel(Employee employee, int numberOfReports) {
        this.employee = employee;
        this.numberOfReports = numberOfReports;
    }

    public int getNumberOfReports() {
        return numberOfReports;
    }

    public Employee getEmployee() {
        return employee;
    }
}

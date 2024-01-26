package com.mindex.challenge.service;

import com.mindex.challenge.data.Employee;
import com.mindex.challenge.model.ReportingStructureModel;

import java.util.Optional;

public interface EmployeeService {
    Optional<Employee> create(Employee employee);
    Optional<Employee> read(String id);
    Optional<Employee> update(Employee employee);
    Optional<ReportingStructureModel> buildReportingStructure(String id);

}

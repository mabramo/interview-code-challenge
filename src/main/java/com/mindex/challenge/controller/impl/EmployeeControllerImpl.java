package com.mindex.challenge.controller.impl;

import com.mindex.challenge.controller.EmployeeController;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.model.ReportingStructureModel;
import com.mindex.challenge.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class EmployeeControllerImpl implements EmployeeController {
    private static final Logger LOG = LoggerFactory.getLogger(EmployeeControllerImpl.class);

    private final EmployeeService employeeService;

    @Autowired
    public EmployeeControllerImpl(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @Override
    public ResponseEntity<Employee> create(Employee employee) {
        LOG.debug("Received employee create request for [{}]", employee);

        return new ResponseEntity<>(employeeService.create(employee).get(), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Employee> read(String id) {
        LOG.debug("Received employee read request for id [{}]", id);

        return new ResponseEntity<>(employeeService.read(id).get(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Employee> update(String id, Employee employee) {
        LOG.debug("Received employee create request for id [{}] and employee [{}]", id, employee);

        employee.setEmployeeId(id);
        return new ResponseEntity<>(employeeService.update(employee).get(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ReportingStructureModel> readReportingStructure(String id) {
        LOG.debug("Received employee reporting structure read requests for id [{}]", id);

        Optional<ReportingStructureModel> opt = employeeService.readReportingStructure(id);

        return opt.map(reportingStructureModel -> new ResponseEntity<>(reportingStructureModel, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));

    }

    @Override
    public ResponseEntity<Compensation> createCompensation(String id, Compensation compensation) {
        LOG.debug("Received compensation create request for id [{}]", id);

        Optional<Compensation> opt = employeeService.createCompensation(id, compensation);

        return opt.map(comp -> new ResponseEntity<>(comp, HttpStatus.CREATED))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.FORBIDDEN));
    }

    @Override
    public ResponseEntity<Compensation> readCompensation(String id) {
        LOG.debug("Received compensation read request for id [{}]", id);

        Optional<Compensation> opt = employeeService.readCompensation(id);

        return opt.map(comp -> new ResponseEntity<>(comp, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}

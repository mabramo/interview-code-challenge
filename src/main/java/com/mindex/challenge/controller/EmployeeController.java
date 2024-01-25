package com.mindex.challenge.controller;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.model.ReportingStructureModel;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/employee")
public interface EmployeeController {

    @PostMapping
    ResponseEntity<Employee> create(@RequestBody Employee e);

    @GetMapping("/{id}")
    ResponseEntity<Employee> read(@PathVariable String id);

    @PutMapping("/{id}")
    ResponseEntity<Employee> update(@PathVariable String id, @RequestBody Employee e);

    @GetMapping("/{id}/reporting")
    ResponseEntity<ReportingStructureModel> readReportingStructure(@PathVariable String id);

    @PostMapping("/{id}/compensation")
    ResponseEntity<Compensation> createCompensation(@PathVariable String id, @RequestBody Compensation compensation);

    @GetMapping("/{id}/compensation")
    ResponseEntity<Compensation> readCompensation(@PathVariable String id);
}

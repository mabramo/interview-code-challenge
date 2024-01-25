package com.mindex.challenge.controller;

import com.mindex.challenge.data.Employee;
import com.mindex.challenge.model.ReportingStructureModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/employee")
public interface EmployeeController {

    @PostMapping("/create")
    ResponseEntity<Employee> create(@RequestBody Employee e);

    @GetMapping("/read/{id}")
    ResponseEntity<Employee> read(@PathVariable String id);

    @PutMapping("/update")
    ResponseEntity<Employee> update(@PathVariable String id, @RequestBody Employee e);

    @GetMapping("/read/{id}/reporting")
    ResponseEntity<ReportingStructureModel> readReportingStructure(@PathVariable String id);
}

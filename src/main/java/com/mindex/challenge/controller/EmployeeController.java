package com.mindex.challenge.controller;

import com.mindex.challenge.data.Employee;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/employee")
public interface EmployeeController {

    @PostMapping("/create")
    Employee create(@RequestBody Employee e);

    @GetMapping("/read/{id}")
    Employee read(@PathVariable String id);

    @PutMapping("/update")
    Employee update(@PathVariable String id, @RequestBody Employee e);
}

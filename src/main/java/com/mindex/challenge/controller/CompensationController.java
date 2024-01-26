package com.mindex.challenge.controller;

import com.mindex.challenge.data.Compensation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/compensation")
public interface CompensationController {

    /**
     * Create a compensation entry in the compensation repository for an already existing employee
     *
     * @param compensation a compensation representation
     * @return http CREATED and the compensation object if successful, http BAD_REQUEST if employee id is not found
     */
    @PostMapping
    ResponseEntity<Compensation> createCompensation(@RequestBody Compensation compensation);

    /**
     * Retrieve compensation data
     *
     * @param id an employee id
     * @return the compensation object if it exists, http NOT_FOUND if it does not exist
     */
    @GetMapping("/{id}")
    ResponseEntity<Compensation> readCompensation(@PathVariable String id);

}

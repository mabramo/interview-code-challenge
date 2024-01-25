package com.mindex.challenge.controller.impl;

import com.mindex.challenge.controller.CompensationController;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.service.CompensationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CompensationControllerImpl implements CompensationController {

    private static final Logger LOG = LoggerFactory.getLogger(CompensationControllerImpl.class);

    private final CompensationService compensationService;

    public CompensationControllerImpl(CompensationService compensationService) {
        this.compensationService = compensationService;
    }

    @Override
    public ResponseEntity<Compensation> createCompensation(Compensation compensation) {
        LOG.debug("Received compensation create request for id [{}]", compensation.getEmployeeId());

        Optional<Compensation> opt = compensationService.createCompensation(compensation);

        return opt.map(comp -> new ResponseEntity<>(comp, HttpStatus.CREATED))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.FORBIDDEN));
    }

    @Override
    public ResponseEntity<Compensation> readCompensation(String id) {
        LOG.debug("Received compensation read request for id [{}]", id);

        Optional<Compensation> opt = compensationService.readCompensation(id);

        return opt.map(comp -> new ResponseEntity<>(comp, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

}

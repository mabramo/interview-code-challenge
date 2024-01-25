package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.CompensationRepository;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.service.CompensationService;
import com.mindex.challenge.service.EmployeeService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CompensationServiceImpl implements CompensationService {

    private static final Logger LOG = LoggerFactory.getLogger(CompensationServiceImpl.class);

    private final EmployeeService employeeService;

    private final CompensationRepository compensationRepository;

    @Autowired
    public CompensationServiceImpl(EmployeeService employeeService, CompensationRepository compensationRepository) {
        this.employeeService = employeeService;
        this.compensationRepository = compensationRepository;
    }

    @Override
    public Optional<Compensation> createCompensation(Compensation compensation) {
        LOG.debug("Creating compensation for employee with id [{}]", compensation.getEmployeeId());

        Optional<Employee> optEmployee = employeeService.read(compensation.getEmployeeId());

        if (!optEmployee.isPresent()) {
            LOG.info("Employee with id [{}] was not found. Compensation will not be created.", compensation.getEmployeeId());
            return Optional.empty();
        } else {
            LOG.debug("Found employee with id [{}] while creating compensation", compensation.getEmployeeId());
            try {
                compensation = compensationRepository.insert(compensation);
            } catch (Exception e) {
                LOG.error("Found duplicate key [{}] in compensation repository", compensation.getEmployeeId());
                return Optional.empty();
            }

            return Optional.of(compensation);
        }
    }

    @Override
    public Optional<Compensation> readCompensation(String id) {
        LOG.debug("Reading compensation for employee with id [{}]", id);

        Optional<Compensation> compensation = compensationRepository.findByEmployeeId(id);

        if (!compensation.isPresent()) {
            LOG.info("Compensation information for employee with id [{}] not found", id);
            return Optional.empty();
        } else {
            LOG.debug("Found compensation for employee with id [{}]", id);
            return compensation;
        }
    }

}

package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.CompensationRepository;
import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.model.ReportingStructureModel;
import com.mindex.challenge.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private static final Logger LOG = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    private final EmployeeRepository employeeRepository;
    private final CompensationRepository compensationRepository;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository, CompensationRepository compensationRepository) {
        this.employeeRepository = employeeRepository;
        this.compensationRepository = compensationRepository;

    }

    @Override
    public Optional<Employee> create(Employee employee) {
        LOG.debug("Creating employee [{}]", employee);

        employee.setEmployeeId(UUID.randomUUID().toString());
        employeeRepository.insert(employee);

        return Optional.of(employee);
    }

    @Override
    public Optional<Employee> read(String id) {
        LOG.debug("Reading employee with id [{}]", id);

        employeeRepository.findAll().forEach(o -> System.out.println(o.getEmployeeId()));

        Employee employee = employeeRepository.findByEmployeeId(id);

        if (employee == null) {
            LOG.info("Invalid employeeId: [{}]", id);
            return Optional.empty();
        }

        return Optional.of(employee);
    }

    @Override
    public Optional<Employee> update(Employee employee) {
        LOG.debug("Updating employee [{}]", employee);

        return Optional.of(employeeRepository.save(employee));
    }

    @Override
    public Optional<ReportingStructureModel> readReportingStructure(String id) {
        LOG.debug("Reading report structure for employee with id [{}]", id);

        Optional<Employee> optionalEmployee = read(id);

        if (!optionalEmployee.isPresent()) {
            LOG.debug("Employee with id [{}] not found", id);
            return Optional.empty();
        } else {
            return Optional.of(new ReportingStructureModel(optionalEmployee.get(), countReportees(optionalEmployee.get())));
        }
    }

    @Override
    public Optional<Compensation> createCompensation(String id, Compensation compensation) {
        LOG.debug("Creating compensation for employee with id [{}]", id);

        Employee employee = employeeRepository.findByEmployeeId(id);

        if (employee == null) {
            LOG.info("Employee with id [{}] was not found. Compensation will not be created.", id);
            return Optional.empty();
        } else {
            LOG.debug("Found employee with id [{}] while creating compensation", id);
            compensation = compensationRepository.insert(compensation);
            return Optional.of(compensation);
        }
    }

    @Override
    public Optional<Compensation> readCompensation(String id) {
        LOG.debug("Reading compensation for employee with id [{}]", id);

        Compensation compensation = compensationRepository.findByEmployeeId(id);

        if(compensation == null){
            LOG.info("Compensation information for employee with id [{}] not found", id);
            return Optional.empty();
        } else {
            LOG.debug("Found compensation for employee with id [{}]", id);
            return Optional.of(compensation);
        }
    }

    /**
     * Performs a breadth first traversal against an Employee and its direct reportees and their
     * direct reportees until no more direct reportees are found
     *
     * @param employee An Employee that contains a list of direct reportees
     * @return An integer indicating the number of reportees that are under the
     * original employee's chain of command
     */
    private int countReportees(Employee employee) {
        LOG.debug("Counting reportees for employee with id [{}]", employee.getEmployeeId());

        Queue<Employee> queue = new LinkedList<>();
        HashSet<String> visited = new HashSet<>();
        Employee curr;

        queue.add(employee);

        while (!queue.isEmpty()) {
            curr = queue.poll();
            visited.add(curr.getEmployeeId());

            if (curr.getDirectReports() != null) {
                queue.addAll(curr.getDirectReports().stream()
                        .filter(e -> !visited.contains(e.getEmployeeId()))
                        .collect(Collectors.toList()));
            }

        }

        return visited.size() - 1;
    }
}

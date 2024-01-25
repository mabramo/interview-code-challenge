package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.CompensationRepository;
import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.model.ReportingStructureModel;
import com.mindex.challenge.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;
import java.util.UUID;
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

        Optional<Employee> employee = employeeRepository.findByEmployeeId(id);

        if (!employee.isPresent()) {
            LOG.info("Invalid employeeId: [{}]", id);
            return Optional.empty();
        } else {
            return employee;
        }
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

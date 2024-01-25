package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.CompensationRepository;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.service.CompensationService;
import com.mindex.challenge.service.EmployeeService;

import de.bwaldvogel.mongo.exception.DuplicateKeyError;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;


@RunWith(SpringRunner.class)
public class CompensationServiceImplTest {

    private CompensationService compensationService;
    private EmployeeService employeeService;
    private CompensationRepository compensationRepository;

    @Before
    public void setup() {

        employeeService = Mockito.mock(EmployeeService.class);
        compensationRepository = Mockito.mock(CompensationRepository.class);

        compensationService = new CompensationServiceImpl(employeeService, compensationRepository);
    }

    @Test
    public void readCompensationTest() {
        String testId = "id-a";

        Compensation testComp = new Compensation();
        testComp.setEmployeeId(testId);
        Mockito.when(compensationRepository.findByEmployeeId(testId)).thenReturn(Optional.of(testComp));

        Optional<Compensation> result = compensationService.readCompensation(testId);

        assertTrue(result.isPresent());
        assertEquals(testId, result.get().getEmployeeId());
    }

    @Test
    public void readCompensationTestNotPresent() {
        String testId = "id-a";
        Mockito.when(compensationRepository.findByEmployeeId(anyString())).thenReturn(Optional.empty());

        Optional<Compensation> result = compensationService.readCompensation(testId);

        assertFalse(result.isPresent());
    }

    @Test
    public void createCompensationTest() {
        String testId = "id-a";
        Employee testEmployee = new Employee();
        testEmployee.setEmployeeId(testId);

        Mockito.when(employeeService.read(testId)).thenReturn(Optional.of(testEmployee));

        Compensation testComp = new Compensation();
        testComp.setEmployeeId(testId);

        Mockito.when(compensationRepository.insert(Mockito.any(Compensation.class))).thenReturn(testComp);

        Optional<Compensation> result = compensationService.createCompensation(testComp);

        assertTrue(result.isPresent());
        assertEquals(testId, result.get().getEmployeeId());

    }

    @Test
    public void createCompensationDuplicateTest(){

        String testId = "id-a";
        Employee testEmployee = new Employee();
        testEmployee.setEmployeeId(testId);

        Mockito.when(employeeService.read(testId)).thenReturn(Optional.of(testEmployee));

        Compensation testComp = new Compensation();
        testComp.setEmployeeId(testId);

        Mockito.when(compensationRepository.insert(Mockito.any(Compensation.class))).thenReturn(testComp);

        Optional<Compensation> result = compensationService.createCompensation(testComp);

        assertTrue(result.isPresent());
        assertEquals(testId, result.get().getEmployeeId());

        Mockito.when(compensationRepository.insert(Mockito.any(Compensation.class))).thenThrow(DuplicateKeyError.class);

        result = compensationService.createCompensation(testComp);

        assertFalse(result.isPresent());

    }

    @Test
    public void createCompensationTestNoEmployee() {
        String testId = "id-a";

        Mockito.when(employeeService.read(testId)).thenReturn(Optional.empty());

        Compensation testComp = new Compensation();
        testComp.setEmployeeId(testId);

        Optional<Compensation> result = compensationService.createCompensation(testComp);

        assertFalse(result.isPresent());

    }

}

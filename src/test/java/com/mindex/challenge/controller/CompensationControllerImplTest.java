package com.mindex.challenge.controller;

import com.mindex.challenge.controller.impl.CompensationControllerImpl;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.service.CompensationService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
public class CompensationControllerImplTest {
    private CompensationService compensationService;
    private CompensationController compensationController;

    @Before
    public void setup(){
        compensationService = Mockito.mock(CompensationService.class);
        compensationController = new CompensationControllerImpl(compensationService);
    }

    @Test
    public void createCompensationTest(){
        String testId = "id-a";
        Compensation testComp = new Compensation();
        testComp.setEmployeeId(testId);
        testComp.setSalary(753);
        testComp.setEffectiveDate(Date.from(Instant.now()));

        Mockito.when(compensationService.createCompensation(testComp)).thenReturn(Optional.of(testComp));

        ResponseEntity<Compensation> result = compensationController.createCompensation(testComp);
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(testId, result.getBody().getEmployeeId());
    }

    @Test
    public void createCompensationFailedTest(){
        String testId = "id-a";
        Compensation testComp = new Compensation();
        testComp.setEmployeeId(testId);
        testComp.setSalary(753);
        testComp.setEffectiveDate(Date.from(Instant.now()));

        Mockito.when(compensationService.createCompensation(testComp)).thenReturn(Optional.empty());

        ResponseEntity<Compensation> result = compensationController.createCompensation(testComp);
        assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());
    }

    @Test
    public void readCompensationTest(){
        String testId = "id-a";
        Compensation testComp = new Compensation();
        testComp.setEmployeeId(testId);
        testComp.setSalary(753);
        testComp.setEffectiveDate(Date.from(Instant.now()));

        Mockito.when(compensationService.readCompensation(testId)).thenReturn(Optional.of(testComp));

        ResponseEntity<Compensation> result = compensationController.readCompensation(testId);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(testId, result.getBody().getEmployeeId());
    }

    @Test
    public void readCompensationFailedTest(){
        String testId = "id-a";
        Compensation testComp = new Compensation();
        testComp.setEmployeeId(testId);
        testComp.setSalary(753);
        testComp.setEffectiveDate(Date.from(Instant.now()));

        Mockito.when(compensationService.readCompensation(testId)).thenReturn(Optional.empty());

        ResponseEntity<Compensation> result = compensationController.readCompensation(testId);

        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }


}

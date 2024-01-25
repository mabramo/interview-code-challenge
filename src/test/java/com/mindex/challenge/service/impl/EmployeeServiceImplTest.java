package com.mindex.challenge.service.impl;

import com.mindex.challenge.data.Employee;
import com.mindex.challenge.model.ReportingStructureModel;
import com.mindex.challenge.service.EmployeeService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmployeeServiceImplTest {

    private String employeeUrl;
    private String employeeIdUrl;

    private List<Employee> employeeTestData;

    @Autowired
    private EmployeeService employeeService;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setup() {
        employeeUrl = "http://localhost:" + port + "/employee";
        employeeIdUrl = "http://localhost:" + port + "/employee/{id}";
        employeeTestData = new ArrayList<>();
        setUpEmployeeData();
    }

    @Test
    public void testCreateReadUpdate() {
        Employee testEmployee = new Employee();
        testEmployee.setFirstName("John");
        testEmployee.setLastName("Doe");
        testEmployee.setDepartment("Engineering");
        testEmployee.setPosition("Developer");

        // Create checks
        Employee createdEmployee = restTemplate.postForEntity(employeeUrl, testEmployee, Employee.class).getBody();

        assertNotNull(createdEmployee.getEmployeeId());
        assertEmployeeEquivalence(testEmployee, createdEmployee);


        // Read checks
        Employee readEmployee = restTemplate.getForEntity(employeeIdUrl, Employee.class, createdEmployee.getEmployeeId()).getBody();
        assertEquals(createdEmployee.getEmployeeId(), readEmployee.getEmployeeId());
        assertEmployeeEquivalence(createdEmployee, readEmployee);


        // Update checks
        readEmployee.setPosition("Development Manager");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Employee updatedEmployee =
                restTemplate.exchange(employeeIdUrl,
                        HttpMethod.PUT,
                        new HttpEntity<Employee>(readEmployee, headers),
                        Employee.class,
                        readEmployee.getEmployeeId()).getBody();

        assertEmployeeEquivalence(readEmployee, updatedEmployee);
    }

    @Test
    public void readReportingStructureSomeReportees() {
        Optional<Employee> optEmployee = employeeTestData.stream().filter(e -> e.getPosition().equals("Manager")).findFirst();

        assertTrue(optEmployee.isPresent());

        ReportingStructureModel report = restTemplate.getForEntity(employeeIdUrl + "/reporting", ReportingStructureModel.class, optEmployee.get().getEmployeeId()).getBody();

        assertNotNull(report);
        assertNotNull(report.getEmployee());
        assertEquals(optEmployee.get().getEmployeeId(), report.getEmployee().getEmployeeId());
        assertEquals(3, report.getNumberOfReports());
    }

    @Test
    public void readReportingStructureNoReportees() {
        Optional<Employee> optEmployee = employeeTestData.stream().filter(e -> e.getPosition().equals("Junior Developer")).findFirst();

        assertTrue(optEmployee.isPresent());

        ReportingStructureModel report = restTemplate.getForEntity(employeeIdUrl + "/reporting", ReportingStructureModel.class, optEmployee.get().getEmployeeId()).getBody();

        assertNotNull(report);
        assertNotNull(report.getEmployee());
        assertEquals(optEmployee.get().getEmployeeId(), report.getEmployee().getEmployeeId());
        assertEquals(0, report.getNumberOfReports());
    }

    @Test
    public void readReportingStructureNotFound() {

        ReportingStructureModel report = restTemplate.getForEntity(employeeIdUrl + "/reporting",
                ReportingStructureModel.class,
                "invalid-id")
                .getBody();

        assertNull(report);
    }

    private static void assertEmployeeEquivalence(Employee expected, Employee actual) {
        assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getLastName(), actual.getLastName());
        assertEquals(expected.getDepartment(), actual.getDepartment());
        assertEquals(expected.getPosition(), actual.getPosition());
    }

    private void setUpEmployeeData() {

        //junior level - reports to senior
        Employee testEmployee = new Employee();
        testEmployee.setFirstName("Timmy");
        testEmployee.setLastName("Junior");
        testEmployee.setDepartment("Engineering");
        testEmployee.setPosition("Junior Developer");

        //mid level - reports to manager
        Employee testEmployee2 = new Employee();
        testEmployee2.setFirstName("Johnny");
        testEmployee2.setLastName("Dev");
        testEmployee2.setDepartment("Engineering");
        testEmployee2.setPosition("Developer");

        //senior level - manages a junior - reports to manager
        Employee testEmployee3 = new Employee();
        testEmployee3.setFirstName("Robert");
        testEmployee3.setLastName("Senior");
        testEmployee3.setDepartment("Engineering");
        testEmployee3.setPosition("Senior Developer");

        //highest level employee
        Employee testEmployee4 = new Employee();
        testEmployee4.setFirstName("Gary");
        testEmployee4.setLastName("Boss");
        testEmployee4.setDepartment("Engineering");
        testEmployee4.setPosition("Manager");

        Employee created1 = restTemplate.postForEntity(employeeUrl, testEmployee, Employee.class).getBody();
        Employee created2 = restTemplate.postForEntity(employeeUrl, testEmployee2, Employee.class).getBody();
        Employee created3 = restTemplate.postForEntity(employeeUrl, testEmployee3, Employee.class).getBody();
        Employee created4 = restTemplate.postForEntity(employeeUrl, testEmployee4, Employee.class).getBody();

        assert created4 != null;
        created4.setDirectReports(Arrays.asList(created3, created2));
        assert created3 != null;
        created3.setDirectReports(Arrays.asList(created1));

        employeeTestData.add(created1);
        employeeTestData.add(created2);
        employeeTestData.add(created3);
        employeeTestData.add(created4);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        restTemplate.exchange(employeeIdUrl,
                HttpMethod.PUT,
                new HttpEntity<Employee>(created4, headers),
                Employee.class,
                created4.getEmployeeId()).getBody();


        restTemplate.exchange(employeeIdUrl,
                HttpMethod.PUT,
                new HttpEntity<Employee>(created3, headers),
                Employee.class,
                created3.getEmployeeId()).getBody();


    }


}

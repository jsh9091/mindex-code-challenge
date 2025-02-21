package com.mindex.challenge.service.impl;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import de.bwaldvogel.mongo.backend.Assert;

/**
 * Tests for ReportingStructureServiceImpl. 
 * @author jhorvath
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReportingStructureServiceImplTest {
	
    private String employeeUrl;
    private String numberOfReportsIdUrl;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setup() {
        employeeUrl = "http://localhost:" + port + "/employee";        
        numberOfReportsIdUrl = "http://localhost:" + port + "/totalreports/{id}";
    }
    
    @Test
    public void testTotalReportsReporting() {
    	// build test data and set ID for employee to get reports count for
    	final String rootEmployeeId = buildTestEmplyees();
    	// run feature we are here to test
    	ReportingStructure reportingStructure = restTemplate.getForEntity(numberOfReportsIdUrl, ReportingStructure.class, rootEmployeeId).getBody();
        // check count
    	Assert.equals(2, reportingStructure.getNumberOfReports());
    }
    
    /**
     * Creates our employee test data. 
     * @return String 
     */
    private String buildTestEmplyees() {

    	Employee testScott = new Employee();
        testScott.setFirstName("Montgomery");
        testScott.setLastName("Scott");
        testScott.setDepartment("Engineering");
        testScott.setPosition("Chief Engineer");
        
        Employee createdScott = restTemplate.postForEntity(employeeUrl, testScott, Employee.class).getBody();
        assertNotNull(createdScott.getEmployeeId());

    	Employee testSpock = new Employee();
        testSpock.setFirstName("Spock");
        testSpock.setLastName("YouCouldntPronounceIt");
        testSpock.setDepartment("Science");
        testSpock.setPosition("First Officer");
        // add Montgomery as a direct report of Spock
        List<Employee> spocksReports = new ArrayList<>();
        spocksReports.add(createdScott);
        testSpock.setDirectReports(spocksReports);
        
        Employee createdSpock = restTemplate.postForEntity(employeeUrl, testSpock, Employee.class).getBody();
        assertNotNull(createdSpock.getEmployeeId());
        assertNotNull(createdSpock.getDirectReports());
        Assert.hasSize(createdSpock.getDirectReports(), 1);
        Assert.equals(createdSpock.getDirectReports().get(0).getEmployeeId(), createdScott.getEmployeeId());
        
    	Employee testKirk = new Employee();
        testKirk.setFirstName("James");
        testKirk.setLastName("Kirk");
        testKirk.setDepartment("Command");
        testKirk.setPosition("Big Chair");
        // add Spock as a direct report of James
        List<Employee> kirksReports = new ArrayList<>();
        kirksReports.add(createdSpock);
        testKirk.setDirectReports(kirksReports);
        
        Employee createdKirk = restTemplate.postForEntity(employeeUrl, testKirk, Employee.class).getBody();
        assertNotNull(createdKirk.getEmployeeId());
        assertNotNull(createdKirk.getDirectReports());
        Assert.hasSize(createdKirk.getDirectReports(), 1);
        Assert.equals(createdKirk.getDirectReports().get(0).getEmployeeId(), createdSpock.getEmployeeId());
        
        return createdKirk.getEmployeeId();
    }
    
    
    @Test
    public void numberOfReports_invalidID_exception() {
    	// attempt to get reporting structure with a bad ID
    	ReportingStructure reportingStructure = restTemplate.getForEntity(numberOfReportsIdUrl, ReportingStructure.class, "123ABC").getBody();
    	
    	Assert.isNull(reportingStructure.getEmployee());
    	Assert.equals(0, reportingStructure.getNumberOfReports());
    }
}

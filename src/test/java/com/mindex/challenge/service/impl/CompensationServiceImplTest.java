package com.mindex.challenge.service.impl;

import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import de.bwaldvogel.mongo.backend.Assert;

/**
 * Tests for CompensationServiceImpl. 
 * @author jhorvath
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CompensationServiceImplTest {
	
    private String employeeUrl;
    private String compensationUrl;
    private String compensationIdUrl;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setup() {
        employeeUrl = "http://localhost:" + port + "/employee";        
        compensationUrl = "http://localhost:" + port + "/compensation";        
        compensationIdUrl = "http://localhost:" + port + "/compensation/{id}";
    }
    
    @Test
    public void testCreateRead() {
    	final String employeeId = buildTestEmployee();
    	
    	Compensation compensation = new Compensation();
    	compensation.setEmployeeId(employeeId);
    	compensation.setEffectiveDate("2025-02-21");
    	compensation.setSalary(55000.52);
    	
    	// write record to database
    	Compensation writtenCompensation = restTemplate.postForEntity(compensationUrl, compensation, Compensation.class).getBody();
    	
    	assertNotNull(writtenCompensation);
    	Assert.equals(compensation.getEmployeeId(), writtenCompensation.getEmployeeId());
    	Assert.equals(compensation.getEffectiveDate(), writtenCompensation.getEffectiveDate());
    	Assert.equals(compensation.getSalary(), writtenCompensation.getSalary());
    	
    	// read record from database
    	Compensation readCompensation = restTemplate.getForEntity(compensationIdUrl, Compensation.class, employeeId).getBody();
    	
      	assertNotNull(readCompensation);
    	Assert.equals(compensation.getEmployeeId(), readCompensation.getEmployeeId());
    	Assert.equals(compensation.getEffectiveDate(), readCompensation.getEffectiveDate());
    	Assert.equals(compensation.getSalary(), readCompensation.getSalary());
    }
    
    @Test
    public void create_invalidDate_exception() {
    	final String employeeId = buildTestEmployee();
    	
    	Compensation compensation = new Compensation();
    	compensation.setEmployeeId(employeeId);
    	compensation.setEffectiveDate("February 21, 2025"); // bad value under test
    	compensation.setSalary(55000.52);
    	
    	Compensation emptyCompensation = restTemplate.postForEntity(compensationUrl, compensation, Compensation.class).getBody();
    	
    	Assert.isNull(emptyCompensation.getEmployeeId());
    	Assert.isNull(emptyCompensation.getEffectiveDate());
    	Assert.equals(0.0, emptyCompensation.getSalary());
    }
    
    @Test
    public void create_invalidID_exception() {
    	Compensation compensation = new Compensation();
    	compensation.setEmployeeId("123ABC"); // bad ID
    	compensation.setEffectiveDate("2025-02-21");
    	compensation.setSalary(55000.52);
    	
    	// attempt to write with bad ID
    	Compensation emptyCompensation = restTemplate.postForEntity(compensationUrl, compensation, Compensation.class).getBody();
    	
    	Assert.isNull(emptyCompensation.getEmployeeId());
    	Assert.isNull(emptyCompensation.getEffectiveDate());
    	Assert.equals(0.0, emptyCompensation.getSalary());
    }
    
    @Test
    public void read_invalidID_exception() {
    	// attempt to read record from database, bad ID
    	Compensation readCompensation = restTemplate.getForEntity(compensationIdUrl, Compensation.class, "ABC123").getBody();
    	
    	Assert.isNull(readCompensation.getEmployeeId());
    	Assert.isNull(readCompensation.getEffectiveDate());
    	Assert.equals(0.0, readCompensation.getSalary());
    }
    
    /**
     * Builds and persists a test employee, and returns the ID for
     * @return String
     */
    private String buildTestEmployee() {
    	Employee boimler = new Employee();
    	boimler.setFirstName("Brad");
    	boimler.setLastName("Boimler");
    	boimler.setDepartment("Command Division");
    	boimler.setPosition("Provisional First Officer");
        
    	Employee createdBoimler = restTemplate.postForEntity(employeeUrl, boimler, Employee.class).getBody();
    	assertNotNull(createdBoimler.getEmployeeId());
        
        return createdBoimler.getEmployeeId();
    }
}

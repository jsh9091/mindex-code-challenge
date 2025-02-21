package com.mindex.challenge.service.impl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mindex.challenge.dao.CompensationRepository;
import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.service.CompensationService;

/**
 * Service implementation for reporting structure. 
 * @author jhorvath 
 */
@Service
public class CompensationServiceImpl implements CompensationService {
	
	private static final Logger LOG = LoggerFactory.getLogger(CompensationServiceImpl.class);
	
    @Autowired
    private EmployeeRepository employeeRepository;
    
    @Autowired
    private CompensationRepository compensationRepository;

	@Override
	public Compensation create(Compensation compensation) {
		LOG.debug("Creating employee compensation [{}]", compensation);
		
		// verify that the given employee ID is valid
        Employee employee = employeeRepository.findByEmployeeId(compensation.getEmployeeId());
        if (employee == null) {
            throw new RuntimeException("Invalid employeeId: " + compensation.getEmployeeId());
        }
		
        // validate the date format 
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			formatter = formatter.withLocale(Locale.US);
			LocalDate date = LocalDate.parse(compensation.getEffectiveDate(), formatter);
			final String stringDate = date.format(formatter);
			compensation.setEffectiveDate(stringDate);
			
		} catch (DateTimeParseException ex) {
			throw new RuntimeException("Invalid date format: " + compensation.getEffectiveDate());
		}
		
		compensationRepository.insert(compensation);
		
		return compensation;
	}

	@Override
	public Compensation read(String id) {
		LOG.debug("Reading compensation for employee with id [{}]", id);
		
		Compensation compensation = compensationRepository.findByEmployeeId(id);
		
        if (compensation == null) {
            throw new RuntimeException("Invalid employeeId: " + id);
        }

		return compensation;
	}

}

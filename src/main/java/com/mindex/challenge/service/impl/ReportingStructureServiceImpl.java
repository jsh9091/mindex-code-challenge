package com.mindex.challenge.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.ReportingStructureService;

/**
 * Service implementation for reporting structure.
 * 
 * @author jhorvath
 */
@Service
public class ReportingStructureServiceImpl implements ReportingStructureService {

	private static final Logger LOG = LoggerFactory.getLogger(ReportingStructureServiceImpl.class);

	@Autowired
	private EmployeeRepository employeeRepository;

	@Override
	public ReportingStructure numberOfReports(String id) {
		LOG.debug("Calculate total number reports for: [{}]", id);

		Employee employee = getEmployee(id);

		ReportingStructure reportingStructure = new ReportingStructure(employee);
		reportingStructure.setNumberOfReports(calculateTotalReports(employee, 0));

		return reportingStructure;
	}

	/**
	 * Gets full data for an employee for a given ID.
	 * 
	 * @param id String
	 * @return Employee
	 */
	private Employee getEmployee(String id) {
		Employee employee = employeeRepository.findByEmployeeId(id);

		if (employee == null) {
			throw new RuntimeException("Invalid employeeId: " + id);
		}
		return employee;
	}

	/**
	 * Calculate total number reports for a given employee.
	 * 
	 * @param employee Employee
	 * @param total    int
	 * @return int
	 */
	private int calculateTotalReports(Employee employee, int total) {

		if (employee.getDirectReports() == null || employee.getDirectReports().isEmpty()) {
			// exit condition
			return total;
		}

		total = total + employee.getDirectReports().size();

		// cycle over direct reports of current employee
		for (Employee emp : employee.getDirectReports()) {
			Employee report = getEmployee(emp.getEmployeeId());
			// recurse
			total = calculateTotalReports(report, total);
		}

		return total;
	}

}

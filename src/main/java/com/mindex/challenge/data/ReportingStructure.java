package com.mindex.challenge.data;

/**
 * Data model for reporting structure. 
 * @author jhorvath
 */
public class ReportingStructure {

	private Employee employee;
	private int numberOfReports;
	
	/**
	 * Empty Constructor. 
	 */
	public ReportingStructure() { }
	
	/**
	 * Constructor.
	 * @param employee Employee
	 */
	public ReportingStructure(Employee employee) {
		this.employee = employee;
	}
	
	public Employee getEmployee() {
		return employee;
	}
	public void setEmployee(Employee employee) {
		this.employee = employee;
	}
	public int getNumberOfReports() {
		return numberOfReports;
	}
	public void setNumberOfReports(int numberOfReports) {
		this.numberOfReports = numberOfReports;
	}

}

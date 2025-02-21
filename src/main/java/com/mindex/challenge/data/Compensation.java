package com.mindex.challenge.data;

/**
 * Data model for employee compensation. 
 * @author jhorvath
 */
public class Compensation {
	
	private double salary;
	private String effectiveDate;
	private String employeeId;
	
	/**
	 * Constructor.
	 */
	public Compensation() {}

	public double getSalary() {
		return salary;
	}

	public void setSalary(double salary) {
		this.salary = salary;
	}

	public String getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(String effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

}

package com.oops.payroll.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

/**
 * Represents a part-time employee whose pay depends on worked hours.
 * This class overrides the parent salary method with different logic.
 */
@Entity
@DiscriminatorValue("PART_TIME")
public class PartTimeEmployee extends Employee {

    private double hourlyRate;
    private double hoursWorked;

    public PartTimeEmployee() {
        super();
    }

    public PartTimeEmployee(int employeeId, String name, String password, String designation,
                           Department department, double hourlyRate, double hoursWorked,
                           double allowances, double bonus, double deductions) {
        super(employeeId, name, password, designation, department, 0, allowances, 0, bonus, deductions);
        setHourlyRate(hourlyRate);
        setHoursWorked(hoursWorked);
    }

    @Override
    public double calculateSalary() {
        return (hourlyRate * hoursWorked) + getAllowances() + getBonus() - getDeductions();
    }

    public double getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(double hourlyRate) {
        validatePositiveNumber("Hourly rate", hourlyRate);
        this.hourlyRate = hourlyRate;
    }

    public double getHoursWorked() {
        return hoursWorked;
    }

    public void setHoursWorked(double hoursWorked) {
        validatePositiveNumber("Hours worked", hoursWorked);
        this.hoursWorked = hoursWorked;
    }
}

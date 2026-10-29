package com.oops.payroll.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

/**
 * Represents a full-time employee who receives a standard monthly salary.
 * This class overrides the salary calculation to show polymorphism.
 */
@Entity
@DiscriminatorValue("FULL_TIME")
public class FullTimeEmployee extends Employee {

    public FullTimeEmployee() {
        super();
    }

    public FullTimeEmployee(int employeeId, String name, String password, String designation,
                           Department department, double basicSalary, double allowances,
                           double overtimeHours, double bonus, double deductions) {
        super(employeeId, name, password, designation, department,
                basicSalary, allowances, overtimeHours, bonus, deductions);
    }

    @Override
    public double calculateSalary() {
        double hourlyRate = getBasicSalary() / 160;
        double overtimePay = getOvertimeHours() * hourlyRate * 1.5;
        return getBasicSalary() + getAllowances() + overtimePay + getBonus() - getDeductions();
    }
}

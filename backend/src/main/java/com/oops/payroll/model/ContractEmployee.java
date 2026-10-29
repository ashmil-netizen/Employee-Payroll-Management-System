package com.oops.payroll.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

/**
 * Represents a contract employee paid for a project or contract period.
 * It demonstrates inheritance and method overriding with a different formula.
 */
@Entity
@DiscriminatorValue("CONTRACT")
public class ContractEmployee extends Employee {

    private double contractAmount;

    public ContractEmployee() {
        super();
    }

    public ContractEmployee(int employeeId, String name, String password, String designation,
                           Department department, double contractAmount,
                           double allowances, double bonus, double deductions) {
        super(employeeId, name, password, designation, department,
                0, allowances, 0, bonus, deductions);
        setContractAmount(contractAmount);
    }

    @Override
    public double calculateSalary() {
        return contractAmount + getAllowances() + getBonus() - getDeductions();
    }

    public double getContractAmount() {
        return contractAmount;
    }

    public void setContractAmount(double contractAmount) {
        validatePositiveNumber("Contract amount", contractAmount);
        this.contractAmount = contractAmount;
    }
}

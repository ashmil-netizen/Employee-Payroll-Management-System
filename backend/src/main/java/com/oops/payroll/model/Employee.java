package com.oops.payroll.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * Represents a common employee profile and stores shared employee details.
 * This abstract class demonstrates abstraction and is the base class for all employee types.
 */
@Entity
@Table(name = "employees")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "employee_type")
public abstract class Employee {

    @Id
    private int employeeId;
    private String name;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    private String designation;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "department_id")
    private Department department;
    private double basicSalary;
    private double allowances;
    private double overtimeHours;
    private double bonus;
    private double deductions;

    public Employee() {
    }

    public Employee(int employeeId, String name, String password, String designation,
                    Department department, double basicSalary, double allowances,
                    double overtimeHours, double bonus, double deductions) {
        setEmployeeId(employeeId);
        setName(name);
        setPassword(password);
        setDesignation(designation);
        this.department = department;
        setBasicSalary(basicSalary);
        setAllowances(allowances);
        setOvertimeHours(overtimeHours);
        setBonus(bonus);
        setDeductions(deductions);
    }

    public abstract double calculateSalary();

    public String getEmployeeType() {
        if (this instanceof PartTimeEmployee) {
            return "PART_TIME";
        }
        if (this instanceof ContractEmployee) {
            return "CONTRACT";
        }
        return "FULL_TIME";
    }

    protected void validatePositiveNumber(String fieldName, double value) {
        if (value < 0) {
            throw new IllegalArgumentException(fieldName + " cannot be negative.");
        }
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        if (employeeId <= 0) {
            throw new IllegalArgumentException("Employee ID must be positive.");
        }
        this.employeeId = employeeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Employee name cannot be empty.");
        }
        this.name = name.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty.");
        }
        this.password = password;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        if (designation == null || designation.trim().isEmpty()) {
            throw new IllegalArgumentException("Designation cannot be empty.");
        }
        this.designation = designation.trim();
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public double getBasicSalary() {
        return basicSalary;
    }

    public void setBasicSalary(double basicSalary) {
        validatePositiveNumber("Basic salary", basicSalary);
        this.basicSalary = basicSalary;
    }

    public double getAllowances() {
        return allowances;
    }

    public void setAllowances(double allowances) {
        validatePositiveNumber("Allowances", allowances);
        this.allowances = allowances;
    }

    public double getOvertimeHours() {
        return overtimeHours;
    }

    public void setOvertimeHours(double overtimeHours) {
        validatePositiveNumber("Overtime hours", overtimeHours);
        this.overtimeHours = overtimeHours;
    }

    public double getBonus() {
        return bonus;
    }

    public void setBonus(double bonus) {
        validatePositiveNumber("Bonus", bonus);
        this.bonus = bonus;
    }

    public double getDeductions() {
        return deductions;
    }

    public void setDeductions(double deductions) {
        validatePositiveNumber("Deductions", deductions);
        this.deductions = deductions;
    }
}

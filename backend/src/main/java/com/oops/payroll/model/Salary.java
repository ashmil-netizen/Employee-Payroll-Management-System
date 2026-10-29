package com.oops.payroll.model;

/**
 * Stores the salary components for one pay period.
 * This class demonstrates encapsulation and keeps the salary formula easy to understand.
 */
public class Salary {

    private double basicSalary;
    private double allowances;
    private double overtime;
    private double bonus;
    private double deductions;

    public Salary() {
    }

    public Salary(double basicSalary, double allowances, double overtime, double bonus, double deductions) {
        setBasicSalary(basicSalary);
        setAllowances(allowances);
        setOvertime(overtime);
        setBonus(bonus);
        setDeductions(deductions);
    }

    public double getNetSalary() {
        return basicSalary + allowances + overtime + bonus - deductions;
    }

    public double getBasicSalary() {
        return basicSalary;
    }

    public void setBasicSalary(double basicSalary) {
        if (basicSalary < 0) {
            throw new IllegalArgumentException("Basic salary cannot be negative.");
        }
        this.basicSalary = basicSalary;
    }

    public double getAllowances() {
        return allowances;
    }

    public void setAllowances(double allowances) {
        if (allowances < 0) {
            throw new IllegalArgumentException("Allowances cannot be negative.");
        }
        this.allowances = allowances;
    }

    public double getOvertime() {
        return overtime;
    }

    public void setOvertime(double overtime) {
        if (overtime < 0) {
            throw new IllegalArgumentException("Overtime cannot be negative.");
        }
        this.overtime = overtime;
    }

    public double getBonus() {
        return bonus;
    }

    public void setBonus(double bonus) {
        if (bonus < 0) {
            throw new IllegalArgumentException("Bonus cannot be negative.");
        }
        this.bonus = bonus;
    }

    public double getDeductions() {
        return deductions;
    }

    public void setDeductions(double deductions) {
        if (deductions < 0) {
            throw new IllegalArgumentException("Deductions cannot be negative.");
        }
        this.deductions = deductions;
    }
}

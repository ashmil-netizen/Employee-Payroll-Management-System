package com.oops.payroll.service;

import org.springframework.stereotype.Service;

import com.oops.payroll.model.Employee;
import com.oops.payroll.model.Payroll;
import com.oops.payroll.model.Salary;
import com.oops.payroll.repository.PayrollRepository;

/**
 * Handles payroll calculations and generates payroll records.
 * This service is responsible for the business logic and keeps it out of the UI.
 */
@Service
public class PayrollService {

    private final PayrollRepository payrollRepository;

    public PayrollService(PayrollRepository payrollRepository) {
        this.payrollRepository = payrollRepository;
    }

    public Salary calculateSalary(Employee employee) {
        if (employee == null) {
            throw new IllegalArgumentException("Employee cannot be null.");
        }

        double overtimePay = employee.getOvertimeHours() * (employee.getBasicSalary() / 160.0) * 1.5;
        double salaryBasis = employee.calculateSalary() + employee.getDeductions()
            - employee.getAllowances() - employee.getBonus() - overtimePay;

        return new Salary(
            salaryBasis,
                employee.getAllowances(),
                overtimePay,
                employee.getBonus(),
                employee.getDeductions()
        );
    }

    public Payroll generatePayroll(Employee employee, String month) {
        if (employee == null) {
            throw new IllegalArgumentException("Employee cannot be null.");
        }

        Payroll existingPayroll = payrollRepository
                .findFirstByEmployeeEmployeeIdAndMonth(employee.getEmployeeId(), month)
                .orElse(null);
        if (existingPayroll != null) {
            return existingPayroll;
        }

        Salary salary = calculateSalary(employee);

        Payroll payroll = new Payroll();
        payroll.setEmployee(employee);
        payroll.setMonth(month);
        payroll.setBasicSalary(salary.getBasicSalary());
        payroll.setAllowances(employee.getAllowances());
        payroll.setOvertime(salary.getOvertime());
        payroll.setBonus(employee.getBonus());
        payroll.setDeductions(employee.getDeductions());
        payroll.setNetSalary(salary.getNetSalary());

        return payrollRepository.save(payroll);
    }
}

package com.oops.payroll.service;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.oops.payroll.model.ContractEmployee;
import com.oops.payroll.model.Department;
import com.oops.payroll.model.FullTimeEmployee;
import com.oops.payroll.model.PartTimeEmployee;
import com.oops.payroll.model.Payroll;
import com.oops.payroll.repository.PayrollRepository;

class PayrollServiceTest {

    @Test
    void calculateSalaryGeneratesCorrectPayroll() {
        Department department = new Department(1, "Finance");
        FullTimeEmployee employee = new FullTimeEmployee(
                201,
                "Dana",
                "secret",
                "Accountant",
                department,
                40000,
                6000,
                8,
                3000,
                5000
        );

        PayrollRepository payrollRepository = mock(PayrollRepository.class);
        when(payrollRepository.findFirstByEmployeeEmployeeIdAndMonth(201, "October"))
            .thenReturn(Optional.empty());
        when(payrollRepository.save(any(Payroll.class))).thenAnswer(invocation -> invocation.getArgument(0));
        PayrollService payrollService = new PayrollService(payrollRepository);
        Payroll payroll = payrollService.generatePayroll(employee, "October");

        double expectedOvertime = 8 * (40000.0 / 160.0) * 1.5;
        double expectedNetSalary = 40000 + 6000 + expectedOvertime + 3000 - 5000;

        assertEquals(40000, payroll.getBasicSalary());
        assertEquals(6000, payroll.getAllowances());
        assertEquals(expectedOvertime, payroll.getOvertime(), 0.01);
        assertEquals(3000, payroll.getBonus());
        assertEquals(5000, payroll.getDeductions());
        assertEquals(expectedNetSalary, payroll.getNetSalary(), 0.01);
    }

    @Test
    void payrollUsesPartTimeSalaryCalculation() {
        Department department = new Department(2, "Support");
        PartTimeEmployee employee = new PartTimeEmployee(
                202, "Sam", "secret", "Support Agent", department,
                200, 30, 1000, 500, 250
        );
        PayrollService payrollService = payrollServiceFor(employee.getEmployeeId());

        Payroll payroll = payrollService.generatePayroll(employee, "2026-10");

        assertEquals(employee.calculateSalary(), payroll.getNetSalary(), 0.01);
        assertEquals(6000, payroll.getBasicSalary(), 0.01);
    }

    @Test
    void payrollUsesContractSalaryCalculation() {
        Department department = new Department(3, "Consulting");
        ContractEmployee employee = new ContractEmployee(
                203, "Lee", "secret", "Consultant", department,
                25000, 2000, 1500, 1000
        );
        PayrollService payrollService = payrollServiceFor(employee.getEmployeeId());

        Payroll payroll = payrollService.generatePayroll(employee, "2026-10");

        assertEquals(employee.calculateSalary(), payroll.getNetSalary(), 0.01);
        assertEquals(25000, payroll.getBasicSalary(), 0.01);
    }

    private PayrollService payrollServiceFor(int employeeId) {
        PayrollRepository payrollRepository = mock(PayrollRepository.class);
        when(payrollRepository.findFirstByEmployeeEmployeeIdAndMonth(employeeId, "2026-10"))
                .thenReturn(Optional.empty());
        when(payrollRepository.save(any(Payroll.class))).thenAnswer(invocation -> invocation.getArgument(0));
        return new PayrollService(payrollRepository);
    }
}

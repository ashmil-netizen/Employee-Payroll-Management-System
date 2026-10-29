package com.oops.payroll.controller;

import java.time.YearMonth;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.oops.payroll.model.Employee;
import com.oops.payroll.model.Payroll;
import com.oops.payroll.service.EmployeeService;
import com.oops.payroll.service.PayrollService;

/**
 * Exposes payroll endpoints for salary calculation and salary slip generation.
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class PayrollController {

    private final EmployeeService employeeService;
    private final PayrollService payrollService;

    public PayrollController(EmployeeService employeeService, PayrollService payrollService) {
        this.employeeService = employeeService;
        this.payrollService = payrollService;
    }

    @GetMapping("/payroll/{id}")
    public ResponseEntity<?> getPayroll(@PathVariable int id) {
        Employee employee = employeeService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Employee not found."));

        Payroll payroll = payrollService.generatePayroll(employee, YearMonth.now().toString());
        return ResponseEntity.ok(Map.ofEntries(
            Map.entry("employeeId", employee.getEmployeeId()),
            Map.entry("employeeName", employee.getName()),
            Map.entry("designation", employee.getDesignation()),
            Map.entry("department", employee.getDepartment().getDepartmentName()),
            Map.entry("month", payroll.getMonth()),
            Map.entry("basicSalary", payroll.getBasicSalary()),
            Map.entry("allowances", payroll.getAllowances()),
            Map.entry("overtime", payroll.getOvertime()),
            Map.entry("bonus", payroll.getBonus()),
            Map.entry("deductions", payroll.getDeductions()),
            Map.entry("netSalary", payroll.getNetSalary())
        ));
    }
}

package com.oops.payroll.dto;

public record EmployeeCreateRequest(
        int employeeId,
        String name,
        String password,
        String designation,
        int departmentId,
        String employeeType,
        double basicSalary,
        double allowances,
        double overtimeHours,
        double bonus,
        double deductions,
        double hourlyRate,
        double hoursWorked,
        double contractAmount) {
}
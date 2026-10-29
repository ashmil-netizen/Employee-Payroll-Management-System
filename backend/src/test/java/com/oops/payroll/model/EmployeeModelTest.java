package com.oops.payroll.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

class EmployeeModelTest {

    @Test
    void fullTimeEmployeeCalculatesSalaryWithOvertime() {
        Department department = new Department(1, "Engineering");
        FullTimeEmployee employee = new FullTimeEmployee(
                101,
                "Alice",
                "secret",
                "Developer",
                department,
                30000,
                5000,
                10,
                3000,
                4000
        );

        double expected = 30000 + 5000 + (10 * (30000.0 / 160) * 1.5) + 3000 - 4000;
        assertEquals(expected, employee.calculateSalary(), 0.01);
    }

    @Test
    void partTimeEmployeeUsesHoursWorkedFormula() {
        Department department = new Department(2, "Support");
        PartTimeEmployee employee = new PartTimeEmployee(
                102,
                "Bob",
                "secret",
                "Support Agent",
                department,
                200,
                30,
                1000,
                500,
                250
        );

        assertEquals((200 * 30) + 1000 + 500 - 250, employee.calculateSalary(), 0.01);
    }

    @Test
    void contractEmployeeRejectsNegativeValue() {
        Department department = new Department(3, "Consulting");
        ContractEmployee employee = new ContractEmployee(
                103,
                "Charlie",
                "secret",
                "Consultant",
                department,
                25000,
                2000,
                1500,
                1000
        );

        assertThrows(IllegalArgumentException.class, () -> employee.setDeductions(-5));
    }
}

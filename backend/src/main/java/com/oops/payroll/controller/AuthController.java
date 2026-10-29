package com.oops.payroll.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.oops.payroll.model.Employee;
import com.oops.payroll.service.EmployeeService;

/**
 * Handles the initial login flow for employees and admin.
 * This keeps authentication simple for the prototype stage.
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class AuthController {

    private final EmployeeService employeeService;

    public AuthController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestParam String name,
                                                    @RequestParam String password) {
        if ("admin".equalsIgnoreCase(name) && "admin123".equals(password)) {
            return ResponseEntity.ok(Map.of(
                    "role", "ADMIN",
                    "name", "Admin",
                    "message", "Admin login successful."
            ));
        }

        Employee employee = employeeService.login(name, password);
        if (employee != null) {
            return ResponseEntity.ok(Map.of(
                    "role", "EMPLOYEE",
                    "employeeId", employee.getEmployeeId(),
                    "name", employee.getName(),
                    "message", "Employee login successful."
            ));
        }

        return ResponseEntity.status(401).body(Map.of(
                "message", "Invalid name or password."
        ));
    }
}

package com.oops.payroll.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oops.payroll.dto.EmployeeCreateRequest;
import com.oops.payroll.model.ContractEmployee;
import com.oops.payroll.model.Department;
import com.oops.payroll.model.Employee;
import com.oops.payroll.model.FullTimeEmployee;
import com.oops.payroll.model.PartTimeEmployee;
import com.oops.payroll.repository.AttendanceRepository;
import com.oops.payroll.repository.DepartmentRepository;
import com.oops.payroll.repository.EmployeeRepository;
import com.oops.payroll.repository.LeaveRequestRepository;
import com.oops.payroll.repository.PayrollRepository;

/**
 * Handles basic employee data and login checks.
 * This service keeps employee operations separate from controller logic.
 */
@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final AttendanceRepository attendanceRepository;
    private final LeaveRequestRepository leaveRequestRepository;
    private final PayrollRepository payrollRepository;

    public EmployeeService(EmployeeRepository employeeRepository, DepartmentRepository departmentRepository,
                           AttendanceRepository attendanceRepository,
                           LeaveRequestRepository leaveRequestRepository,
                           PayrollRepository payrollRepository) {
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
        this.attendanceRepository = attendanceRepository;
        this.leaveRequestRepository = leaveRequestRepository;
        this.payrollRepository = payrollRepository;
        seedDemoEmployees();
    }

    private void seedDemoEmployees() {
        if (employeeRepository.count() > 0) {
            return;
        }

        Department engineering = departmentRepository.save(new Department(1, "Engineering"));
        Department hr = departmentRepository.save(new Department(2, "Human Resources"));

        employeeRepository.save(new FullTimeEmployee(
                1001,
                "Ashmil",
                "ashmil123",
                "Java Developer",
                engineering,
                30000,
                5000,
                8,
                3000,
                4000
        ));

        employeeRepository.save(new FullTimeEmployee(
                1002,
                "Rinosh",
                "rinosh123",
                "Senior Engineer",
                hr,
                35000,
                6000,
                5,
                2500,
                3500
        ));
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    public Employee createEmployee(EmployeeCreateRequest request) {
        if (request.employeeId() <= 0 || employeeRepository.existsById(request.employeeId())) {
            throw new IllegalArgumentException("Employee ID must be positive and unique.");
        }

        Department department = departmentRepository.findById(request.departmentId())
                .orElseThrow(() -> new IllegalArgumentException("Department not found."));
        String type = request.employeeType() == null ? "FULL_TIME" : request.employeeType().trim().toUpperCase();

        Employee employee = switch (type) {
            case "FULL_TIME" -> new FullTimeEmployee(request.employeeId(), request.name(), request.password(),
                    request.designation(), department, request.basicSalary(), request.allowances(),
                    request.overtimeHours(), request.bonus(), request.deductions());
            case "PART_TIME" -> new PartTimeEmployee(request.employeeId(), request.name(), request.password(),
                    request.designation(), department, request.hourlyRate(), request.hoursWorked(),
                    request.allowances(), request.bonus(), request.deductions());
            case "CONTRACT" -> new ContractEmployee(request.employeeId(), request.name(), request.password(),
                    request.designation(), department, request.contractAmount(), request.allowances(),
                    request.bonus(), request.deductions());
            default -> throw new IllegalArgumentException("Employee type must be FULL_TIME, PART_TIME, or CONTRACT.");
        };

        return employeeRepository.save(employee);
    }

    @Transactional
    public boolean deleteEmployee(int id) {
        if (!employeeRepository.existsById(id)) {
            return false;
        }

        attendanceRepository.deleteAllByEmployeeId(id);
        leaveRequestRepository.deleteAllByEmployeeId(id);
        payrollRepository.deleteAllByEmployeeEmployeeId(id);
        employeeRepository.deleteById(id);
        return true;
    }

    public Optional<Employee> findById(int id) {
        return employeeRepository.findById(id);
    }

    public Employee login(String name, String password) {
        if (name == null || password == null) {
            return null;
        }

        return employeeRepository.findByNameIgnoreCase(name.trim())
            .filter(employee -> employee.getPassword().equals(password))
                .orElse(null);
    }
}

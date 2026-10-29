package com.oops.payroll.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.oops.payroll.model.Payroll;

public interface PayrollRepository extends JpaRepository<Payroll, Integer> {
    Optional<Payroll> findFirstByEmployeeEmployeeIdAndMonth(int employeeId, String month);
    void deleteAllByEmployeeEmployeeId(int employeeId);
}
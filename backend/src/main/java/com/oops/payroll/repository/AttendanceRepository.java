package com.oops.payroll.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.oops.payroll.model.Attendance;

public interface AttendanceRepository extends JpaRepository<Attendance, Integer> {
    List<Attendance> findAllByEmployeeId(int employeeId);
    void deleteAllByEmployeeId(int employeeId);
}